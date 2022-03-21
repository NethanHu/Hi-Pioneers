package com.jfinal.admin.exam;

import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class ExamAdminService {

    private static int pageSize = 15;

    private Exam dao = new Exam().dao();
    private Paper Pdao = new Paper().dao(); // 在 Exam 模块中引入 Paper 的数据库查询功能

    /**
     * 分页
     */
    public Page<Exam> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from exam order by update_time desc");
    }
    public Page<Paper> Ppaginate(int pageNumber) {
        return Pdao.paginate(pageNumber, pageSize, "select *", "from Paper order by update_time desc");
    }

    /**
     * 删除
     */
    public Ret deleteById(int id) {
        try {
            Exam exam = dao.findById(id);
            exam.delete();
            return Ret.ok("msg", "删除成功");
        } catch (Exception e) {
            return Ret.fail("msg", "删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取 id
     */
    public Exam getById(int id) {
        return dao.findById(id);
    }

    /**
     * 将试卷信息储存到数据库
     */
    public Ret save(String exam_name, int paper_id, int account_id, Timestamp start_time, Time last_time, Exam exam) {
        exam.setExamName(exam_name);
        exam.setPaperId(paper_id);
        exam.setAccountId(account_id);
        exam.setStartTime(start_time);
        exam.setLastTime(last_time);
        exam.setState(exam.STATE_UNPUBLISHED); // 默认未发布
        exam.setUpdateTime(new Date());

        exam.save();
        return Ret.ok("msg", "创建成功");
    }
}
