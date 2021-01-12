package com.dalbit.admin.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface AdminMemberDao {
    @Transactional(readOnly = true)
    ArrayList<HashMap> callMemberDetail(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ArrayList<HashMap> callBroadcastRoomList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ArrayList<HashMap> callClipList(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ArrayList<HashMap> callQuestionList(ProcedureVo procedureVo);

}
