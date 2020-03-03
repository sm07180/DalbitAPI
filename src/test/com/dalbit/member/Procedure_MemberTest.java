package com.dalbit.member;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.procedure.P_JoinVo;
import com.dalbit.member.vo.procedure.P_LoginVo;
import com.dalbit.util.GsonUtil;
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
        P_LoginVo apiSample = new P_LoginVo();

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
            log.info(gsonUtil.toJson(new JsonOutputVo(Status.로그인오류, procedureVo.getData())));
        }

        Assert.assertEquals(Status.로그인성공.getMessageCode(), procedureVo.getRet());
    }

    @Test
    public void 회원가입()throws GlobalException{

        P_JoinVo joinVo = new P_JoinVo();

        memberService.signup(joinVo);
    }

    @Test
    public void 닉네임중복체크(){
        ProcedureVo procedureVo = new ProcedureVo();
        procedureVo.setData("test0005");

        String result = memberService.callNickNameCheck(procedureVo);

        log.debug("닉네임중복체크 결과 : {}", result);

    }


//    @Test
//    public void 비밀번호변경(){
//        String result = memberService.callChangePassword(P_ChangePasswordVo.builder().build());
//        log.debug("비밀번호 변경 결과 : {}", result);
//    }

//    @Test
//    public void 프로필편집(){
//
//        String result = memberService.callProfileEdit(P_ProfileEditVo.builder().build());
//        log.debug("프로필편집 결과 : {}", result);
//    }
//
//    @Test
//    public void 회원팬등록(){
//
//        String result = memberService.callFanstarInsert(P_FanstarInsertVo.builder().build());
//        log.debug("회원팬등록 결과 : {}", result);
//    }
//
//    @Test
//    public void 회원팬해제() {
//
//        String result = memberService.callFanstarDelete(P_FanstarDeleteVo.builder().build());
//        log.debug("회원팬해제 결과 : {}", result);
//    }

    @Test
    public void 회원정보보기() throws GlobalException {

        /*P_InfoVo apiSample = P_InfoVo.builder().build();
        String result = memberService.getMemberInfo(apiSample);

        log.debug("회원정보보기 결과 : {}", result);*/

    }
}
