package com.dalbit.common.service;

import com.dalbit.broadcast.vo.P_RoomJoinTokenVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.CodeVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommonService {

    @Autowired
    CommonDao commonDao;

    /**
     * 방송방 참가를 위해 스트림아이디 토큰아이디 받아오기
     */
    public HashMap callBroadCastRoomStreamIdRequest(String roomNo) throws GlobalException {
        P_RoomJoinTokenVo apiData = new P_RoomJoinTokenVo();
        apiData.setRoom_no(roomNo);
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        commonDao.callBroadCastRoomStreamIdRequest(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);

        String tokenFailData = "";

        if (procedureVo.getRet().equals(Status.방송참여토큰_해당방이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_해당방이없음, procedureVo.getData());
        } else if (procedureVo.getRet().equals(Status.방송참여토큰_방장이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_방장이없음, procedureVo.getData());
        } else if(procedureVo.getRet().equals(Status.방송참여토큰발급_실패.getMessageCode())){
            throw new GlobalException(Status.방송참여토큰발급_실패, procedureVo.getData());
        }

        boolean isTokenSuccess = procedureVo.getRet().equals(Status.방송참여토큰발급.getMessageCode());
        resultMap.put("isTokenSuccess", isTokenSuccess);
        resultMap.put("tokenFailData", tokenFailData);

        log.debug("방송방 참여 토큰발급 결과 [{}]: {}", isTokenSuccess, tokenFailData);

        return resultMap;
    }

    @Cacheable(cacheNames = "codeCache", key = "#code")
    public HashMap getCodeCache(String code) {
        return callCodeDefineSelect();
    }

    @CachePut(cacheNames = "codeCache", key = "#code")
    public HashMap updateCodeCache(String code) {
        return callCodeDefineSelect();
    }

    public HashMap callCodeDefineSelect(){
        List<Map> data = commonDao.callCodeDefineSelect();

        HashMap<String, Object> result = new HashMap<>();
        result.put("memGubun", setData(data, "mem_slct"));
        result.put("osGubun", setData(data, "os_type"));
        result.put("roomType", setData(data, "subject_type"));
        result.put("roomState", setData(data, "broadcast_state"));
        result.put("roomRight", setData(data, "broadcast_auth"));
        result.put("declarReason", setData(data, "report_reason"));

        return result;
    }

    public List<CodeVo> setData(List<Map> list, String type) {
        List<CodeVo> data = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                if(type.equals((String)list.get(i).get("type"))){
                    data.add(new CodeVo((String)list.get(i).get("value"), (String)list.get(i).get("code"), i));
                }
            }
        }
        return data;
    }
}
