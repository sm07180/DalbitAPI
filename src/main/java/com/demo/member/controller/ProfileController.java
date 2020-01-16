package com.demo.member.controller;

import com.demo.exception.GlobalException;
import com.demo.member.service.MemberService;
import com.demo.member.vo.MemberVo;
import com.demo.member.vo.P_InfoVo;
import com.demo.util.DalbitUtil;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("profile")
public class ProfileController {
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MemberService memberService;

    @ApiOperation(value = "프로필편집")
    @PostMapping("")
    public String profile(HttpServletRequest request) throws GlobalException {

        P_InfoVo apiData = P_InfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .target_mem_no(DalbitUtil.convertRequestParamToString(request, "s_mem_no"))
                .build();

        String result = memberService.getMemberInfo(apiData);

        return result;
    }
}
