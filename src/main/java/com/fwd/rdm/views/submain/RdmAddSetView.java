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
public class RdmAddSetView extends AbstractSubFxmlView {

    public void show() {
        super.show("添加set数据");
    }
}
