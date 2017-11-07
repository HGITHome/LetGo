package com.dgut.main.member.action;

import java.text.DecimalFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;

import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Withdraw;
import com.dgut.main.member.manager.WithdrawMng;
import com.dgut.main.web.WebErrors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;



import static com.dgut.common.page.SimplePage.cpn;

@Controller
@RequestMapping("withdraw")
public class LeWithdrawAct {

	@RequestMapping("v_list.do")
	public String getList(String queryUsername, String queryStatus,
			String pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Pagination pagination = withdrawMng.getList(queryUsername, queryStatus,
				cpn(pageNo), CookieUtils.getPageSize(request));

		model.addAttribute("pagination", pagination);
		model.addAttribute("queryUsername", queryUsername);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("pageSize", CookieUtils.getPageSize(request));
		model.addAttribute("pageNo", pageNo);
		return "member/withdraw/list";
	}

	@RequestMapping("o_update.do")
	public String updateWithdraw(Integer id, String queryUsername,
			String queryStatus, String pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		WebErrors errors = validateUpdate(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		// 更新提现进度
		Withdraw withdraw = withdrawMng.findById(id);
		withdraw.setWithdrawStatus(true);
		withdraw.setFinishTime(new Date());
		withdrawMng.update(withdraw);


		DecimalFormat df=new DecimalFormat("######0.00");
		adminLogMng.operating(
				request,
				"cms.withdraw.agree",
				"用户" + withdraw.getMember().getUsername() + "提现金额:"
						+ df.format(withdraw.getWithdrawAmount()));

		return getList(queryUsername, queryStatus, pageNo, request, response,
				model);
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (isValid(id, errors)) {
			return errors;
		}

		return errors;
	}

	private boolean isValid(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		Withdraw entity = withdrawMng.findById(id);
		if (errors.ifNotExist(entity, Withdraw.class, id)) {
			return true;
		} else if (entity.getWithdrawStatus()) {
			errors.addError("该申请已完成，提现失败");
		}
		return false;
	}

	@Autowired
	private WithdrawMng withdrawMng;

	@Resource(name="adminLogMng")
	private UserLogMng adminLogMng;



}
