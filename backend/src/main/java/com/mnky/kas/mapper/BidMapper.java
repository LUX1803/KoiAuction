package com.mnky.kas.mapper;

import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.model.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BidMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "highest", target = "isHighest")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "time", target = "time")
    @Mapping(source = "bidder.id", target = "bidderId")
    @Mapping(source = "bidder.firstname", target = "bidderFirstname")
    @Mapping(source = "bidder.lastname", target = "bidderLastname")
    BidViewResponse toBidViewResponse(Bid bid);
}
