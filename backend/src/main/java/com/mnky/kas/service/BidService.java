package com.mnky.kas.service;

import com.mnky.kas.dto.request.BidRequest;
import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.dto.response.WinnerResponse;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.text.ParseException;
import java.util.List;

public interface BidService {

    void placeFixedPriceBid(short lotId) throws ParseException;

    void placeSealedBid(short lotId, BidRequest bidRequest, String token) throws ParseException;

    void placeEnglishBid(short lotId, BidRequest bidRequest, SimpMessageHeaderAccessor headerAccessor) throws ParseException;

    void buyLotNow(short lotId, String token) throws ParseException;

    void placeDutchBid (short lotId, String token) throws ParseException;

    List<BidViewResponse> getBids(short lotId);

//    WinnerResponse getWinner(short lotId);





    BidViewResponse getHighestBid(short lotId);

    BidViewResponse getBidByLotIdAndBidderId(short lotId, short bidderId);
}
