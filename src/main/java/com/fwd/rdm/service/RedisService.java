package com.fwd.rdm.service;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 查询key信息
     */
    public RedisData getRedisKeyInfo(ConnectionProperties connectionProperties, String key);

    /**
     * 根据key查询数据
     */
    RedisData getRedisDataByKey(ConnectionProperties connectionProperties, String key);

    /**
     * 查询key是否存在
     * @return 存在返回true；不存在返回false
     */
    boolean exists(ConnectionProperties connectionProperties, String key);

    /**
     * 设置key过期时间
     */
    boolean expire(ConnectionProperties connectionProperties, String key, long seconeds);

    /**
     * 将Key设置为持久的（永不过期）
     * @return 设置成功返回1，key不存在或者key已经是持久的返回0
     */
    boolean persist(ConnectionProperties connectionProperties, String key);

    /**
     * 保存
     */
    boolean set(ConnectionProperties connectionProperties, String key, String value);

    /**
     * 删除
     */
    long delete(ConnectionProperties connectionProperties, String pattern);

    /**
     * 删除key
     */
    long deleteKey(ConnectionProperties connectionProperties, String key);

    /**
     * 保存hash数据
     */
    boolean hset(ConnectionProperties connectionProperties, String key, String field, String value);

    /**
     * Hash删除域值
     */
    long hdelete(ConnectionProperties connectionProperties, String key, String field);

    /**
     * 添加list数据
     */
    long ladd(ConnectionProperties connectionProperties, String key, String value);

    /**
     * 修改
     */
    boolean lmodify(ConnectionProperties connectionProperties, String key, String oldValue, String newValue, long index);

    /**
     * 删除数据
     */
    boolean ldel(ConnectionProperties connectionProperties, String key, String value, long index);

    /**
     * set查询数据
     */
    Set<String> sget(ConnectionProperties connectionProperties, String key);

    /**
     * set添加数据
     */
    long sadd(ConnectionProperties connectionProperties, String key, String value);

    /**
     * set删除数据
     */
    long sdel(ConnectionProperties connectionProperties, String key, String val);

    /**
     * set修改数据
     */
    long smodify(ConnectionProperties connectionProperties, String key, String oldValue, String newValue);

    /**
     * zset添加数据
     */
    long zadd(ConnectionProperties connectionProperties, String key, double score, String value);

    /**
     * zset删除数据
     */
    long zdel(ConnectionProperties connectionProperties, String key, String value);

    /**
     * zset修改数据
     */
    long zmodify(ConnectionProperties connectionProperties, String key, String oldValue, double oldScore, String newValue);
}
