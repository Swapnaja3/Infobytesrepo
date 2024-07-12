package com.infobyte_Technosysy.infobyte_Technosys.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtUtils {

    public JwtUtils() {
    }

    private static SecretKey secretKey;
    private static final String ISSUER="coding_streams_auth_server";

    public JwtUtils(SecretKey secretKey) {

        this.secretKey = secretKey;
    }

    public static boolean validateToken(String jwtToken)
    {
        return parseToken(jwtToken) != null;
    }
    private static Claims parseToken(String jwtToken)
    {
        try
        {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        }
        catch (JwtException | IllegalArgumentException e)
        {

           log.error("JWT Exception Occured");
        }
        return null;
    }



    public static String getUsernameFromToken(String jwtToken) {

        Claims claims = parseToken(jwtToken);
        return claims != null ? claims.getSubject() : null;
    }

    public static String generateToken(String username) {
        var currentDate=new Date();
        var jwtExpirationInMinutes=10;
        var expiration=  DateUtils.addMinutes(currentDate,jwtExpirationInMinutes);
      return  Jwts.builder().setId(UUID.randomUUID().toString()).setIssuer(ISSUER).setSubject(username).signWith(secretKey).setIssuedAt(currentDate).setExpiration(expiration).compact();
    }
}
