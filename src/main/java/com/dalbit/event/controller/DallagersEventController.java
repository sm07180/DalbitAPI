package com.dalbit.event.controller;

import com.dalbit.event.service.DallagersEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@RestController
@RequestMapping("/event/dallagers")
@Scope("prototype")
@Slf4j
public class DallagersEventController {
    @Autowired
    DallagersEventService dallagersEventService;

    /**
     * 회차정보 (이벤트 진행여부 및 회차정보)
     * @Param
     *
     * @Return
     * cnt        INT		-- 로우 수
     * seq_no		INT		-- 회차번호
     * start_date	DATETIME	-- 시작일자
     * end_date		DATETIME	-- 종료일자
     * */
    @GetMapping("/reqNo")
    public String getReqNo (){
        return dallagersEventService.getReqNo();
    }

    /**
     * 달라이벤트 이니셜 뽑기 (스톤 뽑기)
     * @Param
     * memNo        BIGINT		-- 회원번호
     * ,useDallaGubunOne CHAR(1)		-- 구분[d,a,l](사용)
     * ,useDallaGubunTwo CHAR(1)		-- 구분[d,a,l](사용)
     * ,insDallaGubun    	CHAR(1)		-- 구분[d,a,l](받은)
     *
     * seqNo        INT         --현재 회차 (insert 후 회원정보 select 용)
     * @Return
     * s_return		INT		--  -1: 스톤 부족, 0:에러, 1: 정상
     *
     * 성공시 { resultStone: 'd', myInfo: {...} }  //뽑기 결과 + 내 정보 재갱신 정보
     * */
    @PostMapping("/stoneReplace")
    public String stoneIns (@RequestBody HashMap param, HttpServletRequest request){
        return dallagersEventService.pEvtDallaCollectBbopgiIns(param, request);
    }

    /**
     * 달라이벤트 회원정보 o
     * @Param
     * seqNo INT 			-- 회차 번호 (1 이상 )
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
    @GetMapping("/myInfo/{seqNo}")
    public String getMyRankInfo (@PathVariable("seqNo") @NotNull Integer seqNo,
                                 HttpServletRequest request){
        return dallagersEventService.getMyRankInfo(seqNo, request);
    }

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
    @GetMapping("/specialMyInfo")
    public String getSpecialMyRankInfo (HttpServletRequest request){
        return dallagersEventService.getSpecialMyRankInfo(request);
    }

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
    @GetMapping("/list/{seqNo}/{pageNo}/{pagePerCnt}")
    public String getRankList (@PathVariable("seqNo") @NotNull Integer seqNo,
                               @PathVariable("pageNo") @NotNull Integer pageNo,
                               @PathVariable("pagePerCnt") @NotNull Integer pagePerCnt,
                               HttpServletRequest request){
        HashMap<String, Object> param = new HashMap<>();
        param.put("seqNo", seqNo);
        param.put("pageNo", pageNo);
        param.put("pagePerCnt", pagePerCnt);
        return dallagersEventService.getRankList(param, request);
    }

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
    @GetMapping("/specialList/{pageNo}/{pagePerCnt}")
    public String getSpecialRankList (@PathVariable("pageNo") @NotNull Integer pageNo,
                                      @PathVariable("pagePerCnt") @NotNull Integer pagePerCnt,
                                      HttpServletRequest request){
        HashMap<String, Object> param = new HashMap<>();
        param.put("pageNo", pageNo);
        param.put("pagePerCnt", pagePerCnt);
        return dallagersEventService.getSpecialRankList(param, request);
    }
}
