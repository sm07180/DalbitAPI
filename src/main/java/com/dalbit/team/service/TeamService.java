package com.dalbit.team.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.dao.PushDao;
import com.dalbit.common.service.PushService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.team.proc.TeamProc;
import com.dalbit.team.vo.*;
import com.dalbit.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class TeamService {
    @Autowired
    private TeamProc teamProc;
    @Autowired
    PushDao pushDao;
    @Autowired
    PushService pushService;

    /**********************************************************************************************
    * @Method 설명 : 팀 등록 체크
    * @작성일   : 2022-03-31
    * @작성자   : 이승재
    * @변경이력  :
    **********************************************************************************************/
    public ResVO getTeamInsChk(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            Integer result = teamProc.pDallaTeamInsChk(vo);
            if(result == -4){
                resVO.setResVO(ResMessage.C60001.getCode(), ResMessage.C60001.getCodeNM(), result);
            }else if(result == -3){
                resVO.setResVO(ResMessage.C60002.getCode(), ResMessage.C60002.getCodeNM(), result);
            }else if(result == -2){
                resVO.setResVO(ResMessage.C60003.getCode(), ResMessage.C60003.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60004.getCode(), ResMessage.C60004.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }

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
            Integer result = teamProc.pDallaTeamIns(vo);
            if(result == -5){
                resVO.setResVO(ResMessage.C60023.getCode(), ResMessage.C60023.getCodeNM(), result);
            }else if(result == -4){
                resVO.setResVO(ResMessage.C60005.getCode(), ResMessage.C60005.getCodeNM(), result);
            }else if(result == -3){
                resVO.setResVO(ResMessage.C60002.getCode(), ResMessage.C60002.getCodeNM(), result);
            }else if(result == -2){
                resVO.setResVO(ResMessage.C60003.getCode(), ResMessage.C60003.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60004.getCode(), ResMessage.C60004.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                int teamNo =  teamProc.pDallaTeamMemInsChk(vo);
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("teamNo", teamNo);
                resultMap.put("result", result);
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            Integer result = teamProc.pDallaTeamUpd(vo);
            if(result == -4){
                resVO.setResVO(ResMessage.C60005.getCode(), ResMessage.C60005.getCodeNM(), result);
            }else if(result == -3){
                resVO.setResVO(ResMessage.C60020.getCode(), ResMessage.C60020.getCodeNM(), result);
            }else if(result == -2){
                resVO.setResVO(ResMessage.C60008.getCode(), ResMessage.C60008.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60007.getCode(), ResMessage.C60007.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            Integer result = teamProc.pDallaTeamDel(vo);
            if(result == -2){
                resVO.setResVO(ResMessage.C60009.getCode(), ResMessage.C60009.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60010.getCode(), ResMessage.C60010.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
                // 팀 삭제할 경우, 팀원 전체에게 삭제 푸시 알림 전송.
                for(Long value : vo.getMemNoList()) {
                    pushService.reqPushData(Long.toString(value), "", vo.getTeamName() + "팀이 해체되었어요.", "108", "");
                };
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            Integer result = teamProc.pDallaTeamMemReqIns(vo);
            if(result == -7){
                resVO.setResVO(ResMessage.C60011.getCode(), ResMessage.C60011.getCodeNM(), result);
            }else if(result == -6){
                resVO.setResVO(ResMessage.C60012.getCode(), ResMessage.C60012.getCodeNM(), result);
            }else if(result == -5){
                resVO.setResVO(ResMessage.C60013.getCode(), ResMessage.C60013.getCodeNM(), result);
            }else if(result == -4){
                resVO.setResVO(ResMessage.C60014.getCode(), ResMessage.C60014.getCodeNM(), result);
            }else if(result == -3){
                resVO.setResVO(ResMessage.C60015.getCode(), ResMessage.C60015.getCodeNM(), result);
            }else if(result == -2){
                resVO.setResVO(ResMessage.C60007.getCode(), ResMessage.C60007.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60004.getCode(), ResMessage.C60004.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
                // 팀 초대일 경우, 초대 받은 사람에게 푸시 알림 전송
                if (vo.getReqSlct().equals("i")) {
                    pushService.reqPushData(Long.toString(vo.getMemNo()), "", vo.getTeamName() + "팀에서 나를 초대했어요.", "101", "");
                // 팀 가입신청일 경우, 팀장에게 푸시 알림 전송
                } else if (vo.getReqSlct().equals("r")) {
                    pushService.reqPushData(Long.toString(vo.getMasterMemNo()), "", vo.getName() + "님이 우리팀에 가입을 신청했어요.", "104", "/team/detail/" + vo.getTeamNo());
                }
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            Integer result = teamProc.pDallaTeamMemIns(vo);
             if(result == -5){
                resVO.setResVO(ResMessage.C60011.getCode(), ResMessage.C60011.getCodeNM(), result);
            }else if(result == -4){
                resVO.setResVO(ResMessage.C60016.getCode(), ResMessage.C60016.getCodeNM(), result);
            }else if(result == -3){
                resVO.setResVO(ResMessage.C60003.getCode(), ResMessage.C60003.getCodeNM(), result);
            }else if(result == -2){
                resVO.setResVO(ResMessage.C60007.getCode(), ResMessage.C60007.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60004.getCode(), ResMessage.C60004.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
                 // 팀 초대 승낙일 경우, 팀장에게 푸시 알림 전송
                 if (vo.getReqSlct().equals("i")) {
                     pushService.reqPushData(Long.toString(vo.getMasterMemNo()), "", vo.getName() + "님이 우리팀의 멤버가 되었어요.", "102", "/team/detail/" + vo.getTeamNo());
                 // 팀 가입신청 수락일 경우, 신청자에게 푸시 알림 전송
                 } else if (vo.getReqSlct().equals("r")) {
                     pushService.reqPushData(Long.toString(vo.getMemNo()), "", vo.getTeamName() + "팀에 가입했어요.", "105", "/team/detail/" + vo.getTeamNo());
                 }
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }


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
            Integer result = teamProc.pDallaTeamMemDel(vo);
            if(result == -2){
                resVO.setResVO(ResMessage.C60017.getCode(), ResMessage.C60017.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60010.getCode(), ResMessage.C60010.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
                // 팀 초대 승낙일 경우, 팀장에게 푸시 알림 전송
                if (vo.getDelSclt().equals("m")) {
                    pushService.reqPushData(Long.toString(vo.getTmMemNo()), "", vo.getMasterName() + "님에 의해 팀에서 탈퇴되었어요.", "109", "");
                }
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            Integer result = teamProc.pDallaTeamMasterUpd(vo);
            if(result == -1){
                resVO.setResVO(ResMessage.C60010.getCode(), ResMessage.C60010.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            Integer result = teamProc.pDallaTeamMemReqDel(vo);
            if(result == -2){
                resVO.setResVO(ResMessage.C60016.getCode(), ResMessage.C60016.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60017.getCode(), ResMessage.C60017.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }

            // 팀 초대 거절일 경우, 팀장에게 푸시 알림 전송
            if (vo.getReqSlct().equals("i")) {
                pushService.reqPushData(Long.toString(vo.getMasterMemNo()), "", vo.getName() + "님에게 보낸 초대가 거절되었어요.", "103", "/team/detail/" + vo.getTeamNo());
            // 팀 가입신청 거절일 경우, 신청자에게 푸시 알림 전송
            } else if (vo.getReqSlct().equals("r")) {
                pushService.reqPushData(Long.toString(vo.getMemNo()), "", vo.getTeamName() + "팀 가입 신청이 거절되었어요.", "106", "");
            }
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
            TeamResultVo result = teamProc.pDallaTeamSel(vo);
            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
        } catch (Exception e) {
            log.error("getTeamSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 팀 정보 (내부 서비스용)
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public TeamResultVo getTeamSelService(TeamParamVo vo){
        ResVO resVO = new ResVO();
        TeamResultVo result = new TeamResultVo();
        try {
            result = teamProc.pDallaTeamSel(vo);
        } catch (Exception e) {
            log.error("getTeamSel error ===> {}", e);
        }
        return result;
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
            List<TeamMemVo> list = DBUtil.getList(object, 1, TeamMemVo.class);
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
            if(result == -2){
                resVO.setResVO(ResMessage.C60018.getCode(), ResMessage.C60018.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60021.getCode(), ResMessage.C60021.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
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
            String statChk = teamProc.pDallaTeamMemStatChk(vo);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("totCnt", totCnt);
            resultMap.put("cnt", cnt);
            resultMap.put("list", list);
            resultMap.put("statChk", statChk);
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
            Integer result = teamProc.pDallaTeamBadgeUpd(vo);

            if(result == -3){
                resVO.setResVO(ResMessage.C60019.getCode(), ResMessage.C60019.getCodeNM(), result);
            }else if(result == -2){
                resVO.setResVO(ResMessage.C60022.getCode(), ResMessage.C60022.getCodeNM(), result);
            }else if(result == -1){
                resVO.setResVO(ResMessage.C60017.getCode(), ResMessage.C60017.getCodeNM(), result);
            }else if(result == 0){
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), result);
            }else if(result == 1){
                resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), result);
            }else{
                resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("getTeamBadgeUpd error ===> {}", e.getMessage());
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
            TeamSymbolVo result = teamProc.pDallaTeamMemMySel(vo.getMemNo().toString());
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

    /**********************************************************************************************
     * @Method 설명 : 팀 정보(상세)
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamDetailSel(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object = teamProc.pDallaTeamDetailSel(vo);
            HashMap<String, Object> resultMap = new HashMap<>();
            int reqInsChk =  teamProc.pDallaTeamMemReqInsChk(vo);
            String statChk = teamProc.pDallaTeamMemStatChk(vo);
            TeamInfoVo teamInfo = DBUtil.getData(object, 0, TeamInfoVo.class);
            Integer badgeTotCnt = DBUtil.getData(object, 1, Integer.class);
            List<TeamBadgeVo> badgeList = DBUtil.getList(object, 2, TeamBadgeVo.class);
            List<TeamMemVo> teamMemList = DBUtil.getList(object, 3, TeamMemVo.class);
            HashMap<String, Object> loginYn = DBUtil.getData(object, 4, HashMap.class);
            if(teamInfo.getTeam_chnge_cnt() > 0){
                resultMap.put("editChk", false);
            }else{
                resultMap.put("editChk", true);
            }
            resultMap.put("reqInsChk", reqInsChk);
            resultMap.put("statChk", statChk);
            resultMap.put("listCnt", badgeTotCnt);
            resultMap.put("teamInfo", teamInfo);
            resultMap.put("badgeList", badgeList);
            resultMap.put("teamMemList", teamMemList);
            resultMap.put("loginYn", loginYn.get("s_loginYn"));

            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("getTeamDetailSel error ===> {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 팬,스타 리스트(팀용)
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public ResVO getTeamMemFanstarList(TeamParamVo vo){
        ResVO resVO = new ResVO();
        try {
            List<Object> object =   teamProc.pDallaTeamMemFanstarList(vo);
            Integer listCnt = DBUtil.getData(object, 0, Integer.class);
            List<TeamFanStarVo> list = DBUtil.getList(object, 1, TeamFanStarVo.class);
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

    /**********************************************************************************************
     * @Method 설명 : 팀번호 리턴용
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    public int getTeamMemInsChk(TeamParamVo vo){
        int teamNo = 0;
        try {
            teamNo =  teamProc.pDallaTeamMemInsChk(vo);
        } catch (Exception e) {
            log.error("getTeamMemInsChk error ===> {}", e);
        }
        return teamNo;
    }

    public Integer getTeamMemInsChkV2(String memNo){
        return teamProc.pDallaTeamMemInsChkV2(memNo);
    }



}
