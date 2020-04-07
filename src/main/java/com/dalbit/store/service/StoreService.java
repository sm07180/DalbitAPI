package com.dalbit.store.service;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.store.dao.StoreDao;
import com.dalbit.store.vo.StoreChargeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
public class StoreService {
    @Autowired
    private StoreDao storeDao;

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
        if("2".equals(request.getParameter("os"))){
            platform = "001";
        }
        return storeDao.selectChargeItem(platform);
    }
}
