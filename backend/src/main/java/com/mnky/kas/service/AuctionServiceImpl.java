package com.mnky.kas.service;

import com.mnky.kas.dto.request.AuctionCreationRequest;
import com.mnky.kas.dto.request.AuctionUpdateRequest;
import com.mnky.kas.dto.response.AuctionResponse;
import com.mnky.kas.exception.AppException;
import com.mnky.kas.exception.ErrorCode;
import com.mnky.kas.mapper.AuctionMapper;
import com.mnky.kas.model.Auction;
import com.mnky.kas.model.Lot;
import com.mnky.kas.repository.AuctionRepository;
import com.mnky.kas.repository.EmployeeRepository;
import com.mnky.kas.repository.LotRepository;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private SchedulerImpl schedulerService;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    EmployeeRepository employeeRepository;


    @Override
    public List<AuctionResponse> getAuctionsWithStatus(String status) {
        List<AuctionResponse> auctions = new ArrayList<>();
        AuctionResponse response = new AuctionResponse();
        for (Auction auction : auctionRepository.getAuctionByAuctionStatus(Auction.AuctionStatus.valueOf(status.toUpperCase()))) {
            auctions.add(auctionMapper.toAuctionResponse(auction));
        }
        if (auctions.isEmpty()) {
            throw new AppException(ErrorCode.AUCTION_NOT_FOUND);
        }

        return auctions;
    }




    @Override
    public AuctionResponse getAuctionById(Short auctionId) {
        Auction auction = auctionRepository.findAuctionById(auctionId);
        AuctionResponse response = new AuctionResponse();
        if (auction == null) {
            throw new AppException(ErrorCode.AUCTION_NOT_FOUND);
        } else {
            response = auctionMapper.toAuctionResponse(auction);
        }
        return response;
    }

    @Override
    public void updateAuctionStatus(short auctionId, String status) {
        Auction auction = auctionRepository.findAuctionById(auctionId);
        if (auction == null) {
            throw new AppException(ErrorCode.AUCTION_NOT_FOUND);
        } else {
            auction.setAuctionStatus(Auction.AuctionStatus.valueOf(status.toUpperCase()));
            if (status.equalsIgnoreCase("UPCOMING")) {
                schedulerService.scheduleAuctionStart(auction);
                schedulerService.scheduleAuctionEnd(auction);
                List<Lot> lots = lotRepository.findByAuctionId(auctionId);
                if (lots != null && !lots.isEmpty()) {
                    for (Lot lot : lots) {
                        lot.setStatus(Lot.LotStatus.UPCOMING);
                        lotRepository.save(lot);
                        schedulerService.scheduleLotStart(lot);
                    }
                }
            }

            if (status.equalsIgnoreCase("CLOSED")) {
                //List<Lot> lots = lotRepository.findByAuctionId(auctionId);

                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                auction.setEnded(currentTimestamp);
                schedulerService.scheduleAuctionEnd(auction);
//                if (lots != null && !lots.isEmpty()) {
//                    for (Lot lot : lots) {
//                        if (lot.getEnded().after(currentTimestamp)) {
//                            lot.setEnded(currentTimestamp);
//                        }
//                        lot.setStatus(Lot.LotStatus.CANCELLED);
//                        lotRepository.save(lot);
//                    }
//                }
//                auction.setEnded(currentTimestamp);
//                auction.setAuctionStatus(Auction.AuctionStatus.CLOSED);
            }

            if (status.equalsIgnoreCase("CANCELLED")) {
                List<Lot> lots = lotRepository.findByAuctionId(auctionId);
                if (lots != null && !lots.isEmpty()) {
                    for (Lot lot : lots) {
                        lot.setStatus(Lot.LotStatus.CANCELLED);
                        lotRepository.save(lot);
                    }
                }
                auction.setAuctionStatus(Auction.AuctionStatus.CANCELLED);
            }

            auctionRepository.save(auction);
        }
    }

    @Override
    @Transactional
    public AuctionResponse createAuction(AuctionCreationRequest request) throws ParseException {
        String bearerToken = httpServletRequest.getHeader("Authorization").substring(7);
        SignedJWT jwt = SignedJWT.parse(bearerToken);
        short memberId = jwt.getJWTClaimsSet().getLongClaim("id").shortValue();
        String staffId = employeeRepository.findByMemberId(memberId).getEeid();

        auctionRepository.saveAuction(request.getStarted(), request.getEnded());
        Auction auction = auctionRepository.findFirstByOrderByIdDesc();
        return auctionMapper.toAuctionResponse(auction);
    }

    @Override
    @Transactional
    public AuctionResponse updateAuction(short auctionId, AuctionUpdateRequest request) {
        Auction auction = auctionRepository.findAuctionById(auctionId);
        auctionMapper.updateAuction(auction, request);

        return auctionMapper.toAuctionResponse(auctionRepository.save(auction));
    }

    @Override
    @Transactional
    public short getPendingAuctionId() {
        short pendingAuctionId = 0;
        List<Auction> auction = auctionRepository.getAuctionByAuctionStatus(Auction.AuctionStatus.PENDING);
        if (auction != null && !auction.isEmpty()) {
            pendingAuctionId = auction.get(0).getId();
        } else {
            pendingAuctionId = auctionRepository.save();
        }
        return pendingAuctionId;
    }

    @Override
    public AuctionResponse getCurrentAuction() {
        Auction auction = auctionRepository.findFirstByOrderByIdDesc();
        if (auction == null) {
            throw new AppException(ErrorCode.AUCTION_NOT_FOUND);
        }
        return auctionMapper.toAuctionResponse(auction);
        
    }
}
