package com.dalbit.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * cookie util
 */
@Slf4j
public class CookieUtil {

    private Map cookieMap = new java.util.HashMap();

    public CookieUtil(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (!DalbitUtil.isEmpty(cookies)) {
            for (int i = 0; i < cookies.length; i++) {
                cookieMap.put(cookies[i].getName(), cookies[i]);
            }
        }
    }

    public static Cookie createCookie(String name, String value)
            throws IOException {
        return new Cookie(name, URLEncoder.encode(value, "utf-8"));
    }

    public static Cookie createCookie(String name, String value, String path, int maxAge) throws IOException {
        Cookie cookie = new Cookie(name, URLEncoder.encode(value, "utf-8"));
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie createCookie(String name, String value, String domain, String path, int maxAge) throws IOException {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie deleteCookie(String name, String domain, String path, int maxAge) throws IOException {
        Cookie cookie = new Cookie(name, null);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public Cookie getCookie(String name) {
        return (Cookie) cookieMap.get(name);
    }

    public String getValue(String name) throws IOException {
        Cookie cookie = (Cookie) cookieMap.get(name);
        if (DalbitUtil.isEmpty(cookie)){
            return null;
        }
        return URLDecoder.decode(cookie.getValue(), "utf-8");
    }

    public boolean exists(String name) {
        return cookieMap.get(name) != null;
    }
}
