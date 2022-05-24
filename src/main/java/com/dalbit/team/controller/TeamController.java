package com.dalbit.team.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.team.vo.TeamResultVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.team.service.TeamService;
import com.dalbit.team.vo.TeamParamVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/team")
@Scope("prototype")
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private GsonUtil gsonUtil;

    /**********************************************************************************************
     * @Method 설명 : 팀 등록 체크
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamChk")
    public ResVO getTeamInsChk(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamInsChk(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamInsChk => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 등록
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamIns")
    public ResVO getTeamIns(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamIns(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamIns => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 정보수정
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamUpd")
    public ResVO getTeamUpd(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamUpd(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamUpd => {}", e);
            result.setFailResVO();
        }
        return  result;
    }
    /**********************************************************************************************
     * @Method 설명 :팀 삭제
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamDel")
    public ResVO getTeamDel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamDel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamUpd => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청/초대
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/memReqIns")
    public ResVO getTeamMemReqIns(@RequestBody TeamParamVo vo, HttpServletRequest request){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemReqIns(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemReqIns => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입 수락
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/memIns")
    public ResVO getTeamMemIns(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemIns(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemReqIns => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 탈퇴
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/memDel")
    public ResVO getTeamMemDel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemDel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemDel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀장변경
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/masterUpd")
    public ResVO getTeamMasterUpd(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMasterUpd(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMasterUpd => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청 거절&취소
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/memReqDel")
    public ResVO getTeamMemReqDel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemReqDel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemReqDel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 정보
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamSel")
    public ResVO getTeamSel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamSel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamSel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 멤버 정보
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamMemSel")
    public ResVO getTeamMemSel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemSel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemSel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 초대 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamInvitationSel")
    public ResVO getTeamInvitationSel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamInvitationSel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamInvitationSel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamRequestSel")
    public ResVO getTeamRequestSel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamRequestSel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamRequestSel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 출석체크
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamAttendanceIns")
    public ResVO getTeamAttendanceIns(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamAttendanceIns(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamAttendanceIns => {}", e);
            result.setFailResVO();
        }
        return  result;
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 활동배지 전체 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamBadgeList")
    public ResVO getTeamBadgeList(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamBadgeList(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamBadgeList => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 대표활동배지 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamRepresentBadgeSel")
    public ResVO getTeamRepresentBadgeSel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamRepresentBadgeSel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamRepresentBadgeSel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 대표 활동배지 변경
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamBadgeUpd")
    public ResVO getTeamBadgeUpd(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamBadgeUpd(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamBadgeUpd => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 :내팀 심볼
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamMemMySel")
    public ResVO getTeamMemMySel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemMySel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemMySel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 :팀 초대/신청내역 읽음처리
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamMemReqUpd")
    public ResVO getTeamMemReqUpd(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemReqUpd(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemReqUpd => {}", e);
            result.setFailResVO();
        }
        return  result;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 심볼 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamSymbolList")
    public ResVO getTeamSymbolList(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamSymbolList(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamSymbolList => {}", e);
            result.setFailResVO();
        }
        return  result;
    }
    /**********************************************************************************************
     * @Method 설명 :팀 정보(상세)
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamDetailSel")
    public ResVO getTeamDetailSel(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamDetailSel(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamDetailSel => {}", e);
            result.setFailResVO();
        }
        return  result;
    }
    /**********************************************************************************************
     * @Method 설명 :팬,스타 리스트(팀용)
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamMemFanstarList")
    public ResVO getTeamMemFanstarList(@RequestBody TeamParamVo vo){
        ResVO result = new ResVO();
        try {
            result = teamService.getTeamMemFanstarList(vo);
        } catch (Exception e) {
            log.error("TeamController / getTeamMemFanstarList => {}", e);
            result.setFailResVO();
        }
        return  result;
    }



}
