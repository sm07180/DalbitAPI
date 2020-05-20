package com.dalbit.common.dao;

import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ItemVo;
import com.dalbit.common.vo.request.SmsVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    List<ItemVo> selectBooster();

    @Transactional(readOnly = true)
    ItemDetailVo selectItem(String item);

    @Transactional(readOnly = true)
    AppVersionVo selectAppVersion(Integer item);

    @Transactional(readOnly = true)
    BanWordVo banWordSelect();

    @Transactional(readOnly = true)
    BanWordVo broadcastBanWordSelect(BanWordVo banWordVo);

}
