package com.dalbit.security.dao;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.security.vo.MemberReportInfoVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LoginDao {
    MemberVo loginUseMemId(MemberVo memberVo);
    MemberVo loginUseMemNo(String userId);

    @Transactional(readOnly = true)
    MemberReportInfoVo selectReportData(String mem_no);
}
