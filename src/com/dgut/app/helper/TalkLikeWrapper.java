package com.dgut.app.helper;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.member.entity.TalkLike;
import com.dgut.main.member.entity.base.BaseFriendship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TalkLikeWrapper {

	/**
	 * 检查是否在点赞列表中
	 * @param member
	 * @param talkLikeList
	 * @return
	 */
	public static TalkLike isInTalkLikeList(Member member, List<TalkLike> talkLikeList){
//		Boolean isExisted=false;
		TalkLike t = null;
		for(TalkLike talkLike:talkLikeList){
			if(talkLike.getPublisher().equals(member) ){
//				isExisted=true;
				t = talkLike;
				break;
			}
		}
		return t;
	}


	public static TalkLike getTalkLike(Member member,List<TalkLike> talkLikeList){
		TalkLike t = null;
		for(TalkLike talkLike:talkLikeList){
			if(talkLike.getPublisher().equals(member)){
				t = talkLike;
				break;
			}
		}
		return t;
	}


	/**
	 * 点赞信息格式化
	 * @param talkLikeList
	 * @return
	 */
	public static List<Map<String,Object>> convertTalkLike(List<TalkLike> talkLikeList,Member user){
		List<Map<String,Object>> list=new ArrayList< Map <String, Object>>();

		Map<String,Object>  talkLikeMap = null,userMap =null;
		Member member = null;
		Friendship friendship = null;

		for(TalkLike talkLike: talkLikeList){
			if(!talkLike.getDisabled()){


				/*userMap = UserWrapper.convertMemberInfo(talkLike.getPublisher());
				Friendship friendship = FriendshipWrapper.getFriendship(talkLike.getPublisher(), publisher.getFriendships());
				if(friendship!=null){
					userMap.put("alias", friendship.getAlias());


					talkLikeMap = new HashMap<String ,Object>();
					talkLikeMap.put("tap_time", talkLike.getMessage_time().getTime());
					talkLikeMap.put("tap_id", Encrypt.encrypt3DES(talkLike.getId()+"", Constants.ENCRYPTION_KEY));
					talkLikeMap.put("user", userMap);

					list.add(talkLikeMap);
				}*/
				member = talkLike.getPublisher();
				talkLikeMap = new HashMap<String ,Object>();
//				userMap = new HashMap<>();
//				userMap.put("userid",Encrypt.encrypt3DES(member.getId()+"",Constants.ENCRYPTION_KEY));

				userMap = UserWrapper.convertMemberInfo(member);

				if(user.equals(member)){
					userMap.put("alias",member.getUsername());
					talkLikeMap.put("user", userMap);
				}
				else{
					friendship = FriendshipWrapper.getFriendship(member,user.getFriendships());
					if(friendship!=null && friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_NORMAL)){
						userMap.put("alias",member.getUsername());
						talkLikeMap.put("user", userMap);
					}
					else{
//						userMap.put("alias",friendship.getAlias());
						continue;
					}
				}

				talkLikeMap.put("tap_time", talkLike.getMessage_time().getTime());
				talkLikeMap.put("tap_id", Encrypt.encrypt3DES(talkLike.getId()+"", Constants.ENCRYPTION_KEY));


				list.add(talkLikeMap);
			}
		}

		return list;
	}

}
