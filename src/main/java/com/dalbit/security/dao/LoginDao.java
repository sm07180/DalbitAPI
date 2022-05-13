package com.dalbit.security.dao;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.security.vo.MemberReportInfoVo;
import com.dalbit.security.vo.P_ListeningRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginDao {
    MemberVo loginUseMemId(MemberVo memberVo);

    MemberVo loginUseMemNo(String userId);

    // @Transactional(readOnly = true)
    MemberReportInfoVo selectReportData(String mem_no);

    // @Transactional(readOnly = true)
    List<String> selectListeningRoom(P_ListeningRoom pListeningRoom);
}
