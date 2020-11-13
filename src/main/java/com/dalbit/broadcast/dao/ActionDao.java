package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_RoomShareLinkVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ActionDao {
    ProcedureVo callBroadCastRoomGood(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_RoomShareLinkVo> callBroadCastRoomShareLink(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomGift(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomBooster(ProcedureVo procedureVo);
    ProcedureVo callroomExtendTime(ProcedureVo procedureVo);

    ProcedureVo callRoomFreeze(ProcedureVo procedureVo);
}
