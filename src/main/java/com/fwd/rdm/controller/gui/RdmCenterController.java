package com.fwd.rdm.controller.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.HashData;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.data.domain.RedisObservableData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.DragUtils;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.main.RdmAddHashView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
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
    public VBox rootBox;
    /**
     * 表格
     */
    @FXML
    public GridPane gridPane;
    /**
     * 缓存key类型
     */
    @FXML
    public Label keyTypeLabel;
    /**
     * 缓存key
     */
    @FXML
    public TextField keyTextField;
    /**
     * key剩余时间
     */
    @FXML
    public Label ttlLabel;
    /**
     * 数据大小
     */
    @FXML
    public Label valueSizeLabel;
    /**
     * 打印类型 Text Json
     */
    @FXML
    public ChoiceBox<String> viewType;
    /**
     * HASH数据显示box
     */
    @FXML
    public HBox hashTableBox;
    /**
     * 拖拽条
     */
    @FXML
    public Separator dragLine;
    /**
     * value显示box
     */
    @FXML
    public VBox valueBox;
    /**
     * Hash数据列表
     */
    @FXML
    public TableView<HashData> hashTableView;
    /**
     * Hash数据field显示
     */
    @FXML
    public VBox hashFieldBox;
    /**
     * Hash数据field
     */
    @FXML
    public TextArea fieldTextArea;
    /**
     * 数据内容
     */
    @FXML
    public TextArea valueTextArea;

    private static final String VIEW_TEXT = "Text";
    private static final String VIEW_JSON = "Json";

    @Autowired
    private RdmAddHashView rdmAddHashView;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    /**
     * 连接信息
     */
    private ConnectionProperties currentConnectionProperties;

    /**
     * 当前显示的key值
     */
    private String currentKey;

    /**
     * 数据信息
     */
    private RedisObservableData redisObservableData = new RedisObservableData();

    /**
     * hashTableData
     */
    private ObservableList<HashData> tableHashDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // 公共初始化--开始
        DragUtils.vResizeDrag(hashTableBox, dragLine, valueBox);
        // 监听刷新数据
        rdmCenterObservableData.updateDateFlagProperty().addListener((observable, oldValue, newValue) -> {
            this.initData(currentConnectionProperties, currentKey);
        });
        // 属性绑定-公共属性
        keyTypeLabel.textProperty().bind(redisObservableData.typeProperty());
        keyTextField.textProperty().bind(redisObservableData.keyProperty());
        ttlLabel.textProperty().bind(redisObservableData.ttlProperty());
        valueSizeLabel.textProperty().bind(redisObservableData.sizeProperty());
        redisObservableData.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueTextArea.setText(newValue);
        });
        // 公共初始化--结束

        // HASH类型数据初始化--开始
        hashTableBox.managedProperty()
                .bind(Bindings
                        .when(redisObservableData.typeProperty().isEqualToIgnoreCase(KeyTypeEnum.HASH.getType()))
                        .then(true)
                        .otherwise(false));
        hashTableBox.visibleProperty().bind(hashTableBox.managedProperty());
        hashFieldBox.managedProperty().bind(hashTableBox.managedProperty());
        hashFieldBox.visibleProperty().bind(hashTableBox.visibleProperty());
        dragLine.visibleProperty().bind(hashTableBox.visibleProperty());
        fieldTextArea.textProperty().bind(redisObservableData.fieldProperty());
        redisObservableData.getHashDataList().addListener((ListChangeListener<HashData>) c -> {
            if (c.next()) {
                ObservableList<? extends HashData> allList = c.getList();
                List<HashData> dataList = allList.stream().map(item -> (HashData) item).collect(Collectors.toList());
                tableHashDataList.clear();
                tableHashDataList.addAll(dataList);
            }
        });
        hashTableView.setItems(tableHashDataList);
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
        // HASH类型数据初始化--结束

        // LIST类型数据初始化--开始
        // LIST类型数据初始化--结束
        // SET类型数据初始化--开始
        // SET类型数据初始化--结束
        // ZSET类型数据初始化--开始
        // ZSET类型数据初始化--结束

    }

    /**
     * 初始化数据
     */
    public void initData(ConnectionProperties connectionProperties, String key) {
        rdmCenterObservableData.setCurrentConnectionProperties(connectionProperties);
        rdmCenterObservableData.setCurrentKey(key);

        this.currentConnectionProperties = connectionProperties;
        this.currentKey = key;

        if (!rootBox.isVisible()) {
            rootBox.setVisible(true);
        }

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
        String field = fieldTextArea.getText();
        KeyTypeEnum type = KeyTypeEnum.typeOf(keyType);
        if (KeyTypeEnum.STRING.equals(type)) {
            // 字符串
            redisService.set(currentConnectionProperties, key, value);
            this.redisObservableData.setValue(value);
            loggerUtils.info("save success");
        } else if (KeyTypeEnum.HASH.equals(type)) {
            if (StringUtils.isEmpty(field)) {
                loggerUtils.alertWarn("Field Not Selected");
                return;
            }
            // HASH
            redisService.hset(currentConnectionProperties, key, field, value);
            this.redisObservableData.setValue(value);
            ObservableList<HashData> hashDataList = this.redisObservableData.getHashDataList();
            hashDataList.forEach(hashData -> {
                if (hashData.getField().equals(field)) {
                    hashData.setValue(value);
                }
            });
            tableHashDataList.clear();
            tableHashDataList.addAll(hashDataList);
        } else {
            // 暂不支持的类型
            loggerUtils.warn("Type [" + keyType + "] not supported");
        }
    }

    /**
     * 添加hash数据
     */
    @FXML
    public void addHash() {
        rdmAddHashView.show();
    }

    /**
     * 删除hash数据
     */
    @FXML
    public void deleteHash() {
        HashData selectedItem = hashTableView.getSelectionModel().getSelectedItem();
        if (null == selectedItem) {
            loggerUtils.alertWarn("Please select a data!!");
            return;
        }
        String field = selectedItem.getField();
        redisService.hdelete(currentConnectionProperties, currentKey, field);
        ObservableList<HashData> hashDataList = redisObservableData.getHashDataList();
        // 删除数据并重新设置行号
        Iterator<HashData> iterator = hashDataList.iterator();
        long index = 1;
        while (iterator.hasNext()) {
            HashData next = iterator.next();
            if (next.getField().equals(field)) {
                iterator.remove();
            } else {
                next.setIndex(index++);
            }
        }
    }

    /**
     * 重新加载
     */
    public void refreshHash() {
        this.initData(currentConnectionProperties, currentKey);
    }
}
