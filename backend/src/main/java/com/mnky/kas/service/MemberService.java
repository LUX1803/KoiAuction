package com.mnky.kas.service;

import com.mnky.kas.dto.request.BreederRegisterRequest;
import com.mnky.kas.dto.request.MemberRegisterRequest;
import com.mnky.kas.dto.response.BreederRegisterResponse;
import com.mnky.kas.dto.response.MemberProfileViewResponse;
import com.mnky.kas.dto.response.MemberRegisterResponse;

public interface MemberService {
    MemberRegisterResponse register(MemberRegisterRequest request);

    BreederRegisterResponse registerBreeder(BreederRegisterRequest request);

    MemberProfileViewResponse viewProfile(short memberId);

    MemberProfileViewResponse viewRecipientProfile(short koiId);
}
