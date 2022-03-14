package com.dalbit.event.service;

import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.procedure.P_RoomGiftVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.DallagersEvent;
import com.dalbit.event.vo.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DallagersEventService {

    @Autowired DallagersEvent dallagersEvent;
    @Autowired GsonUtil gsonUtil;
    @Autowired UserService userService;

    private final Integer DAL_CNT = 50;   // 보낸사람 실서버 조건: 50
    private final Integer BYEOL_CNT = 100; // 받는사람 실서버 조건: 100
    private final Integer FEVER_DAL_CNT = 5000;         // 피버 조건 1) 해당 방 누적 달 5000개씩 마다 피버타임 진행
    private final Integer FEVER_ROOM_LISTENER_CNT = 10;  // 피버 조건 2) 청취자 수 10명 이하인 경우
    private final Integer FEVER_MINUTE = 30;           // 피버 조건 2) 방송 진행시간 30분 이상인 경우

    private final String dearMemNo = "11599118330637";      // Dear memNo ( 특정시간에 제외하고자 하는 유저 memNo )

    // true : 이벤트 진행x
    // 이벤트 진행 제외하기
    // 2022- 03 -08 21:59:59 ~ 23:59:59 동안 이벤트 제외
    public boolean isEventBlockDateChecker(){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(2022, 3, 11, 20, 59,59);  // pm 9 : 00 ~
        LocalDateTime endDateTime = LocalDateTime.of(2022, 3, 11, 23, 59,59);    // pm 11:59: 59

        return localDateTime.isAfter(startDateTime) && localDateTime.isBefore(endDateTime);
    }

    // true : ios
    public boolean isIOSCheck(HttpServletRequest request){
        //String ua = request.getHeader("user-agent");
        //ua = ua.toUpperCase();
        //if(ua.indexOf("IPHONE") != -1 || ua.indexOf("IPAD") != -1 || ua.indexOf("MAC") != -1){
        //    return true;
        //}

        return false;
    }

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

            // 최종 이벤트 점수 등록
            pEvtDallaCollectMemberScoreIns(addVo);

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
    /*public Boolean callEventFeverTimeStart(Long feverSlct, Long roomNo){
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
    }*/

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
    /*public Boolean callEventFeverTimeEnd(Long roomNo){
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
    }*/

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

    /**
     * 달라이벤트 스케줄 (현재 이벤트 진행여부 체크용도)
     * @Param
     *
     * @Return
     * cnt        INT		-- 로우 수
     * seq_no		INT		-- 회차번호
     * start_date	DATETIME	-- 시작일자
     * end_date		DATETIME	-- 종료일자
     * */
    public String getReqNo(){
        try {
            Map<String, Object> result = dallagersEvent.pEvtDallaCollectScheduleSel();

            if (result == null) {
                log.error("DallagersEventService.java / getReqNo() => Db result null");
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, result));
        } catch (Exception e) {
            log.error("DallagersEventService.java / getReqNo() => Exception {}",e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
    }

    // 스톤 랜덤으로 얻기
    private void resultStoneCalc(HashMap<String, Object> param, DallagersEventStoneVO resultStoneVO){
        String resultStone = "";
        Random random = new Random();
        int stoneNumber = random.nextInt(3001);

        // 1:1:1 확률로 스톤 교환하기
        if(stoneNumber < 1000){ //0 ~ 999 : d
            resultStone = "d";
            resultStoneVO.setDStone(resultStoneVO.getDStone() + 1);
        } else if (stoneNumber < 2000) { // 1000 ~ 1999 : a
            resultStone = "a";
            resultStoneVO.setAStone(resultStoneVO.getAStone() + 1);
        } else { // 2000 ~ 3000 : l
            resultStone = "l";
            resultStoneVO.setLStone(resultStoneVO.getLStone() + 1);
        }

        // 합성후 얻은 스톤 (프로시져 파라미터)
        param.put("insDallaGubun", resultStone);
    }

    // 스톤 합성하기
    private void stoneReplace(DallagersEventStoneVO slotVO, DallagersEventStoneVO resultStoneVO, HashMap<String, Object> param){
        // 스톤(d, a, l)이 짝수이면 insert proc 호출해주기
        // resultStoneVO에 얻은 스톤을 set
        if(slotVO.getDStone()/2 > 0){
            int stoneCnt = (int) Math.floor(slotVO.getDStone() / 2);  // 5

            param.put("useDallaGubunOne", "d");
            param.put("useDallaGubunTwo", "d");
            //insert proc call (스톤 교환)
            for (int i = 0; i < stoneCnt; i++) {
                resultStoneCalc(param, resultStoneVO);
                Integer resultCode = dallagersEvent.pEvtDallaCollectBbopgiIns(param);

                if(resultCode == null || resultCode == 0){
                    log.error("DallagersEventService.java / stoneReplace() => db result error, param: {}, resultCode: {}", gsonUtil.toJson(param), resultCode);
                } else if(resultCode == -1){
                    log.error("DallagersEventService.java / stoneReplace() => fail param: {}", gsonUtil.toJson(param));
                }
            }

            return;
        }

        if(slotVO.getAStone()/2 > 0) {
            int stoneCnt = (int) Math.floor(slotVO.getAStone() / 2);

            param.put("useDallaGubunOne", "a");
            param.put("useDallaGubunTwo", "a");
            //insert proc call (스톤 교환)
            for (int i = 0; i < stoneCnt; i++) {
                resultStoneCalc(param, resultStoneVO);
                Integer resultCode = dallagersEvent.pEvtDallaCollectBbopgiIns(param);

                if(resultCode == null || resultCode == 0){
                    log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => db result error, param: {}, resultCode: {}", gsonUtil.toJson(param), resultCode);
                } else if(resultCode == -1){
                    log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => fail param: {}", gsonUtil.toJson(param));
                }
            }

            return;
        }

        if(slotVO.getLStone()/2 > 0) {
            int stoneCnt = (int) Math.floor(slotVO.getLStone() / 2);
            param.put("useDallaGubunOne", "l");
            param.put("useDallaGubunTwo", "l");
            //insert proc call (스톤 교환)
            for (int i = 0; i < stoneCnt; i++) {
                resultStoneCalc(param, resultStoneVO);
                Integer resultCode = dallagersEvent.pEvtDallaCollectBbopgiIns(param);

                if(resultCode == null || resultCode == 0){
                    log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => db result error, param: {}, resultCode: {}", gsonUtil.toJson(param), resultCode);
                } else if(resultCode == -1){
                    log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => fail param: {}", gsonUtil.toJson(param));
                }
            }

            return;
        }
    }

    // 남은 스톤으로 합성하기
    private void remainStoneReplace(DallagersEventStoneVO slotVO1, DallagersEventStoneVO slotVO2, DallagersEventStoneVO slotVO3,
                                       DallagersEventStoneVO resultStoneVO, HashMap<String, Object> param){

        int remainStoneCnt = slotVO1.getDStone() % 2 + slotVO1.getAStone() % 2 + slotVO1.getLStone() % 2
                + slotVO2.getDStone() % 2 + slotVO2.getAStone() % 2 + slotVO2.getLStone() % 2
                + slotVO3.getDStone() % 2 + slotVO3.getAStone() % 2 + slotVO3.getLStone() % 2;   // 0 ~ 3

        // param 구분값 체크용 (Map에 useDallaGubunOne key로 put 했을시 체크용)
        boolean gubunBool1 = false;
        boolean gubunBool2 = false;

        if (remainStoneCnt > 1) {
            // 1번 슬롯
            if (slotVO1.getDStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "d");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "d");
                    gubunBool2 = true;
                }
            } else if (slotVO1.getAStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "a");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "a");
                    gubunBool2 = true;
                }
            } else if (slotVO1.getLStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "l");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "l");
                    gubunBool2 = true;
                }
            }

            //2번 슬롯
            if (slotVO2.getDStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "d");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "d");
                    gubunBool2 = true;
                }
            } else if (slotVO2.getAStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "a");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "a");
                    gubunBool2 = true;
                }
            } else if (slotVO2.getLStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "l");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "l");
                    gubunBool2 = true;
                }
            }

            //3번 슬롯
            if (slotVO3.getDStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "d");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "d");
                    gubunBool2 = true;
                }
            } else if (slotVO3.getAStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "a");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "a");
                    gubunBool2 = true;
                }
            } else if (slotVO3.getLStone() % 2 > 0) {
                if(!gubunBool1){
                    param.put("useDallaGubunOne", "l");
                    gubunBool1 = true;
                } else if(!gubunBool2) {
                    param.put("useDallaGubunTwo", "l");
                    gubunBool2 = true;
                }
            }

            //1:1:1 확률로 합성결과 스톤
            resultStoneCalc(param, resultStoneVO);
            Integer resultCode = dallagersEvent.pEvtDallaCollectBbopgiIns(param);

            if(resultCode == null || resultCode == 0){
                log.error("DallagersEventService.java / remainStoneReplace() => db result error, param: {}, resultCode: {}", gsonUtil.toJson(param), resultCode);
            } else if(resultCode == -1){
                log.error("DallagersEventService.java / remainStoneReplace() => fail param: {}", gsonUtil.toJson(param));
            }
        }
    }

    /**
     * 달라이벤트 이니셜 뽑기 (스톤 뽑기)
     *
     * procedure
     * @Param
     * memNo        BIGINT		-- 회원번호
     * ,useDallaGubunOne CHAR(1)		-- 구분[d,a,l](사용)
     * ,useDallaGubunTwo CHAR(1)		-- 구분[d,a,l](사용)
     * ,insDallaGubun    	CHAR(1)		-- 구분[d,a,l](받은)
     *
     * seqNo        INT         --현재 회차 (insert 후 회원정보 select 용)
     * @Return
     * s_return		INT		--  -1: 스톤 부족, 0:에러, 1: 정상
     * */
    public String pEvtDallaCollectBbopgiIns(HashMap<String, Object> param, HttpServletRequest request){
        try {
            String memNo = MemberVo.getMyMemNo(request);
            String slot1 = DalbitUtil.getStringMap(param, "slot1");   // { d : 1 }
            String slot2 = DalbitUtil.getStringMap(param, "slot2");   // { a : 1 }
            String slot3 = DalbitUtil.getStringMap(param, "slot3");   // { l : 1 }
            Integer seqNo = DalbitUtil.getIntMap(param, "seqNo");

            if(!seqNo.equals(0) && !StringUtils.equals(slot1, "") && !StringUtils.equals(slot2, "") && !StringUtils.equals(slot3, "")
                    && !StringUtils.equals(memNo, null)) {

                Gson gson = new Gson();
                // 스톤슬롯 1
                DallagersEventStoneVO slotVO1 = gson.fromJson(slot1, DallagersEventStoneVO.class);
                // 스톤슬롯 2
                DallagersEventStoneVO slotVO2 = gson.fromJson(slot2, DallagersEventStoneVO.class);
                // 스톤슬롯 3
                DallagersEventStoneVO slotVO3 = gson.fromJson(slot3, DallagersEventStoneVO.class);

                // 스톤 합성결과 VO
                DallagersEventStoneVO resultStoneVO = new DallagersEventStoneVO(0,0,0);
                param.put("memNo", Long.parseLong(memNo));

                // 슬롯 1번 - 같은 이니셜 2개씩 합성
                stoneReplace(slotVO1, resultStoneVO, param);
                param.put("useDallaGubunOne", "");
                param.put("useDallaGubunTwo", "");
                param.put("insDallaGubun", "");

                // 슬롯 2번 - 같은 이니셜 2개씩 합성
                stoneReplace(slotVO2, resultStoneVO, param);
                param.put("useDallaGubunOne", "");
                param.put("useDallaGubunTwo", "");
                param.put("insDallaGubun", "");

                // 슬롯 3번 - 같은 이니셜 2개씩 합성
                stoneReplace(slotVO3, resultStoneVO, param);
                param.put("useDallaGubunOne", "");
                param.put("useDallaGubunTwo", "");
                param.put("insDallaGubun", "");

                // 슬롯 1번, 슬롯 2번, 슬롯 3번 남은 스톤으로 합성하기
                remainStoneReplace(slotVO1, slotVO2, slotVO3, resultStoneVO, param);

                // 스톤 뽑기 완료 후 내 정보 재조회
                HashMap resultData = new HashMap();
                DallagersEventMySelVo mySelVo = dallagersEvent.pEvtDallaCollectMemberRankMySel(param);

                //프로필 이미지 obj set
                mySelVo.setProfImg(new ImageVo(mySelVo.getImage_profile(), mySelVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));

                resultData.put("resultStone", resultStoneVO);
                resultData.put("myInfo", mySelVo);

                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, resultData));
            } else {
                log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => param Error {}", gsonUtil.toJson(param));
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
    }

    /**
     * 달라이벤트 회원정보 o
     * @Param
     * seqNo INT 			-- 회차 번호
     * ,memNo BIGINT			-- 회원 번호
     *
     * @Return
     * rankNo		BIGINT		-- 순위
     * ins_d_cnt		INT		-- 사용가능한 d 수
     * ins_a_cnt		INT		-- 사용가능한 a 수
     * ins_l_cnt		INT		-- 사용가능한 l 수
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_no		BIGINT		-- 회원 번호
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    public String getMyRankInfo(Integer seqNo , HttpServletRequest request){
        try {
            HashMap<String, Object> param = new HashMap();
            String memNo = MemberVo.getMyMemNo(request);
            if(!seqNo.equals(0) && !StringUtils.equals(memNo, null)) {
                param.put("memNo", Long.parseLong(memNo));
                param.put("seqNo", seqNo);
                DallagersEventMySelVo resultVo = dallagersEvent.pEvtDallaCollectMemberRankMySel(param);
                if(resultVo == null){
                    return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
                }

                //프로필 이미지 obj set
                resultVo.setProfImg(new ImageVo(resultVo.getImage_profile(), resultVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));


                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, resultVo));
            } else {
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / getMyRankInfo() => Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
    }

    /**
     * 달라이벤트 리스트 o
     * @Param
     * seqNo 		INT 		-- 회차 번호
     * ,pageNo 		INT UNSIGNED	-- 페이지 번호
     * ,pagePerCnt 	INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * #1
     * cnt		BIGINT		-- 전체 수
     *
     * #2
     * seq_no		INT		-- 회차 번호
     * mem_no		BIGINT		-- 회원 번호
     * ins_d_cnt		INT		-- 사용가능한 d 수
     * ins_a_cnt		INT		-- 사용가능한 a 수
     * ins_l_cnt		INT		-- 사용가능한 l 수
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    public String getRankList(HashMap<String, Object> param, HttpServletRequest request){
        try {
            Integer seqNo = DalbitUtil.getIntMap(param, "seqNo");
            Integer pageNo = DalbitUtil.getIntMap(param, "pageNo");
            Integer pagePerCnt = DalbitUtil.getIntMap(param, "pagePerCnt");

            if(!seqNo.equals(0) && !pageNo.equals(0) && !pagePerCnt.equals(0)) {
                List<Object> queryList = dallagersEvent.pEvtDallaCollectMemberRankList(param);
                Integer cnt = DBUtil.getData(queryList, 0, Integer.class);
                List<DallagersEventMySelVo> list = DBUtil.getList(queryList, 1, DallagersEventMySelVo.class);

                //프로필 이미지 obj set
                for (DallagersEventMySelVo vo: list){
                    vo.setProfImg(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                }

                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, list));
            } else {
                log.error("DallagersEventService.java / getRankList() => param Error {}", gsonUtil.toJson(param));
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / getRankList() => Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
    }

    /**
     * 달라이벤트 스페셜 회원정보 o
     * @Param
     * memNo		 BIGINT		-- 회원 번호
     *
     * @Return
     * rankNo		BIGINT		-- 순위
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_no		BIGINT		-- 회원 번호
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    public String getSpecialMyRankInfo(HttpServletRequest request){
        try {
            String memNo = MemberVo.getMyMemNo(request);
            if(!StringUtils.equals(memNo, null)) {
                DallagersEventSpecialMySelVo resultVo = dallagersEvent.pEvtDallaCollectMemberSpecialRankMySel(Long.parseLong(memNo));
                if(resultVo == null){
                    return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
                }
                
                //프로필 이미지 obj 생성
                resultVo.setProfImg(new ImageVo(resultVo.getImage_profile(), resultVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));

                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, resultVo));
            } else {
                log.error("DallagersEventService.java / getSpecialMyRankInfo() => param Error {}", memNo);
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / getSpecialMyRankInfo() => Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
    }

    /**
     * 달라이벤트 회원 스페셜 리스트 o
     * @Param
     * pageNo        INT UNSIGNED	-- 페이지 번호
     * ,pagePerCnt 	INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * #1
     * cnt		BIGINT		-- 전체 수
     *
     * #2
     * seq_no        INT		-- 회차 번호
     * mem_no		BIGINT		-- 회원 번호
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    public String getSpecialRankList(HashMap<String, Object> param, HttpServletRequest request){
        try {
            Integer pageNo = DalbitUtil.getIntMap(param, "pageNo");
            Integer pagePerCnt = DalbitUtil.getIntMap(param, "pagePerCnt");

            if (!pageNo.equals(0) && !pagePerCnt.equals(0)) {
                HashMap resultMap = new HashMap();
                List<Object> queryList = dallagersEvent.pEvtDallaCollectMemberSpecialRankList(param);
                Integer cnt = DBUtil.getData(queryList, 0, Integer.class);
                List<DallagersEventSpecialMySelVo> list = DBUtil.getList(queryList, 1, DallagersEventSpecialMySelVo.class);
                resultMap.put("cnt", cnt);
                resultMap.put("list", list);

                //프로필 이미지 obj set
                for (DallagersEventSpecialMySelVo vo: list){
                    vo.setProfImg(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                }

                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, resultMap));
            } else {
                log.error("DallagersEventService.java / getSpecialRankList() => param Error {}", gsonUtil.toJson(param));
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / getSpecialRankList() => Exception {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
    }
    /**
     * 이벤트 진행중 여부 체크
     * */
    public Map<String, Object> getBroadcastEventScheduleCheck(HttpServletRequest request, String bjMemNo){
        /* 리브랜딩 달라조각 모으기 이벤트 */
        Map<String, Object> eventInfo = new HashMap();
        String link = DalbitUtil.getProperty("server.mobile.url") + "/event/rebranding?webview=new";
        eventInfo.put("visible", false);    // 이벤트 진행 여부
        eventInfo.put("pageLink", link);    // 클릭시 이동할 주소
        try {
            Map<String, Object> result = dallagersEvent.pEvtDallaCollectScheduleSel();

            if (result == null) {
                log.error("DallagersEventService.java / getReqNo() => Db result null");
            }

            // 특정 유저가 방장인 경우 이벤트 x
            //이벤트 진행중
            if (!isIOSCheck(request) && (int) result.get("seq_no") > 0 && !(StringUtils.equals(dearMemNo, bjMemNo) && isEventBlockDateChecker()) ) {
                eventInfo.put("visible", true);
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / getBroadcastEventScheduleCheck");
        }

        return eventInfo;
    }



    /**
     * 달라이벤트 점수 등록 (스톤 보낸사람, 받는사람 한번씩)
     *
     * memNo            BIGINT      회원번호
     * ,roomNo          BIGINT      방번호
     * ,collectSlct     INT         구분[1:방송청취,2:방송시간,3:보낸달,4: 부스터수,5:받은별]
     * ,slctValCnt      BIGINT      구분에서의 값 [방송청취,방송시간,보낸달,부스터수,받은별]
     * */
    public void pEvtDallaCollectMemberScoreIns(DallagersInitialAddVo vo){
        try {
            HashMap param = new HashMap();
            param.put("memNo", vo.getMemNo());
            param.put("roomNo", vo.getRoomNo());
            param.put("collectSlct", vo.getCollectSlct());
            param.put("slctValCnt", vo.getSlctValCnt());
            Integer result = dallagersEvent.pEvtDallaCollectMemberScoreIns(param);
            
            // 정상 아닌 경우
            if(result != 1){
                log.error("DallagersEventService.java / pEvtDallaCollectMemberScoreIns db result Error => {}", result);
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / pEvtDallaCollectMemberScoreIns Exception => {}", e);
        }
    }

    /**
    * 리브랜딩 스톤 모으기 이벤트 : 소켓 서버에 보낼 패킷데이터 담기
    * 일반 선물시, 부스터 선물시
    * */
    public void getDallagersPacketData(String sRoomNo,String reqMemNo,String rcvMemNo, HashMap itemMap, String actionFlag){
        /* 이벤트 진행중 여부 체크*/
        Map<String, Object> result = dallagersEvent.pEvtDallaCollectScheduleSel();

        if (result == null) {
            log.error("DallagersEventService.java / getReqNo() => Db result null");
            return;
        }

        //이벤트 진행중 아님
        if ((int) result.get("seq_no") == 0 || (StringUtils.equals(dearMemNo,  rcvMemNo) && isEventBlockDateChecker()) ) {
            return;
        }

        HashMap stoneInfoMap = new HashMap();
        HashMap feverInfoMap = new HashMap();
        boolean stoenInfoAdded = false;
        boolean feverInfoAdded = false;

        Long roomNo = Long.parseLong(sRoomNo);

        // 선물아이템 or 부스터 아이템 여부 판단
        boolean isGift = StringUtils.equals(actionFlag, "gift");
        int itemCnt = DalbitUtil.getIntMap(itemMap,"itemCnt");  //부스터 아이템인 경우 dalCnt * itemCnt 해줘야함
        Long dalCnt = DalbitUtil.getLongMap(itemMap,"dalCnt");
        Long byeolCnt =  isGift? dalCnt : 10 * itemCnt; // 방장이 받는 별
            dalCnt = isGift ? dalCnt : 20 * itemCnt; // 선물한 유저의 달

        // 1) 피버 타임 발동 조건체크 (피버 시작, 종료 프로시져는 소켓서버에서 호출함)
        DallagersRoomFerverSelVo feverInfo = callEventRoomFeverInfo(roomNo); // feverInfo is null => error
        if(feverInfo == null)
            log.error("sendGift / feverInfo db result null");

        // 1-1) 누적선물(별 + 부스터) 피버발동 조건 체크 ( 피버타임 시작이 중복인 경우 1-1) 조건 발동 )
        Long goldCnt = Long.valueOf(feverInfo.getGold() + feverInfo.getBooster_cnt()* 10);

        if((Long.valueOf(feverInfo.getGift_fever_cnt())) - Math.floor(goldCnt / FEVER_DAL_CNT) < 0){// if((Long.valueOf(feverInfo.getGift_fever_cnt())) - Math.floor(goldCnt / 5000) < 0){
            feverInfoMap.put("type", 1);
            feverInfoMap.put("time", 60);
            feverInfoAdded = true;
            log.error("DallagersEventService.java / feverTime chk => getGift_fever_cnt: {}, goldCnt : {}, FEVER_DAL_CNT: {}, floor: {}, feverInfo.getGold: {}, feverInfo.boosterCnt: {}", feverInfo.getGift_fever_cnt(), goldCnt, FEVER_DAL_CNT,
                    Math.floor(goldCnt / FEVER_DAL_CNT), feverInfo.getGold(), feverInfo.getBooster_cnt());
        }

        // 1-2) 현재 방의 인원수 체크 10이하 + 방송시간 30분 이상 ( 피버타임 시작이 중복인 경우 1-1)조건 발동 )
        if(!feverInfoAdded) {
            List<P_RoomMemberListVo> listeners = userService.getListenerList(sRoomNo, reqMemNo);

            if (listeners.size() < FEVER_ROOM_LISTENER_CNT && feverInfo.getPlay_fever_cnt() < 2) { //10명 이하 체크 + 방송시간 30분 이상
                try {
                    //30분 이상
                    Calendar now = Calendar.getInstance();
                    now.setTime(new Date());
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(feverInfo.getStart_date());
                    Calendar cmpDate = Calendar.getInstance();
                    cmpDate.setTime(startDate);
                    long diffSec = (now.getTimeInMillis() - cmpDate.getTimeInMillis()) / 1000;
                    long diffMinutes = diffSec / 60;

                    // 30분
                    if (diffMinutes >= FEVER_MINUTE) {
                        Random random = new Random();
                        Integer randNum = random.nextInt(1000);   // d, a, l
                        if(randNum > 100 && randNum <=250){ // 15%
                            feverInfoMap.put("type", 2);
                            feverInfoMap.put("time", 60);
                            feverInfoAdded = true;
                        }
                    }
                } catch (Exception e) {
                    log.error("dallagersService.java / getDallagersPacketData - feverTimeChk2 Exception {}", e);
                }
            }
            listeners = null;
        }

        boolean isFeverTime = StringUtils.equals("y", feverInfo.getFever_yn()); // 현재 피버타임 진행중 여부
        float feverValue = isFeverTime? 1.5F : 1;  // 피버타임시 스톤 획득량 1.5배 적용
        
        // 2) 스톤 등록 처리 (d, a, l 조각 생성)
        double sendPieceCnt = Math.floor( (dalCnt / DAL_CNT) * feverValue ); // 보낸사람 (청취자) 50
        double rcvPieceCnt = Math.floor( (byeolCnt / BYEOL_CNT) * feverValue ); // 받는사람 (방장) 100

        DallagersInitialAddVo addVo = new DallagersInitialAddVo();
        addVo.setRoomNo(roomNo);

        if(sendPieceCnt > 0){   // 스톤 등록 (청취자)
            addVo.setMemNo(Long.parseLong(reqMemNo));
            addVo.setCollectSlct(isGift? 3 : 4);        // 구분 [1:방송청취, 2:방송시간, 3:보낸달, 4: 부스터수 ,5:받은별]
            addVo.setSlctValCnt(dalCnt);    // 달, 별 갯수
            addVo.setFeverYn(isFeverTime? "y": "n");  // 피버 타임 여부 [y,n]
            stoneInfoMap.put("toViewer", callEventInitialAdd(addVo, sendPieceCnt));
            stoenInfoAdded = true;
        }
        if(rcvPieceCnt > 0){    // 스톤 등록 (방장)
            addVo.setMemNo(Long.parseLong(rcvMemNo));
            addVo.setCollectSlct(isGift? 5 : 4);        // 구분 [1:방송청취, 2:방송시간, 3:보낸달, 4:부스터수 ,5:받은별]
            addVo.setSlctValCnt(byeolCnt);    // 달, 별 갯수
            addVo.setFeverYn(isFeverTime? "y": "n");  // 피버 타임 여부 [y,n]
            stoneInfoMap.put("toBJ", callEventInitialAdd(addVo, rcvPieceCnt));
            stoenInfoAdded = true;
        }

        //소켓 데이터 set
        if(stoenInfoAdded) {
            itemMap.put("stoneInfo", stoneInfoMap);
        }
        if(feverInfoAdded) {
            itemMap.put("feverInfo", feverInfoMap);
        }
    }
}
