package com.dalbit.event.service;

import com.dalbit.event.proc.DallagersEvent;
import com.dalbit.event.vo.DallagersInitialAddVo;
import com.dalbit.event.vo.DallagersRoomFerverSelVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class DallagersEventService {

    @Autowired DallagersEvent dallagersEvent;
    @Autowired GsonUtil gsonUtil;

    /**
     * 이니셜 등록 (달조각 등록)
     *
     * @Param
     * memNo;                  BIGINT		-- 회원번호
     * roomNo;                 BIGINT		-- 방번호
     * collectSlct;            INT		    -- 구분[1:방송청취,2:방송시간,3:보낸달,4: 부스터수,5:받은별]
     * dallaGubun;             CHAR(1)		-- 구분[d,a,l]
     * insDallaCnt;            BIGINT		-- 받은 이니셜값
     * slctValCnt;             BIGINT		-- 구분에서의 값 [방송청취,방송시간,보낸달,부스터수,받은별]
     * feverYn;                 VARCHAR(20)	-- 피버[y,n]
     *
     * @Return
     *
     * @Api Return
     * {dStone:xx, aStone:xx, lStone:xx}
     * */
    public Map<String, Object> callEventInitialAdd(DallagersInitialAddVo addVo, double pieceCnt) {
        HashMap resultMap = new HashMap();
        //조각 갯수 랜덤으로 획득
        int dCnt = 0;
        int aCnt = 0;
        int lCnt = 0;

        try {
            Random random = new Random();
            for (int i = 0; i < pieceCnt; i++) {
                Integer randNum = random.nextInt(3001);   // d, a, l
                if(randNum < 1000){ //0 ~ 999 : d
                    dCnt ++;
                } else if (randNum < 2000) { // 1000 ~ 1999 : a
                    aCnt++;
                } else { // 2000 ~ 3000 : l
                    lCnt++;
                }
            }

            /* 이니셜 조각 등록 처리 d, a, l */
            addVo.setDallaGubun("d");
            addVo.setInsDallaCnt(1L);
            for (int i = 0; i < dCnt; i++) {   // d 이니셜 조각 ins
                Integer result = dallagersEvent.pEvtDallaCollectIns(addVo);

                if(result == null || result != 1){
                    log.error("DallagersEventService.java / callEventInitialAdd DB return null {}", gsonUtil.toJson(addVo));
                }
            }

            addVo.setDallaGubun("a");
            for (int i = 0; i < aCnt; i++) {   // a 이니셜 조각 ins
                Integer result = dallagersEvent.pEvtDallaCollectIns(addVo);

                if(result == null || result != 1){
                    log.error("DallagersEventService.java / callEventInitialAdd DB return null {}", gsonUtil.toJson(addVo));
                }
            }

            addVo.setDallaGubun("l");
            for (int i = 0; i < lCnt; i++) {   // l 이니셜 조각 ins
                Integer result = dallagersEvent.pEvtDallaCollectIns(addVo);

                if(result == null || result != 1){
                    log.error("DallagersEventService.java / callEventInitialAdd DB return null {}", gsonUtil.toJson(addVo));
                }
            }

            resultMap.put("dStone", dCnt);
            resultMap.put("aStone", aCnt);
            resultMap.put("lStone", lCnt);

            return resultMap;

        } catch (Exception e) {
            log.error("DallagersEventService.java / callEventInitialAdd Exception {}", e);
            resultMap.put("dStone", dCnt);
            resultMap.put("aStone", aCnt);
            resultMap.put("lStone", lCnt);

            return resultMap;
        }
    }


    /**
     * 피버타임 등록
     *
     * @Param
     * feverSlct        BIGINT		-- 피버 종류(1:선물,2:방송시간)
     * ,roomNo 	        BIGINT		-- 방번호
     *
     * @Return
     * true : 성공, false : 실패
     *
     * s_return		INT		--  -4:방송방시간 피버 2회 초과,  -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 현재 피버타임중 , 0: 에러, 1:정상
     * */
    public Boolean callEventFeverTimeStart(Long feverSlct, Long roomNo){
        try {
            HashMap param = new HashMap();
            param.put("feverSlct", feverSlct);
            param.put("roomNo", roomNo);
            Integer result = dallagersEvent.pEvtDallaCollectRoomFeverIns(param);

            if(result == null){
                log.error("DallagersEventService.java / callEventFeverTimeStart DB return null feverSlct: {} roomNo: {} ", feverSlct, roomNo);
            }

            return result == 1;
        } catch (Exception e) {
            log.error("DallagersEventService.java / callEventFeverTimeStart Exception {}", e);
            return false;
        }
    }

    /**
     * 피버타임 종료
     *
     * @Param
     * ,roomNo 	        BIGINT		-- 방번호
     *
     * @Return
     * true : 성공, false : 실패
     *
     * s_return		INT		--   -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 현재 피버타임없음 , 0: 에러, 1:정상
     * */
    public Boolean callEventFeverTimeEnd(Long roomNo){
        try {
            Integer result = dallagersEvent.pEvtDallaCollectRoomFeverEnd(roomNo);

            if(result == null){
                log.error("DallagersEventService.java / callEventFeverTimeEnd DB return null roomNo: {}", roomNo);
            }
            return result == 1;
        } catch (Exception e) {
            log.error("DallagersEventService.java / callEventFeverTimeEnd Exception {}", e);
            return false;
        }
    }

     /**
     * 달라이벤트 방 피버 정보
      * @Param
      * Long roomNo
      *
      * @Return
      * room_no;              //BIGINT   방번호
      * gift_fever_cnt;       //INT      피버횟수(선물)
      * play_fever_cnt;       //INT      피버횟수(시간)
      * tot_fever_cnt;        //INT      총합
      * fever_yn;             //CHAR     피버확인[y,n]
     */
    public DallagersRoomFerverSelVo callEventRoomFeverInfo(Long roomNo){
        DallagersRoomFerverSelVo resultVo = null;
        try {
            resultVo = dallagersEvent.pEvtDallaCollectRoomFeverSel(roomNo);

            if(resultVo == null){
                log.error("DallagersEventService.java / callEventRoomFeverInfo DB return null roomNo : {}", roomNo);
            }
            return resultVo;
        } catch (Exception e) {
            log.error("DallagersEventService.java / callEventRoomFeverInfo Exception {}", e);
            return null;
        }
    }

}
