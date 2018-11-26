package com.fwd.rdm.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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

    private Label label = new Label();

    private CleanLoggerService service = new CleanLoggerService();
    {
        service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            // 清空日志内容
            label.setText(null);
            label.setGraphic(null);
        });
    }

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
        showAlert(text, Alert.AlertType.ERROR);
    }

    private void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, text);
        alert.showAndWait();
    }

    private void showInStatusBar(String text, Color textColor, FontAwesomeIconView graphic) {
        service.restart();
        label.setText(text);
        label.setBackground(new Background(new BackgroundFill(textColor, CornerRadii.EMPTY, null)));
        label.setGraphic(graphic);
    }

    static class CleanLoggerService<V> extends Service<V> {

        /**
         * 延迟时间
         */
        private static final long DELAY_TIME = 5 * 1000;

        @Override
        protected Task<V> createTask() {
            return new Task<V>() {
                @Override
                protected V call() throws Exception {
                    Thread.sleep(DELAY_TIME);
                    return null;
                }
            };
        }
    }
}
