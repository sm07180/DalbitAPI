package com.dalbit.util;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.security.vo.SecurityUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class LoginUtil {

    @Autowired JwtUtil jwtUtil;

    @Value("${spring.session.memberInfo.key}")
    String SPRING_SESSION_MEMBERINFO_KEY;
    @Value("${spring.security.session.name}")
    String SPRING_SECURITY_SESSION_NAME;

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
                session.setAttribute(SPRING_SECURITY_SESSION_NAME, securityContext);
                session.setAttribute(SPRING_SESSION_MEMBERINFO_KEY, securityUserVo.getMemberVo());
            }
        }
    }
}
