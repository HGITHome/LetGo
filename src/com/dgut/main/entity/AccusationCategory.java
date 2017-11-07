package com.dgut.main.entity;

import com.dgut.main.entity.base.BaseAccusation;

import java.util.List;

/**
 * Created by PUNK on 2017/3/21.
 */
public class AccusationCategory extends BaseAccusation {

    private List<AccusationType> types;

    public List<AccusationType> getTypes() {
        return types;
    }

    public void setTypes(List<AccusationType> types) {
        this.types = types;
    }
}
