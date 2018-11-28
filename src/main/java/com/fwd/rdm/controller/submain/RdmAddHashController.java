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
        String field = fieldTextArea.getText();
        String value = valueTextArea.getText();
        if (StringUtils.isEmpty(field)) {
            loggerUtils.warn("域不可以为空！！");
            return;
        }
        if (StringUtils.isEmpty(value)) {
            loggerUtils.warn("值不可以为空！！");
            return;
        }
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.hsetnx(currentConnectionProperties, currentKey, field, value)) {
            rdmCenterObservableData.publishUpdateHashEvent();
            this.cancel();
        } else {
            loggerUtils.warn("相同的域已经存在！！");
            return;
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
