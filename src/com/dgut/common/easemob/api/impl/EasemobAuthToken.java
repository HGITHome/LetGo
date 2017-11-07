package com.dgut.common.easemob.api.impl;

import com.dgut.common.easemob.api.AuthTokenAPI;
import com.dgut.common.easemob.api.EasemobRestAPI;
import com.dgut.common.easemob.comm.body.AuthTokenBody;
import com.dgut.common.easemob.comm.constant.HTTPMethod;
import com.dgut.common.easemob.comm.helper.HeaderHelper;
import com.dgut.common.easemob.comm.wrapper.BodyWrapper;
import com.dgut.common.easemob.comm.wrapper.HeaderWrapper;

/**
 * Created by PUNK on 2017/1/22.
 */
public class EasemobAuthToken extends EasemobRestAPI implements AuthTokenAPI {

    public static final String ROOT_URI = "/token";


    @Override
    public Object getAuthToken(String clientId, String clientSecret) {
        String url = getContext().getSeriveURL() + getResourceRootURI();
        BodyWrapper body = new AuthTokenBody(clientId, clientSecret);
        HeaderWrapper header = HeaderHelper.getDefaultHeader();

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null,true);
    }

    @Override
    public String getResourceRootURI() {
        return ROOT_URI;
    }
}
