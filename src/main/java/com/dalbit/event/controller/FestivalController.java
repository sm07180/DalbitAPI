package com.dalbit.event.controller;

import com.dalbit.event.service.FestivalService;
import com.dalbit.event.vo.FestivalGroundInputVo;
import com.dalbit.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event/festival")
@Slf4j
public class FestivalController {
    @Autowired FestivalService festivalService;

    /**
     *  2주년 이벤트 선물 지급
     */
    @PostMapping("/gift/ins")
    public String twoYearGiftBoxIns(@RequestParam(value = "memPhone") String memPhone, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        return festivalService.twoYearGiftBoxIns(memNo, memPhone);
    }


    /**
     *  2주년 이벤트 선물 지급 확인
     */
    @GetMapping("/gift/check")
    public String twoYearGiftBoxCheck(HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        return festivalService.twoYearGiftBoxCheck(memNo);
    }

    /**
     *  2주년 이벤트 사연 리스트
     */
    @GetMapping("/story/list")
    public String twoYearStoryList(
        @RequestParam(value = "memNo", defaultValue = "0") String memNo,
        @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
        @RequestParam(value = "pagePerCnt", defaultValue = "50") String pagePerCnt
    ) {
        return festivalService.twoYearStorySelect(memNo, pageNo, pagePerCnt);
    }

    /**
     *  2주년 이벤트 사연 등록
     */
    @PostMapping("/story/ins")
    public String twoYearStoryIns(@RequestParam(value = "storyConts") String storyConts, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        return festivalService.twoYearStoryIns(memNo, storyConts);
    }

    /**
     *  2주년 이벤트 사연 수정
     */
    @PostMapping("/story/upd")
    public String twoYearStoryUpd(
        @RequestParam(value = "storyNo") String storyNo,
        @RequestParam(value = "storyConts") String storyConts
    ) {
        return festivalService.twoYearStoryUpd(storyNo, storyConts);
    }

    /**
     *  2주년 이벤트 사연 삭제
     */
    @DeleteMapping("/story/del")
    public String twoYearStoryDel(
        @RequestParam(value = "storyNo") String storyNo,
        @RequestParam(value = "delChrgrName") String delChrgrName
    ) {
        return festivalService.twoYearStoryDel(storyNo, delChrgrName);
    }

    /**
     *  2주년 이벤트 그라운드 DJ탭
     */
    @GetMapping("/ground/dj")
    public String twoYearGroundDj(@RequestBody FestivalGroundInputVo festivalGroundInputVo) {
        return festivalService.twoYearGroundDj(festivalGroundInputVo);
    }

    /**
     *  2주년 이벤트 그라운드 시청자 탭
     */
    @GetMapping("/ground/viewer/list")
    public String twoYearGroundViewer(@RequestBody FestivalGroundInputVo festivalGroundInputVo) {
        return festivalService.twoYearGroundViewer(festivalGroundInputVo);
    }
}
