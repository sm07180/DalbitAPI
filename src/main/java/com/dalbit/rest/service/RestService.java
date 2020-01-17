package com.dalbit.rest.service;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.exception.GlobalException;
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
    public Map<String, Object> callRest(String server_url, String url_path, HashMap<String, String> params, int method) throws GlobalException{

        try {
            StringBuffer paramString = new StringBuffer();
            if (params != null) {
                for (Map.Entry<String, String> e : params.entrySet()) {
                    if (paramString.length() > 0) {
                        paramString.append("&");
                    }
                    paramString.append(URLEncoder.encode(e.getKey(), "UTF-8"));
                    paramString.append("=");
                    paramString.append(URLEncoder.encode(e.getValue(), "UTF-8"));
                }
            }

            return callRest(server_url, url_path, paramString.toString(), method);
        }catch (Exception e){
            throw new GlobalException(ErrorStatus.호출에러);
        }
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
    public Map<String, Object> callRest(String server_url, String url_path, String params, int method) throws GlobalException{
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

            result = readStream(con.getInputStream(), server_url, url_path, params);
        }catch(IOException e){
            log.debug("{} error {}", request_uri, e.getMessage());
            if(con != null){
                result = readStream(con.getErrorStream(), server_url, url_path, params);
            }
        }

        log.debug(result);
        return new Gson().fromJson(result, Map.class);
        //return result;
    }

    /**
     * http 연결 읽기
     *
     * @param stream : 연결 스트림
     * @param server_url : 요청 서버 도메임 (Exception 처리)
     * @param url_path : 요청 URL 도메임 (Exception 처리)
     * @param params : 요청 파라메터 도메임 (Exception 처리)
     * @return
     * @throws GlobalException
     */
    private String readStream(InputStream stream, String server_url, String url_path, String params) throws GlobalException{
        try {
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
        }catch (Exception e){
            //방송 생성 후 publish token 생성시 오류 일때 방송방 삭제
            if(antServer.equals(server_url) && url_path.endsWith("/getToken") && params.endsWith("&type=publish")){
                String stream_id = params.substring(3, params.indexOf("&"));
                deleteAntRoom(stream_id);
            }
            throw new GlobalException(ErrorStatus.호출에러);
        }
    }

    /**
     * 포토서버 imageURL 업로드
     *
     * @param url : 이미지 URL
     * @return
     * @throws GlobalException
     */
    public Map<String, Object> urlImgSave(String url) throws GlobalException {
        try {
            return callRest(photoServer, "/upload", "imageUrl=" + URLEncoder.encode(url, "UTF-8"), 1);
        }catch (Exception e){
            throw new GlobalException(ErrorStatus.호출에러);
        }
    }

    /**
     * 포토서버 임시 파일 이동
     *
     * @param tempImg : 옮길 파일 경로
     * @return
     * @throws Exception
     */
    public Map<String, Object> imgDone(String tempImg) throws GlobalException{
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
    public Map<String, Object> imgDone(String tempImg, String delImg) throws GlobalException{
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
    public Map<String, Object> antCreate(String roomNm) throws GlobalException {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", roomNm);

        return callRest(antServer, "/" + antName + "/rest/broadcast/create", new Gson().toJson(map), 1);
    }

    /**
     * Ant Media 방토큰 가져오기 (publish)
     *
     * @param streamId : 스트리밍아이디
     * @return
     * @throws Exception
     */
    public Map<String, Object> antToken(String streamId) throws GlobalException{
        return antToken(streamId, "publish");
    }

    /**
     * Ant Media 방토큰 가져오기
     *
     * @param streamId : 스트리밍아이디
     * @param type : 타입 (publish/play)
     * @return
     * @throws Exception
     */
    public Map<String, Object> antToken(String streamId, String type) throws GlobalException{
        if (streamId == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("tokenId", "");
            return map;
        }
        if(!"play".equals(type)){
            type = "publish";
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, antExpire);
        long expire = cal.getTime().getTime() / 1000;

        String params = "id=" + streamId + "&expireDate=" + expire + "&type=" + type;

        return callRest(antServer, "/" + antName + "/rest/broadcast/getToken", params, 0);
    }

    /**
     * Ant Media 토큰 삭제
     *
     * @param streamId : 룸아이디
     * @return
     * @throws Exception
     */
    public Map<String, Object> deleteAntToken(String streamId) throws GlobalException{
        return callRest(antServer, "/" + antName + "/rest/v2/broadcasts/" + streamId + "/tokens", "", 2);
    }

    /**
     * Ant Media 방 삭제
     *
     * @param streamId : 스트리밍아이디
     * @return
     * @throws Exception
     */
    public Map<String, Object> deleteAntRoom(String streamId) throws GlobalException{
        return callRest(antServer, "/" + antName + "/rest/v2/broadcasts/" + streamId, "", 2);
    }

    /**
     * Ant Media 방/토큰 삭제
     *
     * @param streamId : 스트리밍아이디
     * @return
     * @throws Exception
     */
    public Map<String, Object> deleteAntAll(String streamId) throws GlobalException{
        Map<String, Object> result = new HashMap<>();
        result.put("token", deleteAntToken(streamId));
        result.put("room", deleteAntRoom(streamId));
        return result;
    }
}
