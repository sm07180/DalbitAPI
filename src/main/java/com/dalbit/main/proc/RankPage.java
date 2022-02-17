package com.dalbit.main.proc;

import com.dalbit.main.vo.MyRankVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface RankPage {
    /**********************************************************************************************
     * @프로시저 설명   	: myRank(DJ/FAN/GOOD)
     * @Date   		        : 2022-02-14
     * @Author   		    : 강알찬
     * @param	 	        : memNo BIGINT			-- 회원번호
     * @return              :
        s_rankSlct VARCHAR(5)	-- 랭킹종류
        s_rank INT		-- 랭킹
        s_memNo	BIGINT		-- 회원번호
     **********************************************************************************************
     */
    @Select("CALL rd_data.p_ranking_mem_sel(#{memNo})")
    List<MyRankVO> getMyRank(String memNo);
}
