package com.fwd.rdm.controller.main;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.enums.KeyTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.uicomponents.ConnectionTreeCell;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.gui.RdmLeftMenuView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
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
    private TextArea fieldIdTextArea;
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
        Bindings.when(typeChoiceBox.valueProperty().isEqualTo(KeyTypeEnum.STRING.getType()))
                .then(true).otherwise(false);
    }

    public void setTreeItem(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        this.treeItem = treeItem;
    }

    @FXML
    public void add() {
        String key = keyTextField.getText();
        String type = typeChoiceBox.getValue();
        String value = valueTextArea.getText();
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
            if (redisService.set(connectionProperties, key, value)) {
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
