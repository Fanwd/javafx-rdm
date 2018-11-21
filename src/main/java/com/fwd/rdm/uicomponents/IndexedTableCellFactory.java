package com.fwd.rdm.uicomponents;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:13 2018/11/21
 */
public class IndexedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new IndexedTableCell<>();
    }
}
