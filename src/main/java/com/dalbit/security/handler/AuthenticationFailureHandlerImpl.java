package com.dalbit.security.handler;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("authFailureHandler")
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Autowired GsonUtil gsonUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{

        Status status = ((CustomUsernameNotFoundException) exception).getStatus();
        Object data = ((CustomUsernameNotFoundException) exception).getData();

        log.debug("로그인 실패 : {}", MemberVo.getMyMemNo(request));
        log.debug("status : {}", status);
        log.debug("data : {}", data);

        gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(status, data));
    }
}
