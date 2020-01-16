package com.dalbit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class RestApiUtil {

    private static int restApiTimeout;

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
                while ((line = br.readLine()) != null) {
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
                while ((line = br.readLine()) != null) {
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
}
