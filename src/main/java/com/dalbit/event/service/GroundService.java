package com.dalbit.event.service;

import com.dalbit.common.code.EventStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.Event;
import com.dalbit.event.vo.GroundListVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GroundService {
    @Autowired GsonUtil gsonUtil;
    @Autowired Event event;

    public String groundRankList() {
        String result = "";
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("groundNo", 1);
            param.put("pageNo", 1);
            param.put("pagePerCnt", 9999);

            List<Object> procResult = event.teamGroundRankList(param);
            Integer cnt = DBUtil.getData(procResult, 0, Integer.class);
            List<GroundListVo> list = DBUtil.getList(procResult, 1, GroundListVo.class);

            resultMap.put("cnt", cnt);
            resultMap.put("list", list);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드조회_성공, resultMap));
        } catch (Exception e) {
            log.error("GroundService groundRankList error => ", e);
            result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드조회_실패, resultMap));
        }

        return result;
    }

    public String groundMyRankList(String teamNo) {
        String result = "";
        HashMap<String, Object> resultMap = new HashMap<>();
        if(StringUtils.isEmpty(teamNo)) {
            return gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드_내순위_조회_팀번호_없음, resultMap));
        }
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("groundNo", 1);
            param.put("teamNo", teamNo);

            List<GroundListVo> list = event.teamGroundMyRankList(param);

            resultMap.put("list", list);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드_내순위_조회_성공, resultMap));
        } catch (Exception e) {
            log.error("GroundService groundRankList error => ", e);
            result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드_내순위_조회_실패, resultMap));
        }

        return result;
    }
}
