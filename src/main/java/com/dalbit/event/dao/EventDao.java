package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.vo.*;
import com.dalbit.event.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface EventDao {
    @Transactional(readOnly = true)
    List<P_RankingLiveOutputVo> callEventRankingLive(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_RankingResultOutputVo> callEventRankingResult(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ReplyListOutputVo> callEventReplyList(P_ReplyListInputVo pRankingLiveInputVo);


    int callEventReplyAdd(P_ReplyAddInputVo pReplyAddInputVo);
    int callEventReplyDelete(P_ReplyDeleteInputVo pReplyDeleteInputVo);

    @Transactional(readOnly = true)
    int callEventAuthCheck(P_ReplyDeleteInputVo pReplyDeleteInputVo);

    @Transactional(readOnly = true)
    ArrayList<P_AttendanceCheckLoadOutputVo> callAttendanceCheckLoad(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ArrayList<P_AttendanceCheckLoadOutputVo> callAttendanceCheckGift(ProcedureVo procedureVo);
    ArrayList<P_AttendanceCheckLoadOutputVo> callAttendanceCheckBonus(ProcedureVo procedureVo);


    @Transactional(readOnly = true)
    ArrayList<P_RisingEventListOutputVo> callRisingLive(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ArrayList<P_RisingEventListOutputVo> callRisingResult(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo callAttendanceCheck(ProcedureVo procedureVo);

    ProcedureVo callPhoneInput(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ArrayList<P_GifticonWinListOutputVo> callGifticonWinList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    String selectLunarDate();

    @Transactional(readOnly = true)
    List<PhotoEventOutputVo> selectPhotoList(PhotoEventInputVo photoEventInputVo);

    @Transactional(readOnly = true)
    int selectPhotoCnt(PhotoEventInputVo photoEventInputVo);

    int insertEventMember(EventMemberVo eventMemberVo);

    int callEventApply(ProcedureVo procedureVo);

    int insertPhoto(PhotoEventInputVo photoEventInputVo);

    int updatePhoto(PhotoEventInputVo photoEventInputVo);

    int deleteEventMemberPhoto(PhotoEventInputVo photoEventInputVo);

    int deletePhoto(PhotoEventInputVo photoEventInputVo);

    @Transactional(readOnly = true)
    int selectPhotoPcAirTime(PhotoEventInputVo photoEventInputVo);

    @Transactional(readOnly = true)
    EventBasicVo selectEventBasic(int idx);

    @Transactional(readOnly = true)
    List<KnowhowEventOutputVo> selectKnowhowList(KnowhowEventInputVo knowhowEventInputVo);

    @Transactional(readOnly = true)
    int selectKnowhowCnt(KnowhowEventInputVo knowhowEventInputVo);

    @Transactional(readOnly = true)
    KnowhowEventOutputVo selectKnowhowDetail(KnowhowEventInputVo knowhowEventInputVo);

    int updatePhotoViewCnt(int event_idx);

    int insertKnowhow(KnowhowEventInputVo knowhowEventInputVo);

    int updateKnowhow(KnowhowEventInputVo knowhowEventInputVo);

    ProcedureVo callEventGood(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callEventApplyCheck(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callEventApplyCheck004(ProcedureVo procedureVo);
    ProcedureVo callEventApplySP(ProcedureVo procedureVo);
    ProcedureVo callEventApply003(ProcedureVo procedureVo);
    ProcedureVo callEventDetail003(ProcedureVo procedureVo);
    ProcedureVo callEventApply004(ProcedureVo procedureVo);
}
