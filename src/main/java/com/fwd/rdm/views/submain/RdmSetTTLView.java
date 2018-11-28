package com.fwd.rdm.views.submain;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description: 设置超时时间弹出框
 * @Date: Create in 14:19 2018/11/23
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmSetTTL.fxml", bundle = "language.ui", css = {"/rdm/css/rdm.css"})
public class RdmSetTTLView extends AbstractSubFxmlView {

    public void show() {
        super.show("修改TTL");
    }
}
