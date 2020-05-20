package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.RoomGiftHistoryOutVo;
import com.dalbit.broadcast.vo.RoomOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.StateVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
    CommonDao commonDao;
    @Value("${room.bg.count}")
    int ROOM_BG_COUNT;


    /**
     * 방송방 생성
     */
    public String callBroadCastRoomCreate(P_RoomCreateVo pRoomCreateVo, HttpServletRequest request) throws GlobalException{

        String systemBanWord = commonService.banWordSelect();

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(pRoomCreateVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크(제목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pRoomCreateVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방생성제목금지));
        }

        //금지어 체크(인사말)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pRoomCreateVo.getWelcomMsg())){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방생성인사말금지));
        }

        String bgImg = pRoomCreateVo.getBackgroundImage();
        Boolean isDone = false;
        if(DalbitUtil.isEmpty(bgImg)){
            int random = Integer.parseInt(DalbitUtil.randomBgValue());
            bgImg = Code.포토_배경_디폴트_PREFIX.getCode()+"/"+Code.배경이미지_파일명_PREFIX.getCode()+"200417_"+random+".jpg";
        }else{
            if(bgImg.startsWith(Code.포토_배경_임시_PREFIX.getCode())){
                isDone = true;
            }
            bgImg = DalbitUtil.replacePath(bgImg);
        }

        pRoomCreateVo.setBackgroundImage(bgImg);
        ProcedureVo procedureVo = new ProcedureVo(pRoomCreateVo);
        roomDao.callBroadCastRoomCreate(procedureVo);
        String result;
        if(procedureVo.getRet().equals(Status.방송생성.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String roomNo = DalbitUtil.isNullToString(resultMap.get("room_no"));
            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", resultMap);
            log.info("방번호 추출: {}", roomNo);

            P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
            pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
            pRoomInfoViewVo.setMem_no(pRoomCreateVo.getMem_no());
            pRoomInfoViewVo.setRoom_no(roomNo);

            //방송방 정보 조회
            ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);
            log.info(" 방송방 정보 조회 {}", roomInfoVo.getOutputBox());
            RoomOutVo target = (RoomOutVo) roomInfoVo.getOutputBox();

            HashMap returnMap = new HashMap();
            returnMap.put("roomNo", roomNo);
            returnMap.put("bjStreamId",pRoomCreateVo.getBj_streamid());
            returnMap.put("bjPubToken", pRoomCreateVo.getBj_publish_tokenid());
            returnMap.put("title", target.getTitle());
            returnMap.put("bgImg", target.getBgImg());
            returnMap.put("link", target.getLink());
            returnMap.put("bjMemNo", target.getBjMemNo());
            returnMap.put("bjMemId", target.getBjMemId());
            returnMap.put("bjNickNm", target.getBjNickNm());
            returnMap.put("bjProfImg", target.getBjProfImg());
            if("real".equals(DalbitUtil.getActiceProfile())){
                returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_0.png");
            }else{
                returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_" + target.getBjLevel() + ".png");
            }
            returnMap.put("likes", 0);
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("auth", 3);
            returnMap.put("ctrlRole", "1111111111");
            returnMap.put("isFan", false);
            returnMap.put("isRecomm", target.getIsRecomm());
            returnMap.put("isPop", target.getIsPop());
            returnMap.put("isNew", target.getIsNew());
            returnMap.put("isSpecial", target.getIsSpecial());
            returnMap.put("startDt", target.getStartDt());
            returnMap.put("startTs", target.getStartTs());
            returnMap.put("state", target.getState());
            returnMap.put("hasNotice", !DalbitUtil.isEmpty(target.getNotice()));
            returnMap.put("hasStory", false);
            returnMap.put("isLike", true);
            returnMap.put("useBoost", false);

            /*returnMap.put("level", target.getLevel());
            returnMap.put("grade", target.getGrade());
            returnMap.put("exp", target.getExp());
            returnMap.put("expNext", target.getExpNext());
            returnMap.put("dalCnt", target.getRubyCnt());
            returnMap.put("byeolCnt", target.getGoldCnt());*/
            procedureVo.setData(returnMap);

            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomCreateVo.getBackgroundImage()), request);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송생성, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.방송생성_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송생성_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송중인방존재.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송중인방존재));
        } else if (procedureVo.getRet().equals(Status.방송생성_deviceUuid비정상.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송생성_deviceUuid비정상));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방생성실패));
        }
        return result;
    }


    /**
     * 방송방 참여하기
     */
    public String callBroadCastRoomJoin(P_RoomJoinVo pRoomJoinVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomJoinVo);
        roomDao.callBroadCastRoomJoin(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.방송참여성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            int remainTime = DalbitUtil.getIntMap(resultMap, "remainTime");
            String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
            String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
            String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

            P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
            pRoomInfoViewVo.setMemLogin(pRoomJoinVo.getMemLogin());
            pRoomInfoViewVo.setMem_no(pRoomJoinVo.getMem_no());
            pRoomInfoViewVo.setRoom_no(pRoomJoinVo.getRoom_no());

            //방송방 정보 조회
            ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);
            log.info(" 방송방 정보 조회 {}", roomInfoVo.getOutputBox());
            RoomOutVo target = (RoomOutVo) roomInfoVo.getOutputBox();

            HashMap returnMap = new HashMap();
            returnMap.put("roomNo",pRoomJoinVo.getRoom_no());
            returnMap.put("bjStreamId",pRoomJoinVo.getBj_streamid());
            returnMap.put("bjPlayToken",pRoomJoinVo.getBj_play_tokenid());
            returnMap.put("gstStreamId",pRoomJoinVo.getGuest_streamid());
            returnMap.put("gstPlayToken",pRoomJoinVo.getGuest_play_tokenid());
            returnMap.put("title", target.getTitle());
            returnMap.put("bgImg", target.getBgImg());
            returnMap.put("welcomMsg", target.getWelcomMsg());
            returnMap.put("link", target.getLink());
            returnMap.put("state", target.getState());
            returnMap.put("bjMemNo", target.getBjMemNo());
            returnMap.put("bjMemId", target.getBjMemId());
            returnMap.put("bjNickNm", target.getBjNickNm());
            returnMap.put("bjProfImg", target.getBjProfImg());
            if("real".equals(DalbitUtil.getActiceProfile())){
                returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_0.png");
            }else{
                returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_" + target.getBjLevel() + ".png");
            }
            returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
            returnMap.put("gstMemId", target.getGstMemId() == null ? "" : target.getGstMemId());
            returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
            returnMap.put("gstProfImg", target.getGstProfImg());
            returnMap.put("remainTime", remainTime);
            returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("auth", DalbitUtil.getIntMap(resultMap, "auth"));
            returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
            returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
            returnMap.put("isLike", DalbitUtil.isLogin(request) ? "1".equals(DalbitUtil.getStringMap(resultMap, "isGood")) : true);
            returnMap.put("isRecomm", target.getIsRecomm());
            returnMap.put("isPop", target.getIsPop());
            returnMap.put("isNew", target.getIsNew());
            returnMap.put("isSpecial", target.getIsSpecial());
            returnMap.put("startTs", target.getStartTs());
            returnMap.put("startDt", target.getStartDt());
            returnMap.put("hasNotice", !DalbitUtil.isEmpty(target.getNotice()));
            returnMap.put("hasStory", false);
            returnMap.put("useBoost", existsBoostByRoom(pRoomJoinVo.getRoom_no(), pRoomJoinVo.getMem_no()));    //부스터 사용여부
            returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
            /*returnMap.put("level", target.getLevel());
            returnMap.put("grade", target.getGrade());
            returnMap.put("exp", target.getExp());
            returnMap.put("expNext", target.getExpNext());
            returnMap.put("dalCnt", target.getRubyCnt());
            returnMap.put("byeolCnt", target.getGoldCnt());*/
            log.info("returnMap: {}",returnMap);
            procedureVo.setData(returnMap);

            SocketVo vo = socketService.getSocketVo(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            if(target.getState() == 2 || target.getState() == 3 || target.getState() == 0){
                try{
                    socketService.changeRoomState(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), target.getState(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), "join", vo);
                }catch(Exception e){
                    log.info("Socket Service changeRoomState Exception {}", e);
                }
            }

            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(returnMap, "likes"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                socketMap.put("fanRank", returnMap.get("fanRank"));
                socketService.changeCount(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_해당방이없음));
        } else if (procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_종료된방송));
        } else if (procedureVo.getRet().equals(Status.방송참여_이미참가.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String auth = DalbitUtil.getStringMap(resultMap, "auth");
            CodeVo codeVo = commonService.getCodeList("roomRight").stream().filter(code -> code.getCdNm().equals("방장")).findFirst().orElse(null);
            if((!DalbitUtil.isEmpty(codeVo) && auth.equals(codeVo.getCd()))){ //방송중 다른방 참가
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_방송중));
            }else{
                String deviceUuid = DalbitUtil.getStringMap(resultMap, "deviceUuid");
                deviceUuid = deviceUuid == null ? "" : deviceUuid.trim();
                if(deviceUuid.equals(pRoomJoinVo.getDeviceUuid())){ //동일기기 참가일때 /reToken과 동일로직
                    P_RoomStreamVo apiData = new P_RoomStreamVo();
                    apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
                    apiData.setMem_no(MemberVo.getMyMemNo(request));
                    apiData.setRoom_no(pRoomJoinVo.getRoom_no());

                    try{
                        result = callBroadcastRoomStreamSelect(apiData, request);
                    }catch(GlobalException e){
                        result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_이미참가));
                    }
                }else{
                    result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_이미참가));
                }
            }

        } else if (procedureVo.getRet().equals(Status.방송참여_입장제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_입장제한));
        } else if (procedureVo.getRet().equals(Status.방송참여_나이제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_나이제한));
        } else if (procedureVo.getRet().equals(Status.방송참여_강퇴시간제한.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_강퇴시간제한, data));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방참가실패));
        }

        return result;
    }


    /**
     * 방송방 나가기
     */
    public String callBroadCastRoomExit(P_RoomExitVo pRoomExitVo, HttpServletRequest request) {
        boolean isBj = false;
        if(!pRoomExitVo.getMem_no().startsWith("8")){
            P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
            pRoomMemberInfoVo.setTarget_mem_no(pRoomExitVo.getMem_no());
            pRoomMemberInfoVo.setRoom_no(pRoomExitVo.getRoom_no());
            pRoomMemberInfoVo.setMem_no(pRoomExitVo.getMem_no());
            ProcedureVo procedureInfoVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo);
            if(!DalbitUtil.isEmpty(procedureInfoVo.getData())){
                isBj = DalbitUtil.getIntMap((HashMap)procedureInfoVo.getData(), "auth") == 3;
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
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
                }
                try{
                    restService.deleteAntRoom(DalbitUtil.getStringMap(resultMap, "bjStreamId"), request);
                }catch(Exception e){}
            } else {
                String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
                String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
                String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");
                returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                try {
                    if (!"0".equals(request.getParameter("isSocket"))) {
                        socketService.chatEnd(pRoomExitVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.getAuthToken(request), 1, DalbitUtil.isLogin(request), vo);
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
                    }
                } catch (Exception e) {
                    log.info("Socket Service changeCount Exception {}", e);
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
                result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기));
            }
        }
        return result;
    }


    /**
     * 방송방 정보 수정
     */
    public String callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo, HttpServletRequest request) throws GlobalException {

        String systemBanWord = commonService.banWordSelect();

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

            HashMap returnMap = new HashMap();
            returnMap.put("roomType", pRoomEditOutVo.getSubject_type());
            returnMap.put("title", pRoomEditOutVo.getTitle());
            returnMap.put("welcomMsg", pRoomEditOutVo.getMsg_welcom());
            returnMap.put("bgImg", new ImageVo(pRoomEditOutVo.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("bgImgRacy", DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImageGrade()) ? 0 : pRoomEditVo.getBackgroundImageGrade());
            SocketVo vo = socketService.getSocketVo(pRoomEditOutVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                socketService.changeRoomInfo(pRoomEditOutVo.getRoomNo(), MemberVo.getMyMemNo(request), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
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
    public String callBroadCastRoomList(P_RoomListVo pRoomListVo){
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
            outVoList.add(new RoomOutVo(roomVoList.get(i)));
        }
        log.debug("set list time {} ms", ((new Date()).getTime() - st));
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        roomList.put("list", procedureOutputVo.getOutputBox());
        roomList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomListVo.getPageNo(), pRoomListVo.getPageCnt()));

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
     * 방송 정보조회
     */
    public String callBroadCastRoomInfoView(P_RoomInfoViewVo pRoomInfoViewVo, HttpServletRequest request) throws GlobalException{

        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);

        String result = "";
        if(procedureOutputVo.getRet().equals(Status.방정보보기.getMessageCode())) {

            P_RoomStreamVo apiData = new P_RoomStreamVo();
            apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            apiData.setRoom_no(pRoomInfoViewVo.getRoom_no());

            result = callBroadcastRoomStreamSelect(apiData, request);
        }else if(Status.방정보보기_회원번호아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_회원번호아님));
        }else if(Status.방정보보기_해당방없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_해당방없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기_실패));
        }
        return result;
    }


    /**
     * 방송 정보조회(방송방 생성,참여 후)
     */
    public ProcedureOutputVo callBroadCastRoomInfoViewReturnVo(P_RoomInfoViewVo pRoomInfoViewVo){
        ProcedureVo procedureVo = new ProcedureVo(pRoomInfoViewVo);
        P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomInfoViewVo)){
            return new ProcedureOutputVo(procedureVo);
        }else{
            roomInfoViewVo.setExt(procedureVo.getExt());
            procedureOutputVo = new ProcedureOutputVo(procedureVo, new RoomOutVo(roomInfoViewVo));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송진행여부체크_방송방없음));
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
            ItemDetailVo item = commonDao.selectItem("U1447");
            returnMap.put("roomCnt", roomCnt);
            returnMap.put("rank", rank);
            returnMap.put("boostCnt", DalbitUtil.getIntMap(resultMap, "usedItemCnt"));
            returnMap.put("boostTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            returnMap.put("boostCode", "U1447");
            returnMap.put("boostPrice", item.getCost());
            returnMap.put("boostByeul", item.getByeul());
            returnMap.put("boostLottie", "https://image.dalbitlive.com/ani/booster/booster_popup_200519.json");
            returnMap.put("boostWebp", "https://image.dalbitlive.com/ani/booster/booster_popup_200519.webp");
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
                String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
                String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
                String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");

                SocketVo vo = socketService.getSocketVo(pRoomLiveRankInfoVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
                socketMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                socketService.changeCount(pRoomLiveRankInfoVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
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
    public ProcedureVo getBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo){
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberInfoVo);
        roomDao.callBroadCastRoomMemberInfo(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
        String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
        String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");
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
        if("real".equals(DalbitUtil.getActiceProfile())){
            returnMap.put("holder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_0.png");
        }else{
            returnMap.put("holder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_" + DalbitUtil.getIntMap(resultMap, "level") + ".png");
        }
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
        returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
        returnMap.put("isNew", DalbitUtil.getIntMap(resultMap, "newdj_badge") == 1);
        returnMap.put("isSpecial", DalbitUtil.getIntMap(resultMap, "specialdj_badge") == 1);
        procedureVo.setData(returnMap);

        return procedureVo;
    }

    /**
     * 방송방 회원정보 조회
     */
    public String callBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo) {
        ProcedureVo procedureVo = getBroadCastRoomMemberInfo(pRoomMemberInfoVo);

        String result="";
        if(Status.방송방회원정보조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_성공, procedureVo.getData()));
        }else if(Status.방송방회원정보조회_요청회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_요청회원_번호비정상));
        }else if(Status.방송방회원정보조회_대상회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_대상회원_번호비정상));
        }else if(Status.방송방회원정보조회_대상회원_방에없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방회원정보조회_대상회원_방에없음));
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
                String fanRank1 = DalbitUtil.getStringMap(resultUpdateMap, "fanRank1");
                String fanRank2 = DalbitUtil.getStringMap(resultUpdateMap, "fanRank2");
                String fanRank3 = DalbitUtil.getStringMap(resultUpdateMap, "fanRank3");

                log.info("프로시저 응답 코드: {}", procedureVo.getRet());
                log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

                P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
                pRoomInfoViewVo.setMemLogin(pRoomStreamTokenVo.getMemLogin());
                pRoomInfoViewVo.setMem_no(pRoomStreamTokenVo.getMem_no());
                pRoomInfoViewVo.setRoom_no(pRoomStreamTokenVo.getRoom_no());

                //방송방 정보 조회
                ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);
                if(Status.방정보보기.getMessageCode().equals(roomInfoVo.getRet())) {
                    log.info(" 방송방 정보 조회 {}", roomInfoVo.getOutputBox());
                    RoomOutVo target = (RoomOutVo) roomInfoVo.getOutputBox();

                    HashMap returnMap = new HashMap();
                    returnMap.put("roomNo", pRoomStreamVo.getRoom_no());
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
                    returnMap.put("bjNickNm", target.getBjNickNm());
                    returnMap.put("bjProfImg", target.getBjProfImg());
                    if("real".equals(DalbitUtil.getActiceProfile())){
                        returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_0.png");
                    }else{
                        returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_" + target.getBjLevel() + ".png");
                    }
                    returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
                    returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
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
                    returnMap.put("hasStory", getHasStory(auth, pRoomStreamVo.getRoom_no(), MemberVo.getMyMemNo(request)));

                    returnMap.put("useBoost", existsBoostByRoom(pRoomStreamVo.getRoom_no(), pRoomStreamVo.getMem_no()));    //부스터 사용여부
                    returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                    /*returnMap.put("level", target.getLevel());
                    returnMap.put("grade", target.getGrade());
                    returnMap.put("exp", target.getExp());
                    returnMap.put("expNext", target.getExpNext());
                    returnMap.put("dalCnt", target.getRubyCnt());
                    returnMap.put("byeolCnt", target.getGoldCnt());*/
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
        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);

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
                    socketService.changeRoomState(stateVo.getRoomNo(), MemberVo.getMyMemNo(request), old_state, state, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
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
        ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);

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
                if("real".equals(DalbitUtil.getActiceProfile())){
                    returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_0.png");
                }else{
                    returnMap.put("bjHolder", "https://image.dalbitlive.com/level/frame/200513/AAA/ico_frame_" + target.getBjLevel() + ".png");
                }
                returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
                returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
                returnMap.put("gstProfImg", target.getGstProfImg());
                returnMap.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
                returnMap.put("hasStory", getHasStory(auth, pRoomStreamVo.getRoom_no(), MemberVo.getMyMemNo(request)));
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
                        HashMap resultUpdateMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                        String fanRank1 = DalbitUtil.getStringMap(resultUpdateMap, "fanRank1");
                        String fanRank2 = DalbitUtil.getStringMap(resultUpdateMap, "fanRank2");
                        String fanRank3 = DalbitUtil.getStringMap(resultUpdateMap, "fanRank3");

                        returnMap.put("likes", DalbitUtil.getIntMap(resultUpdateMap, "good"));
                        returnMap.put("rank", DalbitUtil.getIntMap(resultUpdateMap, "rank"));
                        returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));

                    /*if(auth == 3) { // DJ
                        try {
                            socketService.changeRoomState(pRoomStreamVo.getRoom_no(), MemberVo.getMyMemNo(request), 1, 0, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
                        } catch (Exception e) {
                            log.info("Socket Service changeRoomState Exception {}", e);
                        }
                    }*/

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
                        String fanRank1 = DalbitUtil.getStringMap(resultCountMap, "fanRank1");
                        String fanRank2 = DalbitUtil.getStringMap(resultCountMap, "fanRank2");
                        String fanRank3 = DalbitUtil.getStringMap(resultCountMap, "fanRank3");

                        returnMap.put("likes", DalbitUtil.getIntMap(resultCountMap, "good"));
                        returnMap.put("rank", DalbitUtil.getIntMap(resultCountMap, "rank"));
                        returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
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

    public boolean getHasStory(int auth, String room_no, String mem_no){
        if(auth == 3) { // DJ
            //사연조회
            P_RoomStoryListVo apiData = new P_RoomStoryListVo();
            apiData.setMem_no(mem_no);
            apiData.setRoom_no(room_no);
            apiData.setPageNo(1);
            apiData.setPageCnt(1);

            String resultStory = contentService.callGetStory(apiData);
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
}
