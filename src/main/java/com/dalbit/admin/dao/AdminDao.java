package com.dalbit.admin.dao;

import com.dalbit.admin.vo.BroadcastExitVo;
import com.dalbit.admin.vo.BroadcastVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.admin.vo.procedure.P_BroadcastDetailOutputVo;
import com.dalbit.admin.vo.procedure.P_RoomForceExitInputVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface AdminDao {

    @Transactional(readOnly = true)
    ArrayList<BroadcastVo> selectBroadcastList(SearchVo searchVo);

    ProcedureVo callBroadcastRoomExit(ProcedureVo procedureVo);

    int updateBroadcastMemberExit(P_RoomForceExitInputVo pRoomForceExitInputVo);

    int updateBroadcastExit(BroadcastExitVo broadcastExitVo);
}
