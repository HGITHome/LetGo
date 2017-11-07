package com.dgut.app.service;

import com.dgut.app.utils.JSONUtils;
import com.dgut.common.sns.spi.SmsVerifyKit;
import com.dgut.common.util.StrUtils;
import com.dgut.main.Constants;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PUNK on 2017/1/22.
 */
@Service
public class SNSService {




    /**
     * 手机申请发送验证码验证 opt=0
     *
     * @return
     * @throws IOException
     */
    public String sendCode(HttpServletRequest request,
                           HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String mobile = request.getParameter("mobile");
        if (StringUtils.isBlank(mobile)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "手机号不能为空");
            return JSONUtils.printObject(jsonMap);
        }
        if (!StrUtils.isMobileNum(mobile)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "手机号格式错误");
            return JSONUtils.printObject(jsonMap);
        }
        try {
            SmsVerifyKit smsVerifyKit= new SmsVerifyKit(Constants.APP_SNS_KEY, mobile,"86");
			/*String result = new SmsVerifyKit(Constants.SNS_ENCRYPTION_KEY, mobile,
					"86").go();*/
            //调用第三方发送短信
            String result=snsAction.sendCode(smsVerifyKit);
            JSONObject object = JSONObject.fromObject(result);


            Integer code= (Integer) object.get("code");
            if (code==200) {
                jsonMap.put("state", 0);
                jsonMap.put("msg", "申请验证码成功");
            } else if (code==403) {
                jsonMap.put("state", -1);
                jsonMap.put("msg", "非法操作");
            } else if (code==414) {
                jsonMap.put("state", -1);
                jsonMap.put("msg", "参数错误");
            } else if (code==416) {
                jsonMap.put("state", -1);
                jsonMap.put("msg", "请求校验验证码频繁");
            } else if (code==500) {
                jsonMap.put("state", -1);
                jsonMap.put("msg", "服务器内部错误");
            }

            else {
                jsonMap.put("state", -1);
                jsonMap.put("msg", "code:" + code);
            }
        } catch (Exception e) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "请求失败" + e.getLocalizedMessage());
        }
        System.out.println("发送验证码:"+JSONUtils.printObject(jsonMap));

        return JSONUtils.printObject(jsonMap);
    }

    @Autowired
    private SNSService snsAction;


    //发送验证码
    public String sendCode(SmsVerifyKit smsVerifyKit) throws Exception{

        return smsVerifyKit.go();
    }

    //检测验证码的有效性
    public String checkCode(SmsVerifyKit smsVerifyKit) throws Exception{
        return smsVerifyKit.checkcode();
    }

    //发送模版代码
    public String sendTplSms(SmsVerifyKit smsVerifyKit) throws Exception{
        return smsVerifyKit.sendTplSms();
    }


    /**
     * 手机验证码验证
     */
    public Map<String, String> codeVerify(String mobile, String code) {
        Map<String, String> jsonMap = new HashMap<String, String>();

        if(StringUtils.isBlank(mobile)){
            jsonMap.put("state", "-1");
            jsonMap.put("msg", "手机号不能为空");
        }
        else if (!StrUtils.isMobileNum(mobile)) {
            jsonMap.put("state", "-1");
            jsonMap.put("msg", "手机号码错误");
            return jsonMap;
        }
        if (StringUtils.isBlank(code)) {
            jsonMap.put("state", "-1");
            jsonMap.put("msg", "验证码不能为空");
            return jsonMap;
        }
        try {
//			String result = new SmsVerifyKit(Constants.SNS_ENCRYPTION_KEY, mobile, "86", code).checkcode();
            SmsVerifyKit smsVerifyKit=new SmsVerifyKit(Constants.APP_SNS_KEY, mobile, "86", code);
            String result=checkCode(smsVerifyKit);
            JSONObject object = JSONObject.fromObject(result);
            System.out.println("mobile"+mobile);
            System.out.println("code"+code);
            System.out.println(object);

            Integer c=(Integer) object.get("code");
            if (c==200) {
                jsonMap.put("state", "0");
                jsonMap.put("msg", "验证成功");
            } else if ((c==404)||c==413) {
                jsonMap.put("state", "-1");
                jsonMap.put("msg", "验证码无效");
            } else if (c==414) {
                jsonMap.put("state", "-1");
                jsonMap.put("msg", "参数错误");
            } else {
                jsonMap.put("state", "-1");
                jsonMap.put("msg", "code:" + c);
            }
        } catch (Exception e) {
            jsonMap.put("state", "-1");
            jsonMap.put("msg", "请求失败" + e.getLocalizedMessage());
        }

        return jsonMap;
    }


}
