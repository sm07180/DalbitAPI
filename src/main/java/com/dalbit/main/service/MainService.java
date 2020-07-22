package com.dalbit.main.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.*;
import com.dalbit.main.dao.MainDao;
import com.dalbit.main.vo.*;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.main.vo.request.MainRecommandOutVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class MainService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MainDao mainDao;

    public String getMain(HttpServletRequest request){

        int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
        String memNo = MemberVo.getMyMemNo(request);
        DeviceVo deviceVo = new DeviceVo(request);

        String platform = "";
        int osInt = deviceVo.getOs() + 1;
        if(osInt == 4){
            osInt = 1;
        }
        for(int i = 1; i < 4; i++){
            if(i == osInt){
                platform += "1";
            }else{
                platform += "_";
            }
        }

        //상위 추천 데이터 조회
        P_MainRecommandVo pMainRecommandVo = new P_MainRecommandVo();
        pMainRecommandVo.setParamPlanMemNo(DalbitUtil.getProperty("inforex.plan.memNo"));
        pMainRecommandVo.setParamDevice(deviceVo.getOs() + "");
        pMainRecommandVo.setParamMemNo(memNo);
        pMainRecommandVo.setParamPlatform(platform);
        List<P_MainRecommandVo> recommendVoList = mainDao.callMainRecommandList(pMainRecommandVo);
        if(DalbitUtil.isEmpty(recommendVoList) || recommendVoList.size() < 10){
            List<P_MainRecommandVo> recommendVoLiveList = mainDao.callMainRecommandLiveList(pMainRecommandVo);
            if(!DalbitUtil.isEmpty(recommendVoLiveList)){
                for(int i = 0; i < recommendVoLiveList.size(); i++){
                    recommendVoList.add(recommendVoLiveList.get(i));
                    if(recommendVoList.size() > 9){
                        break;
                    }
                }
            }
        }

        //DJ랭킹 조회
        P_MainDjRankingVo pMainDjRankingVo = new P_MainDjRankingVo();
        pMainDjRankingVo.setMemLogin(isLogin);
        pMainDjRankingVo.setMem_no(memNo);
        pMainDjRankingVo.setSlct_type(1);
        pMainDjRankingVo.setPageNo(1);
        pMainDjRankingVo.setPageCnt(5);
        ProcedureVo procedureDjRankVo = new ProcedureVo(pMainDjRankingVo);
        List<P_MainDjRankingVo> mainDjRankingVoList = mainDao.callMainDjRanking(procedureDjRankVo);
        if(DalbitUtil.isEmpty(mainDjRankingVoList)){
            pMainDjRankingVo.setSlct_type(2);
            procedureDjRankVo = new ProcedureVo(pMainDjRankingVo);
            mainDjRankingVoList = mainDao.callMainDjRanking(procedureDjRankVo);
        }

        //팬랭킹조회
        P_MainFanRankingVo pMainFanRankingVo = new P_MainFanRankingVo();
        pMainFanRankingVo.setMemLogin(isLogin);
        pMainFanRankingVo.setMem_no(memNo);
        pMainFanRankingVo.setSlct_type(1);
        pMainFanRankingVo.setPageNo(1);
        pMainFanRankingVo.setPageCnt(5);
        ProcedureVo procedureFanRankVo = new ProcedureVo(pMainFanRankingVo);
        List<P_MainFanRankingVo> mainFanRankingVoList = mainDao.callMainFanRanking(procedureFanRankVo);
        if(DalbitUtil.isEmpty(mainFanRankingVoList)){
            pMainFanRankingVo.setPageNo(2);
            procedureFanRankVo = new ProcedureVo(pMainFanRankingVo);
            mainFanRankingVoList = mainDao.callMainFanRanking(procedureFanRankVo);
        }

        //마이스타
        List<P_MainStarVo> starVoList = null;
        if(isLogin == 1){
            starVoList = mainDao.callMainStarList(memNo);
        }

        //배너
        P_BannerVo pBannerVo = new P_BannerVo();
        pBannerVo.setParamPlatform(platform);
        pBannerVo.setParamMemNo(memNo);
        pBannerVo.setParamDevice("" + deviceVo.getOs());
        pBannerVo.setParamPosition("1");
        List<BannerVo> bannerList = mainDao.selectBanner(pBannerVo);

        HashMap mainMap = new HashMap();

        String photoSvrUrl = DalbitUtil.getProperty("server.photo.url");
        List recommend = new ArrayList();
        if(!DalbitUtil.isEmpty(recommendVoList)){
            int setBanner = 0;
            if(!DalbitUtil.isEmpty(bannerList)){
                if(bannerList.size() == 1){
                    setBanner = recommendVoList.size() / 2;
                }else{
                    setBanner = recommendVoList.size() / bannerList.size();
                }
                if(setBanner == 0){
                    setBanner = 1;
                }
            }

            int bannerIdx = 0;
            for (int i=0; i < recommendVoList.size(); i++){
                MainRecommandOutVo outVo = new MainRecommandOutVo();
                outVo.setMemNo(recommendVoList.get(i).getMemNo());
                outVo.setNickNm(recommendVoList.get(i).getNickNm());
                outVo.setGender(recommendVoList.get(i).getGender());
                outVo.setBannerUrl(photoSvrUrl + recommendVoList.get(i).getBannerUrl());
                outVo.setProfImg(new ImageVo(recommendVoList.get(i).getProfileUrl(), recommendVoList.get(i).getGender(), photoSvrUrl));
                outVo.setRoomType(recommendVoList.get(i).getRoomType());
                outVo.setRoomNo(recommendVoList.get(i).getRoomNo());
                if(DalbitUtil.isEmpty(recommendVoList.get(i).getTitle())){
                    outVo.setTitle("방송 준비중");
                }else{
                    outVo.setTitle(recommendVoList.get(i).getTitle());
                }
                outVo.setListeners(recommendVoList.get(i).getListeners());
                outVo.setLikes(recommendVoList.get(i).getLikes());
                outVo.isSpecial = recommendVoList.get(i).isSpecial();
                outVo.isAdmin = recommendVoList.get(i).isAdmin();
                recommend.add(outVo);

                if(setBanner != 0 && ((i + 1) % setBanner) == 0){
                    if(bannerIdx < bannerList.size()){
                        MainRecommandOutVo outVoB = new MainRecommandOutVo();
                        outVoB.setMemNo(bannerList.get(bannerIdx).getIdx() + "");
                        outVoB.setNickNm("banner");
                        outVoB.setGender("");
                        ImageVo tmpVo = new ImageVo();
                        tmpVo.setUrl(bannerList.get(bannerIdx).getThumbsUrl());
                        outVoB.setProfImg(tmpVo);
                        outVoB.setBannerUrl(bannerList.get(bannerIdx).getBannerUrl());
                        outVoB.setRoomType(bannerList.get(bannerIdx).getLinkType());
                        outVoB.setRoomNo(bannerList.get(bannerIdx).getLinkUrl());
                        if(DalbitUtil.isEmpty(bannerList.get(bannerIdx).getTitle())){
                            outVoB.setTitle("방송 준비중");
                        }else{
                            outVoB.setTitle(bannerList.get(bannerIdx).getTitle());
                        }
                        outVoB.setListeners(0);
                        outVoB.setLikes(0);
                        outVoB.isSpecial = false;
                        outVoB.isAdmin = false;
                        recommend.add(outVoB);
                    }
                    bannerIdx++;
                }
            }

            if(bannerIdx < bannerList.size()){
                for (int i=bannerIdx; i < bannerList.size(); i++){
                    MainRecommandOutVo outVo = new MainRecommandOutVo();
                    outVo.setMemNo(bannerList.get(i).getIdx() + "");
                    outVo.setNickNm("banner");
                    outVo.setGender("");
                    ImageVo tmpVo = new ImageVo();
                    tmpVo.setUrl(bannerList.get(i).getThumbsUrl());
                    outVo.setProfImg(tmpVo);
                    outVo.setBannerUrl(bannerList.get(i).getBannerUrl());
                    outVo.setRoomType(bannerList.get(i).getLinkType());
                    outVo.setRoomNo(bannerList.get(i).getLinkUrl());
                    if(DalbitUtil.isEmpty(bannerList.get(i).getTitle())){
                        outVo.setTitle("방송 준비중");
                    }else{
                        outVo.setTitle(bannerList.get(i).getTitle());
                    }
                    outVo.setListeners(0);
                    outVo.setLikes(0);
                    outVo.isSpecial = false;
                    outVo.isAdmin = false;
                    recommend.add(outVo);
                }
            }
        }else{
            for (int i=0; i < bannerList.size(); i++){
                MainRecommandOutVo outVo = new MainRecommandOutVo();
                outVo.setMemNo(bannerList.get(i).getIdx() + "");
                outVo.setNickNm("banner");
                outVo.setGender("");
                ImageVo tmpVo = new ImageVo();
                tmpVo.setUrl(bannerList.get(i).getThumbsUrl());
                outVo.setProfImg(tmpVo);
                outVo.setBannerUrl(bannerList.get(i).getBannerUrl());
                outVo.setRoomType(bannerList.get(i).getLinkType());
                outVo.setRoomNo(bannerList.get(i).getLinkUrl());
                if(DalbitUtil.isEmpty(bannerList.get(i).getTitle())){
                    outVo.setTitle("방송 준비중");
                }else{
                    outVo.setTitle(bannerList.get(i).getTitle());
                }
                outVo.setListeners(0);
                outVo.setLikes(0);
                outVo.isSpecial = false;
                outVo.isAdmin = false;
                recommend.add(outVo);
            }
        }
        mainMap.put("recommend", recommend);

        List djRank = new ArrayList();
        if(!DalbitUtil.isEmpty(mainDjRankingVoList)){
            for (int i=0; i<mainDjRankingVoList.size(); i++){
                djRank.add(new MainDjRankingOutVo(mainDjRankingVoList.get(i)));
            }
        }
        mainMap.put("djRank", djRank);

        List fanRank = new ArrayList();
        for (int i=0; i<mainFanRankingVoList.size(); i++){
            fanRank.add(new MainFanRankingOutVo(mainFanRankingVoList.get(i)));
        }
        mainMap.put("fanRank", fanRank);

        List<MainStarVo> myStar = new ArrayList();
        if(isLogin == 1 && !DalbitUtil.isEmpty(starVoList)){
            for(P_MainStarVo data : starVoList){
                MainStarVo outVo = new MainStarVo();
                outVo.setMemNo(data.getMemNo());
                outVo.setNickNm(data.getNickNm());
                outVo.setGender(data.getGender());
                outVo.setProfImg(new ImageVo(data.getProfImgUrl(), data.getGender(), photoSvrUrl));
                outVo.setRoomType(data.getRoomType());
                outVo.setRoomTypeNm(data.getRoomTypeNm());
                outVo.setRoomNo(data.getRoomNo());
                if(DalbitUtil.isEmpty(data.getTitle())){
                    outVo.setTitle("방송 준비중");
                }else{
                    outVo.setTitle(data.getTitle());
                }
                myStar.add(outVo);
            }
        }
        mainMap.put("myStar", myStar);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, mainMap));
    }


    /**
     * 팬 랭킹
     */
    public String callMainFanRanking(P_MainFanRankingVo pMainFanRankingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainFanRankingVo);
        List<P_MainFanRankingVo> mainFanRankingVoList = mainDao.callMainFanRanking(procedureVo);


        HashMap mainFanRankingList = new HashMap();

        String time = "월요일 업데이트";
        if(pMainFanRankingVo.getSlct_type() == 0){
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:00", Locale.KOREA);
            time = sdf.format(today);
        }else if(pMainFanRankingVo.getSlct_type() == 1){
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DATE, -1);
            SimpleDateFormat sdf = new SimpleDateFormat("M월 d일", Locale.KOREA);
            time = sdf.format(today.getTime()) + " 업데이트";
        }
        mainFanRankingList.put("time",time);

        if(DalbitUtil.isEmpty(mainFanRankingVoList)){
            mainFanRankingList.put("myRank", 0);
            mainFanRankingList.put("myPoint", 0);
            mainFanRankingList.put("myGiftPoint", 0);
            mainFanRankingList.put("myListenPoint", 0);
            mainFanRankingList.put("myUpDown", "");
            mainFanRankingList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_팬랭킹조회_내역없음, mainFanRankingList));
        }
        List<MainFanRankingOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainFanRankingVoList.size(); i++){
            outVoList.add(new MainFanRankingOutVo(mainFanRankingVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        if(pMainFanRankingVo.getSlct_type() == 0){
            mainFanRankingList.put("time",resultMap.get("rankingDate"));
        }
        mainFanRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainFanRankingList.put("myPoint", DalbitUtil.getIntMap(resultMap, "myPoint"));
        mainFanRankingList.put("myGiftPoint", DalbitUtil.getIntMap(resultMap, "myGiftPoint"));
        mainFanRankingList.put("myListenPoint", DalbitUtil.getIntMap(resultMap, "myListenPoint"));
        mainFanRankingList.put("myUpDown", DalbitUtil.getStringMap(resultMap, "myUpDown"));
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

        String time = "월요일 업데이트";
        if(pMainDjRankingVo.getSlct_type() == 0){
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:00", Locale.KOREA);
            time = sdf.format(today);
        }else if(pMainDjRankingVo.getSlct_type() == 1){
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DATE, -1);
            SimpleDateFormat sdf = new SimpleDateFormat("M월 d일", Locale.KOREA);
            time = sdf.format(today.getTime()) + " 업데이트";
        }
        mainDjRankingList.put("time",time);

        if(DalbitUtil.isEmpty(mainDjRankingVoList)){
            mainDjRankingList.put("myRank", 0);
            mainDjRankingList.put("myPoint", 0);
            mainDjRankingList.put("myListenerPoint", 0);
            mainDjRankingList.put("myBroadPoint", 0);
            mainDjRankingList.put("myFanPoint", 0);
            mainDjRankingList.put("myGiftPoint", 0);
            mainDjRankingList.put("myLikePoint", 0);
            mainDjRankingList.put("myUpDown", 0);
            mainDjRankingList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_DJ랭킹조회_내역없음, mainDjRankingList));
        }

        List<MainDjRankingOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainDjRankingVoList.size(); i++){
            outVoList.add(new MainDjRankingOutVo(mainDjRankingVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        if(pMainDjRankingVo.getSlct_type() == 0){
            mainDjRankingList.put("time",resultMap.get("rankingDate"));
        }
        mainDjRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainDjRankingList.put("myPoint", DalbitUtil.getIntMap(resultMap, "myPoint"));
        mainDjRankingList.put("myListenerPoint", DalbitUtil.getIntMap(resultMap, "myListenerPoint"));
        mainDjRankingList.put("myBroadPoint", DalbitUtil.getIntMap(resultMap, "myBroadPoint"));
        mainDjRankingList.put("myFanPoint", DalbitUtil.getIntMap(resultMap, "myFanPoint"));
        mainDjRankingList.put("myGiftPoint", DalbitUtil.getIntMap(resultMap, "myGiftPoint"));
        mainDjRankingList.put("myLikePoint", DalbitUtil.getIntMap(resultMap, "myLikePoint"));
        mainDjRankingList.put("myUpDown", DalbitUtil.getStringMap(resultMap, "myUpDown"));
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
     * Level 랭킹
     */
    public String callMainLevelRanking(P_MainLevelRankingVo pMainLevelRankingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainLevelRankingVo);
        List<P_MainLevelRankingVo> mainLevelRankingVoList = mainDao.callMainLevelRanking(procedureVo);

        HashMap mainLevelRankingList = new HashMap();

        if(DalbitUtil.isEmpty(mainLevelRankingVoList)){
            mainLevelRankingList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_Level랭킹조회_내역없음, mainLevelRankingVoList));
        }

        List<MainLevelRankingOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainLevelRankingVoList.size(); i++){
            outVoList.add(new MainLevelRankingOutVo(mainLevelRankingVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        mainLevelRankingList.put("list", procedureOutputVo.getOutputBox());

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_Level랭킹조회_성공, mainLevelRankingList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_Level랭킹조회_실패));
        }
        return result;
    }


    public String selectBanner(HttpServletRequest request){
        String position = request.getParameter("position");
        List<BannerVo> bannerList =null;

        if("|0|1|3|4|5|6|7|8|9|".indexOf("|" + position + "|") > -1){
            String memNo = MemberVo.getMyMemNo(request);
            DeviceVo deviceVo = new DeviceVo(request);

            String platform = "";
            int osInt = deviceVo.getOs() + 1;
            if(osInt == 4){
                osInt = 1;
            }
            for(int i = 1; i < 4; i++){
                if(i == osInt){
                    platform += "1";
                }else{
                    platform += "_";
                }
            }

            P_BannerVo pBannerVo = new P_BannerVo();
            pBannerVo.setParamPlatform(platform);
            pBannerVo.setParamMemNo(memNo);
            pBannerVo.setParamDevice("" + deviceVo.getOs());
            pBannerVo.setParamPosition(position);
            bannerList = mainDao.selectBanner(pBannerVo);
        }

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, bannerList == null ? new ArrayList() : bannerList));
    }


}
