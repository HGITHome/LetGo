package com.dgut.app.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.app.helper.WithdrawWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.pck.Encrypt;
import com.dgut.common.web.RequestUtils;
import com.dgut.main.Constants;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Withdraw;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.WithdrawMng;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.dgut.common.page.Pagination;
import com.dgut.main.web.CmsUtils;


import static com.dgut.common.page.SimplePage.cpn;
import static com.dgut.common.page.SimplePage.cps;

@Service
public class WithdrawService {

	public String addWithdraw(HttpServletRequest request,
							  HttpServletResponse response, Map<String, String> parameters)
			throws IOException {
		Map<String, String> jsonMap = new HashMap<String, String>();
		Member member = CmsUtils.getMember(request);
		// member=memberMng.findById(5);
		String amount = parameters.get("amount");
		String account = Encrypt.decrypt3DES(member.getBalance(),
				Constants.ENCRYPTION_KEY);
		if (StringUtils.isBlank(member.getPayAccount())) {
			jsonMap.put("state", "-3");
			jsonMap.put("msg", "请先绑定账户");
			return JSONUtils.printObject(jsonMap);
		} else if (Double.parseDouble(amount) < 0) {
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "金额不能是负数");
			return JSONUtils.printObject(jsonMap);
		} else if (Double.parseDouble(amount) > Double.parseDouble(account)) {
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "提现金额大于账户余额");
			return JSONUtils.printObject(jsonMap);
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		/*Withdraw withdraw = new Withdraw();
		withdraw.setMember(member);
		withdraw.setWithdraw_time(new Date());
		withdraw.setWithdraw_status(false);
		withdraw.setWithdraw_amount(Double.parseDouble(amount));
		withdraw.setWithdraw_ip(RequestUtils.getIpAddr(request));
		withdrawMng.save(withdraw);*/

		withdrawMng.save(member,Double.parseDouble(amount), RequestUtils.getIpAddr(request));


		// 冻结余额
		member.setBalance(Encrypt.encrypt3DES(Double.parseDouble(account)
				- Double.parseDouble(amount)+"", Constants.ENCRYPTION_KEY));
		memberMng.updateMember(member);



		jsonMap.put("state", "0");
		jsonMap.put("msg", "你的提现申请已受理,请耐心等候");

		memberLogMng.operating(request, "cms.member.withdraw",
				"提现￥" + df.format(Double.parseDouble(amount)));
		return JSONUtils.printObject(jsonMap);
	}


	public String getListByUser(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
			throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		Member member = CmsUtils.getMember(request);
//		Member member = memberMng.findById(3);
		String flagStr = parameters.get("withdraw_flag");
		String pageNo = parameters.get("pageNo");
		String pageSize = parameters.get("pageSize");
		Boolean flag = null;
		if (!StringUtils.isBlank(flagStr)) {
			flag = Boolean.parseBoolean(flagStr);
		}
		Pagination pagincation = withdrawMng.getListByUser(member.getId(), flag,
				cpn(pageNo), cps(pageSize));
		
		
//		jsonMap.put("withdrawList", WithdrawWrapper.convertWithdrawList((List<Withdraw>) pagincation.getList()));
		jsonMap.put("result",WithdrawWrapper.convertWithdrawList((List<Withdraw>) pagincation.getList()));
		jsonMap.put("pageNo", cpn(pageNo));
		jsonMap.put("pageSize", cps(pageSize));
		jsonMap.put("totalCount", pagincation.getTotalCount());
		jsonMap.put("state", "0");
		jsonMap.put("msg", "获取提现记录成功");

		return JSONUtils.printObject(jsonMap);
	}

	@Resource(name="memberLogMng")
	private UserLogMng memberLogMng;

	@Autowired
	private WithdrawMng withdrawMng;
	
	@Autowired
	private MemberMng memberMng;
}
