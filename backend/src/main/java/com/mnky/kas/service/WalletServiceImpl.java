package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.mapper.TransactionMapper;
import com.mnky.kas.model.*;
import com.mnky.kas.repository.*;
import com.mnky.kas.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final InvoiceRepository invoiceRepository;
    private final LotRepository lotRepository;
    private final KoiRepository koiRepository;
    private final PaymentRepository paymentRepository;
    private final BidRepository bidRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, MemberRepository memberRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper, InvoiceRepository invoiceRepository, LotRepository lotRepository, KoiRepository koiRepository, PaymentRepository paymentRepository, BidRepository bidRepository) {
        this.walletRepository = walletRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.invoiceRepository = invoiceRepository;
        this.lotRepository = lotRepository;
        this.koiRepository = koiRepository;
        this.paymentRepository = paymentRepository;
        this.bidRepository = bidRepository;
    }

    @Override
    @Transactional
    public void createWallet(WalletRegisterRequest walletRegisterRequest) {
        walletRepository.saveWallet(walletRegisterRequest.getBalance(), walletRegisterRequest.getOwnerId());

    }

    public WalletResponse getMemberAndWallet(String bearerToken) throws ParseException {
        // Parse token để lấy thông tin người dùng
        //SignedJWT jwt = SignedJWT.parse(bearerToken);

        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));

        // Tìm ví của họ
        Member member = memberRepository.findByUsername(username);
        Wallet wallet = walletRepository.findByOwner(member);

        WalletResponse res = new WalletResponse();
        res.setBalance(wallet.getBalance());

        return res;
    }

    @Override
    @Transactional
    public TransactionResponse addBalanceTransaction(String bearerToken, Double balance) throws ParseException {
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);

        //Create Transactions
        Transaction transaction = new Transaction();
        transaction.setAmount(balance);
        transaction.setDescription(new Date().getTime() + "_" + "ADD_" + balance);
        transaction.setPaymentType(Transaction.PaymentType.WALLET);
        transaction.setClosed(null);
        transaction.setCreated(new Timestamp(System.currentTimeMillis()));
        transaction.setMember(member);

//        doesnt need invoice and payment
        transaction.setInvoice(null);
        transaction.setPayment(null);
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

