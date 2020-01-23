package com.dalbit.broadcast;

import com.dalbit.broadcast.service.CommonService;
import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
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
    @Autowired
    UserService userService;
    @Autowired
    private CommonService commonService;
    @Autowired
    RestService restService;


    @Test
    public void 방송방생성테스트() throws GlobalException{

        log.debug("방송방생성 테스트");
        //토큰생성
        String streamId = (String) restService.antCreate("자유롭게").get("streamId");
        String publishToken = (String) restService.antToken(streamId, "publish").get("tokenId");
        String playToken = (String) restService.antToken(streamId, "play").get("tokenId");
        log.info("bj_streamId: {}", streamId);
        log.info("bj_publishToken: {}", publishToken);
        log.info("bj_playToken: {}", playToken);

        P_RoomCreateVo apiData = new P_RoomCreateVo();

        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setSubjectType(1);
        apiData.setTitle("방송생성부터 참여 게스트 지정까지");
        apiData.setWelcomMsg("환영해보자");
        apiData.setEntryType(0);
        apiData.setOs(3);
        apiData.setBj_streamid(streamId);
        apiData.setBj_publish_tokenid(publishToken);
        apiData.setBj_play_tokenid(playToken);

        String result = roomService.callBroadCastRoomCreate(apiData);

        log.info(" ### 방송방생성 결과 ###");
        log.info(result);
    }

    @Test
    public void 방송방참여하기(){
        /*log.debug("방송방 참여하기 테스트");
        log.debug(P_RoomJoinVo.builder().build().toString());

        P_RoomJoinVo apiSample = P_RoomJoinVo.builder().build();
        String result = roomService.callBroadCastRoomJoin(apiSample);

        log.info(" ### 방송방참여하기 결과 ###");
        log.info(result);*/
    }

    @Test
    public void 방송방나가기(){
        /*log.debug("방송방 나가기 테스트");
        log.debug(P_RoomExitVo.builder().build().toString());

        P_RoomExitVo apiSample = P_RoomExitVo.builder().build();
        String result = roomService.callBroadCastRoomExit(apiSample);

        log.info(" ### 방송방나가기 결과 ###");
        log.info(result);*/
    }

    @Test
    public void 방송방정보수정(){
        /*log.debug("방송방 정보 수정 테스트");
        log.debug(P_RoomEditVo.builder().build().toString());

        P_RoomEditVo apiSample = P_RoomEditVo.builder().build();
        String result = roomService.callBroadCastRoomEdit(apiSample);

        log.info(" ### 방송방정보수정 결과 ###");
        log.info(result);*/
    }

    @Test
    public void 방송방리스트(){
        /*log.debug("방송방 리스트 테스트");
        log.debug(P_RoomListVo.builder().build().toString());

        P_RoomListVo apiSample = P_RoomListVo.builder().build();
        String result = roomService.callBroadCastRoomList(apiSample);

        log.info(" ### 방송방리스트 결과 ###");
        log.info(result);*/

    }

    @Test
    public void 방송방참여자리스트 (){
        /*log.debug("방송방 참여자 리스트");
        log.debug(P_RoomMemberListVo.builder().build().toString());

        P_RoomMemberListVo apiSample = P_RoomMemberListVo.builder().build();
        String result = roomService.callBroadCastRoomMemberList(apiSample);

        log.info(" ### 프로시저 호출결과 ###");
        log.info(result);*/

    }

    @Test
    public void 방송방좋아요 (){
        log.debug("방송방 좋아요 추가");
        log.debug(P_RoomGoodVo.builder().build().toString());

        P_RoomGoodVo apiSample = P_RoomGoodVo.builder().build();
        String result = roomService.callBroadCastRoomGood(apiSample);


        log.info(" ### 프로시저 호출결과 ###");
    }

    @Test
    public void 방송방참여토큰가져오기() throws GlobalException {
        log.debug("방송방 참여 토큰 가져오기");
        log.debug(P_RoomJoinTokenVo.builder().build().toString());
        P_RoomJoinTokenVo apiSample = P_RoomJoinTokenVo.builder().build();
        HashMap resultMap =  commonService.callBroadCastRoomStreamIdRequest(apiSample.getRoom_no());
        //HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("방송방참여토큰가져오기 resultMap: {}", resultMap);
    }

    @Test
    public void 방송방게스트지정하기(){
        log.debug("방송방 게스트 지정하기");
        //P_RoomGuestAddVo apiSample = P_RoomGuestAddVo.builder().build();
        //String result = userService.callBroadCastRoomGuestAdd(apiSample);

        log.info(" ### 프로시저 호출결과 ###");
        //log.info(result);
    }

    @Test
    public void 방송방게스트취소하기(){
        log.debug("방송방 게스트 취소하기");
        //P_RoomGuestDeleteVo apiSample = P_RoomGuestDeleteVo.builder().build();
        //String result = userService.callBroadCastRoomGuestDelete(apiSample);

        log.info(" ### 프로시저 호출결과 ###");
        //log.info(result);
    }

    @Test
    public void 나이계산(){
        int year = 1990;
        int age = DalbitUtil.ageCalculation(year);
        log.info("나이대: {}", age);
    }

}


