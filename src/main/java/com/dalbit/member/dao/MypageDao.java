package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);
}
