package com.demo.security.service;

import com.demo.common.vo.MemberVo;
import com.demo.security.dao.LoginDao;
import com.demo.security.vo.SecurityUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginDao loginDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	MemberVo memberVo = loginDao.login(username);

        if(memberVo == null) {
            throw new UsernameNotFoundException("아이디와 비밀번호를 확인하신 후 다시 로그인해 주세요.");
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

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemPasswd(), authorities);
        securityUserVo.setUserInfo(memberVo);

        return securityUserVo;
    }
}
