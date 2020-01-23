package com.dalbit.broadcast.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDao {
        ProcedureVo callBroadCastRoomNoticeSelect(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeEdit(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeDelete(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomStoryAdd(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomStoryList(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomStoryDelete(ProcedureVo procedureVo);


}
