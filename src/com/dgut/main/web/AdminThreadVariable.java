package com.dgut.main.web;

import com.dgut.main.entity.Admin;


/**
 * CMS线程变量
 * 
 * @author liufang
 * 
 */
public class AdminThreadVariable {
	/**
	 * 当前用户线程变量
	 */
	private static ThreadLocal<Admin> cmsUserVariable = new ThreadLocal<Admin>();

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public static Admin getUser() {
		return cmsUserVariable.get();
	}

	/**
	 * 设置当前用户
	 * 
	 * @param admin
	 */
	public static void setUser(Admin admin) {
		cmsUserVariable.set(admin);
	}

	/**
	 * 移除当前用户
	 */
	public static void removeUser() {
		cmsUserVariable.remove();
	}
	
	
	

}
