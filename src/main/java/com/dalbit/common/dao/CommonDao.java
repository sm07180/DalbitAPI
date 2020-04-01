package com.dalbit.common.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.request.SmsVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommonDao {
    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);

    List<Map> callCodeDefineSelect();

    void requestSms(SmsVo smsVo);

    ProcedureVo callMemberCertification(ProcedureVo procedureVo);

    ProcedureVo getCertificationChk(ProcedureVo procedureVo);

    ProcedureVo saveErrorLog(ProcedureVo procedureVo);

    ProcedureVo callPushAdd(ProcedureVo procedureVo);
}
