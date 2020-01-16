package com.demo.broadcast;

import com.demo.broadcast.service.RoomService;
import com.demo.broadcast.vo.*;
import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.exception.GlobalException;
import com.demo.util.DalbitUtil;
import com.demo.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class BroadCastTest {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    private RoomService roomService;

    @Test
    public void 방송방생성테스트(){

        log.debug("방송방생성 테스트");
        log.debug(P_RoomCreateVo.builder().build().toString());

        P_RoomCreateVo apiSample = P_RoomCreateVo.builder().build();
        String result = roomService.callBroadCastRoomCreate(apiSample);

        log.info(" ### 방송방생성 결과 ###");
        log.info(result);
    }

    @Test
    public void 방송방참여하기(){
        log.debug("방송방 참여하기 테스트");
        log.debug(P_RoomJoinVo.builder().build().toString());

        P_RoomJoinVo apiSample = P_RoomJoinVo.builder().build();
        String result = roomService.callBroadCastRoomJoin(apiSample);

        log.info(" ### 방송방참여하기 결과 ###");
        log.info(result);
    }

    @Test
    public void 방송방나가기(){
        log.debug("방송방 나가기 테스트");
        log.debug(P_RoomExitVo.builder().build().toString());

        P_RoomExitVo apiSample = P_RoomExitVo.builder().build();
        String result = roomService.callBroadCastRoomExit(apiSample);

        log.info(" ### 방송방나가기 결과 ###");
        log.info(result);
    }

    @Test
    public void 방송방정보수정(){
        log.debug("방송방 정보 수정 테스트");
        log.debug(P_RoomEditVo.builder().build().toString());

        P_RoomEditVo apiSample = P_RoomEditVo.builder().build();
        String result = roomService.callBroadCastRoomEdit(apiSample);

        log.info(" ### 방송방정보수정 결과 ###");
        log.info(result);
    }

    @Test
    public void 방송방리스트(){
        log.debug("방송방 리스트 테스트");
        log.debug(P_RoomListVo.builder().build().toString());

        P_RoomListVo apiSample = P_RoomListVo.builder().build();
        String result = roomService.callBroadCastRoomList(apiSample);

        log.info(" ### 방송방리스트 결과 ###");
        log.info(result);

    }

    @Test
    public void 방송방참여자리스트 (){
        log.debug("방송방 참여자 리스트");
        log.debug(P_RoomMemberListVo.builder().build().toString());

        P_RoomMemberListVo apiSample = P_RoomMemberListVo.builder().build();
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomMemberList(apiSample);

        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info("방송방 참여자 리스트: {}", procedureOutputVo.toString());
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureOutputVo.getRet().equals(Status.방송참여자리스트없음.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트없음, procedureOutputVo.getData())));
        }else if(procedureOutputVo.getRet().equals(Status.방송참여자리스트_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_회원아님, procedureOutputVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_조회, procedureOutputVo.getData())));
        }


    }

    @Test
    public void 방송방좋아요 (){
        log.debug("방송방 좋아요 추가");
        log.debug(P_RoomGoodVo.builder().build().toString());

        P_RoomGoodVo apiSample = P_RoomGoodVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomGood(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.좋아요.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.좋아요, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.좋아요_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.좋아요_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.좋아요_해당방송없음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.좋아요_해당방송없음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.좋아요_방송참가자아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.좋아요_방송참가자아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.좋아요_이미했음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.좋아요_이미했음, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방참여토큰가져오기() throws GlobalException {
        log.debug("방송방 참여 토큰 가져오기");
        log.debug(P_RoomJoinTokenVo.builder().build().toString());
        P_RoomJoinTokenVo apiSample = P_RoomJoinTokenVo.builder().build();
        HashMap resultMap =  roomService.callBroadCastRoomStreamIdRequest(apiSample.getRoom_no());
        //HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("방송방참여토큰가져오기 resultMap: {}", resultMap);
    }

    @Test
    public void 방송방게스트지정하기(){
        log.debug("방송방 게스트 지정하기");
        P_RoomGuestAddVo apiSample = P_RoomGuestAddVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomGuestAdd(apiSample);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.게스트지정.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_해당방이없음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_해당방이없음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방이종료되었음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방이종료되었음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방소속_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방장아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방장아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아이디아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_방소속_회원아이디아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트지정_불가.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_불가, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트지정_실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방게스트취소하기(){
        log.debug("방송방 게스트 취소하기");
        P_RoomGuestDeleteVo apiSample = P_RoomGuestDeleteVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomGuestDelete(apiSample);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.게스트취소.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_해당방이없음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_해당방이없음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방이종료되었음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방이종료되었음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방소속_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방장아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방장아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아이디아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_방소속_회원아이디아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.게스트취소_불가.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_불가, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.게스트취소_실패, procedureVo.getData())));
        }
    }

    @Test
    public void 나이계산(){
        int year = 1990;
        int age = DalbitUtil.ageCalculation(year);
        log.info("나이대: {}", age);
    }
}


