package com.mnky.kas.controller;

import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallet")
    public ApiResponse<WalletResponse> getWallet(@RequestHeader("Authorization") String bearerToken) throws ParseException {
        ApiResponse<WalletResponse> response = new ApiResponse<>();
        response.setData(walletService.getMemberAndWallet(bearerToken));
        return response;
    }
}
