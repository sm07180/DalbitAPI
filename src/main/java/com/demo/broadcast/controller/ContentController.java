package com.demo.broadcast.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("brod")
public class ContentController {

    @Autowired
    private MessageUtil messageUtil;

    /**
     * 공지조회
     */
    @GetMapping("{brodNo}/notice")
    public String getNotice(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }

    /**
     * 공지등록/수정
     */
    @PostMapping("{brodNo}/notice")
    public String postNotice(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.수정, map)));
    }

    /**
     * 공지삭제
     */
    @DeleteMapping("{brodNo}/notice")
    public String deleteNotice(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.수정, map)));
    }

    /**
     * 사연조회
     */
    @GetMapping("{brodNo}/story")
    public String getStory(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }

    /**
     * 사연등록
     */
    @PostMapping("{brodNo}/story")
    public String insertStory(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, map)));
    }
}
