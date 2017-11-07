package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.City;


import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by PUNK on 2017/1/22.
 */
public class BaseProvince implements Serializable {

    //primary key
    private Integer id; // 编号
    //field
    private String province_name; //省份名
    private Integer priority; //优先级

    //collection
    @Transient
    private Set<City> cities;

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public BaseProvince(Integer id, String province_name, Integer priority) {
        this.id = id;
        this.province_name = province_name;
        this.priority = priority;
    }

    private int hashCode = Integer.MIN_VALUE;

    public BaseProvince() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
