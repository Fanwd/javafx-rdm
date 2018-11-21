package com.fwd.rdm.controller.gui;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.HashData;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.data.domain.RedisObservableData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.enums.ViewTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.JsonUtils;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.main.RdmAddHashView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 9:56 2018/11/21
 */
@FXMLController
public class RdmHashModuleController {

    /**
     * 根容器
     */
    @FXML
    private VBox rootBox;

    /**
     * key
     */
    @FXML
    private TextField keyTextField;

    /**
     * key有效时间
     */
    @FXML
    private Label ttlLabel;

    /**
     * 数据展示类型选择框
     */
    @FXML
    private ChoiceBox<String> viewType;

    /**
     * value
     */
    @FXML
    private TextArea valueTextArea;

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

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private RdmAddHashView rdmAddHashView;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    private RedisObservableData redisObservableData = new RedisObservableData();

    /**
     * hashTableData
     */
    private ObservableList<HashData> tableHashDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // 数据绑定
        keyTextField.textProperty().bind(redisObservableData.keyProperty());
        ttlLabel.textProperty().bind(redisObservableData.ttlProperty());
        redisObservableData.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.showValue(newValue);
        });
        viewType.setOnAction(event -> {
            this.showValue(redisObservableData.getValue());
        });
        rootBox.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.initData();
            }
        });

        // HASH类型数据初始化--开始
        fieldTextArea.textProperty().bind(redisObservableData.fieldProperty());
        redisObservableData.getHashDataList().addListener((ListChangeListener<HashData>) c -> {
            if (c.next()) {
                ObservableList<? extends HashData> allList = c.getList();
                tableHashDataList.clear();
                tableHashDataList.addAll(allList);
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
                    redisObservableData.setValue(hashData.getValue());
                }
            }
        });
        // 监听刷新数据
        rdmCenterObservableData.updateDateFlagProperty().addListener((observable, oldValue, newValue) -> {
            this.initData();
        });
        // HASH类型数据初始化--结束
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        RedisData redisData = redisService.getRedisDataByKey(currentConnectionProperties, currentKey);
        String type = redisData.getType();
        KeyTypeEnum keyTypeEnum = KeyTypeEnum.typeOf(type);
        if (KeyTypeEnum.HASH.equals(keyTypeEnum)) {
            redisObservableData.setType(type);
            redisObservableData.setKey(redisData.getKey());
            redisObservableData.setTtl(String.valueOf(redisData.getTtl()));
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
        }
    }

    /**
     * 保存数据
     */
    @FXML
    public void save() {
        // 获取数据
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        String value = valueTextArea.getText();
        String field = fieldTextArea.getText();
        if (StringUtils.isEmpty(field)) {
            loggerUtils.alertWarn("Field Not Selected");
            return;
        }
        // 保存数据
        redisService.hset(currentConnectionProperties, currentKey, field, value);
        this.redisObservableData.setValue(value);
        // 刷新页面数据
        ObservableList<HashData> hashDataList = this.redisObservableData.getHashDataList();
        hashDataList.forEach(hashData -> {
            if (hashData.getField().equals(field)) {
                hashData.setValue(value);
            }
        });
        tableHashDataList.clear();
        tableHashDataList.addAll(hashDataList);
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
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
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
    @FXML
    public void refreshHash() {
        this.initData();
    }

    /**
     * 显示value值
     */
    private void showValue(String value) {
        if (ViewTypeEnum.isJson(viewType.getValue())) {
            valueTextArea.setText(JsonUtils.prettyJsonString(value));
        } else {
            valueTextArea.setText(value);
        }
    }
}
