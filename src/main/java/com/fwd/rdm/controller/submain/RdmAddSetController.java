package com.fwd.rdm.controller.submain;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.utils.LoggerUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:51 2018/11/22
 */
@FXMLController
public class RdmAddSetController {

    @FXML
    private StackPane rootStackPane;

    @FXML
    private TextArea valueTextArea;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoggerUtils loggerUtils;

    @FXML
    public void initialize() {

    }

    /**
     * 添加
     */
    @FXML
    public void add() {
        String value = valueTextArea.getText();
        if (StringUtils.isEmpty(value)) {
            loggerUtils.warn("Value should not be empty!!");
            return;
        }
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.sadd(currentConnectionProperties, currentKey, value) > 0) {
            rdmCenterObservableData.publishUpdateSetEvent();
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
