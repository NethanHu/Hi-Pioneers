package com.jfinal.admin.test;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.admin.common.model.Score;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Path(value = "/admin/test", viewPath = "/admin/test")
public class TestAdminController extends BaseController {
    @Inject
    TestAdminService srv;

    public void index() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date nowDate = new Date();
        String time = sdf.format(nowDate);
        int accountId = getLoginAccountId();
        int roleId = srv.getRoleId(accountId);
        if (roleId == 4) {
            String StudentNo = srv.getStuNo(accountId);
            String[] course_name = srv.getStuCourse(StudentNo);
            Page<Exam> page = srv.studentPaginate(getInt("pn", 1), course_name);
            List<Score> list = srv.getUploadState(StudentNo);
            for (int i = 0; i < page.getList().size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getExamID()==page.getList().get(i).getId()){
                        page.getList().remove(i);
                        break;
                    }
                }
            }
            set("page", page).set("time",time).set("list",list);
            render("index.html");
        } else {
            Page<Exam> page = srv.paginate(getInt("pn", 1));
            set("page", page).set("time",time);
            render("index.html");
        }
    }

    /**
     * 考题预览功能
     */
    public void start() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        String nowTime = sdf.format(nowDate);
        Exam exam = srv.EgetById(getInt("id"));
        Time time = exam.getDuration();

        int paper_id = exam.getPaperId();
        Paper paper = srv.PgetById(paper_id);
        String[] questionId = paper.getContent().split("~~~");
        Page<Question> page = srv.showQuestion(questionId);
        // 让 page 既包含 exam 的内容, 也有 paper 的内容
        set("exam", exam).set("paper", paper).set("page", page).set("time",nowTime);
        render("testing.html");
    }

    public void save() {
        String paperId = get("paperId");
        int paperid = Integer.parseInt(paperId);
        int accountId = getLoginAccountId();
        String StudentNo = srv.getStuNo(accountId);
        Ret ret = srv.save(paperid, StudentNo, getBean(Score.class));
        renderJson(ret);
    }
}
