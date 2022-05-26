package com.dalbit.common.dao;

import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ItemVo;
import com.dalbit.common.vo.procedure.P_KingFanRankListVo;
import com.dalbit.common.vo.request.SmsVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface CommonDao {
    // @Transactional(readOnly = true)
    ProcedureVo callBroadCastRoomStreamIdRequest(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<Map> callCodeDefineSelect();

    void requestSms(SmsVo smsVo);

    ProcedureVo callMemberCertification(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo getCertificationChk(ProcedureVo procedureVo);

    ProcedureVo callPushAdd(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<ItemVo> selectItemList(P_ItemVo item);

    // @Transactional(readOnly = true)
    List<ItemDetailVo> selectMulti(String itemCode);

    // @Transactional(readOnly = true)
    List<ItemVo> selectBooster(String itemCode);

    // @Transactional(readOnly = true)
    ItemDetailVo selectItem(String item);

    // @Transactional(readOnly = true)
    AppVersionVo selectAppVersion(Integer item);

    // @Transactional(readOnly = true)
    BanWordVo banWordSelect(int slctType);

    // @Transactional(readOnly = true)
    BanWordVo broadcastBanWordSelect(BanWordVo banWordVo);

    int updateMemberCertification(ProcedureVo pSelfAuthVo);

    // @Transactional(readOnly = true)
    List<FanBadgeVo> callMemberBadgeSelect(HashMap param);

    List<FanBadgeVo> callLiveBadgeSelect(HashMap param);

    // @Transactional(readOnly = true)
    List<FanBadgeVo> callMemberBadgeList(HashMap param);

    List<FanBadgeVo> callMemberBadgeListServer(HashMap param);

    // @Transactional(readOnly = true)
    BadgeFrameVo callMemberBadgeFrame(HashMap param);

    ProcedureVo callPushClickUpdate(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    AdultCheckVo getMembirth(String mem_no);

    List<P_KingFanRankListVo> callBroadCastRoomRank3(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo getLongTermDate(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<String> getDownloadList();

    // @Transactional(readOnly = true)
    List<String> getPreLoad();

    // @Transactional(readOnly = true)
    String getNationCode(String ip);
}
