package com.dalbit.rank.proc;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface Rank {
    /**********************************************************************************************
     * @프로시저 설명   	: 파트너 dj 리스트
     * @Date   		        : 2022-03-24
     * @Author   		    : 강알찬
     * @param	 	        : memNo 		BIGINT		-- 회원번호
     * @return              :
    Multi Rows

    #1
    cnt		BIGINT		-- 총수

    #2
    mem_no		BIGINT		-- 회원 번호
    mem_nick	VARCHAR	-- 회원 닉네임
    mem_sex		CHAR		-- 회원성별
    image_profile	VARCHAR	-- 프로필
    room_no		BIGINT		-- 방송방 번호
     **********************************************************************************************
     */
    @ResultMap({"ResultMap.integer", "ResultMap.PartnerDjVO"})
    @Select("CALL rd_data.p_partner_dj_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getPartnerDjList(Map map);
}
