package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.TTSService;
import com.dalbit.broadcast.vo.TTSVo;
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
    public void ttsConnection(/*@Valid TTSVo ttsVo*/) {
        TTSVo tempVo = new TTSVo();
        tempVo.setText("tts 테스트입니다.");
        ttsService.ttsConnection(tempVo);
    }

    @PostMapping("/tts/callback")
    public String ttsCallback() {
        return ttsService.ttsCallback();
    }
}
