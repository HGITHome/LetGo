package com.dgut.app.action;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Recharge;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.RechargeMng;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.dgut.app.service.SNSService;
import com.dgut.common.sns.spi.SmsVerifyKit;
import com.dgut.main.Constants;


import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.PingppObject;
import com.pingplusplus.model.Webhooks;

@Controller
public class WebHooksData {
	
	

	/**
	 * 充值回调
	 * 监听pingplus回调
	 * @throws IOException 
	 * 
	 * @throws Exception
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * */
	@RequestMapping("/check.do")
	public void rechargeWebhook(HttpServletRequest request,
			HttpServletResponse response) throws IOException  {
		
		ServletContext context=request.getServletContext();
		Pingpp.apiKey = (String) context.getAttribute("apiKey");
		// 获得头部信息
		Enumeration<String> headNames = request.getHeaderNames();
		while (headNames.hasMoreElements()) {
			String key = headNames.nextElement();
			String value = request.getHeader(key);
//			System.out.println(key + ":" + value);
		}
		// 获得http body内容
		BufferedReader reader = request.getReader();
		StringBuffer buffer = new StringBuffer();
		String string;
		while ((string = reader.readLine()) != null) {
			buffer.append(string);
		}
		System.out.println("buffer=" + buffer.toString());
		reader.close();

		// 获得header中的签名
		String signature = request.getHeader("x-pingplusplus-signature");
		/* String
		 data=getStringFromFile(request.getSession().getServletContext().getRealPath("/WEB-INF")+"/paykey/buffer.txt");*/
		// 获得公钥
		String path = request.getSession().getServletContext()
				.getRealPath("/WEB-INF")
				+ "/paykey/rsa_public_key.pem";
		// 验证是否是ping++的回调请求
		boolean result = false;
		try {
			result = verifyData(buffer.toString(), signature,
					getPubKey(path));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*boolean result = verifyData(data, signature,
				getPubKey(path));*/
		
		
		if (result) {
			// 解析异步通知数据
			Event event = Webhooks.eventParse(buffer.toString());
//			 Event event = Webhooks.eventParse(data);
			if ("charge.succeeded".equals(event.getType())) {

				// 转化为json查找指定的充值记录，修改充值状态
				Gson gson = PingppObject.PRETTY_PRINT_GSON;
				String json = gson.toJson(event.getData().getObject());
				JSONObject object = JSONObject.fromObject(json);
//				String recharge_id = (String) object.get("id");
				Double amount = object.getDouble("amount");
				String transaction_no=object.getString("transaction_no");
				String order_no=object.getString("order_no");
			
				System.out.println("orderno="+order_no);
			
				
				char order_type=order_no.charAt(order_no.length()-1);
				
				Recharge recharge = rechargeMng.findById(order_no);
			
				if (recharge != null) {
					// 更新用户账户余额
				
					Member member = recharge.getMember();
					System.out.println("--------会员----------------"+member.getId());
				
					Double member_account=Double.parseDouble(Encrypt.decrypt3DES(member.getBalance(), Constants.ENCRYPTION_KEY));
				
					member.setBalance(Encrypt.encrypt3DES(member_account + amount/100.0+"",Constants.ENCRYPTION_KEY));
				
					recharge.setRechargeStatus(true);
					recharge.setTransactionNo(transaction_no);
					rechargeMng.update(recharge);
					System.out.println("");
					if(order_type=='0'){
						//获得支付宝账户
						JSONObject extra=(JSONObject) object.get("extra");					
						String account=extra.getString("buyer_account");					
						member.setPayAccount(account);
					}
					memberMng.updateMember(member);
					
					
					// 充值成功发送提示短信
					List<String> mobiles = new ArrayList<String>();
					List<String> params = new ArrayList<String>();

					mobiles.add(member.getMobile());
					params.add(member.getMobile());
					params.add(amount/100 + "");
					smsVerifyKit = new SmsVerifyKit(Constants.APP_SNS_KEY,
							mobiles, params, SmsVerifyKit.RECHARGE_SNS);			
					try {
						snsAction.sendTplSms(smsVerifyKit);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					

				}
				
				
				//返回给ping++成功的状态码
				response.setStatus(200);
			} else if ("refund.succeeded".equals(event.getType())) {
				response.setStatus(200);
			} 
			else {
			
				response.setStatus(500);
			}
		}

	}
	
	
	
	

	/**
	 * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getStringFromFile(String filePath) throws Exception {
		FileInputStream in = new FileInputStream(filePath);
		InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
		BufferedReader bf = new BufferedReader(inReader);
		StringBuilder sb = new StringBuilder();
		String line;
		do {
			line = bf.readLine();
			if (line != null) {
				if (sb.length() != 0) {
					sb.append("\n");
				}
				sb.append(line);
			}
		} while (line != null);

		return sb.toString();
	}

	private PublicKey getPubKey(String path) throws Exception {
		String pubKeyString = getStringFromFile(path);
		pubKeyString = pubKeyString.replaceAll(
				"(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
		byte[] keyBytes = Base64.decodeBase64(pubKeyString);

		// generate public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);
		return publicKey;
	}

	/**
	 * 验证签名
	 * 
	 * @param dataString
	 * @param signatureString
	 * @param publicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean verifyData(String dataString, String signatureString,
			PublicKey publicKey) throws NoSuchAlgorithmException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException {
		byte[] signatureBytes = Base64.decodeBase64(signatureString);
		if (signatureBytes != null) {
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initVerify(publicKey);
			signature.update(dataString.getBytes("UTF-8"));
			return signature.verify(signatureBytes);
		}
		return false;
	}

	@Autowired
	private SNSService snsAction;

	@Autowired
	private MemberMng memberMng;
	
	


	@Autowired
	private RechargeMng rechargeMng;
	
	



	private SmsVerifyKit smsVerifyKit;


}
