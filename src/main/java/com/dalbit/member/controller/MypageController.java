package com.dalbit.member.controller;

import com.dalbit.common.vo.PagingVo;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.P_WalletPopupListVo;
import com.dalbit.member.vo.request.WalletPopupListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mypage")
@Scope("prototype")
public class MypageController {

    @Autowired
    MypageService mypageService;
    @Autowired
    ProfileService profileService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 본인 정보 조회
     */
    @GetMapping("")
    public String memberInfo(HttpServletRequest request){
        int memLogin = DalbitUtil.isLogin(request) ? 1 : 0;
        P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo(request), MemberVo.getMyMemNo(request));

        String result = profileService.callMemberInfo(apiData, request);

        return result;
    }


    /**
     * 프로필편집
     */
    @PostMapping("/profile")
    public String editProfile(@Valid ProfileEditVo profileEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ProfileEditVo apiData = new P_ProfileEditVo(profileEditVo, request);
        String result = mypageService.callProfileEdit(apiData, request);
        return result;
    }


    /**
     * 팬 등록
     */
    @PostMapping("/fan")
    public String fanstarInsert(@Valid FanstartInsertVo fanstartInsertVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanstarInsertVo apiData = new P_FanstarInsertVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo(request));
        apiData.setStar_mem_no(fanstartInsertVo.getMemNo());
        apiData.setType(fanstartInsertVo.getType());

        String result = mypageService.callFanstarInsert(apiData);

        return result;
    }

    /**
     * 추천 DJ 다중 팬 등록
     */
    @PostMapping("/multi/fan")
    public String fanstarMultiInsert(@Valid FanstarMultiInsertVo fanstarMultiInsertVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanstarInsertVo apiData = new P_FanstarInsertVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo(request));
        apiData.setType(1);
        String result = "";

        for(int i=0; i<fanstarMultiInsertVo.getMemNoList().length; i++) {
            apiData.setStar_mem_no(fanstarMultiInsertVo.getMemNoList()[i]);
            result = mypageService.callFanstarInsert(apiData); // 맨 마지막 결과만..
        }

        return result;
    }

    /**
     * 팬 해제
     */
    @DeleteMapping("/fan")
    public String fanstarDelete(@Valid FanstarDeleteVo fanstarDeleteVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanstarDeleteVo apiData = new P_FanstarDeleteVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo(request));
        apiData.setStar_mem_no(fanstarDeleteVo.getMemNo());

        String result = mypageService.callFanstarDelete(apiData);

        return result;
    }


    /**
     * 회원 방송방 기본설정 조회
     */
    @GetMapping("/broad")
    public String broadBasic(HttpServletRequest request){

        P_BroadBasicVo apiData = new P_BroadBasicVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.callBroadBasic(apiData);

        return result;
    }


    /**
     * 회원 방송방 기본설정 수정하기
     */
    @PostMapping("/broad")
    public String broadBasicEdit(@Valid BroadBasicEditVo broadBasicEditVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_BroadBasicEditVo apiData = new P_BroadBasicEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSubjectType(broadBasicEditVo.getRoomType());
        apiData.setTitle(broadBasicEditVo.getTitle());
        apiData.setBackgroundImage(broadBasicEditVo.getBgImg());
        apiData.setBackgroundImageGrade(DalbitUtil.isStringToNumber(broadBasicEditVo.getBgImgRacy()));
        apiData.setWelcomMsg(broadBasicEditVo.getWelcomMsg());
        apiData.setNotice(broadBasicEditVo.getNotice());
        apiData.setEntryType(broadBasicEditVo.getEntryType());

        String result = mypageService.callBroadBasicEdit(apiData);

        return result;
    }


    /**
     * 회원 신고하기
     */
    @PostMapping("/declar")
    public String memberReportAdd(@Valid MemberReportAddVo memberReportAddVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MemberReportAddVo apiData = new P_MemberReportAddVo(memberReportAddVo, new DeviceVo(request), request);

        String result = mypageService.callMemberReportAdd(apiData);

        return result;
    }


    /**
     * 회원 차단하기
     */
    @PostMapping("/block")
    public String broadBlock_Add(@Valid MemberBlockAddVo memberBlockAddVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MemberBlockAddVo apiData = new P_MemberBlockAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setBlocked_mem_no(memberBlockAddVo.getMemNo());

        String result = mypageService.callBlockAdd(apiData);

        return result;
    }


    /**
     * 회원 차단 해제하기
     */
    @DeleteMapping("/block")
    public String broadBlock_Del(@Valid MemberBlockDelVo memberBlockDelVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MemberBlockDelVo apiData = new P_MemberBlockDelVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setBlocked_mem_no(memberBlockDelVo.getMemNo());

        String result = mypageService.callMemBerBlocklDel(apiData);

        return result;
    }


    /**
     * 회원 알림설정 조회하기
     */
    @GetMapping("/notify")
    public String memberNotify(HttpServletRequest request){
        P_MemberNotifyVo apiData = new P_MemberNotifyVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.callMemberNotify(apiData);

        return result;
    }

    /**
     * 회원 알림설정 수정하기
     */
    @PostMapping("/notify")
    public String memberNotifyEdit(@Valid MemberNotifyEditVo memberNotifyEditVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MemberNotifyEditVo apiData = new P_MemberNotifyEditVo(memberNotifyEditVo, request);

        String result = mypageService.callMemberNotifyEdit(apiData);

        return result;
    }

    /**
     * 회원 방송방 빠른말 가져오기
     */
    @GetMapping("/shortcut")
    public String memberShortCut(HttpServletRequest request){
        P_MemberShortCutVo apiData = new P_MemberShortCutVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.callMemberShortCut(apiData, "", request);

        return result;
    }

    /**
     * 회원 방송방 빠른말 수정하기
     */
    @PostMapping("/shortcut")
    public String memberShortCutEdit(@Valid ShortCutEditVo shortCutEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        if(bindingResult.hasErrors()){
            List errorList = bindingResult.getAllErrors();
            for (int i=0; i<bindingResult.getErrorCount(); i++) {
                FieldError fieldError = (FieldError) errorList.get(i);
                if("text".equals(fieldError.getField())){
                    return gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_텍스트오류));
                }
            }
        }

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String isOn = (shortCutEditVo.getIsOn().toUpperCase().equals("1") || shortCutEditVo.getIsOn().toUpperCase().equals("TRUE")) ? "on" : "off";

        P_MemberShortCutEditVo apiData = new P_MemberShortCutEditVo();

        //String text = shortCutEditVo.getText().length() <= 50 ? shortCutEditVo.getText() : shortCutEditVo.getText().substring(0, 49);

        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setOrderNo(shortCutEditVo.getOrderNo());
        apiData.setOrder(shortCutEditVo.getOrder());
        apiData.setText(shortCutEditVo.getText());
        apiData.setOnOff(isOn);

        String result = mypageService.callMemberShortCutEdit(apiData, request);

        return result;
    }

    /**
     * 회원 달 선물하기
     */
    @PostMapping("/gift")
    public String memberGift(@Valid RubyVo rubyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RubyVo apiData = new P_RubyVo(rubyVo, request);
        String result = mypageService.callMemberGiftRuby(apiData, request);

        return result;
    }

    /**
     * 회원 알림 내용 조회
     */
    @GetMapping("/notification")
    public String memberNotification(@Valid NotificationVo notificationVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_NotificationVo apiData = new P_NotificationVo(notificationVo, request);

        String result = mypageService.callMemberNotification(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 등록
     */
//    @PostMapping("/notice/add")
//    public String noticeAdd(@Valid MypageNoticeAddVo mypageNoticeAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MypageNoticeAddVo apiData = new P_MypageNoticeAddVo(mypageNoticeAddVo, request);
//
//        String result = mypageService.callMypageNoticeAdd(apiData, request);
//
//        return result;
//    }

    /**
     * 마이페이지 공지사항 수정
     */
//    @PostMapping("/notice/edit")
//    public String noticeEdit(@Valid MypageNoticeEditVo mypageNoticeEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MypageNoticeEditVo apiData = new P_MypageNoticeEditVo(mypageNoticeEditVo, request);
//
//        String result = mypageService.callMypageNoticeEdit(apiData, request);
//
//        return result;
//    }


    /**
     * 마이페이지 공지사항 수정
     */
//    @PostMapping("/notice/edit")
//    public String noticeEdit(@Valid MypageNoticeEditVo mypageNoticeEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MypageNoticeEditVo apiData = new P_MypageNoticeEditVo(mypageNoticeEditVo, request);
//
//        String result = mypageService.callMypageNoticeEdit(apiData, request);
//
//        return result;
//    }


    /**
     * 마이페이지 공지사항 삭제
     */
//    @DeleteMapping("/notice")
//    public String noticeDel(@Valid MypageNoticeDelVo mypageNoticeDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MypageNoticeDelVo apiData = new P_MypageNoticeDelVo(mypageNoticeDelVo, request);
//
//        String result = mypageService.callMypageNoticeDel(apiData);
//
//        return result;
//    }

    /**
     * 마이페이지 공지사항 조회수
     */
    @PostMapping("/notice/read")
    public String noticeRead(@Valid MypageNoticeReadVo mypageNoticeReadVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeReadVo apiData = new P_MypageNoticeReadVo(mypageNoticeReadVo, request);

        String result = mypageService.callMypageNoticeRead(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 조회
     */
//    @GetMapping("/notice")
//    public String noticeView(@Valid MypageNoticeSelectVo mypageNoticeSelectVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MypageNoticeSelectVo apiData = new P_MypageNoticeSelectVo(mypageNoticeSelectVo, request);
//
//        String result = mypageService.callMypageNoticeSelect(apiData);
//
//        return result;
//    }

    @GetMapping("/notice")
    public String noticeView(@Valid BroadcastNoticeSelVo noticeSelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        return mypageService.mobileBroadcastNoticeSelect(noticeSelVo, request);
    }

    /**
     * 마이페이지 공지사항 댓글 등록
     */
    @PostMapping("/notice/reply/add")
    public String noticeReplyAdd(@Valid MypageNoticeReplyAddVo mypageNoticeReplyAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeReplyAddVo apiData = new P_MypageNoticeReplyAddVo(mypageNoticeReplyAddVo, request);

        String result = mypageService.callMyPageNoticeReplyAdd(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 댓글 삭제
     */
    @PostMapping("/notice/reply/delete")
    public String noticeReplyDelete(@Valid MypageNoticeReplyDeleteVo mypageNoticeReplyDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeReplyDeleteVo apiData = new P_MypageNoticeReplyDeleteVo(mypageNoticeReplyDeleteVo, request);

        String result = mypageService.callMyPageNoticeReplyDelete(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 댓글 수정
     */
    @PostMapping("/notice/reply/edit")
    public String noticeReplyEdit(@Valid MypageNoticeReplyEditVo mypageNoticeReplyEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeReplyEditVo apiData = new P_MypageNoticeReplyEditVo(mypageNoticeReplyEditVo, request);

        String result = mypageService.callMyPageNoticeReplyEdit(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 댓글 보기
     */
    @PostMapping("/notice/reply/list")
    public String noticeReplyList(@Valid MypageNoticeReplyListVo mypageNoticeReplyListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeReplyListVo apiData = new P_MypageNoticeReplyListVo(mypageNoticeReplyListVo, request);

        String result = mypageService.callMyPageNoticeReplySelect(apiData);

        return result;
    }



    /**
     * 마이페이지 리포트 방송내역 보기
     */
    @GetMapping("/report/broad")
    public String reportBroadView(@Valid MypageReportBroadVo mypageReportBroadVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageReportBroadVo apiData = new P_MypageReportBroadVo(mypageReportBroadVo, request);

        String result = mypageService.callMypageMypageReportBroad(apiData);

        return result;
    }

    /**
     * 마이페이지 리포트 청취내역 보기
     */
    @GetMapping("/report/listen")
    public String reportListenView(@Valid MypageReportListenVo mypageReportListenVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageReportListenVo apiData = new P_MypageReportListenVo(mypageReportListenVo, request);

        String result = mypageService.callMypageMypageReportListen(apiData);

        return result;
    }

    /**
     * 방송설정 금지어 단어 조회
     */
    @GetMapping("/banword")
    public String getBanWrod(HttpServletRequest request){

        P_BanWordSelectVo apiData = new P_BanWordSelectVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.callMyapgeGetBanWord(apiData);

        return result;
    }

    /**
     * 방송설정 금지어 저장
     */
    @PostMapping("/banword")
    public String insertBanWrod(@Valid BanWordVo banWordVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_BanWordInsertVo apiData = new P_BanWordInsertVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setBanWord(banWordVo.getBanWord());

        String result = mypageService.callMypageInsertBanWord(apiData, request);

        return result;
    }

    /**
     * 방송설정 유저 검색
     */
    @GetMapping("/search")
    public String searchUser(@Valid SearchUserVo searchUserVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_SearchUserVo apiData = new P_SearchUserVo(searchUserVo, request);
        String result = mypageService.callMypageSearchUser(apiData);

        return result;
    }

    /**
     * 방송설정 고정 매니저 조회
     */
    @GetMapping("/manager")
    public String getManager(HttpServletRequest request){

        P_MypageManagerVo apiData = new P_MypageManagerVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        String result = mypageService.callMypageManager(apiData);

        return result;
    }


    /**
     * 방송설정 고정 매니저 등록
     */
    @PostMapping("/manager/add")
    public String addManager(@Valid MypageManagerAddVo mypageManagerAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MypageManagerAddVo apiData = new P_MypageManagerAddVo(mypageManagerAddVo, request);
        String result = mypageService.callMypageManagerAdd(apiData);

        return result;
    }


    /**
     * 방송설정 고정 매니저 권한수정
     */
    @PostMapping("/manager/edit")
    public String editManager(@Valid MypageManagerEditVo mypageManagerEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MypageManagerEditVo apiData = new P_MypageManagerEditVo(mypageManagerEditVo, request);
        String result = mypageService.callMypageManagerEdit(apiData);

        return result;
    }


    /**
     * 방송설정 고정 매니저 해제
     */
    @DeleteMapping("/manager")
    public String delManager(@Valid MypageManagerDelVo mypageManagerDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MypageManagerDelVo apiData = new P_MypageManagerDelVo(mypageManagerDelVo, request);
        String result = mypageService.callMypageManagerDel(apiData);

        return result;
    }

    /**
     * 방송설정 블랙리스트 조회
     */
    @GetMapping("/black")
    public String getBlackList(@Valid MypageBlackVo mypageBlackVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MypageBlackVo apiData = new P_MypageBlackVo(mypageBlackVo, request);
        String result = mypageService.callMypageBlackListView(apiData);

        return result;
    }

    /**
     * 방송설정 블랙리스트 등록
     */
    @PostMapping("/black/add")
    public String addBlackList(@Valid MypageBlackAddVo mypageBlackAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MypageBlackAddVo apiData = new P_MypageBlackAddVo(mypageBlackAddVo, request);
        String result = mypageService.callMypageBlackListAdd(apiData, request);

        return result;
    }

    /**
     * 방송설정 블랙리스트 해제
     */
    @DeleteMapping("/black")
    public String delBlackList(@Valid MypageBlackDelVo mypageBlackDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MypageBlackDelVo apiData = new P_MypageBlackDelVo(mypageBlackDelVo, request);
        String result = mypageService.callMypageBlackListDel(apiData);

        return result;
    }


    /**
     * 별 달 교환 아이템 가져오기
     */
    @GetMapping("/change/item")
    public String changeItemSelect(@Valid ChangeItemListVo changeItemListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ChangeItemListVo apiData = new P_ChangeItemListVo(changeItemListVo, request);
        String result = mypageService.changeItemSelect(apiData);

        return result;
    }

    /**
     * 별 달 교환하기
     */
    @PostMapping("/change/item")
    public String changeItem(@Valid ChangeItemVo changeItemVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ChangeItemVo apiData = new P_ChangeItemVo(changeItemVo, request);
        String result = mypageService.changeItem(apiData);

        return result;
    }

    /**
     * 별 달 자동교환 설정 변경
     */
    @PostMapping("/auto/change")
    public String autoChangeSettingEdit(@Valid AutoChangeSettingEditVo autoChangeSettingEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_AutoChangeSettingEditVo apiData = new P_AutoChangeSettingEditVo(autoChangeSettingEditVo, request);
        String result = mypageService.callAutoChangeSettingEdit(apiData);
        return result;
    }

    /**
     * 별 달 자동교환 설정 가져오기
     */
    @GetMapping("/auto/change")
    public String autoChangeSettingSelect(HttpServletRequest request) {
        P_AutoChangeSettingSelectVo apiData = new P_AutoChangeSettingSelectVo(request);
        String result = mypageService.callAutoChangeSettingSelect(apiData);
        return result;
    }

    /**
     * 회원 알림 내용 읽음처리
     */
    @PostMapping("/read")
    public String readNotification(@Valid ReadNotificationVo readNnotificationVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ReadNotificationVo apiData = new P_ReadNotificationVo(readNnotificationVo, request);

        String result = mypageService.callReadNotification(apiData);

        return result;
    }

    /**
     * 스페셜 DJ 신청 가능상태 조회
     */
    @RequestMapping("/specialDj/status")
    public String specialDjStatus(HttpServletRequest request, SpecialDjRegManageVo specialDjRegManageVo) throws GlobalException {
        return mypageService.callSpecialDjStatus(request, specialDjRegManageVo);
    }

    /**
     * 스페셜 DJ 신청
     */
    @PostMapping("/specialDj/request")
    public String specialDjRequest(@Valid P_SpecialDjReq pSpecialDjReq, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return mypageService.callSpecialDjReq(pSpecialDjReq, request);
    }

    /**
     * 레벨정보 조회
     */
    @GetMapping("/level")
    public String level() throws GlobalException {

        return mypageService.selectLevel();
    }

    /**
     * 회원 알림 내용 읽음처리
     */
    @GetMapping("/newalarm")
    public String newAlarm(HttpServletRequest request) throws GlobalException{

        P_MemberNotifyVo apiData = new P_MemberNotifyVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.callNewAlarm(apiData);

        return result;
    }


    /**
     * 회원 방송방 빠른말 추가
     */
    @PostMapping("/shortcut/add")
    public String memberShortCutAdd(HttpServletRequest request) throws GlobalException{

        P_MemberShortCutAddVo apiData = new P_MemberShortCutAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.memberShortCutAdd(apiData, request);

        return result;
    }

    /**
     * 회원 방송방 빠른말 연장
     */
    @PostMapping("/shortcut/extend")
    public String memberShortCutExtend(HttpServletRequest request) throws GlobalException{

        P_MemberShortCutExtendVo apiData = new P_MemberShortCutExtendVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.memberShortCutExtend(apiData, request);

        return result;
    }

    /**
     * 메시지 사용 클릭 업데이트
     */
    @PostMapping("/click/update")
    public String msgClickUpdate(@Valid MsgClickUpdateVo msgClickUpdateVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        return gsonUtil.toJson(new JsonOutputVo(Status.메시지클릭업데이트_성공));

        /*DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MsgClickUpdateVo apiData = new P_MsgClickUpdateVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        int msgSlct;
        if("quick".equals(msgClickUpdateVo.getMsgSlct()) || "1".equals(msgClickUpdateVo.getMsgSlct())){
            msgSlct = 1;
        } else {
            msgSlct = 2;
        }

        apiData.setMsg_slct(msgSlct);
        apiData.setMsg_idx(msgClickUpdateVo.getMsgIdx());

        String result = mypageService.msgClickUpdate(apiData);
        return result;*/
    }

    /**
     * 회원 방송방 이모티콘 가져오기
     */
    @GetMapping("/emoticon")
    public String memberEmoticon(HttpServletRequest request) throws GlobalException{

        P_EmoticonListVo apiData = new P_EmoticonListVo(request);
        String result = mypageService.callMemberEmoticon(apiData);

        return result;
    }

    /**
     * 회원 알림 내용 삭제
     */
    @DeleteMapping("/notification/delete")
    public String memberNotificationDelete(@Valid NotificationDeleteVo notificationDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_NotificationDeleteVo apiData = new P_NotificationDeleteVo(notificationDeleteVo, request);
        String result = mypageService.callMemberNotificationDelete(apiData);

        return result;
    }

    @PostMapping("/new")
    public String getMyPageNew(HttpServletRequest request){
        return mypageService.getMyPageNew(request);
    }

    @GetMapping("/new/fanBoard")
    public String getMyPageNewFanBoard(HttpServletRequest request){
        return mypageService.getMyPageNewFanBoard(request);
    }

    @GetMapping("/new/wallet")
    public String getMyPageNewWallet(HttpServletRequest request){
        return mypageService.getMyPageNewWallet(request);
    }


    /**
     *  방송설정 옵션 제목 & 인사말 추가
     */
    @PostMapping("/broadcast/option/add")
    public String broadcastTitleAdd(@Valid BroadcastOptionAddVo broadcastOptionAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result;
        if(broadcastOptionAddVo.getOptionType() == 1){
            if(broadcastOptionAddVo.getContents().length() > 20){
                return gsonUtil.toJson(new JsonOutputVo(Status.벨리데이션체크));
            }
            P_BroadcastTitleAddVo apiData = new P_BroadcastTitleAddVo(broadcastOptionAddVo, request);
            result = mypageService.callBroadcastTitleAdd(apiData);
        }else{
            if(broadcastOptionAddVo.getContents().length() > 100){
                return gsonUtil.toJson(new JsonOutputVo(Status.벨리데이션체크));
            }
            P_BroadcastWelcomeMsgAddVo apiData = new P_BroadcastWelcomeMsgAddVo(broadcastOptionAddVo, request);
            result = mypageService.callBroadcastWelcomeMsgAdd(apiData);
        }

        return result;
    }


    /**
     *  방송설정 옵션 제목 & 인사말 수정
     */
    @PostMapping("/broadcast/option/edit")
    public String broadcastTitleEdit(@Valid BroadcastOptionEditVo broadcastOptionEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result;
        if(broadcastOptionEditVo.getOptionType() == 1){
            if(broadcastOptionEditVo.getContents().length() > 20){
                return gsonUtil.toJson(new JsonOutputVo(Status.벨리데이션체크));
            }
            P_BroadcastTitleEditVo apiData = new P_BroadcastTitleEditVo(broadcastOptionEditVo, request);
            result = mypageService.callBroadcastTitleEdit(apiData);
        }else{
            if(broadcastOptionEditVo.getContents().length() > 100){
                return gsonUtil.toJson(new JsonOutputVo(Status.벨리데이션체크));
            }
            P_BroadcastWelcomeMsgEditVo apiData = new P_BroadcastWelcomeMsgEditVo(broadcastOptionEditVo, request);
           result = mypageService.callBroadcastWelcomeMsgEdit(apiData);
        }
        return result;
    }


    /**
     * 방송설정 옵션 제목 & 인사말 조회
     */
    @GetMapping("/broadcast/option")
    public String broadcastTitleSelect(HttpServletRequest request){

        String result;
        if("1".equals(request.getParameter("optionType"))){
            P_BroadcastOptionListVo apiData = new P_BroadcastOptionListVo(request);
            result = mypageService.callBroadcastTitleSelect(apiData,"");
        }else{
            P_BroadcastWelcomeMsgListVo apiData = new P_BroadcastWelcomeMsgListVo(request);
            result = mypageService.callBroadcastWelcomeMsgSelect(apiData,"");
        }
        return result;
    }


    /**
     *  방송설정 옵션 제목 & 인사말 삭제
     */
    @PostMapping("/broadcast/option/delete")
    public String broadcastTitleDelete(@Valid BroadcastOptionDeleteVo broadcastOptionDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result;
        if(broadcastOptionDeleteVo.getOptionType() == 1){
            P_BroadcastTitleDeleteVo apiData = new P_BroadcastTitleDeleteVo(broadcastOptionDeleteVo, request);
            result = mypageService.callBroadcastTitleDelete(apiData);
        }else{
            P_BroadcastWelcomeMsgDeleteVo apiData = new P_BroadcastWelcomeMsgDeleteVo(broadcastOptionDeleteVo, request);
            result = mypageService.callBroadcastWelcomeMsgDelete(apiData);
        }

        return result;
    }


    /**
     * 방송설정 조회하기(선물 시 자동 팬 추가 및 입/퇴장 메시지 설정)
     */
    @GetMapping("/broadcast/setting")
    public String broadcastSettingSelect(HttpServletRequest request){
        try {
            P_BroadcastSettingVo apiData = new P_BroadcastSettingVo(request);
            String result = (String) mypageService.callBroadcastSettingSelect(apiData, false);
            return result;
        } catch (Exception e) {
            log.error("MyPageController broadcastSettingSelect => {}", e);
            return null;
        }
    }


    /**
     * 방송설정 수정하기
     */
    @PostMapping("/broadcast/setting/edit")
    public String broadcastSettingEdit(@Valid BroadcastSettingEditVo broadcastSettingEditVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_BroadcastSettingEditVo apiData = new P_BroadcastSettingEditVo(broadcastSettingEditVo, request);

        String result = mypageService.callBroadcastSettingEdit(apiData, request, "edit");
        return result;
    }


    /**
     *  개인 좋아요 랭킹 리스트
     */
    @GetMapping("/good/list")
    public String memberGoodList(@Valid GoodListVo goodListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap resultMap = mypageService.getMemberGoodList(goodListVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status) resultMap.get("status"), resultMap.get("data")));
    }

    /**
     * 사연 모아보기
     */
    @GetMapping("/story")
    public String broadStoryList(@Valid StoryVo storyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap resultMap = mypageService.callMemberBoardStory(storyVo, request);
        HashMap returnMap = new HashMap<>();
        returnMap.put("data", resultMap.get("data"));
        returnMap.put("paging", resultMap.get("paging"));

        return gsonUtil.toJson(new JsonOutputVo((Status) resultMap.get("status"), returnMap));
    }

    /**
     * 사연 모아보기 청취자
     */
    @GetMapping("/story/send")
    public String sendStoryList(@Valid StoryVo storyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap resultMap = mypageService.callMemberBoardStorySend(storyVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status) resultMap.get("status"), resultMap.get("data")));
    }

    /**
     * 내지갑 달 or 별 팝업 리스트 & 건수
     */
    @GetMapping("/wallet/pop")
    public String walletPopupList(@Valid WalletPopupListVo walletPopupListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_WalletPopupListVo apiData = new P_WalletPopupListVo(walletPopupListVo, request);

        String result = mypageService.callWalletPopupListView(apiData);
        return result;
    }


    /**
     * 내 지갑 달 or 별 내역 보기
     */
    @GetMapping("/wallet/list")
    public String walletList(@Valid WalletListVo walletListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_WalletListVo apiData = new P_WalletListVo(walletListVo, request);

        String result = mypageService.callWalletList(apiData);
        return result;
    }

    /**
     * 환전 취소하기
     */
    @PostMapping("/exchange/cancel")
    public String exchangeCancel(@Valid ExchangeCancelVo exchangeCancelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeCancelVo apiData = new P_ExchangeCancelVo(exchangeCancelVo, request);
        String result = mypageService.callExchangeCancel(apiData);
        return result;
    }

    /**
     * 설정 방송공지 등록
     */
    @PostMapping("/broad/add")
    public String broadcastNoticeAdd(@Valid @RequestBody BroadcastNoticeAddVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.broadcastNoticeAdd(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / broadcastNoticeAdd Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }
    }

    /**
     * 설정 방송공지 수정
     */
    @PostMapping("/broad/edit")
    public String broadcastNoticeUpd(@Valid @RequestBody BroadcastNoticeUpdVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.broadcastNoticeUpd(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / broadcastNoticeUpd Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_실패));
        }
    }

    /**
     * 설정 방송공지 삭제
     */
    @DeleteMapping("/broad/del")
    public String broadcastNoticeDel(@Valid BroadcastNoticeDelVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return mypageService.broadcastNoticeDel(param, request);
    }

    /**
     * 설정 방송공지 조회
     */
    @GetMapping("/broad/sel")
    public String broadcastNoticeSel(@Valid BroadcastNoticeSelVo noticeSelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return mypageService.broadcastNoticeSel(noticeSelVo, request);
    }

    /**
     * 피드 등록
     */
    @PostMapping("/feed/add")
    public String feedAdd(@Valid @RequestBody MyPageFeedAddVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedAdd(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedAdd Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }
    }

    /**
     * 피드 조회
     */
    @GetMapping("/feed/sel")
    public String feedSelect(@Valid MyPageFeedListVo feedListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedSelect(feedListVo, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedSelect() => {}", e);
            HashMap resultMap = new HashMap();
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(0, 0, 0));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }
    }

    /**
     * 피드 수정
     */
    @PostMapping("/feed/edit")
    public String feedUpdate(@Valid @RequestBody MyPageFeedUpdVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedUpdate(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedUpdate Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_실패));
        }
    }

    /**
     * 피드 삭제
     */
    @DeleteMapping("/feed")
    public String feedDelete(@Valid MyPageFeedDelVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedDelete(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedDelete Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
        }
    }

    /**
     * 피드 상세 조회
     */
    @GetMapping("/feed/detail")
    public String feedDetailSelect(@Valid MyPageFeedDetailListVo feedDetailListVo, HttpServletRequest request) {
        try {
            return mypageService.feedDetailSelect(feedDetailListVo, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedDetailSelect Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
        }
    }

    /**
     * 피드 좋아요
     */
    @PostMapping("/feed/like")
    public String feedLike(@Valid MyPageFeedLikeVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedLike(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedLike Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패));
        }
    }

    /**
     * 피드 좋아요 취소
     */
    @PostMapping("/feed/cancel")
    public String feedLikeCancel(@Valid MyPageFeedLikeCancelVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedLikeCancel(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedLikeCancel Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_취소실패));
        }
    }

    /**
     * 피드 댓글 등록
     */
    @PostMapping("/feed/reply/add")
    public String feedReplyAdd(@Valid @RequestBody MyPageFeedReplyAddVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedReplyAdd(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedReplyAdd Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_실패));
        }
    }

    /**
     * 피드 댓글 수정
     */
    @PostMapping("/feed/reply/upd")
    public String feedReplyUpd(@Valid @RequestBody MyPageFeedReplyUpdVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedReplyUpd(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedReplyUpd Exception: {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_실패));
        }
    }

    /**
     * 피드 댓글 삭제
     */
    @DeleteMapping("/feed/reply")
    public String feedReplyDelete(@Valid MyPageFeedReplyDelVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedReplyDel(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedReplyDelete Exception: {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_실패));
        }
    }

    /**
     * 피드 댓글 조회
     */
    @GetMapping("/feed/reply/sel")
    public String feedReplyList(@Valid MyPageFeedReplyListVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.feedReplyList(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / feedReplyList Exception : {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_실패));
        }
    }

    /**
     * 방송공지 등록
     * @Param
     *  title       String 제목
     *  contents    String 내용
     *  topFix      Integer 상단 고정여부 [0, 1]
     *  photoInfoList List<ProfileFeedPhotoOutVo> 등록 이미지 리스트  [{img_name:""}, {img_name:""}, ...]
     * @Return
     *
     */
    @PostMapping("/notice/add")
    public String noticeAdd(@Valid @RequestBody ProfileFeedAddVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.noticeAdd(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeAdd Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }
    }

    /**
     * 방송공지 조회
     *
     * @Param
     * memNo            String  유저번호;
     * pageNo           Integer 페이지번호
     * pagePerCnt;      Integer 페이지당 리스트갯수
     *
     * @Return
     * # 1
     * cnt                  Integer     총 갯수
     *
     * # 2
     * noticeIdx;            Long		-- 번호
     * mem_no;               Long		-- 회원번호
     * nickName;             String	--닉네임
     * memSex;               String	-- 성별
     * image_profile;        String	-- 프로필
     * title;                String	-- 제목
     * contents;             String	-- 내용
     * imagePath;            String	-- 대표사진
     * topFix;               Long		-- 고정여부[0:미고정 ,1:고정]
     * writeDate;            String	-- 수정일자
     * readCnt;              Long		-- 읽은수
     * replyCnt;             Long		-- 댓글수
     * rcv_like_cnt;         Long		-- 좋아요수
     * rcv_like_cancel_cnt;  Long		-- 취소 좋아요수
     * ImageVo profImg;      String 프로필 이미지
     * List<ProfileFeedPhotoOutVo> photoInfoList;   게시글 사진 리스트
     */
    @GetMapping("/notice/sel")
    public String noticeSelect(@Valid ProfileFeedSelVo noticeSelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.noticeSelect(noticeSelVo.getMemNo(), noticeSelVo.getPageNo(), noticeSelVo.getPageCnt(), request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeSelect() => {}", e);
            HashMap resultMap = new HashMap();
//            resultMap.put("fixList", new ArrayList());
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(0, 0, 0));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }
    }

    /**
     * 방송공지 리스트(고정) 조회
     */
    @GetMapping("notice/fix/sel")
    public String noticeFixSelect(@Valid ProfileFeedFixSelVo noticeFixSelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.noticeFixSelect(noticeFixSelVo.getMemNo(), noticeFixSelVo.getPageNo(), noticeFixSelVo.getPageCnt(), request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeFixSelect() => {}", e);
            HashMap resultMap = new HashMap();
            resultMap.put("fixList", new ArrayList());
            resultMap.put("paging", new PagingVo(0, 0, 0));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }
    }

    /**
     * 방송공지 수정
     *
     * @Param
     * title                String  제목
     * contents             String  내용
     * noticeIdx            Integer 공지글번호
     * topFix               Integer 고정여부 [0: 고정안함, 1: 고정함]
     * photoInfoList List<ProfileFeedPhotoOutVo> 등록 이미지 리스트  [{img_name:""}, {img_name:""}, ...]
     *
     * @Return
     * */
    @PostMapping("/notice/edit")
    public String noticeUpdate(@Valid @RequestBody ProfileFeedUpdVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.noticeUpdate(param, request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeUpdate Error {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_실패));
        }
    }

    /**
     * 방송공지 삭제
     *
     * @Param
     * noticeIdx                Long  공지글번호
     * delChrgrName             String  제목
     *
     * @Return
     * */
    @DeleteMapping("/notice")
    public String noticeDelete(@Valid ProfileFeedDelVo param, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return mypageService.noticeDelete(param);
    }

    /**
     * 방송공지 상세조회
     *
     * @param
     * feedNo              Integer 공지글 번호
     * memNo;              String  프로필 주인의 memNo
     *
     * @return
     * noticeIdx        BIGINT		-- 번호
     * mem_no		BIGINT		-- 회원번호
     * nickName	VARCHAR	--닉네임
     * memSex		VARCHAR	-- 성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 제목
     * contents		VARCHAR	-- 내용
     * imagePath	VARCHAR	-- 대표사진
     * topFix		BIGINT		-- 고정여부[0:미고정 ,1:고정]
     * writeDate		DATETIME	-- 수정일자
     * readCnt		BIGINT		-- 읽은수
     * replyCnt		BIGINT		-- 댓글수
     * rcv_like_cnt	BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt BIGINT		-- 취소 좋아요수
     */
    @GetMapping("/notice/detail")
    public String noticeDetailSelect(@Valid ProfileFeedDetailSelVo noticeSelVo, HttpServletRequest request) {
        try {
            return mypageService.noticeDetailSelect(noticeSelVo, request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeDetailSelect Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
        }
    }

    /**
     * 방송공지 좋아요
     */
    @PostMapping("/notice/like")
    public String noticeLike(@Valid ProfileFeedLikeVo feedLikeVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.noticeLike(feedLikeVo, request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeLike Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패));
        }
    }

    /**
     * 방송공지 좋아요 취소
     */
    @PostMapping("/notice/cancel")
    public String noticeLikeCancel(@Valid ProfileFeedLikeCancelVo feedLikeCancelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return mypageService.noticeLikeCancel(feedLikeCancelVo, request);
        } catch (Exception e) {
            log.error("MypageController.java / noticeLikeCancel Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_취소실패));
        }
    }

}
