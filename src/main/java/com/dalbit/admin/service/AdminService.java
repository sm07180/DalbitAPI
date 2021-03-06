package com.dalbit.admin.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.dao.AdminMemberDao;
import com.dalbit.admin.util.AdminSocketUtil;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.service.GuestService;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.broadcast.vo.request.RoomListVo;
import com.dalbit.common.code.*;
import com.dalbit.common.service.EmailService;
import com.dalbit.common.service.PushService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_pushInsertVo;
import com.dalbit.event.service.EventService;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class AdminService {

    @Autowired
    AdminDao adminDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    AdminMemberDao adminMemberDao;

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

    @Autowired
    SmsService smsService;

    @Autowired
    EmailService emailService;

    @Autowired
    GuestService guestService;

    @Autowired EventService eventService;

    private final String menuJsonKey = "adminMenu";

    @Value("${server.api.url}")
    private String SERVER_API_URL;

    @Value("${wowza.audio.wss.url}")
    private String WOWZA_AUDIO_WSS_URL;
    @Value("${wowza.video.wss.url.edge}")
    private String WOWZA_VIDEO_WSS_URL_EDGE;
    @Value("${wowza.prefix}")
    private String WOWZA_PREFIX;

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    private String PROFILE_DEFAULT_IMAGE = "/profile_3/profile_n_200327.jpg";

    public String authCheck(HttpServletRequest request, SearchVo searchVo) throws GlobalException {
        String headerToken = request.getHeader(SSO_HEADER_COOKIE_NAME);
        TokenVo tokenVo = null;
        try {
            tokenVo = jwtUtil.getTokenVoFromJwt(headerToken);
        }catch(Exception e){}

        var resultMap = new HashMap();
        if(tokenVo != null && tokenVo.isAdmin()){
            resultMap.put("isAdmin", true);
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????????????????, resultMap));
        }
        try{
            String mem_no = MemberVo.getMyMemNo(request);
            if(DalbitUtil.isEmpty(mem_no)){
                resultMap.put("isAdmin", false);
                return gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????, resultMap));
            }

            searchVo.setMem_no(mem_no);
            ProcedureVo procedureVo = new ProcedureVo(searchVo);
            ArrayList<AdminMenuVo> menuList = adminDao.callMobileAdminMenuAuth(procedureVo);
            if (DalbitUtil.isEmpty(menuList)) {
                resultMap.put("isAdmin", false);
                return gsonUtil.toJson(new JsonOutputVo(AdminStatus.?????????????????????_????????????, resultMap));
            }

            resultMap.put("isAdmin", true);
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????????????????, resultMap));

        }catch (Exception e){
            resultMap.put("isAdmin", false);
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????????????????, resultMap));
        }

    }

    public boolean isAdmin(HttpServletRequest request) {
        String mem_no = MemberVo.getMyMemNo(request);
        if (!DalbitUtil.isEmpty(mem_no)) {
            SearchVo searchVo = new SearchVo();
            searchVo.setMem_no(mem_no);
            ArrayList<AdminMenuVo> menuList = adminDao.selectMobileAdminMenuAuth(searchVo);
            return !DalbitUtil.isEmpty(menuList);
        }
        return false;
    }

    public boolean isAdmin(String memNo){
        if (!DalbitUtil.isEmpty(memNo)) {
            SearchVo searchVo = new SearchVo();
            searchVo.setMem_no(memNo);
            ArrayList<AdminMenuVo> menuList = adminDao.selectMobileAdminMenuAuth(searchVo);
            return !DalbitUtil.isEmpty(menuList);
        }
        return false;
    }

    public String selectAdminMenu(HttpServletRequest request){

        var map = new HashMap<>();
        var searchVo = new SearchVo();
        searchVo.setMem_no(MemberVo.getMyMemNo(request));

        List<AdminMenuVo> adminMenuList = adminCommonService.getAdminMenuInSession(request);

        if(DalbitUtil.isEmpty(adminMenuList) || 0 == adminMenuList.size()) {
            return gsonUtil.toJson(new JsonOutputVo(AdminStatus.?????????????????????_????????????));
        }
        map.put(menuJsonKey, adminCommonService.getAdminMenuInSession(request));
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, map));
    }

    /**
     * - ??????????????? > ????????? ????????? ??????
     * - ???????????????
     * - ??????????????? > ?????? ?????? ??????
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

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, map));
    }

    public String selectBroadcastDetail(SearchVo searchVo){
        BroadcastDetailVo broadInfo = adminDao.selectBroadcastSimpleInfo(searchVo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, broadInfo));
    }

    public String selectBroadcastDetailWowza(SearchVo searchVo){
        BroadcastDetailVo broadInfo = adminDao.selectBroadcastSimpleInfo(searchVo);
        if(broadInfo != null){
            log.info("[WOWZA] Request URL : {}", "a".equals(broadInfo.getTypeMedia()) ? WOWZA_AUDIO_WSS_URL : WOWZA_VIDEO_WSS_URL_EDGE );
            broadInfo.setWsUrl("a".equals(broadInfo.getTypeMedia()) ? WOWZA_AUDIO_WSS_URL : WOWZA_VIDEO_WSS_URL_EDGE);
            broadInfo.setApplicationName("edge");
            broadInfo.setStreamName(WOWZA_PREFIX + searchVo.getRoom_no() + "_opus");
        }

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, broadInfo));
    }

    /**
     * ??????????????? > ????????????
     */
    public String roomForceExit(P_RoomForceExitInputVo pRoomForceExitInputVo, HttpServletRequest request) {
        pRoomForceExitInputVo.setMemLogin(1); //????????? ??????
        ProcedureVo procedureVo = new ProcedureVo(pRoomForceExitInputVo);

        //??? ????????? ??????
        if(DalbitUtil.isEmpty(pRoomForceExitInputVo.getRoomExit()) || pRoomForceExitInputVo.getRoomExit().equals("Y")) {
            log.error("callBroadcastRoomExit prev data(AdminService) >>>> {} {} {}", pRoomForceExitInputVo.getMemLogin(), pRoomForceExitInputVo.getMem_no(), pRoomForceExitInputVo.getRoom_no());
            adminDao.callBroadcastRoomExit(procedureVo);
            if (AdminStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(AdminStatus.??????????????????_????????????));

            } else if (AdminStatus.??????????????????_?????????????????????.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(AdminStatus.??????????????????_?????????????????????));

            } else if (AdminStatus.??????????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(AdminStatus.??????????????????_??????????????????));
            }
        }

        P_MemberAdminMemoAddVo pMemberAdminMemoAddVo = new P_MemberAdminMemoAddVo();
        pMemberAdminMemoAddVo.setOpName(selectAdminName(MemberVo.getMyMemNo(request)));
        pMemberAdminMemoAddVo.setMem_no(pRoomForceExitInputVo.getMem_no());
        pMemberAdminMemoAddVo.setMemo("???????????? ?????? ?????? ?????? ?????? ?????? ??????");
        ProcedureVo adminMemoProcedure = new ProcedureVo(pMemberAdminMemoAddVo);
        adminDao.callMemAdminMemoAdd(adminMemoProcedure);

        if(!DalbitUtil.isEmpty(pRoomForceExitInputVo.getRoom_no())) {
            pMemberAdminMemoAddVo.setOpName(selectAdminName(MemberVo.getMyMemNo(request)));
            pMemberAdminMemoAddVo.setMem_no(pRoomForceExitInputVo.getRoom_no());
            pMemberAdminMemoAddVo.setMemo("???????????? ?????? ?????? ?????? ?????? ?????? ??????");
            adminMemoProcedure = new ProcedureVo(pMemberAdminMemoAddVo);
            adminDao.callMemAdminMemoAdd(adminMemoProcedure);
        }

        //???????????? ????????????
        P_MemberBroadcastInputVo pMemberBroadcastInputVo = new P_MemberBroadcastInputVo();
        pMemberBroadcastInputVo.setRoom_no(pRoomForceExitInputVo.getRoom_no());
        ProcedureVo broadcastInfo = new ProcedureVo(pMemberBroadcastInputVo);
        adminDao.callBroadcastInfo(broadcastInfo);

        P_BroadcastDetailOutputVo broadcastDetail = new Gson().fromJson(broadcastInfo.getExt(), P_BroadcastDetailOutputVo.class);

        //?????? ????????? ??????
        adminDao.updateBroadcastMemberExit(pRoomForceExitInputVo);

        //??? ?????? ??????
        adminDao.updateBroadcastExit(new BroadcastExitVo(pRoomForceExitInputVo.getRoom_no(), broadcastDetail.getStartDate()));

        //?????? ?????? ??????
        SocketVo vo = socketService.getSocketVo(pRoomForceExitInputVo.getRoom_no(), pRoomForceExitInputVo.getMem_no(), true);
        socketService.chatEnd(pRoomForceExitInputVo.getRoom_no(), pRoomForceExitInputVo.getMem_no(), jwtUtil.generateToken(pRoomForceExitInputVo.getMem_no(), true), 3, DalbitUtil.isLogin(request), vo);

        return gsonUtil.toJson(new JsonOutputVo(AdminStatus.??????????????????_??????));
    }

    /**
     * ??????????????? > ??????
     */
    public String broadcastHide(P_RoomHideInputVo p_roomHideInputVo){
        int hide = adminDao.broadcastHide(p_roomHideInputVo);
        String ediContents = "";
        if (p_roomHideInputVo.getHide() == 1) {
            ediContents = "????????? ?????? : " + p_roomHideInputVo.getTitle() + " > [????????? ??????]";
        }else {
            ediContents = "????????? ?????? : " + p_roomHideInputVo.getTitle() + " > [????????? ?????? ??????]";
        }

        ProImageInitVo proImageInitVo = new ProImageInitVo();
        proImageInitVo.setMem_no(p_roomHideInputVo.getMem_no());
        proImageInitVo.setEdit_contents(ediContents);
        proImageInitVo.setOp_name(MemberVo.getMyMemNo());
        proImageInitVo.setType(0);
        adminDao.insertProfileHistory(proImageInitVo);

        BroImageInitVo broImageInitVo = new BroImageInitVo();
        broImageInitVo.setRoom_no(p_roomHideInputVo.getRoom_no());
        broImageInitVo.setEdit_contents(ediContents);
        broImageInitVo.setOp_name(MemberVo.getMyMemNo());

        adminDao.insertBroadHistory(broImageInitVo);

        String result;
        if(hide > 0){
            result = gsonUtil.toJson(new JsonOutputVo(AdminStatus.???????????????_??????_??????_??????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(AdminStatus.???????????????_??????_??????_??????));
        }

        return result;
    }

    /**
     * - ??????????????? > ????????? ????????? ??????
     * - ??????????????? > ????????? ??????
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

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, map));
    }

    /**
     * ??????????????? > ????????? ????????? ?????????
     */
    public String proImageInit(HttpServletRequest request, ProImageInitVo proImageInitVo) throws GlobalException {

        try{

            proImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            proImageInitVo.setType(0);
            proImageInitVo.setEdit_contents("?????????????????? ?????? : " + proImageInitVo.getImage_profile() + " >> " + PROFILE_DEFAULT_IMAGE);

            // rd_data.tb_member_profile_edit_history??? insert
            adminDao.insertProfileHistory(proImageInitVo);

            // rd_data.tb_member_profile??? image_profile update
            adminDao.proImageInit(proImageInitVo);

            // rd_data.tb_member_notification??? insert
            NotiInsertVo notiInsertVo = new NotiInsertVo();
            if(!DalbitUtil.isEmpty(proImageInitVo.getNotificationYn()) && proImageInitVo.getNotificationYn().equals("Y")) {

                try {
                    //??????(???) ??????
                    notiInsertVo.setMem_no(proImageInitVo.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents(proImageInitVo.getReport_title());
                    notiInsertVo.setNotiMemo(proImageInitVo.getReport_message());
                    adminDao.insertNotiHistory(notiInsertVo);
                } catch (Exception e) {
                    log.error("[??????????????????] ?????? ?????? - ????????? ????????? ?????????");
                }

                //PUSH ??????
                try {
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                    pPushInsertVo.setMem_nos(proImageInitVo.getMem_no());
                    pPushInsertVo.setSlct_push("35");
                    pPushInsertVo.setPush_slct("57");
                    pPushInsertVo.setSend_title("?????? ???????????? ?????? ??????");
                    pPushInsertVo.setSend_cont(proImageInitVo.getReport_title());
                    pPushInsertVo.setEtc_contents(proImageInitVo.getReport_message().replaceAll("\n", "<br>"));
                    pPushInsertVo.setImage_type("101");

                    pushService.sendPushReqOK(pPushInsertVo);
                } catch (Exception e) {
                    log.error("[??????????????????] PUSH ?????? ?????? - ????????? ????????? ?????????");
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
                log.error("[??????????????????] ???????????? ?????? - ????????? ????????? ?????????");
            }

            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????????????????_??????));

        }catch (Exception e){
            throw new GlobalException(MemberStatus.???????????????????????????_??????, "???????????????????????????_??????");
        }


    }

    /**
     * ??????????????? > ????????? ????????? ?????????
     */
    public String broImageInit(HttpServletRequest request, BroImageInitVo broImageInitVo) throws GlobalException {

        try{
            broImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            broImageInitVo.setEdit_contents("????????? ????????? ?????? : " + broImageInitVo.getImage_background() + " >> " + broImageInitVo.getReset_image_background());

            // rd_data.tb_broadcast_room_edit_history??? insert
            adminDao.insertBroadHistory(broImageInitVo);

            // rd_data.tb_broadcast_room??? image_background update
            adminDao.broImageInit(broImageInitVo);

            // rd_data.tb_member_notification??? insert
//            NotiInsertVo notiInsertVo = new NotiInsertVo();
//            if(!DalbitUtil.isEmpty(broImageInitVo.getNotificationYn()) && broImageInitVo.getNotificationYn().equals("Y")) {
//                try {
//                    // ??????(???)??????
//                    notiInsertVo.setMem_no(broImageInitVo.getMem_no());
//                    notiInsertVo.setSlctType(7);
//                    notiInsertVo.setNotiContents(broImageInitVo.getReport_title());
//                    notiInsertVo.setNotiMemo(broImageInitVo.getReport_message());
//                    adminDao.insertNotiHistory(notiInsertVo);
//                } catch(Exception e) {
//                    log.error("[??????????????????] ?????? ?????? - ????????? ????????? ?????????");
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
                log.error("[??????????????????] ???????????? ?????? - ????????? ????????? ?????????");
            }

            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_??????));

        }catch (Exception e){
            throw new GlobalException(BroadcastStatus.???????????????????????????_??????, "???????????????????????????_??????");
        }
    }

    /**
     * ??????????????? > ????????? ?????????
     */
    public String nickTextInit(HttpServletRequest request, NickTextInitVo nickTextInitVo, ProImageInitVo proImageInitVo) throws GlobalException {

        try {
            proImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            proImageInitVo.setType(0);
            proImageInitVo.setEdit_contents("????????? ?????? : " + nickTextInitVo.getMem_nick() + " >> " + nickTextInitVo.getMem_userid());
            proImageInitVo.setMem_no(nickTextInitVo.getMem_no());

            // rd_data.tb_member_profile_edit_history??? insert
            adminDao.insertProfileHistory(proImageInitVo);

            // rd_data.tb_member_basic??? mem_nick update
            adminDao.nickTextInit(nickTextInitVo);

            // rd_data.tb_member_notification??? insert
            NotiInsertVo notiInsertVo = new NotiInsertVo();
            if(!DalbitUtil.isEmpty(nickTextInitVo.getNotificationYn()) && nickTextInitVo.getNotificationYn().equals("Y")) {
                try {
                    //??????(???) ??????
                    notiInsertVo.setMem_no(nickTextInitVo.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents(nickTextInitVo.getReport_title());
                    notiInsertVo.setNotiMemo(nickTextInitVo.getReport_message());
                    adminDao.insertNotiHistory(notiInsertVo);
                }catch (Exception e){
                    log.error("[??????????????????] ?????? ?????? - ????????? ?????????");
                }

                //PUSH ??????
                try{
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                    pPushInsertVo.setMem_nos(nickTextInitVo.getMem_no());
                    pPushInsertVo.setSlct_push("35");
                    pPushInsertVo.setPush_slct("57");
                    pPushInsertVo.setSend_title("?????? ???????????? ?????? ??????");
                    pPushInsertVo.setSend_cont(nickTextInitVo.getReport_title());
                    pPushInsertVo.setEtc_contents(nickTextInitVo.getReport_message().replaceAll("\n", "<br>"));
                    pPushInsertVo.setImage_type("101");
                    pushService.sendPushReqOK(pPushInsertVo);
                }catch (Exception e){
                    log.error("[??????????????????] PUSH ?????? ?????? - ????????? ?????????");
                }
            }

            try{

                //??????????????? ????????? ???????????? ??????
                MemberInfoVo memberInfo = adminMemberDao.callMemberDetail(new ProcedureVo(new MemberInfoVo(nickTextInitVo.getMem_no())));

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
                log.error("[??????????????????] ???????????? ?????? - ????????? ?????????");
            }

            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????));

        }catch(Exception e) {
            throw new GlobalException(MemberStatus.??????????????????_??????, "??????????????????_??????");
        }
    }

    /**
     * ??????????????? > ?????? ?????? ?????????
     */
    public String broTitleTextInit(HttpServletRequest request, BroTitleTextInitVo broTitleTextInitVo, BroImageInitVo broImageInitVo) throws GlobalException {

        try {
            broImageInitVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
            broImageInitVo.setEdit_contents("???????????? : " + broTitleTextInitVo.getTitle() + " >> " + broTitleTextInitVo.getMem_nick() + "?????? ???????????????.");
            broImageInitVo.setRoom_no(broTitleTextInitVo.getRoom_no());

            // rd_data.tb_broadcast_room_edit_history??? insert
            adminDao.insertBroadHistory(broImageInitVo);

            // rd_data.tb_broadcast_room??? title update
            adminDao.broTitleTextInit(broTitleTextInitVo);

            // rd_data.tb_member_notification??? insert
//            NotiInsertVo notiInsertVo = new NotiInsertVo();
//            if(!DalbitUtil.isEmpty(broTitleTextInitVo.getNotificationYn()) && broTitleTextInitVo.getNotificationYn().equals("Y")) {
//                try{
//                    //??????(???) ??????
//                    notiInsertVo.setMem_no(broTitleTextInitVo.getMem_no());
//                    notiInsertVo.setSlctType(7);
//                    notiInsertVo.setNotiContents(broTitleTextInitVo.getReport_title());
//                    notiInsertVo.setNotiMemo(broTitleTextInitVo.getReport_message());
//                    adminDao.insertNotiHistory(notiInsertVo);
//                }catch (Exception e){
//                    log.error("[??????????????????] ?????? ?????? - ?????? ?????? ?????????");
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
                log.error("[??????????????????] ???????????? ?????? - ?????? ?????? ?????????");
            }

            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));

        }catch(Exception e) {
            throw new GlobalException(BroadcastStatus.?????????????????????_??????, "?????????????????????_??????");
        }
    }

    /**
     * ????????????
     */
    public String declarationOperate(HttpServletRequest request, DeclarationVo declarationVo) throws GlobalException {

        try {
            declarationVo.setOpName(selectAdminName(MemberVo.getMyMemNo(request)));

            var myProcedureVo = new ProcedureVo(new MemberInfoVo(MemberVo.getMyMemNo(request)));
            adminMemberDao.callMemberDetail(myProcedureVo);
            MemberInfoVo myInfo = new Gson().fromJson(myProcedureVo.getExt(), MemberInfoVo.class);
            log.info("myInfo : {}", myInfo);

            var reportedProcedureVo = new ProcedureVo(new MemberInfoVo(declarationVo.getReported_mem_no()));
            adminMemberDao.callMemberDetail(reportedProcedureVo);
            MemberInfoVo reportedInfo = new Gson().fromJson(reportedProcedureVo.getExt(), MemberInfoVo.class);
            log.info("reportedInfo : {}", reportedInfo);

            // ?????????
            declarationVo.setMem_no(myInfo.getMem_no());
            declarationVo.setMem_userid(myInfo.getMem_userid());
            if(!StringUtils.isEmpty(myInfo.getMem_nick())) {
                declarationVo.setMem_nick(myInfo.getMem_nick());
            }else {
                declarationVo.setMem_nick(myInfo.getNickName());
            }
            // ?????? ?????????
            declarationVo.setReported_mem_no(reportedInfo.getMem_no());
            declarationVo.setReported_userid(reportedInfo.getMem_userid());
            declarationVo.setReported_phone(reportedInfo.getMem_phone());
            declarationVo.setReported_level(reportedInfo.getLevel());
            declarationVo.setReported_grade(reportedInfo.getGrade());
            declarationVo.setStatus(1);
            if(!StringUtils.isEmpty(reportedInfo.getMem_nick())) {
                declarationVo.setReported_nick(reportedInfo.getMem_nick());
            }else {
                declarationVo.setReported_nick(reportedInfo.getNickName());
            }

            if(0 < declarationVo.getReportIdx()){
                adminDao.declarationResponseOperate(declarationVo);
            }else{
                adminDao.declarationOperate(declarationVo);
            }


            //???????????? ????????? ?????? VO ??????
            DeclarationVo memberDeclarationVo = new DeclarationVo();
            memberDeclarationVo.setMem_no(reportedInfo.getMem_no());
            memberDeclarationVo.setOpCode(declarationVo.getOpCode());
            memberDeclarationVo.setBlock_type(0);

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
                }else if(opCode == 8) {
                    memberDeclarationVo.setState(5);
                    memberDeclarationVo.setBlock_type(1);
                }
            }
            adminDao.updateState(memberDeclarationVo);


            // ????????? ????????? ??????, ?????? ??? ??????????????????, ?????????????????? ??? ?????? ?????????
            if(memberDeclarationVo.getOpCode() > 2) {

                //uuid ?????? ??????
                if (!DalbitUtil.isEmpty(reportedInfo.getDevice_uuid()) && declarationVo.getUuid_block() == 1) {
                    declarationVo.setBlock_type(1);
                    declarationVo.setBlock_text(reportedInfo.getDevice_uuid());
                    adminDao.insertBlock(declarationVo);

                    declarationVo.setEdit_contents("deviceUuid ?????? ?????? : " + declarationVo.getBlock_text());
                    declarationVo.setEdit_type(0);
                    adminDao.insertBlockHistory(declarationVo);
                }

                //ip ?????? ??????
                if(!DalbitUtil.isEmpty(reportedInfo.getIp()) && declarationVo.getIp_block() == 1){
                    declarationVo.setBlock_type(2);
                    declarationVo.setBlock_text(reportedInfo.getIp());
                    adminDao.insertBlock(declarationVo);

                    declarationVo.setEdit_contents("ip ?????? ?????? : " + declarationVo.getBlock_text());
                    declarationVo.setEdit_type(0);
                    adminDao.insertBlockHistory(declarationVo);
                }

                //???????????? ?????? ??????
                if(!DalbitUtil.isEmpty(reportedInfo.getMem_no()) && declarationVo.getMemNo_block() == 1){
                    declarationVo.setBlock_type(3);
                    declarationVo.setBlock_text(reportedInfo.getMem_no());
                    adminDao.insertBlock(declarationVo);

                    declarationVo.setEdit_contents("???????????? ?????? ?????? : " + declarationVo.getBlock_text());
                    declarationVo.setEdit_type(0);
                    adminDao.insertBlockHistory(declarationVo);
                }

                //??????????????? ?????? ??????
                if(!DalbitUtil.isEmpty(reportedInfo.getMem_phone()) && declarationVo.getPhone_block() == 1){
                    declarationVo.setBlock_type(4);
                    declarationVo.setBlock_text(reportedInfo.getMem_phone());
                    adminDao.insertBlock(declarationVo);

                    declarationVo.setEdit_contents("??????????????? ?????? ?????? : " + declarationVo.getBlock_text());
                    declarationVo.setEdit_type(0);
                    adminDao.insertBlockHistory(declarationVo);
                }

                var hashMap = new HashMap();
                hashMap.put("mem_no", declarationVo.getMem_no());
                hashMap.put("message", declarationVo.getNotiMemo());
                memberForceLogout(request, hashMap);

            }

            //rd_data.tb_member_notification??? insert
            NotiInsertVo notiInsertVo = new NotiInsertVo();
            if(!DalbitUtil.isEmpty(declarationVo.getNotificationYn()) && declarationVo.getNotificationYn().equals("Y")) {
                try{
                    //??????(???) ??????
                    notiInsertVo.setMem_no(reportedInfo.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents(declarationVo.getNotiContents());
                    notiInsertVo.setNotiMemo(declarationVo.getNotiMemo());
                    adminDao.insertNotiHistory(notiInsertVo);
                }catch (Exception e){
                    log.error("[??????????????????] ?????? ?????? - ????????????");
                }
                //PUSH ??????
                try{
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                    pPushInsertVo.setMem_nos(reportedInfo.getMem_no());
                    pPushInsertVo.setSlct_push("35");
                    pPushInsertVo.setSend_title("?????? ???????????? ?????? ??????");
                    pPushInsertVo.setSend_cont(declarationVo.getNotiContents());
                     pPushInsertVo.setEtc_contents(declarationVo.getNotiMemo().replaceAll("\n", "<br>"));
                    pPushInsertVo.setImage_type("101");
                    pushService.sendPushReqOK(pPushInsertVo);
                }catch (Exception e){
                    log.error("[??????????????????] PUSH ?????? ?????? - ????????????");
                }
            }
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_??????));

            }catch (Exception e) {
               throw new GlobalException(MemberStatus.????????????_??????, "????????????_??????");
        }
    }

    /**
     * ?????? ?????? ??????
     */
    public MemberInfoVo getMemberInfo(String mem_no) {
        MemberInfoVo result = adminDao.getMemberInfo(mem_no);
        return result;
    }

    /**
     * ????????? ?????? ??????
     */
    public BroadInfoVo getBroadInfo(String room_no) {
        BroadInfoVo result = adminDao.getBroadInfo(room_no);
        return result;
    }

    /**
     * ??????????????? > ?????? ?????? ????????????
     */
    public String callBroadcastLiveChatInfo(LiveChatInputVo liveChatInputVo) {
        String result;

        try{
            ArrayList<LiveChatOutputVo> liveChatList = adminDao.selectBroadcastLiveChatInfo(liveChatInputVo);

            if(DalbitUtil.isEmpty(liveChatList)) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????_???????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????, liveChatList));
            }
        }catch (Exception e){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????));
        }
        return result;
    }

    /**
     * ??????????????? > ????????? ?????? ??? ?????????
     */
    public String getLiveChatProfile(LiveChatProfileVo liveChatProfileVo) {
        LiveChatProfileVo profile = adminDao.getLiveChatProfile(liveChatProfileVo);
        String result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, profile));

        return result;
    }

    /**
     * ??????????????? > ????????????
     */
    public String forcedOut(HttpServletRequest request, ForcedOutVo forcedOutVo) {

        forcedOutVo.setOpName(selectAdminName(MemberVo.getMyMemNo(request)));
        String sendNoti = "0";
        if(!DalbitUtil.isEmpty(forcedOutVo.getNotificationYn()) && forcedOutVo.getNotificationYn().equals("Y")){
            sendNoti = "1";
            forcedOutVo.setNotiContents(forcedOutVo.getReport_title());
            forcedOutVo.setNotiMemo(forcedOutVo.getReport_message());
            //PUSH ??????
            try{
                P_pushInsertVo pPushInsertVo = new P_pushInsertVo();
                pPushInsertVo.setMem_nos(forcedOutVo.getMem_no());
                pPushInsertVo.setSlct_push("35");
                pPushInsertVo.setSend_title("?????? ???????????? ?????? ??????");
                pPushInsertVo.setSend_cont(forcedOutVo.getReport_title());
                pPushInsertVo.setEtc_contents(forcedOutVo.getReport_message().replaceAll("\n", "<br>"));
                pPushInsertVo.setImage_type("101");
                pushService.sendPushReqOK(pPushInsertVo);
            }catch (Exception e){
                log.error("[??????????????????] PUSH ?????? ?????? - ?????? ??????");
            }
        }
        //???????????? ??????
        SearchVo searchVo = new SearchVo();
        searchVo.setRoom_no(forcedOutVo.getRoom_no());
        BroadcastDetailVo broadInfo = adminDao.selectBroadcastSimpleInfo(searchVo);
        // ????????? ??????????????????
//        if(DalbitUtil.isEmpty(forcedOutVo.getRoomBlock()) || forcedOutVo.getRoomBlock().equals("Y") ){
//            forcedOutVo.setDj_mem_no(broadInfo.getMem_no());
//            adminDao.insertForceLeave_roomBlock(forcedOutVo);
//        }

        forcedOutVo.setSendNoti(sendNoti);
        ProcedureVo procedureVo = new ProcedureVo(forcedOutVo);
        adminDao.callForceLeave(procedureVo);

        String result = "";
        if(BroadcastStatus.???????????????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_??????));

            //????????? ?????? ??????
            HashMap<String,Object> param = new HashMap<>();
            param.put("roomNo", broadInfo.getRoom_no());
            param.put("target_memNo", forcedOutVo.getMem_no());
            param.put("target_nickName", forcedOutVo.getMem_nick());
            param.put("memNo", broadInfo.getMem_no());
            param.put("nickName", broadInfo.getNickNm());
            // option
            param.put("ctrlRole","ctrlRole");
            param.put("revMemNo", forcedOutVo.getMem_no());     // ?????? ??????
            param.put("recvType","system");
            param.put("recvPosition","top1");
            param.put("recvLevel",2);
            param.put("recvTime",1);

            // message set
            Gson gson = new Gson();
            HashMap<String,Object> tmp = new HashMap();
            tmp.put("revMemNo", forcedOutVo.getMem_no());     // ?????? ??????
            tmp.put("revMemNk", forcedOutVo.getMem_nick());
            tmp.put("sndAuth", 4);
            tmp.put("sndMemNo", broadInfo.getMem_no());            // ?????? ??????
            tmp.put("sndMemNk", broadInfo.getNickNm());
            String message =  gson.toJson(tmp);

            adminSocketUtil.setSocket(param,"reqKickOut", message,jwtUtil.generateToken(forcedOutVo.getMem_no(), true));

        }else if(BroadcastStatus.???????????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????));
        } else if(BroadcastStatus.???????????????????????????_?????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_?????????));
        } else if(BroadcastStatus.???????????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????));
        } else if(BroadcastStatus.???????????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_???????????????));
        } else if(BroadcastStatus.???????????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_???????????????));
        }

        return result;

    }

    /**
     * ??????????????? > ?????????????????? ??????
     */
    public String insertContentsMessageAdd(HttpServletRequest request, MessageInsertVo messageInsertVo) throws GlobalException {
        messageInsertVo.setOp_name(selectAdminName(MemberVo.getMyMemNo(request)));
        String result="";

        try{
            // ???????????? ????????? ????????? ?????? ??? ?????? ?????? ??????
            if(!DalbitUtil.isEmpty(messageInsertVo.getSend_all()) && messageInsertVo.getSend_all().equals("0")) {       // ALL
                // ?????? ????????? ????????????
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
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_???????????????));
            }

            messageInsertVo.setTitle("????????? ????????? ??????");
            int insertResult = adminDao.insertContentsMessageAdd(messageInsertVo);

            if(insertResult > 0){
                result = sendSplashApi(messageInsertVo);
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(BroadcastStatus.????????????????????????_??????, "AdminService.insertContentsMessageAdd");
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

            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????));
        }catch (IOException | GlobalException e){
            throw new GlobalException(BroadcastStatus.????????????????????????_??????, "AdminService.sendSplashApi");
        }
    }

    /**
     * ??????????????? > ????????? ??????
     */
    public String selectLiveListener(HttpServletRequest request, ProfileVo profileVo) {
        profileVo.setPagingInfo();
        List<ProfileVo> listenerList = adminDao.selectLiveListener(profileVo);

        var map = new HashMap<>();
        map.put("listenerList", listenerList);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, map));
    }

    /**
     * ?????? > ????????????
     */
    public String callBroadcastTotal(P_StatVo pStatVo) {
        ProcedureVo procedureVo = new ProcedureVo(pStatVo);
        adminDao.callBroadcastTotal(procedureVo);

        P_NewBroadcastTimeOutVo totalInfo = new Gson().fromJson(procedureVo.getExt(), P_NewBroadcastTimeOutVo.class);

        var result = new HashMap<String, Object>();
        result.put("totalInfo", totalInfo);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, result));
    }

    /**
     * ?????? > ???????????? new
     */
    public String callNewBroadcastTimeNew(P_StatVo pStatVo){
        ProcedureVo procedureVo = new ProcedureVo(pStatVo);
        ArrayList<P_NewBroadcastTimeNewOutVo> detailList = adminDao.callNewBroadcastTimeNew(procedureVo);
        P_NewBroadcastTimeNewOutVo totalInfo = new Gson().fromJson(procedureVo.getExt(), P_NewBroadcastTimeNewOutVo.class);
        if(Integer.parseInt(procedureVo.getRet()) <= 0){
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.???????????????));
        }
        var result = new HashMap<String, Object>();
        result.put("totalInfo", totalInfo);
        result.put("detailList", detailList);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, result));
    }

    /**
     * ?????? > ?????? ?????????
     */
    public String callUserCurrentTotal(P_UserCurrentInputVo pUserCurrentInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pUserCurrentInputVo);
        adminDao.callUserCurrentTotal(procedureVo);
        P_UserTotalOutDetailVo detailList = new Gson().fromJson(procedureVo.getExt(), P_UserTotalOutDetailVo.class);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, detailList));
    }

    /**
     * ?????? > ????????? ??????
     */
    public String callLoginInfo(P_LoginTotalInPutVo pLoginTotalInPutVo) {

        // ????????? ???????????? ??????
        ProcedureVo procedureVo = new ProcedureVo(pLoginTotalInPutVo);
        List<P_LoginTotalOutDetailVo> genderList = adminDao.callLoginTotal(procedureVo);
        P_LoginTotalOutVo genderInfo = new Gson().fromJson(procedureVo.getExt(), P_LoginTotalOutVo.class);

        // ??????????????? ???????????? ??????
        List<P_LoginAgeOutDetailVo> ageList = adminDao.callLoginAge(procedureVo);

        var result = new HashMap<String, Object>();
        result.put("genderInfo", genderInfo);
        result.put("ageList", ageList);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, result));
    }

    /**
     * ?????? > ?????? ??????
     */
    public String callPayInfo(P_StatVo pStatVo) {
        ProcedureVo procedureVo = new ProcedureVo(pStatVo);
        adminDao.callPayInfo(procedureVo);

        P_PayInfoOutVo info = new Gson().fromJson(procedureVo.getExt(), P_PayInfoOutVo.class);
        var result = new HashMap<String, Object>();

        result.put("info", info);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, result));
    }

    /**
     * ????????? ??????
     */
    public String callItemInfo(P_StatVo StatVo){

        //return gsonUtil.toJson(new JsonOutputVo(Status.??????, resultList));
        ProcedureVo procedureVo = new ProcedureVo(StatVo);
        ArrayList<P_ItemTotalOutDetailVo> detailList = adminDao.callItemTotal(procedureVo);
        P_ItemTotalOutVo totalInfo = new Gson().fromJson(procedureVo.getExt(), P_ItemTotalOutVo.class);

        if(Integer.parseInt(procedureVo.getRet()) <= 0){
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.???????????????));
        }

        var result = new HashMap<String, Object>();
        result.put("totalInfo", totalInfo);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, totalInfo));
    }

    /**
     * 1:1 ?????? ?????? ??????
     */
    public String callQuestionList(P_QuestionListInputVo pQuestionListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pQuestionListInputVo);
        List<P_QuestionListOutputVo> questionList = adminDao.callQuestionList(procedureVo);

        var map = new HashMap<>();
        map.put("pagingInfo", new AdminProcedureBaseVo(procedureVo.getRet()));
        map.put("questionList", questionList);

        String result;
        if(-1 < Integer.parseInt(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.????????????_??????????????????_??????, map));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.????????????_??????????????????_??????));
        }

        return result;
    }

    /**
     * 1:1 ?????? ?????? ?????? ??????
     */
    public String callServiceCenterQnaDetail(P_QuestionDetailInputVo pQuestionDetailInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pQuestionDetailInputVo);
        adminDao.callServiceCenterQnaDetail(procedureVo);
        P_QuestionDetailOutputVo questionDetail = new Gson().fromJson(procedureVo.getExt(), P_QuestionDetailOutputVo.class);


        //selectMemberRoomListen
        if(DalbitUtil.isLogin(questionDetail.getMem_no())){
            HashMap listenResult = adminDao.selectMemberRoomListen(questionDetail.getMem_no());
            String listen_room_no = DalbitUtil.getStringMap(listenResult, "listen_room_no");
            String listen_title = DalbitUtil.getStringMap(listenResult, "listen_title");
            questionDetail.setListen_room_no(listen_room_no);
            questionDetail.setListen_title(listen_title);

            HashMap broadcastResult = adminDao.selectMemberRoom(questionDetail.getMem_no());
            String broad_room_no = DalbitUtil.getStringMap(broadcastResult, "room_no");
            String broad_title = DalbitUtil.getStringMap(broadcastResult, "title");
            questionDetail.setBroad_room_no(broad_room_no);
            questionDetail.setBroad_title(broad_title);
        }

        String result;

        if(CustomerStatus.????????????_??????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.????????????_??????????????????_??????, questionDetail));
        } else if(CustomerStatus.????????????_??????????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.????????????_??????????????????_??????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.????????????_??????????????????_??????));
        }

        return result;
    }

    /**
     * 1:1 ?????? faq sub list
     */
    public List<FaqVo> selectFaqSubList(FaqVo faqVo) {
        return adminDao.selectFaqSubList(faqVo);
    }

    /**
     * 1:1 ?????? ??????
     */
    public String callServiceCenterQnaOperate(P_QuestionOperateVo pQuestionOperateVo) throws InterruptedException, GlobalException {
        pQuestionOperateVo.setOpName(selectAdminName(MemberVo.getMyMemNo()));
        P_QuestionDetailOutputVo outVo = adminDao.selectServiceCenterQnaState(pQuestionOperateVo);
        String result = "";
        String _answer = pQuestionOperateVo.getAnswer();

        pQuestionOperateVo.setAnswer(pQuestionOperateVo.getAnswer().replaceAll("\\'","\'"));
        pQuestionOperateVo.setAnswer(pQuestionOperateVo.getAnswer().replaceAll("\n","<br>"));
        if(outVo.getState() == 0 || outVo.getState() == 3){
            ProcedureVo procedureVo = new ProcedureVo(pQuestionOperateVo,true);
            adminDao.callServiceCenterQnaOperate(procedureVo);

            if(CustomerStatus.?????????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {

                adminDao.callServiceCenterQnaDetail(procedureVo);
                P_QuestionDetailOutputVo questionDetail = new Gson().fromJson(procedureVo.getExt(), P_QuestionDetailOutputVo.class);

                try{    // PUSH ??????
                    P_pushInsertVo pPushInsertVo = new P_pushInsertVo();

                    pPushInsertVo.setMem_nos(questionDetail.getMem_no());
                    pPushInsertVo.setSlct_push("37");
                    pPushInsertVo.setPush_slct("60");
                    pPushInsertVo.setSend_title("1:1?????? ????????? ??????????????????!");
                    pPushInsertVo.setSend_cont("????????? 1:1????????? ????????? ?????????????????????.");
                    pPushInsertVo.setImage_type("101");
                    pushService.sendPushReqOK(pPushInsertVo);
                }catch (Exception e){
                    log.error("[PUSH ?????? ?????? - ?????????????????????]");
                }

                try{
                    //??????(???) ??????
                    NotiInsertVo notiInsertVo = new NotiInsertVo();

                    notiInsertVo.setMem_no(questionDetail.getMem_no());
                    notiInsertVo.setSlctType(7);
                    notiInsertVo.setNotiContents("????????? 1:1????????? ????????? ?????????????????????.");
                    notiInsertVo.setNotiMemo("????????? 1:1????????? ????????? ?????????????????????.");
                    adminDao.insertNotiHistory(notiInsertVo);

                }catch (Exception e){
                    log.error("[NOTI ?????? ?????? - ?????????????????????]");
                }

                result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????));
            } else if(CustomerStatus.?????????????????????_??????????????????.getMessageCode().equals((procedureVo.getRet()))) {
                result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????????????????));
            } else if(CustomerStatus.?????????????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????));
            }
        }else{
            if(outVo.getState() == 1){
                int updateResult = adminDao.updateServiceCenterQnaUpdate(pQuestionOperateVo);
                if(updateResult == 1) {
                    result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????));
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????));
                }
            }else if(outVo.getState() == 2){
                result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.?????????????????????_??????_?????????));
            }
        }

        if(!DalbitUtil.isEmpty(outVo.getEmail())){      // ????????????
            String sHtml = "";
            StringBuffer mailContent = new StringBuffer();
            BufferedReader in = null;
            try{
                URL url = new URL("http://image.dalbitlive.com/resource/mailForm/mailing.txt");
                URLConnection urlconn = url.openConnection();
                in = new BufferedReader(new InputStreamReader(urlconn.getInputStream(),"utf-8"));

                while((sHtml = in.readLine()) != null){
                    mailContent.append("\n");
                    mailContent.append(sHtml);
                }

                String msgCont;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                String today = dateFormat.format(new Date());

                msgCont = mailContent.toString().replaceAll("@@qnaTime@@", today);
                msgCont = msgCont.replaceAll("@@qnaType@@", outVo.getSlct_type_name());
                msgCont = msgCont.replaceAll("@@qnaTitle@@", outVo.getQuestion_title());
                msgCont = msgCont.replaceAll("@@qnaContent@@", outVo.getQuestion_contents());

                String fileNames = "";
                fileNames += DalbitUtil.isEmpty(outVo.getFile_name1()) ? "" : outVo.getFile_name1();
                if((!DalbitUtil.isEmpty(fileNames) && !DalbitUtil.isEmpty(outVo.getFile_name2()))){
                    fileNames += ", " + outVo.getFile_name2();
                }else if(!DalbitUtil.isEmpty(outVo.getFile_name2())){
                    fileNames += outVo.getFile_name2();
                }

                if((!DalbitUtil.isEmpty(fileNames) && !DalbitUtil.isEmpty(outVo.getFile_name3()))){
                    fileNames += ", " + outVo.getFile_name3();
                }else if(!DalbitUtil.isEmpty(outVo.getFile_name3())){
                    fileNames += outVo.getFile_name3();
                }

                msgCont = msgCont.replaceAll("@@fileName@@", fileNames);
                msgCont = msgCont.replaceAll("@@answer@@", _answer);

                EmailVo emailVo = new EmailVo("[???????????????] 1:1????????? ?????? ????????? ??????????????????.", outVo.getEmail(), msgCont);

                emailService.sendEmail(emailVo);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if(!DalbitUtil.isEmpty(outVo.getPhone())){          // ?????? or ( ?????? and ?????? )
            String answer = _answer;
            String[] array_word = answer.split(""); //????????? ???????????? ????????????

            answer = "";
            int count = 1000;
            boolean smsSw;
            for(int i=0;i<array_word.length;i++) {
                answer = answer + array_word[i];
                smsSw = false;
                if(i != 0) {
                    if (i % count == 0) {
                        smsSw = true;
                    } else {
                        if (array_word.length <= count) {
                            if ((i + 1) == array_word.length) {
                                smsSw = true;
                            }
                        }
                    }
                }

                if(smsSw){
                    Thread.sleep(1500);
                    SmsVo smsSendVo = new SmsVo("[???????????????]"+outVo.getQuestion_title(), answer, outVo.getPhone(), "7");
                    smsSendVo.setSend_name(MemberVo.getMyMemNo());
                    smsSendVo.setMem_no(pQuestionOperateVo.getMem_no());
                    smsSendVo.setCinfo("");
                    int smsResult = smsService.sendMms(smsSendVo);
                    if(smsResult != 1){
                        //result = gsonUtil.toJson(new JsonOutputVo(Status.????????????_??????));
                        break;
                    }
                    answer = "";
                    count+=1000;
                }
            }
        }

        return result;
    }

    /**
     * ???????????? ?????? ??????
     */
    public String callServiceCenterReportList(P_DeclarationListInputVo pDeclarationListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pDeclarationListInputVo);

        ArrayList<P_DeclarationListOutputVo> declarationList = adminDao.callServiceCenterReportList(procedureVo);

        var map = new HashMap<>();
        map.put("pagingInfo", new AdminProcedureBaseVo(procedureVo.getRet()));
        map.put("declarationList", declarationList);

        String result;

        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????, map));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????));
        }

        return result;
    }

    /**
     * ???????????? ?????? ??????
     */
    public String callServiceCenterReportDetail(P_DeclarationDetailInputVo pDeclarationDetailInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pDeclarationDetailInputVo);

        adminDao.callServiceCenterReportDetail(procedureVo);

        P_DeclarationDetailOutputVo declarationDetail = new Gson().fromJson(procedureVo.getExt(), P_DeclarationDetailOutputVo.class);

        String result;

        if(MemberStatus.??????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????, declarationDetail));
        } else if(MemberStatus.??????????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????));
        }

        return result;
    }

    /**
     * ???????????? > ??????????????? ?????? ??????
     */
    public String selectUserProfile(LiveChatProfileVo liveChatProfileVo) {
        LiveChatProfileVo profile = adminDao.selectUserProfile(liveChatProfileVo);
        String result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, profile));

        return result;
    }

    /**
     * ?????????, ?????????????????? ??????????????????
     */
    public LiveChatProfileVo getUserProfile(LiveChatProfileVo liveChatProfileVo) {
        LiveChatProfileVo profileVo = adminDao.selectUserProfile(liveChatProfileVo);
        return profileVo;
    }

    /**
     * ?????? ???????????? ?????? ??????
     */
    public String callMemberEditHistory(P_MemberEditHistInputVo pMemberEditHistInputVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberEditHistInputVo);
        ArrayList<P_MemberEditHistOutputVo> editList = adminDao.callMemberEditHistory(procedureVo);
        String result;
        if (Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, editList));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.???????????????));
        }
        return result;

    }

    /**
     * ????????? ?????? ??????
     */
    public String selectAdminName(String mem_no){
        return adminDao.selectAdminName(mem_no);
    }

    public HashMap getVersion(){
        HashMap result = new HashMap();
        HashMap data = new HashMap();
        data.put("last", adminDao.selectLast());
        data.put("list", adminDao.selectVersion());
        result.put("status", CommonStatus.??????);
        result.put("data", data);
        return result;
    }

    public HashMap getApp(HttpServletRequest request){
        HashMap result = new HashMap();
        HashMap data = new HashMap();
        data.put("list", adminDao.selectApp(request.getParameter("ver")));
        result.put("status", CommonStatus.??????);
        result.put("data", data);
        return result;
    }

    public Status doInsert(HttpServletRequest request){
        Status result = CommonStatus.????????????????????????;
        HashMap params = new HashMap();
        params.put("opName", MemberVo.getMyMemNo());
        params.put("verName", request.getParameter("verName"));
        params.put("appBuild", request.getParameter("appBuild"));
        params.put("dirName", request.getParameter("dirName"));
        params.put("description", request.getParameter("description"));
        if(1 == adminDao.insertApp(params)){
            result = CommonStatus.??????;
        }
        return result;
    }

    public Status doDelete(HttpServletRequest request){
        Status result = CommonStatus.????????????????????????;
        HashMap params = new HashMap();
        params.put("opName", MemberVo.getMyMemNo());
        params.put("idx", request.getParameter("idx"));
        if(1 == adminDao.deleteApp(params)){
            result = CommonStatus.??????;
        }
        return result;
    }

    /**
     * ?????? ????????? ??????
     */
    public String callClipHistoryList(ClipHistoryVo clipHistoryVo) {
        ProcedureVo procedureVo = new ProcedureVo(clipHistoryVo);
        List<ClipHistoryVo> clipList = adminDao.callClipHistoryList(procedureVo);
        AdminBaseVo totalInfo = new Gson().fromJson(procedureVo.getExt(), AdminBaseVo.class);


        if(totalInfo.getPageNo() * totalInfo.getPageCnt() < totalInfo.getTotalCnt()){
            clipHistoryVo.setEndPage(false);
        }else{
            clipHistoryVo.setEndPage(true);
        }

        var map = new HashMap<>();
        map.put("isEndPage", clipHistoryVo.isEndPage());
        map.put("clipList", clipList);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, map));
    }

    /**
     * ?????? ?????? ??????
     */
    public String callClipDetail(ClipDetailVo clipDetailVo) {
        ProcedureVo procedureVo = new ProcedureVo(clipDetailVo);
        adminDao.callClipDetail(procedureVo);

        ClipDetailVo clipDetail = new Gson().fromJson(procedureVo.getExt(), ClipDetailVo.class);

        var map = new HashMap<>();
        map.put("clipDetail", clipDetail);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, map));
    }

    /**
     * ?????? ??????
     */
    public String callClipEdit(ClipAdminEditVo clipAdminEditVo) {
        clipAdminEditVo.setOpName(selectAdminName(MemberVo.getMyMemNo()));
        ProcedureVo procedureVo = new ProcedureVo(clipAdminEditVo);
        adminDao.callClipEdit(procedureVo);

        Status status;
        if (procedureVo.getRet().equals(ClipStatus.????????????_??????.getMessageCode())) {
            status = ClipStatus.????????????_??????;

        } else if(procedureVo.getRet().equals(ClipStatus.????????????_??????????????????.getMessageCode())){
            status = ClipStatus.????????????_??????????????????;

        }else if(procedureVo.getRet().equals(ClipStatus.????????????_??????????????????.getMessageCode())){
            status = ClipStatus.????????????_??????????????????;

        }else if(procedureVo.getRet().equals(ClipStatus.????????????_??????????????????.getMessageCode())){
            status = ClipStatus.????????????_??????????????????;

        }else if(procedureVo.getRet().equals(ClipStatus.????????????_????????????.getMessageCode())){
            status = ClipStatus.????????????_????????????;

        }else if(procedureVo.getRet().equals(ClipStatus.????????????_????????????.getMessageCode())){
            status = ClipStatus.????????????_????????????;

        }else if(procedureVo.getRet().equals(ClipStatus.????????????_???????????????.getMessageCode())){
            status = ClipStatus.????????????_???????????????;

        }else{
            status = CommonStatus.????????????????????????;
        }

        return gsonUtil.toJson(new JsonOutputVo(status));
    }

    /**
     * ?????? ?????? ????????? ??????
     */
    public String selectReplyList(ClipHistoryReplyVo clipHistoryReplyVo) {
        ArrayList<ClipHistoryReplyVo> list = adminDao.selectReplyList(clipHistoryReplyVo);

        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, list));
    }

    /**
     * ?????? ?????? ????????? ??????
     */
    public String deleteReply(ClipHistoryReplyVo clipHistoryReplyVo) {
        clipHistoryReplyVo.setStatus("2");
        adminDao.deleteReply(clipHistoryReplyVo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????));
    }

    /**
     * ?????? (tbl_code_define, type="system_config"??? ??????)
     */
    public String selectSettingList(SettingListVo settingListVo) {
        ArrayList<SettingListVo> list = adminDao.selectSettingList(settingListVo);
        if(DalbitUtil.isEmpty(list) || list.size() == 0) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, new ArrayList<>()));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????, list));
        }


    }

    /**
     * ?????? ??? ??????
     */
    public String updateSetting(SettingListVo settingListVo) {
        String result ="";
        if(!DalbitUtil.isEmpty(settingListVo.getCode()) && !DalbitUtil.isEmpty(settingListVo.getValue())) {
            int status = adminDao.updateSetting(settingListVo);

            if (status > 0) {
                result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????????????????));
            }
        }
        return result;
    }

    /**
     * ?????? ??????/?????? ??? ?????? ???????????? ??????
     */
    public String memberForceLogout(HttpServletRequest request, @RequestParam HashMap<String, String> paramMap) {

        //?????? ?????? ??????
        //SocketVo vo = socketService.getSocketVo(pRoomForceExitInputVo.getRoom_no(), pRoomForceExitInputVo.getMem_no(), true);
        socketService.memberForceLogout(DalbitUtil.getStringMap(paramMap, "memNo"), DalbitUtil.getStringMap(paramMap, "message"));

        return gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????));
    }

}
