package com.demo.exception.conrtoller;

import com.demo.common.code.ErrorStatus;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
public class CommonErrorController implements ErrorController {

    @Autowired
    MessageUtil messageUtil;

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request){

        Object errorStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorRequestUrl = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        HashMap map = new HashMap();
        map.put("httpErrorCode", errorStatus);
        map.put("requestUrl", errorRequestUrl);
        map.put("message", errorMessage);

        log.error(new Gson().toJson(map));

        //return new Gson().toJson(messageUtil.setExceptionInfo(ErrorStatus.잘못된호출, map));
        return new ModelAndView("error/"+errorStatus);
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
