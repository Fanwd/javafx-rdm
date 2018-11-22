package com.fwd.rdm.uicomponents;

import javafx.scene.control.TextField;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 17:04 2018/11/22
 */
public class DoubleTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        String oldText = this.getText();
        int oldLen = oldText.length();
        String newStr = "";
        if (oldLen > 0) {
            if (start > 0) {
                if (end < oldLen) {
                    newStr = oldText.substring(0, start) + text + oldText.substring(end, oldLen);
                } else {
                    newStr = oldText.substring(0, start) + text;
                }
            } else {
                if (oldLen > end) {
                    newStr = text + oldText.substring(end);
                } else {
                    newStr = text;
                }
            }
        } else {
            newStr = text;
        }
        if (newStr.matches("[0-9]{1,}\\.{0,1}[0-9]{0,}")) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
