package com.fwd.rdm.controller.main;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.service.RedisService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:59 2018/11/21
 */
@FXMLController
public class RdmAddListController {

    @FXML
    private StackPane rootStackPane;

    @FXML
    private TextArea valueTextArea;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    @Autowired
    private RedisService redisService;

    @FXML
    public void initialize() {

    }

    /**
     * 添加
     */
    @FXML
    public void add() {
        String value = valueTextArea.getText();
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.lpush(currentConnectionProperties, currentKey, value) > 0) {
            rdmCenterObservableData.publishUpdateListEvent();
            this.close();
        }
    }

    @FXML
    public void cancel() {
        this.close();
    }

    /**
     * 关闭窗口
     */
    private void close() {
        valueTextArea.setText(null);
        Stage currentStage = (Stage) rootStackPane.getScene().getWindow();
        currentStage.close();
    }
}