//        System.out.println("trans: "+ transaction.toString());
        return transactionMapper.toTransactionResponse(transactionRepository.save(transaction));


    }

    @Override
    @Transactional
    public List<TransactionResponse> refundAllBalanceTransactionByLotId(short lotId) {

        List<TransactionResponse> ls = new ArrayList<>();

        List<Bid> listBidder = bidRepository.findAllHighestBidsByLotId(lotId);
        //print the list
        for (Bid b : listBidder) {
            List<Short> listTrans = transactionRepository.findTransactionByDescriptionCodeAndBidder("RET_LOT_" + lotId, b.getBidder().getId());
            if(!listTrans.isEmpty()) continue;

            Transaction transaction = new Transaction();
            transaction.setPaymentType(Transaction.PaymentType.WALLET);
            transaction.setClosed(null);
            transaction.setCreated(new Timestamp(System.currentTimeMillis()));
            transaction.setUpdated(new Timestamp(System.currentTimeMillis()));
            transaction.setMember(b.getBidder());
            //        doesnt need invoice and payment
            transaction.setInvoice(null);
            transaction.setPayment(null);
            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            transaction.setAmount(b.getAmount());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss_dd-MM-yyyy");
            // Định dạng thời gian hiện tại
            String formattedDate = sdf.format(new Date());
            transaction.setDescription(formattedDate + "_" + "RET_LOT_" + lotId + "_" + b.getAmount());
            transactionRepository.save(transaction);
            ls.add(transactionMapper.toTransactionResponse(transaction));
            addBalance(b.getBidder(), b.getAmount());
        }
        return ls;
    }

    @Override
    @Transactional
    public void addBalance(Member owner, Double balance) {
        Wallet wallet = walletRepository.findByOwner(owner);
        wallet.setBalance(wallet.getBalance() + balance);
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void deductBalance(Member owner, Double balance) {
        Wallet wallet = walletRepository.findByOwner(owner);
        wallet.setBalance(wallet.getBalance() - balance);
        walletRepository.save(wallet);
    }

    @Override
    public WalletResponse paymentWithWallet(String bearerToken, List<Integer> transIdList) throws ParseException {
        //getUser
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);
        Wallet wallet = walletRepository.findByOwner(member);

        double amount = 0;

        for (Integer transId : transIdList) {
            //getTransaction
            Transaction transaction = transactionRepository.findById((short) Integer.parseInt(String.valueOf(transId)));

            //calc all amount
            amount += transaction.getAmount();
            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

            //update Transactions
            transaction.setDescription(transaction.getDescription() + "_" + "REM_" + transaction.getAmount());
            transaction.setPaymentType(Transaction.PaymentType.WALLET);

            //Invoice and others
            Invoice invoice = invoiceRepository.findById(transaction.getInvoice().getId());
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
            Lot lot = lotRepository.findByInvoice_Id(invoice.getId());
            Koi koi = lot.getKoi();
            koi.setStatus(Koi.KoiStatus.SHIPPING);

            koiRepository.save(koi);
            invoiceRepository.save(invoice);
            transactionRepository.save(transaction);
        }


        //Create Set<Trans> => add to Payment
        Set<Transaction> transactions = new HashSet<>();
        for (Integer transId : transIdList) {
            transactions.add(transactionRepository.findById(Short.parseShort(transId + "")));
        }
        //create payment
        Payment payment = new Payment();

        payment.setVnpAmount(amount);
        payment.setTransaction(transactions);
        Payment savedPayment = paymentRepository.save(payment);

        //setType to WALLET
        for (Integer transId : transIdList) {
            Transaction trans = transactionRepository.findById(Short.parseShort(transId + ""));
            trans.setPayment(savedPayment);
            trans.setPaymentType(Transaction.PaymentType.WALLET);
            //update transaction
            transactionRepository.save(trans);
        }

        if (wallet.getBalance() <= amount) {
            return null;
        }
        // deduct the wallet
        deductBalance(member, amount);

        return WalletResponse.builder()
                .balance(amount)
                .build();
        //CATCH WHEN WALLET KHONG DU
    }


    //To create transaction for placing bid
    @Override
    @Transactional
    public WalletResponse placeBidUsingWallet(String bearerToken, Double amount, Short lotId) throws ParseException {
        //getUser
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);
        Wallet wallet = walletRepository.findByOwner(member);

        //Create empty Transaction:
        Transaction transaction = new Transaction();
        transaction.setPaymentType(Transaction.PaymentType.WALLET);
        transaction.setClosed(null);
        transaction.setCreated(new Timestamp(System.currentTimeMillis()));
        transaction.setUpdated(new Timestamp(System.currentTimeMillis()));
        transaction.setMember(member);

//        doesnt need invoice and payment
        transaction.setInvoice(null);
        transaction.setPayment(null);
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        if (wallet.getBalance() < amount) {
            System.out.println("Your wallet doesn't have enough money");
            return WalletResponse.builder()
                    .balance(amount)
                    .build();
        } else {

            double newDeductAmount = amount - getPlacedBidByLotId(bearerToken, lotId).getBalance();

            transaction.setAmount(newDeductAmount);
            transaction.setDescription(new Date().getTime() + "_" + "BID_LOT_" + lotId + "_" + newDeductAmount);

            // deduct the wallet
            deductBalance(member, newDeductAmount);
            transactionRepository.save(transaction);

            return WalletResponse.builder()
                    .balance(amount)
                    .build();
        }

    }

    @Override
    public WalletResponse getPlacedBidByLotId(String bearerToken, Short lotId) throws ParseException {
        // Get the placed bid then find the deduct amount
        String username = JWTUtil.getUserNameFromToken(bearerToken.substring(7));
        Member member = memberRepository.findByUsername(username);

        Lot lot = lotRepository.findById(lotId).orElse(null);
        Set<Bid> bidList = lot.getBids();

        double placedBid = 0;

        if (bidList != null) {
            for (Bid bid : bidList) {
                if (bid.getBidder().getId() == member.getId()) {
                    placedBid += bid.getAmount();
                }
            }
        } else {
            System.out.println("Set of Bid Not Found");
            return WalletResponse.builder()
                    .balance(0)
                    .build();
        }


        return WalletResponse.builder()
                .balance(placedBid)
                .build();
    }

}
