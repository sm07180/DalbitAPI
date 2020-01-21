package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RoomService {

    @Autowired
    RoomDao roomDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     * 방송방 생성
     */
    public String callBroadCastRoomCreate(P_RoomCreateVo pRoomCreateVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomCreateVo);
        roomDao.callBroadCastRoomCreate(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String roomNo = DalbitUtil.isNullToString(resultMap.get("room_no"));
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info("방번호 추출: {}", roomNo);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("room_no", roomNo);
        returnMap.put("bj_streamid",pRoomCreateVo.getBj_streamid());
        returnMap.put("bj_publish_tokenid", pRoomCreateVo.getBj_publish_tokenid());
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.방송생성.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송생성_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성_회원아님, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송중인방존재.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송중인방존재, procedureVo.getData())));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방생성실패)));
        }

        return result;
    }

    /**
     * 방송방 참여하기
     */
    public String callBroadCastRoomJoin(P_RoomJoinVo pRoomJoinVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomJoinVo);
        roomDao.callBroadCastRoomJoin(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("mem_no",MemberVo.getUserInfo().getMem_no());
        returnMap.put("room_no",pRoomJoinVo.getRoom_no());
        returnMap.put("guest_streamid",pRoomJoinVo.getGuest_streamid());
        returnMap.put("guest_publish_tokenid",pRoomJoinVo.getGuest_publish_tokenid());
        returnMap.put("guest_play_tokenid",pRoomJoinVo.getGuest_play_tokenid());
        returnMap.put("bj_streamid",pRoomJoinVo.getBj_streamid());
        returnMap.put("bj_publish_tokenid",pRoomJoinVo.getBj_publish_tokenid());
        returnMap.put("bj_play_tokenid",pRoomJoinVo.getBj_play_tokenid());
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.방송참여성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여성공, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_회원아님, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_해당방이없음, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_종료된방송, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_이미참가.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_이미참가, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_입장제한.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_입장제한, procedureVo.getData())));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방참가실패)));
        }

        return result;
    }

    /**
     * 방송방 나가기
     */
    public String callBroadCastRoomExit(P_RoomExitVo pRoomExitVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomExitVo);
        roomDao.callBroadCastRoomExit(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(Status.방송나가기.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_회원아님)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_해당방이없음)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_종료된방송)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_방참가자아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_방참가자아님)));
        } else if (procedureVo.getRet().equals(Status.방송나가기실패.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기실패)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방참가실패)));
        }
        return result;
    }

    /**
     * 방송방 정보 수정
     */
    public String callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomEditVo);
        roomDao.callBroadCastRoomEdit(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(Status.방송정보수정성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정성공)));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정_회원아님)));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_해당방에없는회원번호.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정_해당방에없는회원번호)));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_수정권이없는회원.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정_수정권이없는회원)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정실패)));
        }

        return result;
    }

    /**
     * 방송방 리스트
     */
    public String callBroadCastRoomList(P_RoomListVo pRoomListVo){
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);

        List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<roomVoList.size(); i++){
                outVoList.add(new RoomOutVo(roomVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap roomList = new HashMap();
        roomList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result = "";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트조회, roomList));
        }else if(Status.방송리스트_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트_회원아님, roomList));
        }else if(Status.방송리스트없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트없음, roomList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트조회_실패, roomList));
        }
        return result;
    }


    /**
     * 방송방 좋아요 추가
     */
    public String callBroadCastRoomGood(P_RoomGoodVo pRoomGoodVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodVo);
        roomDao.callBroadCastRoomGood(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        int likes = DalbitUtil.getIntMap(resultMap, "good_count");
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("likes", likes);
        procedureVo.setData(returnMap);

        String result="";
        if(Status.좋아요.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요, procedureVo.getData()));
        }else if(Status.좋아요_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_회원아님,procedureVo.getData()));
        }else if(Status.좋아요_해당방송없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_해당방송없음, procedureVo.getData()));
        }else if(Status.좋아요_방송참가자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_방송참가자아님, procedureVo.getData()));
        }else if(Status.좋아요_이미했음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_이미했음, procedureVo.getData()));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패));
        }

        return result;
    }

    /**
     * 방송방 참가를 위해 스트림아이디 토큰아이디 받아오기
     */
    public HashMap callBroadCastRoomStreamIdRequest(String roomNo) throws GlobalException {
        P_RoomJoinTokenVo apiData = P_RoomJoinTokenVo.builder()
                .room_no(roomNo)
                .build();
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        roomDao.callBroadCastRoomStreamIdRequest(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);

        String tokenFailData = "";

        if (procedureVo.getRet().equals(Status.방송참여토큰_해당방이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_해당방이없음, procedureVo.getData());
        } else if (procedureVo.getRet().equals(Status.방송참여토큰_방장이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_방장이없음, procedureVo.getData());
        } else if(procedureVo.getRet().equals(Status.방송참여토큰발급_실패.getMessageCode())){
            //tokenFailData = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여토큰발급_실패)));
            throw new GlobalException(Status.방송참여토큰발급_실패, procedureVo.getData());
        }

        boolean isTokenSuccess = procedureVo.getRet().equals(Status.방송참여토큰발급.getMessageCode());
        resultMap.put("isTokenSuccess", isTokenSuccess);
        resultMap.put("tokenFailData", tokenFailData);

        log.debug("방송방 참여 토큰발급 결과 [{}]: {}", isTokenSuccess, tokenFailData);

        return resultMap;
    }

    /**
     * 방송방 게스트 지정하기
     */
    public String callBroadCastRoomGuestAdd(P_RoomGuestAddVo pRoomGuestAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestAddVo);
        roomDao.callBroadCastRoomGuestAdd(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("room_no",pRoomGuestAddVo.getRoom_no());
        returnMap.put("guest_streamid",pRoomGuestAddVo.getGuest_streamid());
        returnMap.put("bj_streamid",pRoomGuestAddVo.getBj_streamid());
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result="";
        if(procedureVo.getRet().equals(Status.게스트지정.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_회원아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_해당방이없음, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방이종료되었음, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방소속_회원아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방장아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방장아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아이디아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방소속_회원아이디아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트지정_불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_불가, procedureVo.getData()));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_실패, procedureVo.getData()));
        }

        return result;
    }

    /**
     * 방송방 게스트 취소
     */
    public String callBroadCastRoomGuestDelete(P_RoomGuestDeleteVo pRoomGuestDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestDeleteVo);
        roomDao.callBroadCastRoomGuestDelete(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("room_no",pRoomGuestDeleteVo.getRoom_no());
        returnMap.put("guest_streamid",pRoomGuestDeleteVo.getGuest_streamid());
        returnMap.put("bj_streamid",pRoomGuestDeleteVo.getBj_streamid());
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result = "";
        if(procedureVo.getRet().equals(Status.게스트취소.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_회원아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_해당방이없음, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방이종료되었음, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방소속_회원아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방장아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방장아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아이디아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방소속_회원아이디아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.게스트취소_불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_불가, procedureVo.getData()));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_실패, procedureVo.getData()));
        }
        return result;
    }
}
