package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserDao {
    //@Transactional(readOnly = true)
    List<P_RoomMemberListVo> callBroadCastRoomMemberList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomKickout(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomManagerDel(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    HashMap selectGuestStreamInfo(HashMap params);
    int updateGuestStreamInfo(HashMap params);
}
