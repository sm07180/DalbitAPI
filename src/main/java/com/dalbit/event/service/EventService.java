package com.dalbit.event.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.EventDao;
import com.dalbit.event.vo.GifiticonWinListOutputVo;
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

    /**
     * 출석체크 상태 체크
     */
    public String attendanceCheckStatus(HttpServletRequest request){

        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        var paramMap = new HashMap();
        paramMap.put("mem_no", mem_no);

        /*String memNos = "|11584609037713|11584609037192|11584609037223|";
        if(memNos.contains(mem_no)){
            var dateList = setTestDateList("status");

            var status = new HashMap<>();
            status.put("check_gift", "1");
            status.put("bonus", "0");
            status.put("phone_input", "1");
            status.put("input_enddate", "2020-08-09 23:59:59");

            var summary = new HashMap<>();
            summary.put("attendanceDays", 7);
            summary.put("totalExp", 200);
            summary.put("dalCnt", 120);

            var resultMap = new HashMap<>();
            resultMap.put("status", status);
            resultMap.put("dateList", dateList);
            resultMap.put("summary", summary);
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_성공, resultMap));

        }*/

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<P_AttendanceCheckLoadOutputVo> dateList = eventDao.callAttendanceCheckLoad(procedureVo);

        if(procedureVo.getRet().equals(Status.출석체크이벤트_상태조회_실패_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_상태조회_실패_회원아님));

        }else if (procedureVo.getRet().equals(Status.출석체크이벤트_상태조회_성공.getMessageCode())){

            var returnMap = attendanceResultMap(procedureVo, dateList);
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_상태조회_성공, returnMap));

        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_상태조회_실패));
        }
    }

    public ArrayList setTestDateList(String type){
        var dateList = new ArrayList<>();
        var date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-03");
        date.setThe_day(0);
        date.setCheck_ok(1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(0);
        dateList.add(date);

        date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-04");
        date.setThe_day(1);
        date.setCheck_ok(1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(0);
        dateList.add(date);

        date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-05");
        date.setThe_day(1);
        date.setCheck_ok(1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(0);
        dateList.add(date);

        date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-06");
        date.setThe_day(1);
        date.setCheck_ok(1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(0);
        dateList.add(date);

        date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-07");
        date.setThe_day(1);
        date.setCheck_ok(1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(0);
        dateList.add(date);

        date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-08");
        date.setThe_day(1);
        date.setCheck_ok(1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(0);
        dateList.add(date);

        date = new P_AttendanceCheckLoadOutputVo();
        date.setThe_date("2020-08-09");
        date.setThe_day(1);
        date.setCheck_ok(type.equals("status") ? 2 : 1);
        date.setReward_exp(5);
        date.setReward_dal(4);
        date.setIs_today(1);
        dateList.add(date);

        return dateList;
    }

    /**
     * 출석체크
     */
    public String attendanceCheckIn(HttpServletRequest request){

        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        var paramMap = new HashMap();
        paramMap.put("mem_no", mem_no);

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<P_AttendanceCheckLoadOutputVo> dateList = eventDao.callAttendanceCheckGift(procedureVo);
        /*String memNos = "|11584609037713|11584609037192|11584609037223|";
        if(memNos.contains(mem_no)){
            dateList = setTestDateList("checkIn");

            var status = new HashMap<>();
            status.put("check_gift", "0");
            status.put("bonus", "1");
            status.put("gifticon_check", "1");

            String gifticon_win = "0";
            if("11584609037713".equals(mem_no)){
                gifticon_win = "2";
            }else if("11584609037192".equals(mem_no)){
                gifticon_win = "1";
            }
            status.put("gifticon_win", gifticon_win);
            status.put("phone_input", "1");
            status.put("input_enddate", "2020-08-09 23:59:59");

            var summary = new HashMap<>();
            summary.put("attendanceDays", 7);
            summary.put("totalExp", 200);
            summary.put("dalCnt", 120);

            var resultMap = new HashMap<>();
            resultMap.put("status", status);
            resultMap.put("dateList", dateList);
            resultMap.put("summary", summary);
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_성공, resultMap));

        }*/

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

        /*String memNos = "|11584609037713|11584609037192|11584609037223|";
        if(memNos.contains(mem_no)){
            var summary = new HashMap();
            summary.put("attendanceDays", 7);
            summary.put("totalExp", 200);
            summary.put("dalCnt", 15);

            var status = new HashMap();
            status.put("check_gift", "0");
            status.put("bonus", "2");
            status.put("exp", "50");
            status.put("dal", "200");

            var returnMap = new HashMap();
            returnMap.put("status", status);
            returnMap.put("dateList", "이건 필요없어요.. ");
            returnMap.put("summary", summary);

            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_더줘_성공, returnMap));
        }*/

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

            if(pAttendanceCheckLoadOutputVo.getThe_date().equals(DalbitUtil.getDate("yyyy-MM-dd"))){
                //if(pAttendanceCheckLoadOutputVo.getThe_date().equals(DalbitUtil.getDate("2020-08-02"))){

                pAttendanceCheckLoadOutputVo.setIs_today(1);

                if(pAttendanceCheckLoadOutputVo.getCheck_ok() == 0){
                    pAttendanceCheckLoadOutputVo.setCheck_ok(2);
                }
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
        Calendar dt_2nd = Calendar.getInstance();

        if("real".equals(DalbitUtil.getActiveProfile())){
            dt_2nd.set(2020, 6, 22, 23, 59, 59);
        }else{
            dt_2nd.set(2020, 6, 22, 17, 59, 59);
        }

        int round = 1;
        if(dt_2nd.getTimeInMillis() < today.getTimeInMillis()){
            // 2차 : 23~29일
            round = 2;
            if("real".equals(DalbitUtil.getActiveProfile())) {
                srt1.set(2020, 6,23,0,0,0);
            }else{
                srt1.set(2020, 6,22,18,0,0);
            }
            end1.set(2020, 6, 29, 23, 59, 59);
        }else{
            srt1.set(2020, 6,16,0,0,0);
            end1.set(2020, 6, 22, 23, 59, 59);
        }

        String state="finished";

        if(srt1.getTimeInMillis() <= today.getTimeInMillis() && end1.getTimeInMillis() >= today.getTimeInMillis()) {
            state = "ing";
        }

        HashMap map = new HashMap();
        map.put("state", state);
        map.put("round", round);

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
                result = gsonUtil.toJson(new JsonOutputVo(Status.라이징이벤트_실시간순위_데이터없음, map));
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


    /**
     * 출석완료 체크
     */
    public String callAttendanceCheck(HttpServletRequest request, P_AttendanceCheckVo pAttendanceCheckVo) {

        //비로그인은 무조건 체크 false로 리턴
        if(!DalbitUtil.isLogin(request)){
            var resultMap = new HashMap();
            resultMap.put("isCheck", false);
            return gsonUtil.toJson(new JsonOutputVo(Status.출석완료체크_성공, resultMap));
        }
        ProcedureVo procedureVo = new ProcedureVo(pAttendanceCheckVo);
        eventDao.callAttendanceCheck(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("isCheck", DalbitUtil.getStringMap(resultMap, "attendance_check").equals("0") ? true : false);
        procedureVo.setData(returnMap);

        if(procedureVo.getRet().equals(Status.출석완료체크_성공.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석완료체크_성공, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.출석완료체크_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석완료체크_회원아님));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.출석완료체크_실패));
        }

    }

    /**
     * 당첨자 휴대폰 입력
     */
    public String callPhoneInput(P_PhoneInputVo pPhoneInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pPhoneInputVo);
        eventDao.callPhoneInput(procedureVo);

        if(procedureVo.getRet().equals(Status.휴대폰입력_성공.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_성공, procedureVo.getData()));
        }else if(procedureVo.getRet().equals(Status.휴대폰입력_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_회원아님));
        }else if(procedureVo.getRet().equals(Status.휴대폰입력_당첨자아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_당첨자아님));
        }else if(procedureVo.getRet().equals(Status.휴대폰입력_자리수11미만.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_자리수11미만));
        }else if(procedureVo.getRet().equals(Status.휴대폰입력_입력종료시간지남.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_입력종료시간지남));
        }else if(procedureVo.getRet().equals(Status.휴대폰입력_이미입력된번호.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_이미입력된번호));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.휴대폰입력_실패));
        }
    }


    /**
     * 기프티콘 당첨자 리스트
     */
    public String callGifticonWinList(P_GifticonWinListInputVo pGifticonListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pGifticonListVo);

        List<P_GifticonWinListOutputVo> gifticonWinList = eventDao.callGifticonWinList(procedureVo);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(gifticonWinList)){
            resultMap.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.기프티콘_당첨자리스트없음, resultMap));
        }

        String result;
        if(gifticonWinList.size() > 0) {
            List<GifiticonWinListOutputVo> outList = new ArrayList();
            for(P_GifticonWinListOutputVo vo : gifticonWinList){
                GifiticonWinListOutputVo outVo = new GifiticonWinListOutputVo(vo);
                outList.add(outVo);
            }
            resultMap.put("list", outList);

            result = gsonUtil.toJson(new JsonOutputVo(Status.기프티콘_당첨자리스트조회, resultMap));
        }else if(procedureVo.getRet().equals(Status.기프티콘_당첨자리스트조회_회원아님.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.기프티콘_당첨자리스트조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.기프티콘_당첨자리스트조회_실패));
        }
        return result;
    }

    /**
     * 보름달 뜨는 날짜 조회
     */
    public String selectLunarDate() {
        HashMap resultMap = new HashMap();
        resultMap.put("lunarDt", eventDao.selectLunarDate());
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }
}
