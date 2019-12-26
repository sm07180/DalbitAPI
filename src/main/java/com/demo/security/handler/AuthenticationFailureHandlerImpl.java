package com.demo.security.handler;

import com.demo.common.code.Status;
import com.demo.util.MessageUtil;
import com.demo.common.vo.JsonOutputVo;
import com.google.gson.Gson;
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
import java.io.PrintWriter;
import java.util.HashMap;

@Component("authFailureHandler")
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Autowired
    MessageUtil messageUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String url = "/index";

        HttpSession session = request.getSession(false);
        if(session != null) {
            session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception.getMessage());
        }

        HashMap map = new HashMap();
        map.put("returnUrl", "/index");

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.로그인실패, map))));
        out.flush();
        out.close();
    }
}
