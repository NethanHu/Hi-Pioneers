/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.login;

import java.time.LocalDateTime;
import java.util.Date;
import com.jfinal.admin.common.model.Account;
import com.jfinal.admin.common.model.Session;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.TimeKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 登录业务层
 */
public class LoginService {
	
	public static final String SESSION_ID = "sessionId";
	public static final String LOGIN_ACCOUNT = "loginAccount";
	
	private Account accountDao = new Account().dao();
	private Session sessionDao = new Session().dao();
	
	/**
	 * 登录
	 */
	public Ret login(String userName, String password, String ip, int port) {
		if (StrKit.isBlank(userName)) {
			return Ret.fail("msg", "用户名不能为空");
		}
		if (StrKit.isBlank(password)) {
			return Ret.fail("msg", "密码不能为空");
		}
		
		Ret ret = doLogin(userName, password);
		// 创建登录日志，日志中不能记录密码，密码必须要加盐 hash 存放以免泄漏
		createLoginLog(ret, userName, ip, port);
		return ret;
	}
	
	/**
	 * 登录逻辑
	 */
	private Ret doLogin(String userName, String password) {
		userName = userName.toLowerCase().trim();
		password = password.trim();
		Account account = accountDao.findFirst("select * from account where userName=? limit 1", userName);
		if (account == null) {
			return Ret.fail("msg", "用户名或密码不正确");
		}
		if (account.isStateLock()) {
			return Ret.fail("msg", "账号已被锁定");
		}
		if (account.isStateReg()) {
			return Ret.fail("msg", "账号未激活，请先激活账号");
		}
		
		String salt = account.getSalt();
		String hashedPass = HashKit.sha256(salt + password);
		
		// 未通过密码验证
		if (! account.getPassword().equals(hashedPass) ) {
			return Ret.fail("msg", "用户名或密码不正确");
		}
		
		// session 存活时间设置为 3 天
		long timeToLiveSeconds = 3 * 24 * 60 * 60;
		// expires 用于设置 session 的过期时间点
		Date expires = TimeKit.toDate(LocalDateTime.now().plusSeconds(timeToLiveSeconds));
		
		// 保存登录 session 到数据库
		Session session = new Session();
		String sessionId = StrKit.getRandomUUID();
		session.setId(sessionId);
		session.setAccountId(account.getId());
		session.setCreated(new Date());
		session.setExpires(expires);
		session.save();
		
		// 移除敏感信息 password、salt
		account.removeSensitiveInfo();
		
		return Ret.ok(SESSION_ID, sessionId)
				.set(LOGIN_ACCOUNT, account)
				.set("timeToLiveSeconds", timeToLiveSeconds);   // 用于设置 cookie 的存活时间
	}
	
	/**
	 * login_log 表的主要作用：
	 * 1：防止程序暴力登录攻击，用于代替验证码功能，提升用户体验。由于人工智能的逐步普及，
	 *    验证码已经很容易通过机器学习被破解，所以现在大厂的在线产品验证码的使用在逐步减少
	 *    具体的方案大致如下：   
	 *      从 login_log 表中查询同一 ip 地址在一个时间段内的登录次数，高于一定次数
	 *      则阻止登录
	 * 
	 * 2：如果产品向用户提供了登录功能，可通过 login_log 了解用户活跃度
	 */
	private void createLoginLog(Ret loginRet, String userName, String ip, int port) {
		Record loginLog = new Record();
		
		// 登录成功记录 state 置为 1，否则置为 0
		if (loginRet.isOk()) {
			Account loginAccount = loginRet.getAs(LOGIN_ACCOUNT);
			loginLog.set("state", 1);							// 状态：登录成功
			loginLog.set("accountId", loginAccount.getId());	//  登录成功记录 accountId
		} else {
			loginLog.set("state", 0);							// 状态：登录失败
		}
		
		loginLog.set("userName", userName)
				.set("created", new Date())
				.set("ip", StrKit.notBlank(ip) ? ip : "127.0.0.1")
				.set("port", port);
				
		Db.save("login_log", loginLog);
	}
	
	/**
	 * 退出登录
	 */
	public Ret logout(String sessionId) {
		sessionDao.deleteById(sessionId);
		return Ret.ok();
	}
	
	/**
	 * 通过 sessionId 获取 account
	 */
	public Account getAccountBySessionId(String sessionId) {
		/** 如果项目并发访问量高，可以添加如下的缓存处理，注意要在 ehcache.xml 中添加名称为 loginAccount 的 cache
		Account loginAccount = CacheKit.get(LoginService.loginAccount, sessionId);
		if (loginAccount != null && loginAccount.isStateOk()) {
			return loginAccount;
		} */
		
		String sql = "select a.*, s.id as sessionId, s.created as sessionCreated, s.expires as sessionExpires " +
					 "from account a inner join session s on a.id = s.accountId where s.id = ? limit 1";
		Account loginAccount = accountDao.findFirst(sql, sessionId);
		if (loginAccount != null) {
			// 账户状态 ok
			if (loginAccount.isStateOk()) {
				// session 未过期
				if (loginAccount.getDate("sessionExpires").after(new Date())) {
					return loginAccount;
				}
			}
		}
		
		// 被动式删除 session
		sessionDao.deleteById(sessionId);
		return null;
	}
	
	public static void main(String[] args) {
		// 密码加盐 hash
		String salt = HashKit.generateSaltForSha256();
		String password = "111111";
		password = HashKit.sha256(salt + password);
		System.out.println(salt);
		System.out.println(password);
	}
}
