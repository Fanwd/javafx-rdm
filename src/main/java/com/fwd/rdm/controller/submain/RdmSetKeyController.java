package com.fwd.rdm.controller.submain;

import com.fwd.rdm.dao.RedisDao;
import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.utils.LoggerUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:24 2018/11/26
 */
@FXMLController
public class RdmSetKeyController {

    @FXML
    public VBox rootBox;

    @FXML
    private TextField keyTextField;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private RedisDao redisDao;

    @FXML
    public void initialize() {
        rootBox.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                keyTextField.setText(rdmCenterObservableData.getCurrentKey());
            }
        });
    }

    /**
     * 修改名称
     */
    @FXML
    public void rename() {
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        String newKey = keyTextField.getText();
        if (StringUtils.isEmpty(newKey)) {
            loggerUtils.alertWarn("Key不可以为空");
            return;
        }
        if (currentKey.equals(newKey)) {
            // key没有修改，直接关闭
            this.close();
        }
        if (redisDao.renamenx(currentConnectionProperties, currentKey, newKey)) {
            // 修改成功
            rdmCenterObservableData.setCurrentKey(newKey);
            rdmCenterObservableData.publishUpdateKeyInfoEvent();
            this.close();
        } else {
            // 修改失败
            if (redisDao.exists(currentConnectionProperties, newKey)) {
                loggerUtils.alertError("Key '" + newKey + "' 已存在！！");
                return;
            } else if (!redisDao.exists(currentConnectionProperties, currentKey)) {
                loggerUtils.alertError("Key '" + currentKey + "'已被删除，请刷新后重试！！");
                return;
            }
        }
    }

    /**
     * 取消
     */
    @FXML
    public void cancel() {
        this.close();
    }

    private void close() {
        keyTextField.setText(null);
        ((Stage) rootBox.getScene().getWindow()).close();
    }
}
