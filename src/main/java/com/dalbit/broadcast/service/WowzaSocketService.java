package com.dalbit.broadcast.service;

import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;

@Slf4j
@Service
public class WowzaSocketService {

    @Async("threadTaskExecutor")
    public void sendFirstEdge(String svrDomain, String streamName) throws Exception{
        WowzaWebSocketClient  client = new WowzaWebSocketClient(new URI("wss://" + svrDomain + "/" + DalbitUtil.getProperty("wowza.wss.url.endpoint")));
        client.addMessageHandler(new WowzaWebSocketClient.MessageHandler() {
            @Override
            public void handleMessage(String message) {
                /*log.debug("receive WOWZA message : " + message);
                HashMap result = new Gson().fromJson(message, HashMap.class);
                if(result != null && result.containsKey("status") && "200".equals(((Double)result.get("status")).toString())) {
                    //if("getOffer".equals((String))){

                    //}
                }else if(result != null && result.containsKey("status") && "504".equals(((Double)result.get("status")).toString())){
                }else{
                }*/
                client.close();
            }
        });

        HashMap message = new HashMap();
        HashMap streamInfo = new HashMap();
        streamInfo.put("applicationName", "edge");
        streamInfo.put("sessionId", "");
        streamInfo.put("streamName", streamName + "_opus");
        message.put("command", "getOffer");
        message.put("direction", "play");
        message.put("streamInfo", streamInfo);
        String sendMessage = new Gson().toJson(message);
        client.sendMessage(sendMessage);
        log.debug("WOWZA SEND MESSAGE : " + sendMessage);
    }
}
