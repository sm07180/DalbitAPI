package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
public class MemberService {

    @Autowired
    MemberDao memberDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;

    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    public ProcedureVo callMemberLogin(P_LoginVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberLogin(procedureVo);
        return procedureVo;
    }

    /**
     * 회원 가입
     */
    @Transactional
    public String signup(P_JoinVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberJoin(procedureVo);

        log.info("sp_member_join: {}", procedureVo.getRet());
        log.info("sp_member_join: {}", procedureVo.getExt());

        log.debug("회원가입 결과 : {}", procedureVo.toString());

        String result;
        if(Status.회원가입성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공));

        }else if (Status.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입, procedureVo.getData()));

        }else if (Status.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_닉네임중복, procedureVo.getData()));

        }else if (Status.회원가입실패_파라메터오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData()));

        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류, procedureVo.getData()));
        }

        return result;
    }

    /**
     * 닉네임 중복체크
     */
    public String callNickNameCheck(ProcedureVo procedureVo) {
        memberDao.callNickNameCheck(procedureVo);

        log.debug("닉네임중복체크 결과 : {}", procedureVo.toString());

        String result = "";
        if(Status.닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임중복, procedureVo.getData()));

        }else if(Status.닉네임사용가능.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임사용가능, procedureVo.getData()));
        }
        return result;
    }

    /**
     * 비밀번호 변경
     */
    public String callChangePassword(P_ChangePasswordVo pChangePasswordVo){
        ProcedureVo procedureVo = new ProcedureVo(pChangePasswordVo);
        memberDao.callChangePassword(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.비밀번호변경성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.비밀번호변경성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.비밀번호변경실패_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.비밀번호변경실패_회원아님)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.비밀번호변경오류)));
        }
        return result;
    }
}
