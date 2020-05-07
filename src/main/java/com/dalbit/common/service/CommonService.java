package com.dalbit.common.service;

import com.dalbit.broadcast.vo.procedure.P_RoomJoinTokenVo;
import com.dalbit.common.annotation.NoLogging;
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
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
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
import java.util.*;
import java.util.regex.Pattern;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.SignedJWT;

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

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

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
        String platform = "";
        DeviceVo deviceVo = new DeviceVo(request);
        for(int i = 1; i < 4; i++){
            if(i == deviceVo.getOs()){
                platform += "1";
            }else{
                platform += "_";
            }
        }

        P_ItemVo pItemVo = new P_ItemVo();
        pItemVo.setItem_slct(1);
        pItemVo.setPlatform(platform);

        resultMap.put("items", commonDao.selectItemList(pItemVo));

        pItemVo.setItem_slct(2);
        resultMap.put("particles", commonDao.selectItemList(pItemVo));

        if(deviceVo.getOs() == 1 || deviceVo.getOs() == 2){
            AppVersionVo versionVo = commonDao.selectAppVersion(deviceVo.getOs());
            resultMap.put("version", versionVo.getVersion());
            log.debug(new Gson().toJson(versionVo));
            log.debug(new Gson().toJson(deviceVo));
            if(versionVo.getUpBuildNo() != null && !DalbitUtil.isEmpty(deviceVo.getAppBuild())){
                try{
                    resultMap.put("isForce", (versionVo.getUpBuildNo() > Long.parseLong(deviceVo.getAppBuild())));
                }catch(Exception e){
                    resultMap.put("isForce", false);
                }
            }

            if(deviceVo.getOs() == 2){
                resultMap.put("storeUrl", "itms-apps://itunes.apple.com/us/app/id1490208806?l=ko&ls=1");
                //resultMap.put("storeUrl", "itms-apps://itunes.apple.com/us/app/달빛-라이브-개인-라디오-방송-라이브-채팅-서비스/id1490208806?l=ko&ls=1");
                //resultMap.put("storeUrl", "https://apps.apple.com/us/app/%EB%8B%AC%EB%B9%9B-%EB%9D%BC%EC%9D%B4%EB%B8%8C-%EA%B0%9C%EC%9D%B8-%EB%9D%BC%EB%94%94%EC%98%A4-%EB%B0%A9%EC%86%A1-%EB%9D%BC%EC%9D%B4%EB%B8%8C-%EC%B1%84%ED%8C%85-%EC%84%9C%EB%B9%84%EC%8A%A4/id1490208806?l=ko&ls=1");
            }
            resultMap.put("isPayment", true);
        }

        //TODO - 추후 삭제
        if(DalbitUtil.isEmpty(request.getHeader("custom-header"))){
            resultMap.put("isForce", true);
        }

        return resultMap;
    }

    public HashMap<String, Object> getJwtTokenInfo(HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        Status resultStatus;
        boolean isLogin = DalbitUtil.isLogin(request);

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
            if(!DalbitUtil.isEmpty(headerToken) && !request.getRequestURI().startsWith("/member/logout")){
                log.info("check HeaderToken : " + headerToken);
                tokenVo = jwtUtil.getTokenVoFromJwt(headerToken);
                log.info("tokenVo : " + tokenVo);
            }
        }catch(GlobalException e){}

        if (isLogin) { //토큰의 회원번호가 탈퇴 했거나 정상,경고가 아닐 경우 로그아웃처리
            TokenCheckVo tokenCheckVo = memberDao.selectMemState(MemberVo.getMyMemNo(request));

            //다른 서버의 memNo가 넘어왔을 시 null이다.
            if(DalbitUtil.isEmpty(tokenCheckVo)){
                dbSelectMemNo = "88888888888888";
                isLogin = false;
                tokenVo = null;
            }else if(tokenCheckVo.getMem_state() < 1 && tokenCheckVo.getMem_state() > 2){
                dbSelectMemNo = tokenCheckVo.getMem_no();
                isLogin = false;
                tokenVo = null;
            }else{
                dbSelectMemNo = tokenCheckVo.getMem_no();
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

        if (!DalbitUtil.isEmpty(tokenVo) && !tokenVo.getAuthToken().equals(headerToken)) {
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

        return result;
    }

    public List<CodeVo> setData(List<Map> list, String type) {
        List<CodeVo> data = new ArrayList<>();
        if(!DalbitUtil.isEmpty(list)){
            for(int i = 0; i < list.size(); i++){
                if(type.equals(list.get(i).get("type"))){
                    if((int) list.get(i).get("is_use") == 1){
                        data.add(new CodeVo((String)list.get(i).get("value"), (String)list.get(i).get("code"), DalbitUtil.isEmpty(list.get(i).get("order"))? i : (int) list.get(i).get("order"), (int) list.get(i).get("is_use")));
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

    private List setFanVo(List memberFanVoList, String fanRank){

        HashMap map = new Gson().fromJson(fanRank, HashMap.class);
        MemberFanVo fanVo = new MemberFanVo();
        fanVo.setRank(memberFanVoList.size()+1);
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
    public String callMemberCertification(P_SelfAuthVo pSelfAuthVo) throws GlobalException{
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.callMemberCertification(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        procedureVo.setData(pSelfAuthVo);

        String result ="";
        if(procedureVo.getRet().equals(Status.본인인증성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증성공, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.본인인증_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증_회원아님));
        } else if(procedureVo.getRet().equals(Status.본인인증_중복.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증_중복));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증저장실패, procedureVo.getData()));
        }
        return result;
    }

    /**
     * 회원 본인 인증 여부 체크
     */
    public String getCertificationChk(P_SelfAuthChkVo pSelfAuthVo){
        ProcedureVo procedureVo = new ProcedureVo(pSelfAuthVo);
        commonDao.getCertificationChk(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.본인인증여부_확인.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증여부_확인));
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
     * 사이트 금지어 조회
     */
    public String banWordSelect(){
        BanWordVo banWordVo = commonDao.banWordSelect();
        return banWordVo.getBanWord();
    }

    /**
     * 방송 금지어 조회(개인)
     */
    public String broadcastBanWordSelect(BanWordVo banWordVo){
        BanWordVo resultVo = commonDao.broadcastBanWordSelect(banWordVo);
        return DalbitUtil.isEmpty(resultVo) ? null : resultVo.getBanWord();
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
}
