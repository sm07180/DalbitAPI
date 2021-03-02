package com.dalbit.util;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.security.vo.SecurityUserVo;
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

    public void saveSecuritySession(HttpServletRequest request, UserDetails userDetails){
        if (!DalbitUtil.isEmpty(userDetails)) {
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
                //session.setAttribute(DalbitUtil.getProperty("spring.security.session.name"), securityContext);
                //session.setAttribute(DalbitUtil.getProperty("spring.session.memberInfo.key"), securityUserVo.getMemberVo());
            }
        }
    }
}
