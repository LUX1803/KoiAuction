package com.mnky.kas.dto.response;



import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Koi;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KoiResponse {

    private short id;
    private short yob;
    private short length;
    private Gender gender;
    private String image;
    private Koi.KoiStatus status;
    private Timestamp sent;
    private Timestamp replied;
    private String message;
    private String note;


    private short varietyId;
    private String varietyName;
    private short farmId;
    private String farmName;
    private String staffId;
}
