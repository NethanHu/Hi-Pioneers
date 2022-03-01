package com.jfinal.admin.common.model;

import com.jfinal.admin.common.kit.JsoupFilter;
import com.jfinal.admin.common.model.base.BaseAccount;

/**
 * Account
 */
@SuppressWarnings("serial")
public class Account extends BaseAccount<Account> {
	
	public static final String AVATAR_NO_AVATAR = "x.jpg";    // 刚注册时使用默认头像
	
	public static final int STATE_LOCK = -1;		// 锁定账号，无法做任何事情
	public static final int STATE_REG = 0;			// 注册、未激活
	public static final int STATE_OK = 1;			// 正常、已激活
	
	public boolean isStateOk() {
		return getState() == STATE_OK;
	}
	
	public boolean isStateReg() {
		return getState() == STATE_REG;
	}
	
	public boolean isStateLock() {
		return getState() == STATE_LOCK;
	}
	
	/**
	 * 获取 state 字段的 String 格式的含义值
	 */
	public String getStateStr() {
		switch (getState()) {
			case STATE_OK : return "正常";
			case STATE_REG : return "未激活";
			case STATE_LOCK : return "锁定";
			default : return "未知";
		}
	}
	
	/**
	 * 过滤掉 nickName 中的 html 标记，恶意脚本
	 */
	protected void filter(int filterBy) {
		JsoupFilter.filterAccountNickName(this);
	}
	
	public Account removeSensitiveInfo() {
		remove("password", "salt");
		return this;
	}
}







