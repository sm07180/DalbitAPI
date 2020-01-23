package com.dalbit.security.handler;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component("authSuccessHandler")
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired JwtUtil jwtUtil;
    @Autowired GsonUtil gsonUtil;

    @Value("${sso.domain}")
    private String SSO_DOMAIN;
    @Value("${sso.cookie.name}")
    private String SSO_COOKIE_NAME;
    @Value("${sso.cookie.max.age}")
    private int SSO_COOKIE_MAX_AGE;

    /**
     * 로그인 성공 시
     * @param request
     * @param response
     * @param authentication
     * @throws ServletException
     * @throws IOException
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        String jwtToken = jwtUtil.generateToken(MemberVo.getMyMemNo(), true);

        saveSsoCookie(response, jwtToken);

        gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그인성공, new TokenVo(jwtToken, MemberVo.getMyMemNo(), true)));
    }

    /**
     * SSO 쿠키 저장
     * @param response
     * @throws IOException
     */
    public void saveSsoCookie(HttpServletResponse response, String jwtToken) throws IOException {
        response.addCookie(CookieUtil.createCookie(SSO_COOKIE_NAME, jwtToken, SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE)); // 60 * 60 * 24 * 30 = 30days
    }
}
