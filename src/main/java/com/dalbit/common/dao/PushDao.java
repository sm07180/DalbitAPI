package com.dalbit.common.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.procedure.P_pushInsertVo;
import org.springframework.stereotype.Repository;

@Repository
public interface PushDao {

    int insertContentsPushAdd(P_pushInsertVo pPushInsertVo);
    ProcedureVo callStmpPushAdd(ProcedureVo procedureVo);
}
