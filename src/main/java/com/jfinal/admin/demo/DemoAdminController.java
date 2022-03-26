/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * <p>
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.demo;

import com.jfinal.admin.common.BaseController;
import com.jfinal.core.Path;

import static com.jfinal.admin.demo.Icons.*;

/**
 * 后续版本添加更多组件演示，如 bootstrap 4 组件
 */
@Path("/admin/demo")
public class DemoAdminController extends BaseController {

    /**
     * 集成 echarts 图表
     * 文档 https://echarts.apache.org/examples/zh/index.html
     */
    public void echarts() {
        render("echarts.html");
    }

    /**
     * 集成 font awesome 图标
     * 一共展示 939 个图标，少部分图标同时存在于多个分类，所以会有重复
     */
    public void fontAwesome() {
        set("webAppIcons", webAppIcons);
        set("accessibilityIcons", accessibilityIcons);
        set("handIcons", handIcons);
        set("transportationIcons", transportationIcons);
        set("genderIcons", genderIcons);
        set("fileTypeIcons", fileTypeIcons);
        set("spinnerIcons", spinnerIcons);
        set("formControlIcons", formControlIcons);
        set("paymentIcons", paymentIcons);
        set("chartIcons", chartIcons);
        set("currencyIcons", currencyIcons);
        set("textEditorIcons", textEditorIcons);
        set("directionalIcons", directionalIcons);
        set("videoPlayerIcons", videoPlayerIcons);
        set("brandIcons", brandIcons);
        set("medicalIcons", medicalIcons);
        // set("newIcons", newIcons);		// 新增的图标在上述相应的分类中已经放入，不再展示

        render("font_awesome.html");
    }
}
