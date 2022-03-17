package com.dalbit.star.controller;

import com.dalbit.star.service.StarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class StarAction {
    @Autowired StarService starService;
    @PostMapping("/myStar/list")
    public String getStarList(@RequestBody Map map, HttpServletRequest request){
        return starService.getStarList(map, request);
    }
}
