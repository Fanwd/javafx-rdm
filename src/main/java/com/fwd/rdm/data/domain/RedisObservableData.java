package com.fwd.rdm.data.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 10:27 2018/11/14
 */
public class RedisObservableData {

    private StringProperty type = new SimpleStringProperty();
    private StringProperty key = new SimpleStringProperty();
    private StringProperty value = new SimpleStringProperty();
    private StringProperty ttl = new SimpleStringProperty();
    private StringProperty size = new SimpleStringProperty();

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getTtl() {
        return ttl.get();
    }

    public StringProperty ttlProperty() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl.set(ttl);
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }
}
