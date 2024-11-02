package com.mnky.kas.mapper;

import com.mnky.kas.dto.request.BreederRegisterRequest;
import com.mnky.kas.dto.request.MemberRegisterRequest;
import com.mnky.kas.dto.response.BreederRegisterResponse;
import com.mnky.kas.dto.response.WinnerResponse;
import com.mnky.kas.dto.response.MemberProfileViewResponse;
import com.mnky.kas.dto.response.MemberRegisterResponse;
import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member toMember(MemberRegisterRequest member);

    @Mapping(source = "id", target = "id")
    MemberRegisterResponse toMemberRegisterResponse(Member member);

    Gender toGender(Gender gender);

    Member.Role toRole(Member.Role role);

    @Mapping(source = "farm.id",target = "farmId")
    @Mapping(source = "id", target = "ownerId")
    BreederRegisterResponse toBreederRegisterResponse(Member member);

    MemberProfileViewResponse toMemberProfileViewResponse(Member member);

    MemberRegisterRequest toMemberRegisterRequest(BreederRegisterRequest breederRegisterRequest);

//    @Mapping(source = "firstname", target = "firstname")
//    @Mapping(source = "lastname", target = "lastname")
//    @Mapping(source = "bids.amount", target = "bidAmount")
//    @Mapping(source = "bids.bidTime", target = "bidTime")
//    WinnerResponse toWinnerResponse(Member member);

}
