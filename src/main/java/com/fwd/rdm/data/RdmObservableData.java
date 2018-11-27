package com.fwd.rdm.data;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private ObservableList<ConnectionProperties> connectionList = new ObservableListWrapper<>(Collections.synchronizedList(new ArrayList<>()));

    /**
     * 添加连接数据
     */
    public synchronized void addConnection(ConnectionProperties connectionProperties) {
        connectionProperties.setOrderNo(this.getMaxOrderNo() + 1);
        connectionList.add(connectionProperties);
    }

    /**
     * 交换排序号
     *
     * @param fromId
     * @param toId
     */
    public void exchangeOrderNo(long fromId, long toId) {
        Iterator<ConnectionProperties> iterator = connectionList.iterator();
        ConnectionProperties from = null;
        ConnectionProperties to = null;
        while (iterator.hasNext()) {
            ConnectionProperties next = iterator.next();
            if (next.getId() == fromId) {
                from = next;
            }
            if (next.getId() == toId) {
                to = next;
            }
        }
        if (from != null && to != null) {
            int fromOrderNo = from.getOrderNo();
            from.setOrderNo(to.getOrderNo());
            to.setOrderNo(fromOrderNo);
            connectionList.sort(Comparator.comparingInt(ConnectionProperties::getOrderNo));
        }
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

    private int getMaxOrderNo() {
        int maxOrderNo = -1;
        for (ConnectionProperties connectionProperties : connectionList) {
            if (connectionProperties.getOrderNo() > maxOrderNo) {
                maxOrderNo = connectionProperties.getOrderNo();
            }
        }
        return maxOrderNo;
    }

}
