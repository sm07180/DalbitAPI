package com.dalbit.event.dao;

import com.dalbit.event.vo.ElectricSignDJListOutVo;
import com.dalbit.event.vo.ElectricSignFanListOutVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ElectricSignDao {
    /**
     * 전광판 이벤트 DJ 리스트
     *
     * @param param
     * pageNo           INT         --페이지 번호
     * pagePerCnt       INT         --페이지 당 노출 건수(Limit)
     *
     * @return
     *
     * #1
     * cnt              BIGINT      --전체 수
     *
     * #2
     * mem_no           BIGINT      --회원 번호
     * tot_score_cnt    BIGINT      --총점수
     * mem_id           VARCHAR     --회원 아이디
     * mem_nick         VARCHAR     --회원 닉네임
     * mem_sex          CHAR        --회원성별
     * image_profile    VARCHAR     --프로필
     * mem_level        BIGINT      --레벨
     * mem_state        BIGINT      --회원상태(1:정상, 3:블럭, 4:탈퇴, 5:영구정지)
     * ins_date         DATETIME    --등록일자
     * upd_date         DATETIME    --수정일자
     */
    @ResultMap({"ResultMap.integer", "ResultMap.ElectricSignDJListOutVo"})
    @Select("CALL p_evt_sign_board_dj_rank_list(#{pageNo}, #{pagePerCnt})")
    List<Object> pElectricSignDjList(Map<Object, String> param);

    /**
     * 전광판 이벤트 DJ 회원 정보
     *
     * @param param
     * memNo            BIGINT      --회원 번호
     *
     * @return
     *
     * #1
     * rankNo           BIGINT      --순위
     * tot_score_cnt    BIGINT      --총점수
     * mem_no           BIGINT      --회원 번호
     * mem_id           VARCHAR     --회원 아이디
     * mem_nick         VARCHAR     --회원 닉네임
     * mem_sex          CHAR        --회원성별
     * image_profile    VARCHAR     --프로필
     * mem_level        BIGINT      --레벨
     * mem_state        BIGINT      --회원상태(1:정상, 3:블럭, 4:탈퇴, 5:영구정지)
     * ins_date         DATETIME    --등록일자
     * upd_date         DATETIME    --수정일자
     */
    @Select("CALL p_evt_sign_board_dj_rank_sel(#{memNo})")
    ElectricSignDJListOutVo pElectricSignDjSel(Map<Object, String> param);

    /**
     * 전광판 이벤트 시청자 리스트
     *
     * @param param
     * pageNo           INT         --페이지 번호
     * pagePerCnt       INT         --페이지 당 노출 건수(Limit)
     *
     * @return
     *
     * #1
     * cnt              BIGINT      --전체 수
     *
     * #2
     * mem_no           BIGINT      --회원 번호
     * tot_score_cnt    BIGINT      --총점수
     * mem_id           VARCHAR     --회원 아이디
     * mem_nick         VARCHAR     --회원 닉네임
     * mem_sex          CHAR        --회원성별
     * image_profile    VARCHAR     --프로필
     * mem_level        BIGINT      --레벨
     * mem_state        BIGINT      --회원상태(1:정상, 3:블럭, 4:탈퇴, 5:영구정지)
     * ins_date         DATETIME    --등록일자
     * upd_date         DATETIME    --수정일자
     */
    @ResultMap({"ResultMap.integer", "ResultMap.ElectricSignFanListOutVo"})
    @Select("CALL p_evt_sign_board_mem_rank_list(#{pageNo}, #{pagePerCnt})")
    List<Object> pElectricSignFanList(Map<Object, String> param);

    /**
     * 전광판 이벤트 시청자 회원 정보
     *
     * @param param
     * memNo            BIGINT      --회원 번호
     *
     * @return
     *
     * #1
     * rankNo           BIGINT      --순위
     * tot_score_cnt    BIGINT      --총수
     * mem_no           BIGINT      --회원 번호
     * mem_id           VARCHAR     --회원 아이디
     * mem_nick         VARCHAR     --회원 닉네임
     * mem_sex          CHAR        --회원성별
     * image_profile    VARCHAR     --프로필
     * mem_level        BIGINT      --레벨
     * mem_state        BIGINT      --회원상태(1:정상, 3:블럭, 4:탈퇴, 5:영구정지)
     * ins_date         DATETIME    --등록일자
     * upd_date         DATETIME    --수정일자
     */
    @Select("CALL p_evt_sign_board_mem_rank_sel(#{memNo})")
    ElectricSignFanListOutVo pElectricSignFanSel(Map<Object, String> param);

}
