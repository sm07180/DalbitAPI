package com.dalbit.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
public class ParamCheckInterceptor extends HandlerInterceptorAdapter {

    private final String[] IGNORE_URLS = {
        "/ctrl/check/service", "/self/auth", "/admin/"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        String uri = request.getRequestURI();
        for(String ignoreUri : IGNORE_URLS) {
            if(uri.startsWith(ignoreUri)) {
                return true;
            }
        }

        log.info("========================== Start Request uri = " + request.getRequestURI());
        for(Enumeration<String> itertor = (Enumeration<String>)request.getParameterNames(); itertor.hasMoreElements();){
            String key = itertor.nextElement();
            String[] values = request.getParameterValues(key);
            if (values != null && values.length > 0){
                if(values.length == 1){
                    log.info("========================================= " + request.getRequestURI() + " " + key + " = |" + values[0] + "|");
                }else{
                    for(int i = 0; i < values.length; i++){
                        log.info("========================================= " + request.getRequestURI() + " " + key + "." + i + " = |" + values[i] + "|");
                    }
                }
            }
        }
        log.info("========================== " + request.getRequestURI() + " HEADER authToken : " + request.getHeader("authToken"));
        log.info("========================== End Request uri = " + request.getRequestURI());

        return true;
    }
}
