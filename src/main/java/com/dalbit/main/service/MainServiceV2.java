package com.dalbit.main.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.service.RoomService;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.*;
import com.dalbit.main.dao.MainDao;
import com.dalbit.main.etc.MainEtc;
import com.dalbit.main.proc.MainPage;
import com.dalbit.main.vo.*;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.main.vo.request.MainRankingPageVo;
import com.dalbit.main.vo.request.MainRecommandOutVo;
import com.dalbit.main.vo.request.MainTimeRankingPageVo;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MainServiceV2 {
    @Autowired GsonUtil gsonUtil;

    @Autowired RoomService roomService;
    @Autowired MainService mainService;

    @Autowired MainDao mainDao;
    @Autowired RoomDao roomDao;

    @Autowired MainPage mainPage;
    @Autowired MypageService mypageService;
    @Autowired MypageDao mypageDao;

    public String getEtcData() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("aos", MainEtc.IN_APP_UPDATE_VERSION.AOS);
        returnMap.put("ios", MainEtc.IN_APP_UPDATE_VERSION.IOS);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, returnMap));
    }
    /**
     * 메인 페이지
     */
    public String main(HttpServletRequest request) {
        Map<String, Object> mainMap = new HashMap<>();
        DeviceVo deviceVo = new DeviceVo(request);
        int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
        String memNo = MemberVo.getMyMemNo(request);
        String startUrl = request.getHeader("referer");
        String photoSvrUrl = DalbitUtil.getProperty("server.photo.url");
        String platform = "";

        int osInt = deviceVo.getOs() + 1;
        if(StringUtils.isEmpty(startUrl)) {
            startUrl = "";
        }

        if(osInt == 4){
            if(startUrl.startsWith("https://m.") || startUrl.startsWith("https://devm.") || startUrl.startsWith("https://devm2.")){
                osInt = 2;
            }else{
                osInt = 1;
            }
        }
        for(int i = 1; i < 4; i++){
            if(i == osInt){
                platform += "1";
            }else{
                platform += "_";
            }
        }

        // main 데이터 call
        P_BannerVo pBannerVo = new P_BannerVo();
        List<P_MainRecommandVo> recommendVoList = new ArrayList<>();
        List<BannerVo> bannerList = new ArrayList<>();
/*        List<P_MainTimeRankingPageVo> mainRankingPageVoList = new ArrayList<>();
        List<P_MainRankingPageVo> mainFanRankingVoList = new ArrayList<>();
        List<P_MainLoverRankingPageVo> mainLoverRankingVoList = new ArrayList<>();*/
        List<P_MainStarVo> starVoList = new ArrayList<>();
        Integer starCnt = 0;
        List<?> resultSets;

        try {
            pBannerVo.setParamPlatform(platform);
            pBannerVo.setParamMemNo(memNo);
            pBannerVo.setParamDevice("" + deviceVo.getOs());

            resultSets = mainDao.callMainAll(pBannerVo);

            if(!DalbitUtil.isEmpty(resultSets)){
                recommendVoList = DBUtil.getList(resultSets, 0, P_MainRecommandVo.class);
                bannerList = DBUtil.getList(resultSets, 1, BannerVo.class);
/*                mainRankingPageVoList = DBUtil.getList(resultSets, 2, P_MainTimeRankingPageVo.class);
                mainFanRankingVoList = DBUtil.getList(resultSets, 3, P_MainRankingPageVo.class);
                mainLoverRankingVoList = DBUtil.getList(resultSets, 4, P_MainLoverRankingPageVo.class);*/
                if(DalbitUtil.isLogin(request)){
                    Map<String, Object> starMap = new HashMap<>();
                    starMap.put("memNo", memNo);
                    starMap.put("pageNo", 1);
                    starMap.put("pagePerCnt", 10);
                    List<Object> myStarData = mainPage.getMyStar(starMap);
                    starVoList = DBUtil.getList(myStarData, 1, P_MainStarVo.class);
                    starCnt = DBUtil.getData(myStarData, 0, Integer.class);
                }
            }
        } catch (Exception e) {
            log.error("MainServiceV2 / main / callMainAll", e);
        }

        /* 상단 배너 */
        mainMap.put("topBanner", getTopSlide(recommendVoList, bannerList, deviceVo, startUrl, photoSvrUrl));

        /* 현재 방송중인 내가 등록한 스타 */
        mainMap.put("myStar", getMyStar(starVoList, isLogin, photoSvrUrl));
        mainMap.put("myStarCnt", starCnt);

        /* 메인 center 배너 */
        mainMap.put("centerBanner", mainService.selectBanner("9", request));

        /* 알림 */
        try {
            String alarmInfo = mypageService.getMyPageNew(request);
            mainMap.put("alarmInfo", new JsonParser().parse(alarmInfo).getAsJsonObject().get("data"));
        } catch (Exception e) {
            log.error("MainServiceV2 / main / 알림", e);
            mainMap.put("alarmInfo", null);
        }

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, mainMap));
    }

    /**
     *  메인 상단 슬라이드
     */
    private List<MainRecommandOutVo> getTopSlide(List<P_MainRecommandVo> recommendVoList, List<BannerVo> bannerList, DeviceVo deviceVo
        , String startUrl, String photoSvrUrl) {
        List<MainRecommandOutVo> recommend = new ArrayList<>();
        try {
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
                for(P_MainRecommandVo p_mainRecommandVo : recommendVoList) {
                    MainRecommandOutVo outVo = new MainRecommandOutVo();
                    outVo.setMemNo(p_mainRecommandVo.getMemNo());
                    outVo.setNickNm(p_mainRecommandVo.getNickNm());
                    outVo.setGender(p_mainRecommandVo.getGender());
                    if(p_mainRecommandVo.getBannerUrl().toLowerCase().endsWith(".gif")){
                        if(deviceVo.getOs() == 2 && DalbitUtil.versionCompare("14.0", deviceVo.getSdkVersion())){
                            outVo.setBannerUrl(photoSvrUrl + p_mainRecommandVo.getBannerUrl());
                        }else{
                            outVo.setBannerUrl(photoSvrUrl + p_mainRecommandVo.getBannerUrl() + ".webp");
                        }
                    }else{
                        /*if(startUrl.startsWith("https://m.") || startUrl.startsWith("https://devm.") || startUrl.startsWith("https://devm2.")){
                            outVo.setBannerUrl(photoSvrUrl + p_mainRecommandVo.getBannerUrl() + "?720x440");
                        }else{
                            outVo.setBannerUrl(photoSvrUrl + p_mainRecommandVo.getBannerUrl() + "?920x560");
                        }*/
                        outVo.setBannerUrl(photoSvrUrl + p_mainRecommandVo.getBannerUrl() + "?960x720");
                    }
                    outVo.setProfImg(new ImageVo(p_mainRecommandVo.getProfileUrl(), p_mainRecommandVo.getGender(), photoSvrUrl));
                    outVo.setRoomType(p_mainRecommandVo.getRoomType());
                    outVo.setRoomNo(p_mainRecommandVo.getRoomNo());
                    if(DalbitUtil.isEmpty(p_mainRecommandVo.getTitle())){
                        outVo.setTitle("방송 준비중");
                    }else{
                        outVo.setTitle(p_mainRecommandVo.getTitle());
                    }
                    outVo.setListeners(p_mainRecommandVo.getListeners());
                    outVo.setLikes(p_mainRecommandVo.getLikes());
                    outVo.isSpecial = p_mainRecommandVo.isSpecial();
                    outVo.badgeSpecial = p_mainRecommandVo.getBadgeSpecial();
                    outVo.isAdmin = p_mainRecommandVo.isAdmin();
                    outVo.isNew = p_mainRecommandVo.isNew();
                    outVo.isShining = p_mainRecommandVo.isShining();
                    outVo.isConDj = p_mainRecommandVo.isConDj();
                    outVo.setIsWowza(p_mainRecommandVo.getIsWowza());
                    outVo.setMediaType(p_mainRecommandVo.getMediaType());

                    //badgeService.setBadgeInfo(p_mainRecommandVo.getMemNo(), 6);
                    List<BadgeVo> badgeList = new ArrayList<>();
                    if(!DalbitUtil.isEmpty(p_mainRecommandVo.getLiveBadgeText()) && !DalbitUtil.isEmpty(p_mainRecommandVo.getLiveBadgeIcon())){
                        BadgeVo badgeVo = new BadgeVo();
                        badgeVo.setText(p_mainRecommandVo.getLiveBadgeText());
                        badgeVo.setEndColor(p_mainRecommandVo.getLiveBadgeEndColor());
                        badgeVo.setIcon(p_mainRecommandVo.getLiveBadgeIcon());
                        badgeVo.setStartColor(p_mainRecommandVo.getLiveBadgeStartColor());
                        badgeList.add(badgeVo);
                    }
                    if(!DalbitUtil.isEmpty(p_mainRecommandVo.getFanBadgeText())){
                        BadgeVo badgeVo = new BadgeVo();
                        badgeVo.setText(p_mainRecommandVo.getFanBadgeText());
                        badgeVo.setEndColor(p_mainRecommandVo.getFanBadgeEndColor());
                        badgeVo.setIcon(p_mainRecommandVo.getFanBadgeIcon());
                        badgeVo.setStartColor(p_mainRecommandVo.getFanBadgeStartColor());
                        badgeList.add(badgeVo);
                    }
                    outVo.setLiveBadgeList(badgeList);
                    outVo.setCommonBadgeList(badgeList);
                    recommend.add(outVo);

                    if(setBanner != 0 && ((bannerIdx + 1) % setBanner) == 0){
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
                            outVoB.badgeSpecial = 0;
                            outVoB.isAdmin = false;
                            recommend.add(outVoB);
                        }
                        bannerIdx++;
                    }
                }

                if(bannerIdx < bannerList.size()){
                    for(BannerVo bannerVo : bannerList) {
                        MainRecommandOutVo outVo = new MainRecommandOutVo();
                        outVo.setMemNo(bannerVo.getIdx() + "");
                        outVo.setNickNm("banner");
                        outVo.setGender("");
                        ImageVo tmpVo = new ImageVo();
                        tmpVo.setUrl(bannerVo.getThumbsUrl());
                        outVo.setProfImg(tmpVo);
                        outVo.setBannerUrl(bannerVo.getBannerUrl());
                        outVo.setRoomType(bannerVo.getLinkType());
                        outVo.setRoomNo(bannerVo.getLinkUrl());
                        if(DalbitUtil.isEmpty(bannerVo.getTitle())){
                            outVo.setTitle("방송 준비중");
                        }else{
                            outVo.setTitle(bannerVo.getTitle());
                        }
                        outVo.setListeners(0);
                        outVo.setLikes(0);
                        outVo.isSpecial = false;
                        outVo.badgeSpecial = 0;
                        outVo.isAdmin = false;
                        recommend.add(outVo);
                    }
                }
            }else{
                for(BannerVo bannerVo : bannerList) {
                    MainRecommandOutVo outVo = new MainRecommandOutVo();
                    outVo.setMemNo(bannerVo.getIdx() + "");
                    outVo.setNickNm("banner");
                    outVo.setGender("");
                    ImageVo tmpVo = new ImageVo();
                    tmpVo.setUrl(bannerVo.getThumbsUrl());
                    outVo.setProfImg(tmpVo);
                    outVo.setBannerUrl(bannerVo.getBannerUrl());
                    outVo.setRoomType(bannerVo.getLinkType());
                    outVo.setRoomNo(bannerVo.getLinkUrl());
                    if(DalbitUtil.isEmpty(bannerVo.getTitle())){
                        outVo.setTitle("방송 준비중");
                    }else{
                        outVo.setTitle(bannerVo.getTitle());
                    }
                    outVo.setListeners(0);
                    outVo.setLikes(0);
                    outVo.isSpecial = false;
                    outVo.badgeSpecial = 0;
                    outVo.isAdmin = false;
                    recommend.add(outVo);
                }
            }
        } catch (Exception e) {
            log.error("MainServiceV2 / getTopSlide", e);
        }

        return recommend;
    }

    /**
     *  myStar
     */
    private List<MainStarVo> getMyStar(List<P_MainStarVo> starVoList, int isLogin, String photoSvrUrl) {
        List<MainStarVo> myStar = new ArrayList<>();
        try {
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
        } catch (Exception e) {
            log.error("MainServiceV2 / getMyStar", e);
        }

        return myStar;
    }

    /**
     *  dj 랭킹
     */
    private List<MainTimeRankingPageOutVo> getMainDayDjRank(List<P_MainTimeRankingPageVo> mainRankingPageVoList) {
        List<MainTimeRankingPageOutVo> djRank = new ArrayList<>();
        try {
            if(!DalbitUtil.isEmpty(mainRankingPageVoList)){
                for(P_MainTimeRankingPageVo vo : mainRankingPageVoList) {
                    djRank.add(new MainTimeRankingPageOutVo(vo, false));
                }
            }
        } catch (Exception e){
            log.error("MainServiceV2 / getMainDayDjRank", e);
        }

        return djRank;
    }

    /**
     *  fan 랭킹
     */
    private List<MainFanRankingOutVo> getMainDayFanRank(List<P_MainRankingPageVo> mainFanRankingVoList) {
        List<MainFanRankingOutVo> fanRank = new ArrayList<>();
        try {
            for(P_MainRankingPageVo vo : mainFanRankingVoList) {
                fanRank.add(new MainFanRankingOutVo(vo, false));
            }

        } catch (Exception e) {
            log.error("MainServiceV2 / getMainDayFanRank", e);
        }

        return fanRank;
    }

    /**
     *  lover 랭킹
     */
    private List<MainLoverRankingOutVo> getMainDayLoverRank(List<P_MainLoverRankingPageVo> mainLoverRankingVoList) {
        List<MainLoverRankingOutVo> loverRank = new ArrayList<>();
        try {
            for(P_MainLoverRankingPageVo vo : mainLoverRankingVoList) {
                loverRank.add(new MainLoverRankingOutVo(vo, false));
            }
        } catch (Exception e) {
            log.error("MainServiceV2 / getMainDayLoverRank", e);
        }

        return loverRank;
    }

    /**
     *  메인 페이지 NOW TOP 10
     */
    public String nowTop10(String callType, HttpServletRequest request) {
        String result;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String now = dateFormat.format(new Date()) + ":00:00"; // yyyy-MM-dd HH:00:00

        if(StringUtils.equals(callType, "FAN") || StringUtils.equals(callType, "CUPID")) {
            MainRankingPageVo mainRankingPageVo = new MainRankingPageVo();
            mainRankingPageVo.setRankSlct(StringUtils.equals(callType, "FAN") ? 2 : 3);
            mainRankingPageVo.setRankType(1);
            mainRankingPageVo.setRankingDate(now);
            mainRankingPageVo.setPage(1);
            mainRankingPageVo.setRecords(10);

            P_MainRankingPageVo apiData = new P_MainRankingPageVo(mainRankingPageVo, request);

            result = mainService.mainRankingPage(request, apiData);
        }else {
            MainTimeRankingPageVo mainTimeRankingPageVo = new MainTimeRankingPageVo();
            mainTimeRankingPageVo.setRankSlct(1);
            mainTimeRankingPageVo.setPage(1);
            mainTimeRankingPageVo.setRecords(10);
            mainTimeRankingPageVo.setRankingDate(now);

            P_MainTimeRankingPageVo apiData = new P_MainTimeRankingPageVo(mainTimeRankingPageVo, request);

            result = mainService.mainTimeRankingPage(request, apiData);
        }

        return result;
    }

    public String getMainSwiper(HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);

        String photoSvrUrl = DalbitUtil.getProperty("server.photo.url");

        Map resultMap = new HashMap();

        resultMap.put("photoSvrUrl", photoSvrUrl);

        Map bannerMap = new HashMap();
        bannerMap.put("memNo", memNo);
        bannerMap.put("device", "3");
        bannerMap.put("platform", "1__");
        bannerMap.put("position", 1);

        int cnt = 0;

        List<MainSwiperVO> swiperList = new ArrayList<>();

        List<MainSwiperVO> adminBanner = mainPage.getAdminBanner(bannerMap);
        for (MainSwiperVO vo: adminBanner) {
            vo.setImage_profile(vo.getBannerUrl());
        }

        swiperList.addAll(adminBanner);
        cnt = swiperList.size();

        swiperList.addAll(mainPage.getMainStarList(bannerMap));
        cnt = swiperList.size();

        if (cnt < 10){
            swiperList.addAll(mainPage.getDayRankDjList(bannerMap));
            cnt = swiperList.size();
        }

        if (cnt < 10){
            swiperList.addAll(mainPage.getTopViewList(bannerMap));
            cnt = swiperList.size();
        }

        if (cnt < 10){
            swiperList.addAll(mainPage.getTopLikeList(bannerMap));
            cnt = swiperList.size();
        }

        if (cnt < 3){
            swiperList.addAll(mainPage.getTopLiveList(bannerMap));
        }

        if (cnt > 10){
            swiperList = swiperList.subList(0, 10);
        }

        ArrayList<MainSwiperVO> swiperList2 = new ArrayList<>(swiperList.stream().filter(distinctByKey(o-> o.getMem_no())).collect(Collectors.toList()));
        resultMap.put("swiperList", swiperList2);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, resultMap));
    }

    //중복 제거 함수
    public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
