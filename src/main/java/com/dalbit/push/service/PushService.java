package com.dalbit.push.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushService {

    public void reqPushData(String memNo, String title, String contents, String roomNo, String targetMem) {

        try {

        } catch (Exception e) {
            log.error("PushService Error => {}", e);
        }
    }
}
