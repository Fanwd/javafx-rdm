package com.fwd.rdm.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 22:58 2018/11/13
 */
@Component
public class LoggerUtils {

    private FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
    private FontAwesomeIconView warnIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
    private FontAwesomeIconView errorIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE);

    {
        infoIcon.setFill(Color.GREEN);
        warnIcon.setFill(Color.GREENYELLOW);
        errorIcon.setFill(Color.RED);
    }

    private Label label = new Label();

    public Label getLabel() {
        return label;
    }

    public void info(String text) {
        this.showInStatusBar(text, Color.WHITE, infoIcon);
    }

    public void warn(String text) {
        this.showInStatusBar(text, Color.YELLOW, warnIcon);
    }

    public void error(String text) {
        this.showInStatusBar(text, Color.RED, errorIcon);
    }

    public void alertInfo(String text) {
        showAlert(text, Alert.AlertType.INFORMATION);
    }

    public void alertWarn(String text) {
        showAlert(text, Alert.AlertType.WARNING);
    }

    public void alertError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        alert.showAndWait();
        showAlert(text, Alert.AlertType.ERROR);
    }

    private void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, text);
        alert.showAndWait();
    }

    private void showInStatusBar(String text, Color textColor, FontAwesomeIconView graphic) {
        label.setText(text);
        label.setBackground(new Background(new BackgroundFill(textColor, CornerRadii.EMPTY, null)));
        label.setGraphic(graphic);
    }
}
