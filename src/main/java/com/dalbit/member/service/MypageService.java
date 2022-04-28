package com.dalbit.member.service;

import com.dalbit.admin.vo.MemberInfoVo;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.StoryResultVo;
import com.dalbit.broadcast.vo.procedure.P_MemberBroadcastingCheckVo;
import com.dalbit.broadcast.vo.request.StoryHistoryVo;
import com.dalbit.member.vo.procedure.P_WalletPopupListVo;
import com.dalbit.clip.vo.procedure.P_ClipUploadListVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.GoodListVo;
import com.dalbit.member.vo.request.StoryVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        //닉네임 길이체크
        String trimNickname = pProfileEditVo.getNickName().trim().replace(" ","");
        if(trimNickname.length() < 2){
            return gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임짦음));
        }

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(pProfileEditVo.getNickName())){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크
        if(mypageDao.selectAdminBadge(pProfileEditVo.getMem_no()) != 1){
            if(DalbitUtil.isStringMatchCheck(commonService.banWordSelect(), pProfileEditVo.getNickName())){
                return gsonUtil.toJson(new JsonOutputVo(Status.닉네임금지));
            }
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
        HashMap myInfo = socketService.getMyInfo(MemberVo.getMyMemNo(request));
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
                P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo(request), MemberVo.getMyMemNo(request));
                String resultProfile = profileService.callMemberInfo(apiData, request);
                HashMap profileMap = new Gson().fromJson(resultProfile, HashMap.class);

                try{
                    HashMap profileInfoVo = new Gson().fromJson(new Gson().toJson(profileMap.get("data")), HashMap.class);
                    HashMap socketMap = new HashMap();
                    socketMap.put("nk", profileInfoVo.get("nickNm"));
                    socketMap.put("sex", profileInfoVo.get("gender"));
                    HashMap profImgMap = new Gson().fromJson(new Gson().toJson(profileInfoVo.get("profImg")), HashMap.class);
                    if(DalbitUtil.isEmpty(profImgMap.get("path"))){
                        socketMap.put("image", "/profile_3/profile_" + profileInfoVo.get("gender") + "_200327.jpg");
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
        } else if(procedureVo.getRet().equals(Status.팬등록_본인불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_본인불가));
        } else if(procedureVo.getRet().equals(Status.팬등록_차단회원불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_차단회원불가));
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

        String systemBanWord = commonService.banWordSelect();
        String titleBanWord = commonService.titleBanWordSelect();

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(pBroadBasicEdit.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크(제목)
        if(DalbitUtil.isStringMatchCheck(titleBanWord, pBroadBasicEdit.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방수정제목금지));
        }

        //금지어 체크(인사말)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pBroadBasicEdit.getWelcomMsg())){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방수정인사말금지));
        }

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
        returnMap.put("isAll", DalbitUtil.getIntMap(resultMap, "all_ok"));
        returnMap.put("isMyStar", DalbitUtil.getIntMap(resultMap, "set_1"));
        returnMap.put("isGift", DalbitUtil.getIntMap(resultMap, "set_2"));
        returnMap.put("isFan", DalbitUtil.getIntMap(resultMap, "set_3"));
        returnMap.put("isComment", DalbitUtil.getIntMap(resultMap, "set_4"));
        returnMap.put("isRadio", DalbitUtil.getIntMap(resultMap, "set_5"));
        returnMap.put("isPush", DalbitUtil.getIntMap(resultMap, "set_6"));
        returnMap.put("isLike", DalbitUtil.getIntMap(resultMap, "set_7"));
        returnMap.put("isReply", DalbitUtil.getIntMap(resultMap, "set_8"));
        returnMap.put("isStarClip", DalbitUtil.getIntMap(resultMap, "set_9"));
        returnMap.put("isMyClip", DalbitUtil.getIntMap(resultMap, "set_10"));
        returnMap.put("isReceive", DalbitUtil.getIntMap(resultMap, "set_11"));        //알림받기 방송시작 알림
        returnMap.put("isMailbox", DalbitUtil.getIntMap(resultMap, "set_12"));        //메시지 알림받기
        returnMap.put("isTeam", DalbitUtil.getIntMap(resultMap, "set_15"));        //팀제도 알림받기
        returnMap.put("alimType", DalbitUtil.getStringMap(resultMap, "alim_slct")); //알림음구분(n:무음,s:소리,v:진동)
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
        P_MemberNotifyVo pMemberNotifyVo = new P_MemberNotifyVo();
        pMemberNotifyVo.setMem_no(pMemberNotifyEditVo.getMem_no());
        ProcedureVo procedureViewVo = new ProcedureVo(pMemberNotifyVo);
        mypageDao.callMemberNotify(procedureViewVo);

        ProcedureVo procedureVo = new ProcedureVo(pMemberNotifyEditVo);
        mypageDao.callMemberNotifyEdit(procedureVo);

        String result;
        Status status = null;
        if(procedureVo.getRet().equals(Status.알림설정수정_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureViewVo.getExt(), HashMap.class);

            if(!(DalbitUtil.getStringMap(resultMap, "alim_slct").equals(pMemberNotifyEditVo.getAlim_slct())) && "n".equals(pMemberNotifyEditVo.getAlim_slct())){
                status =Status.알림무음;
            }else if(!(DalbitUtil.getStringMap(resultMap, "alim_slct").equals(pMemberNotifyEditVo.getAlim_slct())) && "s".equals(pMemberNotifyEditVo.getAlim_slct())){
                status =Status.알림소리;
            }else if(!(DalbitUtil.getStringMap(resultMap, "alim_slct").equals(pMemberNotifyEditVo.getAlim_slct())) && "v".equals(pMemberNotifyEditVo.getAlim_slct())){
                status =Status.알림진동;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getAll_ok()) && DalbitUtil.getIntMap(resultMap, "all_ok") != pMemberNotifyEditVo.getAll_ok()){
                status = pMemberNotifyEditVo.getAll_ok() == 1 ? Status.전제알림수신_ON : Status.전제알림수신_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_1()) && DalbitUtil.getIntMap(resultMap, "set_1") != pMemberNotifyEditVo.getSet_1()){
                status = pMemberNotifyEditVo.getSet_1() == 1 ? Status.마이스타방송시작_ON : Status.마이스타방송시작_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_9()) && DalbitUtil.getIntMap(resultMap, "set_9") != pMemberNotifyEditVo.getSet_9()){
                status = pMemberNotifyEditVo.getSet_9() == 1 ? Status.마이스타클립등록_ON : Status.마이스타클립등록_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_2()) && DalbitUtil.getIntMap(resultMap, "set_2") != pMemberNotifyEditVo.getSet_2()){
                status = pMemberNotifyEditVo.getSet_2() == 1 ? Status.마이스타방송공지_ON : Status.마이스타방송공지_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_10()) && DalbitUtil.getIntMap(resultMap, "set_10") != pMemberNotifyEditVo.getSet_10()){
                status = pMemberNotifyEditVo.getSet_10() == 1 ? Status.내클립_ON : Status.내클립_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_3()) && DalbitUtil.getIntMap(resultMap, "set_3") != pMemberNotifyEditVo.getSet_3()){
                status = pMemberNotifyEditVo.getSet_3() == 1 ? Status.신규팬추가_ON : Status.신규팬추가_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_4()) && DalbitUtil.getIntMap(resultMap, "set_4") != pMemberNotifyEditVo.getSet_4()){
                status = pMemberNotifyEditVo.getSet_4() == 1 ? Status.팬보드신규글등록_ON : Status.팬보드신규글등록_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_8()) && DalbitUtil.getIntMap(resultMap, "set_8") != pMemberNotifyEditVo.getSet_8()){
                status = pMemberNotifyEditVo.getSet_8() == 1 ? Status.팬보드댓글등록_ON : Status.팬보드댓글등록_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_5()) && DalbitUtil.getIntMap(resultMap, "set_5") != pMemberNotifyEditVo.getSet_5()){
                status = pMemberNotifyEditVo.getSet_5() == 1 ? Status.선물도착_ON : Status.선물도착_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_6()) && DalbitUtil.getIntMap(resultMap, "set_6") != pMemberNotifyEditVo.getSet_6()){
                status = pMemberNotifyEditVo.getSet_6() == 1 ? Status.일대일문의답변도착_ON : Status.일대일문의답변도착_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_7()) && DalbitUtil.getIntMap(resultMap, "set_7") != pMemberNotifyEditVo.getSet_7()){
                status = pMemberNotifyEditVo.getSet_7() == 1 ? Status.서비스알림_ON : Status.서비스알림_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_11()) && DalbitUtil.getIntMap(resultMap, "set_11") != pMemberNotifyEditVo.getSet_11()){
                status = pMemberNotifyEditVo.getSet_11() == 1 ? Status.방송시작알림_ON : Status.방송시작알림_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_12()) && DalbitUtil.getIntMap(resultMap, "set_12") != pMemberNotifyEditVo.getSet_12()){
                status = pMemberNotifyEditVo.getSet_12() == 1 ? Status.메시지알림_ON : Status.메시지알림_OFF;
            }else if(!DalbitUtil.isEmpty(pMemberNotifyEditVo.getSet_15()) && DalbitUtil.getIntMap(resultMap, "set_15") != pMemberNotifyEditVo.getSet_15()){
                status = pMemberNotifyEditVo.getSet_15() == 1 ? Status.팀알림_ON : Status.팀알림_OFF;
            }else{
                status = Status.알림설정수정_성공;
            }

            result = gsonUtil.toJson(new JsonOutputVo(status));
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
    public String callMemberShortCut(P_MemberShortCutVo pMemberShortCut, String code, HttpServletRequest request){
        String result;

        DeviceVo deviceVo = new DeviceVo(request);
        if( (deviceVo.getOs() == 1 && Integer.parseInt(deviceVo.getAppBuild()) > 1 && Integer.parseInt(deviceVo.getAppBuild()) < 21)
                || (deviceVo.getOs() == 2 && Integer.parseInt(deviceVo.getAppBuild()) < 92)
        ) {
            ProcedureVo procedureVo = new ProcedureVo(pMemberShortCut);
            //mypageDao.callMemberShortCut(procedureVo);
            List<P_MemberShortCutVo> memberShortCutList = mypageDao.callMemberShortCut(procedureVo);

            if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_성공.getMessageCode())) {
                List<MemberShortCutOutVo> outVoList = new ArrayList<>();
                if (!DalbitUtil.isEmpty(memberShortCutList)) {
                    int len = memberShortCutList.size();
                    if(deviceVo.getOs() == 1){
                        len = 3;
                    }
                    for (int i = 0; i < len; i++) {
                        outVoList.add(new MemberShortCutOutVo(memberShortCutList.get(i)));
                    }
                }
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_성공, outVoList));
            } else if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_회원아님.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_회원아님));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회오류));
            }
        } else if (deviceVo.getOs() == 3) {
            ProcedureVo procedureVo = new ProcedureVo(pMemberShortCut);
            //mypageDao.callMemberShortCut(procedureVo);
            List<P_MemberShortCutVo> memberShortCutList = mypageDao.callMemberShortCut(procedureVo);

            if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_성공.getMessageCode())) {
                List<MemberShortCutOutVo> outVoList = new ArrayList<>();
                if (!DalbitUtil.isEmpty(memberShortCutList)) {
                    for (int i = 0; i < memberShortCutList.size(); i++) {
                        outVoList.add(new MemberShortCutOutVo(memberShortCutList.get(i)));
                    }
                }
                HashMap returnMap = new HashMap();
                returnMap.put("list", outVoList);
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_성공, returnMap));
            } else if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_회원아님.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_회원아님));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회오류));
            }
        }else{
            ProcedureVo procedureVo = new ProcedureVo(pMemberShortCut);
            List<P_MemberShortCutVo> memberShortCutList = mypageDao.callMemberShortCut(procedureVo);

            if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_성공.getMessageCode())) {
                List<MemberShortCutOutVo> outVoList = new ArrayList<>();
                if(!DalbitUtil.isEmpty(memberShortCutList)){
                    int len = memberShortCutList.size();
                    if(deviceVo.getOs() == 1 && (Integer.parseInt(deviceVo.getAppBuild()) == 1 || Integer.parseInt(deviceVo.getAppBuild()) == 21)){
                        len = 3;
                    }
                    for (int i = 0; i < len; i++) {
                        outVoList.add(new MemberShortCutOutVo(memberShortCutList.get(i)));
                    }
                }
                ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
                HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
                HashMap returnMap = new HashMap();
                returnMap.put("list", outVoList);
                returnMap.put("itemPrice", DalbitUtil.getIntMap(resultMap,"item_price"));
                returnMap.put("dal", DalbitUtil.getIntMap(resultMap,"dal"));
                returnMap.put("maxCnt", 6);
                returnMap.put("useDay", DalbitUtil.getIntMap(resultMap,"useDay"));
                returnMap.put("endDt", outVoList.size() > 3 ? (!outVoList.get(3).isOn() ? "" : (DalbitUtil.isEmpty(outVoList.get(3).getEndDt()) ? "" : outVoList.get(3).getEndDt())) : "");
                returnMap.put("endTs", outVoList.size() > 3 ? (!outVoList.get(3).isOn() ? 0 : (DalbitUtil.isEmpty(outVoList.get(3).getEndTs()) ? 0 : outVoList.get(3).getEndTs())) : 0);

                if(code.equals("add")){
                    result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말추가_성공, returnMap));
                } else if(code.equals("extend")) {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_성공, returnMap));
                } else if(code.equals("edit")) {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_성공, returnMap));
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_성공, returnMap));
                }

            } else if (procedureVo.getRet().equals(Status.회원방송방빠른말조회_회원아님.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회_회원아님));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말조회오류));
            }
        }
        return result;
    }


    /**
     * 회원 방송방 빠른말 수정하기
     */
    public String callMemberShortCutEdit(P_MemberShortCutEditVo pMemberShortCutEdit, HttpServletRequest request){
        ProcedureVo procedureVo = new ProcedureVo(pMemberShortCutEdit);
        mypageDao.callMemberShortCutEdit(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_성공.getMessageCode())) {
            P_MemberShortCutVo apiData = new P_MemberShortCutVo();
            apiData.setMem_no(pMemberShortCutEdit.getMem_no());
            ProcedureVo procedureListVo = new ProcedureVo(apiData);
            List<P_MemberShortCutVo> memberShortCutList = mypageDao.callMemberShortCut(procedureListVo);
            List<MemberShortCutOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(memberShortCutList)){
                for (int i=0; i<memberShortCutList.size(); i++){
                    outVoList.add(new MemberShortCutOutVo(memberShortCutList.get(i)));
                }
            }
            /*ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("list", outVoList);
            returnMap.put("itemPrice", DalbitUtil.getIntMap(resultMap, "item_price"));
            returnMap.put("dal", DalbitUtil.getIntMap(resultMap, "dal"));
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_성공, returnMap));*/

            result = callMemberShortCut(apiData, "edit", request);
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_회원아님));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_명령어번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_명령어번호없음));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말수정_사용기간만료.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말수정_사용기간만료));
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
                socketService.giftDal(MemberVo.getMyMemNo(request), pRubyVo.getGifted_mem_no(), pRubyVo.getRuby(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
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
        }else if(Status.이전작업대기중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이전작업대기중));
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
        notificationList.put("newCnt", DalbitUtil.getIntMap(resultMap, "newCnt"));
        notificationList.put("list", procedureOutputVo.getOutputBox());
        notificationList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            // 조회 성공 시 읽음 표시 update
            mypageDao.callReadALLNotification(pNotificationVo);

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
    public String callMypageNoticeAdd(P_MypageNoticeAddVo pMypageNoticeAddVo, HttpServletRequest request) throws GlobalException {
        Boolean isDone = false;
        String addFile = pMypageNoticeAddVo.getImagePath();

        if(!DalbitUtil.isEmpty(addFile) && addFile.startsWith(Code.포토_마이페이지공지_임시_PREFIX.getCode())){
            isDone = true;
            addFile = DalbitUtil.replacePath(addFile);
        }

        pMypageNoticeAddVo.setImagePath(addFile);

        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeAddVo);
        mypageDao.callMypageNoticeAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지등록_성공.getMessageCode())) {
            if(isDone) {
                if(!DalbitUtil.isEmpty(pMypageNoticeAddVo.getImagePath())) {
                    restService.imgDone(DalbitUtil.replaceDonePath(pMypageNoticeAddVo.getImagePath()), request);
                }
            }
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
    public String callMypageNoticeEdit(P_MypageNoticeEditVo pMypageNoticeEditVo, HttpServletRequest request) throws GlobalException {
        Boolean isDone = false;
        String addFile = pMypageNoticeEditVo.getImagePath();

        if(!DalbitUtil.isEmpty(addFile) && addFile.startsWith(Code.포토_마이페이지공지_임시_PREFIX.getCode())){
            isDone = true;
            addFile = DalbitUtil.replacePath(addFile);
        }

        pMypageNoticeEditVo.setImagePath(addFile);
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeEditVo);
        mypageDao.callMypageNoticeEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지수정_성공.getMessageCode())) {
            if(isDone) {
                if(!DalbitUtil.isEmpty(pMypageNoticeEditVo.getImagePath())) {
                    restService.imgDone(DalbitUtil.replaceDonePath(pMypageNoticeEditVo.getImagePath()), request);
                }
            }
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
    public String callMypageNoticeRead(P_MypageNoticeReadVo pMypageNoticeDelVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeDelVo);
        mypageDao.callMypageNoticeRead(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지조회수_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회수_성공));
        }else if(procedureVo.getRet().equals(Status.공지조회수_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회수_회원아님));
        }else if(procedureVo.getRet().equals(Status.공지조회수_잘못된공지번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회수_잘못된공지번호));
        }else if(procedureVo.getRet().equals(Status.공지조회수_작성자_조회.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회수_작성자_조회));
        }else if(procedureVo.getRet().equals(Status.공지조회수_이미조회.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회수_이미조회));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회수_실패));
        }

        return result;
    }

    /**
     * 마이페이지 공지사항 조회
     */
    public String callMypageNoticeSelect(P_MypageNoticeSelectVo pMypagNoticeSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypagNoticeSelectVo);
        List<P_MypageNoticeSelectVo> mypageNoticeListVo = mypageDao.callMypageNoticeSelect(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(mypageNoticeListVo)){
            procedureOutputVo = null;
        }else{
            List<MypageNoticeListOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<mypageNoticeListVo.size(); i++){
                outVoList.add(new MypageNoticeListOutVo(mypageNoticeListVo.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap mypageNoticeList = new HashMap();
        mypageNoticeList.put("list", procedureOutputVo.getOutputBox());
        mypageNoticeList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, mypageNoticeList));
        } else if (procedureVo.getRet().equals(Status.공지조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음));
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
     * 방송방 방송공지 불러오기 시
     */
    public String mobileBroadcastNoticeSelect(BroadcastNoticeSelVo noticeSelVo, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        List<BroadcastNoticeListOutVo> noticeRow = null;
        Long memNo = Long.parseLong(noticeSelVo.getMemNo());
        int cnt = 0;

        paramMap.put("memNo", memNo);
        paramMap.put("roomNo", noticeSelVo.getRoomNo());
        noticeRow = mypageDao.pMemberBroadcastNoticeList(paramMap);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(noticeRow)) {
            if(DalbitUtil.isEmpty(noticeRow)) {
                resultMap.put("list", new ArrayList());
                resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
                return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
            }
        }
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(noticeSelVo.getMemNo());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for(int i = 0; i < noticeRow.size(); i++) {
            if(!DalbitUtil.isEmpty(banWord)) {
                noticeRow.get(i).setConts(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, noticeRow.get(i).getConts()));
            } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                noticeRow.get(i).setConts(DalbitUtil.replaceMaskString(systemBanWord, noticeRow.get(i).getConts()));
            }
        }

        String contents = noticeRow.get(0).getConts();
        HashMap map = new HashMap();
        map.put("contents", contents);
        map.put("noticeIdx", noticeRow.get(0).getAuto_no());

        ArrayList arrayList = new ArrayList();
        arrayList.add(map);

        resultMap.put("list", arrayList);
        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultMap));
    }

