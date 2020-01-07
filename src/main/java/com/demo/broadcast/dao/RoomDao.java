package com.demo.broadcast.dao;

import com.demo.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao {

    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomOut(ProcedureVo procedureVo);
}
