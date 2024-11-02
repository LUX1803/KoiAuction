package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.model.Member;

import java.text.ParseException;
import java.util.Map;

public interface WalletService {
    void createWallet(WalletRegisterRequest walletRegisterRequest);
    WalletResponse getMemberAndWallet(String bearerToken) throws ParseException;

    TransactionResponse addBalanceTransaction(String bearerToken, Double balance) throws ParseException;

    void addBalance(Member owner , Double balance);
}
