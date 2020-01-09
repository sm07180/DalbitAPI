package com.demo.broadcast.dao;

import com.demo.broadcast.vo.RoomMemberVo;
import com.demo.broadcast.vo.RoomVo;
import com.demo.common.vo.ProcedureOutputVo;
import com.demo.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao {

    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomEdit(ProcedureVo procedureVo);
    List<RoomVo> callBroadCastRoomList(ProcedureVo procedureVo);
    List<RoomMemberVo> callBroadCastRoomMemberList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGood(ProcedureVo procedureVo);
}
