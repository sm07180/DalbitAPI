package com.dalbit.broadcast.service;

import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.LiveChatProfileVo;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.GuestInfoVo;
import com.dalbit.broadcast.vo.RoomMemberOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    ProfileDao profileDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonService commonService;
    @Autowired
    SocketService socketService;
    @Autowired
    GuestService guestService;
    @Autowired
    AdminService adminService;
    @Autowired
    RoomService roomService;

    public P_RoomInfoViewVo getRoomInfo(P_RoomInfoViewVo pRoomInfoViewVo){
        ProcedureVo procedureVo = new ProcedureVo(pRoomInfoViewVo);
        pRoomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        return pRoomInfoViewVo;
    }


    /**
     * 방송방 참여자 리스트
     */
    public String callBroadCastRoomMemberList(P_RoomMemberListVo pRoomMemberListVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberListVo);
        List<P_RoomMemberListVo> roomMemberVoList = userDao.callBroadCastRoomMemberList(procedureVo);

        HashMap roomMemberList = new HashMap();

        if(DalbitUtil.isEmpty(roomMemberVoList)){
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            roomMemberList.put("totalMemCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
            roomMemberList.put("noMemCnt", DalbitUtil.getIntMap(resultMap, "noMemCnt"));
            roomMemberList.put("list", new ArrayList<>());
            DeviceVo deviceVo = new DeviceVo(request);
            if(deviceVo.getOs() == 2){
                roomMemberList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomMemberListVo.getPageNo(), pRoomMemberListVo.getPageCnt()));
            }else{
                roomMemberList.put("paging", new PagingVo(0, pRoomMemberListVo.getPageNo(), pRoomMemberListVo.getPageCnt()));
            }

            if(Status.방송참여자리스트_참여자아님.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_참여자아님));
            }else if(Status.방송참여자리스트_회원아님.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_회원아님));
            }else if(Status.방송참여자리스트_방없음.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_방없음));
            } else {
                return gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트없음, roomMemberList));
            }

        }

        List<RoomMemberOutVo> outVoList = new ArrayList<>();
        for (int i = 0; i< roomMemberVoList.size(); i++){
            outVoList.add(new RoomMemberOutVo(roomMemberVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        roomMemberList.put("list", procedureOutputVo.getOutputBox());
        roomMemberList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomMemberListVo.getPageNo(), pRoomMemberListVo.getPageCnt()));

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        roomMemberList.put("totalMemCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
        roomMemberList.put("noMemCnt", DalbitUtil.getIntMap(resultMap, "noMemCnt"));

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_조회, roomMemberList));
        }else if(Status.방송참여자리스트_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_회원아님));
        }else if(Status.방송참여자리스트_방없음.getMessageCode().equals(procedureVo.getRet())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트_방없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여자리스트조회_실패));
        }

        return result;
    }


    /**
     * 방송방 강퇴하기
     */
    public String callBroadCastRoomKickout(P_RoomKickoutVo pRoomKickoutVo, HttpServletRequest request) {
        boolean isGuest = false;
        P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
        pRoomMemberInfoVo.setTarget_mem_no(pRoomKickoutVo.getBlocked_mem_no());
        pRoomMemberInfoVo.setRoom_no(pRoomKickoutVo.getRoom_no());
        pRoomMemberInfoVo.setMem_no(pRoomKickoutVo.getMem_no());
        ProcedureVo procedureInfoVo = roomService.getBroadCastRoomMemberInfo(pRoomMemberInfoVo, request);
        if(!DalbitUtil.isEmpty(procedureInfoVo.getData())){
            isGuest = DalbitUtil.getBooleanMap((HashMap)procedureInfoVo.getData(), "isGuest");
        }

        ProcedureVo procedureVo = new ProcedureVo(pRoomKickoutVo);
        HashMap bolckedMap = socketService.getMyInfo(pRoomKickoutVo.getBlocked_mem_no());
        userDao.callBroadCastRoomKickout(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        HashMap fanRankMap = commonService.getKingFanRankList(pRoomKickoutVo.getRoom_no(), request);
        returnMap.put("fanRank", fanRankMap.get("list"));
        returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
        returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
        returnMap.put("kingGender", fanRankMap.get("kingGender"));
        returnMap.put("kingAge", fanRankMap.get("kingAge"));
        returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

        String result = "";
        if(procedureVo.getRet().equals(Status.강제퇴장.getMessageCode())){
            SocketVo vo = socketService.getSocketVo(pRoomKickoutVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                socketService.kickout(pRoomKickoutVo.getRoom_no(), MemberVo.getMyMemNo(request), pRoomKickoutVo.getBlocked_mem_no(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, bolckedMap);
                vo.resetData();

                if(isGuest){
                    //게스트 강퇴 시 소켓 추가
                    GuestInfoVo guestInfoVo = new GuestInfoVo();
                    guestInfoVo.setMode(6);
                    guestInfoVo.setMemNo(pRoomKickoutVo.getBlocked_mem_no());
                    LiveChatProfileVo profileVo = new LiveChatProfileVo();
                    profileVo.setMem_no(pRoomKickoutVo.getBlocked_mem_no());
                    profileVo = adminService.getUserProfile(profileVo);
                    guestInfoVo.setNickNm(profileVo.getMem_nick());
                    guestInfoVo.setProfImg(new ImageVo(profileVo.getImage_profile(), profileVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));

                    socketService.sendGuest(pRoomKickoutVo.getBlocked_mem_no(), pRoomKickoutVo.getRoom_no(), pRoomKickoutVo.getMem_no(), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                    vo.resetData();
                }

            }catch(Exception e){
                log.info("Socket Service kickout Exception {}", e);
            }

            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
                socketMap.put("fanRank", returnMap.get("fanRank"));
                socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                socketService.changeCount(pRoomKickoutVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장, returnMap));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_회원아님));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_해당방이없음));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_방이종료되었음));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_요청회원_방소속회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_요청회원_방소속회원아님));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_권한없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_권한없음));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_대상회원_방소속회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_대상회원_방소속회원아님));
        /*}else if(procedureVo.getRet().equals(Status.강제퇴장_게스트이상불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_게스트이상불가));*/
        }else if(procedureVo.getRet().equals(Status.강제퇴장_매니저가매니저.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_매니저가매니저));
        }else if(procedureVo.getRet().equals(Status.강제퇴장_운영자.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_운영자));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_실패));
        }

        return result;
    }


    /**
     * 정보 조회(방송생성, 참여 시)
     */
    public String callMemberInfo(P_ProfileInfoVo pProfileInfo) {
        ProcedureVo procedureVo = new ProcedureVo(pProfileInfo);
        profileDao.callMemberInfo(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            P_ProfileInfoVo profileInfo = new Gson().fromJson(procedureVo.getExt(), P_ProfileInfoVo.class);
            profileInfo.setMem_no(pProfileInfo.getTarget_mem_no());
            ProfileInfoOutVo profileInfoOutVo = new ProfileInfoOutVo(profileInfo, pProfileInfo.getTarget_mem_no(), pProfileInfo.getTarget_mem_no(), null);

            HashMap returnMap = new HashMap();
            returnMap.put("level", profileInfoOutVo.getLevel());
            returnMap.put("exp", profileInfoOutVo.getExp());
            returnMap.put("expBegin", profileInfoOutVo.getExpBegin());
            returnMap.put("expNext", profileInfoOutVo.getExpNext());
            returnMap.put("grade", profileInfoOutVo.getGrade());
            returnMap.put("dalCnt", profileInfoOutVo.getDalCnt());
            returnMap.put("byeolCnt", profileInfoOutVo.getByeolCnt());
            returnMap.put("expRate", DalbitUtil.getExpRate(profileInfoOutVo.getExp(), profileInfoOutVo.getExpBegin(), profileInfoOutVo.getExpNext()));

            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_성공, returnMap));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_회원아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_대상아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_차단회원불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_차단회원불가));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_실패));
        }
        return result;
    }


    /**
     * 매니저지정
     */
    public String callBroadCastRoomManagerAdd(P_ManagerAddVo pManagerAddVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pManagerAddVo);
        userDao.callBroadCastRoomManagerAdd(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if (procedureVo.getRet().equals(Status.매니저지정_성공.getMessageCode())) {
            SocketVo vo = socketService.getSocketVo(pManagerAddVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                socketService.changeManager(pManagerAddVo.getRoom_no(), MemberVo.getMyMemNo(request), pManagerAddVo.getManager_mem_no(), true, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeManager Exception {}", e);
            }
            if(pManagerAddVo.getManager_type() == 2) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저지정_성공));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_성공));
            }
        } else if (procedureVo.getRet().equals(Status.매니저지정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_회원아님));
        } else if (procedureVo.getRet().equals(Status.매니저지정_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_해당방이없음));
        } else if (procedureVo.getRet().equals(Status.매니저지정_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_방이종료되었음));
        } else if (procedureVo.getRet().equals(Status.매니저지정_요청회원_방소속아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_요청회원_방소속아님));
        } else if (procedureVo.getRet().equals(Status.매니저지정_요청회원_방장아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_요청회원_방장아님));
        } else if (procedureVo.getRet().equals(Status.매니저지정_대상회원아이디_방소속아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_대상회원아이디_방소속아님));
        } else if (procedureVo.getRet().equals(Status.매니저지정_불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_불가));
        } else if (procedureVo.getRet().equals(Status.매니저지정_인원제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_인원제한));
        } else if (procedureVo.getRet().equals(Status.고정매니저지정_인원제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저지정_인원제한));
        } else if (procedureVo.getRet().equals(Status.고정매니저지정_이미지정.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저지정_이미지정));
        } else if (procedureVo.getRet().equals(Status.매니저지정_구분타입_오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_구분타입_오류));
        } else if (procedureVo.getRet().equals(Status.매니저지정_관리자.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_관리자));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저지정_실패));
        }

        return result;
    }


    /**
     * 매니저취소
     */
    public String callBroadCastRoomManagerDel(P_ManagerDelVo pManagerDelVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pManagerDelVo);
        userDao.callBroadCastRoomManagerDel(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if (procedureVo.getRet().equals(Status.매니저취소_성공.getMessageCode())) {
            SocketVo vo = socketService.getSocketVo(pManagerDelVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                socketService.changeManager(pManagerDelVo.getRoom_no(), MemberVo.getMyMemNo(request), pManagerDelVo.getManager_mem_no(), false, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeManager Exception {}", e);
            }
            if(pManagerDelVo.getManager_type() == 2){
                result = gsonUtil.toJson(new JsonOutputVo(Status.고정매니저취소_성공));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_성공));
            }
        } else if (procedureVo.getRet().equals(Status.매니저취소_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_회원아님));
        } else if (procedureVo.getRet().equals(Status.매니저취소_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_해당방이없음));
        } else if (procedureVo.getRet().equals(Status.매니저취소_방이종료되었음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_방이종료되었음));
        } else if (procedureVo.getRet().equals(Status.매니저취소_요청회원_방소속아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_요청회원_방소속아님));
        } else if (procedureVo.getRet().equals(Status.매니저취소_요청회원_방장아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_요청회원_방장아님));
        } else if (procedureVo.getRet().equals(Status.매니저취소_대상회원아이디_방소속아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_대상회원아이디_방소속아님));
        } else if (procedureVo.getRet().equals(Status.매니저취소_매니저아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_매니저아님));
        } else if (procedureVo.getRet().equals(Status.매니저취소_구분타입_오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_구분타입_오류));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.매니저취소_실패));
        }

        return result;
    }

    /**
     * 방송방 팬 등록
     */
    public String callFanstarInsert(P_BroadFanstarInsertVo pBroadFanstarInsertVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadFanstarInsertVo);
        userDao.callFanstarInsert(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.팬등록성공.getMessageCode())) {

            //방 정보 조회
            P_RoomInfoViewVo apiData = new P_RoomInfoViewVo();
            apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
            apiData.setMem_no(pBroadFanstarInsertVo.getStar_mem_no());
            apiData.setRoom_no(pBroadFanstarInsertVo.getRoom_no());

            P_RoomInfoViewVo roomInfoVo = getRoomInfo(apiData);
            if(!DalbitUtil.isEmpty(roomInfoVo.getBj_mem_no()) && pBroadFanstarInsertVo.getStar_mem_no().equals(roomInfoVo.getBj_mem_no())){
                SocketVo vo = socketService.getSocketVo(pBroadFanstarInsertVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                try{
                    HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                    if(DalbitUtil.getIntMap(resultMap, "newFanCnt") > 0){
                        HashMap fanRankMap = commonService.getKingFanRankList(pBroadFanstarInsertVo.getRoom_no(), request);
                        HashMap socketMap = new HashMap();
                        socketMap.put("likes",  DalbitUtil.getIntMap(resultMap, "countGood"));
                        socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
                        socketMap.put("fanRank", fanRankMap.get("list"));
                        socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                        socketService.changeCount(pBroadFanstarInsertVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                    }

                    socketService.addFan(pBroadFanstarInsertVo.getRoom_no(), MemberVo.getMyMemNo(request), roomInfoVo.getBj_mem_no(), DalbitUtil.getAuthToken(request), "1", DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                }catch(Exception e){
                    log.info("Socket Service addFan Exception {}", e);
                }
                log.info("Bj 팬등록 확인 {}", pBroadFanstarInsertVo.getStar_mem_no().equals(roomInfoVo.getBj_mem_no()));
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록성공));
        } else if(procedureVo.getRet().equals(Status.팬등록_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_회원아님));
        } else if(procedureVo.getRet().equals(Status.팬등록_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_스타회원번호이상));
        } else if(procedureVo.getRet().equals(Status.팬등록_이미팬등록됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_이미팬등록됨));
        } else if(procedureVo.getRet().equals(Status.팬등록_본인불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_본인불가));
        } else if(procedureVo.getRet().equals(Status.팬등록_차단회원불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록_차단회원불가));
        } else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬등록실패));
        }
        return result;
    }


    /**
     * 방송방 팬 해제
     */
    public String callFanstarDelete(P_BroadFanstarDeleteVo pBroadFanstarDeleteVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pBroadFanstarDeleteVo);
        userDao.callFanstarDelete(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if (procedureVo.getRet().equals(Status.팬해제성공.getMessageCode())) {
            //방 정보 조회
            P_RoomInfoViewVo apiData = new P_RoomInfoViewVo();
            apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
            apiData.setMem_no(pBroadFanstarDeleteVo.getStar_mem_no());
            apiData.setRoom_no(pBroadFanstarDeleteVo.getRoom_no());

            P_RoomInfoViewVo roomInfoVo = getRoomInfo(apiData);
            if(!DalbitUtil.isEmpty(roomInfoVo.getBj_mem_no()) && pBroadFanstarDeleteVo.getStar_mem_no().equals(roomInfoVo.getBj_mem_no())){
                SocketVo vo = socketService.getSocketVo(pBroadFanstarDeleteVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                try{
                    socketService.addFan(pBroadFanstarDeleteVo.getRoom_no(), MemberVo.getMyMemNo(request), roomInfoVo.getBj_mem_no(), DalbitUtil.getAuthToken(request), "0", DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                }catch(Exception e){
                    log.info("Socket Service addFan Exception {}", e);
                }
                log.info("Bj 팬해재 확인 {}", pBroadFanstarDeleteVo.getStar_mem_no().equals(roomInfoVo.getBj_mem_no()));
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제성공));
        } else if (procedureVo.getRet().equals(Status.팬해제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬해제_스타회원번호이상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제_스타회원번호이상));
        } else if (procedureVo.getRet().equals(Status.팬해제_팬아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제_팬아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬해제실패));
        }
        return result;
    }

    /** 현재 roomNo의 청취자 리스트 */
    public List<P_RoomMemberListVo> getListenerList(String roomNo, String memNo) {
        try {
            P_RoomMemberListVo apiData = new P_RoomMemberListVo();
            apiData.setMem_no(memNo);
            apiData.setRoom_no(roomNo);
            apiData.setPageNo(1);
            apiData.setPageCnt(11);
            ProcedureVo procedureVo = new ProcedureVo(apiData);

            List<P_RoomMemberListVo> resultList = userDao.callBroadCastRoomMemberList(procedureVo);

            if(resultList == null){
                resultList = new ArrayList<>();
                log.error("UserService getListenerList => DB result null");
            }

            return resultList;
        } catch (Exception e) {
            log.error("UserService getListenerList => {}", e);
        }
        return null;
    }
}
