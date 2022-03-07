package com.dalbit.event.proc;

import com.dalbit.event.vo.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface DallagersEvent {

    /**
     * 이니셜 등록
     * @Param
     * memNo               BIGINT          회원번호
     * ,roomNo             BIGINT          방번호
     * ,collectSlct        INT             구분[1:방송청취,2:방송시간,3:보낸달,4: 부스터수,5:받은별]
     * ,dallaGubun         CHAR(1)         구분[d,a,l]
     * ,insDallaCnt        BIGINT          받은 이니셜값
     * ,slctValCnt         BIGINT          구분에서의 값 [방송청취,방송시간,보낸달,부스터수,받은별]
     * ,feverYn            VARCHAR(20)     피버[y,n]
     *
     * @Return
     * s_return		INT		--  -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 회원 데이터 없음 , 0: 에러, 1:정상
     * */
    @Select("Call rd_data.p_evt_dalla_collect_ins(#{memNo}, #{roomNo}, #{collectSlct}, #{dallaGubun}, #{insDallaCnt}, #{slctValCnt}, #{feverYn})")
    Integer pEvtDallaCollectIns(DallagersInitialAddVo param);

    /**
     * 피버타임 등록
     * @Param
     * feverSlct 		BIGINT		-- 피버 종류(1:선물,2:방송시간)
     * ,roomNo 	        BIGINT		-- 방번호
     *
     * @Return
     *
     * s_return		INT		--  -4:방송방시간 피버 2회 초과,  -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 현재 피버타임중 , 0: 에러
     * */
    //@Select("CALL rd_data.p_evt_dalla_collect_room_fever_ins(#{feverSlct}, #{roomNo})")
    //Integer pEvtDallaCollectRoomFeverIns(Map<String, Object> param);

    /**
     * 피버타임 종료
     * roomNo 	        BIGINT		-- 방번호
     *
     * # RETURN
     * s_return		INT		--   -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 현재 피버타임없음 , 0: 에러, 1:정상
     * */
    //@Select("CALL rd_data.p_evt_dalla_collect_room_fever_end(#{roomNo})")
    //Integer pEvtDallaCollectRoomFeverEnd(Long roomNo);

    /**
     * 달라이벤트 방 피버 정보
     * @Param
     * roomNo 	    BIGINT		-- 방번호
     *
     * @Return
     * room_no                 BIGINT   방번호
     * gift_fever_cnt          INT      피버횟수(선물)
     * play_fever_cnt          INT      피버횟수(시간)
     * tot_fever_cnt           INT      총합
     * fever_yn                CHAR     피버확인[y,n]
     * gold		               INT		받은별
     * booster_cnt	           INT		부스터수
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_room_fever_sel(#{roomNo})")
    DallagersRoomFerverSelVo pEvtDallaCollectRoomFeverSel(Long roomNo);

    /**
     * 달라이벤트 스케줄 (현재 이벤트 진행여부 체크용도)
     * @Param
     *
     * @Return
     * cnt        INT		-- 로우 수
     * seq_no		INT		-- 회차번호
     * start_date	DATETIME	-- 시작일자
     * end_date		DATETIME	-- 종료일자
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_schedule_sel()")
    Map<String, Object> pEvtDallaCollectScheduleSel();

    /**
     * 달라이벤트 이니셜 뽑기
     * @Param
     * memNo        BIGINT		-- 회원번호
     * ,useDallaGubunOne CHAR(1)		-- 구분[d,a,l](사용)
     * ,useDallaGubunTwo CHAR(1)		-- 구분[d,a,l](사용)
     * ,insDallaGubun    	CHAR(1)		-- 구분[d,a,l](받은)
     *
     * @Return
     * s_return		INT		--  -1: 스톤 부족, 0:에러, 1: 정상
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_bbopgi_ins(#{memNo}, #{useDallaGubunOne}, #{useDallaGubunTwo}, #{insDallaGubun})")
    Integer pEvtDallaCollectBbopgiIns(Map<String, Object> param);

    /**
     * 달라이벤트 점수 등록
     * @Param
     * memNo            BIGINT      회원번호
     * ,roomNo          BIGINT      방번호
     * ,collectSlct     INT         구분[1:방송청취,2:방송시간,3:보낸달,4: 부스터수,5:받은별]
     * ,slctValCnt      BIGINT      구분에서의 값 [방송청취,방송시간,보낸달,부스터수,받은별]
     *
     * @Return
     * s_return         INT         -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 회원 데이터 없음 , 0: 에러, 1:정상
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_member_score_ins(#{memNo}, #{roomNo}, #{collectSlct}, #{slctValCnt})")
    Integer pEvtDallaCollectMemberScoreIns(Map<String, Object> param);

    /**
     * 달라이벤트 회원정보 o
     * @Param
     * seqNo INT 			-- 회차 번호
     * ,memNo BIGINT			-- 회원 번호
     *
     * @Return
     * rankNo		BIGINT		-- 순위
     * ins_d_cnt		INT		-- 사용가능한 d 수
     * ins_a_cnt		INT		-- 사용가능한 a 수
     * ins_l_cnt		INT		-- 사용가능한 l 수
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_no		BIGINT		-- 회원 번호
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    @Select("CALL p_evt_dalla_collect_member_rank_my_sel(#{seqNo}, #{memNo})")
    DallagersEventMySelVo pEvtDallaCollectMemberRankMySel(Map<String, Object> param);

    /**
     * 달라이벤트 리스트 o
     * @Param
     * seqNo 		INT 		-- 회차 번호
     * ,pageNo 		INT UNSIGNED	-- 페이지 번호
     * ,pagePerCnt 	INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * #1
     * cnt		BIGINT		-- 전체 수
     *
     * #2
     * seq_no		INT		-- 회차 번호
     * mem_no		BIGINT		-- 회원 번호
     * ins_d_cnt		INT		-- 사용가능한 d 수
     * ins_a_cnt		INT		-- 사용가능한 a 수
     * ins_l_cnt		INT		-- 사용가능한 l 수
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    @ResultMap({"ResultMap.integer", "ResultMap.DallagersEventMySelVo"})
    @Select("CALL rd_data.p_evt_dalla_collect_member_rank_list(#{seqNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> pEvtDallaCollectMemberRankList(Map<String, Object> param);


    /**
     * 달라이벤트 스페셜 회원정보 o
     * @Param
     * memNo		 BIGINT		-- 회원 번호
     *
     * @Return
     * rankNo		BIGINT		-- 순위
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_no		BIGINT		-- 회원 번호
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    @Select("CALL p_evt_dalla_collect_member_special_rank_my_sel(#{memNo})")
    DallagersEventSpecialMySelVo pEvtDallaCollectMemberSpecialRankMySel(Long memNo);

    /**
     * 달라이벤트 회원 스페셜 리스트 o
     * @Param
     * pageNo        INT UNSIGNED	-- 페이지 번호
     * ,pagePerCnt 	INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * #1
     * cnt		BIGINT		-- 전체 수
     *
     * #2
     * seq_no        INT		-- 회차 번호
     * mem_no		BIGINT		-- 회원 번호
     * dalla_cnt		INT		-- 달라 수
     * view_time	INT		-- 청취시간
     * play_time		INT		-- 방송시간
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * mem_id		VARCHAR	-- 회원 아이디
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * mem_level	BIGINT		-- 레벨
     * mem_state	BIGINT		-- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     * */
    @ResultMap({"ResultMap.integer", "ResultMap.DallagersEventSpecialMySelVo"})
    @Select("CALL p_evt_dalla_collect_member_special_rank_list(#{pageNo}, #{pagePerCnt})")
    List<Object> pEvtDallaCollectMemberSpecialRankList(Map<String, Object> param);

}
