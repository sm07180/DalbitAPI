package com.dalbit.util;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.common.vo.request.ErrorLogVo;
import com.dalbit.common.vo.request.SelfAuthChkVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.google.gson.Gson;
import com.icert.comm.secu.IcertSecuManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class DalbitUtil {

    private static Environment environment;
    private static CommonService commonService;

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
        if(list != null && list.size() != 0){
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
            log.debug("StringUtil.getSpclStrCnvr NullPointerException - srcString : [{}]", srcString);
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

        String pattern = "yyyyMMddHHmmssSSS";

        SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        String rtnStr = sdfCurrent.format(ts.getTime());

        return rtnStr;
    }

    public static Date getDateMap(HashMap map, String key){
        try{
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = transFormat.parse(map.get(key).toString());
            return date;
        }catch (Exception e){
            log.debug("StringUtil.getDateMap error - key name is [{}]", key);
            return null;
        }
    }



    public static String getStringMap(HashMap map, String key){
        try{
            return map.get(key).toString();
        }catch (Exception e){
            log.debug("StringUtil.getStringMap error - key name is [{}]", key);
            return "";
        }
    }

    public static int getIntMap(HashMap map, String key) {
        try{
            return (int) Math.floor(getDoubleMap(map, key));
        }catch (Exception e){
            log.debug("StringUtil.getIntMap error - key name is [{}]", key);
            return 0;
        }
    }

    public static double getDoubleMap(HashMap map, String key){
        try{
            return Double.valueOf(getStringMap(map, key));
        }catch (Exception e){
            log.debug("StringUtil.getDoubleMap error - key name is [{}]", key);
            return 0.0;
        }
    }

    public static boolean getBooleanMap(HashMap map, String key) {
        try{
            return Boolean.valueOf(getStringMap(map, key));
        }catch (Exception e){
            log.debug("StringUtil.getBooleanMap error - key name is [{}]", key);
            return false;
        }
    }

    public static String getProperty(String key){
        return environment.getProperty(key);

    }

    public static boolean isLogin(HttpServletRequest request){
        String memNo = new MemberVo().getMyMemNo(request);
        return !(DalbitUtil.isEmpty(memNo) || "anonymousUser".equals(memNo) || memNo.startsWith("8"));
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

        sb.append("_");
        sb.append(DalbitUtil.getActiceProfile());
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
            log.debug("StringUtil.getLocation error - ip : [{}], apiResult : [{}]", ip, apiResult);
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

    public static LocalDateTime getUTC(String dt) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            return getUTC(transFormat.parse(dt));
        }catch(ParseException e){}
        return null;
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

    public static String getUTCFormat(String dt){
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            return getUTCFormat(transFormat.parse(dt));
        }catch(ParseException e){}
        return null;
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
    public static long getUTCTimeStamp(String dt){
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            return getUTCTimeStamp(transFormat.parse(dt));
        }catch(ParseException e){}
        return 0;
    }

    /**
     * 로그인 권한
     * @return
     */
    public static Collection<GrantedAuthority> getAuthorities(){
        return setAutorities(new String[]{"ROLE_USER"});
    }

    public static Collection<GrantedAuthority> getGuestAuthorities(){
        return setAutorities(new String[]{"ROLE_ANONYMOUS"});
    }

    public static Collection<GrantedAuthority> getAdminAuthorities(){
        return setAutorities(new String[]{"ROLE_ADMIN", "ROLE_USER"});
    }

    public static Collection<GrantedAuthority> setAutorities(String[] rolNames){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String rol : rolNames) {
            authorities.add(new SimpleGrantedAuthority(rol));
        }
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
    public static void throwValidaionException(BindingResult bindingResult, String methodName) throws GlobalException {

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
            throw new GlobalException(Status.파라미터오류, null, validationResultVo.getValidationMessageDetail(), methodName);
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

    /**
     * 6자리 인증코드 생성
     */
    public static int getSmscode(){
        int code = (int) (Math.random() * 899999) + 100000;
        return code;
    }


    /**
     *  휴대폰 인증 5분제한 체크
     */
    public static boolean isSeconds(long start, long end) {
        long time = (end-start)/1000;
        return (time < 300) ? true : false;
    }

    /**
     * 경험치 비율 계산
     *
     * @param exp
     * @param expBegin
     * @param expNext
     * @return
     */
    public static int getExpRate(int exp, int expBegin, int expNext){
        if((exp - expBegin) > 0) {
            return (int) (((double) (exp - expBegin) / (double) (expNext - expBegin)) * 100);
        }
        return 0;
    }


    /**
     *  Custom-Header 파라미터 세팅
     */
    public static HashMap getParameterMap(HttpServletRequest request){

        String memType = DalbitUtil.convertRequestParamToString(request,"memType");
        String memId = DalbitUtil.convertRequestParamToString(request,"memId");
        String memPwd = DalbitUtil.convertRequestParamToString(request,"memPwd");
        int os = DalbitUtil.convertRequestParamToInteger(request,"os");
        String deviceId = DalbitUtil.convertRequestParamToString(request,"deviceId");
        String deviceToken = DalbitUtil.convertRequestParamToString(request,"deviceToken");
        String appVer = DalbitUtil.convertRequestParamToString(request,"appVer");
        String appAdId = DalbitUtil.convertRequestParamToString(request,"appAdId");

        HashMap map = new HashMap();
        map.put("memType", memType);
        map.put("memId", memId);
        map.put("memPwd", memPwd);
        map.put("os", os);
        map.put("deviceId", deviceId);
        map.put("deviceToken", deviceToken);
        map.put("appVer", appVer);
        map.put("appAdId", appAdId);
        return map;
    }

    /**
     *  날짜 Pattern 형식에 맞춰 현재날짜 계산
     */
    public static String getDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        Calendar cal = Calendar.getInstance();
        return formatter.format(cal.getTime());
    }

    /**
     *  날짜 Pattern 형식, 날짜 '+','-' 계산
     */
    public static String getDate(String pattern, int day) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, day);
        return formatter.format(cal.getTime());
    }

    /**
     * 브라우저 정보 가져오기
     */
    public static String getUserAgent(HttpServletRequest request){
        String userAgent  = request.getHeader("User-Agent");
        String browser;
        if (userAgent .indexOf("MSIE") > -1 || userAgent .indexOf("Trident") > -1) {
            browser = "MSIE";
        } else if (userAgent .indexOf("Opera") > -1) {
            browser =  "Opera";
        } else if (userAgent .indexOf("Firefox") > -1) {
            browser = "Firefox";
        } else if (userAgent .indexOf("Chrome") > -1) {
            browser = "Chrome";
        } else if (userAgent .indexOf("Safari") > -1) {
            browser = "Safari";
        }else {
            browser = "Firefox";
        }
        return browser;
    }

    /**
     * String to Date 패턴변경
     */
    public static String stringToDatePattern(String str, String beforePattern, String afterPattern) throws ParseException{
        SimpleDateFormat beForeFormat = new SimpleDateFormat(beforePattern);
        Date beforeDate = beForeFormat.parse(str);
        SimpleDateFormat afterFormat = new SimpleDateFormat(afterPattern);
        return afterFormat.format(beforeDate);
    }

    /**
     * 휴대폰 유효성 체크
     */
    public static boolean isSmsPhoneNoChk(String phoneNo){
        boolean chk = false;
        if(phoneNo.startsWith("010") || !phoneNo.startsWith("011") || !phoneNo.startsWith("016") || !phoneNo.startsWith("017") || !phoneNo.startsWith("018") || !phoneNo.startsWith("018")){
            chk = true;
        }
        return chk;
    }


    /**
     * 본인인증 요청일자 생성
     */
    public static String getReqDay(){
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String day = sdf.format(today.getTime());
        return day;
    }

    /**
     * 본인인증 요청번호 생성
     */
    public static String getReqNum(String day){
        java.util.Random ran = new Random();
        //랜덤 문자 길이
        int numLength = 6;
        String randomStr = "";

        for (int i = 0; i < numLength; i++) {
            //0 ~ 9 랜덤 숫자 생성
            randomStr += ran.nextInt(10);
        }

        //reqNum은 최대 40byte 까지 사용 가능
        String reqNum = day + randomStr;
        return reqNum;
    }

    /**
     * 본인인증 정보 암호화
     */
    public static String getEncAuthInfo(SelfAuthVo selfAuthVo){

        String extendVar = "0000000000000000";                  // 확장변수
        IcertSecuManager seed  = new IcertSecuManager();

        //02. 1차 암호화 (tr_cert 데이터변수 조합 후 암호화)
        String tr_cert="";
        String enc_tr_cert="";
        tr_cert = selfAuthVo.getCpId() +"/"+ selfAuthVo.getUrlCode() +"/"+ selfAuthVo.getCertNum() +"/"+ selfAuthVo.getDate() +"/"+ selfAuthVo.getCertMet() +"/"+ selfAuthVo.getBirthDay() +"/"+ selfAuthVo.getGender() +"/"+ selfAuthVo.getName() +"/"+ selfAuthVo.getPhoneNo() +"/"+ selfAuthVo.getPhoneCorp() +"/"+ selfAuthVo.getNation() +"/"+ selfAuthVo.getPlusInfo() +"/"+ extendVar;
        enc_tr_cert = seed.getEnc(tr_cert, "");

        //03. 1차 암호화 데이터에 대한 위변조 검증값 생성 (HMAC)
        String hmacMsg = "";
        hmacMsg = seed.getMsg(enc_tr_cert);

        //04. 2차 암호화 (1차 암호화 데이터, HMAC 데이터, extendVar 조합 후 암호화)
        tr_cert  = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");
        return tr_cert;
    }


    /**
     * 본인인증 정보 복호화
     */
    public static SelfAuthSaveVo getDecAuthInfo(SelfAuthChkVo selfAuthChkVo, HttpServletRequest request) throws GlobalException, ParseException{
        //수신된 certNum를 이용하여 복호화
        String rec_cert     = selfAuthChkVo.getRec_cert().trim();  // 결과수신DATA
        String k_certNum    = selfAuthChkVo.getCertNum().trim();   // 파라미터로 수신한 요청번호
        String certNum		= "";			// 요청번호
        String date			= "";			// 요청일시
        String CI	    	= "";			// 연계정보(CI)
        String DI	    	= "";			// 중복가입확인정보(DI)
        String phoneNo		= "";			// 휴대폰번호
        String phoneCorp	= "";			// 이동통신사
        String birthDay		= "";			// 생년월일
        String gender		= "";			// 성별
        String nation		= "";			// 내국인
        String name			= "";			// 성명
        String M_name		= "";			// 미성년자 성명
        String M_birthDay	= "";			// 미성년자 생년월일
        String M_Gender		= "";			// 미성년자 성별
        String M_nation		= "";			// 미성년자 내외국인
        String result		= "";			// 결과값

        String certMet		= "";			// 인증방법
        String ip			= "";			// ip주소
        String plusInfo		= "";
        String encPara		= "";
        String encMsg1		= "";
        String encMsg2		= "";

        IcertSecuManager seed = new IcertSecuManager();

        //02. 1차 복호화
        //수신된 certNum를 이용하여 복호화
        rec_cert  = seed.getDec(rec_cert, k_certNum);

        //03. 1차 파싱
        int inf1 = rec_cert.indexOf("/",0);
        int inf2 = rec_cert.indexOf("/",inf1+1);

        encPara  = rec_cert.substring(0,inf1);         //암호화된 통합 파라미터
        encMsg1  = rec_cert.substring(inf1+1,inf2);    //암호화된 통합 파라미터의 Hash값

        //04. 위변조 검증
        encMsg2  = seed.getMsg(encPara);

        if(!encMsg2.equals(encMsg1)){
            throw new GlobalException(Status.본인인증검증_비정상접근, Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        //05. 2차 복호화
        rec_cert  = seed.getDec(encPara, k_certNum);

        //06. 2차 파싱
        int info1  = rec_cert.indexOf("/",0);
        int info2  = rec_cert.indexOf("/",info1+1);
        int info3  = rec_cert.indexOf("/",info2+1);
        int info4  = rec_cert.indexOf("/",info3+1);
        int info5  = rec_cert.indexOf("/",info4+1);
        int info6  = rec_cert.indexOf("/",info5+1);
        int info7  = rec_cert.indexOf("/",info6+1);
        int info8  = rec_cert.indexOf("/",info7+1);
        int info9  = rec_cert.indexOf("/",info8+1);
        int info10 = rec_cert.indexOf("/",info9+1);
        int info11 = rec_cert.indexOf("/",info10+1);
        int info12 = rec_cert.indexOf("/",info11+1);
        int info13 = rec_cert.indexOf("/",info12+1);
        int info14 = rec_cert.indexOf("/",info13+1);
        int info15 = rec_cert.indexOf("/",info14+1);
        int info16 = rec_cert.indexOf("/",info15+1);
        int info17 = rec_cert.indexOf("/",info16+1);
        int info18 = rec_cert.indexOf("/",info17+1);

        certNum		= rec_cert.substring(0,info1);
        date		= rec_cert.substring(info1+1,info2);
        CI			= rec_cert.substring(info2+1,info3);
        phoneNo		= rec_cert.substring(info3+1,info4);
        phoneCorp	= rec_cert.substring(info4+1,info5);
        birthDay	= rec_cert.substring(info5+1,info6);
        gender		= rec_cert.substring(info6+1,info7);
        nation		= rec_cert.substring(info7+1,info8);
        name		= rec_cert.substring(info8+1,info9);
        result		= rec_cert.substring(info9+1,info10);
        certMet		= rec_cert.substring(info10+1,info11);
        ip			= rec_cert.substring(info11+1,info12);
        M_name		= rec_cert.substring(info12+1,info13);
        M_birthDay	= rec_cert.substring(info13+1,info14);
        M_Gender	= rec_cert.substring(info14+1,info15);
        M_nation	= rec_cert.substring(info15+1,info16);
        plusInfo	= rec_cert.substring(info16+1,info17);
        DI      	= rec_cert.substring(info17+1,info18);

        //07. CI, DI 복호화
        CI  = seed.getDec(CI, k_certNum);
        DI  = seed.getDec(DI, k_certNum);

        String regex = "";
        String msg = "정상";
        if( certNum.length() == 0 || certNum.length() > 40){
            msg = "요청번호 비정상";
        }

        regex = "[0-9]*";
        if( date.length() != 14 || !paramChk(regex, date) ){
            msg = ("요청일시 비정상");
        }

        regex = "[A-Z]*";
        if( certMet.length() != 1 || !paramChk(regex, certMet) ){
            msg = "본인인증방법 비정상" + certMet;
        }

        regex = "[0-9]*";
        if( (phoneNo.length() != 10 && phoneNo.length() != 11) || !paramChk(regex, phoneNo) ){
            msg = "휴대폰번호 비정상";
        }

        regex = "[A-Z]*";
        if( phoneCorp.length() != 3 || !paramChk(regex, phoneCorp) ){
            msg = "이동통신사 비정상";
        }

        regex = "[0-9]*";
        if( birthDay.length() != 8 || !paramChk(regex, birthDay) ){
            msg = "생년월일 비정상";
        }

        regex = "[0-9]*";
        if( gender.length() != 1 || !paramChk(regex, gender) ){
            msg = "성별 비정상";
        }

        regex = "[0-9]*";
        if( nation.length() != 1 || !paramChk(regex, nation) ){
            msg = "내/외국인 비정상";
        }

        regex = "[\\sA-Za-z가-�R.,-]*";
        if( name.length() > 60 || !paramChk(regex, name) ){
            msg = "성명 비정상";
        }

        regex = "[A-Z]*";
        if( result.length() != 1 || !paramChk(regex, result) ){
            msg = "결과값 비정상";
        }

        regex = "[\\sA-Za-z가-?.,-]*";
        if( M_name.length() != 0 ){
            if( M_name.length() > 60 || !paramChk(regex, M_name) ){
                msg = "미성년자 성명 비정상";
            }
        }

        regex = "[0-9]*";
        if( M_birthDay.length() != 0 ){
            if( M_birthDay.length() != 8 || !paramChk(regex, M_birthDay) ){
                msg = "미성년자 생년월일 비정상";
            }
        }

        regex = "[0-9]*";
        if( M_Gender.length() != 0 ){
            if( M_Gender.length() != 1 || !paramChk(regex, M_Gender) ){
                msg = "미성년자 성별 비정상";
            }
        }

        regex = "[0-9]*";
        if( M_nation.length() != 0 ){
            if( M_nation.length() != 1 || !paramChk(regex, M_nation) ){
                msg = "미성년자 내/외국인 비정상";
            }
        }

        // Start - 수신내역 유효성 검증(사설망의 사설 IP로 인해 미사용, 공용망의 경우 확인 후 사용) *********************/
        // 1. date 값 검증
        SimpleDateFormat formatter	= new SimpleDateFormat("yyyyMMddHHmmss",Locale.KOREAN); // 현재 서버 시각 구하기
        String strCurrentTime	= formatter.format(new Date());

        Date toDate				= formatter.parse(strCurrentTime);
        Date fromDate			= formatter.parse(date);
        long timediff			= toDate.getTime()-fromDate.getTime();

        if ( timediff < -30*60*1000 || 30*60*100 < timediff  ){
            msg = "비정상적인 접근입니다. (요청시간경과)";
        }

        // 2. ip 값 검증
        String client_ip = request.getHeader("HTTP_X_FORWARDED_FOR"); // 사용자IP 구하기
        if ( client_ip != null ){
            if( client_ip.indexOf(",") != -1 )
                client_ip = client_ip.substring(0,client_ip.indexOf(","));
        }
        if ( client_ip==null || client_ip.length()==0 ){
            client_ip = request.getRemoteAddr();
        }

        /*if( !client_ip.equals(ip) ){
            msg = "비정상적인 접근입니다. (IP불일치)";
        }*/

        SelfAuthSaveVo selfAuthSaveVo = new SelfAuthSaveVo();
        selfAuthSaveVo.setEncMsg1(encMsg1);
        selfAuthSaveVo.setEncMsg2(encMsg2);
        selfAuthSaveVo.setCertNum(certNum);
        selfAuthSaveVo.setDate(date);
        selfAuthSaveVo.setCI(CI);
        selfAuthSaveVo.setDI(DI);
        selfAuthSaveVo.setPhoneNo(phoneNo);
        selfAuthSaveVo.setPhoneCorp(phoneCorp);
        selfAuthSaveVo.setBirthDay(birthDay);
        selfAuthSaveVo.setNation(nation);
        selfAuthSaveVo.setGender(gender);
        selfAuthSaveVo.setName(name);
        selfAuthSaveVo.setResult(result);
        selfAuthSaveVo.setCertMet(certMet);
        selfAuthSaveVo.setIp(ip);
        selfAuthSaveVo.setM_name(M_name);
        selfAuthSaveVo.setM_birthDay(M_birthDay);
        selfAuthSaveVo.setM_Gender(M_Gender);
        selfAuthSaveVo.setM_nation(M_nation);
        selfAuthSaveVo.setPlusInfo(plusInfo);
        selfAuthSaveVo.setRec_cert(rec_cert);
        selfAuthSaveVo.setMsg(msg);

        return selfAuthSaveVo;
    }


    /**
     * 본인인증 파라미터 유효성 검증
     */
    public static Boolean paramChk(String patn, String param){
        boolean b = true;
        Pattern pattern = Pattern.compile(patn);
        Matcher matcher = pattern.matcher(param);
        b = matcher.matches();
        return b;
    }

    /**
     * 배경이미지 번호 랜덤 추출
     */
    public static String randomBgValue() {
        StringBuffer strPwd = new StringBuffer();
        int[] strs = new int[1];
        for (int i = 0; i < 1; ++i) {
            strs[0] = (int) (Math.random() * 6.0D);
            strPwd.append(strs[0]);
        }
        return strPwd.toString();
    }

    /**
     *  유니코드 인코딩
     */
    public static String encode(String s){
        StringBuffer uni_s = new StringBuffer();
        String temp_s = null;
        for( int i=0 ; i < s.length() ; i++){
            temp_s = Integer.toHexString( s.charAt(i) );
            uni_s.append( "\\u"+(temp_s.length()==4 ? temp_s : "00" + temp_s ) );
        }
        return uni_s.toString();
    }

    /**
     *  유니코드 디코딩
     */
    public static String uniDecode(String uni){
        StringBuffer str = new StringBuffer();
        for( int i= uni.indexOf("\\u") ; i > -1 ; i = uni.indexOf("\\u") ){// euc-kr(%u), utf-8(//u)
            str.append( uni.substring( 0, i ) );
            str.append( String.valueOf( (char)Integer.parseInt( uni.substring( i + 2, i + 6 ) ,16) ) );
            uni = uni.substring( i +6);
        }
        str.append( uni );
        return str.toString();
    }

    /**
     *  html 디코딩
     */
    public static String htmlDecode(String data){
        String decodeStr;
        decodeStr = data.replace("&lt;", "<");
        decodeStr = decodeStr.replace("&gt;", ">");
        decodeStr = decodeStr.replace("&amp;", "&");
        decodeStr = decodeStr.replace("\\\\", "\\");

        return decodeStr;
    }

    public static String escapeCharDecode(String data){
        if(isEmpty(data)){
            return "";
        }
        data = StringUtils.replace(data, "\\\\n", "\\n");
        data = StringUtils.replace(data, "\\\\r", "\\r");
        data = StringUtils.replace(data, "\\\\t", "\\t");
        data = StringUtils.replace(data, "\\\\/", "\\/");
        data = StringUtils.replace(data, "\\\\'", "\\'");
        data = StringUtils.replace(data, "\\\\\"", "\\\"");
        return data;
    }

    /**
     * 금지어 포함 여부
     * str: 금지어 문자열, param: 파라미터
     */
    public static Boolean isStringMatchCheck(String str, String param){
        boolean isMatch = false;
        str = str.replaceAll("\\|\\|", "\\|");
        str = StringUtils.replace(str, "(", "\\(");
        str = StringUtils.replace(str, ")", "\\)");


        try {
            Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(param);
            while (m.find()){
                return isMatch = true;
            }

        } catch (Exception e){
            log.error("금지어 체크 오류 isStringMatchCheck");

            try {
                P_ErrorLogVo errorLogVo = new P_ErrorLogVo();
                errorLogVo.setMem_no("99999999999999");
                errorLogVo.setOs("API");
                errorLogVo.setVersion("");
                errorLogVo.setBuild("");
                errorLogVo.setDtype("banWord");
                errorLogVo.setCtype("금지어 체크 오류");
                errorLogVo.setDesc(param);
                commonService.saveErrorLog(errorLogVo);
            } catch (Exception e1){}

            return isMatch = true;
        }

        return isMatch;
    }

    /**
     * 금지어 *** 변환
     * str: 금지어 문자열, param: 파라미터
     */
    public static String replaceMaskString(String str, String param){
        str = str.replaceAll("\\|\\|", "\\|");
        str = StringUtils.replace(str, "(", "\\(");
        str = StringUtils.replace(str, ")", "\\)");

        StringBuffer sb = new StringBuffer();

        try {

            Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(param);
            while (m.find()){
                m.appendReplacement(sb, maskWord(m.group()));
            }
            m.appendTail(sb);
        } catch (Exception e){
            sb.setLength(0);
            sb.append(param);
            log.error("금지어 변환 오류 replaceMaskString");

            try{
                P_ErrorLogVo errorLogVo = new P_ErrorLogVo();
                errorLogVo.setMem_no("99999999999999");
                errorLogVo.setOs("API");
                errorLogVo.setVersion("");
                errorLogVo.setBuild("");
                errorLogVo.setDtype("banWord");
                errorLogVo.setCtype("금지어 변환 오류");
                errorLogVo.setDesc(param);
                commonService.saveErrorLog(errorLogVo);
            } catch (Exception e1){
            }

        }
        return sb.toString();
    }

    /**
     * 마스킹 유틸
     */
    public static String maskWord(String word) {
        StringBuffer buff = new StringBuffer();
        char[] ch = word.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            /*if (i < 1) {
                buff.append(ch[i]);
            } else {
                buff.append("*");
            }*/
            buff.append("*");
        }
        return buff.toString();
    }


}
