package com.dalbit.rest.controller;

import com.dalbit.rest.service.RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Profile({"local", "dev"})
@Controller
public class RestTestController {

    @Autowired
    private RestService restService;

    @GetMapping("/ex/rest")
    public String rest() throws Exception {
        return "rest";
    }

    @PostMapping("/ex/rest/img")
    @ResponseBody
    public Map<String, Object> img(HttpServletRequest request) throws Exception{
        return restService.imgDone(request.getParameter("tmpImg"), request.getParameter("delImg"));
    }

    @PostMapping("/ex/rest/broad")
    @ResponseBody
    public Map<String, Object> broad(HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        result.put("streamId", restService.antCreate(request.getParameter("roomNm")).get("streamId"));
        result.put("pubToken", restService.antToken((String)result.get("streamId"), "publish").get("tokenId"));
        result.put("playToken", restService.antToken((String)result.get("streamId"), "play").get("tokenId"));

        return result;
    }
}
