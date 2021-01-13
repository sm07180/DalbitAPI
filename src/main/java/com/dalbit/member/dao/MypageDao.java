package com.dalbit.member.dao;

import com.dalbit.admin.vo.MemberInfoVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);

    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    //@Transactional(readOnly = true)
    ProcedureVo callBroadBasic(ProcedureVo procedureVo);

    ProcedureVo callBroadBasicEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberReportAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockDel(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    ProcedureVo callMemberNotify(ProcedureVo procedureVo);

    ProcedureVo callMemberNotifyEdit(ProcedureVo procedureVo);

    List<P_MemberShortCutVo> callMemberShortCut(ProcedureVo procedureVo);

    ProcedureVo callMemberShortCutEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberGiftRuby(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_NotificationVo> callMemberNotification(ProcedureVo procedureVo);

    ProcedureVo callMypageNoticeAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeDel(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MypageNoticeSelectVo> callMypageNoticeSelect(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyAdd(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyDelete(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyEdit(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MypageNoticeReplyListVo> callMyPageNoticeReplySelect(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MypageReportBroadVo> callMypageMypageReportBroad(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MypageReportListenVo> callMypageMypageReportListen(ProcedureVo procedureVo);

    ProcedureVo callMyapgeGetBanWord(ProcedureVo procedureVo);

    ProcedureVo callMypageInsertBanWord(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_SearchUserVo> callMypageSearchUser(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MypageManagerVo> callMypageManager(ProcedureVo procedureVo);

    ProcedureVo callMypageManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerDel(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_MypageBlackVo> callMypageBlackListView(ProcedureVo procedureVo);

    ProcedureVo callMypageBlackListAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageBlackListDel(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_ChangeItemListVo> changeItemSelect(ProcedureVo procedureVo);

    ProcedureVo changeItem(ProcedureVo procedureVo);

    ProcedureVo callAutoChangeSettingEdit(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    ProcedureVo callAutoChangeSettingSelect(ProcedureVo procedureVo);

    ProcedureVo callReadNotification(ProcedureVo procedureVo);

    void callReadALLNotification(P_NotificationVo pNotificationVo);

    //@Transactional(readOnly = true)
    long selectMyByeolCnt(String mem_no);

    //@Transactional(readOnly = true)
    int selectExistsSpecialReq(String mem_no);

    //@Transactional(readOnly = true)
    int selectExistsPhoneSpecialReq(String mem_no);

    void insertSpecialReq(P_SpecialDjReq pSpecialDjReq);

    //@Transactional(readOnly = true)
    long selectSpecialDjBroadcastTime(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    //@Transactional(readOnly = true)
    int selectSpecialDjLikeCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    //@Transactional(readOnly = true)
    int selectSpecialDjBroadcastCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    //@Transactional(readOnly = true)
    SpecialDjRegManageVo selectSpecialDjReqManage(SpecialDjRegManageVo specialDjRegManageVo);

    //@Transactional(readOnly = true)
    List<SpecialDjContentVo> selectSpecialDjReqContent(SpecialDjRegManageVo specialDjRegManageVo);

    //@Transactional(readOnly = true)
    MemberInfoVo selectMemberLevel(String mem_no);

    //@Transactional(readOnly = true)
    int selectMemberFanCnt(String mem_no);

    //@Transactional(readOnly = true)
    int selectListenerCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    //@Transactional(readOnly = true)
    List<LevelVo> selectLevel();

    @Transactional(readOnly = true)
    int callNewAlarm(P_MemberNotifyVo pMemberNotifyVo);

    ProcedureVo memberShortCutAdd(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    ProcedureVo memberShortCutExtend(ProcedureVo procedureVo);

    ProcedureVo msgClickUpdate(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    int selectAdminBadge(String value);

    //@Transactional(readOnly = true)
    List<P_EmoticonListVo> callMemberEmoticon(ProcedureVo procedureVo);

    ProcedureVo callMemberNotificationDelete(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<EmoticonCategoryListVo> selectEmoticonCategory();

    //@Transactional(readOnly = true)
    HashMap selectMyPageNew(HashMap params);

    //@Transactional(readOnly = true)
    Long selectMyPageFanBoard(String mem_no);

    //@Transactional(readOnly = true)
    HashMap selectMyPageWallet(String mem_no);

    ProcedureVo callBroadcastTitleAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadcastTitleEdit(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_BroadcastOptionListVo> callBroadcastTitleSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastTitleDelete(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgEdit(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_BroadcastOptionListVo> callBroadcastWelcomeMsgSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgDelete(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_GoodListVo> callMemberGoodList(ProcedureVo procedureVo);

    ProcedureVo callBroadcastSettingSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastSettingEdit(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    HashMap callMemberBoardCount(HashMap params);

    //@Transactional(readOnly = true)
    ProcedureVo callWalletPopupListView(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_StoryVo> callMemberBoardStory(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_WalletListVo> callWalletList(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    Integer selectIsSpecial(String mem_no);

    ProcedureVo callExchangeCancel(ProcedureVo procedureVo);
}
