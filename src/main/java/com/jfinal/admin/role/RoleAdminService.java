/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.role;

import com.jfinal.admin.common.model.Permission;
import com.jfinal.admin.common.model.Role;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 角色管理
 */
public class RoleAdminService {

    private static int pageSize = 25;
    private Role dao = new Role().dao();

    public Page<Role> paginate(int pageNum) {
        return dao.paginate(pageNum, pageSize, "select *", "from role order by id asc");
    }

    public Role findById(int roleId) {
        return dao.findById(roleId);
    }

    public List<Role> getAllRoles() {
        return dao.find("select * from role order by id asc");
    }

    /**
     * 判断角色名是否存在
     * @param roleId 当前 role 对象的 id 号，如果 role 对象还未创建，提供一个小于 0 的值即可
     * @param name 角色名
     */
    public boolean exists(int roleId, String name) {
        name = name.toLowerCase().trim();
        String sql = "select id from role where lower(name) = ? and id != ? limit 1";
        Integer id = Db.queryInt(sql, name, roleId);
        return id != null;
    }

    /**
     * 创建角色
     */
    public Ret save(Role role) {
        if (exists(-1, role.getName())) {
            return Ret.fail("msg", "角色名称已经存在，请输入别的名称");
        }

        role.setName(role.getName().trim());
        role.setCreated(new Date());
        role.save();
        return Ret.ok("msg", "创建成功");
    }

    /**
     * 更新角色
     */
    public Ret update(Role role) {
        if (role.getId() == 1) {
            return Ret.fail("msg", "超级管理员不能被修改");
        }

        if (exists(role.getId(), role.getName())) {
            return Ret.fail("msg", "角色名称已经存在，请输入别的名称");
        }

        role.setName(role.getName().trim());
        role.update();
        return Ret.ok("msg", "角色更新成功");
    }

    public Ret delete(final int roleId) {
        if (roleId == 1) {
            return Ret.fail("msg", "超级管理员不能被删除");
        }

        Db.tx(() -> {
            Db.delete("delete from account_role where roleId=?", roleId);
            Db.delete("delete from role_permission where roleId=?", roleId);
            dao.deleteById(roleId);
            return true;
        });

        return Ret.ok("msg", "角色删除成功");
    }

    /**
     * 添加权限
     */
    public Ret addPermission(int roleId, int permissionId) {
        if (roleId == 1) {
            return Ret.fail("msg", "超级管理员天然拥有所有权限，无需分配");
        }

        Record rolePermission = new Record().set("roleId", roleId).set("permissionId", permissionId);
        Db.save("role_permission", rolePermission);
        return Ret.ok("msg", "添加权限成功");
    }

    /**
     * 删除权限
     */
    public Ret deletePermission(int roleId, int permissionId) {
        if (roleId == 1) {
            return Ret.fail("msg", "超级管理员天然拥有所有权限，不能删除权限");
        }

        Db.delete("delete from role_permission where roleId=? and permissionId=?", roleId, permissionId);
        return Ret.ok("msg", "删除权限成功");
    }

    /**
     * 标记出 role 拥有的权限，用于在界面输出 checkbox 的 checked 属性
     * 未来用 permission left join role_permission 来优化
     */
    public void markAssignedPermissions(Role role, List<Permission> permissionList) {
        // id 为 1 的超级管理员默认拥有所有权限
        if (role.getId() == 1) {
            for (Permission permission : permissionList) {
                permission.put("assigned", true);
            }
            return;
        }

        String sql = "select roleId from role_permission where roleId=? and permissionId=? limit 1";
        for (Permission permission : permissionList) {
            Integer roleId = Db.queryInt(sql, role.getId(), permission.getId());
            if (roleId != null) {
                // 设置 assigned 用于界面输出 checked
                permission.put("assigned", true);
            }
        }
    }

    /**
     * 根据 controller 将 permission 进行分组
     */
    public LinkedHashMap<String, List<Permission>> groupByController(List<Permission> permissionList) {
        LinkedHashMap<String, List<Permission>> ret = new LinkedHashMap<String, List<Permission>>();

        for (Permission permission : permissionList) {
            String controller = permission.getController();
            List<Permission> list = ret.get(controller);
            if (list == null) {
                list = new ArrayList<Permission>();
                ret.put(controller, list);
            }

            list.add(permission);
        }

        return ret;
    }
}
