package com.demo.member.service;

import com.demo.common.vo.ProcedureVo;
import com.demo.member.dao.MemberDao;
import com.demo.member.vo.P_JoinVo;
import com.demo.member.vo.P_LoginVo;
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

    public ProcedureVo callMemberLogin(P_LoginVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberLogin(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callMemberJoin(P_JoinVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
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

    public ProcedureVo callFanstarInsert(ProcedureVo procedureVo) {
        return memberDao.callFanstarInsert(procedureVo);
    }

    public ProcedureVo callFanstarDelete(ProcedureVo procedureVo) {
        return memberDao.callFanstarDelete(procedureVo);
    }

    public ProcedureVo callMemberInfoView(ProcedureVo procedureVo) {
        return memberDao.callMemberInfoView(procedureVo);
    }
}
