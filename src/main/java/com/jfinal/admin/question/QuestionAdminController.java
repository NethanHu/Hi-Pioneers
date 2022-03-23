package com.jfinal.admin.question;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Question;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 博客管理控制层
 */
@Path("/admin/question")
public class QuestionAdminController extends BaseController {

    @Inject
    QuestionAdminService srv;

    /**
     * 列表与搜索
     */
    public void index() {
        // pn 为分页号 pageNumber
        int pn = getInt("pn", 1);
        String keyword = get("keyword");
        String select = get("select");
        String type = get("type");
        Page<Question> page;

        if (StrKit.isBlank(keyword) & StrKit.isBlank(select) & StrKit.isBlank(type)){
            page = srv.paginate(pn);
        } else {
            if (!StrKit.isBlank(keyword)){
                page = srv.search(keyword, pn);
            } else if (!StrKit.isBlank(select)){
                page = srv.select(select, pn);
            } else {
                page = srv.sort(type, pn);
            }
        }

        // 保持住 keyword 变量，便于输出到搜索框的 value 中

        keepPara("keyword");
        keepPara("select");
        set("page", page);

        render("index.html");
    }

    /**
     * 支持 switch 开关的发布功能
     */
    public void publish() {
        Ret ret = srv.publish(getInt("id"), getBoolean("checked"));
        renderJson(ret);
    }

    /**
     * 进入选择提醒页面
     */
    public void add() {
        // render("add_edit.html");
        // 改用独立于后台 layout 的页面
        render("select_type.html");
    }

    /**
     * 调用添加对应题目的页面
     *
     * 不用 @Clear(LayoutInterceptor.class)，因为该页面依赖于 Layout.
     */
    public void add_edit_full_choice() {
        render("add_edit_full_choice.html");
    }
    public void add_edit_full_answer() {
        render("add_edit_full_answer.html");
    }
    public void add_edit_full_blank() {
        render("add_edit_full_blank.html");
    }
    public void add_edit_full_proof() {
        render("add_edit_full_proof.html");
    }

    /**
     * 保存
     */
    public void save() {
        Ret ret = srv.save(getLoginAccountId(), getBean(Question.class));
        renderJson(ret);
    }

    /**
     * 进入修改页面
     *
     * 注意：使用独立于后台 layout 的页面 add_edit_full.html 时，需要清除掉 LayoutInterceptor 拦截器
     */
    public void edit() {
        Question question = srv.getById((getInt("id")));
        set("question", question);
        // render("add_edit.html");
        // 改用独立于后台 layout 的页面
        keepPara("pn");	 // 将页号参数 pn 传递到页面使用
        switch (question.getType()) {
            case "一般选择题" : render("edit_choice.html"); break;
            case "简答题" : render("edit_answer.html"); break;
            case "填空题" : render("edit_blank.html"); break;
            case "证明题" : render("edit_proof.html"); break;
        }
    }

    /**
     * 更新
     */
    public void update() {
        Ret ret = srv.update(getBean(Question.class));
        renderJson(ret);
    }

    /**
     * 删除
     */
    public void delete() {
        Ret ret = srv.deleteById(getInt("id"));
        renderJson(ret);
    }

    /**
     * 预览题目
     */
    public void preview() {
        set("question", srv.getById(getInt("id")));
        render("preview.html");
    }

}
