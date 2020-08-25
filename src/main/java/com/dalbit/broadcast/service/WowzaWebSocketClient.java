package com.dalbit.broadcast.service;

import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
@Slf4j
public class WowzaWebSocketClient {
    Session userSession = null;
    private MessageHandler messageHandler;

    public WowzaWebSocketClient(URI endPointURI) {
        try{
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endPointURI);
        }catch(Exception e) {
            if("local".equals(DalbitUtil.getActiveProfile())){e.printStackTrace();}
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        log.debug("WOWZA WSS OPENDED");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        log.debug("WOWZA WSS RECEIVE MESSAGE : " + message);
        if(this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    public void close(){
        if(this.userSession != null){
            try {
                this.userSession.close();
            }catch(Exception e){}
            this.userSession = null;
        }
    }

    public void addMessageHandler(MessageHandler msgHandler){
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message){
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}
