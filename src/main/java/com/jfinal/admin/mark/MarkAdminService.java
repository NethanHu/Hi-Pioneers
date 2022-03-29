package com.jfinal.admin.mark;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.*;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

public class MarkAdminService  {
    private static int pageSize = 25;
    private Score dao = new Score().dao();
    private Teaching Tdao = new Teaching().dao();
    private Course Cdao = new Course().dao();
    private Exam Edao = new Exam().dao();
    /**
     * 分页
     */
    public Page<Score> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from Score order by updateTime desc");
    }
    public Score getById(int id) {
        return dao.findById(id);
    }
    public Exam EgetById(int id) {
        return Edao.findById(id);
    }
    public Page<Score> TeacherPaginate(int pageNumber , String[] name) {
        String sql = "from Score where";
        for (int i = 0; i < name.length; i++) {
            if (i==0){
                sql = sql+" type = '"+name[i]+"' ";
            }
            else {
                sql = sql+"or type = '"+name[i]+"' ";
            }
        }
        sql=sql+" order by updateTime ";
        return dao.paginate(pageNumber, pageSize, "select *",sql);
    }
    public String getTeacherNo(int accountId){
        String sql = "select number from account where id ="+accountId+" limit 1";
        return Db.queryStr(sql);
    }
    public String[] getTeacherCourse(String Sno){
        String sql = "select distinct Cno from teaching where Tno = "+Sno;
        List<Teaching> T= Tdao.find(sql);
        String[] course=new String[T.size()];
        for (int i = 0; i < T.size(); i++) {
            Teaching Tea = T.get(i);
            course[i]=Tea.getCno();
        }
        String[] course_name = new String[T.size()];
        String sql_name = "select distinct name from Course where ";
        for (int i = 0; i < T.size(); i++) {
            if (i==0){
                sql_name = sql_name+"Cno = '"+course[i]+"' ";
            }
            else {
                sql_name = sql_name+" or Cno ='"+course[i]+"' ";
            }
        }
        List<Course> courseList = Cdao.find(sql_name);
        for (int i = 0; i < courseList.size(); i++) {
            Course courseNname = courseList.get(i);
            course_name[i]=courseNname.getName();
        }
        return course_name;
    }
    public Ret save(Score score,String TeacherNo,String result,int scores){
        score.setTeacherId(TeacherNo);
        score.setScore(scores);
        score.setScoreItems(result);
        score.update();
        return Ret.ok("msg","提交成功");
    }
}
