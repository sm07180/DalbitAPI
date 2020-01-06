package com.demo.sample.service;

import com.demo.common.vo.ProcedureVo;
import com.demo.common.vo.SampleVo;
import com.demo.sample.dao.SampleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
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
}
