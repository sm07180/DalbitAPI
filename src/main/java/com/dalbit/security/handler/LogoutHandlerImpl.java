package com.dalbit.security.handler;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.database.D_MyBoardcastCountVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
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
import javax.servlet.http.HttpSession;
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

        int myBraodCastCount = roomService.getMyBroadcastCount(new D_MyBoardcastCountVo(MemberVo.getMyMemNo(), 3, 0));
        log.debug("나의 방송 카운트 : {}", myBraodCastCount);
        if(0 < myBraodCastCount){

            try {
                gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그아웃실패_진행중인방송));
                //throw new GlobalException(Status.로그아웃실패_진행중인방송));
            }catch (Exception e){
                log.error("LogoutHandlerImpl.logout : {}", e.getMessage());
            }
        }else{
            log.debug("cookie 삭제해야함.");
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
