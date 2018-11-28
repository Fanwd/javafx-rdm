package com.fwd.rdm.dao;

import com.fwd.rdm.data.domain.ConnectionProperties;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 11:09 2018/11/10
 */
@Component
public class RedisDao {

    private final static Map<Long, StatefulRedisConnection<String, String>> connectionPool = new ConcurrentHashMap<>();

    /**
     * 超时时间
     */
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    /**
     * 获取连接
     */
    public synchronized StatefulRedisConnection<String, String> createConnection(ConnectionProperties connectionProperties) {
        long id = connectionProperties.getId();
        StatefulRedisConnection<String, String> connection = connectionPool.get(id);
        if (null == connection) {
            // 连接不存在，新建连接
            RedisURI redisURI = new RedisURI(connectionProperties.getIp(), connectionProperties.getPort(), TIMEOUT);
            redisURI.setPassword(connectionProperties.getAuth());
            RedisClient redisClient = RedisClient.create(redisURI);
            connection = redisClient.connect();
            connectionPool.put(id, connection);
        } else {
            // 连接已存在，测试连接
            RedisCommands<String, String> redisCommands = connection.sync();
            String pong = redisCommands.ping();
            if (!RedisReturnCode.PONG.equalsIgnoreCase(pong)) {
                // 连接已失效，尝试关闭连接，重新创建连接
                try {
                    connection.close();
                } catch (Exception ex) {
                }
                RedisURI redisURI = new RedisURI(connectionProperties.getIp(), connectionProperties.getPort(), TIMEOUT);
                redisURI.setPassword(connectionProperties.getAuth());
                RedisClient redisClient = RedisClient.create(redisURI);
                connection = redisClient.connect();
                connectionPool.put(id, connection);
            }
        }
        return connection;
    }

    /**
     * 查询所有数据库
     */
    public Map<Integer, Integer> getAllDb(ConnectionProperties connectionProperties) {
        StatefulRedisConnection<String, String> connection = this.createConnection(connectionProperties);
        RedisCommands<String, String> redisCommands = connection.sync();
        int dbIndex = 0;
        Map<Integer, Integer> keyCountMap = new ConcurrentHashMap<>();
        while (true) {
            try {
                String retCode = redisCommands.select(dbIndex);
                if (RedisReturnCode.ERR_INVALID_DB_INDEX.equals(retCode)) {
                    break;
                }
            } catch (RedisCommandExecutionException ex) {
                break;
            }
            List<String> allKeys = redisCommands.keys("*");
            keyCountMap.put(dbIndex, allKeys.size());
            dbIndex++;
        }
        return keyCountMap;
    }

    /**
     * 查询所有key
     *
     * @param connectionProperties
     * @return
     */
    public List<String> keys(ConnectionProperties connectionProperties, String pattern) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.keys(pattern);
    }

    /**
     * 查询key类型
     */
    public String type(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.type(key);
    }

    /**
     * 查询key剩余时间
     */
    public long ttl(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.ttl(key);
    }

    /**
     * 检查key值是否存在
     */
    public boolean exists(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.exists(key) > 0;
    }

    /**
     * 设置key有效期
     */
    public boolean expire(ConnectionProperties connectionProperties, String key, long seconeds) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.expire(key, seconeds);
    }

    /**
     * 重命名（如果新的key不存在时，将key该为newKey）
     *
     * @return 修改成果返回1，newKey已存在或key不存在时返回0
     */
    public boolean renamenx(ConnectionProperties connectionProperties, String key, String newKey) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.renamenx(key, newKey);
    }

    /**
     * 设置key为持久的
     *
     * @return 设置成功返回1，key不存在或者key已经是持久的返回0
     */
    public boolean persist(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.persist(key);
    }

    /**
     * 查询key值
     */
    public String get(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.get(key);
    }

    /**
     * 获取所有hash数据
     */
    public Map<String, String> hgetAll(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.hgetall(key);
    }

    /**
     * 保存数据
     */
    public boolean set(ConnectionProperties connectionProperties, String key, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        String retCode = redisCommands.set(key, value);
        if (RedisReturnCode.OK.equalsIgnoreCase(retCode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除数据
     */
    public long delete(ConnectionProperties connectionProperties, String... keys) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.del(keys);
    }

    /**
     * 保存Hash数据
     *
     * @return 如果是新建域返回1，如果是修改域返回0
     */
    public boolean hset(ConnectionProperties connectionProperties, String key, String field, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.hset(key, field, value);
    }

    /**
     * 保存hash数据
     *
     * @return 域不存在时新建域返回1，如果域已存在返回0
     */
    public boolean hsetnx(ConnectionProperties connectionProperties, String key, String field, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.hsetnx(key, field, value);
    }

    /**
     * 删除Hash数据
     */
    public long hdelete(ConnectionProperties connectionProperties, String key, String field) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.hdel(key, field);
    }

    /**
     * 根据下标范围获取list数据
     */
    public List<String> lrange(ConnectionProperties connectionProperties, String key, long startPos, long stopPos) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.lrange(key, startPos, stopPos);
    }

    /**
     * 获取下标为index的数据
     */
    public String lindex(ConnectionProperties connectionProperties, String key, long index) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.lindex(key, index);
    }

    /**
     * 修改下标为index的数据
     */
    public boolean lset(ConnectionProperties connectionProperties, String key, String value, long index) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        String result = redisCommands.lset(key, index, value);
        return RedisReturnCode.OK.equalsIgnoreCase(result);
    }

    /**
     * 删除列表中值为value的所有元素
     */
    public long lrem(ConnectionProperties connectionProperties, String key, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.lrem(key, 0, value);
    }

    /**
     * list头部添加数据
     */
    public long lpush(ConnectionProperties connectionProperties, String key, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.lpush(key, value);
    }

    /**
     * set获取数据
     */
    public Set<String> smembers(ConnectionProperties connectionProperties, String key) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.smembers(key);
    }

    /**
     * set添加数据
     */
    public long sadd(ConnectionProperties connectionProperties, String key, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.sadd(key, value);
    }

    /**
     * set删除数据
     */
    public long srem(ConnectionProperties connectionProperties, String key, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.srem(key, value);
    }

    /**
     * zset查询数据
     */
    public List<ScoredValue<String>> zrange(ConnectionProperties connectionProperties, String key, long startPos, long stopPos) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.zrangeWithScores(key, startPos, stopPos);
    }

    /**
     * zset添加数据
     */
    public long zadd(ConnectionProperties connectionProperties, String key, double score, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.zadd(key, score, value);
    }

    /**
     * zset删除数据
     */
    public long zrem(ConnectionProperties connectionProperties, String key, String value) {
        RedisCommands<String, String> redisCommands = this.getRedisCommands(connectionProperties);
        return redisCommands.zrem(key, value);
    }

    /**
     * 获取redisCommands
     */
    private RedisCommands<String, String> getRedisCommands(ConnectionProperties connectionProperties) {
        int dbIndex = connectionProperties.getDbIndex();
        StatefulRedisConnection<String, String> connection = this.createConnection(connectionProperties);
        RedisCommands<String, String> redisCommands = connection.sync();
        redisCommands.select(dbIndex);
        return redisCommands;
    }

    private static class RedisReturnCode {
        /**
         * OK
         */
        public final static String OK = "OK";
        /**
         * ping - pong
         */
        public final static String PONG = "PONG";

        /**
         * 数据库序号错误
         */
        public final static String ERR_INVALID_DB_INDEX = "ERR invalid DB index";

    }
}
