package com.dalbit.util;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.security.vo.SecurityUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginUtil {

    @Autowired JwtUtil jwtUtil;

    @Value("${spring.session.memberInfo.key}")
    String SPRING_SESSION_MEMBERINFO_KEY;

    public void saveSecuritySession(HttpServletRequest request, UserDetails userDetails){
        if (userDetails != null) {
            SecurityUserVo securityUserVo = (SecurityUserVo) userDetails;
            MemberVo memberVo = securityUserVo.getMemberVo();

            // Verify SSO token value
            if (memberVo.getMemId().equals(securityUserVo.getUsername())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        securityUserVo.getMemberVo().getMemNo()
                        , securityUserVo.getMemberVo().getMemPasswd()
                        , userDetails.getAuthorities());

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);

                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                session.setAttribute(SPRING_SESSION_MEMBERINFO_KEY, securityUserVo.getMemberVo());
            }
        }
    }

    /*public void ssoCookieRenerate(HttpServletResponse response, String jwtToken) throws GlobalException{
        Cookie ssoCookie = makeSsoCookie(jwtToken);
        response.addCookie(ssoCookie);
    }*/

    /*public void ssoCookieUpdateFromRequestHeader(HttpServletRequest request, HttpServletResponse response) throws GlobalException{
        String headerAuthToken = request.getHeader(DalbitUtil.getProperty("sso.header.cookie.name"));
        TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(headerAuthToken);
        if(DalbitUtil.isEmpty(tokenVo)){
            throw new GlobalException(ErrorStatus.토큰검증오류);
        }

        String jwtToken = jwtUtil.generateToken(tokenVo.getMemNo(), true);

        Cookie ssoCookie = makeSsoCookie(jwtToken);

        response.addCookie(ssoCookie);
    }*/

    /*public void ssoCookieUpdate(HttpServletRequest request, HttpServletResponse response, boolean isJwtTokenAvailable) throws GlobalException{
        try {
            Cookie ssoCookie;
            if(!isJwtTokenAvailable){
                ssoCookie = expireSsoCookie();

            }else{
                CookieUtil cookieUtil = new CookieUtil(request);

                TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(cookieUtil.getValue(DalbitUtil.getProperty("sso.cookie.name")));
                if(DalbitUtil.isEmpty(tokenVo)){
                    throw new GlobalException(ErrorStatus.토큰검증오류);
                }

                String jwtToken = jwtUtil.generateToken(tokenVo.getMemNo(), tokenVo.isLogin());

                ssoCookie = makeSsoCookie(jwtToken);
            }
            response.addCookie(ssoCookie);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ErrorStatus.쿠키갱신오류);
        }
    }*/

    /*public Cookie makeSsoCookie(String jwtToken) throws GlobalException{
        try{
            return CookieUtil.createCookie(
                    DalbitUtil.getProperty("sso.cookie.name")
                    , jwtToken
                    , DalbitUtil.getProperty("sso.domain")
                    , "/"
                    , Integer.valueOf(DalbitUtil.getProperty("sso.cookie.max.age"))); // 60 * 60 * 24 * 30 = 30days
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(ErrorStatus.쿠키생성오류);
        }
    }*/

    /*public Cookie expireSsoCookie() throws GlobalException{
        try{
            return CookieUtil.deleteCookie(DalbitUtil.getProperty("sso.cookie.name"), DalbitUtil.getProperty("sso.domain"), "/", 0);
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(ErrorStatus.쿠키만료오류);
        }
    }*/


}
