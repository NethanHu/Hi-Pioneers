package com.jfinal.admin.result;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.LayoutInterceptor;
import com.jfinal.admin.common.kit.XLSFileKit;
import com.jfinal.admin.common.model.*;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path(value = "/admin/result", viewPath = "/admin/result")
public class ResultAdminController extends BaseController {
    @Inject
    ResultAdminService srv;

    public void index() {
        int accountId = getLoginAccountId();
        int roleId = srv.getRoleId(accountId);

        if (roleId == 4) { // 仅对于学生
            String StudentNo = srv.getAccNo(accountId);
            String[] course_name = srv.getStuCourse(StudentNo);
            Page<Score> page = srv.studentPaginate(getInt("pn", 1), StudentNo);
            List<Exam> exam = srv.getExam();
            set("page", page).set("exam",exam);
            render("student_index.html");
        }
        if (roleId == 5) {
            String TeacherNo = srv.getAccNo(accountId);
            String[] course_name = srv.getTeacherCourse(TeacherNo);
            String[][] score = srv.teacherPaginate(course_name);
            ArrayList list = new ArrayList();
            for (int i = 0; i < score.length; i++) {
                list.add(score[i]);
            }
            Page page = new Page();
            page.setList(list);
            set("page", page);
            render("teacher_index.html");
        }
        if (roleId == 6) {
            String headNo = srv.getAccNo(accountId);
            String name = srv.getHeaderCourse(headNo);
            Page<Score> page = srv.headerPaginate(getInt("pn", 1), name);
            set("page", page);
            render("teacher_index.html");
        }
    }

    public void showStudentScore() {
        Score score = srv.SgetById(getInt("id"));
        int Pno = score.getPaperId();
        Paper paper = srv.PgetById(Pno);
        String[] Qids = paper.getContent().split("~~~");
        Page<Question> page = srv.Qpaginate(getInt("pn", 1), Qids);
        set("page", page).set("score", score);
        render("student_score.html");
    }

    public void showTotalScore() {
        Page<Score> page = srv.totalScorePaginate(getInt("pn", 1), get("name"));
        List<CourseSelection> list = srv.getName();
        set("page", page).set("courseName", get("name")).set("list", list);
        render("total_score.html");
    }

    public void showCharts() {
        int[] scores = new int[5];
        String name = get("name");
        scores[0] = srv.getScoreNumber(name, 0, 60);
        scores[1] = srv.getScoreNumber(name, 60, 70);
        scores[2] = srv.getScoreNumber(name, 70, 80);
        scores[3] = srv.getScoreNumber(name, 80, 90);
        scores[4] = srv.getScoreNumber(name, 90, 100);
        set("scores", scores);
        render("score_charts.html");
    }


    /**
     * 导出表格
     */
    @Clear(LayoutInterceptor.class)
    public void exportOutExcel() {
        String name = get("name");
        String sheetName = name;

        // 导出`Excel`名称
        String fileName = new Date().getTime() + "_" + UUID.randomUUID().toString() + ".xls";

        // excel`保存路径
        String filePath = getRequest().getRealPath("/") + "/files/export/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String relativePath = "/files/export/" + fileName;
        filePath += fileName;
        XLSFileKit xlsFileKit = new XLSFileKit(filePath);
        List<List<Object>> content = new ArrayList<List<Object>>();
        List<String> title = new ArrayList<String>();

        List<Score> data = srv.getScoreList(name);
        List<CourseSelection> list = srv.getName();

        // 添加`title`,对应的从数据库检索出`datas`的`title`
        title.add("序号");
        title.add("学号");
        title.add("学生名字");
        title.add("线上得分");
        int i = 0;
        OK:
        while (true) {
            if (data.size() < (i + 1)) {
                break OK;
            }
            for (int j = 0; j < list.size(); j++) {

                if (data.get(i).get("studentId").equals(list.get(j).getSno())) {
                    // 判断单元格是否为空，不为空添加数据
                    int index = i + 1;
                    List<Object> row = new ArrayList<Object>();
                    row.add(index + "");
                    row.add(null == data.get(i).get("studentId") ? "" : data.get(i).get("studentId"));
                    row.add(null == list.get(j).get("name") ? "" : list.get(j).get("name"));
                    row.add(null == data.get(i).get("score") ? "" : data.get(i).get("score"));
                    content.add(row);
                    i++;
                    break;
                }
            }
        }
        xlsFileKit.addSheet(content, sheetName, title);
        xlsFileKit.save();

        renderFile("/export/" + fileName);
    }
}
