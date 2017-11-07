package com.dgut.app.helper;

import com.dgut.main.member.entity.Withdraw;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class WithdrawWrapper {

	
	/**
	 * 格式化提现列表
	 * @param list
	 * @return
	 */
	public static List< Map<String ,Object>> convertWithdrawList(List<Withdraw> list){
		List< Map <String ,Object >> result = new ArrayList < Map< String ,Object>>();
		Map <String ,Object > map =null;
		DecimalFormat    df   = new DecimalFormat("######0.00");  
		
		for(Withdraw withdraw :list){
			map = new HashMap<String,Object>();
			map.put("pay_account", withdraw.getMember().getPayAccount());
			map.put("withdraw_time", withdraw.getWithdrawTime().getTime());
			map.put("withdraw_account", df.format(withdraw.getWithdrawAmount()));
			map.put("withdraw_flag", withdraw.getWithdrawStatus());
			
			result.add(map);
		}
		return result;
	}
}
