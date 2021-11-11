package com.dalbit.broadcast.service;

import com.dalbit.broadcast.vo.TTSVo;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class TTSService {
    @Value("${tts.account.id}") private String TTS_ACCOUNT_ID;
    @Value("${tts.account.pw}") private String TTS_ACCOUNT_PW;
    @Value("${tts.account.token}") private String TTS_ACCOUNT_TOKEN;
    @Value("${tts.api.host}") private String TTS_API_HOST;

    public void ttsConnection(TTSVo ttsVo) {
        JsonElement ttsInfo = getTTSInfo();
        String callbackUrl = ttsInfo.getAsJsonObject().get("result").getAsJsonObject().get("callback_url").toString();
        log.warn("callbackUrl zz : {}", callbackUrl);
        if(StringUtils.isEmpty(callbackUrl)) {
            insCallbackUrl();
        }
        String[] actorIdArr = findActor();
        ttsVo.setActor_id(actorIdArr[0]);

//        String speakUrl = ttsSpeak(ttsVo).getAsJsonObject().get("result").getAsJsonObject().get("speak_url").toString();
        log.warn("speak_url : {}", ttsSpeak(ttsVo));
    }

    /**********************************************************************************************
    * @Method 설명 : 달빛 tts 아이디 정보
    * @작성일   : 2021-11-11
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    private JsonElement getTTSInfo() {
        String connUrl = TTS_API_HOST + "/api/me";
        JsonElement result = null;
        result = ttsApiCall(connUrl, "getUserInfo");

        return result;
    }

    /**********************************************************************************************
    * @Method 설명 : callback url 등록
    * @작성일   : 2021-11-11
    * @작성자   : 박성민
    * @param   : callback_url: 콜백받을 url
    **********************************************************************************************/
    private JsonElement insCallbackUrl() {
        String connUrl = TTS_API_HOST + "/api/me/callback-url";
        String callbackUrl = "https://devm-parksm.dalbitlive.com:463/tts/callback";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("callback_url", callbackUrl);
        JsonElement result = ttsApiCall(connUrl, "PUT", params, "insCallbackUrl");

        return result;
    }

    /**********************************************************************************************
    * @Method 설명 : 성우(목소리) 리스트
    * @작성일   : 2021-11-11
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    private String[] findActor() {
        String connUrl = TTS_API_HOST + "/api/actor";
        JsonElement callApiRes = ttsApiCall(connUrl, "findActor");
        JsonElement actorArray = callApiRes.getAsJsonObject().get("result").getAsJsonArray();

        String[] result = new String[actorArray.getAsJsonArray().size()];
        int index = 0;
        for(JsonElement vo : actorArray.getAsJsonArray()) {
            result[index++] = vo.getAsJsonObject().get("actor_id").toString();
            log.warn("actor_id : {}", vo.getAsJsonObject().get("actor_id"));
        }

        return result;
    }

    /**********************************************************************************************
    * @Method 설명 : tts 요청
    * @작성일   : 2021-11-11
    * @작성자   : 박성민
    * @설명    :
    * xapi_hd (true/ false): true면 음질이 높아진다 (default: false)
    * xapi_audio_format: "mp3"로 set하면 mp3 포맷으로 파일을 받는다 (default: wav)
    **********************************************************************************************/
    public JsonElement ttsSpeak(TTSVo ttsVo) {
        String connUrl = TTS_API_HOST + "/api/speak";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("text", ttsVo.getText());
        params.put("lang", ttsVo.getLang());
        params.put("actor_id", ttsVo.getActor_id());
        params.put("max_seconds", ttsVo.getMax_seconds());

        JsonElement result = ttsApiCall(connUrl, "POST", params, "ttsSpeak");

        return result;
    }

    /**********************************************************************************************
    * @Method 설명 : callback method
    * @작성일   : 2021-11-11
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    public String ttsCallback() {
        log.warn("hihi");
        return "";
    }

    /**********************************************************************************************
     * @Method 설명 : tts GET call
     * @작성일   : 2021-11-11
     * @작성자   : 박성민
     * @param  :
     * connUrl: connect url
     * callBy: 호출한 곳
     **********************************************************************************************/
    private JsonElement ttsApiCall(String connUrl, String callBy) {
        JsonElement result = null;

        try {
            URL url = new URL(connUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + TTS_ACCOUNT_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            String connSuccessYn = getResponseCodeHandler(responseCode, callBy);
            if(StringUtils.equals(connSuccessYn, "y")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                result = new JsonParser().parse(URLDecoder.decode(sb.toString(), "UTF-8" ));
            }
        } catch (Exception e) {
            log.error("TTSService / " + callBy + " ===> {}", e);
        }

        log.warn(callBy + " : {}", result.toString());

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : tts PUT | POST call
     * @작성일   : 2021-11-11
     * @작성자   : 박성민
     * @param  :
     * connUrl: connect url
     * requestType: PUT | POST
     * param: put | post JSONObject 타입의 파라미터가 들어간 객체
     * callBy: 호출한 곳
     **********************************************************************************************/
    private JsonElement ttsApiCall(String connUrl, String requestType, Map<String, Object> params, String callBy) {
        JsonElement result = null;

        try {
            URL url = new URL(connUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestType);
            conn.setRequestProperty("Authorization", "Bearer " + TTS_ACCOUNT_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String, Object> param : params.entrySet()) {
                if(postData.length() != 0) postData.append("&");
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
            conn.getOutputStream().write(postDataBytes);
//            bw.write(postDataBytes);
//            bw.flush();
//            bw.close();

            int responseCode = conn.getResponseCode();
            String connSuccessYn = getResponseCodeHandler(responseCode, callBy);

            if(StringUtils.equals(connSuccessYn, "y")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                result = new JsonParser().parse(URLDecoder.decode(sb.toString(), "UTF-8" ));
                br.close();
            }
        } catch (Exception e) {
            log.error("TTSService / " + callBy + " ===> {}", e);
        }

        log.warn(callBy + " : {}", result.toString());

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : HttpURLConnection response 상태
     * @작성일   : 2021-11-11
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    private String getResponseCodeHandler(int responseCode, String path) {
        String successYn = "n";
        switch (responseCode) {
            case 400: log.error(path + " 400 :: 해당 명령 실행 불가"); break;
            case 401: log.error(path + " 401 :: 헤더 오류"); break;
            case 500: log.error(path + " 500 :: 서버 에러"); break;
            default: successYn = "y";
        }

        return successYn;
    }
}
