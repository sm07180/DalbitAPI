package com.dalbit.admin.dao;

import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.common.vo.MessageInsertVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface AdminMemberDao {

    ArrayList<HashMap> callMemberDetail(ProcedureVo procedureVo);

    ArrayList<HashMap> callBroadcastRoomList(ProcedureVo procedureVo);

    ArrayList<HashMap> callClipList(ProcedureVo procedureVo);

    ArrayList<HashMap> callQuestionList(ProcedureVo procedureVo);

}
