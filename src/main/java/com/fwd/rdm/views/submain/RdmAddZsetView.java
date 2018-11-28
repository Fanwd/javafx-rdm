package com.fwd.rdm.views.submain;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:32 2018/11/22
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmAddZset.fxml", bundle = "language.ui", css = {"/rdm/css/rdm.css"})
public class RdmAddZsetView extends AbstractSubFxmlView {

    public void show() {
        super.show("添加zset数据");
    }
}
