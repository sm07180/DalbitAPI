package com.demo.security.handler;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@Component("authFailureHandler")
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Autowired MessageUtil messageUtil;
    @Autowired GsonUtil gsonUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        gsonUtil.responseJsonOutputVoToJson(response, new JsonOutputVo(Status.로그인실패_패스워드틀림));
    }
}
