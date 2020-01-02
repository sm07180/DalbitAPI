package com.demo.security.filter;

import com.demo.common.vo.MemberVo;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.Base64Util;
import com.demo.util.CookieUtil;
import com.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 쿠키기반 sso 필터
 */
@Slf4j
@Component
public class SsoAuthenticationFilter implements Filter {

    @Autowired UserDetailsService userDetailsService;
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

    private final String[] IGNORE_URLS = {/*"/login", */"/logout"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if(!isIgnore(request)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                try {
                    CookieUtil cookieUtil = new CookieUtil(request);
                    boolean isTokenValid = false;

                    if (cookieUtil.exists(SSO_COOKIE_NAME)) {
                        String userId = jwtUtil.getUserNameFromJwt(cookieUtil.getValue(SSO_COOKIE_NAME));
                        log.debug("SsoAuthenticationFilter > JWT FROM ID : " + userId);

                        boolean jwtTokenIsValid = jwtUtil.validateToken(cookieUtil.getValue(SSO_COOKIE_NAME));
                        String newJwtToken = "";
                        if(jwtTokenIsValid){
                            newJwtToken= jwtUtil.generateToken(userId);
                            isTokenValid = true;

                            // Inquiry member information
                            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                            if (userDetails != null) {
                                SecurityUserVo securityUserVo = (SecurityUserVo) userDetails;
                                MemberVo memberVo = securityUserVo.getUserInfo();

                                // Verify SSO token value
                                if (memberVo.getMemId().equals(userId)) {
                                    authentication = new UsernamePasswordAuthenticationToken(securityUserVo, securityUserVo.getPassword(), userDetails.getAuthorities());

                                    SecurityContext securityContext = SecurityContextHolder.getContext();
                                    securityContext.setAuthentication(authentication);

                                    HttpSession session = request.getSession(true);
                                    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

                                }
                            }
                        }

                        // SSO Cookie update
                        Cookie ssoCookie = cookieUtil.createCookie(SSO_COOKIE_NAME, newJwtToken, SSO_DOMAIN, "/", isTokenValid ? SSO_COOKIE_MAX_AGE : 0); // 60 * 60 * 24 * 30 = 30days
                        HttpServletResponse response = (HttpServletResponse)servletResponse;
                        response.addCookie(ssoCookie);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
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
