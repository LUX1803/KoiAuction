package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.WalletResponse;

import java.text.ParseException;
import java.util.Map;

public interface WalletService {
    void createWallet(WalletRegisterRequest walletRegisterRequest);
    WalletResponse getMemberAndWallet(String bearerToken) throws ParseException;
}
