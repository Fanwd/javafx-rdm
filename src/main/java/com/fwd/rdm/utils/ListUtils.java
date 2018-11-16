package com.fwd.rdm.utils;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 18:15 2018/8/5
 */
public class ListUtils {

    public static <T, K> Map<K, List<T>> listToMapList(List<T> list, Function<T, K> getKey) {
        Assert.notNull(getKey, "getKey can not be null");
        Map<K, List<T>> resultMap = new HashMap<>();
        list.stream().reduce(resultMap, (map, item) -> {
            K key = getKey.apply(item);
            List<T> dataList = map.get(key);
            if (null == dataList) {
                dataList = new ArrayList<>();
                map.put(key, dataList);
            }
            dataList.add(item);
            return map;
        }, (mapA, mapB) -> {
            mapA.putAll(mapB);
            return mapA;
        });
        return resultMap;
    }
}
