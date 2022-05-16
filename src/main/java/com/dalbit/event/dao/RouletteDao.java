package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.vo.procedure.P_RouletteApplyListVo;
import com.dalbit.event.vo.procedure.P_RouletteWinListVo;
import com.dalbit.event.vo.request.RouletteCouponHistVo;
import com.dalbit.event.vo.request.RouletteInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouletteDao {
    // @Transactional(readOnly = true)
    ProcedureVo callCouponSelect(ProcedureVo procedureVo);

    ProcedureVo rouletteStart(ProcedureVo procedureVo);

    ProcedureVo inputPhone(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_RouletteApplyListVo> applyList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_RouletteWinListVo> winList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<RouletteInfoVo> selectRouletteInfo(RouletteInfoVo rouletteInfoVo);

    // @Transactional(readOnly = true)
    List<RouletteCouponHistVo> callCouponHistory(ProcedureVo procedureVo);
}
