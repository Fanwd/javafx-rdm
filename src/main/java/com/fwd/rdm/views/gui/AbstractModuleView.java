package com.fwd.rdm.views.gui;

import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.scene.Parent;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 11:05 2018/11/21
 */
public abstract class AbstractModuleView extends AbstractFxmlView implements IModuleView {

    @Override
    public Parent getView() {
        Parent parent = super.getView();
        parent.setVisible(false);
        parent.setVisible(true);
        return parent;
    }

    @Override
    public Parent getModuleView() {
        return this.getView();
    }
}
