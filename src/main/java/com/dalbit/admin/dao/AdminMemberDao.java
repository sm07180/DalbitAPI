package com.dalbit.admin.dao;

import com.dalbit.admin.vo.MemberInfoVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface AdminMemberDao {
    // @Transactional(readOnly = true)
    MemberInfoVo callMemberDetail(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ArrayList<HashMap> callBroadcastRoomList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ArrayList<HashMap> callClipList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ArrayList<HashMap> callQuestionList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ArrayList<HashMap> callImageList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ArrayList<HashMap> callWalletList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    HashMap callCheckBraodcast(ProcedureVo procedureVo);
}
