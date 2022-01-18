package com.dalbit.agora.sample;

import com.dalbit.agora.media.RtcTokenBuilder;
import com.dalbit.agora.media.RtcTokenBuilder.Role;

public class RtcTokenBuilderSample {
    static String appId = "6956f28ae2dc46d1a70fa5a449fbbc0c";
    static String appCertificate = "8405dfa59f2642a8bee67373ce5ad4ea";
    static String channelName = "7d72365eb983485397e3e3f9d460bdda";
    static String userAccount = "2082341273";
    static int uid = 2082341273;
    static int expirationTimeInSeconds = 3600; 

    public static void main(String[] args) throws Exception {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(appId, appCertificate,  
        		 channelName, userAccount, Role.Role_Publisher, timestamp);
        System.out.println(result);
        
        result = token.buildTokenWithUid(appId, appCertificate,  
       		 channelName, uid, Role.Role_Publisher, timestamp);
        System.out.println(result);
    }
}
