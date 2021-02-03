package com.dalbit.broadcast.service;

import com.dalbit.util.DalbitUtil;
import com.dalbit.util.OkHttpClientUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;

@Slf4j
@Service
public class WowzaSocketService {

    @Value("${wowza.audio.disconnect.edge}")
    String[] WOWZA_AUDIO_DISCONNECT;
    @Value("${wowza.video.disconnect.edge}")
    String[] WOWZA_VIDEO_DISCONNECT;
    @Value("${wowza.aac}")
    String WOWZA_ACC;
    @Value("${wowza.opus}")
    String WOWZA_OPUS;

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

    @Async("threadTaskExecutor")
    public void wowzaDisconnect(String mediaType, String streamName, String gstStreamName){
        OkHttpClientUtil httpUtil = new OkHttpClientUtil();
        try{
            String[] disconnect = "a".equals(mediaType) ? WOWZA_AUDIO_DISCONNECT : WOWZA_VIDEO_DISCONNECT;
            Response res;
            Response gstRes;
            String[] edges = {WOWZA_ACC, WOWZA_OPUS};
            for(String server : disconnect) {
                for(String edge : edges){
                    String url = server + "?appName=edge&streamName=" + streamName + edge;
                    log.info("[WOWZA BJ DISCONNECT] Request URL : {}", url );
                    /*RequestBody formBody = new FormBody.Builder()
                            .add("appName", "edge")
                            .add("streamName", streamName)
                            .build();
                    Response res = httpUtil.sendPost(server, formBody);*/
                    res = httpUtil.sendGet(url);
                    if(res != null){
                        String strResBody = res.body().string();
                        if(!DalbitUtil.isEmpty(strResBody)) {
                            log.info("resMap : {}", strResBody);
                            //HashMap resMap = new Gson().fromJson(strResBody, HashMap.class);
                            //log.info("resMap : {}", resMap);
                        }
                    }
                }

                if(!DalbitUtil.isEmpty(gstStreamName)){
                    for(String edge : edges) {
                        String url = server + "?appName=edge&streamName=" + gstStreamName + edge;
                        log.info("[WOWZA GUEST DISCONNECT] Request URL : {}", url);
                        gstRes = httpUtil.sendGet(url);

                        if (gstRes != null) {
                            String strResBody = gstRes.body().string();
                            if (!DalbitUtil.isEmpty(strResBody)) {
                                log.info("resMap : {}", strResBody);
                                //HashMap gstResMap = new Gson().fromJson(strResBody, HashMap.class);
                                //log.info("gstResMap : {}", gstResMap);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
