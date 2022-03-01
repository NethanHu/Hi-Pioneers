/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.system;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import com.jfinal.admin.account.AccountAdminService;
import com.jfinal.admin.common.model.Account;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;

public class SystemService {
	
	@Inject
	AccountAdminService accountService;
	
	/**
	 * 更换头像
	 * 1：将 Base64 位编码的图片进行解码，并保存到指定目录
	 * 2：更新 account 表的 avatar 字段
	 * 
	 * 注意区分 base64 与 base64URL 两种格式的编码：
	 *   java.utilBase64.Encoder 中的 toBase64 与 toBase64URL 两个数组仅有最后两个字符不同
	 *   前者是 '+', '/'，而后者是 '-', '_'
	 *   从前端传回来的 base64 数据中包含了 '+', '/'，所以解码时使用 base64 版本，而非 base64URL 版本
	 *   前者解码使用 Base64.getDecoder()，后者使用 Base64.getUrlDecoder()
	 */
	public Ret changeAvatar(int accountId, String avatarData) {
		String path = PathKit.getWebRootPath();
		path = path + "/" + JFinal.me().getConstants().getBaseUploadPath();
		path = path + "/avatar/";
		String name = accountId + ".png";
		
		try (FileOutputStream write = new FileOutputStream(new File(path + name))) {
			// 将 avatarData 开头的非 base64 字符删除，被删除字符为："data:image/png;base64,"
			String base64Data = avatarData.substring(avatarData.indexOf(",") + 1);
			byte[] imageData = Base64.getDecoder().decode(base64Data);
			write.write(imageData);		// 写入文件
			
			// 更新 account 表的 avatar 字段值
			Account acc = accountService.getById(accountId);
			acc.setAvatar(name);
			acc.update();
			
			return Ret.ok("msg", "更换头像成功");
		}
		catch (Exception e) {
			Log.getLog(SystemService.class).error("更换头像失败", e);
			return Ret.fail("msg", "更换头像失败");
		}
	}
}
