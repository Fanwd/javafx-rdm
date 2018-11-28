package com.fwd.rdm.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 13:42 2018/11/28
 */
public class AlertUtils {

    private static final Alert warnAlert = new Alert(Alert.AlertType.WARNING, null, ButtonType.OK, ButtonType.CANCEL);

    /**
     * 警告信息并返回结果
     */
    public static boolean isWarnOK(String text) {
        warnAlert.setContentText(text);
        Optional<ButtonType> buttonType = warnAlert.showAndWait();
        return ButtonType.OK.equals(buttonType.orElse(ButtonType.CANCEL));
    }
}
