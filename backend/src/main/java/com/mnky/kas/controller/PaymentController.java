package com.mnky.kas.controller;



import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.VNPayResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.service.VNPayService;
import com.mnky.kas.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final VNPayService vnPayService;
    private final WalletService walletService;

    @GetMapping("/vnpay")
    public VNPayResponse pay(HttpServletRequest request) {
        return vnPayService.createVnPayPayment(request);
    }

    @GetMapping("/vnpay-callback")
    public RedirectView payCallbackHandler(HttpServletRequest request) {
        String url = vnPayService.handleVnPayCallback(request);
        return new RedirectView(url);
    }

    @PostMapping("/wallet")
    public ApiResponse<WalletResponse>  paymentWithWallet(@RequestHeader("Authorization") String token, @RequestBody List<Integer> transactionIds) throws IOException, ParseException {
        WalletResponse res = walletService.paymentWithWallet(token,transactionIds);
        return ApiResponse.<WalletResponse>builder()
                .success(true)
                .code(200)
                .message("Payment Success")
                .data(res)
                .build();
    }

}
