package com.demo.security.handler;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.CookieUtil;
import com.demo.util.JwtUtil;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
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

        boolean isEmptyRedirectUrl = StringUtil.isEmpty(request.getParameter("redirectUrl"));
        HashMap resultJsonData = new HashMap();
        resultJsonData.put("returnUrl", isEmptyRedirectUrl ? "/sample" : request.getParameter("redirectUrl"));

        returnLoginResult(response, new JsonOutputVo(Status.로그인, resultJsonData));
    }

    /**
     * SSO 쿠키 저장
     * @param response
     * @throws IOException
     */
    public void saveSsoCookie(HttpServletResponse response) throws IOException {
        SecurityUserVo loginUser = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cookie ssoCookie = CookieUtil.createCookie(SSO_COOKIE_NAME, jwtUtil.generateToken(loginUser.getUsername()), SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE); // 60 * 60 * 24 * 30 = 30days
        response.addCookie(ssoCookie);
    }

    /**
     * 로그인 결과 리턴
     * @param response
     * @param jsonOutputVo
     * @throws IOException
     */
    public void returnLoginResult(HttpServletResponse response, JsonOutputVo jsonOutputVo) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(messageUtil.setJsonOutputVo(jsonOutputVo)));
        out.flush();
        out.close();
    }
}
