package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.P_RoomListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao {

    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomEdit(ProcedureVo procedureVo);
    List<P_RoomListVo> callBroadCastRoomList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGood(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestDelete(ProcedureVo procedureVo);
}
