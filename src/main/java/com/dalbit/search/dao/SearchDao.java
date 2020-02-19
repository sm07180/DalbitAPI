package com.dalbit.search.dao;


import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.search.vo.procedure.P_MemberSearchVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchDao {

    List<P_MemberSearchVo> callMemberNickSearch(ProcedureVo procedureVo);

}