//    public String callMypageNoticeSelect(P_MypageNoticeSelectVo pMypagNoticeSelectVo) {
//        ProcedureVo procedureVo = new ProcedureVo(pMypagNoticeSelectVo);
//        List<P_MypageNoticeSelectVo> mypageNoticeListVo = mypageDao.callMypageNoticeSelect(procedureVo);
//
//        HashMap mypageNoticeList = new HashMap();
//        //mypageNoticeList.put("count", getMemberBoardCount(pMypagNoticeSelectVo));
//        if(DalbitUtil.isEmpty(mypageNoticeListVo)){
//            mypageNoticeList.put("list", new ArrayList<>());
//            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, mypageNoticeList));
//        }
//
//        List<MypageNoticeListOutVo> outVoList = new ArrayList<>();
//        BanWordVo banWordVo = new BanWordVo();
//        banWordVo.setMemNo(pMypagNoticeSelectVo.getTarget_mem_no());
//        String systemBanWord = commonService.banWordSelect();
//        String banWord = commonService.broadcastBanWordSelect(banWordVo);
//        for (int i=0; i<mypageNoticeListVo.size(); i++){
//            //사이트+방송방 금지어 조회 마이페이지 공지사항 제목, 내용 마스킹 처리
//            if(!DalbitUtil.isEmpty(banWord)){
//                mypageNoticeListVo.get(i).setTitle(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, mypageNoticeListVo.get(i).getTitle()));
//                mypageNoticeListVo.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, mypageNoticeListVo.get(i).getContents()));
//            }else if (!DalbitUtil.isEmpty(systemBanWord)){
//                mypageNoticeListVo.get(i).setTitle(DalbitUtil.replaceMaskString(systemBanWord, mypageNoticeListVo.get(i).getTitle()));
//                mypageNoticeListVo.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, mypageNoticeListVo.get(i).getContents()));
//            }
//            outVoList.add(new MypageNoticeListOutVo(mypageNoticeListVo.get(i)));
//        }
//        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
//        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
//        mypageNoticeList.put("list", procedureOutputVo.getOutputBox());
//        mypageNoticeList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
//
//        String result;
//        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, mypageNoticeList));
//        } else if (procedureVo.getRet().equals(Status.공지조회_요청회원번호_회원아님.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_요청회원번호_회원아님));
//        } else if (procedureVo.getRet().equals(Status.공지조회_대상회원번호_회원아님.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_대상회원번호_회원아님));
//        }else{
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
//        }
//        return result;
//    }

    //임시경로 room_1 에서 room_0으로 변경
    public List<ProfileFeedPhotoOutVo> replacePhotoPrefix (List<ProfileFeedPhotoOutVo> list) {
        for(ProfileFeedPhotoOutVo vo : list){
            String imgName = vo.getImg_name();
            if(!DalbitUtil.isEmpty(imgName) && imgName.startsWith(Code.포토_마이페이지공지_임시_PREFIX.getCode())){
                vo.setImg_name(DalbitUtil.replacePath(imgName));
            }
        }

        return list;
    }

    public List<MyPageFeedPictureOutVo> replacePicturePrefix(List<MyPageFeedPictureOutVo> list) {
        for(MyPageFeedPictureOutVo vo : list) {
            String imgName = vo.getImg_name();
            if(!DalbitUtil.isEmpty(imgName) && imgName.startsWith(Code.포토_마이페이지공지_임시_PREFIX.getCode())) {
                vo.setImg_name(DalbitUtil.replacePath(imgName));
            }
        }
        return list;
    }

    /**
     * 설정 방송방공지 등록
     */
    public String broadcastNoticeAdd(BroadcastNoticeAddVo noticeAddVo, HttpServletRequest request) {
       HashMap paramMap = new HashMap();
       Long reqMemNo = Long.parseLong(MemberVo.getMyMemNo(request));
       Long reqRoomNo = Long.parseLong(noticeAddVo.getRoomNo());
       paramMap.put("memNo", reqMemNo);
       paramMap.put("roomNo", reqRoomNo);
       paramMap.put("roomNoticeConts", StringUtils.equals(noticeAddVo.getRoomNoticeConts(),"") ? "default" : noticeAddVo.getRoomNoticeConts());
       int regNo = mypageDao.pMemberBroadcastNoticeIns(paramMap);

       if(regNo > 0) {
           return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
       } else if(regNo == -1) {
           return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_상단고정_초과));
       } else {
           log.error("MypageService.java - broadcastNotice Ins Failed: {}", regNo);
           return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
       }
    }

    /**
     * 설정 방송방공지 수정
     */
    public String broadcastNoticeUpd(BroadcastNoticeUpdVo noticeUpdVo, HttpServletRequest request) {
        HashMap paramMap = new HashMap();

        paramMap.put("roomNoticeNo", noticeUpdVo.getRoomNoticeNo());
        paramMap.put("memNo", MemberVo.getMyMemNo());
        paramMap.put("roomNo", noticeUpdVo.getRoomNo());
        paramMap.put("roomNoticeConts", StringUtils.equals(noticeUpdVo.getRoomNoticeConts(),"") ? "default" : noticeUpdVo.getRoomNoticeConts());
        int result = mypageDao.pMemberBroadcastNoticeUpd(paramMap);
        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_성공));
        } else {
            if(result == -1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_상단고정_초과));
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }
    }

    /**
     * 설정 방송방공지 삭제
     */
    public String broadcastNoticeDel(BroadcastNoticeDelVo noticeDelVo, HttpServletRequest request) {
        Integer deleteResult = mypageDao.pMemberBroadcastNoticeDel(noticeDelVo);
        if(deleteResult == null || deleteResult > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_성공));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
        }
    }

    /**
     * 설정 방송방공지 조회
     */
    public String broadcastNoticeSel(BroadcastNoticeSelVo noticeSelVo, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        List<BroadcastNoticeListOutVo> noticeRow = null;
        Long memNo = Long.parseLong(noticeSelVo.getMemNo());

        paramMap.put("memNo", memNo);
        paramMap.put("roomNo", noticeSelVo.getRoomNo());
        noticeRow = mypageDao.pMemberBroadcastNoticeList(paramMap);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(noticeRow)) {
            resultMap.put("list", new ArrayList());
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }

        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(noticeSelVo.getMemNo());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for(int i = 0; i < noticeRow.size(); i++) {
            if(!DalbitUtil.isEmpty(banWord)) {
                noticeRow.get(i).setConts(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, noticeRow.get(i).getConts()));
            } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                noticeRow.get(i).setConts(DalbitUtil.replaceMaskString(systemBanWord, noticeRow.get(i).getConts()));
            }
        }

        ArrayList arrList = new ArrayList();
        for(int i = 0; i < noticeRow.size(); i++) {
            BroadcastNoticeListOutVo vo = noticeRow.get(i);
            arrList.add(vo);
        }

        String notice = noticeRow.get(0).getConts();

        resultMap.put("list", arrList);
        resultMap.put("notice", notice);
        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultMap));
    }

    /**
     * 피드 등록
     */
    public String feedAdd(MyPageFeedAddVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        Long reqMemNo = Long.parseLong(MemberVo.getMyMemNo(request));

        if (param.getPhotoInfoList() != null && param.getPhotoInfoList().size() > 0)
            replacePicturePrefix(param.getPhotoInfoList());

        paramMap.put("memNo", reqMemNo);
        paramMap.put("feedContents", param.getFeedContents());
        int regNo = mypageDao.pMyPageFeedIns(paramMap);

        if(regNo > 0 && param.getPhotoInfoList() != null && param.getPhotoInfoList().size() > 0) {
            Integer resultCode = 1;
            paramMap.put("feedNo", regNo);
            paramMap.put("memNo", reqMemNo);

            for(MyPageFeedPictureOutVo pictureOutVo : param.getPhotoInfoList()) {
                paramMap.put("imgName", pictureOutVo.getImg_name());
                resultCode = mypageDao.pMyPageFeedPictureIns(paramMap);
                if(resultCode == 0 || resultCode == null) {
                    String feedNo = String.valueOf(regNo);
                    List<MyPageFeedPictureOutVo> pictureList = pictureList = mypageDao.pMyPageFeedPictureList(feedNo);
                    MyPageFeedDelVo delVo = new MyPageFeedDelVo(Integer.parseInt(feedNo), "server");
                    Integer r = feedPictureDelete(delVo, pictureList);
                    Integer feedResult = mypageDao.pMyPageFeedDel(delVo);
                    if(r == 0 || feedResult == 0 || feedResult == null) {
                        log.error("MypageService.java / feedAdd feed Ins Fail2");
                    }
                    log.error("MypageService.java / feedAdd feed Ins fail resultCode: {}", resultCode);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
                }
            }
            if(resultCode == 1) {
                try {
                    for(MyPageFeedPictureOutVo vo : param.getPhotoInfoList()) {
                        Map<String, Object> res = restService.imgDone(DalbitUtil.replaceDonePath(vo.getImg_name()), request);
                    }
                } catch (Exception e) {
                    log.error("MypageService.java / feed Ins PhotoServer request Fail: {}", e);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지이미지_경로변경_실패));
                }
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
            } else {
                log.error("MypageService.java / feed Image Ins Failed : {}", resultCode);
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
            }
        } else if(regNo > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
        } else if(regNo == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_상단고정_초과));
        } else {
            log.error("MypageService.java / feed Ins Failed : {}", regNo);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }

    }

    /**
     * 피드 조회
     */
    public String feedSelect(MyPageFeedListVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        List<Object> feedMultiRow = null;
        List<MyPageFeedOutVo> list = null;
        Long ownerMemNo = Long.parseLong(param.getMemNo()); //페이지 주인
        Long viewMemNo = Long.parseLong(MemberVo.getMyMemNo(request)); //요청자
        int cnt = 0;

        paramMap.put("memNo", ownerMemNo);
        paramMap.put("viewMemNo", viewMemNo);
        paramMap.put("pageNo", param.getPageNo());
        paramMap.put("pageCnt", param.getPageCnt());

        feedMultiRow = mypageDao.pMyPageFeedList(paramMap);
        cnt = DBUtil.getData(feedMultiRow, 0, Integer.class);
        list = DBUtil.getList(feedMultiRow, 1, MyPageFeedOutVo.class);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(feedMultiRow) || list.size() == 0) {
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }
        Long[] feedNoList = new Long[list.size()];

        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(param.getMemNo());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for(int i = 0; i < list.size(); i++) {
            feedNoList[i] = list.get(i).getReg_no();
            if(!DalbitUtil.isEmpty(banWord)) {
                list.get(i).setFeed_conts(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, list.get(i).getFeed_conts()));
            } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                list.get(i).setFeed_conts(DalbitUtil.replaceMaskString(systemBanWord, list.get(i).getFeed_conts()));
            }
            list.get(i).setProfImg(new ImageVo(list.get(i).getImage_profile(), list.get(i).getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }
        List<MyPageFeedPictureOutVo> photoList = mypageDao.pMyPageFeedPictureList(Arrays.toString(feedNoList).replace("[", "").replace("]", ""));

        ArrayList pictureList = new ArrayList();
        for(int i = 0; i < list.size(); i++) {
            MyPageFeedOutVo vo = list.get(i);
            List photoListTemp = photoListTemp = new ArrayList<>();
            for(MyPageFeedPictureOutVo pictureOutVo : photoList) {
                if(vo.getReg_no().equals(pictureOutVo.getFeed_reg_no())) {
                    pictureOutVo.setImgObj(new ImageVo(pictureOutVo.getImg_name(), list.get(i).getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                    photoListTemp.add(pictureOutVo);
                }
            }
            vo.setPhotoInfoList(photoListTemp);
            pictureList.add(vo);
        }
        resultMap.put("list", pictureList);
        resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));

        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultMap));
    }

    /**
     * 피드 수정
     */
    public String feedUpdate(MyPageFeedUpdVo param, HttpServletRequest request) {
        if(param.getPhotoInfoList() != null && param.getPhotoInfoList().size() > 0)
            replacePicturePrefix(param.getPhotoInfoList());

        HashMap paramMap = new HashMap();
        Integer oldResult = 1;
        Integer newResult = 1;
        boolean error = false;
        List<MyPageFeedPictureOutVo> photoList = null;

        photoList = mypageDao.pMyPageFeedPictureList(param.getFeedNo().toString());
        if(param.getPhotoInfoList() != null && param.getPhotoInfoList().size() > 0) {
            HashMap delParam = new HashMap();
            try {
                for(MyPageFeedPictureOutVo oldPhotoVo : photoList) {
                    delParam.put("photoNo", oldPhotoVo.getPhoto_no());
                    delParam.put("feedNo", param.getFeedNo());
                    delParam.put("imgName", oldPhotoVo.getImg_name());
                    delParam.put("delChrgrName", param.getDelChrgrName());
                    oldResult = mypageDao.pMyPageFeedPictureDel(delParam);
                    if(oldResult == 0 || oldResult == null) {
                        error = true;
                        log.error("MypageService.java / feed photo update fail oldResult : {}", oldResult);
                    }
                }
                for(MyPageFeedPictureOutVo newPhotoVo : param.getPhotoInfoList()) {
                    delParam.put("feedNo", param.getFeedNo());
                    delParam.put("memNo", MemberVo.getMyMemNo(request));
                    delParam.put("imgName", newPhotoVo.getImg_name());
                    newResult = mypageDao.pMyPageFeedPictureIns(delParam);
                    if(newResult == 0 || newResult == null) {
                        error = true;
                        log.error("MypageService.java / feed photo update fail newResult : {}", newResult);
                    }
                }
            } catch (Exception e) {
                error = true;
                log.error("MypageService.java / feed photo update Error {} delete : {}", e);
            }
        }
        if(error) {
            Integer r = feedPictureDelete(new MyPageFeedDelVo(param.getFeedNo(), "server"), photoList);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_사진작업_실패));
        } else {
            paramMap.put("feedNo", param.getFeedNo());
            paramMap.put("memNo", MemberVo.getMyMemNo(request));
            paramMap.put("feedContents", param.getFeedContents());
            int result = mypageDao.pMyPageFeedUpd(paramMap);
            if(result > 0) {
                try {
                    for(MyPageFeedPictureOutVo photoOutVo : param.getPhotoInfoList()) {
                        restService.imgDone(DalbitUtil.replaceDonePath(photoOutVo.getImg_name()), request);
                    }
                } catch (Exception e) {
                    log.error("MypageService.java / feed Upd PhotoServer request Fail : {}", e);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지이미지_경로변경_실패));
                }
                return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_성공));
            } else {
                Integer r = feedPictureDelete(new MyPageFeedDelVo(param.getFeedNo(), "server"), photoList);
                if(result == -1) {
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_상단고정_초과));
                }
                return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_실패));
            }
        }

    }

    /**
     * 피드 삭제
     */
    public String feedDelete(MyPageFeedDelVo param, HttpServletRequest request) {
        HashMap delParam = new HashMap();
        List<MyPageFeedPictureOutVo> photoList = mypageDao.pMyPageFeedPictureList(param.getFeedNo().toString());
        Integer resultCode = 1;

        for(MyPageFeedPictureOutVo pictureOutVo : photoList) {
            delParam.put("photoNo", pictureOutVo.getPhoto_no());
            delParam.put("feedNo", param.getFeedNo());
            delParam.put("imgName", pictureOutVo.getImg_name());
            delParam.put("delChrgrName", param.getDelChrgrName());
            resultCode = mypageDao.pMyPageFeedPictureDel(delParam);
            if(resultCode == 0 || resultCode == null) {
                log.error("MypageService.java / feedDelete Error {}", resultCode);
                return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
            }
        }
        Integer feedResult = mypageDao.pMyPageFeedDel(param);
        if(feedResult == null || feedResult < 1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_성공));
        }
    }

    /**
     * 피드 등록 에러시 이미지 삭제
     */
    public Integer feedPictureDelete(MyPageFeedDelVo vo, List<MyPageFeedPictureOutVo> pictureList) {
        if(pictureList == null || pictureList.size() == 0) return 0;
        HashMap delParam = new HashMap();

        Integer resultCode = 1;
        boolean error = false;
        for(MyPageFeedPictureOutVo pictureOutVo : pictureList) {
            delParam.put("photoNo", pictureOutVo.getPhoto_no());
            delParam.put("feedNo", vo.getFeedNo());
            delParam.put("imgName", pictureOutVo.getImg_name());
            delParam.put("delChrgrName", vo.getDelChrgrName());
            resultCode = mypageDao.pMyPageFeedPictureDel(delParam);

            if(resultCode == 0 || resultCode == null) {
                error = true;
                log.error("MypageService.java / feedPictureDelete Error {}", gsonUtil.toJson(delParam));
            }
        }
        return error? 0 : resultCode;
    }

    /**
     * 피드 상세 조회
     */
    public String feedDetailSelect(MyPageFeedDetailListVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        paramMap.put("feedNo", param.getFeedNo());
        paramMap.put("memNo", param.getMemNo());
        paramMap.put("viewMemNo", MemberVo.getMyMemNo(request));
        MyPageFeedOutVo resultVo = mypageDao.pMyPageFeedSel(paramMap);

        if(resultVo == null) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
        }
        resultVo.setPhotoInfoList(mypageDao.pMyPageFeedPictureList(String.valueOf(param.getFeedNo())));

        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(param.getMemNo());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        if(!DalbitUtil.isEmpty(banWord)) {
            resultVo.setFeed_conts(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, resultVo.getFeed_conts()));
        } else if(!DalbitUtil.isEmpty(systemBanWord)) {
            resultVo.setFeed_conts(DalbitUtil.replaceMaskString(systemBanWord, resultVo.getFeed_conts()));
        }
        resultVo.setProfImg(new ImageVo(resultVo.getImage_profile(), resultVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        for(MyPageFeedPictureOutVo pictureOutVo : resultVo.getPhotoInfoList()) {
            pictureOutVo.setImgObj(new ImageVo(pictureOutVo.getImg_name(), resultVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }
        if (!resultVo.equals(null)) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultVo));
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
    }

    /**
     * 피드 좋아요
     */
    public String feedLike(MyPageFeedLikeVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        paramMap.put("feedNo", param.getFeedNo());
        paramMap.put("mMemNo", param.getMMemNo());
        paramMap.put("vMemNo", param.getVMemNo());
        Integer result = mypageDao.pMyPageFeedLike(paramMap);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요));
        } else if(result == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_이미했음));
        } else {
            log.error("MypageService.java / feedLike Failed {}", result);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패));
        }
    }

    /**
     * 피드 좋아요 취소
     */
    public String feedLikeCancel(MyPageFeedLikeCancelVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        paramMap.put("feedNo", param.getFeedNo());
        paramMap.put("mMemNo", param.getMMemNo());
        paramMap.put("vMemNo", param.getVMemNo());
        Integer result = mypageDao.pMyPageFeedLikeCancel(paramMap);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_취소));
        } else if(result == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_하지않음));
        } else {
            log.error("MypageService.java / feedLikeCancel Failed {}", result);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_취소실패));
        }
    }

    /**
     * 피드 댓글 등록
     */
    public String feedReplyAdd(MyPageFeedReplyAddVo param, HttpServletRequest request) {
        Long tMemNo = Long.parseLong(MemberVo.getMyMemNo(request));
        HashMap paramMap = new HashMap();
        paramMap.put("regNo", param.getRegNo());
        paramMap.put("memNo", param.getMemNo());
        paramMap.put("tmemNo", tMemNo);
        paramMap.put("tmemConts", param.getTmemConts());
        int result = mypageDao.pMyPageFeedReplyIns(paramMap);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_성공));
        } else {
            log.error("MypageService.java / feedReplyAdd Ins Failed: {}", result);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_실패));
        }
    }

    /**
     * 피드 댓글 수정
     */
    public String feedReplyUpd(MyPageFeedReplyUpdVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        paramMap.put("tailNo", param.getTailNo());
        paramMap.put("tmemConts", param.getTmemConts());
        int result = mypageDao.pMyPageFeedReplyUpd(paramMap);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_성공));
        } else {
            log.error("MypageService.java / feedReplyUpd Failed: {}", result);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_실패));
        }
    }

    /**
     * 피드 댓글 삭제
     */
    public String feedReplyDel(MyPageFeedReplyDelVo param, HttpServletRequest request) {
        Integer deleteResult = mypageDao.pMyPageFeedReplyDel(param);
        if(deleteResult == null || deleteResult > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_성공));
        } else {
            log.error("MypageService.java / feedReplyDel Delete Failed: {}", deleteResult);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_실패));
        }
    }

    /**
     * 피드 댓글 조회
     */
    public String feedReplyList(MyPageFeedReplyListVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        List<Object> replyMultiRow = null;
        List<MyPageFeedReplyOutVo> list = null;
        int cnt = 0;

        paramMap.put("feedNo", param.getFeedNo());
        paramMap.put("pageNo", param.getPageNo());
        paramMap.put("pageCnt", param.getPageCnt());
        replyMultiRow = mypageDao.pMyPageFeedReplyList(paramMap);
        cnt = DBUtil.getData(replyMultiRow, 0, Integer.class);
        list = DBUtil.getList(replyMultiRow, 1, MyPageFeedReplyOutVo.class);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(replyMultiRow) || list.size() == 0) {
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_댓글없음, resultMap));
        }
        Long[] replyNoList = new Long[list.size()];

        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(MemberVo.getMyMemNo(request));
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for(int i = 0; i < list.size(); i++) {
            replyNoList[i] = list.get(i).getTail_no();
            if(!DalbitUtil.isEmpty(banWord)) {
                list.get(i).setTail_conts(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, list.get(i).getTail_conts()));
            } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                list.get(i).setTail_conts(DalbitUtil.replaceMaskString(systemBanWord, list.get(i).getTail_conts()));
            }
            list.get(i).setProfImg(new ImageVo(list.get(i).getTail_image_profile(), list.get(i).getTail_mem_sex(), DalbitUtil.getProperty("server.photo.url")));

        }
        ArrayList arrayList = new ArrayList();
        for(int i = 0; i < list.size(); i++) {
            MyPageFeedReplyOutVo vo = list.get(i);
            arrayList.add(vo);
        }
        resultMap.put("list", arrayList);
        return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_성공, resultMap));
    }

    /**
     * 피드 댓글 상세 조회
     */

    /**
     * 방송공지 등록
     */
    public String noticeAdd(ProfileFeedAddVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        Long reqMemNo = Long.parseLong(MemberVo.getMyMemNo(request)); //글 등록 유저

        //피드 등록 전에 이미지 경로 변경 (임시경로 -> 실제경로)
        if (param.getPhotoInfoList() != null && param.getPhotoInfoList().size() > 0)
            replacePhotoPrefix(param.getPhotoInfoList());

        //피드 등록 작업
        paramMap.put("memNo", reqMemNo);
        paramMap.put("noticeTitle", StringUtils.equals(param.getTitle(),"") ? "default" : param.getTitle());
        paramMap.put("noticeContents", param.getContents());
        paramMap.put("imgName", param.getImgName());
        paramMap.put("noticeTopFix", param.getTopFix());
        int regNo = mypageDao.pMemberFeedIns(paramMap);

        if(regNo > 0 && param.getPhotoInfoList() != null && param.getPhotoInfoList().size() > 0) { //등록할 사진 리스트가 있는 경우
            Integer resultCode = 1;

            paramMap.put("regNo", regNo);
            paramMap.put("memNo", reqMemNo);

            //피드 이미지경로 DB 등록
            for (ProfileFeedPhotoOutVo photoOutVo : param.getPhotoInfoList()) {
                paramMap.put("imgName", photoOutVo.getImg_name());
                resultCode = mypageDao.pMemberFeedPhotoIns(paramMap);
                if (resultCode == 0 || resultCode == null) {//사진 등록중 에러발생
                    //피드에 사용된 사진 리스트 조회
                    String noticeIdx = String.valueOf(regNo);
                    List<ProfileFeedPhotoOutVo> photoList = photoList = mypageDao.pMemberFeedPhotoList(noticeIdx);
                    ProfileFeedDelVo delVo = new ProfileFeedDelVo(Long.parseLong(noticeIdx), "server");
                    //사진 리스트 삭제
                    Integer r = feedPhotoDelete(delVo, photoList);
                    //게시글 삭제
                    Integer feedResult = mypageDao.pMemberFeedDel(delVo);
                    if (r == 0 || feedResult == 0 || feedResult == null) {
                        log.error("MypageService.java / noticeAdd feed Ins Fail2");
                    }
                    log.error("MypageService.java / noticeAdd feed Ins fail resultCode: {}", resultCode);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
                }
            }

            if(resultCode == 1){//피드글 DB등록 성공

                // 임시경로에서 실제경로로 변경하도록 처리 (포토서버에 요청)
                try {
                    for(ProfileFeedPhotoOutVo vo : param.getPhotoInfoList()){
                        Map<String, Object> res = restService.imgDone(DalbitUtil.replaceDonePath(vo.getImg_name()), request);
                    }
                } catch (Exception e) {
                    log.error("MypageService.java - feed Ins PhotoServer request Fail :{}", e);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지이미지_경로변경_실패));
                }

                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
            } else {    //피드 등록 실패
                log.error("MypageService.java - Profile feed Image Ins Failed :{}", resultCode);
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
            }
        } else if(regNo > 0){   //글만 등록하는 경우
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
        } else if(regNo == -1){    //피드 등록 실패
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_상단고정_초과));
        } else {    // Error
            log.error("MypageService.java - feed Ins Failed :{}", regNo);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }
    }

    /**
     * 방송공지 조회
     *
     * @Param
     * memNo 		BIGINT		-- 회원번호 (프로필 주인)
     * viewMemNo 		BIGINT		-- 회원번호 (프로필 보고있는 유저)
     * ,pageNo 		INT 		-- 페이지 번호
     * ,pageCnt 	INT		-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * Multi Rows
     * #1
     * cnt		INT		--총 수
     * #2
     * noticeIdx		BIGINT		-- 번호
     * mem_no		BIGINT		-- 회원번호
     * nickName	VARCHAR	--닉네임
     * memSex		VARCHAR	-- 성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 제목
     * contents		VARCHAR	-- 내용
     * imagePath	VARCHAR	-- 대표사진
     * topFix		BIGINT		-- 고정여부[0:미고정 ,1:고정]
     * writeDate		DATETIME	-- 수정일자
     * readCnt		BIGINT		-- 읽은수
     * replyCnt		BIGINT		-- 댓글수
     * rcv_like_cnt	BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt BIGINT		-- 취소 좋아요수
     */
    public String noticeSelect(String memNo, Integer pageNo, Integer pageCnt, HttpServletRequest request) throws GlobalException{
        HashMap paramMap = new HashMap();
        List<Object> feedMultiRow = null;
        List<ProfileFeedOutVo> list = null;
        Long ownerMemNo = Long.parseLong(memNo); //페이지 주인 memNo
        Long reqMemNo = Long.parseLong(MemberVo.getMyMemNo(request));   //요청자 memNo
        int cnt = 0;

        paramMap.put("pageNo", pageNo);
        paramMap.put("pageCnt", pageCnt);
        paramMap.put("memNo", ownerMemNo);
        paramMap.put("viewMemNo", reqMemNo);

        feedMultiRow = mypageDao.pMemberFeedList(paramMap);
        cnt = DBUtil.getData(feedMultiRow, 0, Integer.class);
        list = DBUtil.getList(feedMultiRow, 1, ProfileFeedOutVo.class);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(feedMultiRow) || list.size() == 0){
//            resultMap.put("fixList", new ArrayList());
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }
        //사진 리스트 가져오기 위한 index List
        Long[] feedNoList = new Long[list.size()];

        //금지단어 체크
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(memNo); //프로필 주인
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for (int i=0; i<list.size(); i++){
            feedNoList[i] = list.get(i).getNoticeIdx();
            //사이트+방송방 금지어 조회 마이페이지 공지사항 제목, 내용 마스킹 처리
            if(!DalbitUtil.isEmpty(banWord)){
                list.get(i).setTitle(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, list.get(i).getTitle()));
                list.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, list.get(i).getContents()));
            } else if (!DalbitUtil.isEmpty(systemBanWord)) {
                list.get(i).setTitle(DalbitUtil.replaceMaskString(systemBanWord, list.get(i).getTitle()));
                list.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, list.get(i).getContents()));
            }
            //프로필 이미지 사진 세팅
            list.get(i).setProfImg(new ImageVo(list.get(i).getImage_profile(), list.get(i).getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        }

        //사진 리스트 조회
        List<ProfileFeedPhotoOutVo> photoList = mypageDao.pMemberFeedPhotoList(Arrays.toString(feedNoList).replace("[", "").replace("]", ""));

        //피드 리스트에 사진 데이터 set, 고정 리스트 고정, 안된 리스트 분리
//        ArrayList fixedList = new ArrayList();
        ArrayList unfixedList = new ArrayList();
        for(int i=0; i< list.size(); i++) {
            ProfileFeedOutVo vo = list.get(i);
            Long noticeIdx = vo.getNoticeIdx();
            List photoListTemp = photoListTemp = new ArrayList<>();

            for (ProfileFeedPhotoOutVo photoVo : photoList) {
                if(vo.getNoticeIdx().equals(photoVo.getFeed_reg_no())){

                    //이미지 경로 객체 set
                    photoVo.setImgObj(new ImageVo(photoVo.getImg_name(), list.get(i).getMemSex(), DalbitUtil.getProperty("server.photo.url")));
                    photoListTemp.add(photoVo);
                }
            }
            vo.setPhotoInfoList(photoListTemp);

            /* 나중을 위해 나두기 */
            //고정된 글 리스트에 추가
//          if(vo.getTopFix() == 1) fixedList.add(vo);
            //고정 안된 글 리스트에 추가
            if(vo.getTopFix() == 0) unfixedList.add(vo);
            
            // 고정, 비고정 글 모두 list에 담기
//            unfixedList.add(vo);
        }

        //최종 데이터
//        resultMap.put("fixList", fixedList);
        resultMap.put("list", unfixedList);
        resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pagePerCnt")));

        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultMap));
    }

    /**
     * 방송공지 리스트(고정) 조회
     */
    public String noticeFixSelect(String memNo, Integer pageNo, Integer pageCnt, HttpServletRequest request) throws GlobalException {
        HashMap paramMap = new HashMap();
        List<Object> noticeMultiRow = null;
        List<ProfileFeedOutVo> list = null;
        Long ownerMemNo = Long.parseLong(memNo);
        Long reqMemNo = Long.parseLong(MemberVo.getMyMemNo(request));   //요청자 memNo

        int cnt = 0;

        paramMap.put("memNo", ownerMemNo);
        paramMap.put("viewMemNo", reqMemNo);
        paramMap.put("pageNo", pageNo);
        paramMap.put("pageCnt", pageCnt);
        noticeMultiRow = mypageDao.pMemberFeedFixList(paramMap);
        cnt = DBUtil.getData(noticeMultiRow, 0, Integer.class);
        list = DBUtil.getList(noticeMultiRow, 1, ProfileFeedOutVo.class);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(noticeMultiRow) || list.size() == 0) {
            resultMap.put("fixList", new ArrayList());
            resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
        }
        Long[] noticeNoList = new Long[list.size()];

        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(memNo);
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for(int i = 0; i < list.size(); i++) {
            noticeNoList[i] = list.get(i).getNoticeIdx();
            if(!DalbitUtil.isEmpty(banWord)) {
                list.get(i).setTitle(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, list.get(i).getTitle()));
                list.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, list.get(i).getContents()));
            } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                list.get(i).setTitle(DalbitUtil.replaceMaskString(systemBanWord, list.get(i).getTitle()));
                list.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, list.get(i).getContents()));
            }
            list.get(i).setProfImg(new ImageVo(list.get(i).getImage_profile(), list.get(i).getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        }
        List<ProfileFeedPhotoOutVo> photoList = mypageDao.pMemberFeedPhotoList(Arrays.toString(noticeNoList).replace("[", "").replace("]", ""));

        ArrayList fixedList = new ArrayList();
        for(int i = 0; i < list.size(); i++) {
            ProfileFeedOutVo vo = list.get(i);
            List photoListTemp = photoListTemp = new ArrayList<>();
            for(ProfileFeedPhotoOutVo photoVo : photoList) {
                if(vo.getNoticeIdx().equals(photoVo.getFeed_reg_no())) {
                    photoVo.setImgObj(new ImageVo(photoVo.getImg_name(), list.get(i).getMemSex(), DalbitUtil.getProperty("server.photo.url")));
                    photoListTemp.add(photoVo);
                }
            }
            vo.setPhotoInfoList(photoListTemp);
            if(vo.getTopFix() == 1) fixedList.add(vo);
        }
        resultMap.put("fixList", fixedList);
        resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultMap));
    }

    /**
     * 방송공지 수정
     * @Param
     * feedNo 		INT			-- 피드번호
     * ,memNo 		BIGINT			-- 회원번호
     * ,feedTitle 	VARCHAR(20)		-- 피드 등록글 제목
     * ,feedContents	VARCHAR(1024)		-- 피드 등록글 내용
     *
     * @Return
     * s_return		INT		-- # -1:상단 고정  개수 초과, 0:에러, 1: 정상
     * */
    public String noticeUpdate(ProfileFeedUpdVo vo, HttpServletRequest request) {
        //이미지 경로 변경
        if (vo.getPhotoInfoList() != null && vo.getPhotoInfoList().size() > 0)
            replacePhotoPrefix(vo.getPhotoInfoList());

        HashMap paramMap = new HashMap();
        Integer oldResult = 1;
        Integer newResult = 1;
        boolean error = false;
        List<ProfileFeedPhotoOutVo> photoList = null;

        //피드에 사용된 사진 리스트 조회
        photoList = mypageDao.pMemberFeedPhotoList(vo.getNoticeNo().toString());

        // 사진 변경 작업
        if(vo.getPhotoInfoList() != null && vo.getPhotoInfoList().size() > 0) {
            HashMap delParam = new HashMap();

            try {
                //사진 리스트 전부 삭제
                for (ProfileFeedPhotoOutVo oldPhotoVo : photoList) {
                    delParam.put("regNo", vo.getNoticeNo());
                    delParam.put("delChrgrName", vo.getChrgrName());
                    delParam.put("photoNo", oldPhotoVo.getPhoto_no());
                    delParam.put("imageName", oldPhotoVo.getImg_name());
                    oldResult = mypageDao.pMemberFeedPhotoDel(delParam);
                    if (oldResult == 0 || oldResult == null){
                        error = true;
                        log.error("MypageService.java / feed photo update fail oldResult:{}", oldResult);
                    }
                }
                //사진 리스트에 등록
                for (ProfileFeedPhotoOutVo newPhotoVo : vo.getPhotoInfoList()) {
                    delParam.put("regNo", vo.getNoticeNo());
                    delParam.put("memNo", MemberVo.getMyMemNo(request));
                    delParam.put("imgName", newPhotoVo.getImg_name());
                    newResult = mypageDao.pMemberFeedPhotoIns(delParam);

                    if (newResult == 0 || newResult == null) {
                        error = true;
                        log.error("MypageService.java / feed photo update fail newResult:{}", newResult);
                    }
                }
            } catch (Exception e) {
                error = true;
                log.error("MypageService.java / feed photo update Error {} delete : {}", e);
            }
        }
        if (error) {
            Integer r = feedPhotoDelete(new ProfileFeedDelVo(vo.getNoticeNo(), "server"), photoList);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_사진작업_실패));
        } else {
            // 사진 변경작업 성공시 수정 프로시져 호출
            paramMap.put("noticeNo", vo.getNoticeNo());
            paramMap.put("memNo", MemberVo.getMyMemNo(request));
            paramMap.put("noticeTitle", StringUtils.equals(vo.getNoticeTitle(),"") ? "default" : vo.getNoticeTitle());
            paramMap.put("noticeContents", vo.getNoticeContents());
//            paramMap.put("imgName", vo.getImgName());
            paramMap.put("noticeTopFix", vo.getNoticeTopFix());

            int result = mypageDao.pMemberFeedUpd(paramMap);
            if(result > 0) {
                //실제 이미지 경로 변경처리
                try {
                    for(ProfileFeedPhotoOutVo photoOutVo : vo.getPhotoInfoList()){
                        restService.imgDone(DalbitUtil.replaceDonePath(photoOutVo.getImg_name()), request);
                    }
                } catch (Exception e) {
                    log.error("MypageService.java - feed Upd PhotoServer request Fail :{}", e);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지이미지_경로변경_실패));
                }
                
                return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_성공));
            } else {
                Integer r = feedPhotoDelete(new ProfileFeedDelVo(vo.getNoticeNo(), "server"), photoList);
                if(result == -1) {
                    return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_상단고정_초과));
                }
                return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_실패));
            }
        }
    }

    /**
     * 방송공지 삭제
     * @Param
     * feedNo        INT		-- 피드번호
     * ,delChrgrName 	VARCHAR(40)	-- 삭제 관리자명
     * @Return
     * s_return		INT		-- #  0:에러, 1: 정상
     * */
    public String noticeDelete(ProfileFeedDelVo vo) {
        HashMap delParam = new HashMap();

        //피드에 사용된 사진 리스트 조회
        List<ProfileFeedPhotoOutVo> photoList = mypageDao.pMemberFeedPhotoList(vo.getNoticeNo().toString());

        Integer resultCode = 1;
        //사진 리스트 삭제
        for (ProfileFeedPhotoOutVo photoVo : photoList) {
            delParam.put("noticeNo", vo.getNoticeNo());
            delParam.put("delChrgrName", vo.getDelChrgrName());
            delParam.put("photoNo", photoVo.getPhoto_no());
            delParam.put("imageName", photoVo.getImg_name());
            resultCode = mypageDao.pMemberFeedPhotoDel(delParam);

            if (resultCode == 0 || resultCode == null) {
                log.error("MypageService.java / noticeDelete Error {}", gsonUtil.toJson(delParam));
                return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
            }
        }

        //피드 삭제
        Integer feedResult = mypageDao.pMemberFeedDel(vo);
        if(feedResult == null || feedResult < 1){
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_실패));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지삭제_성공));
        }
    }

    //등록, 수정중에 에러 발생시 삭제용
    public Integer feedPhotoDelete(ProfileFeedDelVo vo, List<ProfileFeedPhotoOutVo> photoList){
        if (photoList == null || photoList.size() == 0) return 0;
        HashMap delParam = new HashMap();

        Integer resultCode = 1;
        boolean error = false;
        //사진 리스트 삭제
        for (ProfileFeedPhotoOutVo photoVo : photoList) {
            delParam.put("regNo", vo.getNoticeNo());
            delParam.put("delChrgrName", vo.getDelChrgrName());
            delParam.put("photoNo", photoVo.getPhoto_no());
            delParam.put("imageName", photoVo.getImg_name());
            resultCode = mypageDao.pMemberFeedPhotoDel(delParam);

            if (resultCode == 0 || resultCode == null) {
                error = true;
                log.error("MypageService.java / noticeFeedDelete Error {}", gsonUtil.toJson(delParam));
            }
        }

        return error? 0 : resultCode;
    }

    /**
     * 방송공지 상세 조회
     *
     * @Param
     * noticeNo 		INT		-- 피드번호
     * ,memNo 		BIGINT		-- 회원번호 (프로필 주인)
     * viewMemNo 		BIGINT		-- 회원번호 (프로필 보고있는 유저)
     *
     * @Return
     * noticeIdx        BIGINT		-- 번호
     * mem_no		BIGINT		-- 회원번호
     * nickName	VARCHAR	--닉네임
     * memSex		VARCHAR	-- 성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 제목
     * contents		VARCHAR	-- 내용
     * imagePath	VARCHAR	-- 대표사진
     * topFix		BIGINT		-- 고정여부[0:미고정 ,1:고정]
     * writeDate		DATETIME	-- 수정일자
     * readCnt		BIGINT		-- 읽은수
     * replyCnt		BIGINT		-- 댓글수
     * rcv_like_cnt	BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt BIGINT		-- 취소 좋아요수
     */
    public String noticeDetailSelect(ProfileFeedDetailSelVo vo, HttpServletRequest request){
        HashMap param = new HashMap();
        param.put("noticeNo", vo.getNoticeNo());
        param.put("memNo", vo.getMemNo());
        param.put("viewMemNo", MemberVo.getMyMemNo(request));
        ProfileFeedOutVo resultVo = mypageDao.pMemberFeedSel(param);

        if(resultVo == null){
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
        }

        //사진 리스트 set
        resultVo.setPhotoInfoList(mypageDao.pMemberFeedPhotoList(String.valueOf(vo.getNoticeNo())));

        //금지단어 체크
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(vo.getMemNo()); //프로필 주인
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        //사이트+방송방 금지어 조회 마이페이지 공지사항 제목, 내용 마스킹 처리
        if(!DalbitUtil.isEmpty(banWord)){
            resultVo.setTitle(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, resultVo.getTitle()));
            resultVo.setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, resultVo.getContents()));
        } else if (!DalbitUtil.isEmpty(systemBanWord)) {
            resultVo.setTitle(DalbitUtil.replaceMaskString(systemBanWord, resultVo.getTitle()));
            resultVo.setContents(DalbitUtil.replaceMaskString(systemBanWord, resultVo.getContents()));
        }
        //프로필 이미지 사진 세팅
        resultVo.setProfImg(new ImageVo(resultVo.getImage_profile(), resultVo.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        //이미지 경로 객체 set
        for(ProfileFeedPhotoOutVo photoOutVo : resultVo.getPhotoInfoList()){
            photoOutVo.setImgObj(new ImageVo(photoOutVo.getImg_name(), resultVo.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        }

        if (!resultVo.equals(null)) {
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultVo));
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
    }

    /**
     * 방송공지 좋아요
     */
    public String noticeLike(ProfileFeedLikeVo feedLikeVo, HttpServletRequest request) {
        HashMap param = new HashMap();
        param.put("regNo", feedLikeVo.getRegNo());
        param.put("mMemNo", feedLikeVo.getMMemNo());
        param.put("vMemNo", feedLikeVo.getVMemNo());
        int result = mypageDao.pMemberFeedLikeLogIns(param);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요));
        } else if(result == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_이미했음));
        } else {
            log.error("MypageService.java - noticeLike Failed: {}", result);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패));
        }
    }

    /**
     * 방송공지 좋아요 취소
     */
    public String noticeLikeCancel(ProfileFeedLikeCancelVo feedLikeCancelVo, HttpServletRequest request) {
        HashMap param = new HashMap();
        param.put("regNo", feedLikeCancelVo.getRegNo());
        param.put("mMemNo", feedLikeCancelVo.getMMemNo());
        param.put("vMemNo", feedLikeCancelVo.getVMemNo());
        int result = mypageDao.pMemberFeedLikeCancelIns(param);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_취소));
        } else if(result == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_하지않음));
        } else {
            log.error("MypageService.java - noticeLikeCancel Failed: {}", result);
            return gsonUtil.toJson(new JsonOutputVo(Status.좋아요_취소실패));
        }
    }

    /**
     * 마이페이지 공지사항 댓글 등록
     */
    public String callMyPageNoticeReplyAdd(P_MypageNoticeReplyAddVo pMypageNoticeReplyAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeReplyAddVo);
        mypageDao.callMyPageNoticeReplyAdd(procedureVo);

        String result="";

        if (Status.공지댓글등록_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_성공));
        } else if (Status.공지댓글등록_공지회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_공지회원번호없음));
        } else if(Status.공지댓글등록_작성자회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_작성자회원번호없음));
        } else if(Status.공지댓글등록_공지번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_공지번호없음));
        } else if(Status.공지댓글등록_댓글내용없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_댓글내용없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글등록_실패));
        }

        return result;
    }

    /**
     * 마이페이지 공지사항 댓글 삭제
     */
    public String callMyPageNoticeReplyDelete(P_MypageNoticeReplyDeleteVo pMypageNoticeReplyDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeReplyDeleteVo);
        mypageDao.callMyPageNoticeReplyDelete(procedureVo);

        String result="";

        if (Status.공지댓글삭제_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_성공));
        } else if (Status.공지댓글삭제_공지회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_공지회원번호없음));
        } else if(Status.공지댓글삭제_삭제자회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_삭제자회원번호없음));
        } else if(Status.공지댓글삭제_댓글번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_댓글번호없음));
        } else if(Status.공지댓글삭제_댓글번호등록공지불일치.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_댓글번호등록공지불일치));
        } else if(Status.공지댓글삭제_이미삭제됨.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_이미삭제됨));
        } else if(Status.공지댓글삭제_삭제권한없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_삭제권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글삭제_실패));
        }

        return result;
    }

    /**
     * 마이페이지 공지사항 댓글 수정
     */
    public String callMyPageNoticeReplyEdit(P_MypageNoticeReplyEditVo pMypageNoticeReplyEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeReplyEditVo);
        mypageDao.callMyPageNoticeReplyEdit(procedureVo);

        String result;
        if (Status.공지댓글수정_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_성공));
        } else if (Status.공지댓글수정_공지회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_공지회원번호없음));
        } else if(Status.공지댓글수정_수정자회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_수정자회원번호없음));
        } else if(Status.공지댓글수정_댓글번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_댓글번호없음));
        } else if(Status.공지댓글수정_댓글번호등록공지불일치.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_댓글번호등록공지불일치));
        } else if(Status.공지댓글수정_삭제된댓글.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_삭제된댓글));
        } else if(Status.공지댓글수정_수정권한없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_수정권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글수정_실패));
        }

        return result;
    }

    /**
     * 마이페이지 공지사항 댓글 보기
     */
    public String callMyPageNoticeReplySelect(P_MypageNoticeReplyListVo pMypageNoticeReplyListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageNoticeReplyListVo);
        List<P_MypageNoticeReplyListVo> replyVoList = mypageDao.callMyPageNoticeReplySelect(procedureVo);

        HashMap replyList = new HashMap();
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        if(DalbitUtil.isEmpty(replyVoList)) {
            replyList.put("list", new ArrayList<>());
            replyList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), pMypageNoticeReplyListVo.getPageNo(), pMypageNoticeReplyListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_댓글없음, replyList));
        }

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            List<MypageNoticeReplyListOutVo> outVoList = new ArrayList<>();
            BanWordVo banWordVo = new BanWordVo();
            banWordVo.setMemNo(pMypageNoticeReplyListVo.getStar_mem_no());
            String systemBanWord = commonService.banWordSelect();
            String banWord = commonService.broadcastBanWordSelect(banWordVo);
            for(int i=0; i<replyVoList.size(); i++) {
                //사이트+방송방 금지어 조회 마이페이지 공지 댓글 내용 마스킹 처리
                if(!DalbitUtil.isEmpty(banWord)) {
                    replyVoList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, replyVoList.get(i).getContents()));
                } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                    replyVoList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, replyVoList.get(i).getContents()));
                }
                outVoList.add(new MypageNoticeReplyListOutVo(replyVoList.get(i)));
            }
            replyList.put("list", outVoList);
            replyList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_성공, replyList));
        } else if(Status.공지댓글보기_회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_회원번호없음));
        } else if(Status.공지댓글보기_공지회원번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_회원번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지댓글보기_실패));
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
            pMemberBroadcastingCheckVo.setMem_no(MemberVo.getMyMemNo(request));
            ProcedureVo procedureCheckVo = new ProcedureVo(pMemberBroadcastingCheckVo);
            roomDao.callMemberBroadcastingCheck(procedureCheckVo);
            if(Status.방송진행여부체크_방송중.getMessageCode().equals(procedureCheckVo.getRet())) {
                HashMap resultCheckMap = new Gson().fromJson(procedureCheckVo.getExt(), HashMap.class);
                try{
                    socketService.banWord(DalbitUtil.getStringMap(resultCheckMap, "roomNo"), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
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
    public String callMypageBlackListAdd(P_MypageBlackAddVo pMypageBlackAddVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMypageBlackAddVo);
        mypageDao.callMypageBlackListAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.블랙리스트등록_성공.getMessageCode())) {

            //메시지 체크
            SocketVo vo = socketService.getSocketVo(DalbitUtil.getProperty("socket.global.room"), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            socketService.chatBlack(DalbitUtil.getProperty("socket.global.room"), pMypageBlackAddVo.getMem_no(), pMypageBlackAddVo.getBlack_mem_no(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);

            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_성공));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_블랙회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_블랙회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_이미블랙등록.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_이미블랙등록));
        }else if(procedureVo.getRet().equals(Status.블랙리스트등록_본인등록안됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.블랙리스트등록_본인등록안됨));
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
        changeItemList.put("byeolCnt", mypageDao.selectMyByeolCnt(pChangeItemVo.getMem_no()));
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
        }else if(Status.이전작업대기중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이전작업대기중));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_교환하기_실패));
        }

        return result;

    }

    /**
     * 별 달 자동교환 설정 변경
     */
    public String callAutoChangeSettingEdit(P_AutoChangeSettingEditVo pAutoChangeSettingEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pAutoChangeSettingEditVo);
        mypageDao.callAutoChangeSettingEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.별_달_자동교환설정변경_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("autoChange", DalbitUtil.getIntMap(resultMap, "auto_change"));
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_자동교환설정변경_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.별_달_자동교환설정변경_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_자동교환설정변경_요청회원번호_회원아님));
        } else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_자동교환설정변경_실패));
        }

        return result;
    }

    /**
     * 별 달 자동교환 설정 가져오기
     */
    public String callAutoChangeSettingSelect(P_AutoChangeSettingSelectVo pAutoChangeSettingSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pAutoChangeSettingSelectVo);
        mypageDao.callAutoChangeSettingSelect(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.별_달_자동교환설정조회_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("autoChange", DalbitUtil.getIntMap(resultMap, "auto_change"));
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_자동교환설정조회_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.별_달_자동교환설정조회_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_자동교환설정조회_요청회원번호_회원아님));
        } else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.별_달_자동교환설정조회_실패));
        }

        return result;
    }


    /**
     * 회원 알림 내용 읽음처리
     */
    public String callReadNotification(P_ReadNotificationVo pReadNotificationVo){
        ProcedureVo procedureVo = new ProcedureVo(pReadNotificationVo);
        mypageDao.callReadNotification(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.알림읽음_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림읽음_성공));
        }else if(procedureVo.getRet().equals(Status.알림읽음_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림읽음_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림읽음_실패));
        }
        return result;
    }

    /**
     *  스페셜 DJ 신청 가능상태 확인
     */
    public String callSpecialDjStatus(HttpServletRequest request, SpecialDjRegManageVo specialDjRegManageVo){
        String mem_no = MemberVo.getMyMemNo(request);

        if(!DalbitUtil.isLogin(request)) {

            SpecialDjRegManageVo getSpecialDjRegManageVo = mypageDao.selectSpecialDjReqManage(specialDjRegManageVo);
            var resultMap = new HashMap();

            //컨텐츠 조회
            /*List<SpecialDjContentVo> contentList = mypageDao.selectSpecialDjReqContent(specialDjRegManageVo);
            getSpecialDjRegManageVo.setContentList(contentList);*/
            resultMap.put("eventInfo", getSpecialDjRegManageVo);

            return gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ_회원아님, resultMap));

        }else if(DalbitUtil.isEmpty(specialDjRegManageVo.getSelect_year()) || DalbitUtil.isEmpty(specialDjRegManageVo.getSelect_month())){
            return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류));
        }

        //이미 신청여부 확인
        HashMap userCheckMap = mypageDao.selectExistsSpecialReq(mem_no);
        int already = DalbitUtil.getIntMap(userCheckMap, "is_already") == 0 ? 0 : 1;
        var userInfo = new HashMap();
        userInfo.put("mem_nick", DalbitUtil.getStringMap(userCheckMap, "mem_nick"));

        int cur_specialdj_badge = DalbitUtil.getIntMap(userCheckMap, "specialdj_badge");
        int cur_specialdj_cnt = DalbitUtil.getIntMap(userCheckMap, "specialdj_cnt");
        int is_best = 0;
        if(0 < cur_specialdj_badge && 5 < cur_specialdj_cnt){
            is_best = 1;
        }
        userInfo.put("is_best", is_best);

        var specialDjCondition = new SpecialDjConditionVo();
        ArrayList<HashMap<String,String>> conditionList = new ArrayList<>();

        //스페셜DJ 조건 확인 #7794 스타DJ용으로 변경됨
        SpecialDjRegManageVo getSpecialDjRegManageVo = mypageDao.selectSpecialDjReqManage(specialDjRegManageVo);
        if(DalbitUtil.isEmpty(getSpecialDjRegManageVo)){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_없음_종료));
        }

        SpecialDjConditionSearchVo specialDjConditionSearchVo = new SpecialDjConditionSearchVo();
        specialDjConditionSearchVo.setMem_no(mem_no);
        specialDjConditionSearchVo.setCondition_start_date(getSpecialDjRegManageVo.getCondition_start_date());
        specialDjConditionSearchVo.setCondition_end_date(getSpecialDjRegManageVo.getCondition_end_date());

