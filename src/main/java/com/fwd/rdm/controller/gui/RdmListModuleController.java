package com.fwd.rdm.controller.gui;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.ListData;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.data.domain.RedisObservableData;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.enums.ViewTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.AlertUtils;
import com.fwd.rdm.utils.DragUtils;
import com.fwd.rdm.utils.JsonUtils;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.submain.RdmAddListView;
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

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:56 2018/11/21
 */
@FXMLController
public class RdmListModuleController {

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
    public TableView<ListData> dataTableView;

    @Autowired
    private RdmAddListView rdmAddListView;

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
    private ObservableList<ListData> tableDataList = FXCollections.observableArrayList();

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
        redisObservableData.getListDataList().addListener((ListChangeListener<ListData>) c -> {
            if (c.next()) {
                ObservableList<? extends ListData> allList = c.getList();
                tableDataList.clear();
                tableDataList.addAll(allList);
            }
        });
        dataTableView.setItems(tableDataList);
        // 选中时显示value
        dataTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<ListData>) c -> {
            if (c.next()) {
                List<? extends ListData> selectedData = c.getAddedSubList();
                if (!selectedData.isEmpty()) {
                    ListData data = selectedData.get(0);
                    redisObservableData.setValue(data.getValue());
                    redisObservableData.setIndex(data.getIndex());
                }
            }
        });
        // 监听刷新数据
        rdmCenterObservableData.updateListEvent().addListener((observable, oldValue, newValue) -> {
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
        if (KeyTypeEnum.LIST.equals(keyTypeEnum)) {
            redisObservableData.setType(type);
            redisObservableData.setKey(redisData.getKey());
            redisObservableData.setTtl(String.valueOf(redisData.getTtl()));
            redisObservableData.setField(null);
            redisObservableData.setValue(null);
            redisObservableData.setIndex(-1);
            redisObservableData.setSize(null);
            List<String> dataList = redisData.getListData();
            List<ListData> data = new ArrayList<>();
            long index = BASE_INDEX;
            for (String key : dataList) {
                ListData listData = new ListData(index++, key);
                data.add(listData);
            }
            redisObservableData.getListDataList().clear();
            redisObservableData.getListDataList().addAll(data);
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
        if (redisService.lmodify(currentConnectionProperties, currentKey, redisObservableData.getValue(), value, index)) {
            // 保存成功刷新页面数据
            this.redisObservableData.setValue(value);
            ObservableList<ListData> dataList = this.redisObservableData.getListDataList();
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
        rdmAddListView.show();
    }

    /**
     * 删除数据
     */
    @FXML
    public void delete() {
        ListData selectedItem = dataTableView.getSelectionModel().getSelectedItem();
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

        if (redisService.ldel(currentConnectionProperties, currentKey, selectedValue, selectedIndex)) {
            // 删除成功
            ObservableList<ListData> dataList = redisObservableData.getListDataList();
            redisObservableData.setIndex(-1);
            redisObservableData.setValue(null);
            // 删除数据并重新设置行号
            dataList.remove(selectedIndex.intValue());
            Iterator<ListData> iterator = dataList.iterator();
            long index = BASE_INDEX;
            while (iterator.hasNext()) {
                ListData next = iterator.next();
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
