package com.mnky.kas.dto.request;

import com.mnky.kas.model.Lot;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotRequest implements Serializable {
    private Timestamp started;
    private Timestamp ended;
    private double startingPrice;
    private double estimatedPrice;
    private double reservePrice;
    private double buyNowPrice;
    private double priceInterval;
    private Duration timeInterval;
    private String status;
    private double sellerCommission;
    private double buyerPremium;
    private short auctionId;
    private short koiId;
    private short methodId;
    private String eeid;


}
