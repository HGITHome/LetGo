package com.dgut.common.easemob.api;

/**
 * Created by PUNK on 2017/1/22.
 */
public interface AuthTokenAPI {
    Object getAuthToken(String clientId, String clientSecret);
}
