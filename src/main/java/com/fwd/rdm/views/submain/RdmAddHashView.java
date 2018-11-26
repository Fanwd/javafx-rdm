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
 * @Date: Create in 19:06 2018/11/20
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmAddHash.fxml", css = {"/rdm/css/rdm.css"})
public class RdmAddHashView extends AbstractSubFxmlView {

    public void show() {
        super.show("添加hash数据");
    }
}
