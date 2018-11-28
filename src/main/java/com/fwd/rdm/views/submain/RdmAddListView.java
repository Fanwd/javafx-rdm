package com.fwd.rdm.views.submain;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 17:09 2018/11/21
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmAddList.fxml", bundle = "language.ui", css = {"/rdm/css/rdm.css"})
public class RdmAddListView extends AbstractSubFxmlView {

    public void show() {
        super.show("添加list数据");
    }
}
