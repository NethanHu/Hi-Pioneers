package com.jfinal.admin.question;

import com.jfinal.admin.common.model.Question;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

/**
 * 博客管理业务层
 */
public class QuestionAdminService {

    private static int pageSize = 12;
    private Question dao = new Question().dao();

    /**
     * 分页
     */
    public Page<Question> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from question order by update_time desc");
    }

    /**
     * 搜索
     */
    public Page<Question> search(String key, int pageNumber) {
        String sql = "select * from question where question like concat('%', #para(0), '%') order by update_time desc";
        return dao.templateByString(sql, key).paginate(pageNumber, pageSize);
    }

    public Page<Question> select(String key, int pageNumber) {
        String sql = "select * from question where type like concat('%', #para(0), '%') order by update_time desc";
        return dao.templateByString(sql, key).paginate(pageNumber, pageSize);
    }

    public Page<Question> sort(String key, int pageNumber) {
        String sql = "select * from question order by " + key + " desc";
        return dao.templateByString(sql, key).paginate(pageNumber, pageSize);
    }

    private Ret validate(Question question) {
        if (question == null) {
            return Ret.fail("msg", "question 对象不能为 null");
        }

        if (StrKit.isBlank(question.getQuestion())) {
            return Ret.fail("msg", "content 不能为空");
        }
        return null;
    }

    /**
     * 创建
     */
    public Ret save(int accountId, Question question) {
        Ret ret = validate(question);
        if (ret != null) {
            return ret;
        }

        question.setAccountId(accountId);
        question.setState(question.STATE_UNPUBLISHED);	// 默认未发布
        question.setUpdateTime(new Date());
        question.save();
        return Ret.ok("msg", "创建成功");
    }

    /**
     * 更新
     */
    public Ret update(Question question) {
        Ret ret = validate(question);
        if (ret != null) {
            return ret;
        }

        question.setUpdateTime(new Date());
        question.update();
        return Ret.ok("msg", "更新成功");
    }

    /**
     * 发布
     */
    public Ret publish(int id, boolean checked) {
        int state = checked ? Question.STATE_PUBLISHED : Question.STATE_UNPUBLISHED;
        String sql = "update question set state = ? where id = ?";
        Db.update(sql, state, id);
        return Ret.ok();
    }

    /**
     * 获取
     */
    public Question getById(int id) {
        return dao.findById(id);
    }

    /**
     * 删除
     */
    public Ret deleteById(int id) {
        dao.deleteById(id);
        return Ret.ok("msg", "删除成功");
    }

}