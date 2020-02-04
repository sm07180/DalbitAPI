package com.dalbit.security.filter;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.P_ProfileInfoVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.security.service.UserDetailsServiceImpl;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
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
    @Autowired ProfileService profileService;
    @Autowired MemberService memberService;
    @Autowired Base64Util base64Util;
    @Autowired JwtUtil jwtUtil;
    @Autowired RedisUtil redisUtil;
    @Autowired LoginUtil loginUtil;
    @Autowired GsonUtil gsonUtil;

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
        response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,authToken,custom-header,redirectUrl");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if(!isIgnore(request)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                try {

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
                                if(tokenVo.isLogin()){
                                    ProcedureVo profileProcedureVo = profileService.getProfile(new P_ProfileInfoVo(1, tokenVo.getMemNo()));

                                    MemberVo memberVo = null;
                                    if(profileProcedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {

                                        P_ProfileInfoVo profileInfo = new Gson().fromJson(profileProcedureVo.getExt(), P_ProfileInfoVo.class);
                                        memberVo = new MemberVo(new ProfileInfoOutVo(profileInfo, tokenVo.getMemNo()));
                                        //비밀번호가 없어서 공백으로 넣어둠.
                                        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), "", DalbitUtil.getAuthorities());
                                        securityUserVo.setMemberVo(memberVo);

                                        userDetails = securityUserVo;
                                    }else{
                                        new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
                                    }
                                }else{

                                    MemberVo memberVo = new MemberVo();
                                    memberVo.setMemId(tokenVo.getMemNo());
                                    memberVo.setMemNo(tokenVo.getMemNo());
                                    memberVo.setMemPasswd("");

                                    SecurityUserVo securityUserVo = new SecurityUserVo(tokenVo.getMemNo(), "", DalbitUtil.getGuestAuthorities());
                                    securityUserVo.setMemberVo(memberVo);

                                    memberService.refreshAnonymousSecuritySession(tokenVo.getMemNo());
                                    userDetails = securityUserVo;
                                }
                            }

                            loginUtil.saveSecuritySession(request, userDetails);
                            loginUtil.ssoCookieUpdateFromRequestHeader(request, response, isJwtTokenAvailable);
                        }
                    }

                } catch (GlobalException e) {
                    e.printStackTrace();

                    gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(e.getErrorStatus()));
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
