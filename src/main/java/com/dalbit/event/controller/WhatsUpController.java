package com.dalbit.event.controller;

import com.dalbit.event.service.WhatsUpService;
import com.dalbit.event.vo.request.WhatsUpRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event/whatsUp")
@Scope("prototype")
public class WhatsUpController {

    @Autowired
    WhatsUpService whatsUpService;

    @PostMapping("/getDjList")
    public Object getDjList(@RequestBody WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request){
        return whatsUpService.getWhatsUpDjList(whatsUpRequestVo, request);
    }
    @PostMapping("/getDjSel")
    public Object getDjSel(@RequestBody WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request){
        return whatsUpService.getWhatsUpDjSel(whatsUpRequestVo, request);
    }
    @PostMapping("/getNewMemberList")
    public Object getNewMemberList(@RequestBody WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request){
        return whatsUpService.getWhatsUpNewMemberList(whatsUpRequestVo, request);
    }
    @PostMapping("/getNewMemberSel")
    public Object getNewMemberSel(@RequestBody WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request){
        return whatsUpService.getWhatsUpNewMemberSel(whatsUpRequestVo, request);
    }
}
