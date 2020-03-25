package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerCenterDao {

    @Transactional(readOnly = true)
    List<P_NoticeListVo> callNoticeList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callNoticeDetail(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_FaqListVo> callFaqList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callFaqDetail(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callQnaAdd(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    List<P_QnaListVo> callQnaList(ProcedureVo procedureVo);
}
