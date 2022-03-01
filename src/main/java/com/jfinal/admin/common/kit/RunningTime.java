/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.common.kit;

/**
 * 运行时长
 */
public class RunningTime {
	// 系统启动时间，单位秒
	static long start = System.currentTimeMillis() / 1000;
	
	// 一分钟的秒数
	static final int secondsInMinute = 60;
	// 一小时的秒数
	static final int secondsInHour = 60 * secondsInMinute;
	// 一天的秒数
	static final int secondsInDay = 24 * secondsInHour;
	
	public static String getRunningTime() {
		long end = System.currentTimeMillis() / 1000;
		int duration = (int)(end - start);				// 运行时长，单位秒
		
		int day = duration / secondsInDay;
		int r = duration % secondsInDay;				// r 为余数 remainder
		
		int hour = r / secondsInHour;
		r = r % secondsInHour;
		
		int minute = r / secondsInMinute;
		int seconds = r % secondsInMinute;
		
		StringBuilder ret = new StringBuilder();
		
		if (day > 0) {
			ret.append(day).append("天");
		}
		if (hour > 0 || ret.length() > 0) {
			ret.append(hour).append("小时");
		}
		if (minute > 0 || ret.length() > 0) {
			ret.append(minute).append("分");
		}
		if (seconds > 0 || ret.length() > 0) {
			ret.append(seconds).append("秒");
		}
		
		return ret.toString();
	}
	
	public static void main(String[] args) {
		start = start - (8 * secondsInMinute) - (0 * secondsInHour) - (1 * secondsInDay);
		System.out.println(RunningTime.getRunningTime());
	}
}






