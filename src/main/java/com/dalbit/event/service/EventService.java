package com.dalbit.event.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.vo.AdminMenuVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.EventCode;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.EventDao;
import com.dalbit.event.vo.*;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.event.vo.request.Apply004Vo;
import com.dalbit.event.vo.request.ApplyVo;
import com.dalbit.event.vo.request.CheckVo;
import com.dalbit.event.vo.request.EventGoodVo;
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
    AdminDao adminDao;

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
     * 출석체크 비회원접근 시 dummy data 생성
     */
    public HashMap AnonymousUserDummyData(){
        List dateList = new ArrayList();

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == 1){
            dayOfWeek += 7;
        }
        dayOfWeek--;
        cal2.add(Calendar.DATE, dayOfWeek * -1);
        for(int i=0; i<dayOfWeek; i++){
            cal2.add(Calendar.DATE, 1);
            String year = cal2.get(Calendar.YEAR) + "";

            int calMonth = cal2.get(Calendar.MONTH)+1;
            String month = calMonth < 10 ? "0" + calMonth : ""+calMonth;

            int calDate = cal2.get(Calendar.DATE);
            String date = calDate < 10 ? "0" + calDate : ""+calDate;

            String dummyDate = year + "-" + month + "-" + date;

            int is_today = DalbitUtil.getDate("yyyy-MM-dd").equals(dummyDate) ? 1 : 0;

            var map = new HashMap();
            map.put("the_date", dummyDate);
            map.put("the_day", i);
            map.put("check_ok", is_today == 1 ? 2 : 0);
            map.put("reward_exp", 0);
            map.put("reward_dal", 0);
            map.put("is_today", is_today);

            dateList.add(map);
        }

        var summary = new HashMap();
        summary.put("dalCnt", 0);
        summary.put("attendanceDays", 0);
        summary.put("totalExp", 0);

        var status = new HashMap();
        status.put("check_gift", "1");
        status.put("gifticon_day", "0");
        status.put("bonus", "0");
        status.put("phone_input", "0");
        status.put("exp", "0");
        status.put("dal", "0");

        var resultMap = new HashMap();
        resultMap.put("dateList", dateList);
        resultMap.put("summary", summary);
        resultMap.put("status", status);

        return resultMap;
    }

    /**
     * 출석체크 상태 체크
     */
    public String attendanceCheckStatus(HttpServletRequest request){

        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요_성공, AnonymousUserDummyData()));
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
        DeviceVo deviceVo = new DeviceVo(request);
        var paramMap = new HashMap();
        paramMap.put("mem_no", mem_no);
        paramMap.put("os", deviceVo.getOs());
        paramMap.put("deviceUuid", deviceVo.getDeviceUuid());

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

        //기프트콘 날짜 체크
        var lunarVo = eventDao.selectLunarDate();
        String today = DalbitUtil.convertDateFormat(new Date(), "yyyyMMdd");
        long checkCnt = dateList.stream().filter(checkVo -> checkVo.getCheck_ok() == 1).count();
        status.put("gifticon_day", (today.equals(lunarVo.getDate()) || checkCnt == 7) ? "1" : "0");

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
            resultMap.put("isCheck", true);
            resultMap.put("attendanceCheck", 0);
            return gsonUtil.toJson(new JsonOutputVo(Status.출석완료체크_성공, resultMap));
        }
        ProcedureVo procedureVo = new ProcedureVo(pAttendanceCheckVo);
        eventDao.callAttendanceCheck(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("isCheck", DalbitUtil.getStringMap(resultMap, "attendance_check").equals("2") ? false : true);
        returnMap.put("attendanceCheck", DalbitUtil.getIntMap(resultMap, "attendance_check"));

        if(procedureVo.getRet().equals(Status.출석완료체크_성공.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석완료체크_성공, returnMap));
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
        var lunarVo = eventDao.selectLunarDate();
        resultMap.put("lunarDt", lunarVo.getLunarDate());
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }

    public String selectPhotoList(HttpServletRequest request, PhotoEventInputVo photoEventInputVo){

        String mem_no = MemberVo.getMyMemNo(request);
        photoEventInputVo.setMem_no(mem_no);
        photoEventInputVo.setEvent_idx(EventCode.인증샷.getEventIdx());

        if(photoEventInputVo.getPage() <= 1){
            photoEventInputVo.setPage(0);
        }else{
            photoEventInputVo.setPage((photoEventInputVo.getPage() - 1) * photoEventInputVo.getRecords());
        }

        List<PhotoEventOutputVo> list = eventDao.selectPhotoList(photoEventInputVo);
        list.stream().parallel().forEach(item -> {
            item.setProfImg(new ImageVo(item.getImage_profile(), "n", DalbitUtil.getProperty("server.photo.url")));
        });
        int totCnt = eventDao.selectPhotoCnt(photoEventInputVo);

        HashMap map = new HashMap();
        map.put("list", list);
        map.put("totCnt", totCnt);
        map.put("isAdmin", isAdmin(request));
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
        return result;
    }

    public String insertPhoto(HttpServletRequest request, PhotoEventInputVo photoEventInputVo){

        int eventCheck = eventDateCheck(EventCode.인증샷.getEventIdx());
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        if(!EventCode.인증샷.isMulti()){
            var checkDuplJoin = new PhotoEventInputVo();
            checkDuplJoin.setEvent_idx(EventCode.인증샷.getEventIdx());
            checkDuplJoin.setSlct_type(1);
            checkDuplJoin.setMem_no(MemberVo.getMyMemNo(request));
            int totCnt = eventDao.selectPhotoCnt(checkDuplJoin);
            if(0 < totCnt){
                return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_이미참여));
            }
        }

        String mem_no = MemberVo.getMyMemNo(request);
        photoEventInputVo.setMem_no(mem_no);

        var deviceVo = new DeviceVo(request);

        var eventMemberVo = new EventMemberVo();
        eventMemberVo.setMem_no(mem_no);
        eventMemberVo.setEvent_idx(EventCode.인증샷.getEventIdx());
        eventMemberVo.setPlatform(deviceVo.getOs());
        eventDao.insertEventMember(eventMemberVo);

        photoEventInputVo.setEvent_idx(EventCode.인증샷.getEventIdx());
        photoEventInputVo.setEvent_member_idx(eventMemberVo.getEvent_member_idx());
        eventDao.insertPhoto(photoEventInputVo);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.생성));
        return result;
    }

    public String updatePhoto(HttpServletRequest request, PhotoEventInputVo photoEventInputVo){

        int eventCheck = eventDateCheck(EventCode.인증샷.getEventIdx());
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        photoEventInputVo.setMem_no(mem_no);

        eventDao.updatePhoto(photoEventInputVo);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.수정));
        return result;
    }

    public String deletePhoto(HttpServletRequest request, PhotoEventInputVo photoEventInputVo){

        int eventCheck = eventDateCheck(photoEventInputVo.getEvent_idx());
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        photoEventInputVo.setMem_no(mem_no);
        photoEventInputVo.setDel_yn(1);

        eventDao.deleteEventMemberPhoto(photoEventInputVo);
        eventDao.deletePhoto(photoEventInputVo);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.삭제));
        return result;
    }

    public String statusPhoto(HttpServletRequest request, PhotoEventInputVo photoEventInputVo){

        String mem_no = MemberVo.getMyMemNo(request);
        photoEventInputVo.setEvent_idx(EventCode.인증샷.getEventIdx());
        photoEventInputVo.setMem_no(mem_no);
        photoEventInputVo.setSlct_type(1);

        List<PhotoEventOutputVo> list = eventDao.selectPhotoList(photoEventInputVo);
        int status = list.size() == 0 ? 1 : 0;

        photoEventInputVo.setEvent_idx(EventCode.인증샷.getEventIdx());
        int pcStatus = eventDao.selectPhotoPcAirTime(photoEventInputVo);
        int pcCheck = pcStatus < 600 ? 0 : 1;

        int eventCheck = eventDateCheck(EventCode.인증샷.getEventIdx());

        var map = new HashMap();
        map.put("eventCheck", eventCheck);
        map.put("status", status);
        map.put("pcCheck", pcCheck);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
        return result;
    }

    public EventBasicVo getEventInfo(int idx){
        EventBasicVo eventBasicVo = eventDao.selectEventBasic(idx);
        return eventBasicVo;
    }

    public int eventDateCheck(int idx){
        EventBasicVo eventBasicVo = eventDao.selectEventBasic(idx);
        long startDatetime = eventBasicVo.getStart_datetime().getTime();
        long endDatetime = eventBasicVo.getEnd_datetime().getTime();
        long currentDatetime = new Date().getTime();
        return currentDatetime < startDatetime || endDatetime < currentDatetime ? 0 : 1;
    }

    public String selectKnowhowList(HttpServletRequest request, KnowhowEventInputVo knowhowEventInputVo){

        int eventIdx = EventCode.노하우.getEventIdx();

        String mem_no = MemberVo.getMyMemNo(request);
        knowhowEventInputVo.setMem_no(mem_no);
        knowhowEventInputVo.setEvent_idx(eventIdx);

        if(knowhowEventInputVo.getPage() <= 1){
            knowhowEventInputVo.setPage(0);
        }else{
            knowhowEventInputVo.setPage((knowhowEventInputVo.getPage() - 1) * knowhowEventInputVo.getRecords());
        }

        List<KnowhowEventOutputVo> list = eventDao.selectKnowhowList(knowhowEventInputVo);
        list.stream().parallel().forEach(item -> {
            item.setProfImg(new ImageVo(item.getImage_profile(), "n", DalbitUtil.getProperty("server.photo.url")));
        });
        int totCnt = eventDao.selectKnowhowCnt(knowhowEventInputVo);

        knowhowEventInputVo.setSlct_type(1);
        int myCnt = eventDao.selectKnowhowCnt(knowhowEventInputVo);

        HashMap map = new HashMap();
        map.put("list", list);
        map.put("totCnt", totCnt);
        map.put("myCnt", myCnt);
        map.put("isAdmin", isAdmin(request));
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
        return result;
    }

    /**
     * 노하우 이벤트 등록
     */
    public String insertKnowhow(HttpServletRequest request, KnowhowEventInputVo knowhowEventInputVo){

        Status status = null;
        if(DalbitUtil.isLogin(request)){
            ProcedureVo procedureVo = new ProcedureVo(new P_Apply003Vo(new ApplyVo(EventCode.노하우.getEventIdx()), knowhowEventInputVo, request));
            eventDao.callEventApply003(procedureVo);
            if(Status.이벤트_참여.getMessageCode().equals(procedureVo.getRet())){
                status = Status.이벤트_참여;
            }else if(Status.이벤트_체크_이미참여.getMessageCode().equals(procedureVo.getRet())){
                status = Status.이벤트_체크_이미참여;
            }else if(Status.이벤트_체크_자격안됨.getMessageCode().equals(procedureVo.getRet())){
                status = Status.이벤트_체크_자격안됨;
            }else if(Status.이벤트_없음_종료.getMessageCode().equals(procedureVo.getRet())){
                status = Status.이벤트_없음_종료;
            }else if(Status.이벤트_에러.getMessageCode().equals(procedureVo.getRet())){
                status = Status.이벤트_에러;
            }
        }else{
            status = Status.이벤트_비회원;
        }
        String result = gsonUtil.toJson(new JsonOutputVo(status));
        return result;
    }

    public String updateKnowhow(HttpServletRequest request, KnowhowEventInputVo knowhowEventInputVo){

        int event_idx = EventCode.노하우.getEventIdx();

        knowhowEventInputVo.setEvent_idx(event_idx);
        int eventCheck = eventDateCheck(event_idx);
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        String mem_no = MemberVo.getMyMemNo(request);
        knowhowEventInputVo.setMem_no(mem_no);

        ArrayList<String> list = new ArrayList();
        if(!DalbitUtil.isEmpty(knowhowEventInputVo.getImage_url())){
            list.add(knowhowEventInputVo.getImage_url());
            knowhowEventInputVo.setImage_url("");
        }

        if(!DalbitUtil.isEmpty(knowhowEventInputVo.getImage_url2())){
            list.add(knowhowEventInputVo.getImage_url2());
            knowhowEventInputVo.setImage_url2("");
        }

        if(!DalbitUtil.isEmpty(knowhowEventInputVo.getImage_url3())){
            list.add(knowhowEventInputVo.getImage_url3());
            knowhowEventInputVo.setImage_url3("");
        }

        for(int i=0 ; i<list.size(); i++){
            if(i == 0){
                knowhowEventInputVo.setImage_url(list.get(i));
            }else if(i == 1){
                knowhowEventInputVo.setImage_url2(list.get(i));
            }else if(i == 2){
                knowhowEventInputVo.setImage_url3(list.get(i));
            }
        }

        int updateResult = eventDao.updateKnowhow(knowhowEventInputVo);
        String result = null;
        if(updateResult == 0){
            result = gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.수정));
        }

        return result;
    }

    public String detailKnowhow(HttpServletRequest request, KnowhowEventInputVo knowhowEventInputVo){

        int event_idx = EventCode.노하우.getEventIdx();

        knowhowEventInputVo.setEvent_idx(EventCode.노하우.getEventIdx());
        knowhowEventInputVo.setMem_no(MemberVo.getMyMemNo(request));
        var resultMap = new HashMap();

        if(knowhowEventInputVo.getIs_detail() == 1){
            eventDao.updatePhotoViewCnt(knowhowEventInputVo.getIdx());
        }

        KnowhowEventOutputVo detail = eventDao.selectKnowhowDetail(knowhowEventInputVo);
        if(DalbitUtil.isEmpty(detail)){
            return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }

        detail.setProfImg(new ImageVo(detail.getImage_profile(), "n", DalbitUtil.getProperty("server.photo.url")));

        resultMap.put("isAdmin", isAdmin(request));
        resultMap.put("detail", detail);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
        return result;
    }

    public String eventGood(HttpServletRequest request, EventGoodVo eventGoodVo){

        eventGoodVo.setMem_no(MemberVo.getMyMemNo(request));
        ProcedureVo procedureVo = new ProcedureVo(eventGoodVo);
        eventDao.callEventGood(procedureVo);

        var procedureResult = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

        var resultMap = new HashMap();
        resultMap.put("good_cnt", DalbitUtil.getIntMap(procedureResult, "good_cnt"));

        if(Status.노하우_이벤트_좋아요.getMessageCode().equals(procedureVo.getRet())){
            return gsonUtil.toJson(new JsonOutputVo(Status.노하우_이벤트_좋아요, resultMap));
        }else if(Status.노하우_이벤트_좋아요취소.getMessageCode().equals(procedureVo.getRet())){
            return gsonUtil.toJson(new JsonOutputVo(Status.노하우_이벤트_좋아요취소, resultMap));
        }else if(Status.노하우_이벤트_회원아님.getMessageCode().equals(procedureVo.getRet())){
            return gsonUtil.toJson(new JsonOutputVo(Status.노하우_이벤트_회원아님));
        }else if(Status.노하우_이벤트_이벤트없음.getMessageCode().equals(procedureVo.getRet())){
            return gsonUtil.toJson(new JsonOutputVo(Status.노하우_이벤트_이벤트없음));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류));
        }
    }

    public int isAdmin(HttpServletRequest request){
        int isAdmin = 0;
        if(DalbitUtil.isLogin(request)){
            SearchVo searchVo = new SearchVo();
            searchVo.setMem_no(MemberVo.getMyMemNo(request));
            ArrayList<AdminMenuVo> menuList = adminDao.selectMobileAdminMenuAuth(searchVo);
            isAdmin = DalbitUtil.isEmpty(menuList) ? 0 : 1;
        }
        return isAdmin;
    }

    public HashMap eventCheck(CheckVo checkVo, HttpServletRequest request){
        HashMap resultMap = new HashMap();

        if(DalbitUtil.isLogin(request)){
            ProcedureVo procedureVo = new ProcedureVo(new P_CheckVo(checkVo, request));
            eventDao.callEventApplyCheck(procedureVo);
            if(Status.이벤트_체크_참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_참여);
            }else if(Status.이벤트_체크_이미참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_이미참여);
            }else if(Status.이벤트_체크_자격안됨.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_자격안됨);
            }
        }else{
            resultMap.put("status", Status.이벤트_비회원);
        }

        return resultMap;
    }

    public HashMap eventCheck004(CheckVo checkVo, HttpServletRequest request){
        HashMap resultMap = new HashMap();

        if(DalbitUtil.isLogin(request)){
            checkVo.setEventIdx(4);
            ProcedureVo procedureVo = new ProcedureVo(new P_CheckVo(checkVo, request));
            eventDao.callEventApplyCheck004(procedureVo);
            if(Status.이벤트_체크_참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_참여);
            }else if(Status.이벤트_체크_이미참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_이미참여);
            }else if(Status.이벤트_체크_자격안됨04.getMessageCode().equals(procedureVo.getRet())){
                HashMap returnMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                HashMap data = new HashMap();
                data.put("airHour", returnMap.get("airhour"));
                resultMap.put("status", Status.이벤트_체크_자격안됨04);
                resultMap.put("data", data);
            }
        }else{
            resultMap.put("status", Status.이벤트_비회원);
        }

        return resultMap;
    }

    public HashMap eventApply(ApplyVo applyVo, HttpServletRequest request){
        HashMap resultMap = new HashMap();
        if(DalbitUtil.isLogin(request)){
            ProcedureVo procedureVo = new ProcedureVo(new P_ApplyVo(applyVo, request));
            eventDao.callEventApplySP(procedureVo);
            if(Status.이벤트_참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_참여);
            }else if(Status.이벤트_체크_이미참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_이미참여);
            }else if(Status.이벤트_체크_자격안됨.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_자격안됨);
            }else if(Status.이벤트_없음_종료.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_없음_종료);
            }else if(Status.이벤트_에러.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_에러);
            }
        }else{
            resultMap.put("status", Status.이벤트_비회원);
        }
        return resultMap;
    }

    public HashMap eventApply004(Apply004Vo applyVo, HttpServletRequest request){
        HashMap resultMap = new HashMap();
        if(DalbitUtil.isLogin(request)){
            applyVo.setEventIdx(4);
            ProcedureVo procedureVo = new ProcedureVo(new P_ApplyVo(applyVo, request));
            eventDao.callEventApply004(procedureVo);
            if(Status.이벤트_참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_참여);
            }else if(Status.이벤트_체크_이미참여.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_이미참여);
            }else if(Status.이벤트_체크_자격안됨04.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_체크_자격안됨04);
            }else if(Status.이벤트_없음_종료.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_없음_종료);
            }else if(Status.이벤트_에러.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_에러);
            }else if(Status.이벤트_비회원.getMessageCode().equals(procedureVo.getRet())){
                resultMap.put("status", Status.이벤트_비회원);
            }
        }else{
            resultMap.put("status", Status.이벤트_비회원);
        }
        return resultMap;
    }


    /**
     * 출석완료 체크 방송방 생성, 참여 시
     */
    public HashMap callAttendanceCheckMap(int isLogin, P_AttendanceCheckVo pAttendanceCheckVo) {

        HashMap returnMap = new HashMap();
        //비로그인은 무조건 체크 true 리턴
        if (isLogin == 0) {
            returnMap.put("isCheck", true);
            return returnMap;
        }
        ProcedureVo procedureVo = new ProcedureVo(pAttendanceCheckVo);
        eventDao.callAttendanceCheck(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        returnMap.put("isCheck", DalbitUtil.getStringMap(resultMap, "attendance_check").equals("2") ? false : true);

        return returnMap;

    }
}
