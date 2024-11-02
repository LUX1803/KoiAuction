package com.mnky.kas.dto.response;

import com.mnky.kas.model.Farm;
import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreederRegisterResponse {
    private String username;
    private String email;
    private String phone;
    private String address;
    private String firstname;
    private String lastname;
    private Farm.FarmStatus status;
    private Timestamp sent;
    private Timestamp replied;
    private Gender gender;
    private short ownerId;
    private short farmId;

}