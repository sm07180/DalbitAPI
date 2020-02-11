package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.database.D_MyBoardcastCountVo;
import com.dalbit.broadcast.vo.procedure.P_RoomInfoViewVo;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao {
    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomExit(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomEdit(ProcedureVo procedureVo);
    List<P_RoomListVo> callBroadCastRoomList(ProcedureVo procedureVo);
    P_RoomInfoViewVo callBroadCastRoomInfoView(ProcedureVo procedureVo);

    int getMyBroadcastCount(D_MyBoardcastCountVo dMyBoardcastCountVo);
}
