package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.ProfileEditValidationVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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
    public String editProfile(@Valid ProfileEditValidationVo profileEditValidationVo, BindingResult bindingResult) throws GlobalException {

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        P_ProfileEditVo apiData = new P_ProfileEditVo();

        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setMemSex(profileEditValidationVo.getGender());
        apiData.setNickName(profileEditValidationVo.getNickNm());
        apiData.setName(profileEditValidationVo.getName());
        apiData.setBirthYear(LocalDate.parse(profileEditValidationVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE).getYear());
        apiData.setBirthMonth(LocalDate.parse(profileEditValidationVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE).getMonthValue());
        apiData.setBirthDay(LocalDate.parse(profileEditValidationVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE).getDayOfMonth());
        apiData.setProfileImage(profileEditValidationVo.getProfImg());
        apiData.setProfileImageGrade(profileEditValidationVo.getProfImgRacy());
        apiData.setBackgroundImage(profileEditValidationVo.getBgImg());
        apiData.setBackgroundImageGrade(profileEditValidationVo.getBgImgRacy());
        apiData.setProfileMsg(profileEditValidationVo.getProfMsg());

        String result = mypageService.callProfileEdit(apiData);
        return result;
    }

    /**
     * 팬등록
     */
    @PostMapping("/fan")
    public String fanstarInsert(HttpServletRequest request){
        P_FanstarInsertVo apiData = new P_FanstarInsertVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo());
        apiData.setStar_mem_no(DalbitUtil.convertRequestParamToString(request,"memNo"));
        String result = mypageService.callFanstarInsert(apiData);

        return result;
    }

    /**
     * 팬해제
     */
    @DeleteMapping("/fan")
    public String fanstarDelete(HttpServletRequest request){
        P_FanstarDeleteVo apiData = new P_FanstarDeleteVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo());
        apiData.setStar_mem_no(DalbitUtil.convertRequestParamToString(request,"memNo"));
        String result = mypageService.callFanstarDelete(apiData);

        return result;
    }

    /**
     * 회원 정보 조회
     */
    @GetMapping("")
    public String memberInfo(HttpServletRequest request){
        P_MemberInfoVo apiData = new P_MemberInfoVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setTarget_mem_no(MemberVo.getMyMemNo());
        String result = mypageService.callMemberInfo(apiData);
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
    public String broadBasicEdit(HttpServletRequest request){
        P_BroadBasicEditVo apiData = new P_BroadBasicEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request,"roomType"));
        apiData.setTitle(DalbitUtil.convertRequestParamToString(request,"title"));
        apiData.setBackgroundImage(DalbitUtil.convertRequestParamToString(request,"bgImg"));
        apiData.setBackgroundImageGrade(DalbitUtil.convertRequestParamToInteger(request,"bgImgRacy"));
        apiData.setWelcomMsg(DalbitUtil.convertRequestParamToString(request,"wecomMsg"));
        apiData.setNotice(DalbitUtil.convertRequestParamToString(request,"notice"));
        apiData.setEntryType(DalbitUtil.convertRequestParamToInteger(request,"entryType"));

        String result = mypageService.callBroadBasicEdit(apiData);
        return result;
    }

    /**
     * 회원 신고하기
     */
    @PostMapping("/declar")
    public String memberReportAdd(HttpServletRequest request){
        P_MemberReportAddVo apiData = new P_MemberReportAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setReported_mem_no(DalbitUtil.convertRequestParamToString(request,"memNo"));
        apiData.setReason(DalbitUtil.convertRequestParamToInteger(request,"reason"));
        apiData.setEtc(DalbitUtil.convertRequestParamToString(request,"cont"));
        String result = mypageService.callMemberReportAdd(apiData);
        return result;
    }

    /**
     * 회원 차단하기
     */
    @PostMapping("/block")
    public String broadBlock_Add(HttpServletRequest request){
        P_MemberBlockAddVo apiData = new P_MemberBlockAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setBlocked_mem_no(DalbitUtil.convertRequestParamToString(request,"memNo"));
        String result = mypageService.callBlockAdd(apiData);
        return result;
    }

    /**
     * 회원 차단 해제하기
     */
    @DeleteMapping("/block")
    public String broadBlock_Del(HttpServletRequest request){
        P_MemberBlockDelVo apiData = new P_MemberBlockDelVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setBlocked_mem_no(DalbitUtil.convertRequestParamToString(request,"memNo"));
        String result = mypageService.callMemBerBlocklDel(apiData);
        return result;
    }

    /**
     * 회원 알림설정 조회하기
     */
    @GetMapping("/notify")
    public String memberNotify(HttpServletRequest request){
        P_MemberNotifyVo apiData = new P_MemberNotifyVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        String result = mypageService.callMemberNotify(apiData);
        return result;
    }

    /**
     * 회원 알림설정 수정하기
     */
    @PostMapping("/notify")
    public String memberNotifyEdit(HttpServletRequest request){
        P_MemberNotifyEditVo apiData = new P_MemberNotifyEditVo();

        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setAll_ok(DalbitUtil.convertRequestParamToInteger(request,"isAll"));
        apiData.setFan_reg(DalbitUtil.convertRequestParamToInteger(request,"isFanReg"));
        apiData.setFan_board(DalbitUtil.convertRequestParamToInteger(request,"isBoard"));
        apiData.setStar_broadcast(DalbitUtil.convertRequestParamToInteger(request,"isStarCast"));
        apiData.setStar_notice(DalbitUtil.convertRequestParamToInteger(request,"isStarNoti"));
        apiData.setEvent_notice(DalbitUtil.convertRequestParamToInteger(request,"isEvtNoti"));
        apiData.setSearch(DalbitUtil.convertRequestParamToInteger(request,"isSearch"));

        String result = mypageService.callMemberNotifyEdit(apiData);
        return result;
    }

    /**
     * 회원 방송방 빠른말 가져오기
     */
    @GetMapping("/shortcut")
    public String memberShortCut(HttpServletRequest request){
        P_MemberShortCutVo apiData = new P_MemberShortCutVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        String result = mypageService.callMemberShortCut(apiData);
        return result;
    }

    /**
     * 회원 방송방 빠른말 수정하기
     */
    @PostMapping("/shortcut")
    public String memberShortCutEdit(HttpServletRequest request){
        P_MemberShortCutEditVo apiData = new P_MemberShortCutEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        String[] data = request.getParameterValues("data");
        if(data != null && data.length > 0){
            for(int i = 0; i < data.length; i++){
                HashMap<String, String> d = new Gson().fromJson(data[i], HashMap.class);
                if(!DalbitUtil.isEmpty(d.get("order")) && !DalbitUtil.isEmpty(d.get("text")) && !DalbitUtil.isEmpty(d.get("isOn"))){
                    if(i == 0){
                        apiData.setOrder_1(d.get("order"));
                        apiData.setText_1(d.get("text"));
                        apiData.setOnOff_1(d.get("isOn").toUpperCase().equals("1") || d.get("isOn").toUpperCase().equals("TRUE") ? "on" : "off");
                    }else if(i == 1){
                        apiData.setOrder_2(d.get("order"));
                        apiData.setText_2(d.get("text"));
                        apiData.setOnOff_2(d.get("isOn").toUpperCase().equals("1") || d.get("isOn").toUpperCase().equals("TRUE") ? "on" : "off");
                    }else if(i == 2) {
                        apiData.setOrder_3(d.get("order"));
                        apiData.setText_3(d.get("text"));
                        apiData.setOnOff_3(d.get("isOn").toUpperCase().equals("1") || d.get("isOn").toUpperCase().equals("TRUE") ? "on" : "off");
                    }
                }
            }
        }

        String result = mypageService.callMemberShortCutEdit(apiData);
        return result;
    }
}
