package com.dalbit.member.controller;

import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("mypage")
public class MypageController {

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MypageService mypageService;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * 프로필편집
     */
    @PostMapping("/profile")
    public String editProfile(HttpServletRequest request){

        P_ProfileEditVo apiData = P_ProfileEditVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .memSex(DalbitUtil.convertRequestParamToString(request,"s_gender"))
                .nickName(DalbitUtil.convertRequestParamToString(request,"s_nickNm"))
                .name(DalbitUtil.convertRequestParamToString(request,"s_name"))
                .birthYear(DalbitUtil.convertRequestParamToInteger(request,"i_birthYY"))
                .birthMonth(DalbitUtil.convertRequestParamToInteger(request,"i_birthMM"))
                .birthDay(DalbitUtil.convertRequestParamToInteger(request,"i_birthDD"))
                .profileImage(DalbitUtil.convertRequestParamToString(request,"s_profImg"))
                .profileImage(DalbitUtil.convertRequestParamToString(request,"s_bgImg"))
                .profileMsg(DalbitUtil.convertRequestParamToString(request,"s_message"))
                .build();

        String result = mypageService.callProfileEdit(apiData);
        return result;
    }

    /**
     * 팬가입
     */
    @PostMapping("/fan")
    public String fanstarInsert(HttpServletRequest request){
        P_FanstarInsertVo apiData = P_FanstarInsertVo.builder()
                .fanMemNo(MemberVo.getUserInfo().getMem_no())       //11577690655946
                .starMemNo(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))  //11577931027280
                .build();

        String result = mypageService.callFanstarInsert(apiData);

        return result;
    }

    /**
     * 팬해제
     */
    @DeleteMapping("/fan")
    public String fanstarDelete(HttpServletRequest request){
        P_FanstarDeleteVo apiData = P_FanstarDeleteVo.builder()
                .fanMemNo(MemberVo.getUserInfo().getMem_no())
                .starMemNo(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = mypageService.callFanstarDelete(apiData);

        return result;
    }

    /**
     * 회원 방송방 기본설정 조회
     */
    @GetMapping("/broad")
    public String broadBasic(){
        P_BroadBasicVo apiData = P_BroadBasicVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .build();

        String result = mypageService.callBroadBasic(apiData);
        return result;
    }

    /**
     * 회원 정보 조회
     */
    @GetMapping("")
    public String memverInfo(HttpServletRequest request){
        P_MemberInfoVo apiData = P_MemberInfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .target_mem_no(DalbitUtil.convertRequestParamToString(request,"target_mem_no"))
                .build();

        String result = mypageService.callMemberInfo(apiData);
        return result;
    }

    /**
     * 회원 방송방 기본설정 수정하기
     */
    @PostMapping("/broad")
    public String broadBasicEdit(HttpServletRequest request){
        P_BroadBasicEditVo apiData = P_BroadBasicEditVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .subjectType(DalbitUtil.convertRequestParamToInteger(request,"i_type"))
                .title(DalbitUtil.convertRequestParamToString(request,"s_title"))
                .backgroundImage(DalbitUtil.convertRequestParamToString(request,"s_bgImg"))
                .backgroundImageGrade(DalbitUtil.convertRequestParamToInteger(request,"i_bgRacy"))
                .welcomMsg(DalbitUtil.convertRequestParamToString(request,"s_welcome"))
                .notice(DalbitUtil.convertRequestParamToString(request,"s_notice"))
                .entry(DalbitUtil.convertRequestParamToInteger(request,"i_entry"))
                .age(DalbitUtil.convertRequestParamToInteger(request,"i_age"))
                .build();

        String result = mypageService.callBroadBasicEdit(apiData);
        return result;
    }

    /**
     * 회원 신고하기
     */
    @PostMapping("/declar")
    public String memberReportAdd(HttpServletRequest request){
        P_MemberReportAddVo apiData = P_MemberReportAddVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .reported_mem_no(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .reason(DalbitUtil.convertRequestParamToInteger(request,"i_reason"))
                .etc(DalbitUtil.convertRequestParamToString(request,"s_cont"))
                .build();

        String result = mypageService.callMemberReportAdd(apiData);
        return result;
    }

    /**
     * 회원 차단하기
     */
    @PostMapping("/block")
    public String broadBlock_Add(HttpServletRequest request){
        P_MemberBlockAddVo apiData = P_MemberBlockAddVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .blocked_mem_no(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = mypageService.callBlockAdd(apiData);
        return result;
    }

    /**
     * 회원 차단 해제하기
     */
    @DeleteMapping("/block")
    public String broadBlock_Del(HttpServletRequest request){
        P_MemberBlockDelVo apiData = P_MemberBlockDelVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .blocked_mem_no(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = mypageService.callMemBerBlocklDel(apiData);
        return result;
    }


    /**
     * 회원 알림설정 조회하기
     */
    @GetMapping("/notify")
    public String memberNotify(HttpServletRequest request){
        P_MemberNotifyVo apiData = P_MemberNotifyVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .build();

        String result = mypageService.callMemberNotify(apiData);
        return result;
    }
    /**
     * 회원 알림설정 수정하기
     */
    @PostMapping("/notify")
    public String memberNotifyEdit(HttpServletRequest request){
        P_MemberNotifyEditVo apiData = P_MemberNotifyEditVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .all_ok(DalbitUtil.convertRequestParamToInteger(request,"i_all"))
                .fan_reg(DalbitUtil.convertRequestParamToInteger(request,"i_fan"))
                .fan_board(DalbitUtil.convertRequestParamToInteger(request,"i_board"))
                .star_broadcast(DalbitUtil.convertRequestParamToInteger(request,"i_cast"))
                .star_notice(DalbitUtil.convertRequestParamToInteger(request,"i_notice"))
                .event_notice(DalbitUtil.convertRequestParamToInteger(request,"i_event"))
                .search(DalbitUtil.convertRequestParamToInteger(request,"i_search"))
                .build();

        String result = mypageService.callMemberNotifyEdit(apiData);
        return result;
    }
}
