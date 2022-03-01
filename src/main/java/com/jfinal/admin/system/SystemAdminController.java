/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.system;

import com.jfinal.admin.common.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统管理
 */
@Path("/admin/system")
public class SystemAdminController extends BaseController {
	
	@Inject
	SystemService srv;
	
	public void index() {
		render("index.html");
	}
	
	/**
	 * 显示更换头像界面
	 */
	public void avatar() {
		render("avatar.html");
	}
	
	/**
	 * 更换头像
	 */
	public void changeAvatar() {
		renderJson(srv.changeAvatar(getLoginAccountId(), get("avatarData")));
	}
}


