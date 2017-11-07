package com.dgut.main.util;

import java.util.List;

/**
 * Created by PUNK on 2017/5/7.
 */
public class InfoConvertor {

    public static List<Object[]> convertSexInfo( List<Object[]> list){
        for(Object[] objs :list){
            if(objs[0]!=null && String.valueOf(objs[0]).equals("false")){
                objs[0] ='男';
            }
            else if(objs[0]!=null && String.valueOf(objs[0]).equals("true")){
                objs[0] ='女';
            }
        }
        return list;
    }
}
