package com.demo.broadcast.service;

import com.demo.broadcast.dao.RoomDao;
import com.demo.broadcast.vo.RoomVo;
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
    public ProcedureVo callBroadCastRoomCreate(RoomVo roomVo) {
        ProcedureVo procedureVo = new ProcedureVo(roomVo);
        roomDao.callBroadCastRoomCreate(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 참여하기
     */
    public ProcedureVo callBroadCastRoomJoin(RoomVo roomVo) {
        ProcedureVo procedureVo = new ProcedureVo(roomVo);
        roomDao.callBroadCastRoomJoin(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 나가기
     */
    public ProcedureVo callBroadCastRoomOut(RoomVo roomVo) {
        ProcedureVo procedureVo = new ProcedureVo(roomVo);
        roomDao.callBroadCastRoomOut(procedureVo);
        return procedureVo;
    }
}
