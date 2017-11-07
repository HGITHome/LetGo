package com.dgut.common.easemob.api;

import com.dgut.common.easemob.comm.wrapper.BodyWrapper;
import com.dgut.common.easemob.comm.wrapper.HeaderWrapper;
import com.dgut.common.easemob.comm.wrapper.QueryWrapper;
import com.dgut.common.easemob.comm.wrapper.ResponseWrapper;

/**
 * Created by PUNK on 2017/1/22.
 */
public interface RestAPIInvoker {

    ResponseWrapper sendRequest(String method, String url, HeaderWrapper header, BodyWrapper body, QueryWrapper query, boolean bodyNeed);
    //	ResponseWrapper uploadFile(String url, HeaderWrapper header, File file);
//    ResponseWrapper downloadFile(String url, HeaderWrapper header, QueryWrapper query);
}
