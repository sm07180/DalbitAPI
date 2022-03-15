package com.dalbit.main.proc;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface MainPage {
    /**********************************************************************************************
     * @프로시저 설명   	: 메인 마이스타 리스트
     * @Date   		        : 2022-03-14
     * @Author   		    : 강알찬
     * @param	 	        : memNo   BIGINT 			-- 회차 번호
     * ,pageNo INT UNSIGNED		-- 페이지 번호
     * ,pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @return              :
    #1
    cnt		BIGINT		-- 전체 수


    #2

    mem_no		BIGINT		-- 회원 번호
    mem_nick	VARCHAR	-- 회원 닉네임
    mem_sex		CHAR		-- 회원성별
    image_profile	VARCHAR	-- 프로필
    room_no		BIGINT		-- 방송방 번호
    listen_room_no	BIGINT		-- 청취방방 번호
    last_login_date	DATETIME	-- 접속일자
     **********************************************************************************************
     */
    @ResultMap({"ResultMap.integer", "ResultMap.P_MainStarVo"})
    @Select("CALL rd_data.p_main_my_stat_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getMyStar(Map map);
}
