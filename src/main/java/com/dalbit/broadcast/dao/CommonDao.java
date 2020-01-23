package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.P_RoomListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonDao {

    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);
}
