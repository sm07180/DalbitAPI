package com.dalbit.event.proc;

import com.dalbit.event.vo.*;
import com.dalbit.event.vo.ItemInsVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface Event {
    /**********************************************************************************************
    * @Method 설명 : 아이템 지급[서비스]
    * @작성일   : 2021-11-04
    * @작성자   : 박성민
    * @param  :
    * memNo 		BIGINT		-- 회원번호
    * ,itemType 	BIGINT		-- 1:부스터
    * ,itemState  	BIGINT		-- 1: 지급, 2: 사용, 3: 차감
    * ,itemCnt 		BIGINT		-- 아이템 수
    * ,opName 	VARCHAR(50)	 -- 처리내용
    * @return
    * s_return		INT		--  -3: 이벤트 종료, -1회원 및 아이템수 없음, 0:에러, 1:정상
    **********************************************************************************************/
    @Select("CALL rd_data.p_item_ins(#{memNo}, #{itemType}, #{itemState}, #{itemCnt}, #{opName})")
    int eventItemIns(ItemInsVo param);

    //------------------------------------ 달나라 이벤트 방송방 ↓ ------------------------------------
    /** 달나라 이벤트 일정 리스트 (회차정보 - 방송방)
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Return :
     * moon_no	    INT		--     회차번호
     * start_date	DATETIME	-- 시작일자
     * end_date	    DATETIME	-- 종료일자
     * ins_date	    DATETIME	-- 등록일자
     */
    @Select("CALL rd_data.p_evt_moon_no_sel(#{noSlct})")
    List<MoonLandInfoVO> pEvtMoonNoSel(int noSlct);

    /** 달나라 이벤트 일정 리스트 (전체 회차 정보 - 이벤트페이지)
     * 작성일 : 2021-12-28
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
    @ResultMap({"ResultMap.integer", "ResultMap.map"})
    @Select("CALL rd_data.p_evt_moon_no_sel(#{noSlct})")
    List<Object> pEvtMoonNoSelMultiRows(int noSlct);

    /** 달나라 이벤트 아이템 미션 데이터 완료 리스트
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * moonNo INT                   --회차번호
     * ,roomNo BIGINT			    --방번호
     *
     * @Return :
     * s_moonNo       			INT		--     회차번호
     * s_roomNo       			BIGINT		-- 방번호
     * s_starMemNo        		BIGINT		-- 별똥별완료 회원번호
     * s_lanternsMemNo        	BIGINT		-- 풍등완료 회원번호
     * s_rocketMemNo      		BIGINT		-- 로켓완료 회원번호
     * s_balloonMemNo     		BIGINT		-- 열기구완료 회원번호
     * s_lanternsItemYn       	CHAR(1)		-- 풍등아이템 완료여부
     * s_balloonItemYn        	CHAR(1)		-- 열기구아이템 완료여부
     * s_rocketItemYn     		CHAR(1)		-- 로켓아이템 완료여부
     * s_starItemYn       		CHAR(1)		-- 별똥별아이템 완료여부
     * s_rcvYn        			CHAR(1)		-- 전체아이템 완료여부
     */
    @Select("CALL rd_data.p_evt_moon_item_mission_sel(#{moonNo}, #{roomNo})")
    MoonLandMissionSelVO pEvtMoonItemMissionSel(Map<String, Object> map);

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
    @Select("CALL rd_data.p_evt_moon_item_mission_ins(#{memNo}, #{roomNo}, #{itemCode})")
    MoonLandMissionInsVO pEvtMoonItemMissionIns(Map<String, Object> map);

    //------------------------------------ 달나라 이벤트 페이지 ↓  ------------------------------------

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
    @ResultMap({"ResultMap.integer", "ResultMap.MoonLandRankVO"})
    @Select("CALL rd_data.p_evt_moon_rank_list(#{moonNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> pEvtMoonRankList(Map<String, Object> map);

    /** 달나라 이벤트 나의 순위
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * moonNo INT			    -- 회차번호
     * ,memNo BIGINT 			-- 회원번호
     *
     * @Return :
     * moon_no;				    //INT		-- 회차번호
     * mem_no;				    //BIGINT		-- 회원번호
     * mem_id;				    //VARCHAR(20)	-- 아이디
     * mem_nick;			    //VARCHAR(20)	-- 대화명
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
     */
    @Select("CALL rd_data.p_evt_moon_rank_my_sel(#{moonNo}, #{memNo})")
    MoonLandMyRankVO pEvtMoonRankMySel(Map<String, Object> map);
    /* 달나라 끝 */

    /** 신입 웹컴 이벤트 방송방 조건 체크 프로시저
     * @Param :
     * memNo BIGINT		        -- 회원번호
     * ,memSlct BIGINT		    -- 회원[1:dj, 2:청취자]
     *
     * @Return :
     * s_return		INT		-- -3:본인인증 조건 미달, -2:청취자 자격미달, -1: dj자격 미달,  0: 에러, 1:정상
     * */
    @Select("CALL rd_data.p_welcome_room_chk(#{memNo}, #{memSlct})")
    Integer pWelcomeRoomChk(Map<String, Object> map);

    /** 달나라 이벤트 점수 등록
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * memNo        BIGINT              회원번호
     * ,ptSlct      INT                 구분[1:아이템선물(일반), 2:아이템선물(보너스), 3:좋아요(보너스), 4:부스터(보너스), 5:아이템 미션(보너스), 6:부스터(캐릭터), 7:아이템 미션(캐릭터)]
     * ,rcvScore    INT                 집계 점수
     * ,roomNo      BIGINT              점수 획득한 방번호
     * ,scoreUid    VARCHAR(200)        점수 Uid값
     * @Return :
     * s_return		INT		-- -2 달 선물 갯수 미달, -1: 동일 UID값 있음, 0: 에러 , 1: 정상
     */
    @Select("CALL rd_data.p_evt_moon_rank_pt_ins_v1(#{memNo}, #{ptSlct}, #{rcvScore}, #{roomNo}, #{scoreUid} )")
    Integer pEvtMoonRankPtIns(Map<String, Object> map);

    /** 아이템 4종미션 완료
     * @Param :
     * moonNo   INT         -- 회차번호
     * ,roomNo  BIGINT      -- 방번호
     *
     * @Return :
     * s_return	INT		-- -1: 미션 미완료, 0: 에러 , 1: 정상
     * */
    @Select("CALL rd_data.p_evt_moon_item_mission_com(#{moon}, #{roomNo})")
    Integer pEvtMoonItemMissionCom(Map<String, Object> map);


    /** 방송방 입장 : 선물 갯수 체크
     * @Param :
     * roomNo BIGINT			-- 방번호
     * ,memNo BIGINT			-- 회원번호
     *
     * @Return :
     * s_giftGold	INT		-- 방송방에서 선물한 갯수
     * */
    @Select("CALL rd_data.p_evt_moon_send_dal_chk(#{roomNo}, #{memNo})")
    Integer pEvtMoonSendDalChk(Map<String, Object> map);

    /* 달라그라운드 이벤트 */
    /**
     * ##### 달라그라운드 이벤트 회차정보 (회차 이벤트로 바뀌면 사용)
     *
     *  param: noSlct: -- 일정구분[1:해당회차, 2,전체회차, 3:마지막회차]
     *  return
     *    ground_no      -- 회차번호
     *    start_date     -- 시작일자
     *    end_date       -- 종료일자
     */
    @Select("CALL rd_data.p_evt_dalla_team_ground_no_sel(#{noSlct})")
    GroundInfoVo teamGroundNoSel(int noSlct);

    /**
     * ##### 달라그라운드 순위내역
     * param
     *   groundNo INT			-- 회차번호
     *   ,pageNo INT UNSIGNED		-- 페이지 번호
     *   ,pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * return
     *   #1
     *    cnt		INT		-- 총건수
     *
     *   #2
     *    ground_no 회차번호
     *    team_no 팀번호
     *    team_name 팀이름
     *    team_medal_code 팀 메달 코드(m000 형식)
     *    team_edge_code 팀 테두리 코드(e000 형식)
     *    team_bg_code 팀 배경 코드(b00 형식)
     *    rank_pt 랭킹점수
     *    send_dal_cnt 선물한달수
     *    rcv_byeol_cnt 받은별수
     *    new_fan_cnt 신규팬수
     *    play_time 방송시간
     *    bonus_play_time 방송시간(가선점 시간 방송)
     *    time_rank_bonus 타임랭킹 가산점
     *    ins_date 등록일자
     *    upd_date 수정일자
     */
    @ResultMap({"ResultMap.integer", "ResultMap.GroundListVo"})
    @Select("CALL rd_data.p_evt_dalla_team_ground_rank_list(#{groundNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> teamGroundRankList(GroundInputVo param);

    /**
     * ##### 달라그라운드 내 순위내역
     * param
     *   groundNo INT			-- 회차번호
     *   ,pageNo INT UNSIGNED		-- 페이지 번호
     *   ,pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * return
     *   #1
     *    cnt		INT		-- 총건수
     *
     *   #2
     *    ground_no 회차번호
     *    team_no 팀번호
     *    team_name 팀이름
     *    team_medal_code 팀 메달 코드(m000 형식)
     *    team_edge_code 팀 테두리 코드(e000 형식)
     *    team_bg_code 팀 배경 코드(b00 형식)
     *    rank_pt 랭킹점수
     *    send_dal_cnt 선물한달수
     *    rcv_byeol_cnt 받은별수
     *    new_fan_cnt 신규팬수
     *    play_time 방송시간
     *    bonus_play_time 방송시간(가선점 시간 방송)
     *    my_rank_no 내순위
     *    time_rank_bonus 타임랭킹 가산점
     *    ins_date 등록일자
     *    upd_date 수정일자
     */
    @Select("CALL rd_data.p_evt_dalla_team_ground_rank_my_sel(#{groundNo}, #{teamNo})")
    GroundListVo teamGroundMyRankList(Map<String, Object> param);

}
