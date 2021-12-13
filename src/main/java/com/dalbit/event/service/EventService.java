package com.dalbit.event.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.vo.AdminMenuVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.EventCode;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_SelfAuthVo;
import com.dalbit.event.dao.EventDao;
import com.dalbit.event.proc.Event;
import com.dalbit.event.vo.*;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.event.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
    @Autowired
    CommonService commonService;
    @Autowired
    RestService restService;

    @Autowired Event event;

    public String event200608Term(){
        List<HashMap> eventTerm = new ArrayList<>();
        List<HashMap> eventTmp = new ArrayList<>();
        Calendar today = Calendar.getInstance();

        HashMap term1 = new HashMap();
        Calendar srt1 = Calendar.getInstance();
        Calendar end1 = Calendar.getInstance();
        srt1.set(2020, 5,8,0,0,0);
        end1.set(2021, 5, 14, 23, 59, 59);
        term1.put("round", 1);
        term1.put("srtDt", srt1);
        term1.put("endDt", end1);

        HashMap term2 = new HashMap();
        Calendar srt2 = Calendar.getInstance();
        Calendar end2 = Calendar.getInstance();
        srt2.set(2020, 5,15,0,0,0);
        end2.set(2021, 5, 21, 23, 59, 59);
        term2.put("round", 2);
        term2.put("srtDt", srt2);
        term2.put("endDt", end2);

        HashMap term3 = new HashMap();
        Calendar srt3 = Calendar.getInstance();
        Calendar end3 = Calendar.getInstance();
        srt3.set(2020, 5,22,0,0,0);
        end3.set(2021, 5, 28, 23, 59, 59);
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
        paramMap.put("ip", deviceVo.getIp());

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

        }else if(procedureVo.getRet().equals(Status.출석체크이벤트_출석_실패_동일아이피중복불가.getMessageCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.출석체크이벤트_출석_실패_동일아이피중복불가));

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

        //월~토 중 1일 이상 출석 실패 한 자가 일요일에 출석할 시
        if(!DalbitUtil.isEmpty(status.get("the_day")) && status.get("the_day").equals("6")){
            if(status.get("bonus").equals("0")){
                status.put("sunday_all_day", "0");
            }else{
                status.put("sunday_all_day", "1");
            }
        }

        //본인인증 체크 여부 값 전달
        Code code_define = Code.출석체크이벤트_본인인증체크;
        var authCheck = commonService.selectCodeDefine(new CodeVo(code_define.getCode(), code_define.getDesc()));
        String authCheckYn = "N";
        if (!DalbitUtil.isEmpty(authCheck)) {
            if("Y".equals(authCheck.getValue())) {
                authCheckYn = "Y";
            }
        }

        var returnMap = new HashMap();
        returnMap.put("status", status);
        returnMap.put("dateList", dateList);
        returnMap.put("summary", summary);
        returnMap.put("authCheckYn", authCheckYn);

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
        int attendance_check = DalbitUtil.getIntMap(resultMap, "attendance_check"); //0: 출석체크 완료, 1: 미완료 시간부족, 2: 미완료 시간충족
        boolean isRoulette = DalbitUtil.getIntMap(resultMap, "couponCnt") > 0;   //룰렛가능 여부
        int userEventCheck;                                                           //-1: 버튼없음, 0: 출석기본, 1: 출석애니메이션, 2:룰렛에니메이션
        String eventIconUrl;
        boolean iosAudit = false;   //ios 심사중 여부
        DeviceVo deviceVo = new DeviceVo(request);
        if(deviceVo.getOs() == 2) {
            var iosCodeVo = commonService.selectCodeDefine(new CodeVo(Code.IOS심사중여부.getCode(), Code.IOS심사중여부.getDesc()));
            if (!DalbitUtil.isEmpty(iosCodeVo)) {
                if("Y".equals(iosCodeVo.getValue())) {
                    iosAudit = true;
                }
            }
        }

        //출석 불가 & 룰렛 불가
        if(attendance_check == 1 && !isRoulette){
            userEventCheck = 0;
            eventIconUrl="";
        //출석 불가 & 룰렛 가능
        }else if(attendance_check == 1 && isRoulette){
            if(iosAudit){   //ios심사중인 경우 출석기본
                userEventCheck = 0;
                eventIconUrl="";
            }else{
                userEventCheck = 2;
                eventIconUrl="";
            }
        //출석 가능 & 룰렛 불가
        }else if(attendance_check == 2 && !isRoulette){
            userEventCheck = 1;
            eventIconUrl="";
        //출석 가능 & 룰렛 가능
        }else if(attendance_check == 2 && isRoulette){
            userEventCheck = 1;
            eventIconUrl="";
        //출석 완료 & 룰렛 불가
        }else if(attendance_check == 0 && !isRoulette){
            userEventCheck = -1;
            eventIconUrl="";
        //출석 완료 & 룰렛 가능
        }else if(attendance_check == 0 && isRoulette){
            if(iosAudit){   //ios심사중인 경우 버튼없음
                userEventCheck = -1;
                eventIconUrl="";
            }else{
                userEventCheck = 2;
                eventIconUrl="";
            }
        //나머지 버튼 없음
        }else{
            userEventCheck = -1;
            eventIconUrl="";
        }

        returnMap.put("isCheck", attendance_check != 2);
        returnMap.put("attendanceCheck", attendance_check);
        returnMap.put("userEventCheck", userEventCheck);
        returnMap.put("eventIconUrl", eventIconUrl);

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
        int totCnt = photoEventInputVo.getTotalCnt();

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
            eventDao.selectPhotoList(checkDuplJoin);
            int totCnt = checkDuplJoin.getTotalCnt();
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

    public int eventDateCheck(EventBasicVo eventBasicVo){
        long startDatetime = eventBasicVo.getStart_date().getTime();
        long endDatetime = eventBasicVo.getEnd_date().getTime();
        long currentDatetime = new Date().getTime();
        return currentDatetime < startDatetime || endDatetime < currentDatetime ? 0 : 1;
    }

    public int eventDateCheck(int idx){
        EventBasicVo eventBasicVo = eventDao.selectEventBasic(idx);
        if(eventBasicVo == null || eventBasicVo.getStart_date() == null || eventBasicVo.getEnd_date() == null) {
            return 0;
        }
        long startDatetime = eventBasicVo.getStart_date().getTime();
        long endDatetime = eventBasicVo.getEnd_date().getTime();
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
        int totCnt = knowhowEventInputVo.getTotalCnt();

        knowhowEventInputVo.setSlct_type(1);
        eventDao.selectKnowhowList(knowhowEventInputVo);
        int myCnt = knowhowEventInputVo.getTotalCnt();

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


    /**
     * 이벤트 리스트 조회
     */
    public String callEventPageList(P_EventPageListInputVo pEventPageListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventPageListInputVo);
        ArrayList<P_EventPageListOutputVo> eventList = eventDao.callEventPageList(procedureVo);
        String result="";

        if(eventList.size() > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_리스트조회_성공, eventList));
        } else if (Status.이벤트_리스트조회_실패.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_리스트조회_실패));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_리스트조회_데이터없음));
        }

        return result;
    }

    /**
     * 이벤트 당첨자 명단 조회
     */
    public String callEventPageWinList(P_EventPageWinListInputVo pEventPageWinListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventPageWinListInputVo);
        ArrayList<P_EventPageWinListOutputVo> winList = eventDao.callEventPageWinList(procedureVo);
        String result = "";

        ArrayList rankList = new ArrayList();

        // 등수 (중복제거) 뽑아내기
        if(!DalbitUtil.isEmpty(winList)) {
            for (int i = 0; i < winList.size(); i++) {
                HashMap rank = new HashMap();
                rank.put("rank", winList.get(i).getPrizeRank());
                rank.put("rankName", winList.get(i).getPrizeName());
                rank.put("winnerCnt", winList.get(i).getPrizeCnt());
                if(!rankList.contains(rank)) {
                    rankList.add(rank);
                }
            }
        }

        if(!DalbitUtil.isEmpty(winList) && !DalbitUtil.isEmpty(rankList)) {
            for (int i = 0; i < rankList.size(); i++) {
                List<P_EventPageWinListOutputVo> winnerList = new ArrayList<>();
                for (int j = 0; j < winList.size(); j++) {
                    if(DalbitUtil.getIntMap((HashMap) rankList.get(i), "rank") == winList.get(j).getPrizeRank()){
                        winnerList.add(winList.get(j));
                    }
                }
                ((HashMap) rankList.get(i)).put("winnerList", winnerList);
            }
        }
        HashMap map = new HashMap();
        map.put("rankList", rankList);

        if(winList.size() > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자명단조회_성공, map));
        } else if (Status.이벤트_당첨자명단조회_이벤트번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자명단조회_이벤트번호없음));
        } else if (Status.이벤트_당첨자명단조회_실패.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자명단조회_실패));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자명단조회_결과없음));
        }

        return result;

    }

    /**
     * 이벤트 당첨 여부 조회
     */
    public String callEventPageWinResult(P_EventPageWinResultInputVo pEventPageWinResultInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventPageWinResultInputVo);
        ArrayList<P_EventPageWinResultOutputVo> resultList = eventDao.callEventPageWinResult(procedureVo);

        String result;

        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨여부조회_성공, resultList));
        } else if (Status.이벤트_당첨여부조회_당첨자아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨여부조회_당첨자아님));
        } else if (Status.이벤트_당첨여부조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨여부조회_회원아님));
        } else if (Status.이벤트_당첨여부조회_이벤트번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨여부조회_이벤트번호없음));
        } else if (Status.이벤트_당첨여부조회_결과없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨여부조회_결과없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨여부조회_실패));
        }

        return result;
    }

    /**
     * 이벤트 당첨자 경품 수령방법 선택
     */
    public String callEventPagePrizeReceiveWay(P_EventPagePrizeReceiveVo pEventPagePrizeReceiveVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventPagePrizeReceiveVo);
        eventDao.callEventPagePrizeReceiveWay(procedureVo);

        String result;

        if(Status.이벤트_경품수령방법입력_경품받기_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_경품받기_성공));
        } else if (Status.이벤트_경품수령방법입력_달로받기_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_달로받기_성공));
        } else if (Status.이벤트_경품수령방법입력_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_회원아님));
        } else if (Status.이벤트_경품수령방법입력_이벤트번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_이벤트번호없음));
        } else if (Status.이벤트_경품수령방법입력_결과없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_결과없음));
        } else if (Status.이벤트_경품수령방법입력_당첨자아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_당첨자아님));
        } else if (Status.이벤트_경품수령방법입력_이미경품선택함_입력불가능.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_이미경품선택함_입력불가능));
        } else if (Status.이벤트_경품수령방법입력_수령방법_오류.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_수령방법_오류));
        } else if (Status.이벤트_경품수령방법입력_입금확인후_수정불가능.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_입금확인후_수정불가능));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_경품수령방법입력_실패));
        }

        return result;
    }

    /**
     * 이벤트 당첨자 등록정보 조회
     */
    public String callEventPageWinnerAddInfoSelect(P_EventPageWinnerAddInfoListInputVo pEventPageWinnerAddInfoListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventPageWinnerAddInfoListInputVo);
        eventDao.callEventPageWinnerAddInfoSelect(procedureVo);
        P_EventPageWinnerAddInfoListOutputVo addInfoOutput = new Gson().fromJson(procedureVo.getExt(), P_EventPageWinnerAddInfoListOutputVo.class);

        String result;

        if(Status.이벤트_당첨자등록정보조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_성공, addInfoOutput));
        } else if (Status.이벤트_당첨자등록정보조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_회원아님));
        } else if (Status.이벤트_당첨자등록정보조회_이벤트번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_이벤트번호없음));
        } else if (Status.이벤트_당첨자등록정보조회_결과없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_결과없음));
        } else if (Status.이벤트_당첨자등록정보조회_당첨자아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_당첨자아님));
        } else if (Status.이벤트_당첨자등록정보조회_경품번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_경품번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보조회_실패));
        }

        return result;
    }

    /**
     * 이벤트 당첨자 이름/핸드폰 번호 조회
     */
    public String callEventPageWinnerInfoFormat(P_EventPageWinnerInfoFormatVo pEventPageWinnerInfoFormatVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventPageWinnerInfoFormatVo);
        P_EventPageWinnerInfoFormatVo info = eventDao.callEventPageWinnerInfoFormat(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            log.debug(info.toString());
            result = gsonUtil.toJson(new JsonOutputVo(Status.조회, info));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류));
        }

        return result;
    }

    /**
     * 이벤트 당첨자 등록정보 등록/수정
     */
    public String callEventPageWinnerAddInfoEdit(P_EventPageWinnerAddInfoEditVo pEventPageWinnerAddInfoEditVo, HttpServletRequest request) throws GlobalException {
        Boolean isDone = false; // tmp
        String addFile1 = pEventPageWinnerAddInfoEditVo.getWinner_add_file_1();
        String addFile2 = pEventPageWinnerAddInfoEditVo.getWinner_add_file_2();

        if (!DalbitUtil.isEmpty(addFile1) && addFile1.startsWith(Code.포토_경품수령신청_임시_PREFIX.getCode())) {
            isDone = true;
            addFile1 = DalbitUtil.replacePath(addFile1);
        }

        if (!DalbitUtil.isEmpty(addFile2) && addFile2.startsWith(Code.포토_경품수령신청_임시_PREFIX.getCode())) {
            isDone = true;
            addFile2 = DalbitUtil.replacePath(addFile2);

            //법정대리인 인증정보 파일 업데이트
            P_SelfAuthVo pSelfAuthVo = new P_SelfAuthVo();
            pSelfAuthVo.setMem_no(MemberVo.getMyMemNo(request));
            pSelfAuthVo.setAdd_file(addFile2);
            int success = commonService.updateMemberCertificationFile(pSelfAuthVo);
            if(success > 0) {
                log.info("법정대리인(보호자) 서류 업데이트 성공");
            }
        }

        pEventPageWinnerAddInfoEditVo.setWinner_add_file_1(addFile1);
        pEventPageWinnerAddInfoEditVo.setWinner_add_file_2(addFile2);
        ProcedureVo procedureVo = new ProcedureVo(pEventPageWinnerAddInfoEditVo);
        eventDao.callEventPageWinnerAddInfoEdit(procedureVo);

        String result;

        if(Status.이벤트_당첨자등록정보수정_등록성공.getMessageCode().equals(procedureVo.getRet())) {
            if(isDone) {
                if(!DalbitUtil.isEmpty(pEventPageWinnerAddInfoEditVo.getWinner_add_file_1())) {
                    restService.imgDone(DalbitUtil.replaceDonePath(pEventPageWinnerAddInfoEditVo.getWinner_add_file_1()), request);
                }
                if(!DalbitUtil.isEmpty(pEventPageWinnerAddInfoEditVo.getWinner_add_file_2())) {
                    restService.imgDone(DalbitUtil.replaceDonePath(pEventPageWinnerAddInfoEditVo.getWinner_add_file_2()), request);
                }
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_등록성공));
        } else if (Status.이벤트_당첨자등록정보수정_성공.getMessageCode().equals(procedureVo.getRet())) {
            if(isDone) {
                if(!DalbitUtil.isEmpty(pEventPageWinnerAddInfoEditVo.getWinner_add_file_1())) {
                    restService.imgDone(DalbitUtil.replaceDonePath(pEventPageWinnerAddInfoEditVo.getWinner_add_file_1()), request);
                }
                if(!DalbitUtil.isEmpty(pEventPageWinnerAddInfoEditVo.getWinner_add_file_2())) {
                    restService.imgDone(DalbitUtil.replaceDonePath(pEventPageWinnerAddInfoEditVo.getWinner_add_file_2()), request);
                }
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_성공));
        } else if (Status.이벤트_당첨자등록정보수정_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_회원아님));
        } else if (Status.이벤트_당첨자등록정보수정_이벤트번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_이벤트번호없음));
        } else if (Status.이벤트_당첨자등록정보수정_결과없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_결과없음));
        } else if (Status.이벤트_당첨자등록정보수정_당첨자아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_당첨자아님));
        } else if (Status.이벤트_당첨자등록정보수정_입금확인후_수정불가능.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_입금확인후_수정불가능));
        } else if (Status.이벤트_당첨자등록정보수정_경품번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_경품번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_당첨자등록정보수정_실패));
        }

        return result;
    }

    /**
     * 타임 이벤트 정보 조회
     */
    public String selectTimeEventInfo(TimeEventVo timeEventVo){
        TimeEventVo timeEventInfo = eventDao.selectTimeEventInfo(timeEventVo);
        if(DalbitUtil.isEmpty(timeEventInfo)){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_진행중인이벤트없음));
        }

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, timeEventInfo));
    }


    /**
     * 오픈 기념 이벤트
     */
    public String callOpenEvent(P_OpenEventVo pOpenEventVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pOpenEventVo);
        List<P_OpenEventVo> openEventVoList = eventDao.callOpenEvent(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap rankInfo = new HashMap();
            List<OpenEventOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(openEventVoList)){
                for (int i=0; i<openEventVoList.size(); i++){
                    outVoList.add(new OpenEventOutVo(openEventVoList.get(i)));
                }
            }

            //OpenEventExtVo openEventExtVo = new Gson().fromJson(procedureVo.getExt(), OpenEventExtVo.class);

            rankInfo.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
            rankInfo.put("myPoint", DalbitUtil.getIntMap(resultMap, "myPoint"));
            rankInfo.put("diffPoint", DalbitUtil.getIntMap(resultMap, "diffPoint"));
            rankInfo.put("opRank", DalbitUtil.getIntMap(resultMap, "upRank"));
            rankInfo.put("startDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "startDate")));
            rankInfo.put("startTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "startDate")));
            rankInfo.put("endDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "endDate")));
            rankInfo.put("endTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "endDate")));
            rankInfo.put("detailDesc", DalbitUtil.getStringMap(resultMap, "detailDesc"));
            rankInfo.put("giftDesc", DalbitUtil.getStringMap(resultMap, "giftDesc"));
            rankInfo.put("list", outVoList);
            //openEventExtVo.setList(outVoList);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(Status.오픈이벤트조회_성공, rankInfo));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.오픈이벤트조회_실패));
        }
        return result;
    }


    /**
     * 일간 최고 DJ/FAN 보기
     */
    public String callOpenEventDailyBest(P_OpenEventBestListVo pOpenEventBestListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pOpenEventBestListVo);
        List<P_OpenEventBestListVo> openEventBestListVo = eventDao.callOpenEventDailyBest(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap bestList = new HashMap();
            List<OpenEventBestListOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(openEventBestListVo)){
                for (int i=0; i<openEventBestListVo.size(); i++){
                    outVoList.add(new OpenEventBestListOutVo(openEventBestListVo.get(i)));
                }
            }

            bestList.put("list", outVoList);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(Status.일간최고조회_성공, bestList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.일간최고조회_실패));
        }
        return result;
    }


    /**
     * 스페셜 리그 조회
     */
    public String callSpecialLeague(P_SpecialLeagueVo pSpecialLeagueVo) {
        ProcedureVo procedureVo = new ProcedureVo(pSpecialLeagueVo);
        List<P_SpecialLeagueVo> specialLeagueVoList = eventDao.callSpecialLeague(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap specialLeague = new HashMap();
            List<SpecialLeagueListOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(specialLeagueVoList)){
                for (int i=0; i<specialLeagueVoList.size(); i++){
                    outVoList.add(new SpecialLeagueListOutVo(specialLeagueVoList.get(i)));
                }
            }

            specialLeague.put("list", outVoList);
            specialLeague.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
            specialLeague.put("myUpDown", DalbitUtil.getStringMap(resultMap, "myUpDown"));
            specialLeague.put("myGiftPoint", DalbitUtil.getIntMap(resultMap, "myGiftPoint"));
            specialLeague.put("myListenerPoint", DalbitUtil.getIntMap(resultMap, "myListenerPoint"));
            specialLeague.put("myGoodPoint", DalbitUtil.getIntMap(resultMap, "myGoodPoint"));
            specialLeague.put("myFanPoint", DalbitUtil.getStringMap(resultMap, "myFanPoint"));
            specialLeague.put("myTotalPoint", DalbitUtil.getIntMap(resultMap, "myTotalPoint"));
            specialLeague.put("nowRoundNo", DalbitUtil.getIntMap(resultMap, "nowRoundNo"));
            specialLeague.put("isSpecial", DalbitUtil.getIntMap(resultMap, "isSpecial") == 1);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(Status.스페셜리그_조회_성공, specialLeague));
        } else if (Status.스페셜리그_조회_기수오버.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜리그_조회_기수오버));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜리그_조회_실패));
        }
        return result;
    }


    /**
     * 챔피언십 조회
     */
    public String callChampionshipSelect(P_ChampionshipVo pChampionshipVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChampionshipVo);
        List<P_ChampionshipVo> championshipList = eventDao.callChampionshipSelect(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap rankInfo = new HashMap();
            List<ChampionshipOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(championshipList)){
                for (int i=0; i<championshipList.size(); i++){
                    outVoList.add(new ChampionshipOutVo(championshipList.get(i)));
                }
            }

            rankInfo.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
            rankInfo.put("myPoint", DalbitUtil.getIntMap(resultMap, "myPoint"));
            rankInfo.put("diffPoint", DalbitUtil.getIntMap(resultMap, "diffPoint"));
            rankInfo.put("opRank", DalbitUtil.getIntMap(resultMap, "upRank"));
            rankInfo.put("startDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "startDate")));
            rankInfo.put("startTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "startDate")));
            rankInfo.put("endDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "endDate")));
            rankInfo.put("endTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "endDate")));
            rankInfo.put("detailDesc", DalbitUtil.getStringMap(resultMap, "detailDesc"));
            rankInfo.put("giftDesc", DalbitUtil.getStringMap(resultMap, "giftDesc"));
            rankInfo.put("topBgImg", DalbitUtil.getStringMap(resultMap, "topBgImg"));
            rankInfo.put("djBgImg", DalbitUtil.getStringMap(resultMap, "djBgImg"));
            rankInfo.put("fanBgImg", DalbitUtil.getStringMap(resultMap, "fanBgImg"));
            rankInfo.put("nowEventNo", DalbitUtil.getIntMap(resultMap, "now_event_no"));
            rankInfo.put("list", outVoList);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(Status.챔피언십조회_성공, rankInfo));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.챔피언십조회_실패));
        }
        return result;
    }

    /**
     * 챔피언십 승점 현황 조회
     */
    public String callChampionshipPointSelect(P_ChampionshipPointVo pChampionshipPointVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChampionshipPointVo);
        List<P_ChampionshipPointVo> championshipPointList = eventDao.callChampionshipPointSelect(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap rankInfo = new HashMap();
            List<ChampionshipPointOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(championshipPointList)){
                for (int i=0; i<championshipPointList.size(); i++){
                    outVoList.add(new ChampionshipPointOutVo(championshipPointList.get(i)));
                }
            }

            rankInfo.put("myWinPoint1", DalbitUtil.getIntMap(resultMap, "winPoint1"));
            rankInfo.put("myWinPoint2", DalbitUtil.getIntMap(resultMap, "winPoint2"));
            rankInfo.put("myWinPoint3", DalbitUtil.getIntMap(resultMap, "winPoint3"));
            rankInfo.put("myWinPoint4", DalbitUtil.getIntMap(resultMap, "winPoint4"));
            rankInfo.put("myWinPoint5", DalbitUtil.getIntMap(resultMap, "winPoint5"));
            rankInfo.put("totalWinPoint", DalbitUtil.getIntMap(resultMap, "total_winPoint"));
            rankInfo.put("myRank1", DalbitUtil.getIntMap(resultMap, "rank1"));
            rankInfo.put("myRank2", DalbitUtil.getIntMap(resultMap, "rank2"));
            rankInfo.put("myRank3", DalbitUtil.getIntMap(resultMap, "rank3"));
            rankInfo.put("myRank4", DalbitUtil.getIntMap(resultMap, "rank4"));
            rankInfo.put("myRank5", DalbitUtil.getIntMap(resultMap, "rank5"));
            rankInfo.put("totalRank", DalbitUtil.getIntMap(resultMap, "total_rank"));
            rankInfo.put("nowEventNo", DalbitUtil.getIntMap(resultMap, "now_event_no"));
            rankInfo.put("pointDesc", DalbitUtil.getStringMap(resultMap, "pointDesc"));

            rankInfo.put("list", outVoList);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(Status.챔피언십_승점조회_성공, rankInfo));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.챔피언십_승점조회_실패));
        }
        return result;
    }


    /**
     * 챔피언십 선물받기(부스터)
     */
    public String callChampionshipGift(P_ChampionshipGiftVo pChampionshipGiftVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChampionshipGiftVo);
        eventDao.callChampionshipGift(procedureVo);

        String result;
        if(Status.선물받기_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받기_성공));
        } else if (Status.선물받기_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받기_회원아님));
        } else if (Status.선물받기_이미받음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받기_이미받음));
        } else if (Status.선물받기_10점안됨.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받기_10점안됨));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받기_실패));
        }

        return result;
    }

    /**
     * 아이템 지급[서비스]
     */
    public int eventItemIns(ItemInsVo itemInsVo) {
        return event.eventItemIns(itemInsVo);
    }

    /* 깐부 이벤트~~~ */
    /**
     * 깐부 이벤트 회차 정보
     */
    public GganbuRoundInfoOutputVo getGganbuRoundInfo() {
        GganbuRoundInfoReqVo getData = event.getGganbuRoundInfo();
        return new GganbuRoundInfoOutputVo(
            getData.getGganbu_no(),
            getData.getStart_date(),
            getData.getEnd_date(),
            getData.getIns_date()
        );
    }

    /**
     * 깐부 이벤트 회차 정보 예외처리
     */
    private String gganbuNoCheck(String gganbuNo) {
        if(StringUtils.equals(gganbuNo, "1") || StringUtils.equals(gganbuNo, "2") ||
            StringUtils.equals(gganbuNo, "3") || StringUtils.equals(gganbuNo, "4")
        ) {
            return gganbuNo;
        }

        GganbuRoundInfoOutputVo gganbuRoundInfo = getGganbuRoundInfo(); // 깐부 회차정보
        return gganbuRoundInfo.getGganbuNo();
    }

    /**
     * 깐부 신청
     */
    public int gganbuMemReqIns(GganbuMemReqInsVo gganbuMemReqInsVo) {
        gganbuMemReqInsVo.setGganbuNo(gganbuNoCheck(gganbuMemReqInsVo.getGganbuNo()));
        return event.gganbuMemReqIns(gganbuMemReqInsVo);
    }

    /**
     * 깐부 신청 취소
     */
    public int gganbuMemReqCancel(GganbuMemReqCancelVo gganbuMemReqCancelVo) {
        gganbuMemReqCancelVo.setGganbuNo(gganbuNoCheck(gganbuMemReqCancelVo.getGganbuNo()));
        return event.gganbuMemReqCancel(gganbuMemReqCancelVo);
    }

    /**
     * 깐부 신청 리스트
     */
    public Map<String, Object> gganbuMemReqList(GganbuMemReqListInputVo gganbuMemReqListInputVo) {
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuMemReqList(gganbuMemReqListInputVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuMemReqListOutputVo> list = DBUtil.getList(objList, 1, GganbuMemReqListOutputVo.class);
        for(GganbuMemReqListOutputVo vo : list) {
            vo.setAverage_level((vo.getMem_level() + vo.getPtr_mem_level()) / 2);

            vo.setMem_profile(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
            vo.setPtr_mem_profile(new ImageVo(vo.getPtr_mem_profile(), vo.getPtr_mem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }
        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     * 깐부 신청 수락
     */
    public int gganbuMemIns(GganbuMemInsVo gganbuMemInsVo) {
        gganbuMemInsVo.setGganbuNo(gganbuNoCheck(gganbuMemInsVo.getGganbuNo()));
        return event.gganbuMemIns(gganbuMemInsVo);
    }

    /**
     * 깐부 여부 체크
     */
    public int gganbuMemChk(GganbuMemChkVo gganbuMemChkVo) {
        gganbuMemChkVo.setGganbuNo(gganbuNoCheck(gganbuMemChkVo.getGganbuNo()));
        return event.gganbuMemChk(gganbuMemChkVo);
    }

    /**
     * 깐부 정보
     */
    public GganbuMemSelVo gganbuMemSel(GganbuMemSelInputVo gganbuMemSelInputVo) {
        gganbuMemSelInputVo.setGganbuNo(gganbuNoCheck(gganbuMemSelInputVo.getGganbuNo()));
        GganbuMemSelVo gganbuMemSelVo = event.gganbuMemSel(gganbuMemSelInputVo);
        if(gganbuMemSelVo != null) {
            int averageLevel = (gganbuMemSelVo.getMem_level() + gganbuMemSelVo.getPtr_mem_level()) / 2; // 소수점 버림
            gganbuMemSelVo.setAverage_level(averageLevel);

            int memL = (gganbuMemSelVo.getMem_level() - 1) / 10;
            gganbuMemSelVo.setMem_level_color(DalbitUtil.getProperty("level.color." + memL).split(","));
            gganbuMemSelVo.setMem_profile(new ImageVo(gganbuMemSelVo.getImage_profile(), gganbuMemSelVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));

            int ptrMemL = (gganbuMemSelVo.getPtr_mem_level() - 1) / 10;
            gganbuMemSelVo.setPtr_mem_level_color(DalbitUtil.getProperty("level.color." + ptrMemL).split(","));
            gganbuMemSelVo.setPtr_mem_profile(new ImageVo(gganbuMemSelVo.getPtr_image_profile(), gganbuMemSelVo.getPtr_mem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }

        return gganbuMemSelVo;
    }

    /**
     * 깐부 방송시간 집계 등록 (방송 종료시 호출)
     */
    public GganbuMemMarbleInsInputVo gganbuMemViewStatIns(String memNo, String roomNo, String marbleInsType) {
        GganbuMemViewStatInsVo viewStatVo = event.gganbuMemViewStatIns(memNo);

        if(viewStatVo.getS_return() == 1) {
            int rMarbleCnt = 0;
            int yMarbleCnt = 0;
            int bMarbleCnt = 0;
            int pMarbleCnt = 0;

            for(int i=0; i<viewStatVo.getS_rcvCnt(); i++) {
                int getMarble = getBroadExitMarble();
                if(getMarble == 1) {
                    rMarbleCnt++;
                }else if(getMarble == 2) {
                    yMarbleCnt++;
                }else if(getMarble == 3) {
                    bMarbleCnt++;
                }else {
                    pMarbleCnt++;
                }
            }

            GganbuMemMarbleInsInputVo marbleInsVo = new GganbuMemMarbleInsInputVo(
                memNo, marbleInsType, roomNo, rMarbleCnt, yMarbleCnt, bMarbleCnt, pMarbleCnt
            );

            return gganbuMemMarbleIns(marbleInsVo); // 획득한 구슬 ins
        }

        return new GganbuMemMarbleInsInputVo();
    }

    // 방송 종료시 구슬 종류 얻기 (1: 빨강(40%), 2: 노랑(35%), 3: 파랑(15%), 4: 보라(10%))
    private int getBroadExitMarble() {
        int randomValue = ThreadLocalRandom.current().nextInt(1, 101); // 1~100 난수
        int rArea = 40;
        int yArea = rArea + 35;
        int bArea = yArea + 15;

        if(randomValue <= rArea) {
            return 1;
        }else if(randomValue <= yArea) {
            return 2;
        }else if(randomValue <= bArea) {
            return 3;
        }else {
            return 4;
        }
    }

    private int[] getMarble(int marbleCnt) {
        int[] result = {0, 0, 0, 0};
        int randomValue = ThreadLocalRandom.current().nextInt(1, 101); // 1~100 난수
        int rArea = 25;
        int yArea = rArea + 25;
        int bArea = yArea + 25;

        for(int i=0; i<marbleCnt; i++) {
            if(randomValue <= rArea) {
                result[0]++;
            }else if(randomValue <= yArea) {
                result[1]++;
            }else if(randomValue <= bArea) {
                result[2]++;
            }else {
                result[3]++;
            }
        }

        return result;
    }

    /**
     * 깐부 구슬 획득
     * param:
     *   GganbuMemMarbleInsInputVo의 memNo, insSlct, marbleCnt를 넣어준다
     *
     *  추가 -> insSlct가 b일때 winSlct 넣어준다
    */
    public GganbuMemMarbleInsInputVo gganbuMemMarbleIns(GganbuMemMarbleInsInputVo gganbuMemMarbleInsInputVo) {
        // 확률에 따라 구슬을 지급한다
        if(StringUtils.equals(gganbuMemMarbleInsInputVo.getInsSlct(), "c") ||
            StringUtils.equals(gganbuMemMarbleInsInputVo.getInsSlct(), "e")
        ) {
            int[] getMarbles = getMarble(gganbuMemMarbleInsInputVo.getMarbleCnt());
            gganbuMemMarbleInsInputVo.setRMarbleCnt(getMarbles[0]);
            gganbuMemMarbleInsInputVo.setYMarbleCnt(getMarbles[1]);
            gganbuMemMarbleInsInputVo.setBMarbleCnt(getMarbles[2]);
            gganbuMemMarbleInsInputVo.setVMarbleCnt(getMarbles[3]);
        }

        int s_return = event.gganbuMemMarbleIns(gganbuMemMarbleInsInputVo);
        gganbuMemMarbleInsInputVo.setS_return(s_return);

        int escapeVal = 0;
        while(true) {
            if(gganbuMarbleExchange(gganbuMemMarbleInsInputVo.getMemNo()) == -3) break;
            escapeVal++;
            if(escapeVal > 100) break;
        }

        return gganbuMemMarbleInsInputVo;
    }

    /**
     * 깐부 구슬 리포트
     */
    public Map<String, Object> gganbuMemMarbleLogList(GganbuMemMarbleLogListInputVo gganbuMemMarbleLogListInputVo) {
        gganbuMemMarbleLogListInputVo.setGganbuNo(gganbuNoCheck(gganbuMemMarbleLogListInputVo.getGganbuNo()));
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuMemMarbleLogList(gganbuMemMarbleLogListInputVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuMarbleLogListVo> list = DBUtil.getList(objList, 1, GganbuMarbleLogListVo.class);
        for(GganbuMarbleLogListVo vo : list) {
            vo.setMem_profile(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     * 깐부 구슬모으기 페이지
     */
    public Map<String, Object> gganbuMarbleGather(String memNo) {
        Map<String, Object> result = new HashMap<>();
        GganbuRoundInfoOutputVo gganbuRoundInfo = getGganbuRoundInfo(); // 깐부 회차정보
        String gganbuNo = gganbuRoundInfo.getGganbuNo();
        int gganbuState = gganbuMemChk(new GganbuMemChkVo(gganbuNo, memNo));

        // 깐부 랭킹리스트
        GganbuRankListInputVo gganbuRankListInputVo = new GganbuRankListInputVo();
        gganbuRankListInputVo.setGganbuNo(gganbuNo);
        gganbuRankListInputVo.setPageNo(1);
        gganbuRankListInputVo.setPagePerCnt(50);
        result.put("rankList", gganbuRankList(gganbuRankListInputVo));

        if(gganbuState == 1) { // 깐부 있음
            // 깐부 정보
            GganbuMemSelVo gganbuInfo = gganbuMemSel(new GganbuMemSelInputVo(gganbuNo, memNo));
            result.put("gganbuInfo", gganbuInfo);

            // 내 깐부 랭킹 정보
            gganbuRankListInputVo.setMemNo(memNo);
            GganbuRankListVo myRankList = event.gganbuMyRankList(gganbuRankListInputVo);
            int memL = (myRankList.getMem_level() - 1) / 10;
            myRankList.setMem_level_color(DalbitUtil.getProperty("level.color." + memL).split(","));
            int ptrMemL = (myRankList.getPtr_mem_level() - 1) / 10;
            myRankList.setPtr_mem_level_color(DalbitUtil.getProperty("level.color." + ptrMemL).split(","));

            result.put("myRankInfo", myRankList);

            // 구슬주머니 new 뱃지
            GganbuMemBadgeUpdVo gganbuMemBadgeUpdVo = new GganbuMemBadgeUpdVo(gganbuNo, memNo);
            GganbuMemBadgeSelVo gganbuMemBadgeSelVo = event.gganbuMemBadgeSel(gganbuMemBadgeUpdVo);
            int badgeCnt = 0;
            if(gganbuMemBadgeSelVo != null) {
                badgeCnt = gganbuMemBadgeSelVo.getPocket_cnt();
            }
            result.put("badgeCnt", badgeCnt);
        }

        result.put("gganbuState", gganbuState);
        result.put("gganbuRoundInfo", gganbuRoundInfo);

        return result;
    }

    /**
     * 깐부 구슬 베팅 페이지
     */
    public Map<String, Object> gganbuMarbleBetting(GganbuMarbleBettingPageInputVo gganbuMarbleBettingPageInputVo) {
        Map<String, Object> result = new HashMap<>();
        GganbuRoundInfoOutputVo gganbuRoundInfo = getGganbuRoundInfo(); // 깐부 회차정보
        String gganbuNo = gganbuRoundInfo.getGganbuNo();
        String memNo = gganbuMarbleBettingPageInputVo.getMemNo();

        // 깐부 이벤트 회차
        result.put("gganbuNo", gganbuNo);

        //베팅 리스트
        GganbuBettingLogListInputVo gganbuBettingLogListInputVo = new GganbuBettingLogListInputVo(gganbuNo, memNo, 1, 50);
        result.put("bettingListInfo", gganbuBettingLogList(gganbuBettingLogListInputVo));
        result.put("myBettingListInfo", gganbuMyBettingLogSel(gganbuBettingLogListInputVo));

        // 베팅수 체크
        GganbuMemBettingChkVo gganbuMemBettingChkVo = new GganbuMemBettingChkVo(gganbuNo, memNo);
        int bettingChk = event.gganbuMemBettingChk(gganbuMemBettingChkVo);
        if(bettingChk == -1) {
            result.put("bettingYn", "n");
        }else {
            result.put("bettingYn", "y");
        }

        return result;
    }

    /**
     * 구슬 주머니 획득(구슬교환)
     * memNo, ptrMemNo, roomNo, dalCnt
     */
    public int gganbuMarblePocketStatIns(GganbuMarbleExchangeInputVo gganbuMarbleExchangeInputVo) {
        return event.gganbuMarblePocketStatIns(gganbuMarbleExchangeInputVo);
    }

    /**
     * 구슬 주머니 획득(구슬교환)
     */
    public int gganbuMarbleExchange(String memNo) {
        return event.gganbuMarbleExchange(memNo);
    }

    /**
     * 구슬 주머니 오픈
     */
    public Integer gganbuMarblePocketOpenIns(GganbuMarblePocketOpenInsVo gganbuMarblePocketOpenInsVo) {
        return event.gganbuMarblePocketOpenIns(gganbuMarblePocketOpenInsVo);
    }

    /**
     * 깐부 구슬 주머니 리포트
     */
    public Map<String, Object> gganbuMarblePocketLogList(GganbuMarblePocketLogListInputVo gganbuMarblePocketOpenInsVo) {
        gganbuMarblePocketOpenInsVo.setGganbuNo(gganbuNoCheck(gganbuMarblePocketOpenInsVo.getGganbuNo()));
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuMarblePocketLogList(gganbuMarblePocketOpenInsVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuMarblePocketLogListVo> list = DBUtil.getList(objList, 1, GganbuMarblePocketLogListVo.class);
        for(GganbuMarblePocketLogListVo vo : list) {
            if(StringUtils.equals(vo.getIns_slct(), "e")) {
                vo.setRcvReason("구슬 "+ vo.getExc_marble_cnt() + "개 완성");
            }else if(StringUtils.equals(vo.getIns_slct(), "u")) {
                vo.setRcvReason(vo.getMarble_pocket_cnt() + "만개 선물 하기");
            }else {
                vo.setRcvReason("1만개 선물 받기");
            }
        }

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     * 깐부 랭킹 리스트
     */
    public Map<String, Object> gganbuRankList(GganbuRankListInputVo gganbuRankListInputVo) {
        gganbuRankListInputVo.setGganbuNo(gganbuNoCheck(gganbuRankListInputVo.getGganbuNo()));
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuRankList(gganbuRankListInputVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuRankListVo> list = DBUtil.getList(objList, 1, GganbuRankListVo.class);
        for(GganbuRankListVo vo : list) {
            int memL = (vo.getMem_level() - 1) / 10;
            vo.setMem_level_color(DalbitUtil.getProperty("level.color." + memL).split(","));

            int ptrMemL = (vo.getPtr_mem_level() - 1) / 10;
            vo.setPtr_mem_level_color(DalbitUtil.getProperty("level.color." + ptrMemL).split(","));
        }

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     * 깐부 베팅 리스트
     */
    public Map<String, Object> gganbuBettingLogList(GganbuBettingLogListInputVo bettingLogListInputVo) {
        bettingLogListInputVo.setGganbuNo(gganbuNoCheck(bettingLogListInputVo.getGganbuNo()));
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuBettingLogList(bettingLogListInputVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuBettingLogListVo> list = DBUtil.getList(objList, 1, GganbuBettingLogListVo.class);
        Date time = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormat.format(time);

        for(GganbuBettingLogListVo vo : list) {
            String insDate = dateFormat.format(vo.getIns_date());
            if(nowDate.equals(insDate)) {
                vo.setIsNewYn("y");
            }else {
                vo.setIsNewYn("n");
            }

            vo.setMem_profile(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     * 깐부 나의 베팅 내역
     */
    public Map<String, Object> gganbuMyBettingLogSel(GganbuBettingLogListInputVo bettingLogListInputVo) {
        bettingLogListInputVo.setGganbuNo(gganbuNoCheck(bettingLogListInputVo.getGganbuNo()));
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuMyBettingLogSel(bettingLogListInputVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuMyBettingLogSelVo> list = DBUtil.getList(objList, 1, GganbuMyBettingLogSelVo.class);

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     * 깐부 투표자 집계
     */
    public List<GganbuBettingStatSelVo> gganbuBettingStatSel(String gganbuNo) {
        gganbuNo = gganbuNoCheck(gganbuNo);
        List<GganbuBettingStatSelVo> result = event.gganbuBettingStatSel(gganbuNo);
        double oddBettingCnt = result.get(0).getBetting_cnt();
        double evenBettingCnt = result.get(1).getBetting_cnt();
        double oddProbability = (oddBettingCnt / (oddBettingCnt + evenBettingCnt)) * 100;
        double evenProbability = (evenBettingCnt / (oddBettingCnt + evenBettingCnt)) * 100;
        String oddWinProbability = String.format("%.1f", oddProbability);
        String evenWinProbability = String.format("%.1f", evenProbability);
        result.get(0).setOddWinProbability(oddWinProbability);
        result.get(1).setEvenWinProbability(evenWinProbability);

        return result;
    }

    /**
     * 깐부 뱃지 초기화
     */
    public int gganbuMemBadgeUpd(GganbuMemBadgeUpdVo gganbuMemBadgeUpdVo) {
        return event.gganbuMemBadgeUpd(gganbuMemBadgeUpdVo);
    }

    /**
     *  깐부 찾기
     */
    public Map<String, Object> gganbuMemberSearch(GganbuMemberSearchInputVo gganbuMemberSearchInputVo) {
        Map<String, Object> result = new HashMap<>();
        String getText = gganbuMemberSearchInputVo.getSearchText();
        if(StringUtils.isEmpty(getText) || getText.length() < 2) {
            String[] emptyArr = {};
            result.put("listCnt", 0);
            result.put("list", emptyArr);
            return result;
        }

        List<Object> objList = event.gganbuMemberSearch(gganbuMemberSearchInputVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuMemberSearchVo> list = DBUtil.getList(objList, 1, GganbuMemberSearchVo.class);
        for(GganbuMemberSearchVo vo : list) {
            vo.setMem_profile(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
            vo.setAverage_level((gganbuMemberSearchInputVo.getMyLevel() + vo.getMem_level()) / 2);
        }

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }

    /**
     *  깐부 구슬 주머니 페이지
     */
    public Map<String, Object> gganbuPocketPage(GganbuPocketPageInputVo gganbuPocketPageInputVo) {
        Map<String, Object> result = new HashMap<>();
        GganbuRoundInfoOutputVo gganbuRoundInfo = getGganbuRoundInfo(); // 깐부 회차정보
        String gganbuNo = gganbuRoundInfo.getGganbuNo();
        String memNo = gganbuPocketPageInputVo.getMemNo();

        // 깐부 이벤트 회차
        result.put("gganbuNo", gganbuNo);

        // 주머니 리포트
        result.put("pocketReport", gganbuMarblePocketLogList(new GganbuMarblePocketLogListInputVo(gganbuNo, memNo, "r")));

        // 깐부 정보
        result.put("gganbuMemSel", gganbuMemSel(new GganbuMemSelInputVo(gganbuNo, memNo)));

        return result;
    }

    /**
     *  깐부 찾기 (나의 팬)
     */
    public Map<String, Object> gganbuFanList(GganbuFanListVo gganbuFanListVo) {
        Map<String, Object> result = new HashMap<>();
        List<Object> objList = event.gganbuFanList(gganbuFanListVo);
        Integer listCnt = DBUtil.getData(objList, 0, Integer.class);
        if(listCnt == null) return result;

        List<GganbuFanListVo> list = DBUtil.getList(objList, 1, GganbuFanListVo.class);
        for(GganbuFanListVo vo : list) {
            vo.setFanMemProfile(new ImageVo(vo.getProfileImage(), vo.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
            vo.setAverageLevel((gganbuFanListVo.getMemLevel() + vo.getLevel()) / 2);
        }

        result.put("listCnt", listCnt);
        result.put("list", list);

        return result;
    }
    /* 깐부 이벤트 끝~~~ */
}
