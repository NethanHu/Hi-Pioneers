/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.image;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.admin.common.LayoutInterceptor;
import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.model.Image;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.ExceededSizeException;
import com.jfinal.upload.UploadFile;

/**
 * 图片管理控制层
 */
@Path("/admin/image")
public class ImageAdminController extends BaseController {
	
	@Inject
	ImageAdminService srv;
	
	/**
	 * 首页
	 */
	public void index() {
		Page<Image> page = srv.paginate(getInt("pn", 1));
		set("page", page);
		render("index.html");
	}
	
	/**
	 * 弹出图片上传对话框
	 */
	public void add() {
		render("add.html");
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		renderJson(srv.deleteById(getInt("id")));
	}
	
	/**
	 * 上传图片
	 * 
	 * 注意：
	 *    需要清除 LayoutInterceptor 拦截器，否则会被 render(_admin_layout.html);
	 *    因为上传文件并没有被成功识别为 ajax 请求，详情请见 LayoutInterceptor
	 */
	@Clear(LayoutInterceptor.class)
	public void upload() {
		UploadFile uf = null;
		try {
			getFiles(srv.tempUploadPath, srv.imageMaxSize);	// 将上传文件保存到临时目录，并指定最大的上传大小
			uf = getFile();									// 获取已上传文件
			Ret ret = srv.upload(getLoginAccountId(), uf);	// 调用上传业务
			renderJson(ret);
		}
		catch (ExceededSizeException ex) {
			// 上传型异常发生时，已上传文件会自动删除，无需处理
			renderJson(srv.createUploadFailRet("文件大小超出限制，最大允许长度为 : " + (srv.imageMaxSize / 1024) + "KB"));
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
}





