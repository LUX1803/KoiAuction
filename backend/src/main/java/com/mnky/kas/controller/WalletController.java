package com.mnky.kas.controller;

import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.TransactionResponse;
import com.mnky.kas.dto.response.WalletResponse;
import com.mnky.kas.model.Transaction;
import com.mnky.kas.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/wallet/{balance}")
    @Operation(summary = "Add balance to wallet", description = "This action will also create a transaction with out invoice or payment.")
    public ApiResponse<TransactionResponse> addBalance(@RequestHeader("Authorization") String bearerToken,
                                  @PathVariable Double balance) {
        try {
            TransactionResponse transactionResponse = walletService.addBalanceTransaction(bearerToken, balance);
            return ApiResponse.<TransactionResponse>builder()
                    .code(200)
                    .message("Success")
                    .data(transactionResponse)
                    .build();
        } catch (ParseException e) {
            return ApiResponse.<TransactionResponse>builder()
                    .code(400)
                    .message("Invalid balance format")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<TransactionResponse>builder()
                    .code(500)
                    .message("An error occurred while adding balance")
                    .build();
        }
    }

    @GetMapping("/wallet/{lotId}")
    @Operation(summary = "Get money for placed bid by lot ID", description = "Retrieve the placed bid for a specific lot ID")
    public ApiResponse<WalletResponse> getPlacedBidByLotId(@RequestHeader("Authorization") String bearerToken, @PathVariable("lotId") Short lotId) throws ParseException {
        return ApiResponse.<WalletResponse>builder()
                .code(200)
                .success(true)
                .message("Get placed bid success for lot: "+lotId)
                .data(walletService.getUserPlacedBidByLotId(bearerToken, lotId))
                .build();
    }
}
