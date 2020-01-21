package com.dalbit.member.controller;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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


    /**
     * 정보 조회
     */
    @GetMapping("")
    public String memberInfo(HttpServletRequest request){

        P_ProfileInfo apiData = P_ProfileInfo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .target_mem_no(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = profileService.callMemberInfo(apiData);
        return result;
    }

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

        P_FanboardListVo fanboardListVo = new P_FanboardListVo();
        fanboardListVo.setMem_no(MemberVo.getUserInfo().getMem_no());
        fanboardListVo.setStar_mem_no(DalbitUtil.convertRequestParamToString(request, "s_startNo"));
        fanboardListVo.setPageNo(DalbitUtil.convertRequestParamToInteger(request, "i_page"));
        fanboardListVo.setPageCnt(DalbitUtil.convertRequestParamToInteger(request, "i_records"));

        String result = profileService.callMemberFanboardList(fanboardListVo);

        return result;
    }

    /**
     * 회원 팬보드 삭제하기
     */
    @DeleteMapping("/board")
    public String fanboardDelete(HttpServletRequest request){

        P_FanboardDeleteVo fanboardDeleteVo = P_FanboardDeleteVo.builder()
                .star_mem_no(DalbitUtil.convertRequestParamToString(request, "s_startNo"))
                .delete_mem_no(MemberVo.getUserInfo().getMem_no())
                .board_idx(DalbitUtil.convertRequestParamToInteger(request,"i_board"))
                .build();
        String result = profileService.callMemberFanboardDelete(fanboardDeleteVo);
        return result;
    }

    /**
     * 회원 팬보드 대댓글 조회하기
     */
    @GetMapping("/board/reply")
    public String fanboardReply(HttpServletRequest request){

        P_FanboardReplyVo apiData = P_FanboardReplyVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .star_mem_no(DalbitUtil.convertRequestParamToString(request, "s_startNo"))
                .board_no(DalbitUtil.convertRequestParamToInteger(request, "i_board"))
                .build();
        String result = profileService.callMemberFanboardReply(apiData);
        return result;

    }


}
