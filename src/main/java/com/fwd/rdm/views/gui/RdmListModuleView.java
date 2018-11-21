package com.fwd.rdm.views.gui;

import com.fwd.rdm.enums.KeyTypeEnum;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:41 2018/11/21
 */
@FXMLController
@FXMLView("/rdm/views/gui/rdmListModule.fxml")
public class RdmListModuleView extends AbstractModuleView {
    @Override
    public boolean isSupport(String type) {
        return KeyTypeEnum.LIST.getType().equalsIgnoreCase(type);
    }
}
