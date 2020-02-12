package com.dalbit.security.handler;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.procedure.P_MemberBroadcastingCheckVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.CookieUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
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
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,"+DalbitUtil.getProperty("sso.header.cookie.name")+","+DalbitUtil.getProperty("rest.custom.header.name")+",redirectUrl,Proxy-Client-IP,WL-Proxy-Client-IP,X-Forwarded-For");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        ProcedureVo procedureVo = roomService.callMemberBroadcastingCheck(new P_MemberBroadcastingCheckVo(MemberVo.getMyMemNo()));
        log.debug("방송중인 DJ체크 : {}", procedureVo.toString());
        if(DalbitUtil.isLogin() && procedureVo.getRet().equals(Status.방송중인DJ체크_방송중.getMessageCode())){

            try {
                gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그아웃실패_진행중인방송));

            }catch (Exception e){
                log.error("LogoutHandlerImpl.logout : {}", e.getMessage());
            }
        }else{

            try {
                CookieUtil.deleteCookie(DalbitUtil.getProperty("server.servlet.session.cookie.name"), "", "", 0);
                CookieUtil.deleteCookie(DalbitUtil.getProperty("sso.cookie.name"), "", "", 0);

                memberService.refreshAnonymousSecuritySession("anonymousUser");

                HashMap<String, Object> result = commonService.getJwtTokenInfo(request);
                gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그아웃성공, result.get("tokenVo")));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
