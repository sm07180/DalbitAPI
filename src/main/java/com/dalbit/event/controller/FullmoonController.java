package com.dalbit.event.controller;

import com.dalbit.event.service.FullmoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/event/fullmoon")
@Scope("prototype")
public class FullmoonController {

    @Autowired
    FullmoonService fullmoonService;

    @GetMapping("/info")
    public String fullmoonInfo(HttpServletRequest request){

        String result = fullmoonService.callFullmoonEventInfo(request);

        return result;
    }

    @PostMapping("/rank")
    public String fullmoonRank(HttpServletRequest request, @RequestParam HashMap paramMap){

        String result = fullmoonService.callFullmoonEventRanking(request, paramMap);

        return result;
    }
}
