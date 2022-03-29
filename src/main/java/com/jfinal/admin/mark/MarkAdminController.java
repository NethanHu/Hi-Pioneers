package com.jfinal.admin.mark;


import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Article;
import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.common.model.Score;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@Path("/admin/mark")
public class MarkAdminController extends BaseController {
    @Inject
    MarkAdminService srv;

    /**
     * 列表与搜索
     */
    public void index() {
        // pn 为分页号 pageNumber
        int pn = getInt("pn", 1);
        int accountId = getLoginAccountId();
        String TeacherNo = srv.getTeacherNo(accountId);
        String[] course_name = srv.getTeacherCourse(TeacherNo);
        Page<Score> page = srv.TeacherPaginate(getInt("pn", 1),course_name);
        // 保持住 keyword 变量，便于输出到搜索框的 value 中


        set("page",page);
        render("index.html");
    }
    public void mark(){
        Score score = srv.getById(getInt("id"));
        Exam exam = srv.EgetById(score.getExamID());
        set("score",score).set("exam",exam);
        render("mark.html");
    }
    public void save(){
        int accountId = getLoginAccountId();
        String TeacherNo = srv.getTeacherNo(accountId);
        int score = Integer.parseInt(get("score"));
        String result = get("result");
        Ret ret= srv.save(getBean(Score.class),TeacherNo,result,score);
        renderJson(ret);
    }
}
