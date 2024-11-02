package com.mnky.kas.dto.response;

import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Lot;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotListViewResponse implements Serializable {
    private short auctionId;
    private short lotId;
    private Timestamp started;
    private Timestamp ended;
    private double buyerPremium;
    private double sellerCommission;
    private double startingPrice;
    private double reservePrice;
    private double priceInterval;
    private double estimatedPrice;
    private double buyNowPrice;
    private Duration timeInterval;
    private Lot.LotStatus status;
//
    private short koiId;
    private String image;
    private short length;
    private short yob;
    private String breeder;
    private String variety;
    private Gender gender;
    private short methodId;

    private double currentBid;


}
