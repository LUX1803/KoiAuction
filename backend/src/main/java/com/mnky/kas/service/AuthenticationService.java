package com.mnky.kas.service;


import com.mnky.kas.dto.request.AuthenticationRequest;
import com.mnky.kas.dto.request.IntrospectRequest;
import com.mnky.kas.dto.request.RefreshTokenRequest;
import com.mnky.kas.dto.request.SignoutRequest;
import com.mnky.kas.dto.response.AuthenticationResponse;
import com.mnky.kas.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void signout(SignoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
