package com.demo.sample.dao;

import com.demo.common.vo.ProcedureVo;
import com.demo.common.vo.SampleVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SampleDao {
    @Autowired
    private SqlSessionTemplate sqlSession;

    private String statementPrefix = "sample.";

    public int getCount(){
        return this.sqlSession.selectOne(statementPrefix + "getCount");
    }

    public List<SampleVo> getList(){
        return this.sqlSession.selectList(statementPrefix + "getList");
    }

    public int insertSample(SampleVo sampleVo){
        return this.sqlSession.insert(statementPrefix + "insertSample", sampleVo);
    }

    public ProcedureVo getNickNameCheck(ProcedureVo procedureVo) {
        return this.sqlSession.selectOne(statementPrefix + "getNickNameCheck", procedureVo);
    }

    public List<SampleVo> selectLogData() {
        return this.sqlSession.selectList(statementPrefix + "selectLogData");
    }

}
