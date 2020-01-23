package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.P_RoomMemberListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<P_RoomMemberListVo> callBroadCastRoomMemberList(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestAdd(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGuestDelete(ProcedureVo procedureVo);
}
