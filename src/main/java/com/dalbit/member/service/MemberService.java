package com.dalbit.member.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.vo.ProImageInitVo;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.GuestInfoVo;
import com.dalbit.broadcast.vo.RoomShareLinkOutVo;
import com.dalbit.broadcast.vo.procedure.P_RoomExitVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_SelfAuthVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.ExchangeReApplyVo;
import com.dalbit.member.vo.request.SpecialDjHistoryVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.search.vo.RoomRecommandListOutVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.AES;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
import java.util.*;

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

    @Autowired
    AdminDao adminDao;

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
    public String callChangePassword(HttpServletRequest request, P_ChangePasswordVo pChangePasswordVo){
        ProcedureVo procedureVo = new ProcedureVo(pChangePasswordVo.getPhoneNo(), pChangePasswordVo.getPassword());
        memberDao.callChangePassword(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.비밀번호변경성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경성공));

            try{
                //회원정보수정 로그 쌓기 추가(cs요청)
                TokenCheckVo tokenCheckVo = memberDao.selectMemStateForPhone(pChangePasswordVo.getPhoneNo());
                var proImageInitVo = new ProImageInitVo();
                proImageInitVo.setMem_no(tokenCheckVo.getMem_no());
                proImageInitVo.setType(0);
                proImageInitVo.setEdit_contents("패스워드 변경 : " + pChangePasswordVo.getPassword());
                proImageInitVo.setOp_name(tokenCheckVo.getMem_userid());
                adminDao.insertProfileHistory(proImageInitVo);
            }catch(Exception e){
                log.error("MemberService - callChangePassword : 회원정보수정 로그 쌓기 오류");
            }

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
        /*} else if(procedureVo.getRet().equals(Status.환전신청_첨부파일1오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_첨부파일1오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_첨부파일2오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_첨부파일2오류));*/
        } else if(procedureVo.getRet().equals(Status.환전신청_동의오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_동의오류));
        } else if(procedureVo.getRet().equals(Status.환전신청_별부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_별부족));
        } else if(procedureVo.getRet().equals(Status.환전신청_신청제한.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.환전신청_신청제한));
        }else if(Status.이전작업대기중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이전작업대기중));
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

            try{
                P_ExchangeAccountAddVo addVo = new P_ExchangeAccountAddVo();
                addVo.setMem_no(memNo);
                addVo.setAccount_no(exchangeSuccessVo.getAccountNo());
                addVo.setAccount_name(exchangeSuccessVo.getAccountName());
                addVo.setBank_code(exchangeSuccessVo.getBankCode());

                List<CodeVo> codeVoList = commonService.selectExchangeBankCodeList(new CodeVo(Code.환전은행.getCode()));
                if(!DalbitUtil.isEmpty(codeVoList)){
                    for (int i=0; i < codeVoList.size(); i++){
                        if(codeVoList.get(i).getValue().equals(exchangeSuccessVo.getBankCode())){
                            addVo.setBank_name(codeVoList.get(i).getCdNm());
                        }
                    }
                }else{
                    addVo.setBank_name("은행코드 불일치");
                }

                callAccountAdd(addVo);
            }catch (Exception e){
                log.error("환전 승인건 저장 오류: {}", e);
            }

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
        if(exchangeSuccessVo.getAddFile1().startsWith(Code.포토_환전신청_임시_PREFIX.getCode()) || exchangeSuccessVo.getAddFile2().startsWith(Code.포토_환전신청_임시_PREFIX.getCode())){
            return gsonUtil.toJson(new JsonOutputVo(Status.환전신청_기존신청정보오류));
        }

        P_ExchangeApplyVo pExchangeApplyVo = new P_ExchangeApplyVo();
        pExchangeApplyVo.setMem_no(exchangeSuccessVo.getMemNo());
        pExchangeApplyVo.setByeol(exchangeReApplyVo.getByeol());
        pExchangeApplyVo.setAccount_name(!DalbitUtil.isEmpty(exchangeReApplyVo.getAccountName()) ? exchangeReApplyVo.getAccountName() : exchangeSuccessVo.getAccountName());
        pExchangeApplyVo.setAccount_no(!DalbitUtil.isEmpty(exchangeReApplyVo.getAccountNo()) ? exchangeReApplyVo.getAccountNo() : exchangeSuccessVo.getAccountNo());
        pExchangeApplyVo.setBank_code(!DalbitUtil.isEmpty(exchangeReApplyVo.getBankCode()) ? exchangeReApplyVo.getBankCode() : exchangeSuccessVo.getBankCode());
        pExchangeApplyVo.setSocial_no(exchangeSuccessVo.getSocialNo());
        pExchangeApplyVo.setPhone_no(exchangeSuccessVo.getPhoneNo());
        pExchangeApplyVo.setAddress1(exchangeSuccessVo.getAddress1());
        pExchangeApplyVo.setAddress2(exchangeSuccessVo.getAddress2());
        pExchangeApplyVo.setAdd_file1(exchangeSuccessVo.getAddFile1());
        pExchangeApplyVo.setAdd_file2(exchangeSuccessVo.getAddFile2());
        pExchangeApplyVo.setTerms_agree(exchangeSuccessVo.getTermsAgree());
        pExchangeApplyVo.setLatest_idx(exchangeSuccessVo.getExchangeIdx());
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

                List<GuestRoomInfoVo> guestRoomInfoVoList = memberDao.selectLiveListeningRoomInfo(memNo);
                for(GuestRoomInfoVo guestRoomInfoVo : guestRoomInfoVoList){
                    //게스트일 경우 소켓 발송 추가
                    if(guestRoomInfoVo.getGuestYn() == 1){
                        GuestInfoVo guestInfoVo = new GuestInfoVo();
                        guestInfoVo.setMode(6);
                        guestInfoVo.setMemNo(memNo);
                        guestInfoVo.setNickNm(guestRoomInfoVo.getMemNick());
                        guestInfoVo.setProfImg(new ImageVo(guestRoomInfoVo.getProfImg(), guestRoomInfoVo.getGender(), DalbitUtil.getProperty("server.photo.url")));
                        try{
                            socketService.sendGuest(memNo, guestRoomInfoVo.getRoomNo(), guestRoomInfoVo.getBjMemNo(), "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        }catch(Exception e){}
                    }

                    exitData.setRoom_no(guestRoomInfoVo.getRoomNo());
                    ProcedureVo procedureVo = new ProcedureVo(exitData);
                    roomDao.callBroadCastRoomExit(procedureVo);

                    try{
                        socketService.chatEndRed(memNo, guestRoomInfoVo.getRoomNo(), request, authToken);
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

    /**
     * 회원 팬랭킹 1,2,3 가져오기
     */
    public List fanRank3(P_FanRankVo pFanRankVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanRankVo);
        List<P_FanRankVo> fanRankVoList = memberDao.callFanRank3(procedureVo);

        List<FanRankVo> outVoList = new ArrayList<>();
        if(Integer.parseInt(procedureVo.getRet()) > 0) {

            if (!DalbitUtil.isEmpty(fanRankVoList)) {
                for (int i = 0; i < fanRankVoList.size(); i++) {
                    outVoList.add(new FanRankVo(fanRankVoList.get(i), i+1));
                }
            }

        }
        return outVoList;
    }


    /**
     * 본인인증 히스토리 내역 저장
     */
    public int callProfileEditHistory(P_SelfAuthVo pSelfAuthVo) {
        return memberDao.callProfileEditHistory(pSelfAuthVo);
    }

    /**
     * 환전 계좌 등록
     */
    public String callAccountAdd(P_ExchangeAccountAddVo pExchangeAccountAddVo) {
        int success;
        try{
            success = memberDao.callAccountAdd(pExchangeAccountAddVo);
        }catch (Exception e){
            log.error("계좌등록 오류: {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.계좌등록_실패));
        }

        String result;
        if(success > 0) {
            P_ExchangeAccountListVo apiData = new P_ExchangeAccountListVo();
            apiData.setMem_no(pExchangeAccountAddVo.getMem_no());
            result = callAccountListSelect(apiData, "add");
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.계좌등록_이미등록됨));
        }
        return result;
    }


    /**
     * 환전 계좌 수정
     */
    public String callAccountEdit(P_ExchangeAccountEditVo pExchangeAccountEditVo, HttpServletRequest request) {

        //기존 환전 신청 계좌조회 변경 불가
        P_ExchangeAccountListVo exchangeAccountListVo = new P_ExchangeAccountListVo(request);
        List<P_ExchangeAccountListVo> accountList = memberDao.selectExchangeHistory(exchangeAccountListVo);
        for (int i=0; i<accountList.size(); i++){
            if (pExchangeAccountEditVo.getBeforeAccountNo().equals(accountList.get(i).getAccountNo())){
                return gsonUtil.toJson(new JsonOutputVo(Status.계좌수정_불가));
            }
        }

        int success;
        try {
            success = memberDao.callAccountEdit(pExchangeAccountEditVo);
        }catch (Exception e){
            log.error("계좌수정 오류: {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.계좌수정_실패));
        }

        String result;
        if(success > 0) {
            P_ExchangeAccountListVo apiData = new P_ExchangeAccountListVo();
            apiData.setMem_no(pExchangeAccountEditVo.getMem_no());
            result = callAccountListSelect(apiData, "edit");
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.계좌수정_실패));
        }
        return result;
    }


    /**
     * 환전 계좌 삭제
     */
    public String callAccountDelete(P_ExchangeAccountDeleteVo pExchangeAccountDeleteVo, HttpServletRequest request) {

        //기존 환전 신청 계좌조회 변경 불가
        P_ExchangeAccountListVo exchangeAccountListVo = new P_ExchangeAccountListVo(request);
        List<P_ExchangeAccountListVo> accountList = memberDao.selectExchangeHistory(exchangeAccountListVo);
        for (int i=0; i<accountList.size(); i++){
            if (pExchangeAccountDeleteVo.getBeforeAccountNo().equals(accountList.get(i).getAccountNo())){
                return gsonUtil.toJson(new JsonOutputVo(Status.계좌삭제_불가));
            }
        }

        int success;
        try{
            success = memberDao.callAccountDelete(pExchangeAccountDeleteVo);
        }catch (Exception e){
            log.error("계좌삭제 오류: {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.계좌삭제_실패));
        }

        String result;
        if(success > 0) {
            P_ExchangeAccountListVo apiData = new P_ExchangeAccountListVo();
            apiData.setMem_no(pExchangeAccountDeleteVo.getMemNo());
            result = callAccountListSelect(apiData, "delete");
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.계좌삭제_실패));
        }
        return result;
    }


    /**
     * 환전 계좌 조회
     */
    public String callAccountListSelect(P_ExchangeAccountListVo pExchangeAccountListVo, String state) {
        List<P_ExchangeAccountListVo> accountListVo = memberDao.callAccountListSelect(pExchangeAccountListVo);

        String result;
        HashMap resultMap = new HashMap();
        if(!DalbitUtil.isEmpty(accountListVo)){
            List<ExchangeAccountListOutVo> outVoList = new ArrayList<>();
            for (int i = 0; i < accountListVo.size(); i++) {
                outVoList.add(new ExchangeAccountListOutVo(accountListVo.get(i)));
            }
            resultMap.put("list", outVoList);

            Status status;
            if("add".equals(state)) {
                status = Status.계좌등록_성공;
            }else if("edit".equals(state)) {
                status = Status.계좌수정_성공;
            }else if("delete".equals(state)) {
                status = Status.계좌삭제_성공;
            }else{
                status = Status.계좌조회_성공;
            }
            result = gsonUtil.toJson(new JsonOutputVo(status, resultMap));
        }else{
            resultMap.put("list", new ArrayList<>());
            result = gsonUtil.toJson(new JsonOutputVo(Status.계좌조회_없음, resultMap));
        }
        return result;
    }

    /**
     * 스페셜DJ 선정 이력(약력) 히스토리
     */
    public String getSpecialHistory(P_SpecialDjHistoryVo pSpecialDjHistoryVo, HttpServletRequest request){
        List<P_SpecialDjHistoryVo> specialHistoryListVo = memberDao.getSpecialHistory(pSpecialDjHistoryVo);

        String result;
        HashMap specialHistoryList = new HashMap();
        if(DalbitUtil.isEmpty(specialHistoryListVo)) {
            specialHistoryList.put("list", new ArrayList<>());
            specialHistoryList.put("specialDjCnt", 0);
            specialHistoryList.put("nickNm", "");
            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ선정내역조회_없음, specialHistoryList));

        }else {
            List<SpecialDjHistoryOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<specialHistoryListVo.size(); i++){
                outVoList.add(new SpecialDjHistoryOutVo(specialHistoryListVo.get(i)));
            }
            specialHistoryList.put("list", outVoList);
            specialHistoryList.put("specialDjCnt", specialHistoryListVo.size());
            specialHistoryList.put("nickNm", specialHistoryListVo.get(0).getMemNick());

            result = gsonUtil.toJson(new JsonOutputVo(Status.스페셜DJ선정내역조회_성공, specialHistoryList));
        }

        return result;
    }

    /**
     * 현재 스페셜DJ 선정여부 조회
     */
    public int getSpecialCnt(SpecialDjHistoryVo specialDjHistoryVo){
        P_SpecialDjHistoryVo apiData = new P_SpecialDjHistoryVo(specialDjHistoryVo);
        int resultCnt = memberDao.getSpecialCnt(apiData);
        return resultCnt;
    }


    /**
     * 스페셜DJ 선발 누적 가산점 조회
     */
    public String getSpecialPointList(P_SpecialPointListVo pSpecialPointListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pSpecialPointListVo);
        List<P_SpecialPointListVo> specialPointListVo = memberDao.getSpecialPointList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap specialPointOutList = new HashMap();
            List<SpecialPointListOutVo> outVoList = new ArrayList<>();
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            if(!DalbitUtil.isEmpty(specialPointListVo)) {
                for (int i = 0; i < specialPointListVo.size(); i++) {
                    outVoList.add(new SpecialPointListOutVo(specialPointListVo.get(i)));
                }
            }
            specialPointOutList.put("list", outVoList);
            specialPointOutList.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
            specialPointOutList.put("totalPoint", DalbitUtil.getDoubleMap(resultMap, "totalPoint"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.가산점조회_성공, specialPointOutList));
        }else if(Status.가산점조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.가산점조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.가산점조회_실패));
        }
        return result;
    }


    /**
     * 랭킹데이터 반영 ON/OFF
     */
    public String callRankSetting(P_MemberRankSettingVo pMemberRankSettingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberRankSettingVo);
        memberDao.callRankSetting(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.랭킹반영.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            Status status = null;
            if(pMemberRankSettingVo.getApply_ranking() == 1){
                status = Status.랭킹반영;
            }else{
                status = Status.랭킹미반영;
            }
            HashMap returnMap = new HashMap();
            returnMap.put("isRankData", DalbitUtil.getIntMap(resultMap, "apply_ranking") == 1 ? true : false);
            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        } else if(procedureVo.getRet().equals(Status.랭킹반영설정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹반영설정_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.랭킹반영설정_실패));
        }
        return result;
    }


    /**
     * 회원 알림받기 등록/해제
     */
    public String callRecvEdit(P_MemberReceiveVo pMemberReceiveVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberReceiveVo);
        memberDao.callRecvEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.알림_등록.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            Status status = null;
            if(pMemberReceiveVo.getAlertYn() == 1){
                status = Status.알림_등록;
            }else{
                status = Status.알림_해제;
            }
            HashMap returnMap = new HashMap();
            returnMap.put("isReceive", DalbitUtil.getIntMap(resultMap, "alertYn") == 1 ? true : false);
            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        } else if(procedureVo.getRet().equals(Status.알림_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림_실패));
        }
        return result;
    }


    /**
     * 회원 알림받기 삭제
     */
    public String callRecvDelete(P_MemberReceiveDeleteVo pMemberReceiveDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberReceiveDeleteVo);
        memberDao.callRecvDelete(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.알림회원삭제_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림회원삭제_성공));
        } else if(procedureVo.getRet().equals(Status.알림회원삭제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림회원삭제_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림회원삭제_실패));
        }
        return result;
    }


    /**
     * 알림받기 회원 조회
     */
    public String callRecvList(P_MemberReceiveListVo pMemberReceiveListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberReceiveListVo);
        List<P_MemberReceiveListVo> recvListVo = memberDao.callRecvList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap specialPointOutList = new HashMap();
            List<MemberReceiveListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(recvListVo)) {
                for (int i = 0; i < recvListVo.size(); i++) {
                    outVoList.add(new MemberReceiveListOutVo(recvListVo.get(i)));
                }
            }
            specialPointOutList.put("list", outVoList);
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림회원조회_성공, specialPointOutList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.알림회원조회_실패));
        }
        return result;
    }


    /**
     * 추천 DJ 목록 조회
     */
    public String callDjRecommendList(P_DjRecommendListVo pDjRecommendListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pDjRecommendListVo);
        List<P_DjRecommendListVo> djRecommendListVo = memberDao.callDjRecommendList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap djRecommendOutList = new HashMap();
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            List<DjRecommendListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(djRecommendListVo)) {
                for (int i = 0; i < djRecommendListVo.size(); i++) {
                    outVoList.add(new DjRecommendListOutVo(djRecommendListVo.get(i)));
                }
            }
            djRecommendOutList.put("list", outVoList);
            //djRecommendOutList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
            result = gsonUtil.toJson(new JsonOutputVo(Status.추천DJ목록조회_성공, djRecommendOutList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.추천DJ목록조회_실패));
        }
        return result;
    }


    /**
     * 회원 이미지 신고
     */
    public String callReportImage(P_ReportImageVo pReportImageVo) {
        ProcedureVo procedureVo = new ProcedureVo(pReportImageVo);
        memberDao.callReportImage(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.이미지신고_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_성공));
        } else if(procedureVo.getRet().equals(Status.이미지신고_요청회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_요청회원아님));
        } else if(procedureVo.getRet().equals(Status.이미지신고_대상회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_대상회원아님));
        } else if(procedureVo.getRet().equals(Status.이미지신고_이미신고.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_이미신고));
        } else if(procedureVo.getRet().equals(Status.이미지신고_방번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_방번호없음));
        } else if(procedureVo.getRet().equals(Status.이미지신고_이미지번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_이미지번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.이미지신고_실패));
        }
        return result;
    }
}
