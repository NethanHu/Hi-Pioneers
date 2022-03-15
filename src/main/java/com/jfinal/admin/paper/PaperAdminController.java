package com.jfinal.admin.paper;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.LayoutInterceptor;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.ExceededSizeException;
import com.jfinal.upload.UploadFile;

/**
 * 文件管理控制层
 */
@Path(value = "/admin/paper",viewPath = "/admin/paper")
public class PaperAdminController extends BaseController {

    @Inject
    PaperAdminService srv;

    /**
     * 首页
     */
    public void index() {
        Page<Paper> page = srv.paginate(getInt("pn", 1));
        set("page", page);
        render("index.html");
    }

    /**
     * 弹出文件上传对话框
     */
    public void add() {
        render("add.html");
    }

    /**
     * fill 注入的考题备选框
     */
    public void createPaper() {
        render("create_paper.html");
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(srv.deleteById(getInt("id")));
    }

    /**
     * 下载功能
     */
    public void download() {

    }

    /**
     * 考题预览功能
     */
    public void preview() {
        set("Paper", srv.getById(getInt("id")));
        render("preview.html");
    }

    /**
     * 上传文件
     *
     * 注意：
     *    需要清除 LayoutInterceptor 拦截器，否则会被 render(_admin_layout.html);
     *    因为上传文件并没有被成功识别为 ajax 请求，详情请见 LayoutInterceptor
     */
    @Clear(LayoutInterceptor.class)
    public void upload() {
        UploadFile uf = null;
        try {
            getFiles(srv.tempUploadPath, srv.fileMaxSize);	// 将上传文件保存到临时目录，并指定最大的上传大小
            uf = getFile();									// 获取已上传文件
            Ret ret = srv.upload(getLoginAccountId(), uf);	// 调用上传业务
            renderJson(ret);
        }
        catch (ExceededSizeException ex) {
            // 上传型异常发生时，已上传文件会自动删除，无需处理
            renderJson(srv.createUploadFailRet("文件大小超出限制，最大允许尺寸为 : " + (srv.fileMaxSize / (1024 * 1024)) + "MB"));
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            if (uf != null) {
                // 非上传型异常发生时，已上传文件需主动删除，以免浪费存储空间
                uf.getFile().delete();
            }
            renderJson(srv.createUploadFailRet("上传异常，请告知管理员：" + e.getMessage()));
        }
    }
    /**
     * 保存
     */
    public void save() {
        Ret ret = srv.save(getLoginAccountId(), getBean(Paper.class));
        renderJson(ret);
    }

    /**
     * 智能组卷模块，返回 Ret 形式的 msg
     */
    public void generatePaperAuto() {
        renderJson(srv.generatePaperAuto());
    }

    /**
     * 手动组卷模块，会跳出题库中的题目以供选择
     */
    public void search() {
        int pn = getInt("pn", 1);
        String course = get("course");
        String level = get("level");
        String type = get("type");
        Page<Question> page;

        if(StrKit.isBlank(course) & StrKit.isBlank(level) & StrKit.isBlank(type)){
            page = srv.Qpaginate(pn);
        } else {
            page = srv.searchForQuestion(course, type, level, pn);
        }

        set("page", page);
        render("choose_questions.html");
    }
}