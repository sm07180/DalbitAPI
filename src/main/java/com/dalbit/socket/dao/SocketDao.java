package com.dalbit.socket.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.socket.vo.SocketOutVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SocketDao {
    @Transactional(readOnly = true)
    ProcedureVo callBroadcastMemberInfo(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    SocketOutVo callBroadcastMemberInfoObject(ProcedureVo procedureVo);
}
