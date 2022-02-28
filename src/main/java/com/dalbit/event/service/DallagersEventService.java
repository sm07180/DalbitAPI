package com.dalbit.event.service;

import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.procedure.P_RoomGiftVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.DallagersEvent;
import com.dalbit.event.vo.DallagersEventMySelVo;
import com.dalbit.event.vo.DallagersEventSpecialMySelVo;
import com.dalbit.event.vo.DallagersInitialAddVo;
import com.dalbit.event.vo.DallagersRoomFerverSelVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class DallagersEventService {

    @Autowired DallagersEvent dallagersEvent;
    @Autowired GsonUtil gsonUtil;
    @Autowired UserService userService;

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

    /**
     * 달라이벤트 이니셜 뽑기
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
            String useDallaGubunOne = DalbitUtil.getStringMap(param, "useDallaGubunOne");
            String useDallaGubunTwo = DalbitUtil.getStringMap(param, "useDallaGubunTwo");
            Integer seqNo = DalbitUtil.getIntMap(param, "seqNo");
            String insDallaGubun = "";
            Random random = new Random();
            int stoneNumber = random.nextInt(3001);

            // 스톤 뽑기( 결과 계산 )
            if(stoneNumber < 1000){ //0 ~ 999 : d
                insDallaGubun = "d";
            } else if (stoneNumber < 2000) { // 1000 ~ 1999 : a
                insDallaGubun = "a";
            } else { // 2000 ~ 3000 : l
                insDallaGubun = "l";
            }

            if(!seqNo.equals(0) && !StringUtils.equals(useDallaGubunOne, "") && !StringUtils.equals(useDallaGubunTwo, "") &&
                    !StringUtils.equals(insDallaGubun, "") && !StringUtils.equals(memNo, null)) {
                param.put("memNo", Long.parseLong(memNo));
                // 스톤 뽑기
                Integer resultCode = dallagersEvent.pEvtDallaCollectBbopgiIns(param);

                if(resultCode == null || resultCode == 0){
                    log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => db result error, param: {}, resultCode: {}", gsonUtil.toJson(param), resultCode);
                    return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_DB_실패));
                } else if(resultCode == -1){
                    log.error("DallagersEventService.java / pEvtDallaCollectBbopgiIns() => fail param: {}", gsonUtil.toJson(param));
                    return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_DB_실패));
                }

                // 스톤 뽑기 완료 후 내 정보 재조회
                HashMap resultData = new HashMap();
                DallagersEventMySelVo mySelVo = dallagersEvent.pEvtDallaCollectMemberRankMySel(param);
                resultData.put("resultStone", insDallaGubun);
                resultData.put("myInfo", mySelVo);

                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, resultData));
            } else {
                log.error("DallagersEventService.java / getMyRankInfo() => param Error {}", gsonUtil.toJson(param));
                return gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }
        } catch (Exception e) {
            log.error("DallagersEventService.java / getMyRankInfo() => Exception {}", e);
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

    /* 소켓 서버에 보낼 패킷데이터 생성용*/
    public HashMap getDallagersPacketData(P_RoomGiftVo pRoomGiftVo, HashMap itemMap){
        /* 리브랜딩 달라조각 모으기 이벤트 */
        HashMap result = new HashMap();
        HashMap feverInfoMap = new HashMap();
        HashMap stoneInfoMap = new HashMap();

        Long roomNo = Long.parseLong(pRoomGiftVo.getRoom_no());
        Long dalCnt = DalbitUtil.getLongMap(itemMap,"dalCnt");

        // 1) 피버 타임 발동 조건체크 (피버 시작, 종료 프로시져는 소켓서버에서 호출함)
        DallagersRoomFerverSelVo feverInfo = callEventRoomFeverInfo(roomNo); // feverInfo is null => error
        if(feverInfo == null)
            log.error("sendGift / feverInfo db result null");
        boolean feverChkContinueFlag  = false; // 누적 조건 성립시 다음 조건체크 무시하기 위한 플래그값

        // 1-1) 누적선물(별 + 부스터) 피버발동 조건 체크 ( 피버타임 조건 2개인 경우 1번만 발동 )
        Long goldCnt = Long.valueOf(feverInfo.getGold() + feverInfo.getBooster_cnt()* 20);
        if((Long.valueOf(feverInfo.getGift_fever_cnt())) - (goldCnt % 5000) < 0){
            feverChkContinueFlag = true;
            feverInfoMap.put("type", 1);
            feverInfoMap.put("time", 0);
        }

        // 1-2) 현재 방의 인원수 체크 10이하 + 방송시간 30분 이상 ( 피버타임 조건 2개인 경우 1번만 발동 )
        if(!feverChkContinueFlag) {
            List<P_RoomMemberListVo> listeners = userService.getListenerList(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no());
            if (listeners.size() < 11 && true) { //10명 이하 체크 + 방송시간 30분 이상
                feverInfoMap.put("type", 2);
                feverInfoMap.put("time", 1800); //30분
            }
            listeners = null;
        }

        boolean isFeverTime = StringUtils.equals("y", feverInfo.getFever_yn()); // 현재 피버타임 진행중 여부
        float feverValue = isFeverTime? 1.5F : 1;  // 피버타임시 스톤 획득량 1.5배 적용

        // 2) 스톤 등록 처리 (d, a, l 조각 생성)
        double sendPieceCnt = Math.floor( (dalCnt % 50) * feverValue ); // 보낸사람 (청취자)
        double rcvPieceCnt = Math.floor( (dalCnt % 100) * feverValue ); // 받는사람 (방장)
        DallagersInitialAddVo addVo = new DallagersInitialAddVo();
        addVo.setRoomNo(Long.parseLong(pRoomGiftVo.getRoom_no()));
        if(sendPieceCnt > 0){   // 스톤 등록 (청취자)
            addVo.setMemNo(Long.parseLong(pRoomGiftVo.getMem_no()));
            addVo.setCollectSlct(3);        // 구분 [1:방송청취, 2:방송시간, 3:보낸달, 4: 부스터수 ,5:받은별]
            addVo.setSlctValCnt(dalCnt);    // 달, 별 갯수
            addVo.setFeverYn(isFeverTime? "y": "n");  // 피버 타임 여부 [y,n]
            stoneInfoMap.put("toBJ", callEventInitialAdd(addVo, sendPieceCnt));
        }
        if(rcvPieceCnt > 0){    // 스톤 등록 (방장)
            addVo.setMemNo(Long.parseLong(pRoomGiftVo.getGifted_mem_no()));
            addVo.setCollectSlct(5);        // 구분 [1:방송청취, 2:방송시간, 3:보낸달, 4:부스터수 ,5:받은별]
            addVo.setSlctValCnt(dalCnt);    // 달, 별 갯수
            addVo.setFeverYn(isFeverTime? "y": "n");  // 피버 타임 여부 [y,n]
            stoneInfoMap.put("toViewer", callEventInitialAdd(addVo, rcvPieceCnt));
        }
        addVo = null;
        result.put("stoneInfo", stoneInfoMap);
        result.put("feverInfo", feverInfoMap);

        return result;
    }
}
