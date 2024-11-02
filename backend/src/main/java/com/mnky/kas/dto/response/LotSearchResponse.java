package com.mnky.kas.dto.response;

import com.mnky.kas.model.Lot;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotSearchResponse {
    private short id;
    private Timestamp started;
    private Timestamp ended;
    private double startingPrice;
    private double reservePrice;
    private double priceInterval;
    private Duration timeInterval;
    private Lot.LotStatus status;
    private double sellerCommision;
    private double buyerPremium;
    private short koiId;
    private short auctionId;
    private short methodId;
    private String eeid;
}
