package com.dalbit.main.proc;

import com.dalbit.main.vo.MyRankVO;
import com.dalbit.main.vo.RankingApplyVO;
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

    /**********************************************************************************************
     * @프로시저 설명   	: 랭킹 참여 여부(DJ/FAN/GOOD)
     * @Date   		        : 2022-04-13
     * @Author   		    : 강알찬
     * @param	 	        : memNo BIGINT			-- 회원번호
     * @return              :
    mem_no			BIGINT		-- 회원번호
    apply_ranking		TINYINT		-- 랭킹데이터 적용 (0:미적용, 1:적용)
    last_upd_date		DATETIME	-- 마지막 수정일자
     **********************************************************************************************
     */
    @Select("CALL rd_data.p_member_apply_ranking_sel(#{memNo})")
    RankingApplyVO getRankingApply(String memNo);
}
