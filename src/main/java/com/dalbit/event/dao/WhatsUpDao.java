package com.dalbit.event.dao;

import com.dalbit.event.vo.WhatsUpResultVo;
import com.dalbit.event.vo.request.WhatsUpRequestVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WhatsUpDao {

    /**
     * ##### 와썹맨 dj 리스트
     * CALL p_evt_wassup_man_dj_rank_list(
     * seqNo       INT     -- 회차번호[1,2]
     * ,pageNo         INT UNSIGNED    -- 페이지 번호
     * ,pagePerCnt     INT UNSIGNED    -- 페이지 당 노출 건수 (Limit)
     * )
     * Multi Rows
     * #1
     * cnt     BIGINT      -- 전체 수
     * #2
     * seq_no      BIGINT      -- 회차 번호
     * new_fan_cnt BIGINT      -- 신입팬 수
     * mem_no      BIGINT      -- 회원 번호
     * mem_id      VARCHAR -- 회원 아이디
     * mem_nick    VARCHAR -- 회원 닉네임
     * mem_sex     CHAR        -- 회원성별
     * image_profile   VARCHAR -- 프로필
     * mem_level   BIGINT      -- 레벨
     * mem_state   BIGINT      -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * ins_date        DATETIME    -- 등록일자
     * upd_date        DATETIME    -- 수정일자
     */
    @Transactional(readOnly = true)
    List<Object> pEvtWassupManDjRankList(WhatsUpRequestVo whatsUpRequestVo);

    /**
     * ##### 와썹맨 dj 회원정보
     * CALL p_evt_wassup_man_dj_rank_sel(
     * seqNo       INT     -- 회차번호[1,2]
     * ,memNo      BIGINT      -- 회원 번호
     * )
     * Multi Rows
     * #1
     * rankNo      BIGINT      -- 순위
     * seq_no      BIGINT      -- 회차 번호
     * new_fan_cnt BIGINT      -- 신입팬 수
     * mem_no      BIGINT      -- 회원 번호
     * mem_id      VARCHAR -- 회원 아이디
     * mem_nick    VARCHAR -- 회원 닉네임
     * mem_sex     CHAR        -- 회원성별
     * image_profile   VARCHAR -- 프로필
     * mem_level   BIGINT      -- 레벨
     * mem_state   BIGINT      -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * ins_date        DATETIME    -- 등록일자
     * upd_date    DATETIME    -- 수정일자
     */
    @Transactional(readOnly = true)
    WhatsUpResultVo pEvtWassupManDjRankSel(WhatsUpRequestVo whatsUpRequestVo);

    /**
     * ##### 와썹맨 신입회원 리스트
     * CALL p_evt_wassup_man_new_mem_rank_list(
     * seqNo       INT     -- 회차번호[1,2]
     * ,pageNo         INT UNSIGNED    -- 페이지 번호
     * ,pagePerCnt     INT UNSIGNED    -- 페이지 당 노출 건수 (Limit)
     * )
     * Multi Rows
     * #1
     * cnt     BIGINT      -- 전체 수
     * #2
     * seq_no      BIGINT      -- 회차 번호
     * fan_cnt     BIGINT      -- 팬 수
     * view_score_cnt  BIGINT      -- 방송점수
     * tot_score_cnt   BIGINT      -- 총수
     * mem_no      BIGINT      -- 회원 번호
     * mem_id      VARCHAR -- 회원 아이디
     * mem_nick    VARCHAR -- 회원 닉네임
     * mem_sex     CHAR        -- 회원성별
     * image_profile   VARCHAR -- 프로필
     * mem_level   BIGINT      -- 레벨
     * mem_state   BIGINT      -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * ins_date        DATETIME    -- 등록일자
     * upd_date        DATETIME    -- 수정일자
     */
    @Transactional(readOnly = true)
    List<Object> pEvtWassupManNewMemRankList(WhatsUpRequestVo whatsUpRequestVo);

    /**
     * ##### 와썹맨 신입회원  회원정보
     * CALL p_evt_wassup_man_new_mem_rank_sel(
     * seqNo       INT     -- 회차번호[1,2]
     * ,memNo      BIGINT      -- 회원 번호
     * )
     * Multi Rows
     * #1
     * rankNo      BIGINT      -- 순위
     * seq_no      BIGINT      -- 회차 번호
     * fan_cnt     BIGINT      -- 팬 수
     * view_score_cnt  BIGINT      -- 방송점수
     * tot_score_cnt   BIGINT      -- 총수
     * mem_no      BIGINT      -- 회원 번호
     * mem_id      VARCHAR -- 회원 아이디
     * mem_nick    VARCHAR -- 회원 닉네임
     * mem_sex     CHAR        -- 회원성별
     * image_profile   VARCHAR -- 프로필
     * mem_level   BIGINT      -- 레벨
     * mem_state   BIGINT      -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * ins_date        DATETIME    -- 등록일자
     * upd_date    DATETIME    -- 수정일자
     */
    @Transactional(readOnly = true)
    WhatsUpResultVo pEvtWassupManNewMemRankSel(WhatsUpRequestVo whatsUpRequestVo);

}
