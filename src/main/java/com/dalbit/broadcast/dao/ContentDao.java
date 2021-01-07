package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_RoomStoryListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContentDao {
        @Transactional(readOnly = true)
        ProcedureVo callBroadCastRoomNoticeSelect(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeEdit(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeDelete(ProcedureVo procedureVo);
        ProcedureVo callInsertStory(ProcedureVo procedureVo);
        @Transactional(readOnly = true)
        List<P_RoomStoryListVo> callGetStory(ProcedureVo procedureVo);
        ProcedureVo callDeletetStory(ProcedureVo procedureVo);
}
