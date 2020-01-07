package com.demo.security.dao;

import com.demo.member.vo.MemberVo;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao {
    MemberVo login(String userId);
}
