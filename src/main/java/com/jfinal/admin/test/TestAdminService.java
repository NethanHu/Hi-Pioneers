package com.jfinal.admin.test;

import com.jfinal.admin.common.model.*;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestAdminService {
    private static int pageSize = 15;
    private Score Sdao = new Score().dao();
    private Exam dao = new Exam().dao();
    private Paper Pdao = new Paper().dao(); // 在 Exam 模块中引入 Paper 的数据库查询功能
    private Question Qdao = new Question().dao(); // 在 Exam 模块中引入 Question 的数据库查询功能
    private CourseSelection CSdao = new CourseSelection().dao();
    private Course Cdao = new Course().dao();
    int fileMaxSize = 20971520; // 20Mb
    // 文件临时上传目录
    String tempUploadPath = "/temp";

    // 基础上传目录，该目录与 me.setBaseUpload(...) 要保持一致
    String baseUploadPath = "/upload";
    // 相对路径
    String relativePath = "/paper/";

    String baseDownloadPath = "/upload/paper";

    /**
     * 分页
     */
    public int getRoleId(int accountId) {
        String sql = "select roleId from account_role where accountId =" + accountId + " limit 1";
        return Db.queryInt(sql);
    }

    public String getStuNo(int accountId) {
        String sql = "select number from account where id =" + accountId + " limit 1";
        return Db.queryStr(sql);
    }

    public List<Score> getUploadState(String studentNo){
        String sql="select distinct examID from Score where studentId='"+studentNo+"'";
        return Sdao.find(sql);
    }
    public String[] getStuCourse(String Sno) {
        String sql = "select distinct Cno from CourseSelection where Sno = '" + Sno+"'";
        List<CourseSelection> CS = CSdao.find(sql);
        String[] course = new String[CS.size()];
        for (int i = 0; i < CS.size(); i++) {
            CourseSelection Cs = CS.get(i);
            course[i] = Cs.getCno();
        }
        String[] course_name = new String[CS.size()];
        String sql_name = "select distinct name from Course where ";
        for (int i = 0; i < CS.size(); i++) {
            if (i == 0) {
                sql_name = sql_name + "Cno = '" + course[i] + "' ";
            } else {
                sql_name = sql_name + " or Cno ='" + course[i] + "' ";
            }
        }
        List<Course> courseList = Cdao.find(sql_name);
        for (int i = 0; i < courseList.size(); i++) {
            Course courseNname = courseList.get(i);
            course_name[i] = courseNname.getName();
        }
        return course_name;
    }

    public Page<Exam> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from exam where state=1 order by update_time desc");
    }

    public Page<Exam> studentPaginate(int pageNumber, String[] name) {
        String sql = "from exam where";
        for (int i = 0; i < name.length; i++) {
            if (i == 0) {
                sql = sql + " course_name = '" + name[i] + "' ";
            } else {
                sql = sql + "or course_name = '" + name[i] + "' ";
            }
        }
        sql = sql + " order by start_time ";
        return dao.paginate(pageNumber, pageSize, "select *", sql);
    }

    public Page<Paper> Ppaginate(int pageNumber) {
        return Pdao.paginate(pageNumber, pageSize, "select *", "from Paper order by update_time desc");
    }

    /**
     * 获取 id
     */
    public Exam EgetById(int id) {
        return dao.findById(id);
    }

    public Paper PgetById(int id) {
        return Pdao.findById(id);
    }

    public Page<Question> showQuestion(String[] id) {
        String sql = "select * from question where ";
        for (int i = 1; i < id.length; i++) {
            if (i == 1) {
                sql = sql + "id = " + id[i];
            } else {
                sql = sql + " or id = " + id[i];
            }
        }
        return Qdao.templateByString(sql, id[0]).paginate(1, pageSize);
    }

    /**
     * 创建
     */
    public Ret save(int paperId, String StudentId, Score score) {
        score.setPaperId(paperId);
        score.setUpdateTime(new Date());
        score.setStudentId(StudentId);
        score.setState(score.STATE_UNMARKED);
        score.save();
        return Ret.ok("msg", "创建成功");
    }

}
