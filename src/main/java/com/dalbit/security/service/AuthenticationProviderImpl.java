package com.dalbit.security.service;

import com.dalbit.exception.GlobalException;
import com.dalbit.security.vo.SecurityUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Slf4j
@Component("authProvider")
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HttpSession httpSession;

    @Value("${spring.session.memberInfo.key}")
    String SPRING_SESSION_MEMBERINFO_KEY;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        //String userName = authentication.getName();
        //String password = (String)authentication.getCredentials();

        SecurityUserVo securityUserVo = (SecurityUserVo)userDetailsService.loadUserByUsername();

        httpSession.setAttribute(SPRING_SESSION_MEMBERINFO_KEY, securityUserVo.getMemberVo());

        return new UsernamePasswordAuthenticationToken(
                securityUserVo.getMemberVo().getMemNo()
                , securityUserVo.getMemberVo().getMemPasswd()
                , securityUserVo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
