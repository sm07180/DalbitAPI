package com.demo.sample.dao;

import com.demo.sample.vo.SampleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleDao {

    int getCount();
    List<SampleVo> getList();
    int insertSample(SampleVo sampleVo);
    List<SampleVo> selectLogData();


}
