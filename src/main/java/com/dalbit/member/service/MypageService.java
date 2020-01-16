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
@Transactional
public class MypageService {

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
     * 프로필 편집
     */
    public String callProfileEdit(P_ProfileEditVo pProfileEditVo){
        ProcedureVo procedureVo = new ProcedureVo(pProfileEditVo);
        memberDao.callProfileEdit(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.프로필편집성공.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집실패_닉네임중복)));
        } else{
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집실패_회원아님)));
        }
        return result;
    }

    /**
     * 팬가입
     */
    public String callFanstarInsert(P_FanstarInsertVo pFanstarInsertVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanstarInsertVo);
        memberDao.callFanstarInsert(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.팬등록성공.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.팬등록_회원아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록_회원아님)));
        } else if(procedureVo.getRet().equals(Status.팬등록_스타회원번호이상.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록_스타회원번호이상)));
        } else if(procedureVo.getRet().equals(Status.팬등록_이미팬등록됨.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록_이미팬등록됨)));
        } else{
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록실패)));
        }
        return result;
    }

    /**
     * 팬해제
     */
    public String callFanstarDelete(P_FanstarDeleteVo pFanstarDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanstarDeleteVo);
        memberDao.callFanstarDelete(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.팬해제성공.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.팬해제_회원아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_회원아님)));
        } else if(procedureVo.getRet().equals(Status.팬해제_스타회원번호이상.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_스타회원번호이상)));
        } else if(procedureVo.getRet().equals(Status.팬해제_팬아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_팬아님)));
        } else{
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제실패)));
        }
        return result;
    }
}
