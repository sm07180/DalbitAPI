package com.dalbit.security.filter;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.security.service.UserDetailsServiceImpl;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
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
    //@Autowired RedisUtil redisUtil;
    @Autowired LoginUtil loginUtil;
    @Autowired GsonUtil gsonUtil;

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    private final String[] IGNORE_URLS = {
        "/favicon.ico"
        , "/login", "/logout", "/splash"
        , "/member/login", "/member/logout", "/token"
        , "/sample", "/rest/sample"
        , "/postman/dalbitcast.json"
        , "/ctrl/check/service"
        , "/social"
        , "/self"
        , "/error/log"
        , "/socket"
        , "/paycall/store"
        , "/items"
        , "/api-tester"
        , "/broad/vw/broadcast/hook"
        , "/broad/share", "/clip/share"
        , "/broad/moon/check"
        , "/socket/dbCheck/bySocket"
        , "/tnk/aos/callback", "/tnk/ios/callback"
        , "/mailbox/chat/openTok"
        , "/rest/sample/session/test"
        , "/broad/tts"
        , "/broad/tts/callback"
        , "/security/log"
        , "/pay/result/callback"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        DalbitUtil.setHeader(request, response);

        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            if(!isIgnore(request)) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String headerAuthToken = request.getHeader(SSO_HEADER_COOKIE_NAME);
                try {
                    if (DalbitUtil.isEmpty(authentication)) {
                        if(DalbitUtil.isEmptyHeaderAuthToken(headerAuthToken)){
                            if(!request.getRequestURI().startsWith("/token")){
                                throw new GlobalException(ErrorStatus.토큰검증오류, Thread.currentThread().getStackTrace()[1].getMethodName());
                            }
                        }else{
                            checkToken(request, response);
                        }
                    }else{
                        log.debug("=================================  토큰 컨텍스트에서 통과 정보  ============================================");
                        log.debug(authentication.getPrincipal() + " : " +authentication.toString());
                        log.debug(headerAuthToken);
                        log.debug("=========================================================================================================");

                        if(DalbitUtil.isAnonymousUser(authentication.getPrincipal())){
                            if(DalbitUtil.isEmptyHeaderAuthToken(headerAuthToken)){
                                if(!request.getRequestURI().startsWith("/token")) {
                                    throw new GlobalException(ErrorStatus.토큰검증오류, Thread.currentThread().getStackTrace()[1].getMethodName());
                                }
                            }else{
                                checkToken(request, response);
                            }
                        }
                    }
                } catch (GlobalException e) {
                    //e.printStackTrace();
                    log.info(e.getMessage());

                    gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(e.getErrorStatus(), e.getData(), e.getMethodName()));
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public void checkToken(HttpServletRequest request, HttpServletResponse response) throws GlobalException{
        String headerAuthToken = request.getHeader(SSO_HEADER_COOKIE_NAME);

        boolean isJwtTokenAvailable = jwtUtil.validateToken(headerAuthToken);

        if(isJwtTokenAvailable){

            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(headerAuthToken);
            if(DalbitUtil.isEmpty(tokenVo)){
                throw new GlobalException(ErrorStatus.토큰검증오류, Thread.currentThread().getStackTrace()[1].getMethodName());
            }else{
                log.debug("SsoAuthenticationFilter get request header > JWT FROM TokenVo : {}", tokenVo.toString());
            }

            UserDetails userDetails = null;
            /*if(redisUtil.isExistLoginSession(tokenVo.getMemNo())){
                userDetails = userDetailsService.loadUserBySsoCookieFromRedis(tokenVo.getMemNo());
                if(!DalbitUtil.isEmpty(userDetails)){
                    log.debug("{}로 REDIS 조회 : {}", tokenVo.getMemNo(), userDetails.toString());
                }
            }*/

            //if(DalbitUtil.isEmpty(userDetails)) {
            if (tokenVo.getMemNo() == null) {
                throw new GlobalException(ErrorStatus.토큰검증오류, Thread.currentThread().getStackTrace()[1].getMethodName());
            } else {
                if (tokenVo.isLogin()) {
                    ProcedureVo profileProcedureVo = profileService.getProfile(new P_ProfileInfoVo(1, tokenVo.getMemNo()));

                    MemberVo memberVo = null;
                    if (profileProcedureVo.getRet().equals(MemberStatus.회원정보보기_성공.getMessageCode())) {

                        P_ProfileInfoVo profileInfo = new Gson().fromJson(profileProcedureVo.getExt(), P_ProfileInfoVo.class);
                        memberVo = new MemberVo(new ProfileInfoOutVo(profileInfo, tokenVo.getMemNo(), tokenVo.getMemNo(), null));
                        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemId(), DalbitUtil.getAuthorities());
                        securityUserVo.setMemberVo(memberVo);

                        userDetails = securityUserVo;
                    } else {
                        new CustomUsernameNotFoundException(MemberStatus.로그인실패_패스워드틀림);
                    }
                } else {
                    MemberVo memberVo = new MemberVo();
                    memberVo.setMemId(tokenVo.getMemNo());
                    memberVo.setMemNo(tokenVo.getMemNo());
                    memberVo.setMemPasswd("");

                    SecurityUserVo securityUserVo = new SecurityUserVo(tokenVo.getMemNo(), tokenVo.getMemNo(), DalbitUtil.getGuestAuthorities());
                    securityUserVo.setMemberVo(memberVo);

                    memberService.refreshAnonymousSecuritySession(tokenVo.getMemNo());
                    userDetails = securityUserVo;
                }
            }
            //}

            loginUtil.saveSecuritySession(request, userDetails);
            //loginUtil.ssoCookieUpdateFromRequestHeader(request, response);
        }
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
