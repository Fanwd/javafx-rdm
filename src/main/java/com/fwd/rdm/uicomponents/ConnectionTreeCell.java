package com.fwd.rdm.uicomponents;

import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.enums.ItemTypeEnum;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:37 2018/11/13
 */
public class ConnectionTreeCell extends TreeCell<ConnectionTreeCell.TreeItemData> {

    /**
     * 节点内容文本
     */
    private Label textLabel = new Label();
    /**
     * 上移按钮
     */
    private FontAwesomeIconView upIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);
    private Label upLabel = new Label(null, upIcon);
    /**
     * 下移按钮
     */
    private FontAwesomeIconView downIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_DOWN);
    private Label downLabel = new Label(null, downIcon);
    /**
     * 添加按钮
     */
    private FontAwesomeIconView addIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_CIRCLE);
    private Label addLabel = new Label(null, addIcon);
    /**
     * 刷新按钮
     */
    private FontAwesomeIconView refreshIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
    private Label refreshLabel = new Label(null, refreshIcon);
    /**
     * 删除按钮
     */
    private FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
    private Label deleteLabel = new Label(null, deleteIcon);

    /**
     * 操作按钮box
     */
    private HBox graphicBox = new HBox(upLabel, downLabel, addLabel, refreshLabel, deleteLabel);
    private StackPane pane = new StackPane(textLabel, graphicBox);

    private EventHandler<? super MouseEvent> upEvent;
    private EventHandler<? super MouseEvent> downEvent;
    private EventHandler<? super MouseEvent> addEvent;
    private EventHandler<? super MouseEvent> refreshEvent;
    private EventHandler<? super MouseEvent> deleteEvent;

    public ConnectionTreeCell() {
        // 设置操作图标靠右
        textLabel.setTextOverrun(OverrunStyle.CLIP);
        graphicBox.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setAlignment(textLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(graphicBox, Pos.CENTER_RIGHT);

        upLabel.setOnMouseClicked(event -> {
            // 上移按钮点击事件
            if (null != upEvent) {
                upEvent.handle(event);
                event.consume();
            }
        });
        downLabel.setOnMouseClicked(event -> {
            // 下移按钮点击事件
            if (null != downEvent) {
                downEvent.handle(event);
                event.consume();
            }
        });
        addLabel.setOnMouseClicked(event -> {
            // 新增按钮点击事件
            if (null != addEvent) {
                addEvent.handle(event);
                event.consume();
            }
        });
        refreshLabel.setOnMouseClicked(event -> {
            // 刷新按钮点击事件
            if (null != refreshEvent) {
                refreshEvent.handle(event);
                event.consume();
            }
        });
        deleteLabel.setOnMouseClicked(event -> {
            // 删除按钮点击事件
            if (deleteEvent != null) {
                deleteEvent.handle(event);
                event.consume();
            }
        });

        // 隐藏操作图标
        upLabel.setVisible(false);
        downLabel.setVisible(false);
        addLabel.setVisible(false);
        refreshLabel.setVisible(false);
        deleteLabel.setVisible(false);
        // 选中时显示图标
        selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && !this.getItem().isRemove()) {
                TreeItemData item = this.getItem();

                // 新增按钮显隐控制
                if (ItemTypeEnum.DATABASE.equals(item.itemTypeEnum)) {
                    addLabel.setVisible(true);
                }

                // 刷新按钮显隐控制
                boolean showRefreshLabel = ItemTypeEnum.SERVER.equals(item.itemTypeEnum)
                        || ItemTypeEnum.DATABASE.equals(item.itemTypeEnum)
                        || (ItemTypeEnum.KEY.equals(item.itemTypeEnum) && !item.isLeaf());
                if (showRefreshLabel) {
                    refreshLabel.setVisible(true);
                }

                // 删除按钮显隐控制
                if (ItemTypeEnum.SERVER.equals(item.itemTypeEnum)
                        || ItemTypeEnum.KEY.equals(item.itemTypeEnum)) {
                    deleteLabel.setVisible(true);
                }

                // 上移下移按钮
                if (ItemTypeEnum.SERVER.equals(item.itemTypeEnum) && !this.getItem().isTop()) {
                    upLabel.setVisible(true);
                }
                if (ItemTypeEnum.SERVER.equals(item.itemTypeEnum) && !this.getItem().isBottom()) {
                    downLabel.setVisible(true);
                }
            } else {
                upLabel.setVisible(false);
                downLabel.setVisible(false);
                addLabel.setVisible(false);
                refreshLabel.setVisible(false);
                deleteLabel.setVisible(false);
            }
        });

    }

    /**
     * 上移操作
     *
     * @param event
     */
    public void onMoveUpAction(EventHandler<? super MouseEvent> event) {
        this.upEvent = event;
    }

    /**
     * 下移操作
     *
     * @param event
     */
    public void onMoveDownAction(EventHandler<? super MouseEvent> event) {
        this.downEvent = event;
    }

    /**
     * 添加操作
     */
    public void onAddAction(EventHandler<? super MouseEvent> event) {
        this.addEvent = event;
    }

    /**
     * 刷新操作
     */
    public void onRefreshAction(EventHandler<? super MouseEvent> event) {
        this.refreshEvent = event;
    }

    /**
     * 删除操作
     */
    public void onDeleteAction(EventHandler<? super MouseEvent> event) {
        this.deleteEvent = event;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
    }

    @Override
    protected void updateItem(TreeItemData item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            textLabel.setText(null);
            textLabel.setGraphic(null);
        } else {
            setText(null);
            setGraphic(pane);
            if (item.isRemove()) {
                textLabel.setText(item.getConnectionPropertiesObjectProperty().getName() + " (Removed)");
                textLabel.setTextFill(Color.GRAY);
                textLabel.setGraphic(getTreeItem().getGraphic());
            } else {
                textLabel.setTextFill(Color.BLACK);
                textLabel.setText(item.getConnectionPropertiesObjectProperty().getName());
                textLabel.setGraphic(getTreeItem().getGraphic());
            }
        }
    }

    public static class TreeData extends TreeItemData {
    }

    public static class TreeItemData {

        private LongProperty id = new SimpleLongProperty();
        private StringProperty key = new SimpleStringProperty();
        private StringProperty parentKey = new SimpleStringProperty();
        private StringProperty text = new SimpleStringProperty();
        private ItemTypeEnum itemTypeEnum;
        /**
         * 连接信息
         */
        private ObjectProperty<ConnectionProperties> connectionPropertiesObjectProperty = new SimpleObjectProperty<>();
        /**
         * 叶子节点
         */
        private BooleanProperty leaf = new SimpleBooleanProperty();
        /**
         * 删除标记
         */
        private BooleanProperty remove = new SimpleBooleanProperty(false);
        /**
         * 是否是第一个节点
         */
        private BooleanProperty top = new SimpleBooleanProperty(false);
        /**
         * 是否是最后一个节点
         */
        private BooleanProperty bottom = new SimpleBooleanProperty(false);

        public TreeItemData() {
        }

        public TreeItemData(ConnectionProperties connectionPropertiesObjectProperty, String text, boolean leaf, ItemTypeEnum itemTypeEnum) {
            this.connectionPropertiesObjectProperty.set(connectionPropertiesObjectProperty);
            this.id.set(connectionPropertiesObjectProperty.getId());
            this.text.set(text);
            this.leaf.set(leaf);
            this.itemTypeEnum = itemTypeEnum;
        }

        public TreeItemData(TreeItemData treeItemData) {
            this.id.set(treeItemData.getId());
            this.key.set(treeItemData.getKey());
            this.parentKey.set(treeItemData.getParentKey());
            this.text.set(treeItemData.getText());
            this.itemTypeEnum = treeItemData.getItemTypeEnum();
            this.connectionPropertiesObjectProperty.set(treeItemData.getConnectionPropertiesObjectProperty());
            this.leaf.set(treeItemData.isLeaf());
            this.remove.set(treeItemData.isRemove());
            this.top.set(treeItemData.isTop());
            this.bottom.set(treeItemData.isBottom());
        }

        public long getId() {
            return id.get();
        }

        public LongProperty idProperty() {
            return id;
        }

        public void setId(long id) {
            this.id.set(id);
        }

        public String getKey() {
            return key.get();
        }

        public StringProperty keyProperty() {
            return key;
        }

        public void setKey(String key) {
            this.key.set(key);
        }

        public String getParentKey() {
            return parentKey.get();
        }

        public StringProperty parentKeyProperty() {
            return parentKey;
        }

        public void setParentKey(String parentKey) {
            this.parentKey.set(parentKey);
        }

        public String getText() {
            return text.get();
        }

        public StringProperty textProperty() {
            return text;
        }

        public void setText(String text) {
            this.text.set(text);
        }

        public boolean isLeaf() {
            return leaf.get();
        }

        public BooleanProperty leafProperty() {
            return leaf;
        }

        public void setLeaf(boolean leaf) {
            this.leaf.set(leaf);
        }

        public ConnectionProperties getConnectionPropertiesObjectProperty() {
            return connectionPropertiesObjectProperty.get();
        }

        public ObjectProperty<ConnectionProperties> connectionPropertiesObjectPropertyProperty() {
            return connectionPropertiesObjectProperty;
        }

        public void setConnectionPropertiesObjectProperty(ConnectionProperties connectionPropertiesObjectProperty) {
            this.connectionPropertiesObjectProperty.set(connectionPropertiesObjectProperty);
        }

        public ItemTypeEnum getItemTypeEnum() {
            return itemTypeEnum;
        }

        public void setItemTypeEnum(ItemTypeEnum itemTypeEnum) {
            this.itemTypeEnum = itemTypeEnum;
        }

        public boolean isRemove() {
            return remove.get();
        }

        public BooleanProperty removeProperty() {
            return remove;
        }

        public void setRemove(boolean remove) {
            this.remove.set(remove);
        }

        public boolean isTop() {
            return top.get();
        }

        public BooleanProperty topProperty() {
            return top;
        }

        public void setTop(boolean top) {
            this.top.set(top);
        }

        public boolean isBottom() {
            return bottom.get();
        }

        public BooleanProperty bottomProperty() {
            return bottom;
        }

        public void setBottom(boolean bottom) {
            this.bottom.set(bottom);
        }
    }
}