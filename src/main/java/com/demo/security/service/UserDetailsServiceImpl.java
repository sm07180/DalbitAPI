package com.demo.security.service;

import com.demo.common.code.Status;
import com.demo.exception.CustomUsernameNotFoundException;
import com.demo.member.vo.MemberVo;
import com.demo.security.dao.LoginDao;
import com.demo.security.vo.SecurityUserVo;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
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

        MemberVo memberVo;
        String s_mem = StringUtil.convertRequestParamToString(request, "s_mem");

        if(s_mem.equals("")){
            //쿠키를 이용한 로그인 처리 시 사용
            memberVo = loginDao.loginUseMemNo(username);
        }else{
            MemberVo paramMemberVo = new MemberVo();
            paramMemberVo.setMem_id(username);
            paramMemberVo.setMem_slct(s_mem);

            memberVo = loginDao.loginUseMemId(paramMemberVo);
        }

        if(memberVo == null) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
        }
        /*//직책이 있는 사용자의 경우 MANAGER 등급 부여
        if(!"Y".equals(userInfo.getCareerauth()) && userInfo.getDuty().length() > 0){
        	userInfo.setCareerauth("Y");
        	userInfo.setCareerGrade("MANAGER");
        }

        if(!"Y".equals(userInfo.getCareerauth())){
        	throw new UsernameNotFoundException("접속권한이 없습니다.");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if("Y".equals(userInfo.getRegularetype()) && "ABGZ".indexOf(userInfo.getGrade())>=0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            switch(userInfo.getCareerGrade()) {
                case "ROOT": {
                    authorities.add(new SimpleGrantedAuthority("ROLE_MASTER"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_DIRECTOR"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_ROOT"));
                    break;
                }
                case "DIRECTOR": {
                    authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_DIRECTOR"));
                    break;
                }
                case "MANAGER": {
                    authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                    break;
                }
            }

            if(userInfo.getDepartment().equals("GZ")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_MASTER"));
            }

        }else{
            throw new UsernameNotFoundException("아이디와 비밀번호를 확인하신 후 다시 로그인해 주세요.3");
        }*/

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMem_id(), memberVo.getMem_passwd(), authorities);
        securityUserVo.setUserInfo(memberVo);

        return securityUserVo;
    }
}
