package com.jfinal.admin.finalResult;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.LayoutInterceptor;
import com.jfinal.admin.common.kit.XLSFileKit;
import com.jfinal.admin.common.model.Course;
import com.jfinal.admin.common.model.CourseSelection;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path(value = "/admin/finalResult", viewPath = "/admin/finalResult")
public class FinalResultAdminController extends BaseController {
    @Inject
    FinalResultAdminService srv;

    public void index() {
        int accountId = getLoginAccountId();
        String Tno = srv.getAccNo(accountId);
        String[] course = srv.getTeacherCourse(Tno);
        Page<CourseSelection> page = srv.paginate(getInt("pn", 1), course);
        List<Course> list = srv.getCourse();
        set("page", page).set("list", list).set("Tno", Tno);
        render("index.html");
    }

    public void input() {
        int score = srv.getScore(get("Sno"), get("course"));
        String name = srv.getStuName(get("Sno"));
        int id = Integer.parseInt(get("id"));
        CourseSelection courseSelection = srv.getCourseSelection(get("Sno"), get("course"));
        set("score", score).set("name", name).set("id", id).set("Sno", get("Sno")).set("courseSelection", courseSelection);
        render("input.html");
    }

    public void update() {
        Ret ret = srv.update(getBean(CourseSelection.class), Integer.parseInt(get("final_score")));
        renderJson(ret);
    }

    /**
     * 导出表格
     */
    @Clear(LayoutInterceptor.class)
    public void exportOutExcel() {
        String Tno = get("Tno");
        String[] course = srv.getTeacherCourse(Tno);

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
        for (int i = 0; i < course.length; i++) {
            String sheetName = srv.getCourseName(course[i]);
            List<List<Object>> content = new ArrayList<List<Object>>();
            List<String> title = new ArrayList<String>();
            List<CourseSelection> data = srv.getCS(course[i]);
            // 添加`title`,对应的从数据库检索出`datas`的`title`
            title.add("序号");
            title.add("学号");
            title.add("学生名字");
            title.add("平时成绩");
//            title.add("平时成绩占比");
            title.add("期中考试");
//            title.add("期中考试占比");
            title.add("期末考试");
//            title.add("期末考试占比");
            title.add("最终分数");
            int k = 0;
            OK:
            while (true) {
                if (data.size() < (k + 1)) {
                    break OK;
                }

                // 判断单元格是否为空，不为空添加数据
                int index = k + 1;
                List<Object> row = new ArrayList<Object>();
                row.add(index + "");
                row.add(null == data.get(k).get("Sno") ? "" : data.get(k).get("Sno"));
                row.add(null == data.get(k).get("name") ? "" : data.get(k).get("name"));
                row.add(null == data.get(k).get("usualScore") ? "" : data.get(k).get("usualScore"));
//                        row.add(null == data.get(k).get("usualWeight") ? "" : data.get(k).get("usualWeight"));
                row.add(null == data.get(k).get("midsemester") ? "" : data.get(k).get("midsemester"));
//                        row.add(null == data.get(k).get("midWeight") ? "" : data.get(k).get("midWeight"));
                row.add(null == data.get(k).get("finalExam") ? "" : data.get(k).get("finalExam"));
//                        row.add(null == data.get(k).get("finalWeight") ? "" : data.get(k).get("finalWeight"));
                row.add(null == data.get(k).get("score") ? "" : data.get(k).get("score"));
                content.add(row);
                k++;

            }
            xlsFileKit.addSheet(content, sheetName, title);
        }

        xlsFileKit.save();
        renderFile("/export/" + fileName);
    }
}
