package com.jfinal.admin.question;

import com.jfinal.admin.common.model.Question;
import com.jfinal.admin.common.model.QuestionChoice;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

/**
 * 博客管理业务层
 */
public class QuestionChoiceAdminService {

    private QuestionChoice dao = new QuestionChoice().dao();





    private Ret validate2(QuestionChoice question) {
        if (question == null) {
            return Ret.fail("msg", "question 对象不能为 null");
        }
//        if (StrKit.isBlank(question.getContent())) {
//            return Ret.fail("msg", "title 不能为空");
//        }
        if (StrKit.isBlank(question.getContent())) {
            return Ret.fail("msg", "content 不能为空");
        }
        return null;
    }

    /**
     * 创建
     */
    public Ret save2(QuestionChoice question) {
        Ret ret = validate2(question);
        if (ret != null) {
            return ret;
        }

        question.setUpdateTime(new Date());
        question.save();
        return Ret.ok("msg", "创建成功");
    }

    /**
     * 更新
     */
    public Ret update2(QuestionChoice question) {
        Ret ret = validate2(question);
        if (ret != null) {
            return ret;
        }

        question.setUpdateTime(new Date());
        question.update();
        return Ret.ok("msg", "更新成功");
    }




    /**
     * 删除
     */
    public void deleteById(int id) {
        dao.deleteById(id);

    }

}