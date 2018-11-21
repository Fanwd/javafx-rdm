package com.fwd.rdm.uicomponents;

import javafx.scene.control.TableCell;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:11 2018/11/21
 */
public class IndexedTableCell<S, T> extends TableCell<S, T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            this.setText(null);
        } else {
            this.setText(String.valueOf(this.getIndex()));
        }
    }

    //    @Override
//    protected void updateItem(T item, boolean empty) {
//        super.updateItem(item, empty);
//        if (empty) {
//            this.setText(null);
//        } else {
//            this.setText(String.valueOf(this.getIndex()));
//        }
//    }
}
