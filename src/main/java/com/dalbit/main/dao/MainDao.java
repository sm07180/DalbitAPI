package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MainDao {
    @Transactional(readOnly = true)
    List<P_MainFanRankingVo> callMainFanRanking(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MainDjRankingVo> callMainDjRanking(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MainMyDjVo> callMainMyDjList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MainRecommandVo> callMainRecommandList(String memNo);
    @Transactional(readOnly = true)
    List<P_MainStarVo> callMainStarList(String memNo);
}
