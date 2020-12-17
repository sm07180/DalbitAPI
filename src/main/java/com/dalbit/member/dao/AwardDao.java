package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.procedure.P_AwardListVo;
import com.dalbit.member.vo.procedure.P_AwardVoteResultVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardDao {
    List<P_AwardListVo> callAwardList(ProcedureVo procedureVo);

    ProcedureVo callAwardVote(ProcedureVo procedureVo);

    List<P_AwardVoteResultVo> callAwardVoteResult(ProcedureVo procedureVo);
}
