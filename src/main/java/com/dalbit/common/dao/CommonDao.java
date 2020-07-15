package com.dalbit.common.dao;

import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ItemVo;
import com.dalbit.common.vo.procedure.P_SelfAuthVo;
import com.dalbit.common.vo.request.SmsVo;
import com.dalbit.member.vo.TokenCheckVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface CommonDao {
    @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<Map> callCodeDefineSelect();

    void requestSms(SmsVo smsVo);

    ProcedureVo callMemberCertification(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo getCertificationChk(ProcedureVo procedureVo);

    ProcedureVo saveErrorLog(ProcedureVo procedureVo);

    ProcedureVo callPushAdd(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<ItemVo> selectItemList(P_ItemVo item);

    @Transactional(readOnly = true)
    List<ItemVo> selectBooster(String itemCode);

    @Transactional(readOnly = true)
    ItemDetailVo selectItem(String item);

    @Transactional(readOnly = true)
    AppVersionVo selectAppVersion(Integer item);

    @Transactional(readOnly = true)
    BanWordVo banWordSelect();

    @Transactional(readOnly = true)
    BanWordVo broadcastBanWordSelect(BanWordVo banWordVo);

    @Transactional(readOnly = true)
    NowBroadcastVo selectNowBroadcast(String memNo);

    @Transactional(readOnly = true)
    CodeVo selectCodeDefine(CodeVo codeVo);

    int updateMemberCertification(P_SelfAuthVo pSelfAuthVo);

    List<FanBadgeVo> callMemberBadgeSelect(HashMap param);

    int updateMemberCertificationFile(P_SelfAuthVo pSelfAuthVo);

    ProcedureVo callPushClickUpdate(ProcedureVo procedureVo);

    AdultCheckVo getMembirth(String mem_no);
}
