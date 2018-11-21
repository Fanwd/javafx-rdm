package com.fwd.rdm.views.gui;

import com.fwd.rdm.controller.gui.RdmHashModuleController;
import com.fwd.rdm.enums.KeyTypeEnum;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 9:57 2018/11/21
 */
@FXMLView("/rdm/views/gui/rdmHashModule.fxml")
public class RdmHashModuleView extends AbstractModuleView {

    @Override
    public boolean isSupport(String type) {
        return KeyTypeEnum.HASH.getType().equalsIgnoreCase(type);
    }
}
