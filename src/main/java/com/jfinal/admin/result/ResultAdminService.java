package com.jfinal.admin.result;

import com.jfinal.admin.common.model.*;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;

public class ResultAdminService {
    private static int pageSize = 15;

    private Score dao = new Score().dao();
    private Paper Pdao = new Paper().dao(); // 在 Exam 模块中引入 Paper 的数据库查询功能
    private Question Qdao = new Question().dao(); // 在 Exam 模块中引入 Question 的数据库查询功能
    private CourseSelection CSdao = new CourseSelection().dao();
    private Course Cdao = new Course().dao();
    private Teaching Tdao = new Teaching().dao();
    private Exam Edao = new Exam().dao();

    /**
     * 分页
     */

    public int getRoleId(int accountId) {
        String sql = "select roleId from account_role where accountId =" + accountId + " limit 1";
        return Db.queryInt(sql);
    }

    public String getAccNo(int accountId) {
        String sql = "select number from account where id =" + accountId + " limit 1";
        return Db.queryStr(sql);
    }
    public List<CourseSelection> getName(){
        String sql = " select distinct Sno , name from CourseSelection ";
;        return CSdao.find(sql);
    }

    public String[] getStuCourse(String Sno) {
        String sql = "select distinct Cno from CourseSelection where Sno = " + Sno;
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

    public String[] getTeacherCourse(String Tno) {
        String sql = "select distinct Cno from teaching where Tno = '" + Tno + "'";
        List<Teaching> Teaching = Tdao.find(sql);
        String[] course = new String[Teaching.size()];
        for (int i = 0; i < Teaching.size(); i++) {
            Teaching Teach = Teaching.get(i);
            course[i] = Teach.getCno();
        }
        String[] course_name = new String[Teaching.size()];
        String sql_name = "select distinct name from Course where ";
        for (int i = 0; i < Teaching.size(); i++) {
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

    public String getHeaderCourse(String HNo) {
        String sql = "select Cno from teaching where Tno = '" + HNo + "'";
        String Cno = Db.queryStr(sql);
        String sql_type = "select type from Course where Cno = '" + Cno + "'";
        String type = Db.queryStr(sql_type);
        return type;
    }

    public Page<Score> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from exam where state=1 order by update_time desc");
    }
    public Page<Question> Qpaginate(int pageNumber,String[] Qid){
        String sql="from question where ";
        for (int i = 1; i < Qid.length; i++) {
            if (i==1){
                sql=sql+" id="+Qid[i]+" ";
            }
            else {
                sql=sql +" or id="+Qid[i]+" ";
            }
        }
        return Qdao.paginate(pageNumber,pageSize,"select * ",sql);
    }

    public String[][] teacherPaginate(String[] name) {
        String sql = "select * from exam where ";
        for (int i = 0; i < name.length; i++) {
            if (i == 0) {
                sql = sql + "course_name='" + name[i] + "' ";
            } else {
                sql = sql + "or course_name='" + name[i] + "' ";
            }
        }
        List<Exam> exam = Edao.find(sql);
        String[][] result = new String[exam.size()][5];
        for (int i = 0; i < exam.size(); i++) {
            Exam E = exam.get(i);
            result[i][0] = E.getCourseName();
            result[i][1] = E.getExamName();
            result[i][2] = Integer.toString(E.getPaperId());
            String getInvolveNo = "select count(*) from Score where examName='" + E.getExamName() + "' ";
            result[i][3] = String.valueOf(Db.queryInt(getInvolveNo));
            String getCourseNo = "select Cno from Course where name='" + E.getCourseName() + "'";
            String getTotalNo = "select count(*) from CourseSelection where Cno='" + Db.queryStr(getCourseNo) + "' ";
            result[i][4] = String.valueOf(Db.queryInt(getTotalNo));
        }
        return result;
    }

    public Page<Score> headerPaginate(int pageNumber, String type) {
        String sql = "from Score where type='高等数学' or type='线性代数' or type='概率论'";
        return dao.paginate(pageNumber, pageSize, "select * ", sql);
    }

    public Page<Score> studentPaginate(int pageNumber, String studentNo) {
        String sql = "from Score where studentId='" + studentNo + "'";
        return dao.paginate(pageNumber, pageSize, "select * ", sql);
    }


    public Page<Paper> Ppaginate(int pageNumber) {
        return Pdao.paginate(pageNumber, pageSize, "select *", "from Paper order by update_time desc");
    }

    public Page<Score> totalScorePaginate(int pageNumber, String name) {
        String sql = "from Score where examName='" + name + "' and state=1 order by studentId desc";
        return dao.paginate(pageNumber, pageSize, "select * ", sql);
    }

    public int getScoreNumber(String name, int min_score, int max_score) {
        String sql = "select count(*) from Score where examName='" + name + "' and score>=" + min_score + " and score<" + max_score + " ";
        return Db.queryInt(sql);
    }

    /**
     * 获取 id
     */
    public Score SgetById(int id) {
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
    public Ret save(int paperid, String StudentId, Score score) {
        score.setPaperId(paperid);
        score.setUpdateTime(new Date());
        score.setStudentId(StudentId);
        score.save();
        return Ret.ok("msg", "创建成功");
    }

    // TODO: 这里添加一下，根据老师教的学生来导出相应的分数，这里演示的是导出所有的分数

    public List<Score> getScoreList(String name) {
        String sql = "select * from Score where examName='"+name+"' ";
        return dao.find(sql);
    }

}
