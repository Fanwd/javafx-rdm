package com.fwd.rdm.views.submain;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:24 2018/11/26
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmSetKey.fxml", bundle = "language.ui", css = {"/rdm/css/rdm.css"})
public class RdmSetKeyView extends AbstractSubFxmlView {

    public void show() {
        super.show("重命名Key");
    }
}
