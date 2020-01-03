package com.demo.sample.controller;

import com.demo.common.code.ErrorStatus;
import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.common.vo.SampleVo;
import com.demo.member.service.MemberService;
import com.demo.member.vo.JoinVo;
import com.demo.member.vo.LoginVo;
import com.demo.sample.service.SampleService;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class SampleRestController {

    @Autowired
    SampleService sampleService;

    @Autowired
    MemberService memberService;

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    GsonUtil gsonUtil;

    /**
     * 권한 처리 테스트
     * USER, ADMIN 접근가능
     * @return
     */
    @ApiIgnore
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("rest")
    public String rest(){

        List<SampleVo> list = sampleService.getList();
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    /**
     * USER만 접근 가능
     * @return
     */
    @GetMapping("allowRest")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String allowRest(){
        List<SampleVo> list = sampleService.getList();
        list.addAll(list);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }


    /**
     * USER만 접근 가능
     * @return
     */
    @GetMapping("rejectNotUserRest")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String rejectNotUserRest(){
        List<SampleVo> list = sampleService.getList();

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    /**
     * ADMIN만 접근 가능
     * @return
     */
    @GetMapping("rejectNotAdminRest")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String rejectNotAdminRest(){
        List<SampleVo> list = sampleService.getList();
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    @GetMapping("errorTest")
    public String errorTest(){
        return gsonUtil.toJson(ErrorStatus.권한없음, null);
    }


    @GetMapping("errorTestData")
    public String errorTestData(){

        HashMap map = new HashMap();
        map.put("aa", "에이");
        map.put("bb", "비비");
        map.put("cc", "씨씨");

        return gsonUtil.toJson(ErrorStatus.잘못된호출, map);
    }

    @GetMapping("exception1")
    public String exception1(){

        SampleVo sampleVo = new SampleVo();
        sampleVo.setId("transaction");
        sampleVo.setName("테스트");
        sampleVo.setAge(20);

        sampleService.transactionTest(sampleVo);

        return gsonUtil.toJson(ErrorStatus.권한없음, null);
    }

    /**
     * Swagger Sample
     * @param gubun
     * @param gubunNM
     * @param title
     * @return
     */
    @ApiOperation(value = "방송정보입니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "제목", required = false, dataType = "string", paramType = "query", defaultValue = "asfasf"),

            @ApiImplicitParam(name = "gubun", value = "방송종류", required = true, dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "gubunNM", value = "방송종류명", required = true, dataType = "string", paramType = "query", defaultValue = "2"),
            @ApiImplicitParam(name = "intro", value = "환영인사말", required = true, dataType = "string", paramType = "query", defaultValue = "3")

    })
    @PostMapping("brodTest")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String brodTest(@RequestParam(value = "gubun", required = true) String gubun,
                           @RequestParam(value = "gubunNM", required = true) String gubunNM,
                           @RequestParam(value = "title", required = true) String title,
                           @RequestParam(value = "intro", required = false) String intro){

        log.debug("gubun: {} ", gubun);
        log.debug("gubunNM: {} ", gubunNM);
        log.debug("title: {} ", title);
        log.debug("intro: {} ", intro);


        HashMap map = new HashMap();
        map.put("result", "success");
        map.put("gubun", gubun);
        map.put("gubunNM", gubunNM);
        map.put("bgImg", "방송배경이미지");
        map.put("title", title);
        map.put("intro", intro);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }


    /**
     *
     * @return
     */
    @ApiIgnore
    @GetMapping(value = "/v1.0")
    public String version1(){

        HashMap map = new HashMap();
        map.put("version", "1");

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    @ApiIgnore
    @GetMapping(value = {"/v2.0", "/v2.1"})
    public String version2(){

        HashMap map = new HashMap();
        map.put("version", "2");

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    @ApiIgnore
    @GetMapping(value = "/null")
    public String errorNull(){

        HashMap map = new HashMap();
        map.put("value", Integer.parseInt("asd"));

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("nick")
    public String sp_checkDuplicateNickName(ProcedureVo procedureVo){
        procedureVo.setData("NickName");
        memberService.callNickNameCheck(procedureVo);
        log.info("sp_checkDuplicateNickName: {}", procedureVo.getRet());
        log.info("sp_checkDuplicateNickName: {}", procedureVo.getExt());

        if(0 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임중복, procedureVo.getData()));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임사용가능, procedureVo.getData()));
        }

    }

    @GetMapping("loginTest")
    public String sp_member_login(){

        LoginVo apiSample = LoginVo.builder().build();

        ProcedureVo procedureVo = memberService.callMemberLogin(apiSample);
        log.info("sp_member_login: {}", procedureVo.getRet());
        log.info("sp_member_login: {}", procedureVo.getExt());

        if(0 == procedureVo.getRet()) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인, procedureVo.getData()));
        }else if (1 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.회원가입필요, procedureVo.getData()));
        }else if (-1 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.패스워드틀림, procedureVo.getData()));
        }else if (-2 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData()));
        }else {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패, procedureVo.getData()));
        }
    }

    @GetMapping("joinTest")
    public String sp_member_join(){

        JoinVo apiSample = JoinVo.builder().build();

        ProcedureVo procedureVo = memberService.callMemberJoin(apiSample);
        log.info("sp_member_join: {}", procedureVo.getRet());
        log.info("sp_member_join: {}", procedureVo.getExt());

        if(0 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.회원가입, procedureVo.getData()));
        }else if (-1 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.중복가입, procedureVo.getData()));
        }else if (-2 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임중복, procedureVo.getData()));
        }else if (-3 == procedureVo.getRet()){
            return gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData()));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류, procedureVo.getData()));
        }
    }

    @GetMapping("log")
    public String selectLogData(HttpServletRequest request, HttpServletResponse response){
        //List<SampleVo> list = sampleService.selectLogData();

        HashMap data = new HashMap();
        data.put("스크립트", "<script>alert();</script>");
        data.put("DIV", "<div>12345</div>");
        data.put("TAG", request.getParameter("tag"));

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, data));
        log.debug("selectLogData {}", result);

        return result;
    }

    @GetMapping(value = "/db")
    public String procedureCallTest(){

        sampleService.getCount();

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, null));
    }

    @GetMapping(value = "/jwt")
    public String jwtTokenSample(){

        //UserVo loginUserVo = UserVo.getUserInfo();

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, null));
    }
}
