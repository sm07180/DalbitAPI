package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.TTSService;
import com.dalbit.broadcast.vo.TTSCallbackVo;
import com.dalbit.broadcast.vo.TTSSpeakVo;
import com.dalbit.common.vo.ResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/broad")
public class TTSController {
    @Autowired TTSService ttsService;

    @GetMapping("/tts")
    public void ttsConnection(/*@Valid TTSSpeakVo ttsSpeakVo*/) {
        TTSSpeakVo tempVo = new TTSSpeakVo();
        tempVo.setTtsText("tts 테스트입니다.");
        ttsService.ttsConnection(tempVo);
    }

    @PostMapping("/tts/callback")
    public String ttsCallback(@RequestBody TTSCallbackVo ttsCallbackVo, HttpServletRequest request) {
        log.warn("ip : {}", request.getRemoteAddr());
        return ttsService.ttsCallback(ttsCallbackVo);
    }

    @GetMapping("/tts/actor/list")
    public ResVO getActorList() {
        ResVO resVO = new ResVO();
        try {
            resVO.setSuccessResVO(ttsService.findActor());
        } catch (Exception e) {
            resVO.setFailResVO();
            log.error("TTSController getActorList => {}", e);
        }

        return resVO;
    }
}
