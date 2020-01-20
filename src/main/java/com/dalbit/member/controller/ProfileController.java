package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    ProfileService profileService;
    @Autowired
    MemberService memberService;

//    @ApiOperation(value = "회원 팬보드 등록하기")
//    @PostMapping("/board")
//    public String profile(HttpServletRequest request) throws GlobalException {
//
//        P_InfoVo apiData = P_InfoVo.builder()
//                .mem_no(MemberVo.getUserInfo().getMem_no())
//                .target_mem_no(DalbitUtil.convertRequestParamToString(request, "s_mem_no"))
//                .build();
//
//        String result = memberService.getMemberInfo(apiData);
//
//        return result;
//    }

    /**
     * 정보 조회
     */
/*    @ApiOperation(value = "정보 조회")
    @GetMapping("/{조회회원번호}")
    public String information(@PathVariable String 조회회원번호){


        return "정보 조회";
    }*/

    /**
     * 회원 팬보드 등록하기
     */
    @PostMapping("/board")
    public String fanboardAdd(HttpServletRequest request){

        P_FanboardAddVo fanboardAddVo = P_FanboardAddVo.builder()
            .star_mem_no(DalbitUtil.convertRequestParamToString(request,"s_startNo"))
            .writer_mem_no(MemberVo.getUserInfo().getMem_no())
            .depth(DalbitUtil.convertRequestParamToInteger(request,"i_depth"))
            .board_no(DalbitUtil.convertRequestParamToInteger(request,"i_board"))
            .contents(DalbitUtil.convertRequestParamToString(request,"s_contents"))
            .build();

        String result = profileService.callMemberFanboardAdd(fanboardAddVo);
        return result;
    }

    /**
     * 회원 팬보드 목록조회
     */
    @GetMapping("/board")
    public String fanboardList(HttpServletRequest request){

        return "회원 팬보드 목록조회";
    }

    /**
     * 회원 팬보드 삭제하기
     */
    @DeleteMapping("/board")
    public String fanboardDelete(HttpServletRequest request){

        return "회원 팬보드 삭제하기";
    }

    /**
     * 회원 팬보드 대댓글 조회하기
     */
    @GetMapping("/board/reply")
    public String fanboardReply(HttpServletRequest request){

        return "회원 팬보드 대댓글 조회하기";
    }


}
