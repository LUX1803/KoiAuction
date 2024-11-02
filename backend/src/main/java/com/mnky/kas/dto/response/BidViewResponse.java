package com.mnky.kas.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidViewResponse {
    private short id;
    private double amount;
    private Timestamp time;
    private boolean isHighest;
    private short bidderId;
    private String bidderFirstname;
    private String bidderLastname;
}
