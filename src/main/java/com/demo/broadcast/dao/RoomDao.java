package com.demo.broadcast.dao;

import com.demo.broadcast.vo.P_RoomGoodVo;
import com.demo.broadcast.vo.P_RoomMemberListVo;
import com.demo.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao {

    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomEdit(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomMemberList(P_RoomMemberListVo pRoomMemberListVo);
    ProcedureVo callBroadCastRoomGood(P_RoomGoodVo pRoomGoodVo);
}
