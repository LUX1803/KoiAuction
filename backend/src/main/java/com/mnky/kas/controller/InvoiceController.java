package com.mnky.kas.controller;

import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.InvoiceResponse;
import com.mnky.kas.service.InvoiceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/invoice/lot/{lotId}")
    public ApiResponse<InvoiceResponse> getInvoiceByLotIdAndRecipientId(@PathVariable short lotId, @RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<InvoiceResponse> res = new ApiResponse<>();
        res.setData(invoiceService.getInvoiceByLotIdAndRecipientId(lotId, token));
        return res;
    }

    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<InvoiceResponse> getInvoiceByInvoiceId(@PathVariable short invoiceId) throws ParseException {
        ApiResponse<InvoiceResponse> res = new ApiResponse<>();
        res.setData(invoiceService.getInvoiceById(invoiceId));
        return res;
    }
}
