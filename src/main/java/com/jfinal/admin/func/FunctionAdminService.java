/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.func;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import com.jfinal.admin.common.model.Account;
import com.jfinal.admin.login.LoginService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;

/**
 * Function 业务层
 * 
 * 无论多小的业务逻辑（哪怕只有一行代码），都要写在业务层，养成良好的设计、开发习惯
 */
public class FunctionAdminService {
	
	/**
	 * 获取订单总数
	 */
	public Ret getTotalOrdersToday() {
		int n = ThreadLocalRandom.current().nextInt(1900, 2500);
		return Ret.ok("msg", "今天订单总数为 : " + n);
	}
	
	/**
	 * 清除过期 session
	 */
	public Ret clearExpiredSession() {
		int n = Db.update("delete from session where expires < ?", new Date());
		
		String state = n > 0 ? "ok" : "fail";
		String msg = n > 0 ? n + " 个过期 session 已被清除" : "没有过期 session 需要被清除";
		return Ret.create().set("state", state).set("msg", msg);
	}
	
	/**
	 * 清除缓存
	 */
	public Ret clearCache() {
		return Ret.ok("msg", "缓存清除成功");
	}
	
	/**
	 * 传递数据
	 */
	public Ret passData(String k1, String k2) {
		return Ret.ok("msg", "服务端接收到参数 ： k1 = " + k1 + " k2 = " + k2);
	}
	
	/**
	 * 切换账户
	 */
	public Ret switchAccount(Account loginAccount, int newAccountId) {
		if (loginAccount.getId() == newAccountId) {
			return Ret.fail("msg", "当前账户 ID 已经是 ：" + newAccountId);
		}
		if (loginAccount.getId() != 1) {
			return Ret.fail("msg", "只有 ID 为 1 的账户才有权切换");
		}
		if (Db.queryInt("select id from account where id = ?", newAccountId) == null) {
			return Ret.fail("msg", "切换的账户不存在 : " + newAccountId);
		}
		
		String sessionId = loginAccount.getStr(LoginService.SESSION_ID);
		String sql = "update session set accountId = ? where id = ?";
		Db.update(sql, newAccountId, sessionId);
		return Ret.ok("msg", "账户切换成功");
	}
	
	/**
	 * 重启
	 */
	public Ret restart() {
		return Ret.fail("msg", "演示环境不支持重启 ^_^");
	}
}





