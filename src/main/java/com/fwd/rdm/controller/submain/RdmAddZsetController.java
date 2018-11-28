package com.fwd.rdm.controller.submain;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.uicomponents.DoubleTextField;
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
 * @Date: Create in 16:31 2018/11/22
 */
@FXMLController
public class RdmAddZsetController {

    @FXML
    private StackPane rootStackPane;

    /**
     * 权重
     */
    @FXML
    private DoubleTextField scoreTextField;

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
        String scoreStr = scoreTextField.getText();
        if (StringUtils.isEmpty(scoreStr)) {
            loggerUtils.warn("分数不可以为空！！");
            return;
        }
        if (StringUtils.isEmpty(value)) {
            loggerUtils.warn("值不可以为空！！");
            return;
        }
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.sadd(currentConnectionProperties, currentKey, value) > 0) {
            rdmCenterObservableData.publishUpdateZsetEvent();
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
        scoreTextField.setText(null);
        valueTextArea.setText(null);
        Stage currentStage = (Stage) rootStackPane.getScene().getWindow();
        currentStage.close();
    }
}
