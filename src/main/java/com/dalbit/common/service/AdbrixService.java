package com.dalbit.common.service;

import com.dalbit.common.dao.AdbrixDao;
import com.dalbit.common.vo.AdbrixVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class AdbrixService {

    private static AdbrixDao adbrixDao;

    @Autowired
    private static AdbrixDao tmp_adbrixDao;

    @PostConstruct
    private void init () {
        adbrixDao = this.tmp_adbrixDao;
    }

    public static String makeAdbrixData(String eventName, String memNo){
        AdbrixVo adbrixVo = new AdbrixVo();
        adbrixVo = adbrixDao.getMakeAdbrixData(memNo);
        if(adbrixVo.getKorAge() < 10){
            adbrixVo.setAge("0~9세");
        }else{
            adbrixVo.setAge(Integer.toString(adbrixVo.getKorAge()).substring(0,1) + "0대");
        }
        adbrixVo.setEventName(eventName);

        return new Gson().toJson(adbrixVo);
    }
}
