package com.dalbit.rank.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.P_MainTimeRankingPageVo;
import com.dalbit.rank.vo.procedure.P_RankListVo;
import com.dalbit.rank.vo.procedure.P_TimeRankListVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RankDao {

    ProcedureVo callRankSetting(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_RankListVo> callRankList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_TimeRankListVo> callTimeRankList(ProcedureVo procedureVo);
}
