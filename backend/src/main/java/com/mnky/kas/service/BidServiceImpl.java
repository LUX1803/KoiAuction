package com.mnky.kas.service;

import com.mnky.kas.dto.request.BidRequest;
import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.exception.AppException;
import com.mnky.kas.exception.ErrorCode;
import com.mnky.kas.mapper.BidMapper;
import com.mnky.kas.mapper.LotMapper;
import com.mnky.kas.model.Bid;
import com.mnky.kas.model.Lot;
import com.mnky.kas.model.Member;
import com.mnky.kas.model.Wallet;
import com.mnky.kas.repository.BidRepository;
import com.mnky.kas.repository.LotRepository;
import com.mnky.kas.repository.MemberRepository;
import com.mnky.kas.repository.WalletRepository;
import com.mnky.kas.util.JWTUtil;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BidServiceImpl implements BidService {
    @Autowired
    LotRepository lotRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    BidMapper bidMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private LotMapper lotMapper;
    @Autowired
    private SchedulerImpl schedulerService;


    @Override
    @Transactional
    public void placeSealedBid(short lotId, BidRequest bidRequest, String token) throws ParseException {
        double amount = bidRequest.getAmount();
        Lot lot = lotRepository.findById(lotId);
        String jwtToken = token.substring(7);
        String username = JWTUtil.getUserNameFromToken(jwtToken);
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (lot == null) {
            throw new AppException(ErrorCode.LOT_NOT_FOUND);
        }

        //Minus your balance in your wallet
        walletService.placeBidUsingWallet(token, amount, lotId);

        bidRepository.saveBid(amount, false, member.getId(), lot.getId(), Timestamp.valueOf(LocalDateTime.now()));
        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId() + "/bid", bidMapper.toBidViewResponse(bidRepository.findFirstByLotIdOrderByTimeDesc(lotId)));

    }

    @Override
    public List<BidViewResponse> getBids(short lotId) {
        List<BidViewResponse> responseList = new ArrayList<>();
        List<Bid> bidlist = bidRepository.findByLotId(lotId);
        if (bidlist == null) {
            throw new AppException(ErrorCode.NO_PARTICIPANTS);
        }

        for (Bid bid : bidlist) {
            responseList.add(bidMapper.toBidViewResponse(bid));
        }
        return responseList;
    }

    @Override
    @Transactional
    public void placeFixedPriceBid(short lotId) throws ParseException {
        Lot lot = lotRepository.findById(lotId);
        if (lot == null) {
            throw new AppException(ErrorCode.LOT_NOT_FOUND);
        }
        String bearerToken = request.getHeader("Authorization").substring(7);
        SignedJWT jwt = SignedJWT.parse(bearerToken);
        short bidderId = jwt.getJWTClaimsSet().getLongClaim("id").shortValue();

        if (bidRepository.findByLotIdAndBidderId(lotId, bidderId) != null) {
            throw new AppException(ErrorCode.ALREADY_BIDDED);
        }


        double amount = lot.getStartingPrice();
        Timestamp time = Timestamp.from(Instant.now());

        //Minus your balance in your wallet
        walletService.placeBidUsingWallet(request.getHeader("Authorization"), amount, lotId);

        bidRepository.saveBid(amount, false, bidderId, lotId, time);
        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId() + "/bid", bidMapper.toBidViewResponse(bidRepository.findFirstByLotIdOrderByTimeDesc(lotId)));
    }


    @Override
    @Transactional
    public void placeDutchBid(short lotId, String token) throws ParseException {
        Lot lot = lotRepository.findById(lotId);
        double amount = lot.getBuyNowPrice();
        String jwtToken = token.substring(7);
        String username = JWTUtil.getUserNameFromToken(jwtToken);
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (lot == null) {
            throw new AppException(ErrorCode.LOT_NOT_FOUND);
        }

//        if (lot.getBuyNowPrice() > walletRepository.findByOwnerId(member.getId()).getBalance()){
//            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
//        } else {
//            Wallet wallet = walletRepository.findByOwnerId(member.getId());
//            wallet.setBalance(wallet.getBalance() - amount);
//            walletRepository.updateByOwnerId(member.getId(), wallet.getBalance());

        Timestamp time = Timestamp.valueOf(LocalDateTime.now());

        //Minus your balance in your wallet
        walletService.placeBidUsingWallet(token, amount, lotId);

        bidRepository.saveBid(amount, true, member.getId(), lot.getId(), time);

        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId() + "/bid", bidMapper.toBidViewResponse(bidRepository.findFirstByLotIdOrderByTimeDesc(lotId)));

        lot.setEnded(time);
        schedulerService.scheduleLotClosed(lot);

        Hibernate.initialize(lot.getKoi().getVariety());
        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));


        //}
    }

    @Override
    @Transactional
    public void placeEnglishBid(short lotId, BidRequest bidRequest, SimpMessageHeaderAccessor headerAccessor) throws ParseException {

        String token = headerAccessor.getFirstNativeHeader("Authorization");

        if (token == null || token.isEmpty()) {
            System.out.println("Token is null");
            return;
        }

        String jwtToken = token.substring(7);
        short bidderId = JWTUtil.getMemberIdFromToken(jwtToken);

        Lot lot = lotRepository.findById(lotId);
        if (lot == null) {
            System.out.println("Lot not found");
            return;
        }

        if (lot.getStatus() != Lot.LotStatus.LIVE) {
            return;
        }

        double bidAmount = bidRequest.getAmount();
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());


        long timeRemaining = lot.getEnded().getTime() - time.getTime();
        if (timeRemaining <= 5 * 60 * 1000) {
            // Extend the auction by 5 minutes
            System.out.println("Bid placed in the last 5 minutes. Extending the auction by 5 minutes.");
            lot.setEnded(Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
            lotRepository.save(lot);  // Update the lot end time

            // Reschedule the end time with the new extended time
            schedulerService.rescheduleLotClosed(lot);
            messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));
        }

