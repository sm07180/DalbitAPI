package com.demo.broadcast.dao;

import com.demo.common.vo.ProcedureVo;
import com.demo.common.vo.SampleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao {

    ProcedureVo callBroadCastRoomCreate(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomJoin(ProcedureVo procedureVo);
    ProcedureVo callBroadCastRoomOut(ProcedureVo procedureVo);
}
