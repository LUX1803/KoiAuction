package com.mnky.kas.controller;

import com.mnky.kas.dto.request.AuctionCreationRequest;
import com.mnky.kas.dto.request.AuctionUpdateRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.AuctionResponse;
import com.mnky.kas.model.Auction;
import com.mnky.kas.service.AuctionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@Tag(name = "Auction")
@RequestMapping("/auction")
@Slf4j
@RequiredArgsConstructor
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping("/status/{status}")
    public ApiResponse<List<AuctionResponse>> getAuctionsWithStatus(@PathVariable String status) {
        ApiResponse<List<AuctionResponse>> response = new ApiResponse<>();
        response.setData(auctionService.getAuctionsWithStatus(status));
        return response;
    }

    @GetMapping("/current")
    public ApiResponse<AuctionResponse> getCurrentAuction() {
        ApiResponse<AuctionResponse> response = new ApiResponse<>();
        response.setData(auctionService.getCurrentAuction());
        return response;
    }

    @GetMapping("/{auctionId}")
    public ApiResponse<AuctionResponse> getAuctionById(@PathVariable Short auctionId) {
        ApiResponse<AuctionResponse> response = new ApiResponse<>();
        response.setData(auctionService.getAuctionById(auctionId));
        return response;
    }

    @PatchMapping("/{auctionId}/{status}")
    public ApiResponse<?> updateAuctionStatus(@PathVariable Short auctionId, @PathVariable String status) {
        System.out.println(auctionId + " " + status);
        auctionService.updateAuctionStatus(auctionId, status.toLowerCase());
        return new ApiResponse<>();
    }

    @PutMapping("/create")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse<AuctionResponse> createAuction(@RequestBody AuctionCreationRequest request) throws ParseException {
        ApiResponse<AuctionResponse> response = new ApiResponse<>();
        response.setData(auctionService.createAuction(request));

        return response;
    }

    @PostMapping("/{auctionId}/update")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse<AuctionResponse> updateAuction(@PathVariable short auctionId,
                                                      @RequestBody AuctionUpdateRequest request) {
        ApiResponse<AuctionResponse> response = new ApiResponse<>();
        response.setData(auctionService.updateAuction(auctionId, request));

        return response;
    }
}
