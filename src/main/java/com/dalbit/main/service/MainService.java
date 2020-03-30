package com.dalbit.main.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.*;
import com.dalbit.main.dao.MainDao;
import com.dalbit.main.vo.MainDjRankingOutVo;
import com.dalbit.main.vo.MainFanRankingOutVo;
import com.dalbit.main.vo.MainMyDjOutVo;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_MainMyDjVo;
import com.dalbit.main.vo.procedure.P_MainRecommandVo;
import com.dalbit.main.vo.request.MainRecommandOutVo;
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

        HashMap mainFanRankingList = new HashMap();
        if(DalbitUtil.isEmpty(mainFanRankingVoList)){
            mainFanRankingList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_내역없음, mainFanRankingList));
        }
        List<MainFanRankingOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainFanRankingVoList.size(); i++){
            outVoList.add(new MainFanRankingOutVo(mainFanRankingVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        mainFanRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainFanRankingList.put("list", procedureOutputVo.getOutputBox());
        mainFanRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_성공, mainFanRankingList));
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

        HashMap mainDjRankingList = new HashMap();
        if(DalbitUtil.isEmpty(mainDjRankingVoList)){
            mainDjRankingList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_내역없음, mainDjRankingList));
        }

        List<MainDjRankingOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainDjRankingVoList.size(); i++){
            outVoList.add(new MainDjRankingOutVo(mainDjRankingVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        mainDjRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainDjRankingList.put("list", procedureOutputVo.getOutputBox());
        mainDjRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_성공, mainDjRankingList));
        } else if (procedureVo.getRet().equals(Status.메인_DJ랭킹조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_요청회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_실패));
        }
        return result;
    }


    /**
     * 마이 DJ 방송방 리스트
     */
    public String callMainMyDjList(P_MainMyDjVo pMainMyDjVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainMyDjVo);
        List<P_MainMyDjVo> mainMyDjVoList = mainDao.callMainMyDjList(procedureVo);

        HashMap mainMyDjList = new HashMap();
        if(DalbitUtil.isEmpty(mainMyDjVoList)){
            mainMyDjList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_마이DJ_리스트없음, mainMyDjList));
        }

        List<MainMyDjOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainMyDjVoList.size(); i++){
            outVoList.add(new MainMyDjOutVo(mainMyDjVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        mainMyDjList.put("list", procedureOutputVo.getOutputBox());
        mainMyDjList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_마이DJ_조회성공, mainMyDjList));
        } else if (procedureVo.getRet().equals(Status.메인_마이DJ_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_마이DJ_요청회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_마이DJ_조회실패));
        }
        return result;
    }



    /**
     * 추천 BJ 리스트
     */
    public String callMainRecommandList(String memNo) {

        List<P_MainRecommandVo> recommandVoList = mainDao.callMainRecommandList(memNo);

        String result;
        if(!DalbitUtil.isEmpty(recommandVoList)){
            List list = new ArrayList();

            for (int i=0; i < recommandVoList.size(); i++){
                MainRecommandOutVo outVo = new MainRecommandOutVo();
                outVo.setMemNo(recommandVoList.get(i).getMemNo());
                outVo.setNickNm(recommandVoList.get(i).getNickNm());
                outVo.setGender(recommandVoList.get(i).getGender());
                outVo.setProfImg(new ImageVo(recommandVoList.get(i).getProfileUrl(), recommandVoList.get(i).getGender(), DalbitUtil.getProperty("server.photo.url")));
                outVo.setRoomType(recommandVoList.get(i).getRoomType());
                outVo.setRoomNo(recommandVoList.get(i).getRoomNo());
                if(DalbitUtil.isEmpty(recommandVoList.get(i).getTitle())){
                    outVo.setTitle("방송 준비중");
                }else{
                    outVo.setTitle(recommandVoList.get(i).getTitle());
                }
                outVo.setListeners(recommandVoList.get(i).getListeners());
                outVo.setLikes(recommandVoList.get(i).getLikes());
                list.add(outVo);
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_추천DJ리스트_조회성공, list));
        } else if (recommandVoList.size()==0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_추천DJ리스트_없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_추천DJ리스트_조회실패));
        }

        return result;
    }
}
