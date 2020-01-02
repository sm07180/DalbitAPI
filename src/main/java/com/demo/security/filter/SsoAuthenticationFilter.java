package com.demo.security.filter;

import com.demo.common.vo.MemberVo;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.Base64Util;
import com.demo.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 쿠키기반 sso 필터
 */
@Slf4j
@Component
public class SsoAuthenticationFilter implements Filter {

    @Autowired UserDetailsService userDetailsService;
    @Autowired Base64Util base64Util;

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
                        String ssoToken = cookieUtil.getValue(SSO_COOKIE_NAME);
                        ssoToken = new String(base64Util.decode(ssoToken));

                        ssoToken = ssoToken.replace(SSO_MEMBER_ID_KEY + "=", "").replace(SSO_MEMBER_TOKEN_NAME + "=", "");
                        String[] ssoInfo = ssoToken.split("&");

                        // Inquiry member information
                        UserDetails userDetails = userDetailsService.loadUserByUsername(ssoInfo[0]);
                        if (userDetails != null) {
                            SecurityUserVo securityUserVo = (SecurityUserVo) userDetails;
                            MemberVo memberVo = securityUserVo.getUserInfo();

                            isTokenValid = true;

                            // Verify SSO token value
                            /*if (memberVo.getMem_id().equals(ssoInfo[1])) {
                                authentication = new UsernamePasswordAuthenticationToken(securityUserVo, securityUserVo.getPassword(), userDetails.getAuthorities());

                                SecurityContext securityContext = SecurityContextHolder.getContext();
                                securityContext.setAuthentication(authentication);

                                HttpSession session = request.getSession(true);
                                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

                            }*/

                            // SSO Cookie update
                            Cookie ssoCookie = cookieUtil.createCookie(SSO_COOKIE_NAME, cookieUtil.getValue(SSO_COOKIE_NAME), SSO_DOMAIN, "/", isTokenValid ? SSO_COOKIE_MAX_AGE : 0); // 60 * 60 * 24 * 30 = 30days
                            HttpServletResponse response = (HttpServletResponse)servletResponse;
                            response.addCookie(ssoCookie);
                        }
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
