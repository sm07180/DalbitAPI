package com.dalbit.event.service;

import com.dalbit.common.code.Status;
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
        List<Object> objects = event.pTbEventRbdTailList("0", pageNo, pagePerCnt);
        Integer listCnt = DBUtil.getData(objects, 0, Integer.class);
        List<PoemEventResVo> list = DBUtil.getList(objects, 1, PoemEventResVo.class);
        list.stream().parallel().forEach(item -> {
            item.setProfImg(new ImageVo(item.getImage_profile(), "n", DalbitUtil.getProperty("server.photo.url")));
        });
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("listCnt", listCnt);
        resultMap.put("list", list);
        if(!memNo.equals("false")){
            resultMap.put("myPoem", getMyPoem(memNo));
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }

    public PoemEventResVo getMyPoem(String memNo){
        List<Object> objects = event.pTbEventRbdTailList(memNo, 1, 1);
        List<PoemEventResVo> list = DBUtil.getList(objects, 1, PoemEventResVo.class);
        if(!list.isEmpty()){
            list.stream().parallel().forEach(item -> {
                item.setProfImg(new ImageVo(item.getImage_profile(), "n", DalbitUtil.getProperty("server.photo.url")));
            });
            return list.get(0);
        }else{
            return null;
        }
    }


    public String savePoem(PoemEventReqVo poemEventReqVo){
        Integer result = event.pTbEventRbdTailIns(poemEventReqVo);
        return gsonUtil.toJson(new JsonOutputVo(Status.생성, result));
    }


    public String deletePoem(String tailNo, String tailMemNo){
        Integer result = event.pTbEventRbdTailDel(tailNo, tailMemNo);
        return gsonUtil.toJson(new JsonOutputVo(Status.삭제, result));
    }

    public String editPoem(PoemEventReqVo poemEventReqVo){
        Integer result = event.pTbEventRbdTailUpd(poemEventReqVo);
        return gsonUtil.toJson(new JsonOutputVo(Status.수정, result));
    }

}
