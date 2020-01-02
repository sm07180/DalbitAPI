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
public class SampleService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("javainuse".equals(username)) {
            return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public ProcedureVo callNickNameCheck(ProcedureVo procedureVo) {
        return sampleDao.callNickNameCheck(procedureVo);
    }

    public ProcedureVo callMemberLogin(ProcedureVo procedureVo) {
        return sampleDao.callMemberLogin(procedureVo);
    }

    public List<SampleVo> selectLogData() {
        List<SampleVo> list = sampleDao.selectLogData();
        return list;
    }
}
