package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.P_RoomMemberListVo;
import com.dalbit.broadcast.vo.RoomMemberOutVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    GsonUtil gsonUtil;
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     * 방송방 참여자 리스트
     */
    public String callBroadCastRoomMemberList(P_RoomMemberListVo pRoomMemberListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberListVo);
        List<P_RoomMemberListVo> roomMemberVoList = userDao.callBroadCastRoomMemberList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomMemberVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomMemberOutVo> outVoList = new ArrayList<>();
            for (int i = 0; i< roomMemberVoList.size(); i++){
                outVoList.add(new RoomMemberOutVo(roomMemberVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap roomMemberList = new HashMap();
        roomMemberList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_조회, roomMemberList));
        }else if(Status.방송참여자리스트없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트없음, roomMemberList));
        }else if(Status.방송참여자리스트_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_회원아님, roomMemberList));
        }else if(Status.방송참여자리스트_방없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_방없음, roomMemberList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트조회_실패, roomMemberList));
        }

        return result;
    }
}
