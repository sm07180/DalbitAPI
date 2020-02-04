package com.dalbit.sample.service;

import com.dalbit.sample.dao.SampleDao;
import com.dalbit.sample.vo.SampleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SampleService{

    @Autowired
    private SampleDao sampleDao;

    public int getCount(){
        int cnt = sampleDao.getCount();
        return cnt;
    }

    public List<SampleVo> getList(){
        List<SampleVo> list = sampleDao.getList();
        return list;
    }

    public void transactionTest(SampleVo sampleVo){

        sampleDao.insertSample(sampleVo);
        String[] arr = new String[1];
        log.error(arr[2]);

    }

    public List<SampleVo> selectLogData() {
        List<SampleVo> list = sampleDao.selectLogData();
        return list;
    }

    public List<SampleVo> selectMemId() {
        List<SampleVo> list = sampleDao.selectMemId();
        return list;
    }
}
