package com.mnky.kas.dto.request;

import com.mnky.kas.model.Lot;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotUpdateRequest {
    private Timestamp started;
    private Timestamp ended;
    private Double startingPrice;
    private Double estimatedPrice;
    private Double reservePrice;
    private Double buyNowPrice;
    private Double priceInterval;
    private Duration timeInterval;
    private Double sellerCommission;
    private Double buyerPremium;
    private short methodId;
    private short auctionId;
}
