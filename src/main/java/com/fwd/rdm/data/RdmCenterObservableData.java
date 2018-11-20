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
     * 刷新数据标志
     */
    private LongProperty updateDateFlag = new SimpleLongProperty(0);

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

    public LongProperty updateDateFlagProperty() {
        return updateDateFlag;
    }

    public void addUpdateDateFlag() {
        // 修改updateDateFlag触发更新操作
        long current = this.updateDateFlag.get();
        this.updateDateFlag.set(current + 1);
    }
}
