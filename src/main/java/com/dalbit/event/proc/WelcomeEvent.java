package com.dalbit.event.proc;

import com.dalbit.event.vo.WelcomItemListVO;
import com.dalbit.event.vo.WelcomListVO;
import com.dalbit.event.vo.WelcomeCondVO;
import com.dalbit.event.vo.WelcomeUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface WelcomeEvent {
    /**********************************************************************************************
     * @Method 설명 : 웰컴 회원 정보
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호(신청자)
     * @Return : step_no		INT		-- 단계 번호
     *           mem_no		INT		-- 회원 번호
     *           play_time		INT		-- 방송시간(초)
     *           rcv_like_cnt	INT		-- 받은좋아요
     *           rcv_dal_cnt	INT		-- 선물받은달
     *           dj_gift_req_yn	CHAR		-- 경품신청 완료
     *           dj_gift_rcv_yn	CHAR		-- 경품받기 완료
     *           dj_gift_the_month	DATE		-- 경품 일자(월)
     *           dj_gift_no	INT		-- 경품 번호
     *           dj_gift_name	VARCHAR	-- 경품 이름
     *           ins_date		DATETIME	-- 등록일자
     *           upd_date		DATETIME	-- 수정일자
     **********************************************************************************************/
    @Select("CALL rd_data.p_welcome_mem_info_list(#{memNo}, #{memSlct})")
    List<WelcomListVO> getUserListInfo(@Param(value = "memNo") String memNo, @Param(value = "memSlct") String memSlct);

    /**********************************************************************************************
     * @Method 설명 : 웰컴 아이템 리스트
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : giftSlct 		INT		-- [1:dj, 2:청취자]
     * @Return :
     **********************************************************************************************/
    @Select("CALL rd_data.p_welcome_gift_list(#{giftSlct})")
    List<WelcomItemListVO> getItemListInfo(@Param(value = "giftSlct") String giftSlct);

    /**********************************************************************************************
     * @Method 설명 : 웰컴 아이템 리스트(단계별)
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : giftSlct 		INT		-- [1:dj, 2:청취자]
     *              stepNo 		BIGINT			-- 단계 번호
     * @Return :
     **********************************************************************************************/
    @Select("CALL rd_data.p_welcome_step_gift_list(#{giftSlct}, #{stepNo})")
    List<WelcomItemListVO> getStepItemListInfo(@Param(value = "giftSlct") String giftSlct, @Param(value = "stepNo") String stepNo);

    /**********************************************************************************************
     * @Method 설명 : 웹컴 조건 인증체크 프로시저
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호
     *             memSlct 	BIGINT		-- 회원[1:dj, 2:청취자]
     * @Return :
     **********************************************************************************************/
    @Select("CALL rd_data.p_welcome_mem_chk(#{memNo}, #{memSlct})")
    Integer getWelcomeEventQualityInfo(@Param(value = "memNo") String memNo, @Param(value = "memSlct") String memSlct);

    /**********************************************************************************************
     * @Method 설명 : 웹컴 아이템 인증체크 프로시저
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		 -- 회원번호
     *              memPhone	VARCHAR(20)	 -- 회원폰번호
     *              memSlct 	BIGINT		-- 회원[1:dj, 2:청취자]\
     *              giftStep    BIGINT      -- 단계
     * @Return :
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_welcome_gift_auth_chk(#{memNo}, #{memPhone}, #{giftSlct}, #{giftStepNo})")
    Integer checkWelcomeAuth(@Param(value = "memNo") String memNo, @Param(value = "memPhone") String memPhone, @Param(value = "giftSlct") String giftSlct, @Param(value = "giftStepNo") String giftStepNo);

    /**********************************************************************************************
     * @Method 설명 : 웰컴 경품 선물 받기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :  memNo 		BIGINT		-- 회원번호(신청자)
     *              giftSlct 		INT		-- 1:DJ, 2: 청취자
     *              giftCode 	VARCHAR(12)	-- 경품코드
     *              giftName 	VARCHAR(20)	-- 경품이름
     *              giftCont 		VARCHAR(20)	-- 경품설명
     *              giftDalCnt 	INT 		-- 달수 (달일경우)
     *              giftStepNo 	INT		-- 경품단계
     *              giftTheMonth 	DATE		-- 경품월[0000-00-01]
     * @Return :  s_return		INT		--    0: 에러, 1:정상
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_welcome_gift_ins(#{memNo}, #{giftSlct}, #{giftCode}, #{giftName}, #{giftCont}, #{giftDalCnt}, #{giftStepNo}, #{giftTheMonth}, #{giftOrdNo})")
    Integer insWelcomeItem(Map<String, Object> params);

    /**********************************************************************************************
     * @Method 설명 : 웹컴페이지 접속 체크 값 업데이트 프로시저 (방송방 - 웹, 네이티브)
     * @작성일 : 2022-01-21
     * @작성자 : 박용훈
     * @변경이력 :  memNo 		BIGINT		-- 회원번호
     * @Return :  s_return		INT		--   -1: 이상, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_welcome_mem_day_chk(#{memNo})")
    Integer pWelcomeMemDayChk(Long memNo);

    /**********************************************************************************************
     * @Method 설명 :
     * @작성일 : 2022-02-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : qualifySlct 	INT		-- [1:dj, 2:청취자]
                   theMonth 	DATE		-- 사용월[0000-00-01]
     * @Return :
     **********************************************************************************************/
    @Select("CALL p_welcome_qualify_list(#{memSlct}, #{theMonth})")
    List<WelcomeCondVO> getWelcomeContList(@Param(value = "memSlct") String memSlct, @Param(value = "theMonth") String theMonth);
}
