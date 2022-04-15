package com.dalbit.main.controller;

import com.dalbit.main.service.MainServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/v2/main")
public class MainControllerV2 {
    @Autowired MainServiceV2 mainServiceV2;

    /**********************************************************************************************
    * @Method 설명 : 메인 페이지
    * @작성일   : 2022-01-18
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    @GetMapping("/page")
    public String main(HttpServletRequest request) {
        return mainServiceV2.main(request);
    }

    /**********************************************************************************************
    * @Method 설명 : 메인 페이지 NOW TOP 10
    * @작성일   : 2022-03-31
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    @GetMapping("/top/rank/list")
    public String nowTop10(@RequestParam(value = "callType", defaultValue = "DJ") String callType, HttpServletRequest request) {
        return mainServiceV2.nowTop10(callType, request);
    }

    @RequestMapping("/getMainSwiper")
    public String getMainSwiper(HttpServletRequest request) {
        return mainServiceV2.getMainSwiper(request);
    }
}
