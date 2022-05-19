package com.dalbit.event.controller;

import com.dalbit.event.service.GroundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/event/ground")
public class GroundController {
    @Autowired GroundService groundService;

    @GetMapping("/ranking/list")
    public String groundRankList() {
        return groundService.groundRankList();
    }

    @GetMapping("/ranking/my")
    public String groundMyRankList(HttpServletRequest request) {
        return groundService.groundMyRankList(request);
    }
}
