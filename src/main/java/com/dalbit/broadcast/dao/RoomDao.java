package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao {
    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);

    P_RoomEditOutVo callBroadCastRoomEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_RoomListVo> callBroadCastRoomList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    P_RoomInfoViewVo callBroadCastRoomInfoView(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callMemberBroadcastingCheck(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomLiveRankInfo(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_RoomGiftHistoryVo> callBroadCastRoomGiftHistory(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomMemberInfo(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadcastRoomStreamSelect(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadcastRoomTokenUpdate(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomStateUpate(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomChangeCount(ProcedureVo procedureVo);

    void callUpdateExitTry(String roomNo);

    // @Transactional(readOnly = true)
    List<P_SummaryListenerVo> callBroadcastSummaryListener(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_SummaryListenerVo> callBroadcastSummaryListenerMyStar(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_SummaryDjVo> callBroadcastSummaryDj(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_RoomGoodHistoryVo> callGetGoodHistory(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomContinueCheck(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomContinue(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_BadgeChangeListVo> changeBadge(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomStateEdit(ProcedureVo procedureVo);

    ProcedureVo callRoomStateGuestEdit(ProcedureVo procedureVo);

    ProcedureVo callRoomStateNormalize(ProcedureVo procedureVo);
}
