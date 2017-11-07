package com.dgut.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dgut.main.Constants;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.App;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import org.springframework.stereotype.Service;


@Service
public class ChargeService {


	/**
	 * pingpp 管理平台对应的 API key
	 */
	
//	测试key
//	public static String apiKey = "sk_test_0eL88OzTKOeP1qbHeHzfXDaH";
	
//	public static String apiKey = "sk_live_GenvTOenzj94P4C40OmrLmP0";
	
	
	

	/**
	 * @param amount
	 * @param content
	 * @param orderNo
	 * @param client_ip
	 * @return
	 */
	public Charge createCharge(Double amount,String content,String orderNo,String client_ip) {
		Charge charge = null;
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		
		//正常模式下要乘以100
		chargeMap.put("amount", amount);
	
		chargeMap.put("currency", "cny");
		chargeMap.put("subject", content);
		chargeMap.put("body", "充值费用");
		chargeMap.put("order_no", orderNo);

		chargeMap.put("channel", "alipay");

		
		chargeMap.put("client_ip", client_ip);
		
		Map<String, String> app = new HashMap<String, String>();
		app.put("id",Constants.PINGPP_APP_ID);
		chargeMap.put("app", app);
		try {
			//发起交易请求
			charge = Charge.create(chargeMap);
		} catch (PingppException e) {
			e.printStackTrace();
		}
		return charge;
	}
	



	
	/**
	 * 创建 Charge
	 * 
	 * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
	 * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
	 * @return
	 */
	public Charge createCharge(String order_id,String amount,String content,String cilent_ip,Boolean isBind) {
		Charge charge = null;
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", Integer.parseInt(amount));
		chargeMap.put("currency", "cny");
		chargeMap.put("subject", "MM实验室");
		chargeMap.put("body", content);
		chargeMap.put("order_no", order_id);
		chargeMap.put("channel", "alipay");
		chargeMap.put("client_ip", cilent_ip);
		/*if(isBind){
			Map<String,String> extraMap=new HashMap<String,String>();
			extraMap.put("bind", "1");
			chargeMap.put("extra", extraMap);
		}*/
		Map<String, String> app = new HashMap<String, String>();
		app.put("id",Constants.PINGPP_APP_ID);
		chargeMap.put("app", app);
		try {
			//发起交易请求
			charge = Charge.create(chargeMap);

		} catch (PingppException e) {
			e.printStackTrace();
			return null;
		}
		return charge;
	}

	/**
	 * 查询 Charge
	 * 
	 * 该接口根据 charge Id 查询对应的 charge 。
	 * 参考文档：https://pingxx.com/document/api#api-c-inquiry
	 * 
	 * 该接口可以传递一个 expand ， 返回的 charge 中的 app 会变成 app 对象。
	 * 参考文档： https://pingxx.com/document/api#api-expanding
	 * @param id
	 */
	public void retrieve(String id) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			List<String> expande = new ArrayList<String>();
			expande.add("app");
			param.put("expand", expande);
			//Charge charge = Charge.retrieve(id);
			//Expand app
			Charge charge = Charge.retrieve(id, param);
			if (charge.getApp() instanceof App) {
				//App app = (App) charge.getApp();
				// System.out.println("App Object ,appId = " + app.getId());
			} else {
				// System.out.println("String ,appId = " + charge.getApp());
			}

			System.out.println(charge);

		} catch (PingppException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分页查询Charge
	 * 
	 * 该接口为批量查询接口，默认一次查询10条。
	 * 用户可以通过添加 limit 参数自行设置查询数目，最多一次不能超过 100 条。
	 * 
	 * 该接口同样可以使用 expand 参数。
	 * @return
	 * @throws RateLimitException 
	 */
	public ChargeCollection all() throws RateLimitException {
		ChargeCollection chargeCollection = null;
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("limit", 3);

		//增加此处设施，刻意获取app expande 
		//        List<String> expande = new ArrayList<String>();
		//        expande.add("app");
		//        chargeParams.put("expand", expande);

		try {
			chargeCollection = Charge.all(chargeParams);
			System.out.println(chargeCollection);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chargeCollection;
	}


}
