package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.ProfileBoardDetailOutVo;
import com.dalbit.member.vo.procedure.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProfileDao {
    // @Transactional(readOnly = true)
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);

    ProcedureVo callMemberFanboardAdd(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_FanboardListVo> callMemberFanboardList(ProcedureVo procedureVo);

    ProcedureVo callMemberFanboardDelete(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_FanboardReplyVo> callMemberFanboardReply(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_FanRankingVo> callMemberFanRanking(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callMemberLevelUpCheck(ProcedureVo procedureVo);

    ProcedureVo callMemberFanboardEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_StarRankingVo> selectStarRanking(P_StarRankingVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_FanListVo> callFanList(ProcedureVo procedureVo);

    List<P_FanListNewVo> callFanListNew(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    P_FanMemoVo callFanMemo(ProcedureVo procedureVo);

    ProcedureVo callFanMemoSave(ProcedureVo procedureVo);

    ProcedureVo callFanEdit(ProcedureVo procedureVo);

    List<P_StarListNewVo> callStarListNew(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    P_StarMemoVo callStarMemo(ProcedureVo procedureVo);

    ProcedureVo callStarMemoSave(ProcedureVo procedureVo);

    ProcedureVo callProfileImgAdd(ProcedureVo procedureVo);

    ProcedureVo callProfileImgDelete(ProcedureVo procedureVo);

    ProcedureVo callProfileImgLeader(ProcedureVo procedureVo);

    List<P_ProfileImgListVo> callProfImgList(ProcedureVo procedureVo);

    String callCertificationChkSel(String memNo); // new 프로시져

    /**
     * 팬보드 상세 조회
     *
     * @Param fanboardNo    INT		-- 팬보드번호
     * ,memNo 		BIGINT		-- 회원번호
     * ,viewMemNo 	BIGINT		-- 회원번호(접속자)
     * @Return board_idx;                  //BIGINT		-- 번호
     * writer_mem_no;              //BIGINT		-- 회원번호(작성자)
     * nickName;                 //VARCHAR	--닉네임(작성자)
     * userId;                   //VARCHAR	--아이디(작성자)
     * memSex;                   //VARCHAR	-- 성별(작성자)
     * profileImage;             //VARCHAR	-- 프로필(작성자)
     * STATUS;                     //BIGINT		-- 상태
     * viewOn;                     //BIGINT		-- 1:공개 0:비공개
     * writeDate;                //DATETIME	-- 수정일자
     * ins_date;                 //DATETIME	-- 등록일자
     * rcv_like_cnt;               //BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt;        //BIGINT		-- 취소 좋아요수
     * like_yn;                  //CHAR		-- 좋아요 확인[y,n]
     */
    @Select("CALL p_member_fanboard_sel(#{fanboardNo}, #{memNo}, #{viewMemNo})")
    ProfileBoardDetailOutVo pMemberFanboardSel(Map<String, Object> param);
}
