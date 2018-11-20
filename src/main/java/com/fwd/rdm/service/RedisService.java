package com.fwd.rdm.service;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;

import java.util.List;
import java.util.Map;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 11:02 2018/11/10
 */
public interface RedisService {

    /**
     * 查询数据库列表
     */
    Map<Integer, Integer> getDatabase(ConnectionProperties connectionProperties);

    /**
     * 查询所有key列表
     */
    List<String> getKeys(ConnectionProperties connectionProperties, String pattern);

    /**
     * 根据key查询数据
     */
    RedisData getRedisDataByKey(ConnectionProperties connectionProperties, String key);

    /**
     * 保存
     */
    boolean set(ConnectionProperties connectionProperties, String key, String value);

    /**
     * 保存hash数据
     */
    boolean hset(ConnectionProperties connectionProperties, String key, String field, String value);

    /**
     * 删除
     */
    long delete(ConnectionProperties connectionProperties, String pattern);

    /**
     * 删除key
     */
    long deleteKey(ConnectionProperties connectionProperties, String key);

    /**
     * Hash删除域值
     */
    long hdelete(ConnectionProperties connectionProperties, String key, String field);
}
