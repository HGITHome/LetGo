package com.dgut.common.easemob.comm.helper;

import com.dgut.common.easemob.comm.ClientContext;
import com.dgut.common.easemob.comm.wrapper.HeaderWrapper;

/**
 * Created by PUNK on 2017/1/22.
 */
public class HeaderHelper {

    private static ClientContext context = ClientContext.getInstance();

    public static HeaderWrapper getDefaultHeader() {
        return HeaderWrapper.newInstance().addJsonContentHeader();
    }

    public static HeaderWrapper getDefaultHeaderWithToken() {
        return getDefaultHeader().addAuthorization(context.getAuthToken());
    }



}
