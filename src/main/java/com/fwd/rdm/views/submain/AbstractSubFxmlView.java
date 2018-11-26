package com.fwd.rdm.views.submain;

import com.fwd.rdm.utils.StageHolder;
import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:26 2018/11/26
 */
public abstract class AbstractSubFxmlView extends AbstractFxmlView {

    public void show(String title) {
        Parent rootView = this.getView();
        Scene scene = rootView.getScene();
        if (null == scene) {
            scene = new Scene(rootView);
        }
        rootView.setVisible(false);
        rootView.setVisible(true);
        Stage childStage = StageHolder.getChildStage(title);
        childStage.setScene(scene);
        childStage.show();
    }
}
