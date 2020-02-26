package com.dalbit.util;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.LocationVo;
import com.dalbit.common.vo.ValidationResultVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
public class DalbitUtil {

    private static Environment environment;

    @Autowired
    private Environment activeEnvironment;

    @PostConstruct
    private void init () {
        environment = this.activeEnvironment;
    }

    public static String randomValue(String type, int cnt) {
        type = type.toLowerCase();

        StringBuffer strPwd = new StringBuffer();
        char[] str = new char[1];
        int i;
        if (type.equals("p")) {
            for(i = 0; i < cnt; ++i) {
                str[0] = (char)((int)(Math.random() * 94.0D + 33.0D));
                strPwd.append(str);
            }
        } else if (type.equals("a")) {
            for(i = 0; i < cnt; ++i) {
                str[0] = (char)((int)(Math.random() * 26.0D + 65.0D));
                strPwd.append(str);
            }
        } else if (type.equals("string")) {
            for(i = 0; i < cnt; ++i) {
                str[0] = (char)((int)(Math.random() * 26.0D + 97.0D));
                strPwd.append(str);
            }
        } else {
            //int i;
            if (type.equals("number")) {
                int[] strs = new int[1];

                for(i = 0; i < cnt; ++i) {
                    strs[0] = (int)(Math.random() * 9.0D);
                    strPwd.append(strs[0]);
                }
            } else if (type.equals("c")) {
                Random rnd = new Random();

                for(i = 0; i < cnt; ++i) {
                    if (rnd.nextBoolean()) {
                        strPwd.append((char)(rnd.nextInt(26) + 97));
                    } else {
                        strPwd.append(rnd.nextInt(10));
                    }
                }
            }
        }

        return strPwd.toString();
    }

    /**
     * 문자열에 값이 있는지 체크
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 리스트 값이 있는지 체크
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        if(list != null){
            return false;
        }
        return 0 < list.size() ? false : true;
    }

    /**
     * Object 값이 있는지 체크
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        return object != null ? false : true;
    }


    /**
     * 나이대 계산
     * @param year
     * @return
     */
    public static int ageCalculation(int year){
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        int currentYear = calendar.get(Calendar.YEAR);
        int age = currentYear - year + 1;

        String integerAge = Integer.toString(age);
        String stringAge = integerAge.substring(0,1)+"0";
        age = Integer.parseInt(stringAge);

        return age;
    }

    /**
     * 입력된 날짜가 유효한 날짜인지를 검사한다.
     * @param date 기준일자
     * @return 유효여부(true/false)
     */
    public static boolean isDate( String date ) {
        return isDate( date , null );
    }

