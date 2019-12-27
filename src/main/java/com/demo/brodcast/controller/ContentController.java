package com.demo.brodcast.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
