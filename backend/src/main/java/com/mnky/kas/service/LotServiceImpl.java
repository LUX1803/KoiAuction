package com.mnky.kas.service;

import com.mnky.kas.dto.request.LotRequest;
import com.mnky.kas.dto.request.LotUpdateRequest;
import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.dto.response.LotDetailViewResponse;
import com.mnky.kas.dto.response.LotListViewResponse;
import com.mnky.kas.dto.response.LotSearchResponse;
import com.mnky.kas.exception.AppException;
import com.mnky.kas.exception.ErrorCode;
import com.mnky.kas.mapper.BidMapper;
import com.mnky.kas.mapper.LotMapper;
import com.mnky.kas.model.*;
import com.mnky.kas.repository.*;
import com.mnky.kas.util.JWTUtil;
import com.mnky.kas.util.StringProcess;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;


@Service
public class LotServiceImpl implements LotService {
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private KoiRepository koiRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private LotMapper lotMapper;
    @Autowired
    private AuctionMethodRepository auctionMethodRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public List<LotListViewResponse> viewLotList(short auctionId) {
        List<LotListViewResponse> list = new ArrayList<>();
        for (Lot lot : lotRepository.findByAuctionId(auctionId)){
            //Koi koi = koiRepository.findById(lot.getKoi().getId());
            double currentBid = bidRepository.findHighestBidByLotId(lot.getId());
             
            list.add(lotMapper.toLotListResponse(lot, currentBid));
        }
        if(list.isEmpty()){
            return null;
        }

        return list;
    }

    @Override
    public LotDetailViewResponse viewLotDetail(short auctionId, short lotId) {
        Lot lot = lotRepository.findByAuctionIdAndId(auctionId,lotId);

        if(lot == null){
            return null;
        }

        return lotMapper.toLotDetailResponse(lot);
    }

    public List<LotSearchResponse> searchLots(Short auctionId, String varietyNames,
                                                           Short length, String gender,
                                                           String farmNames, String methodNames,
                                                           Double minPrice, Double maxPrice) {
        String delimiter = ",";
        Specification<Lot> spec = Specification.where(LotSpecification.hasAuctionId(auctionId))
                .and(LotSpecification.hasVarietyNames(StringProcess.splitStringByDelimiter(varietyNames, delimiter)))

                .and(LotSpecification.hasLength(length))

                .and(LotSpecification.hasGender(gender))

                .and(LotSpecification.hasFarmNames(StringProcess.splitStringByDelimiter(farmNames, delimiter)))

                .and(LotSpecification.hasMethodNames(StringProcess.splitStringByDelimiter(methodNames, delimiter)))

                .and(LotSpecification.hasPriceRange(minPrice, maxPrice));

        List<Lot> lots = lotRepository.findAll(spec);
        if (lots.isEmpty()) {
            throw new AppException(ErrorCode.LOT_NOT_FOUND);
        }

        List<LotSearchResponse> lotSearchResponses = new ArrayList<>();
        for (Lot lot : lots) {
            lotSearchResponses.add(lotMapper.toLotSearchResponse(lot));
        }

        return lotSearchResponses;
    }

    @Override
    public void createLot (LotRequest dto, String token) throws ParseException {
        String jwtToken = token.substring(7);
        short memberId = JWTUtil.getMemberIdFromToken(jwtToken);
        String staffId = employeeRepository.findByMemberId(memberId).getEeid();
        try {
            if (dto.getMethodId() == 4) {
                dto.setBuyNowPrice(dto.getStartingPrice());
            }
             lotRepository.save(dto.getStarted(),
                    dto.getEnded(),
                    dto.getStartingPrice(),
                    dto.getEstimatedPrice(),
                    dto.getReservePrice(),
                    dto.getBuyNowPrice(),
                    dto.getPriceInterval(),
                    dto.getTimeInterval(),
                    dto.getSellerCommission(),
                    dto.getBuyerPremium(),
                    auctionService.getPendingAuctionId(),
                    dto.getKoiId(),
                    dto.getMethodId(),
                     staffId);
            Koi koi = koiRepository.findById(dto.getKoiId());
            koi.setStatus(Koi.KoiStatus.AUCTIONING);
            koiRepository.save(koi);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AppException(ErrorCode.CREATION_FAILED);
        }
    }

    @Override
    @Transactional
    public LotDetailViewResponse updateLot(short lotId, LotUpdateRequest request, String token) throws ParseException {
        String jwtToken = token.substring(7);
        String username = JWTUtil.getUserNameFromToken(jwtToken);
        Member member = memberRepository.findByUsername(username);
        System.out.println(request.getMethodId());
        if (!member.getRole().equals(Member.Role.STAFF)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        } else {
            Lot lot = lotRepository.findById(lotId);
            if (lot == null) {
                throw new AppException(ErrorCode.LOT_NOT_FOUND);
            }
            lotMapper.updateLot(lot, request);
            return lotMapper.toLotDetailResponse(lotRepository.save(lot));
        }
    }

    @Override
    public List<LotDetailViewResponse> getLotByKoiId(short koiId) {
        List<LotDetailViewResponse> list = new ArrayList<>();
        for (Lot lot : lotRepository.findByKoiId(koiId)){
            list.add(lotMapper.toLotDetailResponse(lot));
        }

        if (list.isEmpty()){
            return null;
        }

        return list;
    }
}
