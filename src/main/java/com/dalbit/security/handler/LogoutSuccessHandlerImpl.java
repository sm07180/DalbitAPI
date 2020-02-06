package com.dalbit.security.handler;

import com.dalbit.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component("logoutHandler")
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    LoginUtil loginUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        try {
            log.info("onLogoutSuccess");

            //Cookie ssoCookie = loginUtil.expireSsoCookie();
            //httpServletResponse.addCookie(ssoCookie);

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
