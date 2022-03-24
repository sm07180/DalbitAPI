package com.dalbit.rank.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rank.proc.Rank;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RankService {
    @Autowired Rank rank;
    @Autowired
    GsonUtil gsonUtil;

    public String getPartnerDjList(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        List<Object> result = rank.getPartnerDjList(map);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result));
    }
}
