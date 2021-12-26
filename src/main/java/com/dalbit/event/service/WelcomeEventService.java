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
     * @Method 설명 : 웰컴 DJ 정보 가져오기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    public ResVO getWelcomeDjInfo(String memNo) {
        ResVO result = new ResVO();
        List<WelcomListVO> djListInfo = new ArrayList<>();
        List<Object> rewardListInfo = new ArrayList<>();
        Map<String, Object> resultInfo = new HashMap<>();
        LocalDate now = LocalDate.now();

        try {
            djListInfo = welcomeEvent.getDjListInfo(memNo);
            rewardListInfo = welcomeEvent.getDjItemListInfo(now.toString());

            List<WelcomItemListVO> itemList4 = DBUtil.getList(rewardListInfo, 3, WelcomItemListVO.class);

            List<Object> temp = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < 4; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                List<WelcomItemListVO> itemList = DBUtil.getList(rewardListInfo, i, WelcomItemListVO.class);


                if (djListInfo.size() >= (i + 1)) {
                    Map<String, Object> map = objectMapper.convertValue(djListInfo.get(i), Map.class);
                    tempMap.putAll(map);
                } else {
                    WelcomListVO tempItem = new WelcomListVO();
                    tempItem.setStep_no(i + 1);
                    tempItem.setMem_no(memNo);
                    Map<String, Object> map = objectMapper.convertValue(tempItem, Map.class);
                    tempMap.putAll(map);
                }

                if (djListInfo.size() >= (i + 1) && djListInfo.get(i).getDjGiftReqYn().equals("y")) {
                    tempMap.put("itemList", new String[0]);
                } else {
                    tempMap.put("itemList", itemList);
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
            log.error("WelcomeEventService / getWelcomeDjInfo => {}", e);
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웰컴 청취자 정보 가져오기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO getWelcomeUserInfo(String memNo) {
        ResVO result = new ResVO();
        List<WelcomeUserVO> userListInfo = new ArrayList<>();
        List<Object> rewardListInfo = new ArrayList<>();
        Map<String, Object> resultInfo = new HashMap<>();
        LocalDate now = LocalDate.now();

        try {
            userListInfo = welcomeEvent.getUserListInfo(memNo);
            rewardListInfo = welcomeEvent.getUserItemListInfo(now.toString());

            List<Object> temp = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < 4; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                List<WelcomeUserItemVO> itemList = DBUtil.getList(rewardListInfo, i, WelcomeUserItemVO.class);


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

                if (userListInfo.size() >= (i + 1) && userListInfo.get(i).getMemGiftReqYn().equals("y")) {
                    tempMap.put("itemList", new String[0]);
                } else {
                    tempMap.put("itemList", itemList);
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
            log.error("WelcomeEventService / getWelcomeDjInfo => {}", e);
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웰컴 조건 체크 프로시저
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO checkWelcomeAuth(String memNo) {
        ResVO result = new ResVO();
        Integer resultInfo = 0;
        try {
            //resultInfo = welcomeEvent.checkWelcomeAuth(memNo);
        } catch (Exception e) {
            log.error("WelcomeEventService / checkWelcomeAuth => {}", e);
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
}
