package com.dgut.main.action;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.core.entity.Config;
import com.dgut.core.manager.ConfigMng;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.base.BaseAuthentication;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.WebErrors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.DisabledException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.RequestUtils;
import com.dgut.common.web.session.SessionProvider;

import com.dgut.main.manager.AdminMng;
import com.dgut.main.manager.AuthenticationMng;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

import java.util.Date;

import static com.dgut.main.manager.AuthenticationMng.AUTH_KEY;;

/**
 * 登陆控制器
 */
@Controller
public class LeAdminLoginAct {

	public static final String PROCESS_URL = "processUrl";
	public static final String RETURN_URL = "returnUrl";
	public static final String MESSAGE = "message";
	
	private static final Logger log = LoggerFactory
			.getLogger(LeAdminLoginAct.class);



	public static final String COOKIE_ERROR_TIMES = "_error_times";

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String processUrl = RequestUtils.getQueryParam(request, PROCESS_URL);
		String returnUrl = RequestUtils.getQueryParam(request, RETURN_URL);
		String message = RequestUtils.getQueryParam(request, MESSAGE);
		String authId = (String) session.getAttribute(request, AUTH_KEY);
		if (authId != null) {
			// 存在认证ID
			BaseAuthentication auth = authMng.retrieve(authId);
			// 存在认证信息，且未过期
			if (auth != null) {
				String view = getView(processUrl, returnUrl, auth.getId());
				if (view != null) {
					return view;
				} else {
					model.addAttribute("auth", auth);
					return "login";
				}
			}
		}


		Integer cookieErrorTimes = getCookieErrorTimes(request,response);
		if(cookieErrorTimes!=null){
			wirteCookieErrorTimes(cookieErrorTimes-1,request,response,model);
		}
		else{
			wirteCookieErrorTimes(null,request,response,model);
		}


