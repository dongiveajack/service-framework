package org.trips.service_framework.utils;

import org.json.JSONObject;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Abhinav Tripathi 04/11/22
 */
public class CookieUtils {
    public static Cookie readCookie(HttpServletRequest request, String cookieName) {
        return WebUtils.getCookie(request, cookieName);
    }

    public static String readValue(Cookie cookie, String key) {
        String cookieValue = cookie.getValue().split("\\.")[1];
        cookieValue = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
        String payload = new String(Base64.getUrlDecoder().decode(cookieValue));
        return new JSONObject(payload).getString(key);
    }
}
