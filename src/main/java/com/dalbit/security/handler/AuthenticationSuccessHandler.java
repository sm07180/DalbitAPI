package com.dalbit.security.handler;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.CookieUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.MessageUtil;
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

    @Autowired MessageUtil messageUtil;
    @Autowired JwtUtil jwtUtil;
    @Autowired GsonUtil gsonUtil;


    @Value("${sso.domain}")
    private String SSO_DOMAIN;
    @Value("${sso.cookie.name}")
    private String SSO_COOKIE_NAME;
    @Value("${sso.member.id.key}")
    private String SSO_MEMBER_ID_KEY;
    @Value("${sso.member.token.key}")
    private String SSO_MEMBER_TOKEN_NAME;
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

        saveSsoCookie(response);

        HashMap resultJsonData = new HashMap();
        resultJsonData.put("authToken", jwtUtil.generateToken(MemberVo.getUserInfo().getMemNo()));

        gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그인성공, resultJsonData));
    }

    /**
     * SSO 쿠키 저장
     * @param response
     * @throws IOException
     */
    public void saveSsoCookie(HttpServletResponse response) throws IOException {
        //SecurityUserVo loginUser = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cookie ssoCookie = CookieUtil.createCookie(SSO_COOKIE_NAME, jwtUtil.generateToken(MemberVo.getUserInfo().getMemNo()), SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE); // 60 * 60 * 24 * 30 = 30days
        response.addCookie(ssoCookie);
    }
}
