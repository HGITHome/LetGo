package com.dgut.main.entity;

import com.dgut.main.entity.base.BaseAccusation;

/**
 * Created by PUNK on 2017/3/21.
 */
public class AccusationType extends BaseAccusation{

    private AccusationCategory category;

    public AccusationCategory getCategory() {
        return category;
    }

    public void setCategory(AccusationCategory category) {
        this.category = category;
    }
}
