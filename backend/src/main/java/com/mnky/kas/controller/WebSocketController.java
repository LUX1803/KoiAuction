package com.mnky.kas.controller;

import com.mnky.kas.dto.request.BidRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.BidViewResponse;
import com.mnky.kas.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;


@Controller
@RequiredArgsConstructor
public class WebSocketController {

    @Autowired
    private final BidService bidService;

        @MessageMapping("/lot/{lotId}/english-bid")
        //@SendTo("app/lot/{lotId}/bid")
            public ApiResponse<?> placeEnglishBid(@DestinationVariable Short lotId, @Payload BidRequest bidRequest, SimpMessageHeaderAccessor headerAccessor) throws Exception {
                ApiResponse<?> response = new ApiResponse<>();
                bidService.placeEnglishBid(lotId, bidRequest, headerAccessor);
                response.setMessage("Bid successfully");
                return response;
            }

        }
