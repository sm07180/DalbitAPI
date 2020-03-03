package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_RoomStoryListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentDao {
        ProcedureVo callBroadCastRoomNoticeSelect(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeEdit(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeDelete(ProcedureVo procedureVo);
        ProcedureVo callInsertStory(ProcedureVo procedureVo);
        List<P_RoomStoryListVo> callGetStory(ProcedureVo procedureVo);
        ProcedureVo callDeletetStory(ProcedureVo procedureVo);
}
