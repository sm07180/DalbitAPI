package com.dalbit.security.service;

import com.dalbit.common.code.Status;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.security.dao.LoginDao;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.DalbitUtil;
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
    HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberVo paramMemberVo = new MemberVo();
        paramMemberVo.setMem_id(username);
        paramMemberVo.setMem_slct(DalbitUtil.convertRequestParamToString(request, "s_mem"));

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