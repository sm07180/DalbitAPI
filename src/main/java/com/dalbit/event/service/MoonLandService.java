package com.dalbit.event.service;

import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.MoonLandCoinDataVO;
import com.dalbit.broadcast.vo.procedure.P_RoomBoosterVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGiftVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGoodVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.broadcast.vo.request.BoosterVo;
import com.dalbit.broadcast.vo.request.GoodVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ItemVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.procedure.P_ItemVo;
import com.dalbit.event.proc.Event;
import com.dalbit.event.vo.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class MoonLandService {
    @Autowired
    Event event;

    @Autowired
    CommonDao commonDao;

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    UserService userService;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;

    /** 달나라 이벤트 일정 리스트 (현재 회차정보)
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Return :
     * moonNo	    INT		--     회차번호
     * startDate	DATETIME	-- 시작일자
     * endDate	    DATETIME	-- 종료일자
     * insDate	    DATETIME	-- 등록일자
     */
    public List<MoonLandInfoVO> getMoonLandInfoData(){
        List<MoonLandInfoVO> list = event.pEvtMoonNoSel(1); //현재 회차
        return list;
    }

    /** 달나라 이벤트 일정 리스트 (전체 회차 정보 - 이벤트페이지)
     * 작성일 : 2022-03-21
     * 작성자 : 박용훈
     * @Return :
     * @Rows 1
     * cnt
     *
     * @Rows 2
     * moon_no	    INT		--     회차번호
     * start_date	DATETIME	-- 시작일자
     * end_date	    DATETIME	-- 종료일자
     * ins_date	    DATETIME	-- 등록일자
     */
    public HashMap getMoonLandTotalInfoData(){
        try {
            HashMap map = new HashMap();
            List<MoonLandInfoVO> list = event.pEvtMoonNoSel(1); //현재 회차
            map.put("cnt", list.size());
            map.put("list", list == null? new ArrayList<>(): list);

            return map;
        } catch (Exception e) {
            log.error("MoonLandService.java / getMoonLandTotalInfoData() => Exception {}", e);
        }
        return null;
    }

    //------------------------------------ 방송방 ↓  ------------------------------------
    /** 달나라 이벤트 아이템 미션 데이터 완료 리스트 조회
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * moonNo                   INT            --회차번호
     * ,roomNo                  BIGINT		   --방번호
     *
     * @Return :
     * moonNo       			INT		--     회차번호
     * roomNo       			BIGINT		-- 방번호
     * starMemNo        		BIGINT		-- 별똥별완료 회원번호
     * lanternsMemNo        	BIGINT		-- 풍등완료 회원번호
     * rocketMemNo      		BIGINT		-- 로켓완료 회원번호
     * balloonMemNo     		BIGINT		-- 열기구완료 회원번호
     * lanternsItemYn       	CHAR(1)		-- 풍등아이템 완료여부
     * balloonItemYn        	CHAR(1)		-- 열기구아이템 완료여부
     * rocketItemYn     		CHAR(1)		-- 로켓아이템 완료여부
     * starItemYn       		CHAR(1)		-- 별똥별아이템 완료여부
     * rcvYn        			CHAR(1)		-- 전체아이템 완료여부
     * insDate      		    DATETIME	-- 등록일자
     * updDate      		    DATETIME	-- 수정일자
     */
    public Map<String,Object> getMoonLandMissionData (String roomNo, HttpServletRequest request){
        HashMap param = new HashMap();
        ArrayList<MoonLandMissionSelResultVO> resultList = new ArrayList<>();

        List<MoonLandInfoVO> list = getMoonLandInfoData();  // 현재 회차 정보 조회

        if(list == null || list.size() == 0){
            return null;
        }
        MoonLandInfoVO lastMoonNo = list.get(list.size()-1);
        param.put("moonNo", lastMoonNo.getMoon_no());
        param.put("roomNo", Long.parseLong(roomNo));
        MoonLandMissionSelVO missionSelVO = event.pEvtMoonItemMissionSel(param);

        //한번도 점수가 쌓인적이 없으면 null을 리턴함!
        if(missionSelVO == null){
            missionSelVO = new MoonLandMissionSelVO();
        }

        //선물아이템 목록 DB 조회
        List<ItemVo> itemList = getSelectItemList(request);

        //미션 아이템 정보 4종 추가 [로켓, 열기구, 풍등, 별똥별]
        //"items": [{}, ... ]
        int idx = 0;
        int sortKey = 0;    //정렬 순서 지정
        for (ItemVo data: itemList) {
            if(idx == 4) break;
            if(data.getItemNm() != null) {
                String itemName = data.getItemNm();
                String completeYn = "n";
                String itemImg = "";
                boolean included = false;

                switch(itemName){
                    case "로켓":
                        included = true;
                        completeYn = missionSelVO.getS_rocketItemYn();
                        idx++;
                        sortKey = 0;
                        itemImg = "https://image.dalbitlive.com/broadcast/event/gotomoon/pop_gotomoon-item1.png";
                        break;
                    case "열기구":
                        included = true;
                        completeYn = missionSelVO.getS_balloonItemYn();
                        idx++;
                        sortKey = 1;
                        itemImg = "https://image.dalbitlive.com/broadcast/event/gotomoon/pop_gotomoon-item2.png";
                        break;
                    case "풍등":
                        included = true;
                        completeYn = missionSelVO.getS_lanternsItemYn();
                        idx++;
                        sortKey = 2;
                        itemImg = "https://image.dalbitlive.com/broadcast/event/gotomoon/pop_gotomoon-item3.png";
                        break;
                    case "별똥별":
                        included = true;
                        completeYn = missionSelVO.getS_starItemYn();
                        idx++;
                        sortKey = 3;
                        itemImg = "https://image.dalbitlive.com/broadcast/event/gotomoon/pop_gotomoon-item4.png";
                        break;
                    default:
                        break;
                }

                if(included) {
                    resultList.add(new MoonLandMissionSelResultVO(data.getCost(), itemImg, data.getItemNo(), data.getItemNm(), completeYn, sortKey));
                }
            }
        }

        //아이템 순서대로 오름차순 정렬
        Collections.sort(resultList, new Comparator<MoonLandMissionSelResultVO>() {
            @Override
            public int compare(MoonLandMissionSelResultVO o1, MoonLandMissionSelResultVO o2) {
                if(o1.getSortKey() > o2.getSortKey()){
                    return 1;
                } else if (o1.getSortKey() < o2.getSortKey()) {
                    return -1;
                }
                return 0;
            }
        });

        //나의 랭킹 정보 조회
        MoonLandMyRankVO moonLandMyRankVO = null;
        HashMap<String, Object> rankingMap = new HashMap<>();
        HashMap<String, Object> rankingCoinMap = new HashMap<>();
        try {
            param.put("memNo", Long.parseLong(MemberVo.getMyMemNo(request)));
            moonLandMyRankVO = event.pEvtMoonRankMySel(param);
            if (moonLandMyRankVO == null) {
                log.error("MoonLandService getMoonLandMissionData => moonLandMyRankVO is null {}", moonLandMyRankVO);
            }
            moonLandMyRankVO = getCurrentTotalScore(moonLandMyRankVO);

        } catch (Exception e) {
            log.error("MoonLandService Exception getMoonLandMissionData event.pEvtMoonRankMySel(param) => Exception {}, vo: {}", e, gsonUtil.toJson(moonLandMyRankVO));
        }

        rankingMap.put("rank", moonLandMyRankVO.getMy_rank_no());    //내 순위
        rankingCoinMap.put("balance", String.valueOf(moonLandMyRankVO.getRank_pt()));  // 내 점수
        rankingCoinMap.put("total", String.valueOf(moonLandMyRankVO.getTot_score()));    // 현재 단계의 목표점수
        rankingMap.put("coin", rankingCoinMap);
        rankingMap.put("animation", moonLandMyRankVO.getAnimationUrl());
        rankingMap.put("nextGift", moonLandMyRankVO.getNext_reward() + "달");
        rankingMap.put("rankStep", moonLandMyRankVO.getRank_step());

        //최종 result
        param = new HashMap();

        param.put("items", resultList);
        param.put("ranking", rankingMap);

        return param;
    }

    //내부 로직에서 사용할 메소드
    /** 달나라 이벤트 점수 등록
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * memNo        BIGINT			-- 회원번호
     * ,ptSlct      INT			-- 구분[1:아이템선물(일반), 2:아이템선물(보너스), 3:좋아요(보너스), 4:부스터(보너스), 5:아이템 미션(보너스), 6:부스터(캐릭터), 7:아이템 미션(캐릭터)]
     * ,rcvScore    INT			-- 집계 점수
     * ,roomNo      BIGINT			-- 점수 획득한 방번호
     *
     * @Return :
     * s_return		INT		-- 0:에러, 1:정상
     */
    public int setMoonLandScoreIns(Long memNo, Integer ptSlct, Integer rcvScore, Long roomNo, String scoreUid) {
        try {
            //timestamp + roomNo + memNo
            if(memNo > 0 && ptSlct > 0 && rcvScore> 0 && roomNo > 0 && !StringUtils.equals(scoreUid, "")) {
                HashMap paramMap = new HashMap();

                paramMap.put("memNo", memNo);
                paramMap.put("ptSlct", ptSlct);
                paramMap.put("rcvScore", rcvScore);
                paramMap.put("roomNo", roomNo);
                paramMap.put("scoreUid", scoreUid);
                Integer result = event.pEvtMoonRankPtIns(paramMap);

                if(result != 1){
                    log.error("MoonLandService.java / setMoonLandScoreIns Fail errorCode: {}, param: {}", result, gsonUtil.toJson(paramMap));
                }

                return result == null? -1 : result;
            } else {
                log.error("MoonLandService setMoonLandScoreIns INVALID Param => memNo: {}, ptSlct: {}, rcvScore: {}, roomNo: {}, scoreUid", memNo, ptSlct, rcvScore, roomNo, scoreUid);
                return 0;
            }
        } catch(Exception e) {
            log.error("MoonLandService Exception setMoonLandScoreIns Error => memNo: {}, ptSlct: {}, rcvScore: {}, roomNo: {} scoreUid: {} \n Exception: {}", memNo, ptSlct, rcvScore, roomNo, scoreUid, e);
            return -1;
        }
    }

    //내부 로직에서 사용할 메소드
    /** 달나라 이벤트 아이템 미션 데이터 등록
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * memNo BIGINT			    --회원번호
     * ,roomNo BIGINT		    --방번호
     * ,itemCode VARCHAR(10)    --아이템코드
     * @Return :
     * s_Return		            INT	 -3: 이미선물한 아이템, -2:방송방 미션 완료, -1:이벤트 기간아님, 0:에러, 1:정상
     * s_rRocketItemYn	        CHAR(1)	-- 로켓아이템 완료여부
     * s_rBalloonItemYn	        CHAR(1)	-- 열기구아이템 완료여부
     * s_rLanternsItemYn	    CHAR(1)	-- 풍등아이템 완료여부
     * s_rStarItemYn		    CHAR(1)	-- 별똥별아이템 완료여부
     * s_rcvYn			        CHAR(1)	-- 전체아이템 완료여부 : "y", "n"
     */
    public MoonLandMissionInsVO setMoonLandMissionDataIns(Long memNo, Long roomNo, String itemCode){
        try {
            HashMap paramMap = new HashMap();

            paramMap.put("memNo", memNo);
            paramMap.put("roomNo", roomNo);
            paramMap.put("itemCode", itemCode);
            return event.pEvtMoonItemMissionIns(paramMap);
        } catch (Exception e) {
            log.error("MoonLandService.java / setMoonLandMissionDataIns() => Exception: {}", e);
            return null;
        }
    }

    /** 아이템 4종미션 완료 (미션 성공시 데이터 초기화 해주기)
     * @Param :
     * moonNo   INT         -- 회차번호
     * ,roomNo  BIGINT      -- 방번호
     *
     * @Return :
     * s_return	INT		-- -1: 미션 미완료, 0: 에러 , 1: 정상
     * */
    public void missionDataRefresh(Long roomNo, List<MoonLandInfoVO> moonLandRound){
        Integer moonNo = null;
        try {
            moonNo = moonLandRound.get(moonLandRound.size() -1).getMoon_no();

            if(moonNo > 0 && roomNo > 0) {
                HashMap param = new HashMap();
                param.put("moon", moonNo);
                param.put("roomNo", roomNo);

                Integer resultCode = event.pEvtMoonItemMissionCom(param);
                if(resultCode != 1){
                    log.warn("MoonLandService.java / missionDataRefresh() => fail moonNo:{}, roomNo: {}", moonNo, roomNo);
                }
            } else {
                log.warn("MoonLandService.java / missionDataRefresh() => param Error moonNo:{}, roomNo: {}", moonNo, roomNo);
            }
        } catch (Exception e) {
            log.error("MoonLandService.java / missionDataRefresh() => moonNo: {}, roomNo: {}, Exception: {}", moonNo, roomNo, e);
        }
    }

    //------------------------------------ 달나라 이벤트 페이지 ↓  ------------------------------------

    /** 달나라 이벤트 나의 순위
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * moonNo INT			    -- 회차번호
     * ,memNo BIGINT 			-- 회원번호
     *
     * @Return :
     * moon_no;				    //INT		-- 회차번호
     * mem_no;				        //BIGINT		-- 회원번호
     * mem_id;				    //VARCHAR(20)	-- 아이디
     * mem_nick;			        //VARCHAR(20)	-- 대화명
     * image_profile;			//VARCHAR(256)	-- 대표이미지
     * mem_level;			    //INT		-- 회원레벨
     * mem_state;			    //INT		-- 회원상태
     * mem_basic_item_score;	//INT		-- 아이템선물 점수(일반)
     * mem_gold_item_score;		//INT		-- 아이템선물 점수(보너스)
     * mem_gold_like_score;		//INT		-- 좋아요 점수(보너스)
     * mem_gold_booster_score;	//INT		-- 부스터 점수(보너스)
     * mem_gold_mission_score;	//INT		-- 아이템 미션 점수(보너스)
     * mem_cha_booster_score;	//INT		-- 부스터 점수(캐릭터)
     * mem_cha_mission_score;	//INT		-- 아이템 미션 점수(캐릭터)
     * rank_pt;				    //INT		-- 점수합
     * rank_step;			    //INT		-- 랭킹 단계
     * send_like_cnt;			//INT		-- 좋아요 합계
     * view_cnt;			    //INT		-- 청취시간 합계
     * my_rank_no;			    //INT		-- 나의 랭킹순위
     *
     * tot_socre;               //INT 임의로 추가한 데이터
     * next_reward;             //INT 임의로 추가한 데이터
     */
    public MoonLandMyRankVO getMoonLandMyRank(Integer moonNo, HttpServletRequest request){
        HashMap map = new HashMap();
        map.put("moonNo", moonNo);
        map.put("memNo", Long.parseLong(MemberVo.getMyMemNo(request)));
        MoonLandMyRankVO resVO = event.pEvtMoonRankMySel(map);
        return getCurrentTotalScore(resVO);
    }

    /** 달나라 이벤트 랭킹 리스트
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * moonNo INT			-- 회차번호
     * ,memNo BIGINT 			-- 회원번호
     * ,pageNo INT 			-- 페이지 번호
     * ,pagePerCnt INT 		-- 페이지 당 노출 건수 (Limit)
     *
     * @Return : [Multi Rows]
     * #1
     * cnt				            INT		-- 총카운트
     *
     * #2
     * moon_no				        INT		-- 회차번호
     * mem_no				        BIGINT		-- 회원번호
     * mem_id				        VARCHAR(20)	-- 아이디
     * mem_nick			            VARCHAR(20)	-- 대화명
     * image_profile			    VARCHAR(256)	-- 대표이미지
     * mem_level			        INT		-- 회원레벨
     * mem_state			        INT		-- 회원상태
     * mem_basic_item_score		    INT		-- 아이템선물 점수(일반)
     * mem_gold_item_score		    INT		-- 아이템선물 점수(보너스)
     * mem_gold_like_score		    INT		-- 좋아요 점수(보너스)
     * mem_gold_booster_score		INT		-- 부스터 점수(보너스)
     * mem_gold_mission_score		INT		-- 아이템 미션 점수(보너스)
     * mem_cha_booster_score		INT		-- 부스터 점수(캐릭터)
     * mem_cha_mission_score		INT		-- 아이템 미션 점수(캐릭터)
     * rank_pt				        INT		-- 점수합
     * rank_step			        INT		-- 랭킹 단계
     * send_like_cnt			    INT		-- 좋아요 합계
     * view_cnt			            INT		-- 청취시간 합계
     */
    public Map<String, Object> getMoonLandRankList(Integer moonNo, Integer pageNo, Integer pagePerCnt, HttpServletRequest request) {
        HashMap map = new HashMap();
        map.put("moonNo", moonNo);
        map.put("memNo", Long.parseLong(MemberVo.getMyMemNo(request)));
        map.put("pageNo", pageNo);
        map.put("pagePerCnt", pagePerCnt);
        List<Object> multiList = event.pEvtMoonRankList(map);

        map = new HashMap();    //맵 초기화 (재활용)
        if(multiList != null) {
            Integer cnt = DBUtil.getData(multiList, 0, Integer.class);
            List<MoonLandRankVO> list = DBUtil.getList(multiList, 1, MoonLandRankVO.class);

            map.put("cnt", cnt);
            map.put("list", setRankProperty(list, pageNo, pagePerCnt)); //setRankPropery() 순위값 계산후 리스트 반환
        } else {
            log.warn("MoonLandService getMoonLandRankList => multiList is null{}", multiList);
            map.put("cnt", 0);
            map.put("list", new ArrayList<>()); //setRankPropery() 순위값 계산후 리스트 반환
        }
        return map;
    }

    //memNo String을 Long으로 변환
    public Long getStringMemNoToLongMemNo(HttpServletRequest request){
        String memNo = "-1";
        try {
            memNo = MemberVo.getMyMemNo(request);
            return Long.parseLong(memNo);
        }catch(Exception e){
            log.error("MoonLandService Exception getStringToLong => memNo: {} error: {}", memNo, e);
            return 0L;
        }
    };

    //달나라 내 랭킹 : 다음 단계 점수, 달 보상 값 추가
    public MoonLandMyRankVO getCurrentTotalScore(MoonLandMyRankVO vo){
        if(vo == null){
            log.warn("getCurrentTotalScore param is null => {}", vo);
            vo = new MoonLandMyRankVO();
            vo.setTot_score(0);
            vo.setNext_reward(0);
            vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_1st_ani.webp");
            return vo;
        }

        switch(vo.getRank_step()){
            case 1:
                vo.setTot_score(20000);
                vo.setNext_reward(1);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_1st_ani.webp");
                break;
            case 2:
                vo.setTot_score(50000);
                vo.setNext_reward(3);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_2nd_ani.webp");
                break;
            case 3:
                vo.setTot_score(100000);
                vo.setNext_reward(5);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_3rd_ani.webp");
                break;
            case 4:
                vo.setTot_score(150000);
                vo.setNext_reward(10);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_4th_ani.webp");
                break;
            case 5:
                vo.setTot_score(200000);
                vo.setNext_reward(20);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_5th_ani.webp");
                break;
            case 6:
                vo.setTot_score(200000);
                vo.setNext_reward(0);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_6th_ani.webp");
                vo.setRank_step(0); //마지막 단계 보상 미표기 처리용
                break;
            default:
                vo.setTot_score(0);
                vo.setNext_reward(0);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_1st_ani.webp");
                break;
        }

        return vo;
    }

    //달나라 랭킹 순위 지정 (rank_num)
    public List<MoonLandRankVO> setRankProperty(List<MoonLandRankVO> list, Integer pageNo, Integer pagePerCnt){
        int startValue = (pagePerCnt * (pageNo - 1));
        for (int i = 0; i < list.size(); i++) {
            MoonLandRankVO vo = list.get(i);
            vo.setRank_num(startValue + i + 1);
        }

        return list;
    }

    //미션에서 보여줄 아이템 리스트 조회
    public List<ItemVo> getSelectItemList(HttpServletRequest request){
        String platform = "";
        DeviceVo deviceVo = new DeviceVo(request);
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

        P_ItemVo pItemVo = new P_ItemVo();
        pItemVo.setItem_slct(1);
        pItemVo.setPlatform(platform);
        pItemVo.setBooster(DalbitUtil.getProperty("item.code.boost"));
        pItemVo.setLevelUp(DalbitUtil.getProperty("item.code.levelUp"));
        pItemVo.setDirect(DalbitUtil.getProperty("item.code.direct"));
        String mainDirectCode = DalbitUtil.getProperty("item.direct.code.main");
        pItemVo.setDirects(ITEM_DIRECT_CODE);
        pItemVo.setVisibilityDirects((Arrays.stream(ITEM_DIRECT_CODE).filter(s->!s.equals(mainDirectCode))).toArray(String[]::new));

        List<ItemVo> items = commonDao.selectItemList(pItemVo);

        return items;
    }

    //달나라 - 부스터 소켓에 같이 보낼 VO
    //coinDataVO에 값 넣어준 후 coinDataVO 리턴
    public MoonLandCoinDataVO getSendBoosterMoonLandCoinDataVO(MoonLandCoinDataVO coinDataVo, HttpServletRequest request, P_RoomBoosterVo pRoomBoosterVo, BoosterVo boosterVo, HashMap resultMap){
        try {
            List<MoonLandInfoVO> moonLandRound = getMoonLandInfoData();

            //이벤트 진행중 여부 체크 (null 이면 이벤트 기간 아님)
            if(moonLandRound != null && moonLandRound.size() > 0) {
                if( boosterVo.getMemNo() != null ){
                    int boostCnt = pRoomBoosterVo.getItem_cnt(); // 부스터 갯수
                    int listenerCnt = -1; //청취자 수
                    Long rcvDjMemNo = Long.parseLong(boosterVo.getMemNo());
                    Long sendUserMemNo = Long.parseLong(MemberVo.getMyMemNo(request));
                    Long roomNo = Long.parseLong(pRoomBoosterVo.getRoom_no());

                    //청취자 수 계산
                    List<P_RoomMemberListVo> listenerList = userService.getListenerList(pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(request));
                    if(listenerList != null){
                        listenerCnt = listenerList.size();
                    } else {
                        log.error("MoonLandService Exception sendBooster => listenerList is null listenerList: {}, roomNo: {}, memNo: {}", listenerList, pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(request));
                    }

                    if(listenerCnt != -1) {
                        Random random = new Random();
                        coinDataVo = new MoonLandCoinDataVO();
                        int characterCnt = 0;
                        int goldScore = 0;
                        int characterScore = 0;
                        if (listenerCnt < 10) { //청취자 수 10명 미만
                            
                            // 3% 확률 * 부스터 갯수
                            for (int i = 0; i < boostCnt; i++) {
                                int randNumber = random.nextInt(100) + 1; // 1 ~ 100
                                if (randNumber == 50) { //3% Success
                                    /** DJ는 자동 획득, 캐릭터 코인 점수 세팅 */
                                    characterScore += random.nextInt(701) + 300; //300 ~ 1000
                                    characterCnt++;

                                } else { //3% Fail
                                    /** DJ는 자동 획득, 황금코인 점수 세팅 */
                                    goldScore += random.nextInt(21) + 10; //(10 ~ 30)
                                }
                            }
                            
                            //황금 코인 일 수 있음!
                            //bj 점수 ins
                            if(characterScore> 0) { // 3% 성공 캐릭터 코인
                                setMoonLandScoreIns(rcvDjMemNo, 6, characterScore, roomNo, makeStringUid(roomNo, rcvDjMemNo, 6));
                            }
                            if(goldScore > 0){ //3% 실패 캐릭터 코인
                                setMoonLandScoreIns(rcvDjMemNo, 4, goldScore, roomNo,  makeStringUid(roomNo, rcvDjMemNo, 4));
                            }

                        } else {    // 청취자 10명 이상
                            for (int i = 0; i < boostCnt; i++) {
                                /** DJ는 자동 획득, 황금코인 점수 세팅 */
                                goldScore += random.nextInt(21) + 10; // boostCnt & (10 ~ 30)
                            }
                            setMoonLandScoreIns(rcvDjMemNo, 4, goldScore, roomNo, makeStringUid(roomNo, rcvDjMemNo, 4));
                        }

                        coinDataVo.setCharacterCnt(characterCnt);
                        coinDataVo.setGoldScore(goldScore);
                        coinDataVo.setCharacterScore(characterScore);
                    } else {
                        log.error("MoonLandService Exception sendBooster 청취자 수 proc 실패 => listenerCnt : {}", listenerCnt);
                    }
                }
            }

        } catch (Exception e) {
            log.error("MoonLandService Exception sendBooster getSendBoosterMoonLandCoinDataVO {}", e);
        }
        return coinDataVo;
    }

    //달나라 - 아이템선물 소켓에 같이 보낼 VO
    //coinDataVO에 값 넣어준 후 coinDataVO 리턴
    public MoonLandCoinDataVO getSendItemMoonLandCoinDataVO(MoonLandCoinDataVO coinDataVo, P_RoomGiftVo pRoomGiftVo, int dalCnt, String item_code){
        //달나라 조건 체크 - 선물 갯수 10개 이상
        if (dalCnt > 9) {
            try {
                List<MoonLandInfoVO> moonLandRound = getMoonLandInfoData();
                //이벤트 진행중 여부 체크 (null 이면 이벤트 기간 아님)
                if(moonLandRound != null && moonLandRound.size() > 0) {
                    //pRoomGiftVo.getMem_no(); // sendUser
                    //pRoomGiftVo.getGifted_mem_no(); // dj
                    //pRoomGiftVo.getRoom_no();
                    long sendUser = Long.parseLong(pRoomGiftVo.getMem_no());
                    long rcvDj = Long.parseLong(pRoomGiftVo.getGifted_mem_no());
                    long roomNo = Long.parseLong(pRoomGiftVo.getRoom_no());
                    // s_Return         -3: 이미선물한 아이템, -2:방송방 미션 완료, -1:이벤트 기간아님, 0:에러, 1:정상
                    MoonLandMissionInsVO resultVO = setMoonLandMissionDataIns(sendUser, roomNo, item_code);

                    if(resultVO == null){
                        log.error("MoonLandService.java / getSendItemMoonLandCoinDataVO / setMoonLandMissionDataIns => DB return null giftVo: {}",
                                gsonUtil.toJson(pRoomGiftVo));
                        return null;
                    }
                    if (resultVO.getS_Return() != null) {
                        int s_Return = resultVO.getS_Return();
                        //일반 코인 (예외 조건 외에는 무조건 일반코인은 세팅)
                        coinDataVo = new MoonLandCoinDataVO();
                        int score = dalCnt;
                        coinDataVo.setScore(score);

                        //일반 코인 점수 ins
                        /** 선물한 시청자 자동획득 */
                        setMoonLandScoreIns(sendUser, 1, score, roomNo, makeStringUid(roomNo, sendUser, 1));

                        /** DJ는 자동 획득 */
                        setMoonLandScoreIns(rcvDj, 1, score, roomNo, makeStringUid(roomNo, rcvDj, 1));

                        switch (s_Return) {
                            case 1: // 정상 -> 달나라 소켓 보내기
                                //   4종 아이템 미션 완료
                                if (StringUtils.equals("y", resultVO.getS_rBalloonItemYn())
                                        && StringUtils.equals("y", resultVO.getS_rLanternsItemYn())
                                        && StringUtils.equals("y", resultVO.getS_rStarItemYn())
                                        && StringUtils.equals("y", resultVO.getS_rRocketItemYn()) ) {

                                    // 3% 확률 => 황금코인 or 캐릭터코인 결정
                                    Random random = new Random();
                                    int randNumber = random.nextInt(99) + 1; //1 ~ 100
                                    int goldScore = 0;  // 황금 코인
                                    int characterScore = 0;  // 캐릭터 코인
                                    if (randNumber == 50) { //3% Success
                                        /** DJ는 자동 획득, 캐릭터 코인 점수 세팅 */
                                        coinDataVo.setCharacterCnt(1);
                                        characterScore = random.nextInt(2001) + 1000;
                                        coinDataVo.setCharacterScore(characterScore);

                                        //db score ins
                                        setMoonLandScoreIns(rcvDj, 7, characterScore, roomNo, makeStringUid(roomNo, rcvDj, 7) );
                                    } else { //3% Fail
                                        /** DJ는 자동 획득, 황금코인 점수 세팅 */
                                        goldScore = random.nextInt(51) + 250;
                                        coinDataVo.setGoldScore(goldScore);

                                        //db score ins
                                        setMoonLandScoreIns(rcvDj, 5, goldScore, roomNo, makeStringUid(roomNo, rcvDj, 5) );
                                    }

                                    // 미션완료 : 초기화 proc call
                                    missionDataRefresh(roomNo, moonLandRound);

                                    coinDataVo.setMissionSuccessYn("y");    // 미션 달성여부 (달나라 뿅가는 애니메이션 재생해줘요)
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                coinDataVo = null;
                log.error("MoonLandService Exception setMoonLandMissionDataIns => sendMemNo: {} / djMemNo: {} / roomNo : {} / Exception : {}",
                        pRoomGiftVo.getMem_no(), pRoomGiftVo.getGifted_mem_no(), pRoomGiftVo.getRoom_no() ,e);
            }
        }
        return coinDataVo;
    }

    //달나라 - 좋아요 소켓에 같이 보낼 VO
    public MoonLandCoinDataVO getSendLikeMoonLandCoinDataVO(MoonLandCoinDataVO coinDataVo, HashMap returnMap, P_RoomGoodVo pRoomGoodVo, GoodVo goodVo){
        List<MoonLandInfoVO> moonLandRound= getMoonLandInfoData();

        if(moonLandRound != null && moonLandRound.size() > 0 && returnMap.get("goodRank") != null) {
            //보너스 코인 점수 ins
            Random random = new Random();
            int m_goodRank = (int) returnMap.get("goodRank");
            int eventScoreValue = 0;

            switch(m_goodRank){
                case 1: //지사
                    coinDataVo = new MoonLandCoinDataVO();
                    eventScoreValue = random.nextInt(31) + 20; // 20 ~ 50
                    //좋아요 누른사람, 일반 시청자 : 점수
                    coinDataVo.setGoldScore(eventScoreValue);
                    break;
                case 2:
                    coinDataVo = new MoonLandCoinDataVO();
                    eventScoreValue = random.nextInt(31) + 10; // 10 ~ 40
                    //좋아요 누른사람, 일반 시청자 점수
                    coinDataVo.setGoldScore(eventScoreValue);
                    break;
                case 3:
                    coinDataVo = new MoonLandCoinDataVO();
                    eventScoreValue = random.nextInt(11) + 10;  // 10~ 20
                    //좋아요 누른사람, 일반 시청자 점수
                    coinDataVo.setGoldScore(eventScoreValue);
                    break;
                default:
                    break;

            }

            Long djMemNo = Long.parseLong(goodVo.getMemNo());
            Long roomNo = Long.parseLong(pRoomGoodVo.getRoom_no());
            // ~사랑꾼 조건에 들어야 됨
            if (eventScoreValue > 0 && djMemNo > 0) {
                /** DJ는 자동 획득 */
                setMoonLandScoreIns(djMemNo, 3, eventScoreValue, roomNo, makeStringUid(roomNo, djMemNo, 3));

            } else if (djMemNo == 0) {
                log.warn("MoonLandService Exception 사랑꾼 좋아요 => djMemNo값이 없는 경우! => goodVo.getGiftedMemNo() : {}", goodVo.getMemNo());
            }
        }
        return coinDataVo;
    }

    // 방송 입장시 유저가 선물한 달 수치 (소켓서버에 전달용)
    public Integer getUserSendDalCnt(Long roomNo, Long memNo){
        try {
            HashMap param = new HashMap();
            param.put("roomNo", roomNo);
            param.put("memNo", memNo);

            return event.pEvtMoonSendDalChk(param);
        } catch (Exception e) {
            log.error("MoonLandService.java / getUserSendDalCnt => sendDalCnt chk Exception {}", e);
            return 0;
        }
    }

    // 점수 insert 프로시져에서 이 값으로 중복체크
    public String makeStringUid(Long roomNo, Long memNo, Integer type) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(System.currentTimeMillis()).append(memNo).append(type).append(roomNo);
            return stringBuilder.toString();
        }catch(Exception e){
            log.error("MoonLandService.java / makeStringUid => ", e);
            return "";
        }
    }
}
