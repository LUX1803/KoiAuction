package com.mnky.kas.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionResponse {
    private Short id;
    private String name;
    private String description;
    private Timestamp started;
    private Timestamp ended;
    private Timestamp signup_opened;
    private Timestamp signup_closed;
    private double signup_fee;
    private Timestamp created;
    private Timestamp updated;
    private String staff;
    private String status;
}