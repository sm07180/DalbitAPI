package com.dalbit.security.handler;

import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authFailureHandler")
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Autowired GsonUtil gsonUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{

        gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(((CustomUsernameNotFoundException) exception).getStatus()));
    }
}
