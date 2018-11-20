package com.fwd.rdm.data;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:57 2018/11/9
 */
@Component
public class RdmObservableData {

    /**
     * 连接列表
     */
    private ObservableList<ConnectionProperties> connectionList = new ObservableListWrapper<>(new ArrayList<>());

    /**
     * 添加连接数据
     */
    public void addConnection(ConnectionProperties connectionProperties) {
        connectionList.add(connectionProperties);
    }

    /**
     * 删除数据列表
     */
    public void removeConnection(ConnectionProperties connectionProperties) {
        long id = connectionProperties.getId();
        Iterator<ConnectionProperties> iterator = connectionList.iterator();
        while (iterator.hasNext()) {
            ConnectionProperties next = iterator.next();
            if (next.getId() == id) {
                iterator.remove();
                break;
            }
        }
    }

    public ObservableList<ConnectionProperties> getConnectionList() {
        return this.connectionList;
    }

}
