package com.dalbit.event.service;

import com.dalbit.common.code.EventStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.Event;
import com.dalbit.event.vo.GroundInputVo;
import com.dalbit.event.vo.GroundListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.team.service.TeamService;
import com.dalbit.util.DBUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GroundService {
    @Autowired GsonUtil gsonUtil;
    @Autowired Event event;
    @Autowired TeamService teamService;

    public String groundRankList() {
        String result;
        HashMap<String, Object> resultMap = new HashMap<>();
        GroundInputVo groundInputVo = new GroundInputVo();
        try {
            List<Object> procResult = event.teamGroundRankList(groundInputVo);
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

    public String groundMyRankList(HttpServletRequest request) {
        String result;

        try {
            String memNo = MemberVo.getMyMemNo(request);
            Integer teamNo = teamService.getTeamMemInsChkV2(memNo);

            if(teamNo == 0) {
                result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드_내순위_조회_팀없음));
            }else {
                Map<String, Object> param = new HashMap<>();
                param.put("groundNo", 1);
                param.put("teamNo", teamNo);

                GroundListVo myRankInfo = event.teamGroundMyRankList(param);

                result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드_내순위_조회_성공, myRankInfo));
            }
        } catch (Exception e) {
            log.error("GroundService groundMyRankList error => ", e);
            result = gsonUtil.toJsonAdm(new JsonOutputVo(EventStatus.달라그라운드_내순위_조회_실패));
        }

        return result;
    }
}
