package com.dalbit.member.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.procedure.P_RoomExitVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.procedure.P_SelfAuthVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.vo.ConnectRoomVo;
import com.dalbit.member.vo.ExchangeSuccessVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenCheckVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.ExchangeReApplyVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.AES;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class MemberService {

    @Autowired
    MemberDao memberDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;
    @Autowired
    RoomDao roomDao;
    @Autowired
    SocketService socketService;
    @Autowired
    RoomService roomService;

    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    public ProcedureOutputVo callMemberLogin(P_LoginVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        List<ConnectRoomVo> connectRoomList =  memberDao.callMemberLogin(procedureVo);
        return new ProcedureOutputVo(procedureVo, connectRoomList);
    }

    /**
     * 회원 가입
     */
    public ProcedureVo signup(P_JoinVo pLoginVo, HttpServletRequest request) throws GlobalException {

        String profImg = pLoginVo.getProfileImage();
        Boolean isDone = false;
        if(profImg.startsWith(Code.포토_프로필_임시_PREFIX.getCode())){
            isDone = true;
        }
        pLoginVo.setProfileImage(DalbitUtil.replacePath(profImg));
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberJoin(procedureVo);

        if(isDone){
            restService.imgDone(DalbitUtil.replaceDonePath(pLoginVo.getProfileImage()), request);
        }

        return procedureVo;
    }

    /**
     * 닉네임 중복체크
     */
    public String callNickNameCheck(String nickNm) {

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(nickNm)){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크
        if(DalbitUtil.isStringMatchCheck(commonService.banWordSelect(), nickNm)){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임금지));
        }

        ProcedureVo procedureVo = new ProcedureVo(nickNm);
        memberDao.callNickNameCheck(procedureVo);

        String result;
        if(Status.닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임중복));
        }else if(Status.닉네임사용가능.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임사용가능));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임_파라메터오류));
        }
        return result;
    }

    /**
     * 비밀번호 변경
     */
    public String callChangePassword(P_ChangePasswordVo pChangePasswordVo){
        ProcedureVo procedureVo = new ProcedureVo(pChangePasswordVo.getPhoneNo(), pChangePasswordVo.getPassword());
        memberDao.callChangePassword(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.비밀번호변경성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경성공));
        } else if(procedureVo.getRet().equals(Status.비밀번호변경실패_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경실패_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경오류));
        }
        return result;
    }

    /**
     * 비회원 토큰 업데이트
     */
    public void refreshAnonymousSecuritySession(String memNo){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(memNo, "", authorities);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }


    /**
     * 세션 업데이트
     */
    public void callMemberSessionUpdate(P_MemberSessionUpdateVo pMemberSessionUpdateVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberSessionUpdateVo);
        memberDao.callMemberSessionUpdate(procedureVo);
        //log.debug("세션 업데이트 결과: {}", procedureVo.toString());
    }


    /**
     * 회원탈퇴
     */
    public String callMemberWithdrawal(P_WithdrawalVo pWithdrawalVo) {
        ProcedureVo procedureVo = new ProcedureVo(pWithdrawalVo);
        memberDao.callMemberWithdrawal(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.회원탈퇴_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원탈퇴_성공));
        } else if(procedureVo.getRet().equals(Status.회원탈퇴_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원탈퇴_회원아님));
        } else if(procedureVo.getRet().equals(Status.회원탈퇴_이미탈퇴.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원탈퇴_이미탈퇴));
        } else if(procedureVo.getRet().equals(Status.회원탈퇴_방접속중.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원탈퇴_방접속중));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원탈퇴_실패));
        }
        return result;
    }


    /**
     * 회원 환전 계산
     */
    public String callExchangeCalc(P_ExchangeVo pExchangeVo) {
        ProcedureVo procedureVo = new ProcedureVo(pExchangeVo);
        memberDao.callExchangeCalc(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("basicCash", DalbitUtil.getIntMap(resultMap, "basicCash"));       //환전예상금액
        returnMap.put("benefitCash", DalbitUtil.getIntMap(resultMap, "benefitCash"));   //스페셜DJ혜택
        returnMap.put("taxCash", DalbitUtil.getIntMap(resultMap, "taxCash"));           //원천징수세액
        returnMap.put("feeCash", DalbitUtil.getIntMap(resultMap, "feeCash"));           //이체수수료
        returnMap.put("realCash", DalbitUtil.getIntMap(resultMap, "realCash"));         //환전실수령액
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.환전계산성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전계산성공, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.환전계산_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전계산_회원아님));
        } else if(procedureVo.getRet().equals(Status.환전계산_별체크.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전계산_별체크));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전계산실패));
        }
        return result;

    }


    /**
     *  회원 환전 신청
     */
    public String callExchangeApply(P_ExchangeApplyVo pExchangeApplyVo, HttpServletRequest request) throws GlobalException {
        String exchangeFile1 = pExchangeApplyVo.getAdd_file1();
        String exchangeFile2 = pExchangeApplyVo.getAdd_file2();
        String exchangeFile3 = pExchangeApplyVo.getAdd_file3();
        Boolean isDone = false;
        if(!DalbitUtil.isEmpty(exchangeFile1) && exchangeFile1.startsWith(Code.포토_환전신청_임시_PREFIX.getCode())){
            isDone = true;
            exchangeFile1 = DalbitUtil.replacePath(exchangeFile1);
        }

        if(!DalbitUtil.isEmpty(exchangeFile2) && exchangeFile2.startsWith(Code.포토_환전신청_임시_PREFIX.getCode())){
            isDone = true;
            exchangeFile2 = DalbitUtil.replacePath(exchangeFile2);
        }

        if(!DalbitUtil.isEmpty(exchangeFile3) && exchangeFile3.startsWith(Code.포토_환전신청_임시_PREFIX.getCode())){
            isDone = true;
            exchangeFile3 = DalbitUtil.replacePath(exchangeFile3);

            //법정대리인 인증정보 파일 업데이트
            P_SelfAuthVo pSelfAuthVo = new P_SelfAuthVo();
            pSelfAuthVo.setMem_no(MemberVo.getMyMemNo(request));
            pSelfAuthVo.setAdd_file(exchangeFile3);
            int success = commonService.updateMemberCertificationFile(pSelfAuthVo);
            if(success > 0){
                log.info("법정대리인(보호자) 서류 업데이트 성공");
            }
        }

        pExchangeApplyVo.setAdd_file1(exchangeFile1);
        pExchangeApplyVo.setAdd_file2(exchangeFile2);

        ProcedureVo procedureVo = new ProcedureVo(pExchangeApplyVo);
        memberDao.callExchangeApply(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("byeol", DalbitUtil.getIntMap(resultMap, "byeol"));               //환전신청별
        returnMap.put("realCash", DalbitUtil.getIntMap(resultMap, "realCash"));         //환전실수령액
        returnMap.put("accountName", DalbitUtil.getStringMap(resultMap, "accountName"));   //예금주
        returnMap.put("bankCode", DalbitUtil.getStringMap(resultMap, "bankCode"));         //은행
        returnMap.put("accountNo", DalbitUtil.getStringMap(resultMap, "accountNo"));       //계좌번호
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.환전신청성공.getMessageCode())) {

            if(isDone){
                if(!DalbitUtil.isEmpty(pExchangeApplyVo.getAdd_file1())){
                    restService.imgDone(DalbitUtil.replaceDonePath(pExchangeApplyVo.getAdd_file1()), request);
                }
                if(!DalbitUtil.isEmpty(pExchangeApplyVo.getAdd_file2())){
                    restService.imgDone(DalbitUtil.replaceDonePath(pExchangeApplyVo.getAdd_file2()), request);
                }
                if(!DalbitUtil.isEmpty(pExchangeApplyVo.getAdd_file3())){
                    restService.imgDone(DalbitUtil.replaceDonePath(pExchangeApplyVo.getAdd_file3()), request);
                }
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청성공, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.환전신청_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_회원아님));
        } else if(procedureVo.getRet().equals(Status.환전신청_별체크.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_별체크));
        } else if(procedureVo.getRet().equals(Status.환전신청_예금주오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_예금주오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_은행코드오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_은행코드오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_계좌번호오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_계좌번호오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_주민번호오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_주민번호오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_전화번호오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_전화번호오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_주소1오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_주소1오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_첨부파일1오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_첨부파일1오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_첨부파일2오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_첨부파일2오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_동의오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_동의오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_별부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_별부족));
        } else if(procedureVo.getRet().equals(Status.환전신청_신청제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_신청제한));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청실패));
        }
        return result;

    }


    /**
     * 회원 환전 승인 건 조회
     */
    public String exchangeApprovalSelect(String memNo) {
        ExchangeSuccessVo exchangeSuccessVo = memberDao.exchangeApprovalSelect(memNo);

        String result;
        if(!DalbitUtil.isEmpty(exchangeSuccessVo)) {
            
            //2020.07.03 기존 승인 건 조회 시 정보 추가 (첨부파일 제외)
            HashMap returnMap = new HashMap();
            returnMap.put("exchangeIdx", exchangeSuccessVo.getExchangeIdx());
            returnMap.put("accountName", exchangeSuccessVo.getAccountName());
            returnMap.put("accountNo", exchangeSuccessVo.getAccountNo());
            returnMap.put("bankCode", exchangeSuccessVo.getBankCode());
            returnMap.put("socialNo", AES.decrypt(exchangeSuccessVo.getSocialNo(), DalbitUtil.getProperty("social.secret.key")).substring(0,6));
            returnMap.put("phoneNo", exchangeSuccessVo.getPhoneNo());
            returnMap.put("address1", exchangeSuccessVo.getAddress1());
            returnMap.put("address2", exchangeSuccessVo.getAddress2());

            result = gsonUtil.toJson(new JsonOutputVo(Status.환전승인조회성공, returnMap));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전승인조회없음));
        }
        return result;
    }


    /**
     * 환전 재신청
     */
    public String exchangeReapply(ExchangeReApplyVo exchangeReApplyVo, HttpServletRequest request) throws GlobalException {

        ExchangeSuccessVo exchangeSuccessVo = memberDao.exchangeReApprovalSelect(exchangeReApplyVo);
        P_ExchangeApplyVo pExchangeApplyVo = new P_ExchangeApplyVo();
        pExchangeApplyVo.setMem_no(exchangeSuccessVo.getMemNo());
        pExchangeApplyVo.setByeol(exchangeReApplyVo.getByeol());
        pExchangeApplyVo.setAccount_name(exchangeSuccessVo.getAccountName());
        pExchangeApplyVo.setAccount_no(exchangeSuccessVo.getAccountNo());
        pExchangeApplyVo.setBank_code(exchangeSuccessVo.getBankCode());
        pExchangeApplyVo.setSocial_no(exchangeSuccessVo.getSocialNo());
        pExchangeApplyVo.setPhone_no(exchangeSuccessVo.getPhoneNo());
        pExchangeApplyVo.setAddress1(exchangeSuccessVo.getAddress1());
        pExchangeApplyVo.setAddress2(exchangeSuccessVo.getAddress2());
        pExchangeApplyVo.setAdd_file1(exchangeSuccessVo.getAddFile1());
        pExchangeApplyVo.setAdd_file2(exchangeSuccessVo.getAddFile2());
        pExchangeApplyVo.setTerms_agree(exchangeSuccessVo.getTermsAgree());

        return callExchangeApply(pExchangeApplyVo, request);
    }

    public String resetListeningRoom(HttpServletRequest request) throws GlobalException{
        String memNo = request.getParameter("memNo");
        //String mode = request.getParameter("mode");
        if(DalbitUtil.isEmpty(memNo)){
            memNo = MemberVo.getMyMemNo(request);
        }

        if(DalbitUtil.isEmpty(memNo)) {
            return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류));
        }else{
            try{
                DeviceVo deviceVo = new DeviceVo(request);
                P_RoomExitVo exitData = new P_RoomExitVo();
                exitData.setMemLogin(1);
                exitData.setMem_no(memNo);
                exitData.setOs(deviceVo.getOs());
                exitData.setDeviceUuid(deviceVo.getDeviceUuid());
                exitData.setIp(deviceVo.getIp());
                exitData.setAppVersion(deviceVo.getAppVersion());
                exitData.setDeviceToken(deviceVo.getDeviceToken());
                exitData.setIsHybrid(deviceVo.getIsHybrid());
                String authToken = DalbitUtil.getAuthToken(request);

                List<String> roomList = memberDao.selectListeningRoom(memNo);
                for(String room_no : roomList){
                    if(deviceVo.getOs() == 3){
                        exitData.setRoom_no(room_no);
                        ProcedureVo procedureVo = new ProcedureVo(exitData);
                        roomDao.callBroadCastRoomExit(procedureVo);
                    }
                    try{
                        socketService.chatEndRed(memNo, room_no, request, authToken);
                    }catch(Exception e){}
                }
            }catch(Exception g1){
                return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류));
            }
        }

        return gsonUtil.toJson(new JsonOutputVo(Status.조회));
    }

    public TokenCheckVo selectMemState(String mem_no){
        return memberDao.selectMemState(mem_no);
    }
}
