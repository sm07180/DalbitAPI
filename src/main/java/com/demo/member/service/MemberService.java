package com.demo.member.service;

import com.demo.common.vo.ProcedureVo;
import com.demo.member.vo.JoinVo;
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

    public ProcedureVo callMemberJoin(JoinVo joinVo) {
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setData(new Gson().toJson(joinVo));
        sampleDao.callMemberJoin(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callNickNameCheck(ProcedureVo procedureVo) {
        procedureVo.setData(new Gson().toJson(procedureVo));
        sampleDao.callNickNameCheck(procedureVo);
        return procedureVo;
    }


}
