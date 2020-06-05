package com.dalbit.admin.controller;

import com.dalbit.admin.service.AdminCommonService;
import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.P_RoomForceExitInputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
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

    @Autowired
    AdminCommonService adminCommonService;

    @PostMapping("/auth/check")
    public String authCheck(HttpServletRequest request, SearchVo searchVo) throws GlobalException {
        String result = adminService.authCheck(request, searchVo);
        return result;
    }

    /**
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    @PostMapping("/broadcast/list")
    public String broadcastList(HttpServletRequest request, SearchVo searchVo){
        String result = adminService.selectBroadcastList(request, searchVo);
        return result;
    }

    /**
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    @PostMapping("/broadcast/detail")
    public String broadcastDetail(HttpServletRequest request, SearchVo searchVo){
        String result = adminService.selectBroadcastDetail();
        return result;
    }

    /**
     * 생방송관리 > 강제종료
     */
    @PostMapping("/broadcast/forceExit")
    public String broadcastForceExit(HttpServletRequest request, P_RoomForceExitInputVo pRoomForceExitInputVo){
        String result = adminService.roomForceExit(pRoomForceExitInputVo);
        return result;
    }

    /**
     * - 이미지관리 > 프로필 이미지 조회
     * - 텍스트관리 > 닉네임 조회
     */
    @PostMapping("/image/list")
    public String profileList(HttpServletRequest request, ProfileVo profileVo) {
        String result = adminService.selectProfileList(request, profileVo);
        return result;
    }

    /**
     * 이미지관리 > 프로필 이미지 초기화
     */
    @PostMapping("/proImage/init")
    public String proImageInit(HttpServletRequest request, ProImageInitVo proImageInitVo, NotiInsertVo notiInsertVo) throws GlobalException {
        String result = adminService.proImageInit(request, proImageInitVo, notiInsertVo);
        return result;
    }

    /**
     * 이미지관리 > 방송방 이미지 초기화
     */
    @PostMapping("/broImage/init")
    public String broImageInit(HttpServletRequest request, BroImageInitVo broImageInitVo, NotiInsertVo notiInsertVo) throws GlobalException {
        String result = adminService.broImageInit(request, broImageInitVo, notiInsertVo);
        return result;
    }

    /**
     * 텍스트관리 > 닉네임 초기화
     */
    @PostMapping("/nickText/init")
    public String nickTextInit(HttpServletRequest request, NickTextInitVo nickTextInitVo, ProImageInitVo proImageInitVo, NotiInsertVo notiInsertVo) throws GlobalException {
        String result = adminService.nickTextInit(request, nickTextInitVo, proImageInitVo, notiInsertVo);
        return result;
    }

    /**
     * 텍스트관리 > 방송 제목 초기화
     */
    @PostMapping("/broTitleText/init")
    public String broTitleTextInit(HttpServletRequest request, BroTitleTextInitVo broTitleTextInitVo, BroImageInitVo broImageInitVo, NotiInsertVo notiInsertVo) throws GlobalException {
        String result = adminService.broTitleTextInit(request, broTitleTextInitVo, broImageInitVo, notiInsertVo);
        return result;
    }

    /**
     * 신고하기
     */
    @PostMapping("/declaration/operate")
    public String declarationOperate(HttpServletRequest request, DeclarationVo declarationVo, NotiInsertVo notiInsertVo) throws GlobalException {
        declarationVo.setIp(DalbitUtil.getIp(request));
        declarationVo.setBrowser(DalbitUtil.getUserAgent(request));
        String result = adminService.declarationOperate(request, declarationVo,notiInsertVo);
        return result;
    }
}
