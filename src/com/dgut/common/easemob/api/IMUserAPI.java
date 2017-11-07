package com.dgut.common.easemob.api;

/**
 * Created by PUNK on 2017/1/22.
 */
public interface IMUserAPI {

    /**
     * 注册IM用户[单个] <br>
     * POST
     *
     * @param payload
     *            <code>{"username":"${用户名}","password":"${密码}", "nickname":"${昵称值}"}</code>
     * @return
     */
    Object createNewIMUserSingle(Object payload);

    /**
     * 给IM用户的添加好友 <br>
     * POST
     *
     * @param username
     *            用戶名或用戶ID
     * @param friendName
     *            好友用戶名或用戶ID
     * @return
     */
    Object addFriendSingle(String username, String friendName);


    /**
     * 查看某个IM用户的好友信息 <br>
     * GET
     *
     * @param userName
     *            用戶名或用戶ID
     * @return
     */
    Object getFriends(String userName);

    /**
     * 往IM用户的黑名单中加人 <br>
     * POST
     *
     * @param userName
     *            用戶名或用戶ID
     * @param payload
     *            <code>{"usernames":["5cxhactgdj", "mh2kbjyop1"]}</code>
     * @return
     */
    Object addToBlackList(String userName, Object payload);

    /**
     * 从IM用户的黑名单中减人 <br>
     * DELETE
     *
     * @param userName
     *            用戶名或用戶ID
     * @param blackListName
     *            黑名单用戶名或用戶ID
     * @return
     */
    Object removeFromBlackList(String userName, String blackListName);

    /**
     * 解除IM用户的好友关系 <br>
     * DELETE
     *
     * @param userName
     *            用戶名或用戶ID
     * @param friendName
     *            好友用戶名或用戶ID
     * @return
     */
    Object deleteFriendSingle(String userName, String friendName);

    /**
     * 获得用户参加的群组 <br>
     * GET
     *
     * @param userId
     *            用户ID或用户名，数组形式
     * @return
     */
    Object getUserJoinedGroups(String userId);




}
