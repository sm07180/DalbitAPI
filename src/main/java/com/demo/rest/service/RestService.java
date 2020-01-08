package com.demo.rest.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RestService {
    @Value("${server.photo.url}")
    private String photoServer;

    @Value("${server.ant.url}")
    private String antServer;

    @Value("${ant.expire.hour}")
    private int antExpire;

    @Value("${ant.app.name}")
    private String antName;

    /**
     * Rest API 호출
     *
     * @param server_url : 서버
     * @param url_path : 호출URL (도메인 이후)
     * @param params : 파라메터
     * @param method : 메쏘드 (0 : GET(DEFALT), 1 : POST, 2 : DELETE)
     * @return String : 리턴된 JSON 스트링
     * @throws Exception
     */
    public Map<String, Object> callRest(String server_url, String url_path, HashMap<String, String> params, int method) throws Exception{
        StringBuffer paramString = new StringBuffer();
        if(params != null){
            for(Map.Entry<String, String> e :  params.entrySet()){
                if(paramString.length() > 0){
                    paramString.append("&");
                }
                paramString.append(URLEncoder.encode(e.getKey(), "UTF-8"));
                paramString.append("=");
                paramString.append(URLEncoder.encode(e.getValue(), "UTF-8"));
            }
        }

        return callRest(server_url, url_path, paramString.toString(), method);
    }

    /**
     * Rest API 호출
     *
     * @param server_url : 서버
     * @param url_path : 호출URL (도메인 이후)
     * @param params : 파라메터
     * @param method : 메쏘드 (0 : GET(DEFALT), 1 : POST, 2 : DELETE)
     * @return String : 리턴된 JSON 스트링
     * @throws Exception
     */
    public Map<String, Object> callRest(String server_url, String url_path, String params, int method) throws Exception{
        HttpURLConnection con = null;
        URL url = null;
        String method_str = "GET";
        String result = "";

        if(method == 1){
            method_str = "POST";
        }else if(method == 2){
            method_str = "DELETE";
        }

        params = StringUtils.defaultIfEmpty(params, "").trim();

        String request_uri = server_url + url_path;
        if(method != 1 && !"".equals(params)){
            request_uri += "?" + params;
        }

        try{
            url = new URL(request_uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method_str);
            if(method == 1 && !"".equals(params)){
                if(antServer.equals(server_url)){
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                }else{
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                }
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
            log.debug("{} error {}", request_uri, e.getMessage());
            if(con != null){
                result = readStream(con.getErrorStream());
            }
        }

        log.debug(result);
        return new Gson().fromJson(result, Map.class);
        //return result;
    }

    private String readStream(InputStream stream) throws Exception{
        StringBuffer pageContents = new StringBuffer();
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader buff = new BufferedReader(reader);

        String pageContentsR = "";
        while((pageContentsR = buff.readLine()) != null){
            pageContents.append(pageContentsR);
            pageContents.append("\r\n");
        }
        reader.close();
        buff.close();

        return pageContents.toString();
    }

    /**
     * 포토서버 임시 파일 이동
     *
     * @param tempImg : 옮길 파일 경로
     * @return
     * @throws Exception
     */
    public Map<String, Object> imgDone(String tempImg) throws Exception{
        return imgDone(tempImg, "");
    }

    /**
     * 포토서버 임시 파일 이동
     *
     * @param tempImg : 옮길 파일 경로
     * @param delImg : 삭제할 파일 경로
     * @return
     * @throws Exception
     */
    public Map<String, Object> imgDone(String tempImg, String delImg) throws Exception{
        delImg = StringUtils.defaultIfEmpty(delImg, "").trim();
        String params = "tempFileURI=" + tempImg;
        if(!"".equals(delImg)){
            params = "&deleteFileURI=" + delImg;
        }

        return callRest(photoServer, "/done", params, 1);
    }

    /**
     * Ant Media 방 생성
     *
     * @param roomNm : 방제목
     * @return
     * @throws Exception
     */
    public Map<String, Object> antCreate(String roomNm) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", roomNm);

        return callRest(antServer, "/" + antName + "/rest/broadcast/create", new Gson().toJson(map), 1);
    }

    /**
     * Ant Media 방토큰 가져오기 (publish)
     *
     * @param roomId : 룸아이디
     * @return
     * @throws Exception
     */
    public Map<String, Object> antToken(String roomId) throws Exception{
        return antToken(roomId, "publish");
    }

    /**
     * Ant Media 방토큰 가져오기
     *
     * @param roomId : 룸아이디
     * @param type : 타입 (publish/play)
     * @return
     * @throws Exception
     */
    public Map<String, Object> antToken(String roomId, String type) throws Exception{
        if(!"play".equals(type)){
            type = "publish";
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, antExpire);
        long expire = cal.getTime().getTime() / 1000;

        String params = "id=" + roomId + "&expireDate=" + expire + "&type=" + type;

        return callRest(antServer, "/" + antName + "/rest/broadcast/getToken", params, 0);
    }
}
