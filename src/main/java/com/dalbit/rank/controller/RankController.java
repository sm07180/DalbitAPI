package com.dalbit.rank.controller;

import com.dalbit.rank.service.RankService;
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
public class RankController {
    @Autowired RankService rankService;

    /**********************************************************************************************
     * @Method 설명 : 파트너DJ리스트
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/getPartnerDjList")
    public String getPartnerDjList(@RequestBody Map map, HttpServletRequest request){
        return rankService.getPartnerDjList(map, request);
    }

    /**********************************************************************************************
     * @Method 설명 : 내 스디 점수
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/getStarDjScore")
    public String getStarDjScore(@RequestBody Map map, HttpServletRequest request){
        return rankService.getStarDjScore(map, request);
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 신청
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @RequestMapping("/starDjIns")
    public String starDjIns(Map map, HttpServletRequest request){
        return rankService.starDjIns(map, request);
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 약력
     * @작성일 : 2022-04-20
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/getStarDjLog")
    public String getStarDjLog(@RequestBody Map map, HttpServletRequest request){
        return rankService.getStarDjLog(map, request);
    }
}
