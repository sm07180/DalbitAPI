package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
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
import java.util.List;

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
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집성공)));
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
    public String callBroadBasic(P_BroadBasicVo pBroadBasic) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadBasic);
        List<P_BroadBasicVo> BroadBasic = mypageDao.callBroadBasic(procedureVo);


        log.info("프로시저 응답 코드: @@@@@ >  {}", BroadBasic);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("subject_type",DalbitUtil.isNullToString(resultMap.get("subject_type")));
        returnMap.put("title",DalbitUtil.isNullToString(resultMap.get("title")));
        returnMap.put("image_background",new ImageVo(DalbitUtil.getStringMap(resultMap, "image_background"), SERVER_PHOTO_URL));
        returnMap.put("msg_welcom",DalbitUtil.isNullToString(resultMap.get("msg_welcom")));
        returnMap.put("restrict_entry",DalbitUtil.getIntMap(resultMap,"restrict_entry"));
        returnMap.put("restrict_age",DalbitUtil.getIntMap(resultMap,"restrict_age"));
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.방송방기본설정조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정조회_성공, procedureVo.getData())));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정조회_회원아님)));
        }
        return result;
    }

    /**
     * 회원 정보 조회
     */
    public String callMemberInfo(P_MemberInfoVo pMemberInfo) {
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
        returnMap.put("bgImg",new ImageVo(DalbitUtil.getStringMap(resultMap, "backgroundImage"), SERVER_PHOTO_URL));
        returnMap.put("profileImage",new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), SERVER_PHOTO_URL));
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
    public String callBroadBasicEdit(P_BroadBasicEditVo pBroadBasicEdit){
        ProcedureVo procedureVo = new ProcedureVo(pBroadBasicEdit);
        mypageDao.callBroadBasicEdit(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.방송방기본설정수정_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정수정_성공, procedureVo.getData())));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정수정_회원아님)));
        }
        return result;
    }

    /**
     * 회원 신고
     */
    public String callMemberReportAdd(P_MemberReportAddVo pMemberReportAdd){
        ProcedureVo procedureVo = new ProcedureVo(pMemberReportAdd);
        mypageDao.callMemberReportAdd(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.회원신고성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원신고성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원신고_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원신고_요청회원번호_정상아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원신고_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원신고_신고회원번호_정상아님, procedureVo.getData())));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원신고_이미_신고상태)));
        }
        return result;
    }

    /**
     * 회원 차단하기
     */
    public String callBlockAdd(P_MemberBlockAddVo pMemberBlockAdd){
        ProcedureVo procedureVo = new ProcedureVo(pMemberBlockAdd);
        mypageDao.callMemberBlockAdd(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.회원차단성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원차단_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단_요청회원번호_정상아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원차단_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단_신고회원번호_정상아님, procedureVo.getData())));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단_이미_신고상태)));
        }
        return result;
    }

    /**
     * 회원 차단 해제
     */
    public String callMemBerBlocklDel(P_MemberBlockDelVo pMemberBlockDel){
        ProcedureVo procedureVo = new ProcedureVo(pMemberBlockDel);
        mypageDao.callMemberBlockDel(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.회원차단해제성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단해제성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원차단해제_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단해제_요청회원번호_정상아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원차단해제_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단해제_신고회원번호_정상아님, procedureVo.getData())));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단안된상태)));
        }
        return result;
    }

    /**
     * 회원 알림설정 조회하기
     */
    public String callMemberNotify(P_MemberNotifyVo pMemberNotifyVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberNotifyVo);
        mypageDao.callMemberNotify(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("all_ok",DalbitUtil.getStringMap(resultMap,"all_ok"));
        returnMap.put("fan_reg",DalbitUtil.getStringMap(resultMap,"fan_reg"));
        returnMap.put("fan_board",DalbitUtil.getStringMap(resultMap,"fan_board"));
        returnMap.put("star_broadcast",DalbitUtil.getStringMap(resultMap,"star_broadcast"));
        returnMap.put("star_notice",DalbitUtil.getStringMap(resultMap,"star_notice"));
        returnMap.put("event_notice",DalbitUtil.getStringMap(resultMap,"event_notice"));
        returnMap.put("search",DalbitUtil.getStringMap(resultMap,"search"));
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.알림설정조회성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정조회성공, procedureVo.getData())));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정_회원아님)));
        }
        return result;
    }

    /**
     * 회원 알림설정 수정하기
     */
    public String callMemberNotifyEdit(P_MemberNotifyEditVo pMemberNotifyEditVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberNotifyEditVo);
        mypageDao.callMemberNotifyEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.알림설정수정성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정수정성공, procedureVo.getData())));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정_회원아님)));
        }
        return result;
    }

}
