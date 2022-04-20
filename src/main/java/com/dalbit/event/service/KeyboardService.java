package com.dalbit.event.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.Keyboard;
import com.dalbit.event.vo.KeyboardRewardVO;
import com.dalbit.event.vo.KeyboardWinVO;
import com.dalbit.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyboardService {

    private final Keyboard keyboard;
    private final GsonUtil gsonUtil;

    public String keyboardWinList(){
        LocalDateTime now =LocalDateTime.now();
        String tDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-19"));
        List<KeyboardWinVO> list = keyboard.keyboardWinList(tDate);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, list));
    }

    public String keyboardReward(KeyboardRewardVO vo){
        LocalDateTime now =LocalDateTime.now();
        String tDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-19"));
        vo.setTheDate(tDate);
        log.warn("{}", vo);
        Integer result = keyboard.keyboardReward(vo);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result));
    }

}
