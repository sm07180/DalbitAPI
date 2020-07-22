package com.dalbit.event.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.EventDao;
import com.dalbit.event.vo.ReplyListOutputVo;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class EventService {

    @Autowired
    EventDao eventDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    EventService roomService;

    public String event200608Term(){
        List<HashMap> eventTerm = new ArrayList<>();
        List<HashMap> eventTmp = new ArrayList<>();
        Calendar today = Calendar.getInstance();

        HashMap term1 = new HashMap();
        Calendar srt1 = Calendar.getInstance();
        Calendar end1 = Calendar.getInstance();
        srt1.set(2020, 5,8,0,0,0);
        end1.set(2020, 5, 14, 23, 59, 59);
        term1.put("round", 1);
        term1.put("srtDt", srt1);
        term1.put("endDt", end1);

        HashMap term2 = new HashMap();
        Calendar srt2 = Calendar.getInstance();
        Calendar end2 = Calendar.getInstance();
        srt2.set(2020, 5,15,0,0,0);
        end2.set(2020, 5, 21, 23, 59, 59);
        term2.put("round", 2);
        term2.put("srtDt", srt2);
        term2.put("endDt", end2);

        HashMap term3 = new HashMap();
        Calendar srt3 = Calendar.getInstance();
        Calendar end3 = Calendar.getInstance();
        srt3.set(2020, 5,22,0,0,0);
        end3.set(2020, 5, 28, 23, 59, 59);
        term3.put("round", 3);
        term3.put("srtDt", srt3);
        term3.put("endDt", end3);

        eventTmp.add(term1);
        eventTmp.add(term2);
        eventTmp.add(term3);

        int nowRound = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("M/d", Locale.KOREA);
        for(HashMap term : eventTmp){
            String state = "ready";
            Calendar srt = (Calendar)term.get("srtDt");
            Calendar end = (Calendar)term.get("endDt");

            if(srt.getTimeInMillis() <= today.getTimeInMillis() && end.getTimeInMillis() >= today.getTimeInMillis()) {
                state = "ing";
                nowRound = DalbitUtil.getIntMap(term, "round");
            }else if(end.getTimeInMillis() < today.getTimeInMillis()){
                state = "finished";
            }

            HashMap termTmp = new HashMap();
            termTmp.put("round", term.get("round"));
            termTmp.put("srtTs", DalbitUtil.getUTCTimeStamp(srt.getTime()));
            termTmp.put("srtDt", DalbitUtil.getUTCFormat(srt.getTime()));
            termTmp.put("endTs", DalbitUtil.getUTCTimeStamp(end.getTime()));
            termTmp.put("endDt", DalbitUtil.getUTCFormat(end.getTime()));
            termTmp.put("state", state);
            termTmp.put("term", sdf.format(srt.getTime()) + "~" + sdf.format(end.getTime()));

            eventTerm.add(termTmp);
        }

        HashMap data = new HashMap();
        data.put("nowRound", nowRound);
        data.put("terms", eventTerm);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, data));
    }

    /**
     * 랭킹 이벤트 실시간 순위 리스트
     */
    @Transactional(readOnly = true)
    public String callEventRankingLive(P_RankingLiveInputVo pRankingLiveInputVo){
        ProcedureVo procedureVo = new ProcedureVo(pRankingLiveInputVo);
        long st = (new Date()).getTime();
        List<P_RankingLiveOutputVo> rankingLiveVoList = eventDao.callEventRankingLive(procedureVo);
        log.debug("select time {} ms", ((new Date()).getTime() - st));

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        if(DalbitUtil.isEmpty(rankingLiveVoList)){
            resultMap.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.랭킹이벤트실시간순위리스트없음, resultMap));
        }


        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            for(int i = 0; i < rankingLiveVoList.size(); i++){
                if(DalbitUtil.isEmpty(rankingLiveVoList.get(i).getProfileImage())){
                    rankingLiveVoList.get(i).setProfileImage(Code.포토_프로필_디폴트_PREFIX.getCode() + "/" + Code.프로필이미지_파일명_PREFIX.getCode() + rankingLiveVoList.get(i).getMemSex() + "_200327.jpg");
                }
                if(pRankingLiveInputVo.getSlct_type() == 1 && DalbitUtil.isEmpty(rankingLiveVoList.get(i).getFanImage())){
                    rankingLiveVoList.get(i).setFanImage(Code.포토_프로필_디폴트_PREFIX.getCode() + "/" + Code.프로필이미지_파일명_PREFIX.getCode() + rankingLiveVoList.get(i).getFanSex() + "_200327.jpg");
                }
            }
            resultMap.put("list", rankingLiveVoList);

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹이벤트실시간순위리스트조회, resultMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹이벤트실시간순위리스트_실패));
        }
        return result;
    }


    /**
     * 랭킹 이벤트 결과 보기
     */
    @Transactional(readOnly = true)
    public String callEventRankingResult(P_RankingResultInputVo pRankingResultInputVo){
        ProcedureVo procedureVo = new ProcedureVo(pRankingResultInputVo);
        long st = (new Date()).getTime();
        List<P_RankingResultOutputVo> rankingResultVoList = eventDao.callEventRankingResult(procedureVo);
        log.debug("select time {} ms", ((new Date()).getTime() - st));

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        if(DalbitUtil.isEmpty(rankingResultVoList)){
            resultMap.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.랭킹이벤트결과없음, resultMap));
        }


        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            for(int i = 0; i < rankingResultVoList.size(); i++){
                if(DalbitUtil.isEmpty(rankingResultVoList.get(i).getProfileImage())){
                    rankingResultVoList.get(i).setProfileImage(Code.포토_프로필_디폴트_PREFIX.getCode() + "/" + Code.프로필이미지_파일명_PREFIX.getCode() + rankingResultVoList.get(i).getMemSex() + "_200327.jpg");
                }
                if(pRankingResultInputVo.getSlct_type() == 1 && DalbitUtil.isEmpty(rankingResultVoList.get(i).getFanImage())){
                    rankingResultVoList.get(i).setFanImage(Code.포토_프로필_디폴트_PREFIX.getCode() + "/" + Code.프로필이미지_파일명_PREFIX.getCode() + rankingResultVoList.get(i).getFanSex() + "_200327.jpg");
                }
            }
            resultMap.put("list", rankingResultVoList);

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹이벤트결과조회, resultMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹이벤트결과_실패));
        }
        return result;
    }



    /**
     * 이벤트 댓글 리스트 조회
     */
    @Transactional(readOnly = true)
    public String callEventReplyList(P_ReplyListInputVo pRankingLiveInputVo){
        ProcedureVo procedureVo = new ProcedureVo(pRankingLiveInputVo);
        long st = (new Date()).getTime();
        List<P_ReplyListOutputVo> replyVoList = eventDao.callEventReplyList(pRankingLiveInputVo);
        log.debug("select time {} ms", ((new Date()).getTime() - st));

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(replyVoList)){
            resultMap.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트댓글리스트없음, resultMap));
        }

        String result;
        if(replyVoList.size() > 0) {
            List<ReplyListOutputVo> outList = new ArrayList<>();
            for(P_ReplyListOutputVo vo : replyVoList){
                ReplyListOutputVo outVo = new ReplyListOutputVo(vo);
                outList.add(outVo);
            }

            resultMap.put("list", outList);

            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트댓글리스트조회, resultMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트댓글리스트_실패));
        }
        return result;
    }

    /**
     * 이벤트 댓글 등록
     */
    public String callEventReplyAdd(P_ReplyAddInputVo pReplyAddInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pReplyAddInputVo);

        if(pReplyAddInputVo.getMemLogin() == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글달기실패_회원아님));
        }

        int insertResult = eventDao.callEventReplyAdd(pReplyAddInputVo);

        String result;
        if(Status.이벤트_댓글달기성공.getMessageCode().equals(insertResult+"")){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글달기성공));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글달기실패_등록오류));
        }

        return result;
    }

    /**
     * 이벤트 댓글 삭제
     */
    public String callEventReplyDelete(P_ReplyDeleteInputVo pReplyDeleteInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pReplyDeleteInputVo);

        int checkAuth = eventDao.callEventAuthCheck(pReplyDeleteInputVo);
        if(checkAuth <= 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제실패_삭제권한없음));
        }

        int insertResult = eventDao.callEventReplyDelete(pReplyDeleteInputVo);

        String result;
        if(insertResult > 0){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제성공));
        }else if(insertResult == 0){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제정보없음));
        }else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제실패_등록오류));
        }

        return result;
    }

    public String attendanceCheckStatus(HttpServletRequest request){

        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        var paramMap = new HashMap();
        paramMap.put("mem_no", mem_no);

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<P_AttendanceCheckLoadOutputVo> dateList = eventDao.callAttendanceCheckLoad(procedureVo);

        if(procedureVo.getRet().equals(Status.출석체크이벤트_상태조회_실패_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_상태조회_실패_회원아님));

        }else if (procedureVo.getRet().equals(Status.출석체크이벤트_상태조회_성공.getMessageCode())){

            var returnMap = attendanceResultMap(procedureVo, dateList);
            if(mem_no.equals("11583296139594") || mem_no.equals("11584406236831")) {

                //the_date":"2020-06-15","the_day":0,"check_ok":0,"reward_exp":0,"reward_dal":0

                ArrayList<P_AttendanceCheckLoadOutputVo> list = new ArrayList<P_AttendanceCheckLoadOutputVo>();
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-15", 0, 1, 1, 2));
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-16", 1, 1, 1, 2));
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-17", 2, 1, 1, 2));
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-18", 3, 1, 1, 2));
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-19", 4, 1, 1, 2));
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-20", 5, 1, 1, 2));
                list.add(new P_AttendanceCheckLoadOutputVo("2020-06-21", 6, 1, 1, 2));

                returnMap.put("dateList",list );

                var summary = new HashMap();
                summary.put("dalCnt", 7);
                summary.put("attendanceDays", 7);
                summary.put("totalExp", 14);
                returnMap.put("summary",summary );

                var status = new HashMap();
                status.put("check_gift", 0);
                status.put("bonus", 1);
                returnMap.put("status",status );

            }
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_상태조회_성공, returnMap));

        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_상태조회_실패));
        }
    }

    public String attendanceCheckIn(HttpServletRequest request){

        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        var paramMap = new HashMap();
        paramMap.put("mem_no", mem_no);

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<P_AttendanceCheckLoadOutputVo> dateList = eventDao.callAttendanceCheckGift(procedureVo);

        if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_실패_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패_회원아님));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_실패_이미받음.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패_이미받음));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_실패_필요시간부족.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패_필요시간부족));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_실패_보상테이블없음.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패_보상테이블없음));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_실패_동일기기중복불가.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패_동일기기중복불가));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_성공.getMessageCode())){

            var returnMap = attendanceResultMap(procedureVo, dateList);
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_성공, returnMap));

        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패));
        }
    }

    public String attendanceRandomGift(HttpServletRequest request){

        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        var paramMap = new HashMap();
        paramMap.put("mem_no", mem_no);
        
        if(mem_no.equals("11583296139594") || mem_no.equals("11584406236831")){
            var summary = new HashMap();
            summary.put("attendanceDays", 7);
            summary.put("totalExp", 200);
            summary.put("dalCnt", 15);

            var status = new HashMap();
            status.put("check_gift", 0);
            status.put("bonus", 0);
            status.put("exp", 50);
            status.put("dal", 200);

            var returnMap = new HashMap();
            returnMap.put("status", status);
            returnMap.put("dateList", "이건 필요없어요.. ");
            returnMap.put("summary", summary);

            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_성공, returnMap));
        }

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<P_AttendanceCheckLoadOutputVo> dateList = eventDao.callAttendanceCheckBonus(procedureVo);

        if(procedureVo.getRet().equals(Status.출석체크이벤트_더줘_실패_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_실패_회원아님));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_더줘_실패_이미받음.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_실패_이미받음));

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_더줘_실패_대상아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_실패_대상아님));
        
        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_더줘_성공.getMessageCode())){

            int attendanceDays = 0;
            int totalExp = 0;
            int dalCnt = 0;

            for (P_AttendanceCheckLoadOutputVo pAttendanceCheckLoadOutputVo : dateList){
                attendanceDays += pAttendanceCheckLoadOutputVo.getCheck_ok();
                totalExp += pAttendanceCheckLoadOutputVo.getReward_exp();
                dalCnt += pAttendanceCheckLoadOutputVo.getReward_dal();
            }

            var summary = new HashMap();
            summary.put("attendanceDays", attendanceDays);
            summary.put("totalExp", totalExp);
            summary.put("dalCnt", dalCnt);

            var status = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

            var returnMap = new HashMap();
            returnMap.put("status", status);
            returnMap.put("dateList", dateList);
            returnMap.put("summary", summary);

            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_성공, returnMap));

        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_실패));
        }
    }

    public HashMap attendanceResultMap(ProcedureVo procedureVo, ArrayList<P_AttendanceCheckLoadOutputVo> dateList){
        int attendanceDays = 0;
        int totalExp = 0;
        int dalCnt = 0;

        for (P_AttendanceCheckLoadOutputVo pAttendanceCheckLoadOutputVo : dateList){
            attendanceDays += pAttendanceCheckLoadOutputVo.getCheck_ok();
            totalExp += pAttendanceCheckLoadOutputVo.getReward_exp();
            dalCnt += pAttendanceCheckLoadOutputVo.getReward_dal();

            if(pAttendanceCheckLoadOutputVo.getThe_date().equals(DalbitUtil.getDate("yyyy-MM-dd"))
                    && pAttendanceCheckLoadOutputVo.getCheck_ok() == 0 ){
                pAttendanceCheckLoadOutputVo.setCheck_ok(2);
            }
        }

        var summary = new HashMap();
        summary.put("attendanceDays", attendanceDays);
        summary.put("totalExp", totalExp);
        summary.put("dalCnt", dalCnt);

        var status = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

        var returnMap = new HashMap();
        returnMap.put("status", status);

        returnMap.put("dateList", dateList);
        returnMap.put("summary", summary);

        return returnMap;
    }

    /**
     * 라이징 이벤트 실시간 순위보기
     */
    public String callRisingLive(HttpServletRequest request, P_RisingLiveInputVo pRisingLiveInputVo) {
        Calendar today = Calendar.getInstance();
        Calendar srt1 = Calendar.getInstance();
        Calendar end1 = Calendar.getInstance();
        srt1.set(2020, 6,16,0,0,0);
        end1.set(2020, 6, 22, 23, 59, 59);
        String state="finished";

        if(srt1.getTimeInMillis() <= today.getTimeInMillis() && end1.getTimeInMillis() >= today.getTimeInMillis()) {
            state = "ing";
        }

        HashMap map = new HashMap();
        map.put("state", state);

        String result;

        if("ing".equals(state)){
            pRisingLiveInputVo.setMem_no(MemberVo.getMyMemNo(request));
            ProcedureVo procedureVo = new ProcedureVo(pRisingLiveInputVo);
            ArrayList<P_RisingEventListOutputVo> risingList = eventDao.callRisingLive(procedureVo);

            if(Integer.parseInt(procedureVo.getRet()) > 0) {
                P_RisingEventOutputVo risingOutput = new Gson().fromJson(procedureVo.getExt(), P_RisingEventOutputVo.class);

                if(DalbitUtil.isEmpty(risingList)){
                    risingList = new ArrayList<>();
                }else{
                    for(int i = 0; i < risingList.size(); i++){
                        ImageVo imageVo = new ImageVo(risingList.get(i).getProfileImage(), risingList.get(i).getMemSex(), DalbitUtil.getProperty("server.photo.url"));
                        risingList.get(i).setProfileImage(imageVo.getPath());
                        if(pRisingLiveInputVo.getSlct_type() == 1){
                            ImageVo imageFanVo = new ImageVo(risingList.get(i).getFanImage(), risingList.get(i).getFanSex(), DalbitUtil.getProperty("server.photo.url"));
                            risingList.get(i).setFanImage(imageFanVo.getPath());
                        }
                    }
                }

                map.put("risingList", risingList);
                map.put("risingOutput", risingOutput);

                result = gsonUtil.toJson(new JsonOutputVo(Status.라이징이벤트_실시간순위_조회_성공, map));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.라이징이벤트_실시간순위_데이터없음));
            }
        }else {
            map.put("risingList", new ArrayList());
            map.put("risingOutput", new HashMap());
            result = gsonUtil.toJson(new JsonOutputVo(Status.라이징이벤트_실시간순위_데이터없음, map));
        }
        return result;
    }

    /**
     * 라이징 이벤트 결과보기
     */
    public String callRisingResult(HttpServletRequest request, P_RisingResultInputVo pRisingResultInputVo) {
        pRisingResultInputVo.setMem_no(MemberVo.getMyMemNo(request));
        ProcedureVo procedureVo = new ProcedureVo(pRisingResultInputVo);
        ArrayList<P_RisingEventListOutputVo> risingList = eventDao.callRisingResult(procedureVo);

        P_RisingEventOutputVo risingOutput = new Gson().fromJson(procedureVo.getExt(), P_RisingEventOutputVo.class);

        HashMap map = new HashMap();
        map.put("risingList", risingList);
        map.put("risingOutput", risingOutput);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.라이징이벤트_결과_조회_성공, map));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.라이징이벤트_결과_데이터없음));
        }
        return result;
    }
}
