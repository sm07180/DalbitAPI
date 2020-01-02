package com.demo.security.service;

import com.demo.member.service.MemberService;
import com.demo.security.vo.SecurityUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component("authProvider")
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (String)authentication.getCredentials();
        SecurityUserVo securityUserVo = null;
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if(userDetails != null) {
            securityUserVo = (SecurityUserVo)userDetails;
            
            if(securityUserVo == null) {
            	log.debug(securityUserVo.getUsername());
                throw new UsernameNotFoundException("아이디와 비밀번호를 확인하고 다시 로그인해 주세요.");
            }

            /*
            //passwordEncoder.encode(password);
            if (!passwordEncoder.matches(password, securityUserVo.getPassword())) {
            	log.debug(securityUserVo.getPassword());
                throw new UsernameNotFoundException("아이디와 비밀번호를 확인하고 다시 로그인해 주세요.");
            }
            */
            //memberService.callMemberLogin(new LoginVo(userName, password));
            memberService.callMemberLogin(null);
        }
        return new UsernamePasswordAuthenticationToken(securityUserVo, password, securityUserVo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
