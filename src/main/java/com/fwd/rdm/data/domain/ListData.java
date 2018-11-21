package com.fwd.rdm.data.domain;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:39 2018/11/21
 */
public class ListData {

    private Long index;
    private String value;

    public ListData() {
    }

    public ListData(Long index, String value) {
        this.index = index;
        this.value = value;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
