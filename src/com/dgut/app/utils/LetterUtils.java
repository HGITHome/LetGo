package com.dgut.app.utils;

/**
 * Created by PUNK on 2017/1/22.
 */
public class LetterUtils {

    private LetterUtils() throws Exception {
        throw new Exception("cannont create an instance");
    }

    /**
     * 生成四位随机字母
     * @return
     */
    public static String getRandomLetter(){
        char[] letters=new char[4];
        for(int i=0;i<4;i++){
            letters[i]=(char)(int)(Math.random()*26+97);
        }

        //		System.out.println(letters);
        return String.valueOf(letters);
    }


}
