package com.dgut.app.helper;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.member.entity.base.BaseFriendship;

import java.util.*;


public class TalkWrapper {

	/**
	 * 说说信息格式化
	 * 
	 * @param talks
	 * @return
	 */
	public static List<Map<String, Object>> covertTalkInfo(List<Talk> talks,
			Member user) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> talkMap, userMap;
		Member member = null;
		Friendship friendship = null;

		for (Talk talk : talks) {
			talkMap = new HashMap<String, Object>();
			talkMap.put("talk_id", Encrypt.encrypt3DES(talk.getId()+"", Constants.ENCRYPTION_KEY));
			talkMap.put("photos", talk.getPhoto_urls());
			talkMap.put("video", talk.getVideo_url());
			talkMap.put("content", talk.getContent());
			talkMap.put("publish_time", talk.getPublish_time().getTime());

			/*resultMap = new HashMap<String, Object>();
			resultMap.put("talk", talkMap);*/

			member = talk.getPublisher();
//			userMap = new HashMap<String, Object>();
			userMap = UserWrapper.convertMemberInfo(member);
			userMap.put("icon", member.getIcon());
			//
			if(user.equals(member)){
				userMap.put("alias", member.getUsername());
				talkMap.put("user", userMap);
			}
			else{
				 friendship = FriendshipWrapper.getNormalFriendship(member,user.getFriendships());
				if(friendship!=null ){

					userMap.put("alias", friendship.getAlias());
					talkMap.put("user", userMap);
				}
				else{
					continue;
				}
			}


			
			userMap.put("userid", Encrypt.encrypt3DES(member.getId()+"", Constants.ENCRYPTION_KEY));

			
			talkMap.put("talkLike_list", TalkLikeWrapper.convertTalkLike(talk.getTalkLikes(),user));
			talkMap.put("comment_list", TalkCommentWrapper.convertTalkCommentList(talk.getTalkComments(),user));
			
			list.add(talkMap);

		}
		return list;
	}

}
