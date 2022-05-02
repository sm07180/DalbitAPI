package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/broad/owner")
@Scope("prototype")
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @PostMapping("/sel")
    public Object insVote(@RequestParam(value = "roomNo") String roomNo,
                          @RequestParam(value = "memNo") String memNo,
                          HttpServletRequest request){
        return ownerService.pDallaRoomMasterSel(roomNo, memNo, request);
    }
}
