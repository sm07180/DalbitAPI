package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RoomService {

    @Autowired
    RoomDao roomDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;
    @Autowired
    RestService restService;
    @Autowired
    RoomService roomService;
    @Autowired
    CommonService commonService;

    /**
     * 방송방 생성
     */
    public String callBroadCastRoomCreate(P_RoomCreateVo pRoomCreateVo) {
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
            returnMap.put("bgImg", target.getBgImg().getUrl());
            returnMap.put("link", target.getLink());
            returnMap.put("bjMemNo", target.getBjMemNo());
            returnMap.put("bjNickNm", target.getBjNickNm());
            returnMap.put("bjProfImg", target.getBjProfImg());
            // 임의정보
            returnMap.put("bjHolder", "https://devimage.dalbitcast.com/holder/gold.png");
            returnMap.put("likes", 0);
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("roomRole", 3);
            returnMap.put("roleRight", "1111111111");
            returnMap.put("isFan", false);
            /*returnMap.put("level", target.getLevel());
            returnMap.put("grade", target.getGrade());
            returnMap.put("exp", target.getExp());
            returnMap.put("expNext", target.getExpNext());
            returnMap.put("rubyCnt", target.getRubyCnt());
            returnMap.put("goldCnt", target.getGoldCnt());*/
            procedureVo.setData(returnMap);

            if(!DalbitUtil.isEmpty(pRoomCreateVo.getBackgroundImage()) && !pRoomCreateVo.getBackgroundImage().startsWith("/default")){
                try{
                    restService.imgDone("/temp" + pRoomCreateVo.getBackgroundImage());
                }catch (GlobalException e){
                    //TODO 이미지 서버 오류 시 처리
                    e.printStackTrace();
                }
            }
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송생성_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성_회원아님)));
        } else if (procedureVo.getRet().equals(Status.방송중인방존재.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송중인방존재)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방생성실패)));
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
            returnMap.put("bgImg", target.getBgImg().getUrl());
            returnMap.put("link", target.getLink());
            returnMap.put("bjMemNo", target.getBjMemNo());
            returnMap.put("bjNickNm", target.getBjNickNm());
            returnMap.put("bjProfImg", target.getBjProfImg());
            returnMap.put("gstMemNo", target.getGstMemNo() == null ? "" : target.getGstMemNo());
            returnMap.put("gstNickNm", target.getGstNickNm() == null ? "" : target.getGstNickNm());
            returnMap.put("gstProfImg", target.getGstProfImg());
            returnMap.put("remainTime", remainTime);
            // 임의정보
            returnMap.put("bjHolder", "https://devimage.dalbitcast.com/holder/gold.png");
            returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("roomRole", DalbitUtil.getIntMap(resultMap, "auth"));
            returnMap.put("roleRight", DalbitUtil.getStringMap(resultMap, "controlRole"));
            returnMap.put("isFan", "1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
            returnMap.put("isLike", "0".equals(DalbitUtil.getStringMap(resultMap, "isLike")));
            /*returnMap.put("level", target.getLevel());
            returnMap.put("grade", target.getGrade());
            returnMap.put("exp", target.getExp());
            returnMap.put("expNext", target.getExpNext());
            returnMap.put("rubyCnt", target.getRubyCnt());
            returnMap.put("goldCnt", target.getGoldCnt());*/
            returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
            log.info("returnMap: {}",returnMap);
            procedureVo.setData(returnMap);

            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여성공, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_회원아님)));
        } else if (procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_해당방이없음)));
        } else if (procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_종료된방송)));
        } else if (procedureVo.getRet().equals(Status.방송참여_이미참가.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_이미참가)));
        } else if (procedureVo.getRet().equals(Status.방송참여_입장제한.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_입장제한)));
        } else if (procedureVo.getRet().equals(Status.방송참여_나이제한.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_나이제한)));
        } else if (procedureVo.getRet().equals(Status.방송참여_강퇴시간제한.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_강퇴시간제한, data)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방참가실패)));
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
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기, returnMap)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_회원아님)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_해당방이없음)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_종료된방송)));
        } else if (procedureVo.getRet().equals(Status.방송나가기_방참가자아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_방참가자아님)));
        } else if (procedureVo.getRet().equals(Status.방송나가기실패.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기실패)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방참가실패)));
        }
        return result;
    }

    /**
     * 방송방 정보 수정
     */
    public String callBroadCastRoomEdit(P_RoomEditVo pRoomEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomEditVo);
        roomDao.callBroadCastRoomEdit(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(Status.방송정보수정성공.getMessageCode())) {
            if(!DalbitUtil.isEmpty(pRoomEditVo.getBackgroundImage()) && !pRoomEditVo.getBackgroundImage().startsWith("/default")){
                String delImg = pRoomEditVo.getBackgroundImageDelete();
                if(!DalbitUtil.isEmpty(delImg) && delImg.startsWith("/default")){
                    delImg = null;
                }
                try{
                    restService.imgDone("/temp" + pRoomEditVo.getBackgroundImage(), delImg);
                }catch (GlobalException e){
                    //TODO 이미지 서버 오류 시 처리
                    e.printStackTrace();
                }
            }

            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정성공)));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정_회원아님)));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_해당방에없는회원번호.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정_해당방에없는회원번호)));
        } else if (procedureVo.getRet().equals(Status.방송정보수정_수정권이없는회원.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정_수정권이없는회원)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송정보수정실패)));
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
        HashMap roomList = new HashMap();
        roomList.put("list", procedureOutputVo.getOutputBox());
        roomList.put("paging ", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomListVo.getPageNo(), pRoomListVo.getPageCnt()));

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
    public String callBroadCastRoomInfoView(P_RoomInfoViewVo pRoomInfoViewVo) {

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
    public ProcedureOutputVo callBroadCastRoomInfoViewReturnVo(P_RoomInfoViewVo pRoomInfoViewVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomInfoViewVo);
        P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureVo);
        roomInfoViewVo.setExt(procedureVo.getExt());

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomInfoViewVo)){
            return null;
        }else{
            procedureOutputVo = new ProcedureOutputVo(procedureVo, new RoomOutVo(roomInfoViewVo));
        }

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        return procedureOutputVo;
    }
}
