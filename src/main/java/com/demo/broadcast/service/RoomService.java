package com.demo.broadcast.service;

import com.demo.broadcast.dao.RoomDao;
import com.demo.broadcast.vo.*;
import com.demo.common.vo.ProcedureOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    RoomDao roomDao;

    /**
     * 방송방 생성
     */
    public ProcedureVo callBroadCastRoomCreate(P_RoomCreateVo pRoomCreateVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomCreateVo);
        roomDao.callBroadCastRoomCreate(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 참여하기
     */
    public ProcedureVo callBroadCastRoomJoin(P_RoomJoinVo pRoomJoinVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomJoinVo);
        roomDao.callBroadCastRoomJoin(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 나가기
     */
    public ProcedureVo callBroadCastRoomExit(P_RoomExitVo pRoomExitVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomExitVo);
        roomDao.callBroadCastRoomExit(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 정보 수정
     */
    public ProcedureVo callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomEditVo);
        roomDao.callBroadCastRoomEdit(procedureVo);
        return procedureVo;
    }

    /**
     * 방송방 리스트
     */
    public ProcedureOutputVo callBroadCastRoomList(P_RoomListVo pRoomListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);
        List<RoomVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, CommonUtil.isEmpty(roomVoList) ? null : roomVoList);
        return procedureOutputVo;
    }


    /**
     * 방송방 참여자 리스트
     */
    public ProcedureOutputVo callBroadCastRoomMemberList(P_RoomMemberListVo pRoomMemberListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberListVo);
        List<RoomMemberVo> roomMemberVoList = roomDao.callBroadCastRoomMemberList(procedureVo);
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, CommonUtil.isEmpty(roomMemberVoList) ? null : roomMemberVoList);
        return procedureOutputVo;
    }

    /**
     * 방송방 좋아요 추가
     */
    public ProcedureVo callBroadCastRoomGood(P_RoomGoodVo pRoomGoodVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodVo);
        roomDao.callBroadCastRoomGood(procedureVo);
        return procedureVo;
    }
}
