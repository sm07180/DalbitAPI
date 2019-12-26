package com.demo.dao;

import com.demo.vo.UserVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String prefix = "login.";

    public UserVo login(String userId){
        return (UserVo)this.sqlSession.selectOne(prefix + "login", userId);
    }
}
