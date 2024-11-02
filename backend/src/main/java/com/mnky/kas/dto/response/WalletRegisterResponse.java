package com.mnky.kas.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRegisterResponse implements Serializable {
    private double balance;
    private short memberId;
}
