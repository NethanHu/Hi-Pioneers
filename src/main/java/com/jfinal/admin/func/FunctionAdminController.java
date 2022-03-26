/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.func;

import com.jfinal.admin.common.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 演示 jfinal-kit.js 提供的极简、便利、模块化、前后分离的交互功能
 */
@Path("/admin/func")
public class FunctionAdminController extends BaseController {

    @Inject
    FunctionAdminService srv;

    public void index() {
        render("index.html");
    }

    /**
     * 获取订单总数
     */
    public void getTotalOrdersToday() {
        renderJson(srv.getTotalOrdersToday());
    }

    /**
     * 清除过期 session
     */
    public void clearExpiredSession() {
        renderJson(srv.clearExpiredSession());
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        renderJson(srv.clearCache());
    }

    /**
     * 传递数据
     */
    public void passData() {
        renderJson(srv.passData(get("k1"), get("k2")));
    }

    /**
     * 切换账户
     */
    public void switchAccount() {
        renderJson(srv.switchAccount(getLoginAccount(), getInt("value")));
    }

    /**
     * 重启
     */
    public void restart() {
        renderJson(srv.restart());
    }
}





