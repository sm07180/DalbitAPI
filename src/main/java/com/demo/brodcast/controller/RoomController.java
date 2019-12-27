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
    public String brod( @RequestParam(value = "s_gubun", required = true, defaultValue = "BROD00100") String s_gubun,
                        @RequestParam(value = "s_bgPath", required = false) String s_bgPath,
                        @RequestParam(value = "s_bgNm", required = false) String s_bgNm,
                        @RequestParam(value = "s_title", required = true, defaultValue = "제목") String s_title,
                        @RequestParam(value = "s_intro", required = true, defaultValue = "안녕하세요!") String s_intro,
                        @RequestParam(value = "s_fan", required = true, defaultValue = "N") String s_fan,
                        @RequestParam(value = "s_over20", required = true, defaultValue = "N") String s_over20){

        HashMap map = new HashMap();
        map.put("result", "success");
        map.put("brodNo", "BRD00121");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }
}
