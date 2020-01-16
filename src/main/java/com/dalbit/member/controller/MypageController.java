package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.P_InfoVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    MemberService memberService;

    @ApiOperation(value = "회원정보 조회")
    @GetMapping("")
    public String mypage() throws GlobalException {

        P_InfoVo apiData = P_InfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .target_mem_no(MemberVo.getUserInfo().getMem_no())
                .build();

        String result = memberService.getMemberInfo(apiData);

        return result;
    }

    @ApiOperation(value = "프로필편집")
    @PostMapping("profile")
    public String profile(HttpServletRequest request) throws GlobalException {

       return "프로필편집";
    }

    @ApiOperation(value = "팬 등록")
    @PostMapping("pan")
    public String pan(HttpServletRequest request) throws GlobalException {

        return "팬 등록";
    }


}
