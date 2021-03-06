package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinDao {
    ProcedureVo callEventJoinCheck(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callEventJoinDetail(ProcedureVo procedureVo);

    ProcedureVo callEventJoinReward(ProcedureVo procedureVo);
}
