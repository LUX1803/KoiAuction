package com.mnky.kas.service;

import com.mnky.kas.dto.request.AuthenticationRequest;
import com.mnky.kas.dto.request.IntrospectRequest;
import com.mnky.kas.dto.request.RefreshTokenRequest;
import com.mnky.kas.dto.request.SignoutRequest;
import com.mnky.kas.dto.response.AuthenticationResponse;
import com.mnky.kas.dto.response.IntrospectResponse;
import com.mnky.kas.exception.AppException;
import com.mnky.kas.exception.ErrorCode;
import com.mnky.kas.model.InvalidatedToken;
import com.mnky.kas.model.Member;
import com.mnky.kas.repository.InvalidatedTokenRepository;
import com.mnky.kas.repository.MemberRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        String token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername());
        if (member == null) {
            throw new AppException(ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean result = passwordEncoder.matches(request.getPassword(), member.getPassword());

        if (!result) {
            throw new AppException(ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        }

        String token = generateToken(member);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void signout(SignoutRequest request) throws ParseException, JOSEException {
        SignedJWT signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken(jit, expirationTime);
        invalidatedTokenRepository.save(invalidatedToken);

    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request)
            throws ParseException, JOSEException {
        SignedJWT signJWT = verifyToken(request.getToken());

        String jit = signJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken(jit, expirationTime);
        invalidatedTokenRepository.save(invalidatedToken);

        String username = signJWT.getJWTClaimsSet().getSubject();
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(member);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private String generateToken(Member member) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(member.getUsername())
                .issuer("koiauction.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(23, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", member.getRole())
                .claim("firstname", member.getFirstname())
                .claim("lastname", member.getLastname())
                .claim("id", member.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
