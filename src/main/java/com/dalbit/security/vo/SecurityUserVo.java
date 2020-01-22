package com.dalbit.security.vo;

import com.dalbit.member.vo.MemberVo;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * spring security 사용자를 보관할 VO
 */
@Getter
@Setter
public class SecurityUserVo extends User {

    private static final long serialVersionUID = 1L;

    private MemberVo memberVo;

    public SecurityUserVo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityUserVo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
