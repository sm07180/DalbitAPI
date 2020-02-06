package com.dalbit.common.service;

import com.dalbit.broadcast.vo.P_RoomJoinTokenVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.CodeVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.LocationVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.LoginUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommonService {

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    @Autowired
    MemberService memberService;

    @Autowired
    CommonDao commonDao;

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    LoginUtil loginUtil;

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
            throw new GlobalException(Status.방송참여토큰_해당방이없음, procedureVo.getData());
        } else if (procedureVo.getRet().equals(Status.방송참여토큰_방장이없음.getMessageCode())) {
            throw new GlobalException(Status.방송참여토큰_방장이없음, procedureVo.getData());
        } else if(procedureVo.getRet().equals(Status.방송참여토큰발급_실패.getMessageCode())){
            throw new GlobalException(Status.방송참여토큰발급_실패, procedureVo.getData());
        }

        boolean isTokenSuccess = procedureVo.getRet().equals(Status.방송참여토큰발급.getMessageCode());
        resultMap.put("isTokenSuccess", isTokenSuccess);
        resultMap.put("tokenFailData", tokenFailData);

        log.debug("방송방 참여 토큰발급 결과 [{}]: {}", isTokenSuccess, tokenFailData);

        return resultMap;
    }

    @Cacheable(cacheNames = "codeCache", key = "#code")
    public HashMap getCodeCache(String code) {
        return callCodeDefineSelect();
    }

    public HashMap<String, Object> getJwtTokenInfo(HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        TokenVo tokenVo = null;
        Status resultStatus = null;
        boolean isLogin = DalbitUtil.isLogin();

        DeviceVo deviceVo = new DeviceVo(request);
        int os = deviceVo.getOs();
        String deviceId = deviceVo.getDeviceUuid();
        String deviceToken = deviceVo.getDeviceToken();
        String appVer = deviceVo.getAppVersion();
        String appAdId = deviceVo.getAdId();
        LocationVo locationVo = DalbitUtil.getLocation(request);

        if(isLogin){
            tokenVo = new TokenVo(jwtUtil.generateToken(MemberVo.getMyMemNo(), isLogin), MemberVo.getMyMemNo(), isLogin);
            resultStatus = Status.로그인성공;

        }else {

            P_LoginVo pLoginVo = new P_LoginVo("a", os, deviceId, deviceToken, appVer, appAdId, locationVo.getRegionName());
            ProcedureVo procedureVo = memberService.callMemberLogin(pLoginVo);
            if(procedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
                resultStatus = Status.로그인실패_회원가입필요;
            }else if(procedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
                resultStatus = Status.로그인실패_패스워드틀림;
            }else if(procedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
                resultStatus = Status.로그인실패_파라메터이상;
            }else if(procedureVo.getRet().equals(Status.로그인성공.getMessageCode())) {
                HashMap map = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                String memNo = DalbitUtil.getStringMap(map,"mem_no");
                if(memNo == null){
                    resultStatus = Status.로그인실패_파라메터이상;
                }else{
                    resultStatus = Status.로그인성공;
                    tokenVo = new TokenVo(jwtUtil.generateToken(memNo, isLogin), memNo, isLogin);
                    result.put("tokenVo", tokenVo);
                    memberService.refreshAnonymousSecuritySession(memNo);
                }
            }else{
                resultStatus = Status.로그인실패_파라메터이상;
            }
        }

        if(tokenVo != null){
            //세션 업데이트 프로시저 호출
            P_MemberSessionUpdateVo pMemberSessionUpdateVo = new P_MemberSessionUpdateVo(
                    isLogin ? 1 : 0
                    , tokenVo.getMemNo()
                    , os
                    , appAdId
                    , deviceId
                    , deviceToken
                    , appVer
                    , locationVo.getRegionName()
                    , deviceVo.getIp()
            );
            memberService.callMemberSessionUpdate(pMemberSessionUpdateVo);
        }

        result.put("tokenVo", tokenVo);
        result.put("Status", resultStatus);
        return result;
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
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                if(type.equals((String)list.get(i).get("type"))){
                    data.add(new CodeVo((String)list.get(i).get("value"), (String)list.get(i).get("code"), i));
                }
            }
        }
        return data;
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
        fanVo.setProfImg(new ImageVo(DalbitUtil.isNullToString(map.get("profileImage")), DalbitUtil.getProperty("server.photo.url")));

        memberFanVoList.add(fanVo);

        return memberFanVoList;
    }
}
