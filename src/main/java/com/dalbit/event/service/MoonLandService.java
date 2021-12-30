package com.dalbit.event.service;

import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ItemVo;
import com.dalbit.common.vo.procedure.P_ItemVo;
import com.dalbit.event.proc.Event;
import com.dalbit.event.vo.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class MoonLandService {
    @Autowired
    Event event;

    @Autowired
    CommonDao commonDao;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;

    /** 달나라 이벤트 일정 리스트 (전체 회차정보)
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Return :
     * moonNo	    INT		--     회차번호
     * startDate	DATETIME	-- 시작일자
     * endDate	    DATETIME	-- 종료일자
     * insDate	    DATETIME	-- 등록일자
     */
    public List<MoonLandInfoVO> getMoonLandInfoData(){
        List<MoonLandInfoVO> list = event.pEvtMoonNoSel();
        log.error("test getMoonLandInfoData => {}", list);
        return list;
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
    public Map<String, Object> getMoonLandMissionData (Long roomNo, HttpServletRequest request){
        HashMap param = new HashMap();
        ArrayList<MoonLandMissionSelResultVO> resultList = new ArrayList<>();

        List<MoonLandInfoVO> list = event.pEvtMoonNoSel();//가장 최신 회차 조회 (moonNo)
            MoonLandInfoVO lastMoonNo = list.get(list.size()-1);
            param.put("moonNo", lastMoonNo.getMoon_no());
            param.put("roomNo", roomNo);
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
        for (ItemVo data: itemList) {
            if(idx == 4) break;
            if(!StringUtils.equals(null, data.getItemNm())) {
                String itemName = data.getItemNm();
                String completeYn = "n";
                boolean included = false;

                switch(itemName){
                    case "로켓":
                        included = true;
                        completeYn = missionSelVO.getS_rocketItemYn();
                        idx++;
                        break;
                    case "열기구":
                        included = true;
                        completeYn = missionSelVO.getS_balloonItemYn();
                        idx++;
                        break;
                    case "풍등":
                        included = true;
                        completeYn = missionSelVO.getS_lanternsItemYn();
                        idx++;
                        break;
                    case "별똥별":
                        included = true;
                        completeYn = missionSelVO.getS_starItemYn();
                        idx++;
                        break;
                    default:
                        break;
                }

                if(included) {
                    resultList.add(new MoonLandMissionSelResultVO(String.valueOf(data.getCost()), data.getThumbs(), data.getItemNo(), data.getItemNm(), completeYn));
                }
            }
        }

        //나의 랭킹 정보 조회
        //"ranking" : {}
        MoonLandMyRankVO moonLandMyRankVO = null;
        HashMap<String, Object> rankingMap = new HashMap<>();
        HashMap<String, Object> rankingCoinMap = new HashMap<>();
        try {
            param.put("memNo", getStringMemNoToLongMemNo(request));
            moonLandMyRankVO = event.pEvtMoonRankMySel(param);
            if (moonLandMyRankVO != null) {
                moonLandMyRankVO = getCurrentTotalScore(moonLandMyRankVO);
                rankingMap.put("rank", moonLandMyRankVO.getMy_rank_no());    //내 순위
                rankingCoinMap.put("balance", String.valueOf(moonLandMyRankVO.getRank_pt()));  // 내 점수
                rankingCoinMap.put("total", String.valueOf(moonLandMyRankVO.getTot_score()));    // 현재 단계의 목표점수
                rankingMap.put("coin", rankingCoinMap);
                rankingMap.put("animation", moonLandMyRankVO.getAnimationUrl());
                rankingMap.put("nextGift", moonLandMyRankVO.getNext_reward()+"달");
            } else {
                log.error("getMoonLandMissionData => moonLandMyRankVO is null {}", moonLandMyRankVO);
            }

        }catch(Exception e){
            log.error("getMoonLandMissionData event.pEvtMoonRankMySel(param) Exception => {}", moonLandMyRankVO);
        }

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
    public Integer setMoonLandScoreIns(Long memNo, Integer ptSlct, Integer rcvScore, Long roomNo) {
        try {
            if(memNo > 0 && ptSlct > 0 && rcvScore> 0 && roomNo > 0) {
                HashMap paramMap = new HashMap();

                paramMap.put("memNo", memNo);
                paramMap.put("ptSlct", ptSlct);
                paramMap.put("rcvScore", rcvScore);
                paramMap.put("roomNo", roomNo);

                return event.pEvtMoonRankPtIns(paramMap);
            } else {
                log.error("MoonService setMoonLandScoreIns Fail => memNo: {}, ptSlct: {}, rcvScore: {}, roomNo: {}",memNo, ptSlct, rcvScore, roomNo);
                return 0;
            }
        } catch(Exception e) {
            log.error("MoonService setMoonLandScoreIns Error => memNo: {}, ptSlct: {}, rcvScore: {}, roomNo: {} \n error: {}", memNo, ptSlct, rcvScore, roomNo, e);
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
        HashMap paramMap = new HashMap();

        paramMap.put("memNo", memNo);
        paramMap.put("roomNo", roomNo);
        paramMap.put("itemCode", itemCode);
        return event.pEvtMoonItemMissionIns(paramMap);
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
        map.put("memNo", getStringMemNoToLongMemNo(request));
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
        map.put("memNo", getStringMemNoToLongMemNo(request));
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
            log.error("MoonLandService getMoonLandRankList => multiList is null{}", multiList);
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
            log.error("MoonLandService getStringToLong => memNo: {} error: {}", memNo, e);
            return 0L;
        }
    };

    //달나라 내 랭킹 : 다음 단계 점수, 달 보상 값 추가
    public MoonLandMyRankVO getCurrentTotalScore(MoonLandMyRankVO vo){
        if(vo == null){
            log.error("getCurrentTotalScore param is null => {}", vo);
            vo.setTot_score(0);
            vo.setNext_reward(0);
            return vo;
        }

        switch(vo.getRank_step()){
            case 1:
                vo.setTot_score(50000); // 5만점
                vo.setNext_reward(5);   // 5달
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_1st_ani.webp");
                break;
            case 2:
                vo.setTot_score(150000);
                vo.setNext_reward(10);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_2nd_ani.webp");
                break;
            case 3:
                vo.setTot_score(250000);
                vo.setNext_reward(15);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_3rd_ani.webp");
                break;
            case 4:
                vo.setTot_score(400000);
                vo.setNext_reward(20);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_4th_ani.webp");
                break;
            case 5:
                vo.setTot_score(400000);
                vo.setNext_reward(0);
                vo.setAnimationUrl("https://image.dalbitlive.com/ani/webp/to_the_moon/step_5th_ani.webp");
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
}
