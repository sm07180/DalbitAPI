package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.BroadBasicOutVo;
import com.dalbit.member.vo.MemberInfoOutVo;
import com.dalbit.member.vo.MemberShortCutOutVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class MypageService {

    @Autowired
    MypageDao mypageDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonService commonService;
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     * 프로필 편집
     */
    public String callProfileEdit(P_ProfileEditVo pProfileEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pProfileEditVo);
        mypageDao.callProfileEdit(procedureVo);
        String result;
        if (procedureVo.getRet().equals(Status.프로필편집성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집성공)));
        } else if (procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집실패_닉네임중복)));
        } else if (procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집실패_닉네임중복)));
        } else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.프로필편집오류)));
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
        mypageDao.callBroadBasic(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        P_BroadBasicVo BroadBasic = new Gson().fromJson(procedureVo.getExt(), P_BroadBasicVo.class);
        BroadBasicOutVo broadBasicOutVo = new BroadBasicOutVo(BroadBasic);

        String result;
        if(procedureVo.getRet().equals(Status.방송방기본설정조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정조회_성공, broadBasicOutVo)));
        } else if(procedureVo.getRet().equals(Status.방송방기본설정조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정조회_회원아님)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정조회_오류)));
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

        P_MemberInfoVo memberInfo = new Gson().fromJson(procedureVo.getExt(), P_MemberInfoVo.class);
        List fanRankList = commonService.getFanRankList(memberInfo.getFanRank1(), memberInfo.getFanRank2(), memberInfo.getFanRank3());
        MemberInfoOutVo memberInfoOutVo = new MemberInfoOutVo(memberInfo, pMemberInfo.getTarget_mem_no(), fanRankList);

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_성공, memberInfoOutVo)));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_회원아님)));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_대상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_대상아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_실패)));
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
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정수정_성공)));
        } else if(procedureVo.getRet().equals(Status.방송방기본설정수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정수정_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송방기본설정수정_오류)));
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
        }else if(procedureVo.getRet().equals(Status.회원신고_이미_신고상태.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원신고_이미_신고상태)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원신고오류)));
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
        }else if(procedureVo.getRet().equals(Status.회원차단_차단회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단_차단회원번호_정상아님, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.회원차단_이미_차단상태.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단_이미_차단상태)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단오류)));
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
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단해제_요청회원번호_정상아님)));
        }else if(procedureVo.getRet().equals(Status.회원차단해제_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단해제_신고회원번호_정상아님)));
        }else if(procedureVo.getRet().equals(Status.회원차단안된상태.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단안된상태)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원차단해제오류)));
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
        procedureVo.setData(resultMap);

        String result;
        if(procedureVo.getRet().equals(Status.알림설정조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정조회_성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.알림설정조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정조회_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정조회오류)));
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
        if(procedureVo.getRet().equals(Status.알림설정수정_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정수정_성공, procedureVo.getData())));
        }else if(procedureVo.getRet().equals(Status.알림설정수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정수정_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.알림설정수정오류)));
        }
        return result;
    }

    /**
     * 회원 방송방 빠른말 가져오기
     */
    public String callMemberShortCut(P_MemberShortCutVo pMemberShortCut){
        ProcedureVo procedureVo = new ProcedureVo(pMemberShortCut);
        mypageDao.callMemberShortCut(procedureVo);
        List<P_MemberShortCutVo> memberShortCutList = mypageDao.callMemberShortCut(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원방송방빠른말조회_성공, memberShortCutList)));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원방송방빠른말조회_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원방송방빠른말조회오류)));
        }
        return result;
    }

    /**
     * 회원 방송방 빠른말 수정하기
     */
    public String callMemberShortCutEdit(P_MemberShortCutEditVo pMemberShortCutEdit){
        ProcedureVo procedureVo = new ProcedureVo(pMemberShortCutEdit);
        mypageDao.callMemberShortCutEdit(procedureVo);

        String result;

        if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원방송방빠른말수정_성공)));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원방송방빠른말수정_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원방송방빠른말수정오류)));
        }
        return result;
    }
}
