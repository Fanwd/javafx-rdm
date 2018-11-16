package com.fwd.rdm.controller.main;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.service.NewConnectionService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 11:00 2018/11/9
 */
@FXMLController
public class RdmNewConnectionFormController {

    @FXML
    private StackPane rootPane;
    @FXML
    private GridPane newConnectionPane;
    @FXML
    private TextField name;
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private TextField auth;

    @FXML
    private Label messageLabel;

    @Autowired
    private NewConnectionService newConnectionService;

    @FXML
    public void initialize() {
    }

    /**
     * 测试连接
     */
    public void testConnection(ActionEvent actionEvent) {
        try {
            ConnectionProperties connectionProperties = new ConnectionProperties();
            connectionProperties.setName(name.getText());
            connectionProperties.setIp(ip.getText());
            connectionProperties.setPort(Integer.valueOf(port.getText()));
            connectionProperties.setAuth(auth.getText());
            if (newConnectionService.testConnection(connectionProperties)) {
                messageLabel.setTextFill(Color.LIGHTGREEN);
                messageLabel.setText("Connection Success");
            } else {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Connection Failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText(ex.toString());
        }
    }

    /**
     * 添加连接
     */
    public void add(ActionEvent actionEvent) {
        ConnectionProperties connectionProperties = new ConnectionProperties();
        connectionProperties.setName(name.getText());
        connectionProperties.setIp(ip.getText());
        connectionProperties.setPort(Integer.valueOf(port.getText()));
        connectionProperties.setAuth(auth.getText());
        if (newConnectionService.addConnection(connectionProperties)) {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        System.out.println("cancel");
    }
}
