package com.dalbit.event.proc;

import com.dalbit.event.vo.*;
import com.dalbit.event.vo.inputVo.NovemberFanCouponInsInputVo;
import com.dalbit.event.vo.request.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface Event {
    /**********************************************************************************************
    * @Method 설명 : 11월 이벤트 팬 부분 경품 응모 등록
    * @작성일   : 2021-11-02
    * @작성자   : 박성민
    * @param   : memNo: 회원번호, fanGiftNo: 경품번호, couponCnt: 응모권수
    * @return  : -3: 이벤트 종료, -2: 사용쿠폰수 부족, -1경품번호 없음, 0:에러, 1:정상
    **********************************************************************************************/
    @Select("CALL rd_data.p_event_november_fan_coupon_ins(#{memNo}, #{fanGiftNo}, #{couponCnt})")
    int novemberEventFanCouponIns(NovemberFanCouponInsInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 11월 이벤트 종합 경품 이벤트(팬)
     * @작성일   : 2021-11-02
     * @작성자   : 박성민
     * @param   : memNo: 회원번호
     * @return  :
     * #1
     * coupon_cnt	BIGINT		-- 사용가능 응모권
     * fan_use_coupon_cnt	BIGINT	-- 총 응모횟수
     * fan_week_use_coupon_cnt	BIGINT	-- 이번회 응모횟수
     *
     * #2
     * auto_no		BIGINT		-- 경품 번호
     * fan_gift_name	VARCAHR	-- 경품 이름
     * fan_gift_file_name	VARCAHR	-- 경품 사진
     * gift_cnt		BIGINT		-- 경품 수
     * tot_ins_cnt	BIGINT		-- 응모받은수
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * fan_use_coupon_cnt BIGINT		-- 경품별 응모한 횟수
     **********************************************************************************************/
    @ResultMap({"ResultMap.NovemberFanListOutVo1", "ResultMap.NovemberFanListOutVo2"})
    @Select("CALL rd_data.p_event_november_fan_list(#{memNo})")
    List<Object> novemberEventFanList(String memNo);

    /**********************************************************************************************
     * @Method 설명 : 11월 이벤트 회차별 추첨 이벤트(팬)
     * @작성일   : 2021-11-02
     * @작성자   : 박성민
     * @param   : memNo: 회원번호
     * @return  :
     * #1
     * fan_week_use_coupon_cnt	BIGINT	-- 이번회 응모횟수
     *
     * #2
     * evt_no		BIGINT		-- 경품 번호
     * fan_week_gift_no	BIGINT		-- 경품 번호
     * fan_week_gift_name VARCAHR	-- 경품 이름
     * fan_week_gift_file_name VARCAHR	-- 경품 사진
     * fan_week_gift_cnt	BIGINT		-- 경품 수
     * con_ins_cnt	BIGINT		-- 응모조건수
     * tot_ins_cnt	BIGINT		-- 응모조건 만족인원수
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.NovemberFanWeekListOutVo1", "ResultMap.NovemberFanWeekListOutVo2"})
    @Select("CALL rd_data.p_event_november_fan_week_list(#{memNo})")
    List<Object> novemberEventFanWeekList(String memNo);
    
    /**********************************************************************************************
    * @Method 설명 : 11월 이벤트 메인(DJ)
    * @작성일   : 2021-11-04
    * @작성자   : 박성민
    * @param  : memNo: 회원번호
    * @return :
    * #1
    * mem_no		  BIGINT		-- 회원 번호
    * rcv_dal_cnt	  BIGINT		-- 받은달
    * play_time		  BIGINT		-- 방송시간(초)
    * rcv_bonus_gold  BIGINT        -- 추가 적립금 (이벤트)
    * rcv_booster     BIGINT        -- 이벤트로 받은 부스터 수 (20개 한번만)
    *
    * #2
    * pro_dj_gift_no	BIGINT		-- 경품 번호(조건 만족)
    * pro_dj_gift_name	VARCAHR	-- 경품 이름 (조건 만족)
    * pro_dj_gift_file_name VARCAHR	-- 경품 파일이름(조건 만족)
    * pro_dj_gift_dal_cnt	BIGINT		-- 경품 달 수(조건 만족)
    * pro_dj_gift_play_time BIGINT		-- 경품 방송시간 (조건 만족)
    * pro_ins_date	DATETIME	-- 등록일자(조건 만족)
    * pro_upd_date	DATETIME	-- 수정일자(조건 만족)
    *
    * #3
    * dj_gift_no	BIGINT		-- 경품 번호
    * dj_gift_name	VARCAHR	-- 경품 이름
    * dj_gift_file_name 	VARCAHR	-- 경품 파일이름
    * dj_gift_dal_cnt	BIGINT		-- 경품 달 수
    * dj_gift_play_time 	BIGINT		-- 경품 방송시간
    * dj_gift_price BIGINT		-- 경품 가격
    * ins_date		DATETIME	-- 등록일자
    * upd_date		DATETIME	-- 수정일자
    **********************************************************************************************/
    @ResultMap({"ResultMap.NovemberDjListOutVo1", "ResultMap.NovemberDjListOutVo2", "ResultMap.NovemberDjListOutVo3"})
    @Select("CALL rd_data.p_event_november_dj_list(#{memNo})")
    List<Object> novemberEventDjList(String memNo);
    
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

    /* 깐부 이벤트~~~ */
    /**********************************************************************************************
     * @Method 설명 : 깐부 이벤트 회차번호
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * @return
     * gganbu_no - 회차번호
     * start_date - 시작일자
     * end_date - 종료일자
     * ins_date - 등록일자
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_no_sel()")
    GganbuRoundInfoReqVo getGganbuRoundInfo();

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * gganbuNo - 회차번호
     * memNo - 회원번호(신청자)
     * ptrMemNo - 회원번호(대상자)
     * @return
     * s_return:  -7: 상대가 나에게 이미신청, -6: 동일회원에게 이미신청, -5:회원아님, -4: 신청대상자 깐부있음,
     *            -3: 신청건수초과, -2: 신청대상자 탈퇴&정지회원, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_req_ins(#{gganbuNo}, #{memNo}, #{ptrMemNo})")
    int gganbuMemReqIns(GganbuMemReqInsVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 취소
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * gganbuNo - 회차번호
     * memNo - 회원번호(신청자)
     * ptrMemNo - 회원번호(대상자)
     * @return
     * s_return: -3: 신청내역없음, -2:이미수락된 데이터, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_req_cancel(#{gganbuNo}, #{memNo}, #{ptrMemNo})")
    int gganbuMemReqCancel(GganbuMemReqCancelVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 리스트
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * insSlct - 신청구분 [r:받은신청, m:나의신청]
     * gganbuNo - 회차번호
     * memNo - 회원번호
     * pageNo - 페이지 번호
     * pagePerCnt - 페이지 당 노출 건수 (Limit)
     * @return
     * row #1
     *  cnt - 총건수
     * row #2
     *  gganbu_no - 회차번호
     *  mem_no - 회원번호 (신청자)
     *  mem_id - 회원아이디 (신청자)
     *  mem_nick - 회원대화명 (신청자)
     *  image_profile - 회원사진(신청자)
     *  mem_level - 회원레벨 (신청자)
     *  mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *  ptr_mem_no - 회원번호 (대상자)
     *  ptr_mem_id - 회원아이디 (대상자)
     *  ptr_mem_nick - 회원대화명 (대상자)
     *  ptr_image_profile - 회원사진(대상자)
     *  ptr_mem_level - 회원레벨 (대상자)
     *  ptr_mem_stat - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
     *  req_date - 신청일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMemReqListOutputVo"})
    @Select("CALL rd_data.p_evt_gganbu_mem_req_list(#{insSlct}, #{gganbuNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuMemReqList(GganbuMemReqListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 받은 내역 수락
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * gganbuNo - 회차번호
     * memNo - 회원번호(신청자)
     * ptrMemNo - 회원번호(대상자)
     * @return
     * s_return: -5: 신청내역없음, -4:대상자 이미깐부있음, -3: 신청자 이미깐부있음,
     *           -2: 신청자 탈퇴&정지회원, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_ins(#{gganbuNo}, #{memNo}, #{ptrMemNo})")
    int gganbuMemIns(GganbuMemInsVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 여부 체크
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원번호
     * @return
     *   s_return: -1: 깐부없음, 0:에러, 1:깐부있음
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_chk(#{gganbuNo}, #{memNo})")
    int gganbuMemChk(GganbuMemChkVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 정보
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원번호
     * @return
     *   gganbu_no - 회차번호
     *   mem_no - 회원번호 (신청자)
     *   mem_id - 회원아이디 (신청자)
     *   mem_nick - 회원대화명 (신청자)
     *   image_profile - 회원사진(신청자)
     *   mem_level - 회원레벨 (신청자)
     *   mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *   ptr_mem_no - 회원번호 (대상자)
     *   ptr_mem_id - 회원아이디 (대상자)
     *   ptr_mem_nick - 회원대화명 (대상자)
     *   ptr_image_profile - 회원사진(대상자)
     *   ptr_mem_level - 회원레벨 (대상자)
     *   ptr_mem_stat - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
     *   red_marble - 보유중인 빨간구슬 개수
     *   yellow_marble - 보유중인 노랑구슬 개수
     *   blue_marble - 보유중인 파랑구슬 개수
     *   violet_marble - 보유중인 보라구슬 개수
     *   tot_red_marble - 총누적된 빨간구슬 개수
     *   tot_yellow_marble - 총누적된 노랑구슬 개수
     *   tot_blue_marble - 총누적된 파랑구슬 개수
     *   tot_violet_marble - 총누적된 보라구슬 개수
     *   marble_pocket - 보유중인 구슬주머니
     *   marble_pocket_pt - 획득한 구슬주머니 점수
     *   ins_date - 등록일자
     *   upd_date - 수정일자
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_sel(#{gganbuNo}, #{memNo})")
    GganbuMemSelVo gganbuMemSel(GganbuMemSelInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 방송시간 집계등록
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     * @return
     *   s_return: -2: 신청자 깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
     *   s_rcvCnt: 지급할 구슬의수
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_view_stat_ins(#{memNo})")
    GganbuMemViewStatInsVo gganbuMemViewStatIns(String memNo);

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 획득
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     *   insSlct - 획득구분 [r:방송, c:달충전, e:교환, b:배팅]
     *   roomNo - 룸번호[획득구분 r에서 사용]
     *   rMarbleCnt - 빨강구슬 획득수
     *   yMarbleCnt - 노랑구슬 획득수
     *   bMarbleCnt - 파랑구슬 획득수
     *   vMarbleCnt - 보라구슬 획득수
     *   winSlct - 배팅시 승패여부 [w: 승, l: 패]
     *   bettingSlct - 배팅 구분[a:홀, b:짝]
     * @return
     *   s_return: -5: 하루 2회, -4: 배팅구슬 부족, -3: 이미 지급됨, -2: 신청자 이미깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_ins(#{memNo}, #{insSlct}, #{roomNo}, #{rMarbleCnt}, #{yMarbleCnt}, #{bMarbleCnt}, #{vMarbleCnt}, #{winSlct}, #{bettingSlct})")
    int gganbuMemMarbleIns(GganbuMemMarbleInsInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 리포트
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원번호
     *   pageNo
     *   pagePerCnt
     * @return
     *   #1
     *     cnt - 총건수
     *   #2
     *     gganbu_no - 회차번호
     *     mem_no - 회원번호 (신청자)
     *     mem_nick - 회원대화명 (신청자)
     *     ins_slct - 구슬 획득/사용 위치 구분 [r:방송, c:달충전, e:교환, b:배팅]
     *     chng_slct - 획득/사용 구분[s:획득, u:사용]
     *     red_marble - 빨강구슬수
     *     yellow_marble - 노랑구슬수
     *     blue_marble - 파랑구슬수
     *     violet_marble - 보라구슬수
     *     room_no - 룸번호
     *     ins_date - 등록일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMarbleLogListVo"})
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_log_list(#{gganbuNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuMemMarbleLogList(GganbuMemMarbleLogListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬주머니 집계등록
     * @작성일   : 2021-12-03
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     *   ptrMemNo - dj회원번호
     *   roomNo - 룸번호
     *   dalCnt - 달수
     * @return
     *   s_return: -2: 신청자 깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_pocket_stat_ins(#{memNo}, #{roomNo}, #{insSlct}, #{dalCnt})")
    int gganbuMarblePocketStatIns(GganbuMarbleExchangeInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 주머니 획득(구슬 교환)
     * @작성일   : 2021-12-03
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     * @return
     *   s_return: -3: 구슬개수 부족, -2: 신청자 이미깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_exchange(#{memNo})")
    Integer gganbuMarbleExchange(String memNo);

    /**********************************************************************************************
     * @Method 설명 : 구슬 주머니 오픈
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     *   marblePocketPt - 주머니 점수
     * @return
     *   s_return: -3: 구슬주머니 없음, -2: 신청자 이미깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_pocket_open_ins(#{memNo}, #{marblePocketPt})")
    int gganbuMarblePocketOpenIns(GganbuMarblePocketOpenInsVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 주머니 리포트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원번호
     *   pageNo
     *   pagePerCnt
     * @return
     *   #1
     *     cnt
     *   #2
     *     gganbu_no - 회차번호
     *     mem_no - 회원번호(신청자)
     *     mem_nick - 회원대화명(신청자)
     *     marble_pocket_pt - 구슬주머니 점수
     *     ins_date - 등록일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMarblePocketLogListVo"})
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_pocket_log_list(#{gganbuNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuMarblePocketLogList(GganbuMarblePocketLogListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 랭킹리스트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   pageNo
     *   pagePerCnt
     * @return
     *   #1
     *     cnt
     *   #2
     *     gganbu_no - 회차번호
     *     mem_no - 회원번호 (신청자)
     *     mem_id - 회원아이디 (신청자)
     *     mem_nick - 회원대화명 (신청자)
     *     image_profile - 회원사진(신청자)
     *     mem_level - 회원레벨 (신청자)
     *     mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *     ptr_mem_no - 회원번호 (대상자)
     *     ptr_mem_id - 회원아이디 (대상자)
     *     ptr_mem_nick - 회원대화명 (대상자)
     *     ptr_image_profile - 회원사진(대상자)
     *     ptr_mem_level - 회원레벨 (대상자)
     *     ptr_mem_stat - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
     *     marble_pocket_pt - 구슬주머니 점수
     *     ins_date - 등록일자
     *     upd_date - 수정일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuRankListVo"})
    @Select("CALL rd_data.p_evt_gganbu_rank_list(#{gganbuNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuRankList(GganbuRankListInputVo param);


    /**********************************************************************************************
     * @Method 설명 : 깐부 랭킹리스트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   pageNo
     *   pagePerCnt
     * @return
     *   #1
     *     cnt
     *   #2
     *     gganbu_no - 회차번호
     *     mem_no - 회원번호 (신청자)
     *     mem_id - 회원아이디 (신청자)
     *     mem_nick - 회원대화명 (신청자)
     *     image_profile - 회원사진(신청자)
     *     mem_level - 회원레벨 (신청자)
     *     mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *     ptr_mem_no - 회원번호 (대상자)
     *     ptr_mem_id - 회원아이디 (대상자)
     *     ptr_mem_nick - 회원대화명 (대상자)
     *     ptr_image_profile - 회원사진(대상자)
     *     ptr_mem_level - 회원레벨 (대상자)
     *     ptr_mem_stat - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
     *     marble_pocket_pt - 구슬주머니 점수
     *     ins_date - 등록일자
     *     upd_date - 수정일자
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_rank_my_sel(#{gganbuNo}, #{memNo})")
    GganbuRankListVo gganbuMyRankList(GganbuRankListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 배팅 리스트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   pageNo
     *   pagePerCnt
     * @return
     *   #1
     *     cnt
     *   #2
     *     gganbu_no - 회차번호
     *     mem_no - 회원번호 (신청자)
     *     mem_id - 회원아이디 (신청자)
     *     mem_nick - 회원대화명 (신청자)
     *     image_profile - 회원사진(신청자)
     *     mem_level - 회원레벨 (신청자)
     *     mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *     win_slct - 승패여부[w:승, l:패]
     *     ins_date - 등록일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuBettingLogListVo"})
    @Select("CALL rd_data.p_evt_gganbu_betting_log_list(#{gganbuNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuBettingLogList(GganbuBettingLogListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 나의 배팅 내역
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   pageNo
     *   pagePerCnt
     * @return
     *   #1
     *     cnt
     *   #2
     *     gganbu_no - 회차번호
     *     mem_no - 회원번호 (신청자)
     *     mem_id - 회원아이디 (신청자)
     *     mem_nick - 회원대화명 (신청자)
     *     image_profile - 회원사진(신청자)
     *     mem_level - 회원레벨 (신청자)
     *     mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *     win_slct - 승패여부[w:승, l:패]
     *     red_marble - 배팅 빨간구슬
     *     yellow_marble - 배팅 노란구슬
     *     blue_marble - 배팅 파란구슬
     *     violet_marble - 배팅 보라구슬
     *     ins_date - 등록일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMyBettingLogSelVo"})
    @Select("CALL rd_data.p_evt_gganbu_betting_log_sel(#{gganbuNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuMyBettingLogSel(GganbuBettingLogListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 투표자 집계
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     * @return
     *  gganbu_no - 회차번호
     *  betting_slct - 배팅구분 [a:홀, b:짝]
     *  betting_cnt - 배팅수
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_betting_stat_sel(#{gganbuNo})")
    List<GganbuBettingStatSelVo> gganbuBettingStatSel(String gganbuNo);

    /**********************************************************************************************
     * @Method 설명 : 깐부 뱃지 초기화
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원버호(신청자)
     * @return
     *  s_return: -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_badge_upd(#{gganbuNo}, #{memNo})")
    int gganbuMemBadgeUpd(GganbuMemBadgeUpdVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 나의 뱃지 내역
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원버호(신청자)
     * @return
     *  pocket_cnt - 뱃지수
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_badge_sel(#{gganbuNo}, #{memNo})")
    GganbuMemBadgeSelVo gganbuMemBadgeSel(GganbuMemBadgeUpdVo param);


    /**********************************************************************************************
     * @Method 설명 : 깐부 찾기
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     *   searchText - 검색값
     *   pageNo - 페이지 번호
     *   pagePerCnt - 페이지 당 노출 건수 (Limit)
     * @return
     * row #1
     *  cnt - 총건수
     * row #2
     *  mem_no - 회원번호
     *  mem_id - 회원아이디
     *  mem_nick - 회원대화명
     *  image_profile - 회원사진
     *  rcvYn - 내가 신청여부
     *  sendYn - 검색된 회원이 신청여부
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMemberSearchVo"})
    @Select("CALL rd_data.p_evt_gganbu_member_search(#{memNo}, #{searchText}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuMemberSearch(GganbuMemberSearchInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 배팅수 체크
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원버호(신청자)
     * @return
     *  s_return: -1: 배팅2회완료, 0:에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_betting_chk(#{gganbuNo}, #{memNo})")
    int gganbuMemBettingChk(GganbuMemBettingChkVo param);

    /* 깐부 이벤트 끝~~~ */
}
