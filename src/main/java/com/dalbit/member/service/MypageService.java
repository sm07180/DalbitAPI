package com.dalbit.member.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.procedure.P_MemberBroadcastingCheckVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    SocketService socketService;
    @Autowired
    RoomDao roomDao;
    @Autowired
    ProfileService profileService;
    @Autowired
    CommonService commonService;

    /**
     * 프로필 편집
     */
    public String callProfileEdit(P_ProfileEditVo pProfileEditVo, HttpServletRequest request) throws GlobalException {

        //금지어 체크
        if(DalbitUtil.isStringMatchCheck(commonService.banWordSelect(), pProfileEditVo.getNickName())){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임금지));
        }

        Boolean isDone = false;
        String profImg = pProfileEditVo.getProfileImage();
        if(DalbitUtil.isEmpty(profImg) || profImg.startsWith(Code.포토_프로필_디폴트_PREFIX.getCode())){
            if(!DalbitUtil.isEmpty(pProfileEditVo.getProfImgDel())){
                isDone = true;
            }
            profImg = "";
        }else{
            if(profImg.startsWith(Code.포토_프로필_임시_PREFIX.getCode())){
                isDone = true;
            }
            profImg = DalbitUtil.replacePath(profImg);
        }
        pProfileEditVo.setProfileImage(profImg);
        HashMap myInfo = socketService.getMyInfo(new MemberVo().getMyMemNo(request));
        String result;
        if(myInfo != null){
            if(pProfileEditVo.getNickName().equals(DalbitUtil.getStringMap(myInfo, "nickName"))){
                pProfileEditVo.setNickName("");
            }
            ProcedureVo procedureVo = new ProcedureVo(pProfileEditVo);
            mypageDao.callProfileEdit(procedureVo);

            if (procedureVo.getRet().equals(Status.프로필편집성공.getMessageCode())) {

                if(isDone){
                    restService.imgDone(DalbitUtil.replaceDonePath(pProfileEditVo.getProfileImage()), pProfileEditVo.getProfImgDel(), request);
                }
                int memLogin = DalbitUtil.isLogin(request) ? 1 : 0;
                P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, new MemberVo().getMyMemNo(request), new MemberVo().getMyMemNo(request));
                String resultProfile = profileService.callMemberInfo(apiData, request);
                HashMap profileMap = new Gson().fromJson(resultProfile, HashMap.class);

                try{
                    HashMap profileInfoVo = new Gson().fromJson(new Gson().toJson(profileMap.get("data")), HashMap.class);
                    HashMap socketMap = new HashMap();
                    socketMap.put("nk", profileInfoVo.get("nickNm"));
                    socketMap.put("sex", profileInfoVo.get("gender"));
                    HashMap profImgMap = new Gson().fromJson(new Gson().toJson(profileInfoVo.get("profImg")), HashMap.class);
                    if(DalbitUtil.isEmpty(profImgMap.get("path"))){
                        socketMap.put("image", "");
                    }else{
                        socketMap.put("image", profImgMap.get("path"));
                    }
                    socketService.changeMemberInfo(pProfileEditVo.getMem_no(), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
                }catch(Exception e){}

                result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집성공, profileMap.get("data")));

            } else if (procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임중복));
            } else if (procedureVo.getRet().equals(Status.프로필편집실패_닉네임중복.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임중복));
            } else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집오류));
            }
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필편집오류));
        }
        return result;
    }


    /**
     * 팬등록
     */
    public String callFanstarInsert(P_FanstarInsertVo pFanstarInsertVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanstarInsertVo);
        mypageDao.callFanstarInsert(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.팬등록성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록성공));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제성공));
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

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
            returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
            returnMap.put("birth", DalbitUtil.getBirth(DalbitUtil.getStringMap(resultMap, "birthYear"), DalbitUtil.getStringMap(resultMap, "birthMonth"), DalbitUtil.getStringMap(resultMap, "birthDay")));
            returnMap.put("memId", DalbitUtil.getStringMap(resultMap, "memId"));
            returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("profMsg", DalbitUtil.replaceMaskString(commonService.banWordSelect(), DalbitUtil.getStringMap(resultMap, "profileMsg")));
            returnMap.put("dalCnt", DalbitUtil.getIntMap(resultMap, "ruby"));
            returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "gold"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_성공, returnMap));

        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_회원아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_대상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_대상아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_실패));
        }
        log.info("result: {}", result);
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
        returnMap.put("isPush", DalbitUtil.getIntMap(resultMap, "set_6"));
        returnMap.put("isLike", DalbitUtil.getIntMap(resultMap, "set_7"));
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
     * 회원 달 선물하기
     */
    public String callMemberGiftRuby(P_RubyVo pRubyVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRubyVo);
        mypageDao.callMemberGiftRuby(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.달선물_성공.getMessageCode())) {
            try{
                socketService.giftDal(new MemberVo().getMyMemNo(request), pRubyVo.getGifted_mem_no(), pRubyVo.getRuby(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){
                log.info("Socket Service giftDal Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.달선물_성공));
        }else if(procedureVo.getRet().equals(Status.달선물_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달선물_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.달선물_받는회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달선물_받는회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.달선물_달개수_비정상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달선물_달개수_비정상));
        }else if(procedureVo.getRet().equals(Status.달선물_달개수_부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달선물_달개수_부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.달선물_실패));
        }

        return result;
    }


    /**
     * 회원 알림 내용 조회
     */
    public String callMemberNotification(P_NotificationVo pNotificationVo) {
        ProcedureVo procedureVo = new ProcedureVo(pNotificationVo);
        List<P_NotificationVo> notificationVoList = mypageDao.callMemberNotification(procedureVo);

        HashMap notificationList = new HashMap();
        if(DalbitUtil.isEmpty(notificationVoList)){
            notificationList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_알림없음, notificationList));
        }

        List<NotificationOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<notificationVoList.size(); i++){
            outVoList.add(new NotificationOutVo(notificationVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        notificationList.put("list", procedureOutputVo.getOutputBox());
        notificationList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_성공, notificationList));
        } else if (procedureVo.getRet().equals(Status.회원알림내용조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용조회_실패));
        }
        return result;
    }


    /**
     * 마이페이지 공지사항 등록
     */
    public String callMypageNoticeAdd(P_MypageNoticeAddVo pMypageNoticeAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeAddVo);
        mypageDao.callMypageNoticeAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지등록_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
        }else if(procedureVo.getRet().equals(Status.공지등록_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지등록_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지등록_대상회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지등록_대상회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지등록_권한없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지등록_권한없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }

        return result;

    }


    /**
     * 마이페이지 공지사항 수정
     */
    public String callMypageNoticeEdit(P_MypageNoticeEditVo pMypageNoticeEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeEditVo);
        mypageDao.callMypageNoticeEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지수정_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지수정_성공));
        }else if(procedureVo.getRet().equals(Status.공지수정_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지수정_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지수정_대상회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지수정_대상회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지수정_권한없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지수정_권한없음));
        }else if(procedureVo.getRet().equals(Status.공지수정_잘못된공지번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지수정_잘못된공지번호));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지수정_실패));
        }

        return result;
    }


    /**
     * 마이페이지 공지사항 삭제
     */
    public String callMypageNoticeDel(P_MypageNoticeDelVo pMypageNoticeDelVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeDelVo);
        mypageDao.callMypageNoticeDel(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지삭제_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_성공));
        }else if(procedureVo.getRet().equals(Status.공지삭제_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지삭제_대상회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_대상회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지삭제_권한없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_권한없음));
        }else if(procedureVo.getRet().equals(Status.공지삭제_잘못된공지번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_잘못된공지번호));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
        }

        return result;
    }


    /**
     * 마이페이지 공지사항 조회
     */
    public String callMypageNoticeSelect(P_MypageNoticeSelectVo pMypagNoticeSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypagNoticeSelectVo);
        List<P_MypageNoticeSelectVo> mypageNoticeListVo = mypageDao.callMypageNoticeSelect(procedureVo);

        HashMap mypageNoticeList = new HashMap();
        if(DalbitUtil.isEmpty(mypageNoticeListVo)){
            mypageNoticeList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, mypageNoticeList));
        }

        List<MypageNoticeListOutVo> outVoList = new ArrayList<>();
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(pMypagNoticeSelectVo.getTarget_mem_no());
        for (int i=0; i<mypageNoticeListVo.size(); i++){
            //사이트+방송방 금지어 조회 마이페이지 공지사항 제목, 내용 마스킹 처리
            if(!DalbitUtil.isEmpty(commonService.broadcastBanWordSelect(banWordVo))){
                mypageNoticeListVo.get(i).setTitle(DalbitUtil.replaceMaskString(commonService.banWordSelect()+"|"+commonService.broadcastBanWordSelect(banWordVo), mypageNoticeListVo.get(i).getTitle()));
                mypageNoticeListVo.get(i).setContents(DalbitUtil.replaceMaskString(commonService.banWordSelect()+"|"+commonService.broadcastBanWordSelect(banWordVo), mypageNoticeListVo.get(i).getContents()));
            }else{
                mypageNoticeListVo.get(i).setTitle(DalbitUtil.replaceMaskString(commonService.banWordSelect(), mypageNoticeListVo.get(i).getTitle()));
                mypageNoticeListVo.get(i).setContents(DalbitUtil.replaceMaskString(commonService.banWordSelect(), mypageNoticeListVo.get(i).getContents()));
            }
            outVoList.add(new MypageNoticeListOutVo(mypageNoticeListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        mypageNoticeList.put("list", procedureOutputVo.getOutputBox());
        mypageNoticeList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, mypageNoticeList));
        } else if (procedureVo.getRet().equals(Status.공지조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_요청회원번호_회원아님));
        } else if (procedureVo.getRet().equals(Status.공지조회_대상회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_대상회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
        }
        return result;
    }


    /**
     * 내지갑 달 내역 조회
     */
    public String callMemberWalletDal(P_DalVo pDalVo) {
        ProcedureVo procedureVo = new ProcedureVo(pDalVo);
        List<P_DalVo> dalListVo = mypageDao.callMemberWalletDal(procedureVo);

        HashMap mypageDalList = new HashMap();
        if(DalbitUtil.isEmpty(dalListVo)){
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            mypageDalList.put("dalTotCnt", DalbitUtil.getIntMap(resultMap, "dal"));
            mypageDalList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.달사용내역조회_없음, mypageDalList));
        }

        List<WalletDalListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<dalListVo.size(); i++){
            outVoList.add(new WalletDalListOutVo(dalListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        mypageDalList.put("dalTotCnt", DalbitUtil.getIntMap(resultMap, "dal"));
        mypageDalList.put("list", procedureOutputVo.getOutputBox());
        mypageDalList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달사용내역조회_성공, mypageDalList));
        } else if (procedureVo.getRet().equals(Status.달사용내역조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달사용내역조회_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.달사용내역조회_실패));
        }
        return result;
    }


    /**
     * 내지갑 별 내역 조회
     */
    public String callMemberWalletByeol(P_ByeolVo pByeolVo) {
        ProcedureVo procedureVo = new ProcedureVo(pByeolVo);
        List<P_ByeolVo> byeolListVo = mypageDao.callMemberWalletByeol(procedureVo);

        HashMap mypageByeolList = new HashMap();
        if(DalbitUtil.isEmpty(byeolListVo)){
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            mypageByeolList.put("byeolTotCnt", DalbitUtil.getIntMap(resultMap, "byeol"));
            mypageByeolList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.별사용내역조회_없음, mypageByeolList));
        }

        List<WalletByeolListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<byeolListVo.size(); i++){
            outVoList.add(new WalletByeolListOutVo(byeolListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        mypageByeolList.put("byeolTotCnt", DalbitUtil.getIntMap(resultMap, "byeol"));
        mypageByeolList.put("list", procedureOutputVo.getOutputBox());
        mypageByeolList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별사용내역조회_성공, mypageByeolList));
        } else if (procedureVo.getRet().equals(Status.별사용내역조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별사용내역조회_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.별사용내역조회_실패));
        }
        return result;
    }


    /**
     *  마이페이지 리포트 방송내역 보기
     */
    public String callMypageMypageReportBroad(P_MypageReportBroadVo pMypageReportBroadVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageReportBroadVo);
        List<P_MypageReportBroadVo> reportBoradListVo = mypageDao.callMypageMypageReportBroad(procedureVo);

        HashMap reportBroadList = new HashMap();
        if(DalbitUtil.isEmpty(reportBoradListVo)){
            reportBroadList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.방송내역조회_없음, reportBroadList));
        }
        List<MypageReportBroadListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<reportBoradListVo.size(); i++){
            outVoList.add(new MypageReportBroadListOutVo(reportBoradListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        reportBroadList.put("list", procedureOutputVo.getOutputBox());
        reportBroadList.put("broadcastTime", DalbitUtil.getIntMap(resultMap, "broadcastTime"));
        reportBroadList.put("byeolTotCnt", DalbitUtil.getIntMap(resultMap, "goldTotal"));
        reportBroadList.put("goodTotCnt", DalbitUtil.getIntMap(resultMap, "goodTotal"));
        reportBroadList.put("listenerTotCnt", DalbitUtil.getIntMap(resultMap, "listenerTotal"));
        reportBroadList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송내역조회_성공, reportBroadList));
        } else if (procedureVo.getRet().equals(Status.방송내역조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송내역조회_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송내역조회_실패));
        }
        return result;
    }

    /**
     *  마이페이지 리포트 청취내역 보기
     */
    public String callMypageMypageReportListen(P_MypageReportListenVo pMypageReportListenVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageReportListenVo);
        List<P_MypageReportListenVo> reportListenListVo = mypageDao.callMypageMypageReportListen(procedureVo);

        HashMap reportListenList = new HashMap();
        if(DalbitUtil.isEmpty(reportListenListVo)){
            reportListenList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.청취내역조회_없음, reportListenList));
        }

        List<MypageReportListenListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<reportListenListVo.size(); i++){
            outVoList.add(new MypageReportListenListOutVo(reportListenListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        reportListenList.put("list", procedureOutputVo.getOutputBox());
        reportListenList.put("listeningTime", DalbitUtil.getIntMap(resultMap, "listeningTime"));
        reportListenList.put("giftDalTotCnt", DalbitUtil.getIntMap(resultMap, "giftRubyTotal"));
        reportListenList.put("byeolTotCnt", DalbitUtil.getIntMap(resultMap, "goldTotal"));
        reportListenList.put("guestTime", DalbitUtil.getIntMap(resultMap, "guestTime"));
        reportListenList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.청취내역조회_성공, reportListenList));
        } else if (procedureVo.getRet().equals(Status.청취내역조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.청취내역조회_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.청취내역조회_실패));
        }
        return result;
    }

    /**
     * 방송설정 금지어 단어 조회
     */
    public String callMyapgeGetBanWord(P_BanWordSelectVo pBanWordSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBanWordSelectVo);
        mypageDao.callMyapgeGetBanWord(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("banWord", DalbitUtil.getStringMap(resultMap, "banWord"));
        returnMap.put("banWordCnt", DalbitUtil.getIntMap(resultMap, "count"));
        procedureVo.setData(returnMap);

        String result;
        if(Status.금지어조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어조회_성공, procedureVo.getData()));
        }else if(Status.금지어조회_요청번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어조회_요청번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어조회_실패));
        }

        return result;
    }


    /**
     * 방송설정 금지어 저장
     */
    public String callMypageInsertBanWord(P_BanWordInsertVo pBanWordInsertVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pBanWordInsertVo);
        mypageDao.callMypageInsertBanWord(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("banWord", DalbitUtil.getStringMap(resultMap, "banWord"));
        returnMap.put("banWordCnt", DalbitUtil.getIntMap(resultMap, "count"));
        procedureVo.setData(returnMap);

        String result;
        if(Status.금지어저장_성공.getMessageCode().equals(procedureVo.getRet())) {
            // 진행중인 방이 있을 경우 소켓으로 금지어 업데이트 요청
            P_MemberBroadcastingCheckVo pMemberBroadcastingCheckVo = new P_MemberBroadcastingCheckVo();
            pMemberBroadcastingCheckVo.setMem_no(new MemberVo().getMyMemNo(request));
            ProcedureVo procedureCheckVo = new ProcedureVo(pMemberBroadcastingCheckVo);
            roomDao.callMemberBroadcastingCheck(procedureCheckVo);
            if(Status.방송진행여부체크_방송중.getMessageCode().equals(procedureCheckVo.getRet())) {
                HashMap resultCheckMap = new Gson().fromJson(procedureCheckVo.getExt(), HashMap.class);
                try{
                    socketService.banWord(DalbitUtil.getStringMap(resultCheckMap, "roomNo"), new MemberVo().getMyMemNo(request), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
                }catch(Exception e){
                    log.info("Socket Service banWord Exception {}", e);
                }
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_성공, procedureVo.getData()));
        }else if(Status.금지어저장_요청번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_요청번호_회원아님));
        }else if(Status.금지어저장_초과.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_초과));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_실패));
        }

        return result;
    }


    /**
     * 방송설정 유저 검색
     */
    public String callMypageSearchUser(P_SearchUserVo pSearchUserVo) {
        ProcedureVo procedureVo = new ProcedureVo(pSearchUserVo);
        List<P_SearchUserVo> searchUserListVo = mypageDao.callMypageSearchUser(procedureVo);

        HashMap searchUserList = new HashMap();
        if(DalbitUtil.isEmpty(searchUserListVo)){
            searchUserList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.유저검색_없음, searchUserList));
        }

        List<SearchUserOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<searchUserListVo.size(); i++){
            outVoList.add(new SearchUserOutVo(searchUserListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        searchUserList.put("list", procedureOutputVo.getOutputBox());
        searchUserList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.유저검색_성공, searchUserList));
        } else if (procedureVo.getRet().equals(Status.유저검색_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.유저검색_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.유저검색_실패));
        }
        return result;
    }


    /**
     * 방송설정 고정 매니저 조회
     */
    public String callMypageManager(P_MypageManagerVo pMypageManagerVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageManagerVo);
        List<P_MypageManagerVo> managerListVo = mypageDao.callMypageManager(procedureVo);

        HashMap managerList = new HashMap();
        if(DalbitUtil.isEmpty(managerListVo)){
            managerList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.고정매니저조회_없음, managerList));
        }

        List<ManagerOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<managerListVo.size(); i++){
            outVoList.add(new ManagerOutVo(managerListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        managerList.put("list", procedureOutputVo.getOutputBox());

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저조회_성공, managerList));
        } else if (procedureVo.getRet().equals(Status.고정매니저조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저조회_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저조회_실패));
        }
        return result;
    }


    /**
     * 방송설정 고정 매니저 등록
     */
    public String callMypageManagerAdd(P_MypageManagerAddVo pMypageManagerAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageManagerAddVo);
        mypageDao.callMypageManagerAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.고정매니저등록_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저등록_성공));
        }else if(procedureVo.getRet().equals(Status.고정매니저등록_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저등록_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.고정매니저등록_매니저회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저등록_매니저회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.고정매니저등록_5명초과.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저등록_5명초과));
        }else if(procedureVo.getRet().equals(Status.고정매니저등록_이미매니저등록.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저등록_이미매니저등록));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저등록_실패));
        }

        return result;
    }


    /**
     * 방송설정 고정 매니저 권한수정
     */
    public String callMypageManagerEdit(P_MypageManagerEditVo pMypageManagerEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageManagerEditVo);
        mypageDao.callMypageManagerEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.고정매니저_권한수정_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저_권한수정_성공));
        }else if(procedureVo.getRet().equals(Status.고정매니저_권한수정_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저_권한수정_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.고정매니저_권한수정_매니저회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저_권한수정_매니저회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.고정매니저_권한수정_등록된매니저아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저_권한수정_등록된매니저아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저_권한수정_실패));
        }

        return result;
    }


    /**
     * 방송설정 고정 매니저 해제
     */
    public String callMypageManagerDel(P_MypageManagerDelVo pMypageManagerDelVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageManagerDelVo);
        mypageDao.callMypageManagerDel(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.고정매니저해제_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저해제_성공));
        }else if(procedureVo.getRet().equals(Status.고정매니저해제_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저해제_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.고정매니저해제_매니저회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저해제_매니저회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.고정매니저해제_등록된매니저아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저해제_등록된매니저아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저해제_실패));
        }

        return result;
    }


    /**
     * 방송설정 블랙리스트 조회
     */
    public String callMypageBlackListView(P_MypageBlackVo pMypageBlackVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageBlackVo);
        List<P_MypageBlackVo> blackListVo = mypageDao.callMypageBlackListView(procedureVo);

        HashMap blackList = new HashMap();
        if(DalbitUtil.isEmpty(blackListVo)){
            blackList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트조회_없음, blackList));
        }

        List<BlackListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<blackListVo.size(); i++){
            outVoList.add(new BlackListOutVo(blackListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        blackList.put("list", procedureOutputVo.getOutputBox());
        blackList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트조회_성공, blackList));
        } else if (procedureVo.getRet().equals(Status.블랙리스트조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트조회_요청회원번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트조회_실패));
        }
        return result;
    }


    /**
     * 방송설정 블랙리스트 등록
     */
    public String callMypageBlackListAdd(P_MypageBlackAddVo pMypageBlackAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageBlackAddVo);
        mypageDao.callMypageBlackListAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.블랙리스트등록_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_성공));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_블랙회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_블랙회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_이미블랙등록.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_이미블랙등록));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_실패));
        }

        return result;
    }


    /**
     * 방송설정 블랙리스트 해제
     */
    public String callMypageBlackListDel(P_MypageBlackDelVo pMypageBlackDelVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageBlackDelVo);
        mypageDao.callMypageBlackListDel(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.블랙리스트해제_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트해제_성공));
        }else if(procedureVo.getRet().equals(Status.블랙리스트해제_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트해제_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.블랙리스트해제_블랙회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트해제_블랙회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.블랙리스트해제_블랙회원없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트해제_블랙회원없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트해제_실패));
        }

        return result;
    }


    /**
     * 별 달 교환 아이템 가져오기
     */
    public String changeItemSelect(P_ChangeItemListVo pChangeItemVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChangeItemVo);
        List<P_ChangeItemListVo> changeItemListVo = mypageDao.changeItemSelect(procedureVo);

        HashMap changeItemList = new HashMap();
        if(DalbitUtil.isEmpty(changeItemListVo)){
            changeItemList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환아이템_조회_없음, changeItemList));
        }

        List<ChangeItemListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<changeItemListVo.size(); i++){
            outVoList.add(new ChangeItemListOutVo(changeItemListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        changeItemList.put("list", procedureOutputVo.getOutputBox());
        changeItemList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환아이템_조회_성공, changeItemList));
        } else if (procedureVo.getRet().equals(Status.별_달_교환아이템_조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환아이템_조회_요청회원번호_회원아님));
        } else if (procedureVo.getRet().equals(Status.별_달_교환아이템_조회_IOS_지원안함.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환아이템_조회_IOS_지원안함));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환아이템_조회_실패));
        }
        return result;
    }


    /**
     * 별 달 교환하기
     */
    public String changeItem(P_ChangeItemVo pChangeItemVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChangeItemVo);
        mypageDao.changeItem(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
        returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
        returnMap.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
        returnMap.put("expBegin", DalbitUtil.getIntMap(resultMap, "expBegin"));
        returnMap.put("expNext", DalbitUtil.getIntMap(resultMap, "expNext"));
        returnMap.put("isLevelUp", DalbitUtil.getBooleanMap(resultMap, "levelUp"));
        returnMap.put("dalCnt", DalbitUtil.getIntMap(resultMap, "dal"));
        returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "byeol"));
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.별_달_교환하기_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.별_달_교환하기_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_요청회원번호_회원아님));
        } else if (procedureVo.getRet().equals(Status.별_달_교환하기_IOS_지원안함.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_IOS_지원안함));
        } else if (procedureVo.getRet().equals(Status.별_달_교환하기_상품코드없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_상품코드없음));
        } else if (procedureVo.getRet().equals(Status.별_달_교환하기_별부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_별부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_실패));
        }

        return result;

    }
}
