package com.mnky.kas.mapper;

import com.mnky.kas.dto.request.LotUpdateRequest;
import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.dto.response.LotDetailViewResponse;
import com.mnky.kas.dto.response.LotListViewResponse;
import com.mnky.kas.dto.response.LotSearchResponse;
import com.mnky.kas.model.*;
import org.mapstruct.*;

import java.util.Set;

@Mapper
public interface LotMapper {

    @Mapping(source = "lot.auction.id", target = "auctionId")
    @Mapping(source = "lot.id", target = "lotId")
    @Mapping(source = "lot.started", target = "started")
    @Mapping(source = "lot.ended", target = "ended")
    @Mapping(source = "lot.startingPrice", target = "startingPrice")
    @Mapping(source = "lot.estimatedPrice", target = "estimatedPrice")
    @Mapping(source = "lot.reservePrice", target = "reservePrice")
    @Mapping(source = "lot.buyNowPrice", target = "buyNowPrice")
    @Mapping(source = "lot.priceInterval", target = "priceInterval")
    @Mapping(source = "lot.status", target = "status")
//
    @Mapping(source = "lot.koi.id", target = "koiId")
//    @Mapping(source = "lot.koi.length", target = "length")
//    @Mapping(source = "lot.koi.yob", target = "yob")
    @Mapping(source = "lot.koi.image", target = "image")
//    @Mapping(source = "lot.koi.gender", target = "gender")
//    @Mapping(source = "lot.koi.farm.name", target = "breeder")
    @Mapping(source = "lot.koi.variety.name", target = "variety")
//
    @Mapping(source = "lot.method.id", target = "methodId")
    @Mapping(source = "currentBid", target = "currentBid")
    LotListViewResponse toLotListResponse(Lot lot, double currentBid);


    @Mapping(source = "lot.auction.id", target = "auctionId")
    @Mapping(source = "lot.id", target = "lotId")
    @Mapping(source = "lot.started", target = "started")
    @Mapping(source = "lot.ended", target = "ended")
    @Mapping(source = "lot.buyerPremium", target = "buyerPremium")
    @Mapping(source = "lot.sellerCommission", target = "sellerCommission")
    @Mapping(source = "lot.startingPrice", target = "startingPrice")
    @Mapping(source = "lot.status", target = "status")

    @Mapping(source = "lot.koi.id", target = "koiId")
    @Mapping(source = "lot.koi.length", target = "length")
    @Mapping(source = "lot.koi.yob", target = "yob")
    @Mapping(source = "lot.koi.image", target = "image")
    @Mapping(source = "lot.koi.gender", target = "gender")
    @Mapping(source = "lot.koi.farm.name", target = "breeder")
    @Mapping(source = "lot.koi.variety.name", target = "variety")

    @Mapping(source = "lot.method.id",target = "methodId")
    @Mapping(source = "lot.method.name",target = "methodName")
    LotDetailViewResponse toLotDetailResponse(Lot lot);

    @Mapping(source = "koi.id", target = "koiId")
    @Mapping(source = "auction.id", target = "auctionId")
    @Mapping(source = "staff.eeid", target = "eeid")
    @Mapping(source = "method.id", target = "methodId")
    LotSearchResponse toLotSearchResponse(Lot lot);

    //@Mapping(source = "auctionId", target = "auction.id")
    @Mapping(target = "method", expression = "java(mapAuctionMethodIdToAuctionMethod(request.getMethodId()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateLot(@MappingTarget Lot lot, LotUpdateRequest request);

    default AuctionMethod mapAuctionMethodIdToAuctionMethod(short auctionMethodId) {
        if (auctionMethodId == 0) {
            return null;
        }
        AuctionMethod auctionMethod = new AuctionMethod();
        auctionMethod.setId(auctionMethodId);
        return auctionMethod;
    }
}