//        if(is_best == 0){
            //1번 조건 체크
            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getCondition_code1());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getCondition_data1());
            HashMap conditionData1 = checkSpecialDjCondition(specialDjConditionSearchVo);

            //2번 조건 체크
            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getCondition_code2());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getCondition_data2());
            HashMap conditionData2 = checkSpecialDjCondition(specialDjConditionSearchVo);

            //3번 조건 체크
            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getCondition_code3());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getCondition_data3());
            HashMap conditionData3 = checkSpecialDjCondition(specialDjConditionSearchVo);

            //4번 조건 체크
            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getCondition_code4());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getCondition_data4());
            HashMap conditionData4 = checkSpecialDjCondition(specialDjConditionSearchVo);

            conditionList.add(conditionData1);
            conditionList.add(conditionData2);
            conditionList.add(conditionData3);
            conditionList.add(conditionData4);
        /*}else{
            //Best dj 조건 체크
            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getBest_code1());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getBest_data1());
            HashMap bestConditionData1 = checkSpecialDjCondition(specialDjConditionSearchVo);
            conditionList.add(bestConditionData1);

            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getBest_code2());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getBest_data2());
            HashMap bestConditionData2 = checkSpecialDjCondition(specialDjConditionSearchVo);
            conditionList.add(bestConditionData2);

            specialDjConditionSearchVo.setCondition_code(getSpecialDjRegManageVo.getBest_code3());
            specialDjConditionSearchVo.setCondition_data(getSpecialDjRegManageVo.getBest_data3());
            HashMap bestConditionData3 = checkSpecialDjCondition(specialDjConditionSearchVo);
            conditionList.add(bestConditionData3);
        }*/

        specialDjCondition.setConditionList(conditionList);
        specialDjCondition.setAlready(already);

        long nowDateTime = Long.valueOf(DalbitUtil.getUTCFormat(new Date()));
        long reqStartDate = Long.valueOf(getSpecialDjRegManageVo.getReq_start_date());
        long reqEndDate = Long.valueOf(getSpecialDjRegManageVo.getReq_end_date());

        int timeState = (reqStartDate <= nowDateTime && nowDateTime <= reqEndDate) ? 1 : 0;
        specialDjCondition.setTimeState(timeState);

        var resultMap = new HashMap();
        resultMap.put("userInfo", userInfo);
        resultMap.put("specialDjCondition", specialDjCondition);
        //컨텐츠 조회
        /*List<SpecialDjContentVo> contentList = mypageDao.selectSpecialDjReqContent(specialDjRegManageVo);
        getSpecialDjRegManageVo.setContentList(contentList);*/
        resultMap.put("eventInfo", getSpecialDjRegManageVo);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }

    public HashMap checkSpecialDjCondition(SpecialDjConditionSearchVo specialDjConditionSearchVo){

        int code = specialDjConditionSearchVo.getCondition_code();
        int value = specialDjConditionSearchVo.getCondition_data();

        var resultMap = new HashMap();
        /**
         * code 정리
         * [tbl_code_define : special_dj_condition]
         * 1 : 누적 방송시간
         * 2 : 90분 이상 방송
         * 3 : 좋아요
         * 4 : 레벨
         * 5 : 팬 수
         * 6 : 누적 청취자 수
         * 7 : 받은 별 수
         * 8 : 누적 방송시간(팬 방송 제외)
         * 9 : 누적 방송일 수
         * 10 : 신규 팬 등록 수
         */

        long broadcastAirtime;
        switch (code) {
            case 1:
                //누적방송시간 체크(기간)
                broadcastAirtime = mypageDao.selectSpecialDjBroadcastTime(specialDjConditionSearchVo);
                resultMap.put("condition", broadcastAirtime < value ? 0 : 1);
                resultMap.put("title", "누적방송시간");
                resultMap.put("value", "최소 " + value + "시간 방송");
                break;
            case 8:
                //누적방송시간(팬 방송 제외) 체크(기간)
                broadcastAirtime = mypageDao.selectSpecialDjBroadcastTime(specialDjConditionSearchVo);
                resultMap.put("condition", broadcastAirtime < value ? 0 : 1);
                resultMap.put("title", "누적방송시간");
                resultMap.put("subtitle", "(팬 방송 제외)");
                resultMap.put("value", "최소 " + value + "시간 방송");
                resultMap.put("point", broadcastAirtime);
                break;
            case 2:
                //90분 이상 방송(기간)
                int broadcastCnt = mypageDao.selectSpecialDjBroadcastCnt(specialDjConditionSearchVo);
                resultMap.put("condition", broadcastCnt < value ? 0 : 1);
                resultMap.put("title", "90분 이상 방송");
                resultMap.put("value", "최소 " + value + "회 이상");
                break;
            case 3:
                //좋아요 갯수 체크(기간)
                int djLikeCnt = mypageDao.selectSpecialDjLikeCnt(specialDjConditionSearchVo);
                resultMap.put("condition", djLikeCnt < value ? 0 : 1);
                resultMap.put("title", "받은 좋아요");
                resultMap.put("value", "최소 " + value + "개 이상");
                resultMap.put("point", djLikeCnt);
                break;
            case 4:
                //레벨 체크(현재 상태)
                MemberInfoVo memberInfoVo = mypageDao.selectMemberLevel(specialDjConditionSearchVo.getMem_no());
                resultMap.put("condition", memberInfoVo.getLevel() < value ? 0 : 1);
                resultMap.put("title", "현재 레벨");
                resultMap.put("value", "최소 " + value + "렙 이상");
                break;
            case 5:
                //팬 수 체크(현재 상태)
                int fanCnt = mypageDao.selectMemberFanCnt(specialDjConditionSearchVo.getMem_no());
                resultMap.put("condition", fanCnt < value ? 0 : 1);
                resultMap.put("title", "현재 팬 수");
                resultMap.put("value", "최소 " + value + "명 이상");
                break;
            case 6:
                //청취자 수
                int listenCnt = mypageDao.selectListenerCnt(specialDjConditionSearchVo);
                resultMap.put("condition", listenCnt < value ? 0 : 1);
                resultMap.put("title", "누적 청취자 수");
                resultMap.put("value", "최소 " + value + "명 이상");
                resultMap.put("point", listenCnt);
                break;
            case 7:
                //누적 받은 별
                int listenCnt7 = mypageDao.selectStarCnt(specialDjConditionSearchVo);
                resultMap.put("condition", listenCnt7 < value ? 0 : 1);
                resultMap.put("title", "누적 받은 별");
                resultMap.put("value", "최소 " + value + "개 이상");
                resultMap.put("point", listenCnt7);
                break;
//            case 8:
            //누적 방송시간(팬 방송 제외)
//                long broadcastAirtime = mypageDao.selectSpecialDjBroadcastTime(specialDjConditionSearchVo);
//                resultMap.put("condition", broadcastAirtime < value ? 0 : 1);
//                resultMap.put("title", "누적방송시간");
//                resultMap.put("value", "최소 " + value + "시간 방송");
//                break;
            case 9:
                //누적 방송일 수
                int listenCnt9 = mypageDao.selectBroadDateCnt(specialDjConditionSearchVo);
                resultMap.put("condition", listenCnt9 < value ? 0 : 1);
                resultMap.put("title", "누적 방송일 수");
                resultMap.put("value", "최소 " + value + "일 이상");
                break;
            case 10:
                //신규 팬 등록 수
                int listenCnt10 = mypageDao.selectNewFanCnt(specialDjConditionSearchVo);
                resultMap.put("condition", listenCnt10 < value ? 0 : 1);
                resultMap.put("title", "신규 팬 등록 수");
                resultMap.put("value", "최소 " + value + "명 이상");
                break;
        }

        return resultMap;
    }

    /**
     *  스페셜 DJ 신청
     */
    public String callSpecialDjReq(P_SpecialDjReq pSpecialDjReq, HttpServletRequest request){
        String result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ_신청실패));
        pSpecialDjReq.setMem_no(MemberVo.getMyMemNo(request));

        //스페셜DJ 조건 확인
        var specialDjRegManageVo = new SpecialDjRegManageVo();
        specialDjRegManageVo.setSelect_year(pSpecialDjReq.getSelect_year());
        specialDjRegManageVo.setSelect_month(pSpecialDjReq.getSelect_month());
        SpecialDjRegManageVo getSpecialDjRegManageVo = mypageDao.selectSpecialDjReqManage(specialDjRegManageVo);
        //getSpecialDjRegManageVo.setMem_no(pSpecialDjReq.getMem_no());

        //시간 체크
        long nowDateTime = Long.valueOf(DalbitUtil.getUTCFormat(new Date()));
        long reqStartDate = Long.valueOf(getSpecialDjRegManageVo.getReq_start_date());
        long reqEndDate = Long.valueOf(getSpecialDjRegManageVo.getReq_end_date());

        var userCheckMemNo = mypageDao.selectExistsSpecialReq(pSpecialDjReq.getMem_no());
        var userCheckPhoneNo = mypageDao.selectExistsPhoneSpecialReq(pSpecialDjReq.getPhone());

        if(!DalbitUtil.isLogin(request)) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ_회원아님));
        }else if(nowDateTime < reqStartDate || reqEndDate < nowDateTime){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ_기간아님));
        }else if(0 < DalbitUtil.getIntMap(userCheckMemNo, "is_already") || 0 < DalbitUtil.getIntMap(userCheckPhoneNo, "is_already")){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ_이미신청));
        }else{
            try{
                mypageDao.insertSpecialReq(pSpecialDjReq);
                result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ_신청성공));
            }catch(Exception e){
                log.info("스페셜 DJ DB 오류 {}", e.getMessage());
            }
        }

        return result;
    }

    /**
     * 레벨정보 노출
     */
    public String selectLevel() {
        List<HashMap> list = new ArrayList<>();
        List<LevelVo> levels = mypageDao.selectLevel();

        if(!DalbitUtil.isEmpty(levels)){
            String frame = DalbitUtil.getProperty("level.frame");
            for(int i = 0; i < 5; i++){
                int srt = (i * 10) + 1;
                int end = (i + 1) * 10;

                List<LevelVo> levelTmp = new ArrayList<>();
                String[] frameColor = null;
                if(srt == 1) {
                    frameColor = new String[1];
                    frameColor[0] = "#faa118";
                }else if(srt == 11){
                    frameColor = new String[1];
                    frameColor[0] = "#5dc62a";
                }else if(srt == 21){
                    frameColor = new String[1];
                    frameColor[0] = "#4692e9";
                }else if(srt == 31){
                    frameColor = new String[1];
                    frameColor[0] = "#f54640";
                }else if(srt == 41){
                    frameColor = new String[3];
                    frameColor[0] = "#7f18ff";
                    frameColor[1] = "#f52d5b";
                    frameColor[2] = "#ffbf03";
                }
                for(LevelVo level : levels){
                    if(srt <= level.getLevel() && level.getLevel() <= end){
                        level.setFrame(StringUtils.replace(frame, "[level]", "" + level.getLevel()));
                        levelTmp.add(level);
                    }
                }

                HashMap level = new HashMap();
                level.put("level", "Lv " + srt + " ~ " + end);
                level.put("color", frameColor);
                level.put("levels", levelTmp);
                list.add(level);

            }
        }

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, list));
    }

    /**
     * 회원 알림설정 조회하기
     */
    public String callNewAlarm(P_MemberNotifyVo pMemberNotifyVo) {
        int newCnt = 0;
        if (DalbitUtil.isLogin(pMemberNotifyVo.getMem_no())){
            newCnt = mypageDao.callNewAlarm(pMemberNotifyVo);
        }
        HashMap returnMap = new HashMap();
        returnMap.put("newCnt", newCnt);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, returnMap));
    }


    /**
     * 회원 방송방 빠른말 추가
     */
    public String memberShortCutAdd(P_MemberShortCutAddVo pMemberShortCutAddVo, HttpServletRequest request) {

        ProcedureVo procedureVo = new ProcedureVo(pMemberShortCutAddVo);
        mypageDao.memberShortCutAdd(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.회원방송방빠른말추가_성공.getMessageCode())) {

            P_MemberShortCutVo apiData = new P_MemberShortCutVo();
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            result = callMemberShortCut(apiData, "add", request);
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말추가_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말추가_회원아님));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말추가_제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말추가_제한));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말추가_달부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말추가_달부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말추가_오류));
        }
        return result;
    }


    /**
     * 회원 방송방 빠른말 연장
     */
    public String memberShortCutExtend(P_MemberShortCutExtendVo pMemberShortCutRenewVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberShortCutRenewVo);
        mypageDao.memberShortCutExtend(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.회원방송방빠른말연장_성공.getMessageCode())) {

            P_MemberShortCutVo apiData = new P_MemberShortCutVo();
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            result = callMemberShortCut(apiData, "extend", request);

        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말연장_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_회원아님));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말연장_불가번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_불가번호));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말연장_번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_번호없음));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말연장_사용중인번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_사용중인번호));
        } else if (procedureVo.getRet().equals(Status.회원방송방빠른말연장_달부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_달부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원방송방빠른말연장_오류));
        }
        return result;
    }


    /**
     * 메시지 사용 클릭 업데이트
     */
    public String msgClickUpdate(P_MsgClickUpdateVo pMsgClickUpdateVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMsgClickUpdateVo);
        mypageDao.msgClickUpdate(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.메시지클릭업데이트_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메시지클릭업데이트_성공));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메시지클릭업데이트_오류));
        }
        return result;
    }


    /**
     * 회원 방송방 이모티콘 가져오기
     */
    public String callMemberEmoticon(P_EmoticonListVo pEmoticonVo) {

        ProcedureVo procedureVo = new ProcedureVo(pEmoticonVo);
        List<P_EmoticonListVo> emoticonListVo = mypageDao.callMemberEmoticon(procedureVo);

        //카테고리 조회
        List<EmoticonCategoryListVo> categoryListVo = mypageDao.selectEmoticonCategory();

       HashMap emoticonList = new HashMap();
        if(DalbitUtil.isEmpty(emoticonListVo)){
            emoticonList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.이모티콘조회_없음, emoticonList));
        }

        List<EmoticonListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<emoticonListVo.size(); i++){
            outVoList.add(new EmoticonListOutVo(emoticonListVo.get(i)));
        }

        List<EmoticonCategoryListOutVo> outCategoryVoList = new ArrayList<>();
        for (int i=0; i<categoryListVo.size(); i++){
            outCategoryVoList.add(new EmoticonCategoryListOutVo(categoryListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        emoticonList.put("categoryList", outCategoryVoList);
        emoticonList.put("list", procedureOutputVo.getOutputBox());


        String result;
        if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이모티콘조회_성공, emoticonList));
        } else if (procedureVo.getRet().equals(Status.이모티콘조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이모티콘조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.이모티콘조회_실패));
        }
        return result;
    }

    /**
     * 회원 알림 내용 삭제
     */
    public String callMemberNotificationDelete(P_NotificationDeleteVo pNotificationDeleteVo) {

        ProcedureVo procedureVo = new ProcedureVo(pNotificationDeleteVo);

        mypageDao.callMemberNotificationDelete(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.회원알림삭제_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림삭제_성공));
        }else if(procedureVo.getRet().equals(Status.회원알림내용삭제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용삭제_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원알림내용삭제_실패));
        }

        return result;
    }

    public String getMyPageNew(HttpServletRequest request){
        HashMap returnMap = new HashMap();
        if(DalbitUtil.isLogin(request)){
            String notice = request.getParameter("notice");
            String qna = request.getParameter("qna");
            if(!DalbitUtil.isEmpty(notice)){
                String[] nos = notice.split(",");
                List<Long> tmp = new ArrayList<>();

                for(String n : nos){
                    if(!tmp.contains(Long.valueOf(n))){
                        tmp.add(Long.valueOf(n));
                    }
                }
                if(tmp.size() > 50){
                    tmp.sort(null);
                    notice = StringUtils.join(tmp.subList(tmp.size() - 50, tmp.size()), ",");
                }else{
                    notice = StringUtils.join(tmp, ",");
                }
            }
            if(!DalbitUtil.isEmpty(qna)){
                String[] nos = qna.split(",");
                List<Long> tmp = new ArrayList<>();
                for(String n : nos){
                    if(!tmp.contains(Long.valueOf(n))){
                        tmp.add(Long.valueOf(n));
                    }
                }
                if(tmp.size() > 50){
                    tmp.sort(null);
                    qna = StringUtils.join(tmp.subList(tmp.size() - 50, tmp.size()), ",");
                }else{
                    qna = StringUtils.join(tmp, ",");
                }
            }

            DeviceVo deviceVo = new DeviceVo(request);
            HashMap params = new HashMap();
            params.put("myMemNo", MemberVo.getMyMemNo(request));
            params.put("targetMemNo", DalbitUtil.isEmpty(request.getParameter("targetMemNo")) ? MemberVo.getMyMemNo(request) : request.getParameter("targetMemNo"));
            params.put("fanBoard", request.getParameter("fanBoard"));
            params.put("dal", request.getParameter("dal"));
            params.put("byeol", request.getParameter("byeol"));
            params.put("notice", notice);
            params.put("qna", qna);
            params.put("os", deviceVo.getOs());

            returnMap = mypageDao.selectMyPageNew(params);
            returnMap.put("newCnt", DalbitUtil.getIntMap(returnMap, "alarm") + DalbitUtil.getIntMap(returnMap, "qna") + DalbitUtil.getIntMap(returnMap, "notice"));
            returnMap.put("moveUrl", "");
            if(DalbitUtil.getIntMap(returnMap, "alarm") > 0) {
                returnMap.put("moveUrl", "");
            }else if(DalbitUtil.getIntMap(returnMap, "notice") > 0){
                returnMap.put("moveUrl", "/customer/notice");
            }else if(DalbitUtil.getIntMap(returnMap, "qna") > 0) {
                returnMap.put("moveUrl", "/customer/qnaList");
            }
        }else{
            returnMap.put("newCnt", 0);
        }

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, returnMap));
    }

    public String getMyPageNewFanBoard(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, mypageDao.selectMyPageFanBoard(MemberVo.getMyMemNo(request))));
    }

    public String getMyPageNewWallet(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, mypageDao.selectMyPageWallet(MemberVo.getMyMemNo(request))));
    }

    /**
     *  방송설정 제목 추가
     */
    public String callBroadcastTitleAdd(P_BroadcastTitleAddVo pBroadcastTitleAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastTitleAddVo);
        mypageDao.callBroadcastTitleAdd(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.방송설정_제목추가_성공.getMessageCode())) {

            P_BroadcastOptionListVo pBroadcastTitleListVo = new P_BroadcastOptionListVo();
            pBroadcastTitleListVo.setMem_no(pBroadcastTitleAddVo.getMem_no());
            result = callBroadcastTitleSelect(pBroadcastTitleListVo, "titleAdd");

            //result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목추가_성공));
        } else if (procedureVo.getRet().equals(Status.방송설정_제목추가_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목추가_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송설정_제목추가_제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목추가_제한));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목추가_실패));
        }
        return result;
    }


    /**
     *  방송설정 제목 수정
     */
    public String callBroadcastTitleEdit(P_BroadcastTitleEditVo pBroadcastTitleEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastTitleEditVo);
        mypageDao.callBroadcastTitleEdit(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.방송설정_제목수정_성공.getMessageCode())) {

            P_BroadcastOptionListVo pBroadcastTitleListVo = new P_BroadcastOptionListVo();
            pBroadcastTitleListVo.setMem_no(pBroadcastTitleEditVo.getMem_no());
            result = callBroadcastTitleSelect(pBroadcastTitleListVo, "titleEdit");

            //result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목수정_성공));
        } else if (procedureVo.getRet().equals(Status.방송설정_제목수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목수정_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송설정_제목수정_번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목수정_번호없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목수정_오류));
        }
        return result;
    }


    /**
     *  방송설정 제목 조회
     */
    public String callBroadcastTitleSelect(P_BroadcastOptionListVo pBroadcastTitleListVo, String returnCode) {

        ProcedureVo procedureVo = new ProcedureVo(pBroadcastTitleListVo);
        List<P_BroadcastOptionListVo> broadcastTitleListVoList = mypageDao.callBroadcastTitleSelect(procedureVo);

        String result;
        HashMap broadcastTitleList = new HashMap();
        if(DalbitUtil.isEmpty(broadcastTitleListVoList)){
            broadcastTitleList.put("list", new ArrayList<>());
            if("roomCreate".equals(returnCode)){
                return Status.방송설정_제목조회_없음.getMessageCode();
            }
            if(returnCode.equals("titleDelete")){
                return gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목삭제_성공, broadcastTitleList));
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목조회_없음, broadcastTitleList));
        }

        List<BroadcastOptionListOutVo> outVoList = new ArrayList<>();
        if(!DalbitUtil.isEmpty(broadcastTitleListVoList)){
            for (int i = 0; i < broadcastTitleListVoList.size(); i++) {
                outVoList.add(new BroadcastOptionListOutVo(broadcastTitleListVoList.get(i)));
            }
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        broadcastTitleList.put("list", procedureOutputVo.getOutputBox());

        if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            if(returnCode.equals("titleAdd")){
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목추가_성공, broadcastTitleList));
            }else if(returnCode.equals("titleEdit")){
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목수정_성공, broadcastTitleList));
            }else if(returnCode.equals("titleDelete")){
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목삭제_성공, broadcastTitleList));
            }else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목조회_성공, broadcastTitleList));
            }
        } else if (procedureVo.getRet().equals(Status.방송설정_제목조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목조회_실패));
        }
        return result;
    }


    /**
     *  방송설정 제목 삭제
     */
    public String callBroadcastTitleDelete(P_BroadcastTitleDeleteVo pBroadcastTitleDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastTitleDeleteVo);
        mypageDao.callBroadcastTitleDelete(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.방송설정_제목삭제_성공.getMessageCode())) {
            P_BroadcastOptionListVo pBroadcastTitleListVo = new P_BroadcastOptionListVo();
            pBroadcastTitleListVo.setMem_no(pBroadcastTitleDeleteVo.getMem_no());
            result = callBroadcastTitleSelect(pBroadcastTitleListVo, "titleDelete");
            //result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목삭제_성공));
        } else if (procedureVo.getRet().equals(Status.방송설정_제목삭제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목삭제_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송설정_제목삭제_번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목삭제_번호없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_제목삭제_실패));
        }
        return result;
    }


    /**
     *  방송설정 인사말 추가
     */
    public String callBroadcastWelcomeMsgAdd(P_BroadcastWelcomeMsgAddVo pBroadcastWelcomeMsgAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastWelcomeMsgAddVo);
        mypageDao.callBroadcastWelcomeMsgAdd(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.방송설정_인사말추가_성공.getMessageCode())) {
            P_BroadcastWelcomeMsgListVo pBroadcastWelcomeMsgListVo = new P_BroadcastWelcomeMsgListVo();
            pBroadcastWelcomeMsgListVo.setMem_no(pBroadcastWelcomeMsgAddVo.getMem_no());
            result = callBroadcastWelcomeMsgSelect(pBroadcastWelcomeMsgListVo,"msgAdd");

            //result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말추가_성공));
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말추가_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말추가_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말추가_제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말추가_제한));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말추가_실패));
        }
        return result;
    }


    /**
     *  방송설정 인사말 수정
     */
    public String callBroadcastWelcomeMsgEdit(P_BroadcastWelcomeMsgEditVo pBroadcastWelcomeMsgEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastWelcomeMsgEditVo);
        mypageDao.callBroadcastWelcomeMsgEdit(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.방송설정_인사말수정_성공.getMessageCode())) {

            P_BroadcastWelcomeMsgListVo pBroadcastWelcomeMsgListVo = new P_BroadcastWelcomeMsgListVo();
            pBroadcastWelcomeMsgListVo.setMem_no(pBroadcastWelcomeMsgEditVo.getMem_no());
            result = callBroadcastWelcomeMsgSelect(pBroadcastWelcomeMsgListVo,"msgEdit");

            //result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말수정_성공));
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말수정_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말수정_번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말수정_번호없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말수정_오류));
        }
        return result;
    }


    /**
     * 방송설정 인사말 조회
     */
    public String callBroadcastWelcomeMsgSelect(P_BroadcastWelcomeMsgListVo pBroadcastWelcomeMsgListVo, String returnCode) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastWelcomeMsgListVo);
        List<P_BroadcastOptionListVo> broadcastWelcomeMsgListVoList = mypageDao.callBroadcastWelcomeMsgSelect(procedureVo);

        String result;
        HashMap broadcastWelcomeMsgList = new HashMap();
        if(DalbitUtil.isEmpty(broadcastWelcomeMsgListVoList)){
            broadcastWelcomeMsgList.put("list", new ArrayList<>());
            if("roomCreate".equals(returnCode)){
                return Status.방송설정_인사말조회_없음.getMessageCode();
            }

            if(returnCode.equals("msgDelete")){
                return gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말삭제_성공, broadcastWelcomeMsgList));
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말조회_없음, broadcastWelcomeMsgList));
        }

        List<BroadcastOptionListOutVo> outVoList = new ArrayList<>();
        if(!DalbitUtil.isEmpty(broadcastWelcomeMsgListVoList)){
            for (int i = 0; i < broadcastWelcomeMsgListVoList.size(); i++) {
                outVoList.add(new BroadcastOptionListOutVo(broadcastWelcomeMsgListVoList.get(i)));
            }
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        broadcastWelcomeMsgList.put("list", procedureOutputVo.getOutputBox());

        if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            if(returnCode.equals("msgAdd")){
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말추가_성공, broadcastWelcomeMsgList));
            }else if(returnCode.equals("msgEdit")){
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말수정_성공, broadcastWelcomeMsgList));
            }else if(returnCode.equals("msgDelete")){
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말삭제_성공, broadcastWelcomeMsgList));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말조회_성공, broadcastWelcomeMsgList));
            }
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말조회_실패));
        }
        return result;
    }


    /**
     *  방송설정 인사말 삭제
     */
    public String callBroadcastWelcomeMsgDelete(P_BroadcastWelcomeMsgDeleteVo pBroadcastWelcomeMsgDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadcastWelcomeMsgDeleteVo);
        mypageDao.callBroadcastWelcomeMsgDelete(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.방송설정_인사말삭제_성공.getMessageCode())) {
            P_BroadcastWelcomeMsgListVo pBroadcastWelcomeMsgListVo = new P_BroadcastWelcomeMsgListVo();
            pBroadcastWelcomeMsgListVo.setMem_no(pBroadcastWelcomeMsgDeleteVo.getMem_no());
            result = callBroadcastWelcomeMsgSelect(pBroadcastWelcomeMsgListVo,"msgDelete");
            //result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말삭제_성공));
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말삭제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말삭제_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송설정_인사말삭제_번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말삭제_번호없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정_인사말삭제_실패));
        }
        return result;
    }

    public HashMap getMemberGoodList(GoodListVo goodListVo, HttpServletRequest request) {
        HashMap resultMap = new HashMap();
        P_GoodListVo pGoodListVo = new P_GoodListVo(goodListVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pGoodListVo);
        List<P_GoodListVo> goodList = mypageDao.callMemberGoodList(procedureVo);
        if(procedureVo.getRet().equals(Status.개인좋아요랭킹_요청회원_회원아님.getMessageCode())) {
            resultMap.put("status", Status.개인좋아요랭킹_요청회원_회원아님);
        }else if(procedureVo.getRet().equals(Status.개인좋아요랭킹_대상회원_회원아님.getMessageCode())) {
            resultMap.put("status", Status.개인좋아요랭킹_대상회원_회원아님);
        }else if(procedureVo.getRet().equals(Status.개인좋아요랭킹_실패.getMessageCode())) {
            resultMap.put("status", Status.개인좋아요랭킹_실패);
        }else if(procedureVo.getRet().equals(Status.개인좋아요랭킹_없음.getMessageCode()) || DalbitUtil.isEmpty(goodList)) {
            resultMap.put("status", Status.개인좋아요랭킹_없음);
            HashMap returnMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("list", new ArrayList());
            data.put("paging", new PagingVo(DalbitUtil.getIntMap(returnMap, "totalCnt"), DalbitUtil.getIntMap(returnMap, "pageNo"), DalbitUtil.getIntMap(returnMap, "pageCnt")));
            resultMap.put("data", data);
        }else{
            resultMap.put("status", Status.개인좋아요랭킹_성공);
            HashMap returnMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            List<GoodListOutVo> list = new ArrayList<>();
            for(P_GoodListVo p_goodListVo : goodList){
                list.add(new GoodListOutVo(p_goodListVo));
            }
            data.put("list", list);
            data.put("paging", new PagingVo(DalbitUtil.getIntMap(returnMap, "totalCnt"), DalbitUtil.getIntMap(returnMap, "pageNo"), DalbitUtil.getIntMap(returnMap, "pageCnt")));
            resultMap.put("data", data);
        }
        return resultMap;
    }


    /**
     * 방송설정 조회하기(선물 시 자동 팬 추가 및 입/퇴장 메시지 설정)
     */
    public Object callBroadcastSettingSelect(P_BroadcastSettingVo pBroadcastSettingVo, boolean isReturnTypeMap) {

        ProcedureVo procedureVo = new ProcedureVo(pBroadcastSettingVo);
        mypageDao.callBroadcastSettingSelect(procedureVo);

        HashMap returnMap = new HashMap();
        if(procedureVo.getRet().equals(Status.방송설정조회_성공.getMessageCode())) {
            Integer specialCnt = mypageDao.selectIsSpecial(pBroadcastSettingVo.getMem_no());
            int specialdj_badge = specialCnt == null ? 0 : specialCnt.intValue();
            boolean isSpecial = specialdj_badge > 0;

            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            returnMap.put("giftFanReg", DalbitUtil.getIntMap(resultMap, "giftFanReg") == 1);
            returnMap.put("djListenerIn", DalbitUtil.getIntMap(resultMap, "djListenerIn") == 1);
            returnMap.put("djListenerOut", DalbitUtil.getIntMap(resultMap, "djListenerOut") == 1);
            returnMap.put("listenerIn", DalbitUtil.getIntMap(resultMap, "listenerIn") == 1);
            returnMap.put("listenerOut", DalbitUtil.getIntMap(resultMap, "listenerOut") == 1);
            returnMap.put("liveBadgeView", DalbitUtil.getIntMap(resultMap, "liveBadgeView") == 1);
            returnMap.put("listenOpen", DalbitUtil.getIntMap(resultMap, "listenOpen"));
            returnMap.put("isSpecial", isSpecial);
            returnMap.put("specialBadge", specialdj_badge);
            returnMap.put("ttsSound", DalbitUtil.getIntMap(resultMap, "ttsSound") == 1);
            returnMap.put("normalSound", DalbitUtil.getIntMap(resultMap, "normalSound") == 1);
            returnMap.put("djTtsSound", DalbitUtil.getIntMap(resultMap, "djTtsSound") == 1);
            returnMap.put("djNormalSound", DalbitUtil.getIntMap(resultMap, "djNormalSound") == 1);

            return isReturnTypeMap ? returnMap : gsonUtil.toJson(new JsonOutputVo(Status.방송설정조회_성공, returnMap));
        }else if(procedureVo.getRet().equals(Status.방송설정조회_회원아님.getMessageCode())) {
            return isReturnTypeMap ? returnMap : gsonUtil.toJson(new JsonOutputVo(Status.방송설정조회_회원아님));
        }else{
            return isReturnTypeMap? returnMap : gsonUtil.toJson(new JsonOutputVo(Status.방송설정조회_실패));
        }
    }


    /**
     * 방송설정 수정하기(입퇴장 메시지)
     */
    public String callBroadcastSettingEdit(P_BroadcastSettingEditVo pBroadcastSettingEditVo, HttpServletRequest request, String state) {
        //이전 세팅 값 조회 (라이브 배지 문구 변경체크 확인)
        P_BroadcastSettingVo beforeBroadcastSettingVo = new P_BroadcastSettingVo();
        beforeBroadcastSettingVo.setMem_no(pBroadcastSettingEditVo.getMem_no());
        ProcedureVo beforeSettingVo = new ProcedureVo(beforeBroadcastSettingVo);
        mypageDao.callBroadcastSettingSelect(beforeSettingVo);
        HashMap beforeMap = new Gson().fromJson(beforeSettingVo.getExt(), HashMap.class);

        ProcedureVo procedureVo = new ProcedureVo(pBroadcastSettingEditVo);
        mypageDao.callBroadcastSettingEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.방송설정수정_성공.getMessageCode())) {
            HashMap returnMap = new HashMap();
            returnMap.put("giftFanReg", DalbitUtil.getIntMap(beforeMap, "giftFanReg") == 1);
            returnMap.put("djListenerIn", DalbitUtil.getIntMap(beforeMap, "djListenerIn") == 1);
            returnMap.put("djListenerOut", DalbitUtil.getIntMap(beforeMap, "djListenerOut") == 1);
            returnMap.put("listenerIn", DalbitUtil.getIntMap(beforeMap, "listenerIn") == 1);
            returnMap.put("listenerOut", DalbitUtil.getIntMap(beforeMap, "listenerOut") == 1);
            returnMap.put("liveBadgeView", DalbitUtil.getIntMap(beforeMap, "liveBadgeView") == 1);
            returnMap.put("listenOpen", DalbitUtil.getIntMap(beforeMap, "listenOpen"));
            returnMap.put("ttsSound", DalbitUtil.getIntMap(beforeMap, "ttsSound") == 1);
            returnMap.put("normalSound", DalbitUtil.getIntMap(beforeMap, "normalSound") == 1);
            returnMap.put("djTtsSound", DalbitUtil.getIntMap(beforeMap, "djTtsSound") == 1);
            returnMap.put("djNormalSound", DalbitUtil.getIntMap(beforeMap, "djNormalSound") == 1);

            //변경 후 프로시져 결과값에 따라 변경전 Map에 덮어 씌우기
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getGiftFanReg())){
                returnMap.put("giftFanReg", pBroadcastSettingEditVo.getGiftFanReg() == 1);
            }
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjListenerIn())){
                returnMap.put("djListenerIn", pBroadcastSettingEditVo.getDjListenerIn() == 1);
            }
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjListenerOut())){
                returnMap.put("djListenerOut", pBroadcastSettingEditVo.getDjListenerOut() == 1);
            }
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getListenerIn())){
                returnMap.put("listenerIn", pBroadcastSettingEditVo.getListenerIn() == 1);
            }
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getListenerOut())){
                returnMap.put("listenerOut", pBroadcastSettingEditVo.getListenerOut() == 1);
            }
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getLiveBadgeView())){
                returnMap.put("liveBadgeView", pBroadcastSettingEditVo.getLiveBadgeView() == 1);
            }
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getListenOpen())){
                returnMap.put("listenOpen", pBroadcastSettingEditVo.getListenOpen());
            }

            // ttsItem 사용 여부 설정
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getTtsSound())){
                returnMap.put("ttsSound", pBroadcastSettingEditVo.getTtsSound() == 1);
            }
            // soundItem 사용 여부 설정
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getNormalSound())){
                returnMap.put("normalSound", pBroadcastSettingEditVo.getNormalSound() == 1);
            }
            // dj ttsItem 사용 여부 설정
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjTtsSound())){
                returnMap.put("djTtsSound", pBroadcastSettingEditVo.getDjTtsSound() == 1);
            }
            // dj soundItem 사용 여부 설정
            if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjNormalSound())){
                returnMap.put("djNormalSound", pBroadcastSettingEditVo.getDjNormalSound() == 1);
            }

            try{
                HashMap socketMap = new HashMap();
                socketMap.put("dj_listener_in", DalbitUtil.getBooleanMap(returnMap, "djListenerIn") ? 1 : 0);
                socketMap.put("dj_listener_out", DalbitUtil.getBooleanMap(returnMap, "djListenerOut") ? 1 : 0);
                socketMap.put("dj_fan_in", DalbitUtil.getBooleanMap(returnMap, "djListenerIn") ? 1 : 0);
                socketMap.put("dj_fan_out", DalbitUtil.getBooleanMap(returnMap, "djListenerOut") ? 1 : 0);
                socketMap.put("listener_in", DalbitUtil.getBooleanMap(returnMap, "listenerIn") ? 1 : 0);
                socketMap.put("listener_out", DalbitUtil.getBooleanMap(returnMap, "listenerOut") ? 1 : 0);
                socketMap.put("badge_view", DalbitUtil.getBooleanMap(returnMap, "liveBadgeView") ? 1 : 0);
                socketMap.put("tts_sound", DalbitUtil.getBooleanMap(returnMap, "ttsSound"));
                socketMap.put("normal_sound", DalbitUtil.getBooleanMap(returnMap, "normalSound"));
                socketMap.put("dj_tts_sound", DalbitUtil.getBooleanMap(returnMap, "djTtsSound"));
                socketMap.put("dj_normal_sound", DalbitUtil.getBooleanMap(returnMap, "djNormalSound"));

                HashMap inOutMap = new HashMap();
                inOutMap.put("inOut", socketMap);
                socketService.changeMemberInfo(pBroadcastSettingEditVo.getMem_no(), inOutMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));

                /** socketSendApi cmd : "reqDjSetting" - 방장이 tts, sound 아이템 사용설정을 변경 했어요 패킷 전송*/
                // bj가 방송 설정 변경시 !
                // bj인지 체크, 방송방 여부 체크
                // ttsSound on/off시 or normalSound on/off시
                if (StringUtils.equals(MemberVo.getMyMemNo(request), pBroadcastSettingEditVo.getBjMemNo()) && !StringUtils.equals(pBroadcastSettingEditVo.getRoomNo(), "")) {
                    int beforeDjNormalSound = DalbitUtil.getIntMap(beforeMap, "djNormalSound");
                    int beforeDjTtsSound = DalbitUtil.getIntMap(beforeMap, "djTtsSound");
                    int afterDjNormalSound = DalbitUtil.getBooleanMap(returnMap, "djNormalSound")? 1: 0;
                    int afterDjTtsSound = DalbitUtil.getBooleanMap(returnMap, "djTtsSound")? 1: 0;
                    String text = "";
                    if (beforeDjNormalSound != afterDjNormalSound || beforeDjTtsSound != afterDjTtsSound) {
                        if (beforeDjNormalSound > afterDjNormalSound) {// normalSound off
                            text = "DJ의 설정으로 사운드 아이템 옵션을 사용할 수 없습니다.";
                        } else if (beforeDjNormalSound < afterDjNormalSound) {// normalSound on
                            text = "DJ의 설정으로 사운드 아이템 옵션을 제공합니다.";
                        } else if (beforeDjTtsSound > afterDjTtsSound) {// ttsSound off
                            text = "DJ의 설정으로 TTS 아이템 옵션을 사용할 수 없습니다.";
                        } else if (beforeDjTtsSound < afterDjTtsSound) {// ttsSound on
                            text = "DJ의 설정으로 TTS 아이템 옵션을 제공합니다.";
                        }

                        socketMap.put("text", text);
                        inOutMap.put("inOut", socketMap);
                        SocketVo vo = socketService.getSocketVo(pBroadcastSettingEditVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                        socketService.reqDjSetting(pBroadcastSettingEditVo.getRoomNo(), pBroadcastSettingEditVo.getMem_no(), inOutMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    }
                }
            } catch (Exception e) {
                log.error("MyPageService callBroadcastSettingEdit ERROR => {}", e);
            }

            Status status = null;
            if(state.equals("edit")){
                if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getLiveBadgeView()) && pBroadcastSettingEditVo.getLiveBadgeView() != DalbitUtil.getIntMap(beforeMap, "liveBadgeView")) {
                    if (pBroadcastSettingEditVo.getLiveBadgeView() == 1) {
                        status = Status.실시간팬배지_ON;
                    } else {
                        status = Status.실시간팬배지_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getListenOpen()) && pBroadcastSettingEditVo.getListenOpen() != DalbitUtil.getIntMap(beforeMap, "listenOpen")){
                    if (pBroadcastSettingEditVo.getListenOpen() == 1) {
                        status = Status.청취정보_ON;
                    } else if (pBroadcastSettingEditVo.getListenOpen() == 2) {
                        status = Status.청취정보_OFF;
                    } else {
                        status = Status.청취정보_HALFON;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getGiftFanReg()) && pBroadcastSettingEditVo.getGiftFanReg() != DalbitUtil.getIntMap(beforeMap, "giftFanReg")){
                    if (pBroadcastSettingEditVo.getGiftFanReg() == 1) {
                        status = Status.선물스타추가_ON;
                    } else {
                        status = Status.선물스타추가_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getTtsSound()) && pBroadcastSettingEditVo.getTtsSound() != DalbitUtil.getIntMap(beforeMap, "ttsSound")){
                    if (pBroadcastSettingEditVo.getTtsSound() == 1) {
                        status = Status.TTS_아이템_ON;
                    } else {
                        status = Status.TTS_아이템_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getNormalSound()) && pBroadcastSettingEditVo.getNormalSound() != DalbitUtil.getIntMap(beforeMap, "normalSound")){
                    if (pBroadcastSettingEditVo.getNormalSound() == 1) {
                        status = Status.SOUND_아이템_ON;
                    } else {
                        status = Status.SOUND_아이템_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjTtsSound()) && pBroadcastSettingEditVo.getDjTtsSound() != DalbitUtil.getIntMap(beforeMap, "djTtsSound")){
                    if (pBroadcastSettingEditVo.getDjTtsSound() == 1) {
                        status = Status.TTS_아이템_ON;
                    } else {
                        status = Status.TTS_아이템_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjNormalSound()) && pBroadcastSettingEditVo.getDjNormalSound() != DalbitUtil.getIntMap(beforeMap, "djNormalSound")){
                    if (pBroadcastSettingEditVo.getDjNormalSound() == 1) {
                        status = Status.SOUND_아이템_ON;
                    } else {
                        status = Status.SOUND_아이템_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjListenerIn()) && pBroadcastSettingEditVo.getDjListenerIn() != DalbitUtil.getIntMap(beforeMap, "djListenerIn")) {
                    //dj 청취자 입장 표시
                    if (pBroadcastSettingEditVo.getDjListenerIn() == 1) {
                        status = Status.청취자_입장표시_ON;
                    } else {
                        status = Status.청취자_입장표시_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getDjListenerOut()) && pBroadcastSettingEditVo.getDjListenerOut() != DalbitUtil.getIntMap(beforeMap, "djListenerOut")) {
                    //dj 청취자 퇴장 표시
                    if (pBroadcastSettingEditVo.getDjListenerOut() == 1) {
                        status = Status.청취자_퇴장표시_ON;
                    } else {
                        status = Status.청취자_퇴장표시_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getListenerIn()) && pBroadcastSettingEditVo.getListenerIn() != DalbitUtil.getIntMap(beforeMap, "listenerIn")) {
                    //청취자 입장 표시
                    if (pBroadcastSettingEditVo.getListenerIn() == 1) {
                        status = Status.청취자_입장표시_ON;
                    } else {
                        status = Status.청취자_입장표시_OFF;
                    }
                }else if(!DalbitUtil.isEmpty(pBroadcastSettingEditVo.getListenerOut()) && pBroadcastSettingEditVo.getListenerOut() != DalbitUtil.getIntMap(beforeMap, "listenerOut")) {
                    //청취자 퇴장 표시
                    if (pBroadcastSettingEditVo.getListenerOut() == 1) {
                        status = Status.청취자_퇴장표시_ON;
                    } else {
                        status = Status.청취자_퇴장표시_OFF;
                    }
                }else{
                    status = Status.방송설정수정_실패;
                }
            }else{
                status = Status.방송설정수정_성공;
            }

            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        }else if(procedureVo.getRet().equals(Status.방송설정수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정수정_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송설정수정_실패));
        }
        return result;
    }


    public HashMap callBroadcastSettingSelectRoomCreate(P_BroadcastSettingVo pBroadcastSettingVo) {

        ProcedureVo procedureVo = new ProcedureVo(pBroadcastSettingVo);
        mypageDao.callBroadcastSettingSelect(procedureVo);
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("giftFanReg", DalbitUtil.getIntMap(resultMap, "giftFanReg") == 1);
        returnMap.put("djListenerIn", DalbitUtil.getIntMap(resultMap, "djListenerIn") == 1);
        returnMap.put("djListenerOut", DalbitUtil.getIntMap(resultMap, "djListenerOut") == 1);
        returnMap.put("listenerIn", DalbitUtil.getIntMap(resultMap, "listenerIn") == 1);
        returnMap.put("listenerOut", DalbitUtil.getIntMap(resultMap, "listenerOut") == 1);
        returnMap.put("liveBadgeView", DalbitUtil.getIntMap(resultMap, "liveBadgeView") == 1);
        returnMap.put("ttsSound", DalbitUtil.getIntMap(resultMap, "ttsSound") == 1);
        returnMap.put("normalSound", DalbitUtil.getIntMap(resultMap, "normalSound") == 1);
        returnMap.put("djTtsSound", DalbitUtil.getIntMap(resultMap, "djTtsSound") == 1);
        returnMap.put("djNormalSound", DalbitUtil.getIntMap(resultMap, "djNormalSound") == 1);
        return returnMap;
    }

    public HashMap getMemberBoardCount(Object target){
        HashMap params = new HashMap();
        if(target instanceof P_MypageNoticeSelectVo){
            params.put("target_mem_no", ((P_MypageNoticeSelectVo) target).getTarget_mem_no());
            params.put("mem_no", ((P_MypageNoticeSelectVo) target).getMem_no());
        }else if(target instanceof P_FanboardListVo){
            params.put("target_mem_no", ((P_FanboardListVo) target).getStar_mem_no());
            params.put("mem_no", ((P_FanboardListVo) target).getMem_no());
        }else if(target instanceof P_ClipUploadListVo){
            params.put("target_mem_no", ((P_ClipUploadListVo) target).getStar_mem_no());
            params.put("mem_no", ((P_ClipUploadListVo) target).getMem_no());
        }else if(target instanceof P_ProfileInfoVo){
            params.put("target_mem_no", ((P_ProfileInfoVo) target).getTarget_mem_no());
            params.put("mem_no", ((P_ProfileInfoVo) target).getMem_no());
        }
        HashMap result = new HashMap();
        result.put("notice", 0);
        result.put("fanboard", 0);
        result.put("clip", 0);
        if(!params.isEmpty()){
            result = mypageDao.callMemberBoardCount(params);
        }
        return result;
    }

    public HashMap callMemberBoardStory(StoryVo storyVo, HttpServletRequest request){
        HashMap result = new HashMap();

        P_StoryVo pStoryVo = new P_StoryVo(storyVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pStoryVo);
        List<P_StoryVo> storyList = mypageDao.callMemberBoardStory(procedureVo);
        if(Status.방송방사연_조회_실패.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", Status.방송방사연_조회_실패);
        }else if(Status.방송방사연_조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", Status.방송방사연_조회_회원아님);
        }else{
            List<StoryOutVo> outList = new ArrayList<>();
            if(DalbitUtil.isEmpty(storyList)){
                result.put("status", Status.방송방사연_조회_없음);
            }else{
                result.put("status", Status.방송방사연_조회_성공);
                DeviceVo deviceVo = new DeviceVo(request);
                for(P_StoryVo data : storyList){
                    outList.add(new StoryOutVo(data, DalbitUtil.getProperty("server.photo.url"), deviceVo));
                }
            }
            result.put("data", outList);
            result.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), storyVo.getPage(), storyVo.getRecords()));
        }

        return result;
    }

    /**
     * 사연모아보기 (청취자)
     */
    public HashMap callMemberBoardStorySend(StoryVo storyVo, HttpServletRequest request) {
        HashMap result = new HashMap();

        P_StoryVo pStoryVo = new P_StoryVo(storyVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pStoryVo);

        List<P_StorySendVo> storyList = mypageDao.callMemberBoardStorySend(procedureVo);
        if(Status.방송방사연_조회_실패.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", Status.방송방사연_조회_실패);
        }else if(Status.방송방사연_조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", Status.방송방사연_조회_회원아님);
        }else{
            List<StorySendOutVo> outList = new ArrayList<>();
            if(DalbitUtil.isEmpty(storyList)){
                result.put("status", Status.방송방사연_조회_없음);
            }else{
                result.put("status", Status.방송방사연_조회_성공);
                for(P_StorySendVo data : storyList){
                    outList.add(new StorySendOutVo(data, DalbitUtil.getProperty("server.photo.url")));
                }
            }
            HashMap data = new HashMap();
            data.put("list", outList);
            data.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), storyVo.getPage(), storyVo.getRecords()));
            result.put("data", data);
        }

        return result;
    }

    /**
     * 내지갑 달&별 팝업 리스트 & 건수
     */
    public String callWalletPopupListView(P_WalletPopupListVo pWalletPopupListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pWalletPopupListVo);
        mypageDao.callWalletPopupListView(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            ArrayList walletList = new ArrayList();
            ArrayList key = new ArrayList(resultMap.keySet());

            String text = "";
            int walletCode = 0;
            int order=1;
            if(pWalletPopupListVo.getSlctType() == 0){ //별
                for (int i=0; i < key.size(); i++){
                    if("exchangeUse".equals(key.get(i))){
                        text = "환전 사용";
                        walletCode = 1;
                        order = 1;
                    }else if("changeUse".equals(key.get(i))){
                        text = "별＞달 교환 사용";
                        walletCode = 2;
                        order = 2;
            /* =============================================================================== */
                    }else if("itemGet".equals(key.get(i))){
                        text = "DJ-방송 선물 획득";
                        walletCode = 3;
                        order = 3;
                    }else if("guest_itemGet".equals(key.get(i))){
                        text = "게스트-방송 선물 획득";
                        walletCode = 10;
                        order = 4;
                    }else if("mailboxGet".equals(key.get(i))){
                        text = "메시지 선물 획득";
                        walletCode = 12;
                        order = 5;
                    }else if("clipGet".equals(key.get(i))){
                        text = "클립 선물 획득";
                        walletCode = 5;
                        order = 6;
                    /*}else if("secret_itemGet".equals(key.get(i))){
                        text = "DJ-방송 몰래 선물 획득";
                        walletCode = 4;
                        order = 6;
                    }else if("guest_secret_itemGet".equals(key.get(i))){
                        text = "게스트-방송 몰래 선물 획득";
                        walletCode = 11;
                        order = 7;*/
                    }else if("levelupGet".equals(key.get(i))){
                        text = "레벨업 보상 획득";
                        walletCode = 6;
                        order = 7;
                    }else if("eventGet".equals(key.get(i))){
                        text = "이벤트 보상 획득";
                        walletCode = 7;
                        order = 8;
                    }else if("opGet".equals(key.get(i))){
                        text = "운영자 지급 획득";
                        walletCode = 8;
                        order = 9;
                    }else if("exchangeGet".equals(key.get(i))){
                        text = "환전 취소 복구";
                        walletCode = 9;
                        order = 10;
                    }
                    HashMap returnMap = new HashMap();
                    returnMap.put("walletCode", walletCode);
                    returnMap.put("type", key.get(i).toString().toUpperCase().endsWith("USE") ? "use" : "get");
                    returnMap.put("text", text);
                    returnMap.put("cnt", DalbitUtil.getIntMap(resultMap, (String) key.get(i)));
                    returnMap.put("order", order);
                    walletList.add(returnMap);
                }
            } else { // 달
                for (int i=0; i < key.size(); i++){
                    if("itemUse".equals(key.get(i))){
                        text = "DJ-방송 선물 사용";
                        walletCode = 1;
                        order = 1;
                    }else if("guest_itemUse".equals(key.get(i))){
                        text = "게스트-방송 선물 사용";
                        walletCode = 12;
                        order = 2;
                    /*}else if("secret_itemUse".equals(key.get(i))){
                        text = "DJ-방송 몰래 선물 사용";
                        walletCode = 2;
                        order = 3;*/
                    /*}else if("guest_secret_itemUse".equals(key.get(i))){
                        text = "게스트-방송 몰래 선물 사용";
                        walletCode = 13;
                        order = 4;*/
                    }else if("mailboxUse".equals(key.get(i))){
                        text = "메시지 선물 사용";
                        walletCode = 14;
                        order = 3;
                    }else if("clipUse".equals(key.get(i))){
                        text = "클립 선물 사용";
                        walletCode = 4;
                        order = 4;
                    }else if("giftUse".equals(key.get(i))){
                        text = "달 직접 선물 사용";
                        walletCode = 3;
                        order = 5;
            /* =============================================================================== */
                    }else if("buyGet".equals(key.get(i))){
                        text = "달 구매/취소 획득";
                        walletCode = 5;
                        order = 6;
                    }else if("changeGet".equals(key.get(i))){
                        text = "별＞달 교환 획득";
                        walletCode = 9;
                        order = 7;
                    }else if("UseGet".equals(key.get(i))){
                        text = "달 직접 선물 획득";
                        walletCode = 6;
                        order = 8;
                    }else if("rankingGet".equals(key.get(i))){
                        text = "랭킹 보상 획득";
                        walletCode = 7;
                        order = 9;
                    }else if("levelupGet".equals(key.get(i))){
                        text = "레벨 업 보상 획득";
                        walletCode = 8;
                        order = 10;
                    }else if("eventGet".equals(key.get(i))){
                        text = "이벤트 보상 획득";
                        walletCode = 10;
                        order = 11;
                    }else if("opGet".equals(key.get(i))){
                        text = "운영자 지급 획득";
                        walletCode = 11;
                        order = 12;
                    }
                    HashMap returnMap = new HashMap();
                    returnMap.put("walletCode", walletCode);
                    returnMap.put("type", key.get(i).toString().toUpperCase().endsWith("USE") ? "use" : "get");
                    returnMap.put("text", text);
                    returnMap.put("cnt", DalbitUtil.getIntMap(resultMap, (String) key.get(i)));
                    returnMap.put("order", order);
                    walletList.add(returnMap);
                }
            }

            HashMap walletListMap = new HashMap();
            walletListMap.put("list", walletList);

            int size = walletList.size();
            Object[] newArray = new Object[size +1];
            for (int i=0; i < size; i++){
                int orderNo = (int) ((HashMap) walletList.get(i)).get("order");
                newArray[orderNo] =  walletList.get(i);
            }
            Object[] newArray2 = new Object[size];
            for (int i=0; i < size; i++){
                newArray2[i] = newArray[i +1];
            }
            HashMap list = new HashMap();
            list.put("list", newArray2);

            result = gsonUtil.toJson(new JsonOutputVo(Status.내지갑팝업조회_성공, list));
        }else if(procedureVo.getRet().equals(Status.내지갑팝업조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.내지갑팝업조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.내지갑팝업조회_실패));
        }
        return result;
    }


    /**
     * 내 지갑 달 or 별 내역 보기
     */
    public String callWalletList(P_WalletListVo pWalletListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pWalletListVo);
        List<P_WalletListVo> walletListVo = mypageDao.callWalletList(procedureVo);

        String result;
        HashMap walletList = new HashMap();
        if(DalbitUtil.isEmpty(walletListVo)){
            walletList.put("list", new ArrayList<>());
            walletList.put("totalCnt", 0);
            walletList.put("paging", new PagingVo(0, pWalletListVo.getPageNo(), pWalletListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.내지갑_내역조회_없음));
        }

        List<WalletListOutVo> outVoList = new ArrayList<>();
        if(!DalbitUtil.isEmpty(walletListVo)){
            for (int i = 0; i < walletListVo.size(); i++) {
                outVoList.add(new WalletListOutVo(walletListVo.get(i)));
            }
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        walletList.put("list", procedureOutputVo.getOutputBox());
        walletList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
        walletList.put("byeolTotCnt", DalbitUtil.getIntMap(resultMap, "byeol"));
        walletList.put("dalTotCnt", DalbitUtil.getIntMap(resultMap, "dal"));

        if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.내지갑_내역조회_성공, walletList));
        } else if (procedureVo.getRet().equals(Status.내지갑_내역조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.내지갑_내역조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.내지갑_내역조회_실패));
        }
        return result;
    }

    /**
     * 환전 취소하기
     */
    public String callExchangeCancel(P_ExchangeCancelVo pExchangeCancelVo) {
        ProcedureVo procedureVo = new ProcedureVo(pExchangeCancelVo);
        mypageDao.callExchangeCancel(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.환전취소_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_성공));
        }else if(procedureVo.getRet().equals(Status.환전취소_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_회원아님));
        }else if(procedureVo.getRet().equals(Status.환전취소_환전번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_환전번호없음));
        }else if(procedureVo.getRet().equals(Status.환전취소_미처리상태아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_미처리상태아님));
        }else if(procedureVo.getRet().equals(Status.환전취소_취소불가시간.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_취소불가시간));
        }else if(procedureVo.getRet().equals(Status.환전취소_대상불일지.getMessageCode())) {
           result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_대상불일지));
        }else if(procedureVo.getRet().equals(Status.환전취소_이미취소됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_이미취소됨));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전취소_실패));
        }
        return result;
    }

    /**
     * ##### 회원 방송방에서 받은 사연 리스트 (마이페이지에서 사용)
     *
     * @param
     * memNo 		BIGINT			-- 회원번호
     * ,pageNo 		INT UNSIGNED	-- 페이지 번호
     * ,pagePerCnt 	INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * Multi Rows
     *
     * #1
     * cnt		BIGINT		-- 전체 수
     *
     * #2
     * idx                  BIGINT          -- 자동등록 번호
     * dj_mem_no            BIGINT          -- 회원 번호(방장)
     * room_no              BIGINT          -- 방 번호
     * contents             VARCHAR         -- 내용
     * plus_yn              CHAR            -- 플러스[y,n]
     * status               BIGINT          -- 상태 0 정상,1삭제
     * writer_no            BIGINT          -- 회원 번호(보낸이)
     * writer_mem_id        VARCHAR         -- 회원 아이디(보낸이)
     * writer_mem_nick      VARCHAR         -- 회원 닉네임(보낸이)
     * writer_mem_sex       CHAR            -- 회원성별(보낸이)
     * writer_mem_profile   VARCHAR         -- 프로필(보낸이)
     * write_date           DATETIME        -- 등록일자
     */
    public String getStoryHistory(StoryHistoryVo vo, HttpServletRequest requeset){
        HashMap map = new HashMap();
        HashMap returnDefault = new HashMap();
        returnDefault.put("paing", new PagingVo(0, 0, 0));
        returnDefault.put("list", new ArrayList<>());

        try {
            String memNo = MemberVo.getMyMemNo(requeset);

            /* memNo */
            if(StringUtils.equals(memNo, null) || StringUtils.equals(memNo, "")){
                return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요, returnDefault));
            }

            /* db 조회 */
            map.put("memNo", Long.parseLong(memNo));
            map.put("pageNo", vo.getPageNo());
            map.put("pagePerCnt", vo.getPagePerCnt());
            List<Object> list = mypageDao.pBroadcastRoomStoryMemList(map);

            /* 결과 없음 */
            if(list.equals(null)){
                log.error("ContentService.java / getStoryHistory => DB return null", gsonUtil.toJson(map));
                return gsonUtil.toJson(new JsonOutputVo(Status.사연보관함_조회_실패, returnDefault));
            }

            /* 정상 */
            HashMap result = new HashMap();
            Integer cnt = DBUtil.getData(list,0, Integer.class);
            List<StoryResultVo> resultList = DBUtil.getList(list,1, StoryResultVo.class);

            // 남, 녀 디폴트 이미지
            String mImg = new ImageVo("", "m", DalbitUtil.getProperty("server.photo.url")).getPath();
            String fImg = new ImageVo("", "f", DalbitUtil.getProperty("server.photo.url")).getPath();
            for( StoryResultVo storyResultVo : resultList){
                if (StringUtils.equals(storyResultVo.getWriter_mem_profile(), "")) {
                    if (StringUtils.equals(storyResultVo.getWriter_mem_sex(), "m")) {
                        storyResultVo.setWriter_mem_profile(mImg);
                    } else {
                        storyResultVo.setWriter_mem_profile(fImg);
                    }
                }
            }

            result.put("paing", new PagingVo(cnt, vo.getPageNo(), vo.getPagePerCnt()));
            result.put("list", resultList);

            return gsonUtil.toJson(new JsonOutputVo(Status.사연보관함_조회_성공, result));
        } catch (Exception e) {
            log.error("ContentService.java / getStoryHistory => {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방사연_조회_실패, returnDefault));
        }

    }
}
