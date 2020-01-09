package com.demo.broadcast;

import com.demo.broadcast.service.RoomService;
import com.demo.broadcast.vo.*;
import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.util.GsonUtil;
import com.demo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
        ProcedureVo procedureVo = roomService.callBroadCastRoomCreate(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.방송생성.getMessageKey())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송생성, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송생성_회원아님.getMessageKey())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송생성_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송중인방존재.getMessageKey())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송중인방존재, procedureVo.getData())));
        } else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방생성실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방참여하기(){
        log.debug("방송방 참여하기 테스트");
        log.debug(P_RoomJoinVo.builder().build().toString());

        P_RoomJoinVo apiSample = P_RoomJoinVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomJoin(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.방참가성공.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방참가성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송참여_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송참여_해당방이없음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송참여_종료된방송, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.이미참가.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.이미참가, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.입장제한.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.입장제한, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.나이제한.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.나이제한, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방참가실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방나가기(){
        log.debug("방송방 나가기 테스트");
        log.debug(P_RoomExitVo.builder().build().toString());

        P_RoomExitVo apiSample = P_RoomExitVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomExit(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.방송나가기.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송나가기_회원아님.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송나가기_해당방이없음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_해당방이없음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_종료된방송, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방참가자아님.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방참가자아님, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방정보수정(){
        log.debug("방송방 정보 수정 테스트");
        log.debug(P_RoomEditVo.builder().build().toString());

        P_RoomEditVo apiSample = P_RoomEditVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomEdit(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureVo.getRet().equals(Status.방송정보수정성공.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송정보수정_회원아님.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_회원아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송정보수정_해당방이없음.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_해당방이없음, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송정보수정_해당방에없는회원번호.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_해당방에없는회원번호, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.방송정보수정_수정권이없는회원.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_수정권이없는회원, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방리스트(){
        log.debug("방송방 리스트 테스트");
        log.debug(P_RoomListVo.builder().build().toString());

        P_RoomListVo apiSample = P_RoomListVo.builder().build();
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomList(apiSample);

        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info("방송방 리스트: {}", procedureOutputVo.getOutputBox());
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info(" ### 프로시저 호출결과 ###");
        if(procedureOutputVo.getRet().equals(Status.방송리스트없음.getMessageCode())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송리스트없음, procedureOutputVo.getData())));
        }else if(procedureOutputVo.getRet().equals(Status.방송리스트_회원아님.getMessageCode())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송리스트_회원아님, procedureOutputVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송리스트_조회, procedureOutputVo.getData())));
        }
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
}