		if (!StringUtils.isBlank(processUrl)) {
			model.addAttribute(PROCESS_URL, processUrl);
		}
		if (!StringUtils.isBlank(returnUrl)) {
			model.addAttribute(RETURN_URL, returnUrl);
		}
		if (!StringUtils.isBlank(message)) {
			model.addAttribute(MESSAGE, message);
		}
		return "login";
	}




	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String submit(String username, String password, String captcha,
			String processUrl, String returnUrl, String message,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Config.ConfigLogin configLogin = configMng.getConfigLogin();
		//得到系统配置的密码错误次数和输入间隔
		int maxErrorTimes = configLogin.getErrorTimes();
		int maxErrorInterval = configLogin.getErrorInterval();
		int disabledTimes =configLogin.getDisabledTimes();

		Integer errorTimes = adminMng.errorTimes(username);

		WebErrors errors = validateSubmit(username,password,captcha,errorTimes,maxErrorInterval,
				disabledTimes,request,response);



		String ip = RequestUtils.getIpAddr(request);
		if (!errors.hasErrors()) {
			try {

				BaseAuthentication auth = authMng.login(username, password, ip,
						request, response, session);

				Admin admin = adminMng.findById(auth.getUid());
				if (admin.getDisabled()) {
					// 如果已经禁用，则退出登录。
					authMng.deleteById(auth.getId());
					session.logout(request, response);

					throw new DisabledException("用户被禁用，请联系管理员");

				}
				removeCookieErrorTimes(request,response);
				String view = getView(processUrl, returnUrl, auth.getId());
				// 是否需要在这里加上登录次数的更新？按正常的方式，应该在process里面处理的，不过这里处理也没大问题。

				adminMng.updateLoginSuccessInfo(auth.getUid(), ip);
				adminLogMng.loginSuccess(request, username,"cms.admin.login.success");
				if (view != null) {
					return view;
				} else {
					return "redirect:login.jspx";
				}
			} catch (UsernameNotFoundException e) {
				//显示错误信息
				errors.addErrorString(e.getMessage());
				//记录日志
				adminLogMng.loginFailure(request,username,"cms.admin.login.unknown",
						"username=" + username );
			} catch (BadCredentialsException e) {
				errors.addErrorString(e.getMessage());

				adminLogMng.loginFailure(request,username,"cms.admin.login.badCredentials",
						"username=" + username + ";password=" + password);
				// 登录失败,cookie返回
				wirteCookieErrorTimes(errorTimes,request,response,model);
				adminMng.updateLoginFailureInfo(username,ip,maxErrorTimes);
			} catch (DisabledException e) {
				errors.addErrorString(e.getMessage());
				adminLogMng.loginFailure(request, username,"cms.admin.login.disabled",
						"username=" + username );
			}

		}


		//校验失败仍需要重新写入剩余错误次数，此处要加1
		wirteCookieErrorTimes(errorTimes-1,request,response,model);

		errors.toModel(model);




		if (!StringUtils.isBlank(processUrl)) {
			model.addAttribute(PROCESS_URL, processUrl);
		}
		if (!StringUtils.isBlank(returnUrl)) {
			model.addAttribute(RETURN_URL, returnUrl);
		}
		if (!StringUtils.isBlank(message)) {
			model.addAttribute(MESSAGE, message);
		}
		return "login";
	}

	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		String authId = (String) session.getAttribute(request, AUTH_KEY);
		if (authId != null) {
			authMng.deleteById(authId);
			session.logout(request, response);
		}
		String processUrl = RequestUtils.getQueryParam(request, PROCESS_URL);
		String returnUrl = RequestUtils.getQueryParam(request, RETURN_URL);
		String view = getView(processUrl, returnUrl, authId);
		if (view != null) {
			return view;
		} else {
			return "redirect:login.jspx";
		}
	}

	private WebErrors validateSubmit(String username, String password,
									 String captcha, Integer errorTimes,
									 Integer errorInterval, Integer disabledTimes,
									 HttpServletRequest request,
									 HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);



		if (errors.ifOutOfLength(username, "用户名", 3, 100)) {
			return errors;
		}
		if (errors.ifOutOfLength(password, "密码", 3, 32)) {
			return errors;
		}
		// 如果输入了验证码，那么必须验证；如果没有输入验证码，则根据当前用户判断是否需要验证码。
		if (!StringUtils.isBlank(captcha)
				|| (errorTimes != null && errorTimes >= 3)) {
			if (errors.ifBlank(captcha, "验证码", 100)) {
				return errors;
			}
			try {
				if (!imageCaptchaService.validateResponseForID(session
						.getSessionId(request, response), captcha)) {
					errors.addErrorCode("error.invalidCaptcha");
					return errors;
				}
			} catch (CaptchaServiceException e) {
				errors.addErrorCode("error.exceptionCaptcha");
				log.warn("", e);
				return errors;
			}
		}

		//此处校验禁用时间是否已到
		Admin admin = adminMng.findByUsername(username);
		if(admin !=null){

			Date lastErrorTime = admin.getErrorTime();
			if(lastErrorTime!=null && errorTimes==disabledTimes){
				Long now = System.currentTimeMillis();
				if((now - lastErrorTime.getTime())/(60*1000L)<errorInterval){
					errors.addErrorCode("error.interval",(lastErrorTime.getTime()+errorInterval*60*1000L-now)/(60*1000L));
				}
			}

		}
		return errors;
	}

	/**
	 * 获得地址
	 * 
	 * @param processUrl
	 * @param returnUrl
	 * @param authId
	 * @return
	 */
	private String getView(String processUrl, String returnUrl, String authId) {
		if (!StringUtils.isBlank(processUrl)) {
			StringBuilder sb = new StringBuilder("redirect:");
			sb.append(processUrl).append("?").append(AUTH_KEY).append("=")
					.append(authId);
			if (!StringUtils.isBlank(returnUrl)) {
				sb.append("&").append(RETURN_URL).append("=").append(returnUrl);
			}
			return sb.toString();
		} else if (!StringUtils.isBlank(returnUrl)) {
			StringBuilder sb = new StringBuilder("redirect:");
			sb.append(returnUrl);
			return sb.toString();
		} else {
			return null;
		}
	}

	private void wirteCookieErrorTimes(Integer userErrorTimes, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 所有访问的页面都需要写一个cookie，这样可以判断已经登录了几次。
		Integer error = getCookieErrorTimes(request,response);
		Config.ConfigLogin configLogin = configMng.getConfigLogin();
		Integer errorInterval = configLogin.getErrorInterval();
		Integer disabledTimes = configLogin.getDisabledTimes();
		Integer errorTimes = configLogin.getErrorTimes();
		if(userErrorTimes==null){
			error = 0;
		}
		else {
			if(error!=userErrorTimes){
				error = userErrorTimes;
			}
			if(userErrorTimes!=disabledTimes){
				error=++userErrorTimes;
			}

		}
		model.addAttribute("userErrorTimes", error);
		CookieUtils.addCookie(request, response, COOKIE_ERROR_TIMES,
				error.toString(), errorInterval * 60, null);


	}








	private void removeCookieErrorTimes(HttpServletRequest request,
											HttpServletResponse response) {
		CookieUtils.cancleCookie(request, response, COOKIE_ERROR_TIMES,
				null);
	}



	private Integer getCookieErrorTimes(HttpServletRequest request,HttpServletResponse response){
		Cookie cookie = CookieUtils.getCookie(request,COOKIE_ERROR_TIMES);
		if(cookie !=null){
			String value = cookie.getValue();
			if(NumberUtils.isDigits(value)){
				return Integer.parseInt(value);
			}
		}
		return null;
	}

	@Autowired
	private AdminMng adminMng;
	@Autowired
	private ConfigMng configMng;
	@Resource(name="adminAuthenticationMng")
	private AuthenticationMng authMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ImageCaptchaService imageCaptchaService;

	@Resource(name="adminLogMng")
	private UserLogMng adminLogMng;
}
