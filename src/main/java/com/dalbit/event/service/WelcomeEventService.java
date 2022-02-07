package com.dalbit.event.service;

import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.proc.WelcomeEvent;
import com.dalbit.event.vo.WelcomItemListVO;
import com.dalbit.event.vo.WelcomListVO;
import com.dalbit.event.vo.WelcomeUserItemVO;
import com.dalbit.event.vo.WelcomeUserVO;
import com.dalbit.util.DBUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WelcomeEventService {
    @Autowired
    WelcomeEvent welcomeEvent;

    /**********************************************************************************************
     * @Method 설명 : 웰컴 회원 정보 가져오기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO getWelcomeUserInfo(String memNo, String memSlct) {
        ResVO result = new ResVO();
        List<WelcomListVO> userListInfo = new ArrayList<>();
        List<WelcomItemListVO> rewardListInfo = new ArrayList<>();
        Map<String, Object> resultInfo = new HashMap<>();

        try {
            userListInfo = welcomeEvent.getUserListInfo(memNo, memSlct);
            rewardListInfo = welcomeEvent.getItemListInfo(memSlct);

            List<Object> temp = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < 4; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                List<WelcomItemListVO> itemList = new ArrayList<>();

                for (int j = 0; j < rewardListInfo.size(); j++) {
                    if (rewardListInfo.get(j).getGiftStepNo().equals(i + 1)) {
                        itemList.add(rewardListInfo.get(j));
                    }
                }

                if (userListInfo.size() >= (i + 1)) {
                    Map<String, Object> map = objectMapper.convertValue(userListInfo.get(i), Map.class);
                    tempMap.putAll(map);
                } else {
                    WelcomListVO tempItem = new WelcomListVO();
                    tempItem.setStep_no(i + 1);
                    tempItem.setMem_no(memNo);
                    Map<String, Object> map = objectMapper.convertValue(tempItem, Map.class);
                    tempMap.putAll(map);
                }

                if (userListInfo.size() >= (i + 1) && userListInfo.get(i).getGiftReqYn().equals("y") && userListInfo.get(i).getGiftOrd() > 0) {
                    itemList.get(userListInfo.get(i).getGiftOrd() - 1).setGift_code(userListInfo.get(i).getGiftCode());
                    itemList.get(userListInfo.get(i).getGiftOrd() - 1).setGift_dal_cnt(userListInfo.get(i).getDalCnt());
                    itemList.get(userListInfo.get(i).getGiftOrd() - 1).setGift_name(userListInfo.get(i).getGiftName());
                    itemList.get(userListInfo.get(i).getGiftOrd() - 1).setGift_step_no(userListInfo.get(i).getStepNo());
                }

                tempMap.put("itemList", itemList);


                // 1,2,3단계 미션 달성 조건 (하드코딩)
                switch (i) {
                    case 0:
                        tempMap.put("maxMemTime", 3600); // 3600
                        tempMap.put("maxLikeCnt", 3); // 3
                        tempMap.put("maxDalCnt", 0); // 0
                        break;
                    case 1:
                        tempMap.put("maxMemTime", 18000); // 3600 * 5
                        tempMap.put("maxLikeCnt", 10); // 10
                        tempMap.put("maxDalCnt", (memSlct.equals("1") ? 50 : 0)); // (memSlct.equals(1) ? 50 : 0)
                        break;
                    case 2:
                        tempMap.put("maxMemTime", 36000); // 3600 * 10
                        tempMap.put("maxLikeCnt", 20); // 20
                        tempMap.put("maxDalCnt", (memSlct.equals("1") ? 200 : 30)); // (memSlct.equals(1) ? 200 : 30)
                        break;
                }

                if (i == 3) {
                    resultInfo.put("clearItem", tempMap);
                } else {
                    temp.add(tempMap);
                }
            }
            resultInfo.put("stepItem", temp);

            result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);

        } catch (Exception e) {
            log.error("WelcomeEventService / getWelcomeUserInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웹컴 조건 인증체크 프로시저
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호
     *             memSlct 	BIGINT		-- 회원[1:dj, 2:청취자]
     * @Return :
     **********************************************************************************************/
    public ResVO getWelcomeEventQualityInfo(String memNo) {
        ResVO result = new ResVO();
        Map<String, Object> resultInfo = new HashMap<>();

        try {
            // DJ 이벤트 자격 확인
            resultInfo.put("djQuality", (welcomeEvent.getWelcomeEventQualityInfo(memNo, "1").equals(1) ? "y" : "n"));
            // 청취자 이벤트 자격 확인
            resultInfo.put("userQuality", (welcomeEvent.getWelcomeEventQualityInfo(memNo, "2").equals(1) ? "y" : "n"));

            result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
        } catch (Exception e) {
            log.error("WelcomeEventService / checkWelcomeAuth => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웹컴 경품 선물받기
     * @작성일 : 2021-12-28
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    public ResVO putWelcomeGift(String memNo, Map<String, Object> params) {
        ResVO result = new ResVO();
        Integer resultInfo = 0;

        try {
            // 조건 체크
            Integer memCheck = 0;
            // 인증 체크
            Integer auth = 0;

            memCheck = welcomeEvent.getWelcomeEventQualityInfo(memNo, (String)params.get("giftSlct"));
            if (1 != memCheck) {
                result.setResVO(ResMessage.C39005.getCode(), ResMessage.C39005.getCodeNM(), null);
                return result;
            }

            auth = welcomeEvent.checkWelcomeAuth(memNo, (String)params.get("memPhone"), (String)params.get("giftSlct"), (String)params.get("giftStepNo"));
            if (auth == null) {
                log.error("WelcomeEventService / checkWelcomeAuth auth check error => {} {}", params, memNo);
                result.setFailResVO();
                return result;
            }

            if (auth != 1) {
                switch (auth) {
                    case -1:
                        result.setResVO(ResMessage.C39001.getCode(), ResMessage.C39001.getCodeNM(), null);
                        break;
                    case -2:
                        result.setResVO(ResMessage.C39002.getCode(), ResMessage.C39002.getCodeNM(), null);
                        break;
                    case -3:
                        result.setResVO(ResMessage.C39003.getCode(), ResMessage.C39003.getCodeNM(), null);
                        break;
                    case -4:
                        result.setResVO(ResMessage.C39007.getCode(), ResMessage.C39007.getCodeNM(), null);
                        break;
                    default:
                        result.setFailResVO();
                        break;
                }
                return result;
            }

            List<WelcomListVO> userListInfo = welcomeEvent.getUserListInfo(memNo, (String)params.get("giftSlct"));

            if (userListInfo.size() > 0) {
                WelcomListVO temp = userListInfo.get(userListInfo.size() - 1);
                Integer maxMemTime = 0;
                Integer maxLikeCnt = 0;
                Integer maxDalCnt = 0;
                // 1,2,3단계 미션 달성 조건 (하드코딩)
                switch (temp.getStepNo()) {
                    case 1:
                        maxMemTime = 3600;
                        maxLikeCnt = 3;
                        maxDalCnt = 0;
                        break;
                    case 2:
                        maxMemTime = 18000;
                        maxLikeCnt = 10;
                        maxDalCnt = params.get("giftSlct").equals("1") ? 50 : 0;
                        break;
                    case 3:
                        maxMemTime = 36000;
                        maxLikeCnt = 20;
                        maxDalCnt = params.get("giftSlct").equals("1") ? 200 : 30;
                        break;
                }
                if (temp.getMemTime() < maxMemTime || temp.getLikeCnt() < maxLikeCnt || temp.getDalCnt() < maxDalCnt) {
                    result.setResVO(ResMessage.C39005.getCode(), ResMessage.C39005.getCodeNM(), null);
                    return result;
                }
            }

            // 선물 받기 처리
            resultInfo = welcomeEvent.insWelcomeItem(params);

            if (resultInfo != null && resultInfo != 1) {
                switch (resultInfo) {
                    case -1:
                        result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
                        break;
                    case -2:
                        result.setResVO(ResMessage.C39004.getCode(), ResMessage.C39004.getCodeNM(), null);
                        break;
                    case -3:
                        result.setResVO(ResMessage.C39003.getCode(), ResMessage.C39003.getCodeNM(), null);
                        break;
                    default:
                        result.setFailResVO();
                        break;
                }
                return result;
            } else {
                result.setFailResVO();
            }

            log.info("WelcomeEventService / success welcome present => {} {} {} {}", params, memNo, auth, memCheck);
            result.setSuccessResVO(resultInfo);
        } catch (Exception e) {
            log.error("WelcomeEventService / putWelcomeGift => {} {} {}", e, params, memNo);
            result.setFailResVO();
        }

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웹컴페이지 접속 체크값 수정 (방송방에서 하루 한번만 표시용)
     * @작성일 : 2022-01-21
     * @작성자 : 박용훈
     * @변경이력 :
     * @Parameter : memNo       BIGINT
     * @Return : s_return		INT		--   -1: 이상, 0: 에러, 1:정상
     **********************************************************************************************/
    public Integer putWelcomeDayConfirmChecker(String memNo) throws Exception {
        try {
            return welcomeEvent.pWelcomeMemDayChk(Long.parseLong(memNo));
        } catch (Exception e) {
            throw new Exception();
        }
    }
}