package com.demo.broadcast.dao;

import com.demo.broadcast.vo.P_RoomGoodVo;
import com.demo.broadcast.vo.P_RoomMemberListVo;
import com.demo.broadcast.vo.RoomMemberVo;
import com.demo.broadcast.vo.RoomVo;
import com.demo.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao {

    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomEdit(ProcedureVo procedureVo);
    RoomVo callBroadCastRoomList(ProcedureVo procedureVo);
    RoomMemberVo callBroadCastRoomMemberList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGood(ProcedureVo procedureVo);
}
