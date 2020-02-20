package com.dalbit.member.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.vo.ConnectRoomVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class MemberService {

    @Autowired
    MemberDao memberDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    RestService restService;

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
    public ProcedureVo signup(P_JoinVo pLoginVo) throws GlobalException {

        String profImg = pLoginVo.getProfileImage();
        Boolean isDone = false;
        if(profImg.startsWith(Code.포토_프로필_임시_PREFIX.getCode())){
            isDone = true;
        }
        pLoginVo.setProfileImage(DalbitUtil.replacePath(profImg));
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberJoin(procedureVo);

        if(isDone){
            //TODO - 이미지 서버 오류 시 처리 -> GlobalException으로 throw
            restService.imgDone(DalbitUtil.replaceDonePath(pLoginVo.getProfileImage()));
        }

        return procedureVo;
    }

    /**
     * 닉네임 중복체크
     */
    public String callNickNameCheck(ProcedureVo procedureVo) {

        memberDao.callNickNameCheck(procedureVo);

        log.debug("닉네임중복체크 결과 : {}", procedureVo.toString());

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
     * @param memNo
     */
    public void refreshAnonymousSecuritySession(String memNo){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                memNo
                , ""
                , authorities);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }


    /**
     * 세션 업데이트
     */
    public void callMemberSessionUpdate(P_MemberSessionUpdateVo pMemberSessionUpdateVo){
        ProcedureVo procedureVo = new ProcedureVo(pMemberSessionUpdateVo);
        memberDao.callMemberSessionUpdate(procedureVo);

        log.debug("세션 업데이트 결과: {}", procedureVo.toString());
    }

}
