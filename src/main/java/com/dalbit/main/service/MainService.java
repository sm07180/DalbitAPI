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
        Calendar today = Calendar.getInstance();
        P_MainDjRankingVo pMainDjRankingVo = new P_MainDjRankingVo();
        pMainDjRankingVo.setMemLogin(isLogin);
        pMainDjRankingVo.setMem_no(memNo);
        pMainDjRankingVo.setSlct_type(1);
        //if(today.get(Calendar.HOUR_OF_DAY) > 0){
        //    pMainDjRankingVo.setSlct_type(0);
        //}
        pMainDjRankingVo.setPageNo(1);
        pMainDjRankingVo.setPageCnt(10);
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
        //if(today.get(Calendar.HOUR_OF_DAY) > 0){
        //    pMainFanRankingVo.setSlct_type(0);
        //}
        pMainFanRankingVo.setPageNo(1);
        pMainFanRankingVo.setPageCnt(10);
        ProcedureVo procedureFanRankVo = new ProcedureVo(pMainFanRankingVo);
        List<P_MainFanRankingVo> mainFanRankingVoList = mainDao.callMainFanRanking(procedureFanRankVo);
        if(DalbitUtil.isEmpty(mainFanRankingVoList)){
            pMainFanRankingVo.setSlct_type(2);
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
                if(recommendVoList.get(i).getBannerUrl().toLowerCase().endsWith(".gif")){
                    outVo.setBannerUrl(photoSvrUrl + recommendVoList.get(i).getBannerUrl());
                }else{
                    if(deviceVo.getOs() == 3 && !"Y".equals(deviceVo.getIsHybrid())){
                        outVo.setBannerUrl(photoSvrUrl + recommendVoList.get(i).getBannerUrl());
                    }else{
                        outVo.setBannerUrl(photoSvrUrl + recommendVoList.get(i).getBannerUrl() + "?720x440");
                    }
                }
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
        mainFanRankingList.put("isReward", DalbitUtil.getStringMap(resultMap, "rewardGo").equals("1") ? true : false);
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
        mainDjRankingList.put("isReward", DalbitUtil.getStringMap(resultMap, "rewardGo").equals("1") ? true : false);
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


    /**
     * 랭킹 보상 팝업
     */
    public String callMainRankReward(P_MainRankRewardVo pMainRankRewardVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainRankRewardVo);
        mainDao.callMainRankReward(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap mainRewardPopupVo = new HashMap();

        mainRewardPopupVo.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
        mainRewardPopupVo.put("text", DalbitUtil.getStringMap(resultMap, "text"));
        mainRewardPopupVo.put("icon", DalbitUtil.getStringMap(resultMap, "icon"));
        mainRewardPopupVo.put("startColor", DalbitUtil.getStringMap(resultMap, "startColor"));
        mainRewardPopupVo.put("endColor", DalbitUtil.getStringMap(resultMap, "endColor"));
        mainRewardPopupVo.put("rewardDal", DalbitUtil.getIntMap(resultMap, "reward_dal"));
        mainRewardPopupVo.put("rewardExp", DalbitUtil.getIntMap(resultMap, "reward_exp"));
        mainRewardPopupVo.put("isRandomBox", DalbitUtil.getStringMap(resultMap, "exp_random_box").equals("1") ? true : false);

        String result;
        if(procedureVo.getRet().equals(Status.랭킹보상팝업조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹보상팝업조회_성공, mainRewardPopupVo));
        }else if (procedureVo.getRet().equals(Status.랭킹보상팝업조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹보상팝업조회_요청회원_회원아님));
        }else if (procedureVo.getRet().equals(Status.랭킹보상팝업조회_TOP3_아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹보상팝업조회_TOP3_아님));
        }else if (procedureVo.getRet().equals(Status.랭킹보상팝업조회_보상테이블_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹보상팝업조회_보상테이블_없음));
        }else if (procedureVo.getRet().equals(Status.랭킹보상팝업조회_없는구분타입.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹보상팝업조회_없는구분타입));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹보상팝업조회_실패));
        }
       return result;
    }


    /**
     * 경험치 랜덤박스 열기
     */
    public String callMainRankRandomBox(P_MainRankRewardVo pMainRankRewardVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainRankRewardVo);
        mainDao.callMainRankRandomBox(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap randomBoxVo = new HashMap();

        randomBoxVo.put("rewardExp", DalbitUtil.getStringMap(resultMap, "reward_exp"));
        randomBoxVo.put("isRandomBox", DalbitUtil.getStringMap(resultMap, "exp_random_box").equals("1") ? true : false);
        randomBoxVo.put("rewardImg", DalbitUtil.getProperty("server.img.url")+"/event/attend/lottie/"+DalbitUtil.getStringMap(resultMap, "reward_exp")+".json");

        String result;
        if(procedureVo.getRet().equals(Status.랜덤박스열기_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_성공, randomBoxVo));
        }else if (procedureVo.getRet().equals(Status.랜덤박스열기_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_요청회원_회원아님));
        }else if (procedureVo.getRet().equals(Status.랜덤박스열기_TOP3_아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_TOP3_아님));
        }else if (procedureVo.getRet().equals(Status.랜덤박스열기_보상대상_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_보상대상_회원아님));
        }else if (procedureVo.getRet().equals(Status.랜덤박스열기_이미받음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_이미받음));
        }else if (procedureVo.getRet().equals(Status.랜덤박스열기_없는구분타입.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_없는구분타입));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.랜덤박스열기_실패));
        }
        return result;
    }


    /**
     * 랭킹조회
     */
    public String mainRankingPage(P_MainRankingPageVo pMainRankingPageVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMainRankingPageVo);
        List<P_MainRankingPageVo> mainRankingPageVoList = mainDao.callMainRankingPage(procedureVo);

        HashMap mainRankingList = new HashMap();

        /*String time = "";
        if(pMainRankingPageVo.getSlct_type() == 0){
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:00", Locale.KOREA);
            time = sdf.format(today);
        }else if(pMainRankingPageVo.getSlct_type() == 1){
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DATE, -1);
            SimpleDateFormat sdf = new SimpleDateFormat("M월 d일", Locale.KOREA);
            time = sdf.format(today.getTime()) + " 업데이트";
        }
        mainRankingList.put("time",time);*/

        if(DalbitUtil.isEmpty(mainRankingPageVoList)){
            mainRankingList.put("isReward", 0);
            mainRankingList.put("myRank", 0);
            mainRankingList.put("myPoint", 0);
            mainRankingList.put("myGiftPoint", 0);
            mainRankingList.put("myListenPoint", 0);
            mainRankingList.put("myUpDown", "");
            mainRankingList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.메인_랭킹조회_내역없음, mainRankingList));
        }
        List<MainRankingPageOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<mainRankingPageVoList.size(); i++){
            outVoList.add(new MainRankingPageOutVo(mainRankingPageVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        if(pMainRankingPageVo.getSlct_type() == 2){
            Calendar calendar = Calendar.getInstance();
            String date = pMainRankingPageVo.getRankingDate();
            String[] dates = date.split("-");
            int year = Integer.parseInt(dates[0]);
            int month = Integer.parseInt(dates[1]);
            int day = Integer.parseInt(dates[2]);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setMinimalDaysInFirstWeek(7);
            calendar.set(year, month - 1, day);

            mainRankingList.put("time", date.substring(0,8).replace("-",".")+" "+calendar.get(Calendar.WEEK_OF_MONTH)+"주");
        } else {
            mainRankingList.put("time", "");
        }
        mainRankingList.put("isReward", DalbitUtil.getStringMap(resultMap, "rewardGo").equals("1") ? true : false);
        mainRankingList.put("rewardRank", DalbitUtil.getIntMap(resultMap, "rewardRank"));
        mainRankingList.put("myRank", DalbitUtil.getIntMap(resultMap, "myRank"));
        mainRankingList.put("myPoint", DalbitUtil.getIntMap(resultMap, "myPoint"));
        mainRankingList.put("myLikePoint", DalbitUtil.getIntMap(resultMap, "myLikePoint"));
        mainRankingList.put("myListenerPoint", DalbitUtil.getIntMap(resultMap, "myListenerPoint"));
        mainRankingList.put("myBroadPoint", DalbitUtil.getIntMap(resultMap, "myBroadPoint"));
        mainRankingList.put("myGiftPoint", DalbitUtil.getIntMap(resultMap, "myGiftPoint"));
        mainRankingList.put("myListenPoint", DalbitUtil.getIntMap(resultMap, "myListenPoint"));
        mainRankingList.put("myUpDown", DalbitUtil.getStringMap(resultMap, "myUpDown"));
        mainRankingList.put("list", procedureOutputVo.getOutputBox());
        mainRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_랭킹조회_성공, mainRankingList));
        } else if (procedureVo.getRet().equals(Status.메인_랭킹조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_랭킹조회_요청회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.메인_랭킹조회_실패));
        }
        return result;
    }
}