    public static boolean isDate( String date , String format ) {
        if( date == null )
            return false;
        if( format == null )
            format = "yyyyMMdd";
        DateFormat df = new SimpleDateFormat( format , Locale.KOREA );
        df.setLenient( false );
        date = date.replaceAll( "\\D" , "" );
        try {
            df.parse( date );
            return true;
        }catch( ParseException pe ) {
            return false;
        }catch( Exception e ) {
            return false;
        }
    }

    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>defaultStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("하이", "하이", null, "bar") = null
     * StringUtil.decode("하이", "하이  ", "foo", null) = null
     * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
     * StringUtil.decode("하이", "하이  ", "foo", "bar") = "bar"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @param defaultStr sourceStr와 compareStr의 값이 다를 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 defaultStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) throws NullPointerException{
        if (sourceStr == null && compareStr == null) {
            return returnStr;
        }

        if (sourceStr == null && compareStr != null) {
            return defaultStr;
        }

        if (sourceStr.trim().equals(compareStr)) {
            return returnStr;
        }

        return defaultStr;
    }

    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>sourceStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo") = "foo"
     * StringUtil.decode("", null, "foo") = ""
     * StringUtil.decode(null, "", "foo") = null
     * StringUtil.decode("하이", "하이", "foo") = "foo"
     * StringUtil.decode("하이", "바이", "foo") = "하이"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 sourceStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr) throws NullPointerException{
        return decode(sourceStr, compareStr, returnStr, sourceStr);
    }

    /**
     * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
     * @param object 원본 객체
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object) {
        String string = "";

        if (!isEmpty(object)) {
            string = object.toString().trim();
        }

        return string;
    }

    public static int isStringToNumber(String str){
        return isStringToNumber(str, 0);
    }

    public static int isStringToNumber(String str, int nullValue){
        try {
            return DalbitUtil.isEmpty(str) ? nullValue : Integer.parseInt(str);
        } catch (Exception e){
            return nullValue;
        }
    }

    public static String convertRequestParamToString(HttpServletRequest request, String parameterName){
        if(request != null && parameterName != null){
            return DalbitUtil.isNullToString(request.getParameter(parameterName)).trim();
        }
        return "";
    }

    public static int convertRequestParamToInteger(HttpServletRequest request, String parameterName){
        try{
            return Integer.valueOf(request.getParameter(parameterName));
        }catch (Exception e){
            return -1;
        }

    }

    /**
     * 특수문자를 웹 브라우저에서 정상적으로 보이기 위해 특수문자를 처리('<' -> & lT)하는 기능이다
     * @param 	srcString 		- '<'
     * @return 	변환문자열('<' -> "&lt"
     * @exception NullPointerException
     * @see
     */
    public static String getSpclStrCnvr(String srcString) throws NullPointerException{

        String rtnStr;

        try {
            StringBuffer strTxt = new StringBuffer();

            char chrBuff;
            int len = srcString.length();

            for (int i = 0; i < len; i++) {
                chrBuff = srcString.charAt(i);

                switch (chrBuff) {
                    case '<':
                        strTxt.append("&lt;");
                        break;
                    case '>':
                        strTxt.append("&gt;");
                        break;
                    case '&':
                        strTxt.append("&amp;");
                        break;
                    default:
                        strTxt.append(chrBuff);
                }
            }

            rtnStr = strTxt.toString();

        } catch (NullPointerException e) {
            log.warn("StringUtil.getSpclStrCnvr NullPointerException - srcString : [{}]", srcString);
            return srcString;
        }

        return rtnStr;
    }

    /**
     * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
     *
     * @param
     * @return Timestamp 값
     * @see
     */
    public static String getTimeStamp() {

        String pattern = "yyyyMMddhhmmssSSS";

        SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        String rtnStr = sdfCurrent.format(ts.getTime());

        return rtnStr;
    }



    public static String getStringMap(HashMap map, String key){
        try{
            return map.get(key).toString();
        }catch (Exception e){
            log.warn("StringUtil.getStringMap error - key name is [{}]", key);
            return "";
        }
    }

    public static int getIntMap(HashMap map, String key) {
        try{
            return (int) Math.floor(getDoubleMap(map, key));
        }catch (Exception e){
            log.warn("StringUtil.getIntMap error - key name is [{}]", key);
            return 0;
        }
    }

    public static double getDoubleMap(HashMap map, String key){
        try{
            return Double.valueOf(getStringMap(map, key));
        }catch (Exception e){
            log.warn("StringUtil.getDoubleMap error - key name is [{}]", key);
            return 0.0;
        }
    }

    public static boolean getBooleanMap(HashMap map, String key) {
        try{
            return Boolean.valueOf(getStringMap(map, key));
        }catch (Exception e){
            log.warn("StringUtil.getBooleanMap error - key name is [{}]", key);
            return false;
        }
    }

    public static String getProperty(String key){
        return environment.getProperty(key);

    }

    public static boolean isLogin(){
        return !(DalbitUtil.isEmpty(MemberVo.getMyMemNo()) || "anonymousUser".equals(MemberVo.getMyMemNo()) || MemberVo.getMyMemNo().startsWith("8"));
    }

    /**
     * IP 주소 가져오기
     */
    public static String getIp(HttpServletRequest request){
        String clientIp = request.getHeader("Proxy-Client-IP");
        if (clientIp == null) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
            if (clientIp == null) {
                clientIp = request.getHeader("X-Forwarded-For");
                if (clientIp == null) {
                    clientIp = request.getRemoteAddr();
                }
            }
        }
        return clientIp;
    }

    public static String getServerIp(){
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        }catch (Exception e){
            return "";
        }
    }

    public static String setTimestampInJsonOutputVo(){
        StringBuffer sb = new StringBuffer();
        sb.append(getTimeStamp());

        String serverIp = getServerIp();
        if(!isEmpty(serverIp)){
            sb.append("_");
            sb.append(serverIp.substring(serverIp.lastIndexOf(".")+1));
        }
        return sb.toString();
    }

    /**
     * 지역정보, 위도, 경도 가져오기
     */
    public static LocationVo getLocation(HttpServletRequest request){
        return getLocation(getIp(request));
    }
    /**
     * 지역정보, 위도, 경도 가져오기
     */
    public static LocationVo getLocation(String ip){

        String apiResult = RestApiUtil.sendGet(getProperty("geo.location.server.url") + ip);
        LocationVo locationVo = new LocationVo();
        try {
            locationVo = new Gson().fromJson(apiResult, LocationVo.class);
        }catch (Exception e){
            log.warn("StringUtil.getLocation error - ip : [{}], apiResult : [{}]", ip, apiResult);
            locationVo.setRegionName("정보없음");
        }

        return locationVo;
    }
    /**
     * 이미지 path 경로 치환
     */
    public static String replacePath(String path){
        return path.replace(Code.포토_이미지_임시경로.getCode(), Code.포토_이미지_경로.getCode());
    }

    /**
     * 이미지 Done path 경로 치환
     */
    public static String replaceDonePath(String path){
        return path.replace(Code.포토_이미지_경로.getCode(), Code.포토_이미지_임시경로.getCode());
    }

    /**
     * UTC 로 변경
     *
     * @param dt
     * @return
     */
    public static LocalDateTime getUTC(Date dt){
        //return LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of("UTC"));
        return dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * UTC기준 날짜 문자 변환
     *
     * @param dt
     * @return
     */
    public static String getUTCFormat(Date dt){
        return getUTC(dt).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * UTC기준 타임스탬프 변환
     *
     * @param dt
     * @return
     */
    public static long getUTCTimeStamp(Date dt){
        return Timestamp.valueOf(getUTC(dt)).getTime() / 1000;
    }

    /**
     * 로그인 권한
     * @return
     */
    public static Collection<GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    public static Collection<GrantedAuthority> getGuestAuthorities(){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        return authorities;
    }

    public static boolean isEmptyHeaderAuthToken(String header){
        return isEmpty(header) || "undefined".equals(header);
    }

    public static boolean isAnonymousUser(Object principal){
        return isEmpty(principal) || "anonymousUser".equals(principal);
    }

    /**
     * Validation 체크
     */
    public static void throwValidaionException(BindingResult bindingResult) throws GlobalException {

        ValidationResultVo validationResultVo = new ValidationResultVo();
        if(bindingResult.hasErrors()){
            validationResultVo.setSuccess(false);
            List errorList = bindingResult.getAllErrors();

            ArrayList bindingMessageList = new ArrayList();
            for (int i=0; i<bindingResult.getErrorCount(); i++) {
                FieldError fieldError = (FieldError) errorList.get(i);

                bindingMessageList.add("field : " + fieldError.getField()  + ", value : "+ fieldError.getRejectedValue() + ", message : " + fieldError.getDefaultMessage());
            }
            validationResultVo.setValidationMessageDetail(bindingMessageList);
            throw new GlobalException(Status.파라미터오류, null, validationResultVo.getValidationMessageDetail());
        }

    }

    /**
     * 비밀번호 체크
     */
    public static Boolean isPasswordCheck(String password){

        if(DalbitUtil.isEmpty(password)){
            return true;
        }

        boolean isPattern = false;

        String pwPattern_1 = "^[A-Za-z[0-9]]{8,20}$";                         //영문 + 숫자
        String pwPattern_2 = "^[[0-9]!@#$%^&*()\\-_=+{};:,<.>]{8,20}$";       //숫자 + 특수문자
        String pwPattern_3 = "^[[A-Za-z]!@#$%^&*()\\-_=+{};:,<.>]{8,20}$";    //영문 + 특수문자
        String pwPattern_4 = "^[A-Za-z[0-9]!@#$%^&*()\\-_=+{};:,<.>]{8,20}$";  //영문 + 숫자 + 특수문자

        if(Pattern.matches(pwPattern_1, password) || Pattern.matches(pwPattern_2, password) || Pattern.matches(pwPattern_3, password) || Pattern.matches(pwPattern_4, password)){
            isPattern = true;
        }
        return isPattern;
    }

    /**
     * CORS 방지를 위한 Response 헤더 세팅
     * @param request
     * @param response
     */
    public static void setHeader(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,"+DalbitUtil.getProperty("sso.header.cookie.name")+","+DalbitUtil.getProperty("rest.custom.header.name")+",redirectUrl,Proxy-Client-IP,WL-Proxy-Client-IP,X-Forwarded-For");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    public static String getActiceProfile(){
        return environment.getActiveProfiles()[0];
    }

    public static boolean profileCheck(String serverName){
        return getActiceProfile().equals(serverName);
    }

    public static String getAuthToken(HttpServletRequest request){
        String name = getProperty("sso.header.cookie.name");
        String authToken = request.getHeader(name);
        if(isEmpty(authToken)){
            CookieUtil cookieUtil = new CookieUtil(request);
            if (cookieUtil.exists(name)) {
                try{
                    authToken = cookieUtil.getValue(name);
                }catch(IOException e){
                    log.warn("StringUtil.getAuthToken error - request : [{}]", request);
                    log.warn("StringUtil.getAuthToken error - name : [{}]", name);
                    log.warn("StringUtil.getAuthToken error - authToken : [{}]", authToken);
                }
            }
        }

        return isEmpty(authToken) ? "" : authToken.trim();
    }

    /**
     * 생년월일변환
     */
    public static String getBirth(String year, String month, String day){
        month = (month.length() == 1) ? "0"+month : month;
        day = (day.length() == 1) ? "0"+day : day;

        String birth = year+month+day;

        return birth;

    }

    public static String remove(String str, char remove) throws NullPointerException{
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    public static String validChkDate(String dateStr) {
        String _dateStr = dateStr;

        if (dateStr == null || !(dateStr.trim().length() == 8 || dateStr.trim().length() == 10)) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }
        if (dateStr.length() == 10) {
            _dateStr = remove(dateStr, '-');
        }
        return _dateStr;
    }

    public static String validChkTime(String timeStr) {
        String _timeStr = timeStr;

        if (_timeStr.length() == 5) {
            _timeStr = remove(_timeStr, ':');
        }
        if (_timeStr == null || !(_timeStr.trim().length() == 4)) {
            throw new IllegalArgumentException("Invalid time format: " + _timeStr);
        }

        return _timeStr;
    }

    public static String convertDateTime(String sDate, String sTime, String sFormatStr) {
        String dateStr = validChkDate(sDate);
        String timeStr = validChkTime(sTime);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
        cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));

        SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr, Locale.KOREA);

        return sdf.format(cal.getTime());
    }

    public static String convertDate(String sDate, String sFormatStr) {
        String dateStr = validChkDate(sDate);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));

        SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr, Locale.KOREA);

        return sdf.format(cal.getTime());
    }
}
