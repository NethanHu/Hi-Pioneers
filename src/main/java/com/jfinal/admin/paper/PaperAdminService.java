package com.jfinal.admin.paper;

import com.jfinal.admin.common.kit.FileKit;
import com.jfinal.admin.common.model.Account;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.TimeKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    String baseDownloadPath = "/files/papers";


    /**
     * 上传文件
     * <p>
     * ckeditor 文件上传返回值约定：
     * 1：上传成功：
     * {"uploaded":1, "fileName":"xxx.jpg", "url":"/path/xxx.jpg"}
     * url 可以带域名：http://jfinal.com/upload/image/xxx.jpg
     * <p>
     * 2：上传失败：
     * {"uploaded":0, "fileName":"foo.jpg", "error":{"message":"文件格式不正确"}}
     * <p>
     * 3：粘贴上传与对话框上传约定一样：
     * {"uploaded":1, "fileName":"foo.jpg", "url":"/xxx.jpg"}
     * <p>
     * 4：非图片也可以上传：
     * {"uploaded":1, "fileName":"xxx.zip", "url":"/xxx.zip"}
     */

    public Ret upload(int AccountId, UploadFile uf) {
        Ret ret = checkUploadFile(uf);
        if (ret != null) {
            return ret;
        }

        String fileName = buildSavePaperName(AccountId, uf);
        String path = relativePath;
        int length = (int) uf.getFile().length();
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

    private String buildSavePaperName(int accountId) {
        String time = TimeKit.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        return accountId + "_" + time + ".pdf"; // pdf 只是可选名，与实际的储存格式没有实际关系
    }

    /**
     * 创建
     */
    public Ret save(int accountId, Paper paper) {
        paper.setFileName(buildSavePaperName(accountId));
        paper.setAccountId(accountId);
        paper.setState(paper.STATE_UNPUBLISHED);    // 默认未发布
        paper.setUpdateTime(new Date());
        paper.save();
        return Ret.ok("msg", "创建成功");
    }

    public Ret autosave(String name, String content, int accountId, Paper paper) {
        paper.setShowName(name);
        paper.setContent(content);
        paper.setFileName(buildSavePaperName(accountId));
        paper.setAccountId(accountId);
        paper.setState(paper.STATE_UNPUBLISHED);    // 默认未发布
        paper.setUpdateTime(new Date());
        paper.save();
        return Ret.ok("msg", "创建成功");
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
     * {"uploaded":1, "fileName":"xxx.jpg", "url":"/path/xxx.jpg"}
     */
    private Ret createUploadOkRet(String fileName, String url) {
        return Ret.create("uploaded", 1).set("fileName", fileName).set("url", url);
    }

    /**
     * 创建上传失败返回值。上传错误返回格式：
     * {"uploaded":0, "fileName":"foo.jpg", "error":{"message":"文件格式不正确"}}
     */
    public Ret createUploadFailRet(String msg) {
        Ret ret = Ret.create("uploaded", 0).set("fileName", "foo.jpg");
        return ret.set("error", Ret.create("message", msg));
    }

    // -----------------------------------------------------------------------------

    private static int pageSize = 15;

    private Paper dao = new Paper().dao();
    private Question Qdao = new Question().dao(); // 在 Paper 模块中引入 Question 的数据库查询功能


    /**
     * 分页
     */
    public Page<Paper> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from Paper order by update_time desc");
    }

    // 引入范型为 Question 的分页机制，因为要显示题库以供选择
    public Page<Question> Qpaginate(int pageNumber ,String course) {
        return Qdao.paginate(pageNumber, pageSize, "select *", "from question where course_name='"+course+"' order by update_time desc");
    }

    /**
     * 把试卷中涉及的题目数组传入，生成查询 sql 的语句
     *
     * @param id : String[]
     * @return Page<Question> : Question
     */
    public Page<Question> showQuestion(String[] id) {
        String sql = "select * from question where ";
        for (int i = 1; i < id.length; i++) {
            if (i == 1) {
                sql = sql + "id = " + id[i];
            } else {
                sql = sql + " or id = " + id[i];
            }
        }
        return Qdao.templateByString(sql, id[0]).paginate(1, pageSize);
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
     * 发布
     */
    public Ret publish(int id, boolean checked) {
        int state = checked ? Question.STATE_PUBLISHED : Question.STATE_UNPUBLISHED;
        String sql = "update Paper set state = ? where id = ?";
        Db.update(sql, state, id);
        return Ret.ok();
    }

    /**
     * 搜索功能
     */
    public Page<Paper> search(String key, int pageNumber) {
        String sql = "select * from Paper "
                + "where ShowName like concat('%', #para(0), '%') "
                + "order by update_time desc";
        return dao.templateByString(sql, key).paginate(pageNumber, pageSize);
    }

    /**
     * 获取 id
     */
    public Paper getById(int id) {
        return dao.findById(id);
    }

    public  String getCourse(int accountId){
        String sql = "select number from account where id="+accountId+" limit 1";
        String Tno = Db.queryStr(sql);
        String getCno = " select Cno from teaching where Tno='"+Tno+"' limit 1";
        String Cno = Db.queryStr(getCno);
        String getType = "select type from Course where Cno='"+Cno+"' limit 1";
        return Db.queryStr(getType);
    }
    /**
     * 根据题目类型来检索题库，返回对应符合要求的题目
     * 这里由于 if 判断过多，TO DO: 日后可以加以抽象提取一个方法来组合 sql 语句
     */
    public Page<Question> searchForQuestion( String course,String type, String level, int pageNumber) {
        String sql = "select * from question where ";

        if ( StrKit.isBlank(type) & StrKit.isBlank(level)) {
            sql = "select * from question where course_name='"+course+"'";
        }
        else {
            sql = "select * from question where course_name='"+course+"' and ";
        }
        if (!StrKit.isBlank(type)) {
            if (!StrKit.isBlank(level)) {
                sql += "type = '" + type + "' and ";
            } else {
                sql += "type = '" + type + "'";
            }
        }
        ;
        if (!StrKit.isBlank(level)) {
            sql += "level = '" + level + "'";
        }
        ;

        return Qdao.templateByString(sql, new Object()).paginate(pageNumber, pageSize);
    }

    public List<Question> selectBy(String unit, String course, String[][] type, String min_level, String max_level) {
        String sql = "";
        for (int i = 0; i < type.length; i++) {
            if (i > 0) {
                sql = sql + "union" + "(select question.id from question where type ='" + type[i][0] + "' ";
                if (!StrKit.isBlank(unit)) {
                    sql = sql + " and unit = '" + unit + "' ";
                }
                if (!StrKit.isBlank(course)) {
                    sql = sql + " and course_name = '" + course + "' ";
                }
                if (!StrKit.isBlank(min_level)) {
                    sql = sql + " and level >= " + min_level + " ";
                }
                if (!StrKit.isBlank(max_level)) {
                    sql = sql + " and level <= " + max_level + " ";
                }
                sql = sql + "limit " + type[i][1] + ")";
            } else {
                sql = "(select question.id from question where type ='" + type[i][0] + "' ";
                if (!StrKit.isBlank(unit)) {
                    sql = sql + " and unit = '" + unit + "' ";
                }
                if (!StrKit.isBlank(course)) {
                    sql = sql + " and course_name = '" + course + "' ";
                }
                if (!StrKit.isBlank(min_level)) {
                    sql = sql + " and level >= " + min_level + " ";
                }
                if (!StrKit.isBlank(max_level)) {
                    sql = sql + " and level <= " + max_level + " ";
                }
                sql = sql + "limit " + type[i][1] + ")";
            }
        }
        return Qdao.find(sql);
    }
}
