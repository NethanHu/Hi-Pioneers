package com.jfinal.admin.result;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.test.TestAdminService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;

@Path(value = "/admin/score", viewPath = "/admin/score")
public class ResultAdminController extends BaseController {
    @Inject
    TestAdminService srv;

    public void index() {
        render("index.html");
    }

    public void showScore() {
        int accountId = getLoginAccountId();
        int roleId = srv.getRoleId(accountId);
        if (roleId == 4) { // 学生
            String StudentNo = srv.getStuNo(accountId);
            String[] course_name = srv.getStuCourse(StudentNo);
            Page<Exam> page = srv.studentPaginate(getInt("pn", 1), course_name);
            set("page", page);
            render("index.html");
        } else if (roleId == 5) { // 老师

        } else if (roleId == 6) { // 学科主任

        }
    }
}
