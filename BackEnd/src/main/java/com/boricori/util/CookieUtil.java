package com.boricori.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie createCookie(String name, String value){
      Cookie cookie = new Cookie(name, value);
      cookie.setSecure(true);
      cookie.setHttpOnly(true);
      cookie.setAttribute("SameSite", "lax");
      return cookie;
    }
}
