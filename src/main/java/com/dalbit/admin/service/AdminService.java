package com.dalbit.admin.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.util.AdminSocketUtil;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.P_RoomForceExitInputVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class AdminService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AdminSocketUtil adminSocketUtil;
    @Autowired
    AdminCommonService adminCommonService;

    private final String menuJsonKey = "adminMenu";


    /*public String authCheck(HttpServletRequest request, SearchVo searchVo) throws GlobalException {
        String authToken = request.getHeader(DalbitUtil.getProperty("sso.header.cookie.name"));
        if (jwtUtil.validateToken(authToken)) {
            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(authToken);
            tokenVo.getMemNo();

            searchVo.setMem_no(tokenVo.getMemNo());
            ArrayList<AdminMenuVo> menuList = adminDao.selectMobileAdminMenuAuth(searchVo);
            if (DalbitUtil.isEmpty(menuList)) {
                return gsonUtil.toJson(new JsonOutputVo(Status.관리자메뉴조회_권한없음));
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.조회, menuList));

        }

        return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류));
    }*/

    /**
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    public String selectBroadcastList(HttpServletRequest request, SearchVo searchVo) {
        searchVo.setPagingInfo();
        List<BroadcastVo> broadList = adminDao.selectBroadcastList(searchVo);

        if(searchVo.getPageCount() < broadList.size()){
            searchVo.setEndPage(false);
            broadList = broadList.subList(0, searchVo.getPageCount());
        }else{
            searchVo.setEndPage(true);
        }

        var map = new HashMap<>();
        map.put("isEndPage", searchVo.isEndPage());
        map.put("broadList", broadList);
        map.put(menuJsonKey, adminCommonService.getAdminMenuInSession(request));

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    /**
     * 생방송관리 > 강제종료
     */
    public String roomForceExit(P_RoomForceExitInputVo pRoomForceExitInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomForceExitInputVo);
        //방 나가기 처리
        adminDao.callBroadcastRoomExit(procedureVo);
        if (Status.방송강제종료_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_회원아님));

        } else if (Status.방송강제종료_존재하지않는방.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_존재하지않는방));

        } else if (Status.방송강제종료_이미종료된방.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_이미종료된방));
        }

        //회원 나가기 처리
        adminDao.updateBroadcastMemberExit(pRoomForceExitInputVo);

        //방 종료 처리
        adminDao.updateBroadcastExit(new BroadcastExitVo(pRoomForceExitInputVo.getRoom_no(), pRoomForceExitInputVo.getStart_date()));

        //소캣 알림
        HashMap<String, Object> param = new HashMap<>();
        param.put("roomNo", pRoomForceExitInputVo.getRoom_no());
        param.put("memNo", pRoomForceExitInputVo.getMem_no());

        //option
        param.put("ctrlRole", "");
        param.put("recvMemNo", "roomOut");
        param.put("recvType", "chat");
        param.put("recvPosition", "chat");
        param.put("recvLevel", 0);
        param.put("recvTime", 0);

        adminSocketUtil.setSocket(param, "chatEnd", "roomOut", jwtUtil.generateToken(pRoomForceExitInputVo.getMem_no(), true));

        return gsonUtil.toJson(new JsonOutputVo(Status.방송강제종료_성공));
    }

    /**
     * - 이미지관리 > 프로필 이미지 조회
     * - 텍스트관리 > 닉네임 조회
     */
    public String selectProfileList(HttpServletRequest request, ProfileVo profileVo) {
        profileVo.setPagingInfo();
        List<ProfileVo> profileList = adminDao.selectProfileList(profileVo);

        if(profileVo.getPageCount() < profileList.size()){
            profileVo.setEndPage(false);
            profileList = profileList.subList(0, profileVo.getPageCount());
        }else{
            profileVo.setEndPage(true);
        }

        var map = new HashMap<>();
        map.put("isEndPage", profileVo.isEndPage());
        map.put("profileList", profileList);
        map.put(menuJsonKey, adminCommonService.getAdminMenuInSession(request));

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    /**
     * 이미지관리 > 프로필 이미지 초기화
     */
    public String proImageInit(HttpServletRequest request, ProImageInitVo proImageInitVo) {
        proImageInitVo.setOp_name("모바일운영자");
        proImageInitVo.setType(0);
        proImageInitVo.setEdit_contents("프로필이미지 변경 : " + proImageInitVo.getImage_profile() + " >> " + proImageInitVo.getReset_image_profile());

        // rd_data.tb_member_profile_edit_history에 insert
         adminDao.insertProfileHistory(proImageInitVo);

        // rd_data.tb_member_profile에 image_profile update
        int result = adminDao.proImageInit(proImageInitVo);
        if (result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지초기화_성공));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지초기화_실패));
        }
    }

    /**
     * 이미지관리 > 방송방 이미지 초기화
     */
    public String broImageInit(HttpServletRequest request, BroImageInitVo broImageInitVo){

        broImageInitVo.setOp_name(MemberVo.getMyMemNo(request));
        broImageInitVo.setEdit_contents("방송방 이미지 변경 : " + broImageInitVo.getImage_background() + " >> " + broImageInitVo.getReset_image_background());

        // rd_data.tb_broadcast_room_edit_history에 insert
        adminDao.insertBroadHistory(broImageInitVo);

        // rd_data.tb_broadcast_room에 image_background update
        int result = adminDao.broImageInit(broImageInitVo);
        if (result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방이미지초기화_성공));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방이미지초기화_실패));
        }
    }

    /**
     * 텍스트관리 > 닉네임 초기화
     */
    public String nickTextInit(HttpServletRequest request, NickTextInitVo nickTextInitVo, ProImageInitVo proImageInitVo) {
        proImageInitVo.setOp_name(MemberVo.getMyMemNo(request));
        proImageInitVo.setType(0);
        proImageInitVo.setEdit_contents("닉네임 변경 : " + nickTextInitVo.getMem_nick() + " >> " + nickTextInitVo.getMem_userid());
        proImageInitVo.setMem_no(nickTextInitVo.getMem_no());

        // rd_data.tb_member_profile_edit_history에 insert
        adminDao.insertProfileHistory(proImageInitVo);

        // rd_data.tb_member_basic에 mem_nick update
        int result = adminDao.nickTextInit(nickTextInitVo);
        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임초기화_성공));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임초기화_실패));
        }
    }

    /**
     * 텍스트관리 > 방송 제목 초기화
     */
    public String broTitleTextInit(HttpServletRequest request, BroTitleTextInitVo broTitleTextInitVo, BroImageInitVo broImageInitVo) {
        broImageInitVo.setOp_name(MemberVo.getMyMemNo(request));
        broImageInitVo.setEdit_contents("제목변경 : " + broTitleTextInitVo.getTitle() + " >> " + broTitleTextInitVo.getMem_nick() + "님의 방송입니다.");
        broImageInitVo.setRoom_no(broTitleTextInitVo.getRoom_no());

        // rd_data.tb_broadcast_room_edit_history에 insert
        adminDao.insertBroadHistory(broImageInitVo);

        // rd_data.tb_broadcast_room에 title update
        int result = adminDao.broTitleTextInit(broTitleTextInitVo);
        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송제목초기화_성공));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송제목초기화_실패));
        }
    }

}
