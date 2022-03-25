package com.jfinal.admin.test;

import com.jfinal.admin.common.model.Exam;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.plugin.activerecord.Page;

public class TestAdminService {
    private static int pageSize = 15;

    private Exam dao = new Exam().dao();
    private Paper Pdao = new Paper().dao(); // 在 Exam 模块中引入 Paper 的数据库查询功能
    private Question Qdao = new Question().dao(); // 在 Exam 模块中引入 Question 的数据库查询功能

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
    public Page<Exam> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from exam where state=1 order by update_time desc");
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
        for (int i = 1; i < id.length ; i++) {
            if(i == 1){
                sql = sql + "id = " + id[i];
            } else {
                sql = sql + " or id = " + id[i];
            }
        }
        return Qdao.templateByString(sql, id[0]).paginate(1, pageSize);
    }
}
