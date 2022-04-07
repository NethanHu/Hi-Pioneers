package com.jfinal.admin.finalResult;

import com.jfinal.admin.common.model.*;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

public class FinalResultAdminService {
    private static int pageSize = 15;
    private Score dao = new Score().dao();
    private Paper Pdao = new Paper().dao(); // 在 Exam 模块中引入 Paper 的数据库查询功能
    private Question Qdao = new Question().dao(); // 在 Exam 模块中引入 Question 的数据库查询功能
    private CourseSelection CSdao = new CourseSelection().dao();
    private Course Cdao = new Course().dao();
    private Teaching Tdao = new Teaching().dao();
    private Exam Edao = new Exam().dao();

    public Page<CourseSelection> paginate(int pageNumber, String[] course) {
        String sql = "from CourseSelection where ";
        for (int i = 0; i < course.length; i++) {
            if (i == 0) {
                sql = sql + "Cno='" + course[i] + "' ";
            } else {
                sql = sql + "or Cno='" + course[i] + "' ";
            }
        }
        sql = sql + " order by Sno asc";
        return CSdao.paginate(pageNumber, pageSize, "select *", sql);
    }

    public List<Course> getCourse() {
        String sql = "select distinct Cno,name from Course";
        return Cdao.find(sql);
    }

    public String[] getTeacherCourse(String Tno) {
        String sql = "select distinct Cno from teaching where Tno = '" + Tno + "'";
        List<Teaching> Teaching = Tdao.find(sql);
        String[] course = new String[Teaching.size()];
        for (int i = 0; i < Teaching.size(); i++) {
            Teaching Teach = Teaching.get(i);
            course[i] = Teach.getCno();
        }
        return course;
    }

    public String getAccNo(int accountId) {
        String sql = "select number from account where id =" + accountId + " limit 1";
        return Db.queryStr(sql);
    }

    public int getScore(String Sno, String course) {
        String judge = "select count(*) from Score where studentId='" + Sno + "' and type='" + course + "' limit 1";
        String sql = "select score from Score where studentId='" + Sno + "' and type='" + course + "' limit 1";

        return (Db.queryInt(judge) == 0) ? 0 : Db.queryInt(sql);
    }

    public String getStuName(String Sno) {
        String sql = "select name from Student where Sno='" + Sno + "'";
        return Db.queryStr(sql);
    }

    public CourseSelection getCourseSelection(String Sno, String course) {
        String sql = "select Cno from Course where name='" + course + "'";
        String Cno = Db.queryStr(sql);
        String getCS = "select * from CourseSelection where Sno='" + Sno + "' and Cno='" + Cno + "'";
        return CSdao.findFirst(getCS);
    }

    public Ret update(CourseSelection CS, int score) {
        CS.setScore(score);
        CS.update();
        return Ret.ok("msg", "提交成功");
    }

    public List<CourseSelection> getCS(String Cno) {
        String sql = "select * from CourseSelection where Cno='" + Cno + "' order by Sno asc";
        return CSdao.find(sql);
    }

    public String getCourseName(String Cno) {
        String sql = "select name from Course where Cno='" + Cno + "'";
        return Db.queryStr(sql);
    }
}
