package com.jfinal.admin.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("Course", "id", Course.class);
		arp.addMapping("CourseSelection", "id", CourseSelection.class);
		arp.addMapping("Paper", "id", Paper.class);
		arp.addMapping("Score", "id", Score.class);
		arp.addMapping("Student", "id", Student.class);
		arp.addMapping("Teacher", "id", Teacher.class);
		arp.addMapping("account", "id", Account.class);
		arp.addMapping("administrator", "id", Administrator.class);
		arp.addMapping("article", "id", Article.class);
		arp.addMapping("evaluation", "id", Evaluation.class);
		arp.addMapping("exam", "id", Exam.class);
		arp.addMapping("file", "ID", File.class);
		arp.addMapping("image", "id", Image.class);
		arp.addMapping("mistake", "id", Mistake.class);
		arp.addMapping("permission", "id", Permission.class);
		arp.addMapping("question", "id", Question.class);
		arp.addMapping("role", "id", Role.class);
		arp.addMapping("session", "id", Session.class);
		arp.addMapping("teaching", "id", Teaching.class);
	}
}


