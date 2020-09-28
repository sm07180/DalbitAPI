package com.dalbit.clip.dao;

import com.dalbit.clip.vo.procedure.*;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ClipDao {
    ProcedureVo callClipAdd(ProcedureVo procedureVo);

    ProcedureVo callClipEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipListVo> callClipList(ProcedureVo procedureVo);

    ProcedureVo callClipPlay(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipPlayListVo> callClipPlayList(ProcedureVo procedureVo);

    ProcedureVo callClipGift(ProcedureVo procedureVo);

    ProcedureVo callClipGood(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipGiftRankTop3Vo> callClipGiftRankTop3List(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipGiftRankListVo> callClipGiftRankList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipGiftListVo> callClipGiftList(ProcedureVo procedureVo);

    ProcedureVo callClipDelete(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipReplyListVo> callClipReplyList(ProcedureVo procedureVo);

    ProcedureVo callClipReplyAdd(ProcedureVo procedureVo);

    ProcedureVo callClipReplyEdit(ProcedureVo procedureVo);

    ProcedureVo callClipReplyDelete(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipUploadListVo> callClipUploadList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipListenListVo> callClipListenList(ProcedureVo procedureVo);

    ProcedureVo callClipPlayListEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipMainPopListVo> callClipMainPopList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipMainLatestListVo> callClipMainLatestList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ClipMainSubjectTop3ListVo> callClipMainSubjectTop3List(ProcedureVo procedureVo);

    ProcedureVo callClipDeclar(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    HashMap callClipShare(String cast_no);

    @Transactional(readOnly = true)
    ProcedureVo callMyClip(ProcedureVo procedureVo);
}
