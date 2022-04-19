package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.OwnerDao;
import com.dalbit.broadcast.vo.OwnerResultVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class OwnerService {

    @Autowired
    OwnerDao ownerDao;
    @Autowired
    SocketService socketService;
    @Autowired
    GsonUtil gsonUtil;

    public String pDallaRoomMasterSel(String roomNo, HttpServletRequest request) {
        if (StringUtils.isEmpty(roomNo)) {
            return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, roomNo));
        }

        OwnerResultVo pDallaRoomMasterSel = null;
        try {
            pDallaRoomMasterSel = ownerDao.pDallaRoomMasterSel(roomNo);
            if(pDallaRoomMasterSel == null){
                return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음, null));
            }
        } catch (Exception e) {
            log.error("BroadcastService pDallaRoomMasterSel Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, pDallaRoomMasterSel));
    }
}
