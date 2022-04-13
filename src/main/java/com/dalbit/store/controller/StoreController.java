package com.dalbit.store.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.store.etc.Store;
import com.dalbit.store.service.StoreService;
import com.dalbit.store.vo.StoreChargeVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Scope("prototype")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    GsonUtil gsonUtil;

    /*
    * [ios inApp 결제 전용] native -> api
    * 충전시 아이템 목록 불러옴
    * dalCnt, list, defaultNum 바꾸려면 강업해야함
    * */
    @GetMapping("/store/charge")
    public String getChargeList(HttpServletRequest request){
        // List<StoreChargeVo> list = storeService.getStoreChargeList(request);
        Map<String, Object> data = new HashMap<>();
        data.put("dalCnt", storeService.getDalCnt(request));
        data.put("list", storeService.getDalPriceList(Store.ModeType.IN_APP, request));
        data.put("defaultNum", 2); //IOS 스토어 기본값 설정 위한 값
        return  gsonUtil.toJson(new JsonOutputVo(Status.조회, data));
    }

    // 버전분기 처리용
    @GetMapping("/store/getOtherPriceList")
    public String getTargetPriceList(HttpServletRequest request){
        Map<String, Object> data = new HashMap<>();
        data.put("dalCnt", storeService.getDalCnt(request));
        data.put("list", storeService.getTargetPriceList(Store.Platform.OTHER));
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
    public Object getDalPriceList(@RequestParam(value = "platform", required = false) String platform, HttpServletRequest request){
        return storeService.selectChargeItem(platform, request);
    }
    @PostMapping("/store/getDalCnt")
    public Object getDalCnt(HttpServletRequest request){
        return storeService.getDalCntJsonStr(request);
    }
}
