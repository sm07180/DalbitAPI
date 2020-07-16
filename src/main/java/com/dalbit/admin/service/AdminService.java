package com.dalbit.admin.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.util.AdminSocketUtil;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.broadcast.vo.request.RoomListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.PushService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_pushInsertVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class AdminService {

    @Autowired
    AdminDao adminDao;
    @Autowired
    RoomDao roomDao;

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

    @Autowired
    PushService pushService;

    @Autowired
    SocketService socketService;

    private final String menuJsonKey = "adminMenu";

    @Value("${ant.expire.hour}")
    private int ANT_EXPIRE_HOUR;

    @Value("${server.ant.url}")
    private String ANT_SERVER_URL;

    @Value("${ant.app.name}")
    private String ANT_APP_NAME;

    @Value("${server.api.url}")
    private String SERVER_API_URL;

    private String PROFILE_DEFAULT_IMAGE = "/profile_3/profile_n_200327.jpg";

    public String authCheck(HttpServletRequest request, SearchVo searchVo) throws GlobalException {

        var resultMap = new HashMap();

        try{
            String mem_no = MemberVo.getMyMemNo(request);
            if(DalbitUtil.isEmpty(mem_no)){
                resultMap.put("isAdmin", false);
                return gsonUtil.toJson(new JsonOutputVo(Status.로그인오류, resultMap));
            }

            searchVo.setMem_no(mem_no);
            ArrayList<AdminMenuVo> menuList = adminDao.selectMobileAdminMenuAuth(searchVo);
            if (DalbitUtil.isEmpty(menuList)) {
                resultMap.put("isAdmin", false);
                return gsonUtil.toJson(new JsonOutputVo(Status.관리자메뉴조회_권한없음, resultMap));
            }

            resultMap.put("isAdmin", true);
            return gsonUtil.toJson(new JsonOutputVo(Status.관리자로그인성공, resultMap));

        }catch (Exception e){
            resultMap.put("isAdmin", false);
            return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, resultMap));
        }

    }

    public String selectAdminMenu(HttpServletRequest request){

        var map = new HashMap<>();
        var searchVo = new SearchVo();
        searchVo.setMem_no(MemberVo.getMyMemNo(request));

        List<AdminMenuVo> adminMenuList = adminCommonService.getAdminMenuInSession(request);

        if(DalbitUtil.isEmpty(adminMenuList) || 0 == adminMenuList.size()) {
            return gsonUtil.toJson(new JsonOutputVo(Status.관리자메뉴조회_권한없음));
        }
        map.put(menuJsonKey, adminCommonService.getAdminMenuInSession(request));
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    /**
     * - 이미지관리 > 방송방 이미지 조회
     * - 생방송관리
     * - 텍스트관리 > 방송 제목 조회
     */
    public String selectBroadcastList(HttpServletRequest request, BroadcastVo broadcastVo) {
        broadcastVo.setPagingInfo();
        List<BroadcastVo> broadList = adminDao.selectBroadcastList(broadcastVo);

        if(broadcastVo.getPageCount() < broadList.size()){
            broadcastVo.setEndPage(false);
            broadList = broadList.subList(0, broadcastVo.getPageCount());
        }else{
            broadcastVo.setEndPage(true);
        }

        var map = new HashMap<>();
        map.put("isEndPage", broadcastVo.isEndPage());
        map.put("broadList", broadList);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    public String selectBroadcastDetail(SearchVo searchVo){
        BroadcastDetailVo broadInfo = adminDao.selectBroadcastSimpleInfo(searchVo);
        if(broadInfo != null && !DalbitUtil.isEmpty(broadInfo.getBjStreamId())){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, ANT_EXPIRE_HOUR);
            long expire = cal.getTime().getTime() / 1000;
            String params = "expireDate=" + expire + "&type=play";
            OkHttpClientUtil httpUtil = new OkHttpClientUtil();
            try{
                String url = ANT_SERVER_URL + "/" + ANT_APP_NAME + "/rest/v2/broadcasts/" + broadInfo.getBjStreamId() + "/token?" + params;
                log.info("[Ant] Request URL : {}", url );
                Response res = httpUtil.sendGet(url);
                if(res != null){
                    String strResBody = res.body().string();
                    if(!DalbitUtil.isEmpty(strResBody)) {
                        HashMap tokenMap = new Gson().fromJson(strResBody, HashMap.class);
                        if (tokenMap != null && !DalbitUtil.isEmpty(tokenMap.get("tokenId"))) {
                            broadInfo.setBjPlayToken(DalbitUtil.getStringMap(tokenMap, "tokenId"));
                            broadInfo.setAntUrl(DalbitUtil.getProperty("server.ant.edge.url"));
                            broadInfo.setAntAppName(ANT_APP_NAME);
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, broadInfo));
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

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    /**
     * 이미지관리 > 프로필 이미지 초기화
     */
    @Transactional
    public String proImageInit(HttpServletRequest request, ProImageInitVo proImageInitVo) throws GlobalException {

        try{

            proImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            proImageInitVo.setType(0);
            proImageInitVo.setEdit_contents("프로필이미지 변경 : " + proImageInitVo.getImage_profile() + " >> " + PROFILE_DEFAULT_IMAGE);

            // rd_data.tb_member_profile_edit_history에 insert
            adminDao.insertProfileHistory(proImageInitVo);

            // rd_data.tb_member_profile에 image_profile update
            adminDao.proImageInit(proImageInitVo);

            // rd_data.tb_member_notification에 insert
            NotiInsertVo notiInsertVo = new NotiInsertVo();
            if(!DalbitUtil.isEmpty(proImageInitVo.getNotificationYn()) && proImageInitVo.getNotificationYn().equals("Y")) {

                try {
                    //알림(종) 표시
                    notiInsertVo.setMem_no(proImageInitVo.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents(proImageInitVo.getReport_title());
                    notiInsertVo.setNotiMemo(proImageInitVo.getReport_message());
                    adminDao.insertNotiHistory(notiInsertVo);
                } catch (Exception e) {
                    log.error("[모바일어드민] 알림 실패 - 프로필 이미지 초기화");
                }

                //PUSH 발송
                try {
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                    pPushInsertVo.setMem_nos(proImageInitVo.getMem_no());
                    pPushInsertVo.setSlct_push("35");
                    pPushInsertVo.setPush_slct("57");
                    pPushInsertVo.setSend_title("달빛 라이브 운영자 메시지");
                    pPushInsertVo.setSend_cont(proImageInitVo.getReport_title());
                    pPushInsertVo.setEtc_contents(proImageInitVo.getReport_message().replaceAll("\n", "<br>"));
                    pPushInsertVo.setImage_type("101");

                    pushService.sendPushReqOK(pPushInsertVo);
                } catch (Exception e) {
                    log.error("[모바일어드민] PUSH 발송 실패 - 프로필 이미지 초기화");
                }
            }

            try{
                // option
                HashMap<String, Object> param = new HashMap<>();
                param.put("memNo", proImageInitVo.getMem_no());
                param.put("memNk", "");
                param.put("ctrlRole", "ctrlRole");
                param.put("recvType", "chat");
                param.put("recvPosition", "chat");
                param.put("recvLevel", 0);
                param.put("recvTime", 0);
                String defaultGender = "n";
                // message set
                Gson gson = new Gson();
                HashMap<String,Object> tmp = new HashMap();
                if(proImageInitVo.getReset_image_profile().equals(PROFILE_DEFAULT_IMAGE)) {
                    tmp.put("image", new ImageVo("", defaultGender, DalbitUtil.getProperty("server.photo.url")).getUrl().replace(DalbitUtil.getProperty("server.photo.url"), "")); }tmp.put("sex", defaultGender);
                tmp.put("nk", proImageInitVo.getMem_nick());
                String message =  gson.toJson(tmp);
                adminSocketUtil.setSocket(param, "reqMyInfo", message, jwtUtil.generateToken(proImageInitVo.getMem_no(), true));
            }catch (Exception e){
                log.error("[모바일어드민] 소켓통신 실패 - 프로필 이미지 초기화");
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지초기화_성공));

        }catch (Exception e){
            throw new GlobalException(Status.프로필이미지초기화_실패, "프로필이미지초기화_실패");
        }


    }

    /**
     * 이미지관리 > 방송방 이미지 초기화
     */
    @Transactional
    public String broImageInit(HttpServletRequest request, BroImageInitVo broImageInitVo) throws GlobalException {

        try{
            broImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            broImageInitVo.setEdit_contents("방송방 이미지 변경 : " + broImageInitVo.getImage_background() + " >> " + broImageInitVo.getReset_image_background());

            // rd_data.tb_broadcast_room_edit_history에 insert
            adminDao.insertBroadHistory(broImageInitVo);

            // rd_data.tb_broadcast_room에 image_background update
            adminDao.broImageInit(broImageInitVo);

            // rd_data.tb_member_notification에 insert
//            NotiInsertVo notiInsertVo = new NotiInsertVo();
//            if(!DalbitUtil.isEmpty(broImageInitVo.getNotificationYn()) && broImageInitVo.getNotificationYn().equals("Y")) {
//                try {
//                    // 알림(종)표시
//                    notiInsertVo.setMem_no(broImageInitVo.getMem_no());
//                    notiInsertVo.setSlctType(7);
//                    notiInsertVo.setNotiContents(broImageInitVo.getReport_title());
//                    notiInsertVo.setNotiMemo(broImageInitVo.getReport_message());
//                    adminDao.insertNotiHistory(notiInsertVo);
//                } catch(Exception e) {
//                    log.error("[모바일어드민] 알림 실패 - 방송방 이미지 초기화");
//                }
//            }

            try{
                BroadInfoVo broadInfo = getBroadInfo(broImageInitVo.getRoom_no());

                HashMap roomInfo = new HashMap();
                roomInfo.put("roomType", broadInfo.getSubject_type());
                roomInfo.put("title", broadInfo.getTitle());
                roomInfo.put("welcomMsg", broadInfo.getMsg_welcom());
                roomInfo.put("bgImg", new ImageVo(broImageInitVo.getReset_image_background(), DalbitUtil.getProperty("server.photo.url")));
                roomInfo.put("bgImgRacy", DalbitUtil.isEmpty(broadInfo.getGrade_background()) ? 0 : broadInfo.getGrade_background());

                SocketVo socketVo = socketService.getSocketVo(broImageInitVo.getRoom_no(), broImageInitVo.getMem_no(), true);

                socketService.changeRoomInfo(broImageInitVo.getRoom_no(), broImageInitVo.getMem_no(), roomInfo, jwtUtil.generateToken(broImageInitVo.getMem_no()), true, socketVo);

            }catch (Exception e){
                log.error("[모바일어드민] 소켓통신 실패 - 방송방 이미지 초기화");
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.방송방이미지초기화_성공));

        }catch (Exception e){
            throw new GlobalException(Status.방송방이미지초기화_실패, "방송방이미지초기화_실패");
        }
    }

    /**
     * 텍스트관리 > 닉네임 초기화
     */
    @Transactional
    public String nickTextInit(HttpServletRequest request, NickTextInitVo nickTextInitVo, ProImageInitVo proImageInitVo) throws GlobalException {

        try {
            proImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            proImageInitVo.setType(0);
            proImageInitVo.setEdit_contents("닉네임 변경 : " + nickTextInitVo.getMem_nick() + " >> " + nickTextInitVo.getMem_userid());
            proImageInitVo.setMem_no(nickTextInitVo.getMem_no());

            // rd_data.tb_member_profile_edit_history에 insert
            adminDao.insertProfileHistory(proImageInitVo);

            // rd_data.tb_member_basic에 mem_nick update
            adminDao.nickTextInit(nickTextInitVo);

            // rd_data.tb_member_notification에 insert
            NotiInsertVo notiInsertVo = new NotiInsertVo();
            if(!DalbitUtil.isEmpty(nickTextInitVo.getNotificationYn()) && nickTextInitVo.getNotificationYn().equals("Y")) {
                try {
                    //알림(종) 표시
                    notiInsertVo.setMem_no(nickTextInitVo.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents(nickTextInitVo.getReport_title());
                    notiInsertVo.setNotiMemo(nickTextInitVo.getReport_message());
                    adminDao.insertNotiHistory(notiInsertVo);
                }catch (Exception e){
                    log.error("[모바일어드민] 알림 실패 - 닉네임 초기화");
                }

                //PUSH 발송
                try{
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                    pPushInsertVo.setMem_nos(nickTextInitVo.getMem_no());
                    pPushInsertVo.setSlct_push("35");
                    pPushInsertVo.setPush_slct("57");
                    pPushInsertVo.setSend_title("달빛 라이브 운영자 메시지");
                    pPushInsertVo.setSend_cont(nickTextInitVo.getReport_title());
                    pPushInsertVo.setEtc_contents(nickTextInitVo.getReport_message().replaceAll("\n", "<br>"));
                    pPushInsertVo.setImage_type("101");
                    pushService.sendPushReqOK(pPushInsertVo);
                }catch (Exception e){
                    log.error("[모바일어드민] PUSH 발송 실패 - 닉네임 초기화");
                }
            }

            try{

                //소켓통신에 필요한 회원정보 조회
                MemberInfoVo memberInfo = adminDao.getMemberInfo(nickTextInitVo.getMem_no());

                // option
                HashMap<String, Object> param = new HashMap<>();
                param.put("memNo", nickTextInitVo.getMem_no());
                param.put("memNk", "");
                param.put("ctrlRole", "ctrlRole");
                param.put("recvType", "chat");
                param.put("recvPosition", "chat");
                param.put("recvLevel", 0);
                param.put("recvTime", 0);

                String defaultGender = "n";

                // message set
                Gson gson = new Gson();
                HashMap<String,Object> tmp = new HashMap();
                tmp.put("image", memberInfo.getImage_profile());
                tmp.put("sex", defaultGender);
                tmp.put("nk", nickTextInitVo.getMem_userid());
                String message =  gson.toJson(tmp);
                adminSocketUtil.setSocket(param, "reqMyInfo", message, jwtUtil.generateToken(nickTextInitVo.getMem_no(), true));
            }catch (Exception e){
                log.error("[모바일어드민] 소켓통신 실패 - 닉네임 초기화");
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임초기화_성공));

        }catch(Exception e) {
            throw new GlobalException(Status.닉네임초기화_실패, "닉네임초기화_실패");
        }
    }

    /**
     * 텍스트관리 > 방송 제목 초기화
     */
    @Transactional
    public String broTitleTextInit(HttpServletRequest request, BroTitleTextInitVo broTitleTextInitVo, BroImageInitVo broImageInitVo) throws GlobalException {

        try {
            broImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            broImageInitVo.setEdit_contents("제목변경 : " + broTitleTextInitVo.getTitle() + " >> " + broTitleTextInitVo.getMem_nick() + "님의 방송입니다.");
            broImageInitVo.setRoom_no(broTitleTextInitVo.getRoom_no());

            // rd_data.tb_broadcast_room_edit_history에 insert
            adminDao.insertBroadHistory(broImageInitVo);

            // rd_data.tb_broadcast_room에 title update
            adminDao.broTitleTextInit(broTitleTextInitVo);

            // rd_data.tb_member_notification에 insert
//            NotiInsertVo notiInsertVo = new NotiInsertVo();
//            if(!DalbitUtil.isEmpty(broTitleTextInitVo.getNotificationYn()) && broTitleTextInitVo.getNotificationYn().equals("Y")) {
//                try{
//                    //알림(종) 표시
//                    notiInsertVo.setMem_no(broTitleTextInitVo.getMem_no());
//                    notiInsertVo.setSlctType(7);
//                    notiInsertVo.setNotiContents(broTitleTextInitVo.getReport_title());
//                    notiInsertVo.setNotiMemo(broTitleTextInitVo.getReport_message());
//                    adminDao.insertNotiHistory(notiInsertVo);
//                }catch (Exception e){
//                    log.error("[모바일어드민] 알림 실패 - 방송 제목 초기화");
//                }
//            }

            try{
                BroadInfoVo broadInfo = getBroadInfo(broTitleTextInitVo.getRoom_no());

                // message set
                HashMap roomInfo = new HashMap();
                roomInfo.put("roomType", broadInfo.getSubject_type());
                roomInfo.put("title", broTitleTextInitVo.getReset_title());
                roomInfo.put("welcomMsg", broadInfo.getMsg_welcom());
                roomInfo.put("bgImg", new ImageVo(broadInfo.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
                roomInfo.put("bgImgRacy", DalbitUtil.isEmpty(broadInfo.getGrade_background()) ? 0 : broadInfo.getGrade_background());

                SocketVo socketVo = socketService.getSocketVo(broTitleTextInitVo.getRoom_no(), broTitleTextInitVo.getMem_no(), true);

                socketService.changeRoomInfo(broTitleTextInitVo.getRoom_no(), broTitleTextInitVo.getMem_no(), roomInfo, jwtUtil.generateToken(broTitleTextInitVo.getMem_no()), true, socketVo);
            }catch (Exception e){
                log.error("[모바일어드민] 소켓통신 실패 - 방송 제목 초기화");
            }

            return gsonUtil.toJson(new JsonOutputVo(Status.방송제목초기화_성공));

        }catch(Exception e) {
            throw new GlobalException(Status.방송제목초기화_실패, "방송제목초기화_실패");
        }
    }

    /**
     * 신고하기
     */
    @Transactional
    public String declarationOperate(HttpServletRequest request, DeclarationVo declarationVo) throws GlobalException {

        try {
            declarationVo.setOpName(selectAdminName(MemberVo.getMyMemNo(request)));

            MemberInfoVo myInfo = getMemberInfo(MemberVo.getMyMemNo(request));
            log.info(myInfo.getMem_no());
            MemberInfoVo reportedInfo = getMemberInfo(declarationVo.getReported_mem_no());
            log.info(reportedInfo.getGrade());

            // 신고자
            declarationVo.setMem_no(myInfo.getMem_no());
            declarationVo.setMem_userid(myInfo.getMem_userid());
            declarationVo.setMem_nick(myInfo.getMem_nick());
            // 신고 대상자
            declarationVo.setReported_mem_no(reportedInfo.getMem_no());
            declarationVo.setReported_userid(reportedInfo.getMem_userid());
            declarationVo.setReported_nick(reportedInfo.getMem_nick());
            declarationVo.setReported_phone(reportedInfo.getMem_phone());
            declarationVo.setReported_level(reportedInfo.getLevel());
            declarationVo.setReported_grade(reportedInfo.getGrade());

            adminDao.declarationOperate(declarationVo);

            //회원상태 변경을 위한 VO 세팅
            DeclarationVo memberDeclarationVo = new DeclarationVo();
            memberDeclarationVo.setMem_no(reportedInfo.getMem_no());
            memberDeclarationVo.setOpCode(declarationVo.getOpCode());
            if(!DalbitUtil.isEmpty(memberDeclarationVo.getOpCode())){
                int opCode = memberDeclarationVo.getOpCode();
                if(opCode == 2) {
                    memberDeclarationVo.setState(2);
                } else if(opCode == 3 || opCode == 4 || opCode == 5) {
                    memberDeclarationVo.setState(3);
                    if(opCode == 3) {
                        memberDeclarationVo.setBlockDay(1);
                    } else if(opCode == 4) {
                        memberDeclarationVo.setBlockDay(3);
                    } else if(opCode == 5) {
                        memberDeclarationVo.setBlockDay(7);
                    }
                } else if(opCode == 6) {
                    memberDeclarationVo.setState(5);
                }
            }
            adminDao.updateState(memberDeclarationVo);

            //rd_data.tb_member_notification에 insert
            NotiInsertVo notiInsertVo = new NotiInsertVo();
            if(!DalbitUtil.isEmpty(declarationVo.getNotificationYn()) && declarationVo.getNotificationYn().equals("Y")) {
                try{
                    //알림(종) 표시
                    notiInsertVo.setMem_no(reportedInfo.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents(declarationVo.getNotiContents());
                    notiInsertVo.setNotiMemo(declarationVo.getNotiMemo());
                    adminDao.insertNotiHistory(notiInsertVo);
                }catch (Exception e){
                    log.error("[모바일어드민] 알림 실패 - 신고처리");
                }
                //PUSH 발송
                try{
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                    pPushInsertVo.setMem_nos(reportedInfo.getMem_no());
                    pPushInsertVo.setSlct_push("35");
                    pPushInsertVo.setSend_title("달빛 라이브 운영자 메시지");
                    pPushInsertVo.setSend_cont(declarationVo.getNotiContents());
                     pPushInsertVo.setEtc_contents(declarationVo.getNotiMemo().replaceAll("\n", "<br>"));
                    pPushInsertVo.setImage_type("101");
                    pushService.sendPushReqOK(pPushInsertVo);
                }catch (Exception e){
                    log.error("[모바일어드민] PUSH 발송 실패 - 신고처리");
                }
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.신고처리_성공));

            }catch (Exception e) {
               throw new GlobalException(Status.신고처리_에러, "신고처리_에러");
        }
    }

    /**
     * 회원 상세 조회
     */
    public MemberInfoVo getMemberInfo(String mem_no) {
        MemberInfoVo result = adminDao.getMemberInfo(mem_no);
        return result;
    }

    /**
     * 방송방 상세 조회
     */
    public BroadInfoVo getBroadInfo(String room_no) {
        BroadInfoVo result = adminDao.getBroadInfo(room_no);
        return result;
    }

    /**
     * 생방송관리 > 채팅 내역 가져오기
     */
    @Transactional(readOnly = true)
    public String callBroadcastLiveChatInfo(LiveChatInputVo liveChatInputVo) {
        String result;

        try{
            ArrayList<LiveChatOutputVo> liveChatList = adminDao.selectBroadcastLiveChatInfo(liveChatInputVo);

            if(DalbitUtil.isEmpty(liveChatList)) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.생방송메시지조회_성공_데이터없음));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.생방송메시지조회_성공, liveChatList));
            }
        }catch (Exception e){
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방송메시지조회_실패));
        }
        return result;
    }

    /**
     * 생방송관리 > 프로필 상세 창 띄우기
     */
    public String getLiveChatProfile(LiveChatProfileVo liveChatProfileVo) {
        LiveChatProfileVo profile = adminDao.getLiveChatProfile(liveChatProfileVo);
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, profile));

        return result;
    }

    /**
     * 생방송관리 > 강제퇴장
     */
    public String forcedOut(HttpServletRequest request, ForcedOutVo forcedOutVo) {

        forcedOutVo.setOpName(selectAdminName(MemberVo.getMyMemNo(request)));
        String sendNoti = "0";
        if(!DalbitUtil.isEmpty(forcedOutVo.getNotificationYn()) && forcedOutVo.getNotificationYn().equals("Y")){
            sendNoti = "1";
            forcedOutVo.setNotiContents(forcedOutVo.getReport_title());
            forcedOutVo.setNotiMemo(forcedOutVo.getReport_message());
            //PUSH 발송
            try{
                P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                pPushInsertVo.setMem_nos(forcedOutVo.getMem_no());
                pPushInsertVo.setSlct_push("35");
                pPushInsertVo.setSend_title("달빛 라이브 운영자 메시지");
                pPushInsertVo.setSend_cont(forcedOutVo.getReport_title());
                pPushInsertVo.setEtc_contents(forcedOutVo.getReport_message().replaceAll("\n", "<br>"));
                pPushInsertVo.setImage_type("101");
                pushService.sendPushReqOK(pPushInsertVo);
            }catch (Exception e){
                log.error("[모바일어드민] PUSH 발송 실패 - 강제 퇴장");
            }
        }
        forcedOutVo.setSendNoti(sendNoti);
        ProcedureVo procedureVo = new ProcedureVo(forcedOutVo);
        adminDao.callForceLeave(procedureVo);

        //방송정보 조회
        SearchVo searchVo = new SearchVo();
        searchVo.setRoom_no(forcedOutVo.getRoom_no());
        BroadcastDetailVo broadInfo = adminDao.selectBroadcastSimpleInfo(searchVo);

        // 재입장 불가능하도록
        forcedOutVo.setDj_mem_no(broadInfo.getMem_no());
        adminDao.insertForceLeave_roomBlock(forcedOutVo);
        String result = "";
        if(Status.생방청취자강제퇴장_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방청취자강제퇴장_성공));

            //청취자 강제 퇴장
            HashMap<String,Object> param = new HashMap<>();
            param.put("roomNo", broadInfo.getRoom_no());
            param.put("target_memNo", forcedOutVo.getMem_no());
            param.put("target_nickName", forcedOutVo.getMem_nick());
            param.put("memNo", broadInfo.getMem_no());
            param.put("nickName", broadInfo.getNickNm());
            // option
            param.put("ctrlRole","ctrlRole");
            param.put("revMemNo", forcedOutVo.getMem_no());     // 받는 사람
            param.put("recvType","system");
            param.put("recvPosition","top1");
            param.put("recvLevel",2);
            param.put("recvTime",1);

            // message set
            Gson gson = new Gson();
            HashMap<String,Object> tmp = new HashMap();
            tmp.put("revMemNo", forcedOutVo.getMem_no());     // 받는 사람
            tmp.put("revMemNk", forcedOutVo.getMem_nick());
            tmp.put("sndAuth", 4);
            tmp.put("sndMemNo", broadInfo.getMem_no());            // 보낸 사람
            tmp.put("sndMemNk", broadInfo.getNickNm());
            String message =  gson.toJson(tmp);

            adminSocketUtil.setSocket(param,"reqKickOut", message,jwtUtil.generateToken(forcedOutVo.getMem_no(), true));

        }else if(Status.생방청취자강제퇴장_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방청취자강제퇴장_회원아님));
        } else if(Status.생방청취자강제퇴장_방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방청취자강제퇴장_방없음));
        } else if(Status.생방청취자강제퇴장_종료된방.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방청취자강제퇴장_종료된방));
        } else if(Status.생방청취자강제퇴장_청취자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방청취자강제퇴장_청취자아님));
        } else if(Status.생방청취자강제퇴장_퇴장한회원.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.생방청취자강제퇴장_퇴장한회원));
        }

        return result;

    }

    /**
     * 생방송관리 > 시스템메시지 등록
     */
    public String insertContentsMessageAdd(HttpServletRequest request, MessageInsertVo messageInsertVo) throws GlobalException {
        messageInsertVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
        String result="";

        try{
            // 방송중인 방송방 리스트 조회 및 발송 건수 셋팅
            if(!DalbitUtil.isEmpty(messageInsertVo.getSend_all()) && messageInsertVo.getSend_all().equals("0")) {       // ALL
                // 현재 방송방 조회하는
                RoomListVo pRoomListVo = new RoomListVo();
                pRoomListVo.setPage(1);
                pRoomListVo.setRecords(100);
                ProcedureVo roomListProcedureVo = new ProcedureVo(pRoomListVo);
                List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(roomListProcedureVo);

                String targetRooms = "";
                for (P_RoomListVo room : roomVoList) {
                    targetRooms += room.getRoomNo() + "|";
                }

                messageInsertVo.setSend_cnt(roomVoList.size() + "");
                messageInsertVo.setTarget_rooms(targetRooms.substring(0, targetRooms.length() - 1));
            }else{      // Target
                String[] array = messageInsertVo.getTarget_rooms().split("\\|");
                messageInsertVo.setSend_cnt(array.length + "");
            }

            if(messageInsertVo.getSend_cnt().equals("0")){
                return gsonUtil.toJson(new JsonOutputVo(Status.방송방메시지발송_타겟미지정));
            }

            messageInsertVo.setTitle("모바일 관리자 발송");
            int insertResult = adminDao.insertContentsMessageAdd(messageInsertVo);

            if(insertResult > 0){
                result = sendSplashApi(messageInsertVo);
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방메시지발송_에러));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(Status.방송방메시지발송_에러, "AdminService.insertContentsMessageAdd");
        }

        return result;
    }

    public String sendSplashApi(MessageInsertVo messageInsertVo) throws GlobalException {
        RequestBody formBody;
        String uri = "";

        if(DalbitUtil.isEmpty(messageInsertVo.getTarget_rooms())){
            formBody = new FormBody.Builder()
                    .add("message", messageInsertVo.getSend_cont())
                    .build();
            uri = "/socket/sendSystemMessage";
        }else {
            formBody = new FormBody.Builder()
                    .add("message", messageInsertVo.getSend_cont())
                    .add("targetRooms", messageInsertVo.getTarget_rooms())
                    .build();
            uri = "/socket/sendTargetSystemMessage";
        }

        OkHttpClientUtil okHttpClientUtil = new OkHttpClientUtil();

        try{
            String url = SERVER_API_URL + uri;
            Response response = okHttpClientUtil.sendPost(url, formBody);
            String inforexLoginResult = response.body().string();
            log.debug(inforexLoginResult);

            return gsonUtil.toJson(new JsonOutputVo(Status.방송방메시지발송_성공));
        }catch (IOException | GlobalException e){
            throw new GlobalException(Status.방송방메시지발송_에러, "AdminService.sendSplashApi");
        }
    }

    /**
     * 생방송관리 > 청취자 목록
     */
    public String selectLiveListener(HttpServletRequest request, ProfileVo profileVo) {
        profileVo.setPagingInfo();
        List<ProfileVo> listenerList = adminDao.selectLiveListener(profileVo);

        var map = new HashMap<>();
        map.put("listenerList", listenerList);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    /**
     * 통계 > 방송정보
     */
    public String callBroadcastTotal(P_StatVo pStatVo) {
        ProcedureVo procedureVo = new ProcedureVo(pStatVo);
        adminDao.callBroadcastTotal(procedureVo);

        P_BroadcastTotalOutVo totalInfo = new Gson().fromJson(procedureVo.getExt(), P_BroadcastTotalOutVo.class);

        var result = new HashMap<String, Object>();
        result.put("totalInfo", totalInfo);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result));
    }

    /**
     * 통계 > 현재 접속자
     */
    public String callUserCurrentTotal() {
        ProcedureVo procedureVo = new ProcedureVo();
        adminDao.callUserCurrentTotal(procedureVo);
        P_UserTotalOutDetailVo detailList = new Gson().fromJson(procedureVo.getExt(), P_UserTotalOutDetailVo.class);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, detailList));
    }

    /**
     * 통계 > 날짜 별 접속 현황
     */
    public String callUserDayTotal(P_StatVo pStatVo) {

        // 성별을 가져오기 위함
        ProcedureVo procedureVo = new ProcedureVo(pStatVo);
        adminDao.callLoginGender(procedureVo);
        P_LoginTotalOutVo genderInfo = new Gson().fromJson(procedureVo.getExt(), P_LoginTotalOutVo.class);

        // 연령대별을 가져오기 위함
        adminDao.callLoginAge(procedureVo);
        P_LoginAgeOutVo ageInfo = new Gson().fromJson(procedureVo.getExt(), P_LoginAgeOutVo.class);

        var result = new HashMap<String, Object>();
        result.put("genderInfo", genderInfo);
        result.put("ageInfo", ageInfo);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result));
    }

    /**
     * 통계 > 결제 현황
     */
    public String callPayInfo(P_StatVo pStatVo) {
        ProcedureVo procedureVo = new ProcedureVo(pStatVo);
        adminDao.callPayInfo(procedureVo);

        P_PayInfoOutVo info = new Gson().fromJson(procedureVo.getExt(), P_PayInfoOutVo.class);
        var result = new HashMap<String, Object>();

        result.put("info", info);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result));
    }

    /**
     * 1:1 문의 목록 조회
     */
    public String callQuestionList(P_QuestionListInputVo pQuestionListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pQuestionListInputVo);
        List<P_QuestionListOutputVo> questionList = adminDao.callQuestionList(procedureVo);

        var map = new HashMap<>();
        map.put("pagingInfo", new AdminProcedureBaseVo(procedureVo.getRet()));
        map.put("questionList", questionList);

        String result;
        if(-1 < Integer.parseInt(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의내역조회_성공, map));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의내역조회_실패));
        }

        return result;
    }

    /**
     * 1:1 문의 목록 상세 조회
     */
    public String callServiceCenterQnaDetail(P_QuestionDetailInputVo pQuestionDetailInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pQuestionDetailInputVo);
        adminDao.callServiceCenterQnaDetail(procedureVo);
        P_QuestionDetailOutputVo questionDetail = new Gson().fromJson(procedureVo.getExt(), P_QuestionDetailOutputVo.class);

        String result;

        if(Status.고객센터_문의상세조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의상세조회_성공, questionDetail));
        } else if(Status.고객센터_문의상세조회_문의번호없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의상세조회_문의번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의상세조회_에러));
        }

        return result;
    }

    /**
     * 1:1 문의 faq sub list
     */
    public List<FaqVo> selectFaqSubList(FaqVo faqVo) {
        return adminDao.selectFaqSubList(faqVo);
    }

    /**
     * 1:1 문의 처리
     */
    public String callServiceCenterQnaOperate(P_QuestionOperateVo pQuestionOperateVo) {
        pQuestionOperateVo.setOpName(selectAdminName(MemberVo.getMyMemNo()));
        P_QuestionDetailOutputVo outVo = adminDao.selectServiceCenterQnaState(pQuestionOperateVo);
        String result = "";
        if(outVo.getState() == 0){
            ProcedureVo procedureVo = new ProcedureVo(pQuestionOperateVo,true);
            adminDao.callServiceCenterQnaOperate(procedureVo);

            if(Status.일대일문의처리_성공.getMessageCode().equals(procedureVo.getRet())) {

                adminDao.callServiceCenterQnaDetail(procedureVo);
                P_QuestionDetailOutputVo questionDetail = new Gson().fromJson(procedureVo.getExt(), P_QuestionDetailOutputVo.class);

                try{    // PUSH 발송
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();

                    pPushInsertVo.setMem_nos(questionDetail.getMem_no());
                    pPushInsertVo.setSlct_push("37");
                    pPushInsertVo.setPush_slct("60");
                    pPushInsertVo.setSend_title("1:1문의 답변이 등록되었어요!");
                    pPushInsertVo.setSend_cont("등록한 1:1문의에 답변이 등록되었습니다.");
                    pPushInsertVo.setImage_type("101");
                    pushService.sendPushReqOK(pPushInsertVo);
                }catch (Exception e){
                    log.error("[PUSH 발송 실패 - 일대일문의처리]");
                }

                try{
                    //알림(종) 표시
                    NotiInsertVo notiInsertVo = new NotiInsertVo();

                    notiInsertVo.setMem_no(questionDetail.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents("등록한 1:1문의에 답변이 등록되었습니다.");
                    notiInsertVo.setNotiMemo("등록한 1:1문의에 답변이 등록되었습니다.");
                    adminDao.insertNotiHistory(notiInsertVo);

                }catch (Exception e){
                    log.error("[NOTI 발송 실패 - 일대일문의처리]");
                }

                result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의처리_성공));
            } else if(Status.일대일문의처리_문의번호없음.getMessageCode().equals((procedureVo.getRet()))) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의처리_문의번호없음));
            } else if(Status.일대일문의처리_이미처리됐음.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의처리_이미처리됐음));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의처리_에러));
            }
        }else{
            if(outVo.getState() == 1){
                int updateResult = adminDao.updateServiceCenterQnaUpdate(pQuestionOperateVo);
                if(updateResult == 1) {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의수정_성공));
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의처리_에러));
                }
            }else if(outVo.getState() == 2){
                result = gsonUtil.toJson(new JsonOutputVo(Status.일대일문의처리_이미_진행중));
            }
        }

        return result;
    }

    /**
     * 신고내역 목록 조회
     */
    public String callServiceCenterReportList(P_DeclarationListInputVo pDeclarationListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pDeclarationListInputVo);

        ArrayList<P_DeclarationListOutputVo> declarationList = adminDao.callServiceCenterReportList(procedureVo);

        var map = new HashMap<>();
        map.put("pagingInfo", new AdminProcedureBaseVo(procedureVo.getRet()));
        map.put("declarationList", declarationList);

        String result;

        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.신고목록조회_성공, map));
        } else if(Status.신고목록조회_데이터없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.신고목록조회_데이터없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.신고목록조회_에러));
        }

        return result;
    }

    /**
     * 신고내역 상세 조회
     */
    public String callServiceCenterReportDetail(P_DeclarationDetailInputVo pDeclarationDetailInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pDeclarationDetailInputVo);

        adminDao.callServiceCenterReportDetail(procedureVo);

        P_DeclarationDetailOutputVo declarationDetail = new Gson().fromJson(procedureVo.getExt(), P_DeclarationDetailOutputVo.class);

        String result;

        if(Status.신고상세조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.신고상세조회_성공, declarationDetail));
        } else if(Status.신고상세조회_공지번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.신고상세조회_공지번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.신고상세조회_에러));
        }

        return result;
    }

    /**
     * 신고내역 > 신고대상자 정보 조회
     */
    public String selectUserProfile(LiveChatProfileVo liveChatProfileVo) {
        LiveChatProfileVo profile = adminDao.selectUserProfile(liveChatProfileVo);
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, profile));

        return result;
    }

    /**
     * 회원 정보수정 내역 보기
     */
    public String callMemberEditHistory(P_MemberEditHistInputVo pMemberEditHistInputVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberEditHistInputVo);
        ArrayList<P_MemberEditHistOutputVo> editList = adminDao.callMemberEditHistory(procedureVo);
        String result;
        if (Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.조회, editList));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }
        return result;

    }

    /**
     * 관리자 이름 조회
     */
    public String selectAdminName(String mem_no){
        return adminDao.selectAdminName(mem_no);
    }
}
