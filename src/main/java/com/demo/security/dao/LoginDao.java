package com.demo.security.dao;

import com.demo.common.vo.MemberVo;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao {
    MemberVo login(String userId);
}
