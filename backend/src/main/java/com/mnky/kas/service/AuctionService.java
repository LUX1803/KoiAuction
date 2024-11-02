package com.mnky.kas.service;

import com.mnky.kas.dto.request.AuctionCreationRequest;
import com.mnky.kas.dto.request.AuctionUpdateRequest;
import com.mnky.kas.dto.response.AuctionResponse;
import com.mnky.kas.model.Auction;

import java.text.ParseException;
import java.util.List;

public interface AuctionService {

    AuctionResponse getCurrentAuction();

    List<AuctionResponse> getAuctionsWithStatus(String status);



    AuctionResponse getAuctionById(Short auctionId);

    void updateAuctionStatus(short auctionId, String status);

    AuctionResponse createAuction(AuctionCreationRequest request) throws ParseException;

    AuctionResponse updateAuction(short auctionId, AuctionUpdateRequest request);

    short getPendingAuctionId();
}
