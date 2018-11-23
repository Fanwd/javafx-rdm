package com.fwd.rdm.service.impl;

import com.fwd.rdm.dao.RedisDao;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.LoggerUtils;
import io.lettuce.core.ScoredValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 11:02 2018/11/10
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final String LIST_REM_PLACEHOLDER = "***---///LIST_REMOVED_PLACEHOLDER_BY_REM///---***";

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private LoggerUtils loggerUtils;

    @Override
    public Map<Integer, Integer> getDatabase(ConnectionProperties connectionProperties) {
        return redisDao.getAllDb(connectionProperties);
    }

    @Override
    public List<String> getKeys(ConnectionProperties connectionProperties, String pattern) {
        return redisDao.keys(connectionProperties, pattern)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public RedisData getRedisDataByKey(ConnectionProperties connectionProperties, String key) {
        // 查询key类型
        String type = redisDao.type(connectionProperties, key);
        KeyTypeEnum keyTypeEnum = KeyTypeEnum.typeOf(type);
        Long ttl = redisDao.ttl(connectionProperties, key);
        if (KeyTypeEnum.STRING.equals(keyTypeEnum)) {
            String value = redisDao.get(connectionProperties, key);
            return new RedisData(key, type, ttl, value);
        } else if (KeyTypeEnum.HASH.equals(keyTypeEnum)) {
            Map<String, String> hashValue = redisDao.hgetAll(connectionProperties, key);
            return new RedisData(key, type, ttl, hashValue);
        } else if (KeyTypeEnum.LIST.equals(keyTypeEnum)) {
            List<String> listData = redisDao.lrange(connectionProperties, key, 0, -1);
            return new RedisData(key, type, ttl, null, listData);
        } else if (KeyTypeEnum.SET.equals(keyTypeEnum)) {
            Set<String> setData = redisDao.smembers(connectionProperties, key);
            return new RedisData(key, type, ttl, setData);
        } else if (KeyTypeEnum.ZSET.equals(keyTypeEnum)) {
            List<ScoredValue<String>> zsetData = redisDao.zrange(connectionProperties, key, 0, -1);
            return new RedisData(key, type, ttl, zsetData);
        } else {
            return new RedisData(key, type, ttl);
        }

    }

    @Override
    public boolean exists(ConnectionProperties connectionProperties, String key) {
        return redisDao.exists(connectionProperties, key);
    }

    @Override
    public boolean expire(ConnectionProperties connectionProperties, String key, long seconeds) {
        return redisDao.expire(connectionProperties, key, seconeds);
    }

    @Override
    public boolean persist(ConnectionProperties connectionProperties, String key) {
        return redisDao.persist(connectionProperties, key);
    }

    @Override
    public boolean set(ConnectionProperties connectionProperties, String key, String value) {
        return redisDao.set(connectionProperties, key, value);
    }

    @Override
    public boolean hset(ConnectionProperties connectionProperties, String key, String field, String value) {
        return redisDao.hset(connectionProperties, key, field, value);
    }

    @Override
    public long delete(ConnectionProperties connectionProperties, String pattern) {
        List<String> keysList = redisDao.keys(connectionProperties, pattern);
        if (!keysList.isEmpty()) {
            String[] keyArray = keysList.toArray(new String[0]);
            return redisDao.delete(connectionProperties, keyArray);
        }
        return 0;
    }

    @Override
    public long deleteKey(ConnectionProperties connectionProperties, String key) {
        return redisDao.delete(connectionProperties, key);
    }

    @Override
    public long hdelete(ConnectionProperties connectionProperties, String key, String field) {
        return redisDao.hdelete(connectionProperties, key, field);
    }

    // TODO add by fanwd at 2018/11/21-16:17 目前通过cas对list进行操作(由于未加锁小概率情况下会误更新)，但仍会存在bab的问题，可考虑对数据进行全量校验(会降低性能)

    @Override
    public long ladd(ConnectionProperties connectionProperties, String key, String value) {
        return redisDao.lpush(connectionProperties, key, value);
    }

    @Override
    public boolean lmodify(ConnectionProperties connectionProperties, String key, String oldValue, String newValue, long index) {
        String dbValue = redisDao.lindex(connectionProperties, key, index);
        if (!oldValue.equalsIgnoreCase(dbValue)) {
            // redis中的值已变更
            loggerUtils.alertWarn("The data has been modified, Please refresh and retry!!");
            return false;
        }
        redisDao.lset(connectionProperties, key, newValue, index);
        return true;
    }

    @Override
    public boolean ldel(ConnectionProperties connectionProperties, String key, String value, long index) {
        // 查询redis中的值
        String dbValue = redisDao.lindex(connectionProperties, key, index);
        if (!dbValue.equalsIgnoreCase(value)) {
            // redis中的值已变更，请刷新后重试
            loggerUtils.alertWarn("The data has been modified, Please refresh and retry!!");
            return false;
        }
        // TODO add by fanwd at 2018/11/21-16:24 当list中存在与 LIST_REM_PLACEHOLDER 相同的数据时会误删数据，可以通过修改placeholder的特殊性降低概率
        // 将删除的数据替换为placeholder
        redisDao.lset(connectionProperties, key, LIST_REM_PLACEHOLDER, index);
        // 删除list中所有被替换为删除占位符的数据
        redisDao.lrem(connectionProperties, key, LIST_REM_PLACEHOLDER);
        return true;
    }

    @Override
    public Set<String> sget(ConnectionProperties connectionProperties, String key) {
        return redisDao.smembers(connectionProperties, key);
    }

    @Override
    public long sadd(ConnectionProperties connectionProperties, String key, String value) {
        return redisDao.sadd(connectionProperties, key, value);
    }

    @Override
    public long sdel(ConnectionProperties connectionProperties, String key, String val) {
        return redisDao.srem(connectionProperties, key, val);
    }

    @Override
    public long smodify(ConnectionProperties connectionProperties, String key, String oldValue, String newValue) {
        // 删除旧数据
        redisDao.srem(connectionProperties, key, oldValue);
        // 添加新数据
        return redisDao.sadd(connectionProperties, key, newValue);
    }

    @Override
    public long zadd(ConnectionProperties connectionProperties, String key, double score, String value) {
        return redisDao.zadd(connectionProperties, key, score, value);
    }

    @Override
    public long zdel(ConnectionProperties connectionProperties, String key, String value) {
        return redisDao.zrem(connectionProperties, key, value);
    }

    @Override
    public long zmodify(ConnectionProperties connectionProperties, String key, String oldValue, double oldScore, String newValue) {
        // 删除数据
        redisDao.zrem(connectionProperties, key, oldValue);
        return redisDao.zadd(connectionProperties, key, oldScore, newValue);
    }

}
