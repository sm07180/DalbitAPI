package com.dalbit.rank.controller;

import com.dalbit.rank.service.RankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class RankController {
    @Autowired RankService rankService;


    @PostMapping("/getPartnerDjList")
    public String getPartnerDjList(@RequestBody Map map, HttpServletRequest request){
        return rankService.getPartnerDjList(map, request);
    }
}
