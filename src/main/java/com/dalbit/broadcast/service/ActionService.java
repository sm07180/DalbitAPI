package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ActionDao;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.procedure.P_RoomBoosterVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGiftVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGoodVo;
import com.dalbit.broadcast.vo.procedure.P_RoomShareLinkVo;
import com.dalbit.common.code.Item;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.MemberVo;
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
public class ActionService {

    @Autowired
    ActionDao actionDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonService commonService;
    @Autowired
    SocketService socketService;


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

        String result="";
        if(Status.좋아요.getMessageCode().equals(procedureVo.getRet())) {
            String fanRank1 = DalbitUtil.getStringMap(resultMap, "fanRank1");
            String fanRank2 = DalbitUtil.getStringMap(resultMap, "fanRank2");
            String fanRank3 = DalbitUtil.getStringMap(resultMap, "fanRank3");
            try{ //좋아요 발송
                socketService.sendLike(pRoomGoodVo.getRoom_no(), MemberVo.getMyMemNo(), (DalbitUtil.getIntMap(resultMap, "firstGood") == 1), DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
            try{ //좋아요수, 랭킹, 팬랭킹 발송
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(returnMap, "likes"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                socketMap.put("fanRank", commonService.getFanRankList(fanRank1, fanRank2, fanRank3));
                socketService.changeCount(pRoomGoodVo.getRoom_no(), MemberVo.getMyMemNo(), socketMap, DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
            //TODO - 레벨업 유무 소켓추가 추후 확인
            try{
                //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
            }catch(Exception e){}
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

        //방송방 종료 또는 없을 시 방송방 정보 List 가져오기
        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(roomVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomShareLinkOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<roomVoList.size(); i++){
                outVoList.add(new RoomShareLinkOutVo(roomVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap roomList = new HashMap();
        roomList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        //링크체크 성공시 방번호 가져오기
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String roomNo = DalbitUtil.getStringMap(resultMap, "room_no");
        HashMap linkCheck = new HashMap();
        linkCheck.put("roomNo", roomNo);

        String result="";
        if(Status.링크체크_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_성공, linkCheck));
        }else if(Status.링크체크_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_회원아님));
        }else if(Status.링크체크_해당방이없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_해당방이없음, roomList));
        }else if(Status.링크체크_방이종료되어있음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_방이종료되어있음, roomList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.링크체크_실패));
        }

        return result;
    }


    /**
     * 방송방 선물하기
     */
    public String callBroadCastRoomGift(P_RoomGiftVo pRoomGiftVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGiftVo);
        actionDao.callBroadCastRoomGift(procedureVo);

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

        String result="";
        if(Status.선물하기성공.getMessageCode().equals(procedureVo.getRet())) {
            try{
                HashMap itemMap = new HashMap();
                itemMap.put("itemNo", pRoomGiftVo.getItem_no());
                String itemNm = "곰토끼";
                if(Item.애니_곰인형.getItemNo().equals(pRoomGiftVo.getItem_no())) {
                    itemNm = "곰인형";
                }else if(Item.애니_도너츠달.getItemNo().equals(pRoomGiftVo.getItem_no())){
                    itemNm = "도너츠달";
                }else if(Item.애니_도너츠.getItemNo().equals(pRoomGiftVo.getItem_no())){
                    itemNm = "도너츠";
                }else if(Item.스티커_곰.getItemNo().equals(pRoomGiftVo.getItem_no())){
                    itemNm = "곰";
                }else if(Item.스티커_도너츠.getItemNo().equals(pRoomGiftVo.getItem_no())){
                    itemNm = "도너츠";
                }
                itemMap.put("itemNm", itemNm);
                itemMap.put("itemCnt", pRoomGiftVo.getItem_cnt());
                itemMap.put("isSecret", "1".equals(pRoomGiftVo.getSecret()));
                socketService.giftItem(pRoomGiftVo.getRoom_no(), MemberVo.getMyMemNo(), "1".equals(pRoomGiftVo.getSecret()) ? pRoomGiftVo.getGifted_mem_no() : "", itemMap, DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                //TODO - 레벨업 유무 소켓추가 추후 확인
                //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);
                socketMap.put("fanRank", returnMap.get("fanRank"));
                socketService.changeCount(pRoomGiftVo.getRoom_no(), MemberVo.getMyMemNo(), socketMap, DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
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

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
        returnMap.put("roomCnt", DalbitUtil.getIntMap(resultMap, "totalRoomCnt"));
        returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
        returnMap.put("boostCnt", DalbitUtil.getIntMap(resultMap, "usedItemCnt"));
        returnMap.put("boostTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result="";
        if(Status.부스터성공.getMessageCode().equals(procedureVo.getRet())) {
            try{
                socketService.sendBooster(pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(), DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
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
                socketService.changeCount(pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(), socketMap, DalbitUtil.getAuthToken(request));
            }catch(Exception e){}
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
        }else if(Status.부스터_루비부족.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_루비부족));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_실패));
        }

        return result;
    }
}
