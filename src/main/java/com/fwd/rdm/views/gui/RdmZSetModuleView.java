package com.fwd.rdm.views.gui;

import com.fwd.rdm.enums.KeyTypeEnum;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:50 2018/11/21
 */
@FXMLController
@FXMLView("/rdm/views/gui/rdmZSetModule.fxml")
public class RdmZSetModuleView extends AbstractModuleView {

    @Override
    public boolean isSupport(String type) {
        return KeyTypeEnum.ZSET.getType().equalsIgnoreCase(type);
    }
}
