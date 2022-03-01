/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.common;

import com.jfinal.admin.common.model.Account;
import com.jfinal.admin.login.LoginService;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;

/**
 * 基础控制器，方便获取登录信息。随着业务的增加，可在此添加更多便利方法
 * 
 * 注意：
 *    需要 LoginSessionInterceptor 配合，该拦截器使用
 *    setAttr("loginAccount", ...) 事先注入了登录账户
 *    否则即便已经登录，该控制器也会认为没有登录
 */
public class BaseController extends Controller {
	
	private Account loginAccount;
	
	/**
	 * 在使用 FastControllerFactory 时，需要覆盖父类的 _clear_() 方法清除本类中的所有属性
	 */
	protected void _clear_() {
		this.loginAccount = null;
		super._clear_();
	}
	
	/**
	 * 获取登录账户
	 */
	@NotAction
	public Account getLoginAccount() {
		if (loginAccount == null) {
			loginAccount = getAttr(LoginService.LOGIN_ACCOUNT);
			if (loginAccount != null && ! loginAccount.isStateOk()) {
				throw new IllegalStateException("当前用户状态不允许登录，state = " + loginAccount.getState());
			}
		}
		return loginAccount;
	}
	
	/**
	 * 是否已经登录
	 */
	@NotAction
	public boolean isLogin() {
		return getLoginAccount() != null;
	}
	
	/**
	 * 是否没有登录
	 */
	@NotAction
	public boolean notLogin() {
		return !isLogin();
	}
	
	/**
	 * 获取登录账户id
	 * 确保在 FrontAuthInterceptor 之下使用，或者 isLogin() 为 true 时使用
	 * 也即确定已经是在登录后才可调用
	 */
	@NotAction
	public int getLoginAccountId() {
		return getLoginAccount().getId();
	}
	
	/**
	 * 当前请求是否为 ajax 请求
	 */
	@NotAction
	public boolean isAjaxRequest() {
		return "XMLHttpRequest".equalsIgnoreCase(getHeader("X-Requested-With"));
	}
}


