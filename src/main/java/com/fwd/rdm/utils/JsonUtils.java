package com.fwd.rdm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 10:30 2018/11/21
 */
public class JsonUtils {

    /**
     * 美化JSON字符串
     */
    public static String prettyJsonString(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return jsonString;
        }
        // 美化json输出格式
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object object = mapper.readValue(jsonString, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            return jsonString;
        }
    }

    /**
     * 去除json格式化
     */
    public static String dePrettyJsonString(String prettyJson) {
        if (StringUtils.isEmpty(prettyJson)) {
            return prettyJson;
        }
        // 去除json格式化
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object object = mapper.readValue(prettyJson, Object.class);
            return mapper.writer().writeValueAsString(object);
        } catch (IOException e) {
            return prettyJson;
        }
    }

    public static void main(String[] args) throws IOException {
        String json = "{\"name\":\"fanwd\"}";
        System.out.println(json);
        String prettyJson = prettyJsonString(json);
        System.out.println(prettyJson);
        System.out.println(dePrettyJsonString(prettyJson));
    }
}
