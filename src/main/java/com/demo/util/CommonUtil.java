package com.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class CommonUtil {

    public static boolean isNullBlank(String checkValue) {
        return checkValue == null || "".equals(checkValue);
    }

    public static boolean isNullBlank(Object checkValue) {
        if (checkValue instanceof String) {
            return isNullBlank(String.valueOf(checkValue));
        } else {
            return checkValue == null;
        }
    }

    public static boolean isZeroString(String checkValue) {
        return "0".equals(checkValue);
    }

    public static boolean isNullBlankZero(Object checkValue) {
        if (!(checkValue instanceof String) && !(checkValue instanceof Integer) && !(checkValue instanceof Long)) {
            return checkValue == null;
        } else {
            return isNullBlank(String.valueOf(checkValue)) || isZeroString(String.valueOf(checkValue));
        }
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

    public static String convertToUTF8(String s) {
        String out = null;

        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
            return out;
        } catch (UnsupportedEncodingException var3) {
            return null;
        }
    }

    /**
     * 배열에 값이 있는지 체크
     * @param arr
     * @param targetValue
     * @return
     */
    public static boolean isArrayContain(int[] arr, int targetValue) {
        if(arr == null){
            log.debug("CommonUtils.isArrayContain [int array]:: int[] arr IS NULL");
            return false;
        }
        boolean result = Arrays.stream(arr).anyMatch(i -> i == targetValue);
        log.debug("CommonUtils.isArrayContain [int array]:: {}", result);
        return result;
    }

    /**
     * 배열에 값이 있는지 체크
     * @param arr
     * @param targetValue
     * @return
     */
    public static boolean isArrayContain(String[] arr, String targetValue) {
        boolean result = Arrays.asList(arr).contains(targetValue);
        log.debug("CommonUtils.isArrayContain [String array]:: {}", result);
        return result;
    }


    /**
     * 리스트 값이 있는지 체크
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        if(list == null){
            return true;
        }
        return 0 < list.size() ? true : false;
    }

    /**
     * Object 값이 있는지 체크
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        return object != null ? false : true;
    }
}
