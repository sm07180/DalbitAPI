package com.dalbit.event.proc;

import com.dalbit.event.vo.ItemInsVo;
import com.dalbit.event.vo.inputVo.NovemberFanCouponInsInputVo;
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
}
