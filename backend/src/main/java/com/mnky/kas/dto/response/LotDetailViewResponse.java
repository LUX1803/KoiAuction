package com.mnky.kas.dto.response;

import com.mnky.kas.model.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotDetailViewResponse {

    private short auctionId;

    private short lotId;

    private Timestamp started;
    private Timestamp ended;
    private double startingPrice;
    private double reservePrice;
    private double priceInterval;
    private Duration timeInterval;
    private Lot.LotStatus status;
    private double sellerCommission;
    private double buyerPremium;
    private double estimatedPrice;
    private double buyNowPrice;

    private short koiId;
    private String image;
    private Gender gender;
    private short length;
    private short yob;
    private String breeder;
    private String variety;

    private short methodId;
    private String methodName;

}
