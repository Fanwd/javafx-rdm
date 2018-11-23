package com.fwd.rdm.uicomponents;

import javafx.scene.control.TextField;
import org.springframework.util.StringUtils;

/**
 * @Author: fanwd
 * @Description: 数字输入框
 * @Date: Create in 14:24 2018/11/23
 */
public class IntegerTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (StringUtils.isEmpty(text)) {
            super.replaceText(start, end, text);
        } else if (text.matches("[0-9]{1,}")) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
