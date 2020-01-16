package com.dalbit.broadcast.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/brod")
public class UserController {

    @Autowired
    MessageUtil messageUtil;

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

    /**
     * 매니저지정
     */
    @ApiOperation(value = "매니저지정")
    @PostMapping("/{brodNo}/mgr/{memNo}")
    public String addManager(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.매니저지정, data)));
    }

    /**
     * 매니저해제
     */
    @ApiOperation(value = "매니저해제")
    @DeleteMapping("/{brodNo}/mgr/{memNo}")
    public String deleteManager(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }

    /**
     * 게스트초대
     */
    @ApiOperation(value = "게스트초대")
    @PostMapping("/{brodNo}/gst/{memNo}/invite")
    public String inviteGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.게스트초대, data)));
    }

    /**
     * 게스트초대취소
     */
    @ApiOperation(value = "게스트초대취소")
    @DeleteMapping("/{brodNo}/gst/{memNo}/invite")
    public String deleteInviteGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }

    /**
     * 게스트초대수락
     */
    @ApiOperation(value = "게스트초대수락")
    @PostMapping("/{brodNo}/gst")
    public String joinGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.게스트초대수락, data)));
    }

    /**
     * 게스트신청
     */
    @ApiOperation(value = "게스트신청")
    @PostMapping("/{brodNo}/gst/{memNo}/apply")
    public String applyGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.게스트신청, data)));
    }

    /**
     * 게스트해제
     */
    @ApiOperation(value = "게스트해제")
    @DeleteMapping("/{brodNo}/gst")
    public String deleteGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }

    /**
     * 게스트나가기
     */
    @ApiOperation(value = "게스트나가기")
    @DeleteMapping("/{brodNo}/gst/out")
    public String outGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }
}
