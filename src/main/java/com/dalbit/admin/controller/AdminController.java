package com.dalbit.admin.controller;

import com.dalbit.admin.service.AdminCommonService;
import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.P_RoomForceExitInputVo;
import com.dalbit.admin.vo.procedure.P_StatVo;
import com.dalbit.common.vo.MessageInsertVo;
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

    @PostMapping("/menu")
    public String menu(HttpServletRequest request){
        String result = adminService.selectAdminMenu(request);
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
        String result = adminService.selectBroadcastDetail(searchVo);
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
    public String proImageInit(HttpServletRequest request, ProImageInitVo proImageInitVo) throws GlobalException {
        String result = adminService.proImageInit(request, proImageInitVo);
        return result;
    }

    /**
     * 이미지관리 > 방송방 이미지 초기화
     */
    @PostMapping("/broImage/init")
    public String broImageInit(HttpServletRequest request, BroImageInitVo broImageInitVo) throws GlobalException {
        String result = adminService.broImageInit(request, broImageInitVo);
        return result;
    }

    /**
     * 텍스트관리 > 닉네임 초기화
     */
    @PostMapping("/nickText/init")
    public String nickTextInit(HttpServletRequest request, NickTextInitVo nickTextInitVo, ProImageInitVo proImageInitVo) throws GlobalException {
        String result = adminService.nickTextInit(request, nickTextInitVo, proImageInitVo);
        return result;
    }

    /**
     * 텍스트관리 > 방송 제목 초기화
     */
    @PostMapping("/broTitleText/init")
    public String broTitleTextInit(HttpServletRequest request, BroTitleTextInitVo broTitleTextInitVo, BroImageInitVo broImageInitVo) throws GlobalException {
        String result = adminService.broTitleTextInit(request, broTitleTextInitVo, broImageInitVo);
        return result;
    }

    /**
     * 신고하기
     */
    @PostMapping("/declaration/operate")
    public String declarationOperate(HttpServletRequest request, DeclarationVo declarationVo) throws GlobalException {
        declarationVo.setIp(DalbitUtil.getIp(request));
        declarationVo.setBrowser(DalbitUtil.getUserAgent(request));
        String result = adminService.declarationOperate(request, declarationVo);
        return result;
    }

    /**
     * 생방송관리 > 채팅 내역 가져오기
     */
    @PostMapping("/broadcast/liveChat")
    public String broadLiveChat(HttpServletRequest request, LiveChatInputVo liveChatInputVo) {
        String result = adminService.callBroadcastLiveChatInfo(liveChatInputVo);
        return result;
    }

    /**
     * 생방송관리 > 프로필 상세 창 띄우기
     */
    @PostMapping("/broadcast/liveChatProfile")
    public String broadLiveChatProfile(HttpServletRequest request, LiveChatProfileVo liveChatProfileVo) {
        String result = adminService.getLiveChatProfile(liveChatProfileVo);
        return result;
    }

    /**
     * 생방송관리 > 강제퇴장
     */
    @PostMapping("/forcedOut")
    public String forcedOut(HttpServletRequest request, ForcedOutVo forcedOutVo){
        String result = adminService.forcedOut(request, forcedOutVo);
        return result;
    }

    /**
     * 생방송관리 > 시스템메시지 등록
     */
    @PostMapping("/message/insert")
    public String messageInsert(HttpServletRequest request, MessageInsertVo messageInsertVo) throws GlobalException{
         String result = adminService.insertContentsMessageAdd(request, messageInsertVo);
        return result;
    }

    /**
     * 생방송관리 > 청취자 목록
     */
    @PostMapping("/broadcast/liveListener")
    public String liveListener(HttpServletRequest request, ProfileVo profileVo) {
        String result = adminService.selectLiveListener(request, profileVo);
        return result;
    }

    /**
     * 통계 > 방송정보
     */
    @PostMapping("/stat/broadInfo")
    public String statBroad(HttpServletRequest request, P_StatVo pStatVo) {
        String result = adminService.callBroadcastTotal(pStatVo);
        return result;
    }

    /**
     * 통계 > 현재 접속자
     */
    @PostMapping("/stat/userInfo")
    public String statUser(HttpServletRequest request){
        String result = adminService.callUserTotal();
        return result;
    }

    /**
     * 통계 > 결제 현황
     */
    @PostMapping("/stat/payInfo")
    public String statPay(HttpServletRequest request, P_StatVo pStatVo) {
        String result = adminService.callPayInfo(pStatVo);
        return result;
    }
}
