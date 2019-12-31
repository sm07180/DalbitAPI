package com.demo.security.dao;

import com.demo.common.vo.UserVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao {
    UserVo login(String userId);
}
