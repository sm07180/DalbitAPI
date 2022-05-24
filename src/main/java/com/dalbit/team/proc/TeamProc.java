package com.dalbit.team.proc;


import com.dalbit.team.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;


@Component
@Repository
public interface TeamProc {

    /**********************************************************************************************
     * @Method    : 팀 등록 체크
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 개설자 회원번호
     * @return    :
     * INT		-- -4:팀가입 되어 있음, -3:재생성 시간 미경과, -2: 이미생성됨, -1: 레벨미달, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_ins_chk(#{memNo})")
    Integer pDallaTeamInsChk(TeamParamVo param);


    /**********************************************************************************************
     * @Method    : 팀 등록
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 개설자 회원번호
     * teamName VARCHAR(15)		-- 팀이름
     * teamConts VARCHAR(200)		-- 팀소개내용
     * teamMedalCode CHAR(4)		-- 팀 메달 코드(m000 형식)
     * teamEdgeCode CHAR(4)		-- 팀 테두리 코드(e000 형식)
     * teamBgCode CHAR(4)		-- 팀 배경 코드(b000 형식)
     * @return    :
     * INT		-- -4:팀이름 중복, -3:재생성 시간 미경과, -2: 이미생성됨, -1: 팀없음, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_ins (#{memNo},#{teamName},#{teamConts},#{teamMedalCode},#{teamEdgeCode},#{teamBgCode})")
    Integer pDallaTeamIns(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 정보수정
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 개설자 회원번호
     * updSlct CHAR(1)		-- 수정구분[a:심볼및이름, b:소개수정]
     * teamNo BIGINT			-- 팀번호
     * teamName VARCHAR(15)		-- 팀이름
     * teamConts VARCHAR(200)		-- 팀소개내용
     * teamMedalCode CHAR(4)		-- 팀 메달 코드(m000 형식)
     * teamEdgeCode CHAR(4)		-- 팀 테두리 코드(e000 형식)
     * teamBgCode CHAR(4)		-- 팀 배경 코드(b000 형식)
     * reqMemYn CHAR(1)		-- 가입신청 허용여부
     * @return    :
     * INT		-- -4:팀이름 중복, -3:재생성 시간 미경과, -2: 이미생성됨, -1: 팀없음, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_upd (#{memNo},#{updSlct},#{teamNo},#{teamName},#{teamConts},#{teamMedalCode},#{teamEdgeCode},#{teamBgCode},#{reqMemYn})")
    Integer pDallaTeamUpd(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 삭제
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * masterMemNo BIGINT		-- 팀장 회원번호
     * chrgrName VARCHAR(40)		-- 삭제 관리자명 (관리자 삭제시)
     * @return    :
     * INT		-- -2: 팀삭제권한 없음, -1: 미가입 회원, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_del(#{teamNo},#{masterMemNo},#{chrgrName})")
    Integer pDallaTeamDel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 가입신청/초대
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * memNo BIGINT			-- 신청자 회원번호
     * reqSlct CHAR(1)		-- 신청구분 [r:가입신청, i:초대]
     * @return    :
     * INT		-- -7:정원초과, -6:가입신청 미허용, -5:이미초대됨, -4:이미신청함, -3:팀에 가입되어있음, -2: 팀없음, -1: 레벨부족, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_req_ins(#{teamNo},#{memNo},#{reqSlct})")
    Integer pDallaTeamMemReqIns(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 가입 수락
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * memNo BIGINT			-- 신청자 회원번호
     * @return    :
     * INT		-- -5:정원초과, -4:신청내역없음, -3:팀에 가입되어있음, -2: 팀없음, -1: 레벨부족, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_ins(#{teamNo},#{memNo})")
    Integer pDallaTeamMemIns(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 탈퇴
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * delSclt CHAR(1)		-- 탈퇴구분 [ m:팀장탈퇴, t:본인탈퇴, c:관리자탈퇴, e:회원탈퇴 ]
     * tmMemNo BIGINT			-- 팀원 회원번호
     * masterMemNo BIGINT		-- 팀장 회원번호
     * chrgrName VARCHAR(40)		-- 삭제 관리자명 (관리자 삭제시)
     * @return    :
     * INT		-- -2: 팀장아님, -1: 미가입 회원, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_del (#{teamNo},#{delSclt},#{tmMemNo},#{masterMemNo},#{chrgrName})")
    Integer pDallaTeamMemDel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀장변경
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * tmMemNo BIGINT			-- 팀원 회원번호
     * @return    :
     * INT		-- -1: 미가입 회원, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_master_upd (#{teamNo},#{tmMemNo})")
    Integer pDallaTeamMasterUpd(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 가입신청 거절&취소
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * masterMemNo BIGINT		-- 거절자 회원번호
     * memNo BIGINT			-- 신청자 회원번호
     * chrgrName VARCHAR(40)		-- 삭제 관리자명
     * @return    :
     * INT		-- -2: 신청내역없음, -1: 팀장 or 관리자아님, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_req_del (#{teamNo},#{masterMemNo},#{memNo},#{chrgrName})")
    Integer pDallaTeamMemReqDel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 회원 팀 정보
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * memNo BIGINT			-- 회원번호
     * @return    :
     *
     * team_no			BIGINT		-- 팀번호
     * master_mem_no		BIGINT		-- 팀장회원번호
     * team_name		VARCHAR(15)	-- 팀명
     * team_conts		VARCHAR(200)	-- 팀소개
     * team_medal_code		CHAR(4)		-- 팀메달
     * team_edge_code		CHAR(4)		-- 팀테두리
     * team_bg_code		CHAR(4)		-- 팀배경
     * team_mem_cnt		TINYINT		-- 팀원수
     * team_req_mem_cnt	TINYINT		-- 가입신청수
     * team_ivt_mem_cnt	TINYINT		-- 가입초대수
     * team_score		BIGINT		-- 팀점수
     * team_badge_score	BIGINT		-- 팀 배지점수
     * team_bonus_score	BIGINT		-- 팀 보너스점수
     * team_rank		INT		-- 팀랭킹
     * team_chnge_cnt		TINYINT		-- 팀정보 수정횟수
     * req_mem_yn		CHAR(1)		-- 가입신청 허용여부
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_sel (#{teamNo},#{memNo})")
    TeamResultVo pDallaTeamSel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 멤버 정보
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * @return    :
     *
     * team_no			BIGINT		-- 팀번호
     * tm_mem_no		BIGINT		-- 팀원회원번호
     * tm_mem_nick		VARCHAR(20)	-- 팀원대화명
     * tm_image_background	VARCHAR(256)	-- 프로필 배경사진
     * master_mem_no		BIGINT		-- 팀장회원번호
     * team_name		VARCHAR(15)	-- 팀명
     * team_conts		VARCHAR(200)	-- 팀소개
     * team_medal_code		CHAR(4)		-- 팀메달
     * team_edge_code		CHAR(4)		-- 팀테두리
     * team_bg_code		CHAR(4)		-- 팀배경
     * team_mem_cnt		TINYINT		-- 팀원수
     * tm_mem_score		BIGINT		-- 팀 기여점수
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_sel (#{teamNo})")
    TeamResultVo pDallaTeamMemSel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 회원 팀 초대 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 회원번호
     * pageNo INT UNSIGNED		-- 페이지 번호
     * pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @return    :
     *# RETURN [Multi Rows]
     *
     * #1
     *
     * cnt			INT		-- 총건수
     *
     * #2
     * team_no			BIGINT		-- 팀번호
     * tm_mem_no		BIGINT		-- 팀원회원번호
     * master_mem_no		BIGINT		-- 팀장회원번호
     * team_name		VARCHAR(15)	-- 팀명
     * team_conts		VARCHAR(200)	-- 팀소개
     * team_medal_code		CHAR(4)		-- 팀메달
     * team_edge_code		CHAR(4)		-- 팀테두리
     * team_bg_code		CHAR(4)		-- 팀배경
     * team_mem_cnt		TINYINT		-- 팀원수
     * team_score		BIGINT		-- 팀점수
     * team_rank		INT		-- 팀랭킹
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.TeamResultVo"})
    @Select("CALL rd_data.p_dalla_team_invitation_sel (#{memNo},#{pageNo},#{pagePerCnt})")
    List<Object> pDallaTeamInvitationSel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 가입신청 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 회원번호
     * pageNo INT UNSIGNED		-- 페이지 번호
     * pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @return    :
     *# RETURN [Multi Rows]
     *
     * #1
     *
     * cnt			INT		-- 총건수
     *
     * #2
     * team_no			BIGINT		-- 팀번호
     * tm_mem_no		BIGINT		-- 팀원회원버호
     * tm_mem_nick		VARCHAR(20)	-- 팀원대화명
     * tm_image_background	VARCHAR(256)	-- 프로필 배경사진
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.TeamMemVo"})
    @Select("CALL rd_data.p_dalla_team_request_sel (#{teamNo},#{pageNo},#{pagePerCnt})")
    List<Object> pDallaTeamRequestSel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 출석체크
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 회원번호
     * @return    :
     * s_return INT		-- -2:출석완료, -1: 팀 미가입, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_attendance_ins (#{memNo})")
    Integer pDallaTeamAttendanceIns(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 활동배지 전체 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * pageNo INT UNSIGNED		-- 페이지 번호
     * pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @return    :
     * # RETURN [Multi Rows]
     *
     * #1
     * tot_bg_cnt	INT		-- 총 활동배지수
     *
     * #2
     * cnt		INT		-- 획득배지수
     *
     * #3
     * auto_no			BIGINT			-- 배지고유번호
     * team_no			BIGINT			-- 팀번호
     * bg_slct			CHAR(1)			-- 배지구분[a:활동배지, b:배경, e:테두리, m:메달]
     * bg_slct_name		VARCHAR(15)		-- 배지구분명
     * bg_code			CHAR(4)			-- 배지코드
     * bg_name			VARCHAR(15)		-- 배지이름
     * bg_conts		VARCHAR(200)		-- 배지설명
     * bg_color_url		VARCHAR(500)		-- 배지URL(획득)
     * bg_black_url		VARCHAR(500)		-- 배지URL(미획득)
     * bg_bonus		INT			-- 배지경험치
     * bg_objective		INT			-- 목표수치(활동배지용)
     * bg_achieve		INT			-- 달성수치(활동배지용)
     * bg_cnt			BIGINT			-- 사용중인팀수
     * use_yn			CHAR(1)			-- 사용여부
     * bg_achieve_yn		CHAR(1)			-- 획득여부
     * chrgr_name		VARCHAR(40)		-- 등록관리자
     * ins_date		DATETIME		-- 등록일자
     * upd_date		DATETIME		-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer","ResultMap.integer", "ResultMap.TeamBadgeVo"})
    @Select("CALL rd_data.p_dalla_team_badge_list (#{teamNo},#{pageNo},#{pagePerCnt})")
    List<Object> pDallaTeamBadgeList(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 대표활동배지 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * @return    :
     *# RETURN [Multi Rows]
     * auto_no			BIGINT			-- 고유번호
     * team_no			BIGINT			-- 팀번호
     * bg_slct			CHAR(1)			-- 배지구분[a:활동배지, b:배경, e:테두리, m:메달]
     * bg_code			CHAR(4)			-- 배지코드
     * bg_name			VARCHAR(15)		-- 배지이름
     * bg_conts		VARCHAR(200)		-- 배지설명
     * bg_color_url		VARCHAR(500)		-- 배지URL(획득)
     * bg_black_url		VARCHAR(500)		-- 배지URL(미획득)
     * bg_achieve_yn		CHAR(1)			-- 획득여부
     * ins_date		DATETIME		-- 등록일자
     * upd_date		DATETIME		-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_represent_badge_sel (#{teamNo})")
    List<TeamResultVo> pDallaTeamRepresentBadgeSel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 대표 활동배지 변경
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * memNo BIGINT			-- 회원번호
     * updSlct CHAR(1)		-- 업데이트구분[y:대표 설정, n:대표해제]
     * bgCode CHAR(4)			-- 배지코드
     * @return    :
     * s_return		INT		-- -3:대표설정 배지수 초과, -2:배지 미달성, -1: 팀장아님, 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_badge_upd(#{teamNo},#{memNo},#{updSlct},#{bgCode})")
    Integer pDallaTeamBadgeUpd(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 내팀 심볼
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 회원번호
     * @return    :
     * team_no			BIGINT			-- 팀번호
     * team_name		VARCHAR(15)		-- 팀명
     * team_medal_code		CHAR(4)			-- 팀메달
     * team_edge_code		CHAR(4)			-- 팀테두리
     * team_bg_code		CHAR(4)			-- 팀배경
     * req_cnt			INT			-- 초대내역
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_my_sel(#{memNo})")
    TeamSymbolVo pDallaTeamMemMySel(@Param(value = "memNo") String memNo);

    /**********************************************************************************************
     * @Method    : 팀 초대/신청내역 읽음처리
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * s_return			INT		-- 0: 에러, 1:정상
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_req_upd (#{memNo},#{reqSlct})")
    Integer pDallaTeamMemReqUpd(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 심볼 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * symbolSlct CHAR(1)		-- 심볼구분 [b:배경, e:테두리, m:메달]
     * ordSlct CHAR(1)		-- 정렬구분 [f:선호도, c:코드순, i:갱신일순]
     * pageNo INT 			-- 페이지 번호
     * pagePerCnt INT 		-- 페이지 당 노출 건수 (Limit)
     * @return    :
     *
     * # RETURN [Multi Rows]
     *
     * #1
     * cnt		INT		-- 총건
     *
     * #2
     * auto_no			BIGINT		-- 고유번호
     * bg_slct			CHAR(1)		-- 배지구분[b:배경, e:테두리, m:메달]
     * bg_slct_name		VARCHAR(15)	-- 배지구분명
     * bg_code			CHAR(4)		-- 팀 배지 코드(x000 형식)
     * bg_name			VARCHAR(15)	-- 배지 이름
     * bg_conts		VARCHAR(200)	-- 소개
     * bg_url			VARCHAR(500)	-- 배지 URL
     * bg_cnt			BIGINT		-- 사용중인팀수
     * use_yn			CHAR(1)		-- 사용여부
     * chrgr_name		VARCHAR(40)	-- 등록관리자명
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.TeamSymbolVo"})
    @Select("CALL rd_data.p_dalla_team_symbol_list (#{symbolSlct},#{ordSlct},#{pageNo},#{pagePerCnt})")
    List<Object> pDallaTeamSymbolList(TeamParamVo param);


    /**********************************************************************************************
     * @Method    : 팀 심볼 리스트
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * @return    :
     *
     *# RETURN [Multi Rows]
     *
     * #1
     * team_no			BIGINT		-- 팀번호
     * master_mem_no		BIGINT		-- 팀장회원번호
     * team_name		VARCHAR(15)	-- 팀명
     * team_conts		VARCHAR(200)	-- 팀소개
     * team_medal_code		CHAR(4)		-- 팀메달
     * team_edge_code		CHAR(4)		-- 팀테두리
     * team_bg_code		CHAR(4)		-- 팀배경
     * team_mem_cnt		TINYINT		-- 팀원수
     * team_req_mem_cnt	TINYINT		-- 가입신청수
     * team_ivt_mem_cnt	TINYINT		-- 가입초대수
     * team_tot_score		BIGINT		-- 팀점수
     * rank_no			INT		-- 팀랭킹
     * rank_pt			INT		-- 팀랭킹 점수
     * team_chnge_cnt		TINYINT		-- 팀정보 수정횟수
     * req_mem_yn		CHAR(1)		-- 가입신청 허용여부
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     *
     * #2
     * tot_badge_cnt		INT		-- 달성한 활동배지수
     *
     * #3
     * auto_no			BIGINT			-- 고유번호
     * team_no			BIGINT			-- 팀번호
     * bg_slct			CHAR(1)			-- 배지구분[a:활동배지, b:배경, e:테두리, m:메달]
     * bg_code			CHAR(4)			-- 배지코드
     * bg_name			VARCHAR(15)		-- 배지이름
     * bg_conts		VARCHAR(200)		-- 배지설명
     * bg_color_url		VARCHAR(500)		-- 배지URL(획득)
     * bg_black_url		VARCHAR(500)		-- 배지URL(미획득)
     * bg_achieve_yn		CHAR(1)			-- 획득여부
     * ins_date		DATETIME		-- 등록일자
     * upd_date		DATETIME		-- 수정일자
     *
     * #4
     * team_no			BIGINT		-- 팀번호
     * tm_mem_no		BIGINT		-- 팀원회원번호
     * tm_mem_nick		VARCHAR(20)	-- 팀원대화명
     * tm_image_background	VARCHAR(256)	-- 프로필 배경사진
     * tm_mem_score		BIGINT		-- 팀 기여점수
     * team_mem_type		CHAR(1)		-- 팀 멤버 구분[m:개설자 ,t:일반멤버]
     * ins_date		DATETIME	-- 등록일자
     * upd_date		DATETIME	-- 수정일자
     * @변경이력   :
     **********************************************************************************************/
    @ResultMap({"ResultMap.TeamInfoVo","ResultMap.integer", "ResultMap.TeamBadgeVo","ResultMap.TeamMemVo", "ResultMap.map"})
    @Select("CALL rd_data.p_dalla_team_detail_sel(#{teamNo},#{memNo})")
    List<Object> pDallaTeamDetailSel(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀 번호 확인 프로시져
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT			-- 신청자 회원번호
     * @return    :
     * s_return			INT		-- 0: 팂없음,그외번호:팀번호
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_ins_chk (#{memNo})")
    Integer pDallaTeamMemInsChk(TeamParamVo param);

    @Select("CALL rd_data.p_dalla_team_mem_ins_chk(#{memNo})")
    Integer pDallaTeamMemInsChkV2(String memNo);

    /**********************************************************************************************
     * @Method    : 팀 가입신청/초대 체크
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * memNo BIGINT			-- 신청자 회원번호
     * reqSlct CHAR(1)		-- 신청구분 [r:가입신청, i:초대]
     * @return    :
     * s_return			INT		-- 0: 팂없음,그외번호:팀번호
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_req_ins_chk (#{teamNo},#{memNo},#{reqSlct})")
    Integer pDallaTeamMemReqInsChk(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팀원 상태체크
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * teamNo BIGINT			-- 팀번호
     * memNo BIGINT			-- 신청자 회원번호
     * @return    :
     * s_teamMemType		CHAR(1)		-- 팀 멤버 구분[m:개설자 ,t:일반멤버, n:미가입]
     * @변경이력   :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_team_mem_stat_chk (#{teamNo},#{memNo})")
    String pDallaTeamMemStatChk(TeamParamVo param);

    /**********************************************************************************************
     * @Method    : 팬,스타 리스트(팀용)
     * @Date      : 2022-03-30
     * @Author    : 이승재
     * @param     :
     * memNo BIGINT 			-- 회원번호
     * listSlct CHAR(1)		-- 리스트구분[s:스타, f:팬]
     * pageNo INT 			-- 페이지 번호
     * pagePerCnt INT 		-- 페이지 당 노출 건수 (Limit)
     * @return    :
     * #1
     * cnt			INT		-- 총건수
     *
     * #2 (s 일때)
     * mem_no			BIGINT		-- 회원번호
     * mem_no_star		BIGINT		-- 회원번호 (나의스타)
     * mem_nick_star		VARCHAR(20)	-- 회원닉네임 (나의스타)
     * mem_level_star		SMALLINT	-- 회원레벨 (나의스타)
     * team_yn			CHAR(1)		-- 팀가입여부
     * team_req_yn		CHAR(1)		-- 팀가입 초대 (미초대:n, 가입신청:r, 초대:i)
     *
     * OR (f 일때)
     * mem_no			BIGINT		-- 회원번호
     * mem_no_star		BIGINT		-- 회원번호 (나의팬)
     * mem_nick_star		VARCHAR(20)	-- 회원닉네임 (나의팬)
     * mem_level_star		SMALLINT	-- 회원레벨 (나의팬)
     * team_yn			CHAR(1)		-- 팀가입여부
     * team_req_yn		CHAR(1)		-- 팀가입 초대 (미초대:n, 가입신청:r, 초대:i)
     * @변경이력   :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.TeamFanStarVo"})
    @Select("CALL rd_data.p_dalla_team_mem_fanstar_list (#{memNo},#{listSlct},#{pageNo},#{pagePerCnt})")
    List<Object> pDallaTeamMemFanstarList(TeamParamVo param);


}
