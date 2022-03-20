package com.jfinal.admin.common.model;

import com.jfinal.admin.common.model.base.BaseQuestion;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Question extends BaseQuestion<Question> {
    public static final int STATE_PUBLISHED = 1;	// 已发布状态
    public static final int STATE_UNPUBLISHED = 0;	// 未发布状态

    /**
     * 是否已发布
     */
    public boolean isPublished() {
        return getInt("state") == STATE_PUBLISHED;
    }

    /**
     * 是否未发布
     */
    public boolean isUnpublished() {
        return getInt("state") == STATE_UNPUBLISHED;
    }

	
}

