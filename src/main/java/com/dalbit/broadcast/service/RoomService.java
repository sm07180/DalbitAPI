package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.RoomExitVo;
import com.dalbit.broadcast.vo.request.StateVo;
import com.dalbit.common.code.Code;
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
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_BroadcastSettingEditVo;
import com.dalbit.member.vo.procedure.P_BroadcastSettingVo;
import com.dalbit.member.vo.procedure.P_FanRankVo;
import com.dalbit.member.vo.request.BroadcastSettingEditVo;
import com.dalbit.member.vo.request.SpecialDjHistoryVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
    @Value("${room.bg.count}")
    int ROOM_BG_COUNT;


    /**
     * 방송방 나가기
     */
    public String callBroadCastRoomExit(P_RoomExitVo pRoomExitVo, HttpServletRequest request) {
        boolean isBj = false;
        boolean isGuest = false;
        String nickNm="";
        if(!pRoomExitVo.getMem_no().startsWith("8")){
            P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
            pRoomMemberInfoVo.setTarget_mem_no(pRoomExitVo.getMem_no());
            pRoomMemberInfoVo.setRoom_no(pRoomExitVo.getRoom_no());
            pRoomMemberInfoVo.setMem_no(pRoomExitVo.getMem_no());
            ProcedureVo procedureInfoVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo, request);
            if(!DalbitUtil.isEmpty(procedureInfoVo.getData())){
                isBj = DalbitUtil.getIntMap((HashMap)procedureInfoVo.getData(), "auth") == 3;
                isGuest = DalbitUtil.getBooleanMap((HashMap)procedureInfoVo.getData(), "isGuest");
                nickNm = DalbitUtil.getStringMap((HashMap)procedureInfoVo.getData(), "nickNm");
            }

        }

        ProcedureVo procedureVo = new ProcedureVo(pRoomExitVo);
        roomDao.callBroadCastRoomExit(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(Status.방송나가기.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);

            SocketVo vo = socketService.getSocketVo(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            if (isBj) {
                try {
                    socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
                }
            } else {

                HashMap fanRankMap = commonService.getKingFanRankList(pRoomExitVo.getRoom_no());
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
                        //TODO - 레벨업 유무 소켓추가 추후 확인
                        // socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
                        socketService.changeCount(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    }
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
                }

               //게스트 퇴장일 경우 소켓 추가
               if(isGuest){
                   Status status = null;
                   HashMap selParams = new HashMap();
                   selParams.put("mem_no", pRoomExitVo.getMem_no());
                   selParams.put("room_no", pRoomExitVo.getRoom_no());
                   HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);

                   GuestInfoVo guestInfoVo = new GuestInfoVo();
                   guestInfoVo.setMode(6);
                   guestInfoVo.setMemNo(pRoomExitVo.getMem_no());
                   guestInfoVo.setNickNm(nickNm);
                   guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String)roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));

                    //P_RoomGuestVo apiData = new P_RoomGuestVo(DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), "", "", "", "", "", "", request);
                    //status = guestService.callBroadCastRoomGuestCancel(apiData, request);
                   //소켓통신
                   if(status == null || "success".equals(status.getResult())){
                       try{
                           socketService.sendGuest(pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                       }catch(Exception e){}
                   }

                }

            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기, returnMap));
        }else{
            HashMap returnMap = new HashMap();
            returnMap.put("isLevelUp", false);
            if (isBj) {
                if (procedureVo.getRet().equals(Status.방송나가기_회원아님.getMessageCode())) {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_회원아님));
                } else if (procedureVo.getRet().equals(Status.방송나가기_해당방이없음.getMessageCode()) || procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode()) || procedureVo.getRet().equals(Status.방송나가기_방참가자아님.getMessageCode())) {
                    SocketVo vo = socketService.getSocketVo(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    try {
                        socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    } catch (Exception e) {
                        log.info("Socket Service changeCount Exception {}", e);
                    }
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기));
                    //result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_해당방이없음));
                //} else if (procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode())) {
                    //result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_종료된방송));
                } else if (procedureVo.getRet().equals(Status.방송나가기_방참가자아님.getMessageCode())) {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_방참가자아님));
                } else if (procedureVo.getRet().equals(Status.방송나가기실패.getMessageCode())) {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기실패));
                } else {
                    roomDao.callUpdateExitTry(pRoomExitVo.getRoom_no());
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방참가실패));
                }
            }else{
                //게스트 퇴장일 경우 소켓 추가
                if(isGuest){
                    Status status = null;
                    HashMap selParams = new HashMap();
                    selParams.put("mem_no", pRoomExitVo.getMem_no());
                    selParams.put("room_no", pRoomExitVo.getRoom_no());
                    HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);

                    GuestInfoVo guestInfoVo = new GuestInfoVo();
                    guestInfoVo.setMode(6);
                    guestInfoVo.setMemNo(pRoomExitVo.getMem_no());
                    guestInfoVo.setNickNm(nickNm);
                    guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String)roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));

                    //P_RoomGuestVo apiData = new P_RoomGuestVo(DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), "", "", "", "", "", "", request);
                    //status = guestService.callBroadCastRoomGuestCancel(apiData, request);
                    //소켓통신
                    if(status == null || "success".equals(status.getResult())){
                        try{
                            socketService.sendGuest(pRoomExitVo.getMem_no(), pRoomExitVo.getRoom_no(), DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        }catch(Exception e){}
                    }

                }
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기));
            }
        }
        return result;
    }

    public String callBroadCastRoomExitForce(P_RoomExitVo pRoomExitVo, HttpServletRequest request) {
        if(pRoomExitVo.getOs() == 3){
            return callBroadCastRoomExit(pRoomExitVo, request);
        }else{
            try{
                socketService.chatEndRed(MemberVo.getMyMemNo(request), pRoomExitVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
            return gsonUtil.toJson(new JsonOutputVo(Status.방송나가기));
        }
    }

    /**
     * 방송방 정보 수정
     */
    public String callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo, HttpServletRequest request) throws GlobalException {

        String systemBanWord = commonService.banWordSelect();
        //String titleBanWord = commonService.titleBanWordSelect();

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(pRoomEditVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크(제목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pRoomEditVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방수정제목금지));
        }

        //금지어 체크(인사말)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pRoomEditVo.getWelcomMsg())){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방수정인사말금지));
        }

        Boolean isDone = false;
        if(!DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImage()) && pRoomEditVo.getBackgroundImage().startsWith(Code.포토_배경_임시_PREFIX.getCode())){
            isDone = true;
            pRoomEditVo.setBackgroundImage(DalbitUtil.replacePath(pRoomEditVo.getBackgroundImage()));
        }

        ProcedureVo procedureVo = new ProcedureVo(pRoomEditVo);
        P_RoomEditOutVo pRoomEditOutVo = roomDao.callBroadCastRoomEdit(procedureVo);

        //방송설정 입퇴장메시지 + 실시간 팬 배지 세팅 수정
        BroadcastSettingEditVo broadcastSettingEditVo = new BroadcastSettingEditVo();
        broadcastSettingEditVo.setDjListenerIn(pRoomEditVo.getDjListenerIn());
        broadcastSettingEditVo.setDjListenerOut(pRoomEditVo.getDjListenerOut());
        P_BroadcastSettingEditVo pBroadcastSettingEditVo = new P_BroadcastSettingEditVo(broadcastSettingEditVo, request);
        mypageService.callBroadcastSettingEdit(pBroadcastSettingEditVo, request, "edit");

        String result;
        if(procedureVo.getRet().equals(Status.방송정보수정성공.getMessageCode())) {
            if(DalbitUtil.isEmpty(pRoomEditOutVo.getImage_background())){
                pRoomEditOutVo.setImage_background(Code.포토_배경_디폴트_PREFIX.getCode()+"/"+Code.배경이미지_파일명_PREFIX.getCode()+"200410.jpg");
            }

            String delImg = pRoomEditVo.getBackgroundImageDelete();
            if(!DalbitUtil.isEmpty(delImg) && delImg.startsWith(Code.포토_배경_디폴트_PREFIX.getCode())){
                delImg = null;
            }
            //Done 처리
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomEditVo.getBackgroundImage()), delImg, request);
            }

            //방송설정 입퇴장메시지 + 실시간 팬 배지 세팅 조회
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
            try{
                socketService.changeRoomInfo(pRoomEditOutVo.getRoomNo(), MemberVo.getMyMemNo(request), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeRoomInfo Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_해당방에없는회원번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_해당방에없는회원번호));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_수정권한없는회원.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_수정권한없는회원));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정실패));
        }

        return result;
    }


    /**
     * 방송방 리스트
     */
    @Transactional(readOnly = true)
    public String callBroadCastRoomList(P_RoomListVo pRoomListVo, HttpServletRequest request){
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);
        long st = (new Date()).getTime();
        List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);
        log.debug("select time {} ms", ((new Date()).getTime() - st));

        HashMap roomList = new HashMap();
        if(DalbitUtil.isEmpty(roomVoList)){
            roomList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.방송리스트없음, roomList));
        }

        st = (new Date()).getTime();
        List<RoomOutVo> outVoList = new ArrayList<>();
        BanWordVo banWordVo = new BanWordVo();
        String systemBanWord = commonService.banWordSelect();
        DeviceVo deviceVo = new DeviceVo(request);
        for (int i=0; i<roomVoList.size(); i++){
            if(!DalbitUtil.isEmpty(roomVoList.get(i).getNotice())){
                //사이트+방송방 금지어 조회 공지사항 마스킹처리 목록에서 사용하지 않아 주석 처리
                banWordVo.setMemNo(roomVoList.get(i).getBj_mem_no());
                String banWord = commonService.broadcastBanWordSelect(banWordVo);
                if(!DalbitUtil.isEmpty(banWord)){
                    roomVoList.get(i).setNotice(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, roomVoList.get(i).getNotice()));
                }else if(!DalbitUtil.isEmpty(systemBanWord)){
                    roomVoList.get(i).setNotice(DalbitUtil.replaceMaskString(systemBanWord, roomVoList.get(i).getNotice()));
                }
            }
            RoomOutVo roomOutVo = new RoomOutVo(roomVoList.get(i), deviceVo);
            //badgeService.setBadgeInfo(roomOutVo.getBjMemNo(), 6);
            //roomOutVo.setLiveBadgeList(badgeService.getCommonBadge());
            outVoList.add(roomOutVo);
        }
        log.debug("set list time {} ms", ((new Date()).getTime() - st));
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        roomList.put("list", procedureOutputVo.getOutputBox());
        roomList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomListVo.getPageNo(), pRoomListVo.getPageCnt()));
        roomList.put("isGreenMoon", DalbitUtil.getIntMap(resultMap, "greenMoon") == 1 ? true : false);

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트조회, roomList));
        }else if(Status.방송리스트_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트조회_실패));
        }
        return result;
    }


    /**
     * 방송 정보조회(방송방 생성,참여 후)
     */
    public ProcedureOutputVo callBroadCastRoomInfoViewReturnVo(P_RoomInfoViewVo pRoomInfoViewVo, HttpServletRequest request){
        ProcedureVo procedureVo = new ProcedureVo(pRoomInfoViewVo);
        P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomInfoViewVo)){
            return new ProcedureOutputVo(procedureVo);
        }else{
            roomInfoViewVo.setExt(procedureVo.getExt());

            //출석체크 완료 여부 조회
            P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo(request);
            attendanceCheckVo.setMem_no(MemberVo.getMyMemNo(request));
            int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
            HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);
            //보름달 체크
            P_MoonCheckVo pMoonCheckVo = new P_MoonCheckVo();
            pMoonCheckVo.setRoom_no(pRoomInfoViewVo.getRoom_no());
            HashMap moonCheckMap = actionService.callMoonCheckMap(pMoonCheckVo);
            procedureOutputVo = new ProcedureOutputVo(procedureVo, new RoomOutVo(roomInfoViewVo, attendanceCheckMap, moonCheckMap));
        }

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        return procedureOutputVo;
    }


    /**
     * 방송중인 DJ 체크
     */
    public ProcedureVo callMemberBroadcastingCheck(P_MemberBroadcastingCheckVo pMemberBroadcastingCheckVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberBroadcastingCheckVo);
        roomDao.callMemberBroadcastingCheck(procedureVo);
        return procedureVo;
    }

    /**
     * 방송중인 DJ 체크
     */
    public String callMemberBroadcastingCheck(HttpServletRequest request) {
        P_MemberBroadcastingCheckVo pMemberBroadcastingCheckVo = new P_MemberBroadcastingCheckVo();
        pMemberBroadcastingCheckVo.setMem_no(MemberVo.getMyMemNo(request));
        ProcedureVo procedureVo = new ProcedureVo(pMemberBroadcastingCheckVo);
        roomDao.callMemberBroadcastingCheck(procedureVo);
        String result = "";
        if(Status.방송진행여부체크_방송방없음.getMessageCode().equals(procedureVo.getRet())) {
            //이어하기 체크
            ProcedureVo continueVo = new ProcedureVo(pMemberBroadcastingCheckVo);
            roomDao.callBroadCastRoomContinueCheck(continueVo);
            if("0".equals(continueVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.이어하기가능));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_방송방없음));
            }
        }else if(Status.방송진행여부체크_방송중.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            /*if(DalbitUtil.getIntMap(resultMap, "mem_state") != 0 && DalbitUtil.getIntMap(resultMap, "mem_state") != 2){ //방송중인 방이 있으나 tb_broadcast_room_member 테이블에 방장으로 없거나 종료 했을 경우
                //해당방 종료 시키고 방송방 없음으로 처리 함
                try{
                    SocketVo vo = socketService.getSocketVo(DalbitUtil.getStringMap(resultMap, "roomNo"), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    socketService.chatEnd(DalbitUtil.getStringMap(resultMap, "roomNo"), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 3, DalbitUtil.isLogin(request), vo);
                }catch(Exception e){}
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_방송방없음));
            }else{*/
                returnMap.put("roomNo", DalbitUtil.getStringMap(resultMap, "roomNo"));
                returnMap.put("state", DalbitUtil.getIntMap(resultMap, "state"));
                returnMap.put("isWowza", DalbitUtil.getIntMap(resultMap, "isWowza"));
                result = (DalbitUtil.getIntMap(resultMap, "state") == 5) ? gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_비정상, returnMap)) : gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_방송중, returnMap));
            //}
        }else if(Status.방송진행여부체크_요청회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_요청회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_실패));
        }

        return result;
    }

    /**
     * 방송방 현재 순위, 아이템 사용 현황 조회
     */
    public String callBroadCastRoomLiveRankInfo(P_RoomLiveRankInfoVo pRoomLiveRankInfoVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomLiveRankInfo(procedureVo);

        String result="";
        if(Status.순위아이템사용_조회성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            int roomCnt = DalbitUtil.getIntMap(resultMap, "totalRoomCnt");
            int rank = DalbitUtil.getIntMap(resultMap, "rank");
            if(rank > roomCnt){
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

            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_조회성공, returnMap));
        }else if(Status.순위아이템사용_요청회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_요청회원_번호비정상));
        }else if(Status.순위아이템사용_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_해당방없음));
        }else if(Status.순위아이템사용_해당방종료.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_해당방종료));
        }else if(Status.순위아이템사용_요청회원_해당방청취자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_요청회원_해당방청취자아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_조회실패));
        }

        return result;
    }

    /**
     * 방송방 현재 순위, 좋아요수 팬랭킹 조회
     */
    public String callBroadCastRoomChangeCount(P_RoomLiveRankInfoVo pRoomLiveRankInfoVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomChangeCount(procedureVo);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_조회실패));
        if(Status.순위아이템사용_조회성공.getMessageCode().equals(procedureVo.getRet())) {
            try{
                HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

                SocketVo vo = socketService.getSocketVo(pRoomLiveRankInfoVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));

                HashMap fanRankMap = commonService.getKingFanRankList(pRoomLiveRankInfoVo.getRoom_no());
                socketMap.put("fanRank", fanRankMap.get("list"));
                socketMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                socketMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                socketMap.put("kingGender", fanRankMap.get("kingGender"));
                socketMap.put("kingAge", fanRankMap.get("kingAge"));
                socketMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                socketService.changeCount(pRoomLiveRankInfoVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_조회성공));
        }else if(Status.순위아이템사용_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.순위아이템사용_해당방없음));
        }

        return result;
    }

    /**
     * 방송방 선물받은 내역보기
     */
    public String callBroadCastRoomGiftHistory(P_RoomGiftHistoryVo pRoomGiftHistoryVo) {
        pRoomGiftHistoryVo.setPageNo(1);
        pRoomGiftHistoryVo.setPageCnt(999999999);
        ProcedureVo procedureVo = new ProcedureVo(pRoomGiftHistoryVo);
        List<P_RoomGiftHistoryVo> giftHistoryListVo = roomDao.callBroadCastRoomGiftHistory(procedureVo);
        List<RoomGiftHistoryOutVo> outVoList = new ArrayList<>();
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) == 0 || DalbitUtil.isEmpty(giftHistoryListVo)) {
            HashMap giftHistoryList = new HashMap();
            giftHistoryList.put("list", new ArrayList<>());
            giftHistoryList.put("totalCnt", 0);
            giftHistoryList.put("totalGold", 0);
            giftHistoryList.put("paging", new PagingVo(0, pRoomGiftHistoryVo.getPageNo(), pRoomGiftHistoryVo.getPageCnt()));

            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받은내역없음, giftHistoryList));
        }else if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            HashMap giftHistoryList = new HashMap();
            for (int i=0; i<giftHistoryListVo.size(); i++){
                outVoList.add(new RoomGiftHistoryOutVo(giftHistoryListVo.get(i)));
            }

            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            giftHistoryList.put("totalCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
            giftHistoryList.put("totalGold", DalbitUtil.getIntMap(resultMap, "totalGold"));
            giftHistoryList.put("list", procedureOutputVo.getOutputBox());
            giftHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGiftHistoryVo.getPageNo(), pRoomGiftHistoryVo.getPageCnt()));

            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받은내역조회, giftHistoryList));
        }else if(Status.선물내역조회_회원번호정상아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물내역조회_회원번호정상아님));
        }else if(Status.선물내역조회_해당방없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물내역조회_해당방없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받은내역조회_실패));
        }

        return result;
    }

    /**
     * 방송방 회원정보 조회
     */
    public ProcedureVo getBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo, HttpServletRequest request){
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberInfoVo);
        roomDao.callBroadCastRoomMemberInfo(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("memNo", pRoomMemberInfoVo.getTarget_mem_no());
        returnMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
        returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
        returnMap.put("age", DalbitUtil.getIntMap(resultMap, "age"));
        returnMap.put("memId", DalbitUtil.getStringMap(resultMap, "memId"));
        returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("profMsg", DalbitUtil.getStringMap(resultMap, "profileMsg"));
        returnMap.put("holder", StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", DalbitUtil.getIntMap(resultMap, "level") + ""));
        returnMap.put("holderBg", "");
        returnMap.put("profileBg", "");
        int l = (DalbitUtil.getIntMap(resultMap, "level") - 1) / 10;
        if(l > 4){
            returnMap.put("holderBg", StringUtils.replace(DalbitUtil.getProperty("level.frame.bg"),"[level]", l + ""));
        }
        returnMap.put("levelColor", DalbitUtil.getProperty("level.color."+l).split(","));
        returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
        returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
        returnMap.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
        returnMap.put("expBegin", DalbitUtil.getIntMap(resultMap, "expBegin"));
        returnMap.put("expNext", DalbitUtil.getIntMap(resultMap, "expNext"));
        returnMap.put("expRate", DalbitUtil.getExpRate(DalbitUtil.getIntMap(resultMap, "exp"), DalbitUtil.getIntMap(resultMap, "expBegin"), DalbitUtil.getIntMap(resultMap, "expNext")));
        returnMap.put("fanCnt", DalbitUtil.getIntMap(resultMap, "fanCount"));
        returnMap.put("starCnt", DalbitUtil.getIntMap(resultMap, "starCount"));
        returnMap.put("isFan", DalbitUtil.getIntMap(resultMap, "enableFan") == 0 ? true : false);
        returnMap.put("auth", DalbitUtil.getIntMap(resultMap, "auth"));
        returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
        returnMap.put("state", DalbitUtil.getIntMap(resultMap, "state"));

        //팬랭킹 1,2,3 조회 프로시저 분리
        P_FanRankVo pFanRankVo = new P_FanRankVo();
        pFanRankVo.setMem_no(pRoomMemberInfoVo.getTarget_mem_no());
        returnMap.put("fanRank", memberService.fanRank3(pFanRankVo));
        returnMap.put("isNew", DalbitUtil.getIntMap(resultMap, "newdj_badge") == 1);
        returnMap.put("isSpecial", DalbitUtil.getIntMap(resultMap, "specialdj_badge") == 1);
        FanBadgeVo fanBadgeVo = new FanBadgeVo(DalbitUtil.getStringMap(resultMap, "fanBadgeText"), DalbitUtil.getStringMap(resultMap, "fanBadgeIcon"), DalbitUtil.getStringMap(resultMap, "fanBadgeStartColor"), DalbitUtil.getStringMap(resultMap, "fanBadgeEndColor"));
        if(DalbitUtil.isEmpty(fanBadgeVo.getText())){
            returnMap.put("fanBadge", "");
        }else{
            returnMap.put("fanBadge", fanBadgeVo);
        }
        badgeService.setBadgeInfo(pRoomMemberInfoVo.getTarget_mem_no(), -1);
        returnMap.put("liveBadgeList", badgeService.getCommonBadge());
        returnMap.put("commonBadgeList", badgeService.getCommonBadge());
        returnMap.put("fanBadgeList", new ArrayList());

        returnMap.put("cupidMemNo", DalbitUtil.getStringMap(resultMap, "cupidMemNo"));
        returnMap.put("cupidNickNm", DalbitUtil.getStringMap(resultMap, "cupidNickNm"));
        returnMap.put("cupidProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "cupidProfileImage"), DalbitUtil.getStringMap(resultMap, "cupidMemSex"), DalbitUtil.getProperty("server.photo.url")));
        if(DalbitUtil.getIntMap(resultMap, "auth") == 0 || DalbitUtil.getIntMap(resultMap, "auth") == 1){
            returnMap.put("isNewListener", DalbitUtil.getIntMap(resultMap, "new_badge") == 0 ? false : true);
        }else{
            returnMap.put("isNewListener", false);
        }

        returnMap.put("liveDjRank", DalbitUtil.getIntMap(resultMap, "liveDjRank"));
        returnMap.put("liveFanRank", DalbitUtil.getIntMap(resultMap, "liveFanRank"));
        returnMap.put("isGuest", DalbitUtil.getIntMap(resultMap, "isGuest") == 1 ? true : false);
        returnMap.put("managerType", DalbitUtil.getIntMap(resultMap, "managerType"));
        returnMap.put("likeTotCnt", DalbitUtil.getIntMap(resultMap, "receivedGoodTotal"));
        returnMap.put("specialDjCnt", DalbitUtil.getIntMap(resultMap, "specialDjCnt"));


        if(DalbitUtil.getIntMap(resultMap, "specialdj_badge") == 1){
            returnMap.put("wasSpecial", false);
        }else{
            SpecialDjHistoryVo specialDjHistoryVo = new SpecialDjHistoryVo();
            specialDjHistoryVo.setMemNo(pRoomMemberInfoVo.getTarget_mem_no());
            returnMap.put("wasSpecial", memberService.getSpecialCnt(specialDjHistoryVo) > 0 ? true : false);
        }

        procedureVo.setData(returnMap);
        return procedureVo;
    }

    /**
     * 방송방 회원정보 조회
     */
    public String callBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo, HttpServletRequest request) {
        ProcedureVo procedureVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo, request);

        String result="";
        if(Status.방송방회원정보조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_성공, procedureVo.getData()));
        }else if(Status.방송방회원정보조회_요청회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_요청회원_번호비정상));
        }else if(Status.방송방회원정보조회_대상회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_대상회원_번호비정상));
        }else if(Status.방송방회원정보조회_종료된방.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_종료된방));
        }else if(Status.방송방회원정보조회_대상회원_방에없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_대상회원_방에없음));
        }else if(Status.방송방회원정보조회_차단회원_접근불가.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_차단회원_접근불가));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_실패));
        }

        return result;
    }

    /**
     * 방송방 토큰 재생성
     */
    public String callBroadcastRoomStreamSelect(P_RoomStreamVo pRoomStreamVo, HttpServletRequest request) throws GlobalException {
        ProcedureVo procedureVo = new ProcedureVo(pRoomStreamVo);
        roomDao.callBroadcastRoomStreamSelect(procedureVo);

        String result = "";
        if(Status.스트림아이디_조회성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            int auth = DalbitUtil.getIntMap(resultMap, "auth");
            String bjStreamId = DalbitUtil.getStringMap(resultMap, "bj_streamid");
            String bjPubToken = "";
            String bjPlayToken = "";
            String gstStreamId = DalbitUtil.getStringMap(resultMap, "guest_streamid");
            String gstPubToken = "";
            String gstPlayToken = "";
            if(auth == 3){ // DJ
                bjPubToken = (String)restService.antToken(bjStreamId, "publish", request).get("tokenId");
                if(!DalbitUtil.isEmpty(gstStreamId)){
                    gstPlayToken = (String)restService.antToken(gstStreamId, "play", request).get("tokenId");
                }
            }else{
                bjPlayToken = (String)restService.antToken(bjStreamId, "play", request).get("tokenId");
                if(!DalbitUtil.isEmpty(gstStreamId)){
                    if(auth == 2) { //게스트
                        gstPubToken = (String)restService.antToken(gstStreamId, "publish", request).get("tokenId");
                    }else{
                        gstPlayToken = (String)restService.antToken(gstStreamId, "play", request).get("tokenId");
                    }
                }
            }

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

            //토큰 업데이트
            roomDao.callBroadcastRoomTokenUpdate(procedureUpdateVo);

            if(Status.스트림아이디_조회성공.getMessageCode().equals(procedureUpdateVo.getRet())) {
                HashMap resultUpdateMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

                log.info("프로시저 응답 코드: {}", procedureVo.getRet());
                log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

                P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
                pRoomInfoViewVo.setMemLogin(pRoomStreamTokenVo.getMemLogin());
                pRoomInfoViewVo.setMem_no(pRoomStreamTokenVo.getMem_no());
                pRoomInfoViewVo.setRoom_no(pRoomStreamTokenVo.getRoom_no());

                //방송방 정보 조회
                ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);
                if(Status.방정보보기.getMessageCode().equals(roomInfoVo.getRet())) {
                    log.info(" 방송방 정보 조회 {}", roomInfoVo.getOutputBox());
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
                    returnMap.put("isNew", target.getIsNew());
                    returnMap.put("startDt", target.getStartDt());
                    returnMap.put("startTs", target.getStartTs());
                    returnMap.put("hasNotice", DalbitUtil.getIntMap(resultUpdateMap, "auth") == 3 ? false : !DalbitUtil.isEmpty(target.getNotice()));
                    returnMap.put("hasStory", getHasStory(auth, pRoomStreamVo.getRoom_no(), MemberVo.getMyMemNo(request), request));

                    returnMap.put("useBoost", existsBoostByRoom(pRoomStreamVo.getRoom_no(), pRoomStreamVo.getMem_no()));    //부스터 사용여부

                    HashMap fanRankMap = commonService.getKingFanRankList(pRoomStreamVo.getRoom_no());
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
                    if(DalbitUtil.isEmpty(fanBadgeList)){
                        returnMap.put("fanBadgeList", new ArrayList());
                    }else{
                        returnMap.put("fanBadgeList", fanBadgeList);
                    }

                    /*returnMap.put("level", target.getLevel());
                    returnMap.put("grade", target.getGrade());
                    returnMap.put("exp", target.getExp());
                    returnMap.put("expNext", target.getExpNext());
                    returnMap.put("dalCnt", target.getRubyCnt());
                    returnMap.put("byeolCnt", target.getGoldCnt());*/
                    DeviceVo deviceVo = new DeviceVo(request);
                    returnMap.put("antOrigin", DalbitUtil.getProperty("server.ant.origin.url") + DalbitUtil.getProperty("server.ant.path.url"));
                    returnMap.put("antEdge", DalbitUtil.getProperty("server.ant.edge.url") + DalbitUtil.getProperty("server.ant.path.url"));

                    //출석체크 완료 여부 조회
                    P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo(request);
                    attendanceCheckVo.setMem_no(MemberVo.getMyMemNo(request));
                    int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
                    HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);
                    returnMap.put("isCheck", attendanceCheckMap.get("isCheck"));

                    log.info("returnMap: {}", returnMap);

                    result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기, returnMap));
                }else if(Status.방정보보기_회원번호아님.getMessageCode().equals(roomInfoVo.getRet())){
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_회원번호아님));
                }else if(Status.방정보보기_해당방없음.getMessageCode().equals(roomInfoVo.getRet())){
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_해당방없음));
                }else{
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_실패));
                }
            }else if(Status.스트림아이디_회원아님.getMessageCode().equals(procedureUpdateVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_회원아님));
            }else if(Status.스트림아이디_해당방없음.getMessageCode().equals(procedureUpdateVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_해당방없음));
            }else if(Status.스트림아이디_요청회원_방소속아님.getMessageCode().equals(procedureUpdateVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_요청회원_방소속아님));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_조회실패));
            }
        }else if(Status.스트림아이디_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_회원아님));
        }else if(Status.스트림아이디_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_해당방없음));
        }else if(Status.스트림아이디_요청회원_방소속아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_요청회원_방소속아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_조회실패));
        }

        return result;
    }

    /**
     * 방송방 상태 변경
     */
    public String callBroadCastRoomStateUpate(StateVo stateVo, HttpServletRequest request) {
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        pRoomInfoViewVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomInfoViewVo.setRoom_no(stateVo.getRoomNo());
        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);

        String result="";
        if(procedureOutputVo.getRet().equals(Status.방정보보기.getMessageCode())) {
            RoomOutVo target = (RoomOutVo) procedureOutputVo.getOutputBox();
            int old_state = target.getState();
            P_RoomStateUpdateVo pRoomStateUpdateVo = new P_RoomStateUpdateVo();
            pRoomStateUpdateVo.setMem_no(MemberVo.getMyMemNo(request));
            pRoomStateUpdateVo.setRoom_no(stateVo.getRoomNo());
            int state = 1;
            if("0".equals(stateVo.getIsAnt()) || "FALSE".equals(stateVo.getIsAnt().toUpperCase())) {
                state = 0;
            }else if("1".equals(stateVo.getIsCall()) || "TRUE".equals(stateVo.getIsCall().toUpperCase())) {
                state = 3;
            }else if("0".equals(stateVo.getIsMic()) || "FALSE".equals(stateVo.getIsMic().toUpperCase())) {
                state = 2;
            }
            pRoomStateUpdateVo.setState(state);

            if(old_state == state){
                return gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_성공));
            }

            ProcedureVo procedureVo = new ProcedureVo(pRoomStateUpdateVo);
            roomDao.callBroadCastRoomStateUpate(procedureVo);

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");

            if (procedureVo.getRet().equals(Status.방송방상태변경_성공.getMessageCode())) {
                SocketVo vo = socketService.getSocketVo(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                try{
                    DeviceVo deviceVo = new DeviceVo(request);
                    /*
                        채팅방 상단 top1부분 버전분기
                        예전 마이크,통화,앤트 관련하여 최종 하나만 발송
                        변경 마이크,통화,앤트 관련하여 각각 발송
                    */
                    if(deviceVo.getOs() == 3 || deviceVo.getOs() == 1
                        || (deviceVo.getOs() == 2 && Integer.parseInt(deviceVo.getAppBuild()) < 91) //IOS일경우 빌드번호 91보다 작으면 예전
                    ){
                        socketService.changeRoomState(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), old_state, state, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, stateVo.getIsAnt());
                    }else{
                        socketService.changeRoomState(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), old_state, stateVo.getIsAnt(), stateVo.getIsCall(), stateVo.getIsMic(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                    }
                    vo.resetData();
                }catch(Exception e){
                    log.info("Socket Service changeRoomState Exception {}", e);
                }
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_성공));
            } else if (procedureVo.getRet().equals(Status.방송방상태변경_회원아님.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_회원아님));
            } else if (procedureVo.getRet().equals(Status.방송방상태변경_해당방이없음.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_해당방이없음));
            } else if (procedureVo.getRet().equals(Status.방송방상태변경_방이종료되었음.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_방이종료되었음));
            } else if (procedureVo.getRet().equals(Status.방송방상태변경_요청회원_방소속아님.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_요청회원_방소속아님));
            } else if (procedureVo.getRet().equals(Status.방송방상태변경_요청회원_방장아님.getMessageCode())) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_요청회원_방장아님));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송방상태변경_실패));
            }
        }else if(Status.방정보보기_회원번호아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_회원번호아님));
        }else if(Status.방정보보기_해당방없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_해당방없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_실패));
        }

        return result;
    }

    public String callAntRefresh(P_RoomStreamVo pRoomStreamVo, HttpServletRequest request) throws GlobalException {
        String result = "";
        //방정보 조회
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(pRoomStreamVo.getMemLogin());
        pRoomInfoViewVo.setMem_no(pRoomStreamVo.getMem_no());
        pRoomInfoViewVo.setRoom_no(pRoomStreamVo.getRoom_no());
        ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);

        if(Status.방정보보기.getMessageCode().equals(roomInfoVo.getRet())) {
            ProcedureVo procedureVo = new ProcedureVo(pRoomStreamVo);
            roomDao.callBroadcastRoomStreamSelect(procedureVo);

            if(Status.스트림아이디_조회성공.getMessageCode().equals(procedureVo.getRet())) {
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
                returnMap.put("useBoost", existsBoostByRoom(pRoomStreamVo.getRoom_no(), pRoomStreamVo.getMem_no()));    //부스터 사용여부
                returnMap.put("isRecomm", target.getIsRecomm());
                returnMap.put("isPop", target.getIsPop());
                returnMap.put("isNew", target.getIsNew());
                returnMap.put("isSpecial", target.getIsSpecial());
                returnMap.put("startDt", target.getStartDt());
                returnMap.put("startTs", target.getStartTs());
                returnMap.put("auth", auth);
                returnMap.put("hasNotice", auth == 3 ? false : !DalbitUtil.isEmpty(target.getNotice()));
                returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
                returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
                returnMap.put("isLike", (DalbitUtil.isLogin(request)) ? "1".equals(DalbitUtil.getStringMap(resultMap, "isGood")) : true);
                DeviceVo deviceVo = new DeviceVo(request);
                returnMap.put("antOrigin", DalbitUtil.getProperty("server.ant.origin.url") + DalbitUtil.getProperty("server.ant.path.url"));
                returnMap.put("antEdge", DalbitUtil.getProperty("server.ant.edge.url") + DalbitUtil.getProperty("server.ant.path.url"));

                HashMap fanBadgeMap = new HashMap();
                fanBadgeMap.put("mem_no", target.getBjMemNo());
                fanBadgeMap.put("type", 3);
                fanBadgeMap.put("by", "api");
                List fanBadgeList = commonDao.callMemberBadgeSelect(fanBadgeMap);
                if(DalbitUtil.isEmpty(fanBadgeList)){
                    returnMap.put("fanBadgeList", new ArrayList());
                }else{
                    returnMap.put("fanBadgeList", fanBadgeList);
                }

                try{
                    if(auth == 3){ // DJ
                        if("publish".equals(request.getParameter("mode"))){
                            bjStreamId = (String)restService.antCreate(target.getTitle(), request).get("streamId");
                        }
                        bjPubToken = (String)restService.antToken(bjStreamId, "publish", request).get("tokenId");
                        pRoomStreamTokenVo.setBj_streamid(bjStreamId);
                        pRoomStreamTokenVo.setBj_publish_tokenid(bjPubToken);
                        //pRoomStreamTokenVo.setState("0");
                    }else{
                        bjPlayToken = (String)restService.antToken(bjStreamId, "play", request).get("tokenId");
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

                    //토큰 업데이트
                    roomDao.callBroadcastRoomTokenUpdate(procedureUpdateVo);
                    if(Status.스트림아이디_조회성공.getMessageCode().equals(procedureUpdateVo.getRet())) {
                        HashMap resultUpdateMap = new Gson().fromJson(procedureUpdateVo.getExt(), HashMap.class);

                        returnMap.put("likes", DalbitUtil.getIntMap(resultUpdateMap, "good"));
                        returnMap.put("rank", DalbitUtil.getIntMap(resultUpdateMap, "rank"));

                        HashMap fanRankMap = commonService.getKingFanRankList(pRoomStreamVo.getRoom_no());
                        returnMap.put("fanRank", fanRankMap.get("list"));
                        returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                        returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                        returnMap.put("kingGender", fanRankMap.get("kingGender"));
                        returnMap.put("kingAge", fanRankMap.get("kingAge"));
                        returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                        result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기, returnMap));
                    }else if(Status.스트림아이디_회원아님.getMessageCode().equals(procedureUpdateVo.getRet())){
                        result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_회원아님));
                    }else if(Status.스트림아이디_해당방없음.getMessageCode().equals(procedureUpdateVo.getRet())){
                        result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_해당방없음));
                    }else if(Status.스트림아이디_요청회원_방소속아님.getMessageCode().equals(procedureUpdateVo.getRet())){
                        result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_요청회원_방소속아님));
                    }else{
                        result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_조회실패));
                    }
                }catch(Exception e){
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

                    if(Status.순위아이템사용_조회성공.getMessageCode().equals(procedureExceptionVo.getRet())) {
                        HashMap resultCountMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

                        returnMap.put("likes", DalbitUtil.getIntMap(resultCountMap, "good"));
                        returnMap.put("rank", DalbitUtil.getIntMap(resultCountMap, "rank"));
                        HashMap fanRankMap = commonService.getKingFanRankList(pRoomStreamVo.getRoom_no());
                        returnMap.put("fanRank", fanRankMap.get("list"));
                        returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                        returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                        returnMap.put("kingGender", fanRankMap.get("kingGender"));
                        returnMap.put("kingAge", fanRankMap.get("kingAge"));
                        returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));
                    }else{
                        returnMap.put("likes", 0);
                        returnMap.put("rank", 0);
                        returnMap.put("fanRank", new ArrayList<>());
                    }

                    result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기, returnMap));
                }
                if(!oldBjStreamId.equals(bjStreamId)){
                    try{
                        restService.deleteAntRoom(oldBjStreamId, request);
                    }catch (Exception e){}
                }
            }else if(Status.스트림아이디_회원아님.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_회원아님));
            }else if(Status.스트림아이디_해당방없음.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_해당방없음));
            }else if(Status.스트림아이디_요청회원_방소속아님.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_요청회원_방소속아님));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_조회실패));
            }
        }else if(Status.방정보보기_회원번호아님.getMessageCode().equals(roomInfoVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_회원번호아님));
        }else if(Status.방정보보기_해당방없음.getMessageCode().equals(roomInfoVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_해당방없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_실패));
        }
        return result;
    }

    /**
     * 부스터 사용여부
     */
    public boolean existsBoostByRoom(String roomNo, String memNo){
        P_RoomLiveRankInfoVo pRoomLiveRankInfoVo = new P_RoomLiveRankInfoVo();
        pRoomLiveRankInfoVo.setRoom_no(roomNo);
        pRoomLiveRankInfoVo.setMem_no(memNo);

        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomLiveRankInfo(procedureVo);

        if(Status.순위아이템사용_조회성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            return DalbitUtil.getIntMap(resultMap, "usedItemCnt") > 0;
        }
        return false;
    }

    public boolean getHasStory(int auth, String room_no, String mem_no, HttpServletRequest request){
        if(auth == 3) { // DJ
            //사연조회
            P_RoomStoryListVo apiData = new P_RoomStoryListVo();
            apiData.setMem_no(mem_no);
            apiData.setRoom_no(room_no);
            apiData.setPageNo(1);
            apiData.setPageCnt(1);

            String resultStory = contentService.callGetStory(apiData, request);
            HashMap storyMap = new Gson().fromJson(resultStory, HashMap.class);
            if(storyMap.containsKey("result") && "success".equals(storyMap.get("result").toString()) && storyMap.containsKey("data")){
                try{
                    HashMap storyDataMap = new Gson().fromJson(storyMap.get("data").toString(), HashMap.class);
                    if(storyDataMap.containsKey("paging")){
                        HashMap storyPagingMap = new Gson().fromJson(storyDataMap.get("paging").toString(), HashMap.class);
                        return DalbitUtil.getIntMap(storyPagingMap, "total") > 0;
                    }
                }catch(Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 방송종료요약(청취차)
     */
    public HashMap getSummaryListener(RoomExitVo roomExitVo, HttpServletRequest request){
        HashMap result = new HashMap();
        P_SummaryListenerVo pSummaryListenerVo = new P_SummaryListenerVo(roomExitVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pSummaryListenerVo);
        List<P_SummaryListenerVo> recommList = roomDao.callBroadcastSummaryListener(procedureVo);
        if(Status.방송종료요약_성공.getMessageCode().equals(procedureVo.getRet())){
            HashMap data = new HashMap();
            HashMap returnMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            List<SummaryListenerOutVo> recommends = new ArrayList<>();
            List<SummaryListenerOutVo> myStars = new ArrayList<>();
            data.put("djMemNo", DalbitUtil.getStringMap(returnMap, "dj_mem_no"));
            data.put("djNickNm", DalbitUtil.getStringMap(returnMap, "nickName"));
            data.put("djProfImg", new ImageVo(DalbitUtil.getStringMap(returnMap, "image_profile"), DalbitUtil.getStringMap(returnMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            data.put("djHolder", StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", DalbitUtil.getStringMap(returnMap, "level")));
            data.put("listenTime", DalbitUtil.getIntMap(returnMap, "listentime"));
            data.put("bgImg", new ImageVo(DalbitUtil.getStringMap(returnMap, "image_background"), DalbitUtil.getProperty("server.photo.url")));
            data.put("isFan", DalbitUtil.getIntMap(returnMap, "fan") > 0);
            if(!DalbitUtil.isEmpty(recommList)){
                for(P_SummaryListenerVo items : recommList){
                    recommends.add(new SummaryListenerOutVo(items, DalbitUtil.getProperty("server.photo.url")));
                }
            }
            List<P_SummaryListenerVo> myStarList = roomDao.callBroadcastSummaryListenerMyStar(procedureVo);
            if(!DalbitUtil.isEmpty(myStarList)){
                for(P_SummaryListenerVo items : myStarList){
                    myStars.add(new SummaryListenerOutVo(items, DalbitUtil.getProperty("server.photo.url")));
                }
            }
            data.put("recommends", recommends);
            data.put("myStars", myStars);
            result.put("status", Status.방송종료요약_성공);
            result.put("data", data);
        }else if(Status.방송종료요약_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result.put("status", Status.방송종료요약_회원아님);
        }else if(Status.방송종료요약_방없음.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", Status.방송종료요약_방없음);
        }else{
            result.put("status", Status.방송종료요약_실패);
        }
        return result;
    }

    /**
     * 방송종료요약(DJ)
     */
    public HashMap getSummaryDj(RoomExitVo roomExitVo, HttpServletRequest request){
        HashMap result = new HashMap();
        P_SummaryListenerVo pSummaryListenerVo = new P_SummaryListenerVo(roomExitVo, request);
        ProcedureVo procedureVo = new ProcedureVo(pSummaryListenerVo);
        List<P_SummaryDjVo> resultSet = roomDao.callBroadcastSummaryDj(procedureVo);
        if(Status.방송종료요약_성공.getMessageCode().equals(procedureVo.getRet())){
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("roomNo", DalbitUtil.getStringMap(resultMap, "room_no"));
            data.put("memNo", DalbitUtil.getStringMap(resultMap, "mem_no"));
            data.put("title", DalbitUtil.getStringMap(resultMap, "title"));
            data.put("nickNm", DalbitUtil.getStringMap(resultMap, "mem_nick"));
            data.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "image_profile"), DalbitUtil.getStringMap(resultMap, "mem_sex"), DalbitUtil.getProperty("server.photo.url")));
            data.put("holder", StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", DalbitUtil.getIntMap(resultMap, "level") + ""));
            data.put("airTime", DalbitUtil.getIntMap(resultMap, "airtime"));
            data.put("listenerCnt", DalbitUtil.getIntMap(resultMap, "count_entry"));
            data.put("listenerMax", DalbitUtil.getIntMap(resultMap, "count_live_max"));
            data.put("gift", DalbitUtil.getIntMap(resultMap, "gold"));
            data.put("good", DalbitUtil.getIntMap(resultMap, "good"));
            data.put("fanCnt", DalbitUtil.getIntMap(resultMap, "fanCnt"));
            data.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
            data.put("bgImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "image_background"), DalbitUtil.getProperty("server.photo.url")));
            List<SummaryDjOutVo> list = new ArrayList<>();
            for(P_SummaryDjVo item : resultSet){
                list.add(new SummaryDjOutVo(item, DalbitUtil.getProperty("server.photo.url")));
            }
            data.put("list", list);
            result.put("status", Status.방송종료요약_성공);
            result.put("data", data);
        }else if(Status.방송종료요약_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result.put("status", Status.방송종료요약_회원아님);
        }else if(Status.방송종료요약_방없음.getMessageCode().equals(procedureVo.getRet())) {
            result.put("status", Status.방송종료요약_방없음);
        }else{
            result.put("status", Status.방송종료요약_실패);
        }
        return result;
    }


    /**
     * 좋아요 내역
     */
    public String getGoodHistory(P_RoomGoodHistoryVo pRoomGoodHistoryVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodHistoryVo);
        List<P_RoomGoodHistoryVo> goodHistoryListVo = roomDao.callGetGoodHistory(procedureVo);

        String result;
        if(DalbitUtil.isEmpty(goodHistoryListVo)) {
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            int totalCnt = 0;
            //안드로이드 1.3.6 버전 분기
            DeviceVo deviceVo = new DeviceVo(request);
            if((deviceVo.getOs() == 1 && DalbitUtil.versionCompare("1.3.6", deviceVo.getAppVersion()))){
                try {
                    HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                    totalCnt = DalbitUtil.getIntMap(resultMap, "totalCnt");
                }catch (Exception e){

                }
            }

            HashMap goodHistoryList = new HashMap();
            goodHistoryList.put("list", new ArrayList<>());
            goodHistoryList.put("totalCnt", Integer.valueOf(procedureOutputVo.getRet()));
            if(deviceVo.getOs() == 2){
                goodHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGoodHistoryVo.getPageNo(), pRoomGoodHistoryVo.getPageCnt()));
            }else{
                goodHistoryList.put("paging", new PagingVo(totalCnt, pRoomGoodHistoryVo.getPageNo(), pRoomGoodHistoryVo.getPageCnt()));
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_없음, goodHistoryList));
        }else if(Integer.parseInt(procedureVo.getRet()) > 0) {
            List<RoomGoodHistoryOutVo> outVoList = new ArrayList<>();
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
            HashMap goodHistoryList = new HashMap();
            for (int i=0; i<goodHistoryListVo.size(); i++){
                outVoList.add(new RoomGoodHistoryOutVo(goodHistoryListVo.get(i)));
            }

            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            goodHistoryList.put("totalCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
            goodHistoryList.put("list", procedureOutputVo.getOutputBox());
            goodHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGoodHistoryVo.getPageNo(), pRoomGoodHistoryVo.getPageCnt()));

            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_성공, goodHistoryList));
        }else if(Status.좋아요내역조회_회원번호정상아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_회원번호정상아님));
        }else if(Status.좋아요내역조회_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_해당방없음));
        }else if(Status.좋아요내역조회_미참여중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_미참여중));
        }else if(Status.좋아요내역조회_권한없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_권한없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요내역조회_실패));
        }

        return result;
    }
}
