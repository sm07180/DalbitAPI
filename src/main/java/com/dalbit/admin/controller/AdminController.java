package com.dalbit.admin.controller;

import com.dalbit.admin.service.AdminCommonService;
import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.service.GuestService;
import com.dalbit.broadcast.vo.procedure.P_FreezeVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.MessageInsertVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.mailbox.service.MailBoxService;
import com.dalbit.mailbox.vo.procedure.P_MailBoxImageDeleteVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    AdminCommonService adminCommonService;

    @Autowired
    GuestService guestService;

    @Autowired
    ActionService actionService;

    @Autowired
    MailBoxService mailBoxService;

    @Autowired
    SocketService socketService;

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    GsonUtil gsonUtil;

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
    public String broadcastList(HttpServletRequest request, BroadcastVo broadcastVo){
        String result = adminService.selectBroadcastList(request, broadcastVo);
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
     *  (WOWZA)
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    @PostMapping("/broadcast/detail/wowza")
    public String broadcastDetailWowza(HttpServletRequest request, SearchVo searchVo){
        String result = adminService.selectBroadcastDetailWowza(searchVo);
        return result;
    }


    /**
     * 생방송관리 > 강제종료
     */
    @PostMapping("/broadcast/forceExit")
    public String broadcastForceExit(HttpServletRequest request, P_RoomForceExitInputVo pRoomForceExitInputVo){
        String result = adminService.roomForceExit(pRoomForceExitInputVo, request);
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
     * 통계 > 방송정보
     */
    @PostMapping("/stat/broadInfo/new")
    public String statBroadNew(HttpServletRequest request, P_StatVo pStatVo) {
        String result = adminService.callNewBroadcastTimeNew(pStatVo);
        return result;
    }

    /**
     * 통계 > 현재 접속자
     */
    @PostMapping("/stat/userInfoCurrent")
    public String statUserCurrent(HttpServletRequest request, P_UserCurrentInputVo pUserCurrentInputVo){
        String result = adminService.callUserCurrentTotal(pUserCurrentInputVo);
        return result;
    }

    /**
     * 통계 > 로그인 현황
     */
    @PostMapping("/stat/loginInfo")
    public String statLoginInfo(HttpServletRequest request, P_LoginTotalInPutVo pLoginTotalInPutVo) {
        String result = adminService.callLoginInfo(pLoginTotalInPutVo);
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

    /**
     * 1:1 문의 목록 조회
     */
    @PostMapping("/question/list")
    public String questionList(HttpServletRequest request, P_QuestionListInputVo pQuestionListInputVo) {
        String result = adminService.callQuestionList(pQuestionListInputVo);
        return result;
    }

    /**
     * 1:1 문의 상세 조회
     */
    @PostMapping("/question/detail")
    public String questionDetail(HttpServletRequest request, P_QuestionDetailInputVo pQuestionDetailInputVo) {
        String result = adminService.callServiceCenterQnaDetail(pQuestionDetailInputVo);
        return result;
    }

    /**
     * 1:1 문의 faq sub list
     */
    @PostMapping("/question/fabSubList")
    public String questionFaqSub(HttpServletRequest request, FaqVo faqVo) {

        faqVo.setSlct_type(99);

        List<FaqVo> faqSubList = adminService.selectFaqSubList(faqVo);

        HashMap map = new HashMap();
        map.put("faqSubList", faqSubList);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    /**
     * 1:1 문의 처리
     */
    @PostMapping("/question/operate")
    public String operate(P_QuestionOperateVo pQuestionOperateVo) throws GlobalException, InterruptedException {
        String result = adminService.callServiceCenterQnaOperate(pQuestionOperateVo);
        return result;
    }

    /**
     * 신고내역 목록 조회
     */
    @PostMapping("/declaration/list")
    public String declarationList(HttpServletRequest request, P_DeclarationListInputVo pDeclarationListInputVo) {
        String result = adminService.callServiceCenterReportList(pDeclarationListInputVo);
        return result;
    }

    /**
     * 신고내역 상세 조회
     */
    @PostMapping("/declaration/detail")
    public String declarationDetail(HttpServletRequest request, P_DeclarationDetailInputVo pDeclarationDetailInputVo) {
        String result = adminService.callServiceCenterReportDetail(pDeclarationDetailInputVo);
        return result;
    }

    /**
     * 신고내역 > 신고대상자 정보 조회
     */
    @PostMapping("/declaration/userProfile")
    public String declarationUserProfile(HttpServletRequest request, LiveChatProfileVo liveChatProfileVo) {
        String result = adminService.selectUserProfile(liveChatProfileVo);
        return result;
    }

    /**
     * 회원 정보 수정내역
     */
    @PostMapping("/declaration/userEditHistory")
    public String declarationUserEditHistory(HttpServletRequest request, P_MemberEditHistInputVo pMemberEditHistInputVo) {
        String result = adminService.callMemberEditHistory(pMemberEditHistInputVo);
        return result;
    }

    @PostMapping("/ios/version")
    public String version(){
        HashMap result = adminService.getVersion();
        return gsonUtil.toJson(new JsonOutputVo((Status) result.get("status"), result.get("data")));
    }

    @PostMapping("/ios/app")
    public String app(HttpServletRequest request){
        HashMap result = adminService.getApp(request);
        return gsonUtil.toJson(new JsonOutputVo((Status) result.get("status"), result.get("data")));
    }

    @PostMapping("/ios/insert")
    public String insert(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(adminService.doInsert(request)));
    }

    @PostMapping("/ios/delete")
    public String delete(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(adminService.doDelete(request)));
    }

    /**
     * 클립 내역 조회
     */
    @PostMapping("/clip/list")
    public String clipList(ClipHistoryVo clipHistoryVo) {
        return adminService.callClipHistoryList(clipHistoryVo);
    }

    /**
     * 클립 내역 조회
     */
    @PostMapping("/clip/detail")
    public String clipDetail(ClipDetailVo clipDetailVo) {
        return adminService.callClipDetail(clipDetailVo);
    }

    /**
     * 클립 내역 조회
     */
    @PostMapping("/clip/edit")
    public String clipEdit(ClipAdminEditVo clipAdminEditVo) {
        return adminService.callClipEdit(clipAdminEditVo);
    }

    /**
     * 클립 내역 조회
     */
    @PostMapping("/clip/reply/list")
    public String clipReplyList(ClipHistoryReplyVo clipHistoryReplyVo) {
        return adminService.selectReplyList(clipHistoryReplyVo);
    }

    /**
     * 클립 내역 삭제
     */
    @PostMapping("/clip/reply/delete")
    public String clipReplyDelete(ClipHistoryReplyVo clipHistoryReplyVo) {
        return adminService.deleteReply(clipHistoryReplyVo);
    }


    /**
     * 게스트 > 게스트 취소
     */
    @PostMapping("/guestOut")
    public String guestOut(HttpServletRequest request) throws GlobalException {
        String result = guestService.guest(request);
        return result;
    }

    /**
     * 방송방 얼리기/얼리기 해제
     */
    @PostMapping("/freeze")
    public String freeze(P_FreezeVo pFreezeVo, HttpServletRequest request) throws GlobalException {

        HashMap returnMap = new HashMap();
        returnMap.put("isFreeze", false);
        String result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_성공, returnMap));

        //0:해제,  1:DJ , 2:관리자
        boolean isFreeze = !(pFreezeVo.getFreezeMsg() == 0);
        returnMap.put("isFreeze", isFreeze);
        returnMap.put("msg", isFreeze ? messageUtil.get(Status.관리자_얼리기_성공.getMessageKey()) : messageUtil.get(Status.관리자_얼리기_해제.getMessageKey()));

        socketService.changeRoomFreeze(pFreezeVo.getMem_no(), pFreezeVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));

        if(!isFreeze){
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_해제, returnMap));
        }
        return result;
    }

    /**
     * 설정 (tbl_code_define, type="system_config"인 목록)
     */
    @PostMapping("/setting")
    public String selectSettingList(SettingListVo settingListVo) {
        String result = adminService.selectSettingList(settingListVo);
        return result;
    }

    /**
     * 설정 값 변경
     */
    @PostMapping("/setting/update")
    public String updateSetting(SettingListVo settingListVo) {
        String result = adminService.updateSetting(settingListVo);
        return result;
    }

    /**
     * 신고 이미지삭제
     */
    @PostMapping("/declaretion/imageDelete")
    public String imageDelete(HttpServletRequest request, P_MailBoxImageDeleteVo pMailBoxImageDeleteVo) {
        String chatNo = request.getParameter("chatNo");

        HashMap returnMap = new HashMap();
        returnMap.put("chatNo", chatNo);
        returnMap.put("targetMemNo", request.getParameter("targetMemNo"));
        returnMap.put("msgIdx", request.getParameter("msgIdx"));
        returnMap.put("isDelete", true);

        try{
            //이미지 삭제 소켓
            socketService.sendChatImageDelete(chatNo, returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        }catch(Exception e){}

        return gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_성공, returnMap));
    }

    /**
     * 생방송관리 > 강제종료
     */
    @PostMapping("/member/forceLogout")
    public String memberForceLogout(HttpServletRequest request, @RequestParam HashMap<String, String> paramMap){
        String result = adminService.memberForceLogout(request, paramMap);
        return result;
    }
}
