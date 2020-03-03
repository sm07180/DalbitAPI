package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.*;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("mypage")
public class MypageController {

    @Autowired
    MypageService mypageService;

    /**
     * 본인 정보 조회
     */
    @GetMapping("")
    public String memberInfo(){
        P_MemberInfoVo apiData = new P_MemberInfoVo();
        apiData.setMem_no(MemberVo.getMyMemNo());

        String result = mypageService.callMemberInfo(apiData);

        return result;
    }


    /**
     * 프로필편집
     */
    @PostMapping("/profile")
    public String editProfile(@Valid ProfileEditVo profileEditVo, BindingResult bindingResult) throws GlobalException {

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        P_ProfileEditVo apiData = new P_ProfileEditVo();

        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setMemSex(profileEditVo.getGender());
        apiData.setNickName(profileEditVo.getNickNm());
        apiData.setName(profileEditVo.getName());
        apiData.setBirthYear(LocalDate.parse(profileEditVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE).getYear());
        apiData.setBirthMonth(LocalDate.parse(profileEditVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE).getMonthValue());
        apiData.setBirthDay(LocalDate.parse(profileEditVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE).getDayOfMonth());
        apiData.setProfileImage(profileEditVo.getProfImg());
        apiData.setProfileImageGrade(DalbitUtil.isStringToNumber(profileEditVo.getProfImgRacy()));
        apiData.setProfileMsg(profileEditVo.getProfMsg());
        apiData.setProfImgDel(profileEditVo.getProfImgDel());

        String result = mypageService.callProfileEdit(apiData);

        return result;
    }


    /**
     * 팬등록
     */
    @PostMapping("/fan")
    public String fanstarInsert(@Valid FanstartInsertVo fanstartInsertVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_FanstarInsertVo apiData = new P_FanstarInsertVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo());
        apiData.setStar_mem_no(fanstartInsertVo.getMemNo());

        String result = mypageService.callFanstarInsert(apiData);

