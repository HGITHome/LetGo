package com.dgut.common.easemob;

import java.io.IOException;

import net.sf.json.JSONObject;


import com.dgut.common.easemob.api.IMUserAPI;
import com.dgut.common.easemob.comm.ClientContext;
import com.dgut.common.easemob.comm.EasemobRestAPIFactory;
import com.dgut.common.easemob.comm.body.IMUserBody;
import com.dgut.common.easemob.comm.wrapper.BodyWrapper;


public class TestDemo {

	/*static EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();

	static IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);

	public  static  void testFriendList(){
		System.out.println(user.getFriends("cyuk3"));
	}

	public void testRegist(){

		BodyWrapper userBody = new IMUserBody("zzw1223", "123456", "1elloWorld");

		Object result=user.createNewIMUserSingle(userBody);
//		System.out.println(result);
		
		JSONObject obj=JSONObject.fromObject(result);
		System.out.println(obj);
		if(obj.getInt("responseStatus")==200){
			System.out.println("注册成功");
		}
		else{
			System.out.println("注册失败吧");
		}
	}


	
	public static void main(String []args) throws IOException{
		testFriendList();
	}
*/
}
