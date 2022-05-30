package com.dalbit.admin.controller;

import com.dalbit.admin.service.AdminCommonService;
import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.service.GuestService;
import com.dalbit.broadcast.vo.procedure.P_FreezeVo;
import com.dalbit.common.code.BroadcastStatus;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MailBoxStatus;
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
        return adminService.authCheck(request, searchVo);
    }

    @PostMapping("/menu")
    public String menu(HttpServletRequest request){
        return adminService.selectAdminMenu(request);
    }

    /**
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    @PostMapping("/broadcast/list")
    public String broadcastList(HttpServletRequest request, BroadcastVo broadcastVo){
        return adminService.selectBroadcastList(request, broadcastVo);
    }

    /**
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    @PostMapping("/broadcast/detail")
    public String broadcastDetail(HttpServletRequest request, SearchVo searchVo){
        return adminService.selectBroadcastDetail(searchVo);
    }

    /**
     *  (WOWZA)
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    @PostMapping("/broadcast/detail/wowza")
    public String broadcastDetailWowza(SearchVo searchVo){
        return adminService.selectBroadcastDetailWowza(searchVo);
    }


    /**
     * 생방송관리 > 강제종료
     */
    @PostMapping("/broadcast/forceExit")
    public String broadcastForceExit(HttpServletRequest request, P_RoomForceExitInputVo pRoomForceExitInputVo){
        return adminService.roomForceExit(pRoomForceExitInputVo, request);
    }

    /**
     * 생방송관리 > 숨김
     */
    @PostMapping("/broadcast/hide")
    public String broadcastHide(P_RoomHideInputVo p_roomHideInputVo){
        return adminService.broadcastHide(p_roomHideInputVo);
    }

    /**
     * - 이미지관리 > 프로필 이미지 조회
     * - 텍스트관리 > 닉네임 조회
     */
    @PostMapping("/image/list")
    public String profileList(HttpServletRequest request, ProfileVo profileVo) {
        return adminService.selectProfileList(request, profileVo);
    }

    /**
     * 이미지관리 > 프로필 이미지 초기화
     */
    @PostMapping("/proImage/init")
    public String proImageInit(HttpServletRequest request, ProImageInitVo proImageInitVo) throws GlobalException {
        return adminService.proImageInit(request, proImageInitVo);
    }

    /**
     * 이미지관리 > 방송방 이미지 초기화
     */
    @PostMapping("/broImage/init")
    public String broImageInit(HttpServletRequest request, BroImageInitVo broImageInitVo) throws GlobalException {
        return adminService.broImageInit(request, broImageInitVo);
    }

    /**
     * 텍스트관리 > 닉네임 초기화
     */
    @PostMapping("/nickText/init")
    public String nickTextInit(HttpServletRequest request, NickTextInitVo nickTextInitVo, ProImageInitVo proImageInitVo) throws GlobalException {
        return adminService.nickTextInit(request, nickTextInitVo, proImageInitVo);
    }

    /**
     * 텍스트관리 > 방송 제목 초기화
     */
    @PostMapping("/broTitleText/init")
    public String broTitleTextInit(HttpServletRequest request, BroTitleTextInitVo broTitleTextInitVo, BroImageInitVo broImageInitVo) throws GlobalException {
        return adminService.broTitleTextInit(request, broTitleTextInitVo, broImageInitVo);
    }

    /**
     * 신고하기
     */
    @PostMapping("/declaration/operate")
    public String declarationOperate(HttpServletRequest request, DeclarationVo declarationVo) throws GlobalException {
        declarationVo.setIp(DalbitUtil.getIp(request));
        declarationVo.setBrowser(DalbitUtil.getUserAgent(request));
        return adminService.declarationOperate(request, declarationVo);
    }

    /**
     * 생방송관리 > 채팅 내역 가져오기
     */
    @PostMapping("/broadcast/liveChat")
    public String broadLiveChat(LiveChatInputVo liveChatInputVo) {
        return adminService.callBroadcastLiveChatInfo(liveChatInputVo);
    }

    /**
     * 생방송관리 > 프로필 상세 창 띄우기
     */
    @PostMapping("/broadcast/liveChatProfile")
    public String broadLiveChatProfile(LiveChatProfileVo liveChatProfileVo) {
        return adminService.getLiveChatProfile(liveChatProfileVo);
    }

    /**
     * 생방송관리 > 강제퇴장
     */
    @PostMapping("/forcedOut")
    public String forcedOut(HttpServletRequest request, ForcedOutVo forcedOutVo){
        return adminService.forcedOut(request, forcedOutVo);
    }

    /**
     * 생방송관리 > 시스템메시지 등록
     */
    @PostMapping("/message/insert")
    public String messageInsert(HttpServletRequest request, MessageInsertVo messageInsertVo) throws GlobalException{
        return adminService.insertContentsMessageAdd(request, messageInsertVo);
    }

    /**
     * 생방송관리 > 청취자 목록
     */
    @PostMapping("/broadcast/liveListener")
    public String liveListener(HttpServletRequest request, ProfileVo profileVo) {
        return adminService.selectLiveListener(request, profileVo);
    }

    /**
     * 통계 > 방송정보
     */
    @PostMapping("/stat/broadInfo")
    public String statBroad( P_StatVo pStatVo) {
        return adminService.callBroadcastTotal(pStatVo);
    }

    /**
     * 통계 > 방송정보
     */
    @PostMapping("/stat/broadInfo/new")
    public String statBroadNew(P_StatVo pStatVo) {
        return adminService.callNewBroadcastTimeNew(pStatVo);
    }

    /**
     * 통계 > 현재 접속자
     */
    @PostMapping("/stat/userInfoCurrent")
    public String statUserCurrent(P_UserCurrentInputVo pUserCurrentInputVo){
        return adminService.callUserCurrentTotal(pUserCurrentInputVo);
    }

    /**
     * 통계 > 로그인 현황
     */
    @PostMapping("/stat/loginInfo")
    public String statLoginInfo(P_LoginTotalInPutVo pLoginTotalInPutVo) {
        return adminService.callLoginInfo(pLoginTotalInPutVo);
    }

    /**
     * 통계 > 결제 현황
     */
    @PostMapping("/stat/payInfo")
    public String statPay(P_StatVo pStatVo) {
        return adminService.callPayInfo(pStatVo);
    }

    @PostMapping("/stat/itemInfo")
    public String itemInfo(P_StatVo pStatVo){
        return adminService.callItemInfo(pStatVo);
    }

    /**
     * 1:1 문의 목록 조회
     */
    @PostMapping("/question/list")
    public String questionList(P_QuestionListInputVo pQuestionListInputVo) {
        return adminService.callQuestionList(pQuestionListInputVo);
    }

    /**
     * 1:1 문의 상세 조회
     */
    @PostMapping("/question/detail")
    public String questionDetail(P_QuestionDetailInputVo pQuestionDetailInputVo) {
        return adminService.callServiceCenterQnaDetail(pQuestionDetailInputVo);
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

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, map));
    }

    /**
     * 1:1 문의 처리
     */
    @PostMapping("/question/operate")
    public String operate(P_QuestionOperateVo pQuestionOperateVo) throws GlobalException, InterruptedException {
        return adminService.callServiceCenterQnaOperate(pQuestionOperateVo);
    }

    /**
     * 신고내역 목록 조회
     */
    @PostMapping("/declaration/list")
    public String declarationList(HttpServletRequest request, P_DeclarationListInputVo pDeclarationListInputVo) {
        return adminService.callServiceCenterReportList(pDeclarationListInputVo);
    }

    /**
     * 신고내역 상세 조회
     */
    @PostMapping("/declaration/detail")
    public String declarationDetail(HttpServletRequest request, P_DeclarationDetailInputVo pDeclarationDetailInputVo) {
        return adminService.callServiceCenterReportDetail(pDeclarationDetailInputVo);
    }

    /**
     * 신고내역 > 신고대상자 정보 조회
     */
    @PostMapping("/declaration/userProfile")
    public String declarationUserProfile(HttpServletRequest request, LiveChatProfileVo liveChatProfileVo) {
        return adminService.selectUserProfile(liveChatProfileVo);
    }

    /**
     * 회원 정보 수정내역
     */
    @PostMapping("/declaration/userEditHistory")
    public String declarationUserEditHistory(HttpServletRequest request, P_MemberEditHistInputVo pMemberEditHistInputVo) {
        return adminService.callMemberEditHistory(pMemberEditHistInputVo);
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
        return guestService.guest(request);
    }

    /**
     * 방송방 얼리기/얼리기 해제
     */
    @PostMapping("/freeze")
    public String freeze(P_FreezeVo pFreezeVo, HttpServletRequest request) throws GlobalException {

        HashMap returnMap = new HashMap();
        returnMap.put("isFreeze", false);
        String result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.얼리기_성공, returnMap));

        //0:해제,  1:DJ , 2:관리자
        boolean isFreeze = !(pFreezeVo.getFreezeMsg() == 0);
        returnMap.put("isFreeze", isFreeze);
        returnMap.put("msg", isFreeze ? messageUtil.get(BroadcastStatus.관리자_얼리기_성공.getMessageKey()) : messageUtil.get(BroadcastStatus.관리자_얼리기_해제.getMessageKey()));

        socketService.changeRoomFreeze(pFreezeVo.getMem_no(), pFreezeVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));

        if(!isFreeze){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.얼리기_해제, returnMap));
        }
        return result;
    }

    /**
     * 설정 (tbl_code_define, type="system_config"인 목록)
     */
    @PostMapping("/setting")
    public String selectSettingList(SettingListVo settingListVo) {
        return adminService.selectSettingList(settingListVo);
    }

    /**
     * 설정 값 변경
     */
    @PostMapping("/setting/update")
    public String updateSetting(SettingListVo settingListVo) {
        return adminService.updateSetting(settingListVo);
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

        return gsonUtil.toJson(new JsonOutputVo(MailBoxStatus.대화방_이미지삭제_성공, returnMap));
    }

    /**
     * 생방송관리 > 강제종료
     */
    @PostMapping("/member/forceLogout")
    public String memberForceLogout(HttpServletRequest request, @RequestParam HashMap<String, String> paramMap){
        return adminService.memberForceLogout(request, paramMap);
    }
}
