package com.fwd.rdm.data.domain;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 18:39 2018/11/19
 */
public class HashData {

    private Long index;
    private String field;
    private String value;

    public HashData() {
    }

    public HashData(Long index, String field, String value) {
        this.index = index;
        this.field = field;
        this.value = value;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
