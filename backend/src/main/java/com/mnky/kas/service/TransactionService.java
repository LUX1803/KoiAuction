package com.mnky.kas.service;

import com.mnky.kas.dto.response.TransactionResponse;

import java.text.ParseException;
import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactionByMemberId(String token) throws ParseException;

    List<TransactionResponse> getTransactionByMemberIdAndStatus(String token, String status) throws ParseException;
}
