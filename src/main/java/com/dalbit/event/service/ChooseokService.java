package com.dalbit.event.service;

import com.dalbit.common.code.EventStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.event.dao.ChooseokDao;
import com.dalbit.event.vo.procedure.P_ChooseokCheckVo;
import com.dalbit.event.vo.procedure.P_ChooseokDalVo;
import com.dalbit.event.vo.procedure.P_ChooseokPurchaseDalVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;

@Slf4j
@Service
public class ChooseokService {

    @Autowired
    ChooseokDao chooseokDao;

    @Autowired
    GsonUtil gsonUtil;

    /**
     * 추석이벤트 참여 체크
     */
    public String callChooseokCheck(P_ChooseokCheckVo pChooseokCheckVo) {

        //시작일 종료일 체크
        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        //Calendar month + 1 확인
        if("real".equals(DalbitUtil.getActiveProfile())) {
            start.set(2021,0,28,0,0,0);
        } else {
            start.set(2021,0,26,0,0,0);
        }

        end.set(2021,1,2,23,59,59);

        boolean state = false;
        if(start.getTimeInMillis() <= today.getTimeInMillis() && today.getTimeInMillis() <= end.getTimeInMillis()) {
            state = true;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("state", state);

        String result;
        if(state) {
            ProcedureVo procedureVo = new ProcedureVo(pChooseokCheckVo);
            chooseokDao.callChooseokCheck(procedureVo);
            if (EventStatus.추석이벤트체크_참여가능.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트체크_참여가능, resultMap));
            } else if (EventStatus.추석이벤트체크_회원아님.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트체크_회원아님, resultMap));
            } else if (EventStatus.추석이벤트체크_이미받음.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트체크_이미받음, resultMap));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트체크_실패, resultMap));
            }
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트체크_참여기간아님, resultMap));
        }

        return result;
    }

    /**
     * 추석이벤트 무료 달 지급
     */
    public String callChooseokFreeDalCheck(P_ChooseokCheckVo pChooseokCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChooseokCheckVo);
        chooseokDao.callChooseokFreeDalCheck(procedureVo);

        P_ChooseokDalVo dal = new Gson().fromJson(procedureVo.getExt(), P_ChooseokDalVo.class);

        HashMap resultMap = new HashMap();
        resultMap.put("freeDal", dal);

        String result;
        if(EventStatus.추석이벤트_무료달지급_지급성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_무료달지급_지급성공, resultMap));
        } else if(EventStatus.추석이벤트_무료달지급_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_무료달지급_회원아님));
        } else if(EventStatus.추석이벤트_무료달지급_이미지급받음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_무료달지급_이미지급받음));
        } else if(EventStatus.추석이벤트_무료달지급_레벨5미만.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_무료달지급_레벨5미만));
        } else if(EventStatus.추석이벤트_무료달지급_이벤트종료.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_무료달지급_이벤트종료));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_무료달지급_실패));
        }
        return result;
    }

    /**
     * 추석이벤트 구매 달 확인
     */
    public String callChooseokPurchaseSelect(P_ChooseokCheckVo pChooseokCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChooseokCheckVo);
        chooseokDao.callChooseokPurchaseSelect(procedureVo);

        P_ChooseokPurchaseDalVo purchaseDal = new Gson().fromJson(procedureVo.getExt(), P_ChooseokPurchaseDalVo.class);

        HashMap resultMap = new HashMap();
        resultMap.put("purchaseDal", purchaseDal);

        String result;
        if(EventStatus.추석이벤트_구매달조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_구매달조회_성공, resultMap));
        } else if(EventStatus.추석이벤트_구매달조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_구매달조회_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_구매달조회_실패));
        }
        return result;
    }

    /**
     * 추석이벤트 구매 달에 따른 추가 보너스 지급
     */
    public String callChooseokPurchaseBonus(P_ChooseokCheckVo pChooseokCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pChooseokCheckVo);
        chooseokDao.callChooseokPurchaseBonus(procedureVo);

        P_ChooseokDalVo dal = new Gson().fromJson(procedureVo.getExt(), P_ChooseokDalVo.class);

        HashMap resultMap = new HashMap();
        resultMap.put("bonusDal", dal);

        String result;
        if(EventStatus.추석이벤트_보너스달지급_지급성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_지급성공, resultMap));
        } else if(EventStatus.추석이벤트_보너스달지급_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_회원아님));
        } else if(EventStatus.추석이벤트_보너스달지급_이미지급받음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_이미지급받음));
        } else if(EventStatus.추석이벤트_보너스달지급_지급기간이아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_지급기간이아님));
        } else if(EventStatus.추석이벤트_보너스달지급_이벤트종료.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_이벤트종료));
        } else if(EventStatus.추석이벤트_보너스달지급_500달미만.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_500달미만));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.추석이벤트_보너스달지급_지급실패));
        }
        return result;
    }
}
