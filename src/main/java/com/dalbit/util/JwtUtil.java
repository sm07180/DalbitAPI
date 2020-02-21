package com.dalbit.util;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.service.CommonService;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.TokenVo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class JwtUtil {

    @Autowired
    HttpServletRequest request;
    @Autowired
    CommonService commonService;

    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY;

    final String JWT_SEPARATOR = "@";

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    public String generateToken(String name) {
        return Jwts.builder()
                .setId(name)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행일자
                .setExpiration(new Date(System.currentTimeMillis() + 2592000000L)) // 유효시간 설정 (30일 기준)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    public String generateToken(String memNo, boolean isLogin) {
        return generateToken(memNo + JWT_SEPARATOR + isLogin);
    }

    /**
     * Jwt Token을 복호화 하여 이름을 얻는다.
     */
    public String getUserNameFromJwt(String jwt) throws GlobalException {
        return getClaims(jwt).getBody().getId();
    }

    public TokenVo getTokenVoFromJwt(String jwt) throws GlobalException{
        try {
            String[] splitStrArr = getUserNameFromJwt(jwt).split(JWT_SEPARATOR);
            if (splitStrArr.length == 2) {

                if(DalbitUtil.isEmpty(splitStrArr[0]) || DalbitUtil.isEmpty(splitStrArr[1])){
                    throw new GlobalException(ErrorStatus.토큰검증오류, "회원번호 or 로그인 여부가 null값 입니다.");
                }

                boolean isLogin = Boolean.valueOf(splitStrArr[1]);
                return new TokenVo(generateToken(splitStrArr[0], isLogin), splitStrArr[0], isLogin);
            }else{
                throw new GlobalException(ErrorStatus.토큰검증오류, "회원번호 or 로그인 여부가 없습니다.");
            }
        }catch (Exception e){
            throw new GlobalException(ErrorStatus.토큰검증오류, "이상한 토큰이 넘어왔어요.");
        }
    }

    /**
     * Jwt Token의 유효성을 체크한다.
     */
    public boolean validateToken(String jwt) throws GlobalException {
        return this.getClaims(jwt) != null;
    }

    private Jws<Claims> getClaims(String jwt) throws GlobalException {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throwGlobalException(ErrorStatus.토큰검증오류);

        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throwGlobalException(ErrorStatus.토큰검증오류);

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throwGlobalException(ErrorStatus.토큰만료오류);

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throwGlobalException(ErrorStatus.토큰검증오류);

        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throwGlobalException(ErrorStatus.토큰검증오류);
        }

        return null;
    }

    private void throwGlobalException(ErrorStatus errorStatus) throws GlobalException{
        if(request.getRequestURI().startsWith("/token")){
            HashMap<String, Object> result = commonService.getJwtTokenInfo(request);
            throw new GlobalException(errorStatus, result.get("tokenVo"));
        }
        throw new GlobalException(errorStatus);
    }
}
