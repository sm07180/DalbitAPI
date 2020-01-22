package com.dalbit.security.filter;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.security.service.UserDetailsServiceImpl;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.Base64Util;
import com.dalbit.util.CookieUtil;
import com.dalbit.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

/**
 * 쿠키기반 sso 필터
 */
@Slf4j
@Component
public class SsoAuthenticationFilter implements Filter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired Base64Util base64Util;
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
    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    private final String[] IGNORE_URLS = {/*"/login", */"/logout"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
        response.setHeader("Access-Control-Allow-Origin", "*");

        if(!isIgnore(request)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                try {
                    CookieUtil cookieUtil = new CookieUtil(request);

                    if(request.getHeader(SSO_HEADER_COOKIE_NAME) != null){

                        String headerCookie = request.getHeader("sso_cookie");

                        boolean isJwtTokenAvailable = jwtUtil.validateToken(headerCookie);
                        if(isJwtTokenAvailable){

                            String userId = jwtUtil.getUserNameFromJwt(headerCookie);
                            log.debug("SsoAuthenticationFilter get request header > JWT FROM ID : " + userId);

                            saveSecuritySession(request, userDetailsService.loadUserBySsoCookie(userId));
                            ssoCookieUpdateFromRequestHeader(request, response, isJwtTokenAvailable);
                        }
                    }else if (cookieUtil.exists(SSO_COOKIE_NAME)) {
                        boolean isJwtTokenAvailable = jwtUtil.validateToken(cookieUtil.getValue(SSO_COOKIE_NAME));
                        if(isJwtTokenAvailable){

                            String userId = jwtUtil.getUserNameFromJwt(cookieUtil.getValue(SSO_COOKIE_NAME));
                            log.debug("SsoAuthenticationFilter > JWT FROM ID : " + userId);

                            saveSecuritySession(request, userDetailsService.loadUserBySsoCookie(userId));
                        }

                        ssoCookieUpdate(request, response, isJwtTokenAvailable);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean isIgnore(HttpServletRequest request) {
        /*if(request.getSession().getAttribute("logout") != null) {
            request.getSession().removeAttribute("logout");
            return true;
        }*/

        String uri = request.getRequestURI();

        boolean result = false;
        for(String ignoreUri : IGNORE_URLS) {
            if(uri.startsWith(ignoreUri)) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * spring security에 session 저장
     */
    private void saveSecuritySession(HttpServletRequest request, UserDetails userDetails){
        if (userDetails != null) {
            SecurityUserVo securityUserVo = (SecurityUserVo) userDetails;
            MemberVo memberVo = securityUserVo.getMemberVo();

            // Verify SSO token value
            if (memberVo.getMemId().equals(securityUserVo.getUsername())) {
                HashMap map = new HashMap();
                map.put("memberInfo", securityUserVo.getMemberJsonInfo());
                Authentication authentication = new UsernamePasswordAuthenticationToken(memberVo.getMemNo(), securityUserVo.getPassword(), userDetails.getAuthorities());

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);

                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            }
        }
    }

    /**
     * sso cookie 갱신
     */
    public void ssoCookieUpdate(HttpServletRequest request, HttpServletResponse response, boolean isJwtTokenAvailable){
        try {
            Cookie ssoCookie;
            if(!isJwtTokenAvailable){
                ssoCookie = CookieUtil.deleteCookie(SSO_COOKIE_NAME, SSO_DOMAIN, "/", 0);

            }else{
                CookieUtil cookieUtil = new CookieUtil(request);

                String memNo = jwtUtil.getUserNameFromJwt(cookieUtil.getValue(SSO_COOKIE_NAME));
                String jwtToken = jwtUtil.generateToken(memNo);

                ssoCookie = CookieUtil.createCookie(SSO_COOKIE_NAME, jwtToken, SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE); // 60 * 60 * 24 * 30 = 30days
            }
            response.addCookie(ssoCookie);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sso cookie 갱신
     */
    public void ssoCookieUpdateFromRequestHeader(HttpServletRequest request, HttpServletResponse response, boolean isJwtTokenAvailable){
        try {
            Cookie ssoCookie;
            if(!isJwtTokenAvailable){
                ssoCookie = CookieUtil.deleteCookie(SSO_COOKIE_NAME, SSO_DOMAIN, "/", 0);

            }else{
                String cookieValue = request.getHeader(SSO_HEADER_COOKIE_NAME);
                String userId = jwtUtil.getUserNameFromJwt(cookieValue);
                String jwtToken = jwtUtil.generateToken(userId);

                ssoCookie = CookieUtil.createCookie(SSO_COOKIE_NAME, jwtToken, SSO_DOMAIN, "/", SSO_COOKIE_MAX_AGE); // 60 * 60 * 24 * 30 = 30days
            }
            response.addCookie(ssoCookie);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
