package com.dalbit.broadcast.service;

import com.dalbit.broadcast.vo.TTSCallbackVo;
import com.dalbit.broadcast.vo.TTSSpeakVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@Service
public class TTSService {
    @Value("${tts.account.id}") private String TTS_ACCOUNT_ID;
    @Value("${tts.account.pw}") private String TTS_ACCOUNT_PW;
    @Value("${tts.account.token}") private String TTS_ACCOUNT_TOKEN;
    @Value("${tts.api.host}") private String TTS_API_HOST;
    String callbackUrl = "http://devm-parksm.dalbitlive.com:8080/broad/tts/callback";

    public void ttsConnection(TTSSpeakVo ttsVo) {
//        JsonElement ttsInfo = getTTSInfo();
//        String getCallbackUrl = ttsInfo.getAsJsonObject().get("result").getAsJsonObject().get("callback_url").toString().replaceAll("\"", "");
//        log.warn("callbackUrl 11 : {}", getCallbackUrl);
//        if(!StringUtils.equals(callbackUrl, getCallbackUrl)) {
//            getCallbackUrl = insCallbackUrl().getAsJsonObject().get("result").getAsJsonObject().get("callback_url").toString().replaceAll("\"", "");
//            log.warn("callbackUrl 22 : {}", getCallbackUrl);
//        }
//        String[] actorIdArr = tempFindActor();
//        ttsVo.setActorId(actorIdArr[0]);
//
//        JsonElement speakResult = ttsSpeak(ttsVo).getAsJsonObject().get("result").getAsJsonObject();
//        String speakUrl = speakResult.getAsJsonObject().get("speak_url").toString().replaceAll("\"", "");
//        String playId = speakResult.getAsJsonObject().get("play_id").toString().replaceAll("\"", "");
//        log.warn("speak_url : {}", speakUrl);
//        log.warn("playId : {}", playId);

//        JsonElement voice = makeVoice(speakUrl);
    }

    /**********************************************************************************************
    * @Method ?????? : ?????? tts ????????? ??????
    * @?????????   : 2021-11-11
    * @?????????   : ?????????
    * @????????????  :
    **********************************************************************************************/
    private JsonElement getTTSInfo() {
        String connUrl = TTS_API_HOST + "/api/me";
        return ttsApiCall(connUrl, "getUserInfo");
    }

    /**********************************************************************************************
    * @Method ?????? : callback url ??????
    * @?????????   : 2021-11-11
    * @?????????   : ?????????
    * @param   : callback_url: ???????????? url
    **********************************************************************************************/
    private JsonElement insCallbackUrl() {
        String connUrl = TTS_API_HOST + "/api/me/callback-url";

        JSONObject params = new JSONObject();
        params.put("callback_url", callbackUrl);

        return ttsApiCall(connUrl, "PUT", params, "insCallbackUrl");
    }

    /**********************************************************************************************
    * @Method ?????? : ??????(?????????) ?????????
    * @?????????   : 2021-11-11
    * @?????????   : ?????????
    * @????????????  :
    **********************************************************************************************/
    public String[] tempFindActor() {
        String connUrl = TTS_API_HOST + "/api/actor";
        JsonElement callApiRes = ttsApiCall(connUrl, "findActor");
        JsonElement actorArray = callApiRes.getAsJsonObject().get("result").getAsJsonArray();

        String[] result = new String[actorArray.getAsJsonArray().size()];
        int index = 0;
        for(JsonElement vo : actorArray.getAsJsonArray()) {
            result[index++] = vo.getAsJsonObject().get("actor_id").toString().replaceAll("\"", "");
        }

        return result;
    }

