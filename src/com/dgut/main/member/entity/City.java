package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseCity;




/**
 * Created by PUNK on 2017/1/22.
 */
public class City extends BaseCity {

    //relations

    private Province province;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City(Integer id, String city_name, Integer priority) {
        super(id, city_name, priority);
    }

    public City() {
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof City)) return false;
        else {
            City city = (City) obj;
            if (null == this.getId() || null == city.getId()) return false;
            else {
                return (this.getId().equals(city.getId()));
            }
        }
    }
}
