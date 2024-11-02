package com.mnky.kas.controller;

import com.mnky.kas.dto.request.LotRequest;
import com.mnky.kas.dto.request.LotUpdateRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.LotDetailViewResponse;
import com.mnky.kas.dto.response.LotListViewResponse;
import com.mnky.kas.dto.response.LotSearchResponse;
import com.mnky.kas.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
public class LotController {
    @Autowired
    private LotService lotService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/auction/{auctionId}/list")
    public ApiResponse<List<LotListViewResponse>> viewLotList(@PathVariable short auctionId) {
        ApiResponse<List<LotListViewResponse>> response = new ApiResponse<>();
        response.setData(lotService.viewLotList(auctionId));

        return response;
    }


    @GetMapping("/auction/{auctionId}/{lotId}")
    public ApiResponse<LotDetailViewResponse> viewLotListDetail(@PathVariable short auctionId, @PathVariable short lotId) {
        ApiResponse<LotDetailViewResponse> response = new ApiResponse<>();
        LotDetailViewResponse lotDetailViewResponse = lotService.viewLotDetail(auctionId, lotId);
        response.setData(lotDetailViewResponse);

        if (lotDetailViewResponse.getStatus().equals("LIVE") || lotDetailViewResponse.getStatus().equals("UPCOMING")) {
            messagingTemplate.convertAndSend("/topic/auction/" + auctionId + "/" + lotId, lotDetailViewResponse);
        }

        return response;
    }

    @GetMapping("/auction/{auctionId}/search")
    public ApiResponse<List<LotSearchResponse>> searchLots(
            @PathVariable Short auctionId,
            @RequestParam(required = false) String varietyNames,
            @RequestParam(required = false) Short length,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String farmNames,
            @RequestParam(required = false) String methodNames,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        ApiResponse<List<LotSearchResponse>> response = new ApiResponse<>();
        response.setData(lotService.searchLots(auctionId, varietyNames, length, gender,
                                                farmNames, methodNames, minPrice, maxPrice));

        return response;
    }


    @PostMapping("/lot")
    public ApiResponse<LotDetailViewResponse> createLot(@RequestBody LotRequest dto, @RequestHeader("Authorization") String token) throws ParseException {
        System.out.println(dto.toString());
        ApiResponse<LotDetailViewResponse> response = new ApiResponse<>();
        lotService.createLot(dto, token);
        return response;
    }

    @PostMapping("lot/{lotId}/updateLot")
    public ApiResponse<LotDetailViewResponse> updateLot(@PathVariable short lotId, @RequestBody LotUpdateRequest request, @RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<LotDetailViewResponse> response = new ApiResponse<>();
        response.setData(lotService.updateLot(lotId,request,token));
        return response;
    }

    @GetMapping("/lot/koi/{koiId}")
    public ApiResponse<List<LotDetailViewResponse>> getLotByKoiId(@PathVariable short koiId) {
        ApiResponse<List<LotDetailViewResponse>> response = new ApiResponse<>();
        response.setData(lotService.getLotByKoiId(koiId));
        return response;
    }
}
