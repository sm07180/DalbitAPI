package com.dalbit.common.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.service.AdminService;
import com.dalbit.broadcast.vo.procedure.P_RoomJoinTokenVo;
import com.dalbit.common.annotation.NoLogging;
import com.dalbit.common.code.*;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.proc.Common;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.*;
import com.dalbit.common.vo.request.*;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
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

    @Autowired EmailService emailService;
    @Autowired AdminService adminService;
    @Autowired Common common;

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;

    @Value("${social.facebook.app.ver}")
    private String FACEBOOK_APP_VER;


    /**
     * ????????? ????????? ?????? ?????????????????? ??????????????? ????????????
     */
    public HashMap callBroadCastRoomStreamIdRequest(String roomNo) throws GlobalException {
        P_RoomJoinTokenVo apiData = new P_RoomJoinTokenVo();
        apiData.setRoom_no(roomNo);
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        commonDao.callBroadCastRoomStreamIdRequest(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", resultMap);

        String tokenFailData = "";

        if (procedureVo.getRet().equals(BroadcastStatus.??????????????????_??????????????????.getMessageCode())) {
            throw new GlobalException(BroadcastStatus.??????????????????_??????????????????, procedureVo.getData(), Thread.currentThread().getStackTrace()[1].getMethodName());
        } else if (procedureVo.getRet().equals(BroadcastStatus.??????????????????_???????????????.getMessageCode())) {
            throw new GlobalException(BroadcastStatus.??????????????????_???????????????, procedureVo.getData(), Thread.currentThread().getStackTrace()[1].getMethodName());
        } else if(procedureVo.getRet().equals(BroadcastStatus.????????????????????????_??????.getMessageCode())){
            throw new GlobalException(BroadcastStatus.????????????????????????_??????, procedureVo.getData(), Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        boolean isTokenSuccess = procedureVo.getRet().equals(BroadcastStatus.????????????????????????.getMessageCode());
        resultMap.put("isTokenSuccess", isTokenSuccess);
        resultMap.put("tokenFailData", tokenFailData);

        log.debug("????????? ?????? ???????????? ?????? [{}]: {}", isTokenSuccess, tokenFailData);

        return resultMap;
    }

    @Cacheable(cacheNames = "codeCache", key = "#code")
    public HashMap getCodeCache(String code, HttpServletRequest request) {
        HashMap resultMap = callCodeDefineSelect();

        return resultMap;
    }

    @Cacheable(cacheNames = "codeCache2", key = "#code")
    public HashMap getCodeCache2(String code, HttpServletRequest request) {
        HashMap resultMap = callCodeDefineSelect2();

        return resultMap;
    }

    public HashMap getItemVersion(HashMap resultMap, HttpServletRequest request){
        return getItemVersion(resultMap, request, null);
    }

    public HashMap getItemVersion(HashMap resultMap, HttpServletRequest request, String type){
        String itemPaticleCode = DalbitUtil.getProperty("item.paticle.code");
        String itemLoveGoodCode = DalbitUtil.getProperty("item.love.good.code");
        String itemCodeBoost = DalbitUtil.getProperty("item.code.boost");
        String itemCodeLevelUp = DalbitUtil.getProperty("item.code.levelUp");
        String itemCodeDirect = DalbitUtil.getProperty("item.code.direct");
        String mainDirectCode = DalbitUtil.getProperty("item.direct.code.main");
        String itemCodeRocketBoost = DalbitUtil.getProperty("item.code.rocket.boost");
        String itemCodeStory = DalbitUtil.getProperty("item.code.story");

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

        pItemVo.setBooster(itemCodeBoost);
        pItemVo.setLevelUp(itemCodeLevelUp);
        pItemVo.setDirect(itemCodeDirect);
        pItemVo.setDirects(ITEM_DIRECT_CODE);
        pItemVo.setVisibilityDirects((Arrays.stream(ITEM_DIRECT_CODE).filter(s->!s.equals(mainDirectCode))).toArray(String[]::new));

        List<ItemVo> items = commonDao.selectItemList(pItemVo);
        List<ItemVo> newItems = new ArrayList<>();
        HashMap itemIsNew = new HashMap();
        itemIsNew.put("normal", false);
        itemIsNew.put("combo", false);
        itemIsNew.put("emotion", false);
        itemIsNew.put("text", false);

        if(!DalbitUtil.isEmpty(items)){
            for(int i = 0; i < items.size(); i++){
                // ???????????? ?????????, ?????? ????????? ??????
                if (StringUtils.equals(items.get(i).getCategory(), "signature") || items.get(i).getItemNo().equals(itemCodeStory)) {
                    continue;
                }
                if (deviceVo.getOs() == 3) {
                    items.get(i).setWebpUrl(StringUtils.replace(items.get(i).getWebpUrl(), "_1X", "_2X"));
                }
                if (items.get(i).isNew()) {
                    itemIsNew.put(items.get(i).getCategory(), true);
                }

                newItems.add(items.get(i));
            }
        }
        resultMap.put("items", newItems);
        //pItemVo.setItem_slct(2);

        StringBuffer sbf = new StringBuffer("'");
        sbf.append("G9997")
                .append("','")
                .append("G9998")
                .append("','")
                .append(itemCodeBoost)
                .append("','")
                .append(itemCodeRocketBoost)
                .append("','")
                .append(itemCodeLevelUp);
        List particleList = new ArrayList();
        String[] itemPaticleCodeList = itemPaticleCode.split(",");
        String[] itemLoveGoodCodeList = itemLoveGoodCode.split(",");
        for (int i = 0; i< itemPaticleCodeList.length; i++){
            sbf.append("','").append(itemPaticleCodeList[i]);
        }

        List loveGoodList = new ArrayList();
        for (int i = 0; i< itemLoveGoodCodeList.length; i++){
            sbf.append("','").append(itemLoveGoodCodeList[i]);
        }
        // ?????? ????????? ????????? ??????
        sbf.append("','").append(itemCodeStory);
        sbf.append("'");
        List<ItemDetailVo> itemDetailVos = commonDao.selectMulti(sbf.toString());
        log.debug("{}",itemDetailVos);

        List boostList = new ArrayList();
        List levelUpList = new ArrayList();
        List storyList = new ArrayList();
        for(ItemDetailVo temp : itemDetailVos){
            String itemNo = temp.getItemNo();
            if(itemPaticleCode.contains(itemNo)){
                particleList.add(temp);
            }else if(itemLoveGoodCode.contains(itemNo)){
                loveGoodList.add(temp);
            }else if(itemCodeBoost.contains(itemNo)){
                boostList.add(temp);
            }else if(itemCodeRocketBoost.contains(itemNo)){
                boostList.add(temp);
            }else if(itemCodeLevelUp.contains(itemNo)){
                levelUpList.add(temp);
            }else if("G9997".contains(itemNo)){
                levelUpList.add(temp);
            }else if("G9998".contains(itemNo)){
                levelUpList.add(temp);
            }else if(itemCodeStory.contains(itemNo)){
                storyList.add(temp);
            }
        }
        resultMap.put("particles", particleList);
        resultMap.put("loveGood", loveGoodList);
        resultMap.put("boost", boostList);
        resultMap.put("levelUp", levelUpList);
        resultMap.put("story", storyList);

        List<HashMap> itemCategories = new ArrayList<>();
        HashMap itemCate1 = new HashMap();
        itemCate1.put("code", "normal");
        itemCate1.put("value", "??????");
        itemCate1.put("isNew", itemIsNew.get("normal"));
        HashMap itemCate2 = new HashMap();
        itemCate2.put("code", "combo");
        itemCate2.put("value", "??????");
        itemCate2.put("isNew", itemIsNew.get("combo"));
        HashMap itemCate3 = new HashMap();
        itemCate3.put("code", "emotion");
        itemCate3.put("value", "??????");
        itemCate3.put("isNew", itemIsNew.get("emotion"));
        HashMap itemCate4 = new HashMap();
        itemCate4.put("code", "text");
        itemCate4.put("value", "??????");
        itemCate4.put("isNew", itemIsNew.get("text"));

        itemCategories.add(itemCate1);
        itemCategories.add(itemCate2);
        itemCategories.add(itemCate3);
        itemCategories.add(itemCate4);

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

            if(deviceVo.getOs() == 1){ // AOS ????????? ??????
                resultMap.put("isAosCheck", "real".equals(DalbitUtil.getActiveProfile()));
            }

            //TODO - ?????? ??????
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
            resultMap.put("giftDalDirect", true);
            resultMap.put("giftDalMin", 10);
            int[] giftDalByRoom = {50, 100, 500, 1000, 2000, 3000, 5000, 10000};
            resultMap.put("giftDalByRoom", giftDalByRoom);
            resultMap.put("giftDalByRoomDirect", true);
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
                boolean isAdmin = adminService.isAdmin(request);
                tokenVo.setAdmin(isAdmin);
            }
        }catch(Exception e){
            isLogin = false;
            tokenVo = null;
        }

        //????????? block ?????? ??????
        String myMemNo = MemberVo.getMyMemNo(request);
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(deviceVo, myMemNo));

        if (isLogin && 0 == adminBlockCnt) {
            //????????? ??????????????? ?????? ????????? ??????,????????? ?????? ?????? ??????????????????
            TokenCheckVo tokenCheckVo = memberDao.selectMemState(myMemNo);

            //?????? ????????? memNo??? ???????????? ??? null??????.
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
        }else{ //??????????????? ?????????/??????????????? ??????????????? ??????
            TokenCheckVo tokenCheckVo = memberDao.selectAnonymousMem(myMemNo);
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
                resultStatus = MemberStatus.???????????????;

            } else {
                //locationVo = DalbitUtil.getLocation(request);
                P_LoginVo pLoginVo = new P_LoginVo("a", os, deviceId, deviceToken, appVer, appAdId, locationVo == null ? "" : locationVo.getRegionName(), ip, browser);
                //ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
                ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
                //ProcedureOutputVo LoginProcedureVo;
                /*if(DalbitUtil.isEmpty(loginList)){
                    throw new CustomUsernameNotFoundException(Status.???????????????_??????????????????);
                }else{
                    LoginProcedureVo = new ProcedureOutputVo(procedureVo);
                }*/
                log.debug("????????? ?????? : {}", new Gson().toJson(LoginProcedureVo));
                if (DalbitUtil.isEmpty(LoginProcedureVo)) {
                    throw new CustomUsernameNotFoundException(MemberStatus.???????????????_??????????????????);
                }
                if (LoginProcedureVo.getRet().equals(MemberStatus.???????????????_??????????????????.getMessageCode())) {
                    resultStatus = MemberStatus.???????????????_??????????????????;
                } else if (LoginProcedureVo.getRet().equals(MemberStatus.???????????????_??????????????????.getMessageCode())) {
                    resultStatus = MemberStatus.???????????????_??????????????????;
                } else if (LoginProcedureVo.getRet().equals(MemberStatus.???????????????_??????????????????.getMessageCode())) {
                    resultStatus = MemberStatus.???????????????_??????????????????;
                } else if (LoginProcedureVo.getRet().equals(MemberStatus.???????????????.getMessageCode())) {
                    HashMap map = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
                    String memNo = DalbitUtil.getStringMap(map, "mem_no");
                    if (DalbitUtil.isEmpty(memNo)) {
                        resultStatus = MemberStatus.???????????????_??????????????????;
                    } else {
                        resultStatus = MemberStatus.???????????????;
                        tokenVo = new TokenVo(jwtUtil.generateToken(memNo, isLogin), memNo, isLogin);
                        result.put("tokenVo", tokenVo);
                        memberService.refreshAnonymousSecuritySession(memNo);
                    }
                } else {
                    resultStatus = MemberStatus.???????????????;
                }
            }
        }else{
            resultStatus = MemberStatus.???????????????;
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

    public HashMap callCodeDefineSelect2(){
        List<Map> data = commonDao.callCodeDefineSelect();

        HashMap<String, Object> result = new HashMap<>();
        result.put("memGubun", setData(data, "mem_slct"));
        result.put("osGubun", setData(data, "os_type"));
        result.put("roomType", new ArrayList());
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
                Map map = list.get(i);
                Object type1 = map.get("type");
                if(type.equals(type1)){
                    int is_use = (int) map.get("is_use");
                    if(is_use == 1){
                        String value = (String) map.get("value");
                        String code = (String) map.get("code");
                        Object order1 = map.get("order");
                        boolean empty = DalbitUtil.isEmpty(order1);
                        if(append == 2){
                            data.add(new CodeVo((String) type1, value, code, empty ? i :  (int) order1, is_use));
                        }else{
                            data.add(new CodeVo(value, code, empty ? i :  (int) order1, is_use));
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
     * ????????? 1,2,3??? ????????????
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
     * ????????? ?????? & ????????? 1,2,3??? ????????????
     */
    public HashMap getKingFanRankList(String roomNo, HttpServletRequest request) {

        P_KingFanRankListVo apiData = new P_KingFanRankListVo(roomNo, request);
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
                P_KingFanRankListVo p_kingFanRankListVo = kingFanRankListVo.get(i);
                liveBadgeMap.put("mem_no", p_kingFanRankListVo.getMem_no());
                liveBadgeMap.put("type", 0);
                liveBadgeMap.put("by", "api");
                BadgeFrameVo badgeFrameVo = commonDao.callMemberBadgeFrame(liveBadgeMap);
                if(DalbitUtil.isEmpty(badgeFrameVo)){
                    outVoList.add(new P_KingFanRankListOutVo(p_kingFanRankListVo, i+1));
                }else{
                    outVoList.add(new P_KingFanRankListOutVo(p_kingFanRankListVo, i+1, badgeFrameVo));
                }
            }

            HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
            fanRankList.put("list", outVoList);
            fanRankList.put("kingMemNo", DalbitUtil.getStringMap(resultMap, "king_mem_no"));
            fanRankList.put("kingNickNm", DalbitUtil.getStringMap(resultMap, "king_nickName"));
            fanRankList.put("kingGender", DalbitUtil.getStringMap(resultMap, "king_memSex"));
            fanRankList.put("kingAge", DalbitUtil.getIntMap(resultMap, "m_king_age"));
            fanRankList.put("kingProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "m_king_profileImage"), DalbitUtil.getStringMap(resultMap, "king_memSex"), DalbitUtil.getProperty("server.photo.url")));
            fanRankList.put("msgCode", BroadcastStatus.?????????_???????????????_??????.getMessageCode());

        } else if (BroadcastStatus.?????????_???????????????_?????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            fanRankList.put("msgCode", BroadcastStatus.?????????_???????????????_??????.getMessageCode());
        } else if (BroadcastStatus.?????????_???????????????_???????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            fanRankList.put("msgCode", BroadcastStatus.?????????_???????????????_???????????????.getMessageCode());
        } else if (BroadcastStatus.?????????_???????????????_????????????.getMessageCode().equals(procedureOutputVo.getRet())) {
            fanRankList.put("msgCode", BroadcastStatus.?????????_???????????????_????????????.getMessageCode());
        } else {
            fanRankList.put("msgCode", BroadcastStatus.?????????_???????????????_??????.getMessageCode());
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

    public String getServerInstance(HttpServletRequest request){
        String result = request.getLocalAddr();
        if(DalbitUtil.isEmpty(result)){
            return "";
        }
        result = result.substring(result.length() - 3);
        String rootDir = request.getSession().getServletContext().getRealPath("/");
        if(rootDir.endsWith("/") || rootDir.endsWith("\\")){
            rootDir = rootDir.substring(0, rootDir.length() - 1);
        }
        String instance = rootDir.substring(rootDir.length() - 1);
        if(!Pattern.matches("\\d", instance)){
            instance = "1";
        }
        result += "_" + instance;
        return result;
    }

    /**
     * ????????? ???????????? ??????
     */
    public void requestSms(SmsVo smsVo) {
        commonDao.requestSms(smsVo);
    }


    /**
     * ?????? ?????? ??????
     */
    public String callMemberCertification(P_SelfAuthVo pSelfAuthVo){
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.callMemberCertification(procedureVo);

        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());
        procedureVo.setData(pSelfAuthVo);

        String result ="";
        if(procedureVo.getRet().equals(MemberStatus.??????????????????.getMessageCode())) {
            pSelfAuthVo.setComment("???????????? ??????");
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(MemberStatus.????????????_????????????.getMessageCode())) {
            pSelfAuthVo.setComment("???????????? ?????? ??????");
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????));
        } else if(procedureVo.getRet().equals(MemberStatus.????????????_??????.getMessageCode())) {
            pSelfAuthVo.setComment("???????????? ??????");
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_??????));
        } else {
            pSelfAuthVo.setComment("???????????? ??????");
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????????????????, procedureVo.getData()));
        }
        //???????????? ???????????? ??????
        try {
            memberService.callProfileEditHistory(pSelfAuthVo);
        }catch(Exception e){
            log.info("???????????? ?????? callProfileEditHistory Exception {}, {}", pSelfAuthVo.getComment(), e);
        }
        return result;
    }

    private String isYnChk(String value){
        // ?????? "y" ????????? "n"??? ???????????? "n"?????? ??????
        if(StringUtils.equals(value, "y") || StringUtils.equals(value, "n")){
            return value;
        }
        
        return "n";
    }

    /**
     * ?????? ?????? ?????? ?????? ??????
     */
    public String getCertificationChk(P_SelfAuthChkVo pSelfAuthVo){
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.getCertificationChk(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();

        String result;
        if(procedureVo.getRet().equals(MemberStatus.??????????????????_??????.getMessageCode())) {
            //???????????? ?????? ??? ???????????? ?????? ??????
            AdultCheckVo adultCheckVo = commonDao.getMembirth(pSelfAuthVo.getMem_no());
            int birthYear = Integer.parseInt(adultCheckVo.getMem_birth_year());
            int birthMonth = Integer.parseInt(adultCheckVo.getMem_birth_month());
            int birthDay = Integer.parseInt(adultCheckVo.getMem_birth_day());
            String adultYn = birthToAmericanAge(birthYear, birthMonth, birthDay) < 19 ? "n" : "y"; // 19??? ??????
            /*if(DalbitUtil.getKorAge(Integer.parseInt(adultCheckVo.getMem_birth_year())) < 20){
                adultYn = "n";
            } else {
                adultYn = "y";
            }*/
            returnMap.put("adultYn", adultYn);       //????????????
            returnMap.put("parentsAgreeYn", isYnChk(DalbitUtil.getStringMap(resultMap, "parents_agree_yn")));   //??????????????????
            returnMap.put("phoneNo", DalbitUtil.getStringMap(resultMap, "mem_phone"));      //???????????????
            returnMap.put("company", DalbitUtil.getStringMap(resultMap, "comm_company"));   //???????????? ?????? '??????'
            returnMap.put("ci", DalbitUtil.getStringMap(resultMap, "ci"));
            returnMap.put("memName", DalbitUtil.getStringMap(resultMap, "memName"));
            returnMap.put("isSimplePay", DalbitUtil.getIntMap(resultMap, "simplePayCnt") > 0);
            procedureVo.setData(returnMap);

            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(MemberStatus.??????????????????_??????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????));
        } else if(procedureVo.getRet().equals(MemberStatus.??????????????????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_??????));
        }
        return result;
    }


    /**
     * ?????? ?????? ??????
     */

    public String saveErrorLog(P_ErrorLogVo pErrorLogVo){
        return saveErrorLog(pErrorLogVo, null);
    }

    public String saveErrorLog(P_ErrorLogVo pErrorLogVo, HttpServletRequest request){
        if(request != null && request instanceof HttpServletRequest){
            String desc = "Server : " + getServerInstance(request) + "\n";
            DeviceVo deviceVo  = new DeviceVo(request);
            desc += "AuthToken : " + request.getHeader(SSO_HEADER_COOKIE_NAME) + "\n";
            desc += (new Gson().toJson(deviceVo)) + "\n";
            desc += pErrorLogVo.getDesc();
            pErrorLogVo.setDesc(desc);
        }
        ProcedureVo procedureVo = new ProcedureVo(pErrorLogVo);
        commonDao.saveErrorLog(procedureVo);

        String result;
        if(procedureVo.getRet().equals(CommonStatus.??????????????????_??????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????????????????_??????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????????????????_??????));
        }
        return result;
    }


    /**
     * PUSH ?????? ??????
     */
    public String callPushAdd(P_PushVo pPushVo) {
        ProcedureVo procedureVo = new ProcedureVo(pPushVo);
        commonDao.callPushAdd(procedureVo);

        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());

        String result;
        if(procedureVo.getRet().equals(CommonStatus.????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????));
        } else if(procedureVo.getRet().equals(CommonStatus.??????_????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????_????????????));
        } else if(procedureVo.getRet().equals(CommonStatus.??????_????????????????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????_????????????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????));
        }
        return result;

    }

    /**
     * PUSH Click
     */
    public String callPushClickUpdate(P_PushClickVo pPushClickVo) {
        ProcedureVo procedureVo = new ProcedureVo(pPushClickVo);
        commonDao.callPushClickUpdate(procedureVo);

        log.info("???????????? ?????? ??????: {}", procedureVo.getRet());
        log.info("???????????? ?????? ?????????: {}", procedureVo.getExt());

        String result;
        if(procedureVo.getRet().equals(CommonStatus.??????????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.??????????????????));
        } else if(procedureVo.getRet().equals(CommonStatus.????????????_??????????????????.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????_??????????????????));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????_??????));
        }
        return result;

    }

    /**
     * ????????? ????????? ??????
     */
    public String banWordSelect(){
        BanWordVo banWordVo = commonDao.banWordSelect(0);
        return banWordVo.getBanWord();
    }
    /**
     * ??????????????? ????????? ??????
     */
    public String titleBanWordSelect(){
        BanWordVo banWordVo = commonDao.banWordSelect(1);
        return banWordVo.getBanWord();
    }

    /**
     * ?????? ????????? ??????(??????)
     */
    public String broadcastBanWordSelect(BanWordVo banWordVo){
        BanWordVo resultVo = commonDao.broadcastBanWordSelect(banWordVo);
        if(DalbitUtil.isEmpty(resultVo)){
            return null;
        }

        if(DalbitUtil.isEmpty(resultVo.getBanWord())){ // TODO - size ?????? ??????
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
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????, resultMap));
        }else if("invalid token".equals(result)){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????????????????));
        }else if("blank token".equals(result)){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????));

        }
        return result;
    }

    private String readStream(InputStream stream){
        StringBuffer pageContents = new StringBuffer();
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader buff = new BufferedReader(reader);

            String pageContentsR = "";
            while((pageContentsR = buff.readLine()) != null){
                pageContents.append(pageContentsR);
                pageContents.append("\r\n");
            }
            reader.close();
            buff.close();

        }catch (Exception e){ }

        return pageContents.toString();
    }

    public void setStateSession(HttpServletRequest request){
        String pStateS = request.getParameter("state");
        HashMap pStateMap = new Gson().fromJson(pStateS, HashMap.class);
        setStateSession(request, pStateMap);
    }

    public void setStateSession(HttpServletRequest request, HashMap stateMap){
        request.setAttribute("SocialState", stateMap);
    }

    public String connectFacebookNative(HttpServletRequest request) {
        String result = "";
        HashMap resultMap = new HashMap();
        String access_token = request.getParameter("accessToken");
        if(DalbitUtil.isEmpty(access_token)){
            result = "blank token";
        }else {
            StringBuffer urlString = new StringBuffer();
            urlString.append("https://graph.facebook.com/");
            urlString.append(FACEBOOK_APP_VER);
            urlString.append("/me?fields=id,name,first_name,last_name,middle_name,picture&access_token=");
            urlString.append(access_token);

            try{
                URL url = new URL(urlString.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String first = readStream(con.getInputStream());

                HashMap facebookInfo = new Gson().fromJson(first, HashMap.class);
                if(!DalbitUtil.isEmpty(facebookInfo)){
                    resultMap.put("memType", "f");
                    resultMap.put("memId", DalbitUtil.getStringMap(facebookInfo, "id"));
                    resultMap.put("email", "");
                    if(!DalbitUtil.isEmpty(facebookInfo.get("email"))){
                        resultMap.put("email", DalbitUtil.getStringMap(facebookInfo, "email"));
                    }
                    String name = DalbitUtil.getStringMap(facebookInfo, "first_name");
                    if(!DalbitUtil.isEmpty(facebookInfo.get("middle_name"))){
                        name += " ";
                        name += DalbitUtil.getStringMap(facebookInfo, "middle_name");
                    }
                    name += " ";
                    name += DalbitUtil.getStringMap(facebookInfo, "last_name");
                    resultMap.put("name", name);
                    resultMap.put("nickNm", name);
                    resultMap.put("gender", "n");

                    resultMap.put("profImgUrl", "");
                    if(!DalbitUtil.isEmpty(facebookInfo.get("picture"))){
                        HashMap picture = new Gson().fromJson(new Gson().toJson(facebookInfo.get("picture")), HashMap.class);
                        if(!DalbitUtil.isEmpty(picture.get("data"))){
                            HashMap pictureData = new Gson().fromJson(new Gson().toJson(picture.get("data")), HashMap.class);
                            if(!DalbitUtil.isEmpty(pictureData.get("url"))){
                                resultMap.put("profImgUrl", DalbitUtil.getStringMap(pictureData, "url"));
                            }
                        }
                    }

                    result = "success";
                }else {
                    result = "invalid token";
                }
            }catch(Exception e) {
                log.info("============= facebook connected error : {}", e.getMessage());
            }
        }

        setStateSession(request);

        if("success".equals(result)) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.?????????????????????_??????, resultMap));
        }else if("invalid token".equals(result)){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.?????????????????????_??????????????????));
        }else if("blank token".equals(result)){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.?????????????????????_????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.?????????????????????_??????));
        }

        return result;
    }

    public String connectNaverNative(HttpServletRequest request) {
        String result = "";
        String access_token = request.getParameter("accessToken");
        if(DalbitUtil.isEmpty(access_token)){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????));
        }else {
            StringBuffer urlString = new StringBuffer();
            urlString.append("https://openapi.naver.com/v1/nid/me");

            try{
                URL url = new URL(urlString.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Authorization", "Bearer " + access_token);
                String first = readStream(con.getInputStream());

                HashMap naverRootInfo = new Gson().fromJson(first, HashMap.class);
                if(!DalbitUtil.isEmpty(naverRootInfo)){
                    HashMap naverInfo = new Gson().fromJson(new Gson().toJson(naverRootInfo.get("response")), HashMap.class);
                    if(!DalbitUtil.isEmpty(naverInfo)){
                        HashMap resultMap = new HashMap();
                        resultMap.put("memType", "n");
                        resultMap.put("memId", DalbitUtil.getStringMap(naverInfo, "id"));
                        resultMap.put("nickNm", "");
                        if(!DalbitUtil.isEmpty(naverInfo.get("nickname"))){
                            resultMap.put("nickNm", DalbitUtil.getStringMap(naverInfo, "nickname"));
                        }
                        resultMap.put("name", "");
                        if(!DalbitUtil.isEmpty(naverInfo.get("name"))){
                            resultMap.put("name", DalbitUtil.getStringMap(naverInfo, "name"));
                        }
                        resultMap.put("email", "n");
                        if(!DalbitUtil.isEmpty(naverInfo.get("email"))){
                            resultMap.put("email", DalbitUtil.getStringMap(naverInfo, "email"));
                        }

                        resultMap.put("gender", "");
                        if(!DalbitUtil.isEmpty(naverInfo.get("gender"))){
                            resultMap.put("gender", DalbitUtil.getStringMap(naverInfo, "gender").toLowerCase());
                        }

                        resultMap.put("profImgUrl", "");
                        if(!DalbitUtil.isEmpty(naverInfo.get("profile_image"))){
                            resultMap.put("profImgUrl", DalbitUtil.getStringMap(naverInfo, "profile_image"));
                        }

                        result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????, resultMap));
                    }
                }
            }catch(Exception e) {
                log.error("============= naver connected error : {}", e.getMessage());
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????));
            }
        }

        setStateSession(request);

        return result;
    }

    public String connectKakaoNative(HttpServletRequest request) {
        String result = "";
        String access_token = request.getParameter("accessToken");
        if(DalbitUtil.isEmpty(access_token)){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????));
        }else {
            StringBuffer urlString = new StringBuffer();
            urlString.append("https://kapi.kakao.com/v2/user/me");

            try{
                URL url = new URL(urlString.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Authorization", "Bearer " + access_token);
                String first = readStream(con.getInputStream());

                HashMap kakaoRootInfo = new Gson().fromJson(first, HashMap.class);
                if(!DalbitUtil.isEmpty(kakaoRootInfo)){
                    if(!DalbitUtil.isEmpty(kakaoRootInfo.get("id")) && !DalbitUtil.isEmpty(kakaoRootInfo.get("kakao_account"))){
                        HashMap kakaoAcntInfo = new Gson().fromJson(new Gson().toJson(kakaoRootInfo.get("kakao_account")), HashMap.class);
                        if(!DalbitUtil.isEmpty(kakaoAcntInfo) && !DalbitUtil.isEmpty(kakaoAcntInfo.get("profile"))){
                            HashMap kakaoProfInfo = new Gson().fromJson(new Gson().toJson(kakaoAcntInfo.get("profile")), HashMap.class);
                            if(!DalbitUtil.isEmpty(kakaoProfInfo)){
                                HashMap resultMap = new HashMap();
                                resultMap.put("memType", "k");
                                resultMap.put("memId", DalbitUtil.getStringMap(kakaoRootInfo, "id"));
                                resultMap.put("nickNm", "");
                                resultMap.put("name", "");
                                if(!DalbitUtil.isEmpty(kakaoProfInfo.get("nickname"))){
                                    resultMap.put("nickNm", DalbitUtil.getStringMap(kakaoProfInfo, "nickname"));
                                }
                                resultMap.put("email", "");
                                if(!DalbitUtil.isEmpty(kakaoAcntInfo.get("email"))){
                                    resultMap.put("email", DalbitUtil.getStringMap(kakaoAcntInfo, "email"));
                                }
                                resultMap.put("gender", "n");
                                if(!DalbitUtil.isEmpty(kakaoAcntInfo.get("gender"))){
                                    resultMap.put("gender", DalbitUtil.getStringMap(kakaoAcntInfo, "gender").toLowerCase().substring(0, 1));
                                }
                                resultMap.put("profImgUrl", "");
                                if(!DalbitUtil.isEmpty(kakaoProfInfo.get("profile_image_url"))){
                                    resultMap.put("profImgUrl", DalbitUtil.getStringMap(kakaoProfInfo, "profile_image_url"));
                                }

                                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????, resultMap));
                            }
                        }
                    }
                }
            }catch(Exception e) {
                log.error("============= kakao connected error : {}", e.getMessage());
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.???????????????_??????));
            }
        }

        setStateSession(request);
        return result;
    }

    /**
     * ???????????? ??????
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
     * ???????????????(?????????) ?????? ????????????
     */
    public String updateMemberCertification(P_SelfAuthVo pSelfAuthVo) {

        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.updateMemberCertification(procedureVo);

        String result ="";
        if("0".equals(procedureVo.getRet())) {
            pSelfAuthVo.setComment("??????????????? ?????? ??????");
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.?????????????????????, pSelfAuthVo));
        } else {
            pSelfAuthVo.setComment("??????????????? ?????? ??????");
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.?????????????????????));
        }
        //???????????? ???????????? ??????
        try {
            memberService.callProfileEditHistory(pSelfAuthVo);
        }catch(Exception e){
            log.info("??????????????? ?????? ?????? callProfileEditHistory Exception {}, {}",pSelfAuthVo.getComment(), e);
        }
        return result;


    }

    /**
     * ???????????????(?????????) ?????? ?????? ????????????
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
     * ???????????? ???????????? ?????? ????????????
     */
    public List<CodeVo> selectRoomTypeCodeList(CodeVo codeVo){
        return getCodeListCache(codeVo.getCd());
    }

    /**
     * ???????????? ?????? ?????? ????????????
     */
    public List<CodeVo> selectClipTypeCodeList(CodeVo codeVo){
        return getCodeListCache(codeVo.getCd());
    }

    /**
     * ???????????? ??????
     */
    public List<CodeVo> selectExchangeBankCodeList(CodeVo codeVo){
        return getCodeListCache(codeVo.getCd());
    }


    /**
     * ?????? ????????? & ?????? ?????? ?????? ??????
     */
    public String getLongTermDate(P_LongTermVo pLongTermVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLongTermVo);
        commonDao.getLongTermDate(procedureVo);

        HashMap returnMap  = new HashMap<>();
        String result;
        if(ClipStatus.????????????_??????.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            returnMap.put("memNo", DalbitUtil.getStringMap(resultMap, "mem_no"));
            returnMap.put("type", DalbitUtil.getIntMap(resultMap, "type"));
            DateFormat format = DateFormat.getDateInstance(DateFormat.LONG, Locale.KOREAN);
            returnMap.put("lastLoginDate", format.format(DalbitUtil.getDateMap(resultMap, "lastLoginDate")));
            returnMap.put("dueDate", format.format(DalbitUtil.getDateMap(resultMap, "dueDate")));
            var codeVo = selectCodeDefine(new CodeVo(Code.??????_?????????_????????????.getCode(), Code.??????_?????????_????????????.getDesc()));
            if (!DalbitUtil.isEmpty(codeVo)) {
                returnMap.put("longTermDate", codeVo.getValue());
            } else {
                returnMap.put("longTermDate", "");
            }
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????_??????, returnMap));
        }else if(ClipStatus.????????????_??????.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????_????????????));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????_??????));
        }

        return result;
    }

    /**
     * ?????? ???????????? ?????? ??? ???????????? (????????????)
     */
    public String sleepMemChkUpd(String memNo, String memPhone) {
        String result = "";
        Integer procResult = common.sleepMemChkUpd(memNo, memPhone);
        if(procResult == 1) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????_??????_??????));
        }else if(procResult == -1) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????_????????????));
        }else if(procResult == -2) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.????????????_????????????_??????????????????));
        }else if(procResult == -3) {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.??????????????????_????????????));
        }else {
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.????????????????????????));
        }

        return result;
    }

    /**
     * ??????????????? ???????????????(???????????? ?????? ?????? ??????)
     */
    public ResVO parentCertIns(ParentCertInputVo agreeInfo) {
        ResVO result = new ResVO();
        try {
            Integer insResult = common.parentsAuthEmailIns(agreeInfo);
            //-3:?????????, -2:?????? ?????????, -1:?????? ????????? ?????????, 0:??????, 1:??????
            switch (insResult) {
                case -3: result.setResVO(ResMessage.C40003.getCode(), ResMessage.C40003.getCodeNM(), insResult); break;
                case -2: result.setResVO(ResMessage.C40002.getCode(), ResMessage.C40002.getCodeNM(), insResult); break;
                case -1: result.setResVO(ResMessage.C40001.getCode(), ResMessage.C40001.getCodeNM(), insResult); break;
                case 0: result.setDBErrorResVO(); log.error("CommonService / parentsAuthChk DB error"); break;
                case 1: result.setSuccessResVO(insResult); break;
                default: result.setFailResVO();
            }
        } catch (Exception e) {
            log.error("CommonService / parentsAuthChk ???????????? ?????? ????????? ????????? ins ??????", e);
        }

        return result;
    }

    /**
     * ??????????????? pass ??????(??????)
     */
    public ResVO parentsAuthIns(ParentCertInputVo agreeInfo) {
        ResVO result = new ResVO();
        try {
            String memNo = agreeInfo.getMemNo();
            ParentsAuthSelVo parentsAuthSel = common.parentsAuthSel(memNo);
            String email = parentsAuthSel.getParents_mem_email();
            agreeInfo.setPMemEmail(email);
            agreeInfo.setAgreementDate(parentsAuthSel.getAgreement_date());

            Integer insResult = common.parentsAuthIns(agreeInfo);
            // -5:???????????????,-4:?????????, -3:?????? ?????????, -2: ????????? ?????????, -1:?????? ????????? ?????????, 0:??????, 1:??????
            switch (insResult) {
                case -5: result.setResVO(ResMessage.C40004.getCode(), ResMessage.C40004.getCodeNM(), insResult); break;
                case -4: result.setResVO(ResMessage.C40003.getCode(), ResMessage.C40003.getCodeNM(), insResult); break;
                case -3: result.setResVO(ResMessage.C40002.getCode(), ResMessage.C40002.getCodeNM(), insResult); break;
                case -2: result.setResVO(ResMessage.C40005.getCode(), ResMessage.C40005.getCodeNM(), insResult); break;
                case -1: result.setResVO(ResMessage.C40001.getCode(), ResMessage.C40001.getCodeNM(), insResult); break;
                case 0: result.setDBErrorResVO(); break;
                case 1:
                    try {
                        ParentsAgreeEmailVo emailVo = new ParentsAgreeEmailVo();
                        emailVo.setMemNo(memNo);
                        emailVo.setEmail(email);
                        emailVo.setAgreeAllowUserName(agreeInfo.getPMemName());
                        emailVo.setAgreeRcvUserName(parentsAuthSel.getMem_name());
                        emailVo.setAgreeRcvUserId(parentsAuthSel.getMem_id());
                        emailVo.setAgreeDuration(parentsAuthSel.getAgreement_date() + "??????");
                        emailVo.setAgreeExpireDate(parentsAuthSel.getExpire_date().substring(0,10).replaceAll("-", "."));

                        sendPayAgreeEmail(emailVo); // ?????? ??????
                        result.setSuccessResVO(insResult);
                    } catch (Exception e) {
                        log.error("CommonService / parentsAuthIns ????????? ?????? ???????????? ??????", e);
                        result.setFailResVO();
                    }
                    break;
                default:
            }
        } catch (Exception e) {
            log.error("CommonService / parentsAuthIns ??????????????? pass ?????? ins ??????", e);
            result.setFailResVO();
        }

        return result;
    }

    /**
     * ???????????? ???????????????(??????) ?????? ?????? ?????? ??????
     */
    private void sendPayAgreeEmail(ParentsAgreeEmailVo parentsAgreeEmailVo) {
        String memNo = parentsAgreeEmailVo.getMemNo();
        String email = parentsAgreeEmailVo.getEmail();
        String sHtml = "";
        StringBuffer mailContent = new StringBuffer();
        BufferedReader in = null;
        try{
            URL url = new URL("http://image.dalbitlive.com/resource/mailForm/agreeInfo.txt");
            URLConnection urlconn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlconn.getInputStream(),"utf-8"));

            while((sHtml = in.readLine()) != null){
                mailContent.append("\n");
                mailContent.append(sHtml);
            }

            String msgCont;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(new Date());

            String rcvUserName = parentsAgreeEmailVo.getAgreeRcvUserName();
            String rcvUserLastLetterReplace = rcvUserName.substring(0, rcvUserName.length()-1) + "*"; // ?????? ????????? ?????? * ??????
            String allowUserName = parentsAgreeEmailVo.getAgreeAllowUserName();
            String allowUserLastLetterReplace = allowUserName.substring(0, allowUserName.length()-1) + "*"; // ?????? ????????? ?????? * ??????

            msgCont = mailContent.toString().replaceAll("@@agreeAllowUserName@@", allowUserLastLetterReplace);
            msgCont = msgCont.replaceAll("@@agreeRcvUserName@@", rcvUserLastLetterReplace);
            msgCont = msgCont.replaceAll("@@agreeRcvUserId@@", parentsAgreeEmailVo.getAgreeRcvUserId());
            msgCont = msgCont.replaceAll("@@agreeDate@@", today);
            msgCont = msgCont.replaceAll("@@agreeDuration@@", parentsAgreeEmailVo.getAgreeDuration());
            msgCont = msgCont.replaceAll("@@agreeExpireDate@@", parentsAgreeEmailVo.getAgreeExpireDate());
            msgCont = msgCont.replaceAll("@@agreeScope@@", "??????(??? ??????, ????????? ??????)");

            EmailVo emailVo = new EmailVo("?????? ?????????(???????????????) ?????? ??????", email, msgCont);
            emailService.sendEmail(emailVo);

            // ????????? ?????? ??????
            JSONObject mailEtcInfo = new JSONObject();
            mailEtcInfo.put("agreeAllowUserName", allowUserLastLetterReplace);
            mailEtcInfo.put("agreeRcvUserName", rcvUserLastLetterReplace);
            mailEtcInfo.put("agreeRcvUserId", parentsAgreeEmailVo.getAgreeRcvUserId());
            mailEtcInfo.put("agreeDate", today);
            mailEtcInfo.put("agreeDuration", parentsAgreeEmailVo.getAgreeDuration());
            mailEtcInfo.put("agreeExpireDate", parentsAgreeEmailVo.getAgreeExpireDate());
            mailEtcInfo.put("agreeScope", "??????(??? ??????, ????????? ??????)");
            mailEtcInfo.put("rcvUserFullName", rcvUserName);
            mailEtcInfo.put("allowUserFullName", allowUserName);
            ParentsEmailLogInsVo parentsEmailLogInsVo = new ParentsEmailLogInsVo(memNo, email, "a", mailEtcInfo.toString());

            Integer logInsRes = common.parentsAuthEmailLogIns(parentsEmailLogInsVo);
            if(logInsRes != 1) {
                log.error("CommonService / sendPayAgreeEmail ????????? ?????? ?????? ins ?????? => memNo: {}", memNo);
            }
        } catch(Exception e){
            log.error("CommonService / sendPayAgreeEmail ????????? ?????? ??????", e);
        }
    }

    /**
     * ???????????? ?????? ????????? ?????? ?????? ?????? (??????)
     */
    public String parentsAuthChk(String memNo) {
        String result = "n";
        try {
            result = common.parentsAuthChk(memNo);
        } catch (Exception e) {
            log.error("CommonService / parentsAuthChk ???????????? ?????? ????????? ?????? ?????? ?????? ??????", e);
        }

        return result;
    }

    /**
     * ???????????? ???????????????(??????) ?????? ?????? ?????? ??????
     */
    public void sendPayMailManager(String memNo, String orderId) {
        try {
            //private String order_id;
            //    private String mem_no;
            //    private String pay_way;
            //    private String pay_amt;
            //    private String item_amt;
            //    private String pay_code;
            //    private String card_no;
            //    private String card_nm;
            //    private String pay_ok_date;
            //    private String pay_ok_time;
            PaySuccSelVo paySuccSelVo = common.paySuccSel(memNo, orderId);
            // ???????????? ?????? ??????
            String[] birthSplit = paySuccSelVo.getBirth().split("-");
            int birthYear = Integer.parseInt(birthSplit[0]);
            int birthMonth = Integer.parseInt(birthSplit[1]);
            int birthDay = Integer.parseInt(birthSplit[2]);
            if(birthToAmericanAge(birthYear, birthMonth, birthDay) < 19) {
                ParentsAuthSelVo parentsAuthSel = common.parentsAuthSel(memNo);
                String email = parentsAuthSel.getParents_mem_email();

                String[] accountInfo = accountInfo(paySuccSelVo);
                DecimalFormat formatter = new DecimalFormat("###,###");
                int payPrice = (int)Double.parseDouble(paySuccSelVo.getPay_amt());
                String priceComma = formatter.format(payPrice);
                ParentsPayEmailVo parentsPayEmailVo = new ParentsPayEmailVo();
                parentsPayEmailVo.setOrderId(paySuccSelVo.getOrder_id());
                parentsPayEmailVo.setMemNo(paySuccSelVo.getMem_no());
                parentsPayEmailVo.setEmail(email);
                parentsPayEmailVo.setPaymentUserName(parentsAuthSel.getParents_mem_name());

                parentsPayEmailVo.setPaymentDate(paySuccSelVo.getPay_ok_date() + " " + paySuccSelVo.getPay_ok_time()); // ????????????
                parentsPayEmailVo.setPaymentMethod(getPayWay(paySuccSelVo.getPay_way())); // ?????? ??????
                parentsPayEmailVo.setPaymentAccount(accountInfo[0]);// ?????? ??????1
                parentsPayEmailVo.setPaymentBank(accountInfo[1]);// ?????? ??????2
                parentsPayEmailVo.setPaymentProduct(paySuccSelVo.getPay_code());// ?????? ??????
                parentsPayEmailVo.setPaymentQuantity(paySuccSelVo.getItem_amt());// ?????? ??????
                parentsPayEmailVo.setPaymentPrice(priceComma + "???");// ?????? ??????

                sendPayMail(parentsPayEmailVo);
            }
        } catch (Exception e) {
            log.error("CommonService / sendPayMailManager error : ", e);
        }
    }

    private String[] accountInfo(PaySuccSelVo paySuccSelVo) {
        String[] result = new String[2];
        String info1 = "-";
        String info2 = "";
        switch (paySuccSelVo.getPay_way()) {
            case "MC":
                if(!StringUtils.isEmpty(paySuccSelVo.getPay_info_no())) {
                    info1 = paySuccSelVo.getPay_info_no()
                        .replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                }
                break;
            case "CN":
                if(!StringUtils.isEmpty(paySuccSelVo.getPay_info_no())) {
                    info1 = paySuccSelVo.getPay_info_no()
                        .replaceAll("/([0-9*]{4})([0-9*]{4})([0-9*]{4})([0-9*]{4})/", "$1-$2-$3-$4");
                    info2 = paySuccSelVo.getPay_info_nm();
                }
                break;
            case "cashbee":
            case "tmoney":
            case "payco":
            case "kakaopay":
            case "kakaoMoney":
                info1 = paySuccSelVo.getPay_info();
                break;
            case "simple":
                info1 = paySuccSelVo.getAccount_no();
                //info2 = paySuccSelVo.getBank_code(); // bank code
                break;
            default:
        }

        result[0] = info1;
        result[1] = info2;
        return result;
    }

    // ?????????
    public int birthToAmericanAge(int birthYear, int birthMonth, int birthDay) {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);
        int monthAndDay = Integer.parseInt(nowMonth + "" + nowDay);

        int birthMonthAndDay = Integer.parseInt(birthMonth + "" + birthDay);
        int yearDiff = nowYear - birthYear;
        int monthAndDayDiff = monthAndDay - birthMonthAndDay;
        int manAge = yearDiff;

        if(monthAndDayDiff < 0) {
            manAge--;
        }

        return manAge;
    }

    private String getPayWay(String code) {
        switch (code) {
            case "simple": return "????????????(??????)";
//            case "VA": return "????????????";
            case "CN": return "????????????";
            case "MC": return "?????????";
            case "kakaoMoney": return "???????????????(??????)";
            case "kakaopay": return "???????????????(??????)";
            case "payco": return "?????????";
            case "GM": return "???????????????";
            case "tmoney": return "?????????";
            case "cashbee": return "?????????";
            case "HM": return "?????????????????????";
//            case "InApp": return "??????(IOS)";

            case "GG": return "?????????????????????"; // ?????? ????????? ??????
            case "GC": return "?????????????????????"; // ?????? ????????? ??????
            default: return "";
        }
    }

    private void sendPayMail(ParentsPayEmailVo parentsPayEmailVo) {
        String memNo = parentsPayEmailVo.getMemNo();
        String email = parentsPayEmailVo.getEmail();
        String sHtml = "";
        StringBuffer mailContent = new StringBuffer();
        BufferedReader in = null;
        try{
            URL url = new URL("http://image.dalbitlive.com/resource/mailForm/payInfo.txt");
            URLConnection urlconn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlconn.getInputStream(),"utf-8"));

            while((sHtml = in.readLine()) != null){
                mailContent.append("\n");
                mailContent.append(sHtml);
            }

            String msgCont;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String today = dateFormat.format(new Date());
            String paymentUserName = parentsPayEmailVo.getPaymentUserName();
            String paymentUserLastLetterReplace = paymentUserName.substring(0, paymentUserName.length()-1) + "*"; // ?????? ????????? ?????? * ??????

            msgCont = mailContent.toString().replaceAll("@@paymentUserName@@", paymentUserLastLetterReplace);
            msgCont = msgCont.replaceAll("@@paymentDate@@", today);
            msgCont = msgCont.replaceAll("@@paymentMethod@@", parentsPayEmailVo.getPaymentMethod());
            msgCont = msgCont.replaceAll("@@paymentAccount@@", parentsPayEmailVo.getPaymentAccount());
            msgCont = msgCont.replaceAll("@@paymentBank@@", parentsPayEmailVo.getPaymentBank());
            msgCont = msgCont.replaceAll("@@paymentProduct@@", parentsPayEmailVo.getPaymentProduct());
            msgCont = msgCont.replaceAll("@@paymentQuantity@@", parentsPayEmailVo.getPaymentQuantity());
            msgCont = msgCont.replaceAll("@@paymentPrice@@", parentsPayEmailVo.getPaymentPrice());

            EmailVo emailVo = new EmailVo("?????? ?????? ??????", email, msgCont);
            emailService.sendEmail(emailVo);

            // ????????? ?????? ??????
            JSONObject mailEtcInfo = new JSONObject();
            mailEtcInfo.put("paymentUserName", paymentUserLastLetterReplace);
            mailEtcInfo.put("paymentDate", today);
            mailEtcInfo.put("paymentMethod", parentsPayEmailVo.getPaymentMethod());
            mailEtcInfo.put("paymentAccount", parentsPayEmailVo.getPaymentAccount());
            mailEtcInfo.put("paymentBank", parentsPayEmailVo.getPaymentBank());
            mailEtcInfo.put("paymentProduct", parentsPayEmailVo.getPaymentProduct());
            mailEtcInfo.put("paymentQuantity", parentsPayEmailVo.getPaymentQuantity());
            mailEtcInfo.put("paymentPrice", parentsPayEmailVo.getPaymentPrice());
            mailEtcInfo.put("paymentUserFullName", paymentUserName);
            ParentsEmailLogInsVo parentsEmailLogInsVo = new ParentsEmailLogInsVo(memNo, email, "p", mailEtcInfo.toString());

            Integer logInsRes = common.parentsAuthEmailLogIns(parentsEmailLogInsVo);
            if(logInsRes != 1) {
                log.error("CommonService / sendPayMail ????????? ?????? ?????? ins ?????? => memNo: {}", memNo);
            }
        }
        catch(Exception e){
            log.error("CommonService / sendPayMail ????????? ?????? ?????? : ", e);
        }
    }

    public String getNationCode(HttpServletRequest request) {
        String nationCode = null;
        try {
            nationCode = commonDao.getNationCode(request.getRemoteAddr());
            if(nationCode == null){
                return gsonUtil.toJson(new JsonOutputVo(CommonStatus.?????????_??????_????????????, null));
            }
        } catch (Exception e) {
            log.error("CommonService getNationCode Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.?????????_??????, nationCode));
    }
}
