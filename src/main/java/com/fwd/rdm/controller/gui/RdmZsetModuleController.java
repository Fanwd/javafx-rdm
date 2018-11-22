package com.fwd.rdm.controller.gui;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.data.domain.RedisObservableData;
import com.fwd.rdm.data.domain.ZsetData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.enums.ViewTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.uicomponents.DoubleTextField;
import com.fwd.rdm.utils.DragUtils;
import com.fwd.rdm.utils.JsonUtils;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.main.RdmAddZsetView;
import de.felixroske.jfxsupport.FXMLController;
import io.lettuce.core.ScoredValue;
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

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:56 2018/11/21
 */
@FXMLController
public class RdmZsetModuleController {

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
     * 分数
     */
    @FXML
    private DoubleTextField scoreTextField;

    /**
     * value
     */
    @FXML
    private TextArea valueTextArea;

    /**
     * 数据显示box
     */
    @FXML
    public HBox dataTableBox;

    /**
     * 拖拽条
     */
    @FXML
    public Separator dragLine;

    /**
     * 值显示区域
     */
    @FXML
    private VBox valueBox;

    /**
     * 数据列表
     */
    @FXML
    public TableView<ZsetData> dataTableView;

    @Autowired
    private RdmAddZsetView rdmAddZsetView;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    private RedisObservableData redisObservableData = new RedisObservableData();

    /**
     * TableData
     */
    private ObservableList<ZsetData> tableDataList = FXCollections.observableArrayList();

    /**
     * 列表序号开始数字
     */
    private final int BASE_INDEX = 0;

    @FXML
    public void initialize() {

        // 垂直拖拽
        DragUtils.vResizeDrag(dataTableBox, dragLine, valueBox);

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
        redisObservableData.scoreProperty().addListener((observable, oldValue, newValue) -> {
            scoreTextField.setText(newValue);
        });

        // 数据初始化--开始
        redisObservableData.getZsetDataList().addListener((ListChangeListener<ZsetData>) c -> {
            if (c.next()) {
                ObservableList<? extends ZsetData> allList = c.getList();
                tableDataList.clear();
                tableDataList.addAll(allList);
            }
        });
        dataTableView.setItems(tableDataList);
        // 选中时显示value
        dataTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<ZsetData>) c -> {
            if (c.next()) {
                List<? extends ZsetData> selectedData = c.getAddedSubList();
                if (!selectedData.isEmpty()) {
                    ZsetData data = selectedData.get(0);
                    redisObservableData.setValue(data.getValue());
                    redisObservableData.setIndex(data.getIndex());
                    redisObservableData.setScore(String.valueOf(data.getScore()));
                }
            }
        });
        // 监听刷新数据
        rdmCenterObservableData.updateZsetEvent().addListener((observable, oldValue, newValue) -> {
            this.initData();
        });
        // 数据初始化--结束


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
        if (KeyTypeEnum.ZSET.equals(keyTypeEnum)) {
            redisObservableData.setType(type);
            redisObservableData.setKey(redisData.getKey());
            redisObservableData.setTtl(String.valueOf(redisData.getTtl()));
            redisObservableData.setField(null);
            redisObservableData.setValue(null);
            redisObservableData.setIndex(-1);
            redisObservableData.setSize(null);
            redisObservableData.setScore(null);
            List<ScoredValue<String>> dataList = redisData.getZsetData();
            List<ZsetData> data = new ArrayList<>();
            long index = BASE_INDEX;
            for (ScoredValue<String> key : dataList) {
                ZsetData dataItem = new ZsetData(index++, key.getValue(), key.getScore());
                data.add(dataItem);
            }
            redisObservableData.getZsetDataList().clear();
            redisObservableData.getZsetDataList().addAll(data);
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
        String score = scoreTextField.getText();
        long index = redisObservableData.getIndex();
        if (StringUtils.isEmpty(value) || index == -1) {
            loggerUtils.alertWarn("Field Not Selected");
            return;
        }
        if (StringUtils.isEmpty(score)) {
            loggerUtils.alertWarn("Score is Empty!!");
            return;
        }
        if (StringUtils.isEmpty(value)) {
            loggerUtils.alertWarn("Value is Empty!!");
        }
        Double doubleScore = Double.valueOf(score);
        // 保存数据

        if (redisService.zmodify(currentConnectionProperties, currentKey, redisObservableData.getValue(), doubleScore, value) > 0) {
            // 保存成功刷新页面数据
            this.redisObservableData.setValue(value);
            this.redisObservableData.setScore(score);
            ObservableList<ZsetData> dataList = this.redisObservableData.getZsetDataList();
            dataList.get((int) index).setValue(value);
            dataList.get((int) index).setScore(doubleScore);
            tableDataList.clear();
            tableDataList.addAll(dataList);
        }
    }

    /**
     * 添加数据
     */
    @FXML
    public void add() {
        rdmAddZsetView.show();
    }

    /**
     * 删除数据
     */
    @FXML
    public void delete() {
        ZsetData selectedItem = dataTableView.getSelectionModel().getSelectedItem();
        if (null == selectedItem) {
            loggerUtils.alertWarn("Please select a data!!");
            return;
        }
        Long selectedIndex = selectedItem.getIndex();
        String selectedValue = selectedItem.getValue();
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();

        if (redisService.zdel(currentConnectionProperties, currentKey, selectedValue) >= 0) {
            // 删除成功
            ObservableList<ZsetData> dataList = redisObservableData.getZsetDataList();
            redisObservableData.setIndex(-1);
            redisObservableData.setValue(null);
            // 删除数据并重新设置行号
            dataList.remove(selectedIndex.intValue());
            Iterator<ZsetData> iterator = dataList.iterator();
            long index = BASE_INDEX;
            while (iterator.hasNext()) {
                ZsetData next = iterator.next();
                next.setIndex(index++);
            }
        }
    }

    /**
     * 重新加载
     */
    @FXML
    public void refresh() {
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
