package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/broad/guest")
public class GuestController {
    @Autowired
    GuestService guestService;

    @PostMapping("/test")
    public String testGuest(HttpServletRequest request){
        return guestService.guestStream(request);
    }
}
