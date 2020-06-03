package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.vo.procedure.P_RankingLiveOutputVo;
import com.dalbit.event.vo.procedure.P_RankingResultOutputVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventDao {
    @Transactional(readOnly = true)
    List<P_RankingLiveOutputVo> callEventRankingLive(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_RankingResultOutputVo> callEventRankingResult(ProcedureVo procedureVo);
}
