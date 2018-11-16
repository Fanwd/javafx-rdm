package com.fwd.rdm.data.domain;

import javafx.beans.property.*;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 15:05 2018/11/9
 */
public class ConnectionProperties {

    /**
     * 连接id
     */
    private LongProperty id = new SimpleLongProperty();
    /**
     * 连接名称
     */
    private StringProperty name = new SimpleStringProperty();
    /**
     * 地址
     */
    private StringProperty ip = new SimpleStringProperty();
    /**
     * 端口
     */
    private IntegerProperty port = new SimpleIntegerProperty();
    /**
     * 密码
     */
    private StringProperty auth = new SimpleStringProperty();
    /**
     * 数据库序号
     */
    private IntegerProperty dbIndex = new SimpleIntegerProperty();
    /**
     * 当前数据库key个数
     */
    private IntegerProperty keyCount = new SimpleIntegerProperty();

    public ConnectionProperties() {
    }

    public ConnectionProperties(ConnectionProperties connectionProperties) {
        this.id.set(connectionProperties.getId());
        this.name.set(connectionProperties.getName());
        this.ip.set(connectionProperties.getIp());
        this.port.set(connectionProperties.getPort());
        this.auth.set(connectionProperties.getAuth());
        this.dbIndex.set(connectionProperties.getDbIndex());
        this.keyCount.set(connectionProperties.getKeyCount());
    }

    public ConnectionProperties(long id, String name, String ip, int port, String auth, int dbIndex, int keyCount) {
        this.id.set(id);
        this.name.set(name);
        this.ip.set(ip);
        this.port.set(port);
        this.auth.set(auth);
        this.dbIndex.set(dbIndex);
        this.keyCount.set(keyCount);
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public String getAuth() {
        return auth.get();
    }

    public StringProperty authProperty() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth.set(auth);
    }

    public int getDbIndex() {
        return dbIndex.get();
    }

    public IntegerProperty dbIndexProperty() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex.set(dbIndex);
    }

    public int getKeyCount() {
        return keyCount.get();
    }

    public IntegerProperty keyCountProperty() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount.set(keyCount);
    }
}
