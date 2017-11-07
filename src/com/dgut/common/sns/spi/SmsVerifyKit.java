package com.dgut.common.sns.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONML;
import org.springframework.stereotype.Component;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;

import com.dgut.app.AppConstants;

import com.dgut.common.sns.utils.MobClient;
import com.dgut.common.util.DateUtils;
import com.dgut.main.Constants;


/**
 * 服务端发起验证请求验证移动端(手机)发送的短信
 * @author Administrator
 *
 */
@Component
public class SmsVerifyKit {

	private String appkey;
	private String phone ;
	private String zone ;
	private String code ;
	
	private JSONArray mobiles;
	private JSONArray params;
	private String templateID; 
	
	
	//扣费成功
	public static final String CHARGEING_SNS="3033242";
	//退款成功
	public static final String REFUND_SNS="3033244";
	//充值短信模版
	public static final String RECHARGE_SNS="3031278";	

	
	
	
	public SmsVerifyKit() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * 
	 * @param appkey 应用KEY
	 * @param phone 电话号码 xxxxxxxxx
	 * @param zone 区号 86
	 * @param code 验证码 xx
	 */
	public SmsVerifyKit(String appkey, String phone, String zone, String code) {
		super();
		this.appkey = appkey;
		this.phone = phone;
		this.zone = zone;
		this.code = code;
	}
	
	
	
	/**
	 * 
	 * @param appkey 应用KEY
	 * @param phone 电话号码 xxxxxxxxx
	 * @param zone 区号 86
	 * 
	 */
	public SmsVerifyKit(String appkey, String phone, String zone) {
		super();
		this.appkey = appkey;
		this.phone = phone;
		this.zone = zone;
	}
	
	



	/**
	 * 
	 * @param appkey 应用KEY
	 * @param mobiles 电话号码(不超过100)
	 * @param params 参数，若为空则返回短信全文
	 * 
	 */
	public SmsVerifyKit(String appkey, List<String> mobiles, List<String> params,String templateId) {
		super();
		this.appkey = appkey;
		this.mobiles = JSONArray.fromObject(mobiles);
		this.params = JSONArray.fromObject(params);
		this.templateID=templateId;
	}



	/**
	 * 服务端发起验证请求验证移动端(手机)发送的短信
	 * @return
	 * @throws Exception
	 */
	public  String go() throws Exception{

		String address = "https://api.netease.im/sms/sendcode.action";
		MobClient client = null;
		try {
			String nonce = "asdhauisdasd";
			String time = System.currentTimeMillis()/1000+"";
			client = new MobClient(address);
			client.addParam("mobile", phone);
			client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			client.addRequestProperty("AppKey", appkey);
			client.addRequestProperty("Nonce", nonce);
			client.addRequestProperty("CurTime",time);
			client.addRequestProperty("CheckSum", CheckSumBuilder.getCheckSum("e2fd8936a00c", nonce, time));

			String result = client.post();
			System.out.println(result);
			return result;
		} finally {
			client.release();
		}
	}


	/**
	 * 服务端发验证服务端发送的短信
	 * @return
	 * @throws Exception
	 */
	public  String checkcode() throws Exception{

		String address = "https://api.netease.im/sms/verifycode.action";
		MobClient client = null;
		try {
			

			String nonce = "asdhauisdasd";
			String time = System.currentTimeMillis()/1000+"";
			client = new MobClient(address);
			client.addParam("mobile", phone).addParam("code", code);
			client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			client.addRequestProperty("AppKey", appkey);
			client.addRequestProperty("Nonce", nonce);
			client.addRequestProperty("CurTime",time);
			client.addRequestProperty("CheckSum", CheckSumBuilder.getCheckSum("e2fd8936a00c", nonce, time));
			String result = client.post();
			System.out.println(result);
			return result;

		
		} finally {
			client.release();
		}
	}
	
	/**
	 * 服务端发验证短信模版
	 * @return
	 * @throws Exception
	 */
	public  String sendTplSms() throws Exception{

		String address = "https://api.netease.im/sms/sendtemplate.action";
		MobClient client = null;
		try {
			
			
			String nonce = "asdhakisdasd";
			String time = System.currentTimeMillis()/1000+"";
			client = new MobClient(address);
			client.addParam("mobiles", mobiles.toString()).addParam("templateid", templateID).addParam("params", params.toString());
			client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			client.addRequestProperty("AppKey", appkey);
			client.addRequestProperty("Nonce", nonce);
			client.addRequestProperty("CurTime",time);
			

			client.addRequestProperty("CheckSum", CheckSumBuilder.getCheckSum("e2fd8936a00c", nonce, time));
			

			String result = client.post();
			System.out.println(result);
			return result;
			
		
		} finally {
			client.release();
		}
	}

	public static void main(String[] args) throws Exception {
		/*List<String> mobiles=new ArrayList<String>();
		mobiles.add("15818455500");
		
		
		List<String> params=new ArrayList<String>();
		params.add("15818348376");
		params.add("201341402206");
		params.add("zzw");
		params.add("15818348376");
		
		String c=new SmsVerifyKit(Constants.SNS_ENCRYPTION_KEY, mobiles,params,CHOOSE_EMPLOYEE_SNS).sendTplSms();
		System.out.println(c);*/
		
		SmsVerifyKit sv=new SmsVerifyKit(Constants.APP_SNS_KEY, "15818348376", "86");
		sv.go();
		
		
	}

}
