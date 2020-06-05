package com.dalbit.util;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.exception.GlobalException;
import lombok.var;
import okhttp3.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OkHttpClientUtil {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // one instance, reuse
    OkHttpClient client = new OkHttpClient();

    public Response sendGet(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response;
    }

    public String sendGet(String url,HttpServletRequest httpRequest, String path, String domain) throws Exception {

        Cookie[] cookies = httpRequest.getCookies();

        String ADMIN_COOKIE= "";
        String gSTAFF = "";
        String NAME = "";
        String USER_ID = "";

        for(var i=0;i<cookies.length;i++){
            Cookie c = cookies[i];
            String cName = c.getName();
            String cValue = c.getValue();

            if ("ADMIN_COOKIE".equals(cName)){
                ADMIN_COOKIE = cName + "=" + cValue + ";expires=Mon, 25-Mar-2020 01:41:53 GMT; Path=" + path + "; Domain=" + domain;
            }else if("gSTAFF".equals(cName)){
                gSTAFF = cName + "=" + cValue + ";expires=Mon, 25-Mar-2020 01:41:53 GMT; Path=" + path + "; Domain=" + domain;
            }else if("NAME".equals(cName)){
                NAME = cName + "=" + cValue + ";expires=Mon, 25-Mar-2020 01:41:53 GMT; Path=" + path + "; Domain=" + domain;
            }else if("USER_ID".equals(cName)){
                USER_ID = cName + "=" + cValue + ";expires=Mon, 25-Mar-2020 01:41:53 GMT; Path=" + path + "; Domain=" + domain;
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Set-Cookie", ADMIN_COOKIE)
                .addHeader("Set-Cookie", gSTAFF)
                .addHeader("Set-Cookie", NAME)
                .addHeader("Set-Cookie", USER_ID)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.headers().toString());
            return response.body().string();
        }

    }

    public Response sendPost(String url, RequestBody formBody) throws GlobalException {
        try{

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            return response;
        }catch (Exception e){
            throw new GlobalException(ErrorStatus.잘못된호출, "OkHttpClientUtil.sendPost");
        }

    }


    public String sendDelete(String url) throws IOException {
        Request request = new Request.Builder().url(url).delete().build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
