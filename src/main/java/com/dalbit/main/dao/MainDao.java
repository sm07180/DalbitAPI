package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainDao {
    List<P_MainFanRankingVo> callMainFanRanking(ProcedureVo procedureVo);
    List<P_MainDjRankingVo> callMainDjRanking(ProcedureVo procedureVo);
}
