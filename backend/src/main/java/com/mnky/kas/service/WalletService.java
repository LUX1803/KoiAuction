package com.mnky.kas.service;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.model.Member;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface WalletService {
    void createWallet(WalletRegisterRequest walletRegisterRequest);
    WalletResponse getMemberAndWallet(String bearerToken) throws ParseException;

    TransactionResponse addBalanceTransaction(String bearerToken, Double balance) throws ParseException;
    List<TransactionResponse> refundAllBalanceTransactionByLotId(short lotId);

    void addBalance(Member owner , Double balance);

    void deductBalance(Member owner , Double balance);

    WalletResponse paymentWithWallet(String bearerToken, List<Integer> transactionIds) throws ParseException;

    WalletResponse placeBidUsingWallet(String bearerToken, Double amount, Short lotId) throws ParseException;

    //Get User placed bid by lot ID ( highest amount all placed Bid )
    WalletResponse getUserPlacedBidByLotId(String bearerToken, Short lotId) throws ParseException;


}
