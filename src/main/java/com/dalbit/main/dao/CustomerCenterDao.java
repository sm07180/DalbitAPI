package com.dalbit.main.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_NoticeListVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCenterDao {

    List<P_NoticeListVo> callNoticeList(ProcedureVo procedureVo);
    ProcedureVo callNoticeDetail(ProcedureVo procedureVo);
}
