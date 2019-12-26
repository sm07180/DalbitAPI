package com.demo.service;

import com.demo.dao.SampleDao;
import com.demo.vo.SampleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class SampleService {

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
}
