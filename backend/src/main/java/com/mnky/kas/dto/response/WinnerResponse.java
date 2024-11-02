package com.mnky.kas.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinnerResponse {
    private String firstname;
    private String lastname;
    private double bidAmount;
    private Timestamp bidTime;
}
