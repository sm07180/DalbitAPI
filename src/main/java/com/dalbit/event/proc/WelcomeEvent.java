package com.dalbit.event.proc;

import com.dalbit.event.vo.WelcomItemListVO;
import com.dalbit.event.vo.WelcomListVO;
import com.dalbit.event.vo.WelcomeUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface WelcomeEvent {

    /**********************************************************************************************
     * @Method 설명 : 웰컴 DJ 정보
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호(신청자)
     *              giftSlct 		INT		-- 1:DJ, 2: 청취자
     *              theMonth 	DATE		-- 경품월[0000-00-01]
     *              memGiftNo 	BIGINT	 	-- 경품번호
     *              memStepNo 	BIGINT		-- 경품단계번호
     * @Return : s_return		INT		--    0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_welcome_dj_info_list(#{memNo})")
    List<WelcomListVO> getDjListInfo(@Param(value = "memNo") String memNo);
    
    /**********************************************************************************************
     * @Method 설명 : 웰컴 DJ 아이템 리스트
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : tDate 		DATE			-- 검색일자 (0000-00-00)
     * @Return : the_month	DATE		-- 경품 일자(월)
     *           dj_gift_no	INT		-- 경품 번호
     *           dj_step_no	INT		-- 단계 번호
     *           dj_gift_name	VARCHAR	-- 경품 이름
     *           dj_gift_file_name	VARCHAR	-- 경품 파일이름
     *           dj_gift_dal_cnt	INT		-- 달수
     *           dj_gift_ord	INT		-- 정렬 [1,2,3]
     *           tot_ins_cnt	INT		-- 응모수
     *           ins_date		DATETIME	-- 등록일자
     *           upd_date		DATETIME	-- 수정일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.WelcomeItemInfoVo1", "ResultMap.WelcomeItemInfoVo2", "ResultMap.WelcomeItemInfoVo3", "ResultMap.WelcomeItemInfoVo4"})
    @Select("CALL rd_data.p_welcome_dj_item_list(#{tDate})")
    List<Object> getDjItemListInfo(@Param(value = "tDate") String tDate);


    /**********************************************************************************************
     * @Method 설명 : 웰컴 청취자 정보
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
    @Select("CALL rd_data.p_welcome_mem_info_list(#{memNo})")
    List<WelcomeUserVO> getUserListInfo(@Param(value = "memNo") String memNo);

    /**********************************************************************************************
     * @Method 설명 : 웰컴 청취자 아이템 리스트
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : tDate 		DATE			-- 검색일자 (0000-00-00)
     * @Return :
     **********************************************************************************************/
    @ResultMap({"ResultMap.WelcomeUserItemVo1", "ResultMap.WelcomeUserItemVo2", "ResultMap.WelcomeUserItemVo3", "ResultMap.WelcomeUserItemVo4"})
    @Select("CALL rd_data.p_welcome_mem_item_list(#{tDate})")
    List<Object> getUserItemListInfo(@Param(value = "tDate") String tDate);

    /**********************************************************************************************
     * @Method 설명 : 웹컴 아이템 인증체크 프로시저
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		 -- 회원번호
     *              memPhone	VARCHAR(20)	 -- 회원폰번호
     * @Return :
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_welcome_item_auth_chk(#{memNo}, #{memPhone})")
    Integer checkWelcomeAuth(@Param(value = "memNo") String memNo, @Param(value = "memPhone") String memPhone);


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
     * @Method 설명 : 웰컴 경품 선물 받기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호(신청자)
     *             giftSlct 		INT		-- 1:DJ, 2: 청취자
     *             theMonth 	DATE		-- 경품월[0000-00-01]
     *             memGiftNo 	BIGINT	 	-- 경품번호
     *             memStepNo 	BIGINT		-- 경품단계번호
     * @Return :  s_return		INT		--    0: 에러, 1:정상
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_welcome_item_ins(#{memNo}, #{giftSlct}, #{theMonth}, #{memGiftNo}, #{memStepNo})")
    Integer insWelcomeItem(@Param(value = "memNo") String memNo, @Param(value = "giftSlct") String memSlct, @Param(value = "theMonth") String theMonth, @Param(value = "memGiftNo") String memGiftNo, @Param(value = "memStepNo") String memStepNo);

}
