package com.fwd.rdm.service.impl;

import com.fwd.rdm.dao.RedisDao;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 11:02 2018/11/10
 */
@Service
public class RedisServiceImpl implements RedisService {


    @Autowired
    private RedisDao redisDao;

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
        if (KeyTypeEnum.STRING.equals(keyTypeEnum)) {
            Long ttl = redisDao.ttl(connectionProperties, key);
            String value = redisDao.get(connectionProperties, key);
            return new RedisData(key, type, ttl, value, null);
        } else if (KeyTypeEnum.HASH.equals(keyTypeEnum)) {
            Long ttl = redisDao.ttl(connectionProperties, key);
            Map<String, String> hashValue = redisDao.hgetAll(connectionProperties, key);
            return new RedisData(key, type, ttl, null, hashValue);
        } else {
            return new RedisData(key, type, null, null, null);
        }

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

}
