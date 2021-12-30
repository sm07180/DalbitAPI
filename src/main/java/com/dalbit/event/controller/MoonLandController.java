package com.dalbit.event.controller;

import com.dalbit.event.service.MoonLandService;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.vo.MoonLandMissionSelVO;
import com.dalbit.event.vo.MoonLandScoreInsVO;
import com.dalbit.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/event/moonLand")
@Scope("prototype")
public class MoonLandController {
    @Autowired
    MoonLandService moonLandService;

    /** 달나라 이벤트 일정 리스트 (회차정보)
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @PROC : CALL rd_data.tb_evt_moon_no_sel()
     * @Param :
     * @Return :
     */
    @GetMapping("/info/sel")
    public ResVO getMoonLandInfoData(){
        ResVO resVO = new ResVO();
        try{
            resVO.setSuccessResVO(moonLandService.getMoonLandInfoData());
        }catch(Exception e){
            resVO.setFailResVO();
            log.error("getMoonLandInfoData => {}", e);
        }
        return resVO;
    }

    //------------------------------------ 방송방 ↓ ------------------------------------
    /** 달나라 이벤트 아이템 미션 데이터 완료 리스트
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     *
     * @PROC : CALL rd_data.tb_evt_moon_item_mission_sel (#{moonNo}, #{roomNo})
     * @Param :
     * moonNo INT                   --회차번호
     * ,roomNo BIGINT			    --방번호
     *
     * @Return :
     */
    @GetMapping("/mission/sel/{roomNo}")
    public ResVO getMoonLandMissionData(@PathVariable("roomNo") Long roomNo, HttpServletRequest request){
        ResVO resVO = new ResVO();
        try {
            if(roomNo != null) {
                resVO.setSuccessResVO(moonLandService.getMoonLandMissionData(roomNo, request));
            } else {
                resVO.setFailResVO();
                log.error("MoonLandController getMoonLandMissionData param is null roomNo: {}", roomNo);
            }
        } catch (Exception e) {
            resVO.setFailResVO();
            log.error("MoonLandController getMoonLandPopUpData => {}", e);
        }

        return resVO;
    }

    //------------------------------------ 달나라 이벤트 페이지 ↓  ------------------------------------

    /** 달나라 이벤트 나의 순위
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @PROC : CALL rd_data.tb_evt_moon_rank_my_sel(#{moonNo}, #{memNo})
     * @Param :
     * moonNo INT			    -- 회차번호
     * ,memNo BIGINT 			-- 회원번호
     *
     * @Return :
     */
    @GetMapping("/rank/my/sel/{moonNo}")
    public ResVO getMoonLandMyRank(@PathVariable("moonNo") Integer moonNo, HttpServletRequest request){
        ResVO resVO = new ResVO();
        try {
            if(moonNo != null) {
                resVO.setSuccessResVO(moonLandService.getMoonLandMyRank(moonNo, request));
            } else {
                resVO.setFailResVO();
                log.error("MoonLandController getMoonLandMyRank param is null moonNo: {}", moonNo);
            }
        } catch (Exception e) {
            resVO.setFailResVO();
            log.error("MoonLandController getMoonLandMyRank => {}", e);
        }

        return resVO;
    }

    /** 달나라 이벤트 랭킹 리스트
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * moonNo INT			-- 회차번호
     * ,memNo BIGINT 			-- 회원번호
     * ,pageNo INT 			-- 페이지 번호
     * ,pagePerCnt INT 		-- 페이지 당 노출 건수 (Limit)
     *
     * @Return : [Multi Rows]
     */
    @GetMapping("/rank/list/{moonNo}/{pageNo}/{pagePerCnt}")
    public ResVO getMoonLandRankList(@PathVariable("moonNo") Integer moonNo,
                                     @PathVariable("pageNo") Integer pageNo,
                                     @PathVariable("pagePerCnt") Integer pagePerCnt,
                                     HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            if(moonNo != null && pageNo != null && pagePerCnt != null) {
                resVO.setSuccessResVO(moonLandService.getMoonLandRankList(moonNo, pageNo, pagePerCnt, request));
            } else {
                resVO.setFailResVO();
                log.error("MoonLandController getMoonLandRankList param is null moonNo: {}, pageNo: {}, pagePerCnt: {}", moonNo, pageNo, pagePerCnt);
            }
        } catch (Exception e) {
            resVO.setFailResVO();
            log.error("MoonLandController getMoonLandRankList => {}", e);
        }

        return resVO;
    }

    /** 달나라 점수 누적
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * @Return : 0 or 1
     */
    @PostMapping("/score")
    public ResVO setMoonLandScore(@RequestBody MoonLandScoreInsVO paramVO, HttpServletRequest request){
        ResVO resVO = new ResVO();
        try {
                Long memNo = Long.parseLong(MemberVo.getMyMemNo(request));
                Long roomNo = Long.parseLong(paramVO.getRoomNo());
                resVO.setSuccessResVO(moonLandService.setMoonLandScoreIns(memNo, paramVO.getType(), paramVO.getScore(), roomNo));
        } catch (Exception e) {
            resVO.setFailResVO();
            log.error("MoonLandController setMoonLandScore => {}", e);
        }

        return resVO;
    }
}
