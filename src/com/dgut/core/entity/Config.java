package com.dgut.core.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


import com.dgut.core.entity.base.BaseConfig;

public class Config extends BaseConfig {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Config() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Config(String id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	public static class ConfigLogin {
		public static String LOGIN_ERROR_INTERVAL = "login_error_interval";
		public static String LOGIN_ERROR_TIMES = "login_error_times";
		public static String LOGIN_DISABLED_TIMES ="login_disabled_times";

		private Map<String, String> attr;

		public static ConfigLogin create(Map<String, String> map) {
			ConfigLogin configLogin = new ConfigLogin();
			configLogin.setAttr(map);
			return configLogin;
		}

		public Map<String, String> getAttr() {
			if (attr == null) {
				attr = new HashMap<String, String>();
			}
			return attr;
		}

		public void setAttr(Map<String, String> attr) {
			this.attr = attr;
		}

		public Integer getErrorInterval() {
			String interval = getAttr().get(LOGIN_ERROR_INTERVAL);
			if (NumberUtils.isDigits(interval)) {
				return Integer.parseInt(interval);
			} else {
				// 默认间隔30分钟
				return 30;
			}
		}



		public void setErrorInterval(Integer errorInterval) {
			if (errorInterval != null) {
				getAttr().put(LOGIN_ERROR_INTERVAL, errorInterval.toString());
			} else {
				getAttr().put(LOGIN_ERROR_INTERVAL, null);
			}
		}

		public Integer getErrorTimes() {
			String times = getAttr().get(LOGIN_ERROR_TIMES);
			if (NumberUtils.isDigits(times)) {
				return Integer.parseInt(times);
			} else {
				// 默认3次输入有误，显示验证码
				return 3;
			}
		}

		public void setErrorTimes(Integer errorTimes) {
			if (errorTimes != null) {
				getAttr().put(LOGIN_ERROR_TIMES, errorTimes.toString());
			} else {
				getAttr().put(LOGIN_ERROR_TIMES, null);
			}
		}

		public Integer getDisabledTimes(){
			String disabled_times = getAttr().get(LOGIN_DISABLED_TIMES);
			if(NumberUtils.isDigits(disabled_times)){
				return Integer.parseInt(disabled_times);
			}
			else{
				//默认输错密码五次冻结账户
				return 5;
			}
		}

		public void setLoginDisabledTimes(Integer disabledTimes){
			if(disabledTimes!=null){
				getAttr().put(LOGIN_DISABLED_TIMES,disabledTimes.toString());
			}
			else{
				getAttr().put(LOGIN_ERROR_TIMES,null);
			}
		}
	}





}