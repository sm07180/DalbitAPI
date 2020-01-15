package com.demo.member;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.member.service.MemberService;
import com.demo.member.vo.*;
import com.demo.util.CommonUtil;
import com.demo.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class Procedure_MemberTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private GsonUtil gsonUtil;

    @Test
    public void 회원_로그인(){
        P_LoginVo apiSample = P_LoginVo.builder().build();

        ProcedureVo procedureVo = memberService.callMemberLogin(apiSample);

        log.debug("회원_로그인 결과 : {}", procedureVo.toString());

        if(Status.로그인성공.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.로그인성공, procedureVo.getData())));

        }else if (Status.로그인실패_회원가입필요.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_회원가입필요, procedureVo.getData())));

        }else if (Status.로그인실패_패스워드틀림.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_패스워드틀림, procedureVo.getData())));

        }else if (Status.파라미터오류.equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData())));

        }else {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_패스워드틀림, procedureVo.getData())));
        }

        Assert.assertEquals(Status.로그인성공.getMessageCode(), procedureVo.getRet());
    }

    @Test
    public void 회원가입(){

        P_JoinVo joinVo = P_JoinVo.builder()
            .memSlct("p")
            .id("010-" + CommonUtil.randomValue("number", 4) + "-" + CommonUtil.randomValue("number", 4))
            .nickName("T_" + CommonUtil.randomValue("string", 6) + CommonUtil.randomValue("number", 2))
        .build();

        ProcedureVo procedureVo = memberService.callMemberJoin(joinVo);
        log.info("sp_member_join: {}", procedureVo.getRet());
        log.info("sp_member_join: {}", procedureVo.getExt());

        log.debug("회원가입 결과 : {}", procedureVo.toString());

        if(Status.회원가입성공.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공, procedureVo.getData())));

        }else if (Status.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입, procedureVo.getData())));

        }else if (Status.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_닉네임중복, procedureVo.getData())));

        }else if (Status.회원가입실패_파라미터오류.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData())));

        }else{
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류, procedureVo.getData())));
        }

        Assert.assertEquals(Status.회원가입성공.getMessageCode(), procedureVo.getRet());
    }

    @Test
    public void 닉네임중복체크(){
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setData("123123123");

        ProcedureVo resultProcedureVo = memberService.callNickNameCheck(procedureVo);

        log.debug("닉네임중복체크 결과 : {}", procedureVo.toString());

        if(Status.닉네임중복.getMessageCode().equals(resultProcedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.닉네임중복, procedureVo.getData())));

        }else if(Status.닉네임사용가능.getMessageCode().equals(resultProcedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.닉네임사용가능, procedureVo.getData())));
        }

        Assert.assertEquals(Status.닉네임사용가능.getMessageCode(), procedureVo.getRet());
    }


    @Test
    public void 비밀번호변경(){
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setBox(P_ChangePasswordVo.builder().build());

        memberService.callChangePassword(procedureVo);

        log.debug("비밀번호 변경 결과 : {}", procedureVo.toString());

        if(Status.비밀번호변경실패_회원아님.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경실패_회원아님, procedureVo.getData())));

        }else if(Status.비밀번호변경성공.getMessageCode().equals(procedureVo.getRet())){
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경성공, procedureVo.getData())));
        }

        Assert.assertEquals(Status.비밀번호변경성공.getMessageCode(), procedureVo.getRet());
    }

    @Test
    public void 프로필편집(){

        ProcedureVo procedureVo = new ProcedureVo(P_ProfileEditVo.builder().build());

        memberService.callProfileEdit(procedureVo);

        log.debug("프로필편집 결과 : {}", procedureVo.toString());

        if(Status.프로필편집성공.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.프로필편집성공, procedureVo.getData())));

        }else if(Status.프로필편집실패_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_회원아님, procedureVo.getData())));

        }else if(Status.프로필편집실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임중복, procedureVo.getData())));
        }

        Assert.assertEquals(Status.프로필편집성공.getMessageCode(), procedureVo.getRet());

    }

    @Test
    public void 회원팬등록(){

        ProcedureVo procedureVo = new ProcedureVo(P_FanstarInsertVo.builder().build());
        memberService.callFanstarInsert(procedureVo);

        log.debug("회원팬등록 결과 : {}", procedureVo.toString());

        if(Status.팬등록성공.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬등록성공, procedureVo.getData())));
        }else if(Status.팬등록_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬등록_회원아님, procedureVo.getData())));
        }else if(Status.팬등록_스타회원번호이상.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬등록_스타회원번호이상, procedureVo.getData())));
        }else if(Status.팬등록_이미팬등록됨.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬등록_이미팬등록됨, procedureVo.getData())));
        }else {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬등록실패, procedureVo.getData())));
        }
        //Assert.assertEquals(Status.팬등록성공.getMessageCode(), procedureVo.getRet());
    }

    @Test
    public void 회원팬해제(){

        ProcedureVo procedureVo = new ProcedureVo(P_FanstarDeleteVo.builder().build());
        memberService.callFanstarDelete(procedureVo);

        log.debug("회원팬해제 결과 : {}", procedureVo.toString());

        if(Status.팬해제성공.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬해제성공, procedureVo.getData())));
        }else if(Status.팬해제_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬해제_회원아님, procedureVo.getData())));
        }else if(Status.팬해제_스타회원번호이상.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬해제_스타회원번호이상, procedureVo.getData())));
        }else if(Status.팬해제_팬아님.getMessageCode().equals(procedureVo.getRet())) {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬해제_팬아님, procedureVo.getData())));
        }else {
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.팬해제실패, procedureVo.getData())));
        }
        //Assert.assertEquals(Status.팬해제성공.getMessageCode(), procedureVo.getRet());
    }

    @Test
    public void 회원정보보기(){

        P_InfoVo apiSample = P_InfoVo.builder().build();
        String result = memberService.callMemberInfoView(apiSample);

        log.debug("회원정보보기 결과 : {}", result);

    }
}