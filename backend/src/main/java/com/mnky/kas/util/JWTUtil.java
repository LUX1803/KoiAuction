package com.mnky.kas.util;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class JWTUtil {
    public static JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token); // Parse token
        return signedJWT.getJWTClaimsSet(); // Lấy thông tin claims
    }

    // Lấy username của nhân viên từ JWT
    public static String getUserNameFromToken(String token) throws ParseException {
        JWTClaimsSet claims = getClaimsFromToken(token);
        // Giả sử bạn lưu ID vào "sub" (subject) trong token
        return claims.getSubject();
    }

    public static short getMemberIdFromToken(String token) throws ParseException {
        JWTClaimsSet claims = getClaimsFromToken(token);
        return claims.getLongClaim("id").shortValue();
    }

}
