package com.fwd.rdm.views.submain;

import com.fwd.rdm.utils.StageHolder;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:55 2018/11/22
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmAddSet.fxml", css = {"/rdm/css/rdm.css"})
public class RdmAddSetView extends AbstractFxmlView {

    public void show() {
        Scene scene = this.getView().getScene();
        if (null == scene) {
            scene = new Scene(this.getView());
        }
        Stage childStage = StageHolder.getChildStage("Add Set");
        childStage.setScene(scene);
        childStage.show();
    }
}
