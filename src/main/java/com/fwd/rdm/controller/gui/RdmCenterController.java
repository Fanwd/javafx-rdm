package com.fwd.rdm.controller.gui;

import com.fwd.rdm.data.RdmCenterObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.data.domain.RedisData;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.views.gui.IModuleView;
import com.fwd.rdm.views.main.RdmSetTTLView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:09 2018/11/12
 */
@FXMLController
public class RdmCenterController {

    @FXML
    public ScrollPane rootScrollPane;

    @FXML
    public VBox baseBox;

    @FXML
    public StackPane centerScrollPane;

    /**
     * Key
     */
    @FXML
    private TextField keyTextField;

    /**
     * TTL
     */
    @FXML
    public Label ttlLabel;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RdmSetTTLView rdmSetTTLView;

    @Autowired
    private RdmCenterObservableData rdmCenterObservableData;

    @Autowired
    private List<IModuleView> moduleViewList;

    @FXML
    public void initialize() {
        rdmCenterObservableData.updateKeyInfoEvent().addListener((observable, oldValue, newValue) -> {
            ConnectionProperties currentConnectionProperties = rdmCenterObservableData.getCurrentConnectionProperties();
            String currentKey = rdmCenterObservableData.getCurrentKey();
            RedisData redisData = redisService.getRedisKeyInfo(currentConnectionProperties, currentKey);
            keyTextField.setText(redisData.getKey());
            ttlLabel.setText(String.valueOf(redisData.getTtl()));
        });
    }

    /**
     * 初始化数据
     */
    public void initData(ConnectionProperties connectionProperties, String key) {
        rdmCenterObservableData.setCurrentConnectionProperties(connectionProperties);
        rdmCenterObservableData.setCurrentKey(key);

        RedisData redisData = redisService.getRedisKeyInfo(connectionProperties, key);

        keyTextField.setText(key);
        ttlLabel.setText(String.valueOf(redisData.getTtl()));

        String type = redisData.getType();
        for (IModuleView iModuleView : moduleViewList) {
            if (iModuleView.isSupport(type)) {
                baseBox.setVisible(true);
                baseBox.setManaged(true);
                rootScrollPane.setContent(baseBox);
                centerScrollPane.getChildren().setAll(iModuleView.getModuleView());
                return;
            }
        }
        rootScrollPane.setContent(this.show404());
    }

    private VBox show404() {
        VBox box404 = new VBox();
        box404.setSpacing(20);
        box404.setAlignment(Pos.CENTER);
        Label label404 = new Label("404");
        Label labelErrorInfo = new Label("所选Key不存在");
        box404.getChildren().addAll(label404, labelErrorInfo);
        return box404;
    }

    @FXML
    public void setTTL() {
        rdmSetTTLView.show();
    }
}
