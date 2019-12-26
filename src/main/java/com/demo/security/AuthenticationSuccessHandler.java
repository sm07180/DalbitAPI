package com.demo.security;

import com.demo.code.Status;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import com.demo.vo.helper.JsonOutputVo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@Component("authSuccessHandler")
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    MessageUtil messageUtil;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        boolean isEmptyRedirectUrl = StringUtil.isEmpty(request.getParameter("redirectUrl"));

        HashMap map = new HashMap();
        map.put("returnUrl", isEmptyRedirectUrl ? "/sample" : request.getParameter("redirectUrl"));

        loginResult(response, new JsonOutputVo(Status.로그인, map));
    }

    /**
     * 로그인 결과 리턴
     * @param response
     * @param jsonOutputVo
     * @throws IOException
     */
    public void loginResult(HttpServletResponse response, JsonOutputVo jsonOutputVo) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(messageUtil.setJsonOutputVo(jsonOutputVo)));
        out.flush();
        out.close();
    }
}
