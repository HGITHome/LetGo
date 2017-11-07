package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseCity;
import com.dgut.main.member.entity.base.BaseProvince;

import java.util.Set;

/**
 * Created by PUNK on 2017/1/22.
 */
public class Province extends BaseProvince {



    public Province(Integer id, String province_name, Integer priority) {
        super(id, province_name, priority);
    }

    public Province() {
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Province)) return false;
        else {
            Province province = (Province) obj;
            if (null == this.getId() || null == province.getId()) return false;
            else return (this.getId().equals(province.getId()));
        }
    }
}
