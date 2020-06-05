package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.LevelVo;
import com.dalbit.member.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callBroadBasic(ProcedureVo procedureVo);
    ProcedureVo callBroadBasicEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberReportAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockDel(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    ProcedureVo callMemberNotify(ProcedureVo procedureVo);
    ProcedureVo callMemberNotifyEdit(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    List<P_MemberShortCutVo> callMemberShortCut(ProcedureVo procedureVo);
    ProcedureVo callMemberShortCutEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberGiftRuby(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_NotificationVo> callMemberNotification(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeDel(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MypageNoticeSelectVo> callMypageNoticeSelect(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_DalVo> callMemberWalletDal(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_ByeolVo> callMemberWalletByeol(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MypageReportBroadVo> callMypageMypageReportBroad(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MypageReportListenVo> callMypageMypageReportListen(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callMyapgeGetBanWord(ProcedureVo procedureVo);
    ProcedureVo callMypageInsertBanWord(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_SearchUserVo> callMypageSearchUser(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MypageManagerVo> callMypageManager(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerDel(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_MypageBlackVo> callMypageBlackListView(ProcedureVo procedureVo);
    ProcedureVo callMypageBlackListAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageBlackListDel(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_ChangeItemListVo> changeItemSelect(ProcedureVo procedureVo);
    ProcedureVo changeItem(ProcedureVo procedureVo);
    ProcedureVo callReadNotification(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    long selectMyByeolCnt(String mem_no);

    @Transactional(readOnly = true)
    int selectExistsSpecialReq(String mem_no);
    @Transactional(readOnly = true)
    int selectExistsPhoneSpecialReq(String mem_no);
    void insertSpecialReq(P_SpecialDjReq pSpecialDjReq);

    @Transactional(readOnly = true)
    List<LevelVo> selectLevel();
}
