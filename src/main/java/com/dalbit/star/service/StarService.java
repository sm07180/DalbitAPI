package com.dalbit.star.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.main.proc.MainPage;
import com.dalbit.main.vo.MyRankVO;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StarService {
    @Autowired MainPage mainPage;
    @Autowired GsonUtil gsonUtil;

    public String getStarList(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        String photoSvrUrl = DalbitUtil.getProperty("server.photo.url");
        String result;
        List<Object> mystar = mainPage.getMyStar(map);
        mystar.add(photoSvrUrl);

        if (memNo == null) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_요청회원_정보없음));
        } else if (mystar.size() > 0){
            result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, mystar));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
        return result;
    }
}
