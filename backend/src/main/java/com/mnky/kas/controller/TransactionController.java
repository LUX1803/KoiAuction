package com.mnky.kas.controller;

import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ApiResponse<List<TransactionResponse>> getTransactionByLotId(@RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>();
        response.setData(transactionService.getTransactionByMemberId(token));
        return response;
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<TransactionResponse>> getTransactionByLotId(@PathVariable("status") String status, @RequestHeader("Authorization") String token) throws ParseException {
        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>();
        response.setData(transactionService.getTransactionByMemberIdAndStatus(token, status));
        return response;
    }
}
