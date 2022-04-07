/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.index;

import com.jfinal.admin.account.AccountAdminService;
import com.jfinal.admin.common.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Path;

/**
 * 后台首页控制层
 */
@Path(value = "/admin", viewPath = "/admin/index")
public class IndexAdminController extends BaseController {

    @Inject
    IndexAdminService srv;

    @Inject
    AccountAdminService accountAdminSrv;

    /**
     * 根路径暂时重定向到 "/admin"，二次开发添加前台时需要去掉该 action
     */
    @ActionKey("/")
    public void frontIndex() {
        redirect("/admin");
    }

    /**
     * 首页
     */
    public void index() {
        set("isDefaultPassword", accountAdminSrv.isDefaultPassword(getLoginAccount()));
        render("index.html");
    }

    /**
     * 概览
     */
    public void overview() {
        set("totalArticle", srv.getTotalArticle());
        set("totalImage", srv.getTotalImage());
        set("totalAccount", srv.getTotalAccount());
        set("totalRole", srv.getTotalRole());
        set("totalPermission", srv.getTotalPermission());
        set("totalTestTeacher", srv.totalTestTeacher());
        set("totalNotMark", srv.totalNotMark());
        set("seeStudentScore", srv.seeStudentScore());
        set("totalTestStudent", srv.totalTestStudent());
        set("seeMyScore", srv.seeMyScore(getLoginAccountId()));
        render("_overview.html");
    }

}


