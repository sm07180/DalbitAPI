package com.dalbit.member.controller;

import com.dalbit.common.vo.DeviceVo;
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
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    MypageService mypageService;

    /**
     * 본인 정보 조회
     */
    @GetMapping("")
    public String memberInfo(HttpServletRequest request){
        P_MemberInfoVo apiData = new P_MemberInfoVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = mypageService.callMemberInfo(apiData);

        return result;
    }


    /**
     * 프로필편집
     */
    @PostMapping("/profile")
    public String editProfile(@Valid ProfileEditVo profileEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ProfileEditVo apiData = new P_ProfileEditVo();

        apiData.setMem_no(new MemberVo().getMyMemNo(request));
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
        apiData.setFan_mem_no(new MemberVo().getMyMemNo(request));
        apiData.setStar_mem_no(fanstartInsertVo.getMemNo());

        String result = mypageService.callFanstarInsert(apiData);

        return result;
    }


    /**
     * 팬 해제
     */
    @DeleteMapping("/fan")
    public String fanstarDelete(@Valid FanstarDeleteVo fanstarDeleteVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanstarDeleteVo apiData = new P_FanstarDeleteVo();
        apiData.setFan_mem_no(new MemberVo().getMyMemNo(request));
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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));

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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));

        String result = mypageService.callMemberNotify(apiData);

        return result;
    }

    /**
     * 회원 알림설정 수정하기
     */
    @PostMapping("/notify")
    public String memberNotifyEdit(@Valid MemberNotifyEditVo memberNotifyEditVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MemberNotifyEditVo apiData = new P_MemberNotifyEditVo();

        apiData.setMem_no(new MemberVo().getMyMemNo(request));
        apiData.setAll_ok(memberNotifyEditVo.getIsAll());
        apiData.setSet_1(memberNotifyEditVo.getIsMyStar());
        apiData.setSet_2(memberNotifyEditVo.getIsGift());
        apiData.setSet_3(memberNotifyEditVo.getIsFan());
        apiData.setSet_4(memberNotifyEditVo.getIsComment());
        apiData.setSet_5(memberNotifyEditVo.getIsRadio());
        apiData.setSet_6(memberNotifyEditVo.getIsPush());
        apiData.setSet_7(memberNotifyEditVo.getIsLike());

        String result = mypageService.callMemberNotifyEdit(apiData);

        return result;
    }

    /**
     * 회원 방송방 빠른말 가져오기
     */
    @GetMapping("/shortcut")
    public String memberShortCut(HttpServletRequest request){
        P_MemberShortCutVo apiData = new P_MemberShortCutVo();
        apiData.setMem_no(new MemberVo().getMyMemNo(request));

        String result = mypageService.callMemberShortCut(apiData);

        return result;
    }

    /**
     * 회원 방송방 빠른말 수정하기
     */
    @PostMapping("/shortcut")
    public String memberShortCutEdit(@Valid ShortCutEditVo shortCutEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String isOn = (shortCutEditVo.getIsOn().toUpperCase().equals("1") || shortCutEditVo.getIsOn().toUpperCase().equals("TRUE")) ? "on" : "off";

        P_MemberShortCutEditVo apiData = new P_MemberShortCutEditVo();
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
        apiData.setOrderNo(shortCutEditVo.getOrderNo());
        apiData.setOrder(shortCutEditVo.getOrder());
        apiData.setText(shortCutEditVo.getText());
        apiData.setOnOff(isOn);

        String result = mypageService.callMemberShortCutEdit(apiData);

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
    @PostMapping("/notice/add")
    public String noticeAdd(@Valid MypageNoticeAddVo mypageNoticeAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeAddVo apiData = new P_MypageNoticeAddVo(mypageNoticeAddVo, request);

        String result = mypageService.callMypageNoticeAdd(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 수정
     */
    @PostMapping("/notice/edit")
    public String noticeEdit(@Valid MypageNoticeEditVo mypageNoticeEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeEditVo apiData = new P_MypageNoticeEditVo(mypageNoticeEditVo, request);

        String result = mypageService.callMypageNoticeEdit(apiData);

        return result;
    }


    /**
     * 마이페이지 공지사항 삭제
     */
    @DeleteMapping("/notice")
    public String noticeDel(@Valid MypageNoticeDelVo mypageNoticeDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeDelVo apiData = new P_MypageNoticeDelVo(mypageNoticeDelVo, request);

        String result = mypageService.callMypageNoticeDel(apiData);

        return result;
    }

    /**
     * 마이페이지 공지사항 조회
     */
    @GetMapping("/notice")
    public String noticeView(@Valid MypageNoticeSelectVo mypageNoticeSelectVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MypageNoticeSelectVo apiData = new P_MypageNoticeSelectVo(mypageNoticeSelectVo, request);

        String result = mypageService.callMypageNoticeSelect(apiData);

        return result;
    }

    /**
     * 내지갑 달 내역 조회
     */
    @GetMapping("/dal")
    public String dalView(@Valid DalVo dalVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_DalVo apiData = new P_DalVo(dalVo, request);

        String result = mypageService.callMemberWalletDal(apiData);

        return result;
    }

    /**
     * 내지갑 별 내역 조회
     */
    @GetMapping("/byeol")
    public String byeolView(@Valid ByeolVo byeolVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ByeolVo apiData = new P_ByeolVo(byeolVo, request);

        String result = mypageService.callMemberWalletByeol(apiData);

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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));

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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
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
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
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
        String result = mypageService.callMypageBlackListAdd(apiData);

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
}
