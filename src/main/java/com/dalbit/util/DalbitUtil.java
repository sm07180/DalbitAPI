package com.dalbit.util;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.common.vo.request.SelfAuthChkVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenVo;
import com.google.gson.Gson;
import com.icert.comm.secu.IcertSecuManager;
import com.opentok.*;
import com.opentok.exception.OpenTokException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
    private static JwtUtil jwtUtil;
    private static CommonService commonService;

    @Autowired
    private Environment activeEnvironment;
    @Autowired
    private JwtUtil getJwtUtil;
    @Autowired
    private CommonService getCommonService;

    @PostConstruct
    private void init () {
        environment = this.activeEnvironment;
        jwtUtil = this.getJwtUtil;
        commonService = this.getCommonService;
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
     * ???????????? ?????? ????????? ??????
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * ????????? ?????? ????????? ??????
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){

        if(null == list){
            return true;
        }

        if(list != null && list.size() != 0){
            return false;
        }
        return 0 < list.size() ? false : true;
    }

    public static boolean isEmpty(String[] arr){
        try{
            if(arr != null && arr.length != 0){
                return false;
            }
            return 0 < arr.length ? false : true;
        }catch (NullPointerException e){
            return true;
        }

    }

    /**
     * Object ?????? ????????? ??????
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        return object != null ? false : true;
    }


    /**
     * ????????? ??????
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
     * ????????? ????????? ????????? ??????????????? ????????????.
     * @param date ????????????
     * @return ????????????(true/false)
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
     * <p>???????????? decode ????????? ????????? ????????? ?????? ???????????????.
     * <code>sourStr</code>??? <code>compareStr</code>??? ?????? ?????????
     * <code>returStr</code>??? ????????????, ?????????  <code>defaultStr</code>??? ????????????.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("??????", "??????", null, "bar") = null
     * StringUtil.decode("??????", "??????  ", "foo", null) = null
     * StringUtil.decode("??????", "??????", "foo", "bar") = "foo"
     * StringUtil.decode("??????", "??????  ", "foo", "bar") = "bar"
     * </pre>
     *
     * @param sourceStr ????????? ?????????
     * @param compareStr ?????? ?????? ?????????
     * @param returnStr sourceStr??? compareStr??? ?????? ?????? ??? ????????? ?????????
     * @param defaultStr sourceStr??? compareStr??? ?????? ?????? ??? ????????? ?????????
     * @return sourceStr??? compareStr??? ?????? ??????(equal)??? ??? returnStr??? ????????????,
     *         <br/>????????? defaultStr??? ????????????.
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
     * <p>???????????? decode ????????? ????????? ????????? ?????? ???????????????.
     * <code>sourStr</code>??? <code>compareStr</code>??? ?????? ?????????
     * <code>returStr</code>??? ????????????, ?????????  <code>sourceStr</code>??? ????????????.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo") = "foo"
     * StringUtil.decode("", null, "foo") = ""
     * StringUtil.decode(null, "", "foo") = null
     * StringUtil.decode("??????", "??????", "foo") = "foo"
     * StringUtil.decode("??????", "??????", "foo") = "??????"
     * </pre>
     *
     * @param sourceStr ????????? ?????????
     * @param compareStr ?????? ?????? ?????????
     * @param returnStr sourceStr??? compareStr??? ?????? ?????? ??? ????????? ?????????
     * @return sourceStr??? compareStr??? ?????? ??????(equal)??? ??? returnStr??? ????????????,
     *         <br/>????????? sourceStr??? ????????????.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr) throws NullPointerException{
        return decode(sourceStr, compareStr, returnStr, sourceStr);
    }

    /**
     * ????????? null?????? ???????????? null??? ?????? "" ??? ????????? ?????????
     * @param object ?????? ??????
     * @return resultVal ?????????
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
     * ??????????????? ??? ?????????????????? ??????????????? ????????? ?????? ??????????????? ??????('<' -> & lT)?????? ????????????
     * @param 	srcString 		- '<'
     * @return 	???????????????('<' -> "&lt"
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
     * ?????????????????????????????? ???????????? ???????????? ?????? ???????????????17?????????TIMESTAMP?????? ????????? ??????
     *
     * @param
     * @return Timestamp ???
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


    public static long getLongMap(HashMap map, String key) {
        try{
            return (long) Math.floor(getDoubleMap(map, key));
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
        try{
            return environment.getProperty(key);
        }catch(Exception e){
            return "";
        }
    }

    public static boolean isLogin(HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        String authToken = request.getHeader("authToken");
        if(isLogin(memNo)){
            //?????????????????? ????????? ????????? false??? ?????? ?????? (???????????? ?????? ??????)
            try{
                TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(authToken);
                return tokenVo.isLogin();
            }catch (Exception e){
                return false;
            }
        }else{
            return false;
        }
    }

    public static boolean isLogin(String memNo){
        return !(DalbitUtil.isEmpty(memNo) || "anonymousUser".equals(memNo) || memNo.startsWith("8"));
    }

    /**
     * IP ?????? ????????????
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
        sb.append(DalbitUtil.getActiveProfile());
        return sb.toString();
    }

    /**
     * ????????????, ??????, ?????? ????????????
     */
    public static LocationVo getLocation(HttpServletRequest request){
        return getLocation(getIp(request));
    }
    /**
     * ????????????, ??????, ?????? ????????????
     */
    public static LocationVo getLocation(String ip){

        String apiResult = RestApiUtil.sendGet(getProperty("geo.location.server.url") + ip);
        LocationVo locationVo = new LocationVo();
        try {
            locationVo = new Gson().fromJson(apiResult, LocationVo.class);
        }catch (Exception e){
            log.debug("StringUtil.getLocation error - ip : [{}], apiResult : [{}]", ip, apiResult);
            locationVo.setRegionName("????????????");
        }

        return locationVo;
    }
    /**
     * ????????? path ?????? ??????
     */
    public static String replacePath(String path){
        return path.replace(Code.??????_?????????_????????????.getCode(), Code.??????_?????????_??????.getCode());
    }

    /**
     * ????????? Done path ?????? ??????
     */
    public static String replaceDonePath(String path){
        return path.replace(Code.??????_?????????_??????.getCode(), Code.??????_?????????_????????????.getCode());
    }

    /**
     * UTC ??? ??????
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
     * UTC?????? ?????? ?????? ??????
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

    public static String convertDateFormat(Date date, String format){
        format = isEmpty(format) ? "yyyyMMddHHmmss" : format;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * UTC?????? ??????????????? ??????
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
     * ????????? ??????
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
     * Validation ??????
     */
    public static void throwValidaionException(BindingResult bindingResult, String methodName) throws GlobalException {

        ValidationResultVo validationResultVo = new ValidationResultVo();
        if(bindingResult.hasErrors()){
            validationResultVo.setSuccess(false);
            List errorList = bindingResult.getAllErrors();

            ArrayList bindingMessageList = new ArrayList();
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:messages/validation");
            messageSource.setDefaultEncoding("UTF-8");
            messageSource.setUseCodeAsDefaultMessage(true);

            for (int i=0; i<bindingResult.getErrorCount(); i++) {
                FieldError fieldError = (FieldError) errorList.get(i);
                String message = null;
                if("Password".equals(fieldError.getCode())) {
                    message = fieldError.getDefaultMessage();
                }else {
                    String fieldName = "";
                    String field = fieldError.getDefaultMessage();
                    if (!isEmpty(field)) {
                        try {
                            HashMap<String, String> fieldMap = new Gson().fromJson(field, HashMap.class);
                            if (!isEmpty(fieldMap) && fieldMap.containsKey(LocaleContextHolder.getLocale().toString())) {
                                fieldName = getStringMap(fieldMap, LocaleContextHolder.getLocale().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (!isEmpty(fieldName)) {
                        String validation_message_key = "";
                        List argList = new ArrayList();
                        argList.add(fieldName);
                        if ("NotBlank".equals(fieldError.getCode())) {
                            validation_message_key="validation.not_blank";
                        } else if ("NotNull".equals(fieldError.getCode())) {
                            validation_message_key="validation.not_blank";
                        } else if ("Min".equals(fieldError.getCode())) {
                            if(fieldError.getArguments().length == 2){
                                validation_message_key="validation.min";
                                argList.add(fieldError.getArguments()[1]);
                            }
                        } else if ("Max".equals(fieldError.getCode())) {
                            if(fieldError.getArguments().length == 2){
                                validation_message_key="validation.max";
                                argList.add(fieldError.getArguments()[1]);
                            }
                        } else if ("Size".equals(fieldError.getCode())) {
                            if(fieldError.getArguments().length == 3) {
                                if((int)fieldError.getArguments()[1] < 2147483647 && (int)fieldError.getArguments()[2] > 0 && (int)fieldError.getArguments()[1] == (int)fieldError.getArguments()[2]){
                                    validation_message_key="validation.size_same";
                                    argList.add(fieldError.getArguments()[1]);
                                }else if((int)fieldError.getArguments()[1] < 2147483647 && (int)fieldError.getArguments()[2] > 0 ){
                                    validation_message_key="validation.size";
                                    argList.add(fieldError.getArguments()[2]); //?????????
                                    argList.add(fieldError.getArguments()[1]); //?????????
                                }else if((int)fieldError.getArguments()[1] == 2147483647 && (int)fieldError.getArguments()[2] > 0){
                                    validation_message_key="validation.size_min";
                                    argList.add(fieldError.getArguments()[2]);
                                }else if((int)fieldError.getArguments()[1] < 2147483647 && (int)fieldError.getArguments()[2] == 0){
                                    validation_message_key="validation.size_max";
                                    argList.add(fieldError.getArguments()[1]);
                                }
                            }
                        } else if ("Pattern".equals(fieldError.getCode())) {
                            validation_message_key="validation.pattern";
                            argList.add(fieldError.getArguments()[1]);
                        }
                        if (!isEmpty(validation_message_key)) {
                            message = messageSource.getMessage(validation_message_key, argList.toArray(), LocaleContextHolder.getLocale());
                        }
                    }

                    if (isEmpty(message)) {
                        message = "param : " + fieldError.getField() + ", value : " + fieldError.getRejectedValue() + ", message : " + fieldError.getDefaultMessage();
                    }
                }
                bindingMessageList.add(message);
            }
            validationResultVo.setValidationMessageDetail(bindingMessageList);
            throw new GlobalException(CommonStatus.?????????????????????, null, validationResultVo == null ? new ArrayList() : validationResultVo.getValidationMessageDetail(), methodName, true);
        }

    }

    /**
     * ???????????? ??????
     */
    public static Boolean isPasswordCheck(String password){

        if(DalbitUtil.isEmpty(password)){
            return true;
        }

        boolean isPattern = false;

        String pwPattern_1 = "^[A-Za-z[0-9]]{8,20}$";                         //?????? + ??????
        String pwPattern_2 = "^[[0-9]!@#$%^&*()\\-_=+{};:,<.>]{8,20}$";       //?????? + ????????????
        String pwPattern_3 = "^[[A-Za-z]!@#$%^&*()\\-_=+{};:,<.>]{8,20}$";    //?????? + ????????????
        String pwPattern_4 = "^[A-Za-z[0-9]!@#$%^&*()\\-_=+{};:,<.>]{8,20}$";  //?????? + ?????? + ????????????

        if(Pattern.matches(pwPattern_1, password) || Pattern.matches(pwPattern_2, password) || Pattern.matches(pwPattern_3, password) || Pattern.matches(pwPattern_4, password)){
            isPattern = true;
        }
        return isPattern;
    }

    /**
     * CORS ????????? ?????? Response ?????? ??????
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

    public static String getActiveProfile(){
        return environment.getActiveProfiles()[0];
    }

    public static boolean profileCheck(String serverName){
        return getActiveProfile().equals(serverName);
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
     * ??????????????????
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
     * 6?????? ???????????? ??????
     */
    public static int getSmscode(){
        int code = (int) (Math.random() * 899999) + 100000;
        return code;
    }


    /**
     *  ????????? ?????? 5????????? ??????
     */
    public static boolean isSeconds(long start, long end) {
        long time = (end-start)/1000;
        return time < 300;
    }

    /**
     * ????????? ?????? ??????
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
     *  Custom-Header ???????????? ??????
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
     *  ?????? Pattern ????????? ?????? ???????????? ??????
     */
    public static String getDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        Calendar cal = Calendar.getInstance();
        return formatter.format(cal.getTime());
    }

    /**
     *  ?????? Pattern ??????, ?????? '+','-' ??????
     */
    public static String getDate(String pattern, int day) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, day);
        return formatter.format(cal.getTime());
    }

    /**
     * ???????????? ?????? ????????????
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String browser;
        if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1) {
            browser = "MSIE";
        } else if (userAgent.indexOf("Opera") > -1) {
            browser =  "Opera";
        } else if (userAgent.indexOf("Firefox") > -1) {
            browser = "Firefox";
        } else if (userAgent.indexOf("Edg") > -1) {
            browser = "Edge";
        } else if (userAgent.indexOf("Chrome") > -1) {
            DeviceVo deviceVo = new DeviceVo(request);
            if(deviceVo.getOs() == 1){
                browser = "WebView";
            }else{
                browser = "Chrome";
            }
        } else if (userAgent.indexOf("Safari") > -1) {
            browser = "Safari";
        }else if (userAgent.indexOf("AppleWebKit") > -1) { //??????
            browser = "WebView";
        }else {
            browser = "Firefox";
        }
        return browser;
    }

    /**
     * String to Date ????????????
     */
    public static String stringToDatePattern(String str, String beforePattern, String afterPattern) throws ParseException{
        SimpleDateFormat beForeFormat = new SimpleDateFormat(beforePattern);
        Date beforeDate = beForeFormat.parse(str);
        SimpleDateFormat afterFormat = new SimpleDateFormat(afterPattern);
        return afterFormat.format(beforeDate);
    }

    /**
     * ????????? ????????? ??????
     */
    public static boolean isSmsPhoneNoChk(String phoneNo){
        boolean chk = false;
        if(phoneNo.startsWith("010") || !phoneNo.startsWith("011") || !phoneNo.startsWith("016") || !phoneNo.startsWith("017") || !phoneNo.startsWith("018") || !phoneNo.startsWith("018")){
            chk = true;
        }
        return chk;
    }


    /**
     * ???????????? ???????????? ??????
     */
    public static String getReqDay(){
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String day = sdf.format(today.getTime());
        return day;
    }

    /**
     * ???????????? ???????????? ??????
     */
    public static String getReqNum(String day){
        java.util.Random ran = new Random();
        //?????? ?????? ??????
        int numLength = 6;
        String randomStr = "";

        for (int i = 0; i < numLength; i++) {
            //0 ~ 9 ?????? ?????? ??????
            randomStr += ran.nextInt(10);
        }

        //reqNum??? ?????? 40byte ?????? ?????? ??????
        String reqNum = day + randomStr;
        return reqNum;
    }

    /**
     * ???????????? ?????? ?????????
     */
    public static String getEncAuthInfo(SelfAuthVo selfAuthVo){

        String extendVar = "0000000000000000";                  // ????????????
        IcertSecuManager seed  = new IcertSecuManager();

        //02. 1??? ????????? (tr_cert ??????????????? ?????? ??? ?????????)
        String tr_cert="";
        String enc_tr_cert="";
        tr_cert = selfAuthVo.getCpId() +"/"+ selfAuthVo.getUrlCode() +"/"+ selfAuthVo.getCertNum() +"/"+ selfAuthVo.getDate() +"/"+ selfAuthVo.getCertMet() +"/"+ selfAuthVo.getBirthDay() +"/"+ selfAuthVo.getGender() +"/"+ selfAuthVo.getName() +"/"+ selfAuthVo.getPhoneNo() +"/"+ selfAuthVo.getPhoneCorp() +"/"+ selfAuthVo.getNation() +"/"+ selfAuthVo.getPlusInfo() +"/"+ extendVar;
        enc_tr_cert = seed.getEnc(tr_cert, "");

        //03. 1??? ????????? ???????????? ?????? ????????? ????????? ?????? (HMAC)
        String hmacMsg = "";
        hmacMsg = seed.getMsg(enc_tr_cert);

        //04. 2??? ????????? (1??? ????????? ?????????, HMAC ?????????, extendVar ?????? ??? ?????????)
        tr_cert  = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");
        return tr_cert;
    }


    /**
     * ???????????? ?????? ?????????
     */
    public static SelfAuthSaveVo getDecAuthInfo(SelfAuthChkVo selfAuthChkVo, HttpServletRequest request) throws GlobalException, ParseException{
        //????????? certNum??? ???????????? ?????????
        String rec_cert     = selfAuthChkVo.getRec_cert().trim();  // ????????????DATA
        String k_certNum    = selfAuthChkVo.getCertNum().trim();   // ??????????????? ????????? ????????????
        String certNum		= "";			// ????????????
        String date			= "";			// ????????????
        String CI	    	= "";			// ????????????(CI)
        String DI	    	= "";			// ????????????????????????(DI)
        String phoneNo		= "";			// ???????????????
        String phoneCorp	= "";			// ???????????????
        String birthDay		= "";			// ????????????
        String gender		= "";			// ??????
        String nation		= "";			// ?????????
        String name			= "";			// ??????
        String M_name		= "";			// ???????????? ??????
        String M_birthDay	= "";			// ???????????? ????????????
        String M_Gender		= "";			// ???????????? ??????
        String M_nation		= "";			// ???????????? ????????????
        String result		= "";			// ?????????

        String certMet		= "";			// ????????????
        String ip			= "";			// ip??????
        String plusInfo		= "";
        String encPara		= "";
        String encMsg1		= "";
        String encMsg2		= "";

        IcertSecuManager seed = new IcertSecuManager();

        //02. 1??? ?????????
        //????????? certNum??? ???????????? ?????????
        rec_cert  = seed.getDec(rec_cert, k_certNum);

        //03. 1??? ??????
        int inf1 = rec_cert.indexOf("/",0);
        int inf2 = rec_cert.indexOf("/",inf1+1);

        encPara  = rec_cert.substring(0,inf1);         //???????????? ?????? ????????????
        encMsg1  = rec_cert.substring(inf1+1,inf2);    //???????????? ?????? ??????????????? Hash???

        //04. ????????? ??????
        encMsg2  = seed.getMsg(encPara);

        if(!encMsg2.equals(encMsg1)){
            throw new GlobalException(MemberStatus.??????????????????_???????????????, Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        //05. 2??? ?????????
        rec_cert  = seed.getDec(encPara, k_certNum);

        //06. 2??? ??????
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

        //07. CI, DI ?????????
        CI  = seed.getDec(CI, k_certNum);
        DI  = seed.getDec(DI, k_certNum);

        String regex = "";
        String msg = "??????";
        if( certNum.length() == 0 || certNum.length() > 40){
            msg = "???????????? ?????????";
        }

        regex = "[0-9]*";
        if( date.length() != 14 || !paramChk(regex, date) ){
            msg = ("???????????? ?????????");
        }

        regex = "[A-Z]*";
        if( certMet.length() != 1 || !paramChk(regex, certMet) ){
            msg = "?????????????????? ?????????" + certMet;
        }

        regex = "[0-9]*";
        if( (phoneNo.length() != 10 && phoneNo.length() != 11) || !paramChk(regex, phoneNo) ){
            msg = "??????????????? ?????????";
        }

        regex = "[A-Z]*";
        if( phoneCorp.length() != 3 || !paramChk(regex, phoneCorp) ){
            msg = "??????????????? ?????????";
        }

        regex = "[0-9]*";
        if( birthDay.length() != 8 || !paramChk(regex, birthDay) ){
            msg = "???????????? ?????????";
        }

        regex = "[0-9]*";
        if( gender.length() != 1 || !paramChk(regex, gender) ){
            msg = "?????? ?????????";
        }

        regex = "[0-9]*";
        if( nation.length() != 1 || !paramChk(regex, nation) ){
            msg = "???/????????? ?????????";
        }

        regex = "[\\sA-Za-z???-???R.,-]*";
        if( name.length() > 60 || !paramChk(regex, name) ){
            msg = "?????? ?????????";
        }

        regex = "[A-Z]*";
        if( result.length() != 1 || !paramChk(regex, result) ){
            msg = "????????? ?????????";
        }

        regex = "[\\sA-Za-z???-?.,-]*";
        if( M_name.length() != 0 ){
            if( M_name.length() > 60 || !paramChk(regex, M_name) ){
                msg = "???????????? ?????? ?????????";
            }
        }

        regex = "[0-9]*";
        if( M_birthDay.length() != 0 ){
            if( M_birthDay.length() != 8 || !paramChk(regex, M_birthDay) ){
                msg = "???????????? ???????????? ?????????";
            }
        }

        regex = "[0-9]*";
        if( M_Gender.length() != 0 ){
            if( M_Gender.length() != 1 || !paramChk(regex, M_Gender) ){
                msg = "???????????? ?????? ?????????";
            }
        }

        regex = "[0-9]*";
        if( M_nation.length() != 0 ){
            if( M_nation.length() != 1 || !paramChk(regex, M_nation) ){
                msg = "???????????? ???/????????? ?????????";
            }
        }

        // Start - ???????????? ????????? ??????(???????????? ?????? IP??? ?????? ?????????, ???????????? ?????? ?????? ??? ??????) *********************/
        // 1. date ??? ??????
        SimpleDateFormat formatter	= new SimpleDateFormat("yyyyMMddHHmmss",Locale.KOREAN); // ?????? ?????? ?????? ?????????
        String strCurrentTime	= formatter.format(new Date());

        Date toDate				= formatter.parse(strCurrentTime);
        Date fromDate			= formatter.parse(date);
        long timediff			= toDate.getTime()-fromDate.getTime();

        if ( timediff < -30*60*1000 || 30*60*100 < timediff  ){
            msg = "??????????????? ???????????????. (??????????????????)";
        }

        // 2. ip ??? ??????
        String client_ip = request.getHeader("HTTP_X_FORWARDED_FOR"); // ?????????IP ?????????
        if ( client_ip != null ){
            if( client_ip.indexOf(",") != -1 )
                client_ip = client_ip.substring(0,client_ip.indexOf(","));
        }
        if ( client_ip==null || client_ip.length()==0 ){
            client_ip = request.getRemoteAddr();
        }

        /*if( !client_ip.equals(ip) ){
            msg = "??????????????? ???????????????. (IP?????????)";
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
     * ???????????? ???????????? ????????? ??????
     */
    public static Boolean paramChk(String patn, String param){
        boolean b = true;
        Pattern pattern = Pattern.compile(patn);
        Matcher matcher = pattern.matcher(param);
        b = matcher.matches();
        return b;
    }

    /**
     * ??????????????? ?????? ?????? ??????
     */
    public static String randomBgValue() {
        StringBuffer strPwd = new StringBuffer();
        int[] strs = new int[1];
        for (int i = 0; i < 1; ++i) {
            strs[0] = (int) (Math.random() * 5.0D);
            strPwd.append(strs[0]);
        }
        return strPwd.toString();
    }

    /**
     * ?????? ??????????????? ?????? ?????? ??????
     */
    public static String randomClipBgValue() {
        StringBuffer strPwd = new StringBuffer();
        int[] strs = new int[1];
        for (int i = 0; i < 1; ++i) {
            strs[0] = (int) (Math.random() * 2.0D);
            strPwd.append(strs[0]);
        }
        return strPwd.toString();
    }

    /**
     *  ???????????? ?????????
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
     *  ???????????? ?????????
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
     *  html ?????????
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
        data = StringUtils.replace(data, "\\u003d", "=");
        //data = StringUtils.replace(data, "\\\\\"", "\\\"");
        return data;
    }

    public static boolean isCheckSlash(String data){
        if(isEmpty(data.trim())){
            return true;
        }

        data = StringUtils.replace(data, "\\\\n", "\\n");
        data = StringUtils.replace(data, "\\\\r", "\\r");
        data = StringUtils.replace(data, "\\\\t", "\\t");
        data = StringUtils.replace(data, "\\\\u202E", "\\u202E");

        if("".equals(data.trim())){
            return true;
        }
        if(data.indexOf("\\n") >= 0 || (data.indexOf("\n") >= 0)){
            return true;
        }
        if(data.indexOf("\\t") >= 0 || data.indexOf("\t") >= 0){
            return true;
        }
        if(data.indexOf("\\r") >= 0 || data.indexOf("\r") >= 0){
            return true;
        }
        if(data.indexOf("\\u202E") >= 0 || data.indexOf("\u202E") >= 0){

        }
        if(data.indexOf("???") >= 0){
            return true;
        }
        if(data.indexOf("???") >= 0){
            return true;
        }
        if(data.indexOf("???") >= 0){
            return true;
        }
        if(data.indexOf("\\u00AD") >= 0 || data.indexOf("\u00AD") >= 0){
            return true;
        }

        return false;
    }

    /**
     * ????????? ?????? ??????
     * str: ????????? ?????????, param: ????????????
     */
    public static Boolean isStringMatchCheck(String str, String param){
        boolean isMatch = false;
        str = str.replaceAll("\\|\\|", "\\|");
        str = str.replace("?", "0X01");
        str = str.replace("+", "0X02");
        str = str.replace("*", "0X03");
        str = str.replace(".", "0X04");
        str = str.replace("^", "0X05");
        str = str.replace("(", "0X06");
        str = str.replace(")", "0X07");
        str = str.replace("[", "0X08");
        str = str.replace("]", "0X09");
        str = str.replace("{", "0X10");
        str = str.replace("}", "0X11");
        str = str.replace("'", "0X12");
        str = str.replace("\"", "0X13");
        str = str.replace("\\\\", "0X14");

        param = param.replaceAll("\\|\\|", "\\|");
        param = param.replace("?", "0X01");
        param = param.replace("+", "0X02");
        param = param.replace("*", "0X03");
        param = param.replace(".", "0X04");
        param = param.replace("^", "0X05");
        param = param.replace("(", "0X06");
        param = param.replace(")", "0X07");
        param = param.replace("[", "0X08");
        param = param.replace("]", "0X09");
        param = param.replace("{", "0X10");
        param = param.replace("}", "0X11");
        param = param.replace("'", "0X12");
        param = param.replace("\"", "0X13");
        param = param.replace("\\\\", "0X14");

        try {
            Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE); //???????????? ????????????
            Matcher m = p.matcher(param);
            while (m.find()){
                return isMatch = true;
            }
        } catch (Exception e){
            log.error("????????? ?????? ?????? isStringMatchCheck");

            try {
                P_ErrorLogVo errorLogVo = new P_ErrorLogVo();
                errorLogVo.setMem_no("99999999999999");
                errorLogVo.setOs("API");
                errorLogVo.setVersion("");
                errorLogVo.setBuild("");
                errorLogVo.setDtype("banWord");
                errorLogVo.setCtype("????????? ?????? ??????");
                errorLogVo.setDesc(param);
                commonService.saveErrorLog(errorLogVo);
            } catch (Exception e1){}

            return isMatch = true;
        }

        return isMatch;
    }

    /**
     * ????????? *** ??????
     * str: ????????? ?????????, param: ????????????
     */
    public static String replaceMaskString(String str, String param){
        str = str.replaceAll("\\|\\|", "\\|");
        str = str.replaceAll("\\|\\|", "\\|");
        str = str.replace("?", "0X01");
        str = str.replace("+", "0X02");
        str = str.replace("*", "0X03");
        str = str.replace(".", "0X04");
        str = str.replace("^", "0X05");
        str = str.replace("(", "0X06");
        str = str.replace(")", "0X07");
        str = str.replace("[", "0X08");
        str = str.replace("]", "0X09");
        str = str.replace("{", "0X10");
        str = str.replace("}", "0X11");
        str = str.replace("'", "0X12");
        str = str.replace("\"", "0X13");
        str = str.replace("\\\\", "0X14");

        param = param.replaceAll("\\|\\|", "\\|");
        param = param.replace("?", "0X01");
        param = param.replace("+", "0X02");
        param = param.replace("*", "0X03");
        param = param.replace(".", "0X04");
        param = param.replace("^", "0X05");
        param = param.replace("(", "0X06");
        param = param.replace(")", "0X07");
        param = param.replace("[", "0X08");
        param = param.replace("]", "0X09");
        param = param.replace("{", "0X10");
        param = param.replace("}", "0X11");
        param = param.replace("'", "0X12");
        param = param.replace("\"", "0X13");
        param = param.replace("\\\\", "0X14");

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
            log.error("????????? ?????? ?????? replaceMaskString");

            try{
                P_ErrorLogVo errorLogVo = new P_ErrorLogVo();
                errorLogVo.setMem_no("99999999999999");
                errorLogVo.setOs("API");
                errorLogVo.setVersion("");
                errorLogVo.setBuild("");
                errorLogVo.setDtype("banWord");
                errorLogVo.setCtype("????????? ?????? ??????");
                errorLogVo.setDesc(param);
                commonService.saveErrorLog(errorLogVo);
            } catch (Exception e1){
            }

        }

        return sb.toString().replaceAll("0X01", "?")
                            .replaceAll("0X02", "+")
                            .replaceAll("0X03", "*")
                            .replaceAll("0X04", ".")
                            .replaceAll("0X05", "^")
                            .replaceAll("0X06", "(")
                            .replaceAll("0X07", ")")
                            .replaceAll("0X08", "[")
                            .replaceAll("0X09", "]")
                            .replaceAll("0X10", "{")
                            .replaceAll("0X11", "}")
                            .replaceAll("0X12", "'")
                            .replaceAll("0X13", "\"")
                            .replaceAll("0X14", "\\\\");
    }

    /**
     * ????????? ??????
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

    /**
     * ??? ?????? ?????????
     */
    public static int getAge(int birthYear, int birthMonth, int birthDay) {
        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // ?????? ??? ?????? ?????? -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age;
    }

    /**
     * KOR ?????? ?????????
     */
    public static int getKorAge(int birthYear) {
        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int korAge = currentYear - birthYear + 1;
        return korAge;
    }

    public static int isWowza(DeviceVo deviceVo){
        /*try {
            if ( ((deviceVo.getOs() == 1 && (Integer.parseInt(deviceVo.getAppBuild()) >= 25)))
                    || (deviceVo.getOs() == 2 && Integer.parseInt(deviceVo.getAppBuild()) >= 107)
                || (deviceVo.getOs() == 3  && !"real".equals(getActiveProfile()))) {
                return 1;
            }
        }catch(Exception e){}*/
        return 1;
    }

    public static boolean versionCompare(String str1, String str2) {
        try ( Scanner s1 = new Scanner(str1);
              Scanner s2 = new Scanner(str2);) {
            s1.useDelimiter("\\.");
            s2.useDelimiter("\\.");

            while (s1.hasNextInt() && s2.hasNextInt()) {
                int v1 = s1.nextInt();
                int v2 = s2.nextInt();
                if (v1 < v2) {
                    return false;
                } else if (v1 > v2) {
                    return true;
                }
            }

            if (s1.hasNextInt() && s1.nextInt() != 0)
                return true; //str1 has an additional lower-level version number
            if (s2.hasNextInt() && s2.nextInt() != 0)
                return false; //str2 has an additional lower-level version

            return false;
        }
    }

    /**
     * ????????? ??????????????? ?????? ?????? ??????
     */
    public static String randomMoonAniValue() {
        StringBuffer strPwd = new StringBuffer();
        int[] strs = new int[1];
        for (int i = 0; i < 1; ++i) {
            strs[0] = (int) (Math.random() * 3.0D);
            strPwd.append(strs[0]);
        }
        return strPwd.toString();
    }

    public static String getListenRoomNo(String roomNo, int listenOpen, boolean isAdmin){
        if(!isEmpty(roomNo)){
            if(isAdmin){
                return roomNo;
            }
            if(listenOpen == 1) {
                return roomNo;
            }else if(listenOpen == 2){
                return "";
            }else{
                return "A000000000";
            }
        }
        return "";
    }

    public static OpenTokVo getOpenTok(){
        return getOpenTok(null);
    }

    public static OpenTokVo getOpenTok(String sessionId){
        OpenTok openTok = null;
        OpenTokVo openTokVo = new OpenTokVo();
        int apiKey = Integer.parseInt(getProperty("open.tok.app.key"));
        String apiSecret = getProperty("open.tok.app.secret");
        //int expireHour = 2;
        try{
            openTok = new OpenTok(apiKey, apiSecret);
            if(isEmpty(sessionId)){
                Session session = openTok.createSession(new SessionProperties.Builder()
                        .mediaMode(MediaMode.RELAYED)
                        .archiveMode(ArchiveMode.MANUAL)
                        .build()
                );
                openTokVo.setApiKey(String.valueOf(session.getApiKey()));
                openTokVo.setSessionId(session.getSessionId());
                openTokVo.setToken(session.generateToken(new TokenOptions.Builder()
                        //.expireTime((System.currentTimeMillis() / 1000L) + (expireHour * 60 * 60))
                        .build()
                ));
            }else{
                openTokVo.setApiKey(String.valueOf(apiKey));
                openTokVo.setSessionId(sessionId);
                openTokVo.setToken(openTok.generateToken(sessionId, new TokenOptions.Builder()
                        //.expireTime((System.currentTimeMillis() / 1000L) + (expireHour * 60 * 60))
                        .build()
                ));
            }
        }catch(OpenTokException e){
        }finally {
            if(openTok != null){
                openTok.close();
            }
        }
        return openTokVo;
    }

    public static String getLevelFrameBg(int level) {
        String result = "";
        if(level <= 100) {
            int l = (level - 1) / 10;
            if(l > 4){
                result = StringUtils.replace(DalbitUtil.getProperty("level.frame.bg"),"[level]", l + "");
            }
        }else {
            result = StringUtils.replace(DalbitUtil.getProperty("level.frame.bg2"),"[level]", level + "");
        }
        return result;
    }
}
