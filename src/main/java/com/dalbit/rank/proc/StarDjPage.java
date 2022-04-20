package com.dalbit.rank.proc;

import com.dalbit.rank.vo.StarDjPointVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Component
@Repository
public interface StarDjPage {
    /**********************************************************************************************
     * @프로시저 설명   	: 스타 dj 신청 조건 점수 리스트
     * @Date   		        : 2022-04-18
     * @Author   		    : 강알찬
     * @param	 	        : memNo 		BIGINT		-- 회원번호
     * ,tYear 		CHAR(4)		-- 신청/수집기간 년 0000
     * ,tMonth 		CHAR(2)		-- 신청/수집기간 월 00
     * @return              :
    reg_no		BIGINT		-- 접수기간 번호
    mem_no		BIGINT		-- 회원 번호
    select_year	CHAR		-- 스페셜DJ 선정년도
    select_month	CHAR		-- 스페셜DJ 선정월
    play_cnt		BIGINT		-- 방송시간
    like_score_cnt	BIGINT		-- 받은 좋아요수
    byeol_cnt		BIGINT		-- 선물받은별수
    view_cnt		BIGINT		-- 시청자수
     **********************************************************************************************
     */
    @Select("CALL rd_data.p_period_star_dj_score_list(#{memNo}, #{tYear}, #{tMonth})")
    StarDjPointVO getStarDjScore(Map map);

    /**********************************************************************************************
     * @프로시저 설명   	: 스타 dj 신청
     * @Date   		        : 2022-04-18
     * @Author   		    : 강알찬
     * @param	 	        : memNo 		BIGINT		-- 회원번호
     * ,tYear 		CHAR(4)		-- 신청/수집기간 년 0000
     * ,tMonth 		CHAR(2)		-- 신청/수집기간 월 00
     * @return              :
    reg_no		BIGINT		-- 접수기간 번호
    mem_no		BIGINT		-- 회원 번호
    select_year	CHAR		-- 스페셜DJ 선정년도
    select_month	CHAR		-- 스페셜DJ 선정월
    play_cnt		BIGINT		-- 방송시간
    like_score_cnt	BIGINT		-- 받은 좋아요수
    byeol_cnt		BIGINT		-- 선물받은별수
    view_cnt		BIGINT		-- 시청자수
     **********************************************************************************************
     */
    @Select("CALL rd_data.sp_member_special_dj_apply(#{memNo}, '', '', '', '', '', '')")
    void starDjIns(Map map);

}
