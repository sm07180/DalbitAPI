package com.dalbit.rank.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface RankDao {

    ProcedureVo callRankSetting(ProcedureVo procedureVo);
}
