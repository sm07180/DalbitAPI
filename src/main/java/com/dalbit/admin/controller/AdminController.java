package com.dalbit.admin.controller;

import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.ProfileVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.admin.vo.procedure.P_RoomForceExitInputVo;
import com.dalbit.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/auth/check")
    public String authCheck(HttpServletRequest request, SearchVo searchVo) throws GlobalException {
        String result = adminService.authCheck(request, searchVo);
        return result;
    }

    @PostMapping("/broadcast/list")
    public String broadcastList(HttpServletRequest request, SearchVo searchVo){
        String result = adminService.selectBroadcastList(searchVo);
        return result;
    }

    @PostMapping("/broadcast/forceExit")
    public String broadcastForceExit(HttpServletRequest request, P_RoomForceExitInputVo pRoomForceExitInputVo){
        String result = adminService.roomForceExit(pRoomForceExitInputVo);
        return result;
    }

    @PostMapping("/image/list")
    public String profileList(HttpServletRequest request, ProfileVo profileVo) {
        String result = adminService.selectProfileList(profileVo);
        return result;
    }

}
