package com.demo.member;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.member.service.MemberService;
import com.demo.member.vo.ChangePasswordVo;
import com.demo.member.vo.LoginVo;
import com.demo.member.vo.ProfileEditVo;
import com.demo.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class MemberTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private GsonUtil gsonUtil;

    @Test
    public void 로그인테스트(){
        log.debug("로그이넽스트");
    }

    @Test
    public void build테스트(){
        log.debug("ㅁ니ㅏ어림넝리ㅏㅓ밍러미어");
        log.debug(LoginVo.builder().build().toString());
    }

    @Test
    public void 닉네임중복체크(){
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setData("123123123");
        memberService.callNickNameCheck(procedureVo);

        log.info("닉네임 중복 체크 결과 : {}", procedureVo);
    }

    @Test
    public void 비밀번호변경_성공(){
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setBox(ChangePasswordVo.builder().build());

        memberService.callChangePassword(procedureVo);

        log.debug("비밀번호 변경 결과 : {}", procedureVo.toString());

        log.info(gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경성공, procedureVo.getData())));
    }

    @Test
    public void 비밀번호변경_실패(){
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setBox(ChangePasswordVo.builder().build());

        memberService.callChangePassword(procedureVo);

        log.debug("비밀번호 변경 결과 : {}", procedureVo.toString());

        log.info(gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경실패, procedureVo.getData())));

    }

    @Test
    public void 프로필편집_성공(){

        ProcedureVo procedureVo = new ProcedureVo(ProfileEditVo.builder().build());

        memberService.callProfileEdit(procedureVo);

        log.debug("프로필편집 결과 : {}", procedureVo.toString());
        log.info(gsonUtil.toJson(new JsonOutputVo(Status.프로필편집성공, procedureVo.getData())));

    }

    @Test
    public void 프로필편집_실패_회원아님(){
        ProcedureVo procedureVo = new ProcedureVo(ProfileEditVo.builder().mem_no("12345").build());

        memberService.callProfileEdit(procedureVo);

        log.debug("프로필편집 결과 : {}", procedureVo.toString());
        log.info(gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_회원아님, procedureVo.getData())));
    }

    @Test
    public void 프로필편집_실패_닉네임중복(){
        ProcedureVo procedureVo = new ProcedureVo(ProfileEditVo.builder().nickName("radio").build());

        memberService.callProfileEdit(procedureVo);

        log.debug("프로필편집 결과 : {}", procedureVo.toString());
        log.info(gsonUtil.toJson(new JsonOutputVo(Status.프로필편집실패_닉네임중복, procedureVo.getData())));
    }

    @Test
    public void 테스트(){
        String aa = "member.edit.profile.fail.notUser";
        log.debug("참?? : {}", aa.contains("fail") || aa.contains("error"));
    }

}