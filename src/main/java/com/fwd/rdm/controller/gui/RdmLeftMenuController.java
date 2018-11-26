package com.fwd.rdm.controller.gui;

import com.fwd.rdm.data.RdmObservableData;
import com.fwd.rdm.data.domain.ConnectionProperties;
import com.fwd.rdm.enums.ItemTypeEnum;
import com.fwd.rdm.service.RedisService;
import com.fwd.rdm.uicomponents.ConnectionTreeCell;
import com.fwd.rdm.utils.ConnectionXmlUtils;
import com.fwd.rdm.utils.ListUtils;
import com.fwd.rdm.utils.LoggerUtils;
import com.fwd.rdm.views.submain.RdmAddView;
import de.felixroske.jfxsupport.FXMLController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:04 2018/11/13
 */
@FXMLController
public class RdmLeftMenuController {

    @FXML
    private VBox leftBox;
    @FXML
    private Separator leftLine;
    @FXML
    private TreeView<ConnectionTreeCell.TreeItemData> conTree;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RdmAddView rdmAddView;

    @Autowired
    private RdmCenterController rdmCenterController;

    @Autowired
    private RdmObservableData rdmObservableData;

    @Autowired
    private LoggerUtils loggerUtils;

    /**
     * 系统初始化
     */
    @FXML
    public void initialize() {

        // 左侧窗口拖拽大小
        leftLine.setCursor(Cursor.W_RESIZE);
        leftLine.setOnMouseDragged(event -> {
            double newPrefWidth = event.getX() + leftBox.getPrefWidth();
            double minWidth = leftBox.getMinWidth();
            if (newPrefWidth < minWidth) {
                newPrefWidth = minWidth;
            }
            leftBox.setPrefWidth(newPrefWidth);
        });

        // 初始化根节点
        ConnectionProperties rootProperties = new ConnectionProperties(-1, "服务器连接列表", "", 0, "", 0, 0);
        ConnectionTreeCell.TreeItemData rootItemData = new ConnectionTreeCell.TreeItemData(rootProperties, rootProperties.getName(), false, ItemTypeEnum.ROOT);
        TreeItem<ConnectionTreeCell.TreeItemData> rootItem = new TreeItem<>(rootItemData);
        conTree.setRoot(rootItem);

        // 创建点击事件
        conTree.setCellFactory(view -> {
            ConnectionTreeCell cell = new ConnectionTreeCell();
            cell.prefWidthProperty().bind(conTree.widthProperty().add(cell.getFont().getSize() * -1.8));
            // 点击添加按钮
            cell.onAddAction(event -> {
                rdmAddView.show(cell.getTreeItem());
            });
            // 点击刷新按钮
            cell.onRefreshAction(event -> {
                try {
                    this.refreshCell(cell.getTreeItem());
                } catch (RuntimeException re) {
                    re.printStackTrace();
                    loggerUtils.error(re.toString());
                }
            });
            // 点击删除按钮
            cell.onDeleteAction(event -> {
                ConnectionTreeCell.TreeItemData itemData = cell.getItem();
                TreeItem<ConnectionTreeCell.TreeItemData> treeItem = cell.getTreeItem();
                if (ItemTypeEnum.KEY.equals(itemData.getItemTypeEnum())) {
                    if (itemData.isLeaf()) {
                        redisService.deleteKey(itemData.getConnectionPropertiesObjectProperty(), itemData.getKey());
                    } else {
                        redisService.delete(itemData.getConnectionPropertiesObjectProperty(), itemData.getKey() + "*");
                        itemData.getConnectionPropertiesObjectProperty().setName(itemData.getText() + " (0)");
                    }
                    treeItem.getChildren().clear();
                    itemData.setRemove(true);
                    ConnectionTreeCell.TreeItemData data = new ConnectionTreeCell.TreeItemData(itemData);
                    treeItem.setValue(data);
                } else if (ItemTypeEnum.SERVER.equals(itemData.getItemTypeEnum())) {
                    rdmObservableData.removeConnection(itemData.getConnectionPropertiesObjectProperty());
                }
            });
            // 点击事件
            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    if (MouseButton.PRIMARY.equals(event.getButton())) {
                        treeItemAction(cell.getTreeItem());
                    }
                } catch (RuntimeException re) {
                    re.printStackTrace();
                    loggerUtils.error(re.toString());
                }
            });
            return cell;
        });


        // 动态添加服务器信息
        rdmObservableData.getConnectionList().addListener((ListChangeListener<ConnectionProperties>) c -> {
            if (c.next()) {
                List<? extends ConnectionProperties> subList = c.getAddedSubList();
                for (ConnectionProperties connectionProperties : subList) {
                    // 创建连接节点
                    FontAwesomeIconView serverItemIcon = new FontAwesomeIconView(FontAwesomeIcon.SERVER);
                    ConnectionTreeCell.TreeItemData serverItemData = new ConnectionTreeCell.TreeItemData(connectionProperties, connectionProperties.getName(), false, ItemTypeEnum.SERVER);
                    TreeItem<ConnectionTreeCell.TreeItemData> serverItem = new TreeItem<>(serverItemData, serverItemIcon);
                    rootItem.getChildren().add(serverItem);
                }
                List<? extends ConnectionProperties> removedList = c.getRemoved();
                for (ConnectionProperties connectionProperties : removedList) {
                    // 删除连接节点
                    ObservableList<TreeItem<ConnectionTreeCell.TreeItemData>> children = rootItem.getChildren();
                    Iterator<TreeItem<ConnectionTreeCell.TreeItemData>> iterator = children.iterator();
                    while (iterator.hasNext()) {
                        TreeItem<ConnectionTreeCell.TreeItemData> next = iterator.next();
                        ConnectionTreeCell.TreeItemData itemData = next.getValue();
                        ConnectionProperties childProperties = itemData.getConnectionPropertiesObjectProperty();
                        if (childProperties.getId() == connectionProperties.getId()) {
                            iterator.remove();
                        }
                    }
                }
            }
        });

        // 初始化服务器列表信息
        List<ConnectionProperties> initConList = ConnectionXmlUtils.load();
        if (null != initConList && !initConList.isEmpty()) {
            rdmObservableData.getConnectionList().addAll(initConList);
        }

        // 将连接信息保存到xml文件
        rdmObservableData.getConnectionList().addListener((ListChangeListener<ConnectionProperties>) c -> {
            ObservableList<ConnectionProperties> connectionList = rdmObservableData.getConnectionList();
            try {
                ConnectionXmlUtils.save(connectionList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 刷新树节点
     *
     * @param treeItem
     */
    public void refreshCell(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        ConnectionTreeCell.TreeItemData itemData = treeItem.getValue();
        if (ItemTypeEnum.SERVER.equals(itemData.getItemTypeEnum())
                || ItemTypeEnum.DATABASE.equals(itemData.getItemTypeEnum())
                || (ItemTypeEnum.KEY.equals(itemData.getItemTypeEnum()) && !itemData.isLeaf())) {
            // 刷新前记录展开状态
            Map<String, Boolean> keyExpandMap = new HashMap<>();
            this.recordExpandStatus(keyExpandMap, treeItem);
            // 刷新数据
            treeItem.getChildren().clear();
            this.treeItemAction(treeItem);
            // 恢复展开状态
            this.recoveryExpandStatus(keyExpandMap, treeItem);
        }
    }

    /**
     * 记录子节点展开状态
     */
    private void recordExpandStatus(Map<String, Boolean> keyExpandMap, TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        ObservableList<TreeItem<ConnectionTreeCell.TreeItemData>> children = treeItem.getChildren();
        if (null != children && !children.isEmpty()) {
            for (TreeItem<ConnectionTreeCell.TreeItemData> child : children) {
                ConnectionTreeCell.TreeItemData itemData = child.getValue();
                if (!itemData.isLeaf()) {
                    keyExpandMap.put(itemData.getKey(), child.isExpanded());
                    this.recordExpandStatus(keyExpandMap, child);
                }
            }
        }
    }

    /**
     * 恢复展开状态
     */
    private void recoveryExpandStatus(Map<String, Boolean> keyExpandMap, TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        ObservableList<TreeItem<ConnectionTreeCell.TreeItemData>> children = treeItem.getChildren();
        if (null != children && !children.isEmpty()) {
            for (TreeItem<ConnectionTreeCell.TreeItemData> child : children) {
                ConnectionTreeCell.TreeItemData itemData = child.getValue();
                if (!itemData.isLeaf()) {
                    if (keyExpandMap.containsKey(itemData.getKey())) {
                        child.setExpanded(keyExpandMap.get(itemData.getKey()));
                    }
                    this.recoveryExpandStatus(keyExpandMap, child);
                }
            }
        }
    }

    /**
     * treeItem事件处理
     */
    private void treeItemAction(TreeItem<ConnectionTreeCell.TreeItemData> treeItem) {
        if (null == treeItem) {
            return;
        }
        // 获取当前点击节点
        TreeItem<ConnectionTreeCell.TreeItemData> selectedItem = treeItem;
        // 获取当前节点数据
        ConnectionTreeCell.TreeItemData item = treeItem.getValue();
        ConnectionProperties parentProperties = item.getConnectionPropertiesObjectProperty();
        if (item.isRemove()) {
            // 已删除节点不处理点击事件
            return;
        }
        if (item != null && ItemTypeEnum.ROOT.equals(item.getItemTypeEnum())) {
            // 点击根节点，不需要处理
        }
        if (item != null && ItemTypeEnum.SERVER.equals(item.getItemTypeEnum())) {
            // 点击服务器节点，展示数据库列表
            if (selectedItem.getChildren().isEmpty()) {
                // 子节点为空，重新获取数据库列表
                Map<Integer, Integer> databaseMap = this.redisService.getDatabase(parentProperties);
                databaseMap.forEach((key, value) -> {
                    ConnectionProperties properties = new ConnectionProperties(parentProperties);
                    properties.setDbIndex(key);
                    properties.setKeyCount(value);
                    properties.setName("db" + key + " (" + value + ")");
                    FontAwesomeIconView databaseItemIcon = new FontAwesomeIconView(FontAwesomeIcon.DATABASE);
                    ConnectionTreeCell.TreeItemData databaseItemData = new ConnectionTreeCell.TreeItemData(properties, "db", false, ItemTypeEnum.DATABASE);
                    TreeItem<ConnectionTreeCell.TreeItemData> databaseItem = new TreeItem<>(databaseItemData, databaseItemIcon);
                    selectedItem.getChildren().add(databaseItem);
                });
                // 默认展开子节点
                selectedItem.setExpanded(true);
            }
        }
        if (item != null && ItemTypeEnum.DATABASE.equals(item.getItemTypeEnum())) {
            // 点击数据库节点，查询所有key
            if (selectedItem.getChildren().isEmpty()) {
                // 如果当前节点的子为空，重新获取key列表
                List<String> keyList = this.redisService.getKeys(parentProperties, "*");
                if (keyList.size() != parentProperties.getKeyCount()) {
                    // key个数变更，更新key值
                    int dbIndex = parentProperties.getDbIndex();
                    parentProperties.setKeyCount(keyList.size());
                    parentProperties.setName("db" + dbIndex + " (" + keyList.size() + ")");
                    item.setConnectionPropertiesObjectProperty(parentProperties);
                    selectedItem.setValue(item);
                }
                if (!keyList.isEmpty()) {
                    Map<String, Map<String, List<ConnectionTreeCell.TreeData>>> treeMap = this.buildTreeData(keyList);
                    Map<String, List<ConnectionTreeCell.TreeData>> directoryMap = treeMap.get("directory");
                    Map<String, List<ConnectionTreeCell.TreeData>> leafMap = treeMap.get("leaf");

                    // 添加数据节点
                    this.createTreeItem(keyList, directoryMap, leafMap, "", parentProperties, selectedItem);
                }
                // 默认展开子节点
                selectedItem.setExpanded(true);
            }
        }
        if (item != null && ItemTypeEnum.KEY.equals(item.getItemTypeEnum())) {
            // 点击关键字节点，判断是否是叶子节点
            if (item.isLeaf()) {
                // 叶子节点，展示数据
                rdmCenterController.initData(item.getConnectionPropertiesObjectProperty(), item.getKey());
            } else {
                // 目录节点，刷新数据
                if (selectedItem.getChildren().isEmpty()) {
                    String dicKey = item.getKey();
                    List<String> keyList = this.redisService.getKeys(parentProperties, dicKey + "*");

                    if (keyList.size() != parentProperties.getKeyCount()) {
                        // key个数变更，更新key值
                        parentProperties.setKeyCount(keyList.size());
                        parentProperties.setName(item.getText() + " (" + keyList.size() + ")");
                        item.setConnectionPropertiesObjectProperty(parentProperties);
                        treeItem.setValue(item);
                    }
                    if (!keyList.isEmpty()) {
                        Map<String, Map<String, List<ConnectionTreeCell.TreeData>>> treeMap = this.buildTreeData(keyList);
                        Map<String, List<ConnectionTreeCell.TreeData>> directoryMap = treeMap.get("directory");
                        Map<String, List<ConnectionTreeCell.TreeData>> leafMap = treeMap.get("leaf");

                        // 添加数据节点
                        this.createTreeItem(keyList, directoryMap, leafMap, dicKey, parentProperties, selectedItem);
                    }
                    // 默认展开子节点
                    selectedItem.setExpanded(true);
                }
            }
        }
    }

    /**
     * 创建树
     */
    private void createTreeItem(List<String> allKeyList,
                                Map<String, List<ConnectionTreeCell.TreeData>> directoryMap,
                                Map<String, List<ConnectionTreeCell.TreeData>> leafMap,
                                String parent,
                                ConnectionProperties parentProperties,
                                TreeItem<ConnectionTreeCell.TreeItemData> parentTreeItem) {
        List<ConnectionTreeCell.TreeData> directoryList = directoryMap.get(parent);
        List<ConnectionTreeCell.TreeData> leafList = leafMap.get(parent);
        if (directoryList != null && !directoryList.isEmpty()) {
            for (ConnectionTreeCell.TreeData directoryData : directoryList) {
                // 计算叶子节点个数
                String dicName = directoryData.getKey();
                int leafCount = ((int) allKeyList.stream().filter(key -> key.startsWith(dicName)).count());
                // 创建节点
                ConnectionProperties currentItemProperties = new ConnectionProperties(parentProperties);
                currentItemProperties.setName(directoryData.getText() + " (" + leafCount + ")");
                currentItemProperties.setKeyCount(leafCount);
                FontAwesomeIconView directoryIconView = new FontAwesomeIconView(FontAwesomeIcon.FOLDER);
                ConnectionTreeCell.TreeItemData directoryItemData = new ConnectionTreeCell.TreeItemData(currentItemProperties, directoryData.getText(), false, ItemTypeEnum.KEY);
                directoryItemData.setKey(directoryData.getKey());
                directoryItemData.setParentKey(directoryData.getParentKey());
                TreeItem<ConnectionTreeCell.TreeItemData> directoryTreeItem = new TreeItem<>(directoryItemData, directoryIconView);
                // 添加子节点
                parentTreeItem.getChildren().add(directoryTreeItem);
                this.createTreeItem(allKeyList, directoryMap, leafMap, dicName,
                        currentItemProperties, directoryTreeItem);
            }
        }
        if (leafList != null && !leafList.isEmpty()) {
            for (ConnectionTreeCell.TreeData leafData : leafList) {
                // 创建节点
                ConnectionProperties currentItemProperties = new ConnectionProperties(parentProperties);
                currentItemProperties.setName(leafData.getText());
                currentItemProperties.setKeyCount(0);
                FontAwesomeIconView leafIconView = new FontAwesomeIconView(FontAwesomeIcon.KEY);
                ConnectionTreeCell.TreeItemData leafItemData = new ConnectionTreeCell.TreeItemData(currentItemProperties, leafData.getText(), true, ItemTypeEnum.KEY);
                leafItemData.setKey(leafData.getKey());
                leafItemData.setParentKey(leafData.getParentKey());
                TreeItem<ConnectionTreeCell.TreeItemData> leafTreeItem = new TreeItem<>(leafItemData, leafIconView);
                // 添加子节点
                parentTreeItem.getChildren().add(leafTreeItem);
            }
        }
    }

    /**
     * 构建tree结构
     */
    private Map<String, Map<String, List<ConnectionTreeCell.TreeData>>> buildTreeData(List<String> keyList) {
        Map<String, Map<String, List<ConnectionTreeCell.TreeData>>> result = new HashMap<>();
        // 目录tree列表
        List<ConnectionTreeCell.TreeData> directoryTreeList = new ArrayList<>();
        // 叶子节点tree列表
        List<ConnectionTreeCell.TreeData> leafTreeList = new ArrayList<>();
        keyList.forEach(key -> {
            // 解决结尾是':'情况
            String[] split = (key + "@").split(":");
            if (split.length == 1) {
                ConnectionTreeCell.TreeData treeData = new ConnectionTreeCell.TreeData();
                treeData.setText(key);
                treeData.setKey(key);
                treeData.setParentKey("");
                treeData.setItemTypeEnum(ItemTypeEnum.ROOT);
                treeData.setLeaf(true);
                leafTreeList.add(treeData);
            } else {
                String leafParent = "";
                for (int i = 0; i < split.length - 1; i++) {
                    String name = split[i];
                    String preKey = leafParent;
                    leafParent += name + ":";
                    ConnectionTreeCell.TreeData treeData = new ConnectionTreeCell.TreeData();
                    treeData.setText(name);
                    treeData.setKey(leafParent);
                    treeData.setParentKey(preKey);
                    if (i == 0) {
                        treeData.setItemTypeEnum(ItemTypeEnum.ROOT);
                    } else {
                        treeData.setItemTypeEnum(ItemTypeEnum.KEY);
                    }
                    treeData.setLeaf(false);
                    // 判断key是否以添加过
                    if (directoryTreeList.stream().noneMatch(item -> treeData.getKey().equals(item.getKey()))) {
                        directoryTreeList.add(treeData);
                    }
                }
                ConnectionTreeCell.TreeData treeData = new ConnectionTreeCell.TreeData();
                treeData.setText(key);
                treeData.setKey(key);
                treeData.setParentKey(leafParent);
                treeData.setItemTypeEnum(ItemTypeEnum.KEY);
                treeData.setLeaf(true);
                leafTreeList.add(treeData);
            }
        });
        Map<String, List<ConnectionTreeCell.TreeData>> directoryMap = ListUtils.listToMapList(directoryTreeList, ConnectionTreeCell.TreeData::getParentKey);
        Map<String, List<ConnectionTreeCell.TreeData>> leafMap = ListUtils.listToMapList(leafTreeList, ConnectionTreeCell.TreeData::getParentKey);
        result.put("directory", directoryMap);
        result.put("leaf", leafMap);
        return result;
    }
}
