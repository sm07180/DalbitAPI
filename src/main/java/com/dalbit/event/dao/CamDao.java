package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CamDao {
    @Transactional(readOnly = true)
    ProcedureVo callCamCheck(ProcedureVo procedureVo);

    ProcedureVo callCamApply(ProcedureVo procedureVo);
}
