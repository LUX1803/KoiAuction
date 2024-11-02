package com.mnky.kas.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRegisterRequest implements Serializable {
    private double balance = 0;
    private short ownerId;
}
