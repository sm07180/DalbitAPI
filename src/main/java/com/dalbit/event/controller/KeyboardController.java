package com.dalbit.event.controller;

import com.dalbit.event.service.KeyboardService;
import com.dalbit.event.vo.KeyboardRewardVO;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/event/keyboard")
@Slf4j
public class KeyboardController {

    private final KeyboardService keyboardService;

    @GetMapping
    public String keyboardWinList(){
        return keyboardService.keyboardWinList();
    }

    @PostMapping
    public String keyboardReward(@RequestBody @Valid KeyboardRewardVO vo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return keyboardService.keyboardReward(vo);
    }

    @GetMapping("/bonus")
    public String keyboardMyInfo(@RequestParam String memNo){
        return keyboardService.keyboardMyInfo(memNo);
    }

    @PostMapping("/bonus")
    public String getBonusItem(@RequestBody HashMap<String, Object> payload){
        String memNo = (String) payload.get("memNo");
        String bonusSlct = (String) payload.get("bonusSlct");
        return keyboardService.getBonusItem(memNo, bonusSlct);
    }


}


