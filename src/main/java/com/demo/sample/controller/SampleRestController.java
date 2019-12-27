package com.demo.sample.controller;

import com.demo.common.code.ErrorStatus;
import com.demo.common.code.Status;
import com.demo.sample.service.SampleService;
import com.demo.util.MessageUtil;
import com.demo.common.vo.SampleVo;
import com.demo.common.vo.JsonOutputVo;
import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class SampleRestController {

    @Autowired
    SampleService sampleService;

    @Autowired
    MessageUtil messageUtil;

    /**
     * 권한 처리 테스트
     * USER, ADMIN 접근가능
     * @return
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("rest")
    public String rest(){

        List<SampleVo> list = sampleService.getList();
        String result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, list)));

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

        String result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, list)));

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

        String result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, list)));

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
        String result = new Gson().toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    @GetMapping("errorTest")
    public String errorTest(){
        return new Gson().toJson(messageUtil.setExceptionInfo(ErrorStatus.권한없음, null));
    }


    @GetMapping("errorTestData")
    public String errorTestData(){

        HashMap map = new HashMap();
        map.put("aa", "에이");
        map.put("bb", "비비");
        map.put("cc", "씨씨");

        return new Gson().toJson(messageUtil.setExceptionInfo(ErrorStatus.잘못된호출, map));
    }

    @GetMapping("exception1")
    public String exception1(){

        SampleVo sampleVo = new SampleVo();
        sampleVo.setId("transaction");
        sampleVo.setName("테스트");
        sampleVo.setAge(20);

        sampleService.transactionTest(sampleVo);

        return new Gson().toJson(messageUtil.setExceptionInfo(ErrorStatus.권한없음, null));
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
            @ApiImplicitParam(name = "title", value = "제목", required = false, dataType = "string", paramType = "header", defaultValue = "asfasf"),

            @ApiImplicitParam(name = "gubun", value = "방송종류", required = true, dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "gubunNM", value = "방송종류명", required = true, dataType = "string", paramType = "path", defaultValue = "2"),
            @ApiImplicitParam(name = "intro", value = "환영인사말", required = true, dataType = "string", paramType = "path", defaultValue = "3")

    })
    @PostMapping("brodTest")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String brodTest(@RequestParam(value = "gubun", required = true) String gubun,
                           @RequestParam(value = "gubunNM", required = true) String gubunNM,
                           @RequestParam(value = "title", required = true) String title,
                           @RequestParam(value = "intro", required = false) String intro){

        HashMap map = new HashMap();
        map.put("result", "success");
        map.put("gubun", gubun);
        map.put("gubunNM", gubunNM);
        map.put("bgImg", "방송배경이미지");
        map.put("title", title);
        map.put("intro", intro);

        String result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));

        return result;
    }


    /**
     *
     * @return
     */
    @GetMapping(value = "/version", params = "v=1")
    public String version1(){

        HashMap map = new HashMap();
        map.put("version", "1");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }

    @GetMapping(value = "/version", params = "v=2")
    public String version2(){

        HashMap map = new HashMap();
        map.put("version", "2");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }

    @GetMapping(value = "/null")
    public String errorNull(){

        HashMap map = new HashMap();
        map.put("value", Integer.parseInt("asd"));

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }
}
