package com.dgut.main.member.entity.base;

import java.io.Serializable;

/**
 * city base 类
 * Created by PUNK on 2017/1/22.
 */
public class BaseCity implements Serializable {
    private int hashCode = Integer.MIN_VALUE;

    //primary key
    private Integer id; // 编号
    private String city_name; //城市名
    private Integer priority;  //优先级

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BaseCity(Integer id, String city_name, Integer priority) {
        this.id = id;
        this.city_name = city_name;
        this.priority = priority;
    }

    public BaseCity() {
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }


}
