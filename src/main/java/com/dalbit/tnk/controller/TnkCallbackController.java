package com.dalbit.tnk.controller;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.tnk.service.TnkCallbackService;
import com.dalbit.tnk.vo.TnkCallbackVo;
import com.dalbit.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tnk")
@Scope("prototype")
public class TnkCallbackController {
    @Autowired
    TnkCallbackService tnkCallbackService;
    @Autowired
    private GsonUtil gsonUtil;

    @PostMapping("/{os}/callback")
    public String callback(TnkCallbackVo tnkCallbackVo, @PathVariable("os") String os, HttpServletResponse response) throws GlobalException {
        tnkCallbackService.callbackTnk(tnkCallbackVo, os, response);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회));
    }

    @PostMapping("/confirm")
    public String confirm(HttpServletRequest request) throws GlobalException {
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, tnkCallbackService.callTnkConfirm(request)));
    }
}
