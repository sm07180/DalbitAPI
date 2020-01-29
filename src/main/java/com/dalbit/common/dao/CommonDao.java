package com.dalbit.common.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommonDao {
    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);

    List<Map> callCodeDefineSelect();
}
