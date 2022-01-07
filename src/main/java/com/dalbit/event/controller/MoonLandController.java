package com.dalbit.event.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.service.MoonLandService;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.vo.MoonLandInfoVO;
import com.dalbit.event.vo.MoonLandMissionSelVO;
import com.dalbit.event.vo.MoonLandScoreInsVO;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/event/moonLand")
@Scope("prototype")
public class MoonLandController {
    @Autowired
    MoonLandService moonLandService;
    @Autowired
    GsonUtil gsonUtil;

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
            List<MoonLandInfoVO> list = moonLandService.getMoonLandInfoData();
            if(list != null && list.size() > 0){
                resVO.setSuccessResVO(list);
            } else {
                List<MoonLandInfoVO> hardCodingList  = new ArrayList<>();
                MoonLandInfoVO vo = new MoonLandInfoVO();
                vo.setMoon_no(1);
                vo.setStart_date("2022-01-10");
                vo.setEnd_date("2022-01-16");
                vo.setIns_date("");
                hardCodingList.add(vo);
                resVO.setSuccessResVO(list);
            }
        }catch(Exception e){
            resVO.setFailResVO();
            log.error("MoonLandController Exception getMoonLandInfoData => {}", e);
        }
        return resVO;
    }

    //------------------------------------ 방송방 ↓ ------------------------------------
    /** 달나라 이벤트 아이템 미션 데이터 완료 리스트 (달나라 팝업 띄우기)
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
    @GetMapping("/mission/sel")
    public String getMoonLandMissionData(@RequestParam("roomNo") String roomNo, HttpServletRequest request){
        try {
            if(!StringUtils.equals(null, roomNo)) {
                Map<String, Object> result = moonLandService.getMoonLandMissionData(roomNo, request);
                if(result != null){
                    return gsonUtil.toJson(new JsonOutputVo(Status.달나라_팝업조회_성공, result));    
                } else {    //db 에러로 list가 null 인 경우
                    return gsonUtil.toJson(new JsonOutputVo(Status.달나라_팝업조회_실패));
                }
            } else {
                log.error("MoonLandController getMoonLandMissionData param is null roomNo: {}", roomNo);
                return gsonUtil.toJson(new JsonOutputVo(Status.달나라_팝업조회_실패));
            }
        } catch (Exception e) {
            log.error("MoonLandController Exception getMoonLandPopUpData => {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.달나라_팝업조회_실패));
        }
    }

    /** 달나라 점수 누적
     * 작성일 : 2021-12-28
     * 작성자 : 박용훈
     * @Param :
     * @Return : 0: 실패 or 1: 성공
     */
    @PostMapping("/score") //todo => returnType : String
    public String setMoonLandScore(@Valid MoonLandScoreInsVO paramVO, HttpServletRequest request){
        try {
            Long memNo = Long.parseLong(MemberVo.getMyMemNo(request));
            Long roomNo = Long.parseLong(paramVO.getRoomNo());
            return gsonUtil.toJson(new JsonOutputVo(Status.달나라_점수등록_성공, moonLandService.setMoonLandScoreIns(memNo, paramVO.getType(), paramVO.getScore(), roomNo)));
        } catch (Exception e) {
            log.error("MoonLandController Exception setMoonLandScore => {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.달나라_점수등록_실패));
        }
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
            log.error("MoonLandController Exception getMoonLandMyRank => {}", e);
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
            log.error("MoonLandController Exception getMoonLandRankList => {}", e);
        }

        return resVO;
    }

}
