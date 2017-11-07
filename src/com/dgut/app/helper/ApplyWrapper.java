package com.dgut.app.helper;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;

import java.util.*;


public class ApplyWrapper {

	/**
	 * 格式化申请信息
	 * @param applications
	 * @return
	 */
	public static List<Map<String,Object>> convertApplys(List<ApplyFriend> applications, Member member){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		Set<Friendship> friendships=member.getFriendships();
		for(ApplyFriend a:applications){
			map=new HashMap<String, Object>();
			map.put("username", a.getPublisher().getUsername());
			map.put("icon", a.getPublisher().getIcon());
			map.put("apply_reason", a.getApply_reason());
			map.put("userid", Encrypt.encrypt3DES( a.getPublisher().getId()+"", Constants.ENCRYPTION_KEY));
			map.put("handle_flag", a.getHandle_flag().ordinal());
			map.put("apply_id", Encrypt.encrypt3DES(a.getId()+"", Constants.ENCRYPTION_KEY));
			map.put("in_blacklist", FriendshipWrapper.isInBlackList(a.getReceiver(), friendships));
			map.put("is_stranger", FriendshipWrapper.isStranger(a.getReceiver(), friendships));
			list.add(map);
		}
		return list;
	}

}
