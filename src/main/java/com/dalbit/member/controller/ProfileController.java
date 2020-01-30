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

        P_ProfileInfoVo apiData = new P_ProfileInfoVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setTarget_mem_no(DalbitUtil.convertRequestParamToString(request,"memNo"));
        String result = profileService.callMemberInfo(apiData);
        return result;
    }

    /**
     * 회원 팬보드 등록하기
     */
    @PostMapping("/board")
    public String fanboardAdd(HttpServletRequest request){

        P_FanboardAddVo fanboardAddVo = new P_FanboardAddVo();
        fanboardAddVo.setStar_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        fanboardAddVo.setWriter_mem_no(DalbitUtil.convertRequestParamToString(request, "writerNo"));
        fanboardAddVo.setDepth(DalbitUtil.convertRequestParamToInteger(request, "depth"));
        fanboardAddVo.setBoard_no(DalbitUtil.convertRequestParamToInteger(request, "boardNo"));
        fanboardAddVo.setContents(DalbitUtil.convertRequestParamToString(request, "content"));

        String result = profileService.callMemberFanboardAdd(fanboardAddVo);

        return result;
    }

    /**
     * 회원 팬보드 목록조회
     */
    @GetMapping("/board")
    public String fanboardList(HttpServletRequest request){

        P_FanboardListVo fanboardListVo = new P_FanboardListVo();
        fanboardListVo.setMem_no(MemberVo.getMyMemNo());
        fanboardListVo.setStar_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        fanboardListVo.setPageNo(DalbitUtil.convertRequestParamToInteger(request, "page"));
        fanboardListVo.setPageCnt(DalbitUtil.convertRequestParamToInteger(request, "records"));

        String result = profileService.callMemberFanboardList(fanboardListVo);

        return result;
    }

    /**
     * 회원 팬보드 삭제하기
     */
    @DeleteMapping("/board")
    public String fanboardDelete(HttpServletRequest request){

        P_FanboardDeleteVo fanboardDeleteVo = new P_FanboardDeleteVo();
        fanboardDeleteVo.setStar_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        fanboardDeleteVo.setDelete_mem_no(MemberVo.getMyMemNo());  // 물어보기
        fanboardDeleteVo.setBoard_idx(DalbitUtil.convertRequestParamToInteger(request, "boardIdx"));

        String result = profileService.callMemberFanboardDelete(fanboardDeleteVo);
        return result;
    }

    /**
     * 회원 팬보드 대댓글 조회하기
     */
    @GetMapping("/board/reply")
    public String fanboardReply(HttpServletRequest request){

        P_FanboardReplyVo fanboardReplyVo = new P_FanboardReplyVo();
        fanboardReplyVo.setMem_no(MemberVo.getMyMemNo());
        fanboardReplyVo.setStar_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        fanboardReplyVo.setBoard_no(DalbitUtil.convertRequestParamToInteger(request, "boardNo"));

        String result = profileService.callMemberFanboardReply(fanboardReplyVo);
        return result;

    }


}
