package com.dalbit.event.service;

import com.dalbit.common.code.EventStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.FestivalEvent;
import com.dalbit.event.vo.*;
import com.dalbit.util.DBUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class FestivalService {
    @Autowired FestivalEvent festivalEvent;
    @Autowired GsonUtil gsonUtil;

    /**
     *  2주년 이벤트 선물 지급
     */
    public String twoYearGiftBoxIns(String memNo, String memPhone) {
        String result = "";
        try {
            Integer giftBoxInsResult = festivalEvent.twoYearGiftBoxIns(memNo, memPhone);
            switch (giftBoxInsResult) {
                case 1:
                    result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_지급_성공, giftBoxInsResult));
                    break;
                case -1:
                    result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_회원_없음, giftBoxInsResult));
                    break;
                case -2:
                    result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_이미_받음, giftBoxInsResult));
                    break;
                case -3:
                    result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_본인인증_미인증, giftBoxInsResult));
                    break;
                default:
                    result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_지급_실패, giftBoxInsResult));
            }
        } catch (Exception e) {
            log.error("FestivalService twoYearGiftBoxIns error => ", e);
        }

        return result;
    }

    /**
     *  2주년 이벤트 선물 지급 확인
     */
    public String twoYearGiftBoxCheck(String memNo) {
        String result = "";
        try {
            Integer giftBoxCheckResult = festivalEvent.twoYearGiftBoxCheck(memNo);
            if(giftBoxCheckResult == 1 || giftBoxCheckResult == -1) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_지급_여부_확인_성공, giftBoxCheckResult));
            }else {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_선물_지급_여부_확인_실패, giftBoxCheckResult));
            }
        } catch (Exception e) {
            log.error("FestivalService twoYearGiftBoxCheck error => ", e);
        }

        return result;
    }

    /**
     *  2주년 이벤트 사연 리스트
     */
    public String twoYearStorySelect(String memNo, String pageNo, String pagePerCnt) {
        String result = "";
        try {
            HashMap<String, Object> storyDataMap = new HashMap<>();
            List<Object> storyListObj = festivalEvent.twoYearStorySelect(memNo, pageNo, pagePerCnt);
            Integer storyListCnt = DBUtil.getData(storyListObj, 0, Integer.class);
            List<FestivalStoryVo> storyList = DBUtil.getList(storyListObj, 1, FestivalStoryVo.class);
            storyDataMap.put("storyListCnt", storyListCnt);
            storyDataMap.put("storyList", storyList);

            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_조회_성공, storyDataMap));
        } catch (Exception e) {
            log.error("FestivalService twoYearStoryList error => ", e);
        }

        return result;
    }

    /**
     *  2주년 이벤트 사연 등록
     */
    public String twoYearStoryIns(String memNo, String storyConts) {
        String result = "";
        try {
            Integer storyInsResult = festivalEvent.twoYearStoryIns(memNo, storyConts);
            if(storyInsResult == 0) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_등록_성공, storyInsResult));
            }else {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_등록_실패, storyInsResult));
            }
        } catch (Exception e) {
            log.error("FestivalService twoYearStoryIns error => ", e);
        }

        return result;
    }

    /**
     *  2주년 이벤트 사연 수정
     */
    public String twoYearStoryUpd(String storyNo, String storyConts) {
        String result = "";
        try {
            Integer storyUpdResult = festivalEvent.twoYearStoryUpd(storyNo, storyConts);
            if(storyUpdResult == 1) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_등록_실패, storyUpdResult));
            }else {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_등록_실패, storyUpdResult));
            }

        } catch (Exception e) {
            log.error("FestivalService twoYearStoryUpd error => ", e);
        }

        return result;
    }

    /**
     *  2주년 이벤트 사연 삭제
     */
    public String twoYearStoryDel(String storyNo, String delChrgrName) {
        String result = "";
        try {
            Integer storyDelResult = festivalEvent.twoYearStoryDel(storyNo, delChrgrName);
            if(storyDelResult == 1) {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_등록_실패, storyDelResult));
            }else {
                result = gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_사연_등록_실패, storyDelResult));
            }
        } catch (Exception e) {
            log.error("FestivalService twoYearStoryDel error => ", e);
        }

        return result;
    }

    /**
     *  2주년 이벤트 그라운드 DJ탭
     */
    public String twoYearGroundDj(FestivalGroundInputVo festivalGroundInputVo) {
        HashMap<String, Object> groundDataMap = new HashMap<>();

        // 랭킹 리스트
        try {
            List<Object> groundDataObj = festivalEvent.twoYearGroundDjList(festivalGroundInputVo);
            Integer cnt = DBUtil.getData(groundDataObj, 0, Integer.class);
            List<FestivalGroundDjListVo> list = DBUtil.getList(groundDataObj, 1, FestivalGroundDjListVo.class);
            groundDataMap.put("listCnt", cnt);
            groundDataMap.put("list", list);
        } catch (Exception e) {
            log.error("FestivalService twoYearGroundDjList error => ", e);
        }

        // 내 정보
        try {
            FestivalGroundDjInfoVo djTabMyInfo = festivalEvent.twoYearGroundDjInfoSel(festivalGroundInputVo);
            groundDataMap.put("myInfo", djTabMyInfo);
        } catch (Exception e) {
            log.error("FestivalService twoYearGroundDjInfoSel error => ", e);
        }

        return gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_그라운드_DJ_리스트_조회_성공, groundDataMap));
    }

    /**
     *  2주년 이벤트 그라운드 시청자 탭
     */
    public String twoYearGroundViewer(FestivalGroundInputVo festivalGroundInputVo) {
        HashMap<String, Object> groundDataMap = new HashMap<>();

        // 랭킹 리스트
        try {
            List<Object> groundDataObj = festivalEvent.twoYearGroundViewerList(festivalGroundInputVo);
            Integer cnt = DBUtil.getData(groundDataObj, 0, Integer.class);
            List<FestivalGroundViewerListVo> list = DBUtil.getList(groundDataObj, 1, FestivalGroundViewerListVo.class);
            groundDataMap.put("listCnt", cnt);
            groundDataMap.put("list", list);
        } catch (Exception e) {
            log.error("FestivalService twoYearGroundViewerList error => ", e);
        }

        // 내 정보
        try {
            FestivalGroundViewerInfoVo viewerMyInfo = festivalEvent.twoYearGroundViewerInfoSel(festivalGroundInputVo);
            groundDataMap.put("", viewerMyInfo);
        } catch (Exception e) {
            log.error("FestivalService twoYearGroundViewerInfoSel error => ", e);
        }

        return gsonUtil.toJson(new JsonOutputVo(EventStatus.이주년_그라운드_시청자_리스트_조회_성공, groundDataMap));
    }
}
