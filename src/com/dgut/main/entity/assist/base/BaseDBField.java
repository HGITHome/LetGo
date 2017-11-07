package com.dgut.main.entity.assist.base;

import java.io.Serializable;

/**
 * 数据库表字段
 * Created by PUNK on 2017/1/20.
 */
public class BaseDBField implements Serializable {
    private int hashCode = Integer.MIN_VALUE;

    // fields
    private java.lang.String name;              //字段名
    private java.lang.String fieldType;         //字段类型
    private java.lang.String fieldDefault;      //字段默认值
    private java.lang.String fieldProperty;
    private java.lang.String comment;           //备注
    private java.lang.String nullable;          //空值
    private java.lang.String extra;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldDefault() {
        return fieldDefault;
    }

    public void setFieldDefault(String fieldDefault) {
        this.fieldDefault = fieldDefault;
    }

    public String getFieldProperty() {
        return fieldProperty;
    }

    public void setFieldProperty(String fieldProperty) {
        this.fieldProperty = fieldProperty;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public BaseDBField() {
    }

    public BaseDBField(String name, String fieldType, String fieldDefault, String fieldProperty, String comment, String nullable, String extra) {
        this.name = name;
        this.fieldType = fieldType;
        this.fieldDefault = fieldDefault;
        this.fieldProperty = fieldProperty;
        this.comment = comment;
        this.nullable = nullable;
        this.extra = extra;
    }
}
