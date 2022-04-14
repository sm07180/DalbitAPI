package com.dalbit.team.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.team.proc.TeamProc;
import com.dalbit.team.vo.*;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
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
    public ResVO getTeamInsChk(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamInsChk(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamInsChk error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 등록
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamIns(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamIns(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamIns error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 정보수정
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamUpd(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamUpd(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamUpd error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 :팀 삭제
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamDel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamDel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamDel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청/초대
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemReqIns(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamMemReqIns(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemReqIns error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 가입 수락
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemIns(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamMemIns(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemReqIns error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 탈퇴
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/

    public ResVO getTeamMemDel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamMemDel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemReqIns error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }
    /**********************************************************************************************
     * @Method 설명 : 팀장변경
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMasterUpd(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamMasterUpd(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMasterUpd error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청 거절&취소
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemReqDel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamMemReqDel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemReqDel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 정보
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamSel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<TeamResultVo> result = teamProc.pDallaTeamSel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }


    /**********************************************************************************************
     * @Method 설명 : 팀 멤버 정보
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemSel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            TeamResultVo result = teamProc.pDallaTeamMemSel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 초대 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamInvitationSel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object =  teamProc.pDallaTeamInvitationSel(vo);
            Integer listCnt = DBUtil.getData(object, 0, Integer.class);
            List<TeamResultVo> list = DBUtil.getList(object, 1, TeamResultVo.class);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("listCnt", listCnt);
            resultMap.put("list", list);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("getTeamInvitationSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 가입신청 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/

    public ResVO getTeamRequestSel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object =   teamProc.pDallaTeamRequestSel(vo);
            Integer listCnt = DBUtil.getData(object, 0, Integer.class);
            List<TeamResultVo> list = DBUtil.getList(object, 1, TeamResultVo.class);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("listCnt", listCnt);
            resultMap.put("list", list);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("getTeamRequestSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 출석체크
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamAttendanceIns(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamAttendanceIns(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamAttendanceIns error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }
    /**********************************************************************************************
     * @Method 설명 : 팀 활동배지 전체 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/

    public ResVO getTeamBadgeList(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object =   teamProc.pDallaTeamBadgeList(vo);
            Integer totCnt = DBUtil.getData(object, 0, Integer.class);
            Integer cnt = DBUtil.getData(object, 1, Integer.class);
            List<TeamBadgeVo> list = DBUtil.getList(object, 2, TeamBadgeVo.class);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("totCnt", totCnt);
            resultMap.put("cnt", cnt);
            resultMap.put("list", list);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("getTeamBadgeList error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 대표활동배지 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamRepresentBadgeSel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<TeamResultVo> list = teamProc.pDallaTeamRepresentBadgeSel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), list);
        } catch (Exception e) {
            log.error("getTeamRepresentBadgeSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 대표 활동배지 변경
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamBadgeUpd(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamBadgeUpd(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamBadgeUpd error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }
    /**********************************************************************************************
     * @Method 설명 : 내팀 심볼
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemMySel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            TeamSymbolVo result = teamProc.pDallaTeamMemMySel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemMySel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 초대/신청내역 읽음처리
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemReqUpd(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            int result = teamProc.pDallaTeamMemReqUpd(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamMemReqUpd error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 랭킹 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamRankWeekList(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object =   teamProc.pDallaTeamRankWeekList(vo);
            Integer listCnt = DBUtil.getData(object, 0, Integer.class);
            List<TeamRankVo> list = DBUtil.getList(object, 1, TeamRankVo.class);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("listCnt", listCnt);
            resultMap.put("list", list);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("getTeamRankWeekList error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팀 심볼 리스트
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamSymbolList(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object =   teamProc.pDallaTeamSymbolList(vo);
            Integer listCnt = DBUtil.getData(object, 0, Integer.class);
            List<TeamSymbolVo> list = DBUtil.getList(object, 1, TeamSymbolVo.class);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("listCnt", listCnt);
            resultMap.put("list", list);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("getTeamSymbolList error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }


}
