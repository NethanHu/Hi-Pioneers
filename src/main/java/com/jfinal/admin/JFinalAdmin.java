/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin;

import com.jfinal.admin.common.AppConfig;
import com.jfinal.server.undertow.UndertowServer;

/**
 * 启动入口
 */
public class JFinalAdmin {
    public static void main(String[] args) {
        UndertowServer.start(AppConfig.class);
    }
}





