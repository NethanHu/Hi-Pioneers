/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.permission;

import com.jfinal.admin.auth.AdminAuthService;
import com.jfinal.admin.common.model.Account;
import com.jfinal.admin.login.LoginService;
import com.jfinal.aop.Aop;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 界面上的权限控制功能
 * 用来控制界面上的菜单、按钮等等元素的显示
 *
 * 使用示例见模板文件： /_view/_admin/project/index.html 或者 /_view/_admin/permission/index.html
 * #permission("/admin/project/edit")
 * 		<a href="/admin/project/edit?id=#(x.id)">
 * 	  	  <i class="fa fa-pencil" title="修改"></i>
 * 		</a>
 * #end
 *
 * 别名： #perm() #end
 */
public class PermissionDirective extends Directive {

    static AdminAuthService adminAuthSrv = Aop.get(AdminAuthService.class);

    public void exec(Env env, Scope scope, Writer writer) {
        Account account = (Account) scope.getRootData().get(LoginService.LOGIN_ACCOUNT);
        if (account != null && account.isStateOk()) {
            // 如果是超级管理员，或者拥有指定的权限则放行
            if (adminAuthSrv.isSuperAdmin(account.getId()) ||
                    adminAuthSrv.hasPermission(account.getId(), getPermission(scope))) {
                stat.exec(env, scope, writer);
            }
        }
    }

    /**
     * 从 #permission 指令参数中获取 permission
     */
    private String getPermission(Scope scope) {
        Object value = exprList.eval(scope);
        if (value instanceof String) {
            return (String) value;
        } else {
            throw new IllegalArgumentException("权限参数只能为 String 类型");
        }
    }

    public boolean hasEnd() {
        return true;
    }
}