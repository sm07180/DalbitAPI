package com.demo.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY;
    @Value("${sso.cookie.max.age}")
    private long SSO_COOKIE_MAX_AGE;

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    public String generateToken(String name) {
        Date now = new Date();
        return Jwts.builder()
                .setId(name)
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + SSO_COOKIE_MAX_AGE)) // 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    /**
     * Jwt Token을 복호화 하여 이름을 얻는다.
     */
    public String getUserNameFromJwt(String jwt) {
        return getClaims(jwt).getBody().getId();
    }

    /**
     * Jwt Token의 유효성을 체크한다.
     */
    public boolean validateToken(String jwt) {
        return this.getClaims(jwt) != null;
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            //throw ex;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            //throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            //throw ex;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            //throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            //throw ex;
        }
        return null;
    }
}
