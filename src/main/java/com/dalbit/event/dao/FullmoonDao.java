package com.dalbit.event.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;


@Repository
public interface FullmoonDao {

    @Transactional(readOnly = true)
    HashMap callFullmoonEventInfo(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ArrayList<HashMap> callFullmoonEventRanking(ProcedureVo procedureVo);
}
