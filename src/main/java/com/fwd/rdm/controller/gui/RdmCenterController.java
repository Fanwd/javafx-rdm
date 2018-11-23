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
    public ScrollPane centerScrollPane;

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
    }

    /**
     * 初始化数据
     */
    public void initData(ConnectionProperties connectionProperties, String key) {
        rdmCenterObservableData.setCurrentConnectionProperties(connectionProperties);
        rdmCenterObservableData.setCurrentKey(key);

        RedisData redisData = redisService.getRedisDataByKey(connectionProperties, key);

        String type = redisData.getType();
        for (IModuleView iModuleView : moduleViewList) {
            if (iModuleView.isSupport(type)) {
                rootScrollPane.setContent(iModuleView.getModuleView());
                return;
            }
        }
        VBox notSupportBox = new VBox();
        notSupportBox.setSpacing(20);
        notSupportBox.setAlignment(Pos.CENTER);
        Label label404 = new Label("404");
        Label labelErrorInfo = new Label("暂不支持'" + type + "'类型数据!!");
        if (!redisService.exists(connectionProperties, key)) {
            labelErrorInfo.setText("Key '" + key + "'不存在");
        }
        notSupportBox.getChildren().addAll(label404, labelErrorInfo);
        rootScrollPane.setContent(notSupportBox);
    }

    @FXML
    public void setTTL() {
        rdmSetTTLView.show();
    }
}
