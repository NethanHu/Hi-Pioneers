/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.face;

import com.jfinal.admin.common.kit.ImageKit;
import com.jfinal.admin.common.model.Image;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.TimeKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 图片管理业务层
 */
public class FaceAdminService {

    // 图片文件最大长度
    int imageMaxSize = 500 * 1024;
    // 图片临时上传目录
    String tempUploadPath = "/temp";

    // 基础上传目录，该目录与 me.setBaseUpload(...) 要保持一致
    String baseUploadPath = "/upload";
    // 相对路径
    String relativePath = "/image/";

    /**
     * 上传图片
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
    public Ret upload(int accountId, UploadFile uf) {
        Ret ret = checkUploadFile(uf);
        if (ret != null) {
            return ret;
        }

        String fileName = buildSaveFileName(accountId, uf);
        String path = relativePath;
        int length = (int) uf.getFile().length();
        String fullFileName = PathKit.getWebRootPath() + baseUploadPath + path + fileName;
        saveOriginalFileToTargetFile(uf.getFile(), fullFileName);

        // 保存上传图片到数据库
        Image image = new Image();
        image.setAccountId(accountId);
        image.setFileName(fileName);
        image.setShowName(fileName);
        image.setPath(path);
        image.setCreated(new Date());
        image.setLength(length);
        image.save();

        String url = baseUploadPath + path + fileName;
        return createUploadOkRet(fileName, url);
    }

    /**
     * 创建保存文件名
     */
    private String buildSaveFileName(int accountId, UploadFile uf) {
        String time = TimeKit.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        String extName = "." + ImageKit.getExtName(uf.getFileName());
        return accountId + "_" + time + extName;
    }

    /**
     * 目前使用 File.renameTo(targetFileName) 的方式保存到目标文件，
     * 如果 linux 下不支持，或者将来在 linux 下要跨磁盘保存，则需要
     * 改成 copy 文件内容的方式并删除原来文件的方式来保存
     */
    private void saveOriginalFileToTargetFile(File originalFile, String targetFile) {
        originalFile.renameTo(new File(targetFile));
    }

    /**
     * 检查上传图片的合法性，返回值格式需要符合 ckeditor 的要求
     */
    private Ret checkUploadFile(UploadFile uf) {
        if (uf == null || uf.getFile() == null) {
            return createUploadFailRet("上传文件为 null");
        }
        if (ImageKit.notImageExtName(uf.getFileName())) {
            uf.getFile().delete();      // 非图片类型，立即删除，避免浪费磁盘空间
            return createUploadFailRet("只支持 jpg、jpeg、png、bmp、gif 五种图片类型");
        }
        if (uf.getFile().length() > imageMaxSize) {
            uf.getFile().delete();      // 图片大小超出范围，立即删除，避免浪费磁盘空间
            return createUploadFailRet("图片尺寸超出范畴: " + (imageMaxSize / 1024) + "KB");
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
    private Image dao = new Image().dao();

    /**
     * 分页
     */
    public Page<Image> paginate(int pageNumber) {
        return dao.paginate(pageNumber, pageSize, "select *", "from image order by created desc");
    }

    /**
     * 删除
     */
    public Ret deleteById(int id) {
        try {
            Image image = dao.findById(id);
            String fullName = PathKit.getWebRootPath() + "/upload" + image.getPath() + image.getFileName();
            new File(fullName).delete();
            image.delete();
            return Ret.ok("msg", "删除成功");
        } catch (Exception e) {
            return Ret.fail("msg", "删除失败: " + e.getMessage());
        }
    }
}




