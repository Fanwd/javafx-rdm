package com.fwd.rdm.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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
        warnIcon.setFill(Color.YELLOW);
        errorIcon.setFill(Color.RED);
    }

    private Label label = new Label();

    public Label getLabel() {
        return label;
    }

    public void info(String text) {
        this.showInStatusBar(text, Color.GREEN, infoIcon);
    }

    public void warn(String text) {
        this.showInStatusBar(text, Color.YELLOW, warnIcon);
    }

    public void error(String text) {
        this.showInStatusBar(text, Color.RED, errorIcon);
    }

    public void alertInfo(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.showAndWait();
    }

    public void alertWarn(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }

    public void alertError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        alert.showAndWait();
    }

    private void showInStatusBar(String text, Color textColor, FontAwesomeIconView graphic) {
        label.setText(text);
        label.setTextFill(textColor);
        label.setGraphic(graphic);
    }
}
