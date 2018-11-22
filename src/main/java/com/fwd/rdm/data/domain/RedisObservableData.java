package com.fwd.rdm.data.domain;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;

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
    private StringProperty field = new SimpleStringProperty();
    private LongProperty index = new SimpleLongProperty();
    private StringProperty score = new SimpleStringProperty();
    private ObservableList<HashData> hashDataList = new ObservableListWrapper<>(new ArrayList<>());
    private ObservableList<ListData> listDataList = new ObservableListWrapper<>(new ArrayList<>());
    private ObservableList<SetData> setDataList = new ObservableListWrapper<>(new ArrayList<>());
    private ObservableList<ZsetData> zsetDataList = new ObservableListWrapper<>(new ArrayList<>());

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

    public String getField() {
        return field.get();
    }

    public StringProperty fieldProperty() {
        return field;
    }

    public void setField(String field) {
        this.field.set(field);
    }

    public long getIndex() {
        return index.get();
    }

    public LongProperty indexProperty() {
        return index;
    }

    public void setIndex(long index) {
        this.index.set(index);
    }

    public String getScore() {
        return score.get();
    }

    public StringProperty scoreProperty() {
        return score;
    }

    public void setScore(String score) {
        this.score.set(score);
    }

    public ObservableList<HashData> getHashDataList() {
        return hashDataList;
    }

    public void setHashDataList(ObservableList<HashData> hashDataList) {
        this.hashDataList = hashDataList;
    }

    public ObservableList<ListData> getListDataList() {
        return listDataList;
    }

    public void setListDataList(ObservableList<ListData> listDataList) {
        this.listDataList = listDataList;
    }

    public ObservableList<SetData> getSetDataList() {
        return setDataList;
    }

    public void setSetDataList(ObservableList<SetData> setDataList) {
        this.setDataList = setDataList;
    }

    public ObservableList<ZsetData> getZsetDataList() {
        return zsetDataList;
    }

    public void setZsetDataList(ObservableList<ZsetData> zsetDataList) {
        this.zsetDataList = zsetDataList;
    }
}
