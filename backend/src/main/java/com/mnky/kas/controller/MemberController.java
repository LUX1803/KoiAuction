package com.mnky.kas.controller;

import com.mnky.kas.dto.request.BreederRegisterRequest;
import com.mnky.kas.dto.request.MemberRegisterRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.BreederRegisterResponse;
import com.mnky.kas.dto.response.MemberProfileViewResponse;
import com.mnky.kas.dto.response.MemberRegisterResponse;
import com.mnky.kas.mapper.MemberMapper;
import com.mnky.kas.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @PostMapping("/signup")
    public ApiResponse<MemberRegisterResponse> register(@RequestBody MemberRegisterRequest request) {
        ApiResponse<MemberRegisterResponse> response = new ApiResponse<>();
        response.setData(memberService.register(request));

        return response;
    }

    @PostMapping("/breeder-register")
    public ApiResponse<BreederRegisterResponse> registerBreeder(@RequestBody BreederRegisterRequest request) {
        memberService.register(memberMapper.toMemberRegisterRequest(request));

        ApiResponse<BreederRegisterResponse> response = new ApiResponse<>();
        response.setData(memberService.registerBreeder(request));

        return response;
    }

    @PostMapping("/{memberId}")
    public ApiResponse<MemberProfileViewResponse> viewProfile(@PathVariable short memberId) {
        ApiResponse<MemberProfileViewResponse> response = new ApiResponse<>();
        response.setData(memberService.viewProfile(memberId));

        return response;
    }

    @GetMapping("/recipient/koi/{koiId}")
    public ApiResponse<MemberProfileViewResponse> viewRecipientProfile(@PathVariable short koiId) {
        ApiResponse<MemberProfileViewResponse> response = new ApiResponse<>();
        response.setData(memberService.viewRecipientProfile(koiId));
        return response;
    }
}
