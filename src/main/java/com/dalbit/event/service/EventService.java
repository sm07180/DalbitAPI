package com.dalbit.event.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.EventDao;
import com.dalbit.event.vo.ReplyListOutputVo;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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

        if("real".equals(DalbitUtil.getActiceProfile())){
            HashMap term1 = new HashMap();
            Calendar srt1 = Calendar.getInstance();
            Calendar end1 = Calendar.getInstance();
            srt1.set(2020, 5,8,0,0,0);
            end1.set(2020, 5, 17, 23, 59, 59);
            term1.put("round", 1);
            term1.put("srtDt", srt1);
            term1.put("endDt", end1);

            HashMap term2 = new HashMap();
            Calendar srt2 = Calendar.getInstance();
            Calendar end2 = Calendar.getInstance();
            srt2.set(2020, 5,18,0,0,0);
            end2.set(2020, 5, 27, 23, 59, 59);
            term2.put("round", 2);
            term2.put("srtDt", srt2);
            term2.put("endDt", end2);

            HashMap term3 = new HashMap();
            Calendar srt3 = Calendar.getInstance();
            Calendar end3 = Calendar.getInstance();
            srt3.set(2020, 5,28,0,0,0);
            end3.set(2020, 6, 7, 23, 59, 59);
            term3.put("round", 3);
            term3.put("srtDt", srt3);
            term3.put("endDt", end3);

            eventTmp.add(term1);
            eventTmp.add(term2);
            eventTmp.add(term3);
        }else{
            HashMap term1 = new HashMap();
            Calendar srt1 = Calendar.getInstance();
            Calendar end1 = Calendar.getInstance();
            srt1.set(2020, 5,3,0,0,0);
            end1.set(2020, 5, 3, 23, 59, 59);
            term1.put("round", 1);
            term1.put("srtDt", srt1);
            term1.put("endDt", end1);

            HashMap term2 = new HashMap();
            Calendar srt2 = Calendar.getInstance();
            Calendar end2 = Calendar.getInstance();
            srt2.set(2020, 5,4,0,0,0);
            end2.set(2020, 5, 4, 23, 59, 59);
            term2.put("round", 2);
            term2.put("srtDt", srt2);
            term2.put("endDt", end2);

            HashMap term3 = new HashMap();
            Calendar srt3 = Calendar.getInstance();
            Calendar end3 = Calendar.getInstance();
            srt3.set(2020, 5,5,0,0,0);
            end3.set(2020, 5, 5, 23, 59, 59);
            term3.put("round", 3);
            term3.put("srtDt", srt3);
            term3.put("endDt", end3);

            eventTmp.add(term1);
            eventTmp.add(term2);
            eventTmp.add(term3);
        }

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
}
