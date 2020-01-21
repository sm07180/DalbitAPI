package com.dalbit.security.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.P_LoginVo;
import com.dalbit.security.dao.LoginDao;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //MemberVo memberVo;
        P_LoginVo pLoginVo = new P_LoginVo(
            DalbitUtil.convertRequestParamToString(request,"memType")
            , DalbitUtil.convertRequestParamToString(request,"memId")
            , DalbitUtil.convertRequestParamToString(request,"memPwd")
            , DalbitUtil.convertRequestParamToInteger(request,"os")
            , DalbitUtil.convertRequestParamToString(request,"deviceId")
            , DalbitUtil.convertRequestParamToString(request,"deviceToken")
            , DalbitUtil.convertRequestParamToString(request,"appVer")
            , DalbitUtil.convertRequestParamToString(request,"appAdId")
        );

        ProcedureVo procedureVo = memberService.callMemberLogin(pLoginVo);
        log.debug("로그인 결과 : {}", new Gson().toJson(procedureVo));

        if(procedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_회원가입필요);

        }else if(procedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);

        }else if(procedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_파라메터이상.getMessageKey());
        }

        MemberVo paramMemberVo = new MemberVo();
        paramMemberVo.setMem_id(DalbitUtil.convertRequestParamToString(request,"memId"));
        paramMemberVo.setMem_slct(DalbitUtil.convertRequestParamToString(request, "memType"));

        MemberVo memberVo = loginDao.loginUseMemId(paramMemberVo);
        if(memberVo == null) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMem_id(), memberVo.getMem_passwd(), authorities);
        securityUserVo.setMemberVo(memberVo);

        return securityUserVo;
    }


    public UserDetails loadUserBySsoCookie(String username) throws UsernameNotFoundException {

        MemberVo memberVo = loginDao.loginUseMemNo(username);

        if(memberVo == null) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMem_id(), memberVo.getMem_passwd(), authorities);
        securityUserVo.setMemberVo(memberVo);

        return securityUserVo;
    }
}
