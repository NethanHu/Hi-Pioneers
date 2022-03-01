/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.permission;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Permission;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 权限管理
 */
@Path("/admin/permission")
public class PermissionAdminController extends BaseController {
	
	@Inject
	PermissionAdminService srv;
	
	public void index() {
		int pn = getInt("pn", 1);			// pn 为分页号 pageNumber
		keepPara("keyword");				// 保持住 keyword 变量，便于输出到搜索框的 value 中
		String keyword = get("keyword");
		
		Page<Permission> page = StrKit.isBlank(keyword)
									? srv.paginate(pn)
									: srv.search(keyword, pn);
		
		srv.replaceControllerPrefix(page, "com.jfinal.admin.", "...");
		boolean hasRemovedPermission = srv.markRemovedActionKey(page);
		set("page", page);
		set("hasRemovedPermission", hasRemovedPermission);
		render("index.html");
	}
	
	public void sync() {
		Ret ret = srv.sync();
		renderJson(ret);
	}
	
	public void edit() {
		keepPara("pn");	// 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
		Permission permission = srv.findById(getInt("id"));
		set("permission", permission);
		render("edit.html");
	}
	
	public void update() {
		Ret ret = srv.update(getBean(Permission.class));
		renderJson(ret);
	}
	
	public void delete() {
		Ret ret = srv.delete(getInt("id"));
		renderJson(ret);
	}
}




