package com.dalbit.rank.proc;

import com.dalbit.rank.vo.MyRankVO;
import com.dalbit.rank.vo.RankApplyVO;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface Rank {

    @Select("CALL rd_data.p_ranking_mem_sel(#{memNo})")
    List<MyRankVO> getMyRank(String memNo);

    @Select("CALL rd_data.p_member_apply_ranking_sel(#{memNo})")
    RankApplyVO rankApplyProc(String memNo);

    /**********************************************************************************************
     * @Method    : 팀 랭킹 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT-- 팀번호
     * @return    :
     *# RETURN [Multi Rows]
     * #1
     * cntINT-- 총건수
     *
     * #2
     * the_week_dateDATE-- 집계일자
     * team_noBIGINT-- 팀번호
     * team_nameVARCHAR(15)-- 팀이름
     * team_contsVARCHAR(200)-- 팀소개
     * team_medal_codeCHAR(4)-- 팀메달코드
     * team_edge_codeCHAR(4)-- 팀 테두리코드
     * team_bg_codeCHAR(4)-- 팀 배경코드
     * rank_ptINT-- 랭킹점수
     * send_dal_cntBIGINT-- 보낸달수
     * rcv_byeol_cntBIGINT-- 받은별수
     * new_fan_cntBIGINT-- 신규팬수
     * play_timeBIGINT-- 총방송시간
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.TeamRankVo"})
    @Select("CALL rd_data.p_dalla_team_rank_week_list(#{tDate}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getTeamRankProc(String tDate, String memNo, Integer pageNo, Integer pagePerCnt);


    /**********************************************************************************************
     * @프로시저 설명    : 파트너 dj 리스트
     * @Date                : 2022-03-24
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
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
