package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.BannerVo;
import com.dalbit.main.vo.procedure.*;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MainDao {
    //@Transactional(readOnly = true)
    List<P_MainFanRankingVo> callMainFanRanking(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    List<P_MainDjRankingVo> callMainDjRanking(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    List<P_MainMyDjVo> callMainMyDjList(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    List<P_MainRecommandVo> callMainRecommandList(P_MainRecommandVo pMainRecommandVo);

    //@Transactional(readOnly = true)
    List<P_MainStarVo> callMainStarList(String memNo);

    //@Transactional(readOnly = true)
    List<BannerVo> selectBanner(P_BannerVo pBannerVo);

    //@Transactional(readOnly = true)
    List<P_MainLevelRankingVo> callMainLevelRanking(ProcedureVo procedureVo);

    ProcedureVo callMainRankReward(ProcedureVo procedureVo);

    ProcedureVo callMainRankRandomBox(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MainRankingPageVo> callMainRankingPage(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MainGoodRankingVo> callMainGoodRanking(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_SpecialHistoryVo> callSpecialDjHistory(ProcedureVo procedureVo);
}
