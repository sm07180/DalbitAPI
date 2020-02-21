package com.dalbit.socket.service;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Slf4j
@Service
public class SocketService {
    @Value("${socket.ip}")
    private String SERVER_SOCKET_IP;

    @Value("${socket.port}")
    private String SERVER_SOCKET_PORT;

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    public Map<String, Object> sendSocketApi(String authToken, String url_path, String params, int method) {
        HttpURLConnection con = null;
        URL url = null;
        String method_str = "GET";
        String result = "";

        if(method == 1){
            method_str = "POST";
        }else if(method == 2){
            method_str = "DELETE";
        }else if(method == 3){
            method_str = "PUT";
        }

        params = StringUtils.defaultIfEmpty(params, "").trim();

        String request_uri = "https://" + SERVER_SOCKET_IP + ":" + SERVER_SOCKET_PORT + url_path;
        if(method != 1 && !"".equals(params)){
            request_uri += "?" + params;
        }

        try{
            url = new URL(request_uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method_str);
            con.setRequestProperty("x-custom-header", authToken);
            if(method == 1 && !"".equals(params)){
                //con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setAllowUserInteraction(true);

                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.write(params.getBytes("UTF-8"));
                out.flush();
            }

            result = readStream(con.getInputStream());
        }catch(IOException e){
            log.debug("Socket Rest {} error {}", request_uri, e.getMessage());
            if(con != null){
                result = readStream(con.getErrorStream());
            }

        }

        return new Gson().fromJson(result, Map.class);
    }

    /**
     * http 연결 읽기
     *
     * @param stream : 연결 스트림
     * @return
     * @throws GlobalException
     */
    private String readStream(InputStream stream){
        StringBuffer pageContents = new StringBuffer();
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader buff = new BufferedReader(reader);

            String pageContentsR = "";
            while((pageContentsR = buff.readLine()) != null){
                pageContents.append(pageContentsR);
                pageContents.append("\r\n");
            }
            reader.close();
            buff.close();

        }catch (Exception e){ }

        return pageContents.toString();
    }

    public Map<String, Object> sendMessage(HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo();
        String roomNo = DalbitUtil.convertRequestParamToString(request,"roomNo");
        String message = DalbitUtil.convertRequestParamToString(request,"message");
        String authToken = DalbitUtil.getAuthToken(request, SSO_HEADER_COOKIE_NAME);
        memNo = memNo == null ? "" : memNo.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(message) && !"".equals(authToken)){
            String socketUrl = "/api/chatroom/sendmessage/" + roomNo;
            StringBuffer params = new StringBuffer();
            params.append("command=");
            params.append("chat");
            params.append("&message=");
            params.append(message);
            params.append("&memNo=");
            params.append(memNo);
            params.append("&isFan=");
            params.append(true);
            params.append("&roomRole=");
            params.append(1);
            params.append("&roomRight=");
            params.append("111111111");
            params.append("&recvMemNo=");

            return sendSocketApi(authToken, socketUrl, params.toString(), 1);
        }

        return null;
    }
}
