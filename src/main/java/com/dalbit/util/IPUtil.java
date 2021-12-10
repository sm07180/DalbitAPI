package com.dalbit.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class IPUtil {

    //접근 가능 IP
    @Value("${api.connect.ip}")
    private String API_CONNECT_IP;

    //접근 가능 IP 목록 new
    @Value("${inner.connect.ip}")
    public String INNER_CONNECT_IP;

    /**
     * 요청 IP 가져오기
     *
     * @param request
     * @return
     */
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        log.debug("> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.debug("> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.debug(">  WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.debug("> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.debug("> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            log.debug("> getRemoteAddr : "+ip);
        }
        log.debug("> Result : IP Address : "+ip);

        return ip;
    }

    //공통으로 사용하는 아이피 체크
    public List<String> getValidationIP(String[] ipArray){
        List<String> result = new ArrayList<String>();

        // ex.) 192.168.0.1
        Pattern pattenIP = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[*])\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[*])$");
        // ex.) 192.169.0.1-30
        Pattern pattenIPs = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[*])\\.){3}((?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)-(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))$");
        for(String ip : ipArray){
            // IP 유효성 검사
            if(pattenIP.matcher(ip).find()){
                result.add(ip);
                continue;
            }

            // 범위 IP 유효성 검사
            if(pattenIPs.matcher(ip).find()){

                int substrIdx = ip.lastIndexOf("-");
                String standardIP = ip.substring(0, substrIdx);
                String[] arrayStandardIP = standardIP.split("\\.");
                int rangeStart = Integer.parseInt(arrayStandardIP[arrayStandardIP.length-1]);
                int rangeEnd = Integer.parseInt(ip.substring(substrIdx+1, ip.length()));

                // IP 유효성 검사
                if (pattenIP.matcher(ip.substring(0, substrIdx)).find()){

                    if(rangeStart < rangeEnd){
                        for(; rangeStart <= rangeEnd;rangeStart++ ){
                            String addIP = standardIP.substring(0, standardIP.lastIndexOf(".")+1) + rangeStart;
                            result.add(addIP);
                        }
                        continue;
                    }else{
                        result.add(standardIP);
                        continue;
                    }
                }
            }

            log.warn("[WARN] API Connect IP Error : {}", ip);
        }

        log.info("[SETTING] API Connect IP LIST : {}", result);
        return result;
    }

    /**
     * IP 유효성 검사
     * - 192.168.123.1
     * - 192.145.162.1-10
     * - 192.156.*.*
     */
    public List<String> validationIP() {
        String[] ipArray = API_CONNECT_IP.split(",");
        
        //없을 경우 ... 나중에 보기
        if(DalbitUtil.isEmpty(ipArray)){
            return null;
        }

        return getValidationIP(ipArray);
    }

    /**
     * 내부 IP 체크
     *
     * @param requestIP
     * @return
     */
    public boolean isInnerIP(String requestIP) {

        List<String> connectIpList = validationIP();

        log.debug(" Request IP : [{}] / Connectable IP List : {}", requestIP, connectIpList);
        if(connectIpList == null || connectIpList.isEmpty()){
            return false;
        }


        if(connectIpList.indexOf(requestIP) < 0){
            String[] arrayReqIP = requestIP.split("\\.");

            for(int i = 0; i < arrayReqIP.length; i++){
                Iterator<String> ip = connectIpList.iterator();
                while (ip.hasNext()){
                    String[] arrayConIP = ip.next().split("\\.");
                    if(arrayConIP[i].equals("*") || arrayReqIP[i].equals(arrayConIP[i])){
                        break;
                    }

                    if(!ip.hasNext()){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 사내 IP 체크 (new)
     */
    public boolean validationInnerIP(String requestIP) {
        String[] ipArray = INNER_CONNECT_IP.split(",");

        if(DalbitUtil.isEmpty(ipArray)){
            return false;
        }
        //아이피 유효성 검증
        List<String> connectIpList = getValidationIP(ipArray);

        if(connectIpList == null || connectIpList.isEmpty()){
            return false;
        }

        if(connectIpList.indexOf(requestIP) < 0){
            String[] arrayReqIP = requestIP.split("\\.");

            for(int i = 0; i < arrayReqIP.length; i++){
                Iterator<String> ip = connectIpList.iterator();
                while (ip.hasNext()){
                    String[] arrayConIP = ip.next().split("\\.");

                    if(arrayConIP[i].equals("*") || arrayReqIP[i].equals(arrayConIP[i])){
                        break;
                    }

                    if(!ip.hasNext()){
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
