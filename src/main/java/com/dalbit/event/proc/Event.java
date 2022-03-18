package com.dalbit.event.proc;

import com.dalbit.event.vo.*;
import com.dalbit.event.vo.ItemInsVo;
import com.dalbit.event.vo.request.*;
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
     *   marble_pt - 구슬점수
     *   tot_marble_pocket_pt - 합산점수
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
     *   insSlct - 획득구분 [r:방송, v:청취, c:달충전, e:교환, b:베팅]
     *   roomNo - 룸번호[획득구분 r에서 사용]
     *   rMarbleCnt - 빨강구슬 획득수
     *   yMarbleCnt - 노랑구슬 획득수
     *   bMarbleCnt - 파랑구슬 획득수
     *   vMarbleCnt - 보라구슬 획득수
     *   winSlct - 베팅시 승패여부 [w: 승, l: 패]
     *   bettingSlct - 베팅 구분[a:홀, b:짝]
     * @return
     *   s_return: -6: 베팅 개수 초과, -5: 하루 2회, -4: 베팅구슬 부족, -3: 이미 지급됨, -2: 신청자 이미깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
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
     *     ins_slct - 구슬 획득/사용 위치 구분 [r:방송, v:청취, c:달충전, e:교환, b:베팅]
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
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_pocket_stat_ins(#{memNo}, #{ptrMemNo}, #{roomNo}, #{dalCnt})")
    GganbuPocketStatInsVo gganbuMarblePocketStatIns(GganbuMarbleExchangeInputVo param);

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
    int gganbuMarbleExchange(String memNo);

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
    Integer gganbuMarblePocketOpenIns(GganbuMarblePocketOpenInsVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 주머니 리포트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원번호
     *   tabSlct - 탭구분[r: 주머니 받은내역, p: 점수내역]
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
     *     chng_slct - [s: 획득, u: 사용]
     *     ins_slct - 구슬주머니 획득구분[e:구슬교환, u: 1만개 사용누적, s:1만개 선물 누적]
     *     marble_pocket_cnt - 주머니 수
     *     exc_marble_cnt - 교환 구슬 수
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMarblePocketLogListVo"})
    @Select("CALL rd_data.p_evt_gganbu_mem_marble_pocket_log_list(#{gganbuNo}, #{memNo}, #{tabSlct}, #{pageNo}, #{pagePerCnt})")
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
     *     marble_pt - 구슬점수
     *     tot_marble_pocket_pt - 합산점수
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
     *     marble_pt - 구슬점수
     *     tot_marble_pocket_pt - 합산점수
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_rank_my_sel(#{gganbuNo}, #{memNo})")
    GganbuRankListVo gganbuMyRankList(GganbuRankListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 베팅 리스트
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
     * @Method 설명 : 깐부 나의 베팅 내역
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
     *     red_marble - 베팅 빨간구슬
     *     yellow_marble - 베팅 노란구슬
     *     blue_marble - 베팅 파란구슬
     *     violet_marble - 베팅 보라구슬
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
     *  betting_slct - 베팅구분 [a:홀, b:짝]
     *  betting_cnt - 베팅수
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_betting_stat_sel(#{gganbuNo})")
    GganbuBettingStatSelVo gganbuBettingStatSel(String gganbuNo);

    /**********************************************************************************************
     * @Method 설명 : 깐부 뱃지 초기화
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원버호(신청자)
     *   badgeSlct - 뱃지구분 [p:주머니, r: 깐부신청]
     * @return
     *  s_return: -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_badge_upd(#{gganbuNo}, #{memNo}, #{badgeSlct})")
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
     *  req_cnt - 깐부 신청 뱃지
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
     * @Method 설명 : 깐부 베팅수 체크
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @param  :
     *   gganbuNo - 회차번호
     *   memNo - 회원버호(신청자)
     * @return
     *  s_return: -1: 베팅2회완료, 0:에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_betting_chk(#{gganbuNo}, #{memNo})")
    int gganbuMemBettingChk(GganbuMemBettingChkVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 찾기 (나의 팬)
     * @작성일   : 2021-12-08
     * @작성자   : 박성민
     * @param  :
     *   memNo - 회원번호
     *   ptrMemNo - 검색자 회원번호
     *   ordSlct - 정렬순서
     *   pageNo
     *   pagePerCnt
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuFanListVo"})
    @Select("CALL rd_data.p_evt_gganbu_member_fan_list(#{memNo}, #{ptrMemNo}, #{ordSlct}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuFanList(GganbuFanListVo param);

    /* 깐부 이벤트 끝~~~ */

    //------------------------------------ 달나라 이벤트 방송방 ↓ ------------------------------------
    /** 달나라 이벤트 일정 리스트 (회차정보) (방송방, 이벤트페이지 공용)
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
}
