package com.fwd.rdm.data.domain;

import io.lettuce.core.ScoredValue;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Map<String, String> hashData;
    private List<String> listData;
    private Set<String> setData;
    private List<ScoredValue<String>> zsetData;

    public RedisData() {
    }

    public RedisData(String key, String type, Long ttl) {
        this.key = key;
        this.type = type;
        this.ttl = ttl;
    }

    public RedisData(String key, String type, Long ttl, String value) {
        this(key, type, ttl, value, null, null, null, null);
    }

    public RedisData(String key, String type, Long ttl, Map<String, String> hashData) {
        this(key, type, ttl, null, hashData, null, null, null);
    }


    public RedisData(String key, String type, Long ttl, String value, List<String> listData) {
        this(key, type, ttl, value, null, listData, null, null);
    }

    public RedisData(String key, String type, Long ttl, Set<String> setData) {
        this(key, type, ttl, null, null, null, setData, null);
    }

    public RedisData(String key, String type, Long ttl, List<ScoredValue<String>> zsetData) {
        this(key, type, ttl, null, null, null, null, zsetData);
    }

    public RedisData(String key, String type, Long ttl, String value, Map<String, String> hashData, List<String> listData, Set<String> setData, List<ScoredValue<String>> zsetData) {
        this.key = key;
        this.type = type;
        this.ttl = ttl;
        this.value = value;
        this.hashData = hashData;
        this.listData = listData;
        this.setData = setData;
        this.zsetData = zsetData;
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

    public Map<String, String> getHashData() {
        return hashData;
    }

    public void setHashData(Map<String, String> hashData) {
        this.hashData = hashData;
    }

    public List<String> getListData() {
        return listData;
    }

    public void setListData(List<String> listData) {
        this.listData = listData;
    }

    public Set<String> getSetData() {
        return setData;
    }

    public void setSetData(Set<String> setData) {
        this.setData = setData;
    }

    public List<ScoredValue<String>> getZsetData() {
        return zsetData;
    }

    public void setZsetData(List<ScoredValue<String>> zsetData) {
        this.zsetData = zsetData;
    }
}
