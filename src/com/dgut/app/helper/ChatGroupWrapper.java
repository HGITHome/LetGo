package com.dgut.app.helper;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.GroupMember;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseFriendship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PUNK on 2017/3/15.
 */
public class ChatGroupWrapper {

    /**
     * 群组信息格式化
     * @param groups
     * @return
     */
    public static List<Map<String,Object>> convertChatGroup(List<ChatGroup> groups,Member user){
        List<Map<String,Object>> groupList  = new ArrayList<>();
        Map<String,Object> groupMap = null;
        Map<String,Object> userMap = null;
        List<GroupMember> gmList  = null;
        List<Map<String,Object>> memberMapList =null;
        Friendship friendship = null;
        if(groups.size()!=0){
            for(ChatGroup group : groups){
                groupMap = new HashMap<>();
                groupMap.put("group_id", Encrypt.encrypt3DES(group.getId().toString(), Constants.ENCRYPTION_KEY));
                groupMap.put("groupName",group.getGroupName());
                groupMap.put("desc",group.getDescription());
                groupMap.put("total",group.getCapacity());

                groupMap.put("owner",UserWrapper.convertMemberInfo(group.getOwner()));

                gmList = group.getMemberList();



                if(gmList.size()>0){
                    memberMapList = new ArrayList<>();
                    for(GroupMember gm : gmList){
                        if(gm.getDisabled()==false){
                            userMap =  UserWrapper.convertMemberInfo(gm.getMember());
                            if(gm.getMember().equals(user)){
                                userMap.put("alias",user.getUsername());
                                userMap.put("is_stranger",false);
                                userMap.put("is_blacklist",false);
                            }
                            else{
                                friendship = FriendshipWrapper.getFriendship(gm.getMember(),user.getFriendships());
                                if(friendship!=null && !friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE) ){
                                    userMap.put("alias",friendship.getAlias());
                                    userMap.put("is_stranger",false);
                                    userMap.put("in_blacklist",friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST));
                                }
                                else{
                                    continue;
                                }
                            }

                            memberMapList.add(userMap);
                        }
                    }
                }
                groupMap.put("memberList",memberMapList);
                groupMap.put("current", memberMapList.size());
                groupList.add(groupMap);

            }
        }
        return groupList;
    }

    public static  Boolean isInGroup(Member member,ChatGroup group){
        List<GroupMember> list = group.getMemberList();
        Boolean isExisted = false;
        for(GroupMember gm :list){
            if(gm.getMember().equals(member) && !gm.getDisabled()){
                isExisted = true;
                break;
            }
        }
        return isExisted;
    }
}
