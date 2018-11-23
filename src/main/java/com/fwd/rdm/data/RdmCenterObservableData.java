package com.fwd.rdm.data;

import com.fwd.rdm.data.domain.ConnectionProperties;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import org.springframework.stereotype.Component;

/**
 * @Author: fanwd
 * @Description: Center数据展示页面共享数据
 * @Date: Create in 19:43 2018/11/20
 */
@Component
public class RdmCenterObservableData {

    /**
     * 当前连接
     */
    private static final ThreadLocal<ConnectionProperties> currentConnectionPropertiesThreadLocal = new ThreadLocal<>();

    /**
     * 当前key
     */
    private static final ThreadLocal<String> currentKeyThreadLocal = new ThreadLocal<>();

    /**
     * 刷新hash数据标志
     */
    private LongProperty updateHashFlag = new SimpleLongProperty(0);

    /**
     * 刷新list数据标志
     */
    private LongProperty updateListFlag = new SimpleLongProperty(0);

    /**
     * 刷新set数据标志
     */
    private LongProperty updateSetFlag = new SimpleLongProperty(0);

    /**
     * 刷新zset数据标志
     */
    private LongProperty updateZsetFlag = new SimpleLongProperty(0);

    /**
     * 刷新TTL标志
     */
    private LongProperty updateTTLFlag = new SimpleLongProperty(0);

    /**
     * 刷新Key标志
     */
    private LongProperty updateKeyFlag = new SimpleLongProperty(0);

    public void setCurrentConnectionProperties(ConnectionProperties connectionProperties) {
        // 设置和获取数据时必须是在JavaFX Application线程
        assert Platform.isFxApplicationThread();
        currentConnectionPropertiesThreadLocal.set(connectionProperties);
    }

    public ConnectionProperties getCurrentConnectionProperties() {
        assert Platform.isFxApplicationThread();
        return currentConnectionPropertiesThreadLocal.get();
    }

    public void setCurrentKey(String key) {
        assert Platform.isFxApplicationThread();
        currentKeyThreadLocal.set(key);
    }

    public String getCurrentKey() {
        assert Platform.isFxApplicationThread();
        return currentKeyThreadLocal.get();
    }

    public LongProperty updateHashEvent() {
        return updateHashFlag;
    }

    public void publishUpdateHashEvent() {
        long current = this.updateHashFlag.get();
        this.updateHashFlag.set(current + 1);
    }

    public LongProperty updateListEvent() {
        return updateListFlag;
    }

    public void publishUpdateListEvent() {
        long current = this.updateListFlag.get();
        this.updateListFlag.set(current + 1);
    }

    public LongProperty updateSetEvent() {
        return updateSetFlag;
    }

    public void publishUpdateSetEvent() {
        this.updateSetFlag.set(this.updateSetFlag.get() + 1);
    }

    public LongProperty updateZsetEvent() {
        return updateZsetFlag;
    }

    public void publishUpdateZsetEvent() {
        this.updateZsetFlag.set(this.updateZsetFlag.get() + 1);
    }

    public LongProperty updateTTLEvent() {
        return updateTTLFlag;
    }

    public void publishUpdateTTLEvent() {
        this.updateTTLFlag.set(this.updateTTLFlag.get() + 1);
    }

    public LongProperty updateKeyEvent() {
        return updateKeyFlag;
    }

    public void publishUpdateKeyEvent() {
        this.updateKeyFlag.set(this.updateKeyFlag.get() + 1);
    }
}
