package com.dgut.app.helper;

import java.util.*;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;


import com.dgut.main.Constants;
import com.dgut.main.member.entity.base.BaseFriendship;
;

public class FriendshipWrapper {
	
	/**
	 * 好友列表返回信息格式化
	 * @param friendships
	 * @return
	 */
	public static List<Map<String, Object>> convertFriendListInfo(List<Friendship> friendships){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String,Object> userMap;
		for(Friendship friendship:friendships){
			
			userMap= UserWrapper.convertMemberInfo(friendship.getFriend());
			userMap.put("alias", friendship.getAlias());
			userMap.put("index", friendship.getChinese().substring(0, 1).toUpperCase());
		
			userMap.put("is_stranger", false);
			userMap.put("in_blacklist", friendship.getFriendship_status().equals(Friendship.Friendship_status.IN_BLACKLIST));
			userMap.put("friendship_id", Encrypt.encrypt3DES(friendship.getId()+"", Constants.ENCRYPTION_KEY));
			list.add(userMap);
			
		}
		return list;
	}

	/**
	 * 得到会员间信息
	 * @param member
	 * @param member
     * @return
     */
	public static  Map<String,Object> convertMember(Member member,Member owner){

		Map<String,Object> memberMap = null;
		Friendship f = null;

		memberMap = UserWrapper.convertMemberInfo(member);
		if(!member.equals(owner)) {

			f = getFriendship(member, owner.getFriendships());
			if (f != null && !f.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE)) {
				memberMap.put("alias", f.getAlias());
				memberMap.put("is_stranger", false);
				memberMap.put("in_blacklist", f.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST));

			}
		}
		else{
			memberMap.put("alias",member.getUsername());
			memberMap.put("is_stranger",false);
			memberMap.put("is_blacklist",false);
		}
		return memberMap;


	}
	
	
	/**
	 * 得到好友关系
	 * @param member 用户
	 * @param friendships 另一个用户的好友关系
	 * @return
	 */
	public static  Friendship getFriendship(Member member, Set<Friendship> friendships) {
	/*	Boolean flag=true;
		Friendship friendship = null;
		Set<Friendship> memberFriends = null;
		for(Friendship f:friendships){
			if(f.getFriend().equals(member) && !f.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE)){
				flag=false;
				//查看是否被别人拉黑
				memberFriends = member.getFriendships();
				for(Friendship mf : memberFriends){
					if(mf.getFriend().equals(f.getOwner()) && !mf.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE)){
						friendship = f;
						break;
					}
				}


			}
		}*/
		Friendship friendship = null;
		for(Friendship f: friendships){
			if(f.getFriend().equals(member)){
				friendship = f;
				break;
			}
		}
		return friendship;
	}

	public  static Friendship getNormalFriendship(Member member ,Set<Friendship> friendships){
		Friendship friendship = null;
		for(Friendship f :friendships){
			if(f.getFriend().equals(member) && f.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_NORMAL)){
				friendship = f;
				break;
			}
		}
		return friendship;
	}
	
	
	
	/**
	 * 检查是否是黑名单用户
	 * @param member 要查询的用户
	 * @param friendships 另一个用户的好友关系
	 * @return
	 */
	public static  Boolean isInBlackList(Member member, Set<Friendship> friendships) {
		Boolean flag=false;
		
		for(Friendship f:friendships){
			if(f.getFriend().equals(member) && f.getFriendship_status().equals(Friendship.Friendship_status.IN_BLACKLIST)){
				flag=true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 检查是否是黑名单用户
	 * @param member 要查询的用户
	 * @param friendships 另一个用户的好友关系
	 * @return
	 */
	public static  Boolean isStranger(Member member, Set<Friendship> friendships) {
		Boolean flag=false;
		Friendship friendship = null;
		for(Friendship f:friendships){
			if(f.getFriend().equals(member) ){
				friendship = f;

				break;
			}
		}
		if(friendship==null || friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE)){
			flag= true;
		}
		return flag;
	}
	
	
	/**
	 * 得到好友间的关系
	 * @param friend
	 * @param friendships
	 * @return
	 *//*
	public static Friendship getFriendship(Member friend,List<Friendship> friendships){
		Friendship friendship=null;
		for(Friendship f: friendships){
			if(f.getFriend().equals(friend)){
				friendship=f;
				break;
			}
		}
		return friendship;
	}
	
	*//**
	 * 查看好友关系是否正常
	 * @param friend 待检验的用户
	 * @param friendships 用户正常的好友关系list
	 * @return
	 *//*
	public static Boolean isInNormal(Member friend,List<Friendship> friendships){
		Boolean isNormal=false;
		for(Friendship f:friendships){
			if(f.getFriend().equals(friend)){
				isNormal=true;
				break;
			}
		}
		return isNormal;
	}*/

}
