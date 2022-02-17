package com.dalbit.event.controller;


import com.dalbit.event.service.InviteService;
import com.dalbit.member.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/event/invite")
@Slf4j
@RequiredArgsConstructor
public class InviteController {

    private final InviteService service;
    //추천코드 및 회원 등록
    @PostMapping("/register")
    public String pEvtInvitationMemberIns(@RequestBody HashMap<String, Object> param, HttpServletRequest request) {
        log.warn("{}", param);
        String memNo = MemberVo.getMyMemNo(request);
        String invitationCode = (String) param.get("invitationCode");
        String result = service.pEvtInvitationMemberIns(memNo, invitationCode);
        return result;
    }

    //추천받은 회원 체크
    @GetMapping("/check")
    public String pEvtInvitationRcvMemberChk(HttpServletRequest request, HashMap<String, Object> paramMap) {
        String memNo = MemberVo.getMyMemNo(request);
//        service.pEvtInvitationRcvMemberChk();
        return "";
    }

    //추천코드 보상 지급
    @PostMapping("/reward")
    public String pEvtInvitationRewardIns(HttpServletRequest request, HashMap<String, Object> paramMap) {
//        service.pEvtInvitationRewardIns();
        return "";
    }

    //초대페이지 회원 정보
    @PostMapping("/my")
    public String pEvtInvitationMemberSel(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        String result = service.pEvtInvitationMemberSel(memNo);
        return result;
    }

    //친구초대 회원 리스트
    @GetMapping("/list")
    public String pEvtInvitationMemberRankList(HttpServletRequest request, HashMap<String, Object> paramMap) {
        String memNo = MemberVo.getMyMemNo(request);
        service.pEvtInvitationMemberRankList(1, 999);
        return "";
    }

    //친구초대 내정보
    @GetMapping("/my-rank")
    public String pEvtInvitationMemberRankMySel(HttpServletRequest request, HashMap<String, Object> paramMap) {
        String memNo = MemberVo.getMyMemNo(request);
        return service.pEvtInvitationMemberRankMySel(memNo);
    }

    //초대한 회원 리스트
    @PostMapping("/my-list")
    public String pEvtInvitationRcvMemberList(HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        String result = service.pEvtInvitationRcvMemberList(memNo, 1, 999);
        return result;
    }
}
