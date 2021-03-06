package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.VoteResultVo;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteDao {
    /**
     * ##### 투표 정보 등록
     * <p>
     * CALL p_room_vote_ins(
     * memNo BIGINT			-- 회원번호
     * ,roomNo BIGINT			-- 방번호
     * ,voteTitle VARCHAR(30)		-- 투표제목
     * ,voteAnonyYn CHAR(1)		-- 익명투표 여부 [y:익명, n:일반]
     * ,voteDupliYn CHAR(1)		-- 중복투표 여부 [y:익명, n:일반]
     * ,voteItemCnt SMALLINT		-- 투표항목수
     * ,endTime INT			-- 마감설정시간 (초)
     * )
     * <p>
     * # RETURN
     * <p>
     * s_return		INT		--  -1:투표개설수 초과 , 0:에러, 0이상: 개설된 투표번호
     */
    @Select("CALL p_room_vote_ins(#{memNo},#{roomNo},#{voteTitle},#{voteAnonyYn},#{voteDupliYn},#{voteItemCnt},#{endTime})")
    Integer pRoomVoteIns(VoteRequestVo voteRequestVo);

    /**
     * ##### 투표 아이템 등록
     * <p>
     * CALL p_room_vote_item_ins(
     * voteNo BIGINT			-- 투표번호
     * memNo BIGINT			-- 회원번호
     * ,roomNo BIGINT			-- 방번호
     * ,voteItemName VARCHAR(30)		-- 투표 아이템 제목
     * )
     * <p>
     * # RETURN
     * <p>
     * s_return		INT		--  -2: 투표항목수 초과, -1: 개설된 투표없음, 0: 에러, 1:정상
     */
    @Select("CALL p_room_vote_item_ins(#{param1},#{param2},#{param3},#{param4})")
    Integer pRoomVoteItemIns(Integer voteNo, String MemNo, String roomNo, String voteItemName);

    /**
     * ##### 투표하기
     * <p>
     * CALL p_room_vote_mem_ins(
     * memNo BIGINT			-- 회원번호 (투표개설자)
     * ,pmemNo BIGINT			-- 회원번호 (투표자)
     * ,roomNo BIGINT			-- 방번호
     * ,voteNo BIGINT			-- 투표번호
     * ,itemNo BIGINT			-- 투표항목번호
     * ,voteItemName VARCHAR(30)	-- 항목이름
     * )
     * <p>
     * # RETURN
     * <p>
     * s_return	INT		-- -1: 투표없음, 0: 에러, 1:정상
     */
    @Select("CALL p_room_vote_mem_ins(#{memNo},#{pmemNo},#{roomNo},#{voteNo},#{itemNo},#{voteItemName})")
    Integer pRoomVoteMemIns(VoteRequestVo voteRequestVo);

    /**
     * ##### 방송방 투표 삭제
     * <p>
     * CALL p_room_vote_del(
     * memNo BIGINT			-- 회원번호
     * ,roomNo BIGINT			-- 방번호
     * ,voteNo BIGINT			-- 투표번호
     * )
     * <p>
     * # RETURN
     * <p>
     * s_return		INT		--  -1: 삭제할투표 없음, 0: 에러, 1:정상
     */
    @Select("CALL p_room_vote_del(#{memNo},#{roomNo},#{voteNo})")
    Integer pRoomVoteDel(VoteRequestVo voteRequestVo);

    /**
     * ##### 방송방 투표 마감
     * <p>
     * CALL p_room_vote_end(
     * memNo BIGINT			-- 회원번호
     * ,roomNo BIGINT		-- 방번호
     * ,voteNo BIGINT		-- 투표번호
     * ,endSlct CHAR(1)		-- 마감구분[a:전체마감, o:단일마감]
     * )
     * <p>
     * # RETURN
     * <p>
     * s_return		INT		--  0:에러, 1:정상
     */
    @Select("CALL p_room_vote_end(#{memNo},#{roomNo},#{voteNo},#{endSlct})")
    Integer pRoomVoteEnd(VoteRequestVo voteRequestVo);

    /**
     * ##### 방송방 투표 리스트
     * <p>
     * CALL p_room_vote_list(
     * memNo BIGINT			-- 회원번호
     * ,roomNo BIGINT			-- 방번호
     * ,voteSlct CHAR(1)		-- 투표리스트 구분 [s:투표중, e:마감]
     * )
     * <p>
     * # RETURN
     * <p>
     * vote_no		bigint(20)	-- 투표번호
     * mem_no		bigint(20)	-- 투표개설자 회원번호
     * room_no		bigint(20)	-- 투표개설 방번호
     * vote_title	varchar(30)	-- 투표제목
     * vote_end_slct	char(1)		-- 투표종료구분[s:투표중, e:마감, d:투표삭제]
     * vote_anony_yn	char(1)		-- 익명투표 여부
     * vote_dupli_yn	char(1)		-- 중복투표 여부
     * vote_mem_cnt	smallint(6)	-- 투표참여회원수
     * vote_item_cnt	smallint(6)	-- 투표항목수
     * end_time	int(11)		-- 마감설정시간(초)
     * start_date	datetime(6)	-- 투표개설 일자
     * end_date	datetime(6)	-- 투표종료 일자
     * ins_date	datetime(6)	-- 등록일자
     * upd_date	datetime(6)	-- 변경일자
     */
    // @Transactional(readOnly = true)
    List<Object> pRoomVoteList(VoteRequestVo voteRequestVo);

    /**
     * ##### 투표 정보 조회
     * <p>
     * CALL p_room_vote_sel(
     * memNo BIGINT			-- 회원번호
     * ,roomNo BIGINT			-- 방번호
     * ,voteNo BIGINT			-- 투표번호
     * )
     * <p>
     * # RETURN
     * vote_no		bigint(20)	-- 투표번호
     * mem_no		bigint(20)	-- 투표개설자 회원번호
     * room_no		bigint(20)	-- 투표개설 방번호
     * vote_title	varchar(30)	-- 투표제목
     * vote_end_slct	char(1)		-- 투표종료구분[s:투표중, e:마감, d:투표삭제]
     * vote_anony_yn	char(1)		-- 익명투표 여부
     * vote_dupli_yn	char(1)		-- 중복투표 여부
     * vote_mem_cnt	smallint(6)	-- 투표참여회원수
     * vote_item_cnt	smallint(6)	-- 투표항목수
     * end_time	int(11)		-- 마감설정시간(초)
     * start_date	datetime(6)	-- 투표개설 일자
     * end_date	datetime(6)	-- 투표종료 일자
     * ins_date	datetime(6)	-- 등록일자
     * upd_date	datetime(6)	-- 변경일자
     */
    // insert 후 바로 조회 => Master
    // @Transactional(readOnly = true)
    VoteResultVo pRoomVoteSel(VoteRequestVo voteRequestVo);

    /**
     * ##### 투표 항목 리스트
     * <p>
     * CALL p_room_vote_detail_list(
     * memNo BIGINT			-- 회원번호
     * ,pmemNo BIGINT			-- 회원번호(투표자)
     * ,roomNo BIGINT			-- 방번호
     * ,voteNo BIGINT			-- 투표번호
     * )
     * <p>
     * #RETURN
     * item_no		bigint(20)	-- 투표항목번호
     * vote_no		bigint(20)	-- 투표번호
     * mem_no		bigint(20)	-- 투표개설자 회원번호
     * room_no		bigint(20)	-- 투표개설 방번호
     * vote_item_name	varchar(30)	-- 투표 항목 이름
     * vote_mem_cnt	int(11)		-- 투표인원수
     * ins_date	datetime(6)	-- 등록일자
     * upd_date	datetime(6)	-- 변경일자
     */
    // @Transactional(readOnly = true)
    List<VoteResultVo> pRoomVoteDetailList(VoteRequestVo voteRequestVo);
}
