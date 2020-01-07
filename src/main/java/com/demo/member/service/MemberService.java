package com.demo.member.service;

import com.demo.common.vo.ProcedureVo;
import com.demo.member.dao.MemberDao;
import com.demo.member.vo.JoinVo;
import com.demo.member.vo.LoginVo;
import com.demo.sample.dao.SampleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class MemberService {

    @Autowired
    MemberDao memberDao;

    public ProcedureVo callMemberLogin(LoginVo loginVo) {
        ProcedureVo procedureVo = new ProcedureVo(loginVo);
        memberDao.callMemberLogin(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callMemberJoin(JoinVo joinVo) {
        ProcedureVo procedureVo = new ProcedureVo(joinVo);
        memberDao.callMemberJoin(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callNickNameCheck(ProcedureVo procedureVo) {
        memberDao.callNickNameCheck(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callChangePassword(ProcedureVo procedureVo){
        memberDao.callChangePassword(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callProfileEdit(ProcedureVo procedureVo){
        return memberDao.callProfileEdit(procedureVo);
    }
}
