package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.RoomGiftHistoryOutVo;
import com.dalbit.broadcast.vo.RoomOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.StateVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
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


    /**
     * 방송방 생성
     */
    public String callBroadCastRoomCreate(P_RoomCreateVo pRoomCreateVo) throws GlobalException{
        String bgImg = pRoomCreateVo.getBackgroundImage();
        Boolean isDone = false;
        if(DalbitUtil.isEmpty(bgImg)){
            bgImg = Code.포토_배경_디폴트_PREFIX.getCode()+"/"+Code.배경이미지_파일명_PREFIX.getCode()+(DalbitUtil.randomValue("number", 1))+".jpg";
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
            log.info(" ### 방송방 정보 조회 시작 ###");

            P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
            pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
            pRoomInfoViewVo.setMem_no(pRoomCreateVo.getMem_no());
            pRoomInfoViewVo.setRoom_no(roomNo);

            //방송방 정보 조회
            ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);
            log.info(" 방송방 정보 조회 {}", roomInfoVo.getOutputBox());
            RoomOutVo target = (RoomOutVo) roomInfoVo.getOutputBox();

            log.info(" ### 프로시저 호출결과 ###");
            HashMap returnMap = new HashMap();
            returnMap.put("roomNo", roomNo);
            returnMap.put("bjStreamId",pRoomCreateVo.getBj_streamid());
            returnMap.put("bjPubToken", pRoomCreateVo.getBj_publish_tokenid());
            returnMap.put("title", target.getTitle());
            returnMap.put("bgImg", target.getBgImg());
            returnMap.put("link", target.getLink());
            returnMap.put("state", target.getState());
            returnMap.put("bjMemNo", target.getBjMemNo());
            returnMap.put("bjNickNm", target.getBjNickNm());
            returnMap.put("bjProfImg", target.getBjProfImg());
            returnMap.put("isRecomm", target.getIsRecomm());
            returnMap.put("isPop", target.getIsPop());
            returnMap.put("isNew", target.getIsNew());
            returnMap.put("startDt", target.getStartDt());
            returnMap.put("startTs", target.getStartTs());
            // 임의정보
            returnMap.put("bjHolder", "https://devimage.dalbitcast.com/holder/gold.png");
            returnMap.put("likes", 0);
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("auth", 3);
            returnMap.put("ctrlRole", "1111111111");
            returnMap.put("isFan", false);

            /*returnMap.put("level", target.getLevel());
            returnMap.put("grade", target.getGrade());
            returnMap.put("exp", target.getExp());
            returnMap.put("expNext", target.getExpNext());
            returnMap.put("dalCnt", target.getRubyCnt());
            returnMap.put("byeolCnt", target.getGoldCnt());*/
            procedureVo.setData(returnMap);

            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomCreateVo.getBackgroundImage()));
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
    public String callBroadCastRoomJoin(P_RoomJoinVo pRoomJoinVo) {
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

            log.info(" ### 프로시저 호출결과 ###");

            HashMap returnMap = new HashMap();
            returnMap.put("roomNo",pRoomJoinVo.getRoom_no());
            returnMap.put("bjStreamId",pRoomJoinVo.getBj_streamid());
            returnMap.put("bjPlayToken",pRoomJoinVo.getBj_play_tokenid());
            returnMap.put("gstStreamId",pRoomJoinVo.getGuest_streamid());
            returnMap.put("gstPlayToken",pRoomJoinVo.getGuest_play_tokenid());
            returnMap.put("title", target.getTitle());
            returnMap.put("bgImg", target.getBgImg());
            returnMap.put("link", target.getLink());
            returnMap.put("state", target.getState());
            returnMap.put("bjMemNo", target.getBjMemNo());
            returnMap.put("bjNickNm", target.getBjNickNm());
            returnMap.put("bjProfImg", target.getBjProfImg());
            returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
            returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
            returnMap.put("gstProfImg", target.getGstProfImg());
            returnMap.put("remainTime", remainTime);
            returnMap.put("isRecomm", target.getIsRecomm());
            returnMap.put("isPop", target.getIsPop());
            returnMap.put("isNew", target.getIsNew());
            returnMap.put("startDt", target.getStartDt());
            returnMap.put("startTs", target.getStartTs());
            // 임의정보
            returnMap.put("bjHolder", "https://devimage.dalbitcast.com/holder/gold.png");
            returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("auth", DalbitUtil.getIntMap(resultMap, "auth"));
            returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
            returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
            returnMap.put("isLike", "0".equals(DalbitUtil.getStringMap(resultMap, "isLike")));
            /*returnMap.put("level", target.getLevel());
            returnMap.put("grade", target.getGrade());
            returnMap.put("exp", target.getExp());
            returnMap.put("expNext", target.getExpNext());
            returnMap.put("dalCnt", target.getRubyCnt());
            returnMap.put("byeolCnt", target.getGoldCnt());*/
            returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
            log.info("returnMap: {}",returnMap);
            procedureVo.setData(returnMap);

            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_해당방이없음));
        } else if (procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_종료된방송));
        } else if (procedureVo.getRet().equals(Status.방송참여_이미참가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송참여_이미참가));
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
    public String callBroadCastRoomExit(P_RoomExitVo pRoomExitVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomExitVo);
        roomDao.callBroadCastRoomExit(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
        String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
        String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));

        String result;
        if(procedureVo.getRet().equals(Status.방송나가기.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기, returnMap));
        } else if (procedureVo.getRet().equals(Status.방송나가기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송나가기_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_해당방이없음));
        } else if (procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_종료된방송));
        } else if (procedureVo.getRet().equals(Status.방송나가기_방참가자아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기_방참가자아님));
        } else if (procedureVo.getRet().equals(Status.방송나가기실패.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송나가기실패));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방참가실패));
        }
        return result;
    }


    /**
     * 방송방 정보 수정
     */
    public String callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo) throws GlobalException {
        Boolean isDone = false;
        if(!DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImage()) && pRoomEditVo.getBackgroundImage().startsWith(Code.포토_배경_임시_PREFIX.getCode())){
            isDone = true;
            pRoomEditVo.setBackgroundImage(DalbitUtil.replacePath(pRoomEditVo.getBackgroundImage()));
        }

        ProcedureVo procedureVo = new ProcedureVo(pRoomEditVo);
        P_RoomEditOutVo pRoomEditOutVo = roomDao.callBroadCastRoomEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.방송정보수정성공.getMessageCode())) {
            if(isDone){
                String delImg = pRoomEditVo.getBackgroundImageDelete();
                if(!DalbitUtil.isEmpty(delImg) && delImg.startsWith(Code.포토_배경_디폴트_PREFIX.getCode())){
                    delImg = null;
                }
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomEditVo.getBackgroundImage()), delImg);
            }

            HashMap returnMap = new HashMap();
            returnMap.put("roomType", pRoomEditOutVo.getRoomNo());
            returnMap.put("title", pRoomEditOutVo.getTitle());
            returnMap.put("welcomMsg", pRoomEditOutVo.getMsg_welcom());
            returnMap.put("bgImg", new ImageVo(pRoomEditOutVo.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("bgImgRacy", DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImageGrade()) ? 0 : pRoomEditVo.getBackgroundImageGrade());

            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_회원아님));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_해당방에없는회원번호.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_해당방에없는회원번호));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_수정권이없는회원.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정_수정권이없는회원));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송정보수정실패));
        }

        return result;
    }


    /**
     * 방송방 리스트
     */
    public String callBroadCastRoomList(P_RoomListVo pRoomListVo){
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);

        List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<roomVoList.size(); i++){
                outVoList.add(new RoomOutVo(roomVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        if(procedureOutputVo == null){
            return gsonUtil.toJson(new JsonOutputVo(Status.방송리스트없음));
        }

        HashMap roomList = new HashMap();
        roomList.put("list", procedureOutputVo.getOutputBox());
        roomList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomListVo.getPageNo(), pRoomListVo.getPageCnt()));

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result = "";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트조회, roomList));
        }else if(Status.방송리스트_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트_회원아님));
        }else if(Status.방송리스트없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송리스트조회_실패));
        }
        return result;
    }


    /**
     * 방송 정보조회
     */
    public String callBroadCastRoomInfoView(P_RoomInfoViewVo pRoomInfoViewVo){

        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);

        String result = "";
        if(procedureOutputVo.getRet().equals(Status.방정보보기.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기, procedureOutputVo.getOutputBox()));
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
     * 방송방 현재 순위, 아이템 사용 현황 조회
     */
    public String callBroadCastRoomLiveRankInfo(P_RoomLiveRankInfoVo pRoomLiveRankInfoVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomLiveRankInfoVo);
        roomDao.callBroadCastRoomLiveRankInfo(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("roomCnt", DalbitUtil.getIntMap(resultMap, "totalRoomCnt"));
        returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
        returnMap.put("boostCnt", DalbitUtil.getIntMap(resultMap, "usedItemCnt"));
        returnMap.put("boostTime", DalbitUtil.getIntMap(resultMap, "remainTime"));

        String result="";
        if(Status.순위아이템사용_조회성공.getMessageCode().equals(procedureVo.getRet())) {
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
     * 방송방 선물받은 내역보기
     */
    public String callBroadCastRoomGiftHistory(P_RoomGiftHistoryVo pRoomGiftHistoryVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGiftHistoryVo);
        List<P_RoomGiftHistoryVo> giftHistoryListVo = roomDao.callBroadCastRoomGiftHistory(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(giftHistoryListVo)){
            procedureOutputVo = null;
        }else{
            List<RoomGiftHistoryOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<giftHistoryListVo.size(); i++){
                outVoList.add(new RoomGiftHistoryOutVo(giftHistoryListVo.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);

        HashMap giftHistoryList = new HashMap();
        giftHistoryList.put("totalCnt", DalbitUtil.getIntMap(resultMap, "totalCnt"));
        giftHistoryList.put("totalGold", DalbitUtil.getIntMap(resultMap, "totalGold"));
        giftHistoryList.put("list", procedureOutputVo.getOutputBox());
        giftHistoryList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomGiftHistoryVo.getPageNo(), pRoomGiftHistoryVo.getPageCnt()));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받은내역조회, giftHistoryList));
        }else if(Status.선물받은내역없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물받은내역없음));
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
    public String callBroadCastRoomMemberInfo(P_RoomMemberInfoVo pRoomMemberInfoVo) {
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
        returnMap.put("holder", "https://devimage.dalbitcast.com/holder/gold.png");
        returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
        returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
        returnMap.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
        returnMap.put("expBegin", DalbitUtil.getIntMap(resultMap, "expBegin"));
        returnMap.put("expNext", DalbitUtil.getIntMap(resultMap, "expNext"));
        returnMap.put("fanCnt", DalbitUtil.getIntMap(resultMap, "fanCount"));
        returnMap.put("starCnt", DalbitUtil.getIntMap(resultMap, "starCount"));
        returnMap.put("isFan", DalbitUtil.getIntMap(resultMap, "enableFan") == 0 ? true : false);
        returnMap.put("auth", DalbitUtil.getIntMap(resultMap, "auth"));
        returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
        returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
        procedureVo.setData(returnMap);

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
    public String callBroadcastRoomStreamSelect(P_RoomStreamVo pRoomStreamVo) throws GlobalException {
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
                bjPubToken = (String)restService.antToken(bjStreamId, "publish").get("tokenId");
                if(!DalbitUtil.isEmpty(gstStreamId)){
                    //bjPlayToken = (String)restService.antToken(bjStreamId, "play").get("tokenId");
                    //gstPubToken = (String)restService.antToken(gstStreamId, "publish").get("tokenId");
                    gstPlayToken = (String)restService.antToken(gstStreamId, "play").get("tokenId");
                }
            }else{
                bjPlayToken = (String)restService.antToken(bjStreamId, "play").get("tokenId");
                if(!DalbitUtil.isEmpty(gstStreamId)){
                    if(auth == 2) { //게스트
                        gstPubToken = (String)restService.antToken(gstStreamId, "publish").get("tokenId");
                    }else{
                        gstPlayToken = (String)restService.antToken(gstStreamId, "play").get("tokenId");
                    }
                }
                /*if(!DalbitUtil.isEmpty(gstStreamId)){
                    gstPlayToken = (String)restService.antToken(gstStreamId, "play").get("tokenId");
                }
                if(auth == 2){ //게스트
                    bjPubToken = (String)restService.antToken(bjStreamId, "publish").get("tokenId");
                    gstPubToken = (String)restService.antToken(gstStreamId, "publish").get("tokenId");
                }*/
            }

            P_RoomStreamTokenVo pRoomStreamTokenVo = new P_RoomStreamTokenVo();
            pRoomStreamTokenVo.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
            pRoomStreamTokenVo.setMem_no(MemberVo.getMyMemNo());
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
                    returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
                    returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
                    returnMap.put("gstProfImg", target.getGstProfImg());
                    returnMap.put("isRecomm", target.getIsRecomm());
                    returnMap.put("isPop", target.getIsPop());
                    returnMap.put("isNew", target.getIsNew());
                    returnMap.put("startDt", target.getStartDt());
                    returnMap.put("startTs", target.getStartTs());
                    // 임의정보
                    returnMap.put("bjHolder", "https://devimage.dalbitcast.com/holder/gold.png");
                    returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                    returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
                    returnMap.put("auth", DalbitUtil.getIntMap(resultMap, "auth"));
                    returnMap.put("ctrlRole", DalbitUtil.getStringMap(resultMap, "controlRole"));
                    returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
                    returnMap.put("isLike", "0".equals(DalbitUtil.getStringMap(resultMap, "isLike")));
                    /*returnMap.put("level", target.getLevel());
                    returnMap.put("grade", target.getGrade());
                    returnMap.put("exp", target.getExp());
                    returnMap.put("expNext", target.getExpNext());
                    returnMap.put("dalCnt", target.getRubyCnt());
                    returnMap.put("byeolCnt", target.getGoldCnt());*/
                    returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                    log.info("returnMap: {}", returnMap);

                    result = gsonUtil.toJson(new JsonOutputVo(Status.스트림아이디_조회성공, returnMap));
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
        pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        pRoomInfoViewVo.setMem_no(MemberVo.getMyMemNo());
        pRoomInfoViewVo.setRoom_no(stateVo.getRoomNo());
        ProcedureOutputVo procedureOutputVo = callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);

        String result="";
        if(procedureOutputVo.getRet().equals(Status.방정보보기.getMessageCode())) {
            RoomOutVo target = (RoomOutVo) procedureOutputVo.getOutputBox();
            int old_state = target.getState();
            P_RoomStateUpdateVo pRoomStateUpdateVo = new P_RoomStateUpdateVo();
            pRoomStateUpdateVo.setMem_no(MemberVo.getMyMemNo());
            pRoomStateUpdateVo.setRoom_no(stateVo.getRoomNo());
            int state = 1;
            if(stateVo.getIsCall()) {
                state = 3;
            }else if(!stateVo.getIsMic()){
                state = 2;
            }
            pRoomStateUpdateVo.setState(state);

            ProcedureVo procedureVo = new ProcedureVo(pRoomStateUpdateVo);
            roomDao.callBroadCastRoomStateUpate(procedureVo);

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");

            if (procedureVo.getRet().equals(Status.방송방상태변경_성공.getMessageCode())) {
                try{
                    socketService.changeRoomState(stateVo.getRoomNo(), MemberVo.getMyMemNo(), old_state, state, DalbitUtil.getAuthToken(request));
                }catch(Exception e){}
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
}
