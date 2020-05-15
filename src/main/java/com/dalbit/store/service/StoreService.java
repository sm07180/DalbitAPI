package com.dalbit.store.service;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_MemberInfoVo;
import com.dalbit.store.dao.StoreDao;
import com.dalbit.store.vo.PayChargeVo;
import com.dalbit.store.vo.StoreChargeVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class StoreService {
    @Autowired
    private StoreDao storeDao;
    @Autowired
    MypageService mypageService;

    public List<StoreChargeVo> getStoreChargeList(HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        String platform = "110";
        if(deviceVo.getOs() == 2){
            platform = "001";
        }
        return storeDao.selectChargeItem(platform);
    }

    public List<StoreChargeVo> getStoreChargeListByParam(HttpServletRequest request){
        String platform = "110";
        String itemNo = request.getParameter("itemNo");
        if("2".equals(request.getParameter("os"))){
            platform = "001";
        }
        PayChargeVo payChargeVo = new PayChargeVo();
        payChargeVo.setItemNo(itemNo);
        payChargeVo.setPlatform(platform);
        return storeDao.selectPayChargeItem(payChargeVo);
    }

    public int getDalCnt(HttpServletRequest request){
        int dalCnt = 0;
        if(DalbitUtil.isLogin(request)){
            P_MemberInfoVo apiData = new P_MemberInfoVo();
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            String result = mypageService.callMemberInfo(apiData);
            HashMap infoView = new Gson().fromJson(result, HashMap.class);
            if(!DalbitUtil.isEmpty(infoView) && !DalbitUtil.isEmpty(infoView.get("result")) && "success".equals(infoView.get("result").toString()) && !DalbitUtil.isEmpty(infoView.get("data"))){
                HashMap infoMap = new Gson().fromJson(new Gson().toJson(infoView.get("data")), HashMap.class);
                if(!DalbitUtil.isEmpty(infoMap) && !DalbitUtil.isEmpty(infoMap.get("dalCnt"))){
                    dalCnt = DalbitUtil.getIntMap(infoMap, "dalCnt");
                }
            }
        }
        return dalCnt;
    }
}
