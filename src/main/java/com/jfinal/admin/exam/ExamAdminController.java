package com.jfinal.admin.exam;

import com.jfinal.admin.common.BaseController;
import com.jfinal.admin.common.LayoutInterceptor;
import com.jfinal.admin.common.model.Paper;
import com.jfinal.admin.common.model.Question;
import com.jfinal.admin.paper.PaperAdminService;
import com.jfinal.admin.question.QuestionAdminService;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.ExceededSizeException;
import com.jfinal.upload.UploadFile;
import java.util.List;

/**
 * 考试管理控制层
 */
@Path(value = "/admin/exam",viewPath = "/admin/exam")
public class ExamAdminController extends BaseController {

    @Inject
    ExamAdminService srv;

}
