/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.role;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Permission;
import com.jfinal.admin.common.model.Role;
import com.jfinal.admin.permission.PermissionAdminService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 角色管理控制器
 */
@Path("/admin/role")
public class RoleAdminController extends BaseController {

    @Inject
    RoleAdminService srv;

    @Inject
    PermissionAdminService permissionAdminSrv;

    public void index() {
        Page<Role> page = srv.paginate(getInt("pn", 1));
        set("page", page);
        render("index.html");
    }

    public void add() {
        render("add_edit.html");
    }

    @Before(RoleAdminValidator.class)
    public void save() {
        Role role = getBean(Role.class);
        Ret ret = srv.save(role);
        renderJson(ret);
    }

    public void edit() {
        keepPara("pn");    // 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
        Role role = srv.findById(getInt("id"));
        set("role", role);
        render("add_edit.html");
    }

    /**
     * 提交修改
     */
    @Before(RoleAdminValidator.class)
    public void update() {
        Role role = getBean(Role.class);
        Ret ret = srv.update(role);
        renderJson(ret);
    }

    public void delete() {
        Ret ret = srv.delete(getInt("id"));
        renderJson(ret);
    }

    /**
     * 分配权限
     */
    public void assignPermissions() {
        Role role = srv.findById(getInt("id"));
        List<Permission> permissionList = permissionAdminSrv.getAllPermissions();
        srv.markAssignedPermissions(role, permissionList);
        LinkedHashMap<String, List<Permission>> permissionMap = srv.groupByController(permissionList);

        set("role", role);
        set("permissionMap", permissionMap);
        render("assign_permissions.html");
    }

    /**
     * switch 开关分配权限。约定参数 checked 在选中时为 true，否则为 false
     */
    public void assignPermission() {
        if (getBoolean("checked")) {
            renderJson(srv.addPermission(getInt("roleId"), getInt("permissionId")));
        } else {
            renderJson(srv.deletePermission(getInt("roleId"), getInt("permissionId")));
        }
    }
}