    public ArrayList<Map<String, String>> findActor() {
        String connUrl = TTS_API_HOST + "/api/actor";
        JsonElement callApiRes = ttsApiCall(connUrl, "findActor");
        JsonElement actorArray = callApiRes.getAsJsonObject().get("result").getAsJsonArray();

        ArrayList<Map<String, String>> result = new ArrayList<>();
        for(JsonElement vo : actorArray.getAsJsonArray()) {
            Map<String, String> data = new HashMap<>();
             String actorId = vo.getAsJsonObject().get("actor_id").toString().replaceAll("\"", "");
             String actorName = vo.getAsJsonObject().get("name").getAsJsonObject().get("ko").toString().replaceAll("\"", "");

            if(StringUtils.equals(actorName, "??????") || StringUtils.equals(actorName, "?????????")) {
                data.put("actorId", actorId);
                data.put("actorName", actorName);
                result.add(data);
            }
            /*if(StringUtils.equals(vo.getAsJsonObject().get("hidden").toString(), "false")) {
                String actorId = vo.getAsJsonObject().get("actor_id").toString().replaceAll("\"", "");
                String actorName = vo.getAsJsonObject().get("name").getAsJsonObject().get("ko").toString().replaceAll("\"", "");

                data.put("actorId", actorId);
                data.put("actorName", actorName);
                result.add(data);
            }*/
        }

        // actorName ???????????? ??????
        Collections.sort(result, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return o1.get("actorName").compareTo(o2.get("actorName"));
            }
        });

        for(int i=0; i<result.size(); i++) {
            result.get(i).put("idx", i + "");
        }

        return result;
    }

    public String[] findActorIds() {
        String connUrl = TTS_API_HOST + "/api/actor";
        JsonElement callApiRes = ttsApiCall(connUrl, "findActor");
        JsonElement actorArray = callApiRes.getAsJsonObject().get("result").getAsJsonArray();

        String[] result = new String[actorArray.getAsJsonArray().size()];
        int index = 0;
        for(JsonElement vo : actorArray.getAsJsonArray()) {
            result[index++] = vo.getAsJsonObject().get("actor_id").toString().replaceAll("\"", "");
        }

        return result;
    }

    /**********************************************************************************************
    * @Method ?????? : tts ??????
    * @?????????   : 2021-11-11
    * @?????????   : ?????????
    * @param    : ttsText(30???), actorId
    * xapi_hd (true/ false): true??? ????????? ???????????? (default: false)
    * xapi_audio_format: "mp3"??? set?????? mp3 ???????????? ????????? ????????? (default: wav)
    **********************************************************************************************/
    public JsonElement ttsSpeak(TTSSpeakVo ttsVo) {
        String connUrl = TTS_API_HOST + "/api/speak";
        JSONObject params = new JSONObject();
        params.put("text", ttsVo.getTtsText());
        params.put("lang", ttsVo.getLang());
        params.put("actor_id", ttsVo.getActorId());
        params.put("max_seconds", ttsVo.getMaxSeconds());

        return ttsApiCall(connUrl, "POST", params, "ttsSpeak");
    }

    /**********************************************************************************************
     * @Method ?????? : ?????? ?????? ??????
     * @?????????   : 2021-11-18
     * @?????????   : ?????????
     * @????????????  :
     **********************************************************************************************/
    public JsonElement makeVoice(String speakUrl) {
        return ttsApiCall(speakUrl,"makeVoice");
    }

    /**********************************************************************************************
    * @Method ?????? : callback method
    * @?????????   : 2021-11-11
    * @?????????   : ?????????
    * @????????????  :
    **********************************************************************************************/
    public String ttsCallback(TTSCallbackVo ttsCallbackVo) {
        log.warn("hihi 1 : {}", ttsCallbackVo.getAudio_url_no_redirect());
        log.warn("hihi 2 : {}", ttsCallbackVo.getPlay_id());
        log.warn("hihi 3 : {}", ttsCallbackVo.getSpeak_url());
        return "";
    }

    /**********************************************************************************************
     * @Method ?????? : tts GET call
     * @?????????   : 2021-11-11
     * @?????????   : ?????????
     * @param  :
     * connUrl: connect url
     * callBy: ????????? ???
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

        return result;
    }

    /**********************************************************************************************
     * @Method ?????? : tts PUT | POST call
     * @?????????   : 2021-11-11
     * @?????????   : ?????????
     * @param  :
     * connUrl: connect url
     * requestType: PUT | POST
     * param: put | post JSONObject ????????? ??????????????? ????????? ??????
     * callBy: ????????? ???
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

            if(params != null) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                bw.write(params.toString());
                bw.flush();
                bw.close();
            }

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

        return result;
    }

    /**********************************************************************************************
     * @Method ?????? : HttpURLConnection response ??????
     * @?????????   : 2021-11-11
     * @?????????   : ?????????
     * @????????????  :
     **********************************************************************************************/
    private String getResponseCodeHandler(int responseCode, String path) {
        String successYn = "n";
        switch (responseCode) {
            case 400: log.error(path + " 400 :: ?????? ?????? ?????? ??????"); break;
            case 401: log.error(path + " 401 :: ?????? ??????"); break;
            case 500: log.error(path + " 500 :: ?????? ??????"); break;
            default: successYn = "y";
        }

        return successYn;
    }

    // tts actorSlct
    public String[] getTtsActorSlct(String actorId) {
        // return [actorSlct, actorName]
        switch (actorId) {
            case "6063252471850cc8f04c7600": return new String[] {"a", "????????????"}; // ????????????
            case "61659cc118732016a95fe7c6": return new String[] {"b", "??????"}; // ??????
            case "5c547544fcfee90007fed455": return new String[] {"c", "??????"}; // ??????
            case "5ffda49bcba8f6d3d46fc447": return new String[] {"d", "?????????"}; // ?????????
            default: return new String[] {"c", "??????"}; // ??????
        }
    }
}
