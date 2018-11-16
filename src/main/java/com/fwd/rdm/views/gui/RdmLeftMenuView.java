package com.fwd.rdm.views.gui;

import com.fwd.rdm.controller.gui.RdmLeftMenuController;
import com.fwd.rdm.uicomponents.ConnectionTreeCell;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.control.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:05 2018/11/13
 */
@FXMLController
@FXMLView("/rdm/views/gui/rdmLeftMenu.fxml")
public class RdmLeftMenuView extends AbstractFxmlView {

    @Autowired
    private RdmLeftMenuController rdmLeftMenuController;

    public void refreshCell(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        rdmLeftMenuController.refreshCell(treeItem);
    }
}
