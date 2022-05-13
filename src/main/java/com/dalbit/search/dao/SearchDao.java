package com.dalbit.search.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.search.vo.procedure.P_LiveRoomSearchVo;
import com.dalbit.search.vo.procedure.P_MemberSearchVo;
import com.dalbit.search.vo.procedure.P_RoomRecommandListVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchDao {
    // @Transactional(readOnly = true)
    List<P_MemberSearchVo> callMemberNickSearch(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_LiveRoomSearchVo> callLiveRoomSearch(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_RoomRecommandListVo> callRoomRecommandList(ProcedureVo procedureVo);
}
