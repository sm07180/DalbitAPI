package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, Object> selectAppVersion();

    /**
     * 서비스 공지 읽음 표시
     *
     * @Param
     * memNo            BIGINT          -- 회원번호
     * notiNo           BIGINT          -- 팬공지 번호
     *
     * @Return
     * s_return         INT             -- -1: 읽음, 0: 에러, 1: 정상
     */
    @Select("CALL p_service_center_notice_read_upd(#{memNo}, #{notiNo})")
    Integer callNoticeReadUpd(Map<String, Object> param);

}
