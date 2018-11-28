package com.fwd.rdm.controller.main;

import com.fwd.rdm.language.LanguageHelper;
import com.fwd.rdm.language.MessageCode;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.utils.StageHolder;
import com.fwd.rdm.views.gui.RdmCenterView;
import com.fwd.rdm.views.gui.RdmLeftMenuView;
import com.fwd.rdm.views.submain.RdmNewConnectionFormView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 10:45 2018/11/9
 */
@FXMLController
public class RdmMainController {

    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane bodyPane;

    @Autowired
    private RdmCenterView rdmCenterView;

    @Autowired
    private RdmLeftMenuView rdmLeftMenuView;

    @Autowired
    private RdmNewConnectionFormView rdmNewConnectionFormView;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private LanguageHelper languageHelper;

    /**
     * 系统初始化
     */
    @FXML
    public void initialize() {
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            rootPane.setPrefWidth(bounds.getWidth() / 1.5);
            rootPane.setPrefHeight(bounds.getHeight() / 1.35);
        } catch (Exception ex) {
        }
        // 菜单
        bodyPane.setLeft(rdmLeftMenuView.getView());
        bodyPane.setCenter(rdmCenterView.getView());
        // 状态栏
        bodyPane.setBottom(loggerUtils.getLabel());
        BorderPane.setAlignment(loggerUtils.getLabel(), Pos.BOTTOM_LEFT);
    }

    /**
     * 新建连接
     */
    @FXML
    public void newConnection() {
        // 弹出窗口
        Scene scene = rdmNewConnectionFormView.getView().getScene();
        if (scene == null) {
            scene = new Scene(rdmNewConnectionFormView.getView());
        }
        Stage childStage = StageHolder.getChildStage(languageHelper.getMessage(MessageCode.NEW_CONNECTION));
        childStage.setScene(scene);
        childStage.show();
    }

}
