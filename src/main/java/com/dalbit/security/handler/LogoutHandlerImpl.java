package com.dalbit.security.handler;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.procedure.P_MemberBroadcastingCheckVo;
import com.dalbit.broadcast.vo.procedure.P_RoomExitVo;
import com.dalbit.broadcast.vo.procedure.P_RoomJoinVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.security.dao.LoginDao;
import com.dalbit.security.vo.P_ListeningRoom;
import com.dalbit.util.CookieUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component("logoutHandler")
public class LogoutHandlerImpl implements LogoutHandler {

    @Autowired
    RoomService roomService;
    @Autowired
    CommonService commonService;
    @Autowired
    MemberService memberService;

    @Autowired
    LoginDao loginDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    RestService restService;

    @Autowired
    GsonUtil gsonUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){

        DalbitUtil.setHeader(request, response);

        ProcedureVo procedureVo = roomService.callMemberBroadcastingCheck(new P_MemberBroadcastingCheckVo(MemberVo.getMyMemNo(request)));
        log.debug("방송중인 DJ체크 : {}", procedureVo.toString());

        HashMap broadcastingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String state = DalbitUtil.getStringMap(broadcastingInfo, "state");
        boolean isBroadcast = state.equals(Code.방송중체크_방송중.getCode());

        if(DalbitUtil.isLogin(request) && procedureVo.getRet().equals(Status.방송중인DJ체크_방송중.getMessageCode()) && isBroadcast){
            DeviceVo deviceVo = new DeviceVo(request);
            String device_uuid = DalbitUtil.getStringMap(broadcastingInfo, "device_uuid");
            device_uuid = device_uuid == null ? "" : device_uuid;
            if(device_uuid.equals(deviceVo.getDeviceUuid())){
                try {
                    gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그아웃실패_진행중인방송));

                }catch (Exception e){
                    log.error("LogoutHandlerImpl.logout : {}", e.getMessage());
                }
            }else{
                logout(request, response);
            }
        }else{
            logout(request, response);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response){
        try {
            String olderMemNo = MemberVo.getMyMemNo(request);
            CookieUtil.deleteCookie(DalbitUtil.getProperty("server.servlet.session.cookie.name"), "", "", 0);
            CookieUtil.deleteCookie(DalbitUtil.getProperty("sso.cookie.name"), "", "", 0);

            memberService.refreshAnonymousSecuritySession("anonymousUser");

            HashMap<String, Object> result = commonService.getJwtTokenInfo(request);
            gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그아웃성공, result.get("tokenVo")));
            String newMemNo = MemberVo.getMyMemNo(request);

            //회원으로 청취중인 방 퇴장 후 비회원으로 조인
            if(!DalbitUtil.isEmpty(olderMemNo) && !DalbitUtil.isEmpty(newMemNo)){
                DeviceVo deviceVo = new DeviceVo(request);
                P_ListeningRoom pListeningRoom = new P_ListeningRoom();
                pListeningRoom.setMem_no(olderMemNo);
                pListeningRoom.setDevice_uuid(deviceVo.getDeviceUuid());
                List<String> listeningRoom = loginDao.selectListeningRoom(pListeningRoom);
                for(String room_no : listeningRoom){
                    try{
                        //회원 룸 종료
                        P_RoomExitVo exitData = new P_RoomExitVo();
                        exitData.setMemLogin(1);
                        exitData.setMem_no(olderMemNo);
                        exitData.setRoom_no(room_no);
                        exitData.setOs(deviceVo.getOs());
                        exitData.setDeviceUuid(deviceVo.getDeviceUuid());
                        exitData.setIp(deviceVo.getIp());
                        exitData.setAppVersion(deviceVo.getAppVersion());
                        exitData.setDeviceToken(deviceVo.getDeviceToken());
                        exitData.setIsHybrid(deviceVo.getIsHybrid());
                        ProcedureVo procedureExitVo = new ProcedureVo(exitData);
                        roomDao.callBroadCastRoomExit(procedureExitVo);
                    }catch(Exception e){}

                    try{
                        //방참가를 위한 토큰 조회
                        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(room_no);
                        P_RoomJoinVo joinData = new P_RoomJoinVo();
                        joinData.setMemLogin(0);
                        joinData.setMem_no(newMemNo);
                        joinData.setRoom_no(room_no);
                        joinData.setGuest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"));
                        joinData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
                        joinData.setOs(deviceVo.getOs());
                        joinData.setDeviceUuid(deviceVo.getDeviceUuid());
                        joinData.setIp(deviceVo.getIp());
                        joinData.setAppVersion(deviceVo.getAppVersion());
                        joinData.setDeviceToken(deviceVo.getDeviceToken());
                        joinData.setIsHybrid(deviceVo.getIsHybrid());
                        ProcedureVo procedureJoinVo = new ProcedureVo(joinData);
                        roomDao.callBroadCastRoomJoin(procedureJoinVo);
                    }catch(Exception e){}
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            log.info(e.getMessage());
        }
    }
}
