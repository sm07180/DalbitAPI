package com.demo.security.vo;

import com.demo.common.vo.MemberVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * spring security 사용자를 보관할 VO
 */
public class SecurityUserVo extends User {

    private MemberVo memberVo;

    public SecurityUserVo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityUserVo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public MemberVo getUserInfo() {
        return memberVo;
    }

    public void setUserInfo(MemberVo memberVo) {
        this.memberVo = memberVo;
    }
}
