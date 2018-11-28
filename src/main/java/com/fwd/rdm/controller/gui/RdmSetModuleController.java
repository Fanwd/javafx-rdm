package com.fwd.rdm.controller.gui;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.data.domain.RedisObservableData;
import com.fwd.rdm.data.domain.SetData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.enums.ViewTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.AlertUtils;
import com.fwd.rdm.utils.DragUtils;
import com.fwd.rdm.utils.JsonUtils;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.submain.RdmAddSetView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:56 2018/11/21
 */
@FXMLController
public class RdmSetModuleController {

    /**
     * 根容器
     */
    @FXML
    private VBox rootBox;

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
    public TableView<SetData> dataTableView;

    @Autowired
    private RdmAddSetView rdmAddSetView;

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
    private ObservableList<SetData> tableDataList = FXCollections.observableArrayList();

    /**
     * 列表序号开始数字
     */
    private final int BASE_INDEX = 0;

    @FXML
    public void initialize() {

        // 垂直拖拽
        DragUtils.vResizeDrag(dataTableBox, dragLine, valueBox);

        // 数据绑定
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

        // 数据初始化--开始
        redisObservableData.getSetDataList().addListener((ListChangeListener<SetData>) c -> {
            if (c.next()) {
                ObservableList<? extends SetData> allList = c.getList();
                tableDataList.clear();
                tableDataList.addAll(allList);
            }
        });
        dataTableView.setItems(tableDataList);
        // 选中时显示value
        dataTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<SetData>) c -> {
            if (c.next()) {
                List<? extends SetData> selectedData = c.getAddedSubList();
                if (!selectedData.isEmpty()) {
                    SetData data = selectedData.get(0);
                    redisObservableData.setValue(data.getValue());
                    redisObservableData.setIndex(data.getIndex());
                }
            }
        });
        // 监听刷新数据
        rdmCenterObservableData.updateSetEvent().addListener((observable, oldValue, newValue) -> {
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
        if (KeyTypeEnum.SET.equals(keyTypeEnum)) {
            redisObservableData.setType(type);
            redisObservableData.setKey(redisData.getKey());
            redisObservableData.setTtl(String.valueOf(redisData.getTtl()));
            redisObservableData.setField(null);
            redisObservableData.setValue(null);
            redisObservableData.setIndex(-1);
            redisObservableData.setSize(null);
            Set<String> dataList = redisData.getSetData();
            List<SetData> data = new ArrayList<>();
            long index = BASE_INDEX;
            for (String key : dataList) {
                SetData dataItem = new SetData(index++, key);
                data.add(dataItem);
            }
            redisObservableData.getSetDataList().clear();
            redisObservableData.getSetDataList().addAll(data);
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
        if (ViewTypeEnum.isJson(viewType.getValue())) {
            value = JsonUtils.dePrettyJsonString(value);
        }
        long index = redisObservableData.getIndex();
        if (StringUtils.isEmpty(value) || index == -1) {
            loggerUtils.alertWarn("Field Not Selected");
            return;
        }
        // 保存数据

        if (redisService.smodify(currentConnectionProperties, currentKey, redisObservableData.getValue(), value) > 0) {
            // 保存成功刷新页面数据
            this.redisObservableData.setValue(value);
            ObservableList<SetData> dataList = this.redisObservableData.getSetDataList();
            dataList.get((int) index).setValue(value);
            tableDataList.clear();
            tableDataList.addAll(dataList);
            loggerUtils.alertInfo("保存成功！！");
        }
    }

    /**
     * 添加数据
     */
    @FXML
    public void add() {
        rdmAddSetView.show();
    }

    /**
     * 删除数据
     */
    @FXML
    public void delete() {
        SetData selectedItem = dataTableView.getSelectionModel().getSelectedItem();
        if (null == selectedItem) {
            loggerUtils.alertWarn("Please select a data!!");
            return;
        }
        if (!AlertUtils.isWarnOK("确认删除？")) {
            return;
        }
        Long selectedIndex = selectedItem.getIndex();
        String selectedValue = selectedItem.getValue();
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();

        if (redisService.sdel(currentConnectionProperties, currentKey, selectedValue) >= 0) {
            // 删除成功
            ObservableList<SetData> dataList = redisObservableData.getSetDataList();
            redisObservableData.setIndex(-1);
            redisObservableData.setValue(null);
            // 删除数据并重新设置行号
            dataList.remove(selectedIndex.intValue());
            Iterator<SetData> iterator = dataList.iterator();
            long index = BASE_INDEX;
            while (iterator.hasNext()) {
                SetData next = iterator.next();
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
