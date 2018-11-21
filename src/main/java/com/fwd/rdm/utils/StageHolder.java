package com.fwd.rdm.utils;

import javafx.stage.Stage;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:47 2018/11/14
 */
public class StageHolder {

    private static Stage mainStage;
    private static Stage childStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        StageHolder.mainStage = mainStage;
    }

    public static Stage getChildStage(String title) {
        childStage.setTitle(title);
        return childStage;
    }

    public static void setChildStage(Stage childStage) {
        StageHolder.childStage = childStage;
    }
}
