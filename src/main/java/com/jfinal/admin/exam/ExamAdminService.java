package com.jfinal.admin.exam;

import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

public class ExamAdminService {

    private static int pageSize = 15;

    private Exam dao = new Exam().dao();

    /**
     * 分页
     */
    public Page<Exam> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from exam order by id desc");
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

}
