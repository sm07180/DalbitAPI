package com.demo.security.service;

import com.demo.common.code.ProcedureStatus;
import com.demo.common.code.Status;
import com.demo.common.vo.MemberVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.member.service.MemberService;
import com.demo.member.vo.LoginVo;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
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

    @Autowired
    private MessageUtil messageUtil;

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
                throw new UsernameNotFoundException(messageUtil.get(Status.로그인실패.getMessageKey()));
            }

            MemberVo memberVo = securityUserVo.getUserInfo();
            LoginVo loginVo = LoginVo.builder()
                .memSlct(memberVo.getMemSlct())
                .id(userName)
                .pw(password)
                .os("1")
                .deviceUuid("123456")
                .deviceToken("3123123123")
                .appVersion("1.0.0.1")
                .build();

            ProcedureVo procedureVo = memberService.callMemberLogin(loginVo);
            log.debug("로그인 결과 : {}", new Gson().toJson(procedureVo));
            if(procedureVo.getRet() != ProcedureStatus.성공.getResultCode()){
                throw new UsernameNotFoundException(messageUtil.get(Status.로그인실패.getMessageKey()));
            }
        }
        return new UsernamePasswordAuthenticationToken(securityUserVo, password, securityUserVo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
