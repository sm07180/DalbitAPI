package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.*;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class MypageService {

    @Autowired
    MypageDao mypageDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;

    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     * 프로필 편집
     */
    public String callProfileEdit(P_ProfileEditVo pProfileEditVo){
        ProcedureVo procedureVo = new ProcedureVo(pProfileEditVo);
        mypageDao.callProfileEdit(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.프로필편집성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집실패_닉네임중복)));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집실패_회원아님)));
        }
        return result;
    }

    /**
     * 팬가입
     */
    public String callFanstarInsert(P_FanstarInsertVo pFanstarInsertVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanstarInsertVo);
        mypageDao.callFanstarInsert(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.팬등록성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.팬등록_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록_회원아님)));
        } else if(procedureVo.getRet().equals(Status.팬등록_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록_스타회원번호이상)));
        } else if(procedureVo.getRet().equals(Status.팬등록_이미팬등록됨.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록_이미팬등록됨)));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬등록실패)));
        }
        return result;
    }

    /**
     * 팬해제
     */
    public String callFanstarDelete(P_FanstarDeleteVo pFanstarDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanstarDeleteVo);
        mypageDao.callFanstarDelete(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.팬해제성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.팬해제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_회원아님)));
        } else if(procedureVo.getRet().equals(Status.팬해제_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_스타회원번호이상)));
        } else if(procedureVo.getRet().equals(Status.팬해제_팬아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_팬아님)));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제실패)));
        }
        return result;
    }

    /**
     * 회원 정보 조회
     */
    public String callMemberInfo(P_MemberInfo pMemberInfo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberInfo);
        mypageDao.callMemberInfo(procedureVo);
        String result;


        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        if(procedureVo.getRet().equals(Status.회원정보보기성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원정보_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보_회원아님)));
        }else if(procedureVo.getRet().equals(Status.회원정보_대상회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보_대상회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기실패)));
        }
        log.info("result:{}" + result);
        return result;
    }

    /**
     * 회원 방송방 기본설정 조회하기
     */
    public String callBroadBasic(P_BroadBasic pBroadBasic) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadBasic);
        mypageDao.callBroadBasic(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.회원정보보기성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원정보_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보_회원아님)));
        }else if(procedureVo.getRet().equals(Status.회원정보_대상회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보_대상회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기실패)));
        }
        log.info("result:{}" + result);
        return result;
    }
}
