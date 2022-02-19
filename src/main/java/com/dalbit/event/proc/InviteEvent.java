package com.dalbit.event.proc;

import com.dalbit.event.vo.InviteVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface InviteEvent {

    //추천코드 및 회원 등록
    @Select("CALL rd_data.p_evt_invitation_member_ins(#{memNo}, #{invitationCode}, #{memPhone})")
    int pEvtInvitationMemberIns(String memNo, String invitationCode, String memPhone);

    //추천받은 회원 체크
    @Select("CALL rd_data.p_evt_invitation_rcv_member_chk(#{memNo}, #{memPhone})")
    int pEvtInvitationRcvMemberChk(String memNo, String memPhone);

    //추천코드 보상 지급
    @Select("CALL rd_data.p_evt_invitation_reward_ins(#{rcvMemNo}, #{rcvMemIp}, #{invitationCode})")
    int pEvtInvitationRewardIns(String rcvMemNo, String rcvMemIp, String invitationCode);

    //초대페이지 회원 정보
    @Select("CALL rd_data.p_evt_invitation_member_sel(#{memNo})")
    InviteVo pEvtInvitationMemberSel(String memNo);

    //초대왕 현황 리스트
    @ResultMap({"ResultMap.integer", "ResultMap.InviteVo"})
    @Select("CALL rd_data.p_evt_invitation_member_rank_list(#{pageNo}, #{pagePerCnt})")
    List<Object> pEvtInvitationMemberRankList(Integer pageNo, Integer pagePerCnt);

    //초대왕 현황 내 정보
    @Select("CALL rd_data.p_evt_invitation_member_rank_my_sel(#{memNo})")
    InviteVo pEvtInvitationMemberRankMySel(String memNo);

    //나의 현황 리스트
    @ResultMap({"ResultMap.integer", "ResultMap.InviteVo"})
    @Select("CALL rd_data.p_evt_invitation_rcv_member_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> pEvtInvitationRcvMemberList(String memNo, Integer pageNo, Integer pagePerCnt);
}
