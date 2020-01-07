package com.demo.broadcast.service;

import com.demo.broadcast.dao.RoomDao;
import com.demo.broadcast.vo.P_RoomVo;
import com.demo.common.vo.ProcedureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    RoomDao roomDao;

    /**
     * 방송방 생성
     */
    public ProcedureVo callBroadCastRoomCreate(P_RoomVo pRoomVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomVo);
        roomDao.callBroadCastRoomCreate(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 참여하기
     */
    public ProcedureVo callBroadCastRoomJoin(P_RoomVo pRoomVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomVo);
        roomDao.callBroadCastRoomJoin(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 나가기
     */
    public ProcedureVo callBroadCastRoomOut(P_RoomVo pRoomVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomVo);
        roomDao.callBroadCastRoomOut(procedureVo);
        return procedureVo;
    }
}
