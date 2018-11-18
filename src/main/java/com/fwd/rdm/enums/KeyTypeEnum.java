package com.fwd.rdm.enums;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 18:59 2018/11/13
 */
public enum KeyTypeEnum {
    // key不存在
    NONE("none"),
    // 字符串
    STRING("string"),
    // 列表
    LIST("list"),
    // 集合
    SET("set"),
    // 有序集合
    ZSET("zset"),
    // 哈希表
    HASH("hash"),
    // 其他
    OTHOR("othor"),
    ;
    private String type;

    KeyTypeEnum(String type) {
        this.type = type;
    }

    public static KeyTypeEnum typeOf(String type) {
        KeyTypeEnum[] values = KeyTypeEnum.values();
        for (KeyTypeEnum value : values) {
            if (value.type.equalsIgnoreCase(type)) {
                return value;
            }
        }
        return OTHOR;
    }

    public String getType() {
        return this.type;
    }
}
