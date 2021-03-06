package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ActionDao;
import com.dalbit.broadcast.proc.Broadcast;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.BoosterVo;
import com.dalbit.broadcast.vo.request.GoodVo;
import com.dalbit.common.code.*;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.event.service.DallagersEventService;
import com.dalbit.event.service.EventService;
import com.dalbit.event.service.MoonLandService;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    MessageUtil messageUtil;

    @Autowired TTSService ttsService;

    @Autowired EventService eventService;

    @Autowired MoonLandService moonLandService;

    @Autowired UserService userService;

    @Autowired DallagersEventService dallaEvent;

    @Autowired Broadcast broadcast;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;
    @Value("${item.direct.min}")
    private int[] ITEM_DIRECT_MIN;
    @Value("${item.direct.max}")
    private int[] ITEM_DIRECT_MAX;
    @Value("${item.direct.code.main}")
    private String ITEM_DIRECT_CODE_MAIN;

    /**
     * ????????? ????????? ??????
     */
    public String callBroadCastRoomGood(P_RoomGoodVo pRoomGoodVo, GoodVo goodVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodVo);
        actionDao.callBroadCastRoomGood(procedureVo);

        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());
        log.info(" ### ???????????? ???????????? ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

        HashMap returnMap = new HashMap();
        returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
        returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
        returnMap.put("goodRank", DalbitUtil.getIntMap(resultMap, "goodRank"));
        procedureVo.setData(returnMap);

        String result;
        if(BroadcastStatus.?????????.getMessageCode().equals(procedureVo.getRet())) {
            SocketVo vo = socketService.getSocketObjectVo(pRoomGoodVo.getRoom_no(), pRoomGoodVo.getMem_no(), DalbitUtil.isLogin(request));
            try{ //????????? ??????
                boolean isFirst = true;
                if(DalbitUtil.profileCheck("real")){
                    isFirst = (DalbitUtil.getIntMap(resultMap, "firstGood") == 1);
                }
                boolean isLoveGood = DalbitUtil.getIntMap(resultMap, "goodRank") > 0;

                //????????? ?????????
                MoonLandCoinDataVO coinDataVO = null;
                coinDataVO = moonLandService.getSendLikeMoonLandCoinDataVO(coinDataVO, returnMap, pRoomGoodVo, goodVo);

                socketService.sendLike(pRoomGoodVo.getRoom_no(), pRoomGoodVo.getMem_no(), isFirst, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, isLoveGood, DalbitUtil.getIntMap(resultMap, "goodRank"),
                        new DeviceVo(request), coinDataVO);

                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendLike Exception {}", e);
            }

            try{ //????????????, ??????, ????????? ??????
                HashMap fanRankMap = commonService.getKingFanRankList(pRoomGoodVo.getRoom_no(), request);
                returnMap.put("fanRank", fanRankMap.get("list"));
                returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                returnMap.put("kingGender", fanRankMap.get("kingGender"));
                returnMap.put("kingAge", fanRankMap.get("kingAge"));
                returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(returnMap, "likes"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                socketMap.put("fanRank", fanRankMap.get("list"));
                socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                socketService.changeCount(pRoomGoodVo.getRoom_no(), pRoomGoodVo.getMem_no(), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//????????? ?????? ?????? ??????
                try{
                    socketService.sendLevelUp(pRoomGoodVo.getMem_no(), pRoomGoodVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){}
            }

            String resultCode = moonCheckSocket(pRoomGoodVo.getRoom_no(), request, "good");
            if("error".equals(resultCode)){
                log.error("????????? ?????? ??????");
            }

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????, procedureVo.getData()));
        }else if(BroadcastStatus.?????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????));
        }else if(BroadcastStatus.?????????_??????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????????????????));
        }else if(BroadcastStatus.?????????_?????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_?????????????????????));
        }else if(BroadcastStatus.?????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????));
        }

        return result;
    }


    /**
     * ????????? ???????????? ??????
     */
    public String callBroadCastShareLink(P_RoomShareLinkVo p_roomShareLinkVo) {
        ProcedureVo procedureVo = new ProcedureVo(p_roomShareLinkVo);
        List<P_RoomShareLinkVo> roomVoList = actionDao.callBroadCastRoomShareLink(procedureVo);

        log.debug("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.debug("???????????? ?????? ?????????: {}", procedureVo.getExt());
        log.debug(" ### ???????????? ???????????? ###");

        String result;
        if(BroadcastStatus.????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String roomNo = DalbitUtil.getStringMap(resultMap, "room_no");
            HashMap linkCheck = new HashMap();
            linkCheck.put("roomNo", roomNo);

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_??????, linkCheck));
        }else if(BroadcastStatus.????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????));
        }else if(BroadcastStatus.????????????_????????????????????????.getMessageCode().equals(procedureVo.getRet())){
            HashMap roomList = new HashMap();
            List<RoomShareLinkOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<roomVoList.size(); i++){
                outVoList.add(new RoomShareLinkOutVo(roomVoList.get(i)));
            }
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
            roomList.put("list", procedureOutputVo.getOutputBox());

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????????????????, roomList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_??????));
        }

        return result;
    }


    public String callBroadCastRoomShareLink(P_RoomInfoViewVo pRoomInfoViewVo, HttpServletRequest request) throws GlobalException{
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);
        String result = "";
        if(procedureOutputVo.getRet().equals(BroadcastStatus.???????????????.getMessageCode())) {
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
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
        }else if(BroadcastStatus.???????????????_??????????????????.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????????????????));
        }else if(BroadcastStatus.???????????????_???????????????.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_???????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????_??????));
        }
        return result;
    }

    private boolean actorCheck(String actorId) {
        boolean actorIdReg = false;
        String[] findActorIds = ttsService.findActorIds();

        for(int i=0; i<findActorIds.length; i++) {
            if(StringUtils.equals(findActorIds[i], actorId)) {
                actorIdReg = true;
                break;
            }
        }

        return actorIdReg;
    }

    private String banWordMasking(String roomNo, String word) {
        // ????????? ??????(????????? + ??????)
        BanWordVo banWordVo = new BanWordVo();
        String systemBanWord = commonService.banWordSelect();
        banWordVo.setRoomNo(roomNo);
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        if(!DalbitUtil.isEmpty(banWord)){
            word = DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, word);
        }
        if(!DalbitUtil.isEmpty(systemBanWord)){
            word = DalbitUtil.replaceMaskString(systemBanWord, word);
        }

        return word;
    }

    // TTS ?????? ????????? ????????? ???????????????
    private boolean hasTTSPermutationWord(String word) {
        return word.matches(".*[???-??????-??????-???a-zA-Z0-9]+.*");
    }

    /**
     * ????????? ????????????
     */
    public String callBroadCastRoomGift(P_RoomGiftVo pRoomGiftVo, HttpServletRequest request) {
        String result;
        String ttsText = pRoomGiftVo.getTtsText();
        String actorId = pRoomGiftVo.getActorId();
        String sendItemSuccessMsg = "????????? ????????? ???????????????."; // ?????? || tts

        if(ttsText != null) ttsText = ttsText.trim();

        // tts ?????????
        if(!StringUtils.isEmpty(ttsText)) {
            // actor ??????
            if(!actorCheck(actorId)) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_TTS_????????????));
            }

            ttsText = banWordMasking(pRoomGiftVo.getRoom_no(), ttsText); // ????????? *??? ????????? ??????

            if(!hasTTSPermutationWord(ttsText)) {
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_TTS_????????????????????????));
            }

            sendItemSuccessMsg += "\n?????? ??? ???????????? ???????????????.";
        }

        String item_code = pRoomGiftVo.getItem_code();
        boolean isDirect = false;
        boolean isStory = !StringUtils.equals(pRoomGiftVo.getStoryText(), "");  // ?????? ????????? ?????????
        int directItemCnt = 1;
        for(String direct : ITEM_DIRECT_CODE){
            if(direct.equals(pRoomGiftVo.getItem_code())){
                isDirect = true;
                break;
            }
        }
        if(ITEM_DIRECT_CODE_MAIN.equals(pRoomGiftVo.getItem_code()) || isDirect){ //?????????????????? ????????? ??????
            isDirect = true;
            for(int i = 0; i <  ITEM_DIRECT_MIN.length; i++){
                if (ITEM_DIRECT_MIN[i] <= pRoomGiftVo.getItem_cnt() && (ITEM_DIRECT_MAX[i] >= pRoomGiftVo.getItem_cnt() || ITEM_DIRECT_MAX[i] == -1)) {
                    item_code = ITEM_DIRECT_CODE[i];
                    if(ITEM_DIRECT_MAX[i] == -1){
                        directItemCnt = 2;
                    }
                    break;
                }
            }
        }
        ProcedureVo procedureVo = new ProcedureVo(pRoomGiftVo);
        actionDao.callBroadCastRoomGift(procedureVo);

        if(BroadcastStatus.??????????????????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
            log.info("???????????? ?????? ?????????: {}", resultMap);
            log.info(" ### ???????????? ???????????? ###");

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
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);

            HashMap fanRankMap = commonService.getKingFanRankList(pRoomGiftVo.getRoom_no(), request);
            returnMap.put("fanRank", fanRankMap.get("list"));
            returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
            returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
            returnMap.put("kingGender", fanRankMap.get("kingGender"));
            returnMap.put("kingAge", fanRankMap.get("kingAge"));
            returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

            SocketVo vo = socketService.getSocketObjectVo(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no(), DalbitUtil.isLogin(request));
            try{
                HashMap itemMap = new HashMap();
                itemMap.put("itemNo", item_code);
                itemMap.put("itemMoveNo", pRoomGiftVo.getItem_code());
                SocketVo vo1 = socketService.getSocketVo(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getGifted_mem_no(), DalbitUtil.isLogin(request));

                ItemDetailVo item = commonDao.selectItem(item_code);
                String itemNm = item.getItemNm();
                String itemThumbs = item.getThumbs();
                itemMap.put("itemNm", itemNm);
                itemMap.put("itemCnt", isDirect ? 1 : pRoomGiftVo.getItem_cnt());
//                itemMap.put("repeatCnt", isDirect ? directItemCnt : (DalbitUtil.isEmpty(item.getSoundFileUrl()) ? pRoomGiftVo.getItem_cnt() : pRoomGiftVo.getItem_cnt() * 2));
                itemMap.put("repeatCnt", isDirect ? directItemCnt : pRoomGiftVo.getItem_cnt());
                itemMap.put("itemImg", itemThumbs);
                itemMap.put("isSecret", "1".equals(pRoomGiftVo.getSecret()));
                itemMap.put("itemType", isStory? "story" : isDirect ? "direct" : "items");
                itemMap.put("authName", vo1.getAuthName());
                itemMap.put("auth", vo1.getAuth());
                itemMap.put("nickNm", vo1.getMemNk());
                itemMap.put("memNo", vo1.getMemNo());
                itemMap.put("dalCnt", item.getByeol() * pRoomGiftVo.getItem_cnt());
                itemMap.put("storyText", pRoomGiftVo.getStoryText());
                // tts ??????
                itemMap.put("ttsText", ttsText);
                itemMap.put("actorId", actorId);
                returnMap.put("message", itemMap.get("nickNm") + sendItemSuccessMsg);

                try {
                    // tts ??????
                    String ttsYn = pRoomGiftVo.getTtsYn();
                    TtsLogVo ttsLogVo = new TtsLogVo();

                    ttsLogVo.setTtsYn(ttsYn); // tts ????????? ?????? ??????
                    ttsLogVo.setMemNo(pRoomGiftVo.getMem_no()); // ?????????
                    ttsLogVo.setPmemNo(pRoomGiftVo.getGifted_mem_no()); // ?????????
                    ttsLogVo.setItemCode(pRoomGiftVo.getItem_code());
                    ttsLogVo.setTtsMsg(ttsText); // ????????? ??????
                    ttsLogVo.setSendItemCnt(isDirect ? 1 : pRoomGiftVo.getItem_cnt()); // ????????? ?????????
                    ttsLogVo.setSendDalCnt(item.getByeol() * pRoomGiftVo.getItem_cnt()); // ?????? ??????

                    if(StringUtils.equals(ttsYn, "y")) {
                        String[] actorInfo = ttsService.getTtsActorSlct(pRoomGiftVo.getActorId());
                        ttsLogVo.setItemName(actorInfo[1]); // ????????? ??????
                        ttsLogVo.setTtsCrtSlct(actorInfo[0]); // actor ??????(a: ????????????, b: ??????)
                    }

                    broadcast.ttsLogIns(ttsLogVo);
                } catch (Exception e) {
                    log.error("ActionService/callBroadCastRoomGift tts ?????? ins ??????", e);
                }

                //????????? ?????????
                MoonLandCoinDataVO coinDataVO = null;
                coinDataVO = moonLandService.getSendItemMoonLandCoinDataVO(coinDataVO, pRoomGiftVo, (int) itemMap.get("dalCnt"), item_code );
                itemMap.put("coinData", coinDataVO); //????????? ?????? ?????????

                //???????????? ?????? ?????????: itemMap - put {stoneInfo, feverInfo}
                dallaEvent.getDallagersPacketData(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no(), pRoomGiftVo.getGifted_mem_no(), itemMap, "gift");

                socketService.giftItem(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no(), "1".equals(pRoomGiftVo.getSecret()) ? pRoomGiftVo.getGifted_mem_no() : "", itemMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service giftItem Exception {}", e);
            }
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                //TODO - ????????? ?????? ???????????? ?????? ??????
                //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
                socketMap.put("fanRank", returnMap.get("fanRank"));
                socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "isNewFan") > 0 ? DalbitUtil.getIntMap(resultMap, "isNewFan") :  DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                socketService.changeCount(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no(), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//????????? ?????? ?????? ??????
                try{
                    socketService.sendLevelUp(pRoomGiftVo.getMem_no(), pRoomGiftVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            String djMemNo = DalbitUtil.getStringMap(resultMap, "dj_mem_no");
            if(DalbitUtil.getIntMap(resultMap, "dj_levelUp") == 1){//DJ ????????? ?????? ?????? ??????
                try{
                    socketService.sendLevelUp(djMemNo, pRoomGiftVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
                try{
                    socketService.sendDjLevelUp(pRoomGiftVo.getRoom_no(), request, vo, pRoomGiftVo.getMem_no(), DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            //????????? ??????
            String resultCode = moonCheckSocket(pRoomGiftVo.getRoom_no(), request, "gift");
            if("error".equals(resultCode)){
                log.error("????????? ?????? ??????");
            }

            Status status;
            if("1".equals(pRoomGiftVo.getSecret())){
                status = pRoomGiftVo.getGifted_mem_no().equals(djMemNo) ? BroadcastStatus.DJ_??????_?????????????????? : BroadcastStatus.?????????_??????_??????????????????;
            }else {
                status = pRoomGiftVo.getGifted_mem_no().equals(djMemNo) ? BroadcastStatus.DJ_?????????????????? : BroadcastStatus.?????????_??????????????????;
            }
            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        }else if(BroadcastStatus.????????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????_???????????????));
        }else if(BroadcastStatus.????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_???????????????));
        }else if(BroadcastStatus.????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_???????????????));
        }else if(BroadcastStatus.????????????_????????????_????????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????_????????????????????????));
        }else if(BroadcastStatus.????????????_????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????_??????????????????));
        }else if(BroadcastStatus.????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????));
        }else if(BroadcastStatus.????????????_?????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_?????????????????????));
        }else if(BroadcastStatus.????????????_?????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_?????????));
        }else if(CommonStatus.?????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.?????????????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_??????));
        }

        return result;
    }

    /**
     * ????????? ????????????
     */
    public String callBroadCastRoomBooster(P_RoomBoosterVo pRoomBoosterVo, BoosterVo boosterVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomBoosterVo);
        actionDao.callBroadCastRoomBooster(procedureVo);

        String result;
        if(BroadcastStatus.???????????????.getMessageCode().equals(procedureVo.getRet())) {
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
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);

            log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
            log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());
            log.info(" ### ???????????? ???????????? ###");

            //????????? ????????? ??????
            MoonLandCoinDataVO coinDataVO = null;
            coinDataVO = moonLandService.getSendBoosterMoonLandCoinDataVO(coinDataVO, request, pRoomBoosterVo, boosterVo, returnMap);

            SocketVo vo = socketService.getSocketObjectVo(pRoomBoosterVo.getRoom_no(), pRoomBoosterVo.getMem_no(), DalbitUtil.isLogin(request));
            try{
                socketService.sendBooster(pRoomBoosterVo.getRoom_no(), pRoomBoosterVo.getMem_no(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendBooster Exception {}", e);
            }

            try{
                HashMap itemMap = new HashMap();
                SocketVo vo1 = socketService.getSocketVo(pRoomBoosterVo.getRoom_no(), DalbitUtil.getStringMap(resultMap, "dj_mem_no"), DalbitUtil.isLogin(request));
                ItemDetailVo item = commonDao.selectItem(pRoomBoosterVo.getItem_cnt() < 10 ? DalbitUtil.getProperty("item.code.boost") : DalbitUtil.getProperty("item.code.rocket.boost"));
                //String itemNm = item.getItemNm();
                String itemThumbs = item.getThumbs();
                itemMap.put("itemNo", pRoomBoosterVo.getItem_cnt() < 10 ? DalbitUtil.getProperty("item.code.boost") : DalbitUtil.getProperty("item.code.rocket.boost"));
                itemMap.put("itemNm", "?????????");   //???????????????, ??????????????? ???????????? ???????????? ?????????????????? ????????????...
                itemMap.put("itemCnt", pRoomBoosterVo.getItem_cnt());
                itemMap.put("repeatCnt", pRoomBoosterVo.getItem_cnt() < 10 ? pRoomBoosterVo.getItem_cnt() : 1);
                itemMap.put("itemImg", itemThumbs);
                itemMap.put("isSecret", false);
                itemMap.put("itemType", "boost");
                itemMap.put("authName", vo1.getAuthName());
                itemMap.put("auth", vo1.getAuth());
                itemMap.put("nickNm", vo1.getMemNk());
                itemMap.put("dalCnt", item.getByeol());
                itemMap.put("memNo", vo1.getMemNo());
                itemMap.put("isGuest", false);
                itemMap.put("coinData", coinDataVO);    //????????? ?????? ?????? (?????????)

                //???????????? ?????? ????????? returnMap - put {stoneInfo, feverInfo}
                dallaEvent.getDallagersPacketData(pRoomBoosterVo.getRoom_no(), pRoomBoosterVo.getMem_no(), boosterVo.getMemNo(), itemMap, "booster");

                socketService.giftItem(pRoomBoosterVo.getRoom_no(), pRoomBoosterVo.getMem_no(), "", itemMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendBooster Exception {}", e);
            }
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));

                HashMap fanRankMap = commonService.getKingFanRankList(pRoomBoosterVo.getRoom_no(), request);
                returnMap.put("fanRank", fanRankMap.get("list"));
                returnMap.put("kingMemNo", fanRankMap.get("kingMemNo"));
                returnMap.put("kingNickNm", fanRankMap.get("kingNickNm"));
                returnMap.put("kingGender", fanRankMap.get("kingGender"));
                returnMap.put("kingAge", fanRankMap.get("kingAge"));
                returnMap.put("kingProfImg", fanRankMap.get("kingProfImg"));

                socketMap.put("fanRank", fanRankMap.get("list"));
                socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                socketService.changeCount(pRoomBoosterVo.getRoom_no(), pRoomBoosterVo.getMem_no(), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//????????? ?????? ?????? ??????
                try{
                    socketService.sendLevelUp(pRoomBoosterVo.getMem_no(), pRoomBoosterVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            if(DalbitUtil.getIntMap(resultMap, "dj_levelUp") == 1){//DJ ????????? ?????? ?????? ??????
                try{
                    String djMemNo = DalbitUtil.getStringMap(resultMap, "dj_mem_no");
                    socketService.sendLevelUp(djMemNo, pRoomBoosterVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }

                try{
                    socketService.sendDjLevelUp(pRoomBoosterVo.getRoom_no(), request, vo, pRoomBoosterVo.getMem_no(), DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            //????????? ??????
            String resultCode = moonCheckSocket(pRoomBoosterVo.getRoom_no(), request, "booster");
            if("error".equals(resultCode)){
                log.error("????????? ?????? ??????");
            }

            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.???????????????, returnMap));
        }else if(BroadcastStatus.?????????_????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????_???????????????));
        }else if(BroadcastStatus.?????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_???????????????));
        }else if(BroadcastStatus.?????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_???????????????));
        }else if(BroadcastStatus.?????????_????????????_????????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????_????????????????????????));
        }else if(BroadcastStatus.?????????_?????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_?????????????????????));
        }else if(BroadcastStatus.?????????_??????????????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????????????????????????????));
        }else if(BroadcastStatus.?????????_?????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_?????????));
        }else if(CommonStatus.?????????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.?????????????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????));
        }

        return result;
    }


    /**
     * ?????? ?????? ??????
     */
    public String callroomExtendTime(P_ExtendTimeVo pExtendTimeVo, HttpServletRequest request) {

        log.error("?????? ?????? ?????? start - pExtendTimeVo: {}, {}", pExtendTimeVo.getRoom_no(), pExtendTimeVo.getMem_no());
        ProcedureVo procedureVo = new ProcedureVo(pExtendTimeVo);
        actionDao.callroomExtendTime(procedureVo);

        log.error("ret: {}, {}", procedureVo.getRet(), pExtendTimeVo.getMem_no());
        String result;
        if(BroadcastStatus.??????????????????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            SocketVo vo = socketService.getSocketVo(pExtendTimeVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                log.error("reqRoomChangeTime start - extendEndDate: {}", DalbitUtil.getStringMap(resultMap, "extendEndDate"));
                socketService.roomChangeTime(pExtendTimeVo.getRoom_no(), pExtendTimeVo.getMem_no(), DalbitUtil.getStringMap(resultMap, "extendEndDate"), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                log.error("reqRoomChangeTime end");
                vo.resetData();
            }catch(Exception e){}
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????));
        }else if(BroadcastStatus.????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????));
        }else if(BroadcastStatus.????????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_???????????????));
        }else if(BroadcastStatus.????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????));
        }else if(BroadcastStatus.????????????_??????????????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_??????????????????));
        }else if(BroadcastStatus.????????????_????????????_5?????????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.????????????_????????????_5?????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.??????????????????));
        }

        return result;
    }


    /**
     * ????????? ?????? ?????????
     */
    public String callRoomFreeze(P_FreezeVo pFreezeVo, HttpServletRequest request) {
        HashMap returnMap = new HashMap();
        returnMap.put("isFreeze", false);

        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        pRoomInfoViewVo.setMem_no(pFreezeVo.getMem_no());
        pRoomInfoViewVo.setRoom_no(pFreezeVo.getRoom_no());
        //????????? ????????? ?????? ?????? (????????? ?????? ??????)
        ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);
        if(((RoomOutVo) roomInfoVo.getOutputBox()).getFreezeMsg() == 2){
            if(pFreezeVo.getFreezeMsg() == 0){
                returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_????????????_DJ????????????.getMessageKey()));
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????_DJ????????????, returnMap));
            }else if(pFreezeVo.getFreezeMsg() == 1){
                returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_????????????_???????????????.getMessageKey()));
                return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????_???????????????, returnMap));
            }
        };

        ProcedureVo procedureVo = new ProcedureVo(pFreezeVo);
        actionDao.callRoomFreeze(procedureVo);

        String result;
        if(BroadcastStatus.?????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            boolean isFreeze = DalbitUtil.getIntMap(resultMap, "freezeMsg") == 1;
            returnMap.put("isFreeze", isFreeze);
            try{
                returnMap.put("msg", isFreeze ? messageUtil.get(BroadcastStatus.?????????_??????.getMessageKey()) : messageUtil.get(BroadcastStatus.?????????_??????.getMessageKey()));
                socketService.changeRoomFreeze(pFreezeVo.getMem_no(), pFreezeVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){}

            if(isFreeze){
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????, returnMap));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????, returnMap));
            }
        }else if(BroadcastStatus.?????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_????????????.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????, returnMap));
        }else if(BroadcastStatus.?????????_???????????????.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_???????????????.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_???????????????, returnMap));
        }else if(BroadcastStatus.?????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_????????????.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????, returnMap));
        }else if(BroadcastStatus.?????????_????????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_????????????_????????????.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????_????????????, returnMap));
        }else if(BroadcastStatus.?????????_????????????.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_????????????.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_????????????, returnMap));
        }else{
            returnMap.put("msg", messageUtil.get(BroadcastStatus.?????????_??????.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.?????????_??????, returnMap));
        }

        return result;
    }


    /**
     * ????????? ????????? ??????
     */
    public String callMoon(P_MoonVo pMoonVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMoonVo);
        actionDao.callMoon(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        List<HashMap> resultList = new ArrayList<>();
        returnMap.put("step", DalbitUtil.getIntMap(resultMap, "step"));

        returnMap.put("broadcastTime", DalbitUtil.getIntMap(resultMap, "broadcastTime"));
        returnMap.put("targetTime", DalbitUtil.getIntMap(resultMap, "targetTime"));
        returnMap.put("textTime", DalbitUtil.getIntMap(resultMap, "broadcastTime") >= DalbitUtil.getIntMap(resultMap, "targetTime") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "broadcastTime") + "/" + DalbitUtil.getIntMap(resultMap, "targetTime")+"???");
        if(DalbitUtil.getIntMap(resultMap, "targetTime") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "????????????");
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "broadcastTime"));
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetTime"));
            data.put("countStr", DalbitUtil.getIntMap(resultMap, "broadcastTime") >= DalbitUtil.getIntMap(resultMap, "targetTime") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "broadcastTime") + "/" + DalbitUtil.getIntMap(resultMap, "targetTime")+"???");
            resultList.add(data);
        }

        returnMap.put("totalCount", DalbitUtil.getIntMap(resultMap, "totalCount"));
        returnMap.put("targetCount", DalbitUtil.getIntMap(resultMap, "targetCount"));
        returnMap.put("textCount", DalbitUtil.getIntMap(resultMap, "totalCount") >= DalbitUtil.getIntMap(resultMap, "targetCount") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "totalCount") + "/" + DalbitUtil.getIntMap(resultMap, "targetCount")+"???");
        if(DalbitUtil.getIntMap(resultMap, "targetCount") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "?????? ?????????");
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "totalCount"));
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetCount"));
            data.put("countStr", DalbitUtil.getIntMap(resultMap, "totalCount") >= DalbitUtil.getIntMap(resultMap, "targetCount") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "totalCount") + "/" + DalbitUtil.getIntMap(resultMap, "targetCount")+"???");
            resultList.add(data);
        }

        returnMap.put("giftedByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
        returnMap.put("targetByeol", DalbitUtil.getIntMap(resultMap, "targetByeol"));
        returnMap.put("textByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "giftedByeol") + "/" + DalbitUtil.getIntMap(resultMap, "targetByeol")+"???");
        if(DalbitUtil.getIntMap(resultMap, "targetByeol") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "??????");
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetByeol"));
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
            if(DalbitUtil.getIntMap(resultMap, "auth") == 3) {
                data.put("countStr", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "giftedByeol") + "/" + DalbitUtil.getIntMap(resultMap, "targetByeol")+"???");
            }else{
                String textByeol = "????????? ?????? ??????!";
                int ingPercent = (int)((DalbitUtil.getDoubleMap(resultMap, "giftedByeol") / DalbitUtil.getDoubleMap(resultMap, "targetByeol")) * 100);
                if(ingPercent >= 100){
                    textByeol = "CLEAR!";
                    data.put("currentCount", 100);
                    data.put("maxCount", 100);
                }else if(ingPercent >= 70){
                    textByeol = "?????? ??? ????????????!";
                    data.put("currentCount", ingPercent);
                    data.put("maxCount", 100);
                }else if(ingPercent >= 35){
                    textByeol = "????????? ?????? ??????!";
                    data.put("currentCount", ingPercent);
                    //data.put("currentCount", (ingPercent / 10.0) * 10);
                    data.put("maxCount", 100);
                }else{
                    data.put("currentCount", 0);
                }
                data.put("countStr", textByeol);
            }
            resultList.add(data);
        }

        returnMap.put("goodPoint", DalbitUtil.getIntMap(resultMap, "goodPoint"));
        returnMap.put("targetGood", DalbitUtil.getIntMap(resultMap, "targetGood"));
        returnMap.put("textGood", DalbitUtil.getIntMap(resultMap, "goodPoint") >= DalbitUtil.getIntMap(resultMap, "targetGood") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "goodPoint") + "/" + DalbitUtil.getIntMap(resultMap, "targetGood")+"???");
        if(DalbitUtil.getIntMap(resultMap, "targetGood") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "?????????");
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "goodPoint"));
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetGood"));
            data.put("countStr", DalbitUtil.getIntMap(resultMap, "goodPoint") >= DalbitUtil.getIntMap(resultMap, "targetGood") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "goodPoint") + "/" + DalbitUtil.getIntMap(resultMap, "targetGood")+"???");
            resultList.add(data);
        }

        returnMap.put("dlgText", DalbitUtil.getStringMap(resultMap, "dlgText"));
        returnMap.put("helpText1", DalbitUtil.getStringMap(resultMap, "helpText1"));
        returnMap.put("helpText2", DalbitUtil.getStringMap(resultMap, "helpText2"));
        returnMap.put("helpText3", DalbitUtil.getStringMap(resultMap, "helpText3"));

        //???????????? ??????
        if(DalbitUtil.getIntMap(resultMap, "auth") != 3){
            returnMap.put("giftedByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? DalbitUtil.getIntMap(resultMap, "targetByeol") : DalbitUtil.getIntMap(resultMap, "giftedByeol"));
            returnMap.put("textByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? "CLEAR!" : "ING..");
        }
        returnMap.put("results", resultList);

        String result ="";
        if(procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_??????, returnMap));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_?????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_?????????, returnMap));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_????????????));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_?????????_??????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_?????????_??????));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_????????????));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_???????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_???????????????));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_??????));
        }
        return result;
    }


    /**
     * ????????? ????????? ?????? (?????????)
     */
    public String callMoonCheck(P_MoonCheckVo pMoonCheckVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMoonCheckVo);
        actionDao.callMoonCheck(procedureVo);

        String result ="";
        if(procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode()) || procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode()) || procedureVo.getRet().equals(EventStatus.?????????_?????????.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("moonStep", DalbitUtil.getIntMap(resultMap, "step"));
            returnMap.put("oldStep", DalbitUtil.getIntMap(resultMap, "oldStep"));
            returnMap.put("broadcastTime", DalbitUtil.getIntMap(resultMap, "broadcastTime"));
            returnMap.put("totalCount", DalbitUtil.getIntMap(resultMap, "totalCount"));
            returnMap.put("giftedByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
            returnMap.put("goodPoint", DalbitUtil.getIntMap(resultMap, "goodPoint"));
            returnMap.put("dlgTitle", DalbitUtil.getStringMap(resultMap, "dlgTitle"));
            returnMap.put("dlgText", DalbitUtil.getStringMap(resultMap, "dlgText"));
            returnMap.put("moonStepFileNm", "");
            returnMap.put("moonStepAniFileNm", "");
            returnMap.put("aniDuration", 0);
            returnMap.put("fullmoon_yn", DalbitUtil.getIntMap(resultMap, "fullmoon_yn"));

            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.?????????_??????.getCode(), String.valueOf(returnMap.get("moonStep"))));
            if (!DalbitUtil.isEmpty(codeVo)) {
                returnMap.put("moonStepFileNm", codeVo.getValue());
                returnMap.put("aniDuration", codeVo.getSortNo());
                if (DalbitUtil.getIntMap(resultMap, "step") == 4) {
                    String code = Code.?????????_???????????????.getCode();
                    String value = DalbitUtil.randomMoonAniValue();

                    if(procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode())){
                        code = Code.?????????_???????????????.getCode();
                        value = "1";
                    }
                    var aniCodeVo = commonService.selectCodeDefine(new CodeVo(code, value));
                    if (!DalbitUtil.isEmpty(aniCodeVo)) {
                        returnMap.put("moonStepAniFileNm", aniCodeVo.getValue());
                        returnMap.put("aniDuration", aniCodeVo.getSortNo());
                    }
                }
            }

            //????????? ??????
            if (DalbitUtil.getIntMap(resultMap, "fullmoon_yn") == 1 && DalbitUtil.getIntMap(resultMap, "step") != DalbitUtil.getIntMap(resultMap, "oldStep")) {
                String resultCode = moonCheckSocket(pMoonCheckVo.getRoom_no(), request, "server", returnMap);
                if ("error".equals(resultCode)) {
                    log.error("????????? ?????? ??????");
                }
            }
            Status status = null;
            if(procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode())){
                status = EventStatus.?????????_??????;
            }else if(procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode())){
                status = EventStatus.?????????_??????;
            }else{
                status = EventStatus.?????????_?????????;
            }
            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_?????????_??????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_?????????_??????));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_????????????));
        } else if(procedureVo.getRet().equals(EventStatus.?????????_???????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_???????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(EventStatus.?????????_??????));
        }
        return result;
    }

    /**
     * ????????? ????????? ??????
     */
    public HashMap callMoonCheckMap(P_MoonCheckVo pMoonCheckVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMoonCheckVo);
        actionDao.callMoonCheck(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        if(Integer.parseInt(procedureVo.getRet()) >= 0){
            returnMap.put("moonStep", DalbitUtil.getIntMap(resultMap, "step"));
            returnMap.put("oldStep", DalbitUtil.getIntMap(resultMap, "oldStep"));
            returnMap.put("broadcastTime", DalbitUtil.getIntMap(resultMap, "broadcastTime"));
            returnMap.put("totalCount", DalbitUtil.getIntMap(resultMap, "totalCount"));
            returnMap.put("giftedByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
            returnMap.put("goodPoint", DalbitUtil.getIntMap(resultMap, "goodPoint"));
            returnMap.put("dlgTitle", DalbitUtil.getStringMap(resultMap, "dlgTitle"));
            returnMap.put("dlgText", DalbitUtil.getStringMap(resultMap, "dlgText"));
            returnMap.put("moonStepFileNm", "");
            returnMap.put("moonStepAniFileNm", "");
            returnMap.put("aniDuration", 0);
            returnMap.put("fullmoon_yn", DalbitUtil.getIntMap(resultMap, "fullmoon_yn"));

            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.?????????_??????.getCode(), String.valueOf(returnMap.get("moonStep"))));
            if(!DalbitUtil.isEmpty(codeVo)){
                returnMap.put("moonStepFileNm", codeVo.getValue());
                returnMap.put("aniDuration", codeVo.getSortNo());
                if(DalbitUtil.getIntMap(resultMap, "step") == 4){
                    String code = Code.?????????_???????????????.getCode();
                    String value = DalbitUtil.randomMoonAniValue();

                    if(procedureVo.getRet().equals(EventStatus.?????????_??????.getMessageCode())){
                        code = Code.?????????_???????????????.getCode();
                        value = "1";
                    }
                    var aniCodeVo = commonService.selectCodeDefine(new CodeVo(code, value));
                    if(!DalbitUtil.isEmpty(aniCodeVo)) {
                        returnMap.put("moonStepAniFileNm", aniCodeVo.getValue());
                        returnMap.put("aniDuration", aniCodeVo.getSortNo());
                    }

                    String resultCode = moonCheckSocket(pMoonCheckVo.getRoom_no(), request, "", returnMap);
                    if("error".equals(resultCode)){
                        log.error("????????? ?????? ??????");
                    }
                }
            }
        }else{
            returnMap.put("fullmoon_yn", 0);
        }

        return returnMap;
    }


    /**
     *  ????????? ?????? ????????????(??????)
     */
    public String moonCheckSocket(String roomNo, HttpServletRequest request, String callState){
        return moonCheckSocket(roomNo, request, callState, null);
    }
    public String moonCheckSocket(String roomNo, HttpServletRequest request, String callState, HashMap checkMap){

        String result;
        SocketVo vo = socketService.getSocketVo(roomNo, MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
        P_MoonCheckVo pMoonCheckVo = new P_MoonCheckVo();
        pMoonCheckVo.setRoom_no(roomNo);
        if(checkMap == null){
            checkMap = callMoonCheckMap(pMoonCheckVo, request);
        }
        try{ //????????? ??????
            if(DalbitUtil.getIntMap(checkMap, "fullmoon_yn") == 1){
                if("roomJoin".equals(callState) || "server".equals(callState)){
                    socketService.sendMoonCheck(roomNo, checkMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), callState);
                }else{
                    if(checkMap.get("moonStep") != checkMap.get("oldStep")){
                        socketService.sendMoonCheck(roomNo, checkMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), callState);
                    }
                }
            }
            vo.resetData();
            result = "success";
        }catch(Exception e){
            if (DalbitUtil.getIntMap(checkMap, "moonStep") == 4){
                log.error("Socket Service MoonCheck Complete Exception {}, {}", e, callState);
            }else{
                log.info("Socket Service MoonCheck Complete Exception {}, {}", e, callState);
            }
            result = "error";
        }
        return result;
    }
}
