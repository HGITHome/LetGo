package com.dgut.common.easemob.api.impl;

import com.dgut.common.easemob.api.EasemobRestAPI;
import com.dgut.common.easemob.api.IMUserAPI;
import com.dgut.common.easemob.comm.constant.HTTPMethod;
import com.dgut.common.easemob.comm.helper.HeaderHelper;
import com.dgut.common.easemob.comm.wrapper.BodyWrapper;
import com.dgut.common.easemob.comm.wrapper.HeaderWrapper;

/**
 * Created by PUNK on 2017/1/22.
 */
public class EasemobIMUsers extends EasemobRestAPI implements IMUserAPI {

    private static final String ROOT_URI = "/users";

    @Override
    public String getResourceRootURI() {
        return ROOT_URI;
    }

    @Override
    public Object createNewIMUserSingle(Object payload) {
        String url = getContext().getSeriveURL() + getResourceRootURI();
        BodyWrapper body = (BodyWrapper) payload;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null,true);
    }

    @Override
    public Object addFriendSingle(String username, String friendName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + username + "/contacts/users/" + friendName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, null, null,false);
    }

    @Override
    public Object addToBlackList(String userName, Object payload) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/blocks/users";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
        BodyWrapper body = (BodyWrapper) payload;

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null,true);
    }

    @Override
    public Object getFriends(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/contacts/users";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null, null,false);
    }

    @Override
    public Object removeFromBlackList(String userName, String blackListName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/blocks/users/" + blackListName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, null, null,false);
    }

    @Override
    public Object deleteFriendSingle(String userName, String friendName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/contacts/users/" + friendName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, null, null,false);
    }

    @Override
    public Object getUserJoinedGroups(String userId) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userId + "/joined_chatgroups";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null, null,false);
    }


}
