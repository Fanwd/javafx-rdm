package com.fwd.rdm.views.gui;

import com.fwd.rdm.enums.KeyTypeEnum;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 9:57 2018/11/21
 */
@FXMLView("/rdm/views/gui/rdmStringModule.fxml")
public class RdmStringModuleView extends AbstractModuleView {
    @Override
    public boolean isSupport(String type) {
        return KeyTypeEnum.STRING.getType().equalsIgnoreCase(type);
    }
}
