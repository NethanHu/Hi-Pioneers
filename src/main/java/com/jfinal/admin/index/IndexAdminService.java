/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.index;

import java.util.List;
import com.jfinal.admin.common.model.Image;
import com.jfinal.plugin.activerecord.Db;

/**
 * 后台首页业务层
 */
public class IndexAdminService {
	
	/**
	 * 获取博客总数
	 */
	public int getTotalArticle() {
		return Db.queryInt("select count(*) from article");
	}
	
	/**
	 * 获取图片总数
	 */
	public int getTotalImage() {
		return Db.queryInt("select count(*) from image");
	}
	
	/**
	 * 获取账户总数
	 */
	public int getTotalAccount() {
		return Db.queryInt("select count(*) from account");
	}
	
	/**
	 * 获取角色总数
	 */
	public int getTotalRole() {
		return Db.queryInt("select count(*) from role");
	}
	
	/**
	 * 获取权限总数
	 */
	public int getTotalPermission() {
		return Db.queryInt("select count(*) from permission");
	}
	
	/**
	 * 获取最新图片
	 */
	public List<Image> getLatestImage() {
		return new Image().find("select * from image order by created desc limit 10");
	}
}



