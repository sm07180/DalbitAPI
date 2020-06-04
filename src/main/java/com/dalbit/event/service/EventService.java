package com.dalbit.event.service;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class EventService {

    @Autowired
    EventDao eventDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    EventService roomService;

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

        resultMap.put("list", rankingLiveVoList);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
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

        resultMap.put("list", rankingResultVoList);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
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

//        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
//        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
//        log.info(" ### 프로시저 호출결과 ###");

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
        int insertResult = eventDao.callEventReplyDelete(pReplyDeleteInputVo);

        String result;
        if(Status.이벤트_댓글삭제성공.getMessageCode().equals(insertResult+"")){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제성공));
        }else if(Status.이벤트_댓글삭제없음.getMessageCode().equals(insertResult+"")){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제없음));
        }else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글삭제실패_등록오류));
        }

        return result;
    }
}
