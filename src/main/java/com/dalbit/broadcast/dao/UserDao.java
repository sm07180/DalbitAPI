package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.DevRoomVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDao {
    @Transactional(readOnly = true)
    List<P_RoomMemberListVo> callBroadCastRoomMemberList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestDelete(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomKickout(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomManagerDel(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<DevRoomVo> selectBjRoom(String memNo);
    @Transactional(readOnly = true)
    List<DevRoomVo> selectJoinRoom(String memNo);
    @Transactional(readOnly = true)
    List<DevRoomVo> selectDisconnectRoom(String memNo);
    void updateNormalRoom(String roomNo);


}
