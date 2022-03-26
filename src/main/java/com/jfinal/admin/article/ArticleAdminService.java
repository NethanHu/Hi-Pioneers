/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.article;

import com.jfinal.admin.common.model.Article;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

/**
 * 博客管理业务层
 */
public class ArticleAdminService {

    private static int pageSize = 25;
    private Article dao = new Article().dao();

    /**
     * 分页
     */
    public Page<Article> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from article order by updated desc");
    }

    /**
     * 搜索
     */
    public Page<Article> search(String key, int pageNumber) {
        String sql = "select * from article where title like concat('%', #para(0), '%') order by updated desc";
        return dao.templateByString(sql, key).paginate(pageNumber, pageSize);
    }

    private Ret validate(Article article) {
        if (article == null) {
            return Ret.fail("msg", "article 对象不能为 null");
        }
        if (StrKit.isBlank(article.getTitle())) {
            return Ret.fail("msg", "title 不能为空");
        }
        if (StrKit.isBlank(article.getContent())) {
            return Ret.fail("msg", "content 不能为空");
        }
        return null;
    }

    /**
     * 创建
     */
    public Ret save(int accountId, Article article) {
        Ret ret = validate(article);
        if (ret != null) {
            return ret;
        }

        article.setAccountId(accountId);
        article.setState(Article.STATE_UNPUBLISHED);    // 默认未发布
        article.setCreated(new Date());
        article.setUpdated(article.getCreated());
        article.setViewCount(0);
        article.save();
        return Ret.ok("msg", "创建成功");
    }

    /**
     * 更新
     */
    public Ret update(Article article) {
        Ret ret = validate(article);
        if (ret != null) {
            return ret;
        }

        article.setUpdated(new Date());
        article.update();
        return Ret.ok("msg", "更新成功");
    }

    /**
     * 发布
     */
    public Ret publish(int id, boolean checked) {
        int state = checked ? Article.STATE_PUBLISHED : Article.STATE_UNPUBLISHED;
        String sql = "update article set state = ? where id = ?";
        Db.update(sql, state, id);
        return Ret.ok();
    }

    /**
     * 获取
     */
    public Article getById(int id) {
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




