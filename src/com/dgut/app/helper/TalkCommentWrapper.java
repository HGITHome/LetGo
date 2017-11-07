package com.dgut.app.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.TalkComment;
import com.dgut.main.member.entity.base.BaseFriendship;

/**
 * 朋友圈评论处理工具类
 * @author zw
 *
 */
public class TalkCommentWrapper {

	public static List<Map<String,Object>> convertTalkCommentList(List<TalkComment> talkComments,Member user){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> talkCommentMap=null,userMap = null,receiverMap = null;
		Member member = null,receiver = null;
		Friendship friendship = null;
		for(TalkComment talkComment :talkComments){
			if(!talkComment.getDisabled()){
				member = talkComment.getPublisher();
				receiver = talkComment.getReceiver();
				/*userMap = new HashMap<>();
				userMap.put("userid",Encrypt.encrypt3DES(member.getId()+"",Constants.ENCRYPTION_KEY));
*/

				talkCommentMap=new HashMap<String,Object>();
				receiverMap = new HashMap<>();

				userMap = UserWrapper.convertMemberInfo(member);
				// 本人查看
				if(user.equals(member)){
					userMap.put("alias",member.getUsername());
					talkCommentMap.put("user", userMap);
				}
				//查看其他好友
				else{
					//拉黑状态不显示评论
					friendship = FriendshipWrapper.getFriendship(member,user.getFriendships());
					if(friendship!=null && !friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST)){
						userMap.put("alias",friendship.getAlias());
						talkCommentMap.put("user", userMap);
					}
					else{
						continue;
					}
				}



//				receiverMap = new HashMap<>();
				if(receiver!=null) {
					/*receiverMap.put("userid", Encrypt.encrypt3DES(receiver.getId() + "", Constants.ENCRYPTION_KEY));*/
					receiverMap = UserWrapper.convertMemberInfo(receiver);
					if (user.equals(receiver)) {
						receiverMap.put("alias", receiver.getUsername());
						talkCommentMap.put("receiver", receiverMap);
					}
					else {
						friendship = FriendshipWrapper.getFriendship(receiver, user.getFriendships());
						if (friendship != null && !friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST)) {
							receiverMap.put("alias", friendship.getAlias());
							talkCommentMap.put("receiver", receiverMap);
						} else {
							continue;
						}
					}

//					talkCommentMap.put("receiver", receiverMap);
				}
				else{
					talkCommentMap.put("receiver", null);
				}



				talkCommentMap.put("message_time", talkComment.getMessage_time().getTime());


				talkCommentMap.put("content", talkComment.getContent());
				talkCommentMap.put("comment_id", Encrypt.encrypt3DES(talkComment.getId()+"", Constants.ENCRYPTION_KEY));
//				talkCommentMap.put("receiver", receiverMap);
				list.add(talkCommentMap);

				/*userMap = UserWrapper.convertMemberInfo(talkComment.getPublisher());

				Friendship friendship = FriendshipWrapper.getFriendship(talkComment.getPublisher(), member.getFriendships());
				if(friendship!=null){
					userMap.put("alias", friendship.getAlias());

					talkCommentMap=new HashMap<String,Object>();
					talkCommentMap.put("message_time", talkComment.getMessage_time().getTime());
					talkCommentMap.put("user", userMap);

					talkCommentMap.put("content", talkComment.getContent());
					talkCommentMap.put("comment_id", Encrypt.encrypt3DES(talkComment.getId()+"", Constants.ENCRYPTION_KEY));
					talkCommentMap.put("receiver", UserWrapper.convertMemberInfo(talkComment.getReceiver()));
					list.add(talkCommentMap);
				}*/
			}
		}

		return list;
	}

}
