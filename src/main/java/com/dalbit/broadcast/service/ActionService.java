package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ActionDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_LevelUpCheckVo;
import com.dalbit.rest.service.RestService;
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
import java.util.Map;

@Slf4j
@Service
public class ActionService {

    @Autowired
    ActionDao actionDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonService commonService;
    @Autowired
    SocketService socketService;
    @Autowired
    RoomService roomService;
    @Autowired
    RestService restService;
    @Autowired
    CommonDao commonDao;
    @Autowired
    ProfileDao profileDao;


    /**
     * 방송방 좋아요 추가
     */
    public String callBroadCastRoomGood(P_RoomGoodVo pRoomGoodVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodVo);
        actionDao.callBroadCastRoomGood(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
        returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
        procedureVo.setData(returnMap);

        String result;
        if(Status.좋아요.getMessageCode().equals(procedureVo.getRet())) {
            String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
            String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
            String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");
            SocketVo vo = socketService.getSocketVo(pRoomGoodVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{ //좋아요 발송
                boolean isFirst = true;
                if(DalbitUtil.profileCheck("real")){
                    isFirst = (DalbitUtil.getIntMap(resultMap, "firstGood") == 1);
                }
                socketService.sendLike(pRoomGoodVo.getRoom_no(), new MemberVo().getMyMemNo(request), isFirst, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendLike Exception {}", e);
            }

            try{ //좋아요수, 랭킹, 팬랭킹 발송
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(returnMap, "likes"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                socketMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                socketService.changeCount(pRoomGoodVo.getRoom_no(), new MemberVo().getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            if(!"real".equals(DalbitUtil.getActiceProfile()) || DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//레벨업 일때 소켓 발송
                try{
                    socketService.sendLevelUp(new MemberVo().getMyMemNo(request), pRoomGoodVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){}
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요, procedureVo.getData()));
        }else if(Status.좋아요_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_회원아님));
        }else if(Status.좋아요_해당방송없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_해당방송없음));
        }else if(Status.좋아요_방송참가자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_방송참가자아님));
        }else if(Status.좋아요_이미했음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_이미했음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.좋아요_실패));
        }

        return result;
    }


    /**
     * 방송방 공유링크 확인
     */
    public String callBroadCastShareLink(P_RoomShareLinkVo p_roomShareLinkVo) {
        ProcedureVo procedureVo = new ProcedureVo(p_roomShareLinkVo);
        List<P_RoomShareLinkVo> roomVoList = actionDao.callBroadCastRoomShareLink(procedureVo);

        log.debug("프로시저 응답 코드: {}", procedureVo.getRet());
        log.debug("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.debug(" ### 프로시저 호출결과 ###");

        String result;
        if(Status.링크체크_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String roomNo = DalbitUtil.getStringMap(resultMap, "room_no");
            HashMap linkCheck = new HashMap();
            linkCheck.put("roomNo", roomNo);

            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_성공, linkCheck));
        }else if(Status.링크체크_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_회원아님));
        }else if(Status.링크체크_방이종료되어있음.getMessageCode().equals(procedureVo.getRet())){
            HashMap roomList = new HashMap();
            List<RoomShareLinkOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<roomVoList.size(); i++){
                outVoList.add(new RoomShareLinkOutVo(roomVoList.get(i)));
            }
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
            roomList.put("list", procedureOutputVo.getOutputBox());

            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_방이종료되어있음, roomList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_실패));
        }

        return result;
    }


    public String callBroadCastRoomShareLink(P_RoomInfoViewVo pRoomInfoViewVo, HttpServletRequest request) throws GlobalException{
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo);
        String result = "";
        if(procedureOutputVo.getRet().equals(Status.방정보보기.getMessageCode())) {
            RoomOutVo target = (RoomOutVo) procedureOutputVo.getOutputBox();
            HashMap returnMap = new HashMap();
            returnMap.put("roomNo", pRoomInfoViewVo.getRoom_no());
            returnMap.put("title", target.getTitle());

            Map<String, Object> firebaseMap = restService.makeFirebaseDynamicLink(pRoomInfoViewVo.getRoom_no(), target.getLink(), target.getBjNickNm(), target.getBjProfImg().getUrl(), target.getTitle(), request);
            String dynamicLink = "";
            if(!DalbitUtil.isEmpty(firebaseMap) && !DalbitUtil.isEmpty(firebaseMap.get("shortLink"))){
                dynamicLink = (String)firebaseMap.get("shortLink");
            }
            if(DalbitUtil.isEmpty(dynamicLink)){
                returnMap.put("shareLink", DalbitUtil.getProperty("server.www.url") + "/l/" + target.getLink() + "?etc={\"push_type\":1,\"room_no\":\"" + pRoomInfoViewVo.getRoom_no() + "\"}");
            }else{
                returnMap.put("shareLink", dynamicLink);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.방정보보기, returnMap));
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
     * 방송방 선물하기
     */
    public String callBroadCastRoomGift(P_RoomGiftVo pRoomGiftVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGiftVo);
        actionDao.callBroadCastRoomGift(procedureVo);

        String result;
        if(Status.선물하기성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
            String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
            String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");
            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", resultMap);
            log.info(" ### 프로시저 호출결과 ###");

            HashMap returnMap = new HashMap();
            returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
            returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
            returnMap.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
            returnMap.put("expBegin", DalbitUtil.getIntMap(resultMap, "expBegin"));
            returnMap.put("expNext", DalbitUtil.getIntMap(resultMap, "expNext"));
            returnMap.put("expRate", DalbitUtil.getExpRate(DalbitUtil.getIntMap(resultMap, "exp"), DalbitUtil.getIntMap(resultMap, "expBegin"), DalbitUtil.getIntMap(resultMap, "expNext")));
            returnMap.put("dalCnt", DalbitUtil.getIntMap(resultMap, "ruby"));
            returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "gold"));
            returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
            returnMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));

            SocketVo vo = socketService.getSocketVo(pRoomGiftVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                HashMap itemMap = new HashMap();
                itemMap.put("itemNo", pRoomGiftVo.getItem_code());

                ItemDetailVo item = commonDao.selectItem(pRoomGiftVo.getItem_code());
                String itemNm = item.getItemNm();
                String itemThumbs = item.getThumbs();
                itemMap.put("itemNm", itemNm);
                itemMap.put("itemCnt", pRoomGiftVo.getItem_cnt());
                itemMap.put("itemImg", itemThumbs);
                itemMap.put("isSecret", "1".equals(pRoomGiftVo.getSecret()));
                itemMap.put("itemType", "items");
                socketService.giftItem(pRoomGiftVo.getRoom_no(), new MemberVo().getMyMemNo(request), "1".equals(pRoomGiftVo.getSecret()) ? pRoomGiftVo.getGifted_mem_no() : "", itemMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service giftItem Exception {}", e);
            }
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                //TODO - 레벨업 유무 소켓추가 추후 확인
                //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
                socketMap.put("fanRank", returnMap.get("fanRank"));
                socketService.changeCount(pRoomGiftVo.getRoom_no(), new MemberVo().getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            if(DalbitUtil.getIntMap(resultMap, "dj_levelUp") == 1){//DJ 레벨업 일때 소켓 발송
                log.error("dj_levelUp : {}", resultMap.toString());
                try{
                    socketService.sendDjLevelUp(pRoomGiftVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
                try{
                    String djMemNo = DalbitUtil.getStringMap(resultMap, "dj_mem_no");
                    socketService.sendLevelUp(djMemNo, pRoomGiftVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//레벨업 일때 소켓 발송
                log.error("levelUp : {}", resultMap.toString());
                try{
                    socketService.sendLevelUp(new MemberVo().getMyMemNo(request), pRoomGiftVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기성공, returnMap));
        }else if(Status.선물하기_요청회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_요청회원_번호비정상));
        }else if(Status.선물하기_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_해당방없음));
        }else if(Status.선물하기_해당방종료.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_해당방종료));
        }else if(Status.선물하기_요청회원_해당방청취자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_요청회원_해당방청취자아님));
        }else if(Status.선물하기_받는회원_해당방에없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_받는회원_해당방에없음));
        }else if(Status.선물하기_없는대상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_없는대상));
        }else if(Status.선물하기_아이템번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_아이템번호없음));
        }else if(Status.선물하기_달부족.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_달부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_실패));
        }

        return result;
    }


    /**
     * 부스터 사용하기
     */
    public String callBroadCastRoomBooster(P_RoomBoosterVo pRoomBoosterVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomBoosterVo);
        actionDao.callBroadCastRoomBooster(procedureVo);

        String result;
        if(Status.부스터성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
            int roomCnt = DalbitUtil.getIntMap(resultMap, "totalRoomCnt");
            int rank = DalbitUtil.getIntMap(resultMap, "rank");
            if(rank > roomCnt){
                roomCnt = rank;
            }
            returnMap.put("roomCnt", roomCnt);
            returnMap.put("rank", rank);
            returnMap.put("boostCnt", DalbitUtil.getIntMap(resultMap, "usedItemCnt"));
            returnMap.put("boostTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");

            SocketVo vo = socketService.getSocketVo(pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                socketService.sendBooster(pRoomBoosterVo.getRoom_no(), new MemberVo().getMyMemNo(request), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendBooster Exception {}", e);
            }

            try{
                HashMap itemMap = new HashMap();
                itemMap.put("itemNo", DalbitUtil.getProperty("item.code.boost"));

                ItemDetailVo item = commonDao.selectItem(DalbitUtil.getProperty("item.code.boost"));
                String itemNm = item.getItemNm();
                String itemThumbs = item.getThumbs();
                itemMap.put("itemNm", itemNm);
                itemMap.put("itemCnt", 1);
                itemMap.put("itemImg", itemThumbs);
                itemMap.put("isSecret", false);
                itemMap.put("itemType", "boost");

                socketService.giftItem(pRoomBoosterVo.getRoom_no(), new MemberVo().getMyMemNo(request), "", itemMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendBooster Exception {}", e);
            }
            try{
                String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
                String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
                String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                //TODO - 레벨업 유무 소켓추가 추후 확인
                //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
                socketMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                socketService.changeCount(pRoomBoosterVo.getRoom_no(), new MemberVo().getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }


            if(DalbitUtil.getIntMap(resultMap, "dj_levelUp") == 1){//DJ 레벨업 일때 소켓 발송
                log.error("dj_levelUp : {}", resultMap.toString());
                try{
                    socketService.sendDjLevelUp(pRoomBoosterVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
                try{
                    String djMemNo = DalbitUtil.getStringMap(resultMap, "dj_mem_no");
                    socketService.sendLevelUp(djMemNo, pRoomBoosterVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//레벨업 일때 소켓 발송
                log.error("levelUp : {}", resultMap.toString());
                try{
                    socketService.sendLevelUp(new MemberVo().getMyMemNo(request), pRoomBoosterVo.getRoom_no(), request, vo);
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터성공, returnMap));

        }else if(Status.부스터_요청회원_번호비정상.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_요청회원_번호비정상));
        }else if(Status.부스터_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_해당방없음));
        }else if(Status.부스터_해당방종료.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_해당방종료));
        }else if(Status.부스터_요청회원_해당방청취자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_요청회원_해당방청취자아님));
        }else if(Status.부스터_아이템번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_아이템번호없음));
        }else if(Status.부스터_사용불가능아이템번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_사용불가능아이템번호));
        }else if(Status.부스터_달부족.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_달부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_실패));
        }

        return result;
    }


    /**
     * 방송 시간 연장
     */
    public String callroomExtendTime(P_ExtendTimeVo pExtendTimeVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pExtendTimeVo);
        actionDao.callroomExtendTime(procedureVo);

        String result;
        if(Status.시간연장성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            SocketVo vo = socketService.getSocketVo(pExtendTimeVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                socketService.roomChangeTime(pExtendTimeVo.getRoom_no(), pExtendTimeVo.getMem_no(), DalbitUtil.getStringMap(resultMap, "extendEndDate"), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){}
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장성공));
        }else if(Status.시간연장_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장_회원아님));
        }else if(Status.시간연장_방번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장_방번호없음));
        }else if(Status.시간연장_종료된방.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장_종료된방));
        }else if(Status.시간연장_이미한번연장.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장_이미한번연장));
        }else if(Status.시간연장_남은시간_5분안됨.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장_남은시간_5분안됨));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.시간연장실패));
        }

        return result;
    }
}
