package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.TTSService;
import com.dalbit.broadcast.vo.TTSCallbackVo;
import com.dalbit.broadcast.vo.TTSSpeakVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String ttsCallback(@Valid TTSCallbackVo ttsCallbackVo) {
        return ttsService.ttsCallback(ttsCallbackVo);
    }
}
