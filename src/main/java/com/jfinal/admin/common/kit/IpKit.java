/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.common.kit;

import javax.servlet.http.HttpServletRequest;

public class IpKit {

    public static String getRealIp(HttpServletRequest request, String defaultValue) {
        String ip = getRealIp(request);
        return isInvalid(ip) ? defaultValue : ip;
    }

    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isInvalid(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalid(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalid(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    static boolean isInvalid(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    public static String getRealIpV2(HttpServletRequest request) {
        String accessIP = request.getHeader("x-forwarded-for");
        if (null == accessIP)
            return request.getRemoteAddr();
        return accessIP;
    }
}
