package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.RoomExitVo;
import com.dalbit.broadcast.vo.request.StateEditVo;
import com.dalbit.broadcast.vo.request.StateVo;
import com.dalbit.common.code.BroadcastStatus;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.BadgeService;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.event.service.EventService;
import com.dalbit.event.vo.procedure.P_AttendanceCheckVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_BroadcastSettingVo;
import com.dalbit.member.vo.procedure.P_FanRankVo;
import com.dalbit.member.vo.request.SpecialDjHistoryVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.team.service.TeamService;
import com.dalbit.team.vo.TeamParamVo;
import com.dalbit.team.vo.TeamResultVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RoomService {

    @Autowired
    RoomDao roomDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    RestService restService;
    @Autowired
    RoomService roomService;
    @Autowired
    CommonService commonService;
    @Autowired
    SocketService socketService;
    @Autowired
    ContentService contentService;
    @Autowired
    MypageService mypageService;
    @Autowired
    EventService eventService;
    @Autowired
    GuestService guestService;
    @Autowired
    MemberService memberService;
    @Autowired
    ActionService actionService;
    @Autowired
    UserDao userDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    BadgeService badgeService;
    @Autowired
    ProfileService profileService;
    @Autowired
    TeamService teamService;

    @Value("${room.bg.count}")
    int ROOM_BG_COUNT;


    /**
     * ????????? ?????????
     */
    public String callBroadCastRoomExit(P_RoomExitVo pRoomExitVo, HttpServletRequest request) {
        boolean isBj = false;
        boolean isGuest = false;
        String nickNm = "";

        if (!DalbitUtil.isEmpty(pRoomExitVo.getMem_no())) {
            if (!pRoomExitVo.getMem_no().startsWith("8")) {
                P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
                pRoomMemberInfoVo.setTarget_mem_no(pRoomExitVo.getMem_no());
                pRoomMemberInfoVo.setRoom_no(pRoomExitVo.getRoom_no());
                pRoomMemberInfoVo.setMem_no(pRoomExitVo.getMem_no());
                ProcedureVo procedureInfoVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo, request);
                if (!DalbitUtil.isEmpty(procedureInfoVo.getData())) {
                    isBj = DalbitUtil.getIntMap((HashMap) procedureInfoVo.getData(), "auth") == 3;
                    isGuest = DalbitUtil.getBooleanMap((HashMap) procedureInfoVo.getData(), "isGuest");
                    nickNm = DalbitUtil.getStringMap((HashMap) procedureInfoVo.getData(), "nickNm");
                }
            }
        } else {
            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_????????????));
        }

        HashMap returnMap = new HashMap();

        ProcedureVo procedureVo = new ProcedureVo(pRoomExitVo);
        log.error("callBroadcastRoomExit prev data(RoomService => callBroadCastRoomExit1) >>>> {} {} {}", pRoomExitVo.getMemLogin(), pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no());
        roomDao.callBroadCastRoomExit(procedureVo);

        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());
        log.info(" ### ???????????? ???????????? ###");

        String result;
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        if (procedureVo.getRet().equals(BroadcastStatus.???????????????.getMessageCode())) {
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);

            SocketVo vo = socketService.getSocketVo(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            if (isBj) {
                try {
                    socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
                }
            } else {

                HashMap fanRankMap = commonService.getKingFanRankList(pRoomExitVo.getRoom_no(), request);
                returnMap.put("fanRank", fanRankMap.get("list"));
                returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                returnMap.put("kingGender", fanRankMap.get("kingGender"));
                returnMap.put("kingAge", fanRankMap.get("kingAge"));
                returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                try {
                    if (!"0".equals(request.getParameter("isSocket"))) {
                        socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 1, DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    }
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
                }
                try {
                    if (resultMap.containsKey("good")) {
                        HashMap socketMap = new HashMap();
                        socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                        socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
                        socketMap.put("fanRank", returnMap.get("fanRank"));
                        //TODO - ????????? ?????? ???????????? ?????? ??????
                        // socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
                        socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                        socketService.changeCount(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    }
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
                }

                //????????? ????????? ?????? ?????? ??????
                if (isGuest) {
                    Status status = null;
                    HashMap selParams = new HashMap();
                    selParams.put("mem_no", pRoomExitVo.getMem_no());
                    selParams.put("room_no", pRoomExitVo.getRoom_no());
                    HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);

                    GuestInfoVo guestInfoVo = new GuestInfoVo();
                    guestInfoVo.setMode(6);
                    guestInfoVo.setMemNo(pRoomExitVo.getMem_no());
                    guestInfoVo.setNickNm(nickNm);
                    guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String) roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));

                    //P_RoomGuestVo apiData = new P_RoomGuestVo(DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), "", "", "", "", "", "", request);
                    //status = guestService.callBroadCastRoomGuestCancel(apiData, request);
                    //????????????
                    if (status == null || "success".equals(status.getResult())) {
                        try {
                            socketService.sendGuest(pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        } catch (Exception e) {
                        }
                    }

                }

            }

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
        } else {
            returnMap.put("isLevelUp", false);
            if (isBj) {
                if (procedureVo.getRet().equals(BroadcastStatus.???????????????_????????????.getMessageCode())) {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_????????????));
                } else if (procedureVo.getRet().equals(BroadcastStatus.???????????????_??????????????????.getMessageCode()) || procedureVo.getRet().equals(BroadcastStatus.???????????????_???????????????.getMessageCode()) || procedureVo.getRet().equals(BroadcastStatus.???????????????_??????????????????.getMessageCode())) {
                    SocketVo vo = socketService.getSocketVo(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    try {
                        socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    } catch (Exception e) {
                        log.info("Socket Service changeCount Exception {}", e);
                    }
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????));
                    //result = gsonUtil.toJson(new JsonOutputVo(Status.???????????????_??????????????????));
                    //} else if (procedureVo.getRet().equals(Status.???????????????_???????????????.getMessageCode())) {
                    //result = gsonUtil.toJson(new JsonOutputVo(Status.???????????????_???????????????));
                } else if (procedureVo.getRet().equals(BroadcastStatus.???????????????_??????????????????.getMessageCode())) {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
                } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????.getMessageCode())) {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????));
                } else {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????));
                }
            } else {
                //????????? ????????? ?????? ?????? ??????
                if (isGuest) {
                    Status status = null;
                    HashMap selParams = new HashMap();
                    selParams.put("mem_no", pRoomExitVo.getMem_no());
                    selParams.put("room_no", pRoomExitVo.getRoom_no());
                    HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);

                    GuestInfoVo guestInfoVo = new GuestInfoVo();
                    guestInfoVo.setMode(6);
                    guestInfoVo.setMemNo(pRoomExitVo.getMem_no());
                    guestInfoVo.setNickNm(nickNm);
                    guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String) roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));

                    //P_RoomGuestVo apiData = new P_RoomGuestVo(DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), "", "", "", "", "", "", request);
                    //status = guestService.callBroadCastRoomGuestCancel(apiData, request);
                    //????????????
                    if (status == null || "success".equals(status.getResult())) {
                        try {
                            socketService.sendGuest(pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        } catch (Exception e) {
                        }
                    }

                }

                //DB LOCK ????????? ?????? ????????? ????????? (????????? ????????? ?????? ??? -> ????????????(??????X, ??????????????? ??? ????????? ??????))
                if (DalbitUtil.getStringMap(resultMap, "desc").indexOf("Deadlock") > -1) {
                    log.error("callBroadcastRoomExit prev data(RoomService => callBroadCastRoomExit2) >>>> {} {} {}", pRoomExitVo.getMemLogin(), pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no());
                    roomDao.callBroadCastRoomExit(procedureVo);
                    if (procedureVo.getRet().equals(BroadcastStatus.???????????????.getMessageCode())) {
                        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
                        SocketVo vo = socketService.getSocketVo(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                        if (isBj) {
                            try {
                                socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                                vo.resetData();
                            } catch (Exception e) {
                                log.info("Socket Service changeCount Exception {}", e);
                            }
                        } else {
                            HashMap fanRankMap = commonService.getKingFanRankList(pRoomExitVo.getRoom_no(), request);
                            returnMap.put("fanRank", fanRankMap.get("list"));
                            returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                            returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                            returnMap.put("kingGender", fanRankMap.get("kingGender"));
                            returnMap.put("kingAge", fanRankMap.get("kingAge"));
                            returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                            try {
                                if (!"0".equals(request.getParameter("isSocket"))) {
                                    socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 1, DalbitUtil.isLogin(request), vo);
                                    vo.resetData();
                                }
                            } catch (Exception e) {
                                log.info("Socket Service changeCount Exception {}", e);
                            }
                            try {
                                if (resultMap.containsKey("good")) {
                                    HashMap socketMap = new HashMap();
                                    socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                                    socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
                                    socketMap.put("fanRank", returnMap.get("fanRank"));
                                    socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                                    socketService.changeCount(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                                    vo.resetData();
                                }
                            } catch (Exception e) {
                                log.info("Socket Service changeCount Exception {}", e);
                            }

                            //????????? ????????? ?????? ?????? ??????
                            if (isGuest) {
                                Status status = null;
                                HashMap selParams = new HashMap();
                                selParams.put("mem_no", pRoomExitVo.getMem_no());
                                selParams.put("room_no", pRoomExitVo.getRoom_no());
                                HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);

                                GuestInfoVo guestInfoVo = new GuestInfoVo();
                                guestInfoVo.setMode(6);
                                guestInfoVo.setMemNo(pRoomExitVo.getMem_no());
                                guestInfoVo.setNickNm(nickNm);
                                guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String) roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));

                                //????????????
                                if (status == null || "success".equals(status.getResult())) {
                                    try {
                                        socketService.sendGuest(pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                                    } catch (Exception e) {
                                    }
                                }

                            }

                        }
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
                    } else if (procedureVo.getRet().equals(BroadcastStatus.???????????????_????????????.getMessageCode())) {
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_????????????));
                    } else if (procedureVo.getRet().equals(BroadcastStatus.???????????????_??????????????????.getMessageCode())) {
                        roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
                    } else {
                        roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????));
                    }
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????));
                }
            }
        }
        return result;
    }

    public String callBroadCastRoomExitForce(P_RoomExitVo pRoomExitVo, HttpServletRequest request) {
        if (pRoomExitVo.getOs() == 3) {
            return callBroadCastRoomExit(pRoomExitVo, request);
        } else {
            try {
                socketService.chatEndRed(MemberVo.getMyMemNo(request), pRoomExitVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
            } catch (Exception e) {
            }
            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????));
        }
    }

    /**
     * ????????? ?????? ??????
     */
    public String callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo, HttpServletRequest request) throws GlobalException {

        String systemBanWord = commonService.banWordSelect();
        String titleBanWord = commonService.titleBanWordSelect();

        // ????????????????????? ?????? ( "\r", "\n", "\t")
        if (DalbitUtil.isCheckSlash(pRoomEditVo.getTitle())) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.?????????????????????));
        }

        //????????? ??????(??????)
        if (DalbitUtil.isStringMatchCheck(titleBanWord, pRoomEditVo.getTitle())) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.???????????????????????????));
        }

        //????????? ??????(?????????)
        if (DalbitUtil.isStringMatchCheck(systemBanWord, pRoomEditVo.getWelcomMsg())) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????????????????????????????));
        }

        Boolean isDone = false;
        if (!DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImage()) && pRoomEditVo.getBackgroundImage().startsWith(Code.??????_??????_??????_PREFIX.getCode())) {
            isDone = true;
            pRoomEditVo.setBackgroundImage(DalbitUtil.replacePath(pRoomEditVo.getBackgroundImage()));
        }

        ProcedureVo procedureVo = new ProcedureVo(pRoomEditVo);
        P_RoomEditOutVo pRoomEditOutVo = roomDao.callBroadCastRoomEdit(procedureVo);

        //???????????? ?????????????????? + ????????? ??? ?????? ?????? ??????
        //BroadcastSettingEditVo broadcastSettingEditVo = new BroadcastSettingEditVo();
        //broadcastSettingEditVo.setDjListenerIn(pRoomEditVo.getDjListenerIn());
        //broadcastSettingEditVo.setDjListenerOut(pRoomEditVo.getDjListenerOut());
        //P_BroadcastSettingEditVo pBroadcastSettingEditVo = new P_BroadcastSettingEditVo(broadcastSettingEditVo, request);
        //mypageService.callBroadcastSettingEdit(pBroadcastSettingEditVo, request, "edit");

        String result;
        if (procedureVo.getRet().equals(BroadcastStatus.????????????????????????.getMessageCode())) {
            if (DalbitUtil.isEmpty(pRoomEditOutVo.getImage_background())) {
                pRoomEditOutVo.setImage_background(Code.??????_??????_?????????_PREFIX.getCode() + "/" + Code.???????????????_?????????_PREFIX.getCode() + "200410.jpg");
            }

            String delImg = pRoomEditVo.getBackgroundImageDelete();
            if (!DalbitUtil.isEmpty(delImg) && delImg.startsWith(Code.??????_??????_?????????_PREFIX.getCode())) {
                delImg = null;
            }
            //Done ??????
            if (isDone) {
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomEditVo.getBackgroundImage()), delImg, request);
            }

            //???????????? ?????????????????? + ????????? ??? ?????? ?????? ??????
            P_BroadcastSettingVo pBroadcastSettingVo = new P_BroadcastSettingVo(request);
            HashMap settingMap = mypageService.callBroadcastSettingSelectRoomCreate(pBroadcastSettingVo);

            HashMap returnMap = new HashMap();
            returnMap.put("roomType", pRoomEditOutVo.getSubject_type());
            returnMap.put("title", pRoomEditOutVo.getTitle());
            returnMap.put("welcomMsg", pRoomEditOutVo.getMsg_welcom());
            returnMap.put("bgImg", new ImageVo(pRoomEditOutVo.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("bgImgRacy", DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImageGrade()) ? 0 : pRoomEditVo.getBackgroundImageGrade());
            returnMap.put("djListenerIn", DalbitUtil.getBooleanMap(settingMap, "djListenerIn"));
            returnMap.put("djListenerOut", DalbitUtil.getBooleanMap(settingMap, "djListenerOut"));
            returnMap.put("imageType", pRoomEditVo.getImageType());

            SocketVo vo = socketService.getSocketVo(pRoomEditOutVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try {
                socketService.changeRoomInfo(pRoomEditOutVo.getRoomNo(), MemberVo.getMyMemNo(request), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            } catch (Exception e) {
                log.info("Socket Service changeRoomInfo Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????, returnMap));
        } else if (procedureVo.getRet().equals(BroadcastStatus.??????????????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
        } else if (procedureVo.getRet().equals(BroadcastStatus.??????????????????_??????????????????????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_??????????????????????????????));
        } else if (procedureVo.getRet().equals(BroadcastStatus.??????????????????_????????????????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????));
        }

        return result;
    }

    /**
     * ????????? ?????????
     */
    // @Transactional(readOnly = true)
    public String callBroadCastRoomList(P_RoomListVo pRoomListVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);
        long st = (new Date()).getTime();
        List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);
        log.debug("select time {} ms", ((new Date()).getTime() - st));

        HashMap roomList = new HashMap();
        if (DalbitUtil.isEmpty(roomVoList)) {
            roomList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????, roomList));
        }

        st = (new Date()).getTime();
        List<RoomOutVo> outVoList = new ArrayList<>();
        DeviceVo deviceVo = new DeviceVo(request);
        for (int i = 0; i < roomVoList.size(); i++) {
            RoomOutVo roomOutVo = new RoomOutVo(roomVoList.get(i), deviceVo);

            /* main ???????????????????????? isContents ????????? isConDj ?????? ?????? */
            roomOutVo.setIsConDj(roomVoList.get(i).getIsContents() > 0);

            outVoList.add(roomOutVo);
        }
        log.debug("set list time {} ms", ((new Date()).getTime() - st));
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        roomList.put("list", procedureOutputVo.getOutputBox());
        roomList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomListVo.getPageNo(), pRoomListVo.getPageCnt()));
        //roomList.put("isGreenMoon", DalbitUtil.getIntMap(resultMap, "greenMoon") == 1);
        //roomList.put("isGreenMoon", false);

        log.info("???????????? ?????? ??????: {}", procedureOutputVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureOutputVo.getExt());
        log.info(" ### ???????????? ???????????? ###");

        String result;
        if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????, roomList));
        } else if (BroadcastStatus.???????????????_????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
        }
        return result;
    }


    /**
     * ?????? ????????????(????????? ??????,?????? ???)
     */
    public ProcedureOutputVo callBroadCastRoomInfoViewReturnVo(P_RoomInfoViewVo pRoomInfoViewVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomInfoViewVo);
        P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if (DalbitUtil.isEmpty(roomInfoViewVo)) {
            return new ProcedureOutputVo(procedureVo);
        } else {
            roomInfoViewVo.setExt(procedureVo.getExt());

            //???????????? ?????? ?????? ??????
            P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo(request);
            attendanceCheckVo.setMem_no(MemberVo.getMyMemNo(request));
            int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
            HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);
            //????????? ??????
            P_MoonCheckVo pMoonCheckVo = new P_MoonCheckVo();
            pMoonCheckVo.setRoom_no(pRoomInfoViewVo.getRoom_no());
            HashMap moonCheckMap = actionService.callMoonCheckMap(pMoonCheckVo, request);

            boolean isMiniGame = false;
            var minigame = commonService.selectCodeDefine(new CodeVo(Code.????????????_????????????.getCode(), Code.????????????_????????????.getDesc()));
            if (!DalbitUtil.isEmpty(minigame)) {
                if ("1".equals(minigame.getValue())) {
                    isMiniGame = true;
                }
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, new RoomOutVo(roomInfoViewVo, attendanceCheckMap, moonCheckMap, isMiniGame));
        }

        log.info("???????????? ?????? ??????: {}", procedureOutputVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureOutputVo.getExt());
        log.info(" ### ???????????? ???????????? ###");

        return procedureOutputVo;
    }


    /**
     * ???????????? DJ ??????
     */
    public ProcedureVo callMemberBroadcastingCheck(P_MemberBroadcastingCheckVo pMemberBroadcastingCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberBroadcastingCheckVo);
        roomDao.callMemberBroadcastingCheck(procedureVo);
        return procedureVo;
    }

    /**
     * ???????????? DJ ??????
     */
    public String callMemberBroadcastingCheck(HttpServletRequest request) {
        P_MemberBroadcastingCheckVo pMemberBroadcastingCheckVo = new P_MemberBroadcastingCheckVo();
        pMemberBroadcastingCheckVo.setMem_no(MemberVo.getMyMemNo(request));
        ProcedureVo procedureVo = new ProcedureVo(pMemberBroadcastingCheckVo);
        roomDao.callMemberBroadcastingCheck(procedureVo);
        String result = "";
        if (BroadcastStatus.????????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            //???????????? ??????
            ProcedureVo continueVo = new ProcedureVo(pMemberBroadcastingCheckVo);
            roomDao.callBroadCastRoomContinueCheck(continueVo);
            if ("0".equals(continueVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_???????????????));
            }
        } else if (BroadcastStatus.????????????????????????_?????????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            /*if(DalbitUtil.getIntMap(resultMap, "mem_state") != 0 && DalbitUtil.getIntMap(resultMap, "mem_state") != 2){ //???????????? ?????? ????????? tb_broadcast_room_member ???????????? ???????????? ????????? ?????? ?????? ??????
                //????????? ?????? ????????? ????????? ???????????? ?????? ???
                try{
                    SocketVo vo = socketService.getSocketVo(DalbitUtil.getStringMap(resultMap, "roomNo"), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    socketService.chatEnd(DalbitUtil.getStringMap(resultMap, "roomNo"), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                }catch(Exception e){}
                result = gsonUtil.toJson(new JsonOutputVo(Status.????????????????????????_???????????????));
            }else{*/
            returnMap.put("roomNo", DalbitUtil.getStringMap(resultMap, "roomNo"));
            returnMap.put("state", DalbitUtil.getIntMap(resultMap, "state"));
            returnMap.put("isWowza", DalbitUtil.getIntMap(resultMap, "isWowza"));
            result = (DalbitUtil.getIntMap(resultMap, "state") == 5) ? gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_?????????, returnMap)) : gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_?????????, returnMap));
            //}
        } else if (BroadcastStatus.????????????????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????));
        }

        return result;
    }

    /**
     * ????????? ?????? ??????, ????????? ?????? ?????? ??????
     */
    public String callBroadCastRoomLiveRankInfo(P_RoomLiveRankInfoVo pRoomLiveRankInfoVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomLiveRankInfo(procedureVo);

        String result = "";
        if (BroadcastStatus.?????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            int roomCnt = DalbitUtil.getIntMap(resultMap, "totalRoomCnt");
            int rank = DalbitUtil.getIntMap(resultMap, "rank");
            if (rank > roomCnt) {
                roomCnt = rank;
            }
            ItemDetailVo item = commonDao.selectItem(DalbitUtil.getProperty("item.code.boost"));
            returnMap.put("roomCnt", roomCnt);
            returnMap.put("rank", rank);
            returnMap.put("boostCnt", DalbitUtil.getIntMap(resultMap, "usedItemCnt"));
            returnMap.put("boostTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            returnMap.put("boostCode", DalbitUtil.getProperty("item.code.boost"));
            returnMap.put("boostPrice", item.getCost());
            returnMap.put("boostByeul", item.getByeol());
            returnMap.put("boostLottie", "https://image.dalbitlive.com/ani/booster/booster_popup_200519.json");
            returnMap.put("boostWebp", "https://image.dalbitlive.com/ani/booster/booster_popup_200519.webp");
            returnMap.put("boostItemCnt", DalbitUtil.getIntMap(resultMap, "haveItemCnt"));

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????, returnMap));
        } else if (BroadcastStatus.?????????????????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_???????????????));
        } else if (BroadcastStatus.?????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_???????????????));
        } else if (BroadcastStatus.?????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_???????????????));
        } else if (BroadcastStatus.?????????????????????_????????????_????????????????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_????????????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
        }

        return result;
    }

    private String getStatusString(ArrayList<Status> statusArrayList, Status error, String ret) {
        String result = "";
        for (Status status : statusArrayList) {
            String messageCode = status.getMessageCode();
            if (messageCode.equals(ret)) {
                result = gsonUtil.toJson(new JsonOutputVo(status));
                break;
            }
        }
        if (result.equals("")) {
            gsonUtil.toJson(new JsonOutputVo(error));
        }
        return result;
    }


    /**
     * ????????? ?????? ??????, ???????????? ????????? ??????
     */
    public String callBroadCastRoomChangeCount(P_RoomLiveRankInfoVo pRoomLiveRankInfoVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomChangeCount(procedureVo);

        String result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
        if (BroadcastStatus.?????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            try {
                HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

                SocketVo vo = socketService.getSocketVo(pRoomLiveRankInfoVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));

                HashMap fanRankMap = commonService.getKingFanRankList(pRoomLiveRankInfoVo.getRoom_no(), request);
                socketMap.put("fanRank", fanRankMap.get("list"));
                socketMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                socketMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                socketMap.put("kingGender", fanRankMap.get("kingGender"));
                socketMap.put("kingAge", fanRankMap.get("kingAge"));
                socketMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                socketService.changeCount(pRoomLiveRankInfoVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            } catch (Exception e) {
                log.info("Socket Service changeCount Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
        } else if (BroadcastStatus.?????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_???????????????));
        }

        return result;
    }

    /**
     * ????????? ???????????? ????????????
     */
    public String callBroadCastRoomGiftHistory(P_RoomGiftHistoryVo pRoomGiftHistoryVo) {
        pRoomGiftHistoryVo.setPageNo(1);
        pRoomGiftHistoryVo.setPageCnt(999999999);
        ProcedureVo procedureVo = new ProcedureVo(pRoomGiftHistoryVo);
        List<P_RoomGiftHistoryVo> giftHistoryListVo = roomDao.callBroadCastRoomGiftHistory(procedureVo);
        List<RoomGiftHistoryOutVo> outVoList = new ArrayList<>();
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);

        String result;
        if (Integer.parseInt(procedureOutputVo.getRet()) == 0 || DalbitUtil.isEmpty(giftHistoryListVo)) {
            HashMap giftHistoryList = new HashMap();
            giftHistoryList.put("list", new ArrayList<>());
            giftHistoryList.put("totalCnt", 0);
            giftHistoryList.put("totalGold", 0);
            giftHistoryList.put("paging", new PagingVo(0, pRoomGiftHistoryVo.getPageNo(), pRoomGiftHistoryVo.getPageCnt()));

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????, giftHistoryList));
        } else if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            HashMap giftHistoryList = new HashMap();
            for (int i = 0; i < giftHistoryListVo.size(); i++) {
                outVoList.add(new RoomGiftHistoryOutVo(giftHistoryListVo.get(i)));
            }

            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            giftHistoryList.put("totalCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
            giftHistoryList.put("totalGold", DalbitUtil.getIntMap(resultMap, "totalGold"));
            giftHistoryList.put("list", procedureOutputVo.getOutputBox());
            giftHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGiftHistoryVo.getPageNo(), pRoomGiftHistoryVo.getPageCnt()));

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????, giftHistoryList));
        } else if (BroadcastStatus.??????????????????_????????????????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????????????????));
        } else if (BroadcastStatus.??????????????????_???????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????????????????_??????));
        }

        return result;
    }

    /**
     * ????????? ???????????? ??????
     */
    public ProcedureVo getBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberInfoVo);
        roomDao.callBroadCastRoomMemberInfo(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", resultMap);
        log.info(" ### ???????????? ???????????? ###");

        int level = DalbitUtil.getIntMap(resultMap, "level");
        HashMap returnMap = new HashMap();
        returnMap.put("memNo", pRoomMemberInfoVo.getTarget_mem_no());
        returnMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
        returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
        returnMap.put("age", DalbitUtil.getIntMap(resultMap, "age"));
        returnMap.put("memId", DalbitUtil.getStringMap(resultMap, "memId"));
        returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("profMsg", DalbitUtil.getStringMap(resultMap, "profileMsg"));
        returnMap.put("holder", StringUtils.replace(DalbitUtil.getProperty("level.frame"), "[level]", level + ""));
        returnMap.put("holderBg", "");
        returnMap.put("profileBg", "");
        int l = (level - 1) / 10;
        returnMap.put("holderBg", DalbitUtil.getLevelFrameBg(level));
        returnMap.put("levelColor", DalbitUtil.getProperty("level.color." + l).split(","));
        returnMap.put("level", level);
        returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
        returnMap.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
        returnMap.put("expBegin", DalbitUtil.getIntMap(resultMap, "expBegin"));
        returnMap.put("expNext", DalbitUtil.getIntMap(resultMap, "expNext"));
        returnMap.put("expRate", DalbitUtil.getExpRate(DalbitUtil.getIntMap(resultMap, "exp"), DalbitUtil.getIntMap(resultMap, "expBegin"), DalbitUtil.getIntMap(resultMap, "expNext")));
        returnMap.put("fanCnt", DalbitUtil.getIntMap(resultMap, "fanCount"));
        returnMap.put("starCnt", DalbitUtil.getIntMap(resultMap, "starCount"));
        returnMap.put("isFan", DalbitUtil.getIntMap(resultMap, "enableFan") == 0);
        returnMap.put("auth", DalbitUtil.getIntMap(resultMap, "auth"));
        returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
        returnMap.put("state", DalbitUtil.getIntMap(resultMap, "state"));

        //????????? ????????????
        TeamParamVo vo = new TeamParamVo();
        TeamResultVo resTeam = new TeamResultVo();
        HashMap teamMap = new HashMap();
        vo.setMemNo(Long.parseLong(pRoomMemberInfoVo.getTarget_mem_no()));
        int teamNo = teamService.getTeamMemInsChk(vo);
        if (teamNo > 0) {
            String teamImgUrl = "https://image.dalbitlive.com/team/parts";
            vo.setTeamNo(teamNo);
            resTeam = teamService.getTeamSelService(vo);
            if (resTeam.getTeam_no() > 0) {
                teamMap.put("teamName", resTeam.getTeam_name());
                teamMap.put("medalUrl", teamImgUrl + "/M/" + resTeam.getTeam_medal_code() + ".png");
                teamMap.put("borderUrl", teamImgUrl + "/E/" + resTeam.getTeam_edge_code() + ".png");
                teamMap.put("backgroundUrl", teamImgUrl + "/B/" + resTeam.getTeam_bg_code() + ".png");
                teamMap.put("pageLink", "/team/detail/" + resTeam.getTeam_no() + "?webview=new");
                returnMap.put("teamInfo", teamMap);
            }
        }

        //????????? 1,2,3 ?????? ???????????? ??????
        P_FanRankVo pFanRankVo = new P_FanRankVo();
        pFanRankVo.setMem_no(pRoomMemberInfoVo.getTarget_mem_no());
        returnMap.put("fanRank", memberService.fanRank3(pFanRankVo));
        returnMap.put("isNew", DalbitUtil.getIntMap(resultMap, "newdj_badge") == 1);
        returnMap.put("isSpecial", DalbitUtil.getIntMap(resultMap, "specialdj_badge") > 0);
        returnMap.put("badgeSpecial", DalbitUtil.getIntMap(resultMap, "specialdj_badge"));
        FanBadgeVo fanBadgeVo = new FanBadgeVo(DalbitUtil.getStringMap(resultMap, "fanBadgeText"), DalbitUtil.getStringMap(resultMap, "fanBadgeIcon"), DalbitUtil.getStringMap(resultMap, "fanBadgeStartColor"), DalbitUtil.getStringMap(resultMap, "fanBadgeEndColor"));
        if (DalbitUtil.isEmpty(fanBadgeVo.getText())) {
            returnMap.put("fanBadge", "");
        } else {
            returnMap.put("fanBadge", fanBadgeVo);
        }

        if (badgeService.setBadgeInfo(pRoomMemberInfoVo.getTarget_mem_no(), -1)) {
            try {
                log.error("NULL ====> getBroadCastRoomMemberInfo -1 : getTarget_mem_no {}", pRoomMemberInfoVo.getTarget_mem_no());
                String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
                customHeader = java.net.URLDecoder.decode(customHeader);
                log.error(" NULL ====> getBroadCastRoomMemberInfo  customHeader : {}", customHeader);
                String referer = request.getHeader("Referer");
                log.error(" NULL ====> getBroadCastRoomMemberInfo  referer : {}", referer);
            } catch (Exception e) {
            }
        }

        returnMap.put("liveBadgeList", badgeService.getCommonBadge());
        returnMap.put("commonBadgeList", badgeService.getCommonBadge());
        returnMap.put("fanBadgeList", new ArrayList());

        returnMap.put("cupidMemNo", DalbitUtil.getStringMap(resultMap, "cupidMemNo"));
        returnMap.put("cupidNickNm", DalbitUtil.getStringMap(resultMap, "cupidNickNm"));
        returnMap.put("cupidProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "cupidProfileImage"), DalbitUtil.getStringMap(resultMap, "cupidMemSex"), DalbitUtil.getProperty("server.photo.url")));
        if (DalbitUtil.getIntMap(resultMap, "auth") == 0 || DalbitUtil.getIntMap(resultMap, "auth") == 1) {
            returnMap.put("isNewListener", DalbitUtil.getIntMap(resultMap, "new_badge") > 0);
        } else {
            returnMap.put("isNewListener", false);
        }

        returnMap.put("liveDjRank", DalbitUtil.getIntMap(resultMap, "liveDjRank"));
        returnMap.put("liveFanRank", DalbitUtil.getIntMap(resultMap, "liveFanRank"));
        returnMap.put("isGuest", DalbitUtil.getIntMap(resultMap, "isGuest") == 1);
        returnMap.put("managerType", DalbitUtil.getIntMap(resultMap, "managerType"));
        returnMap.put("likeTotCnt", DalbitUtil.getIntMap(resultMap, "receivedGoodTotal"));
        returnMap.put("specialDjCnt", DalbitUtil.getIntMap(resultMap, "specialDjCnt"));

        if (DalbitUtil.getIntMap(resultMap, "specialdj_badge") > 0) {
            returnMap.put("wasSpecial", false);
        } else {
            SpecialDjHistoryVo specialDjHistoryVo = new SpecialDjHistoryVo();
            specialDjHistoryVo.setMemNo(pRoomMemberInfoVo.getTarget_mem_no());
            returnMap.put("wasSpecial", memberService.getSpecialCnt(specialDjHistoryVo) > 0);
        }

        returnMap.put("isReceive", DalbitUtil.getIntMap(resultMap, "alertYn") == 1);

        //????????? ??????????????? ?????????
        HashMap imgListMap = profileService.callProfImgList(pRoomMemberInfoVo.getTarget_mem_no(), request);
        returnMap.put("profImgList", imgListMap.get("list"));

        returnMap.put("isMailboxOn", DalbitUtil.getIntMap(resultMap, "mailboxOnOff") == 1);

        procedureVo.setData(returnMap);
        return procedureVo;
    }

    /**
     * ????????? ???????????? ??????
     */
    public String callBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo, HttpServletRequest request) {
        ProcedureVo procedureVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo, request);

        String result = "";
        if (BroadcastStatus.???????????????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_??????, procedureVo.getData()));
        } else if (BroadcastStatus.???????????????????????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????_???????????????));
        } else if (BroadcastStatus.???????????????????????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????_???????????????));
        } else if (BroadcastStatus.???????????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????));
        } else if (BroadcastStatus.???????????????????????????_????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????_????????????));
        } else if (BroadcastStatus.???????????????????????????_????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_????????????_????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????????????????_??????));
        }

        return result;
    }

    /**
     * ????????? ?????? ?????????
     */
    public String callBroadcastRoomStreamSelect(P_RoomStreamVo pRoomStreamVo, HttpServletRequest request) throws GlobalException {
        ProcedureVo procedureVo = new ProcedureVo(pRoomStreamVo);
        roomDao.callBroadcastRoomStreamSelect(procedureVo);

        String result = "";
        if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            int auth = DalbitUtil.getIntMap(resultMap, "auth");
            String bjStreamId = DalbitUtil.getStringMap(resultMap, "bj_streamid");
            String bjPubToken = "";
            String bjPlayToken = "";
            String gstStreamId = DalbitUtil.getStringMap(resultMap, "guest_streamid");
            String gstPubToken = "";
            String gstPlayToken = "";

            P_RoomStreamTokenVo pRoomStreamTokenVo = new P_RoomStreamTokenVo();
            pRoomStreamTokenVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
            pRoomStreamTokenVo.setMem_no(MemberVo.getMyMemNo(request));
            pRoomStreamTokenVo.setRoom_no(pRoomStreamVo.getRoom_no());
            pRoomStreamTokenVo.setBj_publish_tokenid(bjPubToken);
            pRoomStreamTokenVo.setBj_play_tokenid(bjPlayToken);
            pRoomStreamTokenVo.setGuest_publish_tokenid(gstPubToken);
            pRoomStreamTokenVo.setGuest_play_tokenid(gstPlayToken);
            procedureVo.setData(pRoomStreamTokenVo);
            ProcedureVo procedureUpdateVo = new ProcedureVo(pRoomStreamTokenVo);

            //?????? ????????????
            roomDao.callBroadcastRoomTokenUpdate(procedureUpdateVo);

            if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                HashMap resultUpdateMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

                log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
                log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());

                P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
                pRoomInfoViewVo.setMemLogin(pRoomStreamTokenVo.getMemLogin());
                pRoomInfoViewVo.setMem_no(pRoomStreamTokenVo.getMem_no());
                pRoomInfoViewVo.setRoom_no(pRoomStreamTokenVo.getRoom_no());

                //????????? ?????? ??????
                ProcedureOutputVo roomInfoVo = roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);
                if (BroadcastStatus.???????????????.getMessageCode().equals(roomInfoVo.getRet())) {
                    log.info(" ????????? ?????? ?????? {}", roomInfoVo.getOutputBox());
                    RoomOutVo target = (RoomOutVo) roomInfoVo.getOutputBox();

                    HashMap returnMap = new HashMap();
                    returnMap.put("roomNo", pRoomStreamVo.getRoom_no());
                    returnMap.put("roomType", target.getRoomType());
                    returnMap.put("welcomMsg", target.getWelcomMsg());
                    returnMap.put("entryType", target.getEntryType());
                    returnMap.put("bjStreamId", bjStreamId);
                    returnMap.put("bjPubToken", bjPubToken);
                    returnMap.put("bjPlayToken", bjPlayToken);
                    returnMap.put("gstStreamId", gstStreamId);
                    returnMap.put("gstPubToken", gstPubToken);
                    returnMap.put("gstPlayToken", gstPlayToken);
                    returnMap.put("title", target.getTitle());
                    returnMap.put("bgImg", target.getBgImg());
                    returnMap.put("link", target.getLink());
                    returnMap.put("state", target.getState());
                    returnMap.put("bjMemNo", target.getBjMemNo());
                    returnMap.put("bjMemId", target.getBjMemId());
                    returnMap.put("bjNickNm", target.getBjNickNm());
                    returnMap.put("bjProfImg", target.getBjProfImg());
                    returnMap.put("bjHolder", StringUtils.replace(DalbitUtil.getProperty("level.frame"), "[level]", target.getBjLevel() + ""));
                    returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
                    returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
                    returnMap.put("gstNickId", target.getGstMemId() == null ? "" : target.getGstMemId());
                    returnMap.put("gstProfImg", target.getGstProfImg());
                    returnMap.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
                    returnMap.put("likes", DalbitUtil.getIntMap(resultUpdateMap, "good"));
                    returnMap.put("rank", DalbitUtil.getIntMap(resultUpdateMap, "rank"));
                    returnMap.put("auth", DalbitUtil.getIntMap(resultUpdateMap, "auth"));
                    returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultUpdateMap, "controlRole"));
                    returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultUpdateMap, "isFan")));
                    returnMap.put("isLike", (DalbitUtil.isLogin(request)) ? "1".equals(DalbitUtil.getStringMap(resultUpdateMap, "isGood")) : true);
                    returnMap.put("isRecomm", target.getIsRecomm());
                    returnMap.put("isPop", target.getIsPop());
                    returnMap.put("isSpecial", target.getIsSpecial());
                    returnMap.put("badgeSpecial", target.getBadgeSpecial());
                    returnMap.put("isNew", target.getIsNew());
                    returnMap.put("startDt", target.getStartDt());
                    returnMap.put("startTs", target.getStartTs());
                    returnMap.put("hasNotice", DalbitUtil.getIntMap(resultUpdateMap, "auth") == 3 ? false : !DalbitUtil.isEmpty(target.getNotice()));
                    returnMap.put("hasStory", getHasStory(auth, pRoomStreamVo.getRoom_no(), MemberVo.getMyMemNo(request), request));

                    returnMap.put("useBoost", existsBoostByRoom(pRoomStreamVo.getRoom_no(), pRoomStreamVo.getMem_no()));    //????????? ????????????

                    HashMap fanRankMap = commonService.getKingFanRankList(pRoomStreamVo.getRoom_no(), request);
                    returnMap.put("fanRank", fanRankMap.get("list"));
                    returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                    returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                    returnMap.put("kingGender", fanRankMap.get("kingGender"));
                    returnMap.put("kingAge", fanRankMap.get("kingAge"));
                    returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                    HashMap fanBadgeMap = new HashMap();
                    fanBadgeMap.put("mem_no", target.getBjMemNo());
                    fanBadgeMap.put("type", 3);
                    fanBadgeMap.put("by", "api");
                    List fanBadgeList = commonDao.callMemberBadgeSelect(fanBadgeMap);
                    if (DalbitUtil.isEmpty(fanBadgeList)) {
                        returnMap.put("fanBadgeList", new ArrayList());
                    } else {
                        returnMap.put("fanBadgeList", fanBadgeList);
                    }

                    /*returnMap.put("level", target.getLevel());
                    returnMap.put("grade", target.getGrade());
                    returnMap.put("exp", target.getExp());
                    returnMap.put("expNext", target.getExpNext());
                    returnMap.put("dalCnt", target.getRubyCnt());
                    returnMap.put("byeolCnt", target.getGoldCnt());*/
                    DeviceVo deviceVo = new DeviceVo(request);
                    returnMap.put("antOrigin", "");
                    returnMap.put("antEdge", "");

                    //???????????? ?????? ?????? ??????
                    P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo(request);
                    attendanceCheckVo.setMem_no(MemberVo.getMyMemNo(request));
                    int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
                    HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);
                    returnMap.put("isCheck", attendanceCheckMap.get("isCheck"));

                    log.info("returnMap: {}", returnMap);

                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
                } else if (BroadcastStatus.???????????????_??????????????????.getMessageCode().equals(roomInfoVo.getRet())) {
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
                } else if (BroadcastStatus.???????????????_???????????????.getMessageCode().equals(roomInfoVo.getRet())) {
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_???????????????));
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????));
                }
            } else if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
            } else if (BroadcastStatus.??????????????????_???????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_???????????????));
            } else if (BroadcastStatus.??????????????????_????????????_???????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????_???????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
            }
        } else if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
        } else if (BroadcastStatus.??????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_???????????????));
        } else if (BroadcastStatus.??????????????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
        }

        return result;
    }

    /**
     * ????????? ?????? ??????
     */
    public String callBroadCastRoomStateUpate(StateVo stateVo, HttpServletRequest request) {
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        pRoomInfoViewVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomInfoViewVo.setRoom_no(stateVo.getRoomNo());
        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);

        String result = "";
        if (procedureOutputVo.getRet().equals(BroadcastStatus.???????????????.getMessageCode())) {
            RoomOutVo target = (RoomOutVo) procedureOutputVo.getOutputBox();
            int old_state = target.getState();
            P_RoomStateUpdateVo pRoomStateUpdateVo = new P_RoomStateUpdateVo();
            pRoomStateUpdateVo.setMem_no(MemberVo.getMyMemNo(request));
            pRoomStateUpdateVo.setRoom_no(stateVo.getRoomNo());
            int state = 1;
            if ("0".equals(stateVo.getIsAnt()) || "FALSE".equals(stateVo.getIsAnt().toUpperCase())) {
                state = 0;
            } else if ("1".equals(stateVo.getIsCall()) || "TRUE".equals(stateVo.getIsCall().toUpperCase())) {
                state = 3;
            } else if ("0".equals(stateVo.getIsMic()) || "FALSE".equals(stateVo.getIsMic().toUpperCase())) {
                state = 2;
            }
            pRoomStateUpdateVo.setState(state);

            if (old_state == state) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }

            ProcedureVo procedureVo = new ProcedureVo(pRoomStateUpdateVo);
            roomDao.callBroadCastRoomStateUpate(procedureVo);

            log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
            log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());
            log.info(" ### ???????????? ???????????? ###");

            if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_??????.getMessageCode())) {
                SocketVo vo = socketService.getSocketVo(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                try {
                    DeviceVo deviceVo = new DeviceVo(request);
                    /*
                        ????????? ?????? top1?????? ????????????
                        ?????? ?????????,??????,?????? ???????????? ?????? ????????? ??????
                        ?????? ?????????,??????,?????? ???????????? ?????? ??????
                    */
                    if (deviceVo.getOs() == 3 || deviceVo.getOs() == 1
                            || (deviceVo.getOs() == 2 && Integer.parseInt(deviceVo.getAppBuild()) < 91) //IOS????????? ???????????? 91?????? ????????? ??????
                    ) {
                        socketService.changeRoomState(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), old_state, state, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, stateVo.getIsAnt());
                    } else {
                        socketService.changeRoomState(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), old_state, stateVo.getIsAnt(), stateVo.getIsCall(), stateVo.getIsMic(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                    }
                    vo.resetData();
                } catch (Exception e) {
                    log.info("Socket Service changeRoomState Exception {}", e);
                }
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_??????????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_?????????????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_?????????????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????_???????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_???????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????_????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }
        } else if (BroadcastStatus.???????????????_??????????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
        } else if (BroadcastStatus.???????????????_???????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????));
        }

        return result;
    }

    public String callAntRefresh(P_RoomStreamVo pRoomStreamVo, HttpServletRequest request) throws GlobalException {
        String result = "";
        //????????? ??????
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(pRoomStreamVo.getMemLogin());
        pRoomInfoViewVo.setMem_no(pRoomStreamVo.getMem_no());
        pRoomInfoViewVo.setRoom_no(pRoomStreamVo.getRoom_no());
        ProcedureOutputVo roomInfoVo = roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);

        if (BroadcastStatus.???????????????.getMessageCode().equals(roomInfoVo.getRet())) {
            ProcedureVo procedureVo = new ProcedureVo(pRoomStreamVo);
            roomDao.callBroadcastRoomStreamSelect(procedureVo);

            if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
                RoomOutVo target = (RoomOutVo) roomInfoVo.getOutputBox();
                HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                int auth = DalbitUtil.getIntMap(resultMap, "auth");
                String bjStreamId = DalbitUtil.getStringMap(resultMap, "bj_streamid");
                String oldBjStreamId = bjStreamId;
                String bjPubToken = "";
                String bjPlayToken = "";
                String gstStreamId = "";
                String gstPubToken = "";
                String gstPlayToken = "";

                P_RoomStreamTokenVo pRoomStreamTokenVo = new P_RoomStreamTokenVo();
                pRoomStreamTokenVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
                pRoomStreamTokenVo.setMem_no(MemberVo.getMyMemNo(request));
                pRoomStreamTokenVo.setRoom_no(pRoomStreamVo.getRoom_no());
                pRoomStreamTokenVo.setGuest_streamid("");
                pRoomStreamTokenVo.setGuest_publish_tokenid("");
                pRoomStreamTokenVo.setGuest_play_tokenid("");

                HashMap returnMap = new HashMap();
                returnMap.put("roomNo", pRoomStreamVo.getRoom_no());
                returnMap.put("title", target.getTitle());
                returnMap.put("bgImg", target.getBgImg());
                returnMap.put("link", target.getLink());
                returnMap.put("state", target.getState());
                returnMap.put("bjMemNo", target.getBjMemNo());
                returnMap.put("bjNickNm", target.getBjNickNm());
                returnMap.put("bjProfImg", target.getBjProfImg());
                returnMap.put("bjHolder", StringUtils.replace(DalbitUtil.getProperty("level.frame"), "[level]", target.getBjLevel() + ""));
                returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
                returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
                returnMap.put("gstProfImg", target.getGstProfImg());
                returnMap.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
                returnMap.put("hasStory", getHasStory(auth, pRoomStreamVo.getRoom_no(), MemberVo.getMyMemNo(request), request));
                returnMap.put("useBoost", existsBoostByRoom(pRoomStreamVo.getRoom_no(), pRoomStreamVo.getMem_no()));    //????????? ????????????
                returnMap.put("isRecomm", target.getIsRecomm());
                returnMap.put("isPop", target.getIsPop());
                returnMap.put("isNew", target.getIsNew());
                returnMap.put("isSpecial", target.getIsSpecial());
                returnMap.put("badgeSpecial", target.getBadgeSpecial());
                returnMap.put("startDt", target.getStartDt());
                returnMap.put("startTs", target.getStartTs());
                returnMap.put("auth", auth);
                returnMap.put("hasNotice", auth == 3 ? false : !DalbitUtil.isEmpty(target.getNotice()));
                returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
                returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
                returnMap.put("isLike", (DalbitUtil.isLogin(request)) ? "1".equals(DalbitUtil.getStringMap(resultMap, "isGood")) : true);
                DeviceVo deviceVo = new DeviceVo(request);
                returnMap.put("antOrigin", "");
                returnMap.put("antEdge", "");

                HashMap fanBadgeMap = new HashMap();
                fanBadgeMap.put("mem_no", target.getBjMemNo());
                fanBadgeMap.put("type", 3);
                fanBadgeMap.put("by", "api");
                List fanBadgeList = commonDao.callMemberBadgeSelect(fanBadgeMap);
                if (DalbitUtil.isEmpty(fanBadgeList)) {
                    returnMap.put("fanBadgeList", new ArrayList());
                } else {
                    returnMap.put("fanBadgeList", fanBadgeList);
                }

                try {
                    if (auth == 3) { // DJ
                        pRoomStreamTokenVo.setBj_streamid(bjStreamId);
                        pRoomStreamTokenVo.setBj_publish_tokenid(bjPubToken);
                        //pRoomStreamTokenVo.setState("0");
                    } else {
                        pRoomStreamTokenVo.setBj_play_tokenid(bjPlayToken);
                    }

                    returnMap.put("bjStreamId", bjStreamId);
                    returnMap.put("bjPubToken", bjPubToken);
                    returnMap.put("bjPlayToken", bjPlayToken);
                    returnMap.put("gstStreamId", gstStreamId);
                    returnMap.put("gstPubToken", gstPubToken);
                    returnMap.put("gstPlayToken", gstPlayToken);

                    procedureVo.setData(pRoomStreamTokenVo);
                    ProcedureVo procedureUpdateVo = new ProcedureVo(pRoomStreamTokenVo);

                    //?????? ????????????
                    roomDao.callBroadcastRoomTokenUpdate(procedureUpdateVo);
                    if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                        HashMap resultUpdateMap = new Gson().fromJson(procedureUpdateVo.getExt(), HashMap.class);

                        returnMap.put("likes", DalbitUtil.getIntMap(resultUpdateMap, "good"));
                        returnMap.put("rank", DalbitUtil.getIntMap(resultUpdateMap, "rank"));

                        HashMap fanRankMap = commonService.getKingFanRankList(pRoomStreamVo.getRoom_no(), request);
                        returnMap.put("fanRank", fanRankMap.get("list"));
                        returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                        returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                        returnMap.put("kingGender", fanRankMap.get("kingGender"));
                        returnMap.put("kingAge", fanRankMap.get("kingAge"));
                        returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
                    } else if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
                    } else if (BroadcastStatus.??????????????????_???????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_???????????????));
                    } else if (BroadcastStatus.??????????????????_????????????_???????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????_???????????????));
                    } else {
                        result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
                    }
                } catch (Exception e) {
                    bjPubToken = DalbitUtil.getStringMap(resultMap, "bj_publish_tokenid");
                    bjPlayToken = DalbitUtil.getStringMap(resultMap, "bj_play_tokenid");
                    gstStreamId = DalbitUtil.getStringMap(resultMap, "guest_streamid");
                    gstPubToken = DalbitUtil.getStringMap(resultMap, "guset_publish_tokenid");
                    gstPlayToken = DalbitUtil.getStringMap(resultMap, "guest_play_tokenid");

                    returnMap.put("bjStreamId", bjStreamId);
                    returnMap.put("bjPubToken", bjPubToken);
                    returnMap.put("bjPlayToken", bjPlayToken);
                    returnMap.put("gstStreamId", gstStreamId);
                    returnMap.put("gstPubToken", gstPubToken);
                    returnMap.put("gstPlayToken", gstPlayToken);

                    P_RoomLiveRankInfoVo pRoomLiveRankInfoVo = new P_RoomLiveRankInfoVo();
                    pRoomLiveRankInfoVo.setRoom_no(pRoomStreamVo.getRoom_no());
                    pRoomLiveRankInfoVo.setMem_no(pRoomStreamVo.getMem_no());
                    ProcedureVo procedureExceptionVo = new ProcedureVo(pRoomLiveRankInfoVo);
                    roomDao.callBroadCastRoomChangeCount(procedureExceptionVo);

                    if (BroadcastStatus.?????????????????????_????????????.getMessageCode().equals(procedureExceptionVo.getRet())) {
                        HashMap resultCountMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

                        returnMap.put("likes", DalbitUtil.getIntMap(resultCountMap, "good"));
                        returnMap.put("rank", DalbitUtil.getIntMap(resultCountMap, "rank"));
                        HashMap fanRankMap = commonService.getKingFanRankList(pRoomStreamVo.getRoom_no(), request);
                        returnMap.put("fanRank", fanRankMap.get("list"));
                        returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                        returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                        returnMap.put("kingGender", fanRankMap.get("kingGender"));
                        returnMap.put("kingAge", fanRankMap.get("kingAge"));
                        returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));
                    } else {
                        returnMap.put("likes", 0);
                        returnMap.put("rank", 0);
                        returnMap.put("fanRank", new ArrayList<>());
                    }

                    result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
                }
            } else if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
            } else if (BroadcastStatus.??????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_???????????????));
            } else if (BroadcastStatus.??????????????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????_???????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????_????????????));
            }
        } else if (BroadcastStatus.???????????????_??????????????????.getMessageCode().equals(roomInfoVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
        } else if (BroadcastStatus.???????????????_???????????????.getMessageCode().equals(roomInfoVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????));
        }
        return result;
    }

    /**
     * ????????? ????????????
     */
    public boolean existsBoostByRoom(String roomNo, String memNo) {
        P_RoomLiveRankInfoVo pRoomLiveRankInfoVo = new P_RoomLiveRankInfoVo();
        pRoomLiveRankInfoVo.setRoom_no(roomNo);
        pRoomLiveRankInfoVo.setMem_no(memNo);

        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomLiveRankInfo(procedureVo);

        if (BroadcastStatus.?????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            return DalbitUtil.getIntMap(resultMap, "usedItemCnt") > 0;
        }
        return false;
    }

    public boolean getHasStory(int auth, String room_no, String mem_no, HttpServletRequest request) {
        if (auth == 3) { // DJ
            //????????????
            P_RoomStoryListVo apiData = new P_RoomStoryListVo();
            apiData.setMem_no(mem_no);
            apiData.setRoom_no(room_no);
            apiData.setPageNo(1);
            apiData.setPageCnt(1);

            String resultStory = contentService.callGetStory(apiData, "", request);
            HashMap storyMap = new Gson().fromJson(resultStory, HashMap.class);
            if (storyMap.containsKey("result") && "success".equals(storyMap.get("result").toString()) && storyMap.containsKey("data")) {
                try {
                    HashMap storyDataMap = new Gson().fromJson(storyMap.get("data").toString(), HashMap.class);
                    if (storyDataMap.containsKey("paging")) {
                        HashMap storyPagingMap = new Gson().fromJson(storyDataMap.get("paging").toString(), HashMap.class);
                        return DalbitUtil.getIntMap(storyPagingMap, "total") > 0;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * ??????????????????(?????????)
     */
    public HashMap getSummaryListener(RoomExitVo roomExitVo, HttpServletRequest request) {
        HashMap result = new HashMap();
        P_SummaryListenerVo pSummaryListenerVo = new P_SummaryListenerVo(roomExitVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pSummaryListenerVo);
        List<P_SummaryListenerVo> recommList = roomDao.callBroadcastSummaryListener(procedureVo);
        if (BroadcastStatus.??????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap data = new HashMap();
            HashMap returnMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            List<SummaryListenerOutVo> recommends = new ArrayList<>();
            List<SummaryListenerOutVo> myStars = new ArrayList<>();
            data.put("djMemNo", DalbitUtil.getStringMap(returnMap, "dj_mem_no"));
            data.put("djNickNm", DalbitUtil.getStringMap(returnMap, "nickName"));
            data.put("djProfImg", new ImageVo(DalbitUtil.getStringMap(returnMap, "image_profile"), DalbitUtil.getStringMap(returnMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            data.put("djHolder", StringUtils.replace(DalbitUtil.getProperty("level.frame"), "[level]", DalbitUtil.getStringMap(returnMap, "level")));
            data.put("listenTime", DalbitUtil.getIntMap(returnMap, "listentime"));
            data.put("bgImg", new ImageVo(DalbitUtil.getStringMap(returnMap, "image_background"), DalbitUtil.getProperty("server.photo.url")));
            data.put("isFan", DalbitUtil.getIntMap(returnMap, "fan") > 0);
            data.put("bannerImgUrl", DalbitUtil.getStringMap(returnMap, "bannerImgUrl"));
            if (!DalbitUtil.isEmpty(recommList)) {
                for (P_SummaryListenerVo items : recommList) {
                    recommends.add(new SummaryListenerOutVo(items, DalbitUtil.getProperty("server.photo.url")));
                }
            }
            List<P_SummaryListenerVo> myStarList = roomDao.callBroadcastSummaryListenerMyStar(procedureVo);
            if (!DalbitUtil.isEmpty(myStarList)) {
                for (P_SummaryListenerVo items : myStarList) {
                    myStars.add(new SummaryListenerOutVo(items, DalbitUtil.getProperty("server.photo.url")));
                }
            }
            data.put("recommends", recommends);
            data.put("myStars", myStars);
            result.put("status", BroadcastStatus.??????????????????_??????);
            result.put("data", data);
        } else if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", BroadcastStatus.??????????????????_????????????);
        } else if (BroadcastStatus.??????????????????_?????????.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", BroadcastStatus.??????????????????_?????????);
        } else {
            result.put("status", BroadcastStatus.??????????????????_??????);
        }
        return result;
    }

    /**
     * ??????????????????(DJ)
     */
    public HashMap getSummaryDj(RoomExitVo roomExitVo, HttpServletRequest request) {
        HashMap result = new HashMap();
        P_SummaryListenerVo pSummaryListenerVo = new P_SummaryListenerVo(roomExitVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pSummaryListenerVo);
        List<P_SummaryDjVo> resultSet = roomDao.callBroadcastSummaryDj(procedureVo);
        if (BroadcastStatus.??????????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("roomNo", DalbitUtil.getStringMap(resultMap, "room_no"));
            data.put("memNo", DalbitUtil.getStringMap(resultMap, "mem_no"));
            data.put("title", DalbitUtil.getStringMap(resultMap, "title"));
            data.put("nickNm", DalbitUtil.getStringMap(resultMap, "mem_nick"));
            data.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "image_profile"), DalbitUtil.getStringMap(resultMap, "mem_sex"), DalbitUtil.getProperty("server.photo.url")));
            data.put("holder", StringUtils.replace(DalbitUtil.getProperty("level.frame"), "[level]", DalbitUtil.getIntMap(resultMap, "level") + ""));
            data.put("airTime", DalbitUtil.getIntMap(resultMap, "airtime"));
            data.put("listenerCnt", DalbitUtil.getIntMap(resultMap, "count_entry"));
            data.put("listenerMax", DalbitUtil.getIntMap(resultMap, "count_live_max"));
            data.put("gift", DalbitUtil.getIntMap(resultMap, "gold"));
            data.put("good", DalbitUtil.getIntMap(resultMap, "good"));
            data.put("fanCnt", DalbitUtil.getIntMap(resultMap, "fanCnt"));
            data.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
            data.put("bgImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "image_background"), DalbitUtil.getProperty("server.photo.url")));
            data.put("bannerImgUrl", DalbitUtil.getStringMap(resultMap, "bannerImgUrl"));
            List<SummaryDjOutVo> list = new ArrayList<>();
            for (P_SummaryDjVo item : resultSet) {
                list.add(new SummaryDjOutVo(item, DalbitUtil.getProperty("server.photo.url")));
            }
            data.put("list", list);
            result.put("status", BroadcastStatus.??????????????????_??????);
            result.put("data", data);
        } else if (BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", BroadcastStatus.??????????????????_????????????);
        } else if (BroadcastStatus.??????????????????_?????????.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", BroadcastStatus.??????????????????_?????????);
        } else {
            result.put("status", BroadcastStatus.??????????????????_??????);
        }
        return result;
    }


    /**
     * ????????? ??????
     */
    public String getGoodHistory(P_RoomGoodHistoryVo pRoomGoodHistoryVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodHistoryVo);
        List<P_RoomGoodHistoryVo> goodHistoryListVo = roomDao.callGetGoodHistory(procedureVo);

        String result;
        if (DalbitUtil.isEmpty(goodHistoryListVo)) {
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            int totalCnt = 0;
            //??????????????? 1.3.6 ?????? ??????
            DeviceVo deviceVo = new DeviceVo(request);
            if ((deviceVo.getOs() == 1 && DalbitUtil.versionCompare("1.3.6", deviceVo.getAppVersion()))) {
                try {
                    HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                    totalCnt = DalbitUtil.getIntMap(resultMap, "totalCnt");
                } catch (Exception e) {

                }
            }

            HashMap goodHistoryList = new HashMap();
            goodHistoryList.put("list", new ArrayList<>());
            goodHistoryList.put("totalCnt", Integer.valueOf(procedureOutputVo.getRet()));
            if (deviceVo.getOs() == 2) {
                goodHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGoodHistoryVo.getPageNo(), pRoomGoodHistoryVo.getPageCnt()));
            } else {
                goodHistoryList.put("paging", new PagingVo(totalCnt, pRoomGoodHistoryVo.getPageNo(), pRoomGoodHistoryVo.getPageCnt()));
            }

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????, goodHistoryList));
        } else if (Integer.parseInt(procedureVo.getRet()) > 0) {
            List<RoomGoodHistoryOutVo> outVoList = new ArrayList<>();
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
            HashMap goodHistoryList = new HashMap();
            for (int i = 0; i < goodHistoryListVo.size(); i++) {
                outVoList.add(new RoomGoodHistoryOutVo(goodHistoryListVo.get(i)));
            }

            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            goodHistoryList.put("totalCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
            goodHistoryList.put("list", procedureOutputVo.getOutputBox());
            goodHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGoodHistoryVo.getPageNo(), pRoomGoodHistoryVo.getPageCnt()));

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????, goodHistoryList));
        } else if (BroadcastStatus.?????????????????????_????????????????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????????????????));
        } else if (BroadcastStatus.?????????????????????_???????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_???????????????));
        } else if (BroadcastStatus.?????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
        } else if (BroadcastStatus.?????????????????????_????????????.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
        }

        return result;
    }

    /**
     * ????????? ?????? ??????(????????????)
     */
    public String callRoomStateEdit(StateEditVo stateEditVo, HttpServletRequest request) {
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        pRoomInfoViewVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomInfoViewVo.setRoom_no(stateEditVo.getRoomNo());
        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);

        String result = "";
        if (procedureOutputVo.getRet().equals(BroadcastStatus.???????????????.getMessageCode())) {
            RoomOutVo target = (RoomOutVo) procedureOutputVo.getOutputBox();
            P_RoomStateUpdateVo pRoomStateUpdateVo = new P_RoomStateUpdateVo();
            pRoomStateUpdateVo.setMem_no(MemberVo.getMyMemNo(request));
            pRoomStateUpdateVo.setRoom_no(stateEditVo.getRoomNo());

            boolean isGuest = false;

            //????????? ???????????? ??????
            P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
            pRoomMemberInfoVo.setTarget_mem_no(MemberVo.getMyMemNo(request));
            pRoomMemberInfoVo.setRoom_no(stateEditVo.getRoomNo());
            pRoomMemberInfoVo.setMem_no(MemberVo.getMyMemNo(request));
            ProcedureVo procedureInfoVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo, request);
            if (!DalbitUtil.isEmpty(procedureInfoVo.getData())) {
                isGuest = DalbitUtil.getBooleanMap((HashMap) procedureInfoVo.getData(), "isGuest");
            }

            int state = 1;
            //?????? ????????? ????????????
            if (isGuest) {
                return callRoomStateGuestEdit(stateEditVo, request);
            } else {
                if ("mic".equals(stateEditVo.getMediaState().toLowerCase())) {
                    if (target.isMic() == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                        return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
                    }
                    state = ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn())) ? 1 : 2;
                } else if ("call".equals(stateEditVo.getMediaState().toLowerCase())) {
                    if (target.isCall() == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                        return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
                    }
                    state = ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn())) ? 3 : 6;
                } else if ("video".equals(stateEditVo.getMediaState().toLowerCase())) {
                    if (target.isVideo() == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                        return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
                    }
                    state = ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn())) ? 8 : 9;
                } else if ("server".equals(stateEditVo.getMediaState().toLowerCase())) {
                    if (target.isServer() == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                        return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
                    }
                    state = ("FALSE".equals(stateEditVo.getMediaOn().toUpperCase()) || "0".equals(stateEditVo.getMediaOn())) ? 0 : 7;
                }
            }
            pRoomStateUpdateVo.setState(state);
            ProcedureVo procedureVo = new ProcedureVo(pRoomStateUpdateVo);
            roomDao.callBroadCastRoomStateEdit(procedureVo);
            if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_??????.getMessageCode())) {
                SocketVo vo = socketService.getSocketVo(stateEditVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                try {
                    socketService.roomStateEdit(stateEditVo.getRoomNo(), MemberVo.getMyMemNo(request), stateEditVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, isGuest, "edit");
                    vo.resetData();
                } catch (Exception e) {
                    log.info("Socket Service roomStateEdit Exception {}", e);
                }
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_??????????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_?????????????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_?????????????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????_???????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_???????????????));
            } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????_????????????.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_????????????));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }
        } else if (BroadcastStatus.???????????????_??????????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
        } else if (BroadcastStatus.???????????????_???????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????));
        }

        return result;
    }

    /**
     * ????????? ????????????
     */
    public String callRoomStateGuestEdit(StateEditVo stateEditVo, HttpServletRequest request) {

        String result = "";
        HashMap selParams = new HashMap();
        selParams.put("mem_no", MemberVo.getMyMemNo(request));
        selParams.put("room_no", stateEditVo.getRoomNo());
        HashMap guestStateInfo = userDao.selectGuestStateInfo(selParams);
        boolean isMic = true;
        boolean isCall = false;
        boolean isVideo = true;
        boolean isServer = true;
        if (!DalbitUtil.isEmpty(guestStateInfo)) {
            isMic = "1".equals(String.valueOf(guestStateInfo.get("mic_state")));
            isCall = "1".equals(String.valueOf(guestStateInfo.get("call_state")));
            isVideo = "1".equals(String.valueOf(guestStateInfo.get("video_state")));
            isServer = "1".equals(String.valueOf(guestStateInfo.get("server_state")));
        }

        //int oldState = Integer.parseInt(String.valueOf(guestStateInfo.get("state")));

        P_RoomGuestStateUpdateVo pRoomGuestStateUpdateVo = new P_RoomGuestStateUpdateVo();
        pRoomGuestStateUpdateVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomGuestStateUpdateVo.setRoom_no(stateEditVo.getRoomNo());

        if ("mic".equals(stateEditVo.getMediaState().toLowerCase())) {
            if (isMic == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }
        } else if ("call".equals(stateEditVo.getMediaState().toLowerCase())) {
            if (isCall == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }
        } else if ("video".equals(stateEditVo.getMediaState().toLowerCase())) {
            if (isVideo == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }
        } else if ("server".equals(stateEditVo.getMediaState().toLowerCase())) {
            if (isServer == ("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn()))) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
            }
        }

        pRoomGuestStateUpdateVo.setStateType(stateEditVo.getMediaState());
        pRoomGuestStateUpdateVo.setState(("TRUE".equals(stateEditVo.getMediaOn().toUpperCase()) || "1".equals(stateEditVo.getMediaOn())) ? 1 : 0);
        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestStateUpdateVo);
        roomDao.callRoomStateGuestEdit(procedureVo);
        if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_??????.getMessageCode())) {
            SocketVo vo = socketService.getSocketVo(stateEditVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try {
                socketService.roomStateEdit(stateEditVo.getRoomNo(), MemberVo.getMyMemNo(request), stateEditVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, true, "edit");
                vo.resetData();
            } catch (Exception e) {
                log.info("Socket Service roomStateEdit Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
        } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????));
        } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_?????????????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_?????????????????????));
        } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????_????????????_???????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_????????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????????????????_??????));
        }

        return result;
    }
}
