package com.fwd.rdm.service;

import com.fwd.rdm.data.domain.ConnectionProperties;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 13:57 2018/11/9
 */
public interface NewConnectionService {

    /**
     * 测试连接
     *
     * @param connectionProperties
     * @return
     */
    public boolean testConnection(ConnectionProperties connectionProperties);

    /**
     * 添加连接
     *
     * @param connectionProperties
     * @return
     */
    boolean addConnection(ConnectionProperties connectionProperties);
}
