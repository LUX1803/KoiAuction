package com.mnky.kas.dto.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KoiRequest {
    private short id;          // For update, this will be required
    private short yob;
    private short length;
    private String gender;
    private String image;
    private String note;
    private String message;
    private String status;

    private Short varietyId;    // Assuming variety is mandatory

}
