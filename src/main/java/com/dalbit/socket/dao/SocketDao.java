package com.dalbit.socket.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SocketDao {
    //@Transactional(readOnly = true)
    ProcedureVo callBroadcastMemberInfo(ProcedureVo procedureVo);
}
