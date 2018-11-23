package com.fwd.rdm.controller.main;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.uicomponents.IntegerTextField;
import com.fwd.rdm.utils.LoggerUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:21 2018/11/23
 */
@FXMLController
public class RdmSetTTLController {

    @FXML
    public VBox rootBox;
    @FXML
    private IntegerTextField ttlTextField;

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
     * 设置为持久的
     */
    @FXML
    public void setPersistent() {
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.persist(currentConnectionProperties, currentKey)) {
            // 设置成功
            rdmCenterObservableData.publishUpdateTTLEvent();
            this.close();
        } else if (!redisService.exists(currentConnectionProperties, currentKey)) {
            // key值不存在
            loggerUtils.alertError("Key '" + currentKey + "' 不存在!!");
            this.close();
        } else {
            // key已经是持久的
            this.close();
        }
    }

    /**
     * 设置超时时间
     */
    @FXML
    public void setTTL() {
        String ttl = ttlTextField.getText();
        if (StringUtils.isEmpty(ttl)) {
            // ttl不能为空
            loggerUtils.warn("TTL不能为空!!");
            return;
        }
        Long seconeds = Long.valueOf(ttl);
        if (seconeds < 0) {
            // ttl为大于等于0的整数
            loggerUtils.warn("ttl应该为大于等于0的整数!!");
            return;
        }
        ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
        String currentKey = rdmCenterObservableData.getCurrentKey();
        if (redisService.expire(currentConnectionProperties, currentKey, seconeds)) {
            // 设置成功，刷新ttl
            rdmCenterObservableData.publishUpdateTTLEvent();
            this.close();
        } else if (!redisService.exists(currentConnectionProperties, currentKey)) {
            // key不存在
            loggerUtils.alertWarn("Key '" + currentKey + "' 不存在!!");
            return;
        } else {
            // 不支持此操作（2.1.3以下）
            loggerUtils.alertError("不支持此操作（2.1.3版本以下）！！");
        }
    }

    @FXML
    public void cancel() {
        this.close();
    }

    private void close() {
        ttlTextField.setText(null);
        ((Stage) rootBox.getScene().getWindow()).close();
    }
}
