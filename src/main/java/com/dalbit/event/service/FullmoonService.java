package com.dalbit.event.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.FullmoonDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
public class FullmoonService {

    @Autowired
    private FullmoonDao fullmoonDao;

    @Autowired
    private GsonUtil gsonUtil;

    /**
     * 보름달 이벤트 정보 조회
     */
    public String callFullmoonEventInfo(HttpServletRequest request) {

        ProcedureVo procedureVo = new ProcedureVo();
        HashMap eventInfoMap = fullmoonDao.callFullmoonEventInfo(procedureVo);

        String result = "";
        if(DalbitUtil.isEmpty(eventInfoMap)){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_진행중인이벤트없음));
        }

        result = gsonUtil.toJson(new JsonOutputVo(Status.조회, eventInfoMap));
        return result;
    }

    /**
     * 보름달 이벤트 랭킹(문법사, 문집사) 조회
     */
    public String callFullmoonEventRanking(HttpServletRequest request, HashMap paramMap) {

        int slct_type = DalbitUtil.getIntMap(paramMap, "slct_type");
        int fullmoon_idx = DalbitUtil.getIntMap(paramMap, "fullmoon_idx");
        if(!(slct_type == 1 || slct_type == 2) || fullmoon_idx <= 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류));
        }

        paramMap.put("mem_no", MemberVo.getMyMemNo());

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<HashMap> rankList = fullmoonDao.callFullmoonEventRanking(procedureVo);

        String result = "";
        if(DalbitUtil.isEmpty(rankList)){
            result = gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }

        rankList.parallelStream().forEach(data -> {
            data.put("profileImage", new ImageVo(DalbitUtil.getStringMap(data, "profileImage"), DalbitUtil.getStringMap(data, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        });

        result = gsonUtil.toJson(new JsonOutputVo(Status.조회, rankList));
        return result;
    }
}
