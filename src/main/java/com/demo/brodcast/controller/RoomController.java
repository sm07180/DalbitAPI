package com.demo.brodcast.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/brod")
public class RoomController {

    @Autowired
    MessageUtil messageUtil;

    /**
     * 방송생성
     */
    @ApiOperation(value = "방송생성")
    @PostMapping("")
    public String createBrod( @RequestParam(value = "s_gubun", required = true, defaultValue = "BROD00100") String s_gubun,
                        @RequestParam(value = "s_bgPath", required = false) String s_bgPath,
                        @RequestParam(value = "s_bgNm", required = false) String s_bgNm,
                        @RequestParam(value = "s_title", required = true, defaultValue = "제목") String s_title,
                        @RequestParam(value = "s_intro", required = true, defaultValue = "안녕하세요!") String s_intro,
                        @RequestParam(value = "s_fan", required = true, defaultValue = "N") String s_fan,
                        @RequestParam(value = "s_over20", required = true, defaultValue = "N") String s_over20){

        HashMap map = new HashMap();
        map.put("result", "success");
        map.put("brodNo", "BRD00121");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성, map)));
    }

    /**
     * 방송종료
     */
    @ApiOperation(value = "방송종료")
    @DeleteMapping("/{brodNo}")
    public String deleteBrod(@PathVariable String brodNo ){

        HashMap map = new HashMap();
        map.put("result", "success");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송종료, map)));
    }

    /**
     * 방송참여
     */
    @ApiOperation(value = "방송참여")
    @PostMapping("/{brodNo}/in")
    public String inBrod(@PathVariable String brodNo ){

        HashMap map = new HashMap();
        map.put("result", "success");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여, map)));
    }

    /**
     * 방송 나가기
     */
    @ApiOperation(value = "방송나가기")
    @PostMapping("/{brodNo}/out")
    public String outBrod(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("result", "success");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기, map)));
    }

    /**
     * 방송 정보조회
     */
    @ApiOperation(value = "방송 정보조회")
    @GetMapping("/{brodNo}")
    public String getBrod(@PathVariable String brodNo){

        HashMap result = new HashMap();
        result.put("result", "success");

        HashMap data = new HashMap();
        data.put("gubun", "BROD00100");
        data.put("gubunNM", "일상");
        data.put("bgImg", " ");
        data.put("title", "방 제목입니다.이렇게!");
        data.put("intro", "안녕하세요! \n 제 방송 즐겁게 즐기다 가세요!");
        data.put("isFan", true);
        data.put("isOver20", false);

        HashMap owner = new HashMap();
        owner.put("owner", "방장정보");
        owner.put("img", "대표이미지");
        owner.put("memNo", "M000125");
        owner.put("nickNm", "방장닉네임");

        HashMap guest = new HashMap();
        guest.put("guest", "게스트정보");
        guest.put("img", "대표이미지");
        guest.put("memNo", "M000125");
        guest.put("nickNm", "게스트닉네임");

        HashMap managers = new HashMap();
        managers.put("guest", "매니저정보");
        managers.put("img", "대표이미지");
        managers.put("memNo", "M000125");
        managers.put("nickNm", "매니저닉네임");

        ArrayList list = new ArrayList();
        list.add(result);
        list.add(data);
        list.add(owner);
        list.add(guest);
        list.add(managers);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, list)));
    }

    /**
     * 방송 정보수정
     */
    @ApiOperation(value = "방송 정보수정")
    @PostMapping("/{brodNo}")
    public String updateBrod(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("result", "success");

//        map.put("s_gubun", "BROD00100");
//        map.put("s_bgPath", "/2019/12/05/15");
//        map.put("s_bgNm", "1231567454123.jpg");
//        map.put("title", "방 제목입니다.이렇게!");
//        map.put("s_intro", "안녕하세요! \n 제 방송 즐겁게 즐기다 가세요!");
//        map.put("s_over20", "N");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.수정, map)));
    }
}

