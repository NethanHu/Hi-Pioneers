/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.auth;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * 后台管理员授权业务
 */
public class AdminAuthService {

	/**
	 * 是否为超级管理员，role.id 值为 1 的为超级管理员
	 */
	public boolean isSuperAdmin(int accountId) {
		Integer ret = Db.template("admin.auth.isSuperAdmin", accountId).queryInt();
		return ret != null;
	}

	/**
	 * 当前账号是否拥有某些角色
	 */
	public boolean hasRole(int accountId, String[] roleNameArray) {
		if (roleNameArray == null || roleNameArray.length == 0) {
			return false;
		}

		Kv data = Kv.by("accountId", accountId).set("roleNameArray", roleNameArray);
		Integer ret = Db.template("admin.auth.hasRole", data).queryInt();
		return ret != null;
	}

	/**
	 * 是否拥有具体某个权限
	 */
	public boolean hasPermission(int accountId, String actionKey) {
		if (StrKit.isBlank(actionKey)) {
			return false;
		}

		Integer ret = Db.template("admin.auth.hasPermission", actionKey, accountId).queryInt();
		return ret != null;
	}
}

