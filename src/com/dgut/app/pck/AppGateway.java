package com.dgut.app.pck;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.common.pck.Encrypt;
import com.dgut.common.security.encoder.Md5PwdEncoder;

import org.apache.commons.lang.StringUtils;

import com.dgut.common.util.DateUtils;
import com.dgut.main.Constants;


public class AppGateway {

	public static int handle(String key, int allowTimespanSeconds, GeneralRestGatewayInterface grgi,HttpServletRequest request,
			HttpServletResponse response, StringBuilder errorDescription)
					throws IOException
	{
		errorDescription.delete(0, errorDescription.length());
		request.setCharacterEncoding("utf-8");
		if (StringUtils.isBlank(key))
		{
			throw new RuntimeException("在使用 GeneralRestGateway.handle 方法解析参数并处理业务时，必须提供一个用于摘要签名用的 key (俗称 MD5 加盐)。");
		}

		if ((request == null) || (response == null)) {
			errorDescription.append("Http 上下文错误。");

			return -2;
		}

		Set<String> keys = request.getParameterMap().keySet();

		//	    Set keys = request.params.data.keySet();

		if (keys.isEmpty()) {
			errorDescription.append("没有找到 Query 参数，接口数据非法。");
			return -3;
		}

		Map<String,String> parameters = new HashMap<String,String>();
		String _s = ""; String _t = "";

		for (String t_key : keys) {
			if (t_key.equals("_s")) {
				_s = request.getParameter("_s");
			}
			else
			{
				if (t_key.equals("_t")) {
					_t = URLDecoder.decode(request.getParameter("_t"), "utf-8");
				}
				//	        System.out.println("-----");
				//	        String keyValue = StrUtils.replaceKeyString(URLDecoder.decode(request.getParameter(t_key), "utf-8"));
				//	        System.out.println("key:"+t_key+" value :" +keyValue);
				//	        System.out.println("-----");
				parameters.put(t_key, URLDecoder.decode(request.getParameter(t_key), "utf-8"));
			}
		}
		if ((StringUtils.isBlank(_s)) || (StringUtils.isBlank(_t))) {
			errorDescription.append("缺少 _s 或 _t 参数，接口数据非法。");

			return -4;
		}

		List<String> parameterNames = new ArrayList<String>(parameters.keySet());
		Collections.sort(parameterNames);

		StringBuffer signData = new StringBuffer();

		for (int i = 0; i < parameters.size(); i++) {
			signData.append((String)parameterNames.get(i) + "=" + (String)parameters.get(parameterNames.get(i)) + (i < parameters.size() - 1 ? "&" : ""));
		}

		Date Result = getLongGoneDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		try
		{
			Result = formatter.parse(_t);
		}
		catch (Exception localException) {
		}

		Date timestamp = Result;
		long span = Math.abs((timestamp.getTime() - System.currentTimeMillis()) / 1000L);

		if ((allowTimespanSeconds > 0) && (span > allowTimespanSeconds)) {
			errorDescription.append("访问超时。");

			return -5;
		}

		if (!_s.equalsIgnoreCase(Encrypt.MD5(signData + key, "utf-8"))) {
			errorDescription.append("签名错误，接口数据非法。");

			return -6;
		}

		parameters.remove("_t");

		String result = "";
		try
		{
			result = grgi.delegateHandleRequest(request,response,parameters, errorDescription);
		} catch (Exception e) {
			e.printStackTrace();
			errorDescription.append("应用程序的代理回调程序遇到异常，详细原因是：" + e.getMessage());

			return -7;
		}

		if (!StringUtils.isBlank(result)) {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(result);
			out.flush();
			out.close();
		}

		return 0;
	}

	private static Date getLongGoneDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(1, -30);

		return calendar.getTime();
	}



}
