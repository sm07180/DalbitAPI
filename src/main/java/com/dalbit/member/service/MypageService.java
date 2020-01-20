package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.MypageDao;
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

        if (procedureVo.getRet().equals(Status.팬해제성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제성공, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.팬해제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_회원아님)));
        } else if (procedureVo.getRet().equals(Status.팬해제_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_스타회원번호이상)));
        } else if (procedureVo.getRet().equals(Status.팬해제_팬아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제_팬아님)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.팬해제실패)));
        }
        return result;
    }

    /**
     * 회원 방송방 기본설정 조회하기
     */
    public String callBroadBasic(P_BroadBasic pBroadBasic) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadBasic);
        mypageDao.callBroadBasic(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("subject_type",DalbitUtil.isNullToString(resultMap.get("subject_type")));
        returnMap.put("title",DalbitUtil.isNullToString(resultMap.get("title")));
        returnMap.put("image_background",DalbitUtil.isNullToString(resultMap.get("image_background")));
        returnMap.put("msg_welcom",DalbitUtil.isNullToString(resultMap.get("msg_welcom")));
        if(DalbitUtil.getStringMap(resultMap,"restrict_entry").equals("0")) returnMap.put("restrict_entry","False");
        else returnMap.put("restrict_entry","True");
        if(DalbitUtil.getStringMap(resultMap,"restrict_age").equals("0")) returnMap.put("restrict_age","False");
        else returnMap.put("restrict_age","True");
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_회원아님)));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기실패)));
        }
        log.info("result:{}" + result);
        return result;
    }

    /**
     * 회원 정보 조회
     */
    public String callMemberInfo(P_MemberInfo pMemberInfo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberInfo);
        mypageDao.callMemberInfo(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("memNo",MemberVo.getUserInfo().getMem_no());
        returnMap.put("nickName",DalbitUtil.isNullToString(resultMap.get("nickName")));
        returnMap.put("memSex",DalbitUtil.isNullToString(resultMap.get("memSex")));
        returnMap.put("age",DalbitUtil.isNullToString(resultMap.get("age")));
        returnMap.put("backgroundImage",DalbitUtil.isNullToString(resultMap.get("backgroundImage")));
        returnMap.put("profileImage",DalbitUtil.isNullToString(resultMap.get("profileImage")));
        returnMap.put("profileMsg",DalbitUtil.isNullToString(resultMap.get("profileMsg")));
        returnMap.put("level",DalbitUtil.isNullToString(resultMap.get("level")));
        returnMap.put("grade",DalbitUtil.isNullToString(resultMap.get("grade")));
        returnMap.put("fanCount",DalbitUtil.isNullToString(resultMap.get("fanCount")));
        returnMap.put("starCount",DalbitUtil.isNullToString(resultMap.get("starCount")));
        returnMap.put("enableFan",DalbitUtil.isNullToString(resultMap.get("enableFan")));
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.회원정보조회성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보조회성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원정보조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보조회_회원아님)));
        }else if(procedureVo.getRet().equals(Status.회원정보조회_대상회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보조회_대상회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보조회_실패)));
        }
        log.info("result:{}" + result);
        return result;
    }



    /**
     * 회원 방송방 기본설정 수정하기
     */
    public String callBroadBasicEdit(P_BroadBasicEdit pBroadBasicEdit){
        ProcedureVo procedureVo = new ProcedureVo(pBroadBasicEdit);
        mypageDao.callBroadBasicEdit(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.방송방기본설정_수정성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정_수정성공, procedureVo.getData())));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정_회원아님)));
        }
        return result;
    }

}
