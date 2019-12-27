package com.demo.brodcast.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/brod")
public class RoomController {

    @Autowired
    MessageUtil messageUtil;

    /**
     * 방송생성
     */
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
    @DeleteMapping("/{brodNo}")
    public String deleteBrod(@PathVariable String brodNo ){

        HashMap map = new HashMap();
        map.put("result", "success");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송종료, map)));
    }

    /**
     * 방송참여
     */
    @PostMapping("/{brodNo}/in")
    public String inBrod(@PathVariable String brodNo ){

        HashMap map = new HashMap();
        map.put("result", "success");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여, map)));
    }

    /**
     * 방송 나가기
     */
    @PostMapping("/{brodNo}/out")
    public String outBrod(@PathVariable String brodNo ){

        HashMap map = new HashMap();
        map.put("result", "success");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기, map)));
    }

    /**
     * 방송 정보조회
     */
    @GetMapping("/{brodNo}")
    public String getBrod(@PathVariable String brodNo ){

        HashMap map = new HashMap();
        map.put("result", "success");
        map.put("gubun", "BROD00100");
        map.put("gubunNM", "일상");
        map.put("bgImg", " ");
        map.put("title", "방 제목입니다.이렇게!");
        map.put("intro", "안녕하세요! \n 제 방송 즐겁게 즐기다 가세요!");
        map.put("isFan", true);
        map.put("isOver20", false);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }
}
