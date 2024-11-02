package com.mnky.kas.dto.response;

import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Lot;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotItemViewResponse {
    private Timestamp started;
    private Timestamp ended;
    private double startingPrice;
    private Lot.LotStatus status;

    private short koiId;
    private String variety;
    private String image;
//    private short length;
//    private short yob;
//    private String breeder;
//    private Gender gender;

    private double currentBid;
}
