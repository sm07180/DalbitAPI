package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ActionDao;
import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.MoonLandCoinDataVO;
import com.dalbit.broadcast.vo.RoomOutVo;
import com.dalbit.broadcast.vo.RoomShareLinkOutVo;
import com.dalbit.broadcast.vo.TTSSpeakVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.BoosterVo;
import com.dalbit.broadcast.vo.request.GoodVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ItemVo;
import com.dalbit.event.service.EventService;
import com.dalbit.event.service.MoonLandService;
import com.dalbit.event.vo.GganbuPocketStatInsVo;
import com.dalbit.event.vo.MoonLandInfoVO;
import com.dalbit.event.vo.MoonLandMissionInsVO;
import com.dalbit.event.vo.request.GganbuMarbleExchangeInputVo;
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
import org.springframework.security.core.parameters.P;
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

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;
    @Value("${item.direct.min}")
    private int[] ITEM_DIRECT_MIN;
    @Value("${item.direct.max}")
    private int[] ITEM_DIRECT_MAX;
    @Value("${item.direct.code.main}")
    private String ITEM_DIRECT_CODE_MAIN;

    /**
     * 방송방 좋아요 추가
     */
    public String callBroadCastRoomGood(P_RoomGoodVo pRoomGoodVo, GoodVo goodVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGoodVo);
        actionDao.callBroadCastRoomGood(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

        log.error("callBroadCastRoomGood - resultMap => goodRank {}", resultMap);
        HashMap returnMap = new HashMap();
        returnMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
        returnMap.put("rank", DalbitUtil.getIntMap(resultMap, "rank"));
        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
        returnMap.put("goodRank", DalbitUtil.getIntMap(resultMap, "goodRank"));
        procedureVo.setData(returnMap);

        String result;
        if(Status.좋아요.getMessageCode().equals(procedureVo.getRet())) {
            SocketVo vo = socketService.getSocketObjectVo(pRoomGoodVo.getRoom_no(), pRoomGoodVo.getMem_no(), DalbitUtil.isLogin(request));
            try{ //좋아요 발송
                boolean isFirst = true;
                if(DalbitUtil.profileCheck("real")){
                    isFirst = (DalbitUtil.getIntMap(resultMap, "firstGood") == 1);
                }
                boolean isLoveGood = DalbitUtil.getIntMap(resultMap, "goodRank") > 0;

                //달나라 좋아요
                MoonLandCoinDataVO coinDataVO = null;
                List<MoonLandInfoVO> moonLandRound= moonLandService.getMoonLandInfoData();

                if(moonLandRound != null && moonLandRound.size() > 0 && returnMap.get("goodRank") != null) {
                    //보너스 코인 점수 ins
                    Random random = new Random();
                    int m_goodRank = (int) returnMap.get("goodRank");
                    int eventScoreValue = 0;

                    log.error("좋아요 goodRank chk => {}", m_goodRank);
                    switch(m_goodRank){
                        case 1: //지사
                            coinDataVO = new MoonLandCoinDataVO();
                            eventScoreValue = random.nextInt(31) + 20; // 20 ~ 50
                            //좋아요 누른사람, 일반 시청자 : 점수
                            coinDataVO.setEventScore(eventScoreValue);
                            break;
                        case 2:
                            coinDataVO = new MoonLandCoinDataVO();
                            eventScoreValue = random.nextInt(31) + 10; // 10 ~ 40
                            //좋아요 누른사람, 일반 시청자 점수
                            coinDataVO.setEventScore(eventScoreValue);
                            break;
                        case 3:
                            coinDataVO = new MoonLandCoinDataVO();
                            eventScoreValue = random.nextInt(11) + 10;  // 10~ 20
                            //좋아요 누른사람, 일반 시청자 점수
                            coinDataVO.setEventScore(eventScoreValue);
                            break;
                        default:
                            break;
                            
                    }

                    Long djMemNo = Long.parseLong(goodVo.getMemNo());
                    Long roomNo = Long.parseLong(pRoomGoodVo.getRoom_no());
                    // ~사랑꾼 조건에 들어야 됨
                    if (eventScoreValue > 0 && djMemNo > 0) {
                        /** DJ는 자동 획득 */
                        int insResult = moonLandService.setMoonLandScoreIns(djMemNo, 3, eventScoreValue, roomNo);

                        if (insResult != 1) {
                            log.error("사랑꾼 좋아요 점수 등록 실패 !! => moonLandService.setMoonLandScoreIns() djMemNo: {}, eventScoreValue: {}, roomNo: {}", djMemNo, eventScoreValue, roomNo);
                        } else {
                            log.warn("사랑꾼 좋아요 점수 등록 성공 !! => moonLandService.setMoonLandScoreIns() djMemNo: {}, eventScoreValue: {}, roomNo: {}", djMemNo, eventScoreValue, roomNo);
                        }
                    } else if (djMemNo == 0) {
                        log.error("사랑꾼 좋아요 => djMemNo를 안준경우! => goodVo.getGiftedMemNo() : {}", goodVo.getMemNo());
                    } else {
                        log.error("사랑꾼이 아닌데 좋아요 누름! => m_goodRank : {}", m_goodRank);
                    }
                }

                socketService.sendLike(pRoomGoodVo.getRoom_no(), pRoomGoodVo.getMem_no(), isFirst, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, isLoveGood, DalbitUtil.getIntMap(resultMap, "goodRank"),
                        new DeviceVo(request), coinDataVO);

                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendLike Exception {}", e);
            }

            try{ //좋아요수, 랭킹, 팬랭킹 발송
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

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//레벨업 일때 소켓 발송
                try{
                    socketService.sendLevelUp(pRoomGoodVo.getMem_no(), pRoomGoodVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){}
            }

            String resultCode = moonCheckSocket(pRoomGoodVo.getRoom_no(), request, "good");
            if("error".equals(resultCode)){
                log.error("보름달 체크 오류");
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
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);
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
        // 금지어 체크(시스템 + 방송)
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

    // TTS 치환 가능한 문자를 포함했는지
    private boolean hasTTSPermutationWord(String word) {
        return word.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]+.*");
    }

    /**
     * 방송방 선물하기
     */
    public String callBroadCastRoomGift(P_RoomGiftVo pRoomGiftVo, HttpServletRequest request) {
        String result;
        String ttsText = pRoomGiftVo.getTtsText();
        String actorId = pRoomGiftVo.getActorId();
        String sendItemSuccessMsg = "님에게 선물을 보냈습니다."; // 일반 || tts

        if(ttsText != null) ttsText = ttsText.trim();

        // tts 아이템
        if(!StringUtils.isEmpty(ttsText)) {
            // actor 체크
            if(!actorCheck(actorId)) {
                return gsonUtil.toJson(new JsonOutputVo(Status.선물하기_TTS_성우없음));
            }

            ttsText = banWordMasking(pRoomGiftVo.getRoom_no(), ttsText); // 금지어 *로 마스킹 처리

            if(!hasTTSPermutationWord(ttsText)) {
                return gsonUtil.toJson(new JsonOutputVo(Status.선물하기_TTS_변환가능문자없음));
            }

            sendItemSuccessMsg += "\n잠시 후 목소리가 재생됩니다.";
        }

        String item_code = pRoomGiftVo.getItem_code();
        boolean isDirect = false;
        int directItemCnt = 1;
        for(String direct : ITEM_DIRECT_CODE){
            if(direct.equals(pRoomGiftVo.getItem_code())){
                isDirect = true;
                break;
            }
        }
        if(ITEM_DIRECT_CODE_MAIN.equals(pRoomGiftVo.getItem_code()) || isDirect){ //직접선물하기 일경우 체크
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

        if(Status.선물하기성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
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
                itemMap.put("itemType", isDirect ? "direct" : "items");
                itemMap.put("authName", vo1.getAuthName());
                itemMap.put("auth", vo1.getAuth());
                itemMap.put("nickNm", vo1.getMemNk());
                itemMap.put("memNo", vo1.getMemNo());
                itemMap.put("dalCnt", item.getByeol() * pRoomGiftVo.getItem_cnt());
                // tts 정보
                itemMap.put("ttsText", ttsText);
                itemMap.put("actorId", actorId);
                returnMap.put("message", itemMap.get("nickNm") + sendItemSuccessMsg);

                // 깐부 이벤트
                GganbuMarbleExchangeInputVo gganbuInputVo = new GganbuMarbleExchangeInputVo(
                    pRoomGiftVo.getMem_no(), pRoomGiftVo.getGifted_mem_no(),
                    pRoomGiftVo.getRoom_no(), item.getByeol() * pRoomGiftVo.getItem_cnt()
                );
                GganbuPocketStatInsVo statIns = eventService.gganbuMarblePocketStatIns(gganbuInputVo);
                returnMap.put("gganbuPocketCnt", statIns);

                //달나라 조건 체크 - 선물 갯수 10개 이상
                itemMap.put("coinData", null); //달나라 관련 데이터
                int dalCnt = (int) itemMap.get("dalCnt");
                log.error("dal cnt chk => {}", dalCnt);
                if (dalCnt > 9) {
                    MoonLandCoinDataVO coinDataVO = null;
                    try {
                        List<MoonLandInfoVO> moonLandRound= moonLandService.getMoonLandInfoData();
                        //이벤트 진행중 여부 체크 (null 이면 이벤트 기간 아님)
                        if(moonLandRound != null && moonLandRound.size() > 0) {
                            //pRoomGiftVo.getMem_no(); // sendUser
                            //pRoomGiftVo.getGifted_mem_no(); // dj
                            //pRoomGiftVo.getRoom_no();
                            long sendUser = Long.parseLong(pRoomGiftVo.getMem_no());
                            long rcvDj = Long.parseLong(pRoomGiftVo.getMem_no());
                            long roomNo = Long.parseLong(pRoomGiftVo.getRoom_no());
                            // s_Return         -3: 이미선물한 아이템, -2:방송방 미션 완료, -1:이벤트 기간아님, 0:에러, 1:정상
                            MoonLandMissionInsVO resultVO = moonLandService.setMoonLandMissionDataIns(sendUser, roomNo, item_code);
                            if (resultVO.getS_Return() != null) {
                                int s_Return = resultVO.getS_Return();
                                log.error("1 moonLandService.setMoonLandMissionDataIns() Call => s_Return: {}", s_Return);

                                //일반 코인 (예외 조건 외에는 무조건 일반코인은 세팅)
                                coinDataVO = new MoonLandCoinDataVO();
                                int score = dalCnt;
                                coinDataVO.setScore(score);

                                //일반 코인 점수 ins
                                /** 선물한 시청자 자동획득 */
                                moonLandService.setMoonLandScoreIns(sendUser, 1, score, roomNo); //todo 0, 1 예외 처리 필요
                                /** DJ는 자동 획득 */
                                moonLandService.setMoonLandScoreIns(rcvDj, 1, score, roomNo);

                                log.error("2 일반 코인 점수 moonLandService.setMoonLandScoreIns() Call => sendUser: {}, rcvDj: {}, score: {}, resultVO{}", sendUser, rcvDj, score, gsonUtil.toJson(resultVO));

                                switch (s_Return) {
                                    case 1: // 정상 -> 달나라 소켓 보내기
                                        //   4종 아이템 미션 완료
                                        if (StringUtils.equals("y", resultVO.getS_rBalloonItemYn())
                                                && StringUtils.equals("y", resultVO.getS_rLanternsItemYn())
                                                && StringUtils.equals("y", resultVO.getS_rStarItemYn())
                                                && StringUtils.equals("y", resultVO.getS_rRocketItemYn()) ) {

                                            // 3% 확률 => 황금코인 or 캐릭터코인 결정
                                            Random random = new Random();
                                            int randNumber = random.nextInt(100) + 1; //1 ~ 100
                                            int eventScoreValue = 0;  //1000 ~ 3000 점 랜덤

                                            if (randNumber == 1 || randNumber == 50 || randNumber == 100) { //3% Success
                                                coinDataVO.setJackpotYn("y");
                                                eventScoreValue = random.nextInt(2001) + 1000;
                                                /** DJ는 자동 획득, 캐릭터 코인 점수 세팅 */
                                                moonLandService.setMoonLandScoreIns(rcvDj, 7, eventScoreValue, roomNo);
                                            } else { //3% Fail
                                                eventScoreValue = random.nextInt(51) + 250;
                                                /** DJ는 자동 획득, 황금코인 점수 세팅 */
                                                moonLandService.setMoonLandScoreIns(rcvDj, 5, eventScoreValue, roomNo);
                                            }
                                            coinDataVO.setEventScore(eventScoreValue);

                                            //소켓에 데이터 세팅!

                                            log.error("3 - 1) 아이템 미션 완료 moonLandService.setMoonLandScoreIns() Call => sendUser: {}, rcvDj: {}", sendUser, rcvDj);
                                        }
                                        log.error("3 - 2) 아이템 미션 미완! moonLandService.setMoonLandScoreIns() Call => sendUser: {}, rcvDj: {}", sendUser, rcvDj);
                                        break;
                                    default:
                                        log.error("3 - 3) 아이템 미션 실패! moonLandService.setMoonLandScoreIns() Call => sendUser: {}, rcvDj: {}, s_Return {}", sendUser, rcvDj, s_Return);
                                        break;
                                }

                                itemMap.put("coinData", coinDataVO);
                                log.error("4 최종 coinData, itemMap chk => coinData : {}, \n itemMap : {}",  gsonUtil.toJson(coinDataVO), gsonUtil.toJson(itemMap));
                            }
                        }
                    }catch(Exception e){
                        log.error("ActionService.java - moonLandService.setMoonLandMissionDataIns() Exception => sendMemNo: {} / djMemNo: {} / roomNo : {}",
                                pRoomGiftVo.getMem_no(), pRoomGiftVo.getGifted_mem_no(), pRoomGiftVo.getRoom_no() ,e);
                    }
                }

                socketService.giftItem(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no(), "1".equals(pRoomGiftVo.getSecret()) ? pRoomGiftVo.getGifted_mem_no() : "", itemMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service giftItem Exception {}", e);
            }
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", DalbitUtil.getIntMap(resultMap, "good"));
                socketMap.put("rank", DalbitUtil.getIntMap(returnMap, "rank"));
                //TODO - 레벨업 유무 소켓추가 추후 확인
                //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
                socketMap.put("fanRank", returnMap.get("fanRank"));
                socketMap.put("newFanCnt", DalbitUtil.getIntMap(resultMap, "isNewFan") > 0 ? DalbitUtil.getIntMap(resultMap, "isNewFan") :  DalbitUtil.getIntMap(resultMap, "newFanCnt"));
                socketService.changeCount(pRoomGiftVo.getRoom_no(), pRoomGiftVo.getMem_no(), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//레벨업 일때 소켓 발송
                try{
                    socketService.sendLevelUp(pRoomGiftVo.getMem_no(), pRoomGiftVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            String djMemNo = DalbitUtil.getStringMap(resultMap, "dj_mem_no");
            if(DalbitUtil.getIntMap(resultMap, "dj_levelUp") == 1){//DJ 레벨업 일때 소켓 발송
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

            //보름달 체크
            String resultCode = moonCheckSocket(pRoomGiftVo.getRoom_no(), request, "gift");
            if("error".equals(resultCode)){
                log.error("보름달 체크 오류");
            }

            Status status;
            if("1".equals(pRoomGiftVo.getSecret())){
                status = pRoomGiftVo.getGifted_mem_no().equals(djMemNo) ? Status.DJ_몰래_선물하기성공 : Status.게스트_몰래_선물하기성공;
            }else {
                status = pRoomGiftVo.getGifted_mem_no().equals(djMemNo) ? Status.DJ_선물하기성공 : Status.게스트_선물하기성공;
            }
            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
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
        }else if(Status.이전작업대기중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이전작업대기중));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.선물하기_실패));
        }

        return result;
    }


    /**
     * 부스터 사용하기
     */
    public String callBroadCastRoomBooster(P_RoomBoosterVo pRoomBoosterVo, BoosterVo boosterVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomBoosterVo);
        actionDao.callBroadCastRoomBooster(procedureVo);

        String result;
        if(Status.부스터성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();

            log.error("callBroadCastRoomBooster => {} roomCnt", resultMap);

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

            log.info("프로시저 응답 코드: {}", procedureVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");

            //달나라 갈거야 시작
            MoonLandCoinDataVO coinDataVO = null;
            try {
                List<MoonLandInfoVO> moonLandRound= moonLandService.getMoonLandInfoData();

                //이벤트 진행중 여부 체크 (null 이면 이벤트 기간 아님)
                if(moonLandRound != null && moonLandRound.size() > 0) {
                    if( !StringUtils.equals(null, boosterVo.getMemNo()) ){
                        int boostCnt = DalbitUtil.getIntMap(resultMap, "usedItemCnt");
                        int listenerCnt = -1;
                        Long rcvDjMemNo = Long.parseLong(boosterVo.getMemNo());
                        Long sendUserMemNo = moonLandService.getStringMemNoToLongMemNo(request);
                        Long roomNo = Long.parseLong(pRoomBoosterVo.getRoom_no());

                        //청취자 수 계산
                        List<P_RoomMemberListVo> listenerList= userService.getListenerList(pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(request));
                        if(listenerList != null){
                            listenerCnt = listenerList.size();
                            log.warn("ActionService sendBooster => listenerCnt : {}",listenerCnt);
                        } else {
                            log.error("ActionService sendBooster => listenerList is null listenerList: {}, roomNo: {}, memNo: {}", listenerList, pRoomBoosterVo.getRoom_no(), MemberVo.getMyMemNo(request));
                        }

                        Random random = new Random();
                        int eventScoreValue = 0;

                        if(listenerCnt != -1) {
                            coinDataVO = new MoonLandCoinDataVO();
                            if (listenerCnt < 10) { //청취자 수 10명 미만
                                // 3% 확률
                                int randNumber = random.nextInt(100) + 1; // 1 ~ 100
                                if (randNumber == 1 || randNumber == 50 || randNumber == 100) { //3% Success
                                    coinDataVO.setJackpotYn("y");
                                    eventScoreValue = boostCnt * (random.nextInt(701) + 300); //300 ~ 1000
                                    /** DJ는 자동 획득, 캐릭터 코인 점수 세팅 */
                                    moonLandService.setMoonLandScoreIns(rcvDjMemNo, 6, eventScoreValue, roomNo);
                                    log.warn("1 10명 미만 sendBooster 3% 확률에 당첨!! rcvDjMemNo: {}, roomNo: {}, eventScoreValue: {}",rcvDjMemNo, roomNo, eventScoreValue);
                                } else { //3% Fail
                                    eventScoreValue = boostCnt * (random.nextInt(21) + 10); //(10 ~ 30)
                                    /** DJ는 자동 획득, 황금코인 점수 세팅 */
                                    moonLandService.setMoonLandScoreIns(rcvDjMemNo, 4, eventScoreValue, roomNo);
                                    log.warn("2 10명 미만 sendBooster 3% 확률에 실패!! rcvDjMemNo: {}, roomNo: {}, eventScoreValue: {}",rcvDjMemNo, roomNo, eventScoreValue);
                                }

                            } else {
                                eventScoreValue = boostCnt * (random.nextInt(21) + 10); // boostCnt & (10 ~ 30)
                                /** DJ는 자동 획득, 황금코인 점수 세팅 */
                                moonLandService.setMoonLandScoreIns(rcvDjMemNo, 4, eventScoreValue, roomNo);
                                log.warn("3 10명 이상 sendBooster 3% 확률에 실패!! rcvDjMemNo: {}, roomNo: {}, eventScoreValue: {}",rcvDjMemNo, roomNo, eventScoreValue);
                            }
                            coinDataVO.setEventScore(eventScoreValue);
                        } else {
                            log.error("ActionService sendBooster => listenerCnt : {}", listenerCnt);
                        }
                    }
                }

            } catch (Exception e) {
                log.error("Socket Service sendBooster MoonLand {}", e);
            }
            //달나라 갈거야 끝

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
                itemMap.put("itemNm", "부스터");   //로켓부스터, 일반부스터 구분없이 화면에서 사용하기위해 하드코딩...
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
                itemMap.put("coinData", coinDataVO);    //달나라 코인 점수 (부스터)

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

            if(DalbitUtil.getIntMap(resultMap, "levelUp") == 1){//레벨업 일때 소켓 발송
                try{
                    socketService.sendLevelUp(pRoomBoosterVo.getMem_no(), pRoomBoosterVo.getRoom_no(), request, DalbitUtil.getAuthToken(request));
                    vo.resetData();
                }catch(Exception e){
                    log.error("sendDjLevelUp error : {}", e);
                }
            }

            if(DalbitUtil.getIntMap(resultMap, "dj_levelUp") == 1){//DJ 레벨업 일때 소켓 발송
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

            //보름달 체크
            String resultCode = moonCheckSocket(pRoomBoosterVo.getRoom_no(), request, "booster");
            if("error".equals(resultCode)){
                log.error("보름달 체크 오류");
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
        }else if(Status.이전작업대기중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이전작업대기중));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.부스터_실패));
        }

        return result;
    }


    /**
     * 방송 시간 연장
     */
    public String callroomExtendTime(P_ExtendTimeVo pExtendTimeVo, HttpServletRequest request) {

        log.error("방송 시간 연장 start - pExtendTimeVo: {}, {}", pExtendTimeVo.getRoom_no(), pExtendTimeVo.getMem_no());
        ProcedureVo procedureVo = new ProcedureVo(pExtendTimeVo);
        actionDao.callroomExtendTime(procedureVo);

        log.error("ret: {}, {}", procedureVo.getRet(), pExtendTimeVo.getMem_no());
        String result;
        if(Status.시간연장성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            SocketVo vo = socketService.getSocketVo(pExtendTimeVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            try{
                log.error("reqRoomChangeTime start - extendEndDate: {}", DalbitUtil.getStringMap(resultMap, "extendEndDate"));
                socketService.roomChangeTime(pExtendTimeVo.getRoom_no(), pExtendTimeVo.getMem_no(), DalbitUtil.getStringMap(resultMap, "extendEndDate"), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                log.error("reqRoomChangeTime end");
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


    /**
     * 방송방 채팅 얼리기
     */
    public String callRoomFreeze(P_FreezeVo pFreezeVo, HttpServletRequest request) {
        HashMap returnMap = new HashMap();
        returnMap.put("isFreeze", false);

        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        pRoomInfoViewVo.setMem_no(pFreezeVo.getMem_no());
        pRoomInfoViewVo.setRoom_no(pFreezeVo.getRoom_no());
        //관리자 얼리기 상태 체크 (방송방 정보 조회)
        ProcedureOutputVo roomInfoVo =  roomService.callBroadCastRoomInfoViewReturnVo(pRoomInfoViewVo, request);
        if(((RoomOutVo) roomInfoVo.getOutputBox()).getFreezeMsg() == 2){
            if(pFreezeVo.getFreezeMsg() == 0){
                returnMap.put("msg", messageUtil.get(Status.관리자_얼리기중_DJ해제실패.getMessageKey()));
                return gsonUtil.toJson(new JsonOutputVo(Status.관리자_얼리기중_DJ해제실패, returnMap));
            }else if(pFreezeVo.getFreezeMsg() == 1){
                returnMap.put("msg", messageUtil.get(Status.관리자_얼리기중_얼리기시도.getMessageKey()));
                return gsonUtil.toJson(new JsonOutputVo(Status.관리자_얼리기중_얼리기시도, returnMap));
            }
        };

        ProcedureVo procedureVo = new ProcedureVo(pFreezeVo);
        actionDao.callRoomFreeze(procedureVo);

        String result;
        if(Status.얼리기_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            boolean isFreeze = DalbitUtil.getIntMap(resultMap, "freezeMsg") == 1;
            returnMap.put("isFreeze", isFreeze);
            try{
                returnMap.put("msg", isFreeze ? messageUtil.get(Status.얼리기_성공.getMessageKey()) : messageUtil.get(Status.얼리기_해제.getMessageKey()));
                socketService.changeRoomFreeze(pFreezeVo.getMem_no(), pFreezeVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){}

            if(isFreeze){
                result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_성공, returnMap));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_해제, returnMap));
            }
        }else if(Status.얼리기_회원아님.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(Status.얼리기_회원아님.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_회원아님, returnMap));
        }else if(Status.얼리기_방번호없음.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(Status.얼리기_방번호없음.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_방번호없음, returnMap));
        }else if(Status.얼리기_종료된방.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(Status.얼리기_종료된방.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_종료된방, returnMap));
        }else if(Status.얼리기_요청회원_방에없음.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(Status.얼리기_요청회원_방에없음.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_요청회원_방에없음, returnMap));
        }else if(Status.얼리기_불가상태.getMessageCode().equals(procedureVo.getRet())){
            returnMap.put("msg", messageUtil.get(Status.얼리기_불가상태.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_불가상태, returnMap));
        }else{
            returnMap.put("msg", messageUtil.get(Status.얼리기_실패.getMessageKey()));
            result = gsonUtil.toJson(new JsonOutputVo(Status.얼리기_실패, returnMap));
        }

        return result;
    }


    /**
     * 보름달 띄우기 조회
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
        returnMap.put("textTime", DalbitUtil.getIntMap(resultMap, "broadcastTime") >= DalbitUtil.getIntMap(resultMap, "targetTime") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "broadcastTime") + "/" + DalbitUtil.getIntMap(resultMap, "targetTime")+"분");
        if(DalbitUtil.getIntMap(resultMap, "targetTime") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "방송시간");
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "broadcastTime"));
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetTime"));
            data.put("countStr", DalbitUtil.getIntMap(resultMap, "broadcastTime") >= DalbitUtil.getIntMap(resultMap, "targetTime") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "broadcastTime") + "/" + DalbitUtil.getIntMap(resultMap, "targetTime")+"분");
            resultList.add(data);
        }

        returnMap.put("totalCount", DalbitUtil.getIntMap(resultMap, "totalCount"));
        returnMap.put("targetCount", DalbitUtil.getIntMap(resultMap, "targetCount"));
        returnMap.put("textCount", DalbitUtil.getIntMap(resultMap, "totalCount") >= DalbitUtil.getIntMap(resultMap, "targetCount") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "totalCount") + "/" + DalbitUtil.getIntMap(resultMap, "targetCount")+"명");
        if(DalbitUtil.getIntMap(resultMap, "targetCount") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "누적 청취자");
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "totalCount"));
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetCount"));
            data.put("countStr", DalbitUtil.getIntMap(resultMap, "totalCount") >= DalbitUtil.getIntMap(resultMap, "targetCount") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "totalCount") + "/" + DalbitUtil.getIntMap(resultMap, "targetCount")+"명");
            resultList.add(data);
        }

        returnMap.put("giftedByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
        returnMap.put("targetByeol", DalbitUtil.getIntMap(resultMap, "targetByeol"));
        returnMap.put("textByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "giftedByeol") + "/" + DalbitUtil.getIntMap(resultMap, "targetByeol")+"별");
        if(DalbitUtil.getIntMap(resultMap, "targetByeol") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "선물");
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetByeol"));
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
            if(DalbitUtil.getIntMap(resultMap, "auth") == 3) {
                data.put("countStr", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "giftedByeol") + "/" + DalbitUtil.getIntMap(resultMap, "targetByeol")+"별");
            }else{
                String textByeol = "보름달 대박 가자!";
                int ingPercent = (int)((DalbitUtil.getDoubleMap(resultMap, "giftedByeol") / DalbitUtil.getDoubleMap(resultMap, "targetByeol")) * 100);
                if(ingPercent >= 100){
                    textByeol = "CLEAR!";
                    data.put("currentCount", 100);
                    data.put("maxCount", 100);
                }else if(ingPercent >= 70){
                    textByeol = "거의 다 왔습니다!";
                    data.put("currentCount", ingPercent);
                    data.put("maxCount", 100);
                }else if(ingPercent >= 35){
                    textByeol = "조금만 힘을 내요!";
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
        returnMap.put("textGood", DalbitUtil.getIntMap(resultMap, "goodPoint") >= DalbitUtil.getIntMap(resultMap, "targetGood") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "goodPoint") + "/" + DalbitUtil.getIntMap(resultMap, "targetGood")+"개");
        if(DalbitUtil.getIntMap(resultMap, "targetGood") > 0){
            HashMap data = new HashMap();
            data.put("labelStr", "좋아요");
            data.put("currentCount", DalbitUtil.getIntMap(resultMap, "goodPoint"));
            data.put("maxCount", DalbitUtil.getIntMap(resultMap, "targetGood"));
            data.put("countStr", DalbitUtil.getIntMap(resultMap, "goodPoint") >= DalbitUtil.getIntMap(resultMap, "targetGood") ? "CLEAR!" : DalbitUtil.getIntMap(resultMap, "goodPoint") + "/" + DalbitUtil.getIntMap(resultMap, "targetGood")+"개");
            resultList.add(data);
        }

        returnMap.put("dlgText", DalbitUtil.getStringMap(resultMap, "dlgText"));
        returnMap.put("helpText1", DalbitUtil.getStringMap(resultMap, "helpText1"));
        returnMap.put("helpText2", DalbitUtil.getStringMap(resultMap, "helpText2"));
        returnMap.put("helpText3", DalbitUtil.getStringMap(resultMap, "helpText3"));

        //청취자일 경우
        if(DalbitUtil.getIntMap(resultMap, "auth") != 3){
            returnMap.put("giftedByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? DalbitUtil.getIntMap(resultMap, "targetByeol") : DalbitUtil.getIntMap(resultMap, "giftedByeol"));
            returnMap.put("textByeol", DalbitUtil.getIntMap(resultMap, "giftedByeol") >= DalbitUtil.getIntMap(resultMap, "targetByeol") ? "CLEAR!" : "ING..");
        }
        returnMap.put("results", resultList);

        String result ="";
        if(procedureVo.getRet().equals(Status.보름달_완성.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_완성, returnMap));
        } else if(procedureVo.getRet().equals(Status.보름달_미완성.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_미완성, returnMap));
        } else if(procedureVo.getRet().equals(Status.보름달_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_회원아님));
        } else if(procedureVo.getRet().equals(Status.보름달_방번호_오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_방번호_오류));
        } else if(procedureVo.getRet().equals(Status.보름달_종료된방.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_종료된방));
        } else if(procedureVo.getRet().equals(Status.보름달_청취자아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_청취자아님));
        } else if(procedureVo.getRet().equals(Status.보름달_이미완성.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_이미완성));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_실패));
        }
        return result;
    }


    /**
     * 보름달 띄우기 체크 (서버용)
     */
    public String callMoonCheck(P_MoonCheckVo pMoonCheckVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMoonCheckVo);
        actionDao.callMoonCheck(procedureVo);

        String result ="";
        if(procedureVo.getRet().equals(Status.슈퍼문_완성.getMessageCode()) || procedureVo.getRet().equals(Status.보름달_완성.getMessageCode()) || procedureVo.getRet().equals(Status.보름달_미완성.getMessageCode())) {
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

            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.보름달_단계.getCode(), String.valueOf(returnMap.get("moonStep"))));
            if (!DalbitUtil.isEmpty(codeVo)) {
                returnMap.put("moonStepFileNm", codeVo.getValue());
                returnMap.put("aniDuration", codeVo.getSortNo());
                if (DalbitUtil.getIntMap(resultMap, "step") == 4) {
                    String code = Code.보름달_애니메이션.getCode();
                    String value = DalbitUtil.randomMoonAniValue();

                    if(procedureVo.getRet().equals(Status.슈퍼문_완성.getMessageCode())){
                        code = Code.슈퍼문_애니메이션.getCode();
                        value = "1";
                    }
                    var aniCodeVo = commonService.selectCodeDefine(new CodeVo(code, value));
                    if (!DalbitUtil.isEmpty(aniCodeVo)) {
                        returnMap.put("moonStepAniFileNm", aniCodeVo.getValue());
                        returnMap.put("aniDuration", aniCodeVo.getSortNo());
                    }
                }
            }

            //보름달 체크
            if (DalbitUtil.getIntMap(resultMap, "fullmoon_yn") == 1 && DalbitUtil.getIntMap(resultMap, "step") != DalbitUtil.getIntMap(resultMap, "oldStep")) {
                String resultCode = moonCheckSocket(pMoonCheckVo.getRoom_no(), request, "server", returnMap);
                if ("error".equals(resultCode)) {
                    log.error("보름달 체크 오류");
                }
            }
            Status status = null;
            if(procedureVo.getRet().equals(Status.보름달_완성.getMessageCode())){
                status = Status.슈퍼문_완성;
            }else if(procedureVo.getRet().equals(Status.보름달_완성.getMessageCode())){
                status = Status.보름달_완성;
            }else{
                status = Status.보름달_미완성;
            }
            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        } else if(procedureVo.getRet().equals(Status.보름달_방번호_오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_방번호_오류));
        } else if(procedureVo.getRet().equals(Status.보름달_종료된방.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_종료된방));
        } else if(procedureVo.getRet().equals(Status.보름달_청취자아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_청취자아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.보름달_실패));
        }
        return result;
    }

    /**
     * 보름달 띄우기 체크
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

            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.보름달_단계.getCode(), String.valueOf(returnMap.get("moonStep"))));
            if(!DalbitUtil.isEmpty(codeVo)){
                returnMap.put("moonStepFileNm", codeVo.getValue());
                returnMap.put("aniDuration", codeVo.getSortNo());
                if(DalbitUtil.getIntMap(resultMap, "step") == 4){
                    String code = Code.보름달_애니메이션.getCode();
                    String value = DalbitUtil.randomMoonAniValue();

                    if(procedureVo.getRet().equals(Status.슈퍼문_완성.getMessageCode())){
                        code = Code.슈퍼문_애니메이션.getCode();
                        value = "1";
                    }
                    var aniCodeVo = commonService.selectCodeDefine(new CodeVo(code, value));
                    if(!DalbitUtil.isEmpty(aniCodeVo)) {
                        returnMap.put("moonStepAniFileNm", aniCodeVo.getValue());
                        returnMap.put("aniDuration", aniCodeVo.getSortNo());
                    }

                    String resultCode = moonCheckSocket(pMoonCheckVo.getRoom_no(), request, "", returnMap);
                    if("error".equals(resultCode)){
                        log.error("보름달 체크 오류");
                    }
                }
            }
        }else{
            returnMap.put("fullmoon_yn", 0);
        }

        return returnMap;
    }


    /**
     *  보름달 체크 소켓발송(공통)
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
        try{ //보름달 체크
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
