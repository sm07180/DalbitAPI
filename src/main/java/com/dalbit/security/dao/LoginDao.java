package com.dalbit.security.dao;

import com.dalbit.member.vo.MemberVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LoginDao {
    MemberVo loginUseMemId(MemberVo memberVo);
    MemberVo loginUseMemNo(String userId);
}
