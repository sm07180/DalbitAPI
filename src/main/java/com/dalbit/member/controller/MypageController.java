package com.dalbit.member.controller;

import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import io.swagger.annotations.ApiOperation;
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


    /**
     * 프로필편집
     */
    @ApiOperation(value = "프로필편집")
    @PostMapping("/profile")
    public String editProfile(HttpServletRequest request){
        P_ProfileEditVo apiData = P_ProfileEditVo.builder()
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

        log.info("playToken: {}", apiData.getName());

        String result = mypageService.callProfileEdit(apiData);
        return result;
    }

    /**
     * 팬가입
     */
    @ApiOperation(value = "팬가입")
    @PostMapping("/fan")
    public String fanstarInsert(HttpServletRequest request){
        //참가를 위한 토큰 받기
        P_FanstarInsertVo apiData = P_FanstarInsertVo.builder()
//                .fanMemNo(MemberVo.getUserInfo().getMem_no())
                .starMemNo(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = mypageService.callFanstarInsert(apiData);

        return result;
    }

    /**
     * 팬해제
     */
    @ApiOperation(value = "팬 해제")
    @DeleteMapping("/fan")
    public String fanstarDelete(HttpServletRequest request){
        //참가를 위한 토큰 받기
        P_FanstarDeleteVo apiData = P_FanstarDeleteVo.builder()
//                .fanMemNo(MemberVo.getUserInfo().getMem_no())
                .starMemNo(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = mypageService.callFanstarDelete(apiData);

        return result;
    }


}
