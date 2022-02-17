package com.dalbit.event.service;


import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.InviteEvent;
import com.dalbit.event.vo.InviteVo;
import com.dalbit.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteEvent event;
    private final GsonUtil gsonUtil;

    public String pEvtInvitationMemberIns(String memNo, String invitationCode) {
        Integer result = event.pEvtInvitationMemberIns(memNo, invitationCode);

        if(result == 1){
            return gsonUtil.toJson(new JsonOutputVo(Status.추천코드_등록, result));
        }else if(result == -1){
            return gsonUtil.toJson(new JsonOutputVo(Status.추천코드_중복, result));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, result));
        }
    }

    public String pEvtInvitationRcvMemberChk(String memNo, Integer memPhone) {
//        event.pEvtInvitationRcvMemberChk(memNo, memPhone);
        return "";
    }

    public String pEvtInvitationRewardIns(Integer sendMemNo, Integer rcvMemNo, Integer invitationCode) {
//        event.pEvtInvitationRewardIns(sendMemNo, rcvMemNo, invitationCode);
        return "";
    }

    public String pEvtInvitationMemberSel(String memNo) {
        InviteVo inviteVo =  event.pEvtInvitationMemberSel(memNo);
        if(inviteVo!=null){
            return gsonUtil.toJson(new JsonOutputVo(Status.조회, inviteVo));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }
    }

    public String pEvtInvitationMemberRankList(Integer pageNo, Integer pagePerCnt) {
        event.pEvtInvitationMemberRankList(pageNo, pagePerCnt);
        return "";
    }

    public String pEvtInvitationMemberRankMySel(String memNo) {
        event.pEvtInvitationMemberRankMySel(memNo);
        return "";
    }

    public String pEvtInvitationRcvMemberList(String memNo, Integer pageNo, Integer pagePerCnt) {
//        List<Object> object = event.pEvtInvitationRcvMemberList(memNo, pageNo, pagePerCnt);
//        return gsonUtil.toJson(new JsonOutputVo(Status.조회, object));

        return "";
    }
}
