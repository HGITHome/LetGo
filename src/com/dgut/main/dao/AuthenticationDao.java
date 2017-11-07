package com.dgut.main.dao;

import com.dgut.main.entity.base.BaseAuthentication;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/15.
 */
public interface AuthenticationDao  {

    public BaseAuthentication findById(String id);


    int deleteExpire(Date date);

    BaseAuthentication deleteById(String id);

    BaseAuthentication save(BaseAuthentication bean);
}
