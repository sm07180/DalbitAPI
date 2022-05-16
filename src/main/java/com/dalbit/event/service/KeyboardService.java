package com.dalbit.event.service;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.Keyboard;
import com.dalbit.event.vo.KeyboardBonusVO;
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
        LocalDateTime now = LocalDateTime.now();
        String tDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<KeyboardWinVO> list = keyboard.keyboardWinList(tDate);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, list));
    }

    public String keyboardReward(KeyboardRewardVO vo){
        LocalDateTime now =LocalDateTime.now();
        String tDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        vo.setTheDate(tDate);
        Integer result = keyboard.keyboardReward(vo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.생성, result));
    }

    public String keyboardMyInfo(String memNo) {
        KeyboardBonusVO keyboardBonusVO = keyboard.keyboardMyInfo(memNo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, keyboardBonusVO != null ? keyboardBonusVO : new KeyboardBonusVO()));
    }

    public String getBonusItem(String memNo, String bonusSlct) {
        LocalDateTime now =LocalDateTime.now();
        String tDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-01"));
        Integer result = keyboard.getBonusItem(memNo, tDate, bonusSlct);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.생성, result));
    }
}
