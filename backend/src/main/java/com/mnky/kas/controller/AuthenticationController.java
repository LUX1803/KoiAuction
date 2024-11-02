package com.mnky.kas.controller;

import com.mnky.kas.dto.request.AuthenticationRequest;
import com.mnky.kas.dto.request.RefreshTokenRequest;
import com.mnky.kas.dto.request.SignoutRequest;
import com.mnky.kas.dto.response.ApiResponse;
import com.mnky.kas.dto.response.AuthenticationResponse;
import com.mnky.kas.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setData(authenticationService.authenticate(request));

        return response;
    }

    @PostMapping("/signout")
    public ApiResponse<?> logout(@RequestBody SignoutRequest request) throws ParseException, JOSEException {
        authenticationService.signout(request);

        return new ApiResponse<>();
    }

    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setData(authenticationService.refreshToken(request));

        return response;
    }
}
