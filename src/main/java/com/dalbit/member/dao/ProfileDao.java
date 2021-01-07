package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProfileDao {

    @Transactional(readOnly = true)
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardAdd(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_FanboardListVo> callMemberFanboardList(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardDelete(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_FanboardReplyVo> callMemberFanboardReply(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_FanRankingVo> callMemberFanRanking(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    ProcedureVo callMemberLevelUpCheck(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_StarRankingVo> selectStarRanking(P_StarRankingVo procedureVo);

    @Transactional(readOnly = true)
    List<P_FanListVo> callFanList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_FanListNewVo> callFanListNew(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo callFanMemo(ProcedureVo procedureVo);

    ProcedureVo callFanMemoSave(ProcedureVo procedureVo);

    ProcedureVo callFanEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_StarListNewVo> callStarListNew(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo callStarMemo(ProcedureVo procedureVo);

    ProcedureVo callStarMemoSave(ProcedureVo procedureVo);
}
