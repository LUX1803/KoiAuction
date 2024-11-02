package com.mnky.kas.dto.request;

import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterRequest implements Serializable {
    @Size(min = 2, message = "INVALID_USERNAME")
    private String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;

    @Email(message = "INVALID_EMAIL")
    private String email;

    @Size(min = 10, max = 10, message = "INVALID_PHONE_NUMBER")
    private String phone;
    private String address;
    private String firstname;
    private String lastname;
    private Gender gender = Gender.UNKNOWN;
    private Member.Role role = Member.Role.BIDDER;
}
