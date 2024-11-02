package com.mnky.kas.mapper;

import com.mnky.kas.dto.request.AuctionUpdateRequest;
import com.mnky.kas.dto.response.AuctionResponse;
import com.mnky.kas.model.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuctionMapper {
    @Mapping(source = "staff.eeid", target = "staff")
    @Mapping(source = "auction.auctionStatus" , target = "status")
    AuctionResponse toAuctionResponse (Auction auction);

    void updateAuction(@MappingTarget Auction auction, AuctionUpdateRequest request);
}
