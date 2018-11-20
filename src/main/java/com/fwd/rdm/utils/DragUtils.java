package com.fwd.rdm.utils;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:23 2018/11/20
 */
public class DragUtils {

    /**
     * 垂直拖拽
     */
    public static void vResizeDrag(Region topPane, Separator dragLine, Region bottomPane) {
        dragLine.setCursor(Cursor.V_RESIZE);
        double topMinHeight = topPane.getMinHeight() == -1 ? 0 : topPane.getMinHeight();
        double topMaxHeight = topPane.getMaxHeight() == -1 ? Double.MAX_VALUE : topPane.getMaxHeight();
        double bottomMinHeight = bottomPane.getMinHeight() == -1 ? 0 : bottomPane.getMinHeight();
        double bottomMaxHeight = bottomPane.getMaxHeight() == -1 ? Double.MAX_VALUE : bottomPane.getMaxHeight();
        dragLine.setOnMouseDragged(event -> {
            double movedY = event.getY();
            double newTopHeight = topPane.getHeight() + movedY;
            double newBottomHeight = bottomPane.getHeight() - movedY;
            if (newTopHeight < topMinHeight || newTopHeight > topMaxHeight) {
                return;
            }
            if (newBottomHeight < bottomMinHeight || newBottomHeight > bottomMaxHeight) {
                return;
            }
            topPane.setPrefHeight(newTopHeight);
            bottomPane.setPrefHeight(newBottomHeight);
        });
    }

    public static void hResizeDrag(Node leftNode, Separator dragLine, Node rightNode) {

    }
}
