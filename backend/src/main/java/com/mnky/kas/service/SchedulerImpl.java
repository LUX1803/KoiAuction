package com.mnky.kas.service;

import com.mnky.kas.exception.AppException;
import com.mnky.kas.exception.ErrorCode;
import com.mnky.kas.mapper.LotMapper;
import com.mnky.kas.model.*;
import com.mnky.kas.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class SchedulerImpl {
    private final TaskScheduler taskScheduler;
    private final AuctionRepository auctionRepository;
    private final LotRepository lotRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final LotMapper lotMapper;
    private final BidRepository bidRepository;
    private final WalletRepository walletRepository;
    private final InvoiceRepository invoiceRepository;
    private final TransactionRepository transactionRepository;
    private final KoiRepository koiRepository;
    private final MemberRepository memberRepository;
    private final WalletService walletService;

    public void scheduleAuctionStart(Auction auction) {
        taskScheduler.schedule(() -> {
            System.out.println("Executing auction start for lot ID: " + auction.getId() + " at " + new Timestamp(System.currentTimeMillis()));
            //if (auction.getAuctionStatus().equals(Auction.AuctionStatus.UPCOMING)) {
                auction.setAuctionStatus(Auction.AuctionStatus.ONGOING);
                auctionRepository.save(auction);
                System.out.println("Auction ID: " + auction.getId() + " status updated to ONGOING");
                scheduleAuctionEnd(auction);
            //}
        }, auction.getStarted());
    }

    //DOING
    public void scheduleAuctionEnd(Auction auction) {
        taskScheduler.schedule(() -> {
            System.out.println("Executing auction end for auction ID: " + auction.getId() + " at " + new Timestamp(System.currentTimeMillis()));

            //if (auction.getAuctionStatus().equals(Auction.AuctionStatus.ONGOING)) {

                System.out.println("Auction ID: " + auction.getId() + " status updated to ClOSED");
                List<Lot> lots = lotRepository.findByAuctionId(auction.getId());
                for (Lot lot : lots) {
                    lot.setEnded(new Timestamp(System.currentTimeMillis()));
                    scheduleLotClosed(lot);
                    lotRepository.save(lot);
                }
                auction.setAuctionStatus(Auction.AuctionStatus.CLOSED);
                System.out.println("auctionClose: "+ auction.toString());
                auctionRepository.save(auction);
            //}
        }, auction.getEnded());
    }

    public void scheduleLotStart(Lot lot) {
        // Log the scheduling time for the start lot

        System.out.println("Scheduling lot start for lot ID: " + lot.getId() + " at " + lot.getStarted());

        // Schedule the task to update status to ONGOING at lot's start time
        taskScheduler.schedule(() -> {
            System.out.println("Executing lot start for lot ID: " + lot.getId() + " at " + new Timestamp(System.currentTimeMillis()));
            updateLotStatusToLive(lot.getId());
        }, lot.getStarted());
    }


    private void updateLotStatusToLive(short lotId) {
        Lot lot = lotRepository.findById(lotId);
        if (lot != null) {
            if (lot.getStatus() == Lot.LotStatus.UPCOMING) {
                lot.setStatus(Lot.LotStatus.LIVE);
                lotRepository.save(lot);
                // Log the status change
                System.out.println("Lot ID: " + lotId + " status updated to LIVE");

                // Notify the clients about the lot's status change
                Hibernate.initialize(lot.getKoi().getVariety());
                messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));

                // Now schedule the lot's end
                scheduleLotClosed(lot);

                if (lot.getMethod().getId() == 4) {
                    handleDutchAuction(lot);
                }
            }
        }
    }
    //DOING
    public void scheduleLotClosed(Lot lot) {
        // Log the scheduling time for the end lot

        System.out.println("Scheduling lot end for lot ID: " + lot.getId() + " at " + lot.getEnded());

        // Schedule the task to update status to CLOSED at lot's end time
        taskScheduler.schedule(() -> updateLotStatusToClosed(lot.getId()), lot.getEnded());
    }

    //closed => tao invoice => create transaction
    public void rescheduleLotClosed(Lot lot) {
        System.out.println("Rescheduling lot end for lot ID: " + lot.getId() + " at " + lot.getEnded());

        // Cancel the previous scheduler if necessary and reschedule with new end time
        taskScheduler.schedule(() -> updateLotStatusToClosed(lot.getId()), lot.getEnded());
    }

    private void updateLotStatusToClosed(short lotId) {
        Lot lot = lotRepository.findById(lotId);
        Koi koi = lot.getKoi();
        if (lot != null) {
            if (lot.getStatus() == Lot.LotStatus.LIVE) {
                boolean winner = false;
                switch (lot.getMethod().getId()) {
                    case 1:
                        winner = fixedPriceAuctionLotWinner(lotId);
                        break;
                    case 2:
                        winner = sealedBidAuctionLotWinner(lotId);
                        break;
                    case 3:
                        winner = englishAuctionLotWinner(lotId);
                        break;
                    case 4:
                        winner = dutchAuctionLotWinner(lotId);
                        break;
                }



                if (winner) {
                    lot.setStatus(Lot.LotStatus.AWARDED);
                    System.out.println("Lot ID: " + lotId + " status updated to AWARDED");

                    //MODIFY REFUND MONEY HERE
                    walletService.refundAllBalanceTransactionByLotId(lotId);


                    koi.setStatus(Koi.KoiStatus.HELD);
                    generateInvoice(lotId);
                } else {
                    lot.setStatus(Lot.LotStatus.MISSED);
                    koi.setStatus(Koi.KoiStatus.QUEUED);
                    System.out.println("Lot ID: " + lotId + " status updated to MISSED");
                }

                koiRepository.save(koi);
                lotRepository.save(lot);
                Hibernate.initialize(lot.getKoi().getVariety());
                Hibernate.initialize(lot.getMethod());
                messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));
            }
        }
    }

    private void generateInvoice(short lotId) {
        Lot lot = lotRepository.findById(lotId);
        if (lot == null) {
            throw new AppException(ErrorCode.LOT_NOT_FOUND);
        }

        Invoice invoice = new Invoice();
        invoice.setId((short) (invoiceRepository.findAll().size() + 1));

        invoice.setLot(lot);
        invoice.setRecipient(bidRepository.findBidIsHighestByLotId(lotId).getBidder());
        invoice.setTax(0.1);
        invoice.setBuyerPremium(lot.getBuyerPremium());
        invoice.setShippingCost(100000);
        invoice.setHammerPrice(bidRepository.findBidIsHighestByLotId(lotId).getAmount());
        invoice.setBuyerTotal(((invoice.getHammerPrice() * (1 + lot.getBuyerPremium())) + invoice.getShippingCost()) * (1 + invoice.getTax()));
        invoice.setDescription("Invoice for lot ID: " + lotId);
        invoice.setStatus(Invoice.InvoiceStatus.PENDING);
        Invoice inv  = invoiceRepository.save(invoice);
        System.out.println("Invoice ID: " + inv.getId());

        generateTransaction(inv.getId());
    }

    private void generateTransaction(short invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId);

        Transaction transaction = new Transaction();
        transaction.setAmount(invoice.getBuyerTotal());
        transaction.setDescription("Lot ID: " + invoice.getLot().getId());
        transaction.setClosed(new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000));
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

        Member member = invoice.getRecipient();
        System.out.println("Member ID: " + member.getId());
        transaction.setMember(member);

        transaction.setInvoice(invoice);
        Transaction trans = transactionRepository.save(transaction);
        checkTransaction(trans);
    }

    public void handleDutchAuction(Lot lott) {
        // Convert `Time` to milliseconds for scheduling the task
        long intervalMillis = lott.getTimeInterval().toMillis(); // Time is in milliseconds

        taskScheduler.schedule(() -> {
            taskScheduler.scheduleAtFixedRate(() -> {
                Lot lot = lotRepository.findById(lott.getId());
                if (lot.getStatus() == Lot.LotStatus.LIVE) {
                    double currentPrice = lot.getBuyNowPrice();
                    double newPrice = currentPrice - lot.getPriceInterval();

                    if (newPrice >= lot.getReservePrice()) {
                        lot.setBuyNowPrice(newPrice);
                        lotRepository.save(lot);

                        Hibernate.initialize(lot.getKoi().getVariety());
                        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));

                        System.out.println("Auction Lot ID: " + lot.getId() + " new price: $" + newPrice);
                    }
                }

            }, intervalMillis);

        }, new Date(System.currentTimeMillis() + intervalMillis));
    }

    private boolean sealedBidAuctionLotWinner(short lotId) {
        List<Bid> list = bidRepository.findBidsWithMaxAmount(lotId);

        if (list == null || list.isEmpty()) {
            return false;
        }

        Bid winner = list.get(0);
        for (Bid bid : list) {
            if (winner.getTime().after(bid.getTime())) {
                winner = bid;
            }
        }

        winner.setHighest(true);
        bidRepository.save(winner);


        return true;
    }

    //Lay Bid winner ra dua theo lotID -> set Bid winner, highest = true
    private boolean englishAuctionLotWinner(short lotId) {
        Bid winner = bidRepository.findFirstByLotIdOrderByAmountDesc(lotId);
        if (winner != null) {
            bidRepository.updateById(winner.getId());
            return true;
        }
        return false;
    }

    private boolean dutchAuctionLotWinner(short lotId) {
        Bid winner = bidRepository.findFirstByLotIdOrderByAmountDesc(lotId);
        if (winner != null) {
            bidRepository.updateById(winner.getId());
            return true;
        }
        return false;
    }

    private boolean fixedPriceAuctionLotWinner(short lotId) {
        List<Bid> bids = bidRepository.findByLotId(lotId);

        if (bids == null || bids.isEmpty()) {
            return false; // No bids, so no winner
        }


        Random random = new Random();
        Bid winner = bids.get(random.nextInt(bids.size()));
        winner.setHighest(true);
        bidRepository.save(winner);
        return true;
    }

    private void checkTransaction(Transaction transaction) {
        System.out.println("Scheduling transaction check for transaction ID: " + transaction.getId() + " at " + transaction.getClosed());
        taskScheduler.schedule(() -> {
            System.out.println("Check for transaction ID: " + transaction.getId() + " at " + new Timestamp(System.currentTimeMillis()));

            Transaction trans = transactionRepository.findById(transaction.getId());

            if (trans.getStatus() == Transaction.TransactionStatus.PENDING) {

                trans.setStatus(Transaction.TransactionStatus.FAILED);
                Invoice invoice = invoiceRepository.findById(trans.getInvoice().getId());
                invoice.setStatus(Invoice.InvoiceStatus.UNPAID);
                System.out.println("unpaid");

                Lot lot = lotRepository.findByInvoice_Id(invoice.getId());
                Koi koi = lot.getKoi();
                koi.setStatus(Koi.KoiStatus.QUEUED);
                koiRepository.save(koi);
                invoiceRepository.save(invoice);
                transactionRepository.save(trans);
            }


        }, transaction.getClosed());
    }
}

