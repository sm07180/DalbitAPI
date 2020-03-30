package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_MainMyDjVo;
import com.dalbit.main.vo.procedure.P_MainRecommandVo;
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
}
