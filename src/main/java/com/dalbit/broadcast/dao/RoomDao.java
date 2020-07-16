package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoomDao {
    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);
    P_RoomEditOutVo callBroadCastRoomEdit(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_RoomListVo> callBroadCastRoomList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    P_RoomInfoViewVo callBroadCastRoomInfoView(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callMemberBroadcastingCheck(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomLiveRankInfo(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_RoomGiftHistoryVo> callBroadCastRoomGiftHistory(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomMemberInfo(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callBroadcastRoomStreamSelect(ProcedureVo procedureVo);
    ProcedureVo callBroadcastRoomTokenUpdate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomStateUpate(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomChangeCount(ProcedureVo procedureVo);

    void callUpdateExitTry(String roomNo);
}
