package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.vo.procedure.P_RouletteApplyListVo;
import com.dalbit.event.vo.procedure.P_RouletteWinListVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RouletteDao {
    @Transactional(readOnly = true)
    ProcedureVo callCouponSelect(ProcedureVo procedureVo);

    ProcedureVo rouletteStart(ProcedureVo procedureVo);

    ProcedureVo inputPhone(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_RouletteApplyListVo> applyList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_RouletteWinListVo> winList(ProcedureVo procedureVo);
}