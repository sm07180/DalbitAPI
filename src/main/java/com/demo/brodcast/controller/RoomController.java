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

        HashMap data = new HashMap();
        data.put("brodNo", "BRD00121");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성, data)));
    }

    /**
     * 방송종료
     */
    @ApiOperation(value = "방송종료")
    @DeleteMapping("/{brodNo}")
    public String deleteBrod(@PathVariable String brodNo ){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송종료, data)));
    }

    /**
     * 방송참여
     */
    @ApiOperation(value = "방송참여")
    @PostMapping("/{brodNo}/in")
    public String inBrod(@PathVariable String brodNo ){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여, data)));
    }

    /**
     * 방송 나가기
     */
    @ApiOperation(value = "방송나가기")
    @PostMapping("/{brodNo}/out")
    public String outBrod(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기, data)));
    }

    /**
     * 방송 정보조회
     */
    @ApiOperation(value = "방송 정보조회")
    @GetMapping("/{brodNo}")
    public String getBrod(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("gubun", "BROD00100");
        data.put("gubunNM", "일상");
        data.put("title", "방 제목입니다.이렇게!");
        data.put("intro", "안녕하세요! \n 제 방송 즐겁게 즐기다 가세요!");
        data.put("isFan", true);
        data.put("isOver20", false);

        HashMap bgImg = new HashMap();
        bgImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        bgImg.put("path","/2019/12/05/15");
        bgImg.put("name","1231567454123.jpg");

        data.put("bgImg", bgImg);

        HashMap owner = new HashMap();
        owner.put("memNo", "M000125");
        owner.put("nickNm", "방장닉네임");

        HashMap ownerImg = new HashMap();
        ownerImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        ownerImg.put("path","/2019/12/05/15");
        ownerImg.put("name","1231567454123.jpg");

        owner.put("img", ownerImg);
        data.put("owner", owner);

        HashMap guest = new HashMap();
        guest.put("memNo", "M000125");
        guest.put("nickNm", "게스트닉네임");

        HashMap guestImg = new HashMap();
        guestImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        guestImg.put("path","/2019/12/05/15");
        guestImg.put("name","1231567454123.jpg");

        guest.put("img", guestImg);
        data.put("guest", guest);

        HashMap managers = new HashMap();
        managers.put("guest", "매니저정보");
        managers.put("memNo", "M000125");
        managers.put("nickNm", "매니저닉네임");

        HashMap managersImg = new HashMap();
        managersImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        managersImg.put("path","/2019/12/05/15");
        managersImg.put("name","1231567454123.jpg");

        managers.put("img", managersImg);
        data.put("managers", managers);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, data)));
    }

    /**
     * 방송 정보수정
     */
    @ApiOperation(value = "방송 정보수정")
    @PostMapping("/{brodNo}")
    public String updateBrod(@PathVariable String brodNo){

        HashMap data = new HashMap();
        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.수정, data)));
    }
}

