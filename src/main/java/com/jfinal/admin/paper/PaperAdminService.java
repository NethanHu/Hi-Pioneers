package com.jfinal.admin.paper;

import com.jfinal.admin.common.kit.FileKit;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.TimeKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 试卷管理业务层
 */
public class PaperAdminService {
    // 文件最大尺寸
    int fileMaxSize = 20971520; // 20Mb
    // 文件临时上传目录
    String tempUploadPath = "/temp";

    // 基础上传目录，该目录与 me.setBaseUpload(...) 要保持一致
    String baseUploadPath = "/upload";
    // 相对路径
    String relativePath = "/paper/";

    String baseDownloadPath = "/upload/paper";

    private Question dao2 = new Question().dao();

    /**
     * 上传文件
     *
     * ckeditor 文件上传返回值约定：
     * 1：上传成功：
     *    {"uploaded":1, "fileName":"xxx.jpg", "url":"/path/xxx.jpg"}
     *    url 可以带域名：http://jfinal.com/upload/image/xxx.jpg
     *
     * 2：上传失败：
     *    {"uploaded":0, "fileName":"foo.jpg", "error":{"message":"文件格式不正确"}}
     *
     * 3：粘贴上传与对话框上传约定一样：
     *    {"uploaded":1, "fileName":"foo.jpg", "url":"/xxx.jpg"}
     *
     * 4：非图片也可以上传：
     *    {"uploaded":1, "fileName":"xxx.zip", "url":"/xxx.zip"}
     */

    public Ret upload(int AccountId, UploadFile uf) {
        Ret ret = checkUploadFile(uf);
        if (ret != null) {
            return ret;
        }

        String fileName = buildSavePaperName(AccountId, uf);
        String path = relativePath;
        int length = (int)uf.getFile().length();
        String fullFileName = PathKit.getWebRootPath() + baseUploadPath + path + fileName;
        saveOriginalFileToTargetFile(uf.getFile(), fullFileName);

        // 保存上传文件到数据库
        Paper paper = new Paper();
        paper.setAccountId(AccountId);
        paper.setFileName(fileName);
        paper.setShowName(fileName);
        paper.setPath(path);
        paper.setUpdateTime(new Date());
        paper.setLength(length);
        paper.save();

        String url = baseUploadPath + path + fileName;
        return createUploadOkRet(fileName, url);
    }

    /**
     * 创建保存文件名
     */
    private String buildSavePaperName(int accountId, UploadFile uf) {
        String time = TimeKit.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        String extName = "." + FileKit.getExtName(uf.getFileName());
        return accountId + "_" + time + extName;
    }

    /**
     * 目前使用 File.renameTo(targetFileName) 的方式保存到目标文件，
     * 如果 linux 下不支持，或者将来在 linux 下要跨磁盘保存，则需要
     * 改成 copy 文件内容的方式并删除原来文件的方式来保存
     */
    private void saveOriginalFileToTargetFile(java.io.File originalFile, String targetFile) {
        originalFile.renameTo(new java.io.File(targetFile));
    }

    /**
     * 检查上传试卷的合法性，返回值格式需要符合 ckeditor 的要求
     */
    private Ret checkUploadFile(UploadFile uf) {
        if (uf == null || uf.getFile() == null) {
            return createUploadFailRet("上传文件为 null");
        }
        /**
         * 试卷多为.doc .docx .pdf .txt .md 类型
         */
        if (FileKit.notPaperExtName(uf.getFileName())) {
            uf.getFile().delete();      // 非试卷格式，立即删除，避免浪费磁盘空间
            return createUploadFailRet("只支持 doc/docx/pdf/txt/md 五种文件类型");
        }
        if (uf.getFile().length() > fileMaxSize) {
            uf.getFile().delete();      // 试卷文件大小超出范围，立即删除，避免浪费磁盘空间
            return createUploadFailRet("图片尺寸超出范畴: " + (fileMaxSize / 1024) + "KB");
        }
        return null;
    }

    /**
     * 创建上传成功返回值。上传成功返回格式：
     *    {"uploaded":1, "fileName":"xxx.jpg", "url":"/path/xxx.jpg"}
     */
    private Ret createUploadOkRet(String fileName, String url) {
        return Ret.create("uploaded", 1).set("fileName", fileName).set("url", url);
    }

    /**
     * 创建上传失败返回值。上传错误返回格式：
     *    {"uploaded":0, "fileName":"foo.jpg", "error":{"message":"文件格式不正确"}}
     */
    public Ret createUploadFailRet(String msg) {
        Ret ret = Ret.create("uploaded", 0).set("fileName", "foo.jpg");
        return ret.set("error", Ret.create("message", msg));
    }

    // -----------------------------------------------------------------------------

    private static int pageSize = 15;
    private Paper dao = new Paper().dao();

    /**
     * 分页
     */
    public Page<Paper> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from Paper order by update_time desc");
    }

    /**
     * 删除
     */
    public Ret deleteById(int id) {
        try {
            Paper paper = dao.findById(id);
            String fullName = PathKit.getWebRootPath() + "/upload" + paper.getPath() + paper.getFileName();
            new java.io.File(fullName).delete();
            paper.delete();
            return Ret.ok("msg", "删除成功");
        } catch (Exception e) {
            return Ret.fail("msg", "删除失败: " + e.getMessage());
        }
    }

    /**
     * 智能组卷功能
     */
    public Ret generatePaperAuto() {
        int n = 111;
        return Ret.ok("msg", "今天订单总数为 : " + n);
    }

    /**
     * 根据题目类型来检索题库，返回对应所需的题目
     */
    public Page<Question> searchForQuestion(String course, String type, String level, int pageNumber) {
        String sql = "select * from question where ";

        if (StrKit.isBlank(course) & StrKit.isBlank(type) & StrKit.isBlank(level)) {
            sql = "select * from question";
        }

        if (!StrKit.isBlank(course)) {
            if(StrKit.isBlank(type) & StrKit.isBlank(level)){
                sql += "course_name = '" + course + "'";
            } else{
                sql += "course_name = '" + course + "' and ";
            }
        };
        if (!StrKit.isBlank(type)) {
            if(!StrKit.isBlank(level)){
            sql += "type = '" + type + "' and ";}
            else{
                sql += "type = '" + type + "'";
            }
        };
        if (!StrKit.isBlank(level)) {
            sql += "level = '" + level + "'";
        };

        return dao2.templateByString(sql, course).paginate(pageNumber, pageSize);
    }
}
