package com.demo.sample.dao;

import com.demo.common.vo.ProcedureVo;
import com.demo.common.vo.SampleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleDao {

    int getCount();
    List<SampleVo> getList();
    int insertSample(SampleVo sampleVo);
    ProcedureVo callNickNameCheck(ProcedureVo procedureVo);
    ProcedureVo callMemberLogin(ProcedureVo procedureVo);
    ProcedureVo callMemberJoin(ProcedureVo procedureVo);
    List<SampleVo> selectLogData();


}
