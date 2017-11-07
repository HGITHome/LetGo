package com.dgut.app.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.City;
import com.dgut.main.member.entity.Member;


public class UserWrapper {





	public static Map<String,Object> convertMemberInfo(Member member){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map<String,Object> provinceMap =null,cityMap = null;

		if(member!=null){

			resultMap.put("username", member.getUsername());

//			resultMap.put("city", city==null? null:city.getCity_name());
			//2017-01-29修改city

			//2017-01-29修改province
			City city=member.getCity();
			if(city!=null){
				cityMap =  new HashMap<>();
				cityMap.put("id",city.getId());
				cityMap.put("city_name",city.getCity_name());

				provinceMap = new HashMap<>();
				provinceMap.put("id", city.getProvince().getId());
				provinceMap.put("province_name",city.getProvince().getProvince_name());

				cityMap.put("province",provinceMap);
			}
			resultMap.put("city",cityMap);





			resultMap.put("mobile", member.getMobile());

			resultMap.put("gender", member.getGender());
			resultMap.put("icon", member.getIcon());
			resultMap.put("easemob_name", member.getEasemob_name());
			resultMap.put("userid", Encrypt.encrypt3DES(member.getId()+"", Constants.ENCRYPTION_KEY));
		}
		//		resultMap.put("index", member.getChinese().substring(0, 1).toUpperCase());
		//		resultMap.put("is_stranger", true);
		return resultMap;
	}



	public static void main(String []args){

	}

}
