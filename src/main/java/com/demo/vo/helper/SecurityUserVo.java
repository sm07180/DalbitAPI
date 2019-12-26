package com.demo.vo.helper;

import com.demo.vo.UserVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * spring security 사용자를 보관할 VO
 */
public class SecurityUserVo extends User {

    private UserVo userVo;

    public SecurityUserVo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityUserVo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserVo getUserInfo() {
        return userVo;
    }

    public void setUserInfo(UserVo userVo) {
        this.userVo = userVo;
    }
}
