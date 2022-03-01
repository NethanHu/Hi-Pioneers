/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.role;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * AccountUpdateValidator 验证角色修改功能表单
 */
public class RoleAdminValidator extends Validator {
	
	protected void validate(Controller c) {
		setShortCircuit(true);

		validateRequiredString("role.name", "msg", "角色名称不能为空");
	}

	protected void handleError(Controller c) {
		c.set("state", "fail");
		c.renderJson();
	}
}

