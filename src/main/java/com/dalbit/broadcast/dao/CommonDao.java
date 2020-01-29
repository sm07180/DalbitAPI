package com.dalbit.broadcast.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonDao {
    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);
}
