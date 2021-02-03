package com.dalbit.rest.service;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RestService {
    @Value("${server.photo.url}")
    private String photoServer;

    @Value("${inforex.plan.memNo}")
    private String[] INFOREX_PLAN_MEMNO;

    @Value("${app.package.aos}")
    private String APP_PACKAGE_AOS;

    @Value("${app.bundle.ios}")
    private String APP_BUNDLE_IOS;

    @Value("${firebase.app.key}")
    private String FIREBASE_APP_KEY;

    @Value("${firebase.dynamic.link.url}")
    private String FIREBASE_DYNAMIC_LINK_URL;

    @Value("${firebase.dynamic.link.prefix}")
    private String FIREBASE_DYNAMIC_LINK_PREFIX;

    @Value("${server.www.url}")
    private String SERVER_WWW_URL;

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
    public Map<String, Object> callRest(String server_url, String url_path, HashMap<String, String> params, int method, HttpServletRequest request) throws GlobalException{

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

            return callRest(server_url, url_path, paramString.toString(), method, request);
        }catch (Exception e){
            throw new GlobalException(ErrorStatus.호출에러, Thread.currentThread().getStackTrace()[1].getMethodName());
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
    public Map<String, Object> callRest(String server_url, String url_path, String params, int method, HttpServletRequest request) throws GlobalException{
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

        String request_uri = server_url;
        /*if(server_url.startsWith("https://") && server_url.endsWith(".dalbitlive.com") && (DalbitUtil.profileCheck("dev") || DalbitUtil.profileCheck("real"))){
            request_uri = server_url.replace("https://", "http://");
        }*/
        request_uri += url_path;
        if(method != 1 && !"".equals(params)){
            request_uri += "?" + params;
        }

        log.debug("{} {} {} rest start", server_url, request_uri, params);
        try{
            url = new URL(request_uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method_str);
            con.setConnectTimeout(5000);
            if(method == 1 && !"".equals(params)){
                if(FIREBASE_DYNAMIC_LINK_URL.equals(server_url)){
                    con.setRequestProperty("Content-Type", "text/json; charset=UTF-8");
                }else{
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                }
                //con.setDoInput(true);
                con.setDoOutput(true);
                con.setInstanceFollowRedirects(false);
                con.setUseCaches(false);
                con.setAllowUserInteraction(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.write(params.getBytes("UTF-8"));
                out.flush();
            }

            result = readStream(con.getInputStream(), server_url, url_path, params, request);
        }catch(IOException e){
            log.info("{} error {}", request_uri, e.getMessage());
            if(con != null){
                result = readStream(con.getErrorStream(), server_url, url_path, params, request);
            }
        }

        log.debug("callRest 결과 : {}", result);

        try {

            return new Gson().fromJson(result, Map.class);

        }catch (Exception e){
            ArrayList messageList = new ArrayList<String>();
            messageList.add("회원번호 : "+ MemberVo.getMyMemNo(request));
            messageList.add("server_url : "+ server_url);
            messageList.add("url_path : "+ url_path);
            messageList.add("params : "+ params);
            messageList.add("method : "+ method);
            messageList.add("result : "+ result);

            throw new GlobalException(ErrorStatus.호출에러, messageList, Thread.currentThread().getStackTrace()[1].getMethodName());
        }
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
    private String readStream(InputStream stream, String server_url, String url_path, String params, HttpServletRequest request) throws GlobalException{
        if(stream == null){
            return "";
        }
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
            log.info("{} {} error {}", server_url, url_path, e.getMessage());
            //방송 생성 후 publish token 생성시 오류 일때 방송방 삭제
            throw new GlobalException(ErrorStatus.호출에러, Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    /**
     * 포토서버 imageURL 업로드
     *
     * @param url : 이미지 URL
     * @return
     * @throws GlobalException
     */
    public Map<String, Object> urlImgSave(String url, HttpServletRequest request) throws GlobalException {
        try {
            return callRest(photoServer, "/upload", "imageUrl=" + URLEncoder.encode(url, "UTF-8"), 1, request);
        }catch (Exception e){
            throw new GlobalException(ErrorStatus.호출에러, Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    /**
     * 포토서버 임시 파일 이동
     *
     * @param tempImg : 옮길 파일 경로
     * @return
     * @throws Exception
     */
    public Map<String, Object> imgDone(String tempImg, HttpServletRequest request) throws GlobalException{
        return imgDone(tempImg, "", request);
    }

    /**
     * 포토서버 임시 파일 이동
     *
     * @param tempImg : 옮길 파일 경로
     * @param delImg : 삭제할 파일 경로
     * @return
     * @throws Exception
     */
    public Map<String, Object> imgDone(String tempImg, String delImg, HttpServletRequest request) throws GlobalException{
        delImg = StringUtils.defaultIfEmpty(delImg, "").trim();
        String params = "tempFileURI=" + tempImg;
        if(!"".equals(delImg)){
            params += "&deleteFileURI=" + delImg;
        }

        return callRest(photoServer, "/done", params, 1, request);
    }

    public Map<String, Object> makeFirebaseDynamicLink(String room_no, String link, String nickNm, String profImg, String title, HttpServletRequest request) throws GlobalException{
        JSONObject androidInfo = new JSONObject();
        androidInfo.put("androidPackageName", APP_PACKAGE_AOS);

        HashMap<String, String> iosInfo = new HashMap<>();
        iosInfo.put("iosBundleId", APP_BUNDLE_IOS);

        HashMap<String, String> socialMetaTagInfo = new HashMap<>();
        socialMetaTagInfo.put("socialTitle", nickNm);
        socialMetaTagInfo.put("socialDescription", title);
        socialMetaTagInfo.put("socialImageLink", profImg);

        HashMap<String, Object> suffix = new HashMap<>();
        suffix.put("option", "SHORT");// UNGUESSABLE or SHORT

        HashMap<String, Object> dynamicLinkInfo = new HashMap<>();
        dynamicLinkInfo.put("domainUriPrefix", FIREBASE_DYNAMIC_LINK_PREFIX);
        dynamicLinkInfo.put("link", SERVER_WWW_URL + "/broadcast/" + room_no + "?etc={\"push_type\":1,\"room_no\":\"" + room_no + "\"}");
        //dynamicLinkInfo.put("link", SERVER_WWW_URL + "/l/" + link + "?etc={\"push_type\":1,\"room_no\":\"" + room_no + "\"}");
        dynamicLinkInfo.put("androidInfo", androidInfo);
        dynamicLinkInfo.put("iosInfo", iosInfo);
        dynamicLinkInfo.put("socialMetaTagInfo", socialMetaTagInfo);

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamicLinkInfo", dynamicLinkInfo);
        map.put("suffix", suffix);

        return callRest(FIREBASE_DYNAMIC_LINK_URL, "/v1/shortLinks?key=" + FIREBASE_APP_KEY, new Gson().toJson(map), 1, request);
    }

    public Map<String, Object> makeClipFirebaseDynamicLink(String cast_no, String nickNm, String backImg, String title, HttpServletRequest request) throws GlobalException{
        JSONObject androidInfo = new JSONObject();
        androidInfo.put("androidPackageName", APP_PACKAGE_AOS);

        HashMap<String, String> iosInfo = new HashMap<>();
        iosInfo.put("iosBundleId", APP_BUNDLE_IOS);

        HashMap<String, String> socialMetaTagInfo = new HashMap<>();
        socialMetaTagInfo.put("socialTitle", nickNm);
        socialMetaTagInfo.put("socialDescription", title);
        socialMetaTagInfo.put("socialImageLink", backImg);

        HashMap<String, Object> suffix = new HashMap<>();
        suffix.put("option", "SHORT");// UNGUESSABLE or SHORT

        HashMap<String, Object> dynamicLinkInfo = new HashMap<>();
        dynamicLinkInfo.put("domainUriPrefix", FIREBASE_DYNAMIC_LINK_PREFIX);
        dynamicLinkInfo.put("link", SERVER_WWW_URL + "/clip/" + cast_no + "?etc={\"push_type\":45,\"room_no\":\"" + cast_no + "\"}");
        dynamicLinkInfo.put("androidInfo", androidInfo);
        dynamicLinkInfo.put("iosInfo", iosInfo);
        dynamicLinkInfo.put("socialMetaTagInfo", socialMetaTagInfo);

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamicLinkInfo", dynamicLinkInfo);
        map.put("suffix", suffix);

        return callRest(FIREBASE_DYNAMIC_LINK_URL, "/v1/shortLinks?key=" + FIREBASE_APP_KEY, new Gson().toJson(map), 1, request);
    }
}
