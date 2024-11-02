package com.mnky.kas.dto.response;

import com.mnky.kas.model.Farm;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FarmResponse {
    private short id;
    private String name;
    private String image;
    private String description;
    private Timestamp sent;
    private Timestamp replied;
    private Farm.FarmStatus status;

    private short ownerId;
    private String ownerFirstname;
    private String ownerLastname;
    private String address;
    private String phone;

}
