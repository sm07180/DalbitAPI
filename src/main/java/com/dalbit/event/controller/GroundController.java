package com.dalbit.event.controller;

import com.dalbit.event.service.GroundService;
import com.dalbit.event.vo.GroundListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String groundMyRankList(@RequestParam(value = "teamNo", defaultValue = "") String teamNo) {
        return groundService.groundMyRankList(teamNo);
    }
}
