package com.demo.member.controller;

import com.demo.exception.GlobalException;
import com.demo.member.service.MemberService;
import com.demo.member.vo.MemberVo;
import com.demo.member.vo.P_InfoVo;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String profile() throws GlobalException {

       return "프로필편집";
    }
}
