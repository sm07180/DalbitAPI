package com.dalbit.event.service;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.PoemEvent;
import com.dalbit.event.vo.PoemEventResVo;
import com.dalbit.event.vo.request.PoemEventReqVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PoemService {

    private final PoemEvent event;
    private final GsonUtil gsonUtil;

    public String getPoemList(String memNo, Integer pageNo, Integer pagePerCnt){
        List<Object> objects = event.pTbEventRbdTailList(memNo, pageNo, pagePerCnt);
        Integer listCnt = DBUtil.getData(objects, 0, Integer.class);
        List<PoemEventResVo> list = DBUtil.getList(objects, 1, PoemEventResVo.class);
        list.stream().parallel().forEach(item -> {
            item.setProfImg(new ImageVo(item.getImage_profile(), "n", DalbitUtil.getProperty("server.photo.url")));
        });
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("listCnt", listCnt);
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, resultMap));
    }


    public String savePoem(PoemEventReqVo poemEventReqVo){
        Integer result = event.pTbEventRbdTailIns(poemEventReqVo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.생성, result));
    }


    public String deletePoem(String tailNo, String tailMemNo){
        Integer result = event.pTbEventRbdTailDel(tailNo, tailMemNo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.삭제, result));
    }

    public String editPoem(PoemEventReqVo poemEventReqVo){
        Integer result = event.pTbEventRbdTailUpd(poemEventReqVo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.수정, result));
    }

}
