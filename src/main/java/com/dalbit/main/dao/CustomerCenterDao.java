package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CustomerCenterDao {

    @Transactional(readOnly = true)
    List<P_NoticeListVo> callNoticeList(ProcedureVo procedureVo);

    ProcedureVo callNoticeDetail(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_FaqListVo> callFaqList(ProcedureVo procedureVo);

    ProcedureVo callFaqDetail(ProcedureVo procedureVo);

    ProcedureVo callQnaAdd(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_QnaListVo> callQnaList(ProcedureVo procedureVo);

    ProcedureVo callQnaDel(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    HashMap selectAppVersion();
}
