package com.fwd.rdm.enums;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 10:34 2018/11/21
 */
public enum ViewTypeEnum {

    TEXT("text"),
    JSON("json");

    private String type;

    ViewTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static boolean isJson(String type) {
        return JSON.getType().equalsIgnoreCase(type);
    }

    public static boolean isText(String type) {
        return TEXT.getType().equalsIgnoreCase(type);
    }
}
