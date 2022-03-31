package com.dalbit.store.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.store.service.StoreService;
import com.dalbit.store.vo.StoreChargeVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@Scope("prototype")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    GsonUtil gsonUtil;

    @GetMapping("/store/charge")
    public String getChargeList(HttpServletRequest request){
        List<StoreChargeVo> list = storeService.getStoreChargeList(request);
        HashMap data = new HashMap();
        data.put("dalCnt", storeService.getDalCnt(request));
        data.put("dalPriceList", list == null ? new ArrayList<>() : list);
        data.put("defaultNum", 2); //IOS 스토어 기본값 설정 위한 값
        return  gsonUtil.toJson(new JsonOutputVo(Status.조회, data));
    }

    @PostMapping("/paycall/store")
    public String getChargeListByParam(HttpServletRequest request){
        StoreChargeVo storeChargeVo = storeService.getStoreChargeListByParam(request);
        return  gsonUtil.toJson(new JsonOutputVo(Status.조회, DalbitUtil.isEmpty(storeChargeVo) ? null : storeChargeVo));
    }

    @PostMapping("/store/getIndexData")
    public Object getIndexData(HttpServletRequest request){
        return storeService.getIndexData(request);
    }
    @PostMapping("/store/getDalPriceList")
    public Object getDalPriceList(@RequestParam(value = "platform") String platform, HttpServletRequest request){
        return storeService.getDalPriceList(platform, request);
    }
}
