package com.demo.security.service;

import com.demo.common.code.Status;
import com.demo.common.vo.ProcedureVo;
import com.demo.exception.CustomUsernameNotFoundException;
import com.demo.member.service.MemberService;
import com.demo.member.vo.MemberVo;
import com.demo.member.vo.P_LoginVo;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.DalbitUtil;
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
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@Component("authProvider")
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    HttpServletRequest request;

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
                throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
            }

            MemberVo memberVo = securityUserVo.getMemberVo();
            P_LoginVo pLoginVo = P_LoginVo.builder()
                .memSlct(DalbitUtil.convertRequestParamToString(request,"s_mem"))
                .id(userName)
                .pw(password)
                .os(DalbitUtil.convertRequestParamToInteger(request,"i_os"))
                .deviceUuid(DalbitUtil.convertRequestParamToString(request,"s_deviceId"))
                .deviceToken(DalbitUtil.convertRequestParamToString(request,"s_deviceToken"))
                .appVersion(DalbitUtil.convertRequestParamToString(request,"s_appVer"))
                //광고 아이디 없음..
                .build();

            ProcedureVo procedureVo = memberService.callMemberLogin(pLoginVo);
            log.debug("로그인 결과 : {}", new Gson().toJson(procedureVo));

            if(procedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
                throw new CustomUsernameNotFoundException(Status.로그인실패_회원가입필요);

            }else if(procedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
                throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);

            }else if(procedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
                throw new CustomUsernameNotFoundException(Status.로그인실패_파라메터이상.getMessageKey());
            }
        }
        HashMap map = new HashMap();
        map.put("memberInfo", securityUserVo.getMemberJsonInfo());
        return new UsernamePasswordAuthenticationToken(map, password, securityUserVo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
