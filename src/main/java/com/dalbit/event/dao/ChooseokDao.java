package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ChooseokDao {
    //@Transactional(readOnly = true)
    ProcedureVo callChooseokCheck(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    ProcedureVo callChooseokFreeDalCheck(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    ProcedureVo callChooseokPurchaseSelect(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    ProcedureVo callChooseokPurchaseBonus(ProcedureVo procedureVo);
}
