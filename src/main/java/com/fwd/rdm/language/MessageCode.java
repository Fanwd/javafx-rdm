package com.fwd.rdm.language;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 19:30 2018/11/28
 */
public enum MessageCode {

    // 新建连接
    NEW_CONNECTION("newConnection");

    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
