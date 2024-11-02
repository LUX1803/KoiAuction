package com.mnky.kas.controller;

import com.mnky.kas.dto.request.BidRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.dto.response.WinnerResponse;
import com.mnky.kas.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/lot")
public class BidController {
    @Autowired
    BidService bidService;

    @GetMapping("/{lotId}/bid")
    public ApiResponse<List<BidViewResponse>> getBids(@PathVariable short lotId) {
        ApiResponse<List<BidViewResponse>> response = new ApiResponse<>();
        response.setData(bidService.getBids(lotId));
        return response;
    }

    @GetMapping("/{lotId}/highest-bid")
    public ApiResponse<BidViewResponse> getHighestBid(@PathVariable short lotId) {
        ApiResponse<BidViewResponse> response = new ApiResponse<>();
        response.setData(bidService.getHighestBid(lotId));
        return response;
    }

    @PostMapping("/{lotId}/fixed-price-bid")
    public ApiResponse<?> bid1(@PathVariable short lotId) throws ParseException {
        ApiResponse<?> response = new ApiResponse<>();
        bidService.placeFixedPriceBid(lotId);
        response.setMessage("Bid successfully");
        return response;
    }

    @PostMapping("/{lotId}/sealed-bid")
    public ApiResponse<?> bid2(@PathVariable short lotId, @RequestBody BidRequest bidRequest, @RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<?> response = new ApiResponse<>();
        bidService.placeSealedBid(lotId, bidRequest, token);
        response.setMessage("Bid successfully");
        return response;
    }


    @PostMapping("/{lotId}/dutch-bid")
    public ApiResponse<?> placeDutchBid(@PathVariable short lotId, @RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<?> response = new ApiResponse<>();
        bidService.placeDutchBid(lotId, token);
        return response;
    }

    @GetMapping("/{lotId}/bidder/{bidderId}")
    public ApiResponse<BidViewResponse> getBidByBidder(@PathVariable short lotId, @PathVariable short bidderId) {
        ApiResponse<BidViewResponse> response = new ApiResponse<>();
        response.setData(bidService.getBidByLotIdAndBidderId(lotId, bidderId));
        return response;
    }
//    @GetMapping("{lotId}/winner")
//    public ApiResponse<WinnerResponse> getWinner(@PathVariable short lotId) {
//        ApiResponse<WinnerResponse> response = new ApiResponse<>();
//        response.setData(bidService.getWinner(lotId));
//        return response;
//    }

    @PostMapping("/{lotId}/buy-now")
    public ApiResponse<?> buyLotNow(@PathVariable short lotId, @RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<?> response = new ApiResponse<>();
        bidService.buyLotNow(lotId, token);
        return response;
    }
}

