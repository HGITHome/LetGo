package com.dgut.main.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dgut.main.entity.Admin;

/**
 * 前台工具类
 * 
 * 
 */
public class FrontUtils {

	/**
	 * 用户
	 */
	public static final String USER = "user";
	public static final String Title = "letsgo后台管理系统";
	/**
	 * 为前台模板设置公用数据
	 * 
	 * @param request
	 * @param map
	 */
	public static void adminData(HttpServletRequest request,
			Map<String, Object> map) {
		 Admin admin = CmsUtils.getAdmin(request);
		if (admin != null) {
			map.put(USER, admin);
		}
		map.put("title", Title);
	}


}
