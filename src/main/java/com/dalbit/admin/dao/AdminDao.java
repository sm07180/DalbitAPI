package com.dalbit.admin.dao;

import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.MessageInsertVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AdminDao {

    @Transactional(readOnly = true)
    ArrayList<AdminMenuVo> selectMobileAdminMenuAuth(SearchVo searchVo);

    @Transactional(readOnly = true)
    ArrayList<BroadcastVo> selectBroadcastList(BroadcastVo broadcastVo);

    BroadcastDetailVo selectBroadcastSimpleInfo(SearchVo searchVo);

    ProcedureVo callBroadcastRoomExit(ProcedureVo procedureVo);

    int updateBroadcastMemberExit(P_RoomForceExitInputVo pRoomForceExitInputVo);

    int updateBroadcastExit(BroadcastExitVo broadcastExitVo);

    @Transactional(readOnly = true)
    ArrayList<ProfileVo> selectProfileList(ProfileVo profileVo);

    int proImageInit(ProImageInitVo proImageInitVo);

    int insertProfileHistory(ProImageInitVo proImageInitVo);

    int broImageInit(BroImageInitVo broImageInitVo);

    int insertBroadHistory(BroImageInitVo broImageInitVo);

    int nickTextInit(NickTextInitVo nickTextInitVo);

    int broTitleTextInit(BroTitleTextInitVo broTitleTextInitVo);

    int insertNotiHistory(NotiInsertVo notiInsertVo);

    // 신고 처리
    int declarationOperate(DeclarationVo declarationVo);

    MemberInfoVo getMemberInfo(String mem_no);

    BroadInfoVo getBroadInfo(String room_no);

    ArrayList<LiveChatOutputVo> selectBroadcastLiveChatInfo(LiveChatInputVo liveChatInputVo);

    int updateState(DeclarationVo declarationVo);

    LiveChatProfileVo getLiveChatProfile(LiveChatProfileVo liveChatProfileVo);

    ProcedureVo callForceLeave(ProcedureVo procedureVo);

    int insertForceLeave_roomBlock(ForcedOutVo forcedOutVo);

    int insertContentsMessageAdd(MessageInsertVo messageInsertVo);

    ArrayList<ProfileVo> selectLiveListener(ProfileVo profileVo);

    // 통계
    @Transactional(readOnly = true)
    ArrayList<P_BroadcastTotalOutDetailVo> callBroadcastTotal(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_UserTotalOutDetailVo> callUserCurrentTotal(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_PayInfoOutVo> callPayInfo(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_LoginTotalOutDetailVo> callLoginTotal(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_LoginAgeOutDetailVo> callLoginAge(ProcedureVo procedureVo);

    // 1:1 문의
    @Transactional(readOnly = true)
    ArrayList<P_QuestionListOutputVo> callQuestionList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    P_QuestionDetailOutputVo callServiceCenterQnaDetail(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<FaqVo> selectFaqSubList(FaqVo faqVo);

    @Transactional(readOnly = true)
    P_QuestionDetailOutputVo selectServiceCenterQnaState(P_QuestionOperateVo pQuestionOperateVo);

    @Transactional(readOnly = true)
    ProcedureVo callServiceCenterQnaOperate(ProcedureVo procedureVo);

    int updateServiceCenterQnaUpdate(P_QuestionOperateVo pQuestionOperateVo);

    // 신고 내역
    @Transactional(readOnly = true)
    ArrayList<P_DeclarationListOutputVo> callServiceCenterReportList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ArrayList<P_DeclarationDetailOutputVo> callServiceCenterReportDetail(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    LiveChatProfileVo selectUserProfile(LiveChatProfileVo liveChatProfileVo);

    @Transactional(readOnly = true)
    ArrayList<P_MemberEditHistOutputVo> callMemberEditHistory(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    String selectAdminName(String mem_no);

    int insertBlock(DeclarationVo declarationVo);

    int insertBlockHistory(DeclarationVo declarationVo);
}
