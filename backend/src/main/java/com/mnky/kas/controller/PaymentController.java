package com.mnky.kas.controller;


import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.VNPayResponse;
import com.mnky.kas.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final VNPayService vnPayService;

    @GetMapping("/vnpay")
    public VNPayResponse pay(HttpServletRequest request) {
        return vnPayService.createVnPayPayment(request);
    }

    @GetMapping("/vnpay-callback")
    public RedirectView payCallbackHandler(HttpServletRequest request) {
        String url = vnPayService.handleVnPayCallback(request);
        return new RedirectView(url);
    }



}
