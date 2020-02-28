package com.dalbit.main.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.dao.MainDao;
import com.dalbit.main.vo.MainDjRankingOutVo;
import com.dalbit.main.vo.MainFanRankingOutVo;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class MainService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MainDao mainDao;


    /**
     * 팬 랭킹
     */
    public String callMainFanRanking(P_MainFanRankingVo pMainFanRankingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainFanRankingVo);
        List<P_MainFanRankingVo> mainFanRankingVoList = mainDao.callMainFanRanking(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(mainFanRankingVoList)){
            procedureOutputVo = null;
        }else{
            List<MainFanRankingOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<mainFanRankingVoList.size(); i++){
                outVoList.add(new MainFanRankingOutVo(mainFanRankingVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap mainFanRankingList = new HashMap();
        mainFanRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainFanRankingList.put("list", procedureOutputVo.getOutputBox());
        mainFanRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_성공, mainFanRankingList));
        } else if (procedureVo.getRet().equals(Status.메인_팬랭킹조회_내역없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_내역없음));
        } else if (procedureVo.getRet().equals(Status.메인_팬랭킹조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_요청회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_실패));
        }
        return result;
    }


    /**
     * DJ 랭킹
     */
    public String callMainDjRanking(P_MainDjRankingVo pMainDjRankingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainDjRankingVo);
        List<P_MainDjRankingVo> mainDjRankingVoList = mainDao.callMainDjRanking(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(mainDjRankingVoList)){
            procedureOutputVo = null;
        }else{
            List<MainDjRankingOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<mainDjRankingVoList.size(); i++){
                outVoList.add(new MainDjRankingOutVo(mainDjRankingVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap mainDjRankingList = new HashMap();
        mainDjRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainDjRankingList.put("list", procedureOutputVo.getOutputBox());
        mainDjRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_성공, mainDjRankingList));
        } else if (procedureVo.getRet().equals(Status.메인_DJ랭킹조회_내역없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_내역없음));
        } else if (procedureVo.getRet().equals(Status.메인_DJ랭킹조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_요청회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_실패));
        }
        return result;
    }
}
