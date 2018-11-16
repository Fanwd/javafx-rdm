package com.fwd.rdm.service.impl;

import com.fwd.rdm.data.RdmObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.service.NewConnectionService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 13:57 2018/11/9
 */
@Service
public class NewConnectionServiceImpl implements NewConnectionService {

    @Autowired
    private RdmObservableData rdmObservableData;

    @Override
    public boolean testConnection(ConnectionProperties connectionProperties) {
        RedisURI redisURI = new RedisURI(connectionProperties.getIp(), connectionProperties.getPort(), Duration.ofSeconds(10));
        redisURI.setPassword(connectionProperties.getAuth());
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        // 同步操作
        RedisCommands<String, String> sync = connect.sync();
        List<String> keys = sync.keys("*");
        return true;
    }

    @Override
    public boolean addConnection(ConnectionProperties connectionProperties) {
        connectionProperties.setId(System.currentTimeMillis());
        rdmObservableData.addConnection(connectionProperties);
        return true;
    }
}
