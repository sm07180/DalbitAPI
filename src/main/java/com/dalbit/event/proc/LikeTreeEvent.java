package com.dalbit.event.proc;

import com.dalbit.event.vo.LikeTreeRankingVO;
import com.dalbit.event.vo.LikeTreeRewardInsVo;
import com.dalbit.event.vo.LikeTreeStoryVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface LikeTreeEvent {
    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 장식리스트
     * @작성일 : 2021-12-16
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.StoryListVo"})
    @Select("CALL rd_data.p_evt_like_tree_decoration_list(#{seqNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getLikeTreeDecoList(@Param(value = "seqNo") Integer seqNo, @Param(value = "pageNo") Integer pageNo, @Param(value = "pagePerCnt") Integer pagePerCnt);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 메인 리스트
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    @ResultMap({"ResultMap.LikeGiftVo", "ResultMap.integer", "ResultMap.StoryVo1", "ResultMap.StoryVo2", "ResultMap.StoryVo3"})
    @Select("CALL rd_data.p_evt_like_tree_list(#{memNo})")
    List<Object> getLikeTreeMainList(@Param(value = "memNo") String memNo);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 리스트
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    @ResultMap({"ResultMap.integer", "ResultMap.StoryListVo"})
    @Select("CALL rd_data.p_evt_like_tree_mem_story_list(#{pageNo}, #{pagePerCnt})")
    List<Object> getLikeTreeStoryList(@Param(value = "pageNo") Integer pageNo, @Param(value = "pagePerCnt") Integer pagePerCnt);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 등록
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_evt_like_tree_mem_story_ins(#{memNo}, #{storyConts})")
    Integer likeTreeStoryIns(Map<String, Object> param);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 수정
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_evt_like_tree_mem_story_upd(#{storyNo}, #{storyConts})")
    Integer likeTreeStoryUpd(Map<String, Object> param);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 삭제
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : storyNo BIGINT-- 사연등록번호, delChrgrName VARCHAR(40)	-- 삭제 관리자명, delSlct BIGINT-- 탈퇴사유[1:본인탈퇴 2:관리자 삭제]
     * @Return :
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_evt_like_tree_mem_story_del(#{storyNo}, #{delChrgrNo}, #{delSlct})")
    Integer likeTreeStoryDel(Map<String, Object> param);
    
    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 신고 등록
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_like_tree_mem_story_rpt_ins(#{memNo}, #{storyNo})")
    Integer likeTreeStoryRptIns(Map<String, Object> param);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 회원 리스트(랭킹)
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.RankListVo"})
    @Select("CALL rd_data.p_evt_like_tree_seq_mem_rank_list(#{seqNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getLikeTreeRankList(@Param(value = "seqNo") Integer seqNo, @Param(value = "pageNo") Integer pageNo, @Param(value = "pagePerCnt") Integer pagePerCnt);

    @Select("CALL rd_data.p_evt_like_tree_seq_mem_rank_sel(#{seqNo}, #{memNo})")
    LikeTreeRankingVO getLikeTreeRankUserInfo(@Param(value = "seqNo") Integer seqNo, @Param(value = "memNo") String memNo);

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 이벤트 보상 지급
     * @작성일 : 2021-12-17
     * @작성자 : 박성민
     * @변경이력 :
     * @Parameter : memNo, memPhone
     * @Return : -3:100점 미만 , -2:본인 미인증, -1:이미 인증받은 번호로 달 받음 , 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL p_evt_like_tree_reward_ins(#{memNo}, #{memPhone})")
    LikeTreeRewardInsVo getLikeTreeRewardIns(Map<String, Object> param);
}
