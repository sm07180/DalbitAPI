package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_RoomStoryListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface ContentDao {
        @Transactional(readOnly = true)
        ProcedureVo callBroadCastRoomNoticeSelect(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeEdit(ProcedureVo procedureVo);
        ProcedureVo callBroadCastRoomNoticeDelete(ProcedureVo procedureVo);
        ProcedureVo callInsertStory(ProcedureVo procedureVo);
        //@Transactional(readOnly = true)
        List<P_RoomStoryListVo> callGetStory(ProcedureVo procedureVo);
        ProcedureVo callDeletetStory(ProcedureVo procedureVo);

        /**
         * 방송방공지 등록
         *
         * @param
         * memNo            BIGINT  -- 회원 번호
         * ,roomNo          BIGINT  -- 방 번호[0: 기본, 방 있을시에만]
         * ,roomNoticeConts VARCHAR -- 방송공지 등록글 내용
         *
         * @Return
         * s_return         INT     -- #-1: 회원번호 없음 또는 공지글 등록있음, 0: 에러, 1: 정상
         */
        @Select("CALL rd_data.p_broadcast_room_notice_ins(#{memNo}, #{roomNo}, #{notice})")
        Integer pMemberBroadcastNoticeIns(Map<String, Object> param);

        /**
         * 방송방공지 수정
         *
         * @param
         * roomNoticeNo     BIGINT  -- 방송공지 키값
         * ,memNo           BIGINT  -- 회원번호
         * ,roomNo          BIGINT  -- 방번호[0: 기본, 방 있을시에만]
         * ,roomNoticeConts VARCHAR -- 방송공지 등록글 내용
         *
         * @Return
         * s_return         INT     -- #-1: 공지글 등록 없음, 0: 에러, 1: 정상
         */
        @Select("CALL rd_data.p_broadcast_room_notice_upd(#{roomNoticeNo}, #{memNo}, #{roomNo}, #{notice})")
        Integer pMemberBroadcastNoticeUpd(Map<String, Object> param);
}
