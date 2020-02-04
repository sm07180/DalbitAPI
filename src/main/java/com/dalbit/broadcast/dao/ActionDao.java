package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.P_RoomShareLinkVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionDao {
    ProcedureVo callBroadCastRoomGood(ProcedureVo procedureVo);
    List<P_RoomShareLinkVo> callBroadCastRoomShareLink(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGift(ProcedureVo procedureVo);
}
