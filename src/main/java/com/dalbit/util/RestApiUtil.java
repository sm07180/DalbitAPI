package com.dalbit.util;

import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class RestApiUtil {

    private static CommonService commonService;
    private static int restApiTimeout;

    @Autowired
    private CommonService getCommonService;

    @PostConstruct
    private void init () {
        commonService = getCommonService;
    }

    @Value("${rest.api.timeout}")
    public void setRestApiTimeout(int timeoutSecond){
        restApiTimeout = timeoutSecond;
    }

    /**
     * get 호출
     * @param strUrl
     * @return
     */
    public static String sendGet(String strUrl) {

        StringBuffer result = new StringBuffer();

        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(false);
            con.setConnectTimeout(restApiTimeout); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(restApiTimeout); // InputStream 읽어 오는 Timeout 시간 설정

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while (!DalbitUtil.isEmpty(line = br.readLine())) {
                    result.append(line);
                }
                br.close();
            } else {
                result.append(con.getResponseMessage());
            }

            log.debug(result.toString());

        } catch (Exception e) {
            result.append(e.toString());

            log.error(e.getStackTrace().toString());
        }
        return result.toString();
    }

    /**
     * post 호출
     * @param strUrl
     * @param jsonMessage
     * @return
     */
    public static String sendPost(String strUrl, String jsonMessage){

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            con.setConnectTimeout(restApiTimeout); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(restApiTimeout); // InputStream 읽어 오는 Timeout 시간 설정

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(jsonMessage.getBytes("UTF-8"));
            outputStream.flush();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while (!DalbitUtil.isEmpty(line = br.readLine())) {
                    result.append(line);
                }
                br.close();
            } else {
                result.append(con.getResponseMessage());
            }

            log.debug(result.toString());

        } catch (Exception e){
            result.append(e.toString());
            e.getStackTrace();
        }

        return result.toString();
    }

    /**
     * socket post 호출
     * @return
     */
    public static String sendSocketPost(String strUrl, String authToken, String roomNo, String params){
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("x-custom-header", authToken);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.setAllowUserInteraction(true);

            con.setConnectTimeout(restApiTimeout); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(restApiTimeout); // InputStream 읽어 오는 Timeout 시간 설정

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(params.getBytes("UTF-8"));
            outputStream.flush();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while (!DalbitUtil.isEmpty(line = br.readLine())) {
                    result.append(line);
                }
                br.close();
            } else {
                result.append(con.getResponseMessage());
            }

            log.debug(result.toString());

        } catch (IOException e){
            P_ErrorLogVo apiData = new P_ErrorLogVo();
            apiData.setOs("API");
            apiData.setDtype("SOCKET");
            apiData.setCtype(roomNo);
            String desc = "";
            if (!DalbitUtil.isEmpty(params)) {
                desc = "Data : \n" + params + "\n";
            }
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            if (sw != null) {
                desc += "Exception : \n" + sw.toString();
            }
            apiData.setDesc(desc);
            commonService.saveErrorLog(apiData);
        } catch (Exception e){
            result.append(e.toString());
            e.getStackTrace();
        }

        return result.toString();
    }
}
