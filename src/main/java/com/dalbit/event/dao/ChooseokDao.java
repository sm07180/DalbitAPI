package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ChooseokDao {
    // @Transactional(readOnly = true)
    ProcedureVo callChooseokCheck(ProcedureVo procedureVo);

    ProcedureVo callChooseokFreeDalCheck(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callChooseokPurchaseSelect(ProcedureVo procedureVo);

    ProcedureVo callChooseokPurchaseBonus(ProcedureVo procedureVo);
}
