package com.demo.security.handler;

import com.demo.common.code.Status;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.CookieUtil;
import com.demo.util.JwtUtil;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import com.demo.common.vo.JsonOutputVo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@Component("authSuccessHandler")
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired MessageUtil messageUtil;
    @Autowired JwtUtil jwtUtil;


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

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

         SecurityUserVo loginUser = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //String ssoToken = SSO_MEMBER_ID_KEY + "=" + loginUser.getUsername() + "&" + SSO_MEMBER_TOKEN_NAME + "=" + loginUser.getUserInfo().getUserToken();
        String ssoToken = SSO_MEMBER_ID_KEY + "=" + loginUser.getUsername() + "&" + SSO_MEMBER_TOKEN_NAME + "=" + loginUser.getUserInfo().getMemId();

        /*
        ssoToken = new String(java.util.Base64.getEncoder().encode(ssoToken.getBytes()));

        Cookie ssoCookie = CookieUtil.createCookie(SSO_COOKIE_NAME, ssoToken, SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE); // 60 * 60 * 24 * 30 = 30days
        response.addCookie(ssoCookie);
        */


        // SSO Cookie update
        Cookie ssoCookie = CookieUtil.createCookie(SSO_COOKIE_NAME, jwtUtil.generateToken(loginUser.getUsername()), SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE); // 60 * 60 * 24 * 30 = 30days
        //HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.addCookie(ssoCookie);

        boolean isEmptyRedirectUrl = StringUtil.isEmpty(request.getParameter("redirectUrl"));
        HashMap map = new HashMap();
        map.put("returnUrl", isEmptyRedirectUrl ? "/sample" : request.getParameter("redirectUrl"));

        loginResult(response, new JsonOutputVo(Status.로그인, map));
    }

    /**
     * 로그인 결과 리턴
     * @param response
     * @param jsonOutputVo
     * @throws IOException
     */
    public void loginResult(HttpServletResponse response, JsonOutputVo jsonOutputVo) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(messageUtil.setJsonOutputVo(jsonOutputVo)));
        out.flush();
        out.close();
    }
}
