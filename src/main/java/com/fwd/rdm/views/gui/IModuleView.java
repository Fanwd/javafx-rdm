package com.fwd.rdm.views.gui;

import javafx.scene.Parent;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 10:46 2018/11/21
 */
public interface IModuleView {

    public boolean isSupport(String type);

    public Parent getModuleView();
}
