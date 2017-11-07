package com.dgut.main.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dgut.common.page.Pagination;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.dao.RedEnvolopeDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.RedEnvolopeMng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class RedEnvolopeQuartz {
	private static final Logger log = LoggerFactory
			.getLogger(RedEnvolopeQuartz.class);

	@Autowired
	private RedEnvolopeMng redEnvolopeMng;

	@Autowired
	private MemberMng memberMng;




	/**
	 * 超时红包处理,隔5分钟检测一次
	 */
	@SuppressWarnings("unchecked")
	public void redEnvolopeRefund() {
		Pagination paginnation = redEnvolopeMng.getRedEnvolopes(null,null,null,null,null,null,"0",1,Integer.MAX_VALUE);

		List<RedEnvolope> list = (List<RedEnvolope>) paginnation.getList();
		Member member = null;
		Double balance = 0.0;
		for(RedEnvolope bean : list){
			if(bean.getSurplus()>0.0){
				bean.setIsInvalid(true);
				balance = Double.parseDouble(Encrypt.decrypt3DES(member.getBalance(), Constants.ENCRYPTION_KEY));
				member.setBalance(Encrypt.encrypt3DES(String.valueOf(balance+bean.getSurplus()),Constants.ENCRYPTION_KEY));
				memberMng.updateMember(member);
				redEnvolopeMng.update(bean);
			}
		}

		System.out.println(123);



	}




}
