package com.fwd.rdm.controller.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.HashData;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.data.domain.RedisObservableData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.LoggerUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:09 2018/11/12
 */
@FXMLController
public class RdmCenterController {

    /**
     * 根容器
     */
    @FXML
    private VBox rootBox;
    /**
     * 表格
     */
    @FXML
    private GridPane gridPane;
    /**
     * 缓存key类型
     */
    @FXML
    private Label keyTypeLabel;
    /**
     * 缓存key
     */
    @FXML
    private TextField keyTextField;
    /**
     * key剩余时间
     */
    @FXML
    private Label ttlLabel;
    /**
     * 数据大小
     */
    @FXML
    Label valueSizeLabel;
    /**
     * 打印类型 Text Json
     */
    @FXML
    ChoiceBox<String> viewType;
    /**
     * HASH数据显示box
     */
    @FXML
    VBox hashBox;
    /**
     * Hash数据列表
     */
    @FXML
    TableView<HashData> hashTableView;
    /**
     * Hash数据field
     */
    @FXML
    TextArea fieldTextArea;
    /**
     * 数据内容
     */
    @FXML
    private TextArea valueTextArea;

    private static final String VIEW_TEXT = "Text";
    private static final String VIEW_JSON = "Json";

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoggerUtils loggerUtils;

    /**
     * 连接信息
     */
    private ConnectionProperties currentConnectionProperties;

    /**
     * 数据信息
     */
    private RedisObservableData redisObservableData = new RedisObservableData();

    @FXML
    public void initialize() {
        // 属性绑定
        keyTypeLabel.textProperty().bind(redisObservableData.typeProperty());
        keyTextField.textProperty().bind(redisObservableData.keyProperty());
        ttlLabel.textProperty().bind(redisObservableData.ttlProperty());
        valueSizeLabel.textProperty().bind(redisObservableData.sizeProperty());
        redisObservableData.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueTextArea.setText(newValue);
        });
        // 显示hash数据
        hashBox.managedProperty()
                .bind(Bindings
                        .when(redisObservableData.typeProperty().isEqualToIgnoreCase(KeyTypeEnum.HASH.getType()))
                        .then(true)
                        .otherwise(false));
        hashBox.visibleProperty()
                .bind(Bindings
                        .when(redisObservableData.typeProperty().isEqualToIgnoreCase(KeyTypeEnum.HASH.getType()))
                        .then(true)
                        .otherwise(false));

        fieldTextArea.textProperty().bind(redisObservableData.fieldProperty());
        redisObservableData.getHashDataList().addListener((ListChangeListener<HashData>) c -> {
            if (c.next()) {
                ObservableList<? extends HashData> allList = c.getList();
                List<HashData> dataList = allList.stream().map(item -> (HashData) item).collect(Collectors.toList());
                hashTableView.setItems(FXCollections.observableArrayList(dataList));
            }
        });
        // 选中时显示field和value
        hashTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<HashData>) c -> {
            if (c.next()) {
                List<? extends HashData> selectedData = c.getAddedSubList();
                if (!selectedData.isEmpty()) {
                    HashData hashData = selectedData.get(0);
                    redisObservableData.setField(hashData.getField());
                    this.printPrettyValue(hashData.getValue());
                    redisObservableData.setSize(String.valueOf(hashData.getValue().getBytes(Charset.forName("UTF-8")).length));
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    public void initData(ConnectionProperties connectionProperties, String key) {
        this.currentConnectionProperties = connectionProperties;
        // 宽自适应
        GridPane.setHgrow(keyTextField, Priority.ALWAYS);

        // value文本框自适应
        VBox.setVgrow(valueTextArea, Priority.ALWAYS);

        if (!rootBox.isVisible()) {
            rootBox.setVisible(true);
        }

        // TODO add by fanwd at 2018/11/13-19:07 区分key类型
        RedisData redisData = redisService.getRedisDataByKey(connectionProperties, key);

        String type = redisData.getType();
        KeyTypeEnum keyTypeEnum = KeyTypeEnum.typeOf(type);
        redisObservableData.setType(type);
        redisObservableData.setKey(redisData.getKey());
        redisObservableData.setTtl(String.valueOf(redisData.getTtl()));
        if (KeyTypeEnum.STRING.equals(keyTypeEnum)) {
            String value = redisData.getValue();
            redisObservableData.setValue(redisData.getValue());
            if (null == value) {
                redisObservableData.setSize("0");
            } else {
                redisObservableData.setSize(String.valueOf(value.getBytes(Charset.forName("UTF-8")).length));
            }

            printPrettyValue(value);

            viewType.setOnAction(event -> printPrettyValue(value));
        } else if (KeyTypeEnum.HASH.equals(keyTypeEnum)) {
            // 清空field和value
            redisObservableData.setField(null);
            redisObservableData.setValue(null);
            redisObservableData.setSize(null);
            Map<String, String> hashData = redisData.getHashData();
            List<HashData> data = new ArrayList<>();
            long index = 1;
            for (Map.Entry<String, String> entry : hashData.entrySet()) {
                HashData item = new HashData(index++, entry.getKey(), entry.getValue());
                data.add(item);
            }
            redisObservableData.getHashDataList().clear();
            redisObservableData.getHashDataList().addAll(data);
            viewType.setOnAction(event -> printPrettyValue(redisData.getValue()));
        } else {
            loggerUtils.alertWarn("Key [" + key + "] not supported");
        }
    }

    /**
     * 美化输出json
     */
    private void printPrettyValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        if (VIEW_JSON.equalsIgnoreCase(viewType.getValue())) {
            // 美化json输出格式
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object object = mapper.readValue(value, Object.class);
                String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                redisObservableData.setValue(jsonStr);
            } catch (IOException e) {
                redisObservableData.setValue(value);
            }
        } else {
            redisObservableData.setValue(value);
        }
    }

    /**
     * 保存
     */
    @FXML
    public void save() {
        String keyType = keyTypeLabel.getText();
        String key = keyTextField.getText();
        String value = valueTextArea.getText();
        KeyTypeEnum type = KeyTypeEnum.typeOf(keyType);
        if (KeyTypeEnum.STRING.equals(type)) {
            // 字符串
            redisService.set(currentConnectionProperties, key, value);
            this.redisObservableData.setValue(value);
            loggerUtils.info("save success");
        } else {
            // 暂不支持的类型
            loggerUtils.warn("Type [" + keyType + "] not supported");
        }
    }
}
