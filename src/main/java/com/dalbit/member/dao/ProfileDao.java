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

    String callCertificationChkSel(String memNo); // new ????????????

    /**
     * ????????? ?????? ??????
     *
     * @Param fanboardNo    INT		-- ???????????????
     * ,memNo 		BIGINT		-- ????????????
     * ,viewMemNo 	BIGINT		-- ????????????(?????????)
     * @Return board_idx;                  //BIGINT		-- ??????
     * writer_mem_no;              //BIGINT		-- ????????????(?????????)
     * nickName;                 //VARCHAR	--?????????(?????????)
     * userId;                   //VARCHAR	--?????????(?????????)
     * memSex;                   //VARCHAR	-- ??????(?????????)
     * profileImage;             //VARCHAR	-- ?????????(?????????)
     * STATUS;                     //BIGINT		-- ??????
     * viewOn;                     //BIGINT		-- 1:?????? 0:?????????
     * writeDate;                //DATETIME	-- ????????????
     * ins_date;                 //DATETIME	-- ????????????
     * rcv_like_cnt;               //BIGINT		-- ????????????
     * rcv_like_cancel_cnt;        //BIGINT		-- ?????? ????????????
     * like_yn;                  //CHAR		-- ????????? ??????[y,n]
     */
    @Select("CALL p_member_fanboard_sel(#{fanboardNo}, #{memNo}, #{viewMemNo})")
    ProfileBoardDetailOutVo pMemberFanboardSel(Map<String, Object> param);
}
