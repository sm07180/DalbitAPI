package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.procedure.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callBroadBasic(ProcedureVo procedureVo);
    ProcedureVo callBroadBasicEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberReportAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockDel(ProcedureVo procedureVo);
    ProcedureVo callMemberNotify(ProcedureVo procedureVo);
    ProcedureVo callMemberNotifyEdit(ProcedureVo procedureVo);
    List<P_MemberShortCutVo> callMemberShortCut(ProcedureVo procedureVo);
    ProcedureVo callMemberShortCutEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberGiftRuby(ProcedureVo procedureVo);
    List<P_NotificationVo> callMemberNotification(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeDel(ProcedureVo procedureVo);
    List<P_MypageNoticeSelectVo> callMypageNoticeSelect(ProcedureVo procedureVo);
    List<P_DalVo> callMemberWalletDal(ProcedureVo procedureVo);
    List<P_ByeolVo> callMemberWalletByeol(ProcedureVo procedureVo);
    List<P_MypageReportBroadVo> callMypageMypageReportBroad(ProcedureVo procedureVo);
    List<P_MypageReportListenVo> callMypageMypageReportListen(ProcedureVo procedureVo);
    ProcedureVo callMyapgeGetBanWord(ProcedureVo procedureVo);
    ProcedureVo callMypageInsertBanWord(ProcedureVo procedureVo);
    List<P_SearchUserVo> callMypageSearchUser(ProcedureVo procedureVo);
    List<P_MypageManagerVo> callMypageManager(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerDel(ProcedureVo procedureVo);
    List<P_MypageBlackVo> callMypageBlackListView(ProcedureVo procedureVo);
    ProcedureVo callMypageBlackListAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageBlackListDel(ProcedureVo procedureVo);


}
