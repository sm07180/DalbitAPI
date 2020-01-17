package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.P_InfoVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
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

    @ApiOperation(value = "회원 팬보드 등록하기")
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
