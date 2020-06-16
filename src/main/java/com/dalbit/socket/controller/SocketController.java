package com.dalbit.socket.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("socket")
public class SocketController {
    @Autowired
    private SocketService socketService;

    @Autowired
    GsonUtil gsonUtil;

    @PostMapping("sendMessage")
    public String sendMessage(HttpServletRequest request) {
        socketService.sendMessage(MemberVo.getMyMemNo(request), request.getParameter("roomNo"), request.getParameter("message"), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        return gsonUtil.toJson(new JsonOutputVo(Status.조회));
    }

    @PostMapping("sendSystemMessage")
    public String sendMessageSystem(HttpServletRequest request) {
        socketService.sendMessage(request.getParameter("message"));
        return gsonUtil.toJson(new JsonOutputVo(Status.조회));
    }

    @PostMapping("sendTargetSystemMessage")
    public String sendTargetMessageSystem(HttpServletRequest request) {
        String targetRooms = request.getParameter("targetRooms");
        List<String> listTargetRoom = null;
        if(!DalbitUtil.isEmpty(targetRooms)){
            listTargetRoom = Arrays.asList(targetRooms.split("\\|"));
        }
        socketService.sendMessage(request.getParameter("message"), listTargetRoom);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회));
        //return gsonUtil.toJson(new JsonOutputVo(Status.조회, socketService.sendMessage(request.getParameter("message"), listTargetRoom)));
    }
}
