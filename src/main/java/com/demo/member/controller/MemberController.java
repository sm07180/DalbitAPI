package com.demo.member.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.member.service.MemberService;
import com.demo.member.vo.P_LoginVo;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
//@RestController
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    private GsonUtil gsonUtil;
    @Autowired
    MemberService memberService;


    /**
     * 참여자목록
     */
    @ApiOperation(value = "참여자목록")
    @PostMapping("/{brodNo}/users")
    public String selectUsers(@PathVariable String brodNo){

        HashMap data = new HashMap(); //유저정보
        data.put("memNo", "M000125");
        data.put("nickNm", "하늘하늘이에요");
        data.put("grade", "dj");
        data.put("isFan", true);

        HashMap img = new HashMap(); //이미지 정보
        img.put("url", "https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        img.put("path", "/2019/12/05/15/1231567454123.jpg");
        img.put("name", "/1231567454123.jpg");

        HashMap paging = new HashMap(); //페이징정보
        paging.put("total ", "102");
        paging.put("recordperpage  ", "10");
        paging.put("page  ", "1");
        paging.put("prev  ", "0");
        paging.put("next   ", "2");
        paging.put("totalpage   ", "21");

        data.put("img", img);
        data.put("paging", paging);



        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, data)));
    }
}
