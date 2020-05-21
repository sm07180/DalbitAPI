package com.dalbit.admin.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.util.AdminSocketUtil;
import com.dalbit.admin.vo.BroadcastExitVo;
import com.dalbit.admin.vo.BroadcastVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.admin.vo.procedure.P_BroadcastDetailOutputVo;
import com.dalbit.admin.vo.procedure.P_RoomForceExitInputVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
public class AdminService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AdminSocketUtil adminSocketUtil;

    @Value("${server.ant.url}")
    private String antServer;

    @Value("${ant.expire.hour}")
    private int antExpire;

    @Value("${ant.app.name}")
    private String antName;

    public String selectBroadcastList(SearchVo searchVo){
        ArrayList<BroadcastVo> broadList = adminDao.selectBroadcastList(searchVo);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, broadList));
    }

    public String roomForceExit(P_RoomForceExitInputVo pRoomForceExitInputVo){
        ProcedureVo procedureVo = new ProcedureVo(pRoomForceExitInputVo);
        //방 나가기 처리
        adminDao.callBroadcastRoomExit(procedureVo);
        if(Status.방송강제종료_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_회원아님));

        }else if(Status.방송강제종료_존재하지않는방.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_존재하지않는방));

        }else if(Status.방송강제종료_이미종료된방.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_이미종료된방));
        }

        //회원 나가기 처리
        adminDao.updateBroadcastMemberExit(pRoomForceExitInputVo);

        //방 종료 처리
        adminDao.updateBroadcastExit(new BroadcastExitVo(pRoomForceExitInputVo.getRoom_no(), pRoomForceExitInputVo.getStart_date()));

        //소캣 알림
        HashMap<String,Object> param = new HashMap<>();
        param.put("roomNo",pRoomForceExitInputVo.getRoom_no());
        param.put("memNo",pRoomForceExitInputVo.getMem_no());

        //option
        param.put("ctrlRole","");
        param.put("recvMemNo","roomOut");
        param.put("recvType","chat");
        param.put("recvPosition","chat");
        param.put("recvLevel",0);
        param.put("recvTime",0);

        adminSocketUtil.setSocket(param,"chatEnd","roomOut",jwtUtil.generateToken(pRoomForceExitInputVo.getMem_no(), true));

        return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_성공));
    }
}
