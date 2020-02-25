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
}
