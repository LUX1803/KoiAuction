package com.mnky.kas.dto.request;

import com.mnky.kas.model.Auction;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionCreationRequest {
    private String name;
    private String description;
    private Timestamp started;
    private Timestamp ended;
    private Timestamp signup_opened;
    private Timestamp signup_closed;
    private double signup_fee;
}
