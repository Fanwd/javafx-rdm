package com.fwd.rdm.data.domain;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:40 2018/11/12
 */
public class RedisData {

    private String key;
    private String type;
    private Long ttl;
    private String value;

    public RedisData() {
    }

    public RedisData(String key, String type, Long ttl, String value) {
        this.key = key;
        this.type = type;
        this.ttl = ttl;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