        return result;
    }


    /**
     * 팬해제
     */
    @DeleteMapping("/fan")
    public String fanstarDelete(@Valid FanstarDeleteVo fanstarDeleteVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_FanstarDeleteVo apiData = new P_FanstarDeleteVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo());
        apiData.setStar_mem_no(fanstarDeleteVo.getMemNo());

        String result = mypageService.callFanstarDelete(apiData);

        return result;
    }


    /**
     * 회원 방송방 기본설정 조회
     */
    @GetMapping("/broad")
    public String broadBasic(){
        P_BroadBasicVo apiData = new P_BroadBasicVo();
        apiData.setMem_no(MemberVo.getMyMemNo());

        String result = mypageService.callBroadBasic(apiData);

        return result;
    }


    /**
     * 회원 방송방 기본설정 수정하기
     */
    @PostMapping("/broad")
    public String broadBasicEdit(@Valid BroadBasicEditVo broadBasicEditVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_BroadBasicEditVo apiData = new P_BroadBasicEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
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
    public String memberReportAdd(@Valid MemberReportAddVo memberReportAddVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_MemberReportAddVo apiData = new P_MemberReportAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setReported_mem_no(memberReportAddVo.getMemNo());
        apiData.setReason(memberReportAddVo.getReason());
        apiData.setEtc(memberReportAddVo.getCont());

        String result = mypageService.callMemberReportAdd(apiData);

        return result;
    }


    /**
     * 회원 차단하기
     */
    @PostMapping("/block")
    public String broadBlock_Add(@Valid MemberBlockAddVo memberBlockAddVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_MemberBlockAddVo apiData = new P_MemberBlockAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setBlocked_mem_no(memberBlockAddVo.getMemNo());

        String result = mypageService.callBlockAdd(apiData);

        return result;
    }


    /**
     * 회원 차단 해제하기
     */
    @DeleteMapping("/block")
    public String broadBlock_Del(@Valid MemberBlockDelVo memberBlockDelVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_MemberBlockDelVo apiData = new P_MemberBlockDelVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setBlocked_mem_no(memberBlockDelVo.getMemNo());

        String result = mypageService.callMemBerBlocklDel(apiData);

        return result;
    }


    /**
     * 회원 알림설정 조회하기
     */
    @GetMapping("/notify")
    public String memberNotify(){
        P_MemberNotifyVo apiData = new P_MemberNotifyVo();
        apiData.setMem_no(MemberVo.getMyMemNo());

        String result = mypageService.callMemberNotify(apiData);

        return result;
    }

    /**
     * 회원 알림설정 수정하기
     */
    @PostMapping("/notify")
    public String memberNotifyEdit(@Valid MemberNotifyEditVo memberNotifyEditVo, BindingResult bindingResult)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_MemberNotifyEditVo apiData = new P_MemberNotifyEditVo();

        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setAll_ok(memberNotifyEditVo.getIsAll());
        apiData.setSet_1(memberNotifyEditVo.getIsMyStar());
        apiData.setSet_2(memberNotifyEditVo.getIsGift());
        apiData.setSet_3(memberNotifyEditVo.getIsFan());
        apiData.setSet_4(memberNotifyEditVo.getIsComment());
        apiData.setSet_5(memberNotifyEditVo.getIsRadio());
        apiData.setSet_6(memberNotifyEditVo.getIsEvent());

        String result = mypageService.callMemberNotifyEdit(apiData);

        return result;
    }

    /**
     * 회원 방송방 빠른말 가져오기
     */
    @GetMapping("/shortcut")
    public String memberShortCut(){
        P_MemberShortCutVo apiData = new P_MemberShortCutVo();
        apiData.setMem_no(MemberVo.getMyMemNo());

        String result = mypageService.callMemberShortCut(apiData);

        return result;
    }

    /**
     * 회원 방송방 빠른말 수정하기
     */
    @PostMapping("/shortcut")
    public String memberShortCutEdit(@Valid ShortCutEditVo shortCutEditVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        String isOn = (shortCutEditVo.getIsOn().toUpperCase().equals("1") || shortCutEditVo.getIsOn().toUpperCase().equals("TRUE")) ? "on" : "off";

        P_MemberShortCutEditVo apiData = new P_MemberShortCutEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setOrderNo(shortCutEditVo.getOrderNo());
        apiData.setOrder(shortCutEditVo.getOrder());
        apiData.setText(shortCutEditVo.getText());
        apiData.setOnOff(isOn);

        String result = mypageService.callMemberShortCutEdit(apiData);

        return result;
    }

    /**
     * 회원 루비선물하기
     */
    @PostMapping("/gift")
    public String memberGift(@Valid RubyVo rubyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RubyVo apiData = new P_RubyVo(rubyVo);
        String result = mypageService.callMemberGiftRuby(apiData, request);

        return result;
    }

    /**
     * 회원 알림 내용 조회
     */
    @GetMapping("/notification")
    public String memberNotification(@Valid NotificationVo notificationVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);
        P_NotificationVo apiData = new P_NotificationVo(notificationVo);

        String result = mypageService.callMemberNotification(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 등록
     */
    @PostMapping("/notice/add")
    public String noticeAdd(@Valid MypageNoticeAddVo mypageNoticeAddVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MypageNoticeAddVo apiData = new P_MypageNoticeAddVo(mypageNoticeAddVo);

        String result = mypageService.callMypageNoticeAdd(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 수정
     */
    @PostMapping("/notice/edit")
    public String noticeEdit(@Valid MypageNoticeEditVo mypageNoticeEditVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MypageNoticeEditVo apiData = new P_MypageNoticeEditVo(mypageNoticeEditVo);

        String result = mypageService.callMypageNoticeEdit(apiData);

        return result;
    }


    /**
     * 마이페이지 공지사항 삭제
     */
    @DeleteMapping("/notice")
    public String noticeDel(@Valid MypageNoticeDelVo mypageNoticeDelVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MypageNoticeDelVo apiData = new P_MypageNoticeDelVo(mypageNoticeDelVo);

        String result = mypageService.callMypageNoticeDel(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 조회
     */
    @GetMapping("/notice")
    public String noticeView(@Valid MypageNoticeSelectVo mypageNoticeSelectVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MypageNoticeSelectVo apiData = new P_MypageNoticeSelectVo(mypageNoticeSelectVo);

        String result = mypageService.callMypageNoticeSelect(apiData);

        return result;
    }

    /**
     * 내지갑 달 내역 조회
     */
    @GetMapping("/dal")
    public String dalView(@Valid DalVo dalVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_DalVo apiData = new P_DalVo(dalVo);

        String result = mypageService.callMemberWalletDal(apiData);

        return result;
    }

    /**
     * 내지갑 별 내역 조회
     */
    @GetMapping("/byeol")
    public String byeolView(@Valid ByeolVo byeolVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_ByeolVo apiData = new P_ByeolVo(byeolVo);

        String result = mypageService.callMemberWalletByeol(apiData);

        return result;
    }

    /**
     * 마이페이지 리포트 방송내역 보기
     */
    @GetMapping("/report/broad")
    public String reportBroadView(@Valid MypageReportBroadVo mypageReportBroadVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MypageReportBroadVo apiData = new P_MypageReportBroadVo(mypageReportBroadVo);

        String result = mypageService.callMypageMypageReportBroad(apiData);

        return result;
    }

    /**
     * 마이페이지 리포트 청취내역 보기
     */
    @GetMapping("/report/listen")
    public String reportListenView(@Valid MypageReportListenVo mypageReportListenVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MypageReportListenVo apiData = new P_MypageReportListenVo(mypageReportListenVo);

        String result = mypageService.callMypageMypageReportListen(apiData);

        return result;
    }

    /**
     * 방송설정 금지어 단어 조회
     */
    @GetMapping("/banwrod")
    public String getBanWrod(){
        P_BanWordSelectVo apiData = new P_BanWordSelectVo();
        apiData.setMem_no(MemberVo.getMyMemNo());

        String result = mypageService.callMyapgeGetBanWord(apiData);

        return result;
    }

    /**
     * 방송설정 금지어 저장
     */
    @PostMapping("/banword")
    public String insertBanWrod(@Valid BanWordVo banWordVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_BanWordInsertVo apiData = new P_BanWordInsertVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setBanWord(banWordVo.getBanWord());

        String result = mypageService.callMypageInsertBanWord(apiData);

        return result;
    }

    /**
     * 방송설정 유저 검색
     */
    @GetMapping("/search")
    public String searchUser(@Valid SearchUserVo searchUserVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_SearchUserVo apiData = new P_SearchUserVo(searchUserVo);
        String result = mypageService.callMypageSearchUser(apiData);

        return result;
    }

    /**
     * 방송설정 고정 매니저 조회
     */
    @GetMapping("/manager")
    public String getManager(){

        P_MypageManagerVo apiData = new P_MypageManagerVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        String result = mypageService.callMypageManager(apiData);

        return result;
    }


    /**
     * 방송설정 고정 매니저 등록
     */
    @PostMapping("/manager/add")
    public String addManager(@Valid MypageManagerAddVo mypageManagerAddVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_MypageManagerAddVo apiData = new P_MypageManagerAddVo(mypageManagerAddVo);
        String result = mypageService.callMypageManagerAdd(apiData);

        return result;
    }


    /**
     * 방송설정 고정 매니저 권한수정
     */
    @PostMapping("/manager/edit")
    public String editManager(@Valid MypageManagerEditVo mypageManagerEditVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_MypageManagerEditVo apiData = new P_MypageManagerEditVo(mypageManagerEditVo);
        String result = mypageService.callMypageManagerEdit(apiData);

        return result;
    }


    /**
     * 방송설정 고정 매니저 해제
     */
    @DeleteMapping("/manager")
    public String delManager(@Valid MypageManagerDelVo mypageManagerDelVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_MypageManagerDelVo apiData = new P_MypageManagerDelVo(mypageManagerDelVo);
        String result = mypageService.callMypageManagerDel(apiData);

        return result;
    }

    /**
     * 방송설정 블랙리스트 조회
     */
    @GetMapping("/black")
    public String getBlackList(@Valid MypageBlackVo mypageBlackVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_MypageBlackVo apiData = new P_MypageBlackVo(mypageBlackVo);
        String result = mypageService.callMypageBlackListView(apiData);

        return result;
    }

    /**
     * 방송설정 블랙리스트 등록
     */
    @PostMapping("/black/add")
    public String addBlackList(@Valid MypageBlackAddVo mypageBlackAddVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_MypageBlackAddVo apiData = new P_MypageBlackAddVo(mypageBlackAddVo);
        String result = mypageService.callMypageBlackListAdd(apiData);

        return result;
    }

    /**
     * 방송설정 블랙리스트 해제
     */
    @DeleteMapping("/black")
    public String delBlackList(@Valid MypageBlackDelVo mypageBlackDelVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_MypageBlackDelVo apiData = new P_MypageBlackDelVo(mypageBlackDelVo);
        String result = mypageService.callMypageBlackListDel(apiData);

        return result;
    }
}
