package com.dalbit.team.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.team.proc.TeamProc;
import com.dalbit.team.vo.TeamBadgeVo;
import com.dalbit.team.vo.TeamParamVo;
import com.dalbit.team.vo.TeamResultVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class TeamService {
    @Autowired
    private GsonUtil gsonUtil;
    @Autowired
    private TeamProc teamProc;

    /**********************************************************************************************
    * @Method 설명 : 팀 등록 체크
    * @작성일   : 2022-03-31   
    * @작성자   : 이승재
    * @변경이력  :  
    **********************************************************************************************/ 
    public int getTeamInsChk(TeamParamVo vo){
        return teamProc.pDallaTeamInsChk(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 등록
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamIns(TeamParamVo vo){
        return teamProc.pDallaTeamIns(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 정보수정
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamUpd(TeamParamVo vo){
        return teamProc.pDallaTeamUpd(vo);
    }

    /**********************************************************************************************
     * @Method 설명 :팀 삭제
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamDel(TeamParamVo vo){
        return teamProc.pDallaTeamDel(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청/초대
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamMemReqIns(TeamParamVo vo){
        return teamProc.pDallaTeamMemReqIns(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입 수락
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamMemIns(TeamParamVo vo){
        return teamProc.pDallaTeamMemIns(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 탈퇴
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamMemDel(TeamParamVo vo){
        return teamProc.pDallaTeamMemDel(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀장변경
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamMasterUpd(TeamParamVo vo){
        return teamProc.pDallaTeamMasterUpd(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청 거절&취소
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamMemReqDel(TeamParamVo vo){
        return teamProc.pDallaTeamMemReqDel(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 정보
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public List<TeamResultVo> getTeamSel(TeamParamVo vo){
        return teamProc.pDallaTeamSel(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 멤버 정보
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public TeamResultVo getTeamMemSel(TeamParamVo vo){
        return teamProc.pDallaTeamMemSel(vo);
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 초대 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public String getTeamInvitationSel(TeamParamVo vo){
        List<Object> object =  teamProc.pDallaTeamInvitationSel(vo);
        Integer listCnt = DBUtil.getData(object, 0, Integer.class);
        List<TeamResultVo> list = DBUtil.getList(object, 1, TeamResultVo.class);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("listCnt", listCnt);
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public String getTeamRequestSel(TeamParamVo vo){
        List<Object> object =   teamProc.pDallaTeamRequestSel(vo);
        Integer listCnt = DBUtil.getData(object, 0, Integer.class);
        List<TeamResultVo> list = DBUtil.getList(object, 1, TeamResultVo.class);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("listCnt", listCnt);
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }


    /**********************************************************************************************
     * @Method 설명 : 팀 출석체크
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamAttendanceIns(TeamParamVo vo){
        return teamProc.pDallaTeamAttendanceIns(vo);
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 활동배지 전체 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public String getTeamBadgeList(TeamParamVo vo){
        List<Object> object =   teamProc.pDallaTeamBadgeList(vo);
        Integer totCnt = DBUtil.getData(object, 0, Integer.class);
        Integer cnt = DBUtil.getData(object, 1, Integer.class);
        List<TeamBadgeVo> list = DBUtil.getList(object, 2, TeamBadgeVo.class);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("totCnt", totCnt);
        resultMap.put("cnt", cnt);
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 대표활동배지 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public String getTeamRepresentBadgeSel(TeamParamVo vo){
        List<TeamResultVo> list = teamProc.pDallaTeamRepresentBadgeSel(vo);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 대표 활동배지 변경
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamBadgeUpd(TeamParamVo vo){
        return teamProc.pDallaTeamBadgeUpd(vo);
    }


}
