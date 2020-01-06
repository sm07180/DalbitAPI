package com.demo.broadcast;

import com.demo.broadcast.service.RoomService;
import com.demo.broadcast.vo.RoomVo;
import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
        log.debug(RoomVo.builder().build().toString());

        RoomVo apiSample = RoomVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomCreate(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(0 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송생성, procedureVo.getData())));
        }else if(-1 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원아님, procedureVo.getData())));
        }else if(-2 == procedureVo.getRet()) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송중인방존재, procedureVo.getData())));
        } else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방생성실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방참여하기(){
        log.debug("방송방 참여하기 테스트");
        log.debug(RoomVo.builder().build().toString());

        RoomVo apiSample = RoomVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomJoin(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(0 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방참가성공, procedureVo.getData())));
        }else if(-1 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원아님, procedureVo.getData())));
        }else if(-2 == procedureVo.getRet()) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.해당방이없음, procedureVo.getData())));
        }else if(-3 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.종료된방송, procedureVo.getData())));
        }else if(-4 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.이미참가, procedureVo.getData())));
        }else if(-5 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.입장제한, procedureVo.getData())));
        }else if(-6 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.나이제한, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방참가실패, procedureVo.getData())));
        }
    }

    @Test
    public void 방송방나가기(){
        log.debug("방송방 나가기 테스트");
        log.debug(RoomVo.builder().build().toString());

        RoomVo apiSample = RoomVo.builder().build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomJoin(apiSample);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        if(0 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기, procedureVo.getData())));
        }else if(-1 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원아님, procedureVo.getData())));
        }else if(-2 == procedureVo.getRet()) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.해당방이없음, procedureVo.getData())));
        }else if(-3 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.종료된방송, procedureVo.getData())));
        }else if(-4 == procedureVo.getRet()){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방참가자아님, procedureVo.getData())));
        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.방송나가기실패, procedureVo.getData())));
        }

    }
}


