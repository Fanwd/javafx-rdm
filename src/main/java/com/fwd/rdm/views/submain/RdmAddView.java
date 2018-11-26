package com.fwd.rdm.views.submain;

import com.fwd.rdm.controller.submain.RdmAddController;
import com.fwd.rdm.uicomponents.ConnectionTreeCell;
import com.fwd.rdm.utils.StageHolder;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 21:45 2018/11/14
 */
@FXMLController
@FXMLView(value = "/rdm/views/submain/rdmAdd.fxml", css = {"/rdm/css/rdm.css"})
public class RdmAddView extends AbstractFxmlView {

    @Autowired
    private RdmAddController rdmAddController;

    public void show(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        rdmAddController.setTreeItem(treeItem);
        Stage childStage = StageHolder.getChildStage("Add Data");
        Scene scene = this.getView().getScene();
        if (null == scene) {
            scene = new Scene(this.getView());
        }
        childStage.setScene(scene);
        childStage.show();
    }
}
