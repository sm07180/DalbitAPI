package com.dalbit.common.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.AdminMenuVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.broadcast.vo.procedure.P_RoomJoinTokenVo;
import com.dalbit.common.annotation.NoLogging;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.*;
import com.dalbit.common.vo.request.SmsVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberFanVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenCheckVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.member.vo.procedure.P_LoginVo;
import com.dalbit.member.vo.procedure.P_MemberSessionUpdateVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.google.gson.Gson;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CommonService {

    @Autowired
    MemberService memberService;

    @Autowired
    CommonDao commonDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    AdminDao adminDao;

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;


    /**
     * 방송방 참가를 위해 스트림아이디 토큰아이디 받아오기
     */
    public HashMap callBroadCastRoomStreamIdRequest(String roomNo) throws GlobalException {
        P_RoomJoinTokenVo apiData = new P_RoomJoinTokenVo();
        apiData.setRoom_no(roomNo);
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        commonDao.callBroadCastRoomStreamIdRequest(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);

        String tokenFailData = "";

        if (procedureVo.getRet().equals(Status.방송참여토큰_해당방이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_해당방이없음, procedureVo.getData(), Thread.currentThread().getStackTrace()[1].getMethodName());
        } else if (procedureVo.getRet().equals(Status.방송참여토큰_방장이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_방장이없음, procedureVo.getData(), Thread.currentThread().getStackTrace()[1].getMethodName());
        } else if(procedureVo.getRet().equals(Status.방송참여토큰발급_실패.getMessageCode())){
            throw new GlobalException(Status.방송참여토큰발급_실패, procedureVo.getData(), Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        boolean isTokenSuccess = procedureVo.getRet().equals(Status.방송참여토큰발급.getMessageCode());
        resultMap.put("isTokenSuccess", isTokenSuccess);
        resultMap.put("tokenFailData", tokenFailData);

        log.debug("방송방 참여 토큰발급 결과 [{}]: {}", isTokenSuccess, tokenFailData);

        return resultMap;
    }

    @Cacheable(cacheNames = "codeCache", key = "#code")
    public HashMap getCodeCache(String code, HttpServletRequest request) {
        HashMap resultMap = callCodeDefineSelect();

        return resultMap;
    }

    public HashMap getItemVersion(HashMap resultMap, HttpServletRequest request){
        return getItemVersion(resultMap, request, null);
    }

    public HashMap getItemVersion(HashMap resultMap, HttpServletRequest request, String type){
        String platform = "";
        DeviceVo deviceVo = new DeviceVo(request);
        int osInt = deviceVo.getOs() + 1;
        if(osInt == 4){
            osInt = 1;
        }
        for(int i = 1; i < 4; i++){
            if(i == osInt){
                platform += "1";
            }else{
                platform += "_";
            }
        }

        P_ItemVo pItemVo = new P_ItemVo();
        pItemVo.setItem_slct(1);
        pItemVo.setPlatform(platform);
        pItemVo.setBooster(DalbitUtil.getProperty("item.code.boost"));
        pItemVo.setLevelUp(DalbitUtil.getProperty("item.code.levelUp"));
        pItemVo.setDirect(DalbitUtil.getProperty("item.code.direct"));
        String mainDirectCode = DalbitUtil.getProperty("item.direct.code.main");
        pItemVo.setDirects(ITEM_DIRECT_CODE);
        pItemVo.setVisibilityDirects((Arrays.stream(ITEM_DIRECT_CODE).filter(s->!s.equals(mainDirectCode))).toArray(String[]::new));

        List<ItemVo> items = commonDao.selectItemList(pItemVo);
        HashMap itemIsNew = new HashMap();
        itemIsNew.put("normal", false);
        itemIsNew.put("combo", false);
        itemIsNew.put("emotion", false);

        if(!DalbitUtil.isEmpty(items)){
            for(int i = 0; i < items.size(); i++){
                if(deviceVo.getOs() == 3){
                    items.get(i).setWebpUrl(StringUtils.replace(items.get(i).getWebpUrl(), "_1X", "_2X"));
                }
                if(items.get(i).isNew()){
                    itemIsNew.put(items.get(i).getCategory(), true);
                }
            }
        }
        resultMap.put("items", items);
        //pItemVo.setItem_slct(2);
        List particleList = new ArrayList();
        for (int i=0; i<DalbitUtil.getProperty("item.paticle.code").split(",").length; i++){
            particleList.add(commonDao.selectItem(DalbitUtil.getProperty("item.paticle.code").split(",")[i]));
        }
        resultMap.put("particles", particleList);

        List loveGoodList = new ArrayList();
        for (int i=0; i<DalbitUtil.getProperty("item.love.good.code").split(",").length; i++){
            loveGoodList.add(commonDao.selectItem(DalbitUtil.getProperty("item.love.good.code").split(",")[i]));
        }
        resultMap.put("loveGood", loveGoodList);
        List boostList = new ArrayList();
        if(!DalbitUtil.isEmpty(commonDao.selectBooster(DalbitUtil.getProperty("item.code.boost")))){
            boostList.add(commonDao.selectBooster(DalbitUtil.getProperty("item.code.boost")).get(0));
        }
        if(!DalbitUtil.isEmpty(commonDao.selectBooster(DalbitUtil.getProperty("item.code.rocket.boost")))){
            boostList.add(commonDao.selectBooster(DalbitUtil.getProperty("item.code.rocket.boost")).get(0));
        }
        resultMap.put("boost", boostList);
        resultMap.put("levelUp", commonDao.selectBooster(DalbitUtil.getProperty("item.code.levelUp")));

        List<HashMap> itemCategories = new ArrayList<>();
        HashMap itemCate1 = new HashMap();
        itemCate1.put("code", "normal");
        itemCate1.put("value", "일반");
        itemCate1.put("isNew", itemIsNew.get("normal"));
        HashMap itemCate2 = new HashMap();
        itemCate2.put("code", "combo");
        itemCate2.put("value", "콤보");
        itemCate2.put("isNew", itemIsNew.get("combo"));
        HashMap itemCate3 = new HashMap();
        itemCate3.put("code", "emotion");
        itemCate3.put("value", "감정");
        itemCate3.put("isNew", itemIsNew.get("emotion"));

        itemCategories.add(itemCate1);
        itemCategories.add(itemCate2);
        itemCategories.add(itemCate3);

        resultMap.put("itemCategories", itemCategories);

        if(!"items".equals(type)){
            if(deviceVo.getOs() == 1 || deviceVo.getOs() == 2){
                AppVersionVo versionVo = commonDao.selectAppVersion(deviceVo.getOs());
                resultMap.put("version", versionVo.getVersion());
                if(versionVo.getUpBuildNo() != null && !DalbitUtil.isEmpty(deviceVo.getAppBuild())){
                    try{
                        resultMap.put("isForce", (versionVo.getUpBuildNo() >= Long.parseLong(deviceVo.getAppBuild())));
                    }catch(Exception e){
                        resultMap.put("isForce", false);
                    }
                    try{
                        resultMap.put("isUpdate", (versionVo.getBuildNo() > Long.parseLong(deviceVo.getAppBuild())));
                    }catch(Exception e){
                        resultMap.put("isUpdate", false);
                    }
                }

                if(deviceVo.getOs() == 2){
                    resultMap.put("storeUrl", "itms-apps://itunes.apple.com/us/app/id1490208806?l=ko&ls=1");
                }
                resultMap.put("isPayment", true);
            }

            if(deviceVo.getOs() == 1){ // AOS 심사중 여부
                resultMap.put("isAosCheck", "real".equals(DalbitUtil.getActiveProfile()) ? true : false);
            }

            //TODO - 추후 삭제
            if(DalbitUtil.isEmpty(request.getHeader("custom-header"))){
                resultMap.put("isForce", true);
            }

            resultMap.put("isExtend", true);

            if("local".equals(DalbitUtil.getActiveProfile()) || "dev".equals(DalbitUtil.getActiveProfile())){
                int[] timeCombos = {1,2,3,4,5,10,20,30,40,50,60,70,80,90,100, 200, 300, 400, 500};
                resultMap.put("itemComboCount", timeCombos);
                resultMap.put("itemComboCout", timeCombos);
            }else{
                int[] timeCombos = {1,2,3,4,5,10,20,30,40,50,60,70,80,90,100};
                resultMap.put("itemComboCount", timeCombos);
                resultMap.put("itemComboCout", timeCombos);
            }
        }
        int[] giftComboCount = {1,2,3,4,5,6,7,8,9,10};
        resultMap.put("giftComboCount", giftComboCount);

        if(deviceVo.getOs() != 2) {
            int[] giftDal = {50, 100, 500, 1000, 2000, 3000, 5000, 10000};
            resultMap.put("giftDal", giftDal);
            resultMap.put("giftDalDirect", false);
            resultMap.put("giftDalMin", 50);
            int[] giftDalByRoom = {50, 100, 500, 1000, 2000, 3000, 5000, 10000};
            resultMap.put("giftDalByRoom", giftDalByRoom);
            resultMap.put("giftDalByRoomDirect", false);
        }
        resultMap.put("itemRepeat", true);
        List<String> downloadList = commonDao.getDownloadList();
        if(DalbitUtil.isEmpty(downloadList)){
            downloadList = new ArrayList<>();
        }
        List<String> preLoader = commonDao.getPreLoad();
        if(DalbitUtil.isEmpty(preLoader)){
            preLoader = new ArrayList<>();
        }
        resultMap.put("downloadList", downloadList);
        resultMap.put("preLoader", preLoader);
        resultMap.put("useMailBox", true);
        return resultMap;
    }

    public HashMap<String, Object> getJwtTokenInfo(HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        Status resultStatus;
        boolean isLogin = false;

        DeviceVo deviceVo = new DeviceVo(request);
        int os = deviceVo.getOs();
        String deviceId = deviceVo.getDeviceUuid();
        String deviceToken = deviceVo.getDeviceToken();
        String appVer = deviceVo.getAppVersion();
        String appAdId = deviceVo.getAdId();
        LocationVo locationVo = null;
        String ip = deviceVo.getIp();
        String browser = DalbitUtil.getUserAgent(request);
        String dbSelectMemNo = "";

        TokenVo tokenVo = null;
        String headerToken = request.getHeader(SSO_HEADER_COOKIE_NAME);
        try{
            isLogin = DalbitUtil.isLogin(request);
            if(!DalbitUtil.isEmpty(headerToken) && !request.getRequestURI().startsWith("/member/logout")){
                log.info("check HeaderToken : " + headerToken);
                tokenVo = jwtUtil.getTokenVoFromJwt(headerToken);
                log.info("tokenVo : " + tokenVo);
            }
        }catch(Exception e){
            isLogin = false;
            tokenVo = null;
        }

        //어드민 block 상태 체크
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(deviceVo, MemberVo.getMyMemNo(request)));

        if (isLogin && 0 == adminBlockCnt) {
            //토큰의 회원번호가 탈퇴 했거나 정상,경고가 아닐 경우 로그아웃처리
            TokenCheckVo tokenCheckVo = memberDao.selectMemState(MemberVo.getMyMemNo(request));

            //다른 서버의 memNo가 넘어왔을 시 null이다.
            if(DalbitUtil.isEmpty(tokenCheckVo)){
                dbSelectMemNo = "88888888888888";
                isLogin = false;
                tokenVo = null;
            }else if(tokenCheckVo.getMem_state() == 1 || tokenCheckVo.getMem_state() == 2){
                dbSelectMemNo = tokenCheckVo.getMem_no();
            }else{
                dbSelectMemNo = tokenCheckVo.getMem_no();
                isLogin = false;
                tokenVo = null;
            }
            /*if(mem_state == null || (mem_state < 1 && mem_state > 2)){
                isLogin = false;
                tokenVo = null;
            }*/
        }else{ //비회원토큰 실서버/개발서버와 충돌있는지 확인
            TokenCheckVo tokenCheckVo = memberDao.selectAnonymousMem(MemberVo.getMyMemNo(request));
            if(DalbitUtil.isEmpty(tokenCheckVo)){
                dbSelectMemNo = "88888888888888";
                isLogin = false;
                tokenVo = null;
            }
        }

        if(DalbitUtil.isEmpty(tokenVo)) {
            if(request.getRequestURI().startsWith("/member/logout")){
                isLogin = false;
            }
            if (isLogin) {
                tokenVo = new TokenVo(jwtUtil.generateToken(dbSelectMemNo, isLogin), dbSelectMemNo, isLogin);
                resultStatus = Status.로그인성공;

            } else {
                //locationVo = DalbitUtil.getLocation(request);
                P_LoginVo pLoginVo = new P_LoginVo("a", os, deviceId, deviceToken, appVer, appAdId, locationVo == null ? "" : locationVo.getRegionName(), ip, browser);
                //ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
                ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
                //ProcedureOutputVo LoginProcedureVo;
                /*if(DalbitUtil.isEmpty(loginList)){
                    throw new CustomUsernameNotFoundException(Status.로그인실패_회원가입필요);
                }else{
                    LoginProcedureVo = new ProcedureOutputVo(procedureVo);
                }*/
                log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));
                if (DalbitUtil.isEmpty(LoginProcedureVo)) {
                    throw new CustomUsernameNotFoundException(Status.로그인실패_회원가입필요);
                }
                if (LoginProcedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
                    resultStatus = Status.로그인실패_회원가입필요;
                } else if (LoginProcedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
                    resultStatus = Status.로그인실패_패스워드틀림;
                } else if (LoginProcedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
                    resultStatus = Status.로그인실패_파라메터이상;
                } else if (LoginProcedureVo.getRet().equals(Status.로그인성공.getMessageCode())) {
                    HashMap map = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
                    String memNo = DalbitUtil.getStringMap(map, "mem_no");
                    if (DalbitUtil.isEmpty(memNo)) {
                        resultStatus = Status.로그인실패_파라메터이상;
                    } else {
                        resultStatus = Status.로그인성공;
                        tokenVo = new TokenVo(jwtUtil.generateToken(memNo, isLogin), memNo, isLogin);
                        result.put("tokenVo", tokenVo);
                        memberService.refreshAnonymousSecuritySession(memNo);
                    }
                } else {
                    resultStatus = Status.로그인오류;
                }
            }
        }else{
            resultStatus = Status.로그인성공;
        }

        if ((!DalbitUtil.isEmpty(tokenVo) && !tokenVo.getAuthToken().equals(headerToken)) || "Y".equals(deviceVo.getIsFirst())) {
            /*if(locationVo == null){
                locationVo = DalbitUtil.getLocation(request);
            }*/
            P_MemberSessionUpdateVo pMemberSessionUpdateVo = new P_MemberSessionUpdateVo(
                    isLogin ? 1 : 0
                    , tokenVo.getMemNo()
                    , os
                    , appAdId
                    , deviceId
                    , deviceToken
                    , appVer
                    , locationVo == null ? "" : locationVo.getRegionName()
                    , deviceVo.getIp()
                    , browser
                    , deviceVo.getDeviceManufacturer()
                    , deviceVo.getDeviceModel()
                    , deviceVo.getSdkVersion()
                    , deviceVo.getAppBuild()
            );
            memberService.callMemberSessionUpdate(pMemberSessionUpdateVo);
        }

        result.put("tokenVo", tokenVo);
        result.put("Status", resultStatus);
        return result;
    }

    public ProcedureOutputVo anonymousLogin(P_LoginVo pLoginVo){
        return memberService.callMemberLogin(pLoginVo);
    }

    @CachePut(cacheNames = "codeCache", key = "#code")
    public HashMap updateCodeCache(String code) {
        return callCodeDefineSelect();
    }

    public HashMap callCodeDefineSelect(){
        List<Map> data = commonDao.callCodeDefineSelect();

        HashMap<String, Object> result = new HashMap<>();
        result.put("memGubun", setData(data, "mem_slct"));
        result.put("osGubun", setData(data, "os_type"));
        result.put("roomType", setData(data, "subject_type"));
        result.put("roomState", setData(data, "broadcast_state"));
        result.put("roomRight", setData(data, "broadcast_auth"));
        result.put("declarReason", setData(data, "report_reason"));
        result.put("clipType", setData(data, "clip_type"));
        result.put("exchangeBankCode", setData(data, "exchange_bank_code"));

        return result;
    }

    @Cacheable(cacheNames = "codeCache", key = "#code")
    public List<Map> callCodeDefineSelect(String code) {
        return commonDao.callCodeDefineSelect();
    }

    public List<CodeVo> setData(List<Map> list, String type) {
        return setData(list, type, 1);
    }


    public List<CodeVo> setData(List<Map> list, String type, int append) {
        List<CodeVo> data = new ArrayList<>();
        if(!DalbitUtil.isEmpty(list)){
            for(int i = 0; i < list.size(); i++){
                if(type.equals(list.get(i).get("type"))){
                    if((int) list.get(i).get("is_use") == 1){
                        if(append == 2){
                            data.add(new CodeVo((String)list.get(i).get("type"), (String)list.get(i).get("value"), (String)list.get(i).get("code"), DalbitUtil.isEmpty(list.get(i).get("order"))? i : (int) list.get(i).get("order"), (int) list.get(i).get("is_use")));
                        }else{
                            data.add(new CodeVo((String)list.get(i).get("value"), (String)list.get(i).get("code"), DalbitUtil.isEmpty(list.get(i).get("order"))? i : (int) list.get(i).get("order"), (int) list.get(i).get("is_use")));
                        }
                    }
                }
            }
        }
        return data;
    }

    public List<CodeVo> getCodeList(String code){
        return (List<CodeVo>)callCodeDefineSelect().get(code);
    }

    /**
     * 팬랭킹 1,2,3위 가져오기
     */
    public List getFanRankList(String rank1, String rank2, String rank3){

        List memberFanVoList = new ArrayList();

        if(!DalbitUtil.isEmpty(rank1)){
            setFanVo(memberFanVoList, rank1);
        }
        if(!DalbitUtil.isEmpty(rank2)){
            setFanVo(memberFanVoList, rank2);
        }
        if(!DalbitUtil.isEmpty(rank3)){
            setFanVo(memberFanVoList, rank3);
        }

        return memberFanVoList;
    }

    /**
     * 방송방 황제 & 팬랭킹 1,2,3위 가져오기
     */
    public HashMap getKingFanRankList(String roomNo) {

        P_KingFanRankListVo apiData = new P_KingFanRankListVo(roomNo);
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        List<P_KingFanRankListVo> kingFanRankListVo = commonDao.callBroadCastRoomRank3(procedureVo);

        List<P_KingFanRankListOutVo> outVoList = new ArrayList<>();
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);

        String result;
        HashMap fanRankList = new HashMap();
        fanRankList.put("list", new ArrayList<>());
        fanRankList.put("kingMemNo", "");
        fanRankList.put("kingNickNm", "");
        fanRankList.put("kingGender", "");
        fanRankList.put("kingAge", 0);
        fanRankList.put("kingProfImg", "");

        if (Integer.parseInt(procedureOutputVo.getRet()) == 0 || DalbitUtil.isEmpty(kingFanRankListVo)) {
            return fanRankList;
        }

        if (Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            for (int i = 0; i < kingFanRankListVo.size(); i++) {
                HashMap liveBadgeMap = new HashMap();
                liveBadgeMap.put("mem_no", kingFanRankListVo.get(i).getMem_no());
                liveBadgeMap.put("type", 0);
                liveBadgeMap.put("by", "api");
                BadgeFrameVo badgeFrameVo = commonDao.callMemberBadgeFrame(liveBadgeMap);
                if(DalbitUtil.isEmpty(badgeFrameVo)){
                    outVoList.add(new P_KingFanRankListOutVo(kingFanRankListVo.get(i), i+1));
                }else{
                    outVoList.add(new P_KingFanRankListOutVo(kingFanRankListVo.get(i), i+1, badgeFrameVo));
                }
            }

            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            fanRankList.put("list", outVoList);
            fanRankList.put("kingMemNo", DalbitUtil.getStringMap(resultMap, "king_mem_no"));
            fanRankList.put("kingNickNm", DalbitUtil.getStringMap(resultMap, "king_nickName"));
            fanRankList.put("kingGender", DalbitUtil.getStringMap(resultMap, "king_memSex"));
            fanRankList.put("kingAge", DalbitUtil.getIntMap(resultMap, "m_king_age"));
            fanRankList.put("kingProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "m_king_profileImage"), DalbitUtil.getStringMap(resultMap, "king_memSex"), DalbitUtil.getProperty("server.photo.url")));
            fanRankList.put("msgCode", Status.방송방_팬랭킹조회_성공.getMessageCode());

        } else if (Status.방송방_팬랭킹조회_팬없음.getMessageCode().equals(procedureOutputVo.getRet())) {
            fanRankList.put("msgCode", Status.방송방_팬랭킹조회_성공.getMessageCode());
        } else if (Status.방송방_팬랭킹조회_방번호없음.getMessageCode().equals(procedureOutputVo.getRet())) {
            fanRankList.put("msgCode", Status.방송방_팬랭킹조회_방번호없음.getMessageCode());
        } else if (Status.방송방_팬랭킹조회_방종료됨.getMessageCode().equals(procedureOutputVo.getRet())) {
            fanRankList.put("msgCode", Status.방송방_팬랭킹조회_방종료됨.getMessageCode());
        } else {
            fanRankList.put("msgCode", Status.방송방_팬랭킹조회_실패.getMessageCode());
        }

        return fanRankList;
    }

    private List setFanVo(List memberFanVoList, String fanRank){
        HashMap map = new Gson().fromJson(fanRank, HashMap.class);
        MemberFanVo fanVo = new MemberFanVo();
        fanVo.setRank(memberFanVoList.size()+1);
        //fanVo.setMemNo(BigDecimal.valueOf(DalbitUtil.getDoubleMap(map, "mem_no")));
        fanVo.setMemNo(DalbitUtil.getStringMap(map, "mem_no"));
        fanVo.setNickNm(DalbitUtil.getStringMap(map, "nickName"));
        fanVo.setGender(DalbitUtil.getStringMap(map, "memSex"));
        fanVo.setAge(DalbitUtil.getIntMap(map, "age"));
        fanVo.setProfImg(new ImageVo(DalbitUtil.isNullToString(map.get("profileImage")), DalbitUtil.getStringMap(map, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        memberFanVoList.add(fanVo);

        return memberFanVoList;
    }

    @Value("${server.healthy.check.dir}")
    private String HEALTHY_DIR;
    @Value("${server.healthy.check.postfix}")
    private String HEALTHY_POSTFIX;

    @NoLogging
    public String checkHealthy(HttpServletRequest request) {
        String rootDir = request.getSession().getServletContext().getRealPath("/");
        String instance;
        String result = "OK";
        if(rootDir.endsWith("/") || rootDir.endsWith("\\")){
            rootDir = rootDir.substring(0, rootDir.length() - 1);
        }
        instance = rootDir.substring(rootDir.length() - 1);

        if(!Pattern.matches("\\d", instance)){
            instance = "1";
        }

        FileReader fileReader = null;
        try{
            File healthyFile = new File(HEALTHY_DIR, "service" + instance + "_" + HEALTHY_POSTFIX + ".txt");
            if(healthyFile.exists()){
                fileReader = new FileReader(healthyFile);
                int cur = 0;
                result = "";
                while((cur = fileReader.read()) != -1){
                    result += (char)cur;
                }
            }
        }catch (FileNotFoundException e) {
            result = "OK";
        }catch (IOException e1) {
            result = "OK";
        }finally {
            if(fileReader != null){
                try {
                    fileReader.close();
                }catch(IOException e){}
            }
        }

        return result;
    }


    /**
     * 휴대폰 인증번호 요청
     */
    public void requestSms(SmsVo smsVo) {
        commonDao.requestSms(smsVo);
    }


    /**
     * 회원 본인 인증
     */
    public String callMemberCertification(P_SelfAuthVo pSelfAuthVo){
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.callMemberCertification(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        procedureVo.setData(pSelfAuthVo);

        String result ="";
        if(procedureVo.getRet().equals(Status.본인인증성공.getMessageCode())) {
            pSelfAuthVo.setComment("본인인증 완료");
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증성공, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.본인인증_회원아님.getMessageCode())) {
            pSelfAuthVo.setComment("본인인증 회원 오류");
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증_회원아님));
        } else if(procedureVo.getRet().equals(Status.본인인증_중복.getMessageCode())) {
            pSelfAuthVo.setComment("본인인증 중복");
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증_중복));
        } else {
            pSelfAuthVo.setComment("본인인증 실패");
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증저장실패, procedureVo.getData()));
        }
        //개인정보 변경내역 저장
        try {
            memberService.callProfileEditHistory(pSelfAuthVo);
        }catch(Exception e){
            log.info("본인인증 내역 callProfileEditHistory Exception {}, {}", pSelfAuthVo.getComment(), e);
        }
        return result;
    }

    /**
     * 회원 본인 인증 여부 체크
     */
    public String getCertificationChk(P_SelfAuthChkVo pSelfAuthVo){
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.getCertificationChk(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();

        String result;
        if(procedureVo.getRet().equals(Status.본인인증여부_확인.getMessageCode())) {
            //생년월일 조회 후 미성년자 여부 체크
            AdultCheckVo adultCheckVo = commonDao.getMembirth(pSelfAuthVo.getMem_no());
            String adultYn;
            if(DalbitUtil.getKorAge(Integer.parseInt(adultCheckVo.getMem_birth_year())) < 20){
                adultYn = "n";
            } else {
                adultYn = "y";
            }
            returnMap.put("adultYn", adultYn);       //성인여부
            returnMap.put("parentsAgreeYn", DalbitUtil.getStringMap(resultMap, "parents_agree_yn"));   //부모동의여부
            returnMap.put("phoneNo", DalbitUtil.getStringMap(resultMap, "mem_phone"));      //휴대폰번호
            returnMap.put("company", DalbitUtil.getStringMap(resultMap, "comm_company"));   //해외인증 판단 '기타'
            procedureVo.setData(returnMap);

            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증여부_확인, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.본인인증여부_안됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증여부_안됨));
        } else if(procedureVo.getRet().equals(Status.본인인증여부_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증여부_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증여부_실패));
        }
        return result;
    }


    /**
     * 에러 로그 저장
     */

    public String saveErrorLog(P_ErrorLogVo pErrorLogVo){
        return saveErrorLog(pErrorLogVo, null);
    }
    public String saveErrorLog(P_ErrorLogVo pErrorLogVo, HttpServletRequest request){
        if(request != null && request instanceof HttpServletRequest){
            DeviceVo deviceVo  = new DeviceVo(request);
            String desc = pErrorLogVo.getDesc();
            desc = "AuthToken : " + request.getHeader(SSO_HEADER_COOKIE_NAME) + "\n" +desc;
            desc = (new Gson().toJson(deviceVo)) + "\n" +desc;
            pErrorLogVo.setDesc(desc);
        }
        ProcedureVo procedureVo = new ProcedureVo(pErrorLogVo);
        commonDao.saveErrorLog(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.에러로그저장_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.에러로그저장_성공));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.에러로그저장_실패));
        }
        return result;
    }


    /**
     * PUSH 발송 추가
     */
    public String callPushAdd(P_PushVo pPushVo) {
        ProcedureVo procedureVo = new ProcedureVo(pPushVo);
        commonDao.callPushAdd(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

        String result;
        if(procedureVo.getRet().equals(Status.푸시성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시성공));
        } else if(procedureVo.getRet().equals(Status.푸시_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시_회원아님));
        } else if(procedureVo.getRet().equals(Status.푸시_디바이스토큰없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시_디바이스토큰없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시실패));
        }
        return result;

    }

    /**
     * PUSH Click
     */
    public String callPushClickUpdate(P_PushClickVo pPushClickVo) {
        ProcedureVo procedureVo = new ProcedureVo(pPushClickVo);
        commonDao.callPushClickUpdate(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

        String result;
        if(procedureVo.getRet().equals(Status.푸시클릭성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시클릭성공));
        } else if(procedureVo.getRet().equals(Status.푸시클릭_푸시번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시클릭_푸시번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.푸시클릭_에러));
        }
        return result;

    }

    /**
     * 사이트 금지어 조회
     */
    public String banWordSelect(){
        BanWordVo banWordVo = commonDao.banWordSelect(0);
        return banWordVo.getBanWord();
    }
    /**
     * 방송방제목 금지어 조회
     */
    public String titleBanWordSelect(){
        BanWordVo banWordVo = commonDao.banWordSelect(1);
        return banWordVo.getBanWord();
    }

    /**
     * 방송 금지어 조회(개인)
     */
    public String broadcastBanWordSelect(BanWordVo banWordVo){
        BanWordVo resultVo = commonDao.broadcastBanWordSelect(banWordVo);
        if(DalbitUtil.isEmpty(resultVo)){
            return null;
        }

        if(DalbitUtil.isEmpty(resultVo.getBanWord())){ // TODO - size 체크 추가
            return null;
        }
        return resultVo.getBanWord();
    }

    public String connectGoogleNative(HttpServletRequest request){
        String result = "error";
        HashMap resultMap = new HashMap();

        String id_token = request.getParameter("idToken");
        log.debug("Connect Google Login start : {}", id_token);
        if(DalbitUtil.isEmpty(id_token)){
            result = "blank token";
        }else{
            if(id_token.indexOf(".") > -1 && id_token.split("\\.").length == 3){
                try{
                    String[] tokens = id_token.split("\\.");
                    SignedJWT signedJWT = new SignedJWT(new Base64URL(tokens[0]), new Base64URL(tokens[1]), new Base64URL(tokens[2]));
                    if(signedJWT.getPayload() != null) {
                        log.debug("Connect Google Login parse result : {}", signedJWT.getPayload());
                        JSONObject googleMap = signedJWT.getPayload().toJSONObject();
                        resultMap.put("memType", "g");
                        if(googleMap.containsKey("sub") && !DalbitUtil.isEmpty(googleMap.getAsString("sub"))) {
                            resultMap.put("memId", googleMap.getAsString("sub"));
                        }else{
                            resultMap.put("memId", googleMap.getAsString("subject"));
                        }

                        resultMap.put("nickNm", "");
                        resultMap.put("name", "");
                        if(googleMap.containsKey("name") && !DalbitUtil.isEmpty(googleMap.getAsString("name"))) {
                            resultMap.put("nickNm", googleMap.getAsString("name"));
                            resultMap.put("name", googleMap.getAsString("name"));
                        }

                        resultMap.put("email", "");
                        if(googleMap.containsKey("email") && !DalbitUtil.isEmpty(googleMap.getAsString("email"))) {
                            resultMap.put("email", googleMap.getAsString("email"));
                        }

                        resultMap.put("profImgUrl", "");
                        if(googleMap.containsKey("picture") && !DalbitUtil.isEmpty(googleMap.getAsString("picture"))) {
                            resultMap.put("profImgUrl", googleMap.getAsString("picture"));
                        }
                        resultMap.put("gender", "n");
                        result = "success";
                    }else{
                        result = "invalid token";
                    }
                }catch(Exception e) {
                    result = "invalid token";
                }
            }else{
                result = "invalid token";
            }
        }

        log.debug("Connect Google Login end : {}", result);
        if("success".equals(result)) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.구글로그인_성공, resultMap));
        }else if("invalid token".equals(result)){
            result = gsonUtil.toJson(new JsonOutputVo(Status.구글로그인_토큰인증실패));
        }else if("blank token".equals(result)){
            result = gsonUtil.toJson(new JsonOutputVo(Status.구글로그인_토큰없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.구글로그인_오류));

        }
        return result;
    }

    /**
     * 공통코드 조회
     * @param codeVo
     * @return
     */
    public CodeVo selectCodeDefine(CodeVo codeVo){
        List<Map> list = callCodeDefineSelect("splash");
        if(!DalbitUtil.isEmpty(list)){
            for(int i = 0; i < list.size(); i++){
                if(codeVo.getCd().equals(list.get(i).get("type")) && codeVo.getCdNm().equals(list.get(i).get("code"))){
                    return new CodeVo((String)list.get(i).get("type"), (String)list.get(i).get("value"), (String)list.get(i).get("code"), DalbitUtil.isEmpty(list.get(i).get("order"))? i : (int) list.get(i).get("order"), (int) list.get(i).get("is_use"));
                }
            }
        }

        return null;
    }


    /**
     * 법정대리인(보호자) 인증 업데이트
     */
    public String updateMemberCertification(P_SelfAuthVo pSelfAuthVo) {

        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.updateMemberCertification(procedureVo);

        String result ="";
        if("0".equals(procedureVo.getRet())) {
            pSelfAuthVo.setComment("법정대리인 인증 완료");
            result = gsonUtil.toJson(new JsonOutputVo(Status.보호자인증성공, pSelfAuthVo));
        } else {
            pSelfAuthVo.setComment("법정대리인 인증 실패");
            result = gsonUtil.toJson(new JsonOutputVo(Status.보호자인증실패));
        }
        //개인정보 변경내역 저장
        try {
            memberService.callProfileEditHistory(pSelfAuthVo);
        }catch(Exception e){
            log.info("법정대리인 인증 내역 callProfileEditHistory Exception {}, {}",pSelfAuthVo.getComment(), e);
        }
        return result;


    }

    /**
     * 법정대리인(보호자) 인증 파일 업데이트
     */
    public int updateMemberCertificationFile(P_SelfAuthVo pSelfAuthVo) {
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.updateMemberCertification(procedureVo);
        return "0".equals(procedureVo.getRet()) ? 1 : 0;
    }

    public List<CodeVo> getCodeListCache(String code){
        return setData(callCodeDefineSelect("splash"), code, 2);
    }

    /**
     * 사용중인 방송주제 목록 가져오기
     */
    public List<CodeVo> selectRoomTypeCodeList(CodeVo codeVo){
        return getCodeListCache(codeVo.getCd());
    }

    /**
     * 사용중인 클립 목록 가져오기
     */
    public List<CodeVo> selectClipTypeCodeList(CodeVo codeVo){
        return getCodeListCache(codeVo.getCd());
    }

    /**
     * 환전은행 목록
     */
    public List<CodeVo> selectExchangeBankCodeList(CodeVo codeVo){
        return getCodeListCache(codeVo.getCd());
    }


    /**
     * 장기 미접속 & 탈퇴 예정 일자 조회
     */
    public String getLongTermDate(P_LongTermVo pLongTermVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLongTermVo);
        commonDao.getLongTermDate(procedureVo);

        HashMap returnMap  = new HashMap<>();
        String result;
        if(Status.클립등록_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            returnMap.put("memNo", DalbitUtil.getStringMap(resultMap, "mem_no"));
            returnMap.put("type", DalbitUtil.getIntMap(resultMap, "type"));
            DateFormat format = DateFormat.getDateInstance(DateFormat.LONG, Locale.KOREAN);
            returnMap.put("lastLoginDate", format.format(DalbitUtil.getDateMap(resultMap, "lastLoginDate")));
            returnMap.put("dueDate", format.format(DalbitUtil.getDateMap(resultMap, "dueDate")));
            var codeVo = selectCodeDefine(new CodeVo(Code.장기_미접속_시행일자.getCode(), Code.장기_미접속_시행일자.getDesc()));
            if (!DalbitUtil.isEmpty(codeVo)) {
                returnMap.put("longTermDate", codeVo.getValue());
            } else {
                returnMap.put("longTermDate", "");
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.휴면탈퇴_일자조회_성공, returnMap));
        }else if(Status.클립등록_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.휴면탈퇴_일자조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.휴면탈퇴_일자조회_실패));
        }

        return result;
    }
}
