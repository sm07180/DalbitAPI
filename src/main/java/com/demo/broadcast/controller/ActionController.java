package com.demo.broadcast.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("brod")
public class ActionController {

    @Autowired
    private MessageUtil messageUtil;

    /**
     * 받은선물내역
     */
    @GetMapping("{brodNo}/gift")
    public String getGiftHistory(@PathVariable String brodNo){

        HashMap data = new HashMap();

        HashMap summary = new HashMap();
        summary.put("receives", 23);
        summary.put("golds", 2407);
        data.put("summary", summary);

        data.put("memNo", "M1001234");
        data.put("img", "/files/filePath/filename.png");
        data.put("nickNm", "하늘하늘이에요");
        data.put("gift", "치킨");
        data.put("combos", 3);
        data.put("golds", 30);
        data.put("date", StringUtil.getTimeStamp());

        HashMap paging = new HashMap();
        paging.put("total", 102);
        paging.put("recordperpage", 10);
        paging.put("page", 1);
        paging.put("prev", 0);
        paging.put("next", 2);
        paging.put("totalpage", 21);
        data.put("paging", paging);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, data)));
    }

    /**
     * 선물하기
     */
    @PostMapping("{brodNo}/gift")
    public String sendGift(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);
        data.put("giftNo", "GT005421");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }

    /**
     * 선물액션조회
     */
    @GetMapping("{brodNo}/gift/{giftNo}")
    public String selectGiftNo(@PathVariable String brodNo, @PathVariable String giftNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);
        data.put("giftNo", giftNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }

    /**
     * 좋아요
     */
    @PostMapping("{brodNo}/likes")
    public String likes(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.좋아요, data)));
    }

    /**
     * 부스트
     */
    @PostMapping("{brodNo}/boost")
    public String boost(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }

    /**
     * 신고하기
     */
    @PostMapping("{brodNo}/declar")
    public String declar(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }
}
