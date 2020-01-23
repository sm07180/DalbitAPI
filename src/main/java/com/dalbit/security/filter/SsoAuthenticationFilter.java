package com.dalbit.security.filter;

import com.dalbit.member.vo.TokenVo;
import com.dalbit.security.service.UserDetailsServiceImpl;
import com.dalbit.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 쿠키기반 sso 필터
 */
@Slf4j
@Component
public class SsoAuthenticationFilter implements Filter {

    @Autowired UserDetailsServiceImpl userDetailsService;
    @Autowired Base64Util base64Util;
    @Autowired JwtUtil jwtUtil;
    @Autowired RedisUtil redisUtil;
    @Autowired LoginUtil loginUtil;

    @Value("${sso.cookie.name}")
    private String SSO_COOKIE_NAME;
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

                        String headerCookie = request.getHeader(SSO_HEADER_COOKIE_NAME);

                        boolean isJwtTokenAvailable = jwtUtil.validateToken(headerCookie);
                        if(isJwtTokenAvailable){

                            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(headerCookie);
                            log.debug("SsoAuthenticationFilter get request header > JWT FROM TokenVo : {}", tokenVo.toString());

                            UserDetails userDetails = null;
                            if(redisUtil.isExistLoginSession(tokenVo.getMemNo())){
                                userDetails = userDetailsService.loadUserBySsoCookieFromRedis(tokenVo.getMemNo());
                            }

                            if(DalbitUtil.isEmpty(userDetails)){
                                userDetails = userDetailsService.loadUserBySsoCookieFromDb(tokenVo.getMemNo());
                            }

                            loginUtil.saveSecuritySession(request, userDetails);
                            loginUtil.ssoCookieUpdateFromRequestHeader(request, response, isJwtTokenAvailable);
                        }
                    }else if (cookieUtil.exists(SSO_COOKIE_NAME)) {
                        boolean isJwtTokenAvailable = jwtUtil.validateToken(cookieUtil.getValue(SSO_COOKIE_NAME));
                        if(isJwtTokenAvailable){

                            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(cookieUtil.getValue(SSO_COOKIE_NAME));
                            log.debug("SsoAuthenticationFilter > JWT FROM TokenVo : {}", tokenVo.toString());

                            loginUtil.saveSecuritySession(request, userDetailsService.loadUserBySsoCookieFromDb(tokenVo.getMemNo()));
                        }

                        loginUtil.ssoCookieUpdate(request, response, isJwtTokenAvailable);

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
}
