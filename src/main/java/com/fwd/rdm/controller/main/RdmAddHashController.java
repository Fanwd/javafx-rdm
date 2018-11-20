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
 * @Date: Create in 19:06 2018/11/20
 */
@FXMLController
public class RdmAddHashController {

    @FXML
    private StackPane rootStackPane;

    @FXML
    private TextArea fieldTextArea;

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
        String field = fieldTextArea.getText();
        String value = valueTextArea.getText();
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.hset(currentConnectionProperties, currentKey, field, value)) {
            rdmCenterObservableData.addUpdateDateFlag();
            this.cancel();
        }
    }

    @FXML
    public void cancel() {
        fieldTextArea.setText(null);
        valueTextArea.setText(null);
        Stage currentStage = (Stage) rootStackPane.getScene().getWindow();
        currentStage.close();
    }
}
