package com.fwd.rdm.views.submain;

import com.fwd.rdm.controller.submain.RdmAddController;
import com.fwd.rdm.uicomponents.ConnectionTreeCell;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.control.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 21:45 2018/11/14
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmAdd.fxml", bundle = "language.ui", css = {"/rdm/css/rdm.css"})
public class RdmAddView extends AbstractSubFxmlView {

    @Autowired
    private RdmAddController rdmAddController;

    public void show(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        rdmAddController.setTreeItem(treeItem);
        super.show("添加数据");
    }
}
