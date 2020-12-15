package com.dalbit.event.service;

import com.dalbit.broadcast.vo.procedure.P_EventJoinDetailVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.JoinDao;
import com.dalbit.event.vo.procedure.P_EventJoinRewardVo;
import com.dalbit.event.vo.procedure.P_JoinCheckVo;
import com.dalbit.event.vo.procedure.P_RouletteCouponVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@Service
public class JoinService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    JoinDao joinDao;

    /**
     * 가입 이벤트 팝업,배너 노출 체크
     */
    public String callEventJoinCheck(P_JoinCheckVo pJoinCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pJoinCheckVo);
        joinDao.callEventJoinCheck(procedureVo);

        String result;
        if (Status.오레벨노출_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.오레벨노출_성공));
        } else if (Status.십레벨노출_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.십레벨노출_성공));
        } else if (Status.노출_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.노출_회원아님));
        } else if (Status.노출_대상아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.노출_대상아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.노출_실패));
        }
        return result;
    }

    /**
     * 가입 이벤트 상세 보기
     */
    public String callEventJoinDetail(P_EventJoinDetailVo pEventJoinDetailVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventJoinDetailVo);
        joinDao.callEventJoinDetail(procedureVo);

        String result;
        if (Status.가입이벤트_상세보기_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("slctType", DalbitUtil.getIntMap(resultMap, "slctType"));
            returnMap.put("startDt", DalbitUtil.getUTCFormat((String)resultMap.get("startDate")));
            returnMap.put("startTs", DalbitUtil.getUTCTimeStamp((String)resultMap.get("startDate")));
            returnMap.put("endDt", DalbitUtil.getUTCFormat((String)resultMap.get("endDate")));
            returnMap.put("endTs", DalbitUtil.getUTCTimeStamp((String)resultMap.get("endDate")));

            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_상세보기_성공, returnMap));
        } else if (Status.가입이벤트_상세보기_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_상세보기_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_상세보기_실패));
        }
        return result;
    }


    /**
     * 가입 이벤트 보상받기
     */
    public String callEventJoinReward(P_EventJoinRewardVo pEventJoinRewardVo) {
        ProcedureVo procedureVo = new ProcedureVo(pEventJoinRewardVo);
        joinDao.callEventJoinReward(procedureVo);

        String result;
        if (Status.가입이벤트_보상받기_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_성공));
        } else if (Status.가입이벤트_보상받기_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_회원아님));
        } else if (Status.가입이벤트_보상받기_레벨부족.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_레벨부족));
        } else if (Status.가입이벤트_보상받기_기간종료.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_기간종료));
        } else if (Status.가입이벤트_보상받기_이미받음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_이미받음));
        } else if (Status.가입이벤트_보상받기_본인인증안됨.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_본인인증안됨));
        } else if (Status.가입이벤트_보상받기_본인인증번호_이미보상받음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_본인인증번호_이미보상받음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가입이벤트_보상받기_실패));
        }
        return result;
    }
}
