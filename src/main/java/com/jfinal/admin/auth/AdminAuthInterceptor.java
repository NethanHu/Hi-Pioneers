/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.auth;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.admin.common.model.Account;
import com.jfinal.admin.login.LoginService;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

/**
 * 后台授权拦截器，已登录并且拥有对当前 action 的访问权限则放行，否则重定向到登录页面
 * 
 * 需要 LoginSessionInterceptor 拦截器先通过 setAttr(LoginService.LOGIN_ACCOUNT, account)
 * 传递 loginAccount 对象
 */
public class AdminAuthInterceptor implements Interceptor {
	
	@Inject
	AdminAuthService srv;
	
	/**
	 * 用于 sharedObject、sharedMethod 扩展中使用
	 */
	private static final ThreadLocal<Account> threadLocal = new ThreadLocal<Account>();
	
	public static Account getThreadLocalAccount() {
		return threadLocal.get();
	}
	
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		
		// 获取 LoginSessionInterceptor 中传递的 loginAccount 对象
		Account loginAccount = c.getAttr(LoginService.LOGIN_ACCOUNT);
		if (loginAccount != null && loginAccount.isStateOk()) {
			// 传递给 sharedObject、sharedMethod 扩展使用
			threadLocal.set(loginAccount);
			
			// 如果是超级管理员或者拥有对当前 action 的访问权限则放行
			if (srv.isSuperAdmin(loginAccount.getId()) ||
				srv.hasPermission(loginAccount.getId(), inv.getActionKey())) {
				inv.invoke();
				return ;
			}
		}
		
		// ajax 请求不能使用 forwardAction(...)、redirect(...)，否则登录界面将被插入到页面右侧
		if (isAjaxRequest(c)) {
			
			String msg;
			if (loginAccount == null) {
				msg = "当前账户已退出登录，请刷新页面并重新登录";
			} else {
				if (loginAccount.isStateLock()) {
					msg = "当前账户已被锁定，请联系管理员";
				} else if (loginAccount.isStateReg()) {
					msg = "当前账户还未激活，请先激活账户";
				} else {
					msg = "当前账户没有操作权限";
				}
			}
			
			c.renderJson(Ret.fail("msg", msg));
			
		} else {
			
			// 未登录转发到登录页面
			if (loginAccount == null) {
				// c.forwardAction("/admin/login");
				c.redirect("/admin/login");
			}
			// 已登录但无权限显示一个提示页面
			else {
				c.render("/_view/admin/common/deny.html");
			}
			
		}
	}
	
	/**
	 * 判断是否为 ajax 请求
	 */
	boolean isAjaxRequest(Controller c) {
		return "XMLHttpRequest".equalsIgnoreCase(c.getRequest().getHeader("X-Requested-With"));
	}
}






