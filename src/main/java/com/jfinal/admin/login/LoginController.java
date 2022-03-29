/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.login;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.kit.IpKit;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;

import static com.jfinal.admin.login.LoginService.LOGIN_ACCOUNT;
import static com.jfinal.admin.login.LoginService.SESSION_ID;

/**
 * 登录控制层
 */
@Path(value = "/admin", viewPath = "/admin/login" )
public class LoginController extends BaseController {

    @Inject
    LoginService srv;

    /**
     * 显示登录界面
     */
    @Clear
    public void login() {
        render("index.html");
    }

    /**
     * 登录
     */
    @Clear
    public void doLogin() {
        String ip = IpKit.getRealIp(getRequest(), "127.0.0.1");
        int port = getRequest().getServerPort();
        Ret ret = srv.login(get("userName"), get("password"), ip, port);
        if (ret.isOk()) {
            // cookie 写入 sessionId
            setCookie(SESSION_ID, ret.getStr(SESSION_ID), ret.getInt("timeToLiveSeconds"), true);

            // 登录成功的 account 对象传递到 enjoy 模板等等需要使用的地方
            set(LOGIN_ACCOUNT, ret.get(LOGIN_ACCOUNT));

            // 如果 returnUrl 存在则跳过去，否则跳去首页
            ret.set("returnUrl", getPara("returnUrl", "/admin"));
        }
        renderJson(ret);
    }

    /**
     * 退出登录
     */
    @Clear
    public void logout() {
        Ret ret = srv.logout(getCookie(SESSION_ID));
        removeCookie(SESSION_ID);
        renderJson(ret);
    }

    /**
     * 如果需要添加验证码功能，在 html 中通过 img 标签的 src 属性指向该 action：
     *   <img src="/admin/login/captcha" onclick="updateCaptcha(this);">
     *
     * 后端在 Controller 中判断验证码是否正确：
     *    if ( ! validateCaptcha("captcha") ) {
     *         renderJson(Ret.fail("msg", "验证码不正确"));
     *         return ;
     *    }
     *  以上代码中的 "captcha" 与页面 form 表单中输入验证码 input 的 name
     *  属性值需要相同：
     *    <input type="text" name="captcha">
     *  该 name 属性名可以任意取名，不必要求是 captcha
     */
    @Clear
    public void captcha() {
        renderCaptcha();
    }

    /**
     * 注册页面
     */
    @Clear
    public void register() {
        render("register.html");
    }
}


