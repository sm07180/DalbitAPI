package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.P_FanboardListVo;
import com.dalbit.member.vo.P_FanboardReplyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileDao {

    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardAdd(ProcedureVo procedureVo);
    List<P_FanboardListVo> callMemberFanboardList(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardDelete(ProcedureVo procedureVo);
    List<P_FanboardReplyVo> callMemberFanboardReply(ProcedureVo procedureVo);

}
