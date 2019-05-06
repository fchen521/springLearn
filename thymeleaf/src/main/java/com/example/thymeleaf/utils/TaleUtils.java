package com.example.thymeleaf.utils;

import com.example.thymeleaf.constant.WebConst;
import com.example.thymeleaf.model.TUsersPo;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TaleUtils {
    /**
     * 返回当前登录用户
     *
     * @return
     */
    public static TUsersPo getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null == session) {
            return null;
        }
        return (TUsersPo) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
    }

    public static void setCookie(HttpServletResponse response, Integer uid) throws Exception {
        String val = Tools.enAes(uid.toString(), WebConst.AES_SALT);
        boolean isSSL = false;
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, val);
        cookie.setPath("/");
        cookie.setMaxAge(60*30);
        cookie.setSecure(isSSL);
        response.addCookie(cookie);
    }

    /**
     * md5加密
     *
     * @param source 数据源
     * @return 加密字符串
     */
    public static String MD5encode(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
        }
        byte[] encode = messageDigest.digest(source.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte anEncode : encode) {
            String hex = Integer.toHexString(0xff & anEncode);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        System.out.println(TaleUtils.MD5encode("admin"+"admin"));
    }
}
