package com.fwd.rdm;

import com.fwd.rdm.utils.StageHolder;
import com.fwd.rdm.views.main.RdmMainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 18:55 2018/11/16
 */
@SpringBootApplication
public class RdmApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        SplashScreen screen = new CustomSplashScreen();
        launch(RdmApplication.class, RdmMainView.class, screen, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Tools");

        Stage childStage = new Stage();
        // 弹出窗口时父窗口不可操作
        childStage.initModality(Modality.WINDOW_MODAL);
        // 设置父窗口
        childStage.initOwner(primaryStage);

        StageHolder.setMainStage(primaryStage);
        StageHolder.setChildStage(childStage);

        super.start(primaryStage);
    }

    /**
     * 自定义启动页面
     */
    public static class CustomSplashScreen extends SplashScreen {
        @Override
        public boolean visible() {
            return false;
        }
    }
}
