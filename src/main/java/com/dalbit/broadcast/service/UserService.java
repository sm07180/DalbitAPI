package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    GsonUtil gsonUtil;
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     * 방송방 참여자 리스트
     */
    public String callBroadCastRoomMemberList(P_RoomMemberListVo pRoomMemberListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberListVo);
        List<P_RoomMemberListVo> roomMemberVoList = userDao.callBroadCastRoomMemberList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomMemberVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomMemberOutVo> outVoList = new ArrayList<>();
            for (int i = 0; i< roomMemberVoList.size(); i++){
                outVoList.add(new RoomMemberOutVo(roomMemberVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap roomMemberList = new HashMap();
        roomMemberList.put("list", procedureOutputVo.getOutputBox());
        roomMemberList.put("paging ", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomMemberListVo.getPageNo(), pRoomMemberListVo.getPageCnt()));

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_조회, roomMemberList));
        }else if(Status.방송참여자리스트없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트없음));
        }else if(Status.방송참여자리스트_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_회원아님));
        }else if(Status.방송참여자리스트_방없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_방없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트조회_실패));
        }

        return result;
    }

    /**
     * 방송방 게스트 지정하기
     */
    public String callBroadCastRoomGuestAdd(P_RoomGuestAddVo pRoomGuestAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestAddVo);
        userDao.callBroadCastRoomGuestAdd(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("roomNo",pRoomGuestAddVo.getRoom_no());
        returnMap.put("gstStreamId",pRoomGuestAddVo.getGuest_streamid());
        returnMap.put("gstPubToken",pRoomGuestAddVo.getGuest_publish_tokenid());
        returnMap.put("gstPlayToken",pRoomGuestAddVo.getGuest_play_tokenid());
        returnMap.put("bjStreamId",pRoomGuestAddVo.getBj_streamid());
        returnMap.put("bjPubToken",pRoomGuestAddVo.getBj_publish_tokenid());
        log.info("returnMap: {}",returnMap);

        String result="";
        if(procedureVo.getRet().equals(Status.게스트지정.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정, returnMap));
        }else if(procedureVo.getRet().equals(Status.게스트지정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_회원아님));
        }else if(procedureVo.getRet().equals(Status.게스트지정_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_해당방이없음));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방이종료되었음));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방소속_회원아님));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방장아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방장아님));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아이디아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방소속_회원아이디아님));
        }else if(procedureVo.getRet().equals(Status.게스트지정_불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_불가));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_실패));
        }

        return result;
    }

    /**
     * 방송방 게스트 취소
     */
    public String callBroadCastRoomGuestDelete(P_RoomGuestDeleteVo pRoomGuestDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestDeleteVo);
        userDao.callBroadCastRoomGuestDelete(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("roomNo",pRoomGuestDeleteVo.getRoom_no());
        returnMap.put("gstStreamId",pRoomGuestDeleteVo.getGuest_streamid());
        returnMap.put("bjStreamId",pRoomGuestDeleteVo.getBj_streamid());
        log.info("returnMap: {}",returnMap);

        String result = "";
        if(procedureVo.getRet().equals(Status.게스트취소.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소, returnMap));
        }else if(procedureVo.getRet().equals(Status.게스트취소_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_회원아님));
        }else if(procedureVo.getRet().equals(Status.게스트취소_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_해당방이없음));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방이종료되었음));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방소속_회원아님));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방장아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방장아님));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아이디아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방소속_회원아이디아님));
        }else if(procedureVo.getRet().equals(Status.게스트취소_불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_불가));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_실패));
        }
        return result;
    }

    /**
     * 방송방 강퇴하기
     */
    public String callBroadCastRoomKickout(P_RoomKickoutVo pRoomKickoutVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomKickoutVo);
        userDao.callBroadCastRoomKickout(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result = "";
        if(procedureVo.getRet().equals(Status.강제퇴장.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_회원아님));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_해당방이없음));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_방이종료되었음));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_요청회원_방소속회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_요청회원_방소속회원아님));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_권한없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_권한없음));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_대상회원_방소속회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_대상회원_방소속회원아님));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_게스트이상불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_게스트이상불가));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_실패));
        }

        return result;
    }
}
