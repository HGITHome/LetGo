package com.dgut.app.service;


import java.io.IOException;

import java.text.DecimalFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.app.utils.JSONUtils;
import com.dgut.common.util.PKUtils;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Recharge;


import com.dgut.main.member.manager.RechargeMng;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.dgut.main.Constants;
import com.dgut.main.web.CmsUtils;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;


@Service
public class RechargeService {


	/**
	 * opt=90 充值账户
	 * @param request
	 * @param response
	 * @param parameters
	 * @return
     * @throws IOException
     */
    public String bindAccount(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
					throws IOException {
//		Pingpp.apiKey = Constants.PINGPP_LICENSE_KEY;
		Pingpp.apiKey = Constants.PINGPP_TEST_KEY;


		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Double amount=1.0;
		
		Member member=CmsUtils.getMember(request);
		//检查是否绑定过账户
		if(StringUtils.isNotBlank(member.getPayAccount())){
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "你已经绑定过账户");
			return JSONUtils.printObject(jsonMap);
		}
		
		String ip=getIp(request);
		//根据尾号判定是充值还是绑定账户，0为绑定，1为充值
		String id=PKUtils.getUniqueID()+"0";
		//
		String content="绑定账户";
		Charge charge=chargeService.createCharge(amount,content, id, ip);
		if(charge==null){
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "获取支付凭证超时，稍等一会再试");
		}
		else{

			/*Recharge recharge=new Recharge();
			recharge.setMember(member);
			recharge.setId(id);
			recharge.setChargeId(charge.getId());
			recharge.setRechargeTime(new Date());
			recharge.setRechargeAmount(Double.parseDouble(amount));
			recharge.setRechargeIp(ip);
			recharge.setRechargeStatus(false);*/


			rechargeMng.save(member,id,charge.getId(),amount,ip);


			jsonMap.put("state", "0");
			jsonMap.put("charge", charge);
			jsonMap.put("msg", "获取支付凭证成功");
			
			memberLogMng.operating(request, "cms.member.bind", "绑定账户");
		}
		return JSONUtils.printObject(jsonMap);

	}


	/**
	 * opt=91 充值凭证
 	 * @param request
	 * @param response
	 * @param parameters
	 * @return
     * @throws IOException
     */
	public String rechargeAccount(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
					throws IOException {
		
//		Pingpp.apiKey = (String) context.getAttribute("apiKey");
		Pingpp.apiKey = Constants.PINGPP_TEST_KEY;
		Pingpp.privateKeyPath = request.getSession().getServletContext().getRealPath("/WEB-INF")+"/paykey/rsa_private_key.pem";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String amount=parameters.get("amount");
		DecimalFormat df=new DecimalFormat("######0.00");
		String ip=getIp(request);
		
		
		if(StringUtils.isBlank(amount)){
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "充值金额不能为空");
			return JSONUtils.printObject(jsonMap);
		}
		else if(Double.parseDouble(amount)<=0){
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "充值金额应大于0");
			return JSONUtils.printObject(jsonMap);
			
			
		}

		String id= PKUtils.getUniqueID();
		String content="账户充值";
		Charge charge=chargeService.createCharge(Double.parseDouble(amount),content, id, ip);
		if(charge==null){
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "获取支付凭证超时，稍等一会再试");
		}
		else{
			Member member=CmsUtils.getMember(request);

			Recharge bean = rechargeMng.save(member,id,charge.getId(),Double.parseDouble(amount),ip);

			jsonMap.put("state", "0");
			jsonMap.put("charge", charge);
			jsonMap.put("msg", "获取支付凭证成功");
			memberLogMng.operating(request, "cms.member.recharge", "充值￥"+df.format(bean.getRechargeAmount()));
		}
		return JSONUtils.printObject(jsonMap);

	}


    /**
	 * 获得ip
	 * @param request
	 * @return
     */
	private String getIp(HttpServletRequest request) {
		String ip=request.getHeader("x-forwarded-for");
		if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("Proxy-Client-IP");
		}
		if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
			ip=request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")? "127.0.0.1":ip;
	}


	

	@Resource(name="memberLogMng")
	private UserLogMng memberLogMng;
	
	
	@Autowired
	private RechargeMng rechargeMng;

	@Autowired
	private ChargeService chargeService;


	

}
