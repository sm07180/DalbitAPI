package com.dalbit.security.handler;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.procedure.P_MemberBroadcastingCheckVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
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
    GsonUtil gsonUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){

        DalbitUtil.setHeader(request, response);

        ProcedureVo procedureVo = roomService.callMemberBroadcastingCheck(new P_MemberBroadcastingCheckVo(MemberVo.getMyMemNo(request)));
        log.debug("방송중인 DJ체크 : {}", procedureVo.toString());

        HashMap broadcastingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String state = DalbitUtil.getStringMap(broadcastingInfo, "state");
        boolean isBroadcast = state.equals(Code.방송중체크_방송중.getCode()) ? true : false;

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
            CookieUtil.deleteCookie(DalbitUtil.getProperty("server.servlet.session.cookie.name"), "", "", 0);
            CookieUtil.deleteCookie(DalbitUtil.getProperty("sso.cookie.name"), "", "", 0);

            memberService.refreshAnonymousSecuritySession("anonymousUser");

            HashMap<String, Object> result = commonService.getJwtTokenInfo(request);
            gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그아웃성공, result.get("tokenVo")));

        } catch (IOException e) {
            //e.printStackTrace();
            log.info(e.getMessage());
        }
    }
}
