package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.DevRoomVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<P_RoomMemberListVo> callBroadCastRoomMemberList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestDelete(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomKickout(ProcedureVo procedureVo);
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomManagerDel(ProcedureVo procedureVo);

    List<DevRoomVo> selectBjRoom(String memNo);
    List<DevRoomVo> selectJoinRoom(String memNo);



}
