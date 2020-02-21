package com.dalbit.member.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.BroadBasicOutVo;
import com.dalbit.member.vo.MemberShortCutOutVo;
import com.dalbit.member.vo.NotificationOutVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    GsonUtil gsonUtil;
    @Autowired
    RestService restService;


    /**
     * 프로필 편집
     */
    public String callProfileEdit(P_ProfileEditVo pProfileEditVo) throws GlobalException {
        Boolean isDone = false;
        String profImg = pProfileEditVo.getProfileImage();
        if(DalbitUtil.isEmpty(profImg)){
            profImg = Code.포토_프로필_디폴트_PREFIX.getCode()+"/"+Code.프로필이미지_파일명_PREFIX.getCode()+pProfileEditVo.getMemSex()+".jpg";
        }else{
            if(profImg.startsWith(Code.포토_프로필_임시_PREFIX.getCode())){
                isDone = true;
            }
            profImg = DalbitUtil.replacePath(profImg);
        }
        pProfileEditVo.setProfileImage(profImg);
        ProcedureVo procedureVo = new ProcedureVo(pProfileEditVo);
        mypageDao.callProfileEdit(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.프로필편집성공.getMessageCode())) {
            if(isDone){
                //TODO - 이미지 서버 오류 시 처리 -> GlobalException으로 throw
                restService.imgDone(DalbitUtil.replaceDonePath(pProfileEditVo.getProfileImage()), pProfileEditVo.getProfImgDel());
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집성공));
        } else if (procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임중복));
        } else if (procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임중복));
        } else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집오류));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록성공, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.팬등록_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_회원아님));
        } else if(procedureVo.getRet().equals(Status.팬등록_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_스타회원번호이상));
        } else if(procedureVo.getRet().equals(Status.팬등록_이미팬등록됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_이미팬등록됨));
        } else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록실패));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.팬해제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬해제_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제_스타회원번호이상));
        } else if (procedureVo.getRet().equals(Status.팬해제_팬아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제_팬아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제실패));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방기본설정조회_성공, broadBasicOutVo));
        } else if(procedureVo.getRet().equals(Status.방송방기본설정조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방기본설정조회_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방기본설정조회_오류));
        }
        return result;
    }


    /**
     * 본인 정보 조회
     */
    public String callMemberInfo(P_MemberInfoVo pMemberInfo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberInfo);
        mypageDao.callMemberInfo(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
        returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
        returnMap.put("birth", DalbitUtil.getBirth(DalbitUtil.getStringMap(resultMap, "birthYear"), DalbitUtil.getStringMap(resultMap, "birthMonth"), DalbitUtil.getStringMap(resultMap, "birthDay")));
        returnMap.put("memId", DalbitUtil.getStringMap(resultMap, "memId"));
        returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("profMsg", DalbitUtil.getStringMap(resultMap, "profileMsg"));
        returnMap.put("rubyCnt", DalbitUtil.getIntMap(resultMap, "ruby"));
        returnMap.put("goldCnt", DalbitUtil.getIntMap(resultMap, "gold"));

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_성공, returnMap));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_회원아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_대상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_대상아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_실패));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방기본설정수정_성공));
        } else if(procedureVo.getRet().equals(Status.방송방기본설정수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방기본설정수정_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방기본설정수정_오류));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원신고성공));
        }else if(procedureVo.getRet().equals(Status.회원신고_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원신고_요청회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.회원신고_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원신고_신고회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.회원신고_이미_신고상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원신고_이미_신고상태));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원신고오류));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단성공, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.회원차단_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단_요청회원번호_정상아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.회원차단_차단회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단_차단회원번호_정상아님, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.회원차단_이미_차단상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단_이미_차단상태));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단오류));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단해제성공, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.회원차단해제_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단해제_요청회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.회원차단해제_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단해제_신고회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.회원차단안된상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단안된상태));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원차단해제오류));
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
        returnMap.put("all_ok", DalbitUtil.getIntMap(resultMap, "all_ok"));
        returnMap.put("isMyStar", DalbitUtil.getIntMap(resultMap, "set_1"));
        returnMap.put("isGift", DalbitUtil.getIntMap(resultMap, "set_2"));
        returnMap.put("isFan", DalbitUtil.getIntMap(resultMap, "set_3"));
        returnMap.put("isComment", DalbitUtil.getIntMap(resultMap, "set_4"));
        returnMap.put("isRadio", DalbitUtil.getIntMap(resultMap, "set_5"));
        returnMap.put("isEvent", DalbitUtil.getIntMap(resultMap, "set_6"));
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.알림설정조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림설정조회_성공, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.알림설정조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림설정조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림설정조회오류));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림설정수정_성공));
        }else if(procedureVo.getRet().equals(Status.알림설정수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림설정수정_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림설정수정오류));
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
            List<MemberShortCutOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(memberShortCutList)){
                for (int i=0; i<memberShortCutList.size(); i++){
                    outVoList.add(new MemberShortCutOutVo(memberShortCutList.get(i)));
                }
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_성공, outVoList));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회오류));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_성공));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정오류));
        }
        return result;
    }


    /**
     * 회원 루비선물하기
     */
    public String callMemberGiftRuby(P_RubyVo pRubyVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRubyVo);
        mypageDao.callMemberGiftRuby(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.루비선물_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.루비선물_성공));
        }else if(procedureVo.getRet().equals(Status.루비선물_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.루비선물_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.루비선물_받는회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.루비선물_받는회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.루비선물_루비개수_비정상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.루비선물_루비개수_비정상));
        }else if(procedureVo.getRet().equals(Status.루비선물_루비개수_부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.루비선물_루비개수_부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.루비선물_실패));
        }

        return result;
    }


    /**
     * 회원 알림 내용 조회
     */
    public String callMemberNotification(P_NotificationVo pNotificationVo) {
        ProcedureVo procedureVo = new ProcedureVo(pNotificationVo);
        List<P_NotificationVo> notificationVoList = mypageDao.callMemberNotification(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(notificationVoList)){
            procedureOutputVo = null;
        }else{
            List<NotificationOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<notificationVoList.size(); i++){
                outVoList.add(new NotificationOutVo(notificationVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap notificationList = new HashMap();
        notificationList.put("list", procedureOutputVo.getOutputBox());
        notificationList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_성공, notificationList));
        } else if (procedureVo.getRet().equals(Status.회원알림내용조회_알림없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_알림없음));
        } else if (procedureVo.getRet().equals(Status.회원알림내용조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_실패));
        }
        return result;
    }
}
