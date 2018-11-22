package com.fwd.rdm.controller.main;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.uicomponents.ConnectionTreeCell;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.gui.RdmLeftMenuView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 21:45 2018/11/14
 */
@FXMLController
public class RdmAddController {

    @FXML
    private StackPane rootStackPane;
    @FXML
    private TextField keyTextField;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Label scoreLabel;
    @FXML
    private TextField scoreTextField;
    @FXML
    private Label fieldLabel;
    @FXML
    private TextArea fieldTextArea;
    @FXML
    private TextArea valueTextArea;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RdmLeftMenuView rdmLeftMenuView;

    @Autowired
    private LoggerUtils loggerUtils;

    private TreeItem<ConnectionTreeCell.TreeItemData> treeItem;

    @FXML
    public void initialize() {
        // hash数据组件可见性设置
        // 设置可见性
        StringProperty str = new SimpleStringProperty();
        fieldLabel.visibleProperty().bind(Bindings
                .when(typeChoiceBox.valueProperty().isEqualTo(KeyTypeEnum.HASH.getType())).then(true)
                .otherwise(false));
        fieldTextArea.visibleProperty().bind(fieldLabel.visibleProperty());
        // 设置隐藏性
        fieldLabel.managedProperty().bind(fieldLabel.visibleProperty());
        fieldTextArea.managedProperty().bind(fieldLabel.visibleProperty());

        // zset组件可见性设置
        scoreLabel.visibleProperty().bind(Bindings
                .when(typeChoiceBox.valueProperty().isEqualTo(KeyTypeEnum.ZSET.getType())).then(true)
                .otherwise(false));
        scoreLabel.managedProperty().bind(scoreLabel.visibleProperty());
        scoreTextField.visibleProperty().bind(scoreLabel.visibleProperty());
        scoreTextField.managedProperty().bind(scoreLabel.visibleProperty());
    }

    public void setTreeItem(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        this.treeItem = treeItem;
    }

    /**
     * 添加数据
     */
    @FXML
    public void add() {
        String key = keyTextField.getText();
        String type = typeChoiceBox.getValue();
        String value = valueTextArea.getText();
        String field = fieldTextArea.getText();
        KeyTypeEnum itemTypeEnum = KeyTypeEnum.typeOf(type);
        ConnectionProperties connectionProperties = treeItem.getValue().getConnectionPropertiesObjectProperty();
        if (StringUtils.isEmpty(key)) {
            loggerUtils.alertError("key is null!");
            return;
        }
        if (StringUtils.isEmpty(value)) {
            loggerUtils.alertError("value is null!!");
            return;
        }

        if (KeyTypeEnum.STRING.equals(itemTypeEnum)) {
            // 添加string类型数据
            if (redisService.set(connectionProperties, key, value)) {
                rdmLeftMenuView.refreshCell(treeItem);
                this.close();
            }
        } else if (KeyTypeEnum.HASH.equals(itemTypeEnum)) {
            // 添加hash类型数据
            if (StringUtils.isEmpty(field)) {
                loggerUtils.alertError("fieldId is null!!");
                return;
            }
            if (redisService.hset(connectionProperties, key, field, value)) {
                rdmLeftMenuView.refreshCell(treeItem);
                this.close();
            }
        } else if (KeyTypeEnum.LIST.equals(itemTypeEnum)) {
            // 添加list类型数据
            if (redisService.ladd(connectionProperties, key, value) > 0) {
                rdmLeftMenuView.refreshCell(treeItem);
                this.close();
            }
        } else {
            loggerUtils.alertWarn("Key type [" + type + "] not supported!");
        }
    }

    @FXML
    public void cancle() {
        this.close();
    }

    private void close() {
        keyTextField.setText(null);
        typeChoiceBox.setValue("string");
        valueTextArea.setText(null);
        ((Stage) rootStackPane.getScene().getWindow()).close();
    }
}
