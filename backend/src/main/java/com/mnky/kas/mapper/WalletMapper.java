package com.mnky.kas.mapper;

import com.mnky.kas.dto.request.WalletRegisterRequest;
import com.mnky.kas.dto.response.WalletRegisterResponse;
import com.mnky.kas.model.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet toWallet(WalletRegisterRequest wallet);

    WalletRegisterResponse toWalletRegisterResponse(Wallet wallet);
}
