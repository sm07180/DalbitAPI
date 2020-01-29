package com.dalbit.common.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommonController {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

    @GetMapping("/splash")
    public String getSplash(){
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.getCodeCache("splash")));
    }

    @PostMapping("/splash")
    public String updateSplash(){
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.updateCodeCache("splash")));
    }
}
