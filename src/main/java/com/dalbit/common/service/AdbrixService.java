package com.dalbit.common.service;

import com.dalbit.common.dao.AdbrixDao;
import com.dalbit.common.vo.AdbrixLayoutVo;
import com.dalbit.common.vo.AdbrixVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdbrixService {

    @Autowired
    private AdbrixDao adbrixDao;

    public AdbrixLayoutVo makeAdbrixData(String eventName, String memNo){

        AdbrixVo adbrixVo = adbrixDao.getMakeAdbrixData(memNo);

        AdbrixLayoutVo adbrixLayoutVo = new AdbrixLayoutVo();
        adbrixLayoutVo.setEventName(eventName);
        adbrixLayoutVo.setAttr(DalbitUtil.isEmpty(adbrixVo) ? new AdbrixVo() : adbrixVo);

        /*if(adbrixVo.getKorAge() < 10){
            adbrixVo.setAge("0~9세");
        }else{
            adbrixVo.setAge(Integer.toString(adbrixVo.getKorAge()).substring(0,1) + "0대");
        }*/

        return adbrixLayoutVo;
    }
}
