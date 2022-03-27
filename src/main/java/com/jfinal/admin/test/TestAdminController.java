package com.jfinal.admin.test;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;


@Path(value = "/admin/test", viewPath = "/admin/test")
public class TestAdminController extends BaseController {
    @Inject
    TestAdminService srv;

    public void index() {
        int accountId = getLoginAccountId();
        int roleId = srv.getRoleId(accountId);
        if(roleId ==4){
            String StudentNo = srv.getStuNo(accountId);
            String[] course_name = srv.getStuCourse(StudentNo);
            Page<Exam> page = srv.studentPaginate(getInt("pn", 1),course_name);
            set("page" , page);
            render("index.html");
        }
        else {
            Page<Exam> page = srv.paginate(getInt("pn", 1));
            set("page", page);
            render("index.html");
        }
    }

    /**
     * 考题预览功能
     */
    public void preview() {
        Exam exam = srv.EgetById(getInt("id"));
        int paper_id = exam.getPaperId();
        Paper paper = srv.PgetById(paper_id);
        String[] questionId = paper.getContent().split("~~~");
        Page<Question> page = srv.showQuestion(questionId);
        set("exam", exam).set("paper", paper).set("page", page);
        render("preview.html");
    }
}
