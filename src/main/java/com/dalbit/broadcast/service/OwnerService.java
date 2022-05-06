package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.OwnerDao;
import com.dalbit.broadcast.vo.OwnerResultVo;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.procedure.P_BroadcastSettingVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OwnerService {

    @Autowired
    OwnerDao ownerDao;
    @Autowired
    MypageDao mypageDao;
    @Autowired
    SocketService socketService;
    @Autowired
    GsonUtil gsonUtil;

    public String pDallaRoomMasterSel(String roomNo, String memNo, HttpServletRequest request) {
        if (StringUtils.isEmpty(roomNo)) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.파라미터오류, roomNo));
        }

        Map<String, String> returnMap = new HashMap<>();
        try {
            OwnerResultVo pDallaRoomMasterSel = ownerDao.pDallaRoomMasterSel(roomNo);

            P_BroadcastSettingVo beforeBroadcastSettingVo = new P_BroadcastSettingVo();
            beforeBroadcastSettingVo.setMem_no(memNo);
            ProcedureVo beforeSettingVo = new ProcedureVo(beforeBroadcastSettingVo);
            mypageDao.callBroadcastSettingSelect(beforeSettingVo);
            returnMap = new Gson().fromJson(beforeSettingVo.getExt(), HashMap.class);
            returnMap.putAll(BeanUtils.describe(pDallaRoomMasterSel));
            if(pDallaRoomMasterSel == null){
                return gsonUtil.toJson(new JsonOutputVo(CommonStatus.데이터없음, null));
            }
        } catch (Exception e) {
            log.error("BroadcastService pDallaRoomMasterSel Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, returnMap));
    }
}
