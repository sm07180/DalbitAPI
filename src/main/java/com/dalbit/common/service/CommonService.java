package com.dalbit.common.service;

import com.dalbit.broadcast.vo.P_RoomJoinTokenVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.CodeVo;
import com.dalbit.common.vo.LocationVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.P_LoginVo;
import com.dalbit.member.vo.P_MemberSessionUpdateVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.LoginUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        String customHeader = request.getHeader("custom-header");
        int os = DalbitUtil.convertRequestParamToInteger(request,"os");
        String deviceId = DalbitUtil.convertRequestParamToString(request,"deviceId");
        String deviceToken = DalbitUtil.convertRequestParamToString(request,"deviceToken");
        String appVer = DalbitUtil.convertRequestParamToString(request,"appVer");
        String appAdId = DalbitUtil.convertRequestParamToString(request,"appAdId");
        if(!DalbitUtil.isEmpty(customHeader)){
            HashMap<String, String> headers = new Gson().fromJson(customHeader, HashMap.class);
            if(headers.get("os") != null && ("1".equals(headers.get("os")) || "2".equals(headers.get("os"))) && headers.get("deviceId") != null && headers.get("appVer") != null ){
                os = DalbitUtil.getIntMap(headers, "os");
                deviceId = DalbitUtil.getStringMap(headers, "deviceId");
                deviceToken = DalbitUtil.getStringMap(headers, "deviceToken");
                appVer = DalbitUtil.getStringMap(headers, "appVer");
                appAdId = DalbitUtil.getStringMap(headers, "appAdId");

                deviceToken = DalbitUtil.isEmpty(deviceToken) ? "" : deviceToken;
                appAdId = DalbitUtil.isEmpty(appAdId) ? "" : appAdId;
            }
        }
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
}
