package com.dgut.main.member.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Recharge;
import com.dgut.main.member.manager.RechargeMng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;

import com.dgut.main.web.WebErrors;


import static com.dgut.common.page.SimplePage.cpn;

@Controller
@RequestMapping("recharge")
public class LeRechargeAct {

	private static final Logger log = LoggerFactory
			.getLogger(LeRechargeAct.class);

	@Autowired
	private RechargeMng rechargeMng;

	@Resource(name="adminLogMng")
	private UserLogMng adminLogMng;

	@RequestMapping("v_list.do")
	public String getList(String queryUsername, String queryStatus,
			String pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		Pagination pagination = rechargeMng.getList(queryUsername, "1",
				cpn(pageNo), CookieUtils.getPageSize(request));

		model.addAttribute("pagination", pagination);
		model.addAttribute("queryUsername", queryUsername);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("pageNo", pageNo);

		return "member/recharge/list";
	}





	private boolean vldExist(String id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		Recharge entity = rechargeMng.findById(id);
		if (errors.ifNotExist(entity, Recharge.class, id)) {
			return true;
		}
		return false;
	}
}
