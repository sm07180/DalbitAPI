package com.demo.member.service;

import com.demo.common.vo.ProcedureVo;
import com.demo.member.vo.LoginVo;
import com.demo.sample.dao.SampleDao;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class MemberService {

    @Autowired
    SampleDao sampleDao;

    public ProcedureVo callMemberLogin(LoginVo loginVo) {
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setData(new Gson().toJson(loginVo));
        sampleDao.callMemberLogin(procedureVo);
        return procedureVo;
    }
}