//        if (bidAmount == lot.getBuyNowPrice()) {
//            bidRepository.saveBid(bidAmount, false, true, bidderId, lotId, time);
//            lot.setStatus(Lot.LotStatus.AWARDED);
//            lot.setEnded(time);
//            lotRepository.save(lot);
//            messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));
//        } else {


        //Minus your balance in your wallet
         walletService.placeBidUsingWallet(token, bidAmount, lotId);

         bidRepository.saveBid(bidAmount, false, bidderId, lotId, time);

        Bid bid = bidRepository.findFirstByLotIdOrderByAmountDesc(lotId);
        Hibernate.initialize(bid.getBidder());
        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId() + "/bid", bidMapper.toBidViewResponse(bid));

    }


    @Override
    @Transactional
    public void buyLotNow(short lotId, String token) throws ParseException {
        if (token == null || token.isEmpty()) {
            System.out.println("Token is null");
            return;
        }

        String jwtToken = token.substring(7);
        short bidderId = JWTUtil.getMemberIdFromToken(jwtToken);

        Lot lot = lotRepository.findById(lotId);
        if (lot == null) {
            System.out.println("Lot not found");
            return;
        }

        if (lot.getStatus() != Lot.LotStatus.LIVE) {
            return;
        }


        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        lot.setEnded(time);
//        lot.setStatus(Lot.LotStatus.AWARDED);
//        lotRepository.save(lot);


        double amount = lot.getBuyNowPrice();

        //Buy Lot = Tien buy now - tien da dat cuoc => hoan tien
        //place bid with highest price
        walletService.placeBidUsingWallet(token, amount, lotId);

        bidRepository.saveBid(amount, true, bidderId, lotId, time);
        Bid bid = bidRepository.findFirstByLotIdOrderByAmountDesc(lotId);

        Hibernate.initialize(bid.getBidder());
        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId() + "/bid", bidMapper.toBidViewResponse(bid));

        Hibernate.initialize(lot.getKoi().getVariety());
        messagingTemplate.convertAndSend("/topic/lot/" + lot.getId(), lotMapper.toLotDetailResponse(lot));

        schedulerService.scheduleLotClosed(lot);
    }


    @Override
    public BidViewResponse getHighestBid(short lotId) {
        Bid highestBid = bidRepository.findFirstByLotIdOrderByAmountDesc(lotId);

        if (highestBid == null) {
            return null;
        }

        return bidMapper.toBidViewResponse(highestBid);
    }

    @Override
    public BidViewResponse getBidByLotIdAndBidderId(short lotId, short bidderId) {
        Bid bid = bidRepository.findByLotIdAndBidderId(lotId, bidderId);
        if (bid == null) {
            return null;
        }
        return bidMapper.toBidViewResponse(bid);
    }


}
