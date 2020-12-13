package com.dalbit.event.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.*;
import com.dalbit.event.dao.RouletteDao;
import com.dalbit.event.vo.RouletteApplyListOutVo;
import com.dalbit.event.vo.RouletteWinListOutVo;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.event.vo.request.RouletteInfoVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RouletteService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    RouletteDao rouletteDao;

    /**
     * 나의 응모 기회 가져오기
     */
    public String couponSelect(P_RouletteCouponVo pRouletteCouponVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRouletteCouponVo);
        rouletteDao.callCouponSelect(procedureVo);

        String result;
        if (Status.응모권조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("couponCnt", DalbitUtil.getIntMap(resultMap, "couponCnt"));
            returnMap.put("eventCouponCnt", DalbitUtil.getIntMap(resultMap, "eventCoupon"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.응모권조회_성공, returnMap));
        } else if (Status.응모권조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.응모권조회_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.응모권조회_실패));
        }
        return result;
    }

    /**
     * 룰렛 스타트
     */
    public String rouletteStart(P_RouletteCouponVo pRouletteCouponVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRouletteCouponVo);
        rouletteDao.rouletteStart(procedureVo);

        String result;
        if (Status.스타트_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("giftiCon", DalbitUtil.getIntMap(resultMap, "giftiCon"));
            returnMap.put("itemNo", DalbitUtil.getIntMap(resultMap, "itemNo"));
            returnMap.put("couponCnt", DalbitUtil.getIntMap(resultMap, "couponCnt"));
            returnMap.put("eventCouponCnt", DalbitUtil.getIntMap(resultMap, "eventCoupon"));
            returnMap.put("phone", DalbitUtil.getStringMap(resultMap, "phone"));
            returnMap.put("winIdx", DalbitUtil.getIntMap(resultMap, "winIdx"));
            returnMap.put("tempNo", DalbitUtil.getIntMap(resultMap, "itemNo"));
            returnMap.put("inputEndDate", DalbitUtil.getStringMap(resultMap, "inputEndDate"));
            returnMap.put("itemName", DalbitUtil.getStringMap(resultMap, "itemName"));
            returnMap.put("itemWinMsg", DalbitUtil.getStringMap(resultMap, "itemWinMsg"));
            returnMap.put("imageUrl", DalbitUtil.getStringMap(resultMap, "imageUrl"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.스타트_성공, returnMap));
        } else if (Status.스타트_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타트_회원아님));
        } else if (Status.스타트_응모권부족.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타트_응모권부족));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타트_실패));
        }
        return result;
    }

    /**
     * 전화번호 입력
     */
    public String inputPhone(P_RoulettePhoneVo pRoulettePhoneVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoulettePhoneVo);
        rouletteDao.inputPhone(procedureVo);

        String result;
        if (Status.전화번호입력_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.전화번호입력_성공));
        } else if (Status.전화번호입력_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.전화번호입력_회원아님));
        } else if (Status.전화번호입력_당첨내역없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.전화번호입력_당첨내역없음));
        } else if (Status.전화번호입력_자리수이상.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.전화번호입력_자리수이상));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.전화번호입력_실패));
        }
        return result;
    }

    /**
     * 나의 참여 이력 조회
     */
    public String applyList(P_RouletteApplyListVo pRouletteApplyVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRouletteApplyVo);
        List<P_RouletteApplyListVo> applyListVo = rouletteDao.applyList(procedureVo);

        HashMap applyList = new HashMap();
        if(DalbitUtil.isEmpty(applyListVo)){
            applyList.put("list", new ArrayList<>());
            applyList.put("paging", new PagingVo(0, pRouletteApplyVo.getPageNo(), pRouletteApplyVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.참여이력조회_없음, applyList));
        }

        List<RouletteApplyListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<applyListVo.size(); i++){
            outVoList.add(new RouletteApplyListOutVo(applyListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);

        applyList.put("list", procedureOutputVo.getOutputBox());
        applyList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.참여이력조회_성공, applyList));
        } else if(Status.참여이력조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.참여이력조회_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.참여이력조회_실패));
        }

        return result;
    }

    /**
     * 당첨자 조회
     */
    public String winList(P_RouletteWinListVo pRouletteWinListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRouletteWinListVo);
        List<P_RouletteWinListVo> winListVo = rouletteDao.winList(procedureVo);

        HashMap winList = new HashMap();
        if(DalbitUtil.isEmpty(winListVo)){
            winList.put("list", new ArrayList<>());
            winList.put("paging", new PagingVo(0, pRouletteWinListVo.getPageNo(), pRouletteWinListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.당첨자조회_없음, winList));
        }

        List<RouletteWinListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<winListVo.size(); i++){
            outVoList.add(new RouletteWinListOutVo(winListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);

        winList.put("list", procedureOutputVo.getOutputBox());
        winList.put("paging", new PagingVo(Integer.parseInt(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.당첨자조회_성공, winList));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.당첨자조회_실패));
        }

        return result;
    }

    public String selectRouletteInfo(RouletteInfoVo rouletteInfoVo){
        List<RouletteInfoVo> rouletteInfoList = rouletteDao.selectRouletteInfo(rouletteInfoVo);
        if(DalbitUtil.isEmpty(rouletteInfoList)){
            return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }

        var resultMap = new HashMap();
        var infoData = rouletteInfoList.stream().filter(item -> item.getSlct_type().equals("0")).findFirst().orElse(new RouletteInfoVo());
        var itemList = rouletteInfoList.stream().filter(item -> !item.getSlct_type().equals("0")).collect(Collectors.toList());
        resultMap.put("info", infoData);
        resultMap.put("itemList", itemList);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }
}
